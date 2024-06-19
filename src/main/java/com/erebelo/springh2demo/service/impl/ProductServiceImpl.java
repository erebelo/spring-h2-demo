package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.request.ProductRequest;
import com.erebelo.springh2demo.domain.response.ProductResponse;
import com.erebelo.springh2demo.exception.model.ConflictException;
import com.erebelo.springh2demo.exception.model.NotFoundException;
import com.erebelo.springh2demo.mapper.ProductMapper;
import com.erebelo.springh2demo.repository.ProductRepository;
import com.erebelo.springh2demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private static final String CHECK_OBJ_LOGGER = "Checking whether product object exists by {}: {}";
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts() {
        LOGGER.info("Getting products");
        var productEntityList = repository.findAll();

        if (productEntityList.isEmpty()) {
            throw new NotFoundException("Product not found");
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntityList);
        return mapper.entityListToResponseList(productEntityList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductByName(String name) {
        LOGGER.info("Getting product by name: {}", name);
        var productEntityList = repository.findByNameContainingIgnoreCase(name);

        if (productEntityList.isEmpty()) {
            throw new NotFoundException(String.format("Product not found by name: %s", name));
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntityList);
        return mapper.entityListToResponseList(productEntityList);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        LOGGER.info("Getting product by id: {}", id);
        var productEntity = repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Product not found by id: %s", id)));

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntity);
        return mapper.entityToResponse(productEntity);
    }

    @Override
    @Transactional
    public ProductResponse insertProduct(ProductRequest productRequest) {
        LOGGER.info(CHECK_OBJ_LOGGER, "name", productRequest.getName());
        repository.findByName(productRequest.getName()).ifPresent(o -> {
            throw new ConflictException(String.format("Product already exists by name: %s", productRequest.getName()));
        });

        var productEntity = mapper.requestToEntity(productRequest);
        LOGGER.info("Inserting product: {}", productEntity);
        productEntity = repository.save(productEntity);

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntity);
        return mapper.entityToResponse(productEntity);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        LOGGER.info(CHECK_OBJ_LOGGER, "id", id);
        var productEntity = repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("The delete operation has not been completed as the product was not found by id: %s", id)));

        LOGGER.info("Deleting product: {}", productEntity);
        repository.delete(productEntity);
    }
}
