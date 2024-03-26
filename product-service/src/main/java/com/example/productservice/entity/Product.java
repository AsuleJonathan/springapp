package com.asule.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbl_products",uniqueConstraints = {
    @UniqueConstraint(columnNames = "product_code",name = "UNIQ_PRODUCT_CODE")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_name")
    private String name;

    @Column(name= "product_price")
    private BigDecimal price;

    @Column(name= "product_code")
    private String productCode;
}
