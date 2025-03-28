package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class FilterFunction_setCRSTest {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    Geometry g;

    @Before
    public void setup() throws Exception {
        g = new WKTReader().read("POINT(0 0)");
    }

    @Test
    public void setCRSObject() {
        Function f = ff.function("setCRS", ff.literal(g), ff.literal(DefaultGeographicCRS.WGS84));
        Geometry sg = (Geometry) f.evaluate(null);
        assertEquals(DefaultGeographicCRS.WGS84, sg.getUserData());
    }

    @Test
    public void setCRSCode() throws Exception {
        Function f = ff.function("setCRS", ff.literal(g), ff.literal("EPSG:4326"));
        Geometry sg = (Geometry) f.evaluate(null);
        assertEquals(CRS.decode("EPSG:4326"), sg.getUserData());
    }

    @Test
    public void setCRSWkt() {
        Function f = ff.function("setCRS", ff.literal(g), ff.literal(DefaultGeographicCRS.WGS84.toWKT()));
        Geometry sg = (Geometry) f.evaluate(null);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, sg.getUserData()));
    }
}
