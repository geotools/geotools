#!/bin/bash
#
# Last update 2011-04-10 MB: 
#   The zip files are now uploaded to ~/stable or ~/latest.
#   Move xxx.zip to xxx.old.zip after unpacking.
#

if [ "$1" != "stable" ] && [ "$1" != "latest" ]; then
  echo "Usage $0 <stable|latest>"
  exit 1
fi

WWW=/osgeo/geotools/htdocs
pushd $WWW/docs/$1 &&

if [ -e ~/$1/userguide.zip ]; then
  if [ -e userguide.old ]; then
     rm -rf userguide.old
  fi
  if [ -e userguide ]; then
     mv userguide userguide.old
  fi
  if [ ! -e userguide ]; then
     mkdir userguide
  fi
  unzip -d userguide ~/$1/userguide.zip
  mv ~/$1/userguide.zip ~/$1/userguide.old.zip
fi

if [ "$1" == "latest" ] && [ -e ~/$1/developer.zip ]; then
  if [ -e developer.old ]; then
    rm -rf developer.old
  fi
  if [ -e developer ]; then
    mv developer developer.old
  fi
  if [ ! -e developer ]; then
    mkdir developer
  fi
  unzip -d developer ~/$1/developer.zip
  mv ~/$1/developer.zip ~/$1/developer.old.zip
fi

if [ -e ~/$1/javadocs.zip ]; then
  if [ -e javadocs ]; then
     rm -rf javadocs
  fi
  mkdir javadocs
  unzip -d javadocs ~/$1/javadocs.zip
  mv ~/$1/javadocs.zip ~/$1/javadocs.old.zip
fi

if [ -e ~/$1/tutorial.zip ]; then
  if [ -e tutorials ]; then
     rm -rf tutorials 
  fi
  mkdir tutorials
  unzip -d tutorials ~/$1/tutorial.zip
  mv ~/$1/tutorial.zip ~/$1/tutorial.old.zip
fi

popd

pushd $WWW &&

if [ "$1" == "latest" ] && [ -e ~/$1/web.zip ]; then
  if [ -e web.old ]; then 
    rm -rf web.old
  fi
  if [ -e web ]; then 
    mv web web.old
  fi
  if [ ! -e web ]; then
    mkdir web
  fi
  unzip -d web ~/$1/web.zip 
  mv ~/$1/web.zip ~/$1/web.old.zip
fi

if [ "$1" == "latest" ] && [ -e ~/$1/index.zip ]; then
  unzip -o -d docs ~/$1/index.zip 
  mv ~/$1/index.zip ~/$1/index.old.zip
fi

popd
