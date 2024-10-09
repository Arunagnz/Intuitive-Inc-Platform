package com.intuitveinc.product_service.service;

public interface IPartnerService {
    String generateToken(String accessKey, String secretKey);
    boolean validateToken(String token);
    Partner getPartnerFromToken(String token);
}