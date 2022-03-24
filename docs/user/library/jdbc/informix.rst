Informix Plugin
---------------

Supports direct access to an Informix database.

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-informix</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the ``groupId`` is ``org.geotools.jdbc`` for this and other JDBC plugin modules.

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

**Connection parameters**

+---------------+----------------------------------------------------+
| Parameter     | Description                                        |
+===============+====================================================+
| ``dbtype``    | Must be the string ``informix-sqli``               |
+---------------+----------------------------------------------------+
| ``user``      | User name                                          |
+---------------+----------------------------------------------------+
| ``passwd``    | Password                                           |
+---------------+----------------------------------------------------+
| ``jdbcUrl``   | The JDBC URL, including the host, port and DB name |
+---------------+----------------------------------------------------+

Creating
^^^^^^^^

Here is a quick example::

  java.util.Map params = new java.util.HashMap();
  params.put(InformixDataStoreFactory.DBTYPE.key, "informix-sqli");
  params.put(InformixDataStoreFactory.USER.key, "geotools");
  params.put(InformixDataStoreFactory.PASSWD.key, "geotools");
  params.put(InformixDataStoreFactory.JDBC_URL.key, "jdbc:informix-sqli://localhost:9088/geotools");
  
  DataStore dataStore=DataStoreFinder.getDataStore(params);
