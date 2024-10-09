package com.intuitveinc.common.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginRequest {
    private String accessKey;
    private String secretKey;
}