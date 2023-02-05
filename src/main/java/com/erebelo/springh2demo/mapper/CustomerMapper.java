package com.erebelo.springh2demo.mapper;

import com.erebelo.springh2demo.domain.entity.CustomerEntity;
import com.erebelo.springh2demo.domain.request.CustomerRequest;
import com.erebelo.springh2demo.domain.response.CustomerResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.WARN;

@Mapper(componentModel = "spring", unmappedTargetPolicy = WARN)
public interface CustomerMapper {

    CustomerResponse entityToResponse(CustomerEntity entity);

    CustomerEntity requestToEntity(CustomerRequest request);

    CustomerEntity responseToEntity(CustomerResponse response);

}
