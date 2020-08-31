Online Tests
------------

We make use of a naming convention, i.e. ensure the name of your TestCase ends in OnlineTest, to indicate the use of external web services and databases. If a unit test requires a network connection to pass, it is an online test.

These tests will be skipped as part of the normal build process, but will be executed by certain build boxes.

JUnit 3 OnlineTestCase
^^^^^^^^^^^^^^^^^^^^^^^

JUnit 3 online tests should extend the OnlineTestCase class, since it will extract connection parameters from fixture properties files in the user's **~/.geotools** directory with minimal hassle.

Test cases extending this class will need to implement the getFixtureId() method which will return the identifier for a fixture. A fixture id of "postgis.typical" will attempt to read the file "~/.geotools/postgis/typical.properties".

Fixture property files allow site-specific connection information such as database authentication credentials to be provided outside the public GeoTools code base.

For example, a developer might provide access to a test database in their own PostGIS instance, but not want to share this online resource with the public. Fixture property files make it easy for other developers to configure online tests to use their own private resources.

JUnit 4 OnlineTestSupport
^^^^^^^^^^^^^^^^^^^^^^^^^

JUnit 4 online tests should extend OnlineTestSupport, which provides the same functionality as OnlineTestCase.

Behaviour of Online Test Cases
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
The default behaviour of OnlineTestCase is that, if connect() throws an exception, the test suite is disabled, causing each test to pass without being run. In addition, exceptions thrown by disconnect() are ignored. This behaviour allows tests to be robust against transient outages of online resources, but also means that local software failures in connect() or disconnect() will be silent. To have exceptions thrown by connect() and disconnect() cause tests to fail, set skip.on.failure=false in the fixture property file. This restores the traditional behaviour of unit tests, that is, that exceptions cause unit tests to fail.

Each module should try to provide default fixture files pointing to publicly accessible servers. Users running online tests will copy these defaults and customize them accordingly. If a fixture file is missing, its tests will not be run; therefore, if one deletes the ~/.geotools/oracle directory, for example, all oracle online tests will be disabled.

For more details see: Online Test Fixtures, which defines the identity of each fixture and what its expected qualities and contents are.

Example OnlineTestCase Use
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Example use (using PostgisOnlineTestCase)::
   
   public abstract class PostgisOnlineTestCase extends OnlineTestCase {
       protected DataStore dataStore;
      
       protected abstract String getFixtureId();
      
       protected void connect() throws Exception {
           Map params = getParams();
           dataStore = new PostgisDataStoreFactory().createDataStore(params);
       }
       
       public Map getParams() {
           Map params = new HashMap();
           
           params.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
           params.put(PostgisDataStoreFactory.HOST.key, fixture.getProperty("host"));
           params.put(PostgisDataStoreFactory.PORT.key, fixture.getProperty("port"));
           params.put(PostgisDataStoreFactory.SCHEMA.key, fixture.getProperty("schema"));
           params.put(PostgisDataStoreFactory.DATABASE.key, fixture.getProperty("database"));
           params.put(PostgisDataStoreFactory.USER.key, fixture.getProperty("user"));
           params.put(PostgisDataStoreFactory.PASSWD.key, fixture.getProperty("password"));
           
           if (fixture.containsKey("wkbEnabled")) {
               params.put(PostgisDataStoreFactory.WKBENABLED.key, fixture.getProperty("wkbEnabled"));
           }
           if (fixture.containsKey("looseBbox")) {
               params.put(PostgisDataStoreFactory.LOOSEBBOX.key, fixture.getProperty("looseBbox"));
           }
           return params;
       }
       protected void disconnect() throws Exception {
           dataStore = null;
       }
   }

Example LocalPostgisOnlineTest

And here is a sample use::
   
   class LocalPostgisOnlineTest extends PostgisOnlineTestCase  {
       protected abstract String getFixtureId(){
           return "local";
       }
   }

As a rule of thumb, online tests should not fail if a server is unavailable.
Example Fixture

Example fixture ~/.geotools/postgis/local.properties::
   
   host=localhost
   port=15234
   schema=bc
   user=postgres
   passwd=postgres
   database=bc
   wkbEnabled=true


In windows you cannot create a ".geotools" folder!

