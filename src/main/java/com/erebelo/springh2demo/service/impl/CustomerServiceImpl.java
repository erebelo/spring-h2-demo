package com.erebelo.springh2demo.service.impl;

import com.erebelo.springh2demo.domain.entity.CustomerEntity;
import com.erebelo.springh2demo.domain.request.CustomerRequest;
import com.erebelo.springh2demo.domain.response.CustomerResponse;
import com.erebelo.springh2demo.exception.StandardException;
import com.erebelo.springh2demo.mapper.CustomerMapper;
import com.erebelo.springh2demo.repository.CustomerRepository;
import com.erebelo.springh2demo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_001;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_002;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_404_003;
import static com.erebelo.springh2demo.exception.ErrorEnum.ERROR_409_001;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper mapper;
    private final CustomerRepository repository;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private static final String CHECK_OBJ_LOGGER = "Checking whether customer object exists by %s: {}";
    private static final String RESPONSE_BODY_LOGGER = "Response body: {}";

    @Override
    public CustomerResponse getCustomerById(Integer id) {
        LOGGER.info("Getting customer by id: {}", id);
        var customerEntity = repository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_001, id));

        LOGGER.info(RESPONSE_BODY_LOGGER, customerEntity);
        return mapper.entityToResponse(customerEntity);
    }

    @Override
    @Transactional
    public CustomerResponse insertCustomer(CustomerRequest customerRequest) {
        LOGGER.info(String.format(CHECK_OBJ_LOGGER, "email"), customerRequest.getEmail());
        repository.findByEmail(customerRequest.getEmail()).ifPresent(o -> {
            throw new StandardException(ERROR_409_001);
        });

        var customerEntity = mapper.requestToEntity(customerRequest);
        LOGGER.info("Inserting customer: {}", customerEntity);
        customerEntity = repository.save(customerEntity);

        LOGGER.info(RESPONSE_BODY_LOGGER, customerEntity);
        return mapper.entityToResponse(customerEntity);
    }

    @Override
    @Transactional
    public void updateCustomer(Integer id, CustomerRequest customerRequest) {
        LOGGER.info(String.format(CHECK_OBJ_LOGGER, "id"), id);
        var customerEntity = repository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_002, id));

        setUpdateAttributes(customerEntity, mapper.requestToEntity(customerRequest));
        LOGGER.info("Updating customer: {}", customerEntity);
        repository.save(customerEntity);
    }

    @Override
    @Transactional
    public void deleteCustomer(Integer id) {
        LOGGER.info(String.format(CHECK_OBJ_LOGGER, "id"), id);
        var customerEntity = repository.findById(id).orElseThrow(() ->
                new StandardException(ERROR_404_003, id));

        LOGGER.info("Deleting customer: {}", customerEntity);
        repository.delete(customerEntity);
    }

    private void setUpdateAttributes(CustomerEntity customerEntity, CustomerEntity requestToEntity) {
        customerEntity.setName(requestToEntity.getName());
        customerEntity.setEmail(requestToEntity.getEmail());
    }
}
