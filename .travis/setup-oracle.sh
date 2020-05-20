#!/bin/bash -e
printf "\nCopy database connection resource file"
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./.travis/oracle.properties ~/.geotools/

printf "\nSetup GEOTOOLS user\n"
docker exec -i geotools sqlplus -l system/oracle@//localhost:1521/XE < .travis/create_user.sql