1. Open up a CMD window
2. cd to your home directory and use mkdir::

    C:\\Documents and Settings\\Fred\>mkdir .geotools


3. And set up any fixtures you need::

    C:\\Documents and Settings\\Fred>cd .geotools
    C:\\Documents and Settings\\Fred\\.geotools>mkdir postgis
    C:\\Documents and Settings\\Fred\\.geotools>cd postgis
    C:\\Documents and Settings\\Fred\\.geotools\\postgis>notepad typical.properties


4. And use these as a guide: https://github.com/geotools/geotools/tree/master/build/fixtures
   
   Examples:
   
   * PostgisOnlineTestCase - Abstract Testcase class which connects to a specified database and creates a datastore
   * PostgisPermissionOnlineTest - Simple online test which makes use of PostgisOnlineTestCase


Setting up a database for online testing
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can use `docker <https://www.docker.com/>`_ to run a database flavour of your choice,
some examples are given below. Also the Travis-CI build script shows how to install / run
SQL Server, MySQL and Oracle XE (see `.travis.yml <https://github.com/geotools/geotools/blob/master/.travis.yml>`_).
Using docker will prevent the hassle of local installation on your computer possibly messing up your configuration.

Oracle XE
_________

Oracle Database Express Edition (XE) is an unsupported version
of Oracle Database an can be used for free (`FAQ / details <https://www.oracle.com/database/technologies/appdev/xe/faq.html>`_).
You can use the following to start a dockerized instance of Oracle Express (unfortunately Oracle does not provide an
official docker image so we are using one from the community). ::

    docker pull pvargacl/oracle-xe-18.4.0:latest
    docker run --rm -p 1521:1521 --name geotools -h geotools -d pvargacl/oracle-xe-18.4.0:latest

It will take up to a few minutes for the database to start up. In case you need to change the portmappings eg.
to have Oracle listen on port ``15211`` instead of the default ``1521`` use ``-p 15211:1521`` instead of ``-p 15211:1521``.

Note that the ``--rm`` option will delete the container after stopping it, the image is preserved so you won't need
to pull it next time, but you may want to preserve the container so you don't have to build a new one. In that case see below::

    docker run -p 1521:1521 --name geotools -h geotools -d pvargacl/oracle-xe-18.4.0:latest
    # stopping
    docker stop geotools
    # starting
    docker start geotools

Also note that the Oracle docker image and container will take quite a few gigabytes of disk space on your computer.

To create a user and schema for testing you can use the following command::

    docker exec -i geotools sqlplus -l system/oracle@//localhost:1521/XE < .travis/create_user.sql

The ``create_user.sql`` can be found in `.travis/ <https://github.com/geotools/geotools/tree/master/.travis>`_ it consists of::

    ALTER SESSION SET "_ORACLE_SCRIPT"=true;
    CREATE USER "GEOTOOLS" IDENTIFIED BY "geotools"  DEFAULT TABLESPACE "USERS" TEMPORARY TABLESPACE "TEMP";
    ALTER USER "GEOTOOLS" QUOTA UNLIMITED ON "USERS";
    GRANT "CONNECT" TO "GEOTOOLS" ;
    GRANT "RESOURCE" TO "GEOTOOLS" ;
    ALTER USER "GEOTOOLS" DEFAULT ROLE "CONNECT","RESOURCE";
    GRANT CREATE VIEW TO "GEOTOOLS" ;
    GRANT CREATE SYNONYM TO "GEOTOOLS" ;

The appropriate fixture for using the above database schema would be::

    url=jdbc:oracle:thin:@127.0.0.1:1521:XE
    port=1521
    user=GEOTOOLS
    username=GEOTOOLS
    password=geotools
    schema=GEOTOOLS
    dbtype=Oracle
    database=XE
    host=127.0.0.1
    driver=oracle.jdbc.OracleDriver

In file ``~/.geotools/oracle.properties``

To run the online test for the ``gt-jdbc-oracle`` module use the following Maven command:::

    mvn install -Dall -pl :gt-jdbc-oracle -Ponline -T1.1C -Dfmt.skip=true -am


Microsoft SQL Server
____________________

