package com.intuitveinc.common.model;

import com.intuitveinc.common.validator.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@ValidDateRange
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Partner is required")
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @NotNull(message = "Product is required")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Customer is required")
    private int customerId;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Sold price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double soldPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
