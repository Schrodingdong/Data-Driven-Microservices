FROM openjdk:latest
EXPOSE 9090
ADD target/product-service-1.0.jar product-service
ENTRYPOINT ["java","-jar","/product-service"]
