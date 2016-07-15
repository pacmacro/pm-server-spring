#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

watch \
  --no-title \
  --interval 1 "
  curl \
    --silent \
    --request GET \
    $base_url/pacman/location \
  | python -m json.tool
"
