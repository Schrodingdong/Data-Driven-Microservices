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
        // ontological matching
        String routeName = ontologyMatching.matchRequestWithOntology(requestBody);

        // Return uri for routing
        String uri = routingUtils.getRouteUrlFromName(routeName, requestBody, requestMethod, requestParams);

        // delegate work to inner gateway
        return routingUtils.routeToUrl(uri, requestBody, requestMethod);
    }
}
