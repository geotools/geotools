HANA Plugin
-----------

Supports direct access to a HANA database.

A free version of HANA (HANA Express Edition) can be downloaded at the link below.

You need HANA's JDBC driver ``ngdbc.jar`` to connect to HANA. Its license does not allow redistribution, so you have to download it separately. It is part of HANA Express Edition.

**References**

* https://www.sap.com/cmp/ft/crm-xu16-dat-hddedft/index.html

**Maven**
   
Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-hana</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "hana"
"host"         Machine name or IP address to connect to
"instance"     Instance of the database
"database"     Database to connect to. Leave empty in case of single-container databases. Set to ``SYSTEMDB`` to connect to the system database of a multi-container database.
"schema"       The database schema to access
"user"         User name
"passwd"       Password
============== ============================================

Creating
^^^^^^^^

Here is an example of connecting::
  
  Map params = new HashMap();
  params.put("dbtype", "hana");       //must be hana
  params.put("host", "localhost");    //the name or ip address of the machine running HANA
  params.put("instance", "00");       //the instance to connect to
  params.put("database", "GT");       //the name of the database to connect to
  params.put("schema", "geotools");   //the table schema
  params.put("user", "SYSTEM");       //the user to connect with
  params.put("passwd", "pw");         //the password of the user
  
  DataStore datastore = DataStoreFinder.getDataStore(params);

Advanced Geotools Parameters
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

+--------------------+-------------------------------------------+
| Parameter          | Description                               |
+====================+===========================================+
| "encode functions" | Flag controlling if a set of filter       |
|                    | functions are translated directly in SQL. |
|                    | Default is false.                         |
+--------------------+-------------------------------------------+

Importing spatial reference systems
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

HANA includes only a few spatial reference systems by default. Therefore, the plugin contains an application for installing all EPSG spatial reference systems.

On Windows::

  java -cp <path to gt-jdbc-hana.jar>;<path to ngdbc.jar> \
    org.geotools.data.hana.metadata.MetadataImport \
    <username> <host> <instance> [<dbname>]

On Linux and Mac::

  java -cp <path to gt-jdbc-hana.jar>:<path to ngdbc.jar> \
    org.geotools.data.hana.metadata.MetadataImport \
    <username> <host> <instance> [<dbname>]
