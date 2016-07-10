#!/bin/sh

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request PUT \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$1',"longitude":'$2'}' \
      http://localhost:8080/pacman/location
else
    echo "Usage: ./put_pacman_location.sh latitude longitude"
fi
