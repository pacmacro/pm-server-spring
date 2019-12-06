# Pac Macro Server

[![Build Status](https://travis-ci.org/pacmacro/pm-server-spring.svg?branch=master)](https://travis-ci.org/pacmacro/pm-server-spring)
[![License](https://img.shields.io/github/license/pacmacro/pm-server-spring)](https://github.com/pacmacro/pm-server-spring/blob/master/LICENSE)

Java & Spring Boot RESTful server for relaying information between devices for the game Pac Macro.  
The official specification for the game can be found [here](https://github.com/pacmacro/pm-specification).

[![System Architecture diagram](readme-img/system-architecture.png)](https://cloudcraft.co/view/e364e7e3-cdc5-48e8-9b5f-82d7ba0d95a6?key=BhmvffJBoBU73zAUh8X22A&embed=true)

The newest stable version of the server is running at [Heroku](http://pacmacro-spring.herokuapp.com/).  
The Travis CI service (continuous integration for tests) is located at [pacmacro/pm-server-spring](https://travis-ci.org/pacmacro/pm-server-spring).  
The Docker image is located at [pacmacro/pm-server](https://hub.docker.com/r/pacmacro/pm-server/).

To set up the server on your local machine (for development or for prototyping other projects), see [CONTRIBUTING.md](https://github.com/pacmacro/pm-server/blob/master/CONTRIBUTING.md).

## API Documentation

For comprehensive and clear documentation, see [the wiki](https://github.com/pacmacro/pm-server/wiki/API-Documentation).

For example calls using cURL, see the directory [api-calls/](https://github.com/pacmacro/pm-server/tree/master/api-calls).

## Software Stack

| Purpose | Technology |
| --- | --- |
| Development Language | Java 1.8 |
| Web Framework | [Spring Boot 1.3.5](http://projects.spring.io/spring-boot/) |
| Testing Framework | [JUnit 4.12](http://junit.org/junit4/) |
| Continous Integration (Testing) | [Travis CI](https://travis-ci.org/)
| Logging Utility | [Apache Log4j 2](http://logging.apache.org/log4j/2.x/) |
| Build System | [Apache Maven](https://maven.apache.org/) |
| Deployment | [Heroku](http://heroku.com/) |
| Application Container | [Docker](https://www.docker.com/) |

## Credits

This project is brought to you in part by:

* ![Mobify logo](readme-img/mobify-logo.png) [Mobify](https://www.mobify.com/about/), a sponsor of this Pac Macro implementation
* [Andy Lumb (alumb)](https://github.com/alumb), the original creator of Pac Macro with the SFU CSSS
