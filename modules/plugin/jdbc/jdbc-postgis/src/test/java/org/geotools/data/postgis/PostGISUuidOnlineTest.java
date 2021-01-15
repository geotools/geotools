/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.data.postgis;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCUuidOnlineTest;
import org.geotools.jdbc.JDBCUuidTestSetup;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** @author kbyte */
public class PostGISUuidOnlineTest extends JDBCUuidOnlineTest {

    @Override
    protected JDBCUuidTestSetup createTestSetup() {
        return new PostGISUuidTestSetup(new PostGISTestSetup());
    }

    // Create a collection where the feature does not have a UUID,
    // so KeysFetcher will need to generate a new ID for this feature
    private SimpleFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("uuidt"));

        GeometryFactory gf = new GeometryFactory();
        Point p = gf.createPoint(new Coordinate(138.6, -34.93));

        SimpleFeature feature1 =
                SimpleFeatureBuilder.build(type, new Object[] {p}, uuid1.toString());
        feature1.getUserData()
                .put(Hints.USE_PROVIDED_FID, false); // false, so that a UUID will be generated
        // feature1.getUserData().put(Hints.PROVIDED_FID, uuid1);

        return DataUtilities.collection(feature1);
    }

    @Override
    public void testUUIDAsPrimaryKey() throws Exception {
        try (Transaction transaction = new DefaultTransaction()) {
            SimpleFeatureStore featureStore =
                    (SimpleFeatureStore) dataStore.getFeatureSource(tname("uuidt"));
            featureStore.setTransaction(transaction);

            featureStore.addFeatures(createFeatureCollection());

            transaction.commit();
            assertEquals(1, featureStore.getCount(Query.ALL));
        }
    }
}
