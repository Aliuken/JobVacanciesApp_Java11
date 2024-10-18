FROM amazoncorretto:11-alpine-jdk
USER root

RUN mkdir /opt/apps
RUN mkdir /opt/apps/jobVacanciesApp

RUN addgroup -S jobVacanciesAppGroup && adduser -S jobVacanciesAppUser -G jobVacanciesAppGroup
USER jobVacanciesAppUser:jobVacanciesAppGroup

EXPOSE 8080

ARG JAR_FILE=./lib/*.jar
ARG PRINT_ARG=default_value

COPY ${JAR_FILE} /opt/apps/jobVacanciesApp/jobVacanciesApp.jar
RUN echo "PRINT_ARG: ${PRINT_ARG}"

CMD ["java", "-jar", "/opt/apps/jobVacanciesApp/jobVacanciesApp.jar"]