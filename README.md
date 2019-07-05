# spring-mongodb-poc

PoC that loads data into MongoDB via a Spring Batch job, and allows several queries on this data.

## Producing data

** Retrieving the ids of JobExecutions running on this server:**

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/monitoring/jobs/runningexecutions
```

** Retrieving the JobExecution:**

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/monitoring/jobs/executions/{executionId}
```

** Starting the loadPrices job:**

```bash
curl -H "Accept: application/json" -X POST http://localhost:8080/batch/operations/jobs/loadPrices -d "jobParameters=dataFile=sample-data-2.csv"
```

** Stopping a job:**

```bash
curl -H "Accept: application/json" -X DELETE http://localhost:8080/batch/operations/jobs/executions/{executionId}
```

** Retrieving a JobExecution's ExitCode:**

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/operations/jobs/executions/{executionId}
```

### Build

[![Build Status](https://secure.travis-ci.org/armandorvila/cloud-native-accounts.png)](http://travis-ci.org/armandorvila/cloud-native-accounts) [![codecov.io](https://codecov.io/github/armandorvila/cloud-native-accounts/coverage.svg)](https://codecov.io/github/armandorvila/cloud-native-accounts) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/62c434b415f444e48bbed29f83b57a1f)](https://www.codacy.com/app/armandorvila/cloud-native-accounts?utm_source=github.com&utm_medium=referral&utm_content=armandorvila/cloud-native-accounts&utm_campaign=Badge_Grade)
