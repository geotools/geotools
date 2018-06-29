/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.sql.Connection;
import java.util.NoSuchElementException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Subtract;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @source $URL$ */
public abstract class JDBCFeatureSourceOnlineTest extends JDBCTestSupport {
    protected ContentFeatureSource featureSource;

    protected void connect() throws Exception {
        super.connect();

        featureSource = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
    }

    public void testSchema() throws Exception {
        SimpleFeatureType schema = featureSource.getSchema();
        assertEquals(tname("ft1"), schema.getTypeName());
        assertEquals(dataStore.getNamespaceURI(), schema.getName().getNamespaceURI());
        assertTrue(areCRSEqual(getWGS84(), schema.getCoordinateReferenceSystem()));

        assertEquals(4, schema.getAttributeCount());
        assertNotNull(schema.getDescriptor(aname("geometry")));
        assertNotNull(schema.getDescriptor(aname("intProperty")));
        assertNotNull(schema.getDescriptor(aname("stringProperty")));
        assertNotNull(schema.getDescriptor(aname("doubleProperty")));
    }

    public void testBounds() throws Exception {
        ReferencedEnvelope bounds = featureSource.getBounds();
        assertEquals(0l, Math.round(bounds.getMinX()));
        assertEquals(0l, Math.round(bounds.getMinY()));
        assertEquals(2l, Math.round(bounds.getMaxX()));
        assertEquals(2l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(getWGS84(), bounds.getCoordinateReferenceSystem()));
    }

    public void testBoundsWithQuery() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        ReferencedEnvelope bounds = featureSource.getBounds(query);
        assertEquals(1l, Math.round(bounds.getMinX()));
        assertEquals(1l, Math.round(bounds.getMinY()));
        assertEquals(1l, Math.round(bounds.getMaxX()));
        assertEquals(1l, Math.round(bounds.getMaxY()));

