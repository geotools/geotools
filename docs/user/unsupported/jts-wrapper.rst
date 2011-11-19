JTS Wrapper Plugin
==================

The **gt-jts-wrapper** module was originally donated by SYS TECHNOLOGIES, it is an implementation of the ISO Geometry interfaces done up as wrappers around the Java Topology Suite SFSQL implementations.

Related Links:

* :doc:`gt-main geometry</library/opengis/geometry>`
* :doc:`gt-jts </library/jts/index>`

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-jts-wrapper</artifactId>
      <version>${geotools.version}</version>
    </dependency>


About JTS Wrapper
-----------------

Since the JTS project tracks the SFSQL specification there is not a direct one to one mapping between ISO Geometry constructs and JTS.

While this code has been used in a commercial setting there is no way this module is not going to be extended to support CurveSegments due to the above technical limitation.

There have been reports of synchronization problems between the wrapper classes (that implement the ISO Geometry interfaces) and their internal JTS Geometry object. But for the most part you can use this module without trouble.

Using JTS Wrapper
-----------------

The JTS Wrapper is a pure plugin and does not define any additional API. Place it on your
CLASSPATH and the GeoTools GeometryFactory will be able to pick up this implementation and use it.

It is recommended to use a GeometryBuilder in order to set up
a group of factories to work together::

  GeometryBuilder builder = new GeometryBuilde( DefaultGeographicCRS.WGS84 );        
  Point point = builder.createPoint( 48.44, -123.37 );

You could also make use of the factories directly::
  
  Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
  PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory( hints );
  PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory( hints );
  
  DirectPosition here = positionFactory.createDirectPosition( new double[]{48.44, -123.37} );
  Point point1 = primitiveFactory.createPoint( here );

Finally gt-main offers a WKT reader that you can use::
  
  WKTParser parser = new WKTParser( DefaultGeographicCRS.WGS84 );
  Point point = (Point) parser.parse("POINT( 48.44 -123.37)");

For additional examples review the more extensive documentation for the **gt-geometry** module.
