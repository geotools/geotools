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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.ConnectionPoolManager;
import org.geotools.data.jdbc.JDBCTransactionState;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.LiteralExpression;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;


/**
 * Test for mysql.  Must use a locally available instance of mysql.
 *
 * @author Chris Holmes, TOPP
 * @source $URL$
 */
public class MySQLDataStoreTest extends TestCase {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.postgis");
    private static String FEATURE_TABLE = "testset";
    private static String TEST_NS = "http://www.geotools.org/data/postgis";
    private static GeometryFactory geomFac = new GeometryFactory();
    private FilterFactory filterFac = FilterFactoryFinder.createFilterFactory();
    private SimpleFeatureCollection collection = FeatureCollections.newCollection();
    private SimpleFeatureType schema;
    private int srid = -1;
    private MySQLDataStore dstore;
    private ManageableDataSource connPool;
    private CompareFilter tFilter;
    private int addId = 32;
    private org.geotools.filter.GeometryFilter geomFilter;

    public MySQLDataStoreTest(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        LOGGER.info("starting suite...");

        TestSuite suite = new TestSuite(MySQLDataStoreTest.class);
        LOGGER.info("made suite...");

        return suite;
    }

    protected void setUp() throws Exception {
        super.setUp();

        PropertyResourceBundle resource;
        resource = new PropertyResourceBundle(this.getClass()
                                                  .getResourceAsStream("fixture.properties"));

        String namespace = resource.getString("namespace");
        String host = resource.getString("host");
        int port = Integer.parseInt(resource.getString("port"));
        String database = resource.getString("database");

        String user = resource.getString("user");
        String password = resource.getString("passwd");

        if (namespace.equals("http://www.geotools.org/data/postgis")) {
            throw new IllegalStateException(
                "The fixture.properties file needs to be configured for your own database");
        }

        connPool = MySQLDataStoreFactory.getDefaultDataSource(host, user, password, port, database,
                10, 2, false);
        setupTestTable(connPool.getConnection());

        dstore = new MySQLDataStore(connPool, namespace);
        dstore.setFIDMapper("testset",
            new TypedFIDMapper(new BasicFIDMapper("gid", 255, true), "testset"));
        dstore.setWKBEnabled(true);
        schema = dstore.getSchema(FEATURE_TABLE);
    }

