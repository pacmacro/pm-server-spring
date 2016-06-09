#!/bin/sh

if [ $# -ge 1 ] ; then
    curl \
      --request GET --include \
      -H "Content-Type: application/json" \
      http://localhost:8080/ghost/"$1"/location
else
    echo "Usage: ./getLocationById id"
fi
