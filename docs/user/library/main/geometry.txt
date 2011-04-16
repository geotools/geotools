WKT Parser
----------

The **gt-main** module supports a "well known text" geometry parser that is able to produce ISO 19107 Geometry instances. This page documents the use of the parser, and provides a little bit of background on the state of ISO 19107 support in GeoTools.

ISO 19107 Geometry
^^^^^^^^^^^^^^^^^^

The gt-opengis project provides interfaces for ISO Geometry interfaces, we make use of them day in and
day out for transforming DirectPositions as described in the referencing module.

ISO 19107 also defines TransfineSet of which GeoTools has two implementations:

* unsupported/jts-wrapper
* unsupported/geometry

As indicated by their unsupported status neither has past our QA checks at this time.

* What about JTS Topology Suite?
  
  As mentioned in the introduction GeoTools makes use JTS Topology Suite (a representation of
  the Simple Features for SQL specification) rather than ISO Geometry.
  
  Although the entire library is not using ISO Geometry yet; this does not prevent you from going there in your own application.
  Just be sure to include one of the module mentioned above before you start.

* What is TransfiniteSet
  
  A TransfiniteSet is the same thing as a JTS Geometry near as I can tell.
  
  From email - thanks Sunburned Surveyor and Bryce:
    
    I'm a little confused about the purpose of TransfiniteSet. Either this class is a little
    too abstract for my purposes or I need more explanation in the Javadoc comments. :]
    
    I don't think so. TransfiniteSet is JTS. It just uses some different terminology (from set
    theory). A circle is what? The set of all points exactly radius "r" from a center point.
    If you ask circle.contains(pointA), you'll get a "true" if and only iff "pointA" is
    exactly circle.radius away from circle.center.
    
    A curve is the set of all points which lie along the curve. "Is pointA on the curve?" is
    exactly the same as asking curve.contains(pointA)? Same thing different words.
    
    19107 was written by math geeks.

ISO Geometry from WKT
^^^^^^^^^^^^^^^^^^^^^

1. Start with a Factory
   
   The first step to creating a Geometry is to acquire a GeometryFactory::
     
     GeometryFactory
     CoordianteReferenceSystem crs = CRS.decode("EPSG:4326");
     Precision precision = new PrecisionModel();
     
     GeometryFactory geometryFactory = new GeometryFactoryImpl( crs );
   
   As you can see in the above example you need to already know your CoordinateReferenceSystem
   and Precision before creating a GeometryFactory. You can create and use several GeometryFactory
   instances at the same time if you need to work in several projections.
   
   There is one problem here - the GeometryFactory is only good for making Geometry. And to make a Geometry you need Coordinates, and primitives and ...

2. Start with Several Factories
   
   So I lied to you. You actually need a few more bits of the puzzle before we can accomplish anything.
   
   GeometryFactory and friends::
     
     PositionFactory positionFactory = new PositionFactoryImpl(...);
     CoordinateFactory coordinateFactory = new CoordinateFactoryImpl(...);
     PrimitiveFactory primitiveFactory = new PrimitiveFactoryImpl(...);
     ComplexFactory complexFactory = new ComplexFactoryImpl(...);
     AggregateFactory aggregtateFactory = new AggregateFactoryImpl(...);
     
     WKTParser parser = new WKTParser( geometryFactory, primitiveFactory, positionFactory, aggregateFactory);

     Point point = (Point) parser.parse( "POINT (80 340)" );

2. But wait!
   
   * Where do all these Impls come from?
   * What do they take as parameters?
   * eek!
   
   The answer is that the factory situation is starting to get so complex that we need to use a good tool to sort it out.

3. Use a Good Tool to Sort out the Factories
   
   Using a container such as a PicoContainer, we can teach it about all the factories
   and dependencies and it will figure out what to use when you create a factory.::
     
     PicoContainer and dependencies...
     // create pico container
     DefaultPicoContainer container = new DefaultPicoContainer();
     
     // Teach Container about Factory Implementations we want to use
     container.registerComponentImplementation(PositionFactoryImpl.class);
     container.registerComponentImplementation(FeatGeomFactoryImpl.class);
     container.registerComponentImplementation(AggregateFactoryImpl.class);
     container.registerComponentImplementation(ComplexFactoryImpl.class);
     container.registerComponentImplementation(GeometryFactoryImpl.class);
     container.registerComponentImplementation(PrimitiveFactoryImpl.class);
     
     // Teach Container about other dependencies needed
     CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
     container.registerComponentInstance( crs );
     Precision pr = new PrecisionModel();
     container.registerComponentInstance( pr );
     
     // Now we can ask for factories and the container will figure out what dependencies that factory needs and do the work for you
     PositionFactoryImpl pf = (PositionFactoryImpl ) container.getComponentInstanceOfType( PositionFactory.class );
     
     // create something with the factory
     DirectPosition dp = pf.createDirectPosition(new double[]{280,560});

4. Well Known Text Geometry Parser
   
   The SFSQL specification defines a Well Known Text (WKT) format for representing the kind
   of simple (Point, Line, Polygon) geometry constructs covered by the simple feature for SQL specification.
   
   We have a parser that will use a GeometryFactory to produce the ISO Geometry constructs for you. It will also parse some extra geometry types.
   
   ================== =====================================================
   WKT                ISO Geometry
   ================== =====================================================
   POINT              org.opengis.geometry.primitive.Point
   LINESTRING         org.opengis.geometry.primitive.Curve
   LINEARRING         org.opengis.geometry.primitive.Curve
   POLYGON            org.opengis.geometry.primitive.Surface
   MULTIPOINT         org.opengis.geometry.coordinate.aggregate.MultiPoint
   MULTILINESTRING    org.opengis.geometry.aggregate.MultiPrimitive
   MULTIPOLYGON       org.opengis.geometry.aggregate.MultiPrimitive
   GEOMETRYCOLLECTION org.opengis.geometry.aggregate.MultiPrimitive 
   ================== =====================================================
