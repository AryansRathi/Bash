#!/bin/bash

set -ex

if [ $# -eq 0 ]; then
    echo "No directory specified."
    exit 1
fi

HELPDIR=$1

cd $HELPDIR

# fail if we are not in a Git repository working copy
if [ ! -d ".git" ]; then
    printf "$(pwd) is not a Git repository.\n"
    exit 1
fi


# do nothing if there are no changes
if [[ ! `git status --porcelain` ]]; then
    printf "No changes.\n"
    exit 0
fi

# determine modifications
STATUS=$(git status --porcelain)
DIFFS=$(git diff)

echo $STATUS
echo $DIFFS

# set the required permissions
[ -d "todo" ] && chmod -R ug+rw "todo"
[ -d "done" ] && chmod -R ug+rw "done"

git add --all
git commit -m "New help modifications."
git push origin master

ssh doctor@doctor.dev.unitedplanet.de \
    'cd /qsk/helpdoc/helpdoc-10100-todo-done && ./sync_with_origin.sh'

# send an e-mail notification
sendmail dokumentation@unitedplanet.com alexander.veit@unitedplanet.com <<EndOfMessage
From: buildrechner <buildrechner@unitedplanet.com>
To: dokumentation@unitedplanet.com, alexander.veit@unitedplanet.com
Subject: [online-help] New changes in 10100
List-Id: online-help
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
X-UP-Notification: online-help

https://gitserver.dev.unitedplanet.de/redaktion/helpdoc-10100-todo-done/
https://gitserver.dev.unitedplanet.de/redaktion/helpdoc-10100-todo-done/commit/$(git rev-parse HEAD)

Changes:
${STATUS}


${DIFFS}

.
EndOfMessage
