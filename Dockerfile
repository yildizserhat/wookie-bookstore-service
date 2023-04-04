FROM openjdk:17
EXPOSE 8080
ARG JAR_FILE=target/wookie-bookstore-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} wookie-bookstore-service.jar
ENTRYPOINT ["java","-jar","/wookie-bookstore-service.jar"]