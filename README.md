# query-sandbox

A sandbox for gaining a better understanding of query optimization

## Local Testing

```sh
# run locally
./mvnw install && ./mvnw spring-boot:run -pl query-sandbox-api

# or
make run

# test basic endpoint
curl 'http://localhost:8080/api/test'
```

Swagger:

- http://localhost:8080/api-docs
- http://localhost:8080/swagger-ui/index.html

## References

- https://spring.io/guides/gs/multi-module
