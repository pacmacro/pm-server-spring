#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 1 ] ; then
    curl \
      --request GET --include \
      -H "Content-Type: application/json" \
      $base_url/ghost/"$1"/state
else
    echo "Usage: ./get_ghost_state_by_id.sh id"
fi
