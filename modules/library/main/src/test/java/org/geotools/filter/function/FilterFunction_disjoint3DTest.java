package org.geotools.filter.function;

import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

/**
 * Unit tests for FilterFunction_disjoint3D
 *
 * @author Martin Davis
 */
public class FilterFunction_disjoint3DTest {

    @Test
    public void testIntersects() throws Exception {
        FilterFactoryImpl ff = new FilterFactoryImpl();

        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        WKTReader reader = new WKTReader();
        Geometry geom1 = reader.read("LINESTRING(0 0 0, 10 10 10)");
        Feature f = SimpleFeatureBuilder.build(type, new Object[] {"testFeature1", geom1}, null);

        Geometry geom2 = reader.read("LINESTRING(10 0 0, 0 10 10)");
        Literal literal_geom = ff.literal(geom2);

        Function exp = ff.function("disjoint3D", ff.property("geom"), literal_geom);
        Object value = exp.evaluate(f);
        Assert.assertTrue(value instanceof Boolean);
        Assert.assertFalse((Boolean) value);
    }
}
