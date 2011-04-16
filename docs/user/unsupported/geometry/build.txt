Build Geometry
--------------

Factories are classes used to create other objects. This module provides geometry factories allowing you to make a range of spatial objects.

Factories
^^^^^^^^^

The gt-opengis module several factories for working with ISO 19107 constructs:

* PositionFactory (implemented by PositionFactoryImpl)
  
  Requires: crs, precision
  
  Creates PointArray and DirectPosition

* GeometryFactory (implemented by GeometryFactoryImpl)
  crs, PositionFactory

  Create arc, B-spline, envelope, geodesic, line segment, line string, polygon, tin, triangle

* PrimitiveFactory (implemented by PrimitiveFactoryImpl)

  Requires: crs, PositionFactory

  Creates curve, point, surface

* ComplexFactory (implemented by ComplexFactoryImpl)
  
  Requires: crs    
  
  Creates composite curve, composite point, composite surface

* AggregateFactory (implemented by AggregateFactoryImpl)
  
  Requires: crs
  
  Creates multi point, multi curve, multi primitive, multi surface

You can create the factories by hand; a technique that is not recommended, but does illustrate how the parts fit together.
  
  PositionFactory posF = new PositionFactoryImpl(DefaultGeographicCRS.WGS84, new PrecisionModel());
  PrimitiveFactory primF = new PrimitiveFactoryImpl(DefaultGeographicCRS.WGS84, posF);

GeometryBuilder
'''''''''''''''

The GeometryBuilder class is the preferred way to manage, create and use factories.

This allows you to use only the factory gt-opengis interfaces and not worry about the implementations. The GeometryBuilder is found in the referencing package at org.geotools.geometry.GeometryBuilder. The following example shows how you can use the builder to create and manage factories::
  
  GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
  PositionFactory posF = builder.getPositionFactory();
  PrimitiveFactory primFF = builder.getPrimitiveFactory();
  GeometryFactory geomF = builder.getGeometryFactory();

When you ask for a factory, if the builder has a matching cached factory, it will return that. Otherwise the builder will create and return a new factory, caching it for later use. You can update the builder's CRS, like in the following example, and get a new factory using the new CRS.::
  
  builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84_3D);
  PositionFactory posF3D = builder.getPositionFactory();

Using a Container
'''''''''''''''''

Another way to manage geometry factories is through the use of a container. This requires you to register the specific factory implementations you want to use.

Below we create a PicoContainer with all the required factories registered; we will then use the container to get the factories we will need and the container will figure out all the dependencies for us.

The call to create the PicoContainer will have to call into the org.picocontainer PicoContainer package.::
  
  DefaultPicoContainer cont = new DefaultPicoContainer(); 
  
  // Teach Container about Factory Implementations we want to use
  cont.registerComponentImplementation(PositionFactoryImpl.class);
  cont.registerComponentImplementation(AggregateFactoryImpl.class);
  cont.registerComponentImplementation(ComplexFactoryImpl.class);
  cont.registerComponentImplementation(GeometryFactoryImpl.class);
  cont.registerComponentImplementation(PrimitiveFactoryImpl.class);
  
  // Teach Container about other dependencies needed
  cont.registerComponentInstance( DefaultGeographicCRS.WGS84);
  cont.registerComponentInstance( new PrecisionModel());

We made a PicoContainer, registered the factories we were going to use and then added two qualifiers. We use the static CoordinateReferencingSystem (CRS) object WGS84 to tell the container we want to use the CRS of the GPS system. We then tell the container we want to use the default precision model.

The general idea is to create a different container for each CRS and/or precision model you want to use. We could therefore put the above code in a method and pass as parameters the CRS and PrecisionModel.

Using the container we just made we can get some factories but we get the interface back rather than the implementation.::
  
  PositionFactory posF = cont.getComponentInstanceOfType( PositionFactory.class );
  PrimitiveFactory primF = cont.getComponentInstanceOfType( PrimitiveFactory.class );
  GeometryFactory geomF = cont.getComponentInstanceOfType( GeometryFactory.class );

Here we simply asked the container to give us the factories of the different types we would need.

Creating a Point
^^^^^^^^^^^^^^^^

Points are the building blocks for most other geometries. The following sections explain how to manipulate Points:

There are a few ways you can go about creating a point. GeometryBuilder has many utility methods for creating geometries, so you don't have to worry about factories. But you can also use factories directly, or you can also use a WKT parser for creating points.

