===========
Unsupported
===========

The modules in the unsupported directory have not passed the quality assurance steps required to be included as
part of the library.

Not everything in the GeoTools code base is ready for public abuse quite yet; for the longest time developers would keep
these experiments to themselves. With the use of our maven build system we have been able to "deploy" some of these jars
for those of you wanting a sneak peek.

The following modules are provided for your amusement only; you can use these pages to swap tips, horror stories and we hope collaborate on making these modules part of our library:

   
.. toctree::
   :maxdepth: 1
   :hidden:

   example
   geojson
   grid
   geometry/index
   image-collection
   jts-wrapper
   wkt
   postgis-versioned/index
   process/index   
   swing/index
   swt/index
   wps
   

| 

.. list-table:: **Modules under active development**
   :header-rows: 1

   * - Module
     - Description

   * - :doc:`example`
     - An example that can be used when creating a new unsupported module
     
   * - :doc:`grid`
     - Generates vector grids (lattices) of polygons or lines

   * - :doc:`image-collection`
     - Publishes a large collection of non georeferenced images as a coverage reader     
     
   * - :doc:`wkt`
     - A MarkFactory allowing the user to specify the shape using the simple geometry WKT syntax

   * - :doc:`swing/index`
     - A small collection of GUI components and utilities based on the Swing framework

   * - :doc:`swt/index`
     - A small collection of GUI components and utilities based on the SWT framework
     
|

.. list-table:: **Modules for which volunteers are needed**
   :header-rows: 1

   * - Module
     - Description

   * - :doc:`geometry/index`
     - Alternative geometry implementation with support for curves and surfaces. Not currently used by any
       GeoTools modules

   * - :doc:`jts-wrapper`
     - ISO Geometry interfaces implemented as wrappers around the Java Topology Suite SFSQL implementations

   * - :doc:`postgis-versioned/index`
     - Builds a data model supporting feature revisions on top of the PostGIS datastore

   * - :doc:`process/index`
     - API for working with GeoSpatial processes and annotations to make defining additional processes easy

   * - :doc:`wps`
     - Provides a “WPS client” API so programmers can easily build Web Process Service requests and parse the responses

   * - :doc:`geojson`
     - Demonstrates how to encode and decode GeoJSON files into GeoTools Feature Collections

|

.. list-table:: **Unsupported Referencing implementations**
   :header-rows: 1

   * - Module
     - Description

   * - :doc:`/library/referencing/oracle`
     - Allows an application to work an `EPSG <http://www.epsg-registry.org/>`_ table of map projections in an 
       Oracle database

   * - :doc:`/library/referencing/h2`
     - Allows an application to work an `EPSG <http://www.epsg-registry.org/>`_ table of map projections in an 
       H2 (pure Java) database

|

.. list-table:: **Unsupported DataStore implementations**
   :header-rows: 1

   * - Module
     - Description
     - Status

   * - :doc:`/library/data/caching`
     - Shows how to to cache a FeatureSource. Intended for use with a web feature server, maintaining a cache of 
       retrieved features.
     - Working example

   * - :doc:`/library/data/csv`
     - Support for the comma-separated values (CSV) file format
     - Under development

   * - :doc:`/library/data/dxf`
     - Support for DXF format files
     - Inactive

   * - :doc:`/library/data/edigeo`
     - Support for EDIGEO file format
     - Inactive

   * - :doc:`/library/data/excel`
     - Support for Microsoft Excel files
     - Under development ?

   * - :doc:`/library/data/georest`
     - Support for a REST service using GeoJSON
     - Unknown

   * - :doc:`/library/data/ogr`
     - Support for a range of vector file formats using the OGR library
     - Inactive

   * - :doc:`/library/data/wfs`
     - Supports communcation with a Web Feature Server using the standard GeoTools DataStore API
     - Stable but not actively maintained

   * - :doc:`/library/data/wfs-ng`
     - Experimental code for "next generation" Web Feature Server support
     - Inactive

   * - :doc:`/library/data/sfs`
     - Support for the experimental GeoServer Simple Feature Service
     - Unknown

   * - :doc:`/library/data/vpf`
     - Support for VPF (Vector Product Format) files
     - Inactive

|

.. list-table:: **Unsupported raster data modules**
   :header-rows: 1

   * - Module
     - Description

   * - :doc:`/library/coverage/coverageio`
     - description pending

   * - :doc:`/library/coverage/experiment`
     - description pending

   * - :doc:`/library/coverage/geotiff_new`
     - description pending

   * - :doc:`/library/coverage/matlab`
     - description pending

   * - :doc:`/library/coverage/tools`
     - description pending

|

.. list-table:: **Modules scheduled to be removed**
   :header-rows: 1

   * - Module
     - Description

   * - :doc:`/library/data/postgis` replaced by gt-jdbc-postgis
     - A DataStore implementation to access a PostGIS database. Old code, replacd by gt-jdbc-postgis, still available because postgis-versioning depends on it

   * - :doc:`/library/render/shapefile` replaced by gt-render
     - A custom renderer for shapefiles. About to be dropped since most of the optimizations have already been generalized and moved to gt-renderer, does not have anymore a maintainer interested in it

|

.. note:: 
   
   If you really need one of these modules to work?
   
   Several of the unsupported modules (for example process) are really popular. That does not however mean that there is anyone in the
   GeoTools community taking care, fixing bugs and providing documentation. In some cases these modules are the work of students, in others
   a commercial company that donated the code (but in both cases more resources are needed to make the module usable and trustworthy).
   
   In the past we simply threw out such work - we are trying a change of policy here by letting you see what can be done
   (and has been done).
   
   If you really do need one of these modules cleaned up, perhaps for a deadline, please check out our support page - a
   range of commercial support options are available. Any one of these organisations can be hired to bring these modules up to speed.
