#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)
player_names=$(../utility/player_names.sh)

if [ $# -ge 3 ] ; then
    curl \
      --include \
      --request POST  \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$2',"longitude":'$3'}' \
      $base_url/player/$1
else
    echo "Usage: ./post_select_player.sh name latitude longitude"
    echo ""
    echo "$player_names"
    echo ""
fi
