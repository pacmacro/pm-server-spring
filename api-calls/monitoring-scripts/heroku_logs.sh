#!/bin/sh

echo ""
echo "The logs are restricted to developers only."
echo "If you are a developer but you are not authorized to access these logs, please contact GitHub user jleung51."
echo "If the last line is 'Process exited', the server is most likely asleep. Call any endpoint to wake the server up."
echo ""
echo "Starting in 5 seconds..."
echo ""

sleep 5s

heroku logs \
  --tail --force-colors \
  --app pacmacro
