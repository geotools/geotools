package org.geotools.data.shapefile;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class ShapefileNullHandlingTest extends TestCaseSupport {

    SimpleFeatureType schema;
    SimpleFeatureCollection collection;
    private SimpleFeature[] features;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Point.class, 4326);
        tb.add("name", String.class);
        tb.setName("testnulls");
        schema = tb.buildFeatureType();

        GeometryFactory gf = new GeometryFactory();

        features = new SimpleFeature[4];
        features[0] = SimpleFeatureBuilder.build(schema, new Object[] {null, "zero"}, "1");
        features[1] =
                SimpleFeatureBuilder.build(
                        schema, new Object[] {gf.createPoint(new Coordinate(0, 10)), "one"}, "2");
        features[2] = SimpleFeatureBuilder.build(schema, new Object[] {null, "two"}, "3");
        features[3] =
                SimpleFeatureBuilder.build(
                        schema, new Object[] {gf.createPoint(new Coordinate(10, 10)), null}, "4");

        collection = DataUtilities.collection(features);
    }

    @Test
    public void testWriteNulls() throws Exception {
        File tempShape = getTempFile();
        ShapefileDataStore store = new ShapefileDataStore(tempShape.toURI().toURL());
        store.createSchema(schema);

        // write out the features
        FeatureStore fs = (FeatureStore) store.getFeatureSource();
        fs.addFeatures(collection);

        // read it back
        SimpleFeature[] readfc = (SimpleFeature[]) fs.getFeatures().toArray(new SimpleFeature[3]);

        // check the first geometry
        Geometry read = (Geometry) features[0].getDefaultGeometry();
        assertNull(read);

        Geometry orig = (Geometry) features[1].getDefaultGeometry();
        read = (Geometry) features[1].getDefaultGeometry();
        assertTrue(orig.equalsTopo(read));

        // check the null geometry
        read = (Geometry) features[2].getDefaultGeometry();
        assertNull(read);

        // make sure the third is ok as well
        orig = (Geometry) features[3].getDefaultGeometry();
        read = (Geometry) features[3].getDefaultGeometry();
        assertTrue(orig.equalsTopo(read));

        store.dispose();
    }
}
