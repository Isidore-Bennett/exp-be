Tests are failing, shocking. I did a rewrite of some key functionality so a lot is broken. So please ./gradlew clean build -x test

Java 8

Postgres 15.1 (please create an experian schema, sorry. If you already have another schema and prefer to use that one, please
change "spring.datasource.url" in [application.properties](src%2Fmain%2Fresources%2Fapplication.properties) from what's there to your url and change the db credentials)

Tried the docker thing, but I can't seem to get it working

swagger on http://localhost:{your port}/swagger-ui/#/ --8080 by default

For now, the tables are deleted and rebuilt on rerun, change "spring.jpa.hibernate.ddl-auto" 
in [application.properties](src%2Fmain%2Fresources%2Fapplication.properties) to update from create if you wish to keep
data between reruns