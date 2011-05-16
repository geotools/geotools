Comparing CoordinateReferenceSystem
-----------------------------------

The objects defined in the referencing module can be compared in a number of different ways.

Related:

* http://home.gdal.org/projects/opengis/wktproblems.html

Compare Identifier
^^^^^^^^^^^^^^^^^^

You can use the identifier name to compare two CoordinateReferenceSystem objects.

A ReferenceIdentifier is often composed of two parts:

* Authority: assigned by by an authority body such as the European Petroleum Standards Group
* Code: uniquely identifiers the referencing object according to the authority body 
  mentioned above

Here is what that looks like::
  
  CoordinateReferenceSystem crs1 = shapefile.getSchema().getDefaultGeometry().getCoordianteRefernceSystem();
  CoordinateReferenceSystem crs2 = CRS.decode("EPSG:4326");
  
  if( crs1.getName().equals( crs2.getName() ){
     // The CoordinateReferenceSystem objects have the same formal definition,
     // the objects may not be exactly the same .. but they are supposed to be
     //
     // we are going to use crs2; as we trust our internal database more than
     // a projection file provided by the user
     ...
  }

In this case you are only comparing the name (ie metadata) of the two objects. You are trusting that the two objects are the same, if they are called the same name.

Compare Equals
^^^^^^^^^^^^^^

Equals comparison is used to check when a CoordinateReferenceSystem, and all its component parts such as Datum, are exactly equal.::
  
  CoordinateReferenceSystem crs1 = shapefile.getSchema().getDefaultGeometry().getCoordianteRefernceSystem();
  CoordianteReferenceSystem crs2 = CRS.decode("EPSG:4326");
  
  if( crs1.equals( crs2 ){
     // The CoordinateReferenceSystem objects are exactly the same data structure
     ...
  }

While the above makes sense, if it not often exactly what you want.

Often it does not matter what name (ie metadata) the data structure goes by, so far it numerically represents the same idea.

You can check if two objects are equal, while ignoring metadata (such as the exact name used for the CoordinateReferenceSystem). This technique compares significant values only, i.e. mostly (with a few exception) the ones that would changes the numerical results when transforming a position from one CRS to the other.::
  
  if( crs1.equals( crs2, false ) ){
      
  }

The name is usually ignored, with only 2 exceptions:

* Datum - The reason that we cannot ignore the name for a Datum is that it is the only
  way to differentiate between datum. We can have many datum using the same Ellipsoid and
  PrimeMeridian, and still be different datum (because AbstractDatum do not stores all
  the geodesy work behind the datum definition - it would be complex and out of ISO 19111
  scope anyway).
  
  In order to still allow a relaxed check we make use of a DatumAlias class, which help
  the referencing engine to recognise for example that "WGS84" and "D_WGS84" are
  synonymous.

* CoordinateSystemAxis - CoordinateSystemAxis is clearly defined by ISO19111, but some
  variation is still seen in the wild. The DefaultCoordinateSystemAxis implementation
  handles a couple of alias as well (much less than Datum however).
  
  We have be unable to define an alias for "x" and "y" up to date, because "x" (for
  example) can means too many different things: "Easting" in a map projection,
  "Geocentric X" in a GeocentricCRS, "Column" in an ImageCRS, etc...
