#!/bin/bash -e
# copy connection resource file
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./.travis/oracle.properties ~/.geotools/

# additional setup for Oracle XE
docker exec -i geotools sqlplus -l system/oracle@//localhost:1521/XE < .travis/create_user.sql
