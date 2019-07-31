Metadata
========

Metadata is used by GeoTools to describe GeoSpatial information and services. The data structure
is an implementation of ISO19115 (for metadata describing spatial data).

.. image:: /images/gt-metadata.png

The gt-metadata module also pulls double duty as the first implementation module it is responsible for providing
the facilities we use to wire the library together at runtime.

The gt-metadata module is responsible for:

* implementation of the metadata interfaces from :doc:`gt-opengis <../opengis/index>` such as Citation and Identifier
* Configuring the library at runtime with **GeoTools** and **FactoryRegistry** and **Logging** facilities
* Utility classes with helpful implementations for Collections, Caching and Object pools
* Default set of :doc:`Converters <convert>` supporting basic Java types

**References**

* ISO 19115
* ISO 19119

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-metadata</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      internal/index
      pool
      
.. toctree::
   :maxdepth: 1
   
   metadata
   geotools
   range
   text
   convert
   urls
