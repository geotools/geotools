package org.geotools.filter.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

/**
 * The FilterFunction_minimumRectangle UnitTest
 *
 * @author Jared Erickson
 */
public class FilterFunction_minimumRectangleTest {

    /** Test of getArgCount method, of class FilterFunction_minimumRectangle. */
    @Test
    public void testGetArgCount() {
        FilterFunction_minimumRectangle f = new FilterFunction_minimumRectangle();
        assertEquals(1, f.getFunctionName().getArgumentCount());
    }

    /** Test of getName method, of class FilterFunction_minimumRectangle. */
    @Test
    public void getName() {
        FilterFunction_minimumRectangle f = new FilterFunction_minimumRectangle();
        assertEquals("minrectangle", f.getName());
    }

    /** Test of evaluate method, of class FilterFunction_minimumCircle. */
    @Test
    public void testEvaluate() throws Exception {
        SimpleFeatureCollection featureCollection = FunctionTestFixture.polygons();

        // Test the Function
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Function exp = ff.function("minrectangle", ff.property("geom"));
        SimpleFeatureIterator iter = featureCollection.features();
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            Geometry geom = (Geometry) feature.getDefaultGeometry();
            Geometry rectangle = new MinimumDiameter(geom).getMinimumRectangle();
            Object value = exp.evaluate(feature);
            assertTrue(value instanceof Polygon);
            assertTrue(rectangle.equals((Geometry) value));
        }
        iter.close();

        // Check for null safeness
        assertNull(exp.evaluate(null));
    }
}
