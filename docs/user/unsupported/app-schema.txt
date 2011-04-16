App-Schema
----------

The app-schema module allows you to configure geotools to support a predefined application schema (as provided by a standards body).

References:

* `Geoserver docs <http://docs.geoserver.org/trunk/en/user/data/app-schema/index.html>`_
* `tut_RoadSegment <https://svn.auscope.org/subversion/AuScope/geoserver/config/geoserver-app-schema-tutorial-config/trunk/workspaces/example/tut_RoadSegment/>`_

Configuration
^^^^^^^^^^^^^

Much of the provided documentation is expressed in terms of use from GeoServer; however the general approach of starting with a target XSD file and producing a mapping.xml document remains the same.

1. The following code example can be used to load a mapping.xml file::
    
    // pending
    
2. One thing that is interesting is the requirement to have your features
   sources available in a Registry (for the mapping file to look them up and
   find them).::
      
     // pending
     
3. In this case the source data is provided by a properties file::
     
     _=the_geom:LineString,FID:String,NAME:String,fromNode:Point,toNode:Point
    RoadSegments.1=LINESTRING (-0.0042 -0.0006, -0.0032 -0.0003, -0.0026 -0.0001, -0.0014 0.0002, 0.0002 0.0007)|102|Route 5|POINT(-0.0042 -0.0006)|POINT(0.0002 0.0007)
    RoadSegments.2=LINESTRING (0.0002 0.0007, 0.0014 0.001, 0.0028 0.0014)|103|Route 5|POINT(0.0002 0.0007)|POINT(0.0028 0.0014)
    RoadSegments.3=LINESTRING (0.0028 0.0014, 0.003 0.0024)|104|Route 5|POINT(0.0028 0.0014)|POINT(0.003 0.0024)
    RoadSegments.4=LINESTRING (0.0002 0.0007, 0.0014 0.001, 0.0028 0.0014, 0.0042 0.0018)|105|Main Street|POINT(0.0002 0.0007)|POINT(0.0042 0.0018)
    RoadSegments.5=LINESTRING (-0.0014 -0.0024, -0.0014 0.0002)|106|Dirt Road by Green Forest|POINT(-0.0014 -0.0024)|POINT(-0.0014 0.0002)

