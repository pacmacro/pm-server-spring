#
# This Dockerfile contains the configuration for running the Pac Macro server locally.
#

FROM ubuntu:18.04
MAINTAINER Jeffrey Leung

RUN apt-get update -y
RUN apt-get upgrade -y

RUN apt-get install -y git openjdk-8-jdk maven

RUN git clone https://github.com/pacmacro/pm-server.git
WORKDIR pm-server

EXPOSE 8080
RUN mvn clean package spring-boot:repackage

CMD ./docker/docker_run_cmd.sh