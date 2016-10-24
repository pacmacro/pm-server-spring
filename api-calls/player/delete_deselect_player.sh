#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)
player_names=$(../utility/player_names.sh)

if [ $# -ge 1 ] ; then
    curl \
      --request DELETE --include \
      $base_url/player/"$1"
else
    echo "Usage: ./delete_player_by_name.sh name"
    echo ""
    echo "$player_names"
    echo ""
fi
