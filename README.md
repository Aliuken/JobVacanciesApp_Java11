> [!IMPORTANT]
> In the following glossary, we specify the abbreviations (for the indicated terms) used in the next sections:
> ```txt
> | Abbreviation | Term                               |
> |---------------------------------------------------|
> | 2FA          | Two-Factor Authentication          |
> | AI           | Artificial Intelligence            |
> | AJAX         | Asynchronous JavaScript And XML    |
> | API          | Application Programming Interface  |
> | app          | Application                        |
> | CLI          | Command-Line Interface             |
> | config       | Configuration                      |
> | CV           | Curriculum Vitae                   |
> | DB           | Database                           |
> | DOM          | Document Object Model              |
> | ELK          | Elastic Stack                      |
> | ERD          | Entity-Relationship Diagram        |
> | GC           | Garbage Collector                  |
> | GNU          | GNU's Not Unix!                    |
> | GUI          | Graphical User Interface           |
> | HTML         | Hypertext Markup Language          |
> | HTTP         | Hypertext Transfer Protocol        |
> | i18n         | Internationalization               |
> | IDE          | Integrated Development Environment |
> | info         | Information                        |
> | JDBC         | Java Database Connectivity         |
> | JDK          | Java Development Kit               |
> | JS           | JavaScript                         |
> | L10n         | Localization                       |
> | LLM          | Large Language Model               |
> | MDN          | Mozilla Developer Network          |
> | ORM          | Object–Relational Mapping          |
> | OS           | Operating System                   |
> | OSI          | Open Systems Interconnection       |
> | PC           | Personal Computer                  |
> | pkg          | Java package                       |
> | PNG          | Portable Network Graphics          |
> | prop         | Property                           |
> | QUIC         | Quick UDP Internet Connections     |
> | regex        | Regular Expression                 |
> | REST         | Representational State Transfer    |
> | SLF4J        | Simple Logging Facade for Java     |
> | SQL          | Structured Query Language          |
> | SMS          | Short Message Service              |
> | TCP          | Transmission Control Protocol      |
> | TS           | TypeScript                         |
> | UDP          | User Datagram Protocol             |
> | UI           | User Interface                     |
> | URL          | Uniform Resource Locator           |
> | UUID         | Universally Unique Identifier      |
> | WSL          | Windows Subsystem for Linux        |
> ```

# JobVacanciesApp_Java11

## 1. About JobVacanciesApp_Java11

**JobVacanciesApp_Java11** is an open-source web application made with **OpenJDK 11** and **Spring Boot 2.7.18** to learn how to make a website to manage job vacancies using state-of-the-art Spring-related technologies.

The code was created under the [Apache License 2.0](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/LICENSE) using the **SOLID principles** in a **monolithic architecture**.

### 1.1. About JobVacanciesApp_AppData_Java11

This repository makes use of the data contained in the repository [JobVacanciesApp_AppData_Java11](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11), which contains the following files that are external to the project:
* The [CV files](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/JobVacanciesApp/auth-user-curriculum-files), [entity query files](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/JobVacanciesApp/auth-user-entity-query-files), [company logo files](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/JobVacanciesApp/job-company-logos) and [log files](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/JobVacanciesApp/log-files) that are used in JobVacanciesApp.
* The Elastic Stack config files used to monitor/analyze the JobVacanciesApp logs, where:
    * [Filebeat](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/ElasticStack/filebeat-config) is configured so that the TRACE and DEBUG logs are ignored, the multiline logs (like Java Stack Traces) are grouped in a single log entry and the output is sent to Logstash (port 5044).
    * [Logstash](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/ElasticStack/logstash-config) reads the input from Beats (port 5044), splits each entry into different data (timestamp, level, pid, thread, class and submessage) and sends the output to Elasticsearch (port 9200).
    * [Elasticsearch](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/ElasticStack/elasticsearch-config) is deployed in the port 9200 as a single-node cluster (jobVacanciesAppCluster).
    * [Kibana](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/ElasticStack/kibana-config) is deployed in the port 5601 and reads the input from Elasticsearch (port 9200).

> [!NOTE]
> **Filebeat** is a subtype of **Beats** (from the Elastic Stack) to read data from files.

### 1.2. Other related projects

