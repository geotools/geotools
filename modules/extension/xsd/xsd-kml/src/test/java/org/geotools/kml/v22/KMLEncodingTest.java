package org.geotools.kml.v22;

import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.xml.Encoder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class KMLEncodingTest extends KMLTestSupport {

    public void testEncodePoint() throws Exception {
        Point p = new GeometryBuilder().point(1,2);
        Document d = encode(p, KML.Point);
        
        assertEquals("Point", d.getDocumentElement().getLocalName());
        Element e = getElementByQName(d, KML.coordinates);
        assertNotNull(e);

        assertEquals("1.0,2.0", e.getFirstChild().getNodeValue());
    }

    public void testEncodePolygon() throws Exception {
        Polygon p = new GeometryBuilder().polygon(1, 1, 2, 2, 3, 3, 1, 1);
        Document d = encode(p, KML.Polygon);
        assertEquals("Polygon", d.getDocumentElement().getLocalName());

        Element e = getElementByQName(d, KML.outerBoundaryIs);
        assertNotNull(e);

        e = getElementByQName(e, KML.LinearRing);
        assertNotNull(e);

        e = getElementByQName(e, KML.coordinates);
        assertNotNull(e);
    }

    public void testEncodeSimpleFeaturecollection() throws Exception {
        GeometryFactory geomFactory = new GeometryFactory();
        DefaultFeatureCollection collection = new DefaultFeatureCollection("internal", null);
        SimpleFeatureType type = DataUtilities.createType("location",
                "geom:Point,attr1:String,attr2:Integer");

        Point point1 = geomFactory.createPoint(new Coordinate(40, 50));
        Point point2 = geomFactory.createPoint(new Coordinate(30, 45));
        Point point3 = geomFactory.createPoint(new Coordinate(35, 46));
        collection.add(SimpleFeatureBuilder.build(type, new Object[] { point1, "first feat.", 17 },
                null));
        collection.add(SimpleFeatureBuilder.build(type, new Object[] { point2, "feature #2", 24 },
                null));
        collection.add(SimpleFeatureBuilder.build(type,
                new Object[] { point3, "third feature", 42 }, null));

        Encoder encoder = new Encoder(new KMLConfiguration());
        // Note: if indenting is set to true, this will give weird results
        // when parsed as XML (extra text XML elements)
        // encoder.setIndenting(true);

        Document kmlDoc = encoder.encodeAsDOM(collection, KML.kml);

        // first child should be kml and the namespace URI should match kml v2.2
        assertTrue(KML.kml.getLocalPart().equals(kmlDoc.getFirstChild().getLocalName()));
        assertTrue(KML.NAMESPACE.equals(kmlDoc.getFirstChild().getNamespaceURI()));

        // //kml/Document id should be internal (first item of the generated doc is text
        // the actual <kml:Document> is the second element of the list).
        Node docNode = kmlDoc.getFirstChild().getChildNodes().item(0);

        assertTrue(KML.Document.getLocalPart().equals(docNode.getLocalName()));
        Attr docId = (Attr) docNode.getAttributes().getNamedItem("id");
        assertTrue("internal".equals(docId.getValue()));

        // items are separated by linefeeds at parsing (XML Text elements)
        Node Placemark1 = docNode.getChildNodes().item(0);
        Node Placemark2 = docNode.getChildNodes().item(1);
        Node Placemark3 = docNode.getChildNodes().item(2);

        assertTrue(KML.Placemark.getLocalPart().equals(Placemark1.getLocalName()));
        assertTrue(KML.Placemark.getLocalPart().equals(Placemark2.getLocalName()));
        assertTrue(KML.Placemark.getLocalPart().equals(Placemark3.getLocalName()));

        // ExtendedData (first feature)
        Node extData1 = Placemark1.getChildNodes().item(0);
        assertTrue(KML.ExtendedData.getLocalPart().equals(extData1.getLocalName()));

        Node data1 = extData1.getChildNodes().item(0), data2 = extData1.getChildNodes().item(1);
        assertTrue(KML.Data.getLocalPart().equals(data1.getLocalName()));
        assertTrue(KML.Data.getLocalPart().equals(data2.getLocalName()));
        // We cannot predict the features order, just check the name of the attribute columns.
        Attr attrName1 = (Attr) data1.getAttributes().getNamedItem(KML.name.getLocalPart());
        Attr attrName2 = (Attr) data2.getAttributes().getNamedItem(KML.name.getLocalPart());

        assertTrue("attr1".equals(attrName1.getValue()));
        assertTrue("attr2".equals(attrName2.getValue()));

    }

}
