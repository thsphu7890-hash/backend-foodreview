# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Tải dependency trước để tận dụng cache của Docker (Build nhanh hơn ở các lần sau)
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# 👇 SỬA QUAN TRỌNG:
# -Xmx384m: Giới hạn Heap 384MB (để lại ~128MB cho JVM overhead)
# -Dserver.port: Đảm bảo Spring Boot chạy đúng port Render cấp (Render dùng biến môi trường PORT)
ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-Dserver.port=${PORT}", "-jar", "app.jar"]