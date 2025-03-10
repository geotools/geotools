#!/bin/bash -e
docker version

# this docker image does not have Java installed, which means you can not use Java stored procedures
# such as "SDO_GEOMETRY('POINT (1.0 2.0)', 4326))"
docker pull ghcr.io/gvenzl/$1

# start the dockerized oracle-xe instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
# start with user/credentials (user/password = system/oracle)
docker run --rm -p 1521:1521 --name geotools -h geotools -e ORACLE_PASSWORD=oracle -d ghcr.io/gvenzl/$1

printf "\n\nStarting Oracle $1 container, this could take a few minutes..."
printf "\nWaiting for Oracle $1 database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools | grep -q 'DATABASE IS READY TO USE!'); then
        printf "\nOracle $1 Database started\n\n"
        break
    fi
    sleep 10
    _WAIT=$(($_WAIT+10))

    if (($_WAIT > 300)); then
        printf "\nWaited 300 seconds for Oracle database, giving up.\n\n"
        break
    fi
done

# print logs
docker logs geotools
