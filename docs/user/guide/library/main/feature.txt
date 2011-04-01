Feature
-------

This page consists of a series of code examples showing how to do common tasks with the GeoTools feature model.

Reference:

* :doc:`../opengis/model` gt-opengis data model
* :doc:`../opengis/type` gt-opengis feature type interfaces
* :doc:`../opengis/feature` gt-opengis feature interfaces
* :doc:`../opengis/simple` gt-opengis simple feature interfaces

Build a Feature Type
^^^^^^^^^^^^^^^^^^^^

Since FeatureType is immutable you will often see them used as constants as shown in the following examples:

Simple case::
  
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

Alternative chaining::
  
  SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
  SimpleFeatureType type;
  
  // you can chain builder methods
  final SimpleFeatureType FLAG =
         b.name("Flag").add("name", String.class ).
         add( "classification", Integer.class ).add( "height", Double.class ).
         crs( DefaultGeographicCRS.WSG84 ).add( "location", Point.class ).buildFeatureType();

Include namespace information::
  
  // you can set a namespace
  b.setNamespaceURI( "http://geotools.org/example" );

Geometry Type
'''''''''''''

Multiple geometries (with implicit default geometry)::
  
  b.setCRS( DefaultGeographicCRS.WSG84 );
  
  //add some geometry properties (first added is the default)
  b.add( "region", Polygon.class );
  b.add( "hub", Point.class );
  b.add( "network", MultiLineString.class );

Multiple geometries with explicit default geoemtry::
  
  b.setCRS( DefaultGeographicCRS.WSG84 );
  
  b.add( "hub", Point.class );
  b.add( "region", Polygon.class );
  b.add( "network", MultiLineString.class );
  
  //set the default geometry
  b.setDefaultGeometry( "region" ):

CRS for geometries::

  CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
  
  //set the coordinate reference system
  b.setCRS( crs );
  
  // when geometry properties are added they will use the crs set above
  b.add( "position", Point.class );
  b.add( "route", LineString.class );

Alternative: Unknown CRS::
  
  b.setCRS( null );
  b.add( "position", Point.class );
  b.add( "route", LineString.class );

Multiple CRS for Geometries::
  
  CoordinateReferenceSystem crs1 = CRS.decode("EPSG:3005");
  CoordinateReferenceSystem crs2 = DefaultGeographicCRS.WSG84;
  
  b.setCRS( crs1 );
  b.add( "local", Point.class );
  
  b.setCRS( crs2 );
  b.add( "world", Point.class );

Alternative: Chaining::

  CoordinateReferenceSystem crs1 = CRS.decode("EPSG:3005");
  CoordinateReferenceSystem crs2 = DefaultGeographicCRS.WSG84;
  
  b.crs( crs1 ).add( "local", Point.class );
  b.crs( crs2 ).add( "world", Point.class );

Alternative: Using an SRS::
  
  b.srs( "EPSG:3005" ).add( "local", Point.class );
  b.srs( "EPSG:4326" ).add( "world", Point.class );

Attribute Descriptor
''''''''''''''''''''

Simple Case::
  
  AttributeTypeBuilder build = new AttributeTypeBuilder();
  build.setNillable(true);
  build.setBinding(String.class);
  
  AttributeDescriptor descriptor = build.buildDescriptor( "name" );

Alternative: With Explicit Attribute Type::
  
  AttributeTypeBuilder build = new AttributeTypeBuilder();
  build.setNillable(true);
  build.setBinding(String.class);
  build.setName("Text");

  AttributeType textType = build.buildType();
  AttributeDescriptor descriptor = build.buildDescriptor( "name", textType );

Building a Geometry Descriptor::
  
  build.setNillable(true);
  build.setCRS(crs);
  build.setBinding(Polygon.class);
  
  GeometryType geometryType = build.buildGeometryType();
  GeometryDescriptor build.buildDescriptor( "the_geom",   geometryType ) );

Building a Geometry Descriptor with Limited Length::
  
  AttributeTypeBuilder build = new AttributeTypeBuilder();
  build.setNillable(true);
  build.setBinding(String.class);
  build.setLength(15);
  AttributeDescriptor descriptor = build.buildDescriptor( "username" );

