FROM amazoncorretto:23-alpine AS build
WORKDIR /workspace/app

# Copia del progetto nella directory di lavoro
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Garantisce che il mvnw script sia eseguibile
RUN chmod +x ./mvnw

# Download delle dipendenze Maven
RUN ./mvnw dependency:go-offline -B

# Copia del codice sorgente
COPY src src

# Compila il progetto con skip dei test
RUN ./mvnw clean package -DskipTests

# Utilizza JRE per l'immagine di runtime
FROM amazoncorretto:23-alpine-jdk
VOLUME /tmp

# Copia il jar dall'immagine di build
COPY --from=build /workspace/app/target/*.jar app.jar

# Espone le porte necessarie
EXPOSE 8080
EXPOSE 9090

# Esegue l'applicazione
ENTRYPOINT ["java","-jar","/app.jar"]