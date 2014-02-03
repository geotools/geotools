package org.geotools.gml3.v3_2;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.xml.Parser;


public class GML32SurfaceParsingTest extends GML32TestSupport {

    public void testMultiSurface() throws Exception {
        GMLConfiguration gml = new GMLConfiguration(true);
        Parser p = new Parser(gml);
        Object multiSurface = p.parse(getClass().getResourceAsStream("multisurface.xml"));
        System.out.println(multiSurface);
        assertTrue(multiSurface instanceof Geometry);
        //assertNotNull(l);
    }
}
