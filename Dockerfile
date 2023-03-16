FROM eclipse-temurin:17
MAINTAINER torfstack.de

RUN mkdir /opt/kayvault
COPY build/libs/kayvault*SNAPSHOT.jar /opt/kayvault/kayvault.jar
CMD ["java", "-jar", "/opt/kayvault/kayvault.jar"]

