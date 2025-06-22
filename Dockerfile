# Utilizza un'immagine base ufficiale di Java 23 JDK
FROM eclipse-temurin:23-jdk-alpine

# Imposta la directory di lavoro
WORKDIR /app

# Copia il file JAR generato, assicurandoti che sia quello corretto
COPY build/libs/ClasseMorta.jar app.jar

# Esporta la porta su cui gira il servizio
EXPOSE 8080

# Comando per avviare l'applicazione
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
CMD []
