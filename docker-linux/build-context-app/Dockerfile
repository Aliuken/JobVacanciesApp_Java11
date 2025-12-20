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

ARG ORIG_JAR_FILE_PATH=./lib/*.jar
ENV DEST_JAR_FILE_PATH=/opt/apps/jobVacanciesApp/jobVacanciesApp.jar

# Copy the host jar file to the container jar file
COPY ${ORIG_JAR_FILE_PATH} ${DEST_JAR_FILE_PATH}

ARG PRINT_ARG=default_value
RUN echo "PRINT_ARG: ${PRINT_ARG}"

# To work in the ENTRYPOINT, the DEST_JAR_FILE_PATH variable needs to be an ENV
ENTRYPOINT ["/bin/sh", "-c", "java -jar ${DEST_JAR_FILE_PATH}"]
