Metadata
========

Metadata is used by GeoTools to describe GeoSpatial information and services. The data structure
is an implementation of ISO19115 (for metadata describing spatial data).

.. image:: /images/gt-metadata.png

The gt-metadata module also pulls double duty as the first implementation module it is responsible for providing the facilities we use to wire the library together at runtime.

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

.. toctree::
   :maxdepth: 1
   
   metadata
   faq

**Utilities**

The GeoTools library is big and nasty (and plays with a big nasty amounts of data) and as such is always slightly ahead of its time. We run into the limits of Java - often years before good solutions show up as part of the Java language.
   
.. toctree::
   :maxdepth: 1

   cache
   collections
   convert
   geotools
   logging/index
   pool
   range
   text
   urls
   utilities

In a perfect world none of these utility classes would need to exist - and we could just use software components from off the shelf projects. In many cases we have found that the volume of GeoSpatial information breaks assumptions made by projects, such as commons collections, leaving us no choice but to roll our own.

You are welcome to use these classes in your own application, in the event any of these solutions are deprecated instructions will be provided on how to move on.