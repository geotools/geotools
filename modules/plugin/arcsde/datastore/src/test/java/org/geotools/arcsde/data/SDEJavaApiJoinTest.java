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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;

import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.SdeRow;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 * ArcSDEDAtaStore test case for a master-child joining
 * <p>
 * This test will create an sde layer (table + spatial table) as master and a business table as
 * child:
 * 
 * <pre>
 * &lt;code&gt;
 *  -----------------------------------------------
 *  |            GT_SDE_TEST_MASTER               |
 *  -----------------------------------------------
 *  |  ID(int)  | NAME (string)  | SHAPE (Point)  |
 *  -----------------------------------------------
 *  |     1     |   name1        |  POINT(1, 1)   |
 *  -----------------------------------------------
 *  |     2     |   name2        |  POINT(2, 2)   |
 *  -----------------------------------------------
 *  |     3     |   name3        |  POINT(3, 3)   |
 *  -----------------------------------------------
 * 
 *  ---------------------------------------------------------------------
 *  |                     GT_SDE_TEST_CHILD                             |
 *  ---------------------------------------------------------------------
 *  | ID(int)   | MASTER_ID      | NAME (string)  | DESCRIPTION(string  |
 *  ---------------------------------------------------------------------
 *  |    1      |      1         |   child1       |    description1     |
 *  ---------------------------------------------------------------------
 *  |    2      |      2         |   child2       |    description2     |
 *  ---------------------------------------------------------------------
 *  |    3      |      2         |   child3       |    description3     |
 *  ---------------------------------------------------------------------
 *  |    4      |      3         |   child4       |    description4     |
 *  ---------------------------------------------------------------------
 *  |    5      |      3         |   child5       |    description5     |
 *  ---------------------------------------------------------------------
 *  |    6      |      3         |   child6       |    description6     |
 *  ---------------------------------------------------------------------
 * &lt;/code&gt;
 * &lt;/re&gt;
 * 
 * </p>
 * <p>
 * The following are rules that may help you in correctly specifying an SQL query that will work
 * with the ArcSDE Java API. This rules was collected empirically based on some of the tests of this
 * test suite. Be aware that ArcSDE Java API only supports &quot;queries&quot; of the following
 * form: <code>
 * SELECT &lt;list of qualified column names&gt; 
 *  FROM &lt;list of qualified table names&gt; 
 *  WHERE &lt;any where clause supported by the RDBMS&gt; 
 *  [ORDER BY &lt;qualified column names&gt;]
 * </code> Rules to create SQL QUERIES:
 * <ul>
 * <li>
 * Use full qualified table names. Queries that usually would work against the underlying RDBMS will
 * not work through the ArcSDE Java API if you do not fully qualify table names.
 * <li>
 * Do not use table aliases, or SHAPE field is fetched as int instead of as geometry.
 * <li>
 * Specifying a GROUP BY clause seems incompatible with using the SHAPE field. If you specify a
 * GROUP BY clause, ArcSDE will return the plain SHAPE field (int) instead of a geometry.
 * <li>
 * And the &lt;strong&gt;most important&lt;/strong&gt; one: &lt;strong&gt;SET THE SPATIAL COLUMN AS
 * THE LAST ONE&lt;/strong&gt;. This is most likely a bug in the ArcSDE Java API, since if you do
 * not set the shape field as the last one in the select items list an IndexOutOfBoundsException is
 * thrown by <code>
 * SeRow.fetch()
 * </code>
 * </ul>
 * </p>
 * 
 * &#064;author Gabriel Roldan, Axios Engineering &#064;source $URL:
 * http://gtsvn.refractions.net/branches
 * /2.5.x/modules/plugin/arcsde/datastore/src/test/java/org/geotools
 * /arcsde/data/SDEJavaApiJoinTest.java $ &#064;version $Id: SDEJavaApiJoinTest.java 31903
 * 2008-11-22 20:44:25Z groldan $ &#064;since 2.3.x
 * 
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/test/java/org
 *         /geotools/arcsde/data/SDEJavaApiJoinTest.java $
 */
public class SDEJavaApiJoinTest {

    /** Helper class that provides config loading and test data for unit tests */
    private static TestData testData;

    /** an ArcSDEDataStore created on setUp() to run tests against */
    private ArcSDEDataStore store;

    /**
     * Initialization code for the whole test suite
     * 
     * @throws IOException
     * @throws SeException
     * @throws FactoryException
     * @throws NoSuchAuthorityCodeException
     * @throws UnavailableConnectionException
     */
    @BeforeClass
    public static void oneTimeSetUp() throws IOException, SeException,
            NoSuchAuthorityCodeException, FactoryException, UnavailableConnectionException {
        testData = new TestData();
        testData.setUp();

        ISession session = testData.getConnectionPool().getSession();
        try {
            InProcessViewSupportTestData.setUp(session, testData);
        } finally {
            session.dispose();
        }
    }

    /**
     * Tear down code for the whole suite
     */
    @AfterClass
    public static void oneTimeTearDown() {
        final boolean cleanTestTable = true;
        final boolean cleanPool = true;
        testData.tearDown(cleanTestTable, cleanPool);
        testData = null;
    }

    /**
     * loads {@code testData/testparams.properties} into a Properties object, wich is used to obtain
     * test tables names and is used as parameter to find the DataStore
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        this.store = testData.getDataStore();
    }

    /*
     * public void testBorehole() throws Exception { final String typeName = "JoinedBoreholes";
     * final String definitionQuery = "SELECT " + " B.QS, B.NUMB, B.BSUFF, B.RT, B.BGS_ID, B.NAME,
     * B.ORIGINAL_N, B.CONFIDENTI, B.LENGTHC," + " G.LITHOSTRAT, G.LITHOLOGY_, G.BASE_BED_C,
     * G.DRILLED_DE, G.DRILLED__1, B.SHAPE" + " FROM SCO.LOUGHBOROUGH_BORES B,
     * SCO.LOUGHBOROUGH_BORE_GEOL G" + " WHERE (B.QS = G.QS AND B.NUMB = G.NUMB AND B.BSUFF =
     * G.BSUFF AND B.RT = G.RT)" + " ORDER BY B.QS, B.RT, B.NUMB, B.BSUFF";
     * 
     * try { store.registerView(typeName, definitionQuery); } catch (Exception e) {
     * e.printStackTrace(); throw e; }
     * 
     * SimpleFeatureType type = (SimpleFeatureType) store.getSchema(typeName); assertNotNull(type);
     * 
     * SimpleFeatureSource fs = store.getFeatureSource(typeName); assertNotNull(fs); int count =
     * fs.getCount(Query.ALL); final int expected = 16479; assertEquals(expected, count); }
     */

    /**
     * Assert that the datastore complains on views with non supported features
     */
    @Test
    public void testRegisterIllegalView() throws IOException {
        final String typeName = "badQuery";
        String plainSql;
        plainSql = "(SELECT * FROM mytable) UNION (SELECT * FROM mytable2 WHERE mytable2.col = 9)";
        SelectBody select;
        try {
            select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSql);
            fail("should complain on union");
        } catch (UnsupportedOperationException e) {
            // OK
        }
        plainSql = "SELECT * FROM t1 INNER JOIN t2 ON t1.id = t2.parent_id";
        try {
            select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSql);
            store.registerView(typeName, (PlainSelect) select);
            fail("should complain on join");
        } catch (UnsupportedOperationException e) {
            // OK
        }
        plainSql = "SELECT f1,f2,f3 FROM t1 GROUP BY f1,f2";
        try {
            select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSql);
            store.registerView(typeName, (PlainSelect) select);
            fail("should complain on group by");
        } catch (UnsupportedOperationException e) {
            // OK
        }
        /*
         * Looks like jsqlparser is not parsing the INTO directive plainSql =
         * "SELECT f1,f2 INTO TEMP FROM t1"; try{ store.registerView(typeName, plainSql);
         * fail("should complain on into"); }catch(UnsupportedOperationException e){ //OK }
         */
        plainSql = "SELECT f1,f2,f3 FROM t1 LIMIT 10";
        try {
            select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSql);
            store.registerView(typeName, (PlainSelect) select);
            fail("should complain on limit");
        } catch (UnsupportedOperationException e) {
            // OK
        }
    }

    /**
     * Fail if tried to register the same view name more than once
     */
    @Test
    public void testRegisterDuplicateViewName() throws IOException {
        final String plainSQL = InProcessViewSupportTestData.masterChildSql;

        SelectBody select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSQL);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);
        try {
            store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);
            fail("Expected IAE on duplicate view name");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testRegisterViewListedInGetTypeNames() throws IOException {
        final String plainSQL = InProcessViewSupportTestData.masterChildSql;

        SelectBody select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSQL);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        List<String> publishedTypeNames = Arrays.asList(store.getTypeNames());
        assertTrue(publishedTypeNames.contains(InProcessViewSupportTestData.typeName));
    }

    @Test
    public void testRegisterViewBuildsCorrectFeatureType() throws IOException {
        final String plainSQL = "SELECT " + InProcessViewSupportTestData.MASTER_UNQUALIFIED
                + ".*, " + InProcessViewSupportTestData.CHILD_UNQUALIFIED + ".DESCRIPTION FROM "
                + InProcessViewSupportTestData.MASTER_UNQUALIFIED + ", "
                + InProcessViewSupportTestData.CHILD_UNQUALIFIED + " WHERE "
                + InProcessViewSupportTestData.CHILD_UNQUALIFIED + ".MASTER_ID = "
                + InProcessViewSupportTestData.MASTER_UNQUALIFIED + ".ID";

        SelectBody select = ViewRegisteringFactoryHelper.parseSqlQuery(plainSQL);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        SimpleFeatureType type = store.getSchema(InProcessViewSupportTestData.typeName);
        assertNotNull(type);

        assertEquals(InProcessViewSupportTestData.typeName, type.getTypeName());

        assertEquals(4, type.getAttributeCount());
        List<AttributeDescriptor> atts = type.getAttributeDescriptors();
        assertEquals(4, atts.size());
        AttributeDescriptor att1 = (AttributeDescriptor) atts.get(0);
        AttributeDescriptor att2 = (AttributeDescriptor) atts.get(1);
        AttributeDescriptor att3 = (AttributeDescriptor) atts.get(2);
        AttributeDescriptor att4 = (AttributeDescriptor) atts.get(3);

        assertEquals("ID", att1.getLocalName());
        assertEquals("NAME", att2.getLocalName());
        assertEquals("SHAPE", att3.getLocalName());
        assertEquals("DESCRIPTION", att4.getLocalName());

        assertEquals(Integer.class, att1.getType().getBinding());
        assertEquals(String.class, att2.getType().getBinding());
        assertEquals(Point.class, att3.getType().getBinding());
        assertEquals(String.class, att4.getType().getBinding());
    }

    @Test
    public void testViewBounds() throws IOException {
        SelectBody select = ViewRegisteringFactoryHelper
                .parseSqlQuery(InProcessViewSupportTestData.masterChildSql);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        SimpleFeatureSource fs = store.getFeatureSource(InProcessViewSupportTestData.typeName);
        assertNotNull(fs);
        Envelope bounds = fs.getBounds();
        assertNotNull(bounds);
        assertEquals(1D, bounds.getMinX(), 0);
        assertEquals(1D, bounds.getMinY(), 0);
        assertEquals(3D, bounds.getMaxX(), 0);
        assertEquals(3D, bounds.getMaxY(), 0);
    }

    @Test
    public void testViewBoundsQuery() throws Exception {
        SelectBody select = ViewRegisteringFactoryHelper
                .parseSqlQuery(InProcessViewSupportTestData.masterChildSql);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        SimpleFeatureSource fs = store.getFeatureSource(InProcessViewSupportTestData.typeName);
        assertNotNull(fs);

        String cqlQuery = "NAME='name2' OR DESCRIPTION='description4'";
        Filter filter = CQL.toFilter(cqlQuery);
        Query query = new Query(InProcessViewSupportTestData.typeName, filter);

        Envelope bounds = fs.getBounds(query);

        assertNotNull(bounds);
        assertEquals(2D, bounds.getMinX(), 0);
        assertEquals(2D, bounds.getMinY(), 0);
        assertEquals(3D, bounds.getMaxX(), 0);
        assertEquals(3D, bounds.getMaxY(), 0);
    }

    @Test
    public void testViewCount() throws Exception {
        SelectBody select = ViewRegisteringFactoryHelper
                .parseSqlQuery(InProcessViewSupportTestData.masterChildSql);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        SimpleFeatureSource fs = store.getFeatureSource(InProcessViewSupportTestData.typeName);
        assertNotNull(fs);
        int count = fs.getCount(Query.ALL);
        final int expected = 7;
        assertEquals(expected, count);
    }

    @Test
    public void testViewCountQuery() throws Exception {
        SelectBody select = ViewRegisteringFactoryHelper
                .parseSqlQuery(InProcessViewSupportTestData.masterChildSql);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        SimpleFeatureSource fs = store.getFeatureSource(InProcessViewSupportTestData.typeName);
        assertNotNull(fs);

        String cqlQuery = "NAME='name2' OR DESCRIPTION='description4'";
        Filter filter = CQL.toFilter(cqlQuery);
        Query query = new Query(InProcessViewSupportTestData.typeName, filter);

        int count = fs.getCount(query);
        final int expected = 3;
        assertEquals(expected, count);
    }

    @Test
    public void testReadView() throws Exception {
        SelectBody select = ViewRegisteringFactoryHelper
                .parseSqlQuery(InProcessViewSupportTestData.masterChildSql);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        SimpleFeatureSource fs = store.getFeatureSource(InProcessViewSupportTestData.typeName);

        Query query = new Query(InProcessViewSupportTestData.typeName, Filter.INCLUDE, Query.ALL_PROPERTIES);
        SimpleFeatureCollection fc = fs.getFeatures(query);
        int fcCount = fc.size();
        int itCount = 0;
        final int expectedCount = 7;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = (SimpleFeature) it.next();
                assertNotNull(f);
                itCount++;
            }
        } finally {
            it.close();
        }
        assertEquals(expectedCount, fcCount);
        assertEquals(expectedCount, itCount);
    }

    @Test
    public void testQueryView() throws Exception {
        SelectBody select = ViewRegisteringFactoryHelper
                .parseSqlQuery(InProcessViewSupportTestData.masterChildSql);
        store.registerView(InProcessViewSupportTestData.typeName, (PlainSelect) select);

        String cqlQuery = "NAME='name2' OR DESCRIPTION='description6'";
        Filter filter = CQL.toFilter(cqlQuery);
        Query query = new Query(InProcessViewSupportTestData.typeName, filter);

        SimpleFeatureSource fs = store.getFeatureSource(InProcessViewSupportTestData.typeName);
        SimpleFeatureCollection fc = fs.getFeatures(query);
        int fcCount = fc.size();
        int itCount = 0;
        final int expectedCount = 3;
        SimpleFeatureIterator it = fc.features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                assertNotNull(f);
                itCount++;
            }
        } finally {
            it.close();
        }
        assertEquals(expectedCount, fcCount);
        assertEquals(expectedCount, itCount);
    }

    /**
     * Meant as example to be sure we're using the ArcSDE java api correctly
     * 
     * @throws Exception
     */
    @Test
    public void testApiOrderBy() throws Exception {
        ISession session = store.getSession(Transaction.AUTO_COMMIT);

        SeSqlConstruct sqlConstruct = new SeSqlConstruct();
        String[] tables = { InProcessViewSupportTestData.MASTER, InProcessViewSupportTestData.CHILD };
        sqlConstruct.setTables(tables);
        String where = InProcessViewSupportTestData.CHILD + ".MASTER_ID = "
                + InProcessViewSupportTestData.MASTER + ".ID";
        sqlConstruct.setWhere(where);

        // tricky part is that SHAPE column must always be the last one
        String[] propertyNames = { InProcessViewSupportTestData.MASTER + ".ID AS myid2",
                InProcessViewSupportTestData.MASTER + ".NAME AS MNAME",
                InProcessViewSupportTestData.CHILD + ".ID",
                InProcessViewSupportTestData.CHILD + ".NAME",
                InProcessViewSupportTestData.CHILD + ".DESCRIPTION",
                InProcessViewSupportTestData.MASTER + ".SHAPE" };
        final int shapeIndex = 5;
        final int expectedCount = 7;

        final SeQueryInfo queryInfo = new SeQueryInfo();
        queryInfo.setConstruct(sqlConstruct);
        queryInfo.setColumns(propertyNames);
        queryInfo.setByClause(" ORDER BY " + InProcessViewSupportTestData.CHILD + ".ID DESC");

        final Integer[] expectedChildIds = { new Integer(7), new Integer(6), new Integer(5),
                new Integer(4), new Integer(3), new Integer(2), new Integer(1) };

        // final int[] expectedShapeIndicators = { SeRow.SE_IS_NOT_NULL_VALUE, // child7
        // SeRow.SE_IS_REPEATED_FEATURE, // child6
        // SeRow.SE_IS_REPEATED_FEATURE, // child5
        // SeRow.SE_IS_REPEATED_FEATURE, // child4
        // SeRow.SE_IS_NOT_NULL_VALUE, // child3
        // SeRow.SE_IS_REPEATED_FEATURE, // child2
        // SeRow.SE_IS_NOT_NULL_VALUE // child1
        // };

        final SeQuery query = session.issue(new Command<SeQuery>() {

            @Override
            public SeQuery execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                SeQuery query = new SeQuery(connection);
                query.prepareQueryInfo(queryInfo);
                query.execute();
                return query;
            }
        });

        try {
            SdeRow row = session.fetch(query);

            int count = 0;
            final int childIdIndex = 2;
            while (row != null) {
                // duplicate shapes are not returned by arcsde.
                // in that case indicator has the value
                // SeRow.SE_IS_REPEATED_FEATURE
                int indicator = row.getIndicator(shapeIndex);
                Integer childId = row.getInteger(childIdIndex);
                assertEquals(expectedChildIds[count], childId);

                // this seems to be DB dependent, on SQLSever repeated shapes have
                // SeRow.SE_IS_REPEATED_FEATURE
                // indicator, on Oracle they're returned as SE_IS_NOT_NULL_VALUE
                // assertEquals("at index " + count, expectedShapeIndicators[count], indicator);

                if (SeRow.SE_IS_NOT_NULL_VALUE == indicator) {
                    Object shape = row.getObject(shapeIndex);
                    assertTrue(shape.getClass().getName(), shape instanceof SeShape);
                }

                count++;
                row = session.fetch(query);
            }
            assertEquals(expectedCount, count);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            session.dispose();
        }
    }

    /**
     * Using table alias leads to ArcSDE returning SHAPE id instead of SHAPE geometry.
     * 
     * @throws Exception
     *             TODO: revisit, this test hangs with SDE 9.2/Oracle9i at
     *             query.prepareQueryInfo(queryInfo);
     */
    @Test
    @Ignore
    public void testApiAlias() throws Exception {
        ISession session = store.getSession(Transaction.AUTO_COMMIT);

        SeSqlConstruct sqlConstruct = new SeSqlConstruct();
        String[] tables = { InProcessViewSupportTestData.MASTER + " MASTER",
                InProcessViewSupportTestData.CHILD + " CHILD" };
        sqlConstruct.setTables(tables);
        String where = "CHILD.MASTER_ID = MASTER.ID";
        sqlConstruct.setWhere(where);

        // tricky part is that SHAPE column must always be the last one
        String[] propertyNames = { "MASTER.ID", "CHILD.NAME", "MASTER.SHAPE" };

        final int shapeIndex = 2;
        final int expectedCount = 7;

        final SeQueryInfo queryInfo = new SeQueryInfo();
        queryInfo.setConstruct(sqlConstruct);
        queryInfo.setColumns(propertyNames);

        final SeQuery query = session.issue(new Command<SeQuery>() {
            @Override
            public SeQuery execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                SeQuery query = new SeQuery(connection);
                query.prepareQueryInfo(queryInfo);
                query.execute();
                return query;
            }
        });

        try {
            SdeRow row = session.fetch(query);
            int count = 0;
            while (row != null) {
                // we would expect SeShape being returned from shapeIndex, but
                // ArcSDE returns shape id
                if (SeRow.SE_IS_NOT_NULL_VALUE == row.getIndicator(shapeIndex)) {
                    Object shape = row.getObject(shapeIndex);
                    // assertTrue(shape.getClass().getName(), shape instanceof
                    // SeShape);
                    assertFalse(shape.getClass().getName(), shape instanceof SeShape);
                }
                count++;
                row = session.fetch(query);
            }
            assertEquals(expectedCount, count);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            session.dispose();
        }
    }

    /**
     * Meant as example to be sure we're using the ArcSDE java api correctly Nasty thing about group
     * by is that is seems that we cannot include/use the geometry column :(
     * 
     * @throws Exception
     */
    @Test
    public void testApiGroupBy() throws Exception {
        ISession session = store.getSession(Transaction.AUTO_COMMIT);

        SeSqlConstruct sqlConstruct = new SeSqlConstruct();
        String[] tables = { InProcessViewSupportTestData.MASTER, InProcessViewSupportTestData.CHILD };
        sqlConstruct.setTables(tables);
        String where = InProcessViewSupportTestData.CHILD + ".MASTER_ID = "
                + InProcessViewSupportTestData.MASTER + ".ID";
        sqlConstruct.setWhere(where);

        // tricky part is that SHAPE column must always be the last one
        String[] propertyNames = { InProcessViewSupportTestData.MASTER + ".ID",
                InProcessViewSupportTestData.CHILD + ".NAME" /*
                                                              * , MASTER + ".SHAPE"
                                                              */
        };

        // final int shapeIndex = 5;
        final int expectedCount = 6;

        final SeQueryInfo queryInfo = new SeQueryInfo();
        queryInfo.setConstruct(sqlConstruct);
        queryInfo.setColumns(propertyNames);

        String groupBy = InProcessViewSupportTestData.MASTER + ".ID, "
                + InProcessViewSupportTestData.CHILD + ".NAME, "
                + InProcessViewSupportTestData.MASTER + ".SHAPE";

        queryInfo.setByClause(" GROUP BY " + groupBy + " ORDER BY "
                + InProcessViewSupportTestData.CHILD + ".NAME DESC");

        // final int[] expectedShapeIndicators = { SeRow.SE_IS_NOT_NULL_VALUE,
        // // child6
        // // (&&
        // // child7)
        // SeRow.SE_IS_REPEATED_FEATURE, // child5
        // SeRow.SE_IS_REPEATED_FEATURE, // child4
        // SeRow.SE_IS_NOT_NULL_VALUE, // child3
        // SeRow.SE_IS_REPEATED_FEATURE, // child2
        // SeRow.SE_IS_NOT_NULL_VALUE // child1
        // };

        SeQuery query = session.issue(new Command<SeQuery>() {
            @Override
            public SeQuery execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                SeQuery query = new SeQuery(connection);
                query.prepareQueryInfo(queryInfo);
                query.execute();
                return query;
            }
        });
        try {
            SdeRow row = session.fetch(query);
            int count = 0;
            while (row != null) {
                // duplicate shapes are not returned by arcsde.
                // in that case indicator has the value
                // SeRow.SE_IS_REPEATED_FEATURE
                // int indicator = row.getIndicator(shapeIndex);

                // assertEquals("at index " + count,
                // expectedShapeIndicators[count], indicator);

                count++;
                row = session.fetch(query);
            }
            assertEquals(expectedCount, count);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            session.dispose();
        }
    }

    /**
     * Meant as example to be sure we're using the ArcSDE java api correctly. We can execute a plain
     * sql query, but shapes are not returned by ArcSDE. Instead, the SHAPE field contains the SHAPE
     * id, just like in the real business table.
     * 
     * @throws Exception
     */
    @Test
    public void testApiPlainSql() throws Exception {
        ISession session = store.getSession(Transaction.AUTO_COMMIT);

        final String plainQuery = "SELECT " + InProcessViewSupportTestData.MASTER + ".ID, "
                + InProcessViewSupportTestData.MASTER + ".SHAPE, "
                + InProcessViewSupportTestData.CHILD + ".NAME  FROM "
                + InProcessViewSupportTestData.MASTER + " INNER JOIN "
                + InProcessViewSupportTestData.CHILD + " ON " + InProcessViewSupportTestData.CHILD
                + ".MASTER_ID = " + InProcessViewSupportTestData.MASTER + ".ID";

        final int shapeIndex = 1;
        final int expectedCount = 7;
        final SeQuery query = session.issue(new Command<SeQuery>() {

            @Override
            public SeQuery execute(ISession session, SeConnection connection) throws SeException,
                    IOException {
                SeQuery query = new SeQuery(connection);
                query.prepareSql(plainQuery);
                query.execute();
                return query;
            }
        });

        try {
            SdeRow row = session.fetch(query);
            int count = 0;
            while (row != null) {
                Object shape = row.getObject(shapeIndex);
                assertTrue(shape instanceof Integer); // returns int instead
                // of shape
                count++;
                row = session.fetch(query);
            }
            assertEquals(expectedCount, count);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            session.dispose();
        }
    }

}
