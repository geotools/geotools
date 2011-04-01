Simple
^^^^^^

Most GIS data does not need a full dynamic type system with associations, operations, multiple values. With this in mind we have a "simple" extension of Feature and FeatureType providing to represent this kind of information.

References:

* `org.opengis.feature.simple <http://docs.geotools.org/stable/javadocs/org/opengis/feature/simple/package-summary.html>`_
* :doc:`../main/feature` gt-main feature code examples

SimpleFeature
^^^^^^^^^^^^^

A "simple" feature is an object that can be drawn on a map (ie a feature) that does not contain any complicated internal structure. A SimpleFeature is "flat" recording of key value pairs. At least one of the values should be a Geometry, and the list of keys is known ahead of time.

SimpleFeature may be used when:

* a feature's properties are limited to mandatory GeometryAttribute and Attribute (no complex attributes or multiplicity allowed).
* Attributes values may be null; but each attribute must be represented
* order of attribute values is considered significant allowing values to be looked up by attribute name, or by the order they are listed
* These restrictions match the abilities of a simple shapefile or database table

Here are the additional methods made available:


.. image:: /images/feature_simple.PNG

Here is an example of constructing a SimpleFeatureType::
   
   SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
   
   //set the name
   b.setName( "Flag" );
   
   //add some properties
   b.add( "name", String.class );
   b.add( "classification", Integer.class );
   b.add( "height", Double.class );
   
   //add a geometry property
   b.setCRS( DefaultGeographicCRS.WSG84 );
   b.add( "location", Point.class );
   
   //build the type
   SimpleFeatureType type = b.buildFeatureType();


