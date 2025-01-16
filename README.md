# baba

Provider registry for ninkasi users

## Build and Run

To run the app locally you will need to have a running PostgreSQL server in your environment, for example through docker. 
You will also need to setup an application.properties file and provide its path as a VM option when running the app, 
for example like this:

`-Dspring.config.location=/Users/my-user/config/nanna/application.properties -Dfile.encoding=UTF-8`

To run the app locally, run the main method of `no.entur.nanna.nanna.App` in the IDE of your choice.

Building:
`mvn clean install`


### Example application.properties file for local development

```
spring.database.driverClassName=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/nanna
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.cloud.gcp.sql.enabled=false

server.port=11102
server.host=localhost
```
