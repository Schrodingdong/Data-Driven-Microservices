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
        String filteredUrl = "";
        System.out.println("[RoutingUtils] serviceName = " + serviceName);
        System.out.println("[RoutingUtils] requestMethod = " + requestMethod);
        System.out.println("[RoutingUtils] requestParams = " + requestParams);
        System.out.println("[RoutingUtils] requestBody = " + requestBody);
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
                    if (key.contains("id") || key.contains("Id") || key.contains("ID")) {
                        filteredUrl = urlList.stream().filter(url -> url.contains(key)).findFirst().orElse("");
                    } else {
                        filteredUrl = urlList.stream().findFirst().orElse("");
                    }
                }
            }
        }
        filteredUrl = routingUrls.getUrlWithInsertedParams(filteredUrl, requestParams);
        return "/" + routingUrls.getServiceName(serviceName) + filteredUrl;
    }

    public String routeToUrl( String uri, String requestBody, String requestMethod){
        return innerGatewayWebClient.forwardRequest(uri, requestBody, requestMethod);
    }
}
