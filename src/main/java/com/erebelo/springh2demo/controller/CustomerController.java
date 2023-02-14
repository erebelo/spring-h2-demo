package com.erebelo.springh2demo.controller;

import com.erebelo.springh2demo.domain.request.CustomerRequest;
import com.erebelo.springh2demo.domain.response.CustomerResponse;
import com.erebelo.springh2demo.service.CustomerService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.erebelo.springh2demo.constants.BusinessConstants.CUSTOMER;

@Validated
@RestController
@RequestMapping(CUSTOMER)
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    private static final String CUSTOMER_RESPONSE = "Customer response: {}";

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        LOGGER.info("Getting customer by id: {}", id);

        var response = service.getCustomerById(id);

        LOGGER.info(CUSTOMER_RESPONSE, response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        LOGGER.info("Inserting customer - Request body: {}", customerRequest);
        var response = service.insertCustomer(customerRequest);

        LOGGER.info(CUSTOMER_RESPONSE, response);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequest customerRequest) {
        LOGGER.info("Updating customer - Request body: {}", customerRequest);
        service.updateCustomer(id, customerRequest);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        LOGGER.info("Deleting customer by id: {}", id);
        service.deleteCustomer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
