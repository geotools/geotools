XML FAQ
-------

Q: What parser does GeoTools use?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We have a range of tools to help you with parsing and encoding xml. As a
general purpose library GeoTools does not have the luxury of choosing a
parser to work with.

The following parsers are based on W3C APIs (SAX, DOM, and XML Transform).
You can choose the most appropriate solution for the system you want to
use GeoTools in.

+------------+-----+-----+--------+---------+------------------------------+
| Technology | SAX | DOM | Encode | Support | Notes                        |
+============+=====+=====+========+=========+==============================+
| SAX        | sax | dom |        | Filter  | Hard to trace through, parse |
|            |     |     |        | GML     | not easily extended          |
|            |     |     |        | SLD     |                              |
+------------+-----+-----+--------+---------+------------------------------+
| DOM        |     | dom |        | Filter  | Forgiving and easy to trace  |
|            |     |     |        | GLM     | through and debug, memory    |
|            |     |     |        | SLD     | limitation for GIS data      |
+------------+-----+-----+--------+---------+------------------------------+
| Transform  |     |     | xml    | Filter  | Easy to trace through and    |
|            |     |     |        | GML2    | debug, difficult to          |
|            |     |     |        | SLD     | configure for specific data  |
+------------+-----+-----+--------+---------+------------------------------+
| JABX       | sax | dom | xml    | n/a     | Fast but not suitable for    |
|            |     |     |        |         | dynamic data, precomplied    |
+------------+-----+-----+--------+---------+------------------------------+
| Pull       | sax | dom |        | n/a     | Should combine the ease of   |
|            |     |     |        |         | DOM with the streaming       |
|            |     |     |        |         | performance of XDO and GTXML |
+------------+-----+-----+--------+---------+------------------------------+
| XDO        | sax | dom | xml    | Filter  | Proof of concept of schema   |
|            |     |     |        | GML     | assisted parsing allowing    |
|            |     |     |        | SLD     | streaming into Java Objects. |
|            |     |     |        | WMS     | Code is fast and well tested |
|            |     |     |        | WFS1.0  | but is hard to trace through |
|            |     |     |        | XSD     |                              |
+------------+-----+-----+--------+---------+------------------------------+
| GTXML      | sax | dom | xml    | Filter  | Schema assisted parsing      |
|            |     |     |        | GML     | backed by Eclipse XSD        |
|            |     |     |        | SLD     | to represent schema.         |
|            |     |     |        | WMS     |                              |
|            |     |     |        | WFS1.0  | Easier to trace through but  |
|            |     |     |        | WFS1.1  | still not straight forward   |
|            |     |     |        | WPS     |                              |
|            |     |     |        | XSD     | Allows streaming for large   |
|            |     |     |        |         | GIS data volumnes.           |
+------------+-----+-----+--------+---------+------------------------------+

The XDO and GTXML solutions are configuration based. You set up a configuration
matching the data you wish to work with and the technology will make use of
any XML schema available to assist in parsing or encoding your content correctly.

The JAXB library from Java is a little different in that you start from an
XML schema and generate a parser or encoder class for use. This solution
performs well but cannot be used for dynamic content such as GML.

Q: What is SAX?
^^^^^^^^^^^^^^^

SAX is a W3C technology from the early days of XML. SAX Parsers work using
callbacks, they pass control between several hard coded implementations. For
basic use you create your own SAX Parser (say responding to a new Geometry being
parsed) and pass control off on of the geotools implementations and wait for it
to call you.

* Allows streaming for large content.
* Rather tricky to set up
* Reuse of a SAX parser is possible, but is very tricky to reuse

As a result SAX parsers are rather "brittle" and difficult to maintain
are currently hardcoded to pass control between themselves, making support for
new specifications tricky.

Example: while parsing a Filter control will be need to be handed over to
a Geometry parser if the expression contains a literal Geometry.

The later Schema Assisted parsers are an attempt to let mere mortals create a
tree of handlers on the fly (they use the schema document to do a bunch of the
grunt work) that is hard coded for the SAX Parsers.

Q: What is DOM?
^^^^^^^^^^^^^^^

The W3C Document Object Model (DOM) is most popularly used to represent the
the contents of a web page as a series of **Nodes**. These notes form a 
tree structure and can be used to represent the contents of an XML document
in memory.

As a result the GeoTools DOM parsers are functions that can wander through an
in memory DOM doing their best to extract content.

Delegation is hard coded in much the same way as with the SAX parsers.

* accessible easy to understand technology
* very nice for quick examples
* obvious how to use the parser
* solution does not scale to large content as steaming not available
* can only handle direct use of GML
* additional coding is always required to parse your own content

Q: What is XML Transform?
^^^^^^^^^^^^^^^^^^^^^^^^^

Traditional xml generation, traverse your data structure and call methods
to generate as you go.

* very fast, so fast we use it for GeoServer even though it is hard to maintain
* scalable (does not load features into memory)
* not open ended
* need to carefully provide hints before use
* may revisit data several times (for bounding box and then content)