Name
''''

Creating a specific name::
  
  Name roadName = new NameImpl("http://localhost/","Road");

Creating a global name::
  
  Name roadName = new NameImpl(null,"Road");

DataUtilities
'''''''''''''

DataUtilities has a method that you can use to quickly create a FeatureType for test cases::
  
  final SimpleFeatureType FLAG = DataUtilities.createType("Flag","Location:Point,Name:String");

You can define the Coordinate Reference System using :
  
  final SimpleFeatureType FLAG = DataUtilities.createType("Flags","geom:MultiPoint:srid=4326,Name:String");

You can also ask for the String representation of a FeatureType:
  
  System.out.println( DataUtilities.spec( FLAG ) );

For more information see :doc:`data`.

FeatureFactory
''''''''''''''

You can also use FeatureFactory directly; this is advised when building nested features (as we only have a SimpleFeatureTypeBuilder at present).

Using a TypeFactory::

  TypeFactory typeFactory = CommonFactoryFinder.getTypeFactory( null );
  SimpleTypeFactory featureTypeFactory =   CommonFactoryFinder.getSimpleTypeFeatureFactory( null );
  
  URI namespace = new URI("http://localhost/Flag/");
  CoordianteReferenceSystem crs = CRS.decode("EPSG:4326");
  
  Name locationName = new NameImpl( namespace, "Location" );
  InternationalString locationDescription = new SimpleInternationalString("Location of the base of this Flag, in WSG84");
  GeometryAttributeType GEOM = typeFactory.createGeometryType( locationName, Point.class, crs, false, false, null, null, locationDescription );
  
  Name idName = new NameImpl( namespace, "Id" );
  AttributeType ID = typeFactory.createAttributeType( idName, Integer.class, false, false, null, null, null );
  
  Name locationName = new NameImpl( namespace, "Name" );
  AttributeType NAME = typeFactory.createAttributeType( nameName, String.class, false, false, null, null, null );
  
  Name name = new NameImpl( new URI("http://localhost/"), "Flag" );
  InternationalString description = new SimpleInternationalString("A Flag used to place a marker on the world");
  
  AttributeDescriptor defaultGeoemtry = typeFactory.createAttributeDescriptor(GEOM, geomName, 1, 1, true, null );
  
  List<AttributeDescriptor> types = new ArrayList<AttributeDescriptor>();
  types.add( defaultGeometry );
  types.add( typeFactory.createAttributeDescriptor(ID, idName, 1, 1, false, new Integer(0) ) );
  types.add( typeFactory.createAttributeDescriptor(NAME, nameName, 1, 1, true, null ) );
  
  final FeatureType FLAG = featureTypeFactory.createSimpleFeatureType( name, types, defaultGeometry, crs, Collections.EMPTY_SET, description );

As you can see we usually recommend SimpleFeatureTypeBuilder as it provides assistance with the above work for you.

Build a Feature
^^^^^^^^^^^^^^^

Simple Case::
  
  //the type, schema = ( name:String, classification:Integer, height:Double, location:Point)
  SimpleFeatureType type = ...;
  
  //create the builder
  SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
  
  //add the values
  builder.add( "Canada" );
  builder.add( 1 );
  builder.add( 20.5 );
  builder.add( new Point( -124, 52 ) );

  //build the feature with provided ID
  SimpleFeature feature = builder.buildFeature( "fid.1" );

Alternative array of values provided in order::
  
  Object[] values = new Object[]{
    "Canada", 1, 20.5, new Point( -124, 52  )
  };
  builder.addAll( values );

Alternative list of values provided in order::
  
  ArrayList<Object> values = new ArrayList<Object>( 4 );
  values.add("Canada");
  values.add( 1 );
  values.add( 20.5 );
  values.add( new Point( -124, 52  ) );
  builder.addAll( list );

Alternative setting by Name::
  
  builder.set( "name", "Canada" );
  builder.set( "classification", 1 );
  builder.set( "height", 20.5 );
  builder.set( "location", new Point( -124, 52  ) );

Alternative setting by index::
  
  builder.set( 0, "Canada" );
  builder.set( 1, 1 );
  builder.set( 2 20.5 );
  builder.set( 3, new Point( -124, 52  ) );

DataUtilities
'''''''''''''

DataUtilities has some utility methods that will create a "template" feature with sensible default values filled in based on the FeatureType.

For more information see :doc:`data`.

FeatureFactory
''''''''''''''

Once again we will ask you to use FilterFactory directly if you are building up a Feature by hand.

Accessing
^^^^^^^^^

Direct access to values::
  
  SimpleFeature feature = ...see above...;
  
  for (Object value : feature.getAttributes() ) {
    System.out.print( value ",");
  }
  // prints Canada,1,20.5,POINT( -124, 52 ),

Access values using index::
  
  for (int i = 0; i < feature.getAttributeCount(); i++ ) {
    Object value = feature.getAttribute( i );
    System.out.print( value ",");
  }
  // prints Canada,1,20.5,POINT( -124, 52 ),

Access values using Name::
  
  for (Property property : feature.getProperties()) {
    String name = property.getName();
    Object value = feature.getAttribute( property.getName() );
    System.out.print( name+"="+value+"," );
  }
  // prints name=Canada,classification=1,height=20.5,location=POINT( -124, 52 ),

Property
''''''''

Property access::
  
  Property property = feature.getProperty( "name" );
  String name = property.getName();
  Object value = property.getValue();

Property access using Index::
  
  Property property = feature.getProperty( 2 );
  String name = property.getName();
  Object value = property.getValue();

Geometry
''''''''

Geometry value access::
  
  Point point = (Point) feature.getDefaultGeometry();

Geometry value access as value::
  
  Point point = (Point) feature.getAttribute( "location" );

Geometry value access as property::
  
  GeometryAttribute geom = feature.getDefaultGeometryProperty();
  
  String name = geom.getName();
  Point point = (Point) geom.getValue();
  CoordinateReferenceSystem crs = geom.getCRS();
  BoundingBox bounds = geom.getBounds();

Geometry value access using name::
  
  GeometryAttribute geom = (GeometryAttribute) feature.getProperty("location");
  
  CoordinateReferenceSystem crs = geom.getCRS();
  BoundingBox bounds = geom.getBounds();
  Geometry point = (Geometry) theGeom.getValue();

Coordinate Reference System
'''''''''''''''''''''''''''

