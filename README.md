# JobVacanciesApp

## 1. About JobVacanciesApp

**JobVacanciesApp** is an application made with **OpenJDK 11** and **Spring Boot** to learn how to made a web site by using all the state-of-the-art Spring-related technologies.

The technologies currently used are:
* **OpenJDK 11** as the Java implementation.
* **Spring Boot 2.7.1** (using **Spring Security**, **Spring MVC** and **Spring Data JPA**).
* **Maven** as the dependency manager and for building the application.
* **Thymeleaf** as the HTML5 template engine.
* **Apache Tomcat** as the web server.
* **Hibernate** as the ORM.
* The latest version of **MySQL Community Server** (currently 8.0.29) as the database.
* **JUnit 5** for unit testing.
* **Lombok** to generate the model objects and for logging.
* **Docker** (with **Dockerfile** and **Docker Compose**).
* **Bootstrap 5.1.3**, **Material Design for Bootstrap 4.2.0** and **jQuery UI 1.13.1** for the look-and-feel.
* **Font Awesome 6.1.1** for the icons.
* **jQuery 3.6.0** to make an easier use of **JavaScript**.
* **jQuery Timepicker Addon 1.6.3** for the calendar element.
* **TinyMCE Community 6.1.0** for the rich text editor.

This repository makes use of the **company logo files** and **Curriculum Vitae files** that are stored in the following repository: <https://github.com/Aliuken/JobVacanciesApp_AppData>. Those files are external to the project.

## 2. Execution procedure

To run the application with an IDE (I recommend using **Spring Tool Suite**, which is based on **Eclipse**):
* **Run the application:** as a Spring Boot application
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

## 4. Credentials to access the application

The database of the application comes with 7 predefined users with the following credentials and roles:

```txt
| Email               | Password | Role          |
| --------------------| ---------|---------------|
| aliuken@aliuken.com | qwerty1  | administrator |
| luis@aliuken.com    | qwerty2  | supervisor    |
| marisol@aliuken.com | qwerty3  | supervisor    |
| daniel@aliuken.com  | qwerty4  | user          |
| miguel@aliuken.com  | qwerty5  | user          |
| antonio@aliuken.com | qwerty6  | user          |
| pai.mei@aliuken.com | qwerty7  | user          |
```

NOTES:
* The users **antonio@aliuken.com** and **pai.mei@aliuken.com** can still not be used. They require confirmation via email.
* The priority order of the roles is: **administrator > supervisor > user**.
* When you create a user for your personal email account, its role will be "user".
* In order to receive the email to confirm your account registration, you need to create a Gmail SMTP account (as indicated here: <https://www.baeldung.com/spring-email#2-spring-boot-mail-server-properties>) and pass the value for the following environment variables to the application: **EMAIL_APPLICATION_ACCOUNT_USER** and **EMAIL_APPLICATION_ACCOUNT_PASSWORD**.