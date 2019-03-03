# CONTRIBUTING

## When Making Contributions

When updating the server version, update it in both `pom.xml` (for the actual version)  and `Procfile` (to find the file to deploy).

## Setup

### Manual Setup / Development Setup

To run the server on your local machine, you'll need [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) as a JDK, as well as [Apache Maven](https://maven.apache.org/install.html).

My personal preference for setup is to run the following:

```shell
sudo apt-get install -y git openjdk-8-jdk maven
```

Got an error? Your dependencies may not be up to date. Try executing the following then re-running the installs:

```shell
sudo apt-get update
sudo apt-get upgrade
```

Now, clone the repository into your current directory:

```shell
git clone https://github.com/pacmacro/pm-server.git
cd pm-server
```

Finally, run the Spring Boot application using Maven:

```shell
mvn spring-boot:run
```

Which compiles, packages, and runs the application in an instance of Apache Tomcat. The console output will tell you how to contact the server (e.g. port `8080` means `http://localhost:8080/`), and you can close the server by pressing `Ctrl + c`.

If you have any problems whatsoever, contact us or open an issue, and someone else working on the project will be more than happy to help!

### Docker Setup (Alternative)

A Docker image is available for a quick and minimal way to run the server on your local machine.

First, install Docker using the installation instructions for your environment at the [official Docker website](https://www.docker.com/).

Download, build, and run the Docker image located at [pacmacro/pm-server](https://hub.docker.com/r/pacmacro/pm-server/):

```
docker run -t -p 8080:8080 pacmacro/pm-server
```

This includes a flag to map port 8080 of the host machine to port 8080 of the virtual machine (which the Spring Boot server runs on by default).

Now you can access the server from your local machine using the URI `http://localhost:8080/`.

If all goes well, your server should be up and running in the terminal (after a long initialization period which will only happen the first time).

You can exit the process while leaving it running in the background by pressing `Ctrl + c`. To check the running process, execute:

```shell
docker ps
```

which will display the ID of the container currently running, and you can use this to kill the process:

```shell
docker kill CONTAINER_ID
```

## Software Architecture

<a target="_blank" href="https://www.draw.io/?lightbox=1&highlight=0000ff&edit=_blank&layers=1&nav=1&title=Server%20(Java)%3A%20Architecture#R7Vtbc6M2FP41fkzGQoDJY%2BJkdzuzmaababf7qICC1cqIFSKx99dXAgkMBpvEF3DcPCToSOhyvu%2FoXByP4HS%2B%2BMxRPLtnAaYjaxwsRvB2ZFnAdV35R0mWucQD41wQchLoQaXgkfzCWmiGpSTASWWgYIwKEleFPosi7IuKDHHOXqvDnhmtrhqjEK8JHn1E16XfSSBm%2BhTOuJR%2FwSScmZXBWPfMkRmsBckMBex1RQTvRnDKGRP503wxxVQpz%2Bglf%2B9TS2%2BxMY4j0eWFr3%2F8msff6dMT5o%2B%2F%2F%2FX50fn05%2F0FgHpzYmlOjAOpAN1kXMxYyCJE70rpDWdpFGA17Vi2yjFfGYulEEjhP1iIpUYTpYJJ0UzMqe7N11QLtZ5FixKWch9vOoDmBOIhFhvGOYXGJVUxm2PBl%2FI9jikS5KW6D6Q5ExbjSrXKB63Zt2g5n%2FcF0VSvNLJcKrd78yQfQvUwZZHgjFLMTZdcqegthnMj%2BYZ9LPedyGmvH37LTvIzxYlI1oeuIVzip8B4nRGBH2OUqflVWnETVi%2BYC7zYjNa6dotbQJuAvgOgab%2BWFgWMmcxWrMmM2zsghU0elfZSXXz5t3r%2F0jHNH3q6rHG7qLSWurVHc7E6mguY9Gkv1nnbCxycvQD7TO3F6WgvXp%2Fm4mw3l3sUyUilo618QVFAM1NBavF69zyV4deFVCUR6oyUhcQfoBXVvY7t9W1F7nacjOBZXm8V9bk%2FU2Y6LpLMYq7lAODFi7LTzPJA0bIEu8TkTmNWkCBfprp0lRs1ECUcoopUIq%2Fhf%2FGUUcalJGKRMv1nQmlNhCgJI9n0JXxyb%2FBGgUtkoH2tO%2BYkCLJ7o4ka1btkH%2Bywq%2BwATgM73AZ2eIdix%2BRo7EB%2BwMT%2F7NjkgQfHDuD04YH36Em9jp70qk9P6m23wW84JEm2VhdXek2pKkFIHWbGdZHE2CfPyl9KJBlXxQdVKohInKrzsegEHKk77tuRXnWBKWYJkSruCNQNSjJUAiRQpm6e%2BiLlOKsZ8VW4Bo%2FPxOobH1O8qwBU01IyQ7F6TEgUUnytSnVSHQHh2M%2FsQPawVG1%2FmwazKp%2BpzY0v4eqPdeXYZoi%2B5OQIAGzLcyb5b6XZfaBgjZ0KCrDBSmADCM7BQGjPogPy0hg%2BKP99oV2xih8ofhbr8YOZJYlRVNxzqzn1isWtjlkRZxuoSve%2FJ7lOXAmEO%2B7nowY1wKsy1GooK0waGAoPxtCrE49pgLtjsKJffWAkC%2Bg1ULbVcqGbKfIgSr9VA6HYxg64dEhUzy8Mqlfl%2Bg%2BDgHfq9qNz3u3l6F0NbTc9d0jNz7DAVjeI%2Fgts5gznFHfaQ4s7jc7POu6slNjOPO6EQ4s7rU5lnrOpH9Tv8f7rB7BD%2FQBHgb68b32KEqX9vXx4iBdErLwmWz%2FMjPK5fEk1evzAsS0iWsHM2XDr75iheFXGOPWyd37MtQRlbR4AaxPVbf7AmQ7sEDB8UKJ1jr1bPto%2BDtEAqCVddYJ0Zpq7hbKHZhps9Tl5bPDeT%2FPWPNed8C83%2BayWaGSDQ%2FowkYgzuI%2F1oL39AvpoGYs7tIwFtv8DzbAylg9rl0PLEGBTxac9LtBaDVAyK%2BL2d1TM%2BnKybr3eXLe0rk62PhGcHNnJNiV2bw3nhhCW9cUEpxaPu2BPTCiK5cdiQtO%2FCJwWE1q%2BsXAkItSvBPtEiWD23UeG150Mw07C6hg67yVD31zokO2%2FqfjWEFmNdo%2BG6kURy16PhoDXBPfB0hTbOgnNySzm0umgO3BU3TVl%2FsPTnXNc3clm%2BdXH3MDLL5DCu%2F8A">
  <img src="readme-img/server-architecture.png" alt="Server Architecture diagram">
</a>

When an API request is received by the server, it is initially sent through a Controller, which does minimal parsing, verification, and data storage/retrieval.

The relevant parsed information is passed to a Manager which processes the information and retrieves/saves any relevant data to/from the Registries.

Each Registry is a thin wrapper designed to provide basic contextual setup and access for a Repository, which is built simply to store and retrieve from a collection of a single unit of data.

## Deployment Pipeline

When a change is pushed to GitHub, two processes are triggered and run concurrently. A [Docker image](https://hub.docker.com/r/pacmacro/pm-server) is built and a [Travis CI](https://www.travis-ci.org/pacmacro/pm-server) build is run. If the Travis CI build passes all tests, then Heroku will build the WAR file, deploy it to a Heroku dyno, and spin up the server at [this URL](http://pacmacro.herokuapp.com/).

## Design Decisions

* _Request_ classes (e.g. `LocationRequest`) are designed as String containers for a specific HTTP request body. The String for each field in the request body is parsed into a specific value by the `ValidationUtils` class.  
_Response_ classes (e.g. `GameStateResponse`) are designed as Java object containers for a specific HTTP response body. Values such as enums are directly assigned to the class members, which are automatically parsed into a response body by Jackson.
