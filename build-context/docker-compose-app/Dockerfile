FROM amazoncorretto:11-alpine-jdk
USER root

RUN mkdir /opt/apps
RUN mkdir /opt/apps/jobVacanciesApp

ARG PRINT_ARG=default_value
RUN echo "PRINT_ARG: ${PRINT_ARG}"

ARG JAR_FILE=./lib/*.jar
COPY ${JAR_FILE} /opt/apps/jobVacanciesApp/jobVacanciesApp.jar

RUN addgroup -S jobVacanciesAppGroup && adduser -S jobVacanciesAppUser -G jobVacanciesAppGroup
USER jobVacanciesAppUser:jobVacanciesAppGroup

EXPOSE 8080

CMD ["java", "-jar", "/opt/apps/jobVacanciesApp/jobVacanciesApp.jar"]