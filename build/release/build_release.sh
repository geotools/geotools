#!/bin/bash

# error out if any statements fail
set -e

function usage() {
  echo "$0 [options] <tag> <user> <email> <series>"
  echo
  echo " tag :  Release tag (eg: 20.1, 21.0-RC1, ...)"
  echo " user:  Git username"
  echo " email: Git email"
  echo " series: Series (stable, maintenance, latest)"
  echo
  echo "Options:"
  echo " -h          : Print usage"
  echo " -b <branch> : Branch to release from (eg: master, 8.x, ...)"
  echo " -r <rev>    : Revision to release (eg: a1b2kc4...)"
  echo
}

# parse options
while getopts "hb:r:" opt; do
  case $opt in
    h)
      usage
      exit
      ;;
    b)
      branch=$OPTARG
      ;;
    r)
      rev=$OPTARG
      ;;
    \?)
      usage
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument"
      exit 1
      ;;
  esac 
done

# clear options to parse main arguments
shift $(( OPTIND -1 ))
tag=$1
git_user=$2
git_email=$3
series=$4

# sanity check
if [ -z $tag ] || [ -z $git_user ] || [ -z $git_email ] || [ -z $series ] || [ ! -z $5 ]; then
  usage
  exit 1
fi

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
#checkout branch locally if it doesn't exist
if ! git show-ref refs/heads/$branch; then 
  git fetch origin $branch:$branch
fi

echo "Building release with following parameters:"
echo "  branch = $branch"
echo "  revision = $rev"
echo "  tag = $tag"
echo "  series = $series"

mvn -version

# ensure there is a jira release
jira_id=`get_jira_id $tag`
if [ -z $jira_id ]; then
  echo "Could not locate release $tag in JIRA"
  exit -1
fi

# move to root of repo
pushd ../../ > /dev/null

# clear out any changes
git reset --hard HEAD

# checkout and update primary branch
git checkout $branch
git pull origin $branch

# check to see if a release branch already exists
set +e && git checkout rel_$tag && set -e
if [ $? == 0 ]; then
  # release branch already exists, kill it
  git checkout $branch
  echo "branch rel_$tag exists, deleting it"
  git branch -D rel_$tag
fi

git checkout $branch

# ensure the specified revision actually on this branch
if [ $rev != "HEAD" ]; then
  set +e
  git log | grep $rev
  if [ $? != 0 ]; then
     echo "Revision $rev not a revision on branch $branch"
     exit -1
  fi
  set -e
fi

# create a release branch
git checkout -b rel_$tag $rev

# update versions
pushd build > /dev/null
ant -f rename.xml -Drelease=$tag -Dseries=$series
popd > /dev/null

# build the release
if [ "$SKIP_BUILD" != true ]; then
  echo "building release"
  export MAVEN_OPTS="-Xmx2048m"
  mvn $MAVEN_FLAGS -DskipTests -Dfmt.skip=true -Dall clean -Pcollect install
  mvn $MAVEN_FLAGS -DskipTests -Dfmt.skip=true assembly:assembly
fi

target=`pwd`/target

# build the javadocs
pushd modules > /dev/null
mvn -Dfmt.skip=true javadoc:aggregate
pushd target/site > /dev/null
zip -r $target/geotools-$tag-doc.zip apidocs
popd > /dev/null
popd > /dev/null

# copy over the artifacts
if [ ! -e $DIST_PATH ]; then
  mkdir -p $DIST_PATH
fi
dist=$DIST_PATH/$tag
if [ -e $dist ]; then
  rm -rf $dist
fi
mkdir $dist

echo "copying artifacts to $dist"
cp $target/*.zip $dist

init_git $git_user $git_email

# commit changes 
git add .
git commit -m "updating version numbers and README for $tag"

# check to see if tag already exists
git fetch --tags
if [ `git tag --list $tag | wc -l` == 1 ]; then
  echo "tag $tag exists, deleting it"
  git tag -d $tag
fi

if  [ `git ls-remote --refs --tags origin tags/$tag | wc -l` == 1 ]; then
  echo "tag $tag exists on $GIT_ROOT, deleting it"
  git push --delete origin $tag
fi

# tag the release branch
git tag $tag

# push up tag
git push origin $tag

popd > /dev/null

# TODO: generate release notes

echo "build complete, artifacts available at $DIST_URL/$tag"
exit 0
