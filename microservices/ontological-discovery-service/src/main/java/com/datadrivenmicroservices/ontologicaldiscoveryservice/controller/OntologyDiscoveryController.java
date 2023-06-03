package com.datadrivenmicroservices.ontologicaldiscoveryservice.controller;

import com.datadrivenmicroservices.ontologicaldiscoveryservice.service.OntologyDiscoveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class OntologyDiscoveryController {
    private final OntologyDiscoveryService ontologyDiscoveryService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> post_routeAndReturn(HttpServletRequest request) throws IOException {
        // get method type
        String requestMethod = request.getMethod();

        // get body of the request
        BufferedReader reader = request.getReader();
        String line;
        String requestBody = "";
        while ((line = reader.readLine()) != null)
            requestBody += line+"\n";

        // get path parameters
        Map<String, String> requestParams = getRequestParams(request);
        // route and return
        String responseBody = ontologyDiscoveryService.routeAndReturn(requestBody, requestMethod, requestParams);

        // construct Response
        ResponseEntity responseEntity = ResponseEntity.ok().body(
                objectMapper.createParser(responseBody).readValueAsTree()
        );
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<?> put_routeAndReturn(HttpServletRequest request) throws IOException {
        // get method type
        String requestMethod = request.getMethod();

        // get body of the request
        BufferedReader reader = request.getReader();
        String line;
        String requestBody = "";
        while ((line = reader.readLine()) != null)
            requestBody += line+"\n";

        // get path parameters
        Map<String, String> requestParams = getRequestParams(request);
        // route and return
        String responseBody = ontologyDiscoveryService.routeAndReturn(requestBody, requestMethod, requestParams);

        // construct Response
        ResponseEntity responseEntity = ResponseEntity.ok().body(
                objectMapper.createParser(responseBody).readValueAsTree()
        );
        return responseEntity;
    }

    @GetMapping
    public ResponseEntity<?> get_routeAndReturn(HttpServletRequest request) throws IOException {
        // get method type
        String requestMethod = request.getMethod();
        String requestBody = "";

        // get path parameters
        Map<String, String> requestParams = getRequestParams(request);
        // route and return
        String responseBody = ontologyDiscoveryService.routeAndReturn(requestBody, requestMethod, requestParams);

        // construct Response
        ResponseEntity responseEntity = ResponseEntity.ok().body(
                objectMapper.createParser(responseBody).readValueAsTree()
        );
        return responseEntity;
    }

    @DeleteMapping
    public ResponseEntity<?> delete_routeAndReturn(HttpServletRequest request){
        // get method type
        String requestMethod = request.getMethod();
        String requestBody = "";

        // get path parameters
        Map<String, String> requestParams = getRequestParams(request);
        // route and return
        String responseBody = ontologyDiscoveryService.routeAndReturn(requestBody, requestMethod, requestParams);

        // construct Response
        ResponseEntity responseEntity = ResponseEntity.ok().body(
                Boolean.parseBoolean(responseBody)
        );
        return responseEntity;
    }




    private static Map<String, String> getRequestParams(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> requestParams = new HashMap<>();
        while(paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameterValues(paramName)[0];
            requestParams.put(paramName, paramValue);
        }
        System.out.println(requestParams);
        return requestParams;
    }
}
