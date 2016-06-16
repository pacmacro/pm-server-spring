# Pac Macro Server

Java & Spring Boot server for relaying information between devices for the game PacMacro.

The newest stable version of the server is running at [Heroku](http://pacmacro.herokuapp.com/).  
The Docker image is located at [pacmacro/pm-server](https://hub.docker.com/r/pacmacro/pm-server/).

To set up the server on your local machine (for development or for prototyping other projects), see [CONTRIBUTING.md](https://github.com/pacmacro/pm-server/blob/master/CONTRIBUTING.md).

## Software Stack

| Purpose | Technology |
| --- | --- |
| Development Language | Java 1.8 |
| Web Framework | [Spring Boot 1.3.5](http://projects.spring.io/spring-boot/) |
| Testing Framework | [JUnit 4.12](http://junit.org/junit4/) |
| Logging Utility | [Apache Log4j2](http://logging.apache.org/log4j/2.x/) |
| Build System | [Apache Maven](https://maven.apache.org/) |
| Deployment | [Heroku](http://heroku.com/) |
| Application Container | [Docker](https://www.docker.com/) |

## API Documentation

For documentation, see [the wiki](https://github.com/pacmacro/pm-server/wiki/API-Documentation).

For example calls using cURL, see the directory *api-calls/*.