    /**
     * @param connection
     */
    private void setupTestTable(Connection conn) throws IOException, SQLException {
        //        gid;int4;4;0;;YES;;
        //        area;float8;8;0;;YES;;
        //        perimeter;float8;8;0;;YES;;
        //        testb_;int4;4;0;;YES;;
        //        testb_id;int4;4;0;;YES;;
        //        name;varchar;0;0;;YES;;
        //        pcedflag;int4;4;0;;YES;;
        //        dbdflag;int4;4;0;;YES;;
        //        the_geom;geometry;-1;0;;YES;;
        Statement st = conn.createStatement();

        try {
            st.execute("DROP TABLE testset");
        } catch (Exception e) {
        }

        st.execute(
            "CREATE TABLE testset ( gid integer, area double, perimeter double, testb_ integer, "
            + " testb_id integer, name varchar(255), pcedflag integer, dbdflag integer, the_geom multipolygon)");
        st.close();

        PreparedStatement ps = conn.prepareStatement(
                "INSERT into testset values(?, ?, ?, ?, ?, ?, ?, ?, GeomFromText(?))");
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream("testdata.txt")));

        try {
            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                ps.setInt(1, Integer.parseInt(values[0]));
                ps.setDouble(2, Double.parseDouble(values[1]));
                ps.setDouble(3, Double.parseDouble(values[2]));
                ps.setInt(4, Integer.parseInt(values[3]));
                ps.setInt(5, Integer.parseInt(values[4]));
                ps.setString(6, values[5]);
                ps.setInt(7, Integer.parseInt(values[6]));
                ps.setInt(8, Integer.parseInt(values[7]));
                ps.setString(9, values[8]);
                ps.execute();
            }
        } finally {
            reader.close();
            ps.close();
        }
    }

    protected void tearDown() {
        try {
            dropTestTable(connPool.getConnection());
        } catch (SQLException e) {
        } finally {
            ConnectionPoolManager.getInstance().closeAll();
        }
    }

    public void dropTestTable(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        st.execute("DROP TABLE testset");
    }

    //todo assert on schema.
    public void testFeatureTypes() throws Exception {
        String[] types = dstore.getTypeNames();
        SimpleFeatureType schema1 = dstore.getSchema(types[0]);

        //FeatureType schema2 = dstore.getSchema(types[1]);
        //need to figure out spatial_ref_system and geometry_columns
        LOGGER.fine("first schemas are: \n" + schema1); // + "\n" + schema2);

        try {
            String badSchema = "bad-schema23";
            dstore.getSchema(badSchema);
            fail("should not have schema " + badSchema);
        } catch (SchemaNotFoundException e) {
            LOGGER.fine("succesfully caught exception: " + e);

            //catch the proper exception
        }
    }

    //tests todo: bad retyping. post filters. 
    public void testGetReader() throws Exception {
        String testTable = FEATURE_TABLE;
        LOGGER.fine("testTable " + testTable + " has schema " + dstore.getSchema(testTable));

         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader(schema, Filter.INCLUDE,
                Transaction.AUTO_COMMIT);
        int numFeatures = count(reader);
        assertEquals("Number of features off:", 6, numFeatures);
    }

    public void testFilter() throws Exception {
        CompareFilter test1 = null;

        try {
            test1 = filterFac.createCompareFilter(AbstractFilter.COMPARE_EQUALS);

            Integer testInt = new Integer(0);
            Expression testLiteral = filterFac.createLiteralExpression(testInt);
            test1.addLeftValue(testLiteral);
            test1.addRightValue(filterFac.createAttributeExpression(schema, "pcedflag"));
        } catch (IllegalFilterException e) {
            fail("Illegal Filter Exception " + e);
        }

        Query query = new DefaultQuery(FEATURE_TABLE, test1);
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader(schema, test1, Transaction.AUTO_COMMIT);
        assertEquals("Number of filtered features off:", 2, count(reader));
    }

    public void testGeomFilter() throws Exception {
        org.geotools.filter.GeometryFilter gf = filterFac.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        Envelope env = new Envelope(428500, 430000, 428500, 440000);
        LiteralExpression right = filterFac.createBBoxExpression(env);
        gf.addRightGeometry(right);
        gf.addLeftGeometry(filterFac.createAttributeExpression(schema, "the_geom"));

         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader(schema, gf, Transaction.AUTO_COMMIT);
        assertEquals("Number of geom filtered features off:", 2, count(reader));
    }

    int count(FeatureReader <SimpleFeatureType, SimpleFeature> reader)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        int count = 0;

        try {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } finally {
            reader.close();
        }

        return count;
    }

    int count(FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
        throws NoSuchElementException, IOException, IllegalAttributeException {
        int count = 0;

        try {
            while (writer.hasNext()) {
                writer.next();
                count++;
            }
        } finally {
            writer.close();
        }

        return count;
    }

    public void testGetFeatureWriter() throws Exception {
        Transaction trans = new DefaultTransaction();
        JDBCTransactionState state = new JDBCTransactionState(connPool);
        trans.putState(connPool, state);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter("testset", Filter.INCLUDE, trans);

        //count(writer);
        assertEquals(6, count(writer));

        try {
            assertFalse(writer.hasNext());
        } catch (IOException expected) {
        }

        //TODO: test that writer.next is an empty feature.
        //try {
        //    writer.next();
        //    fail("Should not be able to use a closed writer");
        //} catch (IOException expected) {
        //}
    }

    public void testBadTypeName() throws Exception {
        try {
            String badType = "badType43";
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter(badType, Filter.INCLUDE,
                    Transaction.AUTO_COMMIT);
            fail("should not have type " + badType);
        } catch (SchemaNotFoundException e) {
            LOGGER.fine("succesfully caught exception: " + e);

            //catch the proper exception
        }
    }

    public void testOptimizedBounds() throws Exception {
        SimpleFeatureSource source = dstore.getFeatureSource(FEATURE_TABLE);
        CompareFilter test1 = null;

        try {
            test1 = filterFac.createCompareFilter(AbstractFilter.COMPARE_EQUALS);

            Integer testInt = new Integer(0);
            Expression testLiteral = filterFac.createLiteralExpression(testInt);
            test1.addLeftValue(testLiteral);
            test1.addRightValue(filterFac.createAttributeExpression(schema, "pcedflag"));
        } catch (IllegalFilterException e) {
            fail("Illegal Filter Exception " + e);
        }

        Query query = new DefaultQuery(FEATURE_TABLE, test1);
        Envelope bounds = source.getBounds(query);
        LOGGER.info("bounds on query " + query + " is " + bounds);

        Envelope fBounds = source.getBounds();
        LOGGER.info("Bounds of source is " + fBounds);

        SimpleFeatureCollection results = source.getFeatures(query);
        LOGGER.info("bounds from feature results is " + results.getBounds());
    }

    public void testGetFeaturesWriterModify() throws IOException, IllegalAttributeException {
        Transaction trans = new DefaultTransaction();
        JDBCTransactionState state = new JDBCTransactionState(connPool);
        trans.putState(connPool, state);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter(FEATURE_TABLE, Filter.INCLUDE, trans);
        int attKeyPos = 0;
        Integer attKey = new Integer(10);
        String attName = "name";
        String newAttVal = "LS 503";
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getAttribute(attKeyPos).equals(attKey)) {
                LOGGER.info("changing name of feature " + feature);
                ;
                feature.setAttribute(attName, newAttVal);
                writer.write();
            }
        }

        //writer.close();
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader(schema, Filter.INCLUDE, trans);

        while (reader.hasNext()) {
            feature = reader.next();

            if (feature.getAttribute(attKeyPos).equals(attKey)) {
                LOGGER.fine("checking feature " + feature);
                ;

                Object modAtt = feature.getAttribute(attName);

                //LOGGER.fine("modified attribute is " + modAtt);
                assertEquals("attribute was not changed", newAttVal, (String) modAtt);
            }
        }

        //feature = (Feature) data.features( "road" ).get( "road.rd1" );
        //assertEquals( "changed", feature.getAttribute("name") );
        state.rollback();
    }

    public void testGetFeaturesWriterModifyGeometry() throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter("road", Filter.INCLUDE,
                Transaction.AUTO_COMMIT);
        SimpleFeature feature;
        Coordinate[] points = {
                new Coordinate(59, 59), new Coordinate(17, 17), new Coordinate(49, 39),
                new Coordinate(57, 67), new Coordinate(79, 79)
            };
        LineString geom = geomFac.createLineString(points);

        while (writer.hasNext()) {
            feature = writer.next();
            LOGGER.info("looking at feature " + feature);

            if (feature.getAttribute(0).equals("asphalt")) {
                LOGGER.info("changing name and geom");
                feature.setAttribute("the_geom", geom);
                writer.write();
            }
        }

        //feature = (Feature) data.features( "road" ).get( "road.rd1" );
        //assertEquals( "changed", feature.getAttribute("name") );
        writer.close();
    }

    public void testGetFeaturesWriterModifyMultipleAtts()
        throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter("road", Filter.INCLUDE,
                Transaction.AUTO_COMMIT);
        SimpleFeature feature;
        Coordinate[] points = {
                new Coordinate(32, 44), new Coordinate(62, 51), new Coordinate(45, 35),
                new Coordinate(55, 65), new Coordinate(73, 75)
            };
        LineString geom = geomFac.createLineString(points);

        while (writer.hasNext()) {
            feature = writer.next();
            LOGGER.info("looking at feature " + feature);

            if (feature.getAttribute(0).equals("asphalt")) {
                LOGGER.info("changing name and geom");
                feature.setAttribute("the_geom", geom);
                feature.setAttribute("name", "trick");
                writer.write();
            }
        }

        //feature = (Feature) data.features( "road" ).get( "road.rd1" );
        //assertEquals( "changed", feature.getAttribute("name") );
        writer.close();
    }

    public void testGetFeaturesWriterAdd() throws IOException, IllegalAttributeException {
        Transaction trans = new DefaultTransaction();
        JDBCTransactionState state = new JDBCTransactionState(connPool);
        trans.putState(connPool, state);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter(FEATURE_TABLE, Filter.INCLUDE, trans);
        int count = 0;

        while (writer.hasNext()) {
            SimpleFeature feature = writer.next();
            count++;
        }

        assertEquals("Checking num features before add", 6, count);
        assertFalse(writer.hasNext());

        SimpleFeature feature = (SimpleFeature) writer.next();
        Object[] atts = getTestAtts("testAdd");
        feature.setAttributes(atts);
        writer.write();
        assertFalse(writer.hasNext());

        //assertEquals( fixture.roadFeatures.length+1, data.features( "road" ).size() );
        writer.close();

         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader(schema, Filter.INCLUDE, trans);
        int numFeatures = count(reader);
        assertEquals("Wrong number of features after add", 7, numFeatures);
        state.rollback();
    }

    private Object[] getTestAtts(String name) {
        Coordinate[] points = {
                new Coordinate(45, 45), new Coordinate(45, 55), new Coordinate(55, 55),
                new Coordinate(55, 45), new Coordinate(45, 45)
            };
        PrecisionModel precModel = new PrecisionModel();
        LinearRing shell = new LinearRing(points, precModel, srid);
        Polygon[] testPolys = { new Polygon(shell, precModel, srid) };
        MultiPolygon the_geom = new MultiPolygon(testPolys, precModel, srid);
        Integer gID = new Integer(addId);
        Double area = new Double(100.0);
        Double perimeter = new Double(40.0);
        Integer testb_ = new Integer(22);
        Integer testb_id = new Integer(4833);
        Integer code = new Integer(0);

        Object[] attributes = { gID, area, perimeter, testb_, testb_id, name, code, code, the_geom };

        return attributes;
    }

    public void testGetFeatureWriterRemove() throws IOException, IllegalAttributeException {
        Transaction trans = new DefaultTransaction();
        JDBCTransactionState state = new JDBCTransactionState(connPool);
        trans.putState(connPool, state);

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter(FEATURE_TABLE, Filter.INCLUDE, trans);

         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader(schema, Filter.INCLUDE, trans);
        int numFeatures = count(reader);

        //assertEquals("Wrong number of features before delete", 6, numFeatures);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = writer.next();

            if (feature.getAttribute(0).equals(new Integer(4))) {
                LOGGER.info("deleting feature " + feature);
                writer.remove();
            }
        }

        writer.close();
        reader = dstore.getFeatureReader(schema, Filter.INCLUDE, trans);
        numFeatures = count(reader);
        assertEquals("Wrong number of features after add", 5, numFeatures);
        state.rollback();
    }

    //assertEquals( fixture.roadFeatures.length-1, data.features( "road" ).size() );
}
