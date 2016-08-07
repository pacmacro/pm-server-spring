#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)

watch \
  --no-title \
  --interval 1 "
  curl \
    --silent \
    --request GET \
    -H "Content-Type: application/json" \
    $base_url/player/details \
  | python -m json.tool
"
