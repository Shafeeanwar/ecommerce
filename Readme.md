1. Clone repository
2. Open intellij
3. start application
4. create product (TODO)
5. create order:

```
curl --location 'http://localhost:8080/api/orders' \
--header 'Content-Type: application/json' \
--data '{
    "items": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 2,
            "quantity": 1
        }
    ]
}'
```

6. Fetch order

```
curl --location 'http://localhost:8080/api/orders/{id}'
```
