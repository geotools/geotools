JDBC
====

Supports accessing information databases using the Java JDBC library.

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
   spatiallite
   sqlserver

Unsupported plugins:

.. toctree::
   :maxdepth: 1
   
   ingres
   teradata

The JDBC module is used as the base for all JDBC/database backed datastores. Alone it does not contain any useful functionality. The useful functionality is contained within modules specific to a particular database. 

.. image:: /images/gt-jdbc.png