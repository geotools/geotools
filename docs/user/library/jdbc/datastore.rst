JDBCDataStore
-------------

The gt-jdbc module is odd in that it provides a single implementation of a DataStore (JDBCDataStore actually) and you are forced to use the DataStoreFinder mechanism in order to access the different formats.

In this case each format is implementing a custom SQLDialect as discussed in the :doc:`gt-jdbc internals <internal>` page.

Creating a JDBCDataStore
^^^^^^^^^^^^^^^^^^^^^^^^

The process of creating a JDBC data store follows the regular steps of creating any type of datastore. That is defining the parameters to connect to the database in a map, and then creating the data store factory.:
  
.. literalinclude:: /../src/main/java/org/geotools/data/SimpleFeatureStoreExamples.java
   :language: java
   :start-after: // postgisExample start
   :end-before: // postgisExample end

An important parameter is the **dbtype** parameter which specifies the type of database to connect to.

Using JNDI
''''''''''

The above example illustrates the case where a direct connection to a database is specified. A JNDI connection can also be specified using the JDBCJNDIDataStoreFactory class.::
  
  Map map = new HashMap();
  map.put( "dbtype", "postgis");
  map.put( "jndiReferenceName", "java:comp/env/jdbc/geotools");
  
  DataStore store =  DataStoreFinder.getDataStore(map);

Connection Pooling
^^^^^^^^^^^^^^^^^^

Internally JDBC data stores use a connection pool when creating database connections. Properly configuring a connection pool for your application can have a profound impact on performance.

Here is an example::
  
  map.put( "max connections", 25);
  map.put( "min connections", 10);
  map.put( "connection timeout", 5);
  
Connection validation is on by default, it takes a small toll to make sure the connection is still valid before using it (e.g., make sure the DBMS did not drop it due to a server side timeout).
If you want to get extra performance and you're sure the connections will never be dropped you can disable connection validation with::
  
  map.put( "validating connections", false);

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

All the gt-jdbc formats have the option of using the following set of connection parameters. You will
need to consult the documentation for the plugin you are using; the following is provided to
help with consistency.

Crucial to this is the agreement that they use a unique "dbtype" for each format.

+--------------------------------+----------------------------------------------------+
| Parameter                      | Description                                        |
+================================+====================================================+
| "dbtype"                       | A URL of the file ending in "shp"                  |
+--------------------------------+----------------------------------------------------+
| "host"                         | Server to connect to                               |
+--------------------------------+----------------------------------------------------+
| "port"                         | Port to connect to.                                |
+--------------------------------+----------------------------------------------------+
| "database"                     | database instance                                  |
+--------------------------------+----------------------------------------------------+
| "schema"                       | database schema                                    |
+--------------------------------+----------------------------------------------------+
| "user                          | username if required                               |
+--------------------------------+----------------------------------------------------+
| "passwd"                       | password if required                               |
+--------------------------------+----------------------------------------------------+
| "namespace"                    | namespace prefix to use for content                |
+--------------------------------+----------------------------------------------------+

**Advanced**

+--------------------------------+----------------------------------------------------+
| Parameter                      | Description                                        |
+================================+====================================================+
| "Data Source"                  | Optional: DataSource instance to use.              |
+--------------------------------+----------------------------------------------------+

**Connection Pooling**

+--------------------------------+----------------------------------------------------+
| Parameter                      | Description                                        |
+================================+====================================================+
| "max connections"              | Maximum number of connection the pool will hold at |
|                                | any time, default is 10                            |
+--------------------------------+----------------------------------------------------+
| "min connections"              | Minimum number of connection the pool will hold at |
|                                | any time, default is 1                             |
+--------------------------------+----------------------------------------------------+
| "connection timeout"           | Maximum number of second the pool will wait when   |
|                                | trying to obtain a connection, default is 20       |
|                                | seconds                                            |
+--------------------------------+----------------------------------------------------+
| "validate connections"         | Flag controlling if the pool should validate       |
|                                | connections when a new connection is obtained      |
+--------------------------------+----------------------------------------------------+
| "Max open prepared statements" | Maximum number of prepared statements kept open    |
|                                | and cached for each connection in the pool.        |
|                                | Set to 0 to have unbounded caching, -1 to disable  |
+--------------------------------+----------------------------------------------------+

**Tweaking and Performance**

+--------------------------------+----------------------------------------------------+
| Parameter                      | Description                                        |
+================================+====================================================+
| "fetch size"                   | Number of records to read                          |
+--------------------------------+----------------------------------------------------+
| "Primary key metadata table"   | The optional table containing primary key          |
|                                | structure and sequence associations. Either        |
|                                | expressed as 'schema.name' or just 'name'          |
+--------------------------------+----------------------------------------------------+
| "Expose primary keys"          | "Expose primary key columns as attributes          |
+--------------------------------+----------------------------------------------------+
