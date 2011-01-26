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
 *
 */
package org.geotools.arcsde.data;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.arcsde.data.ArcSDEQuery.FilterSet;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.arcsde.versioning.AutoCommitVersionHandler;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.BBOX;

import com.esri.sde.sdk.client.SeVersion;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Test suite for the {@link ArcSDEQuery} query wrapper
 * 
 * @author Gabriel Roldan
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *         /org/geotools/arcsde/data/ArcSDEQueryTest.java $
 * @version $Revision: 1.9 $
 */
public class ArcSDEQueryTest {

    private static TestData testData;

    /**
     * do not access it directly, use {@link #getQueryAll()}
     */
    private ArcSDEQuery _queryAll;

    /**
     * do not access it directly, use {@link #createFilteringQuery()}
     */
    private ArcSDEQuery queryFiltered;

    private ArcSDEDataStore dstore;

    private String typeName;

    private Query filteringQuery;

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private SimpleFeatureType ftype;

    private static final int FILTERING_COUNT = 3;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new TestData();
        testData.setUp();

        final boolean insertTestData = true;
        testData.createTempTable(insertTestData);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    /**
     * loads {@code test-data/testparams.properties} into a Properties object, wich is used to
     * obtain test tables names and is used as parameter to find the DataStore
     */
    @Before
    public void setUp() throws Exception {
        if (testData == null) {
            oneTimeSetUp();
        }
        dstore = testData.getDataStore();
        typeName = testData.getTempTableName();
        this.ftype = dstore.getSchema(typeName);

        // grab some fids
        SimpleFeatureSource source = dstore.getFeatureSource(typeName);
        SimpleFeatureCollection features = source.getFeatures();
        SimpleFeatureIterator iterator = features.features();
        List<FeatureId> fids = new ArrayList<FeatureId>();
        for (int i = 0; i < FILTERING_COUNT; i++) {
            fids.add(ff.featureId(iterator.next().getID()));
        }
        iterator.close();
        Id filter = ff.id(new HashSet<FeatureId>(fids));
        filteringQuery = new Query(typeName, filter);
    }

    @After
    public void tearDown() throws Exception {
        try {
            this._queryAll.close();
        } catch (Exception e) {
            // no-op
        }
        try {
            this.queryFiltered.close();
        } catch (Exception e) {
            // no-op
        }
        this._queryAll = null;
        this.queryFiltered = null;
    }

    /**
     * Filters are separated into backend supported and unsupported filters. Once split they should
     * be simplified to avoid silly filters like {@code 1 = 1 AND 1 = 1}
     * 
     * @throws IOException
     * @throws CQLException
     */
    @Test
    public void testSimplifiesFilters() throws IOException, CQLException {

        Filter filter = CQL
                .toFilter("STRING_COL = strConcat('string', STRING_COL) AND STRING_COL > 'String2' AND BBOX(SHAPE, 10.0,20.0,30.0,40.0)");
        filteringQuery = new Query(typeName, filter);
        // filteringQuery based on the above filter...
        ArcSDEQuery sdeQuery = createFilteringQuery();

        FilterSet filters;
        try {
            filters = sdeQuery.getFilters();
        } finally {
            sdeQuery.session.dispose();
            sdeQuery.close();
        }
        Filter geometryFilter = filters.getGeometryFilter();
        Filter sqlFilter = filters.getSqlFilter();
        Filter unsupportedFilter = filters.getUnsupportedFilter();

        System.out.println("geom: " + geometryFilter + ", sql: " + sqlFilter + ", unsupp: "
                + unsupportedFilter);

        assertTrue(geometryFilter instanceof BBOX);
        assertTrue(sqlFilter instanceof PropertyIsGreaterThan);
        assertTrue(unsupportedFilter instanceof PropertyIsEqualTo);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // @id = 'DELETEME.1' AND STRING_COL = 'test'
        filter = ff.and(ff.id(Collections.singleton(ff.featureId(typeName + ".1"))),
                ff.equals(ff.property("STRING_COL"), ff.literal("test")));

        filteringQuery = new Query(typeName, filter);
        // filteringQuery based on the above filter...
        sdeQuery = createFilteringQuery();

        try {
            filters = sdeQuery.getFilters();
        } finally {
            sdeQuery.session.dispose();
            sdeQuery.close();
        }
        geometryFilter = filters.getGeometryFilter();
        sqlFilter = filters.getSqlFilter();
        unsupportedFilter = filters.getUnsupportedFilter();

        System.out.println("geom: " + geometryFilter + ", sql: " + sqlFilter + ", unsupp: "
                + unsupportedFilter);

        Assert.assertEquals(Filter.INCLUDE, geometryFilter);
        assertTrue(String.valueOf(sqlFilter), sqlFilter instanceof And);
        Assert.assertEquals(Filter.INCLUDE, unsupportedFilter);

        // AND( @id = 'DELETEME.1' )
        List<Filter> singleAnded = Collections.singletonList((Filter) ff.id(Collections
                .singleton(ff.featureId(typeName + ".1"))));
        filter = ff.and(singleAnded);

        filteringQuery = new Query(typeName, filter);
        // filteringQuery based on the above filter...
        sdeQuery = createFilteringQuery();

        try {
            filters = sdeQuery.getFilters();
        } finally {
            sdeQuery.session.dispose();
            sdeQuery.close();
        }
        geometryFilter = filters.getGeometryFilter();
        sqlFilter = filters.getSqlFilter();
        unsupportedFilter = filters.getUnsupportedFilter();

        System.out.println("geom: " + geometryFilter + ", sql: " + sqlFilter + ", unsupp: "
                + unsupportedFilter);

        // this one should have been simplified
        assertTrue(sqlFilter instanceof Id);
        Assert.assertEquals(Filter.INCLUDE, geometryFilter);
        Assert.assertEquals(Filter.INCLUDE, unsupportedFilter);
    }

