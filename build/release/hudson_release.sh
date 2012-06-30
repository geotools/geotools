#!/bin/bash

# main script invoked by hudson

# sanity check parameters
[ -z $BRANCH ] && echo "BRANCH variable mandatory" && exit 1
[ -z $REV ] && echo "REV variable mandatory" && exit 1
[ -z $VERSION ] && echo "VERSION variable mandatory" && exit 1

OPTS="-b $BRANCH -r $REV"
if [ ! -z $GIT_USER ]; then
  OPTS="$OPTS -u $GIT_USER"
fi
if [ ! -z $GIT_EMAIL ]; then
  OPTS="$OPTS -e $GIT_EMAIL"
fi

if [ ! -z $JAVA_HOME ]; then
  export PATH=$JAVA_HOME/bin:$PATH
fi
./build_release.sh $OPTS $VERSION
