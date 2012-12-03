JDBC
====

Supports accessing feature information databases using the Java JDBC library.

.. image:: /images/gt-jdbc.png

The JDBC module is used as the base for all JDBC / Database backed DataStores. Alone it does not
contain any useful functionality. The useful functionality is contained within modules specific
to a particular database. 

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-jdbc</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      internal

.. toctree::
   :maxdepth: 1
   
   datastore

DataStore plugins:

.. toctree::
   :maxdepth: 1
   
   db2
   h2
   mysql
   oracle
   postgis
   spatialite
   sqlserver
   teradata

Unsupported plugins:

.. toctree::
   :maxdepth: 1
   
   ingres
