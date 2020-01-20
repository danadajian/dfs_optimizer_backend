#!/bin/bash -e

set -x

chmod a+x ./deployBackend.sh
chmod a+x ./deployFrontend.sh

sh ./deployBackend.sh
sh ./deployFrontend.sh