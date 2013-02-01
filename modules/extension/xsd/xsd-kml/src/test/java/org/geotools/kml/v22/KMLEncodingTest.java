package org.geotools.kml.v22;

import org.geotools.geometry.jts.GeometryBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    
}
