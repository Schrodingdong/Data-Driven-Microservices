package com.datadrivenmicroservices.ontologicaldiscoveryservice.service;

import com.datadrivenmicroservices.ontologicaldiscoveryservice.ontology.OntologyMatching;
import com.datadrivenmicroservices.ontologicaldiscoveryservice.routing.RoutingUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OntologyDiscoveryService {
    private final OntologyMatching ontologyMatching;
    private final RoutingUtils routingUtils;

    public OntologyDiscoveryService(OntologyMatching ontologyMatching, RoutingUtils routingUtils) {
        this.ontologyMatching = ontologyMatching;
        this.routingUtils = routingUtils;
    }

    public String routeAndReturn(String requestBody, String requestMethod, Map<String, String> requestParams){
        // ontological matching on body
        if (requestBody.isEmpty()) requestBody = "{}";
        String routeName = ontologyMatching.matchRequestWithOntology(requestBody, requestParams, requestMethod);
        System.out.println("routeName = " + routeName);
        // Return uri for routing
        String uri = routingUtils.getRouteUrlFromName(routeName, requestBody, requestMethod, requestParams);
        System.out.println("uri = " + uri);

        // delegate work to inner gateway
        return routingUtils.routeToUrl(uri, requestBody, requestMethod);
    }
}
