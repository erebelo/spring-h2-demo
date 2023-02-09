package com.erebelo.springh2demo.config;

import com.erebelo.springh2demo.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class AppLocalConfiguration {

    private final MockService mockService;

    @Bean
    public void instantiateH2Database() {
        mockService.instantiateH2Database();
    }
}
