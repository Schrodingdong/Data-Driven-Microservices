package com.datadrivenmicroservices.ontologicaldiscoveryservice.controller;

import com.datadrivenmicroservices.ontologicaldiscoveryservice.service.OntologyDiscoveryService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class OntologyDiscoveryController {
    private final OntologyDiscoveryService ontologyDiscoveryService;

    @PostMapping
    public ResponseEntity<?> routeAndReturn(@RequestBody String request){
        System.out.println(request);
        ontologyDiscoveryService.routeAndReturn(request);
        return ResponseEntity.ok().body(request);
    }

}
