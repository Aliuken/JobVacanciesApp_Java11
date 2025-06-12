# Layers that will never be altered (so they are cached after the first call)
# ---------------------------------------------------------------------------

FROM amazoncorretto:11-alpine-jdk
USER root

RUN mkdir /opt/apps
RUN mkdir /opt/apps/jobVacanciesApp

RUN addgroup -S jobVacanciesAppGroup && adduser -S jobVacanciesAppUser -G jobVacanciesAppGroup
USER jobVacanciesAppUser:jobVacanciesAppGroup

EXPOSE 8080

# Layers that might be altered (ordered from heaviest to lightest)
# ----------------------------------------------------------------

ARG JAR_FILE=./lib/*.jar
COPY ${JAR_FILE} /opt/apps/jobVacanciesApp/jobVacanciesApp.jar

ARG PRINT_ARG=default_value
RUN echo "PRINT_ARG: ${PRINT_ARG}"

ENTRYPOINT ["java", "-jar", "/opt/apps/jobVacanciesApp/jobVacanciesApp.jar"]