Target XSD
''''''''''

As indicated you need to have a target XML schema in mind before starting your mapping.::

    <?xml version="1.0" encoding="UTF-8"?>
    <xs:schema targetNamespace="http://example.org"
               xmlns:tut="http://example.org"
               xmlns:gml="http://www.opengis.net/gml"
               xmlns:xs="http://www.w3.org/2001/XMLSchema"
               elementFormDefault="qualified"
               attributeFormDefault="unqualified" version="1.0">
      <xs:import namespace="http://www.opengis.net/gml"
        schemaLocation="http://schemas.opengis.net/gml/3.1.1/base/feature.xsd" />
      <xs:element name="RoadSegment" type="tut:RoadSegmentType" />
      <xs:complexType name="RoadSegmentType">
        <xs:complexContent>
          <xs:extension base="gml:AbstractFeatureType">
            <xs:sequence>
              <xs:element name="name" type="xs:string" />
              <xs:element name="fromToNodes" nillable="false">
                <xs:complexType>
                  <xs:sequence>
                    <xs:element name="fromNode" type="gml:PointPropertyType" nillable="true" />
                    <xs:element name="toNode" type="gml:PointPropertyType" nillable="true" />
                  </xs:sequence>
                  <xs:attribute ref="gml:id" use="required" />
                </xs:complexType>
              </xs:element>
              <xs:element name="the_geom" type="gml:LineStringPropertyType" />
             <xs:element ref="tut:broadTypeEl"/>          
            </xs:sequence>
          </xs:extension>
        </xs:complexContent>
      </xs:complexType>
    
    <xs:element name="broadTypeEl" type="tut:broadType"/>
    <xs:complexType name="broadType">
       <xs:sequence>
              <xs:element name="name" type="xs:string" maxOccurs="unbounded"/> 
       </xs:sequence> 	
    </xs:complexType>
    </xs:schema>

Mapping File
''''''''''''

The mapping file goes through the steps of mapping types and attributes in the
target XSD through to features sources provided at the geotools level.

Here is an example mapping file::
    
    <?xml version="1.0" encoding="UTF-8"?>
    <as:AppSchemaDataAccess
          xmlns:as="http://www.geotools.org/app-schema"
          xmlns:ogc="http://www.opengis.net/ogc"
          xmlns:xs="http://www.w3.org/2001/XMLSchema"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.geotools.org/app-schema AppSchemaDataAccess.xsd
                          http://www.opengis.net/ogc http://schemas.opengis.net/filter/1.1.0/expr.xsd">
      <namespaces>
        <Namespace><prefix>tut</prefix><uri>http://example.org</uri></Namespace>
        <Namespace><prefix>gml</prefix><uri>http://www.opengis.net/gml</uri></Namespace>
      </namespaces>
      <sourceDataStores>
        <DataStore>
          <id>directory1</id>
          <parameters>
            <Parameter>
              <name>directory</name>
              <!-- path can be relative to this file if starts with file: -->
              <value>file:./</value>
            </Parameter>
          </parameters>
        </DataStore>
      </sourceDataStores>
      <targetTypes>
        <FeatureType>
          <schemaUri>RoadSegment.xsd</schemaUri>
        </FeatureType>
      </targetTypes>
      <typeMappings>
        <FeatureTypeMapping>
          <sourceDataStore>directory1</sourceDataStore>
          <sourceType>tut_RoadSegment</sourceType>
          <targetElement>tut:RoadSegment</targetElement>
          <groupBy/>
          <attributeMappings>
            <AttributeMapping>
              <targetAttribute>RoadSegment</targetAttribute>
              <idExpression>
                <OCQL>getId()</OCQL>
              </idExpression>
            </AttributeMapping>
            <AttributeMapping>
              <targetAttribute>RoadSegment/gml:name</targetAttribute>
              <ClientProperty>
                <name>codeSpace</name>
                <value>'urn:x-test:classifierScheme:TestAuthority:SDI:transport:v1'</value>
              </ClientProperty>
              <sourceExpression>
                <OCQL>FID</OCQL>
              </sourceExpression>
            </AttributeMapping>
            <AttributeMapping>
              <targetAttribute>RoadSegment/tut:name</targetAttribute>
              <sourceExpression>
                <OCQL>NAME</OCQL>
              </sourceExpression>
            </AttributeMapping>
            <AttributeMapping>
              <targetAttribute>RoadSegment/tut:fromToNodes</targetAttribute>
              <idExpression>
                <OCQL>FID</OCQL>
              </idExpression>
            </AttributeMapping>
            <AttributeMapping>
              <targetAttribute>RoadSegment/tut:fromToNodes/tut:fromNode</targetAttribute>
              <sourceExpression>
                <OCQL>fromNode</OCQL>
              </sourceExpression>
            </AttributeMapping>
            <AttributeMapping>
              <targetAttribute>RoadSegment/tut:fromToNodes/tut:toNode</targetAttribute>
              <sourceExpression>
                <OCQL>toNode</OCQL>
              </sourceExpression>
            </AttributeMapping>      
            <AttributeMapping>
              <targetAttribute>RoadSegment/tut:the_geom</targetAttribute>
              <sourceExpression>
                <OCQL>the_geom</OCQL>
              </sourceExpression>
            </AttributeMapping>
          </attributeMappings>
        </FeatureTypeMapping>
      </typeMappings>
    </as:AppSchemaDataAccess>

App-Schema-Resolver
^^^^^^^^^^^^^^^^^^^

The app-schema resolver allows you to bundle schema files into jars (rather then
have your application resort the the internet every time).

The codebase also appears to support caching of schema information.

References:

*  `App-Schema Geoserver Docs <http://docs.geoserver.org/trunk/en/user/data/app-schema/app-schema-resolution.html#classpath>`_

Code Example
''''''''''''

From test case::

    Configuration configuration =
      new AppSchemaConfiguration("urn:cgi:xmlns:CGI:GeoSciML:2.0",
                                 "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd",
                                 new AppSchemaResolver());
    
    SchemaIndex schemaIndex = Schemas.findSchemas(configuration);
    
    Assert.assertEquals(3, schemaIndex.getSchemas().length);
    String schemaLocation = schemaIndex.getSchemas()[0].getSchemaLocation();
    Assert.assertTrue(schemaLocation.startsWith("jar:file:"));
    Assert.assertTrue(schemaLocation.endsWith("geosciml.xsd"));

