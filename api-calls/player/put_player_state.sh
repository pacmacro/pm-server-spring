#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request PUT  \
      --header "Content-Type: application/json" \
      --data '{"state":"'$2'"}' \
      $base_url/player/"$1"/state
else
    echo "Usage: ./put_player_state.sh name state"
fi
