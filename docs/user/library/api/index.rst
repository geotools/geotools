===
API
===

The **gt-api** module is where we publish out stable interfaces that are implemented by GeoTools.
These interfaces build on the ideas and concepts defined by standards the **gt-opengis** module.

.. image:: /images/gt-api.png

The gt-api module provides:

* Interfaces implemented by :doc:`gt-main <../main/index>` such as *FeatureSource*; and
* Utility classes to help with integration such as *ReferencedEnvelope*; and 
* :doc:`gt-main <../main/index>` offers helper classes to translate Geometry into a Java Shape

In general *gt-opengis* module defines data structures and concepts while the *gt-api* module
provides data access and functionality.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-api</artifactId>
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
   sld
   parameter
   envelope
   jts
   convert
