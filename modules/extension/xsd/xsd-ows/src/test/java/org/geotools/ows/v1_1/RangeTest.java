package org.geotools.ows.v1_1;

import net.opengis.ows11.Ows11Factory;
import net.opengis.ows11.RangeClosureType;
import net.opengis.ows11.RangeType;
import net.opengis.ows11.ValueType;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.junit.Test;

public class RangeTest extends OWSTestSupport_1_1 {

    @Test
    public void testParseRange() throws Exception {
        Parser p = new Parser(createConfiguration());
        Object o = p.parse(getClass().getResourceAsStream("range.xml"));
        RangeType rt = (RangeType) o;
        assertEquals(RangeClosureType.CLOSED_LITERAL, rt.getRangeClosure());
        assertEquals("0.0", rt.getMinimumValue().getValue());
        assertEquals("100.0", rt.getMaximumValue().getValue());
    }

    @Test
    public void testParseOpenClosed() throws Exception {
        Parser p = new Parser(createConfiguration());
        Object o = p.parse(getClass().getResourceAsStream("range-open-closed.xml"));
        RangeType rt = (RangeType) o;
        assertEquals(RangeClosureType.OPEN_CLOSED_LITERAL, rt.getRangeClosure());
        assertNull(rt.getMinimumValue());
        assertEquals("100.0", rt.getMaximumValue().getValue());
    }

    @Test
    public void testEncodeClosedRange() throws Exception {
        Encoder encoder = new Encoder(createConfiguration());
        Ows11Factory factory = Ows11Factory.eINSTANCE;
        RangeType rangeType = factory.createRangeType();
        ValueType min = factory.createValueType();
        min.setValue("0");
        rangeType.setMinimumValue(min);
        ValueType max = factory.createValueType();
        max.setValue("10");
        rangeType.setMaximumValue(max);
        encoder.setIndenting(true);
        String xml = encoder.encodeAsString(rangeType, OWS.Range);
        // System.out.println(xml);
        assertTrue(xml.contains("ows:rangeClosure=\"closed\""));
    }

    @Test
    public void testEncodeClosedOpenRange() throws Exception {
        Encoder encoder = new Encoder(createConfiguration());
        Ows11Factory factory = Ows11Factory.eINSTANCE;
        RangeType rangeType = factory.createRangeType();
        ValueType min = factory.createValueType();
        min.setValue("0");
        rangeType.setMinimumValue(min);
        rangeType.setRangeClosure(RangeClosureType.CLOSED_OPEN_LITERAL);
        encoder.setIndenting(true);
        String xml = encoder.encodeAsString(rangeType, OWS.Range);
        // System.out.println(xml);
        assertTrue(xml.contains("ows:rangeClosure=\"closed-open\""));
    }
}
