package com.erebelo.springh2demo.mapper;

import com.erebelo.springh2demo.domain.entity.OrderEntity;
import com.erebelo.springh2demo.domain.entity.ProductOrderEntity;
import com.erebelo.springh2demo.domain.request.ProductOrderDTO;
import com.erebelo.springh2demo.domain.response.OrderResponse;
import com.erebelo.springh2demo.domain.response.ProductOrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

import static org.mapstruct.ReportingPolicy.WARN;

@Mapper(componentModel = "spring", unmappedTargetPolicy = WARN)
public interface OrderMapper {

    Set<ProductOrderEntity> productOrderRequestSetToEntitySet(Set<ProductOrderDTO> productOrderDTOSet);

    @Mapping(target = "id.product.id", source = "product.id")
    ProductOrderEntity map(ProductOrderDTO productOrderDTO);

    List<OrderResponse> entityListToResponseList(List<OrderEntity> entityList);

    OrderResponse entityToResponse(OrderEntity entity);

    @Mapping(target = "product.id", source = "id.product.id")
    @Mapping(target = "product.name", source = "id.product.name")
    @Mapping(target = "product.price", source = "id.product.price")
    ProductOrderResponseDTO map(ProductOrderEntity id);

}
