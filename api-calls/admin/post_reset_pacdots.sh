#!/bin/sh

cd $(dirname $0)
base_url=$(../utility/base_url.sh)

curl \
  --request POST --include \
  -H "Content-Type: application/json" \
  $base_url/admin/pacdots/reset
