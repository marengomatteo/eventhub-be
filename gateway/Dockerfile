FROM amazoncorretto:23-alpine AS build
WORKDIR /workspace/app

# Copia del progetto nella directory di lavoro
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Garantisce che il mvnw script sia eseguibile
RUN chmod +x ./mvnw
# Compila il jar
RUN ./mvnw install -DskipTests

# Utilizza JRE per l'immagine di runtime
FROM amazoncorretto:23-alpine-jdk
VOLUME /tmp

# Copia il jar dall'immagine di build
COPY --from=build /workspace/app/target/*.jar app.jar

# Espone la porta 8080
EXPOSE 8082

# Esegue l'applicazione
ENTRYPOINT ["java","-jar","/app.jar"]
