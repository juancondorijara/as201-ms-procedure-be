FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/ms-procedure-0.0.1-SNAPSHOT.jar app.jar
ENV DB_URL ${DB_URL}
ENV DB_USERNAME ${DB_USERNAME}
ENV DB_PASSWORD ${DB_PASSWORD}
ENV PROFILE ${PROFILE}

ENV MS_DOCUMENTS_ATTACHMENTS_URL ${MS_DOCUMENTS_ATTACHMENTS_URL}
ENV MS_PERSON_URL ${MS_PERSON_URL}
ENV MS_STUDENT_URL ${MS_STUDENT_URL}
EXPOSE 8093
ENTRYPOINT ["java", "-Dspring.profiles.active=$PROFILE", "-Duser.timezone=GMT-5", "-jar", "app.jar"]