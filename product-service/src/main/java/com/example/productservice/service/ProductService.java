package com.asule.productservice.service;

import com.asule.productservice.model.ProductCreateRequest;
import com.asule.productservice.model.ProductCreateResponse;

import java.util.List;

public interface ProductService {
    ProductCreateResponse createProduct(ProductCreateRequest productCreateRequest);

    List<ProductCreateResponse> findAll();


    ProductCreateResponse findById(Integer productId);
}
