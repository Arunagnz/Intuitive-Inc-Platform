package com.intuitveinc.product_service.controller;


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