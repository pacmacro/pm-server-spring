#!/bin/sh

if [ $# -ge 2 ] ; then
    curl \
      --request POST --include \
      http://localhost:8080/ghost/"$1"/"$2"
else
    echo "Usage: ./post_new_ghost.sh latitude longitude"
fi
