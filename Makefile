MVN = ./mvnw
DOCKER = sudo docker

API = query-sandbox-api
DB = query-sandbox-postgres

POSTGRES_CONTAINER = $(shell $(DOCKER) container ls | grep $(DB) | head -c 12)
POSTGRES_USER = postgres
POSTGRES_DB = query_sandbox

ENV_VARS = $(shell cat .env | xargs)

local-clean:
	$(MVN) clean

local-build:	local-clean
	$(MVN) install

local-api:	local-build
	$(DOCKER) compose up postgres --force-recreate -d
	$(ENV_VARS) $(MVN) spring-boot:run -pl $(API)

compose-up-clean:
	$(DOCKER) compose build --no-cache
	$(DOCKER) compose up --force-recreate -d

compose-up:
	$(DOCKER) compose up -d --force-recreate

compose-down:
	$(DOCKER) compose down

.PHONY: psql
psql:
	@echo "Connecting to $(DB) ($(POSTGRES_CONTAINER)) as $(POSTGRES_USER)..."
	@$(DOCKER) exec -it $(POSTGRES_CONTAINER) psql -U $(POSTGRES_USER) -d $(POSTGRES_DB)