CoordinateReferenceSystem access::
  
  // Access the CRS of getDefaultGeometryProperty()
  CoordinateReferenceSystem crs = feature.getCRS();

CoordinateReferenceSystem of default geometry property::
  
  CoordinateReferenceSystem crs =
       feature.getDefaultGeometryProperty() == null ? null : feature.getDefaultGeometryProperty().getCRS();

CoordinateReferenceSystem of named Property::
  
  GeometryAttribute location = (GeometryAttribute) feature.getProperty( "location" );
  CoordinateReferenceSystem bounds = location.getCRS();

BoundingBox
'''''''''''

BoundingBox access::
  
  // Access the BoundingBox of getDefaultGeometryProperty()
  BoundingBox bounds = feature.getBounds();

BoundingBox of getDefaultGeometryProperty()::
  
  BoundingBox bounds =
       feature.getDefaultGeometryProperty() == null ? null : feature.getDefaultGeometryProperty().getBounds();

BoundingBox of named Property::
  
  GeometryAttribute location = (GeometryAttribute) feature.getProperty( "location" );
  BoundingBox bounds = location.getBounds();

Name
''''

Name access::
  
  // can access both parts of a name - similar to XML QName
  String localName = name.getLocalPart();
  String namespace = name.getNamespaceURI(); // Note a String

Check if name is global::
  
  name.isGlobal(); // true! name.getNamespaceURI() == null

Name comparison::
  
  Name name1 = new Name( "gopher://localhost/example", "name" );
  Name name2 = new Name( "gopher://localhost", "example/name" );
  
  name1.equals( name2 ); // true they both represent gopher://localhost/example/name

Validation
^^^^^^^^^^

Validating a feature::
  
  for (PropertyDescriptor property : feature.getType().getAttributes() )) {
     Object value = feature.getAttribute( property.getName() );
  
     Types.validate( property, value );
  }

Checking Super Types by Hand::
  
  SimpleFeature feature = ...;
  
  for (PropertyDescriptor property : feature.getType().getAttributes() )) {
    PropertyType propertyType = property.getType();
    Object value = feature.getAttribute( property.getName() );
  
    if( value == null ){
       //check nillability
       if ( property.isNillable() ){
          continue;
       }
       else {
          throw new Exception( "value can not be null" );
       }
    }
    //check the type
    if ( type.getBinding().isAssignableFrom( value.getClass() ) ) {
      throw new Exception( "value not same type as binding" );
    }
    // check restrictions for this propertyType and all super types
    for(PropertyType type=propertyType; type !=null; type=propertyType.getSuper() ){
       for( Filter valid : type.getRestrictions() ){
            if( !valid.evaulate( value ) ){
                throw new Exception(
                    "Not a valid "+type.getName()+" values must be:"+valid
                 );
            }
       }
    }
  }
