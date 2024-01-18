FROM openjdk:21-slim AS build

RUN apt-get update -y
RUN apt-get install open-jdk-21-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build target/bank-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java" , "-jar" , "app.jar"]