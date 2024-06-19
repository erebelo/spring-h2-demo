package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.entity.CustomerEntity;
import com.erebelo.springh2demo.domain.request.CustomerRequest;
import com.erebelo.springh2demo.domain.response.CustomerResponse;
import com.erebelo.springh2demo.exception.model.ConflictException;
import com.erebelo.springh2demo.exception.model.NotFoundException;
import com.erebelo.springh2demo.mapper.CustomerMapper;
import com.erebelo.springh2demo.repository.CustomerRepository;
import com.erebelo.springh2demo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper mapper;
    private final CustomerRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private static final String CHECK_OBJ_LOGGER = "Checking whether customer object exists by {}: {}";
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        LOGGER.info("Getting customer by id: {}", id);
        var customerEntity = repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Customer not found by id: %s", id)));

        LOGGER.info(RESPONSE_BODY_LOGGER, customerEntity);
        return mapper.entityToResponse(customerEntity);
    }

    @Override
    @Transactional
    public CustomerResponse insertCustomer(CustomerRequest customerRequest) {
        LOGGER.info(CHECK_OBJ_LOGGER, "email", customerRequest.getEmail());
        repository.findByEmail(customerRequest.getEmail()).ifPresent(o -> {
            throw new ConflictException("Customer already exists. Try updating it instead of entering it");
        });

        var customerEntity = mapper.requestToEntity(customerRequest);
        LOGGER.info("Inserting customer: {}", customerEntity);
        customerEntity = repository.save(customerEntity);

        LOGGER.info(RESPONSE_BODY_LOGGER, customerEntity);
        return mapper.entityToResponse(customerEntity);
    }

    @Override
    @Transactional
    public void updateCustomer(Long id, CustomerRequest customerRequest) {
        LOGGER.info(CHECK_OBJ_LOGGER, "id", id);
        var customerEntity = repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Customer not found by id: %s. Try entering it instead of updating it", id)));

        setUpdateAttributes(customerEntity, mapper.requestToEntity(customerRequest));
        LOGGER.info("Updating customer: {}", customerEntity);
        repository.save(customerEntity);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        LOGGER.info(CHECK_OBJ_LOGGER, "id", id);
        var customerEntity = repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("The delete operation has not been completed as the customer was not found by id: %s", id)));

        LOGGER.info("Deleting customer: {}", customerEntity);
        repository.delete(customerEntity);
    }

    private void setUpdateAttributes(CustomerEntity customerEntity, CustomerEntity requestToEntity) {
        customerEntity.setName(requestToEntity.getName());
        customerEntity.setEmail(requestToEntity.getEmail());
    }
}
