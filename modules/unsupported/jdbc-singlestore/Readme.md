SingleSource Database
=====================

Setting up Dev Environment
==========================
* Get Free license key
* Download docker image
* Create properties file for connection details

Get Free license key
--------------------

Get a free licence from singles store website

Download Docker Image
---------------------

Details for docker image

https://github.com/singlestore-labs/singlestoredb-dev-image

```shell
docker run -d \
  --name singlestore-ciab \
  --platform linux/amd64 \
  -e LICENSE_KEY="BDY5YTQzNjFhMzUxZTQzYjhiOTRhMDRiNDczZGQyN2JlCbx7aAAAAAAAAAAAAAAAAAkwNQIZAO0hqXxzJF0dy0pawhR4SJJlJFUqpdjPygIYSFo8cCGTwP6n7UVwklBwRzz6Vj4K6OjsAA==" \
  -e ROOT_PASSWORD="password" \
  -p 3306:3306 -p 8080:8080 \
  singlestore/cluster-in-a-box:alma-8.5.22-fe61f40cd1-4.1.0-1.17.11
```
The `--platform linux/amd64` is only required for apple silicon macs

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
password=password
driver=com.singlestore.jdbc.Driver
port=3306
host=localhost
user=root
url=jdbc\:singlestore\://localhost/geotools
```