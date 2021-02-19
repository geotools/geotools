#!/bin/bash -e
printf "\nCopy database connection resource file"
mkdir --parents --verbose ~/.geotools
cp --verbose --force ./build/ci/db2/db2.properties ~/.geotools/

printf "\nEnable geotools database for spatial operations...\n"
docker exec -u db2inst1 -e 'DB2INSTANCE=db2inst1' -i geotools /database/config/db2inst1/sqllib/bin/db2se enable_db geotools

printf "\nCreate GEOTOOLS schema...\n"
docker cp ./build/ci/db2/setup_db2.commands geotools:/home/
docker exec -u db2inst1 -e 'DB2INSTANCE=db2inst1' -i geotools /database/config/db2inst1/sqllib/bin/db2 -vf /home/setup_db2.commands
