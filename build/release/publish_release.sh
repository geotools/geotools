#!/bin/bash

# error out if any statements fail
set -e
set -x

function usage() {
  echo "$0 [options] <tag> <branch> <user> <email>"
  echo
  echo " tag : Release tag (eg: 2.1.4, 2.2-beta1, ...)"
  echo " user:  Git username"
  echo " email: Git email"
  echo 
  echo "Environment variables:"
  echo " SKIP_DEPLOY : Skips deploy to maven repository"
}

if [ -z $4 ]; then
  usage
  exit
fi

tag=$1
branch=$2
git_user=$3
git_email=$4
dist=`pwd`/distribution/$tag

echo "maven/java settings:"
mvn -version
echo "MAVEN_FLAGS=$MAVEN_FLAGS"

# load properties + functions
. "$( cd "$( dirname "$0" )" && pwd )"/properties
. "$( cd "$( dirname "$0" )" && pwd )"/functions

if [ `is_version_num $tag` == "0" ]; then
  echo "$tag is a not a valid release tag"
  exit 1
fi
if [ `is_primary_branch_num $tag` == "1" ]; then
  echo "$tag is a not a valid release tag, can't be same as primary branch name"
  exit 1
fi

pushd ../../ > /dev/null

init_git $git_user $git_email

# check to see if a release branch already exists
set +e && git checkout rel_$tag && set -e
if [ $? == 0 ]; then
  # release branch already exists, kill it
  git checkout $branch
  echo "branch rel_$tag exists, deleting it"
  git branch -D rel_$tag
fi

# switch to the release tag
git fetch --tags
if [ `git tag --list $tag | wc -l` == 0 ]; then
  echo "Could not locate tag $tag"
  exit 1;
fi
git checkout tags/$tag -b rel_$tag

# ensure no changes on it (actually release folder is a change)
# set +e
# git status | grep "working directory clean"
# if [ "$?" == "1" ]; then
#   echo "branch rel_$tag dirty, exiting"
#   exit 1
# fi
# set -e

# deploy the release to maven repo
if [ "$SKIP_DEPLOY"  != true ]; then
  echo "deploying with $MAVEN_FLAGS"

  mvn clean deploy -DskipTests -Dall $MAVEN_FLAGS
  mvn clean -P deploy.boundless deploy -DskipTests -Dall $MAVEN_FLAGS
fi

# get <major.minor> for sf release dir
if [ "$( echo $str | egrep "[0-9]+\.[0-9]+((\.|-).*)?" )" != "$str" ]; then
  echo "$tag is not a valid release version number"
  exit 1
fi
dir=`echo $tag | sed 's/\([0-9]*\)\([\.\-]\)\([0-9]*\).*/\1/g'`

pushd $dist > /dev/null

#Assume SSH Key for $SF_USER is added to the SSH Agent
rsync -ave "ssh " *.zip $SF_USER@$SF_HOST:"/home/pfs/project/g/ge/geotools/GeoTools\ $dir\ Releases/$tag/"

popd > /dev/null

# tagging now handled by build_release.sh
# tag the release branch
# git tag --force $tag
# push up tag
# git push --delete origin $tag
# git push origin $tag

popd > /dev/null
