FROM bellsoft/liberica-openjdk-alpine:21.0.6-10
RUN apk update && apk upgrade && apk add --no-cache tini
WORKDIR /deployments
COPY target/nanna-*-SNAPSHOT.jar nanna.jar
RUN addgroup appuser && adduser --disabled-password appuser --ingroup appuser
USER appuser
CMD [ "/sbin/tini", "--", "java", "-jar", "nanna.jar" ]
