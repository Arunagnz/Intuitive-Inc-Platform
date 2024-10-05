package com.intuitveinc.common.model;

import jakarta.persistence.*;
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

    private String name;
    private String description;
    private String sku;
    private Double basePrice;
    private String category;
    private Long partnerId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Pricing> pricing;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
