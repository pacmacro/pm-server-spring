#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)

curl \
  --request GET --include \
  $base_url/admin/gamestate
