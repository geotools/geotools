#!/bin/bash

# main script invoked by hudson

# sanity check parameters
[ -z $BRANCH ] && echo "BRANCH variable mandatory" && exit 1
[ -z $VERSION ] && echo "VERSION variable mandatory" && exit 1
[ -z $GIT_USER ] && echo "GIT_USER variable mandatory" && exit 1
[ -z $GIT_EMAIL ] && echo "GIT_EMAIL variable mandatory" && exit 1
[ -z $SERIES ] && echo "SERIES variable mandatory" && exit 1

OPTS="-b $BRANCH"
if [ ! -z $REV ]; then
  OPTS="$OPTS -r $REV"
fi

# double check default jdk
jrunscript -e 'java.lang.System.out.println(java.lang.System.getProperty("java.home"));'

echo build_release.sh $OPTS $VERSION $GIT_USER $GIT_EMAIL $SERIES

./build_release.sh $OPTS $VERSION $GIT_USER $GIT_EMAIL $SERIES
