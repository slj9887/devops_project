# ===== build stage =====
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -e -DskipTests clean package spring-boot:repackage

# ===== runtime stage =====
FROM eclipse-temurin:17-jre
WORKDIR /app
ENV JAVA_OPTS=""
EXPOSE 8080
COPY --from=build /src/target/*.jar app.jar
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