* There are several createPoint methods provided as part of GeometryBuilder.
  Here is an example using one of them::
    
    GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 );        
    Point point = builder.createPoint( 48.44, -123.37 );

* Using Factories
  
  In some environments you are restricted to just using formal gt-opengis interfaces, here is an example of using the PositionFactory and PrimitiveFactory as is::
    
    Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
    PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory( hints );
    PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory( hints );
    
    DirectPosition here = positionFactory.createDirectPosition( new double[]{48.44, -123.37} );
    
    Point point1 = primitiveFactory.createPoint( here );

* PositionFactory has a helper method allowing you to save one step::
    
    Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
    PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory( hints );
    
    Point point2 = primitiveFactory.createPoint(  new double[]{48.44, -123.37} );
    
    System.out.println( point2 );

* Using WKT
  
  You can use the WKTParser to create a point from a well known text::
    
    WKTParser parser = new WKTParser( DefaultGeographicCRS.WGS84 );
    Point point = (Point) parser.parse("POINT( 48.44 -123.37)");
  
  You can also create the WKTParser to use a specific set of factories::
    
    Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
    
    PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory(hints);
    GeometryFactory geometryFactory = GeometryFactoryFinder.getGeometryFactory(hints);
    PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory(hints);
    AggregateFactory aggregateFactory = GeometryFactoryFinder.getAggregateFactory(hints);
    
    WKTParser parser = new WKTParser( geometryFactory, primitiveFactory, positionFactory, aggregateFactory );
    
    Point point = (Point) parser.parse("POINT( 48.44 -123.37)");

Point
'''''

Sometimes it is useful to take apart a geometry and get the pieces that are used to build it. The following shows how you can get the ordinates of a point::
    
    double[] ords = point.getCentroid().getCoordinates();
    
Creating a Curve
^^^^^^^^^^^^^^^^

Curves, or line objects, are usually created from a series of CurveSegments. Curves can be created directly from the GeometryBuilder, or if you only want to use gt-opengis interfaces you can use factories:

The following sections explain how to manipulate Curves.

* The following example shows how to create a CurveSegment and how to use it
  to build a Curve with the GeometryBuilder.::
    
    // create directpositions
    DirectPosition start = builder.createDirectPosition(new double[]{ 48.44, -123.37 });
    DirectPosition middle = builder.createDirectPosition(new double[]{ 47, -122 });
    DirectPosition end = builder.createDirectPosition(new double[]{ 46.5, -121.5 });        
    
    // add directpositions to a list
    ArrayList<Position> positions = new ArrayList<Position>();
    positions .add(start);
    positions.add(middle);
    positions.add(end);    
    
    // create linestring from directpositions
    LineString line = builder.createLineString(positions);
    
    // create curvesegments from line
    ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
    segs.add(line);
    
    // create curve
    Curve curve = builder.createCurve(segs);

* Using Factories
  
  Building a curve from factories is very similar to the process of using the
  GeometryBuilder, but it lets you only use gt-opengis interfaces:
    
    
    // create directpositions
    DirectPosition start = posF.createDirectPosition(new double[]{ 48.44, -123.37 });
    DirectPosition middle = posF.createDirectPosition(new double[]{ 47, -122 });
    DirectPosition end = posF.createDirectPosition(new double[]{ 46.5, -121.5 });
    
    // add directpositions to a list
    ArrayList<Position> positions = new ArrayList<Position>();
    positions .add(start);
    positions.add(middle);
    positions.add(end);
    
    // create linestring from directpositions
    LineString line = geomF.createLineString(positions);
    
    // create curvesegments from line
    ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
    segs.add(line);
    
    // create curve
    Curve curve = primF.createCurve(segs);

Curve
'''''

Taking apart a Curve to get a list of points may not always return what you expect. For instance in a spline curve, the curve segment is given as a weighted vector sum of the control points. These control points are used to control its shape, and are not always on the curve itself. It can still be useful to obtain these control points, and the following shows how you can do that::
    
    List<CurveSegment> segs = curve.getSegments();
    Iterator<CurveSegment> iter = segs.iterator();
    PointArray samplePoints = null;
    while (iter.hasNext()) {
        if (samplePoints == null) {
            samplePoints = iter.next().getSamplePoints();
        }
        else {
            samplePoints.addAll(iter.next().getSamplePoints());
        }
    }

GeoTools Users Guide : 04 Working with Surface
This page last changed on Sep 05, 2007 by gdavis.
The following sections explain how to manipulate Surfaces:

