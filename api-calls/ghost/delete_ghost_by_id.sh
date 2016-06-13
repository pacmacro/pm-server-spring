#!/bin/sh

if [ $# -ge 1 ] ; then
    curl \
      --request DELETE --include \
      http://localhost:8080/ghost/"$1"
else
    echo "Usage: ./delete_ghost_by_id.sh id"
fi
