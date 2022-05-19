#!/usr/bin/env bash
set -eux

printf "\nCopy database connection resource file"
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./build/ci/informix/informix-sqli.properties ~/.geotools/

printf "\nCopy setup files...\n"
docker cp ./build/ci/informix/setup-informix-database.sql geotools:/home/informix/
docker cp ./build/ci/informix/setup-informix-database.sh geotools:/home/informix/

printf "\nCreate GEOTOOLS schema...\n"
docker exec -u informix -i geotools /home/informix/setup-informix-database.sh