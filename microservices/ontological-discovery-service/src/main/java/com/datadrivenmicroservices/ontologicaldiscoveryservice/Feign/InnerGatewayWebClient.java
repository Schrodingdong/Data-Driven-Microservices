package com.datadrivenmicroservices.ontologicaldiscoveryservice.Feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InnerGatewayWebClient {
    private WebClient webClient;
    @Value("${route-matcher.base-url}")
    private String innerGatewayUrl;
    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @PostConstruct
    private void init(){
        webClient = WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(innerGatewayUrl)
                .filter(lbFunction)
                .build();
    }

    public String forwardRequest(String uri, String requestBody, String requestMethod){
        // forward the request to the inner gateway
        String res ="";
        switch (requestMethod){
            case "POST":
                res = webClient.post().uri(uri).bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
                break;
            case "GET":
                res = webClient.get().uri(uri).retrieve().bodyToMono(String.class).block();
                break;
            case "PUT":
                res = webClient.put().uri(uri).bodyValue(requestBody).retrieve().bodyToMono(String.class).block();
                break;
            case "DELETE":
                res = webClient.delete().uri(uri).retrieve().bodyToMono(String.class).block();
                break;
            default:
                break;
        }
        return res;
    }

}
