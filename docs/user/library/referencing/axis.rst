Axis
----

A quick page to help take apart the CoordianteReferenceSystem object and figure out what the raw data is measuring.

This is most often phrased as the question "What axis is X?" on our mailing list.

Using Transform
^^^^^^^^^^^^^^^

In the normal course of events you will set up a series of math transforms to map from your data representation to the
screen.

World to Screen
'''''''''''''''

We want to set up the following:

* Create a "screen" CoordinateReferenceSystem using DISPLAY_X and DISPLAY_Y as axis
* Construct a MathTransform transforming from your Data's CoordinateReferenceSystem to the "screen"

It is often easier to treat the problem in two steps:

1. Make a "viewport" model recording the location on the world you are displaying,
   
   For your first cut capture this "viewport" using DefaultGeographicCRS.WGS84::
   
     MathTransform data2world = CRS.findMathTransform( crs, DefaultGeographicCRS.WGS84 );
   
2. Construct a MathTransform from your "viewport" CRS to the "screen" CRS.::
   
     AffineTransform2D world2screen = new AffineTransform2D(); // start with identity transform
     world2screen.translate( viewport.minLong, viewport.maxLat ); // relocate to the viewport
     world2screen.scale( viewport.getWidth() / screen.width, viewport.getHeight() / screen.height ); // scale to fit
     world2screen.scale( 1.0, -1.0 ); // flip to match screen
   
   In the OGC Geographic Objects specification, the "viewport" CRS is named objective
   CRS and the "screen" CRS is named display CRS).

3. Combine the two MathTransforms::
     
     MathTransform transform = defaultMathTransformFactory.createConcatenatedTransform( data2world, world2screen );

4. Use the transform to modify your Geometry for the screen::
   
      Geometry screenGeometry = geometry.transform( screenCRS, transform );

5. Draw with confidence knowing that you are using DISPLAY_X and DISPLAY_Y::
     
     final int X = 0;
     final int Y = 1;
     public void drawPoint( Graphics g, DirectPosition point ){
         int x = point.getOrdinate(X);
         int x = point.getOrdinate(Y);
         g.drawPoint(x, y);
     }

6. As long as you do this correctly everything will work out, you will know what "X" is
   (because you defined it) and you will have forced your data into DISPLAY_X and
   DISPLAY_Y.

Screen to World
'''''''''''''''

The above instructions seem to cause some confusion; it may be easier to take the inverse of your world2screen transform
(usually you have one around since you were using it for drawing).::
  
  AffineTransform world2screen =
     RendererUtilities.worldToScreenTransform(mapContext.getLayerBounds(),
                                              new Rectangle(panelMap.getWidth(), panelMap.getHeight()));
  
  AffineTransform screen2world = world2screen.createInverse();
  Point2D pointScreen = screen2world.transform(pointScreenAbsolute, null);

Avoid Assumptions
^^^^^^^^^^^^^^^^^

This is a problem when you run into code that would like to assume that each DirectPosition contains data in (x,y) order
(ie matching the screen).

There exist many methods that are almost helpful:

* getHorizontalCRS return the GeographicCRS or ProjectedCRS part, or a DerivedCRS based on the above, that applies to
  the Earth's surface (ie real geophysical meaning - not the first two axis).

* You would still need to account for axis direction and polar coordinates on your own time.

A really common assumption for Java developers is to treat Geometry in exactly the same manner as Java2D Shape::
  
  public void drawPoint( Graphics g, DirectPosition point ){
      int x = (point.getOrdinate(X) - dx) * scale;
      int y = height - ((point.getOrdinate(Y) - dy) * scale);
      
      g.drawPoint(x, y);
  }

This code can be improved in several ways:

* "x" is assumed to be ordinate(0), "y" is assumed to be ordinate(1)

* A complicated transform is being performed by hand, "y" is inverted to match screen orientation, a transform is
  specified using dx and dy offsets and the entire result is scaled

This code will fail when presented with:

* data in "y"/"x" order
* data in which the direction of the axis is not what was expected
* data that was collected in polar coordinates

Please note that some GeoTools classes, such as CRSUtiities.getCRS2D, often make use of this assumption; blinding
returning the first 2 dimensions no matter what they are.

Quick Fix
'''''''''

If you run into some information that has proceeded with the above assumptions
you can make a quick fix::
  
  public void drawPoint( Graphics g, DirectPosition point ){
     final int X = 0;
     final int Y = 1;
     
     int x = (point.getOrdinate(X) - dx) * scale;
     int y = height - ((point.getOrdinate(Y) - dy)*scale);
     
     g.drawPoint(x, y);
  }

You will also need to provided a set of global hints::
  
  public void static main(String args[] ){
     Map config = new HashMap();
     config.put( Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, true );
     config.put( Hints.FORCE_STANDARD_AXIS_DIRECTIONS, true );
     
     Hints hints = new Hints( config );
     GeoTools.init( hints ); // Set FactoryUsingWKT as the default
     
     ...application code...
  }

GeoTools will now do its best to create CoordinateReferenceSystem objects that agree with your assumptions:

* data is in (x,y) order
* data is collected in the expected direction (ie. EAST and WEST are the same)

Lookup Axis
^^^^^^^^^^^

The following will allow you to math up to a correct axis::
  
  public void drawPoint( Graphics g, DirectPosition point ){
     final int X = indexOfX( point.getCoordinateReferenceSystem() );
     final int Y = indexOfY( point.getCoordinateReferenceSystem() );

     int x = (point.getOrdinate(X) - dx) * scale;
     int y = height - ((point.getOrdinate(Y) - dy)*scale);
     
     g.drawPoint(x, y);
  }

Where the following has been defined::
  
  private int indexOfX( CoordinateReferenceSystem crs ){
    Set<AxisDirection> up = new HashSet<AxisDirection>();
    up.add( AxisDirection.DISPLAY_LEFT );
    up.add( AxisDirection.EAST );
    up.add( AxisDirection.GEOCENTRIC_X );
    up.add( AxisDirection.COLUMN_POSITIVE );
    return indexOf( cs, up );
  }
  private int indexOfX( CoordinateReferenceSystem crs ){
    Set<AxisDirection> up = new HashSet<AxisDirection>();
    up.add( AxisDirection.DISPLAY_UP );
    up.add( AxisDirection.NORTH );
    up.add( AxisDirection.GEOCENTRIC_Y );
    up.add( AxisDirection.ROW_POSITIVE );
    return indexOf( cs, up );
  }
  private int indexOf( CoordinateReferenceSystem crs, Set<AxisDirection> direction ){
    CoordinateSystem cs = coordinateReferenceSystem.getCoordinateSystem();
    for( int index=0; index<cs.getDimension(); index++){
       CoordinateSystemAxis axis = cs.getAxis(i);
       if( direction.contains( axis.getDirection() ) return index;
    }   
    return -1;
  }

This code will fail when presented with:

* data in which the direction of the axis is not what was expected
* data that was collected in polar coordinates

Please note that you will still miss out on a lot of data, we have only looked for AxisDirection that match our
assumptions (ie that the data is across an increasing - such as EAST). We are missing out on other data that is
obviously across but is decreasing - such as WEST.
