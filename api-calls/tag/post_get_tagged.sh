#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)
player_names=$(../utility/player_names.sh)

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request POST  \
      --header "Content-Type: application/json" \
      --data '{"source":"'$2'"}' \
      $base_url/tag/$1
else
    echo "Usage: ./post_get_tagged.sh player taggerplayer"
    echo ""
    echo "$player_names"
    echo ""
fi
