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

# Esporta la porta su cui gira il servizio
EXPOSE 8080

# Esegui l'applicazione con il profilo docker
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=docker"]