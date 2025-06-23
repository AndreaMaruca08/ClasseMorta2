# Usa Java 21 su architettura amd64
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

# Scarica e configura Gradle
ARG GRADLE_VERSION=8.4
RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip gradle-${GRADLE_VERSION}-bin.zip -d /opt && \
    rm gradle-${GRADLE_VERSION}-bin.zip && \
    ln -s /opt/gradle-${GRADLE_VERSION}/bin/gradle /usr/bin/gradle

# Copia il progetto e compila
COPY . .

# Esegui la build senza specificare esplicitamente org.gradle.java.home
RUN gradle dependencies --no-daemon && gradle bootJar --no-daemon && ls -l /app/build/libs/
# Usa il runtime Java 21 per eseguire l'applicazione
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/ClasseMorta.jar app.jar

# Aggiungi wait-for.sh per PostgreSQL
ADD https://raw.githubusercontent.com/eficode/wait-for/master/wait-for /wait-for.sh
RUN chmod +x /wait-for.sh

# Espone la porta 8080
EXPOSE 8080

# Esegui il container con wait-for e un profilo Docker
ENTRYPOINT ["/bin/sh", "/wait-for.sh", "postgres:5432", "--", "java", "--enable-preview", "-Dspring.profiles.active=docker", "-jar", "/app/app.jar"]