package org.geotools.data.csv;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;

public class WKTReadTest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testReadWKT() throws FileNotFoundException, IOException {
        Map<String, Serializable> params = new HashMap<>();
        File output = TestData.file(this, "test-wkt.csv");
        params.put(CSVDataStoreFactory.FILE_PARAM.key, output);
        params.put(CSVDataStoreFactory.STRATEGYP.key, CSVDataStoreFactory.WKT_STRATEGY);
        params.put(CSVDataStoreFactory.WKTP.key, "geom");

        FileDataStore instore = (FileDataStore) DataStoreFinder.getDataStore(params);
        assertNotNull(instore);

        SimpleFeatureCollection features =
                instore.getFeatureSource()
                        .getFeatures(/* CQL.toFilter("STATE_NAME = 'Wyoming'") */ );
        SimpleFeature feature = DataUtilities.first(features);
        assertNotNull(feature);

        MultiPolygon defaultGeometry = (MultiPolygon) feature.getDefaultGeometry();
        assertNotNull(defaultGeometry);
        for (Coordinate coord : defaultGeometry.getCoordinates()) {
            assertTrue(coord.x < 0);
            assertTrue(coord.y > 0);
        }
    }
}
