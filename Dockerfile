# -------- BUILD STAGE --------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copy pom.xml first to leverage Docker cache
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline

# Copy source code and build the application
COPY src ./src
RUN mvn -q -DskipTests clean package

# -------- RUNTIME STAGE --------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/target/*.jar /app/app.jar

# Create data folder and initialize products file if missing
RUN mkdir -p /app/data && \
    if [ ! -f /app/data/products.json ]; then echo "{}" > /app/data/products.json; fi

EXPOSE 8080

RUN mkdir -p /app/logs && chmod 777 /app/logs

# Start the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
