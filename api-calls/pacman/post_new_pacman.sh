#!/bin/sh

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request POST \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$1',"longitude":'$2'}' \
      http://localhost:8080/pacman
else
    echo "Usage: ./post_new_pacman.sh latitude longitude"
fi
