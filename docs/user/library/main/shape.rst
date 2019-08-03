Java2D Shape
------------

GeoTools includes a couple of classes for generating a Java 2D Shape from a JTS Geometry.

The examples shown here are taken from ``LiteShapeTest``.

You can use ``LiteShape`` to quickly make a Java2D shape to display on the screen::
  
  AffineTransform affineTransform = new AffineTransform();
  LiteShape lineShape = new LiteShape(lineString, affineTransform, false);

You can also ask for the shape to be simplified; by default is to generalize to a single pixel.::
  
  LiteShape lineShape = new LiteShape(lineString, affineTransform, true);

If you are using anti-aliasing you may want to drop that down to half a pixel.::
  
  AffineTransform affineTransform = new AffineTransform();
  LiteShape lineShape = new LiteShape(lineString, affineTransform, true, 0.5 );

Drawing
^^^^^^^

The code makes use of an ``AffineTransform`` allowing you to scale and rotate the geometry into the correct position on the screen. You may consider starting with ``graphics2d.getTransform()`` representing your image coordinates and work from there.::
  
  public void draw( Graphics2d g2, Geometry geom ){
     AffineTransform transform = g2.getTransform(); // returns a copy
     transform.translate( 10, 10 ); // offset
     transform.scale( 0.5, 0.5 ); // scale by 50%
     LiteShape lineShape = new LiteShape(lineString, transform, false);
     g2.draw( lineShape );
  }

LiteShape2
^^^^^^^^^^

``LiteShape2`` is similar in function, but makes use of ``MathTransform`` (and thus can be used to handle
the case where you using reprojection to manipulate your geometry priory to display).

This class also makes use of a ``Decimator`` class to control how generalization occurs.::
  
  Decimator decimator = new Decimator(screen2world, paintArea );
  LiteShape2 lineShape = new LiteShape2(lineString, world2screen, decimator, false);
