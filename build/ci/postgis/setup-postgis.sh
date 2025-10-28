#!/bin/bash -e
printf "\nCopy database connection resource files...\n"
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./build/ci/postgis/*.properties ~/.geotools/

#printf "\nCreate GEOTOOLS database...\n"
#docker exec -i postgis createdb -U postgres geotools