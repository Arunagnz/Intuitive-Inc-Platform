package com.intuitveinc.common.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Base price is required")
    private Double basePrice;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Partner is required")
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
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
