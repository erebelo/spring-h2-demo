package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.request.ProductRequest;
import com.erebelo.springh2demo.domain.response.ProductResponse;
import com.erebelo.springh2demo.exception.StandardException;
import com.erebelo.springh2demo.mapper.ProductMapper;
import com.erebelo.springh2demo.repository.ProductRepository;
import com.erebelo.springh2demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_001;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_003;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_004;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_005;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_409_002;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private static final String CHECK_OBJ_LOGGER = "Checking whether product object exists by %s: {}";
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";
    private static final String PRODUCT = "Product";

    @Override
    public List<ProductResponse> getProducts() {
        LOGGER.info("Getting products");
        var productEntityList = repository.findAll();

        if (productEntityList.isEmpty()) {
            throw new StandardException(ERROR_404_004, PRODUCT);
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntityList);
        return mapper.entityListToResponseList(productEntityList);
    }

    @Override
    public List<ProductResponse> getProductByName(String name) {
        LOGGER.info("Getting product by name: {}", name);
        var productEntityList = repository.findByNameContainingIgnoreCase(name);

        if (productEntityList.isEmpty()) {
            throw new StandardException(ERROR_404_005, PRODUCT, name);
        }

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntityList);
        return mapper.entityListToResponseList(productEntityList);
    }

    @Override
    public ProductResponse getProductById(Integer id) {
        LOGGER.info("Getting product by id: {}", id);
        var productEntity = repository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_001, PRODUCT, id));

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntity);
        return mapper.entityToResponse(productEntity);
    }

    @Override
    @Transactional
    public ProductResponse insertProduct(ProductRequest productRequest) {
        LOGGER.info(String.format(CHECK_OBJ_LOGGER, "name"), productRequest.getName());
        repository.findByName(productRequest.getName()).ifPresent(o -> {
            throw new StandardException(ERROR_409_002, PRODUCT, productRequest.getName());
        });

        var productEntity = mapper.requestToEntity(productRequest);
        LOGGER.info("Inserting product: {}", productEntity);
        productEntity = repository.save(productEntity);

        LOGGER.info(RESPONSE_BODY_LOGGER, productEntity);
        return mapper.entityToResponse(productEntity);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        LOGGER.info(String.format(CHECK_OBJ_LOGGER, "id"), id);
        var productEntity = repository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_003, PRODUCT, id));

        LOGGER.info("Deleting product: {}", productEntity);
        repository.delete(productEntity);
    }
}
