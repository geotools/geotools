Welcome GeoServer Developers
============================

.. sidebar:: GeoServer
   
   http://www.geoserver.org/
   
   .. image:: /images/logo/geoserver_200.png
   
   Allows users to share and edit geospatial data. Designed for interoperability, it publishes
   data from any major spatial data source using open standards (WFS, WCS, WMS).

The GeoServer community is the leading example of the power of GeoTools and a source of much of
the research and development work benifiting the library as a whole. This is aided by a long
standing tradition of pushing as much reusable code as possible out into the GeoTools library.

Catalog for DataStore Management
--------------------------------

The GeoServer project is wired together using Spring; giving you an easy way to access the
**Catalog** responsible for managaing DataStores.

DataStores represent physical connections to external servers (often with their own conneciton
pool and resources to be cleaned up after). As such it is important that you always use a
Catalog to allow GeoServer to know when you are obtaining resources, and clean up the mess
if your code happens to crash.

References:

* :doc:`/library/main/repository`
* http://geoserver.org/display/GEOS/Configuration+Proposal
