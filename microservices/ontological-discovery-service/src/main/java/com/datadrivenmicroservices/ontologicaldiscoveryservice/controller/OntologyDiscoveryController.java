package com.datadrivenmicroservices.ontologicaldiscoveryservice.controller;

import com.datadrivenmicroservices.ontologicaldiscoveryservice.service.OntologyDiscoveryService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class OntologyDiscoveryController {
    private final OntologyDiscoveryService ontologyDiscoveryService;

    @PostMapping
    public ResponseEntity<?> routeAndReturn(Object request){
        return ontologyDiscoveryService.routeAndReturn(request);
    }

}
