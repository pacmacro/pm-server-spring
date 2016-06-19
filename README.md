# Pac Macro Server

[![Build Status](https://travis-ci.org/pacmacro/pm-server.svg?branch=master)](https://travis-ci.org/pacmacro/pm-server)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)](https://github.com/pacmacro/pm-server/blob/master/LICENSE)

Java & Spring Boot server for relaying information between devices for the game PacMacro.

The newest stable version of the server is running at [Heroku](http://pacmacro.herokuapp.com/).  
The Travis CI service (continuous integration for tests) is located at [pacmacro/pm-server](https://travis-ci.org/pacmacro/pm-server).  
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
| Logging Utility | [Apache Log4j2](http://logging.apache.org/log4j/2.x/) |
| Build System | [Apache Maven](https://maven.apache.org/) |
| Deployment | [Heroku](http://heroku.com/) |
| Application Container | [Docker](https://www.docker.com/) |
