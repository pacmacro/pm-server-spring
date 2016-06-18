#!/bin/sh

if [ $# -ge 3 ] ; then
    curl \
      --request PUT --include \
      http://localhost:8080/ghost/"$1"/"$2"/"$3"
else
    echo "Usage: ./put_location_by_id.sh latitude longitude"
fi
