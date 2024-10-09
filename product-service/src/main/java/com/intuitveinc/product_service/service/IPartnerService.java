package com.intuitveinc.product_service.service;

import com.intuitveinc.common.model.Partner;

public interface IPartnerService {
    String generateToken(String accessKey, String secretKey);
    boolean validateToken(String token);
    Partner getPartnerFromToken(String token);
}