Creating a Surface
^^^^^^^^^^^^^^^^^^

As with the other geometries, Surfaces are built up from a series of other geometry pieces. Surfaces can be created directly from the GeometryBuilder, or if you only want to use GeoAPI interfaces you can also use factories:

* Surfaces can be built from a list of SurfacePatches or from a
  SurfaceBoundary.
  
  The following example shows how to create a Surface from a SurfaceBoundary
  using the GeometryBuilder.::
    
    GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 );
    
    
    // create a list of connected positions
    List<Position> dps = new ArrayList<Position>();
    dps.add(builder.createDirectPosition( new double[] {20, 10} ));
    dps.add(builder.createDirectPosition( new double[] {40, 10} ));
    dps.add(builder.createDirectPosition( new double[] {50, 40} ));
    dps.add(builder.createDirectPosition( new double[] {30, 50} ));
    dps.add(builder.createDirectPosition( new double[] {10, 30} ));
    dps.add(builder.createDirectPosition( new double[] {20, 10} ));
    
    // create linestring from directpositions
    LineString line = builder.createLineString(dps);
    
    // create curvesegments from line
    ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
    segs.add(line);
    
    // Create list of OrientableCurves that make up the surface
    OrientableCurve curve = builder.createCurve(segs);
    List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
    orientableCurves.add(curve);
    
    // create the interior ring and a list of empty interior rings (holes)
    Ring extRing = builder.createRing(orientableCurves);
    List<Ring> intRings = new ArrayList<Ring>();
    
    // create the surfaceboundary from the rings
    SurfaceBoundary sb = builder.createSurfaceBoundary(extRing, intRings);
    
    
    // create the surface
    Surface surface = builder.createSurface(sb);  

* Using Factories
  
  Building a surface from factories is very similar to the process of using
  the GeometryBuilder, but it lets you only use gt-opengis interfaces:
    
    // create a list of connected positions
    List<Position> dps = new ArrayList<Position>();
    dps.add(posF.createDirectPosition( new double[] {20, 10} ));
    dps.add(posF.createDirectPosition( new double[] {40, 10} ));
    dps.add(posF.createDirectPosition( new double[] {50, 40} ));
    dps.add(posF.createDirectPosition( new double[] {30, 50} ));
    dps.add(posF.createDirectPosition( new double[] {10, 30} ));
    dps.add(posF.createDirectPosition( new double[] {20, 10} ));
    
    // create linestring from directpositions
    LineString line = geomF.createLineString(dps);
    
    // create curvesegments from line
    ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
    segs.add(line);
    
    // Create list of OrientableCurves that make up the surface
    OrientableCurve curve = primF.createCurve(segs);
    List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
    orientableCurves.add(curve);
    
    // create the interior ring and a list of empty interior rings (holes)
    Ring extRing = primF.createRing(orientableCurves);
    List<Ring> intRings = new ArrayList<Ring>();
    
    // create the surfaceboundary from the rings
    SurfaceBoundary sb = primF.createSurfaceBoundary(extRing, intRings);
            
    // create the surface
    Surface surface = primF.createSurface(sb);
    
Surface
'''''''

The list of points that build up a Surface are not a good representation
of that geometry. Surfaces can have holes in them, and a simple list of
points will not tell you if they belong to a hole or to the exterior of
the shape.

However, you can obtain the Rings for the exterior and interior (holes)
of the Surface. If desired, you can also get the points that make those
Rings::

    SurfaceBoundary sb = (SurfaceBoundary) surface2.getBoundary();
    Ring exterior = sb.getExterior();
    List<Ring> interiors = sb.getInteriors();
    Collection<? extends Primitive> extCurve = exterior.getElements();
    Iterator<? extends Primitive> iter = extCurve.iterator();
    PointArray samplePoints = null;
    while (iter.hasNext()) {
        Curve curve = (Curve) iter.next();
        List<CurveSegment> segs = curve.getSegments();
        Iterator<CurveSegment> curveIter = segs.iterator();
        while (curveIter.hasNext()) {
            if (samplePoints == null) {
                samplePoints = curveIter.next().getSamplePoints();
            }
            else {
                samplePoints.addAll(curveIter.next().getSamplePoints());
            }
        }
    }

Rendering a Surface
'''''''''''''''''''
The following are two quick examples of how you can render a Surface (Polygon):

