FROM openjdk:latest
EXPOSE 9091
ADD target/customer-service-1.0.jar customer-service
ENTRYPOINT ["java","-jar","/customer-service"]