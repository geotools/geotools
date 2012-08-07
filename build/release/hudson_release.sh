#!/bin/bash

# main script invoked by hudson

# sanity check parameters
[ -z $BRANCH ] && echo "BRANCH variable mandatory" && exit 1
[ -z $VERSION ] && echo "VERSION variable mandatory" && exit 1
[ -z $GIT_USER ] && echo "GIT_USER variable mandatory" && exit 1
[ -z $GIT_EMAIL ] && echo "GIT_EMAIL variable mandatory" && exit 1

OPTS="-b $BRANCH"
if [ ! -z $REV ]; then
  OPTS="$OPTS -r $REV"
fi

if [ ! -z $JAVA_HOME ]; then
  export PATH=$JAVA_HOME/bin:$PATH
fi
./build_release.sh $OPTS $VERSION $GIT_USER $GIT_EMAIL