Q: Schema Assisted -did you make that up?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Yes we did.

This idea of "Schema Assisted" parsers is a GeoTools specific piece of 
technology. Then general idea is to makes use of XML Schema information to
minimise the amount of code you need to write.

A parser is supplied a configuration of bindings; each binding maps an xml
elements or xml attributes to Java class.

While this sounds similar to other xml parsing technologies we do have a
couple of key differences:

* taking special care to pay attention to the schema at runtime (so we can parse
  new documents using the "best" binding available rather than fail)
* Ensure that data is not loaded into memory; allowing us to "stream" the xml
  document through an application.
* We are on our third generation schema assisted parser.

Q: XDO?
^^^^^^^

XML Data Objects (XDO) is the third generation of our schema
assisted parser idea (where the SAX bindings are referenced by the XMLSchema
rather then directly hard coded). This is fast scalable solution that supports
reading and writing.

* fast and proven solution for geospatial data
* ability to handle MASSIVE content like FeatureCollections
* how to create new bindings is not obvious

Q: GTXML?
^^^^^^^^^

GeoTools XML (GTXML) is the forth generation schema assisted parser, using the
XML Schema data structure (rather then hard coding) to figure out what binding to
call. The XSD is used to hold our representation of the schema at runtime.

* schema aware allowing use of new content without additional coding
* code generator for making custom bindings
* streaming content for MASSIVE content like feature collections
* support for content generation
* hard to debug and trace through the parsing or encoding process
* code generator available to jump start the development of bindings
* examples how how to use Eclipse Modelling Objects (EMF) based bindings to
  work directly from the schema

Q: Why doesn't GeoTools use JAXB?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

JAXB is a set of Java technologies (now included as part of Java 6) that are able
to generate a parser form an XML schema.

A couple of groups have used JAXB have bind things such as OGC Filter. In general
works well; however it does have trouble responding to content that is
negotiated dynamically ... such as GML.

Q: For WFS why does Parser return a Map?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This shows up as the following error:

* ClassCastException "java.util.HashMap cannot be cast to FeatureCollection".


To understand this error please remember that the GML returned by a WFS GetFeatures
request is a normal XML file, with a reference to an XML schemaat the top.

For a WFS GetFeature response the schema reference is usually a DescribeFeatureType call that returns
an XML Schema.

If this schema is incorrectly configured (common with MapServer) or cannot be reached (common with restricted
environments) our Parser will give up guessing what is a Feature and just return the values in a HashMap.

.. note::
   
   If you are using the GML utility class it will perform a bit of analysis and create an ad-hoc
   FeatureType in order to return you Features.
   
   - looking at the HashMaps returned
   - building a feature type that matches that kind of contents
   - building features that match that FeatureType
   
   This is similar to the approach taken by OGR; OGR figures out where the "geometry" is; and then
   goes up two levels and assumes those things are features.

   Both of these approaches are strictly a work around for a common problem of misconfigured WFS servers.
   
Here is how to review the configuration of your WFS Server:

1. If you are having a problem with a HashMap being returned when you expect a Feature, you should check
   GetCapabilities responses for remote server e.g. with browser::
   
      http://{URL}?SERVICE=WFS&VERSION=1.1.0&REQUEST=GetCapabilities

2. After that, ensure that <ows:Operation> elements contain urls that actually work

3. In particular check that DescribeFeatureType responds with the expected XML Schema


Q: I am in a restricted environment, how to configure SchemaLocator?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can configure the Parser with a SchemaLocator (we use this internally to force the parser
to a copy of the GML schema included in the GeoTools jars; rather than force the Parser to download
the GML schema each time).

1. To customise how XML Schemas are located in a restricted environment (such as web portal for multiple
   WFS services that require authorization, or require the use of an http proxy for schema requests).

2. Create custom SchemaLocator we start with configuration like so::

	    GMLConfiguration configuration = new GMLConfiguration() {
	        public void configureContext(final MutablePicoContainer container) {
	            super.configureContext(container);
	            String username = "geotools user";
	            String password = "support osgeo";
	            Boolean useProxy = true;
	            XSDSchemaLocator locator = new CachingSchemaLocator(username, password, useProxy);
	            QName key = new QName("mycustom", "schemaLocator");
	            container.registerComponentInstance(key, locator);
	        }
	    };
   
3. In the above code, CachingSchemaLocator is a custom XSDSchemaLocator::
          
	    public class CachingSchemaLocator implements XSDSchemaLocator {
	        public XSDSchema locateSchema(XSDSchema schema, String namespaceURI, String rawSchemaLocationURI, String resolvedSchemaLocationURI) {
	
	             ... Implementation ...
	
	        }
	    }
   
4. Set up a configuration for use with the Parser::

	    Parser parser = new Parser(configuration);
	
	    parser.setValidating(false);
	    parser.setFailOnValidationError(false);
	    parser.setStrict(false);
	    FeatureCollection<SimpleFeatureType, SimpleFeature> features = parser.parse(... WFS response InputStream Here ...);