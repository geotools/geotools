Style
-----

References:

* http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd (xml schema)
* http://schemas.opengis.net/sld/1.0.0/example-sld.xml (example)


Parse SLD
^^^^^^^^^

Parser
''''''

You can use a GTXML Configuration to parse and encode SLD documents.

To parse an SLD document use the SLDConfiguration::
  
  Configuration config = new SLDConfiguration();
  Parser parser = new Parser(config);
  StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(inputStream);

Consider the following sld document::
  
  <StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
    <NamedLayer>
      <Name>OCEANSEA_1M:Foundation</Name>
      <UserStyle>
         <Name>GEOSYM</Name>
         <IsDefault>1</IsDefault>
         <FeatureTypeStyle>
            <Rule>
               <Name>main</Name>
               <PolygonSymbolizer>
                  <Geometry>
                     <ogc:PropertyName>GEOMETRY</ogc:PropertyName>
                  </Geometry>
                  <Fill>
                     <CssParameter name="fill">#96C3F5</CssParameter>
                  </Fill>
               </PolygonSymbolizer>
            </Rule>
         </FeatureTypeStyle>
      </UserStyle>
    </NamedLayer>
  </StyledLayerDescriptor>

To parse the document::
  
  import java.io.IOException;
  import java.io.InputStream;
  import javax.xml.parsers.ParserConfigurationException;
  import org.geotools.sld.SLDConfiguration;
  import org.geotools.styling.StyledLayerDescriptor;
  import org.geotools.xml.Configuration;
  import org.geotools.xml.Parser;
  import org.xml.sax.SAXException;
  ...
  
  //create the parser with the sld configuration
  Configuration config = new SLDConfiguration();
  Parser parser = new Parser(config);
  
  //the xml instance document above
  InputStream xml = ...
  
  //parse
  try {
      StyledLayerDescriptor sld = (StyledLayerDescriptor) parser.parse(xml);
  
  } catch (IOException iox) {
    // handle the exception
  } catch (SAXException sax) {
    // handle the exception
  } catch (ParserConfigurationException px) {
    // handle the exception
  }

SLDParser (DOM)
'''''''''''''''

The SLDParser makes use of an in memory DOM data structure. it is well tested and used by uDig and GeoServer.

In addition to working with a DOM it offers a number of other constructors:

* SLDParser( StyleFactory, fileName )
* SLDParser( StyleFactory, URL )
* SLDParser( StyleFactory, File )
* SLDParser( StyleFactory, InputStream )
* SLDParser( StyleFactory, Reader )

You can use an SLD parser to directly parse out the full StyleLayerDescriptor
data structure::

  SLDParser stylereader = new SLDParser(styleFactory, url);
  StyledLayerDescriptor sld = stylereader.parseSLD();

Parse a StyleLayerDescriptor from an input stream::
  
  SLDParser stylereader = new SLDParser(styleFactory, inputStream );
  StyledLayerDescriptor sld = stylereader.parseSLD();

Or you can skip all of that and extract the defined Style objects as an array::
  
  SLDParser stylereader = new SLDParser( styleFactory, url);
  Style styles[] = stylereader.readXML();

This also works for an input stram::
  
  SLDParser stylereader = new SLDParser(styleFactory, inputStream);
  Style styles[] = stylereader.readXML();


Encode SLD
^^^^^^^^^^

Encoder
'''''''

The same SLDConfiguration can be used for encoding::
  
  Configuration = new SLDConfiguration();
  Encoder encoder = new org.geotools.xml.Encoder( configuration );
  encoder.encode( sld, org.geotools.sld.bindings.SLD.STYLEDLAYERDESCRIPTOR, outputStream );

SLDTransformer
''''''''''''''

We make use of a traditional XML transform idiom to produce XML content quickly. The Transform walks through an existing data structure generating SAX events during the walk.

You can also use the a Transformer, the usual warning applies that this is a fast solution that requires careful configuration in order to produce valid output.

Example of SLDTransofrmer::
  
  SLDTransformer transformer = new SLDTransformer();
  String xml = transformer.transform( sld );

You can customise the output as well::

  FeatureTransformer transform = new FeatureTranformer();
  
  // optional set up
  transform.setIndentation(4);
  transform.getFeatureNamesapces().declarePrefix("myns","http://somewhere.org");
  transform.setSrsName( "EPSG:4326" );
  
  // generate XML
  transform.transform( features, output );

Parse SE
^^^^^^^^

The Symbology Encoding specification cuts down a lot of the boiler plate SLD constructs
allowing you to focus on FeatureTypeStyle directly:

.. literalinclude:: /../../modules/extension/xsd/xsd-sld/src/test/resources/org/geotools/se/v1_1/example-featurestyle.xml
     :language: xml

It also has a coverage style (which we still represent as a FeatureTypeStyle):

.. literalinclude:: /../../modules/extension/xsd/xsd-sld/src/test/resources/org/geotools/se/v1_1/example-coveragestyle.xml
     :language: xml

Parser
''''''

To parse an SE document use the SEConfiguration::
  
  Configuration config = new SEConfiguration();
  Parser parser = new Parser(config);
  FeatureTypeStyle style = (FeatureTypeStyle) parser.parse(inputStream);

There is also an SLD 1.1 configuration you can use.

Encode SE
^^^^^^^^^

To encode an SE document use the SEConfiguration with the appropriate target.

As an example a feature type style::

  Configuration = new SEConfiguration();
  Encoder encoder = new org.geotools.xml.Encoder( configuration );
  encoder.encode( style, org.geotools.se.SE.FeatureTypeStyle, outputStream );

Example with a coverage style::

  Configuration = new SEConfiguration();
  Encoder encoder = new org.geotools.xml.Encoder( configuration );
  encoder.encode( style, org.geotools.se.SE.CoverageStyle, outputStream );

There is also an SLD 1.1 configuration you can use.