FROM openjdk:21
EXPOSE 8080

ADD target/bank-0.0.1-SNAPSHOT.jar bank-app.jar

ENTRYPOINT ["java" , "-jar", "bank-app.jar"]