Microsoft provides official docker images of SQL Server in various versions, see
`docker hub <https://hub.docker.com/_/microsoft-mssql-server>`_ for all the options.
Extensive documentation is provided at: `SQL Server on Linux <https://docs.microsoft.com/en-us/sql/linux/quickstart-install-connect-docker?view=sql-server-ver15&pivots=cs1-bash>`_.
The system requirements for SQL Server are quite moderate especially when compared to Oracle database.

Use the following to create and start a SQL Server 2019 (developer edition) container listening on port 1433:::

    docker pull mcr.microsoft.com/mssql/server:2019-latest
    docker run -e 'ACCEPT_EULA=Y' -e 'MSSQL_SA_PASSWORD=Password12!' --rm -p 1433:1433 --name geotools -h geotools -d mcr.microsoft.com/mssql/server:2019-latest

Note that the ``--rm`` option will delete the container after stopping it, the image is preserved so you won't need
to pull it next time, but you may want to preserve the container so you don't have to build a new one.

Next create a ``geotools`` database to run the online tests::

    docker exec -it geotools /opt/mssql-tools/bin/sqlcmd -S localhost -U SA -P "Password12!" -Q 'CREATE DATABASE geotools' -d "master"

You can connect to the new database using the ``sa`` user.

The appropriate fixture for using the above database schema would be::

    url=jdbc:sqlserver://127.0.0.1:1433;databaseName=geotools
    port=1433
    user=sa
    username=sa
    password=Password12!
    schema=dbo
    dbtype=sqlserver
    database=geotools
    host=127.0.0.1
    driver=com.microsoft.sqlserver.jdbc.SQLServerDriver

In file ``~/.geotools/sqlserver.properties``

To run the online test for the ``gt-jdbc-sqlserver`` module use the following Maven command:::

    mvn install -Dall -pl :gt-jdbc-sqlserver -Ponline -T1.1C -Dfmt.skip=true -am

When done use ``docker stop geotools`` to stop and cleanup the container.

PostgreSQL / PostGIS
____________________

The PostGIS project provides official docker `images on dockerhub <https://registry.hub.docker.com/r/postgis/postgis>`_.
The project provides a long list of version combinations (tags).

Use the following to create and start a PostgreSQL 12 container with PostGIS 3 listening on port 54321:::

    docker pull postgis/postgis:12-3.0
    docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=geotools --rm -p 54321:5432 --name geotools -h geotools -d postgis/postgis:12-3.0

Note that the ``--rm`` option will delete the container after stopping it, the image is preserved so you won't need
to pull it next time, but you may want to preserve the container so you don't have to build a new one.

The appropriate fixture for using the above database schema would be::

    url=jdbc:postgresql://127.0.0.1:54321/geotools
    port=54321
    user=postgres
    username=postgres
    password=postgres
    passwd=postgres
    schema=public
    dbtype=postgis
    database=geotools
    host=127.0.0.1
    driver=org.postgresql.Driver

In file ``~/.geotools/sqlserver.properties``

To run the online test for the ``gt-jdbc-postgis`` module use the following Maven command:::

    mvn install -Dall -pl :gt-jdbc-postgis -Ponline -T1.1C -Dfmt.skip=true -am

When done use ``docker stop geotools`` to stop and cleanup the container.

MySQL
____________________

Official MySQL images are provided `on dockerhub <https://hub.docker.com/_/mysql/>`_.

Use the following to create and start a MySQL 5 (5.7.31 at the time of writing) container listening on port 3306:::

    docker pull mysql:5
    docker run --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=geotools --name geotools -h geotools -d mysql:5

Note that the ``--rm`` option will delete the container after stopping it, the image is preserved so you won't need
to pull it next time, but you may want to preserve the container so you don't have to build a new one.

Then create a ``geotools`` database using:::

    docker exec -i geotools mysql -uroot -pgeotools < .travis/mysql_setup.sql

The appropriate fixture for using the above database schema would be::

    driver=com.mysql.cj.jdbc.Driver
    url=jdbc:mysql://localhost/geotools
    host=localhost
    port=3306
    user=root
    password=geotools

In file ``~/.geotools/mysql.properties``

To run the online tests for the ``gt-jdbc-mysql`` module use the following Maven command:::

    mvn install -Dall -pl :gt-jdbc-mysql -Ponline -T1.1C -Dfmt.skip=true -am

When done use ``docker stop geotools`` to stop and cleanup/remove the container.