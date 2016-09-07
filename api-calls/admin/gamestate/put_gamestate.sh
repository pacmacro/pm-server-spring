#!/bin/sh

cd $(dirname $0)
base_url=$(../../utility/base_url.sh)
game_states=$(../../utility/game_states.sh)

if [ $# -ge 1 ] ; then
    curl \
      --include \
      --request PUT  \
      --header "Content-Type: application/json" \
      --data '{"state":"'$1'"}' \
      $base_url/admin/gamestate
else
    echo "Usage: ./put_gamestate.sh state"
    echo ""
    echo "$game_states"
    echo ""
fi
