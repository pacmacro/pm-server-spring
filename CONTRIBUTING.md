# CONTRIBUTING

## When Making Contributions

When updating the server version, update it both in the file `pom.xml` (for the actual version)  and the file `Procfile` (to find the file to deploy).

## Setup

### Manual Setup / Development Setup

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

### Docker Setup

A Docker image is available for a quick and minimal way to run the server on your local machine.

First, install Docker using the installation instructions for your environment at the [official Docker website](https://www.docker.com/).

Download and build the Docker image located at [pacmacro/pm-server](https://hub.docker.com/r/pacmacro/pm-server/):

```
docker run -t pacmacro/pm-server
```

If all goes well, your server should be up and running in the terminal (after a long initialization period which will only happen the first time).

The Docker image runs within a virtual machine, so the ports aren't accessible from your local machine without a little manual port configuration. Exit the process while leaving it running in the background by pressing `Ctrl + c`. Check the running process, and kill it with its container ID:

```
docker ps
docker kill CONTAINER_ID
```

This time, run the image while configuring port 8080 of the virtual machine (which the Spring Boot server runs on by default) to be mapped:

```
docker run -t -p 8080 pacmacro/pm-server
```

Finally, `Ctrl + c` out of the process and find the mapped port:

```
docker ps
```

The port mapping should look like `0.0.0.0:22222->8080/tcp`. This tells you that port 22222 of the host machine is mapped to port 8080 of the virtual machine. Now you can access the server from your local machine, with your URI looking something like `http://localhost:22222/`!

From now on, every time you run the server, you should bring it up with `docker run -t -p 8080 pacmacro/pm-server` and find the new URI, because Docker will map the port to any arbitrary available port when it runs.

## Software Architecture

When a request is received by the server, it is initially sent through a Controller, which does minimal parsing, verification, and data storage/retrieval. The relevant information is passed to a Manager (to be created upon an imminent refactor) which processes the information and retrieves/saves any relevant data to/from the Registries. Each Registry is a thin wrapper designed to provide basic contextual setup and access for a Repository, which is built simply to store and retrieve from a collection of a single unit of data.

## Deployment Pipeline

When a change is pushed to GitHub, two processes are triggered and run concurrently. A [Docker image](https://hub.docker.com/r/pacmacro/pm-server) is built and a [Travis CI](travis-ci.org/pacmacro/pm-server) build is run. If the Travis CI build passes all tests, then Heroku will build the WAR file, deploy it to a Heroku dyno, and spin up the server at [this URL](http://pacmacro.herokuapp.com/).

## Design Decisions

* _Request_ classes (e.g. `LocationRequest`) are designed as String containers for a specific HTTP request body. The String for each field in the request body is parsed into a specific value by the `ValidationUtils` class.  
_Response_ classes (e.g. `GameStateResponse`) are designed as Java object containers for a specific HTTP response body. Values such as enums are directly assigned to the class members, which are automatically parsed into a response body by Jackson.
