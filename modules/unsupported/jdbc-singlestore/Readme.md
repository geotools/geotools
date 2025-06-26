SingleSource Database
=====================

Setting up Dev Environment
==========================

* Download docker image
* Create properties file for connection details

Download Docker Image
---------------------

Details for docker image

https://github.com/singlestore-labs/singlestoredb-dev-image

```shell
docker run \
    -d --name singlestoredb-dev \
    -e ROOT_PASSWORD="YOUR SINGLESTORE ROOT PASSWORD" \
    -p 3306:3306 -p 8080:8080 -p 9000:9000 \
    ghcr.io/singlestore-labs/singlestoredb-dev:latest
```
Add `--platform linux/amd64` if you're running on Apple Silicon macs

Create an Administrator geotools
--------------------------------

```sql
CREATE USER geotools IDENTIFIED BY 'geotools';
GRANT ALL PRIVILEGES ON *.* TO 'geotools'@'%';

SHOW GRANTS FOR geotools;
```


Create properties file for connection details
---------------------------------------------


Go to the ${HOME}/.geotools directory 

```shell
touch singlestore.properties
```

Copy and paste the following into the properties files

```
password=geotools
driver=com.singlestore.jdbc.Driver
port=3306
host=localhost
user=geotools
url=jdbc\:singlestore\://localhost/geotools
```