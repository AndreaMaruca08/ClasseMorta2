# Fase 1: Build del progetto Java
FROM eclipse-temurin:23-jdk-alpine as builder

# Imposta la directory di lavoro per la fase di build
WORKDIR /app

# Copia i file necessari per il build
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies --no-daemon
COPY . .
RUN ./gradlew bootJar --no-daemon

# Fase 2: Creazione dell'immagine finale e deploy
FROM eclipse-temurin:23-jdk-alpine

# Imposta la directory di lavoro per l'immagine finale
WORKDIR /app

RUN apk add --no-cache netcat-openbsd

# Copia il file JAR generato dalla build
COPY --from=builder /app/build/libs/ClasseMorta.jar app.jar

# Copia lo script di attesa nel container
COPY wait-for-mysql.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "/wait-for-mysql.sh", "mysql", "3306", "java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]