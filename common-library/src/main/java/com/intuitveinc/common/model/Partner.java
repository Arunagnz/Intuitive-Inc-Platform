package com.intuitveinc.common.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email address")
    private String email;

    @Size(min = 8, max = 16, message = "Access key must be between 8 and 16 characters")
    @JsonIgnore
    private String accessKey;

    @JsonIgnore
    private String hashedSecretKey;

    @NotBlank(message = "Address is required")
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
