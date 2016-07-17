#!/bin/sh

cd $(dirname $0)
base_url=$(../base_url.sh)

watch \
  --no-title \
  --interval 1 "
  curl \
    --silent \
    --request GET \
    -H "Content-Type: application/json" \
    $base_url/ghost/states \
  | python -m json.tool
"
