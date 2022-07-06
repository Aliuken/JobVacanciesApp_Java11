# JobVacanciesApp

## 1. About JobVacanciesApp

**JobVacanciesApp** is an application made with **OpenJDK 11** and **Spring Boot** to learn how to made a web site by using all the state-of-the-art Spring-related technologies.

The technologies currently used are:
* **OpenJDK 11**
* **Spring Boot 2.7.1** (using **Spring Security**, **Spring MVC** and **Spring Data JPA**)
* **Thymeleaf** (as the HTML5 template engine)
* **Lombok** (to generate the model objects and for logging)
* The latest version of **MySQL Community Server** (currently 8.0.29)
* **Docker** (with **Dockerfile** and **Docker Compose**)
* **Bootstrap 5.1.3**, **Material Design for Bootstrap 4.2.0** and **jQuery UI 1.13.1** for the look-and-feel.
* **Font Awesome 6.1.1** for the icons.
* **jQuery 3.6.0** to make an easier use of **JavaScript**.
* **jQuery Timepicker Addon 1.6.3** for the calendar element.
* **TinyMCE Community 6.1.0** for the rich text editor.

This repository makes use of the **company logo files** and **Curriculum Vitae files** that are stored in the following repository: <https://github.com/Aliuken/JobVacanciesApp_AppData>. Those files are external to the project.

## 2. Execution procedure

To run the application with an IDE (I recommend using **Spring Tool Suite**, which is based on **Eclipse**):
* **Run the application:** As a Spring Boot application
* **Open in a web browser:** <http://localhost:8080>

To run the application with Docker Compose:
* **Execute in a terminal:** ./docker-compose-start.sh
* **Open in a web browser:** <http://localhost:9090>

To stop the application with Docker Compose:
* **Press in the terminal previously opened:** Ctrl + C
* **Execute in the terminal:** ./docker-compose-stop.sh and, optionally, ./Dockerfile-stop.sh

## 3. Explanation of docker-compose.yaml

In the docker-compose.yaml file:
* **./src/main/resources/META-INF/db_dumps_folder** contains the database dump file: **db-dump.sql**.
* **/AppData** is the folder in my PC (which is Linux) that has the **company logo files** and **Curriculum Vitae files** used in the application.
* **healthcheck** and **service_healthy** are used joint to determine when the **db-dump.sql** file was executed, to start the **Spring Boot application** after that.
* **internal-net** is used to communicate the Spring Boot application with the database.
* **external-net** is used to communicate the Spring Boot application with the user.