DB2 Plugin
----------

Supports direct access to a DB2 database.

A couple of really good things have happened for DB2 support recently. We have obtained permission to distribute the DB2 driver with GeoTools, and you can now download a free community version of DB2 .

**References**

* http://www-01.ibm.com/software/data/db2/express/

**Maven**::
   
   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-db2</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

+-------------+----------------------------------------------+
| Parameter   | Description                                  |
+=============+==============================================+
| "dbtype"    | Must be the string "db2"                     |
+-------------+----------------------------------------------+
| "host"      | Machine name or IP address to connect to     |
+-------------+----------------------------------------------+
| "port"      | Port number to connect to, default 50000     |
+-------------+----------------------------------------------+
| "tabschema" | The database schema to access                |
+-------------+----------------------------------------------+
| "database"  | The database to connect to                   |
+-------------+----------------------------------------------+
| "user"      | User name                                    |
+-------------+----------------------------------------------+
| "passwd"    | Password                                     |
+-------------+----------------------------------------------+

Creating
^^^^^^^^

Here is an example of connecting::
  
  Map params = new HashMap();
  params.put("dbtype", "db2");        //must be db2 or DB2
  params.put("host", "localhost");    //the name or ip address of the machine running DB2
  params.put("port", "50000");        //the port that DB2 is running on (generally 50000)
  params.put("database", "geotools"); //the name of the database to connect to.
  params.put("user", "db2admin");     //the user to connect with
  params.put("passwd", "adminpw");    //the password of the user
  params.put("tabschema", "SPEAR");   //the table schema
  
  DataStore datastore = DataStoreFinder.getDataStore(params);

Advanced Connection Parameters
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Additional connection parameters are available to configure connection pool use, or
to feed in your own DataSource.

For more information check the java docs for:

* DB2NGJNDIDataStoreFactory
* DB2NGDataStoreFactory

