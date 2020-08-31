#!/bin/bash -e
printf "\nCopy database connection resource file"
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./.travis/mysql.properties ~/.geotools/

printf "\nCreate GEOTOOLS database\n"
docker exec -i geotools mysql -uroot -pgeotools < .travis/mysql_setup.sql
