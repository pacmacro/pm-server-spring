#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

curl \
  --request DELETE --include \
  $base_url/pacman
