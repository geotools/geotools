#!/usr/bin/env bash
set -e
docker version
docker pull ibmcom/informix-developer-database:latest

printf "\n\nStarting IBM Informix container, this could take a while...\ndocker container hash: "
# start the dockerized Informix instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
docker run -e 'LICENSE=accept' -e 'DB_INIT=1' -e 'SIZE=small' -e 'STORAGE=local' --rm -p 9088:9088 --name geotools --privileged=true -d ibmcom/informix-developer-database:latest


printf "\nWaiting for IBM Informix database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools 2>&1 | grep -q 'IBM Informix Dynamic Server Started'); then
        printf "\nIBM Informix database is now ready for client connections\n\n"
        break
    fi
    sleep 10
    _WAIT=$(($_WAIT+10))

    if (($_WAIT > 300)); then
        printf "\nWaited 300 seconds for Informix database, giving up.\n\n"
        break
    fi
done

# print logs
docker logs geotools
