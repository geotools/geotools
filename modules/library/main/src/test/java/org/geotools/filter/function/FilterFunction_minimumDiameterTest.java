package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.junit.Test;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

/**
 * The FilterFunction_minimumDiameter UnitTest
 *
 * @author Jared Erickson
 */
public class FilterFunction_minimumDiameterTest {

    /** Test of getArgCount method, of class FilterFunction_minimumDiameter. */
    @Test
    public void testGetArgCount() {
        FilterFunction_minimumDiameter f = new FilterFunction_minimumDiameter();
        assertEquals(1, f.getFunctionName().getArgumentCount());
    }

    /** Test of getName method, of class FilterFunction_minimumDiameter. */
    @Test
    public void getName() {
        FilterFunction_minimumDiameter f = new FilterFunction_minimumDiameter();
        assertEquals("minimumdiameter", f.getName());
    }

    /** Test of evaluate method, of class FilterFunction_minimumDiameter. */
    @Test
    public void testEvaluate() throws Exception {
        SimpleFeatureCollection featureCollection = FunctionTestFixture.polygons();

        // Test the Function
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Function exp = ff.function("minimumdiameter", ff.property("geom"));
        SimpleFeatureIterator iter = featureCollection.features();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            Geometry geom = (Geometry) feature.getDefaultGeometry();
            Geometry minimumDiameter = new MinimumDiameter(geom).getDiameter();
            Object value = exp.evaluate(feature);
            assertTrue(value instanceof LineString);
            assertTrue(minimumDiameter.equalsExact((Geometry) value, 0.1));
        }
        iter.close();

        // Check for null safeness
        assertNull(exp.evaluate(null));
    }
}
