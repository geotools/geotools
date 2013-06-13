DB2 Plugin
----------

Supports direct access to a DB2 database.

A couple of really good things have happened for DB2 support recently. We have obtained permission to distribute the DB2 driver with GeoTools, and you can now download a free community version of DB2 .

**References**

* http://www-01.ibm.com/software/data/db2/express/

**Maven**
   
Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-db2</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

+-------------+------------------------------------------+
| Parameter   | Description                              |
+=============+==========================================+
| "dbtype"    | Must be the string "db2"                 |
+-------------+------------------------------------------+
| "host"      | Machine name or IP address to connect to |
+-------------+------------------------------------------+
| "port"      | Port number to connect to, default 50000 |
+-------------+------------------------------------------+
| "tabschema" | The database schema to access            |
+-------------+------------------------------------------+
| "database"  | The database to connect to               |
+-------------+------------------------------------------+
| "user"      | User name                                |
+-------------+------------------------------------------+
| "passwd"    | Password                                 |
+-------------+------------------------------------------+

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

Registering spatial columns
^^^^^^^^^^^^^^^^^^^^^^^^^^^

It is necessary to register the spatial columns of a table:: 

   db2se register_spatial_column mydb
        -tableName mytable -columnName mycolumn -srsName USA_SRS_1

.. note::

   If a SQL view includes a spatial column, the column has to be registered for this view.

.. note::

   If a historical table includes a spatial column, this column has to be registered too. (See DB2 temporal support, since DB2 version 10)


Speeding up extent calculation
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Since DB2 Spatial Extender V10 it is possible to store the extent of a geometry column during registration::

   db2se register_spatial_column mydb
        -tableName mytable -columnName mycolumn -srsName USA_SRS_1 -computeExtents 1
        
.. note::        
        
   This makes only sense if the table is populated before registration and there will be no future modifications altering the extent dramatically.
   

Paging Support
^^^^^^^^^^^^^^

Paging support is necessary to retrieve only a subset of a query result. (e. g. rows numbered 100 - 200). Many databases support the SQL keywords
LIMIT and OFFSET, Oracle uses a pseudo column called ROWNUM. This plugin uses a strategy depending on the DB2 compatibility mode.          

*  In MYSQL compatibility mode, LIMIT and OFFSET is used 

*  In Oracle compatibility mode, ROWNUM is used

*  Fetch needed rows and ignore the unneeded rows (e. g fetch rows 1 - 200 and ignore rows from 1-100). 
   This may result in very bad performance. Using a scrollable cursor would not help since BLOBs (vector data) are
   not supported for such cursors in DB2.
   
The strategy used can be found in the log file. If no paging support is enabled, the log file contains a warning::        
   
   Try to set MySql or Oracle compatibility mode
   dbstop
   db2set DB2_COMPATIBILITY_VECTOR=MYS
   db2start
   
The above commands enable  MYSQL compatibility, the command for Oracle compatibility mode is::

   db2set DB2_COMPATIBILITY_VECTOR=ORA
   
On success the log file contains::
   
    Using LIMIT OFFSET for paging support
    
or::

   Using Oracle ROWNUM for paging support       
    
    
   