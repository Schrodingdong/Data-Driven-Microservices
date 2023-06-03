package com.datadrivenmicroservices.ontologicaldiscoveryservice.routing;

import com.datadrivenmicroservices.ontologicaldiscoveryservice.Feign.InnerGatewayWebClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RoutingUtils {
    private final InnerGatewayWebClient innerGatewayWebClient;
    private final RoutingUrls routingUrls;
    public String getRouteUrlFromName(String serviceName, String requestBody, String requestMethod, Map<String,String> requestParams){
        serviceName = "order";
        String filteredUrl = "";
        List<String> urlList = routingUrls.getUrlsForAServiceAndMethod(serviceName, requestMethod);

        // if there is only one url, return it
        if (urlList.size() == 1) {
            filteredUrl = urlList.get(0);
        } else {
            // if not Filter :
            if (requestParams.isEmpty()){
                filteredUrl = urlList.stream().findFirst().orElse("");
            } else {
                for (Map.Entry<String, String> entry : requestParams.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    filteredUrl = urlList.stream().filter(url -> url.contains(key)).findFirst().orElse("");
                }
            }
        }
        filteredUrl = routingUrls.getUrlWithInsertedParams(filteredUrl, requestParams);

        return filteredUrl;
    }

    public String routeToUrl( String uri, String requestBody, String requestMethod){
        return innerGatewayWebClient.forwardRequest(uri, requestBody, requestMethod);
    }
}
