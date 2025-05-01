# Use a JDK base image
FROM gradle:8.4-jdk21 as builder
WORKDIR /app
COPY . .
RUN ./gradlew kobwebBuild kobwebExport

# Use minimal JRE to run the app
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/site/build/kobweb-export ./
EXPOSE 8080
CMD ["java", "-jar", "kobweb-server.jar"]
