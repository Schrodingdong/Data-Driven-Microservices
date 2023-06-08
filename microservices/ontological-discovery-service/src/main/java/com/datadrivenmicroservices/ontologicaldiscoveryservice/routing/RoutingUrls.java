package com.datadrivenmicroservices.ontologicaldiscoveryservice.routing;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
public class RoutingUrls {
    // For Products
    @Value("${route-matcher.services.product.service-name}")
    private String productServiceName;
    @Value("${route-matcher.services.product.method.GET[0]}")
    private String productGetAllUrl;
    @Value("${route-matcher.services.product.method.GET[1]}")
    private String productGetByIdUrl;
    @Value("${route-matcher.services.product.method.POST[0]}")
    private String productSaveUrl;
    @Value("${route-matcher.services.product.method.PUT[0]}")
    private String productUpdateUrl;
    @Value("${route-matcher.services.product.method.DELETE[0]}")
    private String productDeleteUrl;

    // For Customers
    @Value("${route-matcher.services.customer.service-name}")
    private String customerServiceName;
    @Value("${route-matcher.services.customer.method.GET[0]}")
    private String customerGetAllUrl;
    @Value("${route-matcher.services.customer.method.GET[1]}")
    private String customerGetByIdUrl;
    @Value("${route-matcher.services.customer.method.POST[0]}")
    private String customerSaveUrl;
    @Value("${route-matcher.services.customer.method.PUT[0]}")
    private String customerUpdateUrl;
    @Value("${route-matcher.services.customer.method.DELETE[0]}")
    private String customerDeleteUrl;

    // For Orders
    @Value("${route-matcher.services.order.service-name}")
    private String orderServiceName;
    @Value("${route-matcher.services.order.method.GET[0]}")
    private String orderGetAllUrl;
    @Value("${route-matcher.services.order.method.GET[1]}")
    private String orderGetByIdUrl;
    @Value("${route-matcher.services.order.method.GET[2]}")
    private String orderGetByCustomerIdUrl;
    @Value("${route-matcher.services.order.method.POST[0]}")
    private String orderCreateUrl;
    @Value("${route-matcher.services.order.method.PUT[0]}")
    private String orderUpdateUrl;
    @Value("${route-matcher.services.order.method.DELETE[0]}")
    private String orderDeleteUrl;

    private Map<String,List<String>> getUrlsForAService(String serviceName){
        Map<String, List<String>> urls = new HashMap<>();
        switch (serviceName){
            case "product":
                urls.put("GET", List.of(productGetAllUrl, productGetByIdUrl));
                urls.put("POST", List.of(productSaveUrl));
                urls.put("PUT", List.of(productUpdateUrl));
                urls.put("DELETE", List.of(productDeleteUrl));
                break;
            case "customer":
                urls.put("GET", List.of(customerGetAllUrl, customerGetByIdUrl));
                urls.put("POST", List.of(customerSaveUrl));
                urls.put("PUT", List.of(customerUpdateUrl));
                urls.put("DELETE", List.of(customerDeleteUrl));
                break;
            case "order":
                urls.put("GET", List.of(orderGetAllUrl, orderGetByIdUrl, orderGetByCustomerIdUrl));
                urls.put("POST", List.of(orderCreateUrl));
                urls.put("PUT", List.of(orderUpdateUrl));
                urls.put("DELETE", List.of(orderDeleteUrl));
                break;
        }
        return urls;
    }

    public List<String> getUrlsForAServiceAndMethod(String serviceName, String method){
        return getUrlsForAService(serviceName).get(method);
    }

    public String getServiceName(String serviceName){
        return switch (serviceName) {
            case "product" -> productServiceName;
            case "customer" -> customerServiceName;
            case "order" -> orderServiceName;
            default -> null;
        };
    }

    public String getUrlWithInsertedParams(String url, Map<String,String> params){
        for (Map.Entry<String,String> entry : params.entrySet()){
            url = url.replace("{"+entry.getKey()+"}", entry.getValue());
        }
        return url;
    }
}