Other related projects include:
* [JobVacanciesApp_Java17](https://github.com/Aliuken/JobVacanciesApp_Java17) and [JobVacanciesApp_AppData_Java17](https://github.com/Aliuken/JobVacanciesApp_AppData_Java17), which use **Java 17** and **Spring Boot 4.0.1**.
* [JobVacanciesApp_Java25](https://github.com/Aliuken/JobVacanciesApp_Java25) and [JobVacanciesApp_AppData_Java25](https://github.com/Aliuken/JobVacanciesApp_AppData_Java25), which use **Java 25** and **Spring Boot 4.0.1**.

## 2. Design patterns

The following design patterns are used in the application:
* **MVC** (**Model-View-Controller**): Through:
    * @Controller classes (pkg: [com.aliuken.jobvacanciesapp.controller](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/controller)) from Spring MVC.
    * The MVC utility classes (pkg: [com.aliuken.jobvacanciesapp.util.spring.mvc](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/mvc)).
* **Open Session in View**: Through the "spring.jpa.open-in-view" property.
* **DTO** (**Data Transfer Object**): In the package [com.aliuken.jobvacanciesapp.model.dto](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto).
* **DAO** (**Data Access Object**): Through:
    * @Repository interfaces (pkg: [com.aliuken.jobvacanciesapp.repository](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/repository)).
    * @Entity classes (pkg: [com.aliuken.jobvacanciesapp.model.entity](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity)).
* **Singleton**: It is used when we want a single instance of a class and it is not possible to use enums, @Autowired or utility classes (with only static methods). This pattern is used in the packages:
    * [com.aliuken.jobvacanciesapp.model.dto.converter](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/converter): To get EntityToDtoConverter instances statically in controllers, DTOs, JUnit tests and other converters.
    * [com.aliuken.jobvacanciesapp.util.javase](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase): To get the ConfigurableEnumUtils instance statically to execute its instance methods (that use generics).
    * [com.aliuken.jobvacanciesapp.util.javase.stream](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/stream): To get StreamUtils instances statically (of type SequentialStreamUtils or ParallelStreamUtils) to execute its instance methods (that use generics).
    * [com.aliuken.jobvacanciesapp.util.javase.time](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/time): To add the TemporalUtils instances (dateUtils and dateTimeUtils) as static variables in the thymeleafViewResolver (in [WebTemplateConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebTemplateConfig.java)).
    * [com.aliuken.jobvacanciesapp.util.security](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/security): To add springSecurityUtils as a static variable in the thymeleafViewResolver (in [WebTemplateConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebTemplateConfig.java)).
* **Factory**: To create JPA entity instances (pkg: [com.aliuken.jobvacanciesapp.model.entity.factory](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/factory)).

## 3. Technologies

### 3.1. Core technologies

The core technologies currently used are:
* **OpenJDK 11**: As the **Java SE** implementation (using the default garbage collector: **G1 GC**). More details in section **[3.2. Java SE core technologies](https://github.com/Aliuken/JobVacanciesApp_Java11#32-java-se-core-technologies)**.
* **Jakarta EE 8** "javax" classes (detailed in section **[3.3. Jakarta EE technologies](https://github.com/Aliuken/JobVacanciesApp_Java11#33-jakarta-ee-technologies)**), including:
    * **@PostConstruct** and **Bean Validation** annotations (@NotNull, @NotEmpty, @Size, @Digits and @Email).
    * **Servlet** API, **Java Persistence API** (**JPA**) and **Mail** API.
* **Spring Boot 2.7.18** (**Spring Framework 5.3**): Starting in the class [MainClass](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/MainClass.java), which is restartable through the method "MainClass.restartApp(...)". More details in section **[3.4. Spring core technologies](https://github.com/Aliuken/JobVacanciesApp_Java11#34-spring-core-technologies)**.
* **Maven**: As the dependency manager and for building the application.
* **Git**: As the version control system.
* **GitHub**: As the hosting service for the project (in <https://github.com/Aliuken/JobVacanciesApp_Java11>).
* **JUnit 5**: For unit testing.
* **Spring AOP** and **AspectJ** [[&#10138;]](https://mvnrepository.com/artifact/org.aspectj): To deal with cross-cutting concerns. Used in the classes [ControllerAspect](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect/ControllerAspect.java), [ServiceAspect](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect/ServiceAspect.java) and [RepositoryAspect](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect/RepositoryAspect.java). Explained in section **[3.8. AOP technologies](https://github.com/Aliuken/JobVacanciesApp_Java11#38-aop-technologies)**.
* **Lombok**: To generate:
    * The enum fields' getters (pkg: [com.aliuken.jobvacanciesapp.model.entity.enumtype](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype) and [com.aliuken.jobvacanciesapp.enumtype](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype)) with @Getter.
    * The model entities (pkg: [com.aliuken.jobvacanciesapp.model.entity](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity)) with @Getter and @Setter.
    * The DTOs as immutable objects (pkg: [com.aliuken.jobvacanciesapp.model.dto](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto)) with @Data and final attributes.
    * The "log" variable used for logging (with @Slf4j).
* **Configuration**: Explained in section **[7. Configuration and application properties](https://github.com/Aliuken/JobVacanciesApp_Java11#7-configuration-and-application-properties)**.
* **i18n** and **L10n**: It uses Locale and MessageSource (in the interface [Internationalizable](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/superinterface/Internationalizable.java)) and is configured in [I18nConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/I18nConfig.java) (as in <https://www.baeldung.com/spring-boot-internationalization>). It is built for:
    * **English**: Using the file [src/main/resources/messages_en.properties](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/messages_en.properties).
    * **Spanish**: Using the file [src/main/resources/messages_es.properties](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/messages_es.properties).
* **Logging**: By using:
    * The implementation of the **SLF4J** API for **Logback** (with @Slf4j from Lombok).
    * The utility class [ControllerAspectLoggingUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/aop/logging/ControllerAspectLoggingUtils.java): Used in "ControllerAspect" to log multiple stats.
    * The utility class [RepositoryAspectLoggingUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/aop/logging/RepositoryAspectLoggingUtils.java): Used in "RepositoryAspect" to log multiple stats.
* **Utilities**: There are multiple utility classes in the package [com.aliuken.jobvacanciesapp.util](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util).

### 3.2. Java SE core technologies

> [!IMPORTANT]
> The utility classes related to Java SE core are in the package [com.aliuken.jobvacanciesapp.util.javase](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase).

The Java SE core technologies currently used are:
* **Java annotations**: The following ones (pkg: [com.aliuken.jobvacanciesapp.annotation](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/annotation)):
    * **@ServiceMethod**: To annotate the methods of the classes annotated with Spring's @Service.
    * **@RepositoryMethod**: To annotate the methods that access the DB in the classes annotated with Spring's @Repository or @NoRepositoryBean.
    * **@LazyEntityRelationGetter**: To annotate the getters of the classes annotated with JPA's @Entity that make use of a @OneToMany relationship.
* **Java enums**: Located in the packages:
    * [com.aliuken.jobvacanciesapp.model.entity.enumtype](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype) (when they are used in JPA entities).
    * [com.aliuken.jobvacanciesapp.enumtype](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype) (when they are not used in JPA entities).
* **Java functional programming**: In [FunctionalUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/FunctionalUtils.java).
* **Java streams**: To iterate over elements using the following methods of [SequentialStreamUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/stream/SequentialStreamUtils.java) or [ParallelStreamUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/stream/ParallelStreamUtils.java) (of type [StreamUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/stream/superclass/StreamUtils.java)):
    * **ofNullableCollection**: For JPA entity methods annotated with @LazyEntityRelationGetter.
    * **ofEnum**: For Java enum methods.
    * **joinArrays/joinLists/joinSets**: To mix multiple arrays/lists/sets into one.
    * **convertArray/convertList/convertSet**: Used in [EntityToDtoConverter](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/converter/superclass/EntityToDtoConverter.java) to convert JPA entities to DTOs.
* **Java NIO API**: To deal with files and folders by using:
    * The standard Java classes "Path", "Paths", "Files", "DirectoryStream" and "DirectoryStream.Filter".
    * The enum [FileType](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/FileType.java) and the utility classes [FileUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence/file/FileUtils.java) and [FileNameUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence/file/FileNameUtils.java).
* **java.time API**: To deal with dates (including time) by using:
    * The standard Java classes "LocalDate" and "LocalDateTime".
    * The utility classes [DateUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/time/DateUtils.java) and [DateTimeUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/time/DateTimeUtils.java) (of type [TemporalUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/time/superinterface/TemporalUtils.java)).
* **Java try-with-resources**: Used in the classes:
    * **FileType**: Over "DirectoryStream&lt;Path&gt;".
    * **ThrowableUtils**: Over "StringWriter" and "PrintWriter".
    * **FileUtils**: Over "ServletOutputStream" and "ZipFile".
    * **AuthUserQueryReport**: Over "AuthUserQueryReport&lt;T&gt;".
* **Java varargs**: When needed (adding the suffix "Varargs" to the name of the last method argument).
* **Java StringJoiner class**: In [StringUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/StringUtils.java), to deal with String concatenation.
* **Java Locale class**: In [I18nUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/i18n/I18nUtils.java), to deal with i18n and L10n.
* **Java threads**: In [ConcurrencyUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/concurrency/ConcurrencyUtils.java), to run tasks concurrently.

### 3.3. Jakarta EE technologies

The Jakarta EE technologies currently used are:
* **@PostConstruct annotation**: To initialize Spring beans after the constructor and all their dependency injections have been made.
* **Jakarta Servlet API**: Used in the classes with package **javax.servlet...**
* **Jakarta Persistence API** (**JPA**): Used in the classes with package **javax.persistence...**
* **Jakarta Mail API**: Used in the classes with package **javax.mail...**
* **Jakarta Bean Validation**: Used in the classes with package **javax.validation...**

### 3.4. Spring core technologies

> [!IMPORTANT]
> The utility classes related to Spring core are in the package [com.aliuken.jobvacanciesapp.util.spring](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring).

The Spring core technologies currently used are:
* **Dependency Injection** (**DI**): By using:
    * Beans created with **@Component**, **@Controller**, **@Service** and **@Repository**.
    * Beans created with **@Bean** (inside of a class annotated with **@Configuration**).
    * **@Autowired** to get the reference to the beans created by the previous methods.
    * The utility class [BeanFactoryUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/di/BeanFactoryUtils.java) to get, refresh and replace beans statically. It uses internally Spring's **ApplicationContextAware** and **GenericApplicationContext**.
* **Aspect-Oriented Programming** (**AOP**): To deal with cross-cutting concerns. Explained in section **[3.8. AOP technologies](https://github.com/Aliuken/JobVacanciesApp_Java11#38-aop-technologies)**.
* **Spring Expression Language** (**SpEL**): Used to express values dynamically in the HTML pages.
* **Spring @Value annotation**: To get config values from "application.properties" or "application.yaml" files.
* **Spring MessageSource interface**: In [I18nUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/i18n/I18nUtils.java), to deal with i18n and L10n. Configured in [I18nConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/I18nConfig.java).
* **Spring Validation**: In [ControllerValidationUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/mvc/ControllerValidationUtils.java), to deal with controller binding validation.

### 3.5. Web technologies

> [!IMPORTANT]
> The utility classes related to the web are in the package [com.aliuken.jobvacanciesapp.util.spring.mvc](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/mvc).

The web technologies currently used are:
* **Spring MVC**: Configured in [WebMvcConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebMvcConfig.java).
* **HTTP/2 (over TCP)**: As the application-layer communication protocol (in the OSI model).
* **Apache Tomcat**: As the HTTP/2 web server. It has a web container (for Servlets and JSPs), but not an EJB container. It is configured in [WebServerConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebServerConfig.java).
* **HTML5** files: For the web pages. Located in [src/main/resources/templates](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/resources/templates).
* **Thymeleaf**: As the HTML5 template engine (configured in [WebTemplateConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebTemplateConfig.java)), using:
    * The decorator [template.html](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/mandatory/template.html) (which uses **thymeleaf-layout-dialect** [[&#10138;]](https://mvnrepository.com/artifact/nz.net.ultraq.thymeleaf/thymeleaf-layout-dialect) to create the layout template).
    * Other fragments in [src/main/resources/templates/fragments](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/resources/templates/fragments). More info about fragments at <https://www.thymeleaf.org/doc/articles/layouts.html>.
* **JavaScript** (**ES2020**) compiled from **TypeScript** using the [TypeScript Playground](https://www.typescriptlang.org/play/) (so that we didn't need to install "Node.js", "npm" and "TypeScript"). In particular, the TS file [jobvacanciesapp.ts](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/ts/jobvacanciesapp.ts) was compiled to the following JS files: [ajax-utils.js](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/ajax-utils.js) and [page-url-and-dom-utils.js](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/page-url-and-dom-utils.js).
* **Bootstrap 5.3.8** and **Material Design for Bootstrap 9.3.0**: For the look-and-feel.
* **Font Awesome Free For The Web 7.1.0**: For the application icons.
* **jQuery 3.7.1**: To make an easier use of **JavaScript**.
* **jQuery UI 1.14.1** and **jQuery Timepicker Addon 1.6.3**: For the calendar UI-input element.
* **TinyMCE Community 8.3.1**: For the rich text editor.
* The **static resources** (like image/TS/JS/CSS files) are located in [src/main/resources/static](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/resources/static). The following **ad-hoc static files** (created by ourselves) are located in [src/main/resources/static/jobvacanciesapp-utils](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/resources/static/jobvacanciesapp-utils):
    * **calendar-ui-input.css** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/css/calendar-ui-input.css) and **calendar-ui-input.js** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/calendar-ui-input.js): For the calendar UI-input element.
    * **rich-text-editor.css** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/css/rich-text-editor.css) and **rich-text-editor.js** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/rich-text-editor.js): For the rich text editor. The TinyMCE "[Full featured demo: Non-Premium Plugins only](https://www.tiny.cloud/docs/tinymce/latest/full-featured-open-source-demo)" CSS and JS code was copied in those files.
    * **ajax-utils.js** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/ajax-utils.js): With an **AJAX** call to refresh the company logo from model attributes without reloading the full page. This JS file was created by compiling the TS file [jobvacanciesapp.ts](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/ts/jobvacanciesapp.ts).
    * **page-url-and-dom-utils.js** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/page-url-and-dom-utils.js): To reload the full page depending on URL parameters extracted from session attributes and model attributes and also to get elements from the current page DOM. This JS file was created by compiling the TS file [jobvacanciesapp.ts](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/ts/jobvacanciesapp.ts).

> [!NOTE]
> The decorator [template.html](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/mandatory/template.html) calls the JS function **getIsPageWithTable()** of [page-url-and-dom-utils.js](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/js/page-url-and-dom-utils.js) (to check if the current page has a table) and has the following Thymeleaf fragments:
> * **additionalStyles**: Which is a variable fragment used to add new CSS files or &lt;style&gt; tags.
> * **menu**: Which is the fixed fragment [menu.html](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/mandatory/menu.html).
> * **mainContent**: Which is a variable fragment used to contain the main content (in HTML) of the page.
> * **footer**: Which is the fixed fragment [footer.html](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/mandatory/footer.html).
> * **additionalScripts**: Which is a variable fragment used to add new JS files or &lt;script&gt; tags.
>
> Other optional Thymeleaf fragments (in [src/main/resources/templates/fragments/optional](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/resources/templates/fragments/optional)) are:
> * **jobCompanyLogoFragment** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/optional/jobCompanyLogoFragment.html): Which is used to show the selected company logo in "jobCompanyForm.html" and "jobVacancyForm.html".
> * **jumbotron** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/optional/jumbotron.html): Which is used to show the classic Bootstrap's Jumbotron in "home.html".
> * **tableFilterAndPaginationForm** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/optional/tableFilterAndPaginationForm.html): Which is used (in the pages that have a table) to show the filter and pagination forms. Those forms are shown above the table.
> * **tablePaginationNav** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/fragments/optional/tablePaginationNav.html): Which is used (in the pages that have a table) to show the pagination buttons. Those buttons are shown below the table.
>
> If the current page has a table, the DTO [PredefinedFilterDTO](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/PredefinedFilterDTO.java) is used to store the following filtering data:
> * **predefinedFilterName**: Which is the predefined filter field name (one of the codes defined in [PredefinedFilterEntity](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/PredefinedFilterEntity.java)).
> * **predefinedFilterValue**: Which is the predefined filter field value.
>
> If the current page has a table, the DTO [TableSearchDTO](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/TableSearchDTO.java) is used to store the following URL parameters (regarding filtering, sorting and pagination):
> * **languageParam**: Which is the selected language (one of the codes defined in [Language](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/Language.java)).
> * **filterName**: Which is the filter field name (one of the codes defined in [TableField](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TableField.java)).
> * **filterValue**: Which is the filter field value.
> * **sortingField**: Which is the field to be sorted (one of the codes defined in [TableField](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TableField.java)).
> * **sortingDirection**: Which is the sort direction (one of the codes, ASC or DESC, defined in [TableSortingDirection](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TableSortingDirection.java)).
> * **pageSize**: Which is the size of each page (one of the values defined in [TablePageSize](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TablePageSize.java)).
> * **pageNumber**: Which is the number of the current page.

### 3.6. Data technologies

> [!IMPORTANT]
> The utility classes related to persistence are in the package [com.aliuken.jobvacanciesapp.util.persistence](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence).

The data technologies currently used are:
* **Spring Data JPA**: To make an easier use of **JPA**. Configured in [PersistenceConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/PersistenceConfig.java).
* **Hibernate**: As the ORM and **JPA** implementation.
* **JPQL**: In the methods annotated with @RepositoryMethod in @Repository classes (instead of using **SQL**).
* **MySQL Community Server**: As the main relational DB (script: [src/main/resources/db_dumps/mysql-dump.sql](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/db_dumps/mysql-dump.sql)).
* **H2**: As the in-memory relational DB for testing (script: [src/test/resources/db_dumps/h2-dump.sql](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/test/resources/db_dumps/h2-dump.sql)).
* **MySQL Workbench**: As the database administration tool. Although another tool can be used instead.
* **Transactions**: Defined with Spring using:
    * **JpaTransactionManager** (configured in [PersistenceConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/PersistenceConfig.java)).
    * **@Transactional** in the @Service classes (pkg: [com.aliuken.jobvacanciesapp.service](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/service)).
* **AbstractEntity**: A superclass for all the JPA entities (pkg: [com.aliuken.jobvacanciesapp.model.entity.superclass](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/superclass)) that has:
    * An "id" of type Long.
    * The dateTime and user of the "first registration" of the entity.
    * The dateTime and user of the "last modification" of the entity.
    * The "compareTo", "hashCode" and "equals" methods for all the entities (based on the object's Class and id).
* **Comparators for JPA entities** (pkg: [com.aliuken.jobvacanciesapp.model.comparator.superclass](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/comparator/superclass)):
    * **AbstractEntityDefaultComparator**: A comparator based on the object's Class and id.
    * **AbstractEntitySpecificComparator**: A comparator based on the object's Class, a first specific field and the id.
* **JPA converters**: Between DB types and entity fields (pkg: [com.aliuken.jobvacanciesapp.model.converter](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/converter)).
* **DTO converters**: Between JPA entities and DTOs (pkg: [com.aliuken.jobvacanciesapp.model.dto.converter](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/converter)).
* **Date formatters**: For LocalDate and LocalDateTime (pkg: [com.aliuken.jobvacanciesapp.model.formatter](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/formatter)).
* **UpgradedJpaRepository** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/repository/superinterface/UpgradedJpaRepository.java): A subinterface of "JpaRepository<AbstractEntity, Long>" (from Spring Data JPA) to deal with pagination, sorting, query by example and query by specification.
* **DatabaseUtils** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence/database/DatabaseUtils.java): To get objects (of type ExampleMatcher or Predicate) used in complex dynamic queries.
* **FileUtils** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence/file/FileUtils.java), **FileNameUtils** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence/file/FileNameUtils.java) and **FileType** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/FileType.java): To manage the persistence in files (of CVs, query PDFs or logos).
* **Zip4j** [[&#10138;]](https://mvnrepository.com/artifact/net.lingala.zip4j/zip4j): A Java library used to decompress zip files.
* **iText Core itextpdf** [[&#10138;]](https://mvnrepository.com/artifact/com.itextpdf/itextpdf): A Java library used to export queries to PDFs.

> [!NOTE]
> In [UpgradedJpaRepository](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/repository/superinterface/UpgradedJpaRepository.java), the method "saveAndFlush(S entity)" is used both to update and create an entity, depending on whether it already existed or not in the database.
>
> In [PersistenceConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/PersistenceConfig.java), "dataSource", "entityManagerFactory" and "transactionManager" are static final beans to get working the **application restart** (explained in section **[7.2. ConfigPropertiesBean](https://github.com/Aliuken/JobVacanciesApp_Java11#72-configpropertiesbean)**).
>
> The **Entity-Relationship Diagram** of the DB is in the following files:
> * **As an image** in: [documentation/Entity-Relationship-Diagram.png](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/documentation/Entity-Relationship-Diagram.png).
> * **As a draw.io file** (that can be modified using the [draw.io website](https://www.draw.io) or the [drawio-desktop app](https://github.com/jgraph/drawio-desktop/releases)) in: [documentation/Entity-Relationship-Diagram.drawio](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/documentation/Entity-Relationship-Diagram.drawio).
>
> The initial records in the table **auth_user_entity_query** were created from the pages indicated in [documentation/previously-generated-PDFs.md](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/documentation/previously-generated-PDFs.md) and then added to [mysql-dump.sql](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/db_dumps/mysql-dump.sql), [h2-dump.sql](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/test/resources/db_dumps/h2-dump.sql) and [AppData_Java11/JobVacanciesApp/auth-user-entity-query-files](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/JobVacanciesApp/auth-user-entity-query-files).

### 3.7. Security technologies

> [!IMPORTANT]
> The utility classes related to security are in the package [com.aliuken.jobvacanciesapp.util.security](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/security).
>
> The session timeout is set in the **server.servlet.session.timeout** property to 30 minutes.

The security technologies currently used are:
* **Spring Security**: Configured in [WebSecurityConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebSecurityConfig.java).
* **BCrypt**: As the Spring Security password encoder (variable "passwordEncoder" in the code).
* **CustomAuthenticationHandler** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/security/CustomAuthenticationHandler.java): To handle the processes of:
    * **Login**: By adding the authenticated user to the session as the attribute "sessionAuthUser".
    * **Logout**: By removing the session attribute "sessionAuthUser" and redirecting to the result of the call "this.getRedirectEndpoint(...)".
* **SpringSecurityUtils** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/security/SpringSecurityUtils.java): Utility class to manage the security from Java code in HTML pages with Thymeleaf (instead of using Thymeleaf's tag attribute "[sec:authorize](https://www.thymeleaf.org/doc/articles/springsecurity.html)", which is impossible to debug).
* **SessionUtils** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/security/SessionUtils.java): Utility class to get the sessionAuthUser object from the following origins:
    * From the Authentication object of Spring Security.
    * From the "sessionAuthUser" attribute of the HttpSession object of the Servlet API.
* **RandomUtils** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/security/RandomUtils.java): Utility class to get random strings, numbers, enum values or objects (used for security reasons).
* **Remember me based on the email**: Implemented in the class [JdbcTokenByEmailRepository](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/repository/JdbcTokenByEmailRepository.java) making use of the DB table auth_persistent_logins (instead of using the **Remember me based on the username** implemented in the class [JdbcTokenRepositoryImpl](https://github.com/spring-projects/spring-security/blob/main/web/src/main/java/org/springframework/security/web/authentication/rememberme/JdbcTokenRepositoryImpl.java) of Spring Security).
* **AllowedViewsEnum** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/AllowedViewsEnum.java): An enum that contains the allowed views for each role (anonymous, user, supervisor and administrator) when the anonymous access is allowed and when it is not allowed.
* **EmailService**: This service has the following two methods to send emails (implemented in [EmailServiceImpl](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/service/EmailServiceImpl.java), using the templates defined in [EmailConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/EmailConfig.java) for English and Spanish):
    * **sendSignUpConfirmationEmail**: To send the confirmation email to new registered users.
    * **sendResetPasswordEmail**: To send the email to reset the password when you forgot the current one.
* **dependency-check-maven** [[&#10138;]](https://mvnrepository.com/artifact/org.owasp/dependency-check-maven): A Maven plugin used to generate a report with the dependency vulnerabilities in "target/dependency-check-report.html". The Dependency Check is skipped by using "&lt;skip&gt;true&lt;/skip&gt;" because it is a time-consuming task. To learn more about this plugin, check out these links:
    * <https://jeremylong.github.io/DependencyCheck/dependency-check-maven>
    * <https://jeremylong.github.io/DependencyCheck/dependency-check-maven/check-mojo.html>

> [!NOTE]
> Other possible improvements would be:
> - Using **https** instead of **http**. And maybe **HTTP/3 (over QUIC)** instead of **HTTP/2 (over TCP)**.
> - Using a **captcha** in the **user registration**.
> - Storing in the DB the user **mobile phone number** to implement **2FA by sending an SMS** (with a random authentication code). This feature could be optional per user.

### 3.8. AOP technologies

> [!IMPORTANT]
> The utility classes related to AOP are in the package [com.aliuken.jobvacanciesapp.util.spring.aop](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/aop).

AOP (Aspect-Oriented Programming) is implemented (with **Spring AOP** and **AspectJ**) to deal with cross-cutting concerns in different layers of the app (controllers, services and DAOs/entities) and configured in [AopConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/AopConfig.java).

Specifically, the following AOP aspects (pkg: [com.aliuken.jobvacanciesapp.aop.aspect](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect)) were created:
* **ControllerAspect** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect/ControllerAspect.java): For logging stats in controllers (pkg: [com.aliuken.jobvacanciesapp.controller](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/controller)). It works around methods annotated with @RequestMapping, @GetMapping or @PostMapping and uses:
    * The call "EndpointType.getInstance(httpMethod, informedPath)" where the informedPath is used to match with one of the regular expressions defined in [EndpointType](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/EndpointType.java). Those regex can contain "([^/]+)" to represent a path parameter (formed by consecutive characters without "/").
    * The DB time inside the controller, calculated in RepositoryAspect.
* **ServiceAspect** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect/ServiceAspect.java): To create an EntityManagerFactory when the current one is closed. It works around methods annotated with @ServiceMethod in services (pkg: [com.aliuken.jobvacanciesapp.service](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/service)).
* **RepositoryAspect** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/aop/aspect/RepositoryAspect.java): To get and log the DB time inside of controllers (between ">>>> " and "<<<< " logs) and outside of controllers (in the rest of the cases). It works around methods annotated with:
    * @RepositoryMethod in DAOs (pkg: [com.aliuken.jobvacanciesapp.repository](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/repository)).
    * @LazyEntityRelationGetter in JPA entities (pkg: [com.aliuken.jobvacanciesapp.model.entity](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity)).

> [!NOTE]
> Therefore, the annotations used as AOP pointcuts are:
> * **@RequestMapping**, **@GetMapping** and **@PostMapping**: Properly from Spring to define controllers.
> * **@ServiceMethod**, **@RepositoryMethod** and **@LazyEntityRelationGetter**: Defined in the package [com.aliuken.jobvacanciesapp.annotation](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/annotation) specifically to be used as AOP pointcuts.
>
> Also, the following utility classes are used in the AOP aspects:
> * [ControllerAspectRestUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/aop/rest/ControllerAspectRestUtils.java): It gets mappings for typical HTTP methods used in REST APIs (GET, POST, PUT and DELETE). Used in **ControllerAspect** to get the GetMapping or PostMapping from a JoinPoint.
> * [ControllerAspectLoggingUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/aop/logging/ControllerAspectLoggingUtils.java): Used in **ControllerAspect** to log multiple stats.
> * [RepositoryAspectLoggingUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/spring/aop/logging/RepositoryAspectLoggingUtils.java): Used in **RepositoryAspect** to log multiple stats.
>
> To make all this work, the Maven plugin [aspectj-maven-plugin](https://mvnrepository.com/artifact/org.codehaus.mojo/aspectj-maven-plugin) was needed.

### 3.9. Docker technologies

This app can also be run in a **Docker** container through Docker Compose.

In Windows, to run the Docker containers, **Docker Desktop** and **WSL 2** are necessary.

#### 3.9.1. Docker technologies for GNU/Linux

The following Docker technologies (contained in [docker-linux](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux) are used for GNU/Linux:
* **Docker Compose for the app** (in [build-context-app/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/docker-compose.yaml)). It uses:
  * In **app-db-service**: The latest MySQL Docker image ("mysql:latest").
  * In **app-service**: The file [Dockerfile](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/Dockerfile), which uses "amazoncorretto:11-alpine-jdk" (a Docker image with JDK 11 for Alpine Linux).
* **Docker Compose for the Elastic Stack** (in [build-context-elk/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-elk/docker-compose.yaml)). It uses:
  * The **Elastic Stack 9.2.3** Docker images (url: <https://www.docker.elastic.co>): For analyzing the app log files, which pass through the stack in the next order: "Filebeat **&rArr;** Logstash **&rArr;** Elasticsearch **&rArr;** Kibana".

#### 3.9.2. Docker technologies for Windows

The following Docker technologies (contained in [docker-windows](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows) are used for Windows:
* **Docker Compose for the app** (in [build-context-app/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/docker-compose.yaml)). It uses:
  * In **app-db-service**: The latest MySQL Docker image ("mysql:latest").
  * In **app-service**: The file [Dockerfile](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/Dockerfile), which uses "amazoncorretto:11-alpine-jdk" (a Docker image with JDK 11 for Alpine Linux).
* **Docker Compose for the Elastic Stack** (in [build-context-elk/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-elk/docker-compose.yaml)). It uses:
  * The **Elastic Stack 9.2.3** Docker images (url: <https://www.docker.elastic.co>): For analyzing the app log files, which pass through the stack in the next order: "Filebeat **&rArr;** Logstash **&rArr;** Elasticsearch **&rArr;** Kibana".

### 3.10. Other technologies

The PCs used to develop and execute the application were two:
* One with a **Linux Mint 22** 64-bit OS (**GNU/Linux**).
* Another one with a **Windows 11 Home** 64-bit OS.

Other technologies currently used are:
* **IntelliJ IDEA 2025.3.1**: As the IDE.
* **Mozilla Firefox**: As the main web browser.
* **PNG**: As the file format (**.png**) of the [ERD image](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/documentation/Entity-Relationship-Diagram.png), the [images in JobVacanciesApp_Java11](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/resources/static/images) and the [images in JobVacanciesApp_AppData_Java11](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11/tree/main/AppData_Java11/JobVacanciesApp/job-company-logos).
* **draw.io**: As the file format (**.drawio**) of the [ERD](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/documentation/Entity-Relationship-Diagram.drawio).
* **Markdown**: As the file format (**.md**) of the [documentation files](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/documentation).
* **Sourcetree**: As the Git GUI for Windows.
* **Git Bash**: As the command-line shell for the Git CLI.
* **Powershell**: As the command-line shell for the Docker CLI for Windows.
* **MDN** [[&#10138;]](https://developer.mozilla.org/en-US/docs/Learn_web_development): This website was used to learn about **HTTP**, **HTML**, **JavaScript** and **CSS**.
* **DeepSeek R1**: This AI LLM was used (from [deepinfra](https://deepinfra.com/deepseek-ai/DeepSeek-R1) with 16000 max new tokens) in collaboration with ourselves to create the TS file [jobvacanciesapp.ts](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/static/jobvacanciesapp-utils/ts/jobvacanciesapp.ts).
* **GenericControllerAdvice** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/controller/advice/GenericControllerAdvice.java): To be able to:
    * Access the requestURI from Thymeleaf in any web page with "${requestURI}".
    * Handle the exception thrown when uploading a file (CV or logo) too big (more than 10 MB).
* **AbstractEntityServiceSuperclass** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/service/superclass/AbstractEntityServiceSuperclass.java): An abstract class that contains the default implementation for the most common service methods (by calling the [UpgradedJpaRepository](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/repository/superinterface/UpgradedJpaRepository.java) repository methods).
* **Ehcache**: Configured in [CacheConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/CacheConfig.java) and in the property **jobvacanciesapp.useEntityManagerCache**. It is used in [UpgradedJpaRepository](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/repository/superinterface/UpgradedJpaRepository.java) to create the **entityManagerCache** of type "Cache<Class<? extends AbstractEntity>, EntityManager>" to get the EntityManager of a JPA entity class. The execution flow starts by calling to "UpgradedJpaRepository.getEntityManagerConfigurable(entityClass)" and is the following:

```txt
| useEntityManagerCache | Is the searched     | Execution flow                                         |
| property value        | class in the cache? | (after calling to getEntityManagerConfigurable)        |
|-----------------------|---------------------|--------------------------------------------------------|
| true                  | Yes                 | getEntityManagerCacheable                              |
| true                  | No                  | getEntityManagerCacheable => getEntityManagerNotCached |
| false                 | N/A                 | getEntityManagerNotCached                              |
```

## 4. Execution procedure

### 4.1. Run the application using an IDE

> [!IMPORTANT]
> Before running the application, install **MySQL Workbench** and:
> * **Configure a connection** like the one in [documentation/MySQL-Workbench-connection.png](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/documentation/MySQL-Workbench-connection.png) (where the password is "admin").
> * **Reset the database** using the previous connection and running the SQL script **mysql-dump.sql**.
>
> To run the application, I recommend using the IDE **IntelliJ IDEA**.
>
> The Elastic Stack cannot be run using an IDE. It can only be run using Docker Compose.

Steps:
* **Run JobVacanciesApp_Java11 in an IDE**: as a Spring Boot application
* **Open the JobVacanciesApp URL in a web browser**: <http://localhost:8080>

### 4.2. Run and stop the application and the Elastic Stack using Docker Compose

#### 4.2.1. Steps for GNU/Linux:

1. Go to the docker-linux directory and stop everything related to Docker by executing this commands in a new terminal:
   ```shell
   cd ./docker-linux
   sudo ./docker-stop.sh
   ```

2. The following step depends on whether you want to run the app or the ELK:

   a) To run the app with Docker Compose:
      * Execute this command in your terminal:
        ```shell
        sudo ./docker-compose-app-start.sh
        ```
      * Open the JobVacanciesApp URL in a web browser: <http://localhost:9080>

   b) To run the ELK with Docker Compose:
      * Execute this command in your terminal:
        ```shell
        sudo ./docker-compose-elk-start.sh
        ```
      * Open the Kibana URL in a web browser: <http://localhost:5601>

3. Stop everything with Docker again by doing the following:
   * Press in your terminal: Ctrl + C
   * Execute this command in your terminal:
     ```shell
     sudo ./docker-stop.sh
     ```

#### 4.2.2. Steps for Windows:

1. Go to the docker-windows directory and stop everything related to Docker by executing this commands in a new terminal:
   ```shell
   cd .\docker-windows
   .\docker-stop.bat
   ```

2. The following step depends on whether you want to run the app or the ELK:

   a) To run the app with Docker Compose:
      * Execute this command in your terminal:
        ```shell
        .\docker-compose-app-start.bat
        ```
      * Open the JobVacanciesApp URL in a web browser: <http://localhost:9080>

   b) To run the ELK with Docker Compose:
      * Execute this command in your terminal:
        ```shell
        .\docker-compose-elk-start.bat
        ```
      * Open the Kibana URL in a web browser: <http://localhost:5601>

3. Stop everything with Docker again by doing the following:
   * Press in your terminal: Ctrl + C
   * Execute this command in your terminal:
     ```shell
     .\docker-stop.bat
     ```

## 5. Explanation of the docker-compose.yaml files

### 5.1. Explanation for GNU/Linux

> [!IMPORTANT]
> In the next sections, the path **/AppData_Java11** is the root folder in my GNU/Linux PC that contains the data from the repository [JobVacanciesApp_AppData_Java11](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11).

#### 5.1.1. Explanation of the docker-compose.yaml for the app

> [!IMPORTANT]
> The folder [build-context-app](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app) is the build context of the Docker Compose for the app and contains:
> * The file [docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/docker-compose.yaml), with the description of the containers **app-container** and **app-db-container**.
> * The file [.env](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/.env), to override the **docker-compose.yaml** variables' default values.
> * The files [docker-compose-start.sh](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/docker-compose-start.sh) and [docker-compose-stop.sh](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/docker-compose-stop.sh), to start and stop the containers respectively by using Docker Compose.
> * The folder [lib](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/lib), which will contain the jar of the app after a compilation.
> * The file [Dockerfile](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/Dockerfile), used in **docker-compose.yaml** to create the **app-container** image.
> * The file [Dockerfile-start.sh](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/Dockerfile-start.sh), with an example of how to create the image from the **Dockerfile** and run the container from the image without using Docker Compose.
> * The file [.dockerignore](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/.dockerignore), to ignore every file inside **build-context-app** (except the jar file generated inside the **lib** folder) when building the **Dockerfile** image.

In the file [build-context-app/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/docker-compose.yaml):
* **../../src/main/resources/db_dumps** contains the database dump file: **mysql-dump.sql**.
* **/AppData_Java11/JobVacanciesApp** is the folder that has the **CVs**, **query PDFs**, **company logos** and **log files** used in the app.
* **healthcheck** and **service_healthy** are used to check when the **mysql-dump.sql** file was executed, to start the Spring Boot app after that.
* The Spring Boot app is started through the file [Dockerfile](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/Dockerfile), that uses an Alpine Linux OS image (which reduces the size of the image) and reads the jar files inside the folder [build-context-app/lib](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-app/lib).
* **internal-net-app** is used to communicate the Spring Boot application with the database.
* **external-net-app** is used to communicate the Spring Boot application with the end user.

#### 5.1.2. Explanation of the docker-compose.yaml for the ELK

> [!IMPORTANT]
> The folder [build-context-elk](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-elk) is the build context of the Docker Compose for the ELK and contains:
> * The file [docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-elk/docker-compose.yaml), with the description of the containers **filebeat-container**, **logstash-container**, **elasticsearch-container** and **kibana-container**.
> * The files [docker-compose-start.sh](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-elk/docker-compose-start.sh) and [docker-compose-stop.sh](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-elk/docker-compose-stop.sh), to start and stop the containers respectively by using Docker Compose.

In the file [build-context-elk/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-linux/build-context-elk/docker-compose.yaml):
* **/AppData_Java11/JobVacanciesApp/log-files** is the folder that has the log files written by the app.
* **/AppData_Java11/ElasticStack** is the folder that has the configuration files for the ELK.
* **healthcheck** and **service_healthy** are used to check when each service started correctly, to start their dependent services after that. The startup order is: "Elasticsearch **&rArr;** Kibana **&rArr;** Logstash **&rArr;** Filebeat".
* **internal-net-elk** is used to communicate the ELK services among them.
* **external-net-elk** is used to communicate the Kibana service with the end user.

### 5.2. Explanation for Windows

> [!IMPORTANT]
> In the next sections, the path **//c/AppData_Java11** is the root folder in my Windows PC that contains the data from the repository [JobVacanciesApp_AppData_Java11](https://github.com/Aliuken/JobVacanciesApp_AppData_Java11).

#### 5.2.1. Explanation of the docker-compose.yaml for the app

> [!IMPORTANT]
> The folder [build-context-app](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app) is the build context of the Docker Compose for the app and contains:
> * The file [docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/docker-compose.yaml), with the description of the containers **app-container** and **app-db-container**.
> * The file [.env](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/.env), to override the **docker-compose.yaml** variables' default values.
> * The files [docker-compose-start.bat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/docker-compose-start.bat) and [docker-compose-stop.bat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/docker-compose-stop.bat), to start and stop the containers respectively by using Docker Compose.
> * The folder [lib](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/lib), which will contain the jar of the app after a compilation.
> * The file [Dockerfile](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/Dockerfile), used in **docker-compose.yaml** to create the **app-container** image.
> * The file [Dockerfile-start.bat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/Dockerfile-start.bat), with an example of how to create the image from the **Dockerfile** and run the container from the image without using Docker Compose.
> * The file [.dockerignore](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/.dockerignore), to ignore every file inside **build-context-app** (except the jar file generated inside the **lib** folder) when building the **Dockerfile** image.

In the file [build-context-app/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/docker-compose.yaml):
* **//c/Programacion/git/JobVacanciesApp_Java11/src/main/resources/db_dumps** contains the database dump file: **mysql-dump.sql**.
* **//c/AppData_Java11/JobVacanciesApp** is the folder that has the **CVs**, **query PDFs**, **company logos** and **log files** used in the app.
* **healthcheck** and **service_healthy** are used to check when the **mysql-dump.sql** file was executed, to start the Spring Boot app after that.
* The Spring Boot app is started through the file [Dockerfile](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/Dockerfile), that uses an Alpine Linux OS image (which reduces the size of the image) and reads the jar files inside the folder [build-context-app/lib](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-app/lib).
* **internal-net-app** is used to communicate the Spring Boot application with the database.
* **external-net-app** is used to communicate the Spring Boot application with the end user.

#### 5.2.2. Explanation of the docker-compose.yaml for the ELK

> [!IMPORTANT]
> The folder [build-context-elk](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-elk) is the build context of the Docker Compose for the ELK and contains:
> * The file [docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-elk/docker-compose.yaml), with the description of the containers **filebeat-container**, **logstash-container**, **elasticsearch-container** and **kibana-container**.
> * The files [docker-compose-start.bat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-elk/docker-compose-start.bat) and [docker-compose-stop.bat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-elk/docker-compose-stop.bat), to start and stop the containers respectively by using Docker Compose.

In the file [build-context-elk/docker-compose.yaml](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/docker-windows/build-context-elk/docker-compose.yaml):
* **//c/AppData_Java11/JobVacanciesApp/log-files** is the folder that has the log files written by the app.
* **//c/AppData_Java11/ElasticStack** is the folder that has the configuration files for the ELK.
* **healthcheck** and **service_healthy** are used to check when each service started correctly, to start their dependent services after that. The startup order is: "Elasticsearch **&rArr;** Kibana **&rArr;** Logstash **&rArr;** Filebeat".
* **internal-net-elk** is used to communicate the ELK services among them.
* **external-net-elk** is used to communicate the Kibana service with the end user.

## 6. Security credentials

### 6.1. Existent credentials to access the application

The database of the application comes with 9 predefined users with the following credentials and roles:

```txt
| Id | Email                 | Password | Role          | CreatedByUser |
|----|-----------------------|----------|---------------|---------------|
|  1 | anonymous@aliuken.com |          |               |               |
|  2 | aliuken@aliuken.com   | qwerty1  | administrator |               |
|  3 | luis@aliuken.com      | qwerty2  | supervisor    |             2 |
|  4 | marisol@aliuken.com   | qwerty3  | supervisor    |             2 |
|  5 | daniel@aliuken.com    | qwerty4  | user          |             1 |
|  6 | guti@aliuken.com      | qwerty5  | user          |             1 |
|  7 | raul@aliuken.com      | qwerty6  | user          |             1 |
|  8 | antonio@aliuken.com   | qwerty7  | user          |             1 |
|  9 | pai.mei@aliuken.com   | qwerty8  | user          |             1 |
```

where:
* The user **anonymous@aliuken.com** is used for the operations when the user is still not logged in. You cannot log in as the anonymous user as it doesn't have password and role.
* The priority order of the roles is: **administrator > supervisor > user > anonymous**.
* The **allowed views for each user role** are explained in section **[7.6. Other configurations](https://github.com/Aliuken/JobVacanciesApp_Java11#76-other-configurations)**.

> [!NOTE]
> The password of the user **guti@aliuken.com** can be changed by accessing, with a web browser, the link <http://localhost:8080/reset-password?email=guti@aliuken.com&uuid=a0396f47-50e8-470d-94ba-16f981cdfad6&languageParam=en>.
>
> The password of the user **raul@aliuken.com** can be changed by accessing, with a web browser, the link <http://localhost:8080/reset-password?email=raul@aliuken.com&uuid=9088793d-3e0e-4e5e-94a3-9c0267dc2dc5&languageParam=en>.
>
> The user **antonio@aliuken.com** cannot be used until it is confirmed via email (by accessing, with a web browser, the link <http://localhost:8080/signup-confirmed?email=antonio@aliuken.com&uuid=21d27fe6-4b57-413b-8361-2757c45294e3&languageParam=en>).
>
> The user **pai.mei@aliuken.com** cannot be used until it is confirmed via email (by accessing, with a web browser, the link <http://localhost:8080/signup-confirmed?email=pai.mei@aliuken.com&uuid=a0396f47-50e8-470d-94ba-16f981cdfad8&languageParam=en>).
>
> Those links aren't accessible 24 hours after the DB was created (using **mysql-dump.sql** or **h2-dump.sql**).
>
> As you can see in the previous links, a random UUID is used to "reset the password" and to "confirm the signup".

### 6.2. New credentials to access the application

You can **create new users** (for your personal email accounts) in two ways:
* Using the web application (through the **sign up**): So that users will be created with the role "user".
* Using SQL in the database (with **MySQL Workbench**): So that you can create users with any role.

Also, you can **modify the password** of a user using the web application, through the **reset password** option.

When using the **sign up** or **reset password** options, a new email is sent by [EmailServiceImpl](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/service/EmailServiceImpl.java) to confirm the operation (using [EmailDataDTO](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/EmailDataDTO.java) and the [EmailTemplateDTOs](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/EmailTemplateDTO.java) defined in [EmailConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/EmailConfig.java) for English and Spanish) based on the following page: <https://www.baeldung.com/spring-email>.

In order to send the email, you need to create a **Gmail SMTP account with an application password** (not an ordinary password) and pass to the application the account user and password (in the environment variables **APPLICATION_EMAIL_ACCOUNT_USER** and **APPLICATION_EMAIL_ACCOUNT_PASSWORD**).

Due to security changes made in Gmail SMTP accounts, an error is shown when trying to send an email. So, in order to confirm the **sign up** or **reset password** operation, you have to search for the link in the log written before the call to **sendMail(emailDataDTO)** (in **EmailServiceImpl**) and open it in a web browser. That log is:
* **For sign up**: Trying to send an email to '&lt;DestinationEmail&gt;' with the signup-confirmed link '&lt;Link&gt;'.
* **For reset password**: Trying to send an email to '&lt;DestinationEmail&gt;' with the reset-password link '&lt;Link&gt;'.

### 6.3. Credentials to access the Elastic Stack

The Elastic Stack has 2 predefined users with the following passwords:

```txt
| Username      | Password  |
|---------------|-----------|
| elastic       | changeme1 |
| kibana_system | changeme2 |
```

where:
* The **elastic** user must be used to access the Kibana (via <http://localhost:5601>).
* These credentials were created based on the **minimal security for Elasticsearch** (as indicated here: <https://www.elastic.co/guide/en/elasticsearch/reference/current/security-minimal-setup.html>).

## 7. Configuration and application properties

### 7.1. Configuration summary

The configuration of the application is defined in 3 ways:
* In the classes in the package [com.aliuken.jobvacanciesapp.config](https://github.com/Aliuken/JobVacanciesApp_Java11/tree/main/src/main/java/com/aliuken/jobvacanciesapp/config).
* In the following **application.properties** files:
    * [src/main/resources/application.properties](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/application.properties) (the main one).
    * [src/test/resources/application.properties](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/test/resources/application.properties) (the one used for tests).
* In the bean [ConfigPropertiesBean](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/ConfigPropertiesBean.java): Which reads the **ad-hoc properties** (the ones that start with "jobvacanciesapp.") from the corresponding **application.properties** file.

> [!NOTE]
> The **application.properties** files contain:
> * **Ad-hoc properties**: That start with "jobvacanciesapp." and are read by [ConfigPropertiesBean](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/ConfigPropertiesBean.java).
> * **Standard properties**: That don't start with "jobvacanciesapp." and are read by Spring Boot.
>
> The environment variable `EXTERNAL_FILES_PATH` is used to configure the external file paths.
>
> The properties that have file paths in the **application.properties** files start by `${EXTERNAL_FILES_PATH:C:/AppData_Java11/JobVacanciesApp}`, so that the Windows path `C:/AppData_Java11/JobVacanciesApp` is the default if the environment variable `EXTERNAL_FILES_PATH` is not set.

### 7.2. ConfigPropertiesBean

The bean [ConfigPropertiesBean](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/ConfigPropertiesBean.java) is declared to be:
* **Singleton**: Through the annotation @Component and a private constructor, so that the instance is only accessed through @Autowired.
* **Immutable**: Through the private constructor (where all its properties are set using Spring @Value annotation) and final attributes (with only their getters).

The properties read in [ConfigPropertiesBean](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/ConfigPropertiesBean.java) are called **ad-hoc properties** and their name always start with "jobvacanciesapp.". There are 3 types of ad-hoc properties:
* **Ad-hoc non-overwritable properties**: Which are properties that follow these rules:
    * Their initial value is defined in the file **application.properties**.
    * Their value never changes during the application execution.
    * They cannot be overwritten by any other properties.
* **Ad-hoc overwritable properties**: Which are properties that follow these rules:
    * Their initial value is defined in the file **application.properties**.
    * Their value never changes during the application execution.
    * They can be overwritten by ad-hoc overwriting properties.
* **Ad-hoc overwriting properties**: Which are properties that follow these rules:
    * Their initial value is defined in **ConfigPropertiesBean** (they are not defined in any properties file).
    * Their value can only be changed by the administrator by forcing an **application restart**.
    * They can overwrite the value of ad-hoc overwritable properties.

> [!NOTE]
> To deal with ad-hoc overwritable and overwriting properties, two classes were created:
> * [ConfigurableEnum](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/superinterface/ConfigurableEnum.java): To store the configurable property values as Java enums. Each ConfigurableEnum implementation must have at least the value **BY_DEFAULT** (which is used as the default value).
> * [ConfigurableEnumUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/javase/ConfigurableEnumUtils.java): To manage ConfigurableEnum elements.
>
> The DTO [ApplicationDefaultConfigDTO](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/dto/ApplicationDefaultConfigDTO.java) was created to show the current default property values in the page [applicationConfigForm.html](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/app/applicationConfigForm.html), which is used in the **application restart**.
>
> The purpose of the **application restart** is merely to overwrite ad-hoc properties and it can only be done by the administrator with the following sequence of calls:
> 1. **In sessionAuthUserForm.html** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/authUser/sessionAuthUserForm.html): GET /my-user/app/config
> 2. **In SessionAuthUserController** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/controller/SessionAuthUserController.java): GET /my-user/app/config **&rArr;** applicationConfigForm.html
> 3. **In applicationConfigForm.html** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/app/applicationConfigForm.html): POST /my-user/app/config
> 4. **In SessionAuthUserController** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/controller/SessionAuthUserController.java): POST /my-user/app/config **&rArr;** /logout
> 5. **In CustomAuthenticationHandler** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/security/CustomAuthenticationHandler.java): logout(...) **&rArr;** /login
> 6. **In loginForm.html** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/resources/templates/loginForm.html): T(com.aliuken.jobvacanciesapp.MainClass).restartApp(...)
> 7. **In MainClass** [[&#10138;]](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/MainClass.java): MainClass.restartApp(...) **&rArr;** restartExecutorService.submit **&rArr;** MainClass.springApplication.run(MainClass.args)
> 
> However, there is a [ConfigurableEnum](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/superinterface/ConfigurableEnum.java) implementation ([Currency](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/Currency.java)) that cannot be overwritten and whose default value is always the same (US_DOLLAR).

### 7.3. Ad-hoc non-overwritable properties

The ad-hoc non-overwritable properties are the following:
* **jobvacanciesapp.authUserCurriculumFilesPath**: Indicates the folder where the CVs are stored. Initial value: "/AppData_Java11/JobVacanciesApp/auth-user-curriculum-files/".
* **jobvacanciesapp.authUserEntityQueryFilesPath**: Indicates the folder where the entity query files are stored. Initial value: "/AppData_Java11/JobVacanciesApp/auth-user-entity-query-files/".
* **jobvacanciesapp.jobCompanyLogosPath**: Indicates the folder where the company logos are stored. Initial value: "/AppData_Java11/JobVacanciesApp/job-company-logos/".
* **jobvacanciesapp.useAjaxToRefreshJobCompanyLogos**: Indicates whether the company logos can be refreshed without reloading the full page (using AJAX) or not (using URL parameters). Initial value: "true". Possible values: "true" or "false".
* **jobvacanciesapp.useEntityManagerCache**: Indicates if the cache **entityManagerCache** is used or not to get the EntityManager for an AbstractEntity subclass. Initial value: "true". Possible values: "true" or "false".
* **jobvacanciesapp.useParallelStreams**: Indicates if the application should use parallel or sequential streams. Initial value: "true". Possible values: "true" or "false".
* **jobvacanciesapp.signupConfirmationLinkExpirationHours**: Indicates the expiration hours of the signup confirmation link.  Initial value: "24". Possible values: "longs".
* **jobvacanciesapp.resetPasswordLinkExpirationHours**: Indicates the expiration hours of the reset password link.  Initial value: "24". Possible values: "longs".

### 7.4. Ad-hoc overwritable properties

The ad-hoc overwritable properties are the following:
* **jobvacanciesapp.defaultAnonymousAccessPermissionValue**: Indicates whether anonymous users can enter the application or not. Initial value: "false". Possible values (from [AnonymousAccessPermission](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/AnonymousAccessPermission.java)):
    * **-**: Meaning "by default" and, in this property, is ignored and replaced by the value "F".
    * **T**: Meaning true (so that the anonymous access is allowed).
    * **F**: Meaning false (so that the anonymous access is not allowed).
* **jobvacanciesapp.defaultColorModeCode**: Indicates the default colorMode. Initial value: "L". Possible values (from [ColorMode](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/ColorMode.java)):
    * **-**: Meaning "by default" and, in this property, is ignored and replaced by the value "L".
    * **L**: Meaning that the default color mode is "light".
    * **D**: Meaning that the default color mode is "dark".
* **jobvacanciesapp.defaultLanguageCode**: Indicates the default application language. Initial value: "en". Possible values (from [Language](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/Language.java)):
    * **--**: Meaning "by default" and, in this property, is ignored and replaced by the value "en".
    * **en**: Meaning that the default language used is "English".
    * **es**: Meaning that the default language used is "Spanish".
* **jobvacanciesapp.defaultPdfDocumentPageFormatCode**: Indicates the page format for the query PDF documents. Initial value: "A4V". Possible values (from [PdfDocumentPageFormat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/PdfDocumentPageFormat.java)):
    * **---**: Meaning "by default" and, in this property, is ignored and replaced by the value "A4V".
    * **A3V**: Meaning "Vertical A3".
    * **A3H**: Meaning "Horizontal A3".
    * **A4V**: Meaning "Vertical A4".
    * **A4H**: Meaning "Horizontal A4".
* **jobvacanciesapp.defaultInitialTableSortingDirectionCode**: Indicates the default initialTableSortingDirection. Initial value: "asc". Possible values (from [TableSortingDirection](https://github.com/Aliuken/JobVacanciesApp_Java17/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TableSortingDirection.java)):
    * **---**: Meaning "by default" and, in this property, is ignored and replaced by the value "asc".
    * **asc**: Meaning "ascending".
    * **desc**: Meaning "descending".
* **jobvacanciesapp.defaultInitialTablePageSizeValue**: Indicates the default initialTablePageSize. Initial value: "5". Possible values (from [TablePageSize](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TablePageSize.java)):
    * **0**: Meaning "by default" and, in this property, is ignored and replaced by the value "5".
    * **5**, **10**, **25**, **50**, **100**, **250** and **500**: Meaning the quantity specified by the name.
* **jobvacanciesapp.defaultUserInterfaceFrameworkCode**: Indicates the UI framework of the application. Initial value: "M". Possible values (from [UserInterfaceFramework](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/UserInterfaceFramework.java)):
    * **-**: Meaning "by default" and, in this property, is ignored and replaced by the value "M".
    * **M**: Meaning that the UI framework used is "Material Design".
    * **B**: Meaning that the UI framework used is "Bootstrap".

### 7.5. Ad-hoc overwriting properties

The ad-hoc overwriting properties are the following:
* **jobvacanciesapp.defaultAnonymousAccessPermissionValueOverwritten**: Overwrites the anonymous-access configuration type (defined in prop "jobvacanciesapp.defaultAnonymousAccessPermissionValue"). Possible values (from [AnonymousAccessPermission](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/AnonymousAccessPermission.java)):
    * **-**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultAnonymousAccessPermissionValue** will be applied.
    * **T**: Meaning true (so that the anonymous access is allowed).
    * **F**: Meaning false (so that the anonymous access is not allowed).
* **jobvacanciesapp.defaultColorModeCodeOverwritten**: Overwrites the default colorMode (defined in prop "jobvacanciesapp.defaultColorModeCode"). Possible values (from [ColorMode](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/ColorMode.java)):
    * **-**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultColorModeCode** will be applied.
    * **L**: Meaning that the default color mode is "light".
    * **D**: Meaning that the default color mode is "dark".
* **jobvacanciesapp.defaultLanguageCodeOverwritten**: Overwrites the default app language (defined in prop "jobvacanciesapp.defaultLanguageCode"). Possible values (from [Language](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/Language.java)):
    * **--**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultLanguageCode** will be applied.
    * **en**: Meaning that the default language is "English".
    * **es**: Meaning that the default language is "Spanish".
* **jobvacanciesapp.defaultPdfDocumentPageFormatCodeOverwritten**: Overwrites the page format for the query PDF documents (defined in prop "jobvacanciesapp.defaultPdfDocumentPageFormatCode"). Possible values (from [PdfDocumentPageFormat](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/PdfDocumentPageFormat.java)):
    * **---**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultPdfDocumentPageFormatCode** will be applied.
    * **A3V**: Meaning "Vertical A3".
    * **A3H**: Meaning "Horizontal A3".
    * **A4V**: Meaning "Vertical A4".
    * **A4H**: Meaning "Horizontal A4".
* **jobvacanciesapp.defaultInitialTableSortingDirectionCodeOverwritten**: Overwrites the default initialTableSortingDirection (defined in prop "jobvacanciesapp.defaultInitialTableSortingDirectionCode"). Possible values (from [TableSortingDirection](https://github.com/Aliuken/JobVacanciesApp_Java17/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TableSortingDirection.java)):
    * **---**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultInitialTableSortingDirectionCode** will be applied.
    * **asc**: Meaning "ascending".
    * **desc**: Meaning "descending".
* **jobvacanciesapp.defaultInitialTablePageSizeValueOverwritten**: Overwrites the default initialTablePageSize (defined in prop "jobvacanciesapp.defaultInitialTablePageSizeValue"). Possible values (from [TablePageSize](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/model/entity/enumtype/TablePageSize.java)):
    * **0**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultInitialTablePageSizeValue** will be applied.
    * **5**, **10**, **25**, **50**, **100**, **250** and **500**: Meaning the quantity specified by the name.
* **jobvacanciesapp.defaultUserInterfaceFrameworkCodeOverwritten**: Overwrites the UI framework of the application (defined in prop "jobvacanciesapp.defaultUserInterfaceFrameworkCode"). Possible values (from [UserInterfaceFramework](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/UserInterfaceFramework.java)):
    * **-**: The initial value. Meaning "by default", so that the configuration of the property **jobvacanciesapp.defaultUserInterfaceFrameworkCode** will be applied.
    * **M**: Meaning that the UI framework is "Material Design".
    * **B**: Meaning that the UI framework is "Bootstrap".

### 7.6. Other configurations

Other configurations include:

* The **allowed views for each user role** (anonymous, user, supervisor and administrator), which:
    * Depend on the value of the properties **jobvacanciesapp.anonymousAccessPermissionOverwritten** and **jobvacanciesapp.defaultAnonymousAccessPermissionValue**.
    * Are retrieved in the call **AllowedViewsEnum.getInstance(anonymousAccessAllowed)** of [WebSecurityConfig](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/config/WebSecurityConfig.java).

* The **allowed file extensions for CVs and company logos**, which are:
    * Declared in the enums **FileType.USER_CURRICULUM** and **FileType.COMPANY_LOGO** respectively.
    * Validated in the methods **isAllowedFileExtension** and **checkAllowedFileExtension** of [FileType](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/FileType.java).

> [!NOTE]
> The CV/logo files can be compressed into a zip file. The method **getFolderAllowedFilesRecursive** (in [FileType](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/enumtype/FileType.java)) searches recursively the files with the allowed extensions in the whole list of folders/files inside the folder obtained when the zip file is unzipped (with the method **uploadAndUnzipFile** of [FileUtils](https://github.com/Aliuken/JobVacanciesApp_Java11/blob/main/src/main/java/com/aliuken/jobvacanciesapp/util/persistence/file/FileUtils.java)).