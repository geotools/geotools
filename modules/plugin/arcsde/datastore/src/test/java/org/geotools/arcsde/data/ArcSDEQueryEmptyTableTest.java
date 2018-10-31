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

import static org.geotools.filter.text.ecql.ECQL.toFilter;

import com.esri.sde.sdk.client.SeVersion;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.AutoCommitVersionHandler;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Test suite for the {@link ArcSDEQuery} query wrapper
 *
 * @author Gabriel Roldan
 */
public class ArcSDEQueryEmptyTableTest {

    private static TestData testData;

    /** do not access it directly, use {@link #createFilteringQuery()} */
    private ArcSDEQuery queryFiltered;

    private ArcSDEDataStore dstore;

    private String typeName;

    private Query filteringQuery;

    private SimpleFeatureType ftype;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        testData = new TestData();
        testData.setUp();

        testData.createTempTableEmptyExtent();
    }

    @AfterClass
    public static void oneTimeTearDown() {
        boolean cleanTestTable = false;
        boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
    }

    /**
     * loads {@code test-data/testparams.properties} into a Properties object, which is used to
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
    }

    @After
    public void tearDown() throws Exception {
        try {
            this.queryFiltered.close();
        } catch (Exception e) {
            // no-op
        }
        this.queryFiltered = null;
    }

    @Test
    public void testFilterLayerWithEmptyExtent() throws Exception {
        ISession session = dstore.getSession(Transaction.AUTO_COMMIT);
        FeatureTypeInfo fti = ArcSDEAdapter.fetchSchema(typeName, null, session);

        Filter filter = toFilter("BBOX(SHAPE, 49, -4, 60, 1)");
        filteringQuery = new Query(typeName, filter);
        ArcSDEQuery q =
                ArcSDEQuery.createQuery(
                        session,
                        ftype,
                        filteringQuery,
                        fti.getFidStrategy(),
                        new AutoCommitVersionHandler(SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME));
        int calculated;
        try {
            calculated = q.calculateResultCount();
        } finally {
            q.session.dispose();
            q.close();
        }
        Assert.assertEquals(0, calculated);
    }
}
