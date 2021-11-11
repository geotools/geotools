#!/bin/bash -e
docker version
docker pull ibmcom/db2:11.5.6.0a

printf "\n\nStarting IBM Db2 container, this could take a while...\ndocker container hash: "
# start the dockerized Db2 instance (the container will be destroyed/removed on stopping)
# this container can be stopped using: docker stop geotools
docker run -e 'LICENSE=accept' -e 'DB2INST1_PASSWORD=db2inst1' -e 'DBNAME=geotools' -e 'ARCHIVE_LOGS=false' --rm -p 50000:50000 --name geotools --privileged=true -d ibmcom/db2:11.5.6.0a

printf "\nWaiting for IBM Db2 database to start up.... "
_WAIT=0;
while :
do
    printf " $_WAIT"
    if $(docker logs geotools 2>&1 | grep -q 'Setup has completed'); then
        printf "\nIBM Db2 database is now ready for client connections\n\n"
        break
    fi
    sleep 10
    _WAIT=$(($_WAIT+10))
done

# print logs
docker logs geotools
