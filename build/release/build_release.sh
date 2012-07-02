#!/bin/bash

# error out if any statements fail
set -e

function usage() {
  echo "$0 [options] <tag>"
  echo
  echo " tag : Release tag (eg: 2.7.5, 8.0-RC1, ...)"
  echo
  echo "Options:"
  echo " -h          : Print usage"
  echo " -b <branch> : Branch to release from (eg: master, 8.x, ...)"
  echo " -r <rev>    : Revision to release (eg: a1b2kc4...)"
  echo " -u <user>   : git user"
  echo " -e <passwd> : git email"
  echo
}

# parse options
while getopts "hb:r:u:e:" opt; do
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
    u)
      git_user=$OPTARG
      ;;
    e)
      git_email=$OPTARG
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

# sanity check
if [ -z $tag ] || [ ! -z $2 ]; then
  usage
  exit 1
fi
if [ `is_version_num $tag` == "0" ]; then  
  echo "$tag is a not a valid release tag"
  exit 1
fi
if [ `is_primary_branch_num $tag` == "1" ]; then  
  echo "$tag is a not a valid release tag, can't be same as primary branch name"
  exit 1
fi

# load properties + functions
. "$( cd "$( dirname "$0" )" && pwd )"/properties
. "$( cd "$( dirname "$0" )" && pwd )"/functions

echo "Building release with following parameters:"
echo "  branch = $branch"
echo "  revision = $rev"
echo "  tag = $tag"

mvn -version

# ensure there is a jira release
jira_id=`get_jira_id $tag`
if [ -z $jira_id ]; then
  echo "Could not locate release $tag in JIRA"
  exit -1
fi

if [ ! -z $git_user ] && [ ! -z $git_email ]; then
  git_opts="--author $git_user <$git_email>"
fi

# move to root of repo
pushd ../../ > /dev/null

# clear out any changes
git reset --hard HEAD

# change to primary branch
git checkout $branch

# check to see if a release branch already exists
set +e && git checkout rel_$tag && set -e
if [ $? == 0 ]; then
  # release branch already exists, kill it
  echo "branch rel_$tag exists, deleting it"
  git checkout rel_$branch
  git branch -D rel_$tag
fi

# create a release branch
if [ -z $rev ]; then
  # no revision means latest on the primary branch
  rev=$branch
fi

git checkout -b rel_$tag $rev

# update versions
pushd build > /dev/null
sed -i 's/@VERSION@/'$tag'/g' rename.xml 
sed -i "s/@RELEASE_DATE@/`date "+%b %d, %Y"`/g" rename.xml 
ant -f rename.xml
popd > /dev/null

# build the release
if [ -z $SKIP_BUILD ]; then
  echo "building release"
  mvn $MAVEN_FLAGS -Dall clean
  mvn $MAVEN_FLAGS -DskipTests -P process clean install
  mvn $MAVEN_FLAGS -DskipTests assembly:assembly
fi

# sanitize the bin artifact
pushd target > /dev/null
bin=geotools-$tag-bin.zip
unzip $bin
cd geotools-$tag
rm junit*.jar
rm *dummy-*
cd ..
rm $bin
zip -r $bin geotools-$tag
rm -rf geotools-$tag
popd > /dev/null

target=`pwd`/target

# build the javadocs
pushd modules > /dev/null
mvn javadoc:aggregate
pushd target/site > /dev/null
zip -r $target/geotools-$tag-doc.zip apidocs
popd > /dev/null
popd > /dev/null

# build the user docs
pushd docs > /dev/null
mvn $MAVEN_FLAGS install
pushd target/user > /dev/null
zip -r $target/geotools-$tag-userguide.zip html
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

# commit changes 
git add .
git commit $git_opts -m "updating version numbers and README for $tag"

popd > /dev/null

# TODO: generate release notes

echo "build complete, artifacts available at $DIST_URL/$tag"
exit 0
