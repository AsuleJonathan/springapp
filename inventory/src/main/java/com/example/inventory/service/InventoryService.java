package com.asule.inventory.service;

import java.util.List;

import com.asule.inventory.model.InventoryCreateDto;
import com.asule.inventory.model.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryCreateDto inventoryCreateDto);

    Boolean checkInventory(List<String> productCodes, List<Integer> productQuantities);

}
