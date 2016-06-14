# Pac Macro Server

Java & Spring Boot server for relaying information between devices for the game PacMacro.

The newest stable version of the server is running at [Heroku](http://pacmacro.herokuapp.com/).

## Setup

To run the server on your local machine, you'll need [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) as a JDK, as well as [Apache Maven](https://maven.apache.org/install.html).

My personal preference for setup is to run the following:

```
sudo apt-get install -y git openjdk-8-jdk maven
```

Got an error? Your dependencies may not be up to date, so try executing this and then re-running the installs:

```
sudo apt-get update
sudo apt-get upgrade
```

Now, clone the repository into your current directory:

```
git clone https://github.com/pacmacro/pm-server.git
cd pm-server
```

Finally, run the Spring Boot application using Maven:

```
mvn spring-boot:run
```

Which compiles, packages, and runs the application in an instance of Apache Tomcat. The console output will tell you how to contact the server (e.g. port `8080` means `http://localhost:8080/`), and you can close the server by pressing `Ctrl + c`.

If you have any problems whatsoever, [too bad, deal with them yourselves like adults](https://www.youtube.com/watch?v=YUrpjEuBUtk).  
Nah, I'm just kidding, open an issue! I (or someone else working on the project) would be more than happy to help.

## Software Stack

| Purpose | Technology |
| --- | --- |
| Development Language | Java 1.8 |
| Web Framework | [Spring Boot](http://projects.spring.io/spring-boot/) |
| Testing Framework | [JUnit 4.12](http://junit.org/junit4/) |
| Logging Utility | [Apache Log4j2](http://logging.apache.org/log4j/2.x/) |
| Build System | [Apache Maven](https://maven.apache.org/) |
| Deployment | [Heroku](http://heroku.com/) |

## API Documentation

For documentation, see [the wiki](https://github.com/pacmacro/pm-server/wiki/API-Documentation).

For example calls using cURL, see the directory *api-calls/*.
