#!/bin/bash

# error out if any statements fail
set -e
set -x

function usage() {
  echo "$0 [options] <tag>"
  echo
  echo " tag : Release tag (eg: 2.7.5, 8.0-RC1, ...)"
  echo
  echo "Options:"
  echo " -h          : Print usage"
  echo " -b <branch> : Branch to release from (eg: trunk, 2.1.x, ...)"
  echo " -r <rev>    : Revision to release (eg: 12345)"
  echo
  echo "Environment variables:"
  echo " BUILD_FROM_BRANCH : Builds release from branch rather than tag"
  echo " SKIP_SVN_TAG : Skips creation of svn tag"
  echo " SKIP_BUILD : Skips main release build"
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

# sanity check
if [ -z $tag ] || [ ! -z $2 ]; then
  usage
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

if [ ! -z $BUILD_FROM_BRANCH ]; then
  if [ ! -e tags/$tag ]; then
    if [ "$branch" == "trunk" ]; then
      svn_url=$SVN_ROOT/trunk
    else
      svn_url=$SVN_ROOT/branches/$branch
    fi
    
    echo "checking out $svn_url"
    svn co $svn_url tags/$tag  
  fi
else
  # check if the svn tag already exists
  if [ -z $SKIP_SVN_TAG ]; then
    set +e && svn ls $SVN_ROOT/tags/$tag >& /dev/null && set -e
    if [ $? == 0 ]; then
      # tag already exists
      # TODO: delete it automatically... 
      echo "svn tag $tag already exists, delete before proceeding"
      exit 1
    fi
  
    # create svn tag
    revopt="-r $rev"
    if [ "$rev" == "latest" ]; then
      revopt=""
    fi
  
    echo "svn cp -m "tagging $tag" $revopt $SVN_ROOT/$branch $SVN_ROOT/tags/$tag"

    # checkout newly created tag
    if [ -e tags/$tag ]; then
      # remove old checkout
      rm -rf tags/$tag
    fi

    echo "checking out tag $tag"
    svn co $SVN_ROOT/tags/$tag tags/$tag
  fi
fi

if [ ! -z $SKIP_SVN_TAG ] || [ ! -z $BUILD_FROM_BRANCH ]; then
  echo "updating tag $tag"
  svn revert --recursive tags/$tag
  svn up tags/$tag 
fi

# update the rename script
# generate release notes
jira_id=`get_jira_id $tag`
if [ -z $jira_id ]; then
  echo "Could not locate release $tag in JIRA"
  exit -1
fi

pushd tags/$tag > /dev/null

# update versions
pushd build > /dev/null
sed -i 's/@VERSION@/'$tag'/g' rename.xml 
ant -f rename.xml
popd > /dev/null

# build the release
if [ -z $SKIP_BUILD ]; then
  echo "building release"
  mvn $MAVEN_FLAGS -Dall clean
  mvn $MAVEN_FLAGS -DskipTests install
fi
  
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

popd > /dev/null

echo "build complete, artifacts available at $DIST_URL/$tag"
exit 0
