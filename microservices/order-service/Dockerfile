FROM openjdk:latest
EXPOSE 9092
ADD target/order-service-1.0.jar order-service
ENTRYPOINT ["java","-jar","/order-service"]
