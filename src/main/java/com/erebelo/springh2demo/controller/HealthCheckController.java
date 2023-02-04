package com.erebelo.springh2demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.erebelo.springh2demo.constants.BusinessConstants.HEALTH_CHECK;

@RestController
public class HealthCheckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class);

    @GetMapping(value = HEALTH_CHECK, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getHealthCheck() {
        LOGGER.info("Getting health check");
        return new ResponseEntity<>("Spring H2 Demo application is up and running", HttpStatus.OK);
    }
}
