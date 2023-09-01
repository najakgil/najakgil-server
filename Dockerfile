FROM openjdk:17-jdk-slim
ADD /build/libs/*.jar app.jar
EXPOSE 9005
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
