Welcome GeoServer Developers
============================

.. sidebar:: GeoServer
   
   http://www.geoserver.org/
   
   .. image:: /images/logo/GeoServer_200.png
   
   Allows users to share and edit geospatial data. Designed for interoperability, it publishes
   data from any major spatial data source using open standards (WFS, WCS, WMS).

The GeoServer community is the leading example of the power of GeoTools and a source of much of
the research and development work benefiting the library as a whole. This is aided by a long
standing tradition of pushing as much reusable code as possible out into the GeoTools library.

Catalog for DataStore Management
--------------------------------

The GeoServer project is wired together using Spring; giving you an easy way to access the
**Catalog** responsible for managing DataStores.

DataStores represent physical connections to external servers (often with their own connection
pool and resources to be cleaned up after). As such it is important that you always use a
Catalog to allow GeoServer to know when you are obtaining resources, and clean up the mess
if your code happens to crash.

Example of looking up a FeatureTypeInfo for a GeoServer layer:

.. code-block:: java
   
   LayerInfo layerInfo = catalog.getLayerByName(layerName);
   if (layerInfo == null) {
       throw new IllegalArgumentException("Unable to locate layer: " + layerName);
   }
   ResourceInfo resourceInfo = layerInfo.getResource();
   if (resourceInfo == null) {
       throw new IllegalArgumentException("Unable to locate ResourceInfo for layer:" + layerName);
   }   
   if (resourceInfo instanceof FeatureTypeInfo) {
       FeatureTypeInfo featureTypeInfo = (FeatureTypeInfo) resourceInfo;
       SimpleFeatureSource featureSource = (SimpleFeatureSource) featureTypeInfo
                .getFeatureSource(null, GeoTools.getDefaultHints());
       ...
   }

These data structures provide easy access to the FeatureType and are used to provide additional functionality (such as SQL views). You may also user supplied configuration if you just look at GeoTools data structures (such as the case with a user supplied coordinate reference system).

.. code-block:: java

   CoordinateReferenceSystem crs =
      ProjectionPolicy.FORCE_DECLARED == resourceInfo.getProjectionPolicy
         ? resourceInfo.getCRS()
         : resourceInfo.getNativeCRS();

For GeoTools code that needs to look up a FeatureSource can use the Repository interface. GeoServer provides an implementation of Repository (wrapped around the GeoServer catalog).

References:

* :doc:`/library/main/repository`
* http://geoserver.org/display/GEOS/Configuration+Proposal

