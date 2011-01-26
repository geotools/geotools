package org.geotools.gml2.bindings;

import org.geotools.gml2.GML;
import org.w3c.dom.Document;

public class GMLGeometryCollectionTypeBinding2Test extends GMLTestSupport {

    public void testEncode() throws Exception {
        Document dom = encode(GML2MockData.multiGeometry(), GML.MultiGeometry);
        
        assertEquals(3, getElementsByQName(dom, GML.geometryMember).getLength());
        assertNotNull(getElementByQName(dom, GML.Point));
        assertNotNull(getElementByQName(dom, GML.LineString));
        assertNotNull(getElementByQName(dom, GML.Polygon));
    }
}
