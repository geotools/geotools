#!/bin/bash -e
docker version
docker pull postgis/postgis:$1

printf "\n\nStarting Postgis $1 container, this could take a while..."
# start the dockerized ms sql instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
docker run --rm -p 5432:5432 -e POSTGRES_USER=geotools -e POSTGRES_PASSWORD=geotools -e POSTGRES_DB=gttest --name geotools -h geotools -d postgis/postgis:$1

printf "\nWaiting for Postgis Server $1 database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools 2>&1 | grep -q 'database system is ready to accept connections'); then
        printf "\nPostgis Server is now ready for client connections\n\n"
        break
    fi

    sleep 10
    _WAIT=$(($_WAIT+10))

    if (($_WAIT > 300)); then
        printf "\nWaited 300 seconds for Postgis Server $1 database, giving up.\n\n"
        break
    fi
done

# print logs
docker logs geotools