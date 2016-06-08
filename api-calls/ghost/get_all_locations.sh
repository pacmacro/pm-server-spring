#!/bin/sh

curl \
  --request GET --include \
  -H "Content-Type: application/json" \
  http://localhost:8080/ghost/locations
