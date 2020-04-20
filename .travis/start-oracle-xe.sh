#!/bin/bash -e
docker version

# image has the following users/credentials (user/password)
# system / oracle
docker pull pvargacl/oracle-xe-18.4.0:latest

# start the dockerized oracle-xe instance (that will be removed on stop)
# this instance can be stopped using: docker stop geotools
docker run --rm -p 1521:1521 --name geotools -h geotools -d pvargacl/oracle-xe-18.4.0:latest

docker logs geotools

printf "\n\nWaiting for Oracle XE database to initialize.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools | grep -q 'DATABASE IS READY TO USE!'); then
        printf "\n\nOracle XE Database initialized\n\n"
        break
    fi
    sleep 10
    _WAIT=$(($_WAIT+10))
done

#check logs again
docker ps -a
docker logs geotools
