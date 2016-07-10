#!/bin/sh

if [ $# -ge 3 ] ; then
    curl \
      --include \
      --request PUT  \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$2',"longitude":'$3'}' \
      http://localhost:8080/ghost/"$1"/location
else
    echo "Usage: ./put_ghost_location_by_id.sh id latitude longitude"
fi
