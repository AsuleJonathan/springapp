package com.asule.inventory.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCreateDto {

    private String productCode;

    private Integer quantity;
}
