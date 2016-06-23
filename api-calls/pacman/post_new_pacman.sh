#!/bin/sh

if [ $# -ge 2 ] ; then
    curl \
      --request POST --include \
      http://localhost:8080/pacman/"$1"/"$2"
else
    echo "Usage: ./post_new_pacman.sh latitude longitude"
fi
