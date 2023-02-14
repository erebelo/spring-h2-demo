package com.erebelo.springh2demo.service;

import com.erebelo.springh2demo.domain.request.OrderRequest;
import com.erebelo.springh2demo.domain.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse insertOrder(OrderRequest orderRequest);

    List<OrderResponse> getOrderByCustomerId(Long customerId);

    List<OrderResponse> getOrderByProductId(Long productId);

}
