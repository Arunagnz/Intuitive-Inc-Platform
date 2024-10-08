package com.intuitveinc.common.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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
    private List<Pricing> pricing;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
