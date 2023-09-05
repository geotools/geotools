package org.geotools.kml.v22;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.xsd.Encoder;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class KMLEncodingTest extends KMLTestSupport {
    @Test
    public void testEncodePoint() throws Exception {
        Point p = new GeometryBuilder().point(1, 2);
        Document d = encode(p, KML.Point);

        assertEquals("Point", d.getDocumentElement().getLocalName());
        Element e = getElementByQName(d, KML.coordinates);
        assertNotNull(e);

        assertEquals("1.0,2.0", e.getFirstChild().getNodeValue());
    }

    @Test
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

    @Test
    public void testEncodeSimpleFeaturecollection() throws Exception {
        GeometryFactory geomFactory = new GeometryFactory();
        DefaultFeatureCollection collection = new DefaultFeatureCollection("internal", null);
        SimpleFeatureType type =
                DataUtilities.createType("location", "geom:Point,name:String,attr2:Integer");

        Point point1 = geomFactory.createPoint(new Coordinate(40, 50));
        Point point2 = geomFactory.createPoint(new Coordinate(30, 45));
        Point point3 = geomFactory.createPoint(new Coordinate(35, 46));
        collection.add(
                SimpleFeatureBuilder.build(type, new Object[] {point1, "first feat.", 17}, null));
        collection.add(
                SimpleFeatureBuilder.build(type, new Object[] {point2, "feature #2", 24}, null));
        collection.add(
                SimpleFeatureBuilder.build(type, new Object[] {point3, "third feature", 42}, null));

        Encoder encoder = new Encoder(new KMLConfiguration());
        // Note: if indenting is set to true, this will give weird results
        // when parsed as XML (extra text XML elements)
        // encoder.setIndenting(true);

        Document kmlDoc = encoder.encodeAsDOM(collection, KML.kml);

        // first child should be kml and the namespace URI should match kml v2.2
        assertEquals(KML.kml.getLocalPart(), kmlDoc.getFirstChild().getLocalName());
        assertEquals(KML.NAMESPACE, kmlDoc.getFirstChild().getNamespaceURI());

        // //kml/Document id should be internal (first item of the generated doc is text
        // the actual <kml:Document> is the second element of the list).
        Node docNode = kmlDoc.getFirstChild().getChildNodes().item(0);

        assertEquals(KML.Document.getLocalPart(), docNode.getLocalName());
        Attr docId = (Attr) docNode.getAttributes().getNamedItem("id");
        assertEquals("internal", docId.getValue());

        // items are separated by linefeeds at parsing (XML Text elements)
        Node Placemark1 = docNode.getChildNodes().item(0);
        Node Placemark2 = docNode.getChildNodes().item(1);
        Node Placemark3 = docNode.getChildNodes().item(2);

        assertEquals(KML.Placemark.getLocalPart(), Placemark1.getLocalName());
        assertEquals(KML.Placemark.getLocalPart(), Placemark2.getLocalName());
        assertEquals(KML.Placemark.getLocalPart(), Placemark3.getLocalName());

        // First XML child element should be the kml:name one
        Node kmlName = Placemark1.getChildNodes().item(0);
        assertEquals(KML.name.getLocalPart(), kmlName.getLocalName());

        // ExtendedData (second XML element)
        Node extData1 = Placemark1.getChildNodes().item(1);
        assertEquals(KML.ExtendedData.getLocalPart(), extData1.getLocalName());

        Node data1 = extData1.getChildNodes().item(0), data2 = extData1.getChildNodes().item(1);
        assertEquals(KML.Data.getLocalPart(), data1.getLocalName());
        assertEquals(KML.Data.getLocalPart(), data2.getLocalName());
        // We cannot predict the features order, just check the name of the attribute columns.
        Attr attrName1 = (Attr) data1.getAttributes().getNamedItem(KML.name.getLocalPart());
        Attr attrName2 = (Attr) data2.getAttributes().getNamedItem(KML.name.getLocalPart());

        assertEquals("name", attrName1.getValue());
        assertEquals("attr2", attrName2.getValue());
    }

    @Test
    public void testEncodeNullAttribute() throws Exception {
        GeometryFactory geomFactory = new GeometryFactory();
        DefaultFeatureCollection collection = new DefaultFeatureCollection("internal", null);
        SimpleFeatureType type =
                DataUtilities.createType("location", "geom:Point,name:String,attr2:Integer");

        Point point1 = geomFactory.createPoint(new Coordinate(40, 50));
        Point point2 = geomFactory.createPoint(new Coordinate(30, 45));
        Point point3 = geomFactory.createPoint(new Coordinate(35, 46));
        collection.add(SimpleFeatureBuilder.build(type, new Object[] {point1, null, 17}, null));
        collection.add(
                SimpleFeatureBuilder.build(type, new Object[] {point2, "feature #2", null}, null));
        collection.add(
                SimpleFeatureBuilder.build(type, new Object[] {point3, "third feature", 42}, null));

        Encoder encoder = new Encoder(new KMLConfiguration());
        // Note: if indenting is set to true, this will give weird results
        // when parsed as XML (extra text XML elements)
        // encoder.setIndenting(true);
        // this will throw a NPE without GEOT-6122 fix
        encoder.encodeAsDOM(collection, KML.kml);
    }
}
