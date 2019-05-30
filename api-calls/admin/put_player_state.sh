#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)
player_names=$(../utility/player_names.sh)
player_states=$(../utility/player_states.sh)

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request PUT  \
      --header "Content-Type: application/json" \
      --data '{"state":"'$2'"}' \
      $base_url/admin/player/"$1"/state
else
    echo "Usage: ./put_player_state.sh name state"
    echo ""
    echo "$player_names"
    echo ""
    echo "$player_states"
    echo ""
fi
