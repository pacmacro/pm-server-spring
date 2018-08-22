#!/bin/sh

cd $(dirname $0)/..

echo ""
echo "___________________________________________________________"
echo ""
echo "  This Docker image is generated from the following repositories:"
echo "    https://github.com/pacmacro/pm-server"
echo "    https://hub.docker.com/r/pacmacro/pm-server/"
echo ""
echo "__________________"
echo ""
echo "  The Dockerfile is as follows:"
echo ""
cat docker/Dockerfile
echo ""
echo "___________________________________________________________"
echo ""

git pull

mvn spring-boot:run

