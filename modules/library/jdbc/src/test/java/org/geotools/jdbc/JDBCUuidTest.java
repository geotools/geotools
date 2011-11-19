/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.jdbc;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author kbyte
 */
public abstract class JDBCUuidTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCUuidTestSetup createTestSetup();

    protected UUID uuid1 = UUID.fromString("c563f527-507e-4b80-a30b-4cab189d4dba");

    protected UUID uuid2 = UUID.fromString("cae47178-2e84-4319-a5ba-8d4089c9d80d");
    
    protected UUID uuid3 = UUID.fromString("34362328-9842-2385-8926-000000000003");

    public void testGetSchema() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("guid"));
        assertEquals(UUID.class, ft.getDescriptor(aname("uuidProperty")).getType().getBinding());
    }

    public void testGetFeatures() throws Exception {
        FeatureReader<SimpleFeatureType, SimpleFeature> r = dataStore.getFeatureReader(new Query(tname(("guid"))),
                Transaction.AUTO_COMMIT);
        r.hasNext();

        Set<UUID> uuids = new HashSet<UUID>();
        uuids.add(uuid1);
        uuids.add(uuid2);
        while(r.hasNext()) {
            SimpleFeature f = (SimpleFeature) r.next();
            assertNotNull(uuids.remove(f.getAttribute(aname("uuidProperty"))));
        }
        assertTrue(uuids.isEmpty());

        r.close();
    }

    public void testInsertFeatures() throws Exception {
        Transaction transaction = new DefaultTransaction();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(tname("guid"));
        featureStore.setTransaction(transaction);
        assertEquals(featureStore.getCount(Query.ALL),2);

        SimpleFeatureType type = dataStore.getSchema(tname("guid"));
        SimpleFeature feature = SimpleFeatureBuilder.build(type, new Object[] { uuid3 }, "guid.3");

        SimpleFeatureCollection collection = DataUtilities.collection(feature);
        featureStore.addFeatures(collection);
        transaction.commit();
        assertEquals(featureStore.getCount(Query.ALL),3);
        transaction.close();
    }

    public void testModifyFeatures() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> w = dataStore.getFeatureWriter(tname("guid"),Transaction.AUTO_COMMIT);
        w.hasNext();
        SimpleFeature f = w.next();
        f.setAttribute(aname("uuidProperty"), uuid2);
        assertEquals(uuid2, f.getAttribute(aname("uuidProperty")));
        w.write();
        w.close();
    }

    public void testRemoveFeatures() throws Exception {
        SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(tname("guid"));

        final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        Filter filter = ff.equals(ff.property(aname("uuidProperty")), ff.literal(uuid1));
        
        featureStore.removeFeatures(filter);

        assertEquals(1, featureStore.getCount(Query.ALL));
    }

    public void testUUIDAsPrimaryKey() throws Exception {
        Transaction transaction = new DefaultTransaction();
        SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(tname("uuidt"));
        featureStore.setTransaction(transaction);

        featureStore.addFeatures(createFeatureCollection());

        transaction.commit();
        assertEquals(3, featureStore.getCount(Query.ALL));
        transaction.close();
    }

    private SimpleFeatureCollection createFeatureCollection() throws Exception {
        SimpleFeatureType type = dataStore.getSchema(tname("uuidt"));

        GeometryFactory gf = new GeometryFactory();
        Point p = gf.createPoint(new Coordinate(138.6, -34.93));

        SimpleFeature feature1 = SimpleFeatureBuilder.build(type, new Object[] { p }, uuid1.toString());
        feature1.getUserData().put(Hints.USE_PROVIDED_FID, true);
        feature1.getUserData().put(Hints.PROVIDED_FID, uuid1);

        SimpleFeature feature2 = SimpleFeatureBuilder.build(type, new Object[] { p }, uuid2.toString());
        feature2.getUserData().put(Hints.USE_PROVIDED_FID, true);
        feature2.getUserData().put(Hints.PROVIDED_FID, uuid2);

        SimpleFeature feature3 = SimpleFeatureBuilder.build(type, new Object[] { p }, uuid3.toString());
        feature3.getUserData().put(Hints.USE_PROVIDED_FID, true);
        feature3.getUserData().put(Hints.PROVIDED_FID, uuid3);

        return DataUtilities.collection(new SimpleFeature[]{feature1, feature2, feature3});
    }
}
