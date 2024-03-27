package com.erebelo.springh2demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.erebelo.springh2demo.constant.BusinessConstant.HEALTH_CHECK;

@RestController
@RequestMapping(HEALTH_CHECK)
public class HealthCheckController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class);

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getHealthCheck() {
        LOGGER.info("Getting health check");
        return ResponseEntity.ok("Spring H2 Demo application is up and running");
    }
}
