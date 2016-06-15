#
# This Dockerfile contains the configuration for running the Pac Macro server locally.
#

FROM ubuntu
MAINTAINER Jeffrey Leung

RUN apt-get -y update
RUN apt-get -y upgrade

RUN apt-get install -y git
RUN apt-get install -y openjdk-8-jdk
RUN apt-get install -y maven

RUN git clone https://github.com/pacmacro/pm-server.git

RUN mvn -f pm-server/ clean package spring-boot:repackage

CMD ./pm-server/docker/docker_run_cmd.sh 
