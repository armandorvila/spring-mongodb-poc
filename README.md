# spring-mongodb-poc

PoC that loads data into MongoDB via a Spring Batch job, and allows several queries on this data.

## Build and Run

The source code is structured as a Maven multi project with the following modules:

- price-batch-service: Spring Batch service that offers a REST interface to lunch, cancel and query jobs that load data in the Mongo DB database.
- price-service: Spring Webflux REST API that offers endpoints to query prices and batch runs.

To build and run the entire project you can run the following commands:

```bash
$ git clone https://github.com/armandorvila/spring-mongodb-poc
$ cd spring-mongodb-poc
$ mvn clean install
$ docker-compose up --build
```

That will generate one JAR file for each service, it will build two fresh docker images for each one of them, and will run those two docker images along with a mongodb db.

Once the docker compose is up, you can consume the endpoints explained in the next section:

## Endpoints

The following table sums up the system endpoints, full examples based on curl can be found in the next section:

| Endpoint                                          | Method | Description                                                                                                                                                      |
| ------------------------------------------------- | ------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `/batch/operations/jobs/loadPrices`               | POST   | Creates an asynchronous job execution for the loadPrices Spring Batch Job. Each will lunch a new execution, in background returning the execution id of the job. |
| `/batch/monitoring/jobs/runningexecutions`        | GET    | Retrieves the list of job executions currently running.                                                                                                          |
| `/batch/monitoring/jobs/executions/{executionId}` | GET    | Retrieves the information for a given execution ID.                                                                                                              |
| `/batch/operations/jobs/executions/{executionId}` | DELETE | Stops a running execution.                                                                                                                                       |
| `/prices?instrumentId={instrumentId}`             | GET    | Retrieves all the prices in the DB by instrumentId, enforcing offset/size pagination.                                                                            |
| `/batches`                                        | GET    | Retrieves all the batch runs in the DB enforcing offset/size pagination.                                                                                         |
| `/prices/last?instrumentId={instrumentId}`        | GET    | Get the last price for the givenInstrumentId, if no instrumentId is provided a 400 error is returned. If the instrumentId doesn't exist a 404 error is returned. |

## Examples

**Starting a Job**:

```bash
$ curl -H "Accept: application/json" -X POST http://localhost:8080/api/operations/jobs/loadPrices -d "jobParameters=dataFile=sample-data-2.csv"
```

**Retrieving a JobExecution**:

```bash
$ curl -H "Accept: application/json" http://localhost:8080/api/monitoring/jobs/executions/{executionId}
```

**Stopping a JobExecution**:

```bash
$ curl -H "Accept: application/json" -X DELETE http://localhost:8080/api/operations/jobs/executions/{executionId}
```

**Listing prices (default offset:0, default limit 100)**:

```bash
$ curl -H "Accept: application/json" http://localhost:8000/api/prices
```

**Listing batch-runs**:

```bash
$ curl -H "Accept: application/json" http://localhost:8000/api/batches
```

**Getting the last price for an instrument ID**:

```bash
$ curl -H "Accept: application/json" http://localhost:8000/api/prices/last?instrumentId=7f35ef04-4a7b-4934-9523-25a78def8cf1
```

## Build

[![Build Status](https://secure.travis-ci.org/armandorvila/spring-mongodb-poc.png)](http://travis-ci.org/armandorvila/spring-mongodb-poc) [![codecov.io](https://codecov.io/github/armandorvila/spring-mongodb-poc/coverage.svg)](https://codecov.io/github/armandorvila/spring-mongodb-poc) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/62c434b415f444e48bbed29f83b57a1f)](https://www.codacy.com/app/armandorvila/spring-mongodb-poc?utm_source=github.com&utm_medium=referral&utm_content=armandorvila/spring-mongodb-poc&utm_campaign=Badge_Grade)
