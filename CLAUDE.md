# CLAUDE.md

This file was created on 2025-11-25.

## Project Summary: Nanna

**Nanna** is a **Provider Registry application** for Entur (Norwegian public transport) that manages provider information for the Ninkasi system.

### Key Features
- Provider registry management with Chouette integration (transit data exchange)
- Security via OAuth2 and permission-based authorization
- RESTful API with Swagger documentation
- Multi-mode transport support
- User context management
- Health checks & Prometheus metrics

### Development
- Main class: `no.entur.nanna.nanna.App`
- Default port: 11102
- Uses Prettier for code formatting
- OWASP dependency vulnerability scanning
- Licensed under EUPL 1.2

### Build and Run

To run the app locally you will need to have a running PostgreSQL server in your environment, for example through docker. 
You will also need to setup an application.properties file and provide its path as a VM option when running the app, 
for example like this:

`-Dspring.config.location=/Users/my-user/config/nanna/application.properties -Dfile.encoding=UTF-8`

To run the app locally, run the main method of `no.entur.nanna.nanna.App` in the IDE of your choice.

Building:
```bash
mvn clean install
```

### Docker image
The `Dockerfile` extracts the Spring Boot layers and runs them on
`gcr.io/distroless/java25-debian13:nonroot`, in both stages: Temurin JRE, no shell, no package manager.
The builder's `RUN` is therefore exec form. `nonroot` carries no version, so it is pinned to a digest
that Dependabot moves. With no shell to expand a
variable, JVM flags come from `JDK_JAVA_OPTIONS` in `helm/nanna/templates/deployment.yaml`, which the
launcher reads itself and announces on stderr. The runtime JRE has no shell, `jcmd` or `jstack`. To
look inside locally, swap the runtime `FROM` for `gcr.io/distroless/java25-debian13:debug-nonroot` (no
digest) and run with `--entrypoint /busybox/sh`. In the cluster, `kubectl debug -it <pod>
--image=busybox --target=nanna` shares the JVM's process namespace, and `kill -3 1` writes a thread
dump to the pod log.

### Purpose

This is an enterprise-grade microservice for managing transportation provider metadata in Entur's ecosystem.

understand 
