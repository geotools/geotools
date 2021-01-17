#!/bin/bash -e
printf "\nCopy database connection resource file...\n"
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./build/ci/mysql/mysql.properties ~/.geotools/

printf "\nCreate GEOTOOLS database...\n"
docker exec -i geotools mysql -uroot -pgeotools < build/ci/mysql/setup_mysql.sql
