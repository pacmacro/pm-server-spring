#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request POST  \
      --header "Content-Type: application/json" \
      --data '{"latitude":'$1',"longitude":'$2'}' \
      $base_url/ghost
else
    echo "Usage: ./post_new_ghost.sh latitude longitude"
fi
