package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.request.CustomerDTO;
import com.erebelo.springh2demo.domain.request.CustomerRequest;
import com.erebelo.springh2demo.domain.request.OrderRequest;
import com.erebelo.springh2demo.domain.request.ProductDTO;
import com.erebelo.springh2demo.domain.request.ProductOrderDTO;
import com.erebelo.springh2demo.domain.request.ProductRequest;
import com.erebelo.springh2demo.service.CustomerService;
import com.erebelo.springh2demo.service.MockService;
import com.erebelo.springh2demo.service.OrderService;
import com.erebelo.springh2demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class MockServiceImpl implements MockService {

    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MockServiceImpl.class);

    @Override
    public void instantiateH2Database() {
        LOGGER.info("Inserting customer mock");
        var customerRequest = CustomerRequest.builder()
                .name("Eduardo")
                .email("eduardo@springh2demo.com")
                .build();
        var customerResponse = customerService.insertCustomer(customerRequest);

        LOGGER.info("Inserting products mock");
        var productRequestOne = ProductRequest.builder()
                .name("Smartphone E1")
                .price(BigDecimal.valueOf(2789.90))
                .build();
        var productRequestTwo = ProductRequest.builder()
                .name("TV 4k")
                .price(BigDecimal.valueOf(1976.59))
                .build();
        var productResponseOne = productService.insertProduct(productRequestOne);
        var productResponseTwo = productService.insertProduct(productRequestTwo);

        LOGGER.info("Inserting order mock");
        var orderRequest = OrderRequest.builder()
                .customer(CustomerDTO.builder().id(customerResponse.getId()).build())
                .productOrders(new HashSet<>(Arrays.asList(
                        ProductOrderDTO.builder()
                                .amount(1)
                                .discount(BigDecimal.valueOf(0.0))
                                .product(ProductDTO.builder().id(productResponseOne.getId()).build())
                                .build(),
                        ProductOrderDTO.builder()
                                .amount(2)
                                .discount(BigDecimal.valueOf(10.0))
                                .product(ProductDTO.builder().id(productResponseTwo.getId()).build())
                                .build()
                ))).build();
        orderService.insertOrder(orderRequest);
    }
}
