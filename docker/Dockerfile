#
# This Dockerfile contains the configuration for running the Pac Macro server locally.
#

FROM ubuntu
MAINTAINER Jeffrey Leung

RUN apt-get update -y
RUN apt-get upgrade -y

RUN apt-get install -y git
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y maven

RUN git clone https://github.com/pacmacro/pm-server.git
WORKDIR pm-server

RUN mvn clean package spring-boot:repackage

CMD ./docker/docker_run_cmd.sh
