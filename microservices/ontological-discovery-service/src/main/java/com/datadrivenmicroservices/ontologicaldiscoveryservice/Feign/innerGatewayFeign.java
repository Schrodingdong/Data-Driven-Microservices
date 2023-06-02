package com.datadrivenmicroservices.ontologicaldiscoveryservice.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "inner-gateway-service")
public interface innerGatewayFeign {
    ResponseEntity<?> delegateToService(RequestEntity<?> requestEntity);
}
