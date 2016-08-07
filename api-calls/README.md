# PacMacro Server: API Calls

This directory contains easy-to-use, up-to-date scripts to execute cURL commands against the server endpoints.

The `utility/base_url.sh` script contains a configuration for the specific server you want to run the scripts against; modify it to point to whichever server you're working with.

## Directory Guide

* API calls:
  * `home/`: The home page mapping.
  * `player/`: Player CRUD operations.
* Monitoring game status:
  * `monitoring-scripts/`: Continuously-refreshed API calls to monitor the game status.
* Miscellaneous:
  * `utility/`: Contains sub-scripts and general configurations. No actual API calls.
