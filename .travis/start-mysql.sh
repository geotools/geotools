#!/bin/bash -e
docker version

docker pull mysql:5

# start the dockerized mysql instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
docker run --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=geotools --name geotools -h geotools -d mysql:5

# print logs
# docker logs geotools

printf "\n\nStarting MySQL container, this could take a few minutes..."
printf "\nWaiting for MySQL database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools 2>&1 | grep -q 'ready for connections'); then
        printf "\nMySQL ready for connections\n\n"
        break
    fi
    sleep 10
    _WAIT=$(($_WAIT+10))
done

# docker ps -a
# print logs
docker logs geotools
