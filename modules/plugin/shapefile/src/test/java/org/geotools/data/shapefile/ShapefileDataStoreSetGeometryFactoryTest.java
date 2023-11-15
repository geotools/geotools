package org.geotools.data.shapefile;

import static org.geotools.api.data.Transaction.AUTO_COMMIT;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.URLP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.TestData;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * @version $Id$
 * @author Burkhard Strauss
 */
public final class ShapefileDataStoreSetGeometryFactoryTest {

    private static final String BASE = "mzvalues/mpoints";
    private static final String[] NAMES = {
        BASE + ".dbf", BASE + ".prj", BASE + ".shp", BASE + ".shx"
    };

    private static final Map<String, Serializable> PARAMS = new HashMap<>();

    @Before
    public void beforeShapefileDataStoreSetGeometryFactoryTest() throws IOException {

        for (final String name : NAMES) {
            assertTrue(TestData.copy(this, name).canRead());
        }
        final URL url = TestData.file(this, NAMES[2]).toURI().toURL();
        PARAMS.put(URLP.key, url);
    }

    @Test
    public void testSetGeometryFactory() throws IOException {

        final int mySrid = 12345;
        final GeometryFactory myGeometryFactory = new GeometryFactory(new PrecisionModel(), mySrid);
        final ShapefileDataStore dataStore =
                (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(PARAMS);
        final String typeName = dataStore.getTypeNames()[0];
        dataStore.setGeometryFactory(myGeometryFactory);
        final Query query = new Query(typeName);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                dataStore.getFeatureReader(query, AUTO_COMMIT)) {
            assertTrue(reader.hasNext());
            final SimpleFeature feature = reader.next();
            final Object geometry = feature.getDefaultGeometry();
            assertTrue(geometry instanceof Geometry);
            final GeometryFactory factory = ((Geometry) geometry).getFactory();
            assertEquals(mySrid, factory.getSRID());
        }
    }
}
