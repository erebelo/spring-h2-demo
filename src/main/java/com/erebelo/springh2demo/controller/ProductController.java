package com.erebelo.springh2demo.controller;

import com.erebelo.springh2demo.domain.request.ProductRequest;
import com.erebelo.springh2demo.domain.response.ProductResponse;
import com.erebelo.springh2demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.erebelo.springh2demo.constants.BusinessConstants.PRODUCT;
import static com.erebelo.springh2demo.util.UrlUtil.decodeParam;

@Validated
@RestController
@RequestMapping(PRODUCT)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    private static final String PRODUCT_RESPONSE = "Product response: {}";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getProducts() {
        LOGGER.info("Getting products");

        var response = service.getProducts();

        LOGGER.info(PRODUCT_RESPONSE, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponse>> getProductByName(@RequestParam String name) {
        name = decodeParam(name);
        LOGGER.info("Getting product by name: {}", name);

        var response = service.getProductByName(name);

        LOGGER.info(PRODUCT_RESPONSE, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertProduct(@Valid @RequestBody ProductRequest productRequest) {
        LOGGER.info("Inserting product - Request body: {}", productRequest);
        var response = service.insertProduct(productRequest);

        LOGGER.info(PRODUCT_RESPONSE, response);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/").queryParam("name={name}").buildAndExpand(response.getName()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        LOGGER.info("Deleting product by id: {}", id);
        service.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
