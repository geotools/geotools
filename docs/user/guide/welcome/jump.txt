Welcome OpenJump Developers
===========================

This page gathers up some notes for those of us with a background in JUMP or OpenJUMP. The GeoTools project is built up around the same JTS topology library used by JUMP, many of the features and formats in JUMP (and now JTS) started out life as part of GeoTools library.

What is the equivalent of a Feature and FeatureSchema?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The GeoTools SimpleFeature and SimpleFeatureType classes play this role in the GeoTools library.

Here is a quick example::
   
   SimpleFeatureType schema = DataUtilities.createType( "example", "geom:Point,name:String,age:Integer,description:String" );
   
   SimpleFeature feature = SimpleFeatureBuilder.build( schema, new Object[]{ point, name, description }, null );

If you have a Feature you can get the schema::
   
   FeatureType schema = feature.getFeatureType();

If you have a collection of features you can get the schema::
   
   FeatureType schema = featureCollection.getSchema();

The GeoTools SimpleFeatureType model is very similar to that employed by JUMP (and suffer from the same limitations). You can make use of the more general purpose FeatureType and Feature classes to make use of facilities like associations and operations.

What license does GeoTools use?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

LGPL. So you can take the code and run away with it. We do ask that you funnel fixes back in our direction.

Because of the GPL restriction on the JUMP code base we cannot offer you wrappers (or really very much help at all). If you would kindly consider a GPL+Claspath license (the same one that Java uses) we would be able to implement the JUMP Feature and FeatureSchema interfaces and we could get along without all this fuss.

Can I just use the referencing stuff for reprojection
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Yes. The Referencing module is available "standalone" (without all of the rest of the GeoTools feature model and rendering system). But be warned the referencing module is standards happy, and you will drag in a number of dependencies.

For more information please see the Referencing section of this user guide. Remember if you want to support EPSG codes you will need a plugin like epsg-hsql.

It is recommended that you store the CoordianteReferenceSystem in your Geometry user data.

Be warned that the referencing module does not depend on JTS (it is pure math), there is a utility class (called JTS) which works an an example of how to transform JTS Geometry classe.