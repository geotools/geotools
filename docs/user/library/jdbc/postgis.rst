PostGIS Plugin
--------------

Supports direct access to the PostGIS database.

References:

Related

* http://www.foss4g2007.org/workshops/W-04/ PostGIS workshop if you need an introduction to setting up PostGIS

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-postgis</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the ``groupId`` is ``org.geotools.jdbc`` for this and other JDBC plugin modules.

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

====================== ============================================
Parameter              Description
====================== ============================================
``dbtype``             Must be the string ``postgis``
``host``               Machine name or IP address to connect to
``port``               Port number to connect to, default 5432
``schema``             The database schema to access
``database``           The database to connect to
``user``               User name
``passwd``             Password
``preparedStatements`` Use 
``encode functions``   
====================== ============================================

Creation
^^^^^^^^

Connect using DataStore finder:

.. literalinclude:: /../src/main/java/org/geotools/jdbc/JDBCExamples.java
   :language: java
   :start-after: // postgisExample start
   :end-before: // postgisExample end

Advanced
^^^^^^^^

+------------------------+------------------------------------------------+
| Parameter              | Description                                    |
+========================+================================================+
| ``loose bbox``         | Flag controlling loose ``bbox`` comparisons,   |
|                        | default is true                                |
+------------------------+------------------------------------------------+
| ``preparedStatements`` | Flag controlling whether prepared statements   |
|                        | are used, default is false                     |
+------------------------+------------------------------------------------+
| ``encode functions``   | Flag controlling if some common functions can  |
|                        | be encoded into their SQL equivalent           |
+------------------------+------------------------------------------------+

By default JsonArrayContains function will be delegated to @> operator. However for postgres versions >= 12 it will be delegated to jsonPathExists function.
jsonPathExists is able to search values in the root level array.

Example use::
  
  params.put(PostgisNGDataStoreFactory.LOOSEBBOX, true );
  params.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
  
Configuration Flags
^^^^^^^^^^^^^^^^^^^

An optimization on spatial queries can be applied, to avoid a bug with PostGIS query planner with big geometries and small bounding boxes, setting a system property.

This optimization can be enabled using a system-wide default from the command line::
  
  java -Dorg.geotools.data.postgis.largeGeometriesOptimize=true