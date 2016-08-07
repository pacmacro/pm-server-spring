#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)

if [ $# -ge 1 ] ; then
    curl \
      --request GET --include \
      -H "Content-Type: application/json" \
      $base_url/player/"$1"/state
else
    echo "Usage: ./get_player_state.sh name"
fi
