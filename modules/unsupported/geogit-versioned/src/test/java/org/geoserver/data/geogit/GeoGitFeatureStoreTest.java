/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geoserver.data.geogit;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geogit.api.GeoGIT;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.geogit.GeoGitDataStore;
import org.geotools.data.geogit.GeoGitFeatureStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.ResourceId;

public class GeoGitFeatureStoreTest extends GeoGITRepositoryTestCase {

    private static final FilterFactory2 ff = CommonFactoryFinder
            .getFilterFactory2(null);

    private GeoGitDataStore dataStore;

    private GeoGitFeatureStore points;

    private GeoGitFeatureStore lines;

    @Override
    protected void setUpChild() throws Exception {
        dataStore = new GeoGitDataStore(repo);
        dataStore.createSchema(super.pointsType);
        dataStore.createSchema(super.linesType);

        points = (GeoGitFeatureStore) dataStore
                .getFeatureSource(pointsTypeName);
        lines = (GeoGitFeatureStore) dataStore.getFeatureSource(linesTypeName);
    }
    
    @Override
    protected void tearDownChild() throws Exception {
        dataStore = null;
        points = null;
        lines = null;
    }

    public void testAddFeatures() throws Exception {

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
        collection = DataUtilities.collection(Arrays.asList(
                (SimpleFeature) points1, (SimpleFeature) points2,
                (SimpleFeature) points3));

        try {
            points.addFeatures(collection);
            fail("Expected UnsupportedOperationException on AUTO_COMMIT");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("AUTO_COMMIT"));
        }

        final Set<String> insertedFids = new HashSet<String>(Arrays.asList(
                idP1, idP2, idP3));

        Transaction tx = new DefaultTransaction();
        points.setTransaction(tx);
        assertSame(tx, points.getTransaction());
        try {
            List<FeatureId> addedFeatures = points.addFeatures(collection);
            assertNotNull(addedFeatures);
            assertEquals(3, addedFeatures.size());

            for (FeatureId id : addedFeatures) {
                assertFalse(id instanceof ResourceId);
                assertNotNull(id.getFeatureVersion());
            }

            // assert transaction isolation

            assertEquals(3, points.getFeatures().size());
            assertEquals(0, dataStore.getFeatureSource(pointsTypeName)
                    .getFeatures().size());

            tx.commit();

            assertEquals(3, dataStore.getFeatureSource(pointsTypeName)
                    .getFeatures().size());
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            tx.close();
        }
    }

    public void testUseProvidedFIDSupported() throws Exception {

        assertTrue(points.getQueryCapabilities().isUseProvidedFIDSupported());

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection;
        collection = DataUtilities.collection(Arrays.asList(
                (SimpleFeature) points1, (SimpleFeature) points2,
                (SimpleFeature) points3));

        Transaction tx = new DefaultTransaction();
        points.setTransaction(tx);
        try {
            List<FeatureId> newFids = points.addFeatures(collection);
            assertNotNull(newFids);
            assertEquals(3, newFids.size());

            FeatureId fid1 = newFids.get(0);
            FeatureId fid2 = newFids.get(1);
            FeatureId fid3 = newFids.get(2);

            // new ids should have been generated...
            assertFalse(idP1.equals(fid1.getID()));
            assertFalse(idP1.equals(fid1.getID()));
            assertFalse(idP1.equals(fid1.getID()));

            // now force the use of provided feature ids
            points1.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);
            points2.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);
            points3.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);

            List<FeatureId> providedFids = points.addFeatures(collection);
            assertNotNull(providedFids);
            assertEquals(3, providedFids.size());

            FeatureId fid11 = providedFids.get(0);
            FeatureId fid21 = providedFids.get(1);
            FeatureId fid31 = providedFids.get(2);

            // ids should match provided
            assertEquals(idP1, fid11.getID());
            assertEquals(idP2, fid21.getID());
            assertEquals(idP3, fid31.getID());

            tx.commit();

            assertEquals(1,
                    points.getFeatures(ff.id(Collections.singleton(fid1)))
                            .size());
            assertEquals(1,
                    points.getFeatures(ff.id(Collections.singleton(fid2)))
                            .size());
            assertEquals(1,
                    points.getFeatures(ff.id(Collections.singleton(fid3)))
                            .size());

            assertEquals(1,
                    points.getFeatures(ff.id(Collections.singleton(fid11)))
                            .size());
            assertEquals(1,
                    points.getFeatures(ff.id(Collections.singleton(fid21)))
                            .size());
            assertEquals(1,
                    points.getFeatures(ff.id(Collections.singleton(fid31)))
                            .size());

        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            tx.close();
        }
    }
    private SimpleFeature sample( SimpleFeatureSource store, Filter filter ) throws IOException {
        SimpleFeatureCollection result = store.getFeatures( filter );
        return DataUtilities.first( result );
    }
    public void testModifyFeatures() throws Exception {
        // add features circunventing FeatureStore.addFeatures to keep the test
        // independent of the
        // addFeatures functionality
        insertAndAdd(lines1, lines2, lines3, points1, points2, points3);
        new GeoGIT(repo).commit().call();

        Id filter = ff.id(Collections.singleton(ff.featureId(idP1)));
        Transaction tx = new DefaultTransaction();
        points.setTransaction(tx);
        try {
            // initial value
            assertEquals("StringProp1_1", sample( points, filter ).getAttribute("sp"));
            // modify
            points.modifyFeatures("sp", "modified", filter);
            // modified value before commit
            assertEquals("modified", sample( points, filter ).getAttribute("sp"));
            // unmodified value before commit on another store instance (tx
            // isolation)
            assertEquals(
                    "StringProp1_1",
                    sample( dataStore.getFeatureSource(pointsTypeName), filter )
                            .getAttribute("sp"));

            tx.commit();

            // modified value after commit on another store instance
            assertEquals("modified", sample( dataStore.getFeatureSource(pointsTypeName),filter)
                    .getAttribute("sp") );
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            tx.close();
        }
        SimpleFeature modified = sample( points,filter);
        assertEquals("modified", modified.getAttribute("sp"));
    }

    private void setUseProvidedFidHint(boolean useProvidedFid,
            Feature... features) {
        for (Feature f : features) {
            f.getUserData().put(Hints.USE_PROVIDED_FID,
                    Boolean.valueOf(useProvidedFid));
        }
    }

    public void testRemoveFeatures() throws Exception {
        // add features circunventing FeatureStore.addFeatures to keep the test
        // independent of the
        // addFeatures functionality
        insertAndAdd(lines1, lines2, lines3, points1, points2, points3);
        new GeoGIT(repo).commit().call();

        Id filter = ff.id(Collections.singleton(ff.featureId(idP1)));
        Transaction tx = new DefaultTransaction();
        points.setTransaction(tx);
        try {
            // initial # of features
            assertEquals(3, points.getFeatures().size());
            // remove feature
            points.removeFeatures(filter);

            // #of features before commit on the same store
            assertEquals(2, points.getFeatures().size());

            // #of features before commit on a different store instance
            assertEquals(3, dataStore.getFeatureSource(pointsTypeName)
                    .getFeatures().size());

            tx.commit();

            // #of features after commit on a different store instance
            assertEquals(2, dataStore.getFeatureSource(pointsTypeName)
                    .getFeatures().size());
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            tx.close();
        }

        assertEquals(2, points.getFeatures().size());
        assertEquals(0, points.getFeatures(filter).size());
    }

}
