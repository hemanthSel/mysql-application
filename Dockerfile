FROM openjdk:17-jdk-slim
WORKDIR /app
ENV PORT 6655
COPY target/devops-one.jar /app/devops-one.jar
EXPOSE 6655
ENTRYPOINT ["java", "-jar", "/app/devops-one.jar"]
