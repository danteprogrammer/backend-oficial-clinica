# ETAPA 1: Construcción con Maven y Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ETAPA 2: Creación de la imagen final ligera con Java 21
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (Puerto estándar de Spring Boot)
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java","-jar","app.jar"]