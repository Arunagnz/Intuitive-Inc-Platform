package com.intuitveinc.product_service.controller;

import com.intuitveinc.common.request.LoginRequest;
import com.intuitveinc.common.response.AuthResponse;
import com.intuitveinc.product_service.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private PartnerService partnerService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = partnerService.generateToken(loginRequest.getAccessKey(), loginRequest.getSecretKey());
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(authResponse);
    }
}