* Here is a quick example of rendering a Polygon using Java for/each syntax::

    final int X = 0; // easting axis for surface.getCoordinateReferenceSystem()
    final int Y = 1; // westing axis for surface.getCoordinateReferenceSystem()
    for( SurfacePatch patch : surface.getPatches()){
        SurfaceBoundary boundary = patch.getBoundary();
        Ring ring = boundary.getExterior();
        for( Primitive primitive : ring.getElements() ){
            if( primitive instanceof Curve ){
                Curve curve = (Curve) primitive;
                for( CurveSegment segment : curve.getSegments() ){
                    if( segment instanceof LineString){
                        LineString lines = (LineString) segment;
                        for( LineSegment line : lines.asLineSegments() ){
                            DirectPosition point1 = line.getStartPoint();
                            DirectPosition point2 = line.getEndPoint();
                            g.drawLine( point1.getOrdinate(X), point1.getOrdinate(Y),
                                        point2.getOrdinate(X), point2.getOrdinate(Y) );
                        }
                    }
                    else if (segment instanceof LineSegment){
                        LineSegment line = (LineSegment) segment;
                        DirectPosition point1 = line.getStartPoint();
                        DirectPosition point2 = line.getEndPoint();
                        g.drawLine( point1.getOrdinate(X), point1.getOrdinate(Y),
                                    point2.getOrdinate(X), point2.getOrdinate(Y) );
                    }
                }
            }
        }               
    }
  
    Please note:
    
    * Review your CoordianteReferenceSystem to figure out which axis is to use for
      X and Y
    * Surface is a deep data structure, better suited to recursion or a visitor
    
* Using Recursive Code
  
  You can produce less code duplication using recursion to navigate through your Surface::
    
    protected void paint( Graphics2D g, Surface surface  ) {
        for( SurfacePatch patch : surface.getPatches()){
            SurfaceBoundary boundary = patch.getBoundary();
            Ring ring = boundary.getExterior();
            paint( g, ring );
        }            
    }
    protected void paint( Graphics2D g, Ring ring ) {
        for( Primitive primitive : ring.getElements() ){
            if( primitive instanceof Curve ){
                Curve curve = (Curve) primitive;
                paint( g, curve );                
            }
        }
    }
    protected void paint(Graphics2D g, Curve curve ) {
        for( CurveSegment segment : curve.getSegments() ){
            if( segment instanceof LineString){
                LineString lines = (LineString) segment;
                for( LineSegment line : lines.asLineSegments() ){
                    paint( g, line );
                }
            }
            else if (segment instanceof LineSegment){
                LineSegment line = (LineSegment) segment;
                paint( g, line );
            }
        }
    }
    protected void paint(Graphics2D g, LineSegment line) {
        DirectPosition point1 = line.getStartPoint();
        DirectPosition point2 = line.getEndPoint();
        g.drawLine( point1.getOrdinate(X), point1.getOrdinate(Y),
                    point2.getOrdinate(X), point2.getOrdinate(Y) ); 
    }

Creating Envelope
^^^^^^^^^^^^^^^^^

The following sections explain how to manipulate Envelopes:

Envelopes are essentially basic rectangles. Envelopes can be created
directly from the GeometryBuilder, or if you only want to use gt-opengis
interfaces you can use factories::

* The following example shows how to create an Envelope with the GeometryBuilder.::

    GeometryBuilder builder = new GeometryBuilder( DefaultGeographicCRS.WGS84 ); 
    
    DirectPosition upper = builder.createDirectPosition(new double[]{-180,-90});
    DirectPosition lower = builder.createDirectPosition(new double[]{180,90});        
    Envelope envelope = builder.createEnvelope( upper, lower );   	

* Using Factories
  
  Building an envelope from factories is very similar to the process of using
  the GeometryBuilder, but it lets you only use gt-opengis interfaces:::

    Hints hints = new Hints( Hints.CRS, DefaultGeographicCRS.WGS84 );
    
    PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory( hints );
    GeometryFactory geometryFactory = GeometryFactoryFinder.getGeometryFactory( hints );
    
    DirectPosition upper = positionFactory.createDirectPosition(new double[]{-180,-90});
    DirectPosition lower = positionFactory.createDirectPosition(new double[]{180,90});        
    Envelope envelope = geometryFactory.createEnvelope( upper, lower );        

Envelope
''''''''

Please consider that the idea of Width and Height are context dependent, what
axis is "across" depends on the data you are working with.

What you can do is ask for the "length" along an axis.::

    int length0= envelope.getLength( 0 );
    int length1 = envelope.getLength( 1 );