        assertTrue(areCRSEqual(getWGS84(), bounds.getCoordinateReferenceSystem()));
    }

    /**
     * Allows subclasses to use a axis order specific version of it
     *
     * @return
     * @throws FactoryException
     */
    protected CoordinateReferenceSystem getWGS84() throws FactoryException {
        return CRS.decode("EPSG:4326");
    }

    public void testCount() throws Exception {
        assertEquals(3, featureSource.getCount(Query.ALL));
    }

    public void testCountWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);
        assertEquals(1, featureSource.getCount(query));
    }

    public void testCountWithOffsetLimit() throws Exception {
        Query query = new Query();
        query.setStartIndex(1);
        query.setMaxFeatures(1);
        assertEquals(1, featureSource.getCount(query));
    }

    public void testGetFeatures() throws Exception {
        SimpleFeatureCollection features = featureSource.getFeatures();
        assertEquals(3, features.size());
    }

    public void testGetFeaturesWithFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());

            SimpleFeature feature = (SimpleFeature) iterator.next();
            assertEquals("one", feature.getAttribute(aname("stringProperty")));
            assertEquals(new Double(1.1), feature.getAttribute(aname("doubleProperty")));
        }
    }

    public void testGetFeaturesWithInvalidFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));

        // make sure a complaint related to the invalid filter is thrown here
        try (SimpleFeatureIterator fi = featureSource.getFeatures(f).features()) {
            fail("This query should have failed, it contains an invalid filter");
        } catch (Exception e) {
            e.printStackTrace();
            // fine
        }
    }

    public void testGetFeaturesWithLogicFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo property =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));
        BBOX bbox = ff.bbox(aname("geometry"), -20, -20, 20, 20, "EPSG:4326");
        And filter = ff.and(property, bbox);

        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());

            SimpleFeature feature = (SimpleFeature) iterator.next();
            assertEquals("one", feature.getAttribute(aname("stringProperty")));
            assertEquals(new Double(1.1), feature.getAttribute(aname("doubleProperty")));
        }
    }

    public void testCaseInsensitiveFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo sensitive =
                ff.equal(ff.property(aname("stringProperty")), ff.literal("OnE"), true);
        PropertyIsEqualTo insensitive =
                ff.equal(ff.property(aname("stringProperty")), ff.literal("OnE"), false);
        assertEquals(0, featureSource.getCount(new Query(null, sensitive)));
        assertEquals(1, featureSource.getCount(new Query(null, insensitive)));
    }

    public void testGetFeaturesWithQuery() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setPropertyNames(new String[] {aname("doubleProperty"), aname("intProperty")});
        query.setFilter(filter);

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertEquals(1, features.size());

        try (SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());

            SimpleFeature feature = (SimpleFeature) iterator.next();
            assertEquals(2, feature.getAttributeCount());

            assertEquals(new Double(1.1), feature.getAttribute(aname("doubleProperty")));
            assertNotNull(feature.getAttribute(aname("intProperty")));
        }
    }

    public void testGetFeaturesWithInvalidQuery() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        PropertyIsEqualTo f = ff.equals(ff.property("invalidAttribute"), ff.literal(5));

        // make sure a complaint related to the invalid filter is thrown here
        try (SimpleFeatureIterator fi = featureSource.getFeatures(new Query("ft1", f)).features()) {
            fail("This query should have failed, it contains an invalid filter");
        } catch (Exception e) {
            // e.printStackTrace();
            // fine
        }
    }

    public void testGetFeaturesWithSort() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        SortBy sort = ff.sort(aname("stringProperty"), SortOrder.ASCENDING);
        Query query = new Query();
        query.setSortBy(new SortBy[] {sort});

        SimpleFeatureCollection features = featureSource.getFeatures(query);
        assertEquals(3, features.size());

        SimpleFeature f;
        try (SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());

            f = (SimpleFeature) iterator.next();
            assertEquals("one", f.getAttribute(aname("stringProperty")));

            assertTrue(iterator.hasNext());
            f = (SimpleFeature) iterator.next();
            assertEquals("two", f.getAttribute(aname("stringProperty")));

            assertTrue(iterator.hasNext());
            f = (SimpleFeature) iterator.next();
            assertEquals("zero", f.getAttribute(aname("stringProperty")));
        }

        sort = ff.sort(aname("stringProperty"), SortOrder.DESCENDING);
        query.setSortBy(new SortBy[] {sort});
        features = featureSource.getFeatures(query);

        try (SimpleFeatureIterator iterator = features.features()) {
            assertTrue(iterator.hasNext());

            f = (SimpleFeature) iterator.next();
            assertEquals("zero", f.getAttribute(aname("stringProperty")));

            assertTrue(iterator.hasNext());
            f = (SimpleFeature) iterator.next();
            assertEquals("two", f.getAttribute(aname("stringProperty")));

            assertTrue(iterator.hasNext());
            f = (SimpleFeature) iterator.next();
            assertEquals("one", f.getAttribute(aname("stringProperty")));
        }
    }

    public void testGetFeaturesWithMax() throws Exception {
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setMaxFeatures(2);
        SimpleFeatureCollection features = featureSource.getFeatures(q);

        // check size
        assertEquals(2, features.size());

        // check actual iteration
        try (SimpleFeatureIterator it = features.features()) {
            int count = 0;
            ReferencedEnvelope env =
                    new ReferencedEnvelope(features.getSchema().getCoordinateReferenceSystem());
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                env.expandToInclude(ReferencedEnvelope.reference(f.getBounds()));
                count++;
            }
            assertEquals(2, count);
            assertTrue(areReferencedEnvelopesEqual(env, features.getBounds()));
        }
    }

    public void testGetFeaturesWithOffset() throws Exception {
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(
                new SortBy[] {
                    dataStore.getFilterFactory().sort(aname("intProperty"), SortOrder.ASCENDING)
                });
        q.setStartIndex(2);
        SimpleFeatureCollection features = featureSource.getFeatures(q);

        // check size
        assertEquals(1, features.size());

        // check actual iteration
        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            ReferencedEnvelope fe = ReferencedEnvelope.reference(f.getBounds());
            assertEquals(2, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertFalse(it.hasNext());
            // assertEquals(fe, features.getBounds());
            assertTrue(areReferencedEnvelopesEqual(fe, features.getBounds()));
        }
    }

    public void testGetFeaturesWithOffsetLimit() throws Exception {
        Query q = new Query(featureSource.getSchema().getTypeName());
        // no sorting, let's see if the database can use native one
        q.setStartIndex(1);
        q.setMaxFeatures(1);
        SimpleFeatureCollection features = featureSource.getFeatures(q);

        // check size
        assertEquals(1, features.size());

        // check actual iteration
        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            ReferencedEnvelope fe = ReferencedEnvelope.reference(f.getBounds());
            assertEquals(1, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertFalse(it.hasNext());
            // assertEquals(fe, features.getBounds());
            assertTrue(areReferencedEnvelopesEqual(fe, features.getBounds()));
        }
    }

    public void testGetFeaturesWithOffsetLimitAndPostFilter() throws Exception {
        Query q = new Query(featureSource.getSchema().getTypeName());
        // no sorting, let's see if the database can use native one
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equal(
                        ff.literal("one"),
                        ff.function("strToLowerCase", ff.property(aname("stringProperty"))),
                        true);
        q.setFilter(filter);
        q.setStartIndex(0);
        q.setMaxFeatures(1);
        SimpleFeatureCollection features = featureSource.getFeatures(q);

        // check size
        assertEquals(1, features.size());

        // check actual iteration
        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());
            SimpleFeature f = it.next();
            ReferencedEnvelope fe = ReferencedEnvelope.reference(f.getBounds());
            assertEquals(1, ((Number) f.getAttribute(aname("intProperty"))).intValue());
            assertFalse(it.hasNext());
            // assertEquals(fe, features.getBounds());
            assertTrue(areReferencedEnvelopesEqual(fe, features.getBounds()));
        }
    }

    /**
     * Makes sure the datastore works when the renderer uses the typical rendering hints
     *
     * @throws Exception
     */
    public void testRendererBehaviour() throws Exception {
        Query query = new Query(featureSource.getSchema().getTypeName());
        query.setHints(
                new Hints(
                        new Hints(
                                Hints.JTS_COORDINATE_SEQUENCE_FACTORY,
                                new LiteCoordinateSequenceFactory())));
        SimpleFeatureCollection fc = featureSource.getFeatures(query);
        try (SimpleFeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                fi.next();
            }
        }
    }

    public void testQueryCapabilitiesSort() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        QueryCapabilities caps = featureSource.getQueryCapabilities();

        // check we advertise support for sorting on basic attributes
        assertTrue(
                caps.supportsSorting(
                        new SortBy[] {ff.sort(aname("intProperty"), SortOrder.ASCENDING)}));
        assertTrue(
                caps.supportsSorting(
                        new SortBy[] {ff.sort(aname("stringProperty"), SortOrder.DESCENDING)}));
        assertTrue(
                caps.supportsSorting(
                        new SortBy[] {ff.sort(aname("doubleProperty"), SortOrder.ASCENDING)}));

        // but we cannot sort geometries
        assertFalse(
                caps.supportsSorting(
                        new SortBy[] {ff.sort(aname("geometry"), SortOrder.ASCENDING)}));
    }

    public void testQueryCapabilitiesReliableFid() throws Exception {
        QueryCapabilities caps = featureSource.getQueryCapabilities();
        // we have a primary key, right?
        assertTrue(caps.isReliableFIDSupported());
    }

    public void testNaturalSortingAsc() throws Exception {
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(new SortBy[] {SortBy.NATURAL_ORDER});
        try (SimpleFeatureIterator features = featureSource.getFeatures(q).features()) {
            String prevId = null;
            while (features.hasNext()) {
                String currId = features.next().getID();
                if (prevId != null) assertTrue(prevId.compareTo(currId) <= 0);
                prevId = currId;
            }
        }
    }

    public void testNaturalSortingdesc() throws Exception {
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setSortBy(new SortBy[] {SortBy.REVERSE_ORDER});
        try (SimpleFeatureIterator features = featureSource.getFeatures(q).features()) {
            String prevId = null;
            while (features.hasNext()) {
                String currId = features.next().getID();
                if (prevId != null) assertTrue(prevId.compareTo(currId) >= 0);
                prevId = currId;
            }
        }
    }

    public void testFeatureIteratorNextContract() throws Exception {
        try (SimpleFeatureIterator features = featureSource.getFeatures().features()) {
            // 1) non empty iterator, calling next() should just return the feature
            SimpleFeature f = features.next();
            assertNotNull(f);
        }
    }

    public void testFeatureIteratorEmptyContract() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("not_there"));
        try (SimpleFeatureIterator features = featureSource.getFeatures(filter).features()) {
            // 1) non empty iterator, calling next() should just return the feature
            SimpleFeature f = features.next();
            assertNotNull(f);
        } catch (NoSuchElementException e) {
            // ok
        }
    }

    public void testLikeFilter() throws Exception {
        FilterFactory2 ff = (FilterFactory2) dataStore.getFilterFactory();
        PropertyIsLike caseSensitiveLike =
                ff.like(ff.property(aname("stringProperty")), "Z*", "*", "?", "\\", true);
        PropertyIsLike caseInsensitiveLike =
                ff.like(ff.property(aname("stringProperty")), "Z*", "*", "?", "\\", false);
        PropertyIsLike caseInsensitiveLike2 =
                ff.like(ff.property(aname("stringProperty")), "z*", "*", "?", "\\", false);
        assertEquals(0, featureSource.getCount(new Query(null, caseSensitiveLike)));
        assertEquals(1, featureSource.getCount(new Query(null, caseInsensitiveLike)));
        assertEquals(1, featureSource.getCount(new Query(null, caseInsensitiveLike2)));
    }

    public void testConversionFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo f =
                ff.equals(
                        ff.property(aname("doubleProperty")),
                        ff.add(ff.property(aname("intProperty")), ff.literal("0.1")));
        assertEquals(1, featureSource.getCount(new Query(null, f)));
    }

    public void testNotFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.equal(ff.property(aname("stringProperty")), ff.literal("one"), true);
        f = ff.not(f);

        assertEquals(
                featureSource.getCount(Query.ALL) - 1, featureSource.getCount(new Query(null, f)));
    }

    public void testGeometryFactoryHint() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter =
                ff.equals(ff.property(aname("stringProperty")), ff.literal("one"));

        Query query = new Query();
        query.setFilter(filter);

        // check we're respecting the geometry factory hint
        GeometryFactory gf1 = new GeometryFactory();
        query.setHints(new Hints(Hints.JTS_GEOMETRY_FACTORY, gf1));
        SimpleFeature f1 = DataUtilities.first(featureSource.getFeatures(query));
        assertSame(gf1, ((Geometry) f1.getDefaultGeometry()).getFactory());

        // check we're respecting the geometry factory when changing it
        GeometryFactory gf2 = new GeometryFactory();
        query.setHints(new Hints(Hints.JTS_GEOMETRY_FACTORY, gf2));
        SimpleFeature f2 = DataUtilities.first(featureSource.getFeatures(query));
        assertSame(gf2, ((Geometry) f2.getDefaultGeometry()).getFactory());
    }

    public void testGetFeaturesWithArithmeticOpFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();

        Subtract sub = ff.subtract(ff.property(aname("doubleProperty")), ff.literal(0.1));
        PropertyIsEqualTo filter = ff.equals(ff.property(aname("intProperty")), sub);

        // this test is very dependent on the specific database, some db's will round, some won't
        // so just assert that something is returned
        assertTrue(featureSource.getCount(new Query(null, filter)) > 0);
    }

    public void testAcceptsVisitor() throws Exception {
        class TotalVisitor implements FeatureVisitor {
            int total = 0;

            public void visit(Feature feature) {
                total++;
            }
        }
        TotalVisitor visitor = new TotalVisitor();

        // initial test on Transaction.AUTO_COMMIT
        int count = featureSource.getCount(Query.ALL);
        featureSource.accepts(Query.ALL, visitor, null);
        assertEquals(count, visitor.total);
        visitor.total = 0; // reset

        // test on a transaction
        JDBCFeatureStore ft1 = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
        try (Transaction transaction = new DefaultTransaction()) {
            ft1.setTransaction(transaction);
            Connection connection = ft1.getDataStore().getConnection(ft1.getState());
            assertFalse("connection established", connection.isClosed());

            ft1.accepts(Query.ALL, visitor, null);

            assertFalse("connection maintained", connection.isClosed());
        }
    }
}
