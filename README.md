# query-sandbox

A sandbox for messing around with various queries.

I made this to get rid of a bunch of "noise" when trying to optimize queries
tangled up in complex application logic.

## Summary

- Java 17 Springboot API
- Postgres 14.9

## Testing

```sh
# start all containers
make compose-up

# teardown all containers
make compose-down

# run API locally (postgres container and API using local java install)
make local-api

# launch postgres client for container
make psql

# Swagger: http://localhost:8080/swagger-ui/index.html
```

## References

- 
