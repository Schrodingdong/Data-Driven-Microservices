# Ontological Discovery Service
## Possible Urls
- localhost:8080
- localhost:8080/?productId=69
- localhost:8080/?customerId=45
- localhost:8080/?orderId=150

## Possible Methods
- GET
- POST
- PUT
- DELETE

## Possbile Bodies
### Product
For saving a product : 
```json
{
    "productName": "Product Name",
    "productDescription": "Product Description",
    "productPrice": 69.69
}
```
For updating a product (example) :
```json
{
  "productId": 1,
  "productName": "New product Name"
}
```
### Customer
For saving a customer :
```json
{
    "customerFirstname": "Customer First Name",
    "customerLastname": "Customer Last Name",
    "customerEmail": "Customer Email"
}
```
For updating a customer (example) :
```json
{
  "customerId": 1,
  "customerFirstname": "New Customer First Name"
}
```
### Order
For saving an order :
```json
{
    "customerId": 1,
    "orderProducts": [
        {
            "productId": 1,
            "quantity": 53
        },
        {
            "productId": 2,
            "quantity": 12
        }
    ]
}
```
For updating an order :
```json
{
    "orderId": 1,
    "orderProducts": [
        {
            "productId": 1,
            "quantity": 53
        },
        {
            "productId": 2,
            "quantity": 12
        }
    ]
}
```

## Example combinations
NOTE that the following examples are not exhaustive, they are just here to give you an idea of how the service works.

The URLs shall be listed in the application.yaml file, following this structure for each service ontologically discovered
````
serviceName :
    Method :
        GET:
        - url1
        ...
        POST:
        - url1
        ...
        PUT:
        - url1
        ...
        DELETE:
        - url1
        ...
````
### Example 1
Consider the following request :
````http request
POST http://localhost:8080/
Content-Type: application/json

{
    "productName": "Product Name",
    "productDescription": "Product Description",
    "productPrice": 69.69
}
````
This SHOULD create a new product with the given information and will return the following response :
````http request
HTTP/1.1 200
Content-Type: application/json

{
    "productId": 1,
    "productName": "Product Name",
    "productDescription": "Product Description",
    "productPrice": 69.69
}
````
Workflow :
- The request is received by the server
- Get the path variable (here we have none) and the body (product)
- **Ontological analysis of the body**
  - compare with each ontology
  - find the best match
  - return the service name (in this case, it's the product service)
- **Url generation**
  - Analysis of the :
    - Method (POST)
    - Path variable (none)
    - service name (product service)
  - Generate the url (http://localhost:8082/product/save)

### Example 2
Consider the following request :
````http request
PUT http://localhost:8080/
Content-Type: application/json

{
    "productId": 1,
    "productName": "New product Name"
}
````
This SHOULD update the product with the given information and will return the following response :
````http request
HTTP/1.1 200
Content-Type: application/json

{
    "productId": 1,
    "productName": "New product Name",
    "productDescription": "Product Description",
    "productPrice": 69.69
}
````
Workflow :
- The request is received by the server
- Get the path variable (here we have none) and the body (product)
- **Ontological analysis of the body**
    - compare with each ontology
    - find the best match
    - return the service name (in this case, it's the product service)
- **Url generation**
    - Analysis of the :
        - Method (PUT)
        - Path variable (none)
        - service name (product service)
    - Generate the url (http://localhost:8082/product/update)

### Example 3
Consider the following request :
````http request
GET http://localhost:8080/?productId=1
````
This SHOULD return the product with the given id and will return the following response :
````http request
HTTP/1.1 200
Content-Type: application/json

{
    "productId": 1,
    "productName": "product Name",
    "productDescription": "Product Description",
    "productPrice": 69.69
}
````
Workflow :
- The request is received by the server
- Get the path variable (productId) and the body (none)
- **Ontological analysis of the path variable**
    - compare with each ontology
    - find the best match
    - return the service name (in this case, it's the product service)
- **Url generation**
    - Analysis of the :
        - Method (GET)
        - Path variable (productId)
        - service name (product service)
    - Generate the url (http://localhost:8082/product/get/1)