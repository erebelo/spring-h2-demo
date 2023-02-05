package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.entity.OrderEntity;
import com.erebelo.springh2demo.domain.entity.ProductOrderEntity;
import com.erebelo.springh2demo.domain.request.OrderRequest;
import com.erebelo.springh2demo.domain.response.CustomerResponse;
import com.erebelo.springh2demo.domain.response.OrderResponse;
import com.erebelo.springh2demo.domain.response.ProductResponse;
import com.erebelo.springh2demo.mapper.CustomerMapper;
import com.erebelo.springh2demo.mapper.OrderMapper;
import com.erebelo.springh2demo.mapper.ProductMapper;
import com.erebelo.springh2demo.repository.OrderRepository;
import com.erebelo.springh2demo.repository.ProductOrderRepository;
import com.erebelo.springh2demo.service.CustomerService;
import com.erebelo.springh2demo.service.OrderService;
import com.erebelo.springh2demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper mapper;
    private final CustomerMapper customerMapper;
    private final ProductMapper productMapper;

    private final CustomerService customerService;
    private final ProductService productService;

    private final OrderRepository repository;
    private final ProductOrderRepository productOrderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";

    @Override
    @Transactional
    public OrderResponse insertOrder(OrderRequest orderRequest) {
        LOGGER.info("Setting order insert attributes");
        var orderEntity = new OrderEntity();
        setOrderInsertAttributes(orderEntity, orderRequest);

        LOGGER.info("Inserting order: {}", orderEntity);
        orderEntity = repository.save(orderEntity);

        LOGGER.info("Setting product orders insert attributes");
        setProductOrderInsertAttributes(orderEntity);

        LOGGER.info("Inserting product orders: {}", orderEntity.getProductOrders());
        productOrderRepository.saveAll(orderEntity.getProductOrders());

        LOGGER.info(RESPONSE_BODY_LOGGER, orderEntity);
        return mapper.entityToResponse(orderEntity);
    }

    private void setOrderInsertAttributes(OrderEntity orderEntity, OrderRequest orderRequest) {
        LOGGER.info("Getting customer by id from customer service");
        CustomerResponse customerResponse = customerService.getCustomerById(orderRequest.getCustomer().getId());

        orderEntity.setCustomer(customerMapper.responseToEntity(customerResponse));
        orderEntity.setProductOrders(mapper.productOrderRequestSetToEntitySet(orderRequest.getProductOrders()));
        calculateAndSetTotal(orderEntity.getProductOrders());
        LOGGER.info("Order entity: {}", orderEntity);
    }

    private void calculateAndSetTotal(Set<ProductOrderEntity> productOrders) {
        for (ProductOrderEntity productOrder : productOrders) {
            LOGGER.info("Getting product by id from product service");
            ProductResponse productResponse = productService.getProductById(productOrder.getProduct().getId());

            var price = productResponse.getPrice();
            var amount = BigDecimal.valueOf(productOrder.getAmount());
            var discount = (BigDecimal.valueOf(100).subtract(productOrder.getDiscount())).divide(BigDecimal.valueOf(100));

            productOrder.getId().setProduct(productMapper.responseToEntity(productResponse));
            productOrder.setTotal((amount.multiply(price)).multiply(discount));
        }
    }

    private void setProductOrderInsertAttributes(OrderEntity orderEntity) {
        for (ProductOrderEntity productOrder : orderEntity.getProductOrders()) {
            productOrder.getId().setOrder(orderEntity);
        }
    }
}
