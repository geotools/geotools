===========
Unsupported
===========

The modules in the unsupported directory have not passed the quality assurance steps required to be included as
part of the library.

Not everything in the GeoTools code base is ready for public abuse quite yet; for the longest time developers would keep
these experiments to themselves. With the use of our maven build system we have been able to "deploy" some of these jars
for those of you wanting a sneak peek.

The following modules are provided for your amusement only to encourage collaborate on making these modules part of our library:

Research and Development:

.. toctree::
   :maxdepth: 1

   css
   csv
   efeature/index
   geojson
   geometry/index
   jts-wrapper
   mbstyle/index
   process/index
   swing/index
   swt/index
   wps
   sample

Unsupported rendering plugins:

* :doc:`/library/render/shapefile` - replaced by gt-render improvements

Unsupported DataStore implementations:

* :doc:`/library/data/caching` (Example) shows how to to cache a FeatureSource. Intended for use with a web feature server, maintaining a cache of  retrieved features.
* :doc:`/library/data/csv` (Active) support for the comma-separated values (CSV) file format used for tutorial
* :doc:`/library/data/dxf` (Inactive) Support for DXF format files
* :doc:`/library/data/edigeo` (Inactive) Support for EDIGEO file format
* :doc:`/library/data/excel` (Unknown) Support for Microsoft Excel files
* :doc:`/library/data/georest` (Unknown) Support for a REST service using GeoJSON
* :doc:`/library/data/mongodb` (Unknown) Support for using mongodb (https://www.mongodb.com/, https://en.wikipedia.org/wiki/MongoDB) as a feature store.
* :doc:`/library/data/wfs` (Stable but not active) Supports communcation with a Web Feature Server using the standard GeoTools DataStore API
* :doc:`/library/data/wfs-ng` (Inactive) Experimental code for "next generation" Web Feature Server support
* :doc:`/library/data/sfs` (Unknown) Support for the experimental GeoServer Simple Feature Service
* :doc:`/library/data/vpf` (Inactive) Support for VPF (Vector Product Format) files

Unsupported Raster modules:


* :doc:`/library/coverage/coverageio`
* :doc:`/library/coverage/image-collection` - publishes a large collection of non georeferenced images as a coverage reader
* :doc:`/library/coverage/geotiff_new`
* :doc:`/library/coverage/matlab`
* :doc:`/library/coverage/multidim` - New plugins supporting NetCDF and Grib formats
* :doc:`/library/coverage/tools`

Unsupported Referencing implementations:

* :doc:`/library/referencing/oracle` - allows an application to work an `EPSG <http://www.epsg-registry.org/>`_ table of map projections in an  Oracle database
* :doc:`/library/referencing/h2`- Allows an application to work an `EPSG <http://www.epsg-registry.org/>`_ table of map projections in an H2 (pure Java) database

.. note::

   If you really need one of these modules to work?

   Several of the unsupported modules (for example process) are really popular. That does not however mean that there is anyone in the
   GeoTools community taking care, fixing bugs and providing documentation. In some cases these modules are the work of students, in others
   a commercial company that donated the code (but in both cases more resources are needed to make the module usable and trustworthy).

   In the past we threw out such work - we are trying a change of policy here by letting you see what can be done
   (and has been done).

   If you really do need one of these modules cleaned up, perhaps for a deadline, please check out our support page - a
   range of commercial support options are available. Any one of these organisations can be hired to bring these modules up to speed.
