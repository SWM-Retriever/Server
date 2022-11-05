FROM openjdk:11-jdk

VOLUME /tmp

ARG JAR_FILE=./build/libs/*.jar

COPY ${JAR_FILE} dailypet.jar

# 운영
ENTRYPOINT ["nohup","java","-jar","-Dspring.profiles.active=prod","dailypet.jar","2>&1","&"]
