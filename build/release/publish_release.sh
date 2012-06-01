#!/bin/bash

# error out if any statements fail
set -e
set -x

function usage() {
  echo "$0 [options] <tag>"
  echo
  echo " tag : Release tag (eg: 2.1.4, 2.2-beta1, ...)"
  echo 
}

if [ -z $1 ]; then
  usage
  exit
fi

tag=$1

# load properties + functions
. "$( cd "$( dirname "$0" )" && pwd )"/properties
. "$( cd "$( dirname "$0" )" && pwd )"/functions

# deploy the release to maven repo
pushd tags/$tag > /dev/null
mvn deploy -DskipTests
mvn -P deploy.opengeo deploy -DskipTests
popd > /dev/null

# get <major.minor> for sf release dir
if [ "$( echo $str | egrep "[0-9]+\.[0-9]+((\.|-).*)?" )" != "$str" ]; then
  echo "$tag is not a valid release version number"
  exit 1
fi
dir=`echo $tag | sed 's/\([0-9]*\.[0-9]*\).*/\1/g'`

pushd $DIST_PATH/$tag > /dev/null

rsync -ave "ssh -i $SF_PK" *.zip $SF_USER@$SF_HOST:"/home/pfs/project/g/ge/geotools/GeoTools\ $dir\ Releases/$tag/"

popd > /dev/null
