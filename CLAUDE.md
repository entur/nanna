# CLAUDE.md

This file was created on 2025-11-25.

## Project Summary: Nanna

**Nanna** is a **Provider Registry application** for Entur (Norwegian public transport) that manages provider information for the Ninkasi system.

### Tech Stack
- **Java 21** with **Spring Boot** (web application)
- **PostgreSQL** database with Flyway migrations
- **Jersey** (JAX-RS) for REST APIs
- **Hibernate/JPA** for ORM with spatial support
- **Maven** build system
- **Docker** containerization with Liberica OpenJRE Alpine

### Key Features
- Provider registry management with Chouette integration (transit data exchange)
- Security via OAuth2 and permission-based authorization
- RESTful API with Swagger documentation
- Multi-mode transport support
- User context management
- Health checks & Prometheus metrics

### Architecture
- Spring Boot application with Jersey REST endpoints
- JPA repositories for data access
- Database migrations using Flyway (common + dev environments)
- GCP-ready (Google Cloud SQL PostgreSQL support)
- Kubernetes deployment via Helm charts
- Terraform infrastructure

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

### Purpose

This is an enterprise-grade microservice for managing transportation provider metadata in Entur's ecosystem.

## Recent Changes

### OAuth2 Multi-Audience Support (2025-11-25)

Refactored OAuth2 configuration to support multiple audiences following the same pattern as kilili:

**Changes Made:**
- Updated `OAuth2Config.java` to use plural audience methods (`withEnturInternalAuth0Audiences`, `withEnturPartnerAuth0Audiences`)
- Added helper methods `parseAudiences()` and `parseFirstAudience()` to parse comma-separated audience values
- Updated helm configuration templates to use separate audience values for each tenant
- Modified environment-specific values files (dev/tst/prd) to include separate audience configurations

**Configuration:**
- Entur Internal and Entur Partner tenants now support multiple comma-separated audiences
- RoR tenant uses the first audience from the list (limitation in oauth2-helpers v5.50.0)
- Audience values are configured separately per tenant in helm values files

**Helm Configuration Example:**
```yaml
auth0:
  entur:
    internal:
      url: https://internal.dev.entur.org/
      audience: https://api.dev.entur.io
    partner:
      url: https://partner.dev.entur.org/
      audience: https://api.dev.entur.io
```

**Files Modified:**
- `src/main/java/no/entur/nanna/nanna/config/OAuth2Config.java`
- `helm/nanna/templates/configmap.yaml`
- `helm/nanna/env/values-kub-ent-dev.yaml`
- `helm/nanna/env/values-kub-ent-tst.yaml`
- `helm/nanna/env/values-kub-ent-prd.yaml`
understand 