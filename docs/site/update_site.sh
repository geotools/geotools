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

WWW=/var/www/geotools
pushd $WWW/docs/$1 &&

if [ -e userguide.zip ]; then
  if [ -e userguide.old ]; then
     rm -rf userguide.old
  fi
  if [ -e userguide ]; then
     mv userguide userguide.old
  fi
  if [ ! -e userguide ]; then
     mkdir userguide
  fi
  unzip -d userguide userguide.zip
  mv userguide.zip userguide.old.zip
fi

if [ "$1" == "latest" ] && [ -e developer.zip ]; then
  if [ -e developer.old ]; then
    rm -rf developer.old
  fi
  if [ -e developer ]; then
    mv developer developer.old
  fi
  if [ ! -e developer ]; then
    mkdir developer
  fi
  unzip -d developer developer.zip
  mv developer.zip developer.old.zip
fi

if [ -e javadocs.zip ]; then
  if [ -e javadocs ]; then
     rm -rf javadocs
  fi
  mkdir javadocs
  unzip -d javadocs javadocs.zip
  mv javadocs.zip javadocs.old.zip
fi

if [ -e tutorial.zip ]; then
  if [ -e tutorials ]; then
     rm -rf tutorials 
  fi
  mkdir tutorials
  unzip -d tutorials tutorial.zip
  mv tutorial.zip tutorial.old.zip
fi

popd

pushd $WWW &&

if [ "$1" == "latest" ] && [ -e docs/$1/web.zip ]; then
  if [ -e web.old ]; then 
    rm -rf web.old
  fi
  if [ -e web ]; then 
    mv web web.old
  fi
  if [ ! -e web ]; then
    mkdir web
  fi
  unzip -d web docs/$1/web.zip 
  mv docs/$1/web.zip web.old.zip
fi

if [ "$1" == "latest" ] && [ -e docs/$1/index.zip ]; then
  unzip -o -d docs docs/$1/index.zip 
  mv docs/$1/index.zip index.old.zip
fi

popd
