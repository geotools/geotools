Main
====

The gt-main module provides default implementations for the remaining *gt-api* and *gt-opengis*
interfaces (Filter, Style, Feature etc...) and enough glue code to make creating an application
possible (various builders and utility classes).

.. image:: /images/gt-main.png

The gt-main module is responsible for:

* Default implementation :doc:`gt-opengis <../opengis/index>` interfaces for Feature, FeatureType and Filter and Style
* Default set of :doc:`gt-opengis <../api/convert>` Converters supporting basic Java types
* Default set of :doc:`gt-opengis <../opengis/filter>` Functions for working with spatial data
* Helper classes for your own application development such as *DataUtilities* and *SimpleFeatureTypeBuilder*
* Abstract classes to help implementors of :doc:`gt-api <../api/index>` DataStore

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
   filter
   feature
   collection
   shape
   geometry
   repository
