#!/bin/bash -e
docker version
docker pull mysql:$1

printf "\n\nStarting MySQL $1 container, this could take a while..."
# start the dockerized mysql instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
docker run --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=geotools --name geotools -h geotools -d mysql:$1

# the mysql docker images start a temporary server to init and then restart listening on 3306
printf "\nWaiting for MySQL $1 database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    # look for a line with: "mysqld: ready for connections."
    # followed by a line with "port: 3306 " (maybe on the same line)
    # or a line with "Ready for start up." for mysql 5
    if $(docker logs geotools 2>&1 | grep -A2 'ready for connections' | grep -q 'port: 3306 ') || $(docker logs geotools 2>&1 | grep -q 'Ready for start up.'); then
        printf "\nMySQL ready for connections\n\n"
        break
    fi

    sleep 10
    _WAIT=$(($_WAIT+10))

    if (($_WAIT > 300)); then
        printf "\nWaited 300 seconds for MySQL $1 database, giving up.\n\n"
        break
    fi
done

# print logs
docker logs geotools
