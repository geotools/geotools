JTS Utility Class
-----------------

The JTS Utility class is used to smooth out some of the common 
JTS Geometry activities.

Reference:

* http://docs.geotools.org/stable/javadocs/org/geotools/geometry/jts/JTS.html
* :doc:`/tutorial/geometry/geometrycrs` (tutorial)

Distance
^^^^^^^^

There is a helper method allowing you to calcualte the real-world distance between two points:
  
.. literalinclude:: /../src/main/java/org/geotools/jts/JTSExamples.java
   :language: java
   :start-after: // orthodromicDistance start
   :end-before: // orthodromicDistance end

Internally this method makes use of GeodeticCalculator which offers a more general purpose solution
able to take the distance between any two points (even if they are provided in different
coordiante reference systems).

Transform
^^^^^^^^^

You can make use of *MathTransform** directly - it has methods for feeding **DirectPosition** instances in one at a time, transforming, and returning a modified **DirectPosition**.

The problem is our JTS Geometry instances are built out of **Coordinate** instances rather than using **DirectPosition**.

The **JTS** utility class defines a helper method for this common activity::
  
  import org.geotools.geometry.jts.JTS;
  import org.geotools.referencing.CRS;
  
  CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
  CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
  
  MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
  Geometry targetGeometry = JTS.transform( sourceGeometry, transform);
  
As a quick example, you can make use of an affine transformation to perform simple transformations such as rotation and scale.::
  
  Coordinate ancorPoint = geometry.getCentroid(); // or some other point
  AffineTransform affineTransform = AffineTransform.getRotateInstance(angleRad, ancorPoint.x, ancorPoint.y);
  MathTransform mathTransform = new AffineTransform2D(affineTransform);
  
  Geometry rotatedPoint = JTS.transform(geometry, mathTransform);

The same approach works with a JTS Coordinate::
  
  // by default it can make a new Coordinate for the result
  Coordinate targetCoordinate = JTS.transform( coordinate, null, transform );
  
  // or make use of an existing destination coordinate (to save memory)
  JTS.transform( coordinate, destination, transform );
  
  // or modify a coordinate in place
  JTS.transform( coordinate, coordinate, transform );

And also a JTS Envelope, although this case is a bit special in that you get a chance to specify how many points along the edge of the boundary are sampled. If you specify 5, five points along the top, bottom, left and right edges will be transformed - giving you a chance to better account for the curvature of the earth.:
  
.. literalinclude:: /../src/main/java/org/geotools/api/APIExamples.java
   :language: java
   :start-after: // transformEnvelope start
   :end-before: // transformEnvelope end
  
Finally the common target of DefaultGeographicCRS.WGS84 is given its own method (to quickly
transform to geographic bounds)::

  Envelope geographicBounds = JTS.toGeographic( envelope, dataCRS );

Finally there is a very fast method for performing a transform directly on an array of doubles::
  
  JTS.xform( transform, sourceArray, destinationArray );

Convert
^^^^^^^

There are a number of methods to help convert JTS Geometry to some of the ISO Geometry ideas used
by the referencing module.

Quickly convert from a JTS Envelope to the ISO Geometry Envelope (with a provided CoordinateReferenceSystem)::

   Envelope envelope = geometry.getEnvelopeInternal();

   // create with supplied crs
   Envelope2D bounds = JTS.getEnvelope2D( envelope, crs );
   
   // Check geometry.getUserData() for srsName or CoordinateReferenceSystem
   ReferencedEnvelope bounds = JTS.toEnvelope( geometry );

Here are also a large number of methods to help you create a geometry from a range of sources::

    // Convert a normal JTS Envelope
    Polygon polygon = JTS.toGeometry( envelope );
    
    // The methods take an optional GeometryFactory
    Polygon polygon2 = JTS.toGeometry( envelope, geometryFactory );
    
    // Or from ISO Geometry BoundingBox (such as ReferencedEnvelope)
    Polygon polygon3 = JTS.toGeometry( bounds );
    
    // Or from a Java2D Shape
    Geometry geometry = JTS.toGeometry( shape );

Smooth
^^^^^^

A recent addition is the use of splines to "smooth" a geometry into a single curve that contains
all the points.::
   
   // The amount of smoothing can be set between 0.0 and 1.0
   Geometry geometry = JTS.smooth( geometry, 0.4 );
