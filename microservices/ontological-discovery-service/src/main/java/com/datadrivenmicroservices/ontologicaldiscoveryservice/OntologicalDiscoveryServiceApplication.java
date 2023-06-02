package com.datadrivenmicroservices.ontologicaldiscoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OntologicalDiscoveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OntologicalDiscoveryServiceApplication.class, args);
    }

}
