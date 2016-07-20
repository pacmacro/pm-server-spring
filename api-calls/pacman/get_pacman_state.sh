#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

curl \
  --request GET --include \
  $base_url/pacman/state
