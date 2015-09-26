===
WMS
===

The gt-wms extension offers a full featured web map server client. This allows your application to connect to a range of web map services, review the capabilities and issue requests such as "GetMap" and "GetInfo".

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq

.. toctree::
   :maxdepth: 1
   
   wms

The client code takes care of version negotiation, and even a few server specific wrinkles for you.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-wms</artifactId>
      <version>${geotools.version}</version>
    </dependency>

References:

* http://www.opengeospatial.org/standards/wms (OGC standard)
* http://geoserver.org/ (WMS implemented with GeoTools)
* :doc:`/tutorial/raster/image` (tutorial)
