# spring-mongodb-poc

PoC that loads data into MongoDB via a Spring Batch job, and allows several queries on this data.

## Load price data in batches

This section explains how to load price data via offline processes configured to upload chunks of 10K records.

### Starting the loadPrices job:

```bash
curl -H "Accept: application/json" -X POST http://localhost:8080/batch/operations/jobs/loadPrices -d "jobParameters=dataFile=sample-data-2.csv"
```

### Retrieving the ids of JobExecutions running on this server:

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/monitoring/jobs/runningexecutions
```

### Retrieving the JobExecution:

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/monitoring/jobs/executions/{executionId}
```

### Retrieving the JobExecution's ExitCode:

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/operations/jobs/executions/{executionId}
```

### Stopping a JobExecution:

```bash
curl -H "Accept: application/json" -X DELETE http://localhost:8080/batch/operations/jobs/executions/{executionId}
```

### Build

[![Build Status](https://secure.travis-ci.org/armandorvila/spring-mongodb-poc.png)](http://travis-ci.org/armandorvila/spring-mongodb-poc) [![codecov.io](https://codecov.io/github/armandorvila/spring-mongodb-poc/coverage.svg)](https://codecov.io/github/armandorvila/spring-mongodb-poc) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/62c434b415f444e48bbed29f83b57a1f)](https://www.codacy.com/app/armandorvila/spring-mongodb-poc?utm_source=github.com&utm_medium=referral&utm_content=armandorvila/spring-mongodb-poc&utm_campaign=Badge_Grade)
