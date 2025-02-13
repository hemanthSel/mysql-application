FROM openjdk:17-jdk-slim
WORKDIR /app
ENV PORT 6655
COPY target/ncpl-devops-one.jar /app/ncpl-devops-one.jar
EXPOSE 6655
ENTRYPOINT ["java", "-jar", "/app/ncpl-devops-one.jar"]