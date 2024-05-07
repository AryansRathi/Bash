#!/bin/bash

set -ex

if [ $# -eq 0 ]; then
    echo "No directory specified."
    exit 1
fi

HELPDIR=$1

# syncronize doctor with the central repository
ssh doctor@doctor.dev.unitedplanet.de \
    'cd /qsk/helpdoc/helpdoc-10100-todo-done && ./sync_with_origin.sh'

# get the recent content from the central repository
if [ -d "$HELPDIR" ]; then
  rm -r "$HELPDIR"
fi

git clone gitea@gitserver.dev.unitedplanet.de:redaktion/helpdoc-10100-todo-done.git $HELPDIR
