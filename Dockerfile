FROM eclipse-temurin:21-jdk as build
WORKDIR /app
COPY . .
RUN ./gradlew.bat clean bootJar

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE ${PORT:-8080}
ENTRYPOINT ["java", "-jar", "app.jar"]
