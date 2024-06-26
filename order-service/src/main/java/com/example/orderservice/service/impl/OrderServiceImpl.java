package com.asule.orderservice.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.asule.orderservice.entity.Order;
import com.asule.orderservice.entity.OrderItem;
import com.asule.orderservice.exception.InventoryServiceException;
import com.asule.orderservice.exception.NotEnoughQuantityException;
import com.asule.orderservice.exception.OrderServiceException;
import com.asule.orderservice.model.GenericResponse;
import com.asule.orderservice.model.OrderItemRequest;
import com.asule.orderservice.model.OrderRequest;
import com.asule.orderservice.repository.OrderRepository;
import com.asule.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public OrderServiceImpl(OrderRepository orderRepository, WebClient.Builder webClientBuilder){
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }
    @SuppressWarnings("unchecked")
    @Override
    public String placeOrder(OrderRequest orderRequest) {
       Order order = new Order();
        // Checks
        // ! All products exists in the inventory
        // http://localhost:6002/api/inventory/check
        // restTemplate

        List<String> productCodes = new ArrayList<>();
        List<Integer> productQuantities = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            productCodes.add(orderItemRequest.getProductCode());
            productQuantities.add(orderItemRequest.getQuantity());
        }
          
        
        log.info("{}", productCodes);
        log.info("{}", productQuantities);
        GenericResponse<?> response = webClientBuilder.build().get()
                .uri("http://localhost:inventory/api/inventory/check",
                        uriBuilder -> uriBuilder
                                .queryParam("productCodes", productCodes)
                                .queryParam("productQuantities", productQuantities)
                                .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> handleError(clientResponse))
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<?>>() {
                })
                .block();
        if (response.isSuccess()) {
            // stock
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setOrderTime(Instant.now());
            var orderItems = orderRequest.getOrderItems().stream().map(this::mapToOrderItemEntity).toList();
            order.setOrderItems(orderItems);
            orderRepository.save(order);
             //TODO: make the call to inventory to reduce quantity
            //TODO: process payment for an order
            return order.getOrderNumber();
        } else {
            // ! throw an exception with the listing of the products that do have enough
            log.error("Not Enough stock");
            log.info("{}", response.getData());
            if(response.getData() instanceof Map){
                throw new NotEnoughQuantityException(response.getMsg(),(Map<String, Integer>) response.getData());
            }
            throw new OrderServiceException(response.getMsg());
        }

    }

    private Mono<? extends Throwable> handleError(ClientResponse response) {
        log.error("Client error received: {}", response.statusCode());
        return Mono.error(new InventoryServiceException("Error in inventory service"));
    }

    private OrderItem mapToOrderItemEntity(OrderItemRequest itemRequest){
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(itemRequest, orderItem);
        return orderItem;
    }

}