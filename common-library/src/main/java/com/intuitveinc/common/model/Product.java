package com.intuitveinc.common.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Product details")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the product", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull(message = "Name is required")
    @Schema(description = "Name of the product", example = "Product 1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description of the product", example = "Product 1 description", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotBlank(message = "SKU is required")
    @Schema(description = "Stock keeping unit of the product", example = "SKU-1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sku;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(description = "Base price of the product", example = "100.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double basePrice;

    @NotBlank(message = "Category is required")
    @Schema(description = "Category of the product", example = "Category 1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @NotNull(message = "Partner is required")
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    @Schema(description = "Partner details", requiredMode = Schema.RequiredMode.REQUIRED)
    private Partner partner;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Schema(description = "Pricing details", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Pricing> pricing;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sku='" + sku + '\'' +
                ", basePrice=" + basePrice +
                ", category='" + category + '\'' +
                ", partner=" + partner.getId() +
                ", pricing=" + Arrays.toString(getPricingIds(pricing)) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    private double[] getPricingIds(List<Pricing> pricingList) {
        double[] pricingIds = new double[pricingList.size()];
        for(int i = 0; i < pricingList.size();i++)
            pricingIds[i] = pricingList.get(i).getId();
        return pricingIds;
    }
}
