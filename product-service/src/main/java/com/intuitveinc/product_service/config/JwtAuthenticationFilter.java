package com.intuitveinc.product_service.config;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private PartnerService partnerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null && partnerService.validateToken(token)) {
            Long partnerId = partnerService.getPartnerIdFromToken(token);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(partnerId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HTTPServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}