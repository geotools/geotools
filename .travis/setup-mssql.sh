#!/bin/bash -e
export PATH="$PATH:/opt/mssql-tools/bin"
export SQLCMDINI=.travis/init-mssql.sql
sqlcmd -S localhost -U sa -P Password12! -Q "CREATE DATABASE geotools" -d "master"

pwd
echo $HOME
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./.travis/sqlserver.properties ~/.geotools/

ls -l ~/.geotools/

echo $ARGS
