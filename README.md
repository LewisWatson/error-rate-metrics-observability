# Error Rate Demo

Proof of concept demo for metric observability and alerting on errors in a
[Spring Boot](https://spring.io/projects/spring-boot) service using
[Prometheus](https://prometheus.io), and [Grafana](https://grafana.com/).


## Requirements

- [JDK 11](https://docs.oracle.com/en/java/javase/11/)
- [Docker](https://www.docker.com/)

## Build

First build the spring boot application.

```bash
./gradlew build
```

Then build the docker images

```bash
docker compose build
```

## Run

```bash
docker compose up
```

After all services have started successfully, you can navigate to the following
URLs:

- [error-rate-demo Spring Boot service Prometheus endpoint](http://localhost:8080/actuator/prometheus)
- [Prometheus](http://localhost:9090/)
  - See the [pre-configured alerts](monitoring/prometheus/alerts.yml)
- [Grafana](http://localhost:3000/)
  - Username: `admin`
  - Password: `password`
  - See the pre-configured [Error Rate Demo dashboard](monitoring/grafana/provisioning/dashboards/error-rate-demo.dashboard.json)

You can then run a simulation via a HTTP POST request

```bash
curl --location --request POST 'http://localhost:8080/simulate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "iterations": 1000,
    "happyPathOdds": 1,
    "validationErrorOdds": 0.2,
    "slowCatRepoUpdateOdds": 0.1,
    "callToXServiceFailedOdds": 0.09,
    "callToXServiceFailedPersistentlyOdds": 0.01
}'
```

If you prefer to use postman there is a collection in the [src/test/postman](src/test/postman) directory.

## Example output after a simulation is run

### Error Rate Demo prometheus endpoint

```plain
# HELP error_total  
# TYPE error_total counter
error_total{cause="slow cat repo update",persistent="n",} 87.0
error_total{cause="call to x service failed",persistent="n",} 60.0
error_total{cause="validation error",persistent="n",} 147.0
error_total{cause="call to x service failed",persistent="y",} 7.0
```

### Pre-configured Prometheus Alert

![Prometheus Alert](docs/img/prometheus-alert.png)

### Pre-configured Grafana Dashboard

![Grafana Dashboard](docs/img/error-rate-dashboard.png)
