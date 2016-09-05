#!/bin/sh

cd $(dirname $0)/..

git pull
mvn spring-boot:run
