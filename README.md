# spring-mongodb-poc

PoC that loads data into MongoDB via a Spring Batch job, and allows several queries on this data.

## Producing data

**_ Retrieving the ids of JobExecutions running on this server:_**

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/monitoring/jobs/runningexecutions
```

**_ Retrieving the JobExecution:_**

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/monitoring/jobs/executions/{executionId}
```

**_ Starting the loadPrices job:_**

```bash
curl -H "Accept: application/json" -X POST http://localhost:8080/batch/operations/jobs/loadPrices -d "jobParameters=dataFile=sample-data-2.csv"
```

**_ Stopping a job:_**

```bash
curl -H "Accept: application/json" -X DELETE http://localhost:8080/batch/operations/jobs/executions/{executionId}
```

**_ Retrieving a JobExecution's ExitCode:_**

```bash
curl -H "Accept: application/json" http://localhost:8080/batch/operations/jobs/executions/{executionId}
```
