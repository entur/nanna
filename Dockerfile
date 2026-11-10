FROM bellsoft/liberica-openjre-alpine:27-36@sha256:e598f7ef012857bc8d679d245ccee4663906c338c37de19b83814cec85e7d263 AS builder
WORKDIR /builder
COPY target/*-SNAPSHOT.jar application.jar
RUN java -Djarmode=tools  -jar application.jar extract --layers --destination extracted

FROM bellsoft/liberica-openjre-alpine:27-36@sha256:e598f7ef012857bc8d679d245ccee4663906c338c37de19b83814cec85e7d263
RUN apk update && apk upgrade && apk add --no-cache tini
WORKDIR /deployments
RUN addgroup appuser && adduser --disabled-password appuser --ingroup appuser
USER appuser
COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./
ENTRYPOINT [ "/sbin/tini", "--", "java", "-jar", "application.jar" ]
