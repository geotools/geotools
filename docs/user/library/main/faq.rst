Main FAQ
--------

Q: What is gt-main responsible for?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The gt-main module is responsible for the default implementations of the formal interfaces provided by gt-api and gt-opengis. This includes the default implementations for the feature model, filter support, and style definition.

The gt-main module makes this functionality available through the plug-in system allowing you to make use of CommonFactoryFinder rather than directly depend on the default implementations provided here.

Q: How do I make a FeatureType?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can make a feature type quickly using using the DataUtilities class:

    SimpleFeatureType lineType = DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");

For greater control consider direct use of a FeatureTypeBuilder::
  
  SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
  
  //set the name
  b.setName( "Flag" );
  
  //add some properties
  b.add( "name", String.class );
  b.add( "classification", Integer.class );
  b.add( "height", Double.class );
  
  //add a geometry property
  b.setCRS( DefaultGeographicCRS.WSG84 ); // set crs first
  b.add( "location", Point.class ); // then add geometry

  //build the type
  final SimpleFeatureType FLAG = b.buildFeatureType();

Q: How do I modify a FeatureType?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You cannot modify a feature type directly as it is considered immutable and not subject to change.

You can however use a FeatureTypeBuilder to create a modified copy::

    SimpleFeatureType lineType = DataUtilities.createType("LINE", "geom:LineString,name:\"\",id:0");
    SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
    b.init( lineType );
    b.setName("POINT");
    b.add(0, "geom", Point.class );
    SimpleFeatureType pointType = b.buildFeatureType();

Q: How to get FeatureCollection to work with a 'for each' loop?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Feature collection is a wrapper around a live data stream; as such we need to be sure to *close*
the iterator after we are through with it:

.. literalinclude:: /../src/main/java/org/geotools/main/MainExamples.java
   :language: java
   :start-after: // exampleIterator start
   :end-before: // exampleIterator end

This requirement prevents us implementing Collection (and being compatible with 'for each' syntax.
I am afraid this is a fundamental limitation of Java and not something that can or should be
addressed in a future release.
