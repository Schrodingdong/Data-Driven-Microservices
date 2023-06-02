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

    public ResponseEntity<?> routeAndReturn(String requestString){
        // ontological matching
        String routeName = ontologyMatching.matchRequestWithOntology(requestString);

        // Return URL for routing
        String routeUrl = routingUtils.getRouteUrlFromName(routeName);

        // delegate work to inner gateway
        RequestEntity<?> requestEntity = RequestEntity.post("http://localhost").body(requestString);
        System.out.println(requestEntity);
        routingUtils.routeToUrl(requestEntity);

        return null;
    }
}
