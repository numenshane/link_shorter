FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","-redis_ip=redis.link-shorter","-redis_port=6379","-key=AKIASUOSM3BFNIUYH7HB","-secret=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"]
