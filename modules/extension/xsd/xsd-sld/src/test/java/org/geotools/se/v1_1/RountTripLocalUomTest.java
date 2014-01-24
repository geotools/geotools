package org.geotools.se.v1_1;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.junit.Test;
import org.opengis.style.PointSymbolizer;
import org.opengis.style.PolygonSymbolizer;
import org.opengis.style.TextSymbolizer;
import org.w3c.dom.Document;

public class RountTripLocalUomTest extends SETestSupport {

    @Test
    public void testRoundTripPoint() throws Exception {
        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer-local-uom.xml");
        assertEquals(NonSI.PIXEL, sym.getUnitOfMeasure());
        assertEquals("8m", sym.getGraphic().getSize().evaluate(null, String.class));
        Document doc = encode(sym, SE.PointSymbolizer);
        // print(doc);
        // what... nothing getting encoded??
    }
    
    @Test
    public void testRoundTripPolygon() throws Exception {
        PolygonSymbolizer sym = (PolygonSymbolizer) parse("example-polygonsymbolizer-local-uom.xml");
        assertEquals(SI.METER, sym.getUnitOfMeasure());
        assertEquals("2m", sym.getStroke().getWidth().evaluate(null, String.class));
        Document doc = encode(sym, SE.PolygonSymbolizer);
        // print(doc);
        // what... nothing getting encoded??
    }
    
    @Test
    public void testRoundTripText() throws Exception {
        TextSymbolizer sym = (TextSymbolizer) parse("example-textsymbolizer-local-uom.xml");
        assertEquals(NonSI.PIXEL, sym.getUnitOfMeasure());
        assertEquals("10m", sym.getFont().getSize().evaluate(null, String.class));
        Document doc = encode(sym, SE.TextSymbolizer);
        // print(doc);
        // nothing getting encoded...
    }
}
