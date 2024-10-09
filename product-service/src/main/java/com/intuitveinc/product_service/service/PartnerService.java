package com.intuitveinc.product_service.service;

import com.intuitveinc.common.exception.PartnerAuthenticationException;
import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.Partner;
import com.intuitveinc.common.repository.PartnerRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class PartnerService implements IPartnerService {

    public static final String PARTNER_ID_CLAIM_KEY = "PartnerId";

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.expiration}")
    private int jwtExpiration;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String generateToken(String accessKey, String secretKey) {
        Partner partner = partnerRepository.findByAccessKey(accessKey)
                .orElseThrow(() -> new PartnerNotFoundException("Partner not found with access key: " + accessKey));

        if (!passwordEncoder.matches(secretKey, partner.getHashedSecretKey())) {
            throw new PartnerAuthenticationException("Invalid secret key for partner with access key: " + accessKey);
        }

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(partner.getName())
                .claim(PARTNER_ID_CLAIM_KEY, partner.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Partner getPartnerFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long partnerId = claims.get(PARTNER_ID_CLAIM_KEY, Long.class);
        return partnerRepository.findById(partnerId)
                .orElseThrow(() -> new PartnerNotFoundException("Partner not found with ID: " + partnerId));
    }
}
