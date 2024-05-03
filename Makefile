MVN = ./mvnw

clean:
	$(MVN) clean

build:	clean
	$(MVN) install

run:	build
	$(MVN) spring-boot:run -pl query-sandbox-api
