OpenGIS
=======

Interfaces for GeoSpatial concepts, often defined by the OGC or ISO standards bodies. The interfaces in this module serve as a great reference if you do not have the time to purchase and read the official standards documents. Approach the standards using an environment you are comfortable with - Java!

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq

.. toctree::
   :maxdepth: 1

   model
   type
   feature
   filter
   coverage
   geometry
   cs
   se
   progress
   name
   text
   parameter
   unit

GeoTools is all about implementing spatial solutions, and we do our very best to follow a don't invent here policy (rather than get off topic). By referencing standards we are able to use well understood names for common spatial ideas and constructs.

.. image:: /images/gt-opengis.png

The gt-opengis module provides:

* interfaces implemented by :doc:`gt-main <../main/index>` such as Feature, FeatureType, Filter and Function
* interfaces implemented by :doc:`gt-coverage <../referencing/index>` such as GridCoverage
* interfaces implemented by :doc:`gt-referencing <../referencing/index>` such as CoordinateReferenceSystem
* interfaces implemented by :doc:`gt-metadata <../metadata/index>` such as Citation

For more information on the standards covered by the library as whole:

* :ref:`standards`