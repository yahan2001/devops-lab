# Root Cause Analysis – Docker Security Gate Failure

## 1. Incident Summary
GitHub Actions failed during the Trivy security scanning step.
The JUnit tests and Docker build completed successfully.

## 2. Evidence
- JUnit Test Gate: PASSED
- Docker Build: PASSED
- Docker Security Gate: FAILED
- Trivy detected four HIGH vulnerabilities.
- Affected binary: /usr/bin/pebble
- Affected dependency: Go standard library v1.27.1

## 3. Root Cause
The original Docker base image, eclipse-temurin:21-jre,
included the /usr/bin/pebble executable compiled with
a vulnerable version of the Go standard library.

## 4. Corrective Action
Changed the Docker base image from:
eclipse-temurin:21-jre

to:
eclipse-temurin:21-jre-noble

This selects an Ubuntu 24.04-based runtime image.

## 5. Verification
- Docker image rebuilt successfully.
- /usr/bin/pebble was no longer present.
- Local Trivy scan reported zero findings under the configured
  HIGH/CRITICAL, ignore-unfixed security policy.
- GitHub Actions must also be checked to verify the remote gate.

## 6. Prevention
- Review and update Docker base images regularly.
- Maintain automated security scanning in CI.
- Block image acceptance when security policy fails.
- Preserve CI logs and vulnerability reports.
- Re-scan after dependency and base image updates.

## 7. Conclusion
The incident was caused by vulnerable software inherited from
the Docker base image rather than the Java application code.
The updated base image passed the local security policy check.

## 8. Observability and Debugging Evidence

### Logs
Spring Boot application logs were collected from Docker Compose.
Evidence: evidence/app-logs.txt

### Metrics
Prometheus metrics are available at /actuator/prometheus.
Evidence: evidence/metrics.txt

### Distributed Tracing
Micrometer Tracing exports request traces to Zipkin.

Verified requests:
- GET /actuator/health
- GET /tasks
- POST /tasks

The Zipkin dashboard displays trace and span information,
including request processing duration.

### Debugging Method
1. Identify the failing stage in GitHub Actions.
2. Inspect the relevant error logs and Trivy findings.
3. Identify the vulnerable binary and its origin.
4. Replace the vulnerable Docker base image.
5. Rebuild, retest and rescan the application.
6. Verify logs, metrics and traces.
