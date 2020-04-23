#!/bin/bash -e
docker version

# this docker image has the following users/credentials (user/password = system/oracle)
docker pull pvargacl/oracle-xe-18.4.0:latest

# start the dockerized oracle-xe instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
docker run --rm -p 1521:1521 --name geotools -h geotools -d pvargacl/oracle-xe-18.4.0:latest

# print logs
# docker logs geotools

printf "\n\nStarting Oracle XE container, this could take a few minutes..."
printf "\nWaiting for Oracle XE database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools | grep -q 'DATABASE IS READY TO USE!'); then
        printf "\nOracle XE Database started\n\n"
        break
    fi
    sleep 10
    _WAIT=$(($_WAIT+10))
done

# docker ps -a
# print logs
docker logs geotools
