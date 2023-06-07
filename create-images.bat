docker build -t customer-service ./microservices/customer-service
docker build -t product-service ./microservices/product-service
docker build -t order-service ./microservices/order-service

docker build -t gateway-service ./microservices/gateway-service
docker build -t inner-gateway-service ./microservices/inner-gateway-service

docker build -t discovery-service ./microservices/discovery-service
docker build -t ontological-discovery-service ./microservices/ontological-discovery-service
