package org.geotools.filter.function;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

public class FilterFunction_setCRSTest {
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
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
