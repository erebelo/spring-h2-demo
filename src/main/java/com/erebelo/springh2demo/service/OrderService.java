package com.erebelo.springh2demo.service;

import com.erebelo.springh2demo.domain.request.OrderRequest;
import com.erebelo.springh2demo.domain.response.OrderResponse;

public interface OrderService {

    OrderResponse insertOrder(OrderRequest orderRequest);

}
