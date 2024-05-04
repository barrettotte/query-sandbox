# query-sandbox

A sandbox for messing around with various queries.

I made this to get rid of a bunch of "noise" when trying to optimize queries
tangled up in complex business logic.

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
