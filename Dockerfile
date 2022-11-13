FROM maven:3-openjdk-18 AS builder

#create a working directory
WORKDIR /app

#copy the files into the container
COPY pom.xml .
COPY mvnw .
COPY  mvnw.cmd .
COPY src src

#build and skip tests
RUN mvn package -Dmaven.test.skip=true

#copying and building all of this into a jar file

#creating another container
FROM openjdk:18-oracle

WORKDIR /app

COPY --from=builder /app/target/finalprojectbackend-0.0.1-SNAPSHOT.jar finalprojectbackend.jar

#define the environment variables
ENV SERVER_PORT=8080

EXPOSE $SERVER_PORT

ENTRYPOINT ["java", "-jar", "finalprojectbackend.jar"]