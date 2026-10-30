FROM bellsoft/liberica-openjre-alpine:25.0.5.0.3@sha256:6e37a10866f46514a2439295cecaec5666ab817bfcb961d2fc6bcb8001335b61 AS builder
WORKDIR /builder
COPY target/*-SNAPSHOT.jar application.jar
RUN java -Djarmode=tools  -jar application.jar extract --layers --destination extracted

FROM bellsoft/liberica-openjre-alpine:25.0.5.0.3@sha256:6e37a10866f46514a2439295cecaec5666ab817bfcb961d2fc6bcb8001335b61
RUN apk update && apk upgrade && apk add --no-cache tini
WORKDIR /deployments
RUN addgroup appuser && adduser --disabled-password appuser --ingroup appuser
USER appuser
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT [ "/sbin/tini", "--", "java", "-jar", "application.jar" ]
