FROM openjdk:8-jdk-alpine

LABEL author=armando.ramirez.vila@gmail.com

COPY price-batch-service/target/*.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]