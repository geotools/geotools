package org.geotools.ows.v1_1;

import net.opengis.ows11.Ows11Factory;
import net.opengis.ows11.RangeType;
import net.opengis.ows11.ValueType;

import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.junit.Test;

public class RangeTest extends OWSTestSupport {

    @Test
    public void testParseRange() throws Exception {
        Parser p = new Parser(createConfiguration());
        Object o = p.parse(getClass().getResourceAsStream("range.xml"));
        System.out.println(o);
    }

    @Test
    public void testEncodeErange() throws Exception {
        Encoder encoder = new Encoder(createConfiguration());
        Ows11Factory factory = Ows11Factory.eINSTANCE;
        RangeType rangeType = factory.createRangeType();
        ValueType min = factory.createValueType();
        min.setValue("0");
        rangeType.setMinimumValue(min);
        encoder.setIndenting(true);
        String xml = encoder.encodeAsString(rangeType, OWS.Range);
        // System.out.println(xml);
        assertTrue(xml.contains("ows:rangeClosure=\"closed\""));
    }
}
