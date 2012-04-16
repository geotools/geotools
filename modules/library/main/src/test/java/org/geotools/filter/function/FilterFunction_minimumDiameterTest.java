package org.geotools.filter.function;

import com.vividsolutions.jts.algorithm.MinimumDiameter;
import static org.junit.Assert.*;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

/**
 * The FilterFunction_minimumDiameter UnitTest
 * @author Jared Erickson
 */
public class FilterFunction_minimumDiameterTest {

    /**
     * Test of getArgCount method, of class FilterFunction_minimumDiameter.
     */
    @Test
    public void testGetArgCount() {
        FilterFunction_minimumDiameter f = new FilterFunction_minimumDiameter();
        assertEquals(1, f.getArgCount());
    }

    /**
     * Test of getName method, of class FilterFunction_minimumDiameter.
     */
    @Test
    public void getName() {
        FilterFunction_minimumDiameter f = new FilterFunction_minimumDiameter();
        assertEquals("minimumdiameter", f.getName());
    }

    /**
     * Test of evaluate method, of class FilterFunction_minimumDiameter.
     */
    @Test
    public void testEvaluate() throws Exception {

        // Create SimpleFeatures
        SimpleFeatureType type = DataUtilities.createType("polygons", "id:int,geom:Polygon");
        MemoryDataStore store = new MemoryDataStore();
        store.createSchema(type);
        String[] polygons = {
            "POLYGON ((1235702.2034807256 707935.1879023351, 1229587.156498981 671715.2942412316, 1242287.6386918353 688649.2704983709, 1245109.9680680253 677359.9529936113, 1247932.297444215 711227.9055078899, 1239935.6975450104 705583.2467555101, 1235702.2034807256 707935.1879023351))",
            "POLYGON ((1237113.3681688206 622324.5301579087, 1224883.274205331 586575.0247261701, 1258280.8384902447 589397.3541023601, 1237113.3681688206 622324.5301579087))",
            "POLYGON ((1131746.4047910655 718754.1171777296, 1115282.8167632914 681593.4470578962, 1139272.6164609052 679241.5059110713, 1147269.2163601099 707935.1879023351, 1131746.4047910655 718754.1171777296)))"
        };
        WKTReader reader = new WKTReader();
        SimpleFeature[] features = new SimpleFeature[polygons.length];
        for (int i = 0; i < polygons.length; i++) {
            Geometry polygon = reader.read(polygons[i]);
            features[i] = SimpleFeatureBuilder.build(type, new Object[]{i, polygon}, String.valueOf(i));
        }
        store.addFeatures(features);
        SimpleFeatureCollection featureCollection = store.getFeatureSource("polygons").getFeatures();

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
