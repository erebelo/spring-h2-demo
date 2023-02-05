package com.erebelo.springh2demo.mapper;

import com.erebelo.springh2demo.domain.entity.ProductEntity;
import com.erebelo.springh2demo.domain.request.ProductRequest;
import com.erebelo.springh2demo.domain.response.ProductResponse;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.ReportingPolicy.WARN;

@Mapper(componentModel = "spring", unmappedTargetPolicy = WARN)
public interface ProductMapper {

    List<ProductResponse> entityListToResponseList(List<ProductEntity> entityList);

    ProductResponse entityToResponse(ProductEntity entity);

    ProductEntity requestToEntity(ProductRequest request);

}
