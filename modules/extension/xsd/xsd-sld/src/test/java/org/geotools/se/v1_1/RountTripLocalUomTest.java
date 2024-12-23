package org.geotools.se.v1_1;

import static org.junit.Assert.assertEquals;

import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.measure.Units;
import org.junit.Test;
import si.uom.SI;

public class RountTripLocalUomTest extends SETestSupport {

    @Test
    public void testRoundTripPoint() throws Exception {
        PointSymbolizer sym = (PointSymbolizer) parse("example-pointsymbolizer-local-uom.xml");
        assertEquals("pixel", Units.toName(sym.getUnitOfMeasure()));
        assertEquals(Units.PIXEL, sym.getUnitOfMeasure());

        assertEquals("8m", sym.getGraphic().getSize().evaluate(null, String.class));
        encode(sym, SE.PointSymbolizer);
        // print(doc);
        // what... nothing getting encoded??
    }

    @Test
    public void testRoundTripPolygon() throws Exception {
        PolygonSymbolizer sym = (PolygonSymbolizer) parse("example-polygonsymbolizer-local-uom.xml");
        assertEquals(SI.METRE, sym.getUnitOfMeasure());
        assertEquals("2m", sym.getStroke().getWidth().evaluate(null, String.class));
        encode(sym, SE.PolygonSymbolizer);
        // print(doc);
        // what... nothing getting encoded??
    }

    @Test
    public void testRoundTripText() throws Exception {
        TextSymbolizer sym = (TextSymbolizer) parse("example-textsymbolizer-local-uom.xml");

        assertEquals("pixel", Units.toName(sym.getUnitOfMeasure()));

        assertEquals("10m", sym.getFont().getSize().evaluate(null, String.class));
        encode(sym, SE.TextSymbolizer);
        // print(doc);
        // nothing getting encoded...
    }
}
