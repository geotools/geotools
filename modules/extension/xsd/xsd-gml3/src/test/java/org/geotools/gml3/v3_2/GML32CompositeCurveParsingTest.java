package org.geotools.gml3.v3_2;

import org.geotools.xml.Parser;
import org.locationtech.jts.geom.Geometry;

public class GML32CompositeCurveParsingTest extends GML32TestSupport {

    public void testCompositeCurve() throws Exception {
        GMLConfiguration gml = new GMLConfiguration(true);
        Parser p = new Parser(gml);
        Object compositeCurve = p.parse(getClass().getResourceAsStream("gml_compositecurve_1.xml"));
        assertFalse(compositeCurve instanceof String);
        // System.out.println(compositeCurve);
        assertTrue("wrong element type", compositeCurve instanceof Geometry);
        Geometry geom = (Geometry) compositeCurve;
        assertEquals("LINESTRING (353148.991 5530600.811, 353151.478 5530602.263)", geom.toText());
        // assertNotNull(l);
    }
}
