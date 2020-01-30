package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;

import org.geotools.filter.FilterFactoryImpl;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.opengis.filter.expression.Function;

public class FilterFunction_toWKTTest {

    @Test
    public void testToWKTWithTargetClass() {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        Envelope envelope = new Envelope(10, 10, 20, 20);
        Function toWKT = ff.function("toWKT", ff.property("."));
        String result = (String) toWKT.evaluate(envelope);
        assertEquals(result, "POLYGON ((10 20, 10 20, 10 20, 10 20, 10 20))");
    }
}
