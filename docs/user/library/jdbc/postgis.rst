PostGIS Plugin
--------------

Supports direct access to the PostGIS database.

References:

Related

* http://www.foss4g2007.org/workshops/W-04/ PostGIS workshop if you need an intro to setting up PostGIS

**Maven**::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-postgis</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "postgis"
"host"         Machine name or IP address to connect to
"port"         Port number to connect to, default 5432
"schema"       The database schema to access
"database"     The databse to connect to
"user"         User name
"passwd"       Password
============== ============================================

Creation
^^^^^^^^

Connect using DataStore finder:

.. literalinclude:: /../src/main/java/org/geotools/data/SimpleFeatureStoreExamples.java
   :language: java
   :start-after: // postgisExample start
   :end-before: // postgisExample end

Advanced
^^^^^^^^

+----------------------+------------------------------------------------+
| Parameter            | Description                                    |
+======================+================================================+
| "loose bbox"         | Flag controlling loose bbox comparisons,       |
|                      | default is true                                |
+----------------------+------------------------------------------------+
| "preparedStatements" | Flag controlling wether prepared statements    |
|                      | are used, default is false                     |
+----------------------+------------------------------------------------+

Example use::
  
  params.put(PostgisDataStoreFactory.LOOSEBBOX, true );
  params.put(PostgisDataStoreFactory.PREPARED_STATEMENTS, true );
