#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 2 ] ; then
    curl \
      --include \
      --request PUT  \
      --header "Content-Type: application/json" \
      --data '{"state":"'$2'"}' \
      $base_url/ghost/"$1"/state
else
    echo "Usage: ./put_ghost_state_by_id.sh id state"
fi
