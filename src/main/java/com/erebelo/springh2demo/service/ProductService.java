package com.erebelo.springh2demo.service;

import com.erebelo.springh2demo.domain.request.ProductRequest;
import com.erebelo.springh2demo.domain.response.ProductResponse;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getProducts();

    List<ProductResponse> getProductByName(String name);

    ProductResponse getProductById(Long id);

    ProductResponse insertProduct(ProductRequest productRequest);

    void deleteProduct(Long id);

}
