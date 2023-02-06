package com.erebelo.springh2demo.controller;

import com.erebelo.springh2demo.domain.request.OrderRequest;
import com.erebelo.springh2demo.domain.response.OrderResponse;
import com.erebelo.springh2demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.erebelo.springh2demo.constants.BusinessConstants.ORDER;

@Validated
@RestController
@RequestMapping(ORDER)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDER_RESPONSE = "Order response: {}";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> insertOrder(@Valid @RequestBody OrderRequest orderRequest) {
        LOGGER.info("Inserting order - Request body: {}", orderRequest);
        var response = service.insertOrder(orderRequest);

        LOGGER.info(ORDER_RESPONSE, response);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
