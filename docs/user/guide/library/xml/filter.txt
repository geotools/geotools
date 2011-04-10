Filter
------

No utility class, this time out we will be making direct use of the two available GTXML configurations:

* org.geotools.filter.v1_0.OGCConfiguration
* org.geotools.filter.v1_1.OGCConfiguration

The definition of *filter* is considered part of the ogc schema, hence the use of OGCConfiguration above. 

Filter1
^^^^^^^

The Filter 1.0 specification is supported by SAX, DOM and GTXML.

.. note:: Filter 1.0 specification requires the use of GML2 to represent Geometry elements.

Encode
''''''

To encode a filter::
  
  org.geotools.xml.Configuration = new org.geotools.filter.v1_0.OGCConfiguration();
  org.geotools.xml.Encoder encoder = new org.geotools.xml.Encoder( configuration );
  encoder.encode( filter, org.geotools.filter.v1_0.OGC.FILTER, outputStream );

This configurations bring in the correct GML2 version of GMLConfiguration to handle any geometry mentioned in your Filter.

Consider the following filter::
  
  FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
  PropertyName propertyName = ff.property( "testString" );
  Literal literal = ff.literal( 2 );
  PropertyIsEqualTo filter = ff.equals( propertyName, literal );

To encode the filter::
  
  //create the encoder with the filter 1.0 configuration
  org.geotools.xml.Configuration = new org.geotools.filter.v1_0.OGCConfiguration();
  org.geotools.xml.Encoder encoder = new org.geotools.xml.Encoder( configuration );
  
  //create an output stream
  OutputStream xml = ...
  
  //encode
  encoder.encode( filter, org.geotools.filter.v1_0.OGC.FILTER, xml );

Parser
''''''

To parse a filter::
  
  Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
  Parser parser = new Parser( configuration );

  Filter filter = (Filter) parser.parse( imputStream );

Consider the following filter document::

  <Filter xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns="http://www.opengis.net/ogc"
    xsi:schemaLocation="http://www.opengis.net/ogc filter.xsd">
    <PropertyIsEqualTo>
      <PropertyName>testString</PropertyName>
      <Literal>2</Literal>
    </PropertyIsEqualTo>
  </Filter>

To parse the document::
  
  //create the parser with the filter 1.0 configuration
  Configuration configuration = new org.geotools.filter.v1_0.OGCConfiguration();
  Parser parser = new Parser( configuration );
  
  //the xml instance document above
  InputStream xml = ...
  
  //parse
  Filter filter = (Filter) parser.parse( xml );

FilterFilter (SAX)
''''''''''''''''''

An alternative to using a GTXML configuration is to directly handle the SAX events yourself.

As mentioned n the previous page on :doc:`geometry page <geometry>` sax events involve defining a callback class that can be invoked as each Filter is recognised.

Here is a quick example:

.. literalinclude:: /../src/main/java/org/geotools/xml/FilterXMLExamples.java
   :language: java
   :start-after: // saxExample start
   :end-before: // saxExample end

FilterDOMParser DOM
''''''''''''''''''''

DOM based parsers are useful if your application already is working with DOM. An example would be parsing an Style object out of an Web Map Server GetMap request.

You can use the DOM parser to parse individual filter nodes::
  
  InputSource input = new InputSource( reader );
  
  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
  DocumentBuilder db = dbf.newDocumentBuilder();
  Document dom = db.parse( input );

  Filter filter = null;
  
  // first grab a filter node
  NodeList nodes = dom.getElementsByTagName("Filter");
  
  for (int j = 0; j < nodes.getLength(); j++) {
      Element filterNode = (Element) nodes.item(j);
      NodeList list = filterNode.getChildNodes();
      Node child = null;
      
      for (int i = 0; i < list.getLength(); i++) {
          child = list.item(i);
          
          if ((child == null) || (child.getNodeType() != Node.ELEMENT_NODE)) {
              continue;
          }
          
          filter = FilterDOMParser.parseFilter(child);
    }
  }
  System.out.println( "got:"+filter );

Filter 1.1
^^^^^^^^^^

The Filter 1.1 specification is provided by v1_1 OGCConfiguration.

.. note:: Filter 1.1 uses GML3 to represent Geometry as required by the specification.

Ecode
'''''

To encode a filter::
  
  org.geotools.xml.Configuration = new org.geotools.filter.v1_1.OGCConfiguration();
  org.geotools.xml.Encoder encoder = new org.geotools.xml.Encoder( configuration );
  encoder.encode( filter, org.geotools.filter.v1_0.OGC.FILTER, outputStream );

Parse
'''''

To parse a filter::
  
  Configuration configuration = new org.geotools.filter.v1_1.OGCConfiguration();
  Parser parser = new Parser( configuration );

  Filter filter = (Filter) parser.parse( imputStream );
