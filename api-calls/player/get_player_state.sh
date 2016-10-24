#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)
player_names=$(../utility/player_names.sh)

if [ $# -ge 1 ] ; then
    curl \
      --request GET --include \
      -H "Content-Type: application/json" \
      $base_url/player/"$1"/state
else
    echo "Usage: ./get_player_state.sh name"
    echo ""
    echo "$player_names"
    echo ""
fi
