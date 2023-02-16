FROM openjdk:19
COPY target/release-tracker-neon-task-0.0.1-SNAPSHOT.jar release-tracker.jar

ENTRYPOINT ["java", "-jar", "/release-tracker.jar"]