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

Licence
^^^^^^^

This plugin depends on the IBM Informix JDBC driver, which is sourced through Maven. Please read the
`licence agreement`_ before using it.

.. _licence agreement: https://www-40.ibm.com/software/sla/sladb.nsf/doclookup/CA4476C0AF8346EC852579290012D218?OpenDocument
