Main
====

The ``gt-main`` module extends the ``gt-opengis`` concepts with additional interfaces forming the GeoTools API.

The ``gt-main`` module provides default implementations for the GeoTools API
(Filter, Style, Feature etc...) and enough glue code to make creating an application
possible (various builders and utility classes).

.. figure:: /images/gt-main.svg
    
   gt-main module

The ``gt-main`` module is responsible for:

* Default implementation :doc:`gt-opengis <../api/index>` interfaces for ``Feature``, ``FeatureType``, ``Filter`` and ``Style``
* Default set of :doc:`gt-opengis <../api/filter>` Functions for working with spatial data
* Helper classes for your own application development such as ``DataUtilities`` and ``SimpleFeatureTypeBuilder``
* Abstract classes to help implementers of :doc:`DataStore <datastore>`

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
   envelope
   jts
   shape
   datastore
   repository
   sld
   urlchecker
