# Build stage
FROM amazoncorretto:23 AS build

# Installa Maven e protoc manualmente
RUN yum update -y && \
    yum install -y wget tar protobuf-compiler && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xzf apache-maven-3.9.6-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/bin/mvn && \
    yum clean all

WORKDIR /app
COPY . .
RUN chmod +x ./mvnw

# Usa Maven installato o mvnw
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM amazoncorretto:23-alpine-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8084
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]