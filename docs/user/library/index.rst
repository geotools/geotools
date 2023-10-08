*******
Library
*******

The GeoTools library is organized into a series of jars. These jars form a software stack,
each building on the services of the others. Individual jars such as ``gt-jdbc`` are extended
with plugins in order to support additional capabilities. For ``gt-jdbc`` these plugins include
support for common spatial databases such as ``gt-jdbc-postgis`` to support the PostGIS database.

.. toctree::
   :maxdepth: 1
   
   api/index
   jts/index
   metadata/index
   referencing/index
   coverage/index
   main/index
   data/index
   jdbc/index
   cql/index
   xml/index
   render/index
   http/index

Reference:

* :doc:`../welcome/architecture`