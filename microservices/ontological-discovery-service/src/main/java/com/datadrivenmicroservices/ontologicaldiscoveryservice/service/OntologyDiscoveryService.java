package com.datadrivenmicroservices.ontologicaldiscoveryservice.service;

import com.datadrivenmicroservices.ontologicaldiscoveryservice.ontology.OntologyMatching;
import com.datadrivenmicroservices.ontologicaldiscoveryservice.routing.RoutingUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OntologyDiscoveryService {
    private final OntologyMatching ontologyMatching;
    private final RoutingUtils routingUtils;

    public ResponseEntity<?> routeAndReturn(Object requestObject){
        // ontological matching
        String routeName = ontologyMatching.matchRequestWithOntology("request");

        // Return URL for routing
        String routeUrl = routingUtils.getRouteUrlFromName(routeName);

        // delegate work to inner gateway
        RequestEntity<?> requestEntity = RequestEntity.post(routeUrl).body(requestObject);
        routingUtils.routeToUrl(requestEntity);

        return null;
    }
}
