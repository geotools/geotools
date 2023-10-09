Main
====

The GeoTools Main module adds a few more interfaces to the GeoTools API, and provides default implementations for the library.

.. figure:: /images/gt-main.svg
    
   gt-main module

The ``gt-main`` module adds new interfacs to the GeoTools library API:

* ``org.geotools.jts`` integrating geometry from JTS Topology Suite

* ``org.geotools.api.data`` which defines a DataStore API to read and write spatial content

The ``gt-main`` module contains the default implementations for the GeoTools API interfaces
(``Filter``, ``Style``, ``Feature``, ``DataStore``, etc...). Implementors will also find abstract
base classes in ``gt-main`` to use as a starting point for their own implementations.

* Default implementation :doc:`gt-api <../api/index>` interfaces for ``Feature``, ``FeatureType``, ``Filter`` and ``Style``
* Default set of :doc:`gt-api <../api/filter>` Functions for working with spatial data
* Abstract classes to help implementers of :doc:`DataStore <datastore>`

Finally ``gt-main`` provides different kinds of helper classes to make using everything easier.

* Utility classes (such as ``DataUtilities``)provide static methods perform common activities.
* Facades (such as ``JTS``) provide access to module services
* Builders (``SimpleFeatureTypeBuilder``) help you construct data structures, collecting all the information provided before using a fatory to create the required object.

**Maven**::

    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
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
   
   data
   parameter
   filter
   function_list
   feature
   collection
   bounds
   shape
   datastore
   repository
   sld
   urlchecker
