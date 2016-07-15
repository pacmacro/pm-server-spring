#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 1 ] ; then
    curl \
      --request DELETE --include \
      $base_url/ghost/"$1"
else
    echo "Usage: ./delete_ghost_by_id.sh id"
fi
