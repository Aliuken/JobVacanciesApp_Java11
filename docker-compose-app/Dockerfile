FROM amazoncorretto:11-alpine-jdk
USER root

RUN mkdir /opt/apps
RUN mkdir /opt/apps/jobVacanciesApp

ARG JAR_FILE=../lib/*.jar
COPY ${JAR_FILE} /opt/apps/jobVacanciesApp/jobVacanciesApp.jar

RUN addgroup -S jobVacanciesAppGroup && adduser -S jobVacanciesAppUser -G jobVacanciesAppGroup
USER jobVacanciesAppUser:jobVacanciesAppGroup

CMD ["java", "-jar", "/opt/apps/jobVacanciesApp/jobVacanciesApp.jar"]