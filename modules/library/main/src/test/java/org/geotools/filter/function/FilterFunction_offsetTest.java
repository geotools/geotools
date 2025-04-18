package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

public class FilterFunction_offsetTest {

    @Test
    public void testInvert() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Function function = ff.function("offset", ff.property("the_geom"), ff.literal(1), ff.literal(2));

        assertTrue(function instanceof GeometryTransformation);
        GeometryTransformation gt = (GeometryTransformation) function;
        ReferencedEnvelope re = new ReferencedEnvelope(0, 1, 0, 1, DefaultGeographicCRS.WGS84);
        ReferencedEnvelope inverted = gt.invert(re);
        ReferencedEnvelope expected = new ReferencedEnvelope(-1, 0, -2, -1, DefaultGeographicCRS.WGS84);
        assertEquals(expected, inverted);
    }
}
