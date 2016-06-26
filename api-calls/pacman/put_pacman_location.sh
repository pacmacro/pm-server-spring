#!/bin/sh

if [ $# -ge 2 ] ; then
    curl \
      --request PUT --include \
      http://localhost:8080/pacman/location/"$1"/"$2"
else
    echo "Usage: ./put_pacman_location.sh latitude longitude"
fi
