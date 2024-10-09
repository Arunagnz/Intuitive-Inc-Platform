package com.intuitveinc.product_service.service;

@Service
public interface PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private BcryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${security.jwt.secret}")
    private final String jwtSecret;

    @Value("${security.jwt.expiration}")
    private final int jwtExpiration;

    public String generateToken(String accessKey, String secretKey) {
        Partner partner = partnerRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new PartnerNotFoundException("Partner not found with access key: " + accessKey));

        if (!bCryptPasswordEncoder.matches(secretKey, partner.getHashedSecretKey())) {
            throw new PartnerAuthenticationException("Invalid secret key for partner with access key: " + accessKey);
        }

        return Jwts.builder()
                .setSubject(partner.getName())
                .claim("PartnerId", partner.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Partner getPartnerFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Long partnerId = claims.get("PartnerId", Long.class);
        return partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerNotFoundException("Partner not found with ID: " + partnerId));
    }
}