#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

if [ $# -ge 1 ] ; then
    curl \
      --include \
      --request PUT \
      --header "Content-Type: application/json" \
      --data '{"state":"'$1'"}' \
      $base_url/pacman/state
else
    echo "Usage: ./put_pacman_state.sh state"
fi
