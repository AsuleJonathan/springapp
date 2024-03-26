package com.asule.orderservice.service;

import com.asule.orderservice.model.OrderRequest;

public interface OrderService {

    String placeOrder(OrderRequest orderRequest);


}
