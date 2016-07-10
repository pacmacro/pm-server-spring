#!/bin/sh

curl \
  --include \
  --request POST  \
  --header "Content-Type: application/json" \
  --data '{"latitude":1.23,"longitude":12.3}' \
  http://localhost:8080/ghost
