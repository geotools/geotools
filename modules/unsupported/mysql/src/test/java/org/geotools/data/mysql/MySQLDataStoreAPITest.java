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
package org.geotools.data.mysql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DataTestCase;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * This class tests the MySQLDataStore API against the same tests as
 * MemoryDataStore.
 *
 * <p>
 * The test fixture is available in the shared DataTestCase, really the common
 * elements should move to a shared DataStoreAPITestCase.
 * </p>
 *
 * <p>
 * This class does require your own DataStore, it will create a table populated
 * with the Features from the test fixture, and run a test, and then remove
 * the table.
 * </p>
 *
 * <p>
 * Because of the nature of this testing process you cannot run these tests in
 * conjunction with another user, so they cannot be implemented against the
 * public server.
 * </p>
 *
 * <p>
 * A simple properties file has been constructed,
 * <code>fixture.properties</code>, which you may direct to your own potgis
 * database installation.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class MySQLDataStoreAPITest extends DataTestCase {
    private static final int LOCK_DURATION = 3600 * 1000; // one hour

    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.mysql");
    static boolean CHECK_TYPE = false;
    MySQLDataStore data;
    ManageableDataSource pool;
    String database;
    String victim = null; //"testGetFeatureWriterRemoveAll";

    /**
     * Constructor for MemoryDataStoreTest.
     *
     * @param test
     *
     * @throws AssertionError DOCUMENT ME!
     */
    public MySQLDataStoreAPITest(String test) {
        super(test);

        if ((victim != null) && !test.equals(victim)) {
            throw new AssertionError("test supressed " + test);
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        LOGGER.info("starting suite...");

        TestSuite suite = new TestSuite(MySQLDataStoreAPITest.class);
        LOGGER.info("made suite...");

        return suite;
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        PropertyResourceBundle resource;
        resource = new PropertyResourceBundle(this.getClass()
                                                  .getResourceAsStream("fixture.properties"));

        String namespace = resource.getString("namespace");
        String host = resource.getString("host");
        int port = Integer.parseInt(resource.getString("port"));
        String database = resource.getString("database");
        this.database = database;

        String user = resource.getString("user");
        String password = resource.getString("passwd");

        if (namespace.equals("http://www.geotools.org/data/postgis")) {
            throw new IllegalStateException(
                "The fixture.properties file needs to be configured for your own database");
        }

        pool = MySQLDataStoreFactory.getDefaultDataSource(host, user, password, port, database, 10,
                2, false);

        setUpRoadTable();
        setUpRiverTable();

        if (CHECK_TYPE) {
            checkTypesInDataBase();
            CHECK_TYPE = false; // just once
        }

        data = new MySQLDataStore(pool, null, getName());
        data.setFIDMapper("road", new TypedFIDMapper(new BasicFIDMapper("fid", 255, false), "road"));
        data.setFIDMapper("river",
            new TypedFIDMapper(new BasicFIDMapper("fid", 255, false), "river"));
        data.setFIDMapper("testset",
            new TypedFIDMapper(new BasicFIDMapper("gid", 255, true), "testset"));

        //
        // Update Fixture to reflect the actual data in the database
        // I am doing this because it
        updateRoadFeaturesFixture();
        updateRiverFeaturesFixture();
    }

    /**
     * This is a quick hack to have our fixture reflect the FIDs in the
     * database.
     *
     * <p>
     * When the dataStore learns how to preserve our FeatureIds this won't be
     * required.
     * </p>
     *
     * @throws Exception
     */
    protected void updateRoadFeaturesFixture() throws Exception {
        Connection conn = pool.getConnection();
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);

        ReferencedEnvelope bounds = new ReferencedEnvelope();

        try {
            SimpleFeature f;

            while (reader.hasNext()) {
                f = (SimpleFeature) reader.next();

                int index = ((Integer) f.getAttribute("id")).intValue() - 1;
                roadFeatures[index] = f;
                bounds.expandToInclude((Envelope) f.getBounds());
            }
        } finally {
            reader.close();
            conn.close();
        }

        if (!roadBounds.equals(bounds)) {
            System.out.println("warning! Database changed bounds()");
            System.out.println("was:" + roadBounds);
            System.out.println("now:" + bounds);
            roadBounds = bounds;
        }

        ReferencedEnvelope bounds12 = new ReferencedEnvelope();
        bounds12.expandToInclude((Envelope) roadFeatures[0].getBounds());
        bounds12.expandToInclude((Envelope) roadFeatures[1].getBounds());

        if (!rd12Bounds.equals(bounds12)) {
            System.out.println("warning! Database changed bounds of rd1 & rd2");
            System.out.println("was:" + rd12Bounds);
            System.out.println("now:" + bounds12);
            rd12Bounds = bounds12;
        }

        SimpleFeatureType schema = roadFeatures[0].getFeatureType();
        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        CompareFilter tFilter = ff.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        Expression rd1Literal = ff.createLiteralExpression("r1");
        tFilter.addLeftValue(rd1Literal);

        Expression rdNameAtt = ff.createAttributeExpression(schema, "name");
        tFilter.addRightValue(rdNameAtt);
        rd1Filter = tFilter;

        tFilter = ff.createCompareFilter(AbstractFilter.COMPARE_EQUALS);

        Expression rd2Literal = ff.createLiteralExpression("r2");
        tFilter.addLeftValue(rd2Literal);
        tFilter.addRightValue(rdNameAtt);
        rd2Filter = tFilter;
        rd12Filter = ff.or(rd2Filter, rd1Filter);
    }

    /**
     * This is a quick hack to have our fixture reflect the FIDs in the
     * database.
     *
     * <p>
     * When the dataStore learns how to preserve our FeatureIds this won't be
     * required.
     * </p>
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void updateRiverFeaturesFixture() throws Exception {
        Connection conn = pool.getConnection();
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader(new DefaultQuery("river", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);

        ReferencedEnvelope bounds = new ReferencedEnvelope();

        try {
            SimpleFeature f;

            while (reader.hasNext()) {
                f = reader.next();

                int index = ((Integer) f.getAttribute("id")).intValue() - 1;
                riverFeatures[index] = f;
                bounds.expandToInclude((Envelope)f.getBounds());
            }
        } finally {
            reader.close();
            conn.close();
        }

        if (!riverBounds.equals(bounds)) {
            System.out.println("warning! Database changed bounds of river");
            System.out.println("was:" + riverBounds);
            System.out.println("now:" + bounds);
            riverBounds = bounds;
        }

        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        CompareFilter tFilter = factory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        Expression rvLiteral = factory.createLiteralExpression("rv1");
        tFilter.addLeftValue(rvLiteral);

        SimpleFeatureType schema = riverFeatures[0].getFeatureType();
        Expression rvNameAtt = factory.createAttributeExpression(schema, "river");
        tFilter.addRightValue(rvNameAtt);
        rv1Filter = tFilter;
    }

    protected void checkTypesInDataBase() throws SQLException {
        Connection conn = pool.getConnection();

        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs =  //md.getTables( catalog, null, null, null );
                md.getTables(null, "public", "%", new String[] { "TABLE", });
            ResultSetMetaData rsmd = rs.getMetaData();
            int NUM = rsmd.getColumnCount();
            System.out.print(" ");

            for (int i = 1; i <= NUM; i++) {
                System.out.print(rsmd.getColumnName(i));
                System.out.flush();
                System.out.print(":");
                System.out.flush();
                System.out.print(rsmd.getColumnClassName(i));
                System.out.flush();

                if (i < NUM) {
                    System.out.print(",");
                    System.out.flush();
                }
            }

            System.out.println();

            while (rs.next()) {
                System.out.print(rs.getRow());
                System.out.print(":");
                System.out.flush();

                for (int i = 1; i <= NUM; i++) {
                    System.out.print(rsmd.getColumnName(i));
                    System.out.flush();
                    System.out.print("=");
                    System.out.flush();
                    System.out.print(rs.getString(i));
                    System.out.flush();

                    if (i < NUM) {
                        System.out.print(",");
                        System.out.flush();
                    }
                }

                System.out.println();
            }
        } finally {
            conn.close();
        }
    }

    protected void setUpRoadTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE road");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            s.execute(
                "CREATE TABLE road (fid varchar(255) PRIMARY KEY, id int, geom LINESTRING not null, name varchar(255), spatial index(geom) ) engine = myisam");

            for (int i = 0; i < roadFeatures.length; i++) {
                SimpleFeature f = roadFeatures[i];

                //strip out the road. 
                String fid = f.getID().substring("road.".length());
                String ql = "INSERT INTO road (fid,id,geom,name) VALUES (" + "'" + fid + "',"
                    + f.getAttribute("id") + "," + "GeometryFromText('"
                    + ((Geometry) f.getAttribute("geom")).toText() + "', 0 )," + "'"
                    + f.getAttribute("name") + "')";

                s.execute(ql);
            }
        } finally {
            conn.close();
        }
    }

    protected void killTestTables() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + database + "','road','geom')");
            s.execute("SELECT dropgeometrycolumn( '" + database + "','river','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE road");
            s.execute("DROP TABLE river");
        } catch (Exception ignore) {
        } finally {
            conn.close();
        }
    }

    protected void setUpRiverTable() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE river");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            // force a binary collation otherwise MySql will do case insensitive comparisons by default
            s.execute(
                "CREATE TABLE river(fid varchar(255) PRIMARY KEY, id int, geom MULTILINESTRING, river varchar(255) collate latin1_bin, flow double)");

            for (int i = 0; i < riverFeatures.length; i++) {
                SimpleFeature f = riverFeatures[i];
                String fid = f.getID().substring("river.".length());
                s.execute("INSERT INTO river (fid, id, geom, river, flow) VALUES (" + "'" + fid
                    + "'," + f.getAttribute("id") + "," + "GeometryFromText('"
                    + f.getAttribute("geom").toString() + "', 0 )," + "'" + f.getAttribute("river")
                    + "'," + f.getAttribute("flow") + ")");
            }
        } finally {
            conn.close();
        }
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        data = null;

        pool.close();
        super.tearDown();
    }

    public void testGetFeatureTypes() {
        try {
            String[] names = data.getTypeNames();
            assertTrue(contains(names, "road"));
            assertTrue(contains(names, "river"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("An IOException has been thrown!");
        }
    }

    boolean contains(Object[] array, Object expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(expected)) {
                return true;
            }
        }

        return false;
    }

    void assertContains(Object[] array, Object expected) {
        assertFalse(array == null);
        assertFalse(array.length == 0);
        assertNotNull(expected);

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(expected)) {
                return;
            }
        }

        fail("Contains " + expected);
    }

    /**
     * Like contain but based on match rather than equals
     *
     * @param array DOCUMENT ME!
     * @param expected DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean containsLax(SimpleFeature[] array, SimpleFeature expected) {
        if ((array == null) || (array.length == 0)) {
            return false;
        }

        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < array.length; i++) {
            if (match(array[i], expected)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compare based on attributes not getID allows comparison of Diff contents
     *
     * @param expected DOCUMENT ME!
     * @param actual DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean match(SimpleFeature expected, SimpleFeature actual) {
        SimpleFeatureType type = expected.getFeatureType();

        for (int i = 0; i < type.getAttributeCount(); i++) {
            Object av = actual.getAttribute(i);
            Object ev = expected.getAttribute(i);

            if ((av == null) && (ev != null)) {
                return false;
            } else if ((ev == null) && (av != null)) {
                return false;
            } else if (av instanceof Geometry && ev instanceof Geometry) {
                Geometry ag = (Geometry) av;
                Geometry eg = (Geometry) ev;

                if (!ag.equals(eg)) {
                    return false;
                }
            } else if (!av.equals(ev)) {
                return false;
            }
        }

        return true;
    }

    public void testGetSchemaRoad() throws IOException {
        SimpleFeatureType expected = roadType;
        SimpleFeatureType actual = data.getSchema("road");
        assertEquals("namespace", expected.getName().getNamespaceURI(), actual.getName().getNamespaceURI());
        assertEquals("typeName", expected.getTypeName(), actual.getTypeName());

        //assertEquals( "compare", 0, DataUtilities.compare( expected, actual ));
        assertEquals("attributeCount", expected.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);
            assertEquals("attribute " + expectedAttribute.getLocalName(), expectedAttribute,
                actualAttribute);
        }

        assertEquals(expected, actual);
    }

    public void testGetSchemaRiver() throws IOException {
        SimpleFeatureType expected = riverType;
        SimpleFeatureType actual = data.getSchema("river");
        assertEquals("namespace", expected.getName().getNamespaceURI(), actual.getName().getNamespaceURI());
        assertEquals("typeName", expected.getTypeName(), actual.getTypeName());

        //assertEquals( "compare", 0, DataUtilities.compare( expected, actual ));
        assertEquals("attributeCount", expected.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);
            assertEquals("attribute " + expectedAttribute.getLocalName(), expectedAttribute,
                actualAttribute);
        }

        assertEquals(expected, actual);
    }

    static public void assertEquals(String message, String expected, String actual) {
        if (expected == actual) {
            return;
        }

        assertNotNull(message, expected);
        assertNotNull(message, actual);

        if (!expected.equals(actual)) {
            fail(message + " expected:<" + expected + "> but was <" + actual + ">");
        }
    }

    void assertCovers(String msg, SimpleFeatureCollection c1, SimpleFeatureCollection c2) {
        if (c1 == c2) {
            return;
        }

        assertNotNull(msg, c1);
        assertNotNull(msg, c2);
        assertEquals(msg + " size", c1.size(), c2.size());

        SimpleFeature f;

        for (SimpleFeatureIterator i = c1.features(); i.hasNext();) {
            f = i.next();
            assertTrue(msg + " " + f.getID(), c2.contains(f));
        }
    }

    public  FeatureReader<SimpleFeatureType, SimpleFeature> reader(String typeName) throws IOException {
        SimpleFeatureType type = data.getSchema(typeName);

        return data.getFeatureReader(type, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> writer(String typeName) throws IOException {
        return data.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);
    }

    public void testGetFeatureReader() throws IOException, IllegalAttributeException {
        assertCovered(roadFeatures, reader("road"));
        assertEquals(3, count(reader("road")));
    }

    public void testGetFeatureReaderMutability() throws IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader("road");
        SimpleFeature feature;

        while (reader.hasNext()) {
            feature = (SimpleFeature) reader.next();
            feature.setAttribute("name", null);
        }

        reader.close();

        reader = reader("road");

        while (reader.hasNext()) {
            feature = (SimpleFeature) reader.next();
            assertNotNull(feature.getAttribute("name"));
        }

        reader.close();

        try {
            reader.next();
            fail("next should fail with an IOException");
        } catch (IOException expected) {
        }
    }

    public void testGetFeatureReaderConcurancy()
        throws NoSuchElementException, IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader1 = reader("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader2 = reader("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader3 = reader("river");

        SimpleFeature feature1;
        SimpleFeature feature2;
        SimpleFeature feature3;

        while (reader1.hasNext() || reader2.hasNext() || reader3.hasNext()) {
            assertTrue(contains(roadFeatures, reader1.next()));
            assertTrue(contains(roadFeatures, reader2.next()));

            if (reader3.hasNext()) {
                assertTrue(contains(riverFeatures, reader3.next()));
            }
        }

        try {
            reader1.next();
            fail("next should fail with an NoSuchElementException");
        } catch (NoSuchElementException expectedNoElement) {
            // this is new to me, I had expected an IOException
        }

        try {
            reader2.next();
            fail("next should fail with an NoSuchElementException");
        } catch (NoSuchElementException expectedNoElement) {
        }

        try {
            reader3.next();
            fail("next should fail with an NoSuchElementException");
        } catch (NoSuchElementException expectedNoElement) {
        }

        reader1.close();
        reader2.close();
        reader3.close();

        try {
            reader1.next();
            fail("next should fail with an IOException");
        } catch (IOException expectedClosed) {
        }

        try {
            reader2.next();
            fail("next should fail with an IOException");
        } catch (IOException expectedClosed) {
        }

        try {
            reader3.next();
            fail("next should fail with an IOException");
        } catch (IOException expectedClosed) {
        }
    }

    public void testGetFeatureReaderFilterAutoCommit()
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeatureType type = data.getSchema("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = data.getFeatureReader(type, Filter.INCLUDE, Transaction.AUTO_COMMIT);
        assertFalse(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(type, Filter.EXCLUDE, Transaction.AUTO_COMMIT);
        assertTrue(reader instanceof EmptyFeatureReader);

        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader = data.getFeatureReader(type, rd1Filter, Transaction.AUTO_COMMIT);

        //assertTrue(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));
    }

    // TODO: uncomment when and if MySQLDataStore gets transaction capabilities
    //    public void testGetFeatureReaderFilterTransaction()
    //        throws NoSuchElementException, IOException, IllegalAttributeException {
    //        Transaction t = new DefaultTransaction();
    //        FeatureType type = data.getSchema("road");
    //         FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    //
    //        reader = data.getFeatureReader(type, Filter.EXCLUDE, t);
    //        assertTrue(reader instanceof EmptyFeatureReader);
    //        assertEquals(type, reader.getFeatureType());
    //        assertEquals(0, count(reader));
    //
    //        reader = data.getFeatureReader(type, Filter.INCLUDE, t);
    //        assertEquals(type, reader.getFeatureType());
    //        assertEquals(roadFeatures.length, count(reader));
    //
    //        reader = data.getFeatureReader(type, rd1Filter, t);
    //        assertEquals(type, reader.getFeatureType());
    //        assertEquals(1, count(reader));
    //
    //        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Filter.INCLUDE, t);
    //        Feature feature;
    //
    //        while (writer.hasNext()) {
    //            feature = writer.next();
    //
    //            if (feature.getID().equals(roadFeatures[0].getID())) {
    //                writer.remove();
    //            }
    //        }
    //
    //        reader = data.getFeatureReader(type, Filter.EXCLUDE, t);
    //        assertEquals(0, count(reader));
    //
    //        reader = data.getFeatureReader(type, Filter.INCLUDE, t);
    //        assertEquals(roadFeatures.length - 1, count(reader));
    //
    //        reader = data.getFeatureReader(type, rd1Filter, t);
    //        assertEquals(0, count(reader));
    //
    //        t.rollback();
    //        reader = data.getFeatureReader(type, Filter.EXCLUDE, t);
    //        assertEquals(0, count(reader));
    //
    //        reader = data.getFeatureReader(type, Filter.INCLUDE, t);
    //        assertEquals(roadFeatures.length, count(reader));
    //
    //        reader = data.getFeatureReader(type, rd1Filter, t);
    //        assertEquals(1, count(reader));
    //    }

    /**
     * Ensure readers contents equal those in the feature array
     *
     * @param features DOCUMENT ME!
     * @param reader DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalAttributeException DOCUMENT ME!
     */
    void assertCovered(SimpleFeature[] features,  FeatureReader<SimpleFeatureType, SimpleFeature> reader)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        int count = 0;

        try {
            while (reader.hasNext()) {
                assertContains(features, reader.next());
                count++;
            }
        } finally {
            reader.close();
        }

        assertEquals(features.length, count);
    }

    /**
     * Ensure readers contents match those in the feature array
     *
     * <p>
     * Implemented using match on attribute types, not feature id
     * </p>
     *
     * @param array DOCUMENT ME!
     * @param reader DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    void assertMatched(SimpleFeature[] array,  FeatureReader<SimpleFeatureType, SimpleFeature> reader)
        throws Exception {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();
                assertMatch(array, feature);
                count++;
            }
        } finally {
            reader.close();
        }

        assertEquals("array not matched by reader", array.length, count);
    }

    void assertMatch(SimpleFeature[] array, SimpleFeature feature) {
        assertTrue(array != null);
        assertTrue(array.length != 0);

        SimpleFeatureType schema = feature.getFeatureType();

        for (int i = 0; i < array.length; i++) {
            if (match(array[i], feature)) {
                return;
            }
        }

        System.out.println("not found:" + feature);

        for (int i = 0; i < array.length; i++) {
            System.out.println(i + ":" + array[i]);
        }

        fail("array has no match for " + feature);
    }

    /**
     * Ensure that  FeatureReader<SimpleFeatureType, SimpleFeature> reader contains extactly the contents of
     * array.
     *
     * @param reader DOCUMENT ME!
     * @param array DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     * @throws IllegalAttributeException DOCUMENT ME!
     */
    boolean covers(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!contains(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }

        return count == array.length;
    }

    boolean covers(SimpleFeatureIterator reader, SimpleFeature[] array)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!contains(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }

        return count == array.length;
    }

    boolean coversLax(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                if (!containsLax(array, feature)) {
                    return false;
                }

                count++;
            }
        } finally {
            reader.close();
        }

        return count == array.length;
    }

    void dump(String message,  FeatureReader<SimpleFeatureType, SimpleFeature> reader)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = reader.next();

                String msg = message + ": feture " + count + "=" + feature;

                //LOGGER.info( msg );
                System.out.println(msg);
                count++;
            }
        } finally {
            reader.close();
        }
    }

    void dump(String message, Object[] array) {
        for (int i = 0; i < array.length; i++) {
            String msg = message + ": " + i + "=" + array[i];

            //LOGGER.info( msg );
            System.out.println(msg);
        }
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, Filter, Transaction)
     */
    public void testGetFeatureWriter() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Filter.INCLUDE, Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length, count(writer));
    }

    public void testGetFeatureWriterClose() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Filter.INCLUDE, Transaction.AUTO_COMMIT);

        writer.close();

        try {
            assertFalse(writer.hasNext());
            fail("Should not be able to use a closed writer");
        } catch (IOException expected) {
        }

        try {
            assertNull(writer.next());
            fail("Should not be able to use a closed writer");
        } catch (IOException expected) {
        }

        try {
            writer.close();
        } catch (IOException expected) {
        }
    }

    public void testGetFeatureWriterRemove() throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer("road");
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                writer.remove();
            }
        }

        assertEquals(roadFeatures.length - 1, count("road"));
    }

    public void testGetFeatureWriterRemoveAll() throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer("road");
        SimpleFeature feature;

        try {
            while (writer.hasNext()) {
                feature = writer.next();
                writer.remove();
            }
        } finally {
            writer.close();
        }

        assertEquals(0, count("road"));
    }

    public int count(String typeName) throws IOException {
        //return count(reader(typeName));
        // makes use of optimization if any
        return data.getFeatureSource(typeName).getFeatures().size();
    }

    public void testGetFeaturesWriterAdd() throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Transaction.AUTO_COMMIT);
        SimpleFeature feature;

        LOGGER.info("about to call has next on writer " + writer);

        while (writer.hasNext()) {
            feature = (SimpleFeature) writer.next();
        }

        assertFalse(writer.hasNext());

        feature = (SimpleFeature) writer.next();
        feature.setAttributes(newRoad.getAttributes());
        writer.write();

        assertFalse(writer.hasNext());
        assertEquals(roadFeatures.length + 1, count("road"));
    }

    /**
     * Seach for feature based on AttributeType.
     *
     * <p>
     * If attributeName is null, we will search by feature.getID()
     * </p>
     *
     * <p>
     * The provided reader will be closed by this opperations.
     * </p>
     *
     * @param reader reader to search through
     * @param attributeName attributeName, or null for featureID
     * @param value value to match
     *
     * @return Feature
     *
     * @throws NoSuchElementException if a match could not be found
     * @throws IOException We could not use reader
     * @throws IllegalAttributeException if attributeName did not match schema
     */
    public SimpleFeature findFeature(FeatureReader <SimpleFeatureType, SimpleFeature> reader, String attributeName, Object value)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature f;

        try {
            while (reader.hasNext()) {
                f = reader.next();

                if (attributeName == null) {
                    if (value.equals(f.getID())) {
                        return f;
                    }
                } else {
                    if (value.equals(f.getAttribute(attributeName))) {
                        return f;
                    }
                }
            }
        } finally {
            reader.close();
        }

        if (attributeName == null) {
            throw new NoSuchElementException("No match for FID=" + value);
        } else {
            throw new NoSuchElementException("No match for " + attributeName + "=" + value);
        }
    }

    public SimpleFeature feature(String typeName, String fid)
        throws NoSuchElementException, IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader(typeName);
        SimpleFeature f;

        try {
            while (reader.hasNext()) {
                f = reader.next();

                if (fid.equals(f.getID())) {
                    return f;
                }
            }
        } finally {
            reader.close();
        }

        return null;
    }

    public void testGetFeaturesWriterModify() throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer("road");
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                feature.setAttribute("name", "changed");
                writer.write();
            }
        }

        feature = (SimpleFeature) feature("road", roadFeatures[0].getID());
        assertNotNull(feature);
        assertEquals("changed", feature.getAttribute("name"));
    }

    public void testGetFeatureWriterTypeNameTransaction()
        throws NoSuchElementException, IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter("road", Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length, count(writer));

        //writer.close(); called by count.
    }

    public void testGetFeatureWriterAppendTypeNameTransaction()
        throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriterAppend("road", Transaction.AUTO_COMMIT);
        assertEquals(0, count(writer));

        //writer.close(); called by count
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, boolean, Transaction)
     * @task REVISIT: JDBCDataStore currently does not return these proper
     * instanceof's.  If we want to guarantee that people can't append to
     * a request with a FeatureWriter then we could add the functionality to
     * JDBCDataStore by having getFeatureWriter(.. Filter ...) check to see if
     * the FeatureWriter returned is instanceof FilteringFeatureWriter, and if
     * not then just wrap it in a FilteringFeatureWriter(writer, Filter.INCLUDE).
     * I think it'd be a bit of unnecessary overhead, but if we want it it's
     * easy to do.  It will guarantee that calls with Filter won't ever append.
     * Doing with Filter.INCLUDE, however, would require a bit of reworking, as
     * the Filter getFeatureWriter is currently where we do the bulk of
     * the work.
     */
    public void testGetFeatureWriterFilter()
        throws NoSuchElementException, IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter("road", Filter.EXCLUDE, Transaction.AUTO_COMMIT);

        //see task above
        // assertTrue(writer instanceof EmptyFeatureWriter);
        assertEquals(0, count(writer));

        writer = data.getFeatureWriter("road", Filter.INCLUDE, Transaction.AUTO_COMMIT);

        //assertFalse(writer instanceof FilteringFeatureWriter);
        assertEquals(roadFeatures.length, count(writer));

        writer = data.getFeatureWriter("road", rd1Filter, Transaction.AUTO_COMMIT);

        //assertTrue(writer instanceof FilteringFeatureWriter);
        assertEquals(1, count(writer));
    }

    // Uncomment when and if MySQLDataStore will support transaction
    //    /**
    //     * Test two transactions one removing feature, and one adding a feature.
    //     *
    //     * @throws Exception DOCUMENT ME!
    //     */
    //    public void testGetFeatureWriterTransaction() throws Exception {
    //        Transaction t1 = new DefaultTransaction();
    //        Transaction t2 = new DefaultTransaction();
    //        FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 = data.getFeatureWriter("road", rd1Filter, t1);
    //        FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 = data.getFeatureWriterAppend("road", t2);
    //
    //        FeatureType road = data.getSchema("road");
    //         FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    //        Feature feature;
    //        Feature[] ORIGIONAL = roadFeatures;
    //        Feature[] REMOVE = new Feature[ORIGIONAL.length - 1];
    //        Feature[] ADD = new Feature[ORIGIONAL.length + 1];
    //        Feature[] FINAL = new Feature[ORIGIONAL.length];
    //        int i;
    //        int index;
    //        index = 0;
    //
    //        for (i = 0; i < ORIGIONAL.length; i++) {
    //            feature = ORIGIONAL[i];
    //
    //            if (!feature.getID().equals(roadFeatures[0].getID())) {
    //                REMOVE[index++] = feature;
    //            }
    //        }
    //
    //        for (i = 0; i < ORIGIONAL.length; i++) {
    //            ADD[i] = ORIGIONAL[i];
    //        }
    //
    //        ADD[i] = newRoad; // will need to update with Fid from database
    //
    //        for (i = 0; i < REMOVE.length; i++) {
    //            FINAL[i] = REMOVE[i];
    //        }
    //
    //        FINAL[i] = newRoad; // will need to update with Fid from database
    //
    //        // start of with ORIGINAL                        
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(covers(reader, ORIGIONAL));
    //
    //        // writer 1 removes road.rd1 on t1
    //        // -------------------------------
    //        // - tests transaction independence from DataStore
    //        while (writer1.hasNext()) {
    //            feature = writer1.next();
    //            assertEquals(roadFeatures[0].getID(), feature.getID());
    //            writer1.remove();
    //        }
    //
    //        // still have ORIGIONAL and t1 has REMOVE
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(covers(reader, ORIGIONAL));
    //
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t1);
    //        assertTrue(covers(reader, REMOVE));
    //
    //        // close writer1
    //        // --------------
    //        // ensure that modification is left up to transaction commmit
    //        writer1.close();
    //
    //        // We still have ORIGIONAL and t1 has REMOVE
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(covers(reader, ORIGIONAL));
    //
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t1);
    //        assertTrue(covers(reader, REMOVE));
    //
    //        // writer 2 adds road.rd4 on t2
    //        // ----------------------------
    //        // - tests transaction independence from each other
    //        feature = writer2.next();
    //        feature.setAttributes(newRoad.getAttributes(null));
    //        writer2.write();
    //
    //        // HACK: ?!? update ADD and FINAL with new FID from database
    //        //
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t2);
    //        newRoad = findFeature(reader, "id", new Integer(4));
    //        System.out.println("newRoad:" + newRoad);
    //        ADD[ADD.length - 1] = newRoad;
    //        FINAL[FINAL.length - 1] = newRoad;
    //
    //        // We still have ORIGIONAL and t2 has ADD
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(covers(reader, ORIGIONAL));
    //
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t2);
    //        assertMatched(ADD, reader); // broken due to FID problem
    //
    //        writer2.close();
    //
    //        // Still have ORIGIONAL and t2 has ADD
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(covers(reader, ORIGIONAL));
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t2);
    //        assertTrue(coversLax(reader, ADD));
    //
    //        // commit t1
    //        // ---------
    //        // -ensure that delayed writing of transactions takes place
    //        //
    //        t1.commit();
    //
    //        // We now have REMOVE, as does t1 (which has not additional diffs)
    //        // t2 will have FINAL
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(covers(reader, REMOVE));
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t1);
    //        assertTrue(covers(reader, REMOVE));
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t2);
    //        assertTrue(coversLax(reader, FINAL));
    //
    //        // commit t2
    //        // ---------
    //        // -ensure that everyone is FINAL at the end of the day
    //        t2.commit();
    //
    //        // We now have Number( remove one and add one)
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    //        assertTrue(coversLax(reader, FINAL));
    //
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t1);
    //        assertTrue(coversLax(reader, FINAL));
    //
    //        reader = data.getFeatureReader(road, Filter.INCLUDE, t2);
    //        assertTrue(coversLax(reader, FINAL));
    //    }

    // Feature Source Testing
    public void testGetFeatureSourceRoad() throws IOException {
        SimpleFeatureSource road = data.getFeatureSource("road");

        assertEquals(roadType, road.getSchema());
        assertSame(data, road.getDataStore());

        int count = road.getCount(Query.ALL);
        assertTrue((count == 3) || (count == -1));

        Envelope bounds = road.getBounds(Query.ALL);
        assertTrue((bounds == null) || bounds.equals(roadBounds));

        SimpleFeatureCollection all = road.getFeatures();
        assertEquals(3, all.size());
        assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection expected = DataUtilities.collection(roadFeatures);

        assertCovers("all", expected, all);
        assertEquals(roadBounds, all.getBounds());

        SimpleFeatureCollection some = road.getFeatures(rd12Filter);
        assertEquals(2, some.size());

        Envelope e = new Envelope();
        e.expandToInclude((Envelope) roadFeatures[0].getBounds());
        e.expandToInclude((Envelope)roadFeatures[1].getBounds());
        assertEquals(e, some.getBounds());
        assertEquals(some.getSchema(), road.getSchema());

        DefaultQuery query = new DefaultQuery("road", rd12Filter, new String[] { "name" });

        SimpleFeatureCollection half = road.getFeatures(query);
        assertEquals(2, half.size());
        assertEquals(1, half.getSchema().getAttributeCount());

        SimpleFeatureIterator reader = half.features();
        SimpleFeatureType type = half.getSchema();
        reader.close();

        SimpleFeatureType actual = half.getSchema();

        assertEquals(type.getTypeName(), actual.getTypeName());
        assertEquals(type.getName().getNamespaceURI(), actual.getName().getNamespaceURI());
        assertEquals(type.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < type.getAttributeCount(); i++) {
            assertEquals(type.getDescriptor(i), actual.getDescriptor(i));
        }

        assertNull(type.getGeometryDescriptor());
        assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
        assertEquals(type, actual);

        Envelope b = half.getBounds();
        assertEquals(roadBounds, b);
    }

    public void testGetFeatureSourceRiver()
        throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeatureSource river = data.getFeatureSource("river");

        assertEquals(riverType, river.getSchema());
        assertSame(data, river.getDataStore());

        SimpleFeatureCollection all = river.getFeatures();
        assertEquals(2, all.size());
        assertEquals(riverBounds, all.getBounds());
        assertTrue("rivers", covers(all.features(), riverFeatures));

        SimpleFeatureCollection expected = DataUtilities.collection(riverFeatures);
        assertCovers("all", expected, all);
        assertEquals(riverBounds, all.getBounds());
    }
    
    public void testCaseInsensitiveFilter() throws Exception {
        final String riverName = riverType.getName().getLocalPart();
        SimpleFeatureSource rivers = data.getFeatureSource(riverName);
        org.opengis.filter.Filter caseSensitive = ff.equal(ff.property("river"), ff.literal("Rv1"), true);
        assertEquals(0, rivers.getCount(new DefaultQuery(riverName, caseSensitive)));
        org.opengis.filter.Filter caseInsensitive = ff.equal(ff.property("river"), ff.literal("Rv1"), false);
        assertEquals(1, rivers.getCount(new DefaultQuery(riverName, caseInsensitive)));
    }

    //
    // Feature Store Testing
    //
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        //FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        //rd1Filter = factory.createFidFilter( roadFeatures[0].getID() );
        Object changed = new Integer(5);
        AttributeDescriptor name = roadType.getDescriptor("id");
        road.modifyFeatures(name, changed, rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        assertEquals(changed, results.features().next().getAttribute("id"));
    }

    public void testGetFeatureStoreModifyFeatures2() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        rd1Filter = factory.createFidFilter(roadFeatures[0].getID());

        AttributeDescriptor name = roadType.getDescriptor("name");
        road.modifyFeatures(new AttributeDescriptor[] { name, }, new Object[] { "changed", }, rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        assertEquals("changed", results.features().next().getAttribute("name"));
    }

    public void testGetFeatureStoreRemoveFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        road.removeFeatures(rd1Filter);
        assertEquals(0, road.getFeatures(rd1Filter).size());
        assertEquals(roadFeatures.length - 1, road.getFeatures().size());
    }

    public void testGetFeatureStoreAddFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        road.addFeatures(DataUtilities.collection(newRoad));
        assertEquals(roadFeatures.length + 1, count("road"));
    }

    public void testGetFeatureStoreSetFeatures()
        throws NoSuchElementException, IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(new SimpleFeature[] { newRoad, });

        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        assertEquals(3, count("road"));

        road.setFeatures(reader);

        assertEquals(1, count("road"));
    }

    //  Uncomment when and if MySQLDataStore will support transaction
    //    public void testGetFeatureStoreTransactionSupport() throws Exception {
    //        Transaction t1 = new DefaultTransaction();
    //        Transaction t2 = new DefaultTransaction();
    //
    //        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
    //        FeatureStore road1 = (SimpleFeatureStore) data.getFeatureSource("road");
    //        FeatureStore road2 = (SimpleFeatureStore) data.getFeatureSource("road");
    //
    //        road1.setTransaction(t1);
    //        road2.setTransaction(t2);
    //
    //        Feature feature;
    //        Feature[] ORIGIONAL = roadFeatures;
    //        Feature[] REMOVE = new Feature[ORIGIONAL.length - 1];
    //        Feature[] ADD = new Feature[ORIGIONAL.length + 1];
    //        Feature[] FINAL = new Feature[ORIGIONAL.length];
    //        int i;
    //        int index;
    //        index = 0;
    //
    //        for (i = 0; i < ORIGIONAL.length; i++) {
    //            feature = ORIGIONAL[i];
    //            LOGGER.info("id is " + feature.getID());
    //
    //            if (!feature.getID().equals("road.rd1")) {
    //                REMOVE[index++] = feature;
    //            }
    //        }
    //
    //        for (i = 0; i < ORIGIONAL.length; i++) {
    //            ADD[i] = ORIGIONAL[i];
    //        }
    //
    //        ADD[i] = newRoad;
    //
    //        for (i = 0; i < REMOVE.length; i++) {
    //            FINAL[i] = REMOVE[i];
    //        }
    //
    //        FINAL[i] = newRoad;
    //
    //        // start of with ORIGINAL
    //        assertTrue(covers(road.getFeatures().reader(), ORIGIONAL));
    //
    //        // road1 removes road.rd1 on t1
    //        // -------------------------------
    //        // - tests transaction independence from DataStore
    //        road1.removeFeatures(rd1Filter);
    //
    //        // still have ORIGIONAL and t1 has REMOVE
    //        assertTrue(covers(road.getFeatures().reader(), ORIGIONAL));
    //        assertTrue(covers(road1.getFeatures().reader(), REMOVE));
    //
    //        // road2 adds road.rd4 on t2
    //        // ----------------------------
    //        // - tests transaction independence from each other
    //         FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(new Feature[] { newRoad, });
    //        road2.addFeatures(reader);
    //
    //        // We still have ORIGIONAL, t1 has REMOVE, and t2 has ADD
    //        assertTrue(covers(road.getFeatures().reader(), ORIGIONAL));
    //        assertTrue(covers(road1.getFeatures().reader(), REMOVE));
    //        assertTrue(coversLax(road2.getFeatures().reader(), ADD));
    //
    //        // commit t1
    //        // ---------
    //        // -ensure that delayed writing of transactions takes place
    //        //
    //        t1.commit();
    //
    //        // We now have REMOVE, as does t1 (which has not additional diffs)
    //        // t2 will have FINAL
    //        assertTrue(covers(road.getFeatures().reader(), REMOVE));
    //        assertTrue(covers(road1.getFeatures().reader(), REMOVE));
    //        assertTrue(coversLax(road2.getFeatures().reader(), FINAL));
    //
    //        // commit t2
    //        // ---------
    //        // -ensure that everyone is FINAL at the end of the day
    //        t2.commit();
    //
    //        // We now have Number( remove one and add one)
    //        assertTrue(coversLax(road.getFeatures().reader(), FINAL));
    //        assertTrue(coversLax(road1.getFeatures().reader(), FINAL));
    //        assertTrue(coversLax(road2.getFeatures().reader(), FINAL));
    //    }
    boolean isLocked(String typeName, String fid) {
        InProcessLockingManager lockingManager = (InProcessLockingManager) data.getLockingManager();

        return lockingManager.isLocked(typeName, fid);
    }

    //
    // FeatureLocking Testing    
    //

    /*
     * Test for void lockFeatures()
     */
    public void testLockFeatures() throws IOException {
        FeatureLock lock = FeatureLockFactory.generate("test", LOCK_DURATION);
        FeatureLocking<SimpleFeatureType, SimpleFeature> road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road.setFeatureLock(lock);

        assertFalse(isLocked("road", "road.rd1"));
        road.lockFeatures();
        assertTrue(isLocked("road", "road.rd1"));
    }

    public void testUnLockFeatures() throws IOException {
        FeatureLock lock = FeatureLockFactory.generate("test", LOCK_DURATION);
        FeatureLocking<SimpleFeatureType, SimpleFeature> road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road.setFeatureLock(lock);
        road.lockFeatures();

        try {
            road.unLockFeatures();
            fail("unlock should fail due on AUTO_COMMIT");
        } catch (IOException expected) {
        }

        Transaction t = new DefaultTransaction();
        road.setTransaction(t);

        try {
            road.unLockFeatures();
            fail("unlock should fail due lack of authorization");
        } catch (IOException expected) {
        }

        t.addAuthorization(lock.getAuthorization());
        road.unLockFeatures();
    }

    public void testLockFeatureInteraction() throws IOException {
        FeatureLock lockA = FeatureLockFactory.generate("LockA", LOCK_DURATION);
        FeatureLock lockB = FeatureLockFactory.generate("LockB", LOCK_DURATION);
        Transaction t1 = new DefaultTransaction();
        Transaction t2 = new DefaultTransaction();
        FeatureLocking<SimpleFeatureType, SimpleFeature> road1 = (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        FeatureLocking<SimpleFeatureType, SimpleFeature> road2 = (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road1.setTransaction(t1);
        road2.setTransaction(t2);
        road1.setFeatureLock(lockA);
        road2.setFeatureLock(lockB);

        assertFalse(isLocked("road", "road.rd1"));
        assertFalse(isLocked("road", "road.rd2"));
        assertFalse(isLocked("road", "road.rd3"));

        road1.lockFeatures(rd1Filter);
        assertTrue(isLocked("road", "road.rd1"));
        assertFalse(isLocked("road", "road.rd2"));
        assertFalse(isLocked("road", "road.rd3"));

        road2.lockFeatures(rd2Filter);
        assertTrue(isLocked("road", "road.rd1"));
        assertTrue(isLocked("road", "road.rd2"));
        assertFalse(isLocked("road", "road.rd3"));

        try {
            road1.unLockFeatures(rd1Filter);
            fail("need authorization");
        } catch (IOException expected) {
        }

        t1.addAuthorization(lockA.getAuthorization());

        try {
            road1.unLockFeatures(rd2Filter);
            fail("need correct authorization");
        } catch (IOException expected) {
        }

        road1.unLockFeatures(rd1Filter);
        assertFalse(isLocked("road", "road.rd1"));
        assertTrue(isLocked("road", "road.rd2"));
        assertFalse(isLocked("road", "road.rd3"));

        t2.addAuthorization(lockB.getAuthorization());
        road2.unLockFeatures(rd2Filter);
        assertFalse(isLocked("road", "road.rd1"));
        assertFalse(isLocked("road", "road.rd2"));
        assertFalse(isLocked("road", "road.rd3"));
    }

    public void testGetFeatureLockingExpire() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate("Timed", 200);

        FeatureLocking<SimpleFeatureType, SimpleFeature> road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road.setFeatureLock(lock);
        assertFalse(isLocked("road", "road.rd1"));

        road.lockFeatures(rd1Filter);
        assertTrue(isLocked("road", "road.rd1"));

        long then = System.currentTimeMillis();

        do {
            Thread.sleep(200);
        } while ((System.currentTimeMillis() - then) < 200);

        assertFalse(isLocked("road", "road.rd1"));
    }
}
