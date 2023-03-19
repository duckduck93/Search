FROM openjdk:17-alpine3.14

WORKDIR /app

EXPOSE 8080

ARG JAR_FILE=./build/libs/search-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} /app/search.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=development", "-Duser.timezone=Asia/Seoul", "-jar", "/app/search.jar"]