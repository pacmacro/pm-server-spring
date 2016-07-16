#!/bin/sh

echo "The logs are restricted to developers only."
echo "If you are a developer but you are not authorized to access these logs, please contact GitHub user jleung51."
echo ""

heroku logs \
  --tail --force-colors \
  --app pacmacro
