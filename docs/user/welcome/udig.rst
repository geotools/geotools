Welcome to uDig Developers
==========================

.. sidebar:: uDig
   
   http://udig.refractions.net/
   
   .. image:: /images/logo/udig_64.gif
   
   User-friendly Desktop Internet GIS (uDig) supports GeoTools functionality
   in a slick "Eclipse Rich Client Platform" applicaiton.

This page gathers up some notes for those of us looking to put uDig to good use. The uDig project operates as a Eclipse RCP smash up of as many geospatial projects as it can get a hold of - and GeoTools is its first victim.

The uDig project does its best to stick to OpenGIS interfaces (the same interfaces that GeoTools implements). Like GeoTools it makes use of JTS to capture the idea of a Geometry.

Catalog to manage DataStores
----------------------------

DataStores represent physical connections to external servers (often with their own conneciton
pool and resources to be cleaned up after). As such it is important that you always use the uDig
Catalog to allow the applicaiton to know when you are obtaining resources, and clean up the mess
if your code happens to crash.

References:

* :doc:`/library/main/repository`
* http://udig.refractions.net/confluence/display/DEV/2+Catalog

How do I add stuff
------------------

The FeatureStore interface is used to add content.

You can get a FeatureStore in uDig by writing an operation. The operation will show up on the right click menu of anything that can be turned into a FeatureStore (such as a read/write Layer in the map, or File in the catalog)::
   
   public class AddStuff implements IOp {
       static GeometryFactory geomFactory = new GeometryFactory();
       public void op( Display display, Object target, IProgressMonitor monitor ){
           
           FeatureStore store = (FeatureStore) target; // AddStuff registered to accept FeatureStore
           FeatureType schema = store.getSchema();
   
           FeatureCollection collection = FeatureCollections.newInstance();
           Object attributes = new Object[]{
               "name",
               new geomFactory.createPoint( new Coordinate(1,1) ),
               new Integer(3)
           };
           Feature feature = schema.create( attribtues, "featureId" );
           ...
           collection.add( feature );
   
           store.addFeatures( collection );
       }
   }

You can also get a FeatureStore directly from from an ILayer::
   
   if( layer.hasResource( FeatureStore.class ) ){
       FeatureStore store = layer.getResource( FeatureStore.class, monitor );
       ...
   }

Or directly from a GeoResource::
   
   if( geoResource.canResolve( FeatureStore.class ) ){
       FeatureStore store = handle.resolve( FeatureStore.class, monitor );
       ...
   }

Notes:

* features you add this way will not actually hit the disk until the user presses the commit button.
* do not assume anything about the layer - even if it is a WMS layer uDig may be able to hunt you
  down a FeatureStore by looking for a matching WFS service
