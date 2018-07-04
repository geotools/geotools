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

import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeVersion;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.versioning.AutoCommitVersionHandler;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;

/**
 * Test suite for the {@link ArcSDEQuery} query wrapper
 *
 * @author Gabriel Roldan
 * @source $URL$
 *     http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/test/java
 *     /org/geotools/arcsde/data/ArcSDEQueryTest.java $
 * @version $Revision: 1.9 $
 */
public class ArcSDEQueryNewFeatureTest {

    private static TestData testData;

    private ArcSDEDataStore dstore;

    private String typeName;

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    private SimpleFeatureType ftype;

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
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testNewFeatures() throws Exception {
        FeatureType ft = this.dstore.getSchema(testData.getTempTableName());

        // Get current extent of all the features
        ReferencedEnvelope oldBounds =
                this.dstore.getFeatureSource(ft.getName().getLocalPart()).getBounds();
        System.out.println(oldBounds);

        ISession session = testData.getConnectionPool().getSession();

        FeatureTypeInfo fti = ArcSDEAdapter.fetchSchema(typeName, null, session);

        // Create bounding box filter that covers area where new feature will exist
        BBOX filter =
                ff.bbox(
                        ftype.getGeometryDescriptor().getLocalName(),
                        oldBounds.getLowerCorner().getCoordinate()[0] - 2.0,
                        -2.0,
                        oldBounds.getLowerCorner().getCoordinate()[0] + 2.0,
                        2.0,
                        oldBounds.getCoordinateReferenceSystem().getName().getCode());

        Query filteringQuery = new Query(typeName, filter);

        ArcSDEQuery queryFiltered =
                ArcSDEQuery.createQuery(
                        session,
                        ftype,
                        filteringQuery,
                        fti.getFidStrategy(),
                        new AutoCommitVersionHandler(SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME));

        // Output result count
        int resultCount = queryFiltered.calculateResultCount();
        System.out.println(String.format("Result count : %d ", resultCount));
        queryFiltered.close();

        //
        // Add new point outside existing bounding box
        //
        WKTReader reader = new WKTReader();
        Geometry[] geoms = new Geometry[1];
        geoms[0] =
                reader.read(
                        String.format(
                                "POINT(%f %f)",
                                oldBounds.getLowerCorner().getCoordinate()[0] - 1.0, 0.0));

        SeLayer layer = testData.getTempLayer(session);
        testData.insertData(geoms, layer, session);

        // Output bounding box of all the features
        ReferencedEnvelope newBounds =
                this.dstore.getFeatureSource(ft.getName().getLocalPart()).getBounds();
        System.out.println(newBounds);

        ArcSDEQuery newQueryFiltered =
                ArcSDEQuery.createQuery(
                        session,
                        ftype,
                        filteringQuery,
                        fti.getFidStrategy(),
                        new AutoCommitVersionHandler(SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME));

        // Output the updated result count
        int newResultCount = newQueryFiltered.calculateResultCount();
        System.out.println(String.format("New result count : %d", newResultCount));
        Assert.assertEquals(resultCount + 1, newResultCount);

        newQueryFiltered.close();
    }
}
