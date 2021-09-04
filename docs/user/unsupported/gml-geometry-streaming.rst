GML geometry XML parsing support using StAX
-------------------------------------------

Unsupported module that provides a StAX parser for GML geometry.
Efficiently read GML geometry elements using StAX in a streaming manner. No writing.

**Maven**::

    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-gml-geometry-streaming</artifactId>
      <version>${geotools.version}</version>
    </dependency>


Here is a small example from the test cases::

    XMLInputFactory f = XMLInputFactory.newInstance();
    f.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
    XMLStreamReader r = f.createXMLStreamReader(new StringReader(gmlStringSnippet));
    XmlStreamGeometryReader geometryReader = new XmlStreamGeometryReader(r);
    r.nextTag();
    Geometry g = geometryReader.readGeometry();


