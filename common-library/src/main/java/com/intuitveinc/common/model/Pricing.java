package com.intuitveinc.common.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double basePrice;

    @NotNull(message = "Discount is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100.0", inclusive = false)
    private Double discount;

    @NotNull(message = "Pricing strategy is required")
    @Enumerated(EnumType.STRING)
    private PricingStrategy pricingStrategy;

    @NotNull(message = "Partner is required")
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @NotNull(message = "Product is required")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
