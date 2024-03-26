package com.asule.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asule.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Integer>{

}
