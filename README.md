# Spring Boot Starter Homework Project

This project is a homework for [T1 Open School of Java development][1], demonstrating creation of a project with [autoconfiguration][2] known as Spring Boot **Starter** project. The project implements the functionality of logging incoming and outgoing HTTP requests & responses for [Spring MVC][3] project. It can be easily added as a dependency to another project to provide mentioned functionality.

## Prerequisites

In order to run this project you should have a Spring MVC project. The starter project itself contains simple internal API that is used for unit tests, so it can be used to check the functionality also. You can use one of the other homework projects, for example the [AOP Homework project][6].

## Details

Project employs Java EE [Filters][4] and Spring MVC [Interceptors][5] for logging requests and responses. Project is divided into following packages:

- *autoconfigure*
    
    Classes used for autoconfiguration, it's the core of Starter project. **LoggerAutoConfiguration** class declares two beans using *@ConditionalOnProperty* annotation. It enables provision of specific implementation of **LogProcessor** interface on the basis of configuration properties set by means of `application.properties` file. The class features *@AutoConfiguration* and *@EnableConfigurationProperties* annotations: first to enable the autoconfiguration feature and the second defines the class used to map the properties - **LoggerProperties** class in this case. **WebMvcConfigurer** class also makes use of configuration properties, declaring a FilterRegistrationBean for registration of our LoggingFilter and overriding *addInterceptors()* method for registering our LoggingInterceptor. The Filter is registered early in the filter chain with `OrderedFilter.HIGHEST_PRECEDENCE + 1` to be able to measure request processing time. 
- *exception*

    Contains exception class specific for this project.
- *filter*

    **LoggingFilter** class extends **OncePerRequestFilter** class, which guarantees a single execution per request dispatch, on any servlet container. Overridden *doFilterInternal()* method preforms logging of incoming request and partly response, using own wrapper classes **CachedBodyHttpServletRequest** and **LoggingResponseWrapper** to avoid errors accessing request/response body. Time elapsed during request processing is also tracked and logged here.
    - *utility*
        
        Contains utility classes for filter
- *init*

    **LoggerEnvironmentPostProcessor** is used for checking the configuration properties, it will prevent execution if property value is not accepted or no file path & name is provided when logging format is set to `FILE`. It also transforms read properties to uppercase for convenience.  
- *interceptor*

    **LoggingInterceptor** performs logging of outcoming requests or responses, using overridden *afterCompletion()* method. 
- *logger*

    Contains simple interface **LogProcessor**, containing only method *processLog(String logEntry)*, and two implementing classes **ConsoleLogger** and **FileLogger**. Implementations are pretty straightforward and may be suboptimal as it was not the focus of this project.

Also to notice are

- `resources/META-INF/`

    - `spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
    
        this file used by Spring to import classes used in autoconfiguration process

  - `spring.factories`

    this file imports our implementation of Spring **EnvironmentPostProcessor**

## Configuration properties

`log.http.starter.loggingLevel` = [`INFO`, `DEBUG`, `TRACE`] - sets logging level. Level may be not particularly precise here as it was not the focus of the project.

`log.http.starter.loggingFormat`= [`CONSOLE`, `FILE`] - sets logging format. If the `FILE` format is set, next property must be set.

`log.http.starter.loggingFile` - must contain full path to the log file to write.

`log.http.starter.rewriteLog` = [`TRUE`, `FALSE`] - controls if the log file will be overwritten or appended to.

## Use instructions

Add starter project to the dependencies of a project with which you want to use it with. For Maven:

```
<groupId>com.t1.openschool.atumanov</groupId>
<artifactId>log-http-spring-boot-starter</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

For Gradle:

```
implementation 'com.t1.openschool.atumanov:log-http-spring-boot-starter:0.0.1-SNAPSHOT'
```

Set preferred logging level and format in your `application.properties` file (see [configuration properties](#configuration-properties))

Check console or logfile after sending or receiving HTTP requests

## Internal API (in `test` package)

Project uses Spring-embedded H2 database to store test entities that feature name (as primary key), `enum` type, `integer` and `double` values. API is used for usual CRUD operations on this storage in tests.

### `TestEntity`:

- **string** name
- **enum** [`TYPE_A`, `TYPE_B`, `TYPE_C`]
- **integer** integerValue
- **double** doubleValue

> On application start 3 test entities are added to the database automatically

---

- `GET` /entity

Returns all entities
- parameters:
  - *none*
- responses:
  - `200` successful operation
    - **array** of [`TestEntity`](#testentity)
  - `500` internal error

---

- `GET` /entity/{param}

Returns entities by name or type
- parameters:
  - **string** param Entity name or type, depending on query parameter `parameterType`
  - **string** parameterType Type of path parameter `param`, can be either `NAME` or `TYPE`
- responses:
  - `200` successful operation
    - **array** of [`TestEntity`](#testentity)
  - `404` not found

---

- `POST` /entity

Returns entities by name or type
- parameters:

  *none*
- request body:
  [`TestEntity`](#testentity)
- responses:
  - `200` successful operation
    - [`TestEntity`](#testentity)

---

- `PUT` /entity

Returns entities by name or type
- parameters:

  *none*
- request body:
  [`TestEntity`](#testentity)
- responses:
  - `200` successful operation
    - [`TestEntity`](#testentity)
  - `404` not found

---

- `DELETE` /entity/{name}

Returns entities by name or type
- parameters:
  - **string** name Name of entity to delete
- responses:
  - `200` successful operation

---

[1]: https://t1.ru/internship/item/otkrytaya-shkola-dlya-java-razrabotchikov/
[2]: https://docs.spring.io/spring-boot/reference/using/auto-configuration.html
[3]: https://docs.spring.io/spring-framework/reference/web/webmvc.html
[4]: https://docs.oracle.com/javaee/6/tutorial/doc/bnagb.html
[5]: https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-config/interceptors.html
[6]: https://github.com/Wolkenkind/method_time_system