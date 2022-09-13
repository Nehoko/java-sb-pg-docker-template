FROM maven:3.8.1-openjdk-17-slim

# image layer
WORKDIR /app
ADD pom.xml /app
RUN mvn verify clean --fail-never

# image layer: with the application
COPY . /app
RUN mvn -v
RUN mvn clean install -DskipTests
EXPOSE 8080
ARG JAR_FILE=./target/drone-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]