# JobVacanciesApp

## 1. About JobVacanciesApp

**JobVacanciesApp** is an application made with **OpenJDK 11** and **Spring Boot 2.7.1** to learn how to made a web site by using all the state-of-the-art Spring-related technologies.

## 2. Execution procedure

To run the application:
* **Execute in a terminal:** ./docker-compose-start.sh
* **Open in a web browser:** <http://localhost:9090>

To stop the application:
* **Press in the terminal previously opened:** Ctrl + C
* **Execute in the terminal:** ./docker-compose-stop.sh

## 3. Explanation of docker-compose.yaml

In the docker-compose.yaml file:
* **./src/main/resources/META-INF/db_dumps_folder** contains the database dump file: **db-dump.sql**.
* **/AppData** is the folder in my PC (which is Linux) that has the images and documents used in the application.
* **healthcheck** and **service_healthy** are used joint to determine when the **db-dump.sql** file was executed, to start the Spring Boot application after that.
* **internal-net** is used to communicate the Spring Boot application with the database.
* **external-net** is used to communicate the Spring Boot application with the user.