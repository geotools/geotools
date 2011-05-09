===
API
===

The **gt-api** module is where we publish out stable interfaces that are implemented by the rest of the library. 

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      status
      internal

.. toctree::
   :maxdepth: 1

   datastore
   sld
   parameter
   envelope
   jts
   convert

These interfaces build on the ideas and concepts defined by standards (as captured in the **gt-opengis** module).

.. image:: /images/gt-api.png

The gt-api module provides:

* Interfaces implemented by:doc:`gt-main <../main/index>` such as 
* Utility classes to help with integration such as ReferencedEnvelope and 
* :doc:`gt-main <../main/index>` offers helper classes to translate Geometry into a Java Shape

In general:

* gt-opengis module defines data structures and concepts
* gt-api module defines data access and functionality