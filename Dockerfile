# Fase 1: Build del progetto Java
FROM eclipse-temurin:23-jdk-alpine as builder

# Imposta la directory di lavoro per la fase di build
WORKDIR /app

# Copia i file di configurazione del Gradle Wrapper
COPY gradlew .
COPY gradle gradle

# Assicurati che gradlew sia eseguibile
RUN chmod +x gradlew

# Copia i file di configurazione del progetto Gradle
COPY build.gradle settings.gradle ./

# Scarica le dipendenze del progetto (Gradle cache)
RUN ./gradlew dependencies --no-daemon

# Copia tutto il progetto nella directory di lavoro
COPY . .

# Compila il progetto e costruisce il file JAR
RUN ./gradlew bootJar --no-daemon

# Fase 2: Creazione dell'immagine finale e deploy
FROM eclipse-temurin:23-jdk-alpine

# Imposta la directory di lavoro per l'immagine finale
WORKDIR /app

# Copia il file JAR generato dalla fase di build nell'immagine finale
COPY --from=builder /app/build/libs/ClasseMorta.jar app.jar

# Copia lo script wait-for-it.sh nel container
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Esporta la porta su cui gira il servizio
EXPOSE 8080

# Esegui lo script wait-for-it.sh per attendere MySQL, quindi avvia Spring Boot
ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--", "java", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]