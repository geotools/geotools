MySQL Plugin
------------

Supports direct access to a MySQL database.

**Maven**::

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-mysql</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

**Basic connection parameters**

+-------------+----------------------------------------------+
| Parameter   | Description                                  |
+=============+==============================================+
| "dbtype"    | Must be the string "mysql"                   |
+-------------+----------------------------------------------+
| "host"      | Machine name or IP address to connect to     |
+-------------+----------------------------------------------+
| "port"      | Port number to connect to, default 3309      |
+-------------+----------------------------------------------+
| "database"  | The database to connect to                   |
+-------------+----------------------------------------------+
| "user"      | User name                                    |
+-------------+----------------------------------------------+
| "passwd"    | Password                                     |
+-------------+----------------------------------------------+

**Advanced Connection Parameters**

+------------------+----------------------------------------------+
| Parameter        | Description                                  |
+==================+==============================================+
| "storage engine" | Storage engine to use for created tables,    |
|                  | default is "MyISAM".                         |
+------------------+----------------------------------------------+

See additional notes below on storage engine.

Creating
^^^^^^^^

Here is a quick example::

  java.util.Map params = new java.util.HashMap();
  params.put(MySQLDataStoreFactory.DBTYPE.key, "mysql");
  params.put(MySQLDataStoreFactory.HOST.key, "localhost");
  params.put(MySQLDataStoreFactory.PORT.key, 3309);
  params.put(MySQLDataStoreFactory.DATABASE.key, "database");
  params.put(MySQLDataStoreFactory.USER.key, "geotools");
  params.put(MySQLDataStoreFactory.PASSWD.key, "geotools");
  
  DataStore dataStore=DataStoreFinder.getDataStore(params);

Storage Engine
^^^^^^^^^^^^^^

The MySQL Datastore is capable of creating new tables via DataStore#createSchema(). The storage engine connection parameter controls what storange engine is used for the new table. The default is "MyISAM" which is the only engine that supports spatial indexing. However depending on the use of the able other engines may be appropriate. For instance "InnoDB" is fully transaction safe so is more suitable to tables that will be undergoing many concurrent edits.

Example::
  
  params.put(MySQLDataStoreFactory.STORAGE_ENGINE, "MyISAM");
