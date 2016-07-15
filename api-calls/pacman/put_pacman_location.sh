#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request PUT \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$1',"longitude":'$2'}' \
      $base_url/pacman/location
else
    echo "Usage: ./put_pacman_location.sh latitude longitude"
fi