    @Test
    public void testWipesOutInvalidFids() throws IOException {
        final String typeName = this.typeName;
        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId(typeName + ".1"));
        ids.add(ff.featureId(typeName + ".2"));
        // some non valid ones...
        ids.add(ff.featureId(typeName + ".a"));
        ids.add(ff.featureId("states_.1"));

        Filter filter = ff.id(ids);
        filteringQuery = new Query(typeName, filter);
        // filteringQuery based on the above filter...
        ArcSDEQuery sdeQuery = createFilteringQuery();

        FilterSet filters;
        try {
            filters = sdeQuery.getFilters();
        } finally {
            sdeQuery.session.dispose();
            sdeQuery.close();
        }
        Filter sqlFilter = filters.getSqlFilter();
        assertTrue(sqlFilter instanceof Id);
        Id id = (Id) sqlFilter;
        Assert.assertEquals(2, id.getIDs().size());
        Set<Identifier> validFids = new HashSet<Identifier>();
        validFids.add(ff.featureId(typeName + ".1"));
        validFids.add(ff.featureId(typeName + ".2"));
        Id expected = ff.id(validFids);
        Assert.assertEquals(expected, id);
    }

    /**
     * Query specifies no request properties, then all properties should be fetch
     * 
     * @throws Exception
     */
    @Test
    public void testGetSchemaDefaultQuery() throws Exception {
        String[] requestProperties = {};

        Filter filter = Filter.INCLUDE;

        SimpleFeatureType resultingSchema = testGetSchema(requestProperties, filter);

        SimpleFeatureType fullSchema = dstore.getSchema(testData.getTempTableName());
        Assert.assertEquals(fullSchema, resultingSchema);
    }

    /**
     * The query is fully supported, the requested properties does not contain a property that is
     * needed for the filter evaluation, then the resulting schema should not contain the non
     * requested property neither
     * 
     * @throws Exception
     */
    @Test
    public void testGetSchemaSupportedFilterPropertySingleRequestProperty() throws Exception {
        String[] requestProperties = { "INT32_COL" };

        Filter filter = ECQL
                .toFilter("INTERSECTS(SHAPE, POLYGON((-1 -1, -1 0, 0 0, 0 -1, -1 -1)) )");

        SimpleFeatureType resultingSchema = testGetSchema(requestProperties, filter);

        Assert.assertEquals(1, resultingSchema.getAttributeCount());
        Assert.assertEquals("INT32_COL", resultingSchema.getAttributeDescriptors().get(0)
                .getLocalName());
    }

    /**
     * Query filter references a property name inside an unsupported filter/expression, then the
     * referenced property should be part of the resulting schema even if the query didn't requested
     * it, so that the filter/expression can be evaluated at runtime.
     * 
     * @throws Exception
     */
    @Test
    public void testGetSchemaUnsupportedFilterProperty() throws Exception {
        String[] requestProperties = { "SHAPE" };

        Filter filter = ECQL.toFilter("Min(INT32_COL, 5) = 5");// we don't support the Min function
        // natively

        SimpleFeatureType resultingSchema = testGetSchema(requestProperties, filter);

        Assert.assertEquals(2, resultingSchema.getAttributeCount());
        assertNotNull(resultingSchema.getDescriptor("SHAPE"));
        assertNotNull(resultingSchema.getDescriptor("INT32_COL"));

    }

    private SimpleFeatureType testGetSchema(String[] requestProperties, Filter filter)
            throws Exception {

        String typeName = testData.getTempTableName();
        SimpleFeatureType fullSchema = dstore.getSchema(typeName);
        assertNotNull(fullSchema.getDescriptor("SHAPE"));// just a safety check
        assertNotNull(fullSchema.getDescriptor("INT32_COL"));

        Query query = new Query(typeName, filter, requestProperties);
        FIDReader fidReader = new FIDReader.SdeManagedFidReader(typeName, "rowid");
        ArcSdeVersionHandler versioningHandler = ArcSdeVersionHandler.NONVERSIONED_HANDLER;

        ArcSDEQuery sdeQuery;
        ISession session = dstore.getSession(Transaction.AUTO_COMMIT);
        try {
            sdeQuery = ArcSDEQuery.createQuery(session, fullSchema, query, fidReader,
                    versioningHandler);
        } finally {
            session.dispose();
        }
        SimpleFeatureType schema = sdeQuery.getSchema();
        assertNotNull(schema);
        return schema;
    }

    private ArcSDEQuery getQueryAll() throws IOException {
        ISession session = dstore.getSession(Transaction.AUTO_COMMIT);
        this._queryAll = ArcSDEQuery.createQuery(session, ftype, Query.ALL, FIDReader.NULL_READER,
                ArcSdeVersionHandler.NONVERSIONED_HANDLER);
        return this._queryAll;
    }

    private ArcSDEQuery createFilteringQuery() throws IOException {
        ISession session = dstore.getSession(Transaction.AUTO_COMMIT);
        FeatureTypeInfo fti = ArcSDEAdapter.fetchSchema(typeName, null, session);
        this.queryFiltered = ArcSDEQuery.createQuery(session, ftype, filteringQuery, fti
                .getFidStrategy(), new AutoCommitVersionHandler(
                SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME));
        return this.queryFiltered;
    }

    @Test
    public void testClose() throws IOException {
        ArcSDEQuery queryAll = getQueryAll();
        assertNotNull(queryAll.session);

        queryAll.execute();

        assertNotNull(queryAll.session);

        // should nevel do this, just to assert it is
        // not closed by returned to the pool
        ISession session = queryAll.session;

        queryAll.close();

        assertNotNull(queryAll.session);
        assertFalse(session.isClosed());

        session.dispose();
    }

    @Test
    public void testFetch() throws IOException {
        ArcSDEQuery queryAll = getQueryAll();
        try {
            queryAll.fetch();
            fail("fetch without calling execute");
        } catch (IllegalStateException e) {
            // ok
        }

        queryAll.execute();
        assertNotNull(queryAll.fetch());

        queryAll.close();
        try {
            queryAll.fetch();
            fail("fetch after close!");
        } catch (IllegalStateException e) {
            // ok
        }

        queryAll.session.dispose();
    }

    @Test
    public void testCalculateResultCountAll() throws Exception {
        SimpleFeatureCollection features = dstore.getFeatureSource(typeName).getFeatures();
        SimpleFeatureIterator reader = features.features();
        int read = 0;
        try {
            while (reader.hasNext()) {
                reader.next();
                read++;
            }
        } finally {
            reader.close();
        }
        ArcSDEQuery q = getQueryAll();
        int calculated;
        try {
            calculated = q.calculateResultCount();
        } finally {
            q.session.dispose();
            q.close();
        }
        Assert.assertEquals(read, calculated);
    }

    @Test
    public void testCalculateResultCountNonSpatialFilter() throws Exception {
        ArcSDEQuery q = createFilteringQuery();
        int calculated;
        try {
            calculated = q.calculateResultCount();
        } finally {
            q.session.dispose();
            q.close();
        }
        Assert.assertEquals(FILTERING_COUNT, calculated);
    }

    @Test
    public void testCalculateResultCountSpatialFilter() throws Exception {
        ISession session = dstore.getSession(Transaction.AUTO_COMMIT);
        FeatureTypeInfo fti = ArcSDEAdapter.fetchSchema(typeName, null, session);

        // same filter than ArcSDEJavaApiTest.testCalculateCountSpatialFilter
        Filter filter = CQL.toFilter("BBOX(SHAPE, -180, -90, -170, -80)");
        filteringQuery = new Query(typeName, filter);
        ArcSDEQuery q = ArcSDEQuery.createQuery(session, ftype, filteringQuery, fti
                .getFidStrategy(), new AutoCommitVersionHandler(
                SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME));
        int calculated;
        try {
            calculated = q.calculateResultCount();
        } finally {
            q.session.dispose();
            q.close();
        }
        Assert.assertEquals(2, calculated);
    }

    @Test
    public void testCalculateResultCountMixedFilter() throws Exception {
        ISession session = dstore.getSession(Transaction.AUTO_COMMIT);
        FeatureTypeInfo fti = ArcSDEAdapter.fetchSchema(typeName, null, session);

        Filter filter = CQL.toFilter("INT32_COL < 5 AND BBOX(SHAPE, -180, -90, -170, -80)");
        filteringQuery = new Query(typeName, filter);
        ArcSDEQuery q = ArcSDEQuery.createQuery(session, ftype, filteringQuery, fti
                .getFidStrategy(), new AutoCommitVersionHandler(
                SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME));
        int calculated;
        try {
            calculated = q.calculateResultCount();
        } finally {
            q.session.dispose();
            q.close();
        }
        Assert.assertEquals(1, calculated);
    }

    @Test
    public void testCalculateQueryExtent() throws Exception {
        {
            SimpleFeatureCollection features;
            features = dstore.getFeatureSource(typeName).getFeatures();
            SimpleFeatureIterator reader = features.features();
            SimpleFeatureType featureType = features.getSchema();
            GeometryDescriptor defaultGeometry = featureType.getGeometryDescriptor();
            ReferencedEnvelope real = new ReferencedEnvelope(
                    defaultGeometry.getCoordinateReferenceSystem());
            try {
                while (reader.hasNext()) {
                    real.include(reader.next().getBounds());
                }
            } finally {
                reader.close();
            }

            // TODO: make calculateQueryExtent to return ReferencedEnvelope
            ArcSDEQuery queryAll = getQueryAll();
            Envelope actual;
            try {
                actual = queryAll.calculateQueryExtent();
            } finally {
                queryAll.close();
            }
            Envelope expected = new Envelope(real);
            assertNotNull(actual);
            assertEquals(expected, actual);

        }
        {
            FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
            featureReader = dstore.getFeatureReader(filteringQuery, Transaction.AUTO_COMMIT);
            ReferencedEnvelope real = new ReferencedEnvelope();
            try {
                while (featureReader.hasNext()) {
                    real.include(featureReader.next().getBounds());
                }
            } finally {
                featureReader.close();
            }

            ArcSDEQuery queryFiltered = createFilteringQuery();
            Envelope actual;
            try {
                actual = queryFiltered.calculateQueryExtent();
            } finally {
                queryFiltered.close();
            }
            assertNotNull(actual);
            Envelope expected = new Envelope(real);
            assertEquals(expected, actual);
        }
    }

    private void assertEquals(Envelope e1, Envelope e2) {
        final double tolerance = 1.0E-5;
        Assert.assertEquals(e1.getMinX(), e2.getMinX(), tolerance);
        Assert.assertEquals(e1.getMinY(), e2.getMinY(), tolerance);
        Assert.assertEquals(e1.getMaxX(), e2.getMaxX(), tolerance);
        Assert.assertEquals(e1.getMaxY(), e2.getMaxY(), tolerance);
    }
}
