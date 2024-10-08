package com.intuitveinc.common.model;

import com.intuitveinc.common.validator.EitherOr;
import com.intuitveinc.common.validator.ValidDateRange;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ValidDateRange
@EitherOr
public class Promotion {

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

    @NotNull(message = "Description is required")
    @Size(min = 10, max = 255, message = "Description must be between 10 and 255 characters")
    private String description;

    private Double percentage;
    private Double flatRate;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", partner=" + partner.getId() +
                ", product=" + product.getId() +
                ", description='" + description + '\'' +
                ", percentage=" + percentage +
                ", flatRate=" + flatRate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
