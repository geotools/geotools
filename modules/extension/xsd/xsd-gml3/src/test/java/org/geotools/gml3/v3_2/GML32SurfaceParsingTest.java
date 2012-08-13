package org.geotools.gml3.v3_2;

import org.geotools.xml.Parser;

import com.vividsolutions.jts.geom.LineString;

public class GML32SurfaceParsingTest extends GML32TestSupport {

    public void testMultiSurface() throws Exception {
        GMLConfiguration gml = new GMLConfiguration(true);
        Parser p = new Parser(gml);
        System.out.println(p.parse(getClass().getResourceAsStream("multisurface.xml")));
        //assertNotNull(l);
    }
}
