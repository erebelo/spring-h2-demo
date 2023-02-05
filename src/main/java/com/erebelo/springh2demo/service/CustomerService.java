package com.erebelo.springh2demo.service;

import com.erebelo.springh2demo.domain.request.CustomerRequest;
import com.erebelo.springh2demo.domain.response.CustomerResponse;

public interface CustomerService {

    CustomerResponse getCustomerById(Integer id);

    CustomerResponse insertCustomer(CustomerRequest customerRequest);

    void updateCustomer(Integer id, CustomerRequest customerRequest);

    void deleteCustomer(Integer id);

}
