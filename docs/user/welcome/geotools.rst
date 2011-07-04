GeoTools
========

GeoTools is an open source (LGPL) Java code library which provides standards compliant methods for
the manipulation of geospatial data, for example to implement Geographic Information Systems.
The GeoTools library data structures are based on Open Geospatial Consortium (OGC) specifications.

.. image:: /images/geotools.png

GeoTools is used by a number of projects including web services, command line tools and desktop
applications.

Core Features
-------------

* Definition of interfaces for key spatial concepts and data structures
  
  * Integrated Geometry support provided by Java Topology Suite (JTS)
  * Attribute and spatial filters using OGC Filter Encoding specification
  
* A clean data access API supporting feature access, transaction support and locking between threads
  
  * Access GIS data in many file formats and spatial databases
  * Coordinate reference system and transformation support
  * Work with an extensive range of map projections
  * filter and analyze data in terms of spatial and non-spatial attributes

* A stateless, low memory renderer, particularly useful in server-side environments.
  
  * compose and display maps with complex styling

* Powerful *schema asisted* parsing technology using XML Schema to bind to GML content
  
  The parsing / encoding technology is provided with bindings for many OGC standards
  including GML, Filter, KML, SLD, and SE.
  
* GeoTools Plugins: open plug-in system allowing you to teach the library additional formats
  
  * Plug-ins for the ImageIO-EXT project allowing GeoTools to read additional raster formats from GDAL
 
* GeoTools Extensions

  Provide additional capabilities built using the spatial facilites of the core library.

  .. image:: /images/extension.png
  
  Extensions provide graph and networking support (for finding the shortest path), validation,
  a web map server client, bindings for xml parsing and encoding and color brewer!

* GeoTools Unsupported
  
  GeoTools also operates as part of a wider community with a staging area used to foster new
  talent and promote experimentation.
  
  Some highlights are swing support (used in our tutorials!), swt, local and web process support,
  additional symbology, additional data formats, generation of grids and a couple of implementations
  of ISO Geometry.

Supported Formats
-----------------  

* raster formats and data access
  
  arcsde, arcgrid, geotiff, grassraster, gtopo30, image (JPEG, TIFF, GIF, PNG), imageio-ext-gdal, 
  imagemoasaic, imagepyramid, JP2K, matlab
  
* Database "jdbc-ng" support
  
  db2, h2, mysql, oracle, postgis, spatialite, sqlserver

* Vector formats and data access
  
  app-schema, arcsde, csv, dxf, edigeo, excel, geojson, org, property, shapefile, wfs

* XML Bindings

  Java data structures and bindings provided for the following:
  xsd-core (xml simple types), fes, filter, gml2, gml3, kml, ows, sld, wcs, wfs, wms, wps, vpf.
  
  Additional Geometry, Filter and Style parser/encoders available for DOM and SAX applications.
  
Implemented Standards
---------------------

Support of numerous Open Geospatial Consortium (OGC) standards:

* OGC Style Layer Descriptor / Symbology Encoding data structures and rendering engine
* OGC General Feature Model including Simple Feature support
* OGC Grid Coverage representation of raster information
* OGC Filter and Common Constraint Language (CQL)
* Clients for Web Feature Service, Web Map Service and experimental support for Web Process Service
* ISO 19107 Geometry

Details
-------
 
Website: http://geotools.org/

Licence: LGPL

Software Version: 2.7.1

Supported Platforms: Cross Platform Java

API Interfaces: 

Support: :doc:`support`

Quickstart
----------

* :doc:`/tutorial/quickstart/index`
