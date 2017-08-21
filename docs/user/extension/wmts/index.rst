====
WMTS
====

The gt-wmts extension builds on top of the :doc:`gt-tile-client <../tile-client/index>` module in order to offer a web map tile service client.
This allows your application to connect to KVP or REST based web map tile services, review the capabilities and
issue requests such as "GetTile".

.. toctree::
   :maxdepth: 1
   
   wmts

The client code takes care of version negotiation, and even a few server specific wrinkles for you.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-wmts</artifactId>
      <version>${geotools.version}</version>
    </dependency>

References:

* http://www.opengeospatial.org/standards/wmts (OGC standard)
* http://www.geowebcache.org/ (WMTS server)
* :doc:`gt-tile-client <../tile-client/index>`