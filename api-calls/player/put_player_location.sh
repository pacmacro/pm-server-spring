#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 3 ] ; then
    curl \
      --include \
      --request PUT  \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$2',"longitude":'$3'}' \
      $base_url/player/"$1"/location
else
    echo "Usage: ./put_player_location.sh name latitude longitude"
fi
