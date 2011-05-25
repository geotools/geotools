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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureLockFactory;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.postgis.fidmapper.OIDFidMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.FilterType;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.function.FilterFunction_geometryType;
import org.geotools.filter.function.math.FilterFunction_ceil;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * This class tests the PostgisDataStoreAPI, against the same tests as MemoryDataStore.
 * <p>
 * The test fixture is available in the shared DataTestCase, really the common elements should move
 * to a shared DataStoreAPITestCase.
 * </p>
 * <p>
 * This class does require your own DataStore, it will create a table populated with the Features
 * from the test fixture, and run a test, and then remove the table.
 * </p>
 * <p>
 * Because of the nature of this testing process you cannot run these tests in conjunction with
 * another user, so they cannot be implemented against the public server.
 * </p>
 * <p>
 * A simple properties file has been constructed, <code>fixture.properties</code>, which you may
 * direct to your own potgis database installation.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class PostgisDataStoreAPIOnlineTest extends AbstractPostgisDataTestCase {

    private static final int LOCK_DURATION = 3600 * 1000; // one hour

    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");

    String victim = null; // "testGetFeatureWriterRemoveAll";

    /**
     * Constructor for MemoryDataStoreTest.
     * 
     * @param test
     * @throws AssertionError
     *             DOCUMENT ME!
     */
    public PostgisDataStoreAPIOnlineTest(String test) {
        super(test);

        if ((victim != null) && !test.equals(victim)) {
            throw new AssertionError("test supressed " + test);
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        // uncomment these lines and put breakpoints in QueryData and DefaultTransaction finalizers
        // to spot unclosed transactions, readers and writers
        // Runtime.getRuntime().gc(); Runtime.getRuntime().gc();
        // Runtime.getRuntime().runFinalization();
    }

    /**
     * This is a quick hack to have our fixture reflect the FIDs in the database.
     * <p>
     * When the dataStore learns how to preserve our FeatureIds this won't be required.
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
                bounds.include(f.getBounds());
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
        bounds12.include(roadFeatures[0].getBounds());
        bounds12.include(roadFeatures[1].getBounds());

        if (!rd12Bounds.equals(bounds12)) {
            System.out.println("warning! Database changed bounds of rd1 & rd2");
            System.out.println("was:" + rd12Bounds);
            System.out.println("now:" + bounds12);
            rd12Bounds = bounds12;
        }

        SimpleFeatureType schema = roadFeatures[0].getFeatureType();
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        CompareFilter tFilter = factory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        Expression rd1Literal = factory.createLiteralExpression("r1");
        tFilter.addLeftValue(rd1Literal);

        Expression rdNameAtt = factory.createAttributeExpression(schema, "name");
        tFilter.addRightValue(rdNameAtt);
        rd1Filter = tFilter;

        tFilter = factory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);

        Expression rd2Literal = factory.createLiteralExpression("r2");
        tFilter.addLeftValue(rd2Literal);
        tFilter.addRightValue(rdNameAtt);
        rd2Filter = tFilter;

        rd12Filter = ff.or(rd2Filter, rd1Filter);
    }

    /**
     * This is a quick hack to have our fixture reflect the FIDs in the database.
     * <p>
     * When the dataStore learns how to preserve our FeatureIds this won't be required.
     * </p>
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected void updateRiverFeaturesFixture() throws Exception {
        Connection conn = pool.getConnection();
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = data.getFeatureReader(new DefaultQuery("river", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);

        ReferencedEnvelope bounds = new ReferencedEnvelope();

        try {
            SimpleFeature f;

            while (reader.hasNext()) {
                f = (SimpleFeature) reader.next();

                int index = ((Integer) f.getAttribute("id")).intValue() - 1;
                riverFeatures[index] = f;
                bounds.include(f.getBounds());
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
     * @param array
     *            DOCUMENT ME!
     * @param expected
     *            DOCUMENT ME!
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
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
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
        assertEquals("name", expected.getName(), actual.getName());

        // assertEquals( "compare", 0, DataUtilities.compare( expected, actual ));
        assertEquals("attributeCount", expected.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);
            assertEquals("attribute " + expectedAttribute.getLocalName(), expectedAttribute,
                    actualAttribute);
        }
        
        // make sure the geometry is nillable and has minOccurrs to 0
        AttributeDescriptor dg = actual.getGeometryDescriptor();
        assertTrue(dg.isNillable());
        assertEquals(0, dg.getMinOccurs());

        assertEquals(expected, actual);
    }
    
    public void testGetSchemaRoadUnregisteredGeomColumn() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);
        Statement st = conn.createStatement();
        int deleted = st.executeUpdate("DELETE FROM geometry_columns WHERE f_table_name = 'road' and f_table_schema = '" + f.schema + "'");
        assertEquals(1, deleted);
        
        testGetSchemaRoad();
    }


    public void testGetSchemaRiver() throws IOException {
        SimpleFeatureType expected = riverType;
        SimpleFeatureType actual = data.getSchema("river");
        assertEquals("name", expected.getName(), actual.getName());

        // assertEquals( "compare", 0, DataUtilities.compare( expected, actual ));
        assertEquals("attributeCount", expected.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < expected.getAttributeCount(); i++) {
            AttributeDescriptor expectedAttribute = expected.getDescriptor(i);
            AttributeDescriptor actualAttribute = actual.getDescriptor(i);
            assertEquals("attribute " + expectedAttribute.getLocalName(), expectedAttribute,
                    actualAttribute);
        }
        
        // make sure the geometry is nillable and has minOccurrs to 0
        AttributeDescriptor dg = actual.getGeometryDescriptor();
        assertTrue(dg.isNillable());
        assertEquals(0, dg.getMinOccurs());

        assertEquals(expected, actual);
    }

    public void testCreateSchema() throws Exception {
        String featureTypeName = "stuff";

        // delete the table, if it exists
        try {
            Connection conn = pool.getConnection();
            conn.setAutoCommit(true);
            Statement st = conn.createStatement();
            String sql = "DROP TABLE " + featureTypeName + ";";
            st.execute(sql);
            conn.close();
        } catch (Exception e) {
            // table didn't exist
        }

        // create a featureType and write it to PostGIS
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326"); // requires gt2-epsg-wkt
        
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);
        ftb.add("id", Integer.class);
        ftb.add("name", String.class);
        ftb.add("the_geom", Point.class, crs);
        ftb.add("long", Long.class);
        
        SimpleFeatureType newFT = ftb.buildFeatureType();
        data.createSchema(newFT);
        SimpleFeatureType newSchema = data.getSchema(featureTypeName);
        assertNotNull(newSchema);
        assertEquals(4, newSchema.getAttributeCount());
        assertEquals( Integer.class, newSchema.getDescriptor("id").getType().getBinding());
        assertEquals( String.class, newSchema.getDescriptor("name").getType().getBinding());
        assertEquals( Long.class, newSchema.getDescriptor("long").getType().getBinding());
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
            f = (SimpleFeature) i.next();
            assertTrue(msg + " " + f.getID(), c2.contains(f));
        }
    }

    public  FeatureReader<SimpleFeatureType, SimpleFeature> reader(String typeName) throws IOException {
        return data.getFeatureReader(new DefaultQuery(typeName, Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> writer(String typeName) throws IOException {
        return data.getFeatureWriter(typeName, Transaction.AUTO_COMMIT);
    }

    public void testGetFeatureReader() throws IOException, IllegalAttributeException {
        assertCovered(roadFeatures, reader("road"));
        assertEquals(3, count(reader("road")));
    }
    
    public void testGeometryFactoryHintsGF() throws IOException {
        SimpleFeatureSource fs = data.getFeatureSource("road");
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_GEOMETRY_FACTORY));
        
        DefaultQuery q = new DefaultQuery("road");
        GeometryFactory gf = new GeometryFactory(new LiteCoordinateSequenceFactory());
        Hints hints = new Hints(Hints.JTS_GEOMETRY_FACTORY, gf);
        q.setHints(hints);
        SimpleFeatureIterator it = fs.getFeatures(q).features();
        SimpleFeature f = (SimpleFeature) it.next();
        it.close();
        
        LineString ls = (LineString) f.getDefaultGeometry();
        assertTrue(ls.getCoordinateSequence() instanceof LiteCoordinateSequence);
    }
    
    public void testGeometryFactoryHintsCS() throws IOException {
        SimpleFeatureSource fs = data.getFeatureSource("road");
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_COORDINATE_SEQUENCE_FACTORY));
        
        DefaultQuery q = new DefaultQuery("road");
        Hints hints = new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY, new LiteCoordinateSequenceFactory());
        q.setHints(hints);
        SimpleFeatureIterator it = fs.getFeatures(q).features();
        SimpleFeature f = (SimpleFeature) it.next();
        it.close();
        
        LineString ls = (LineString) f.getDefaultGeometry();
        assertTrue(ls.getCoordinateSequence() instanceof LiteCoordinateSequence);
    }
    
    public void testGetFeatureReaderFilterPrePost() throws IOException, IllegalFilterException {
        Transaction t = new DefaultTransaction();
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        FilterFunction_geometryType geomTypeExpr = new FilterFunction_geometryType();
        geomTypeExpr.setArgs(new Expression[] { factory.createAttributeExpression("geom") });

        CompareFilter filter = factory.createCompareFilter(FilterType.COMPARE_EQUALS);
        filter.addLeftValue(geomTypeExpr);
        filter.addRightValue(factory.createLiteralExpression("Polygon"));

        reader = data.getFeatureReader(new DefaultQuery("road", filter), t);
        // if the above statement didn't throw an exception, we're content
        assertNotNull(reader);
        reader.close();
        t.close();
    }

    public void testGetFeatureReaderFilterPrePost2() throws IOException, IllegalFilterException {
        // GEOT-1069, make sure the post filter is run even if the geom property is not requested
        Transaction t = new DefaultTransaction();
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        FilterFunction_geometryType geomTypeExpr = new FilterFunction_geometryType();
        geomTypeExpr.setArgs(new Expression[] { factory.createAttributeExpression("geom") });

        CompareFilter filter = factory.createCompareFilter(FilterType.COMPARE_EQUALS);
        filter.addLeftValue(geomTypeExpr);
        filter.addRightValue(factory.createLiteralExpression("Polygon"));

        reader = data.getFeatureReader(new DefaultQuery("road", filter), t);
        // if the above statement didn't throw an exception, we're content
        assertNotNull(reader);
        reader.close();
        t.close();
    }
    
    /**
     * GEOT-2182, make sure no invalid fids are passed down to the database
     */
    public void testGetFeatureReaderWipesOutInvalidFilterFids() throws IOException, IllegalFilterException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        //grab some valid fids...
        reader = data.getFeatureReader(new DefaultQuery("road"), Transaction.AUTO_COMMIT);
        final String fid1, fid2, fid3;
        try{
            //roads contains initially 3 features, as set up in #dataSetup()
            fid1 = reader.next().getID();
            fid2 = reader.next().getID();
            fid3 = reader.next().getID();
        }finally{
            reader.close();
        }
        
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        
        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(factory.featureId(fid1));
        ids.add(factory.featureId(fid2));
        // the following one should be stripped out from the request. Its structure keeps being
        // <someString>.<number> but the FIDMapper.isValid() method should return false for it
        ids.add(factory.featureId("_" + fid3));
        
        org.opengis.filter.Filter filter = factory.id(ids);

        Transaction t = new DefaultTransaction();
        reader = data.getFeatureReader(new DefaultQuery("road", filter), t);
        assertNotNull(reader);
        try {
            Set<String> expected = new HashSet<String>();
            expected.add(fid1);
            expected.add(fid2);
            
            Set<String> returned = new HashSet<String>();
            assertTrue(reader.hasNext());
            returned.add( reader.next().getID());
            assertTrue(reader.hasNext());
            returned.add( reader.next().getID() );
            assertFalse("expected only 2 features", reader.hasNext());
            
            assertEquals(expected, returned);
        } finally {
            reader.close();
            t.close();
        }
    }

    public void testCaseInsensitiveFilter() throws Exception {
        final String riverName = riverType.getName().getLocalPart();
        SimpleFeatureSource rivers = data.getFeatureSource(riverName);
        org.opengis.filter.Filter caseSensitive = ff.equal(ff.property("river"), ff.literal("Rv1"), true);
        assertEquals(0, rivers.getCount(new DefaultQuery(riverName, caseSensitive)));
        org.opengis.filter.Filter caseInsensitive = ff.equal(ff.property("river"), ff.literal("Rv1"), false);
        assertEquals(1, rivers.getCount(new DefaultQuery(riverName, caseInsensitive)));
    }

    public void testGetFeatureReaderRetypeBug() throws Exception {
        // this is here to avoid http://jira.codehaus.org/browse/GEOT-1069
        // to come up again

        Transaction t = new DefaultTransaction();
        SimpleFeatureType type = data.getSchema("river");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        CompareFilter cf = ff.createCompareFilter(Filter.COMPARE_EQUALS);
        cf.addLeftValue(ff.createAttributeExpression("flow"));
        cf.addRightValue(ff.createLiteralExpression(4.5));

        DefaultQuery q = new DefaultQuery("river");
        q.setPropertyNames(new String[] { "geom" });
        q.setFilter(cf);

        // with GEOT-1069 an exception is thrown here
        reader = data.getFeatureReader(q, t);
        assertTrue(reader.hasNext());
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        reader.next();
        assertFalse(reader.hasNext());
        reader.close();
        t.close();
    }

    public void testGetFeatureReaderRetypeBug2() throws Exception {
        // this is here to avoid http://jira.codehaus.org/browse/GEOT-1069
        // to come up again

        Transaction t = new DefaultTransaction();
        SimpleFeatureType type = data.getSchema("river");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        CompareFilter cf = ff.createCompareFilter(Filter.COMPARE_EQUALS);
        FunctionExpression ceil = new FilterFunction_ceil();
        ceil.setArgs(new Expression[] { ff.createAttributeExpression("flow") });
        cf.addLeftValue(ceil);
        cf.addRightValue(ff.createLiteralExpression(5));

        DefaultQuery q = new DefaultQuery("river");
        q.setPropertyNames(new String[] { "geom" });
        q.setFilter(cf);

        // with GEOT-1069 an exception is thrown here
        reader = data.getFeatureReader(q, t);
        assertTrue(reader.hasNext());
        assertEquals(1, reader.getFeatureType().getAttributeCount());
        reader.next();
        assertFalse(reader.hasNext());
        reader.close();
        t.close();
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

    public void testGetFeatureReaderConcurrency() throws NoSuchElementException, IOException,
            IllegalAttributeException {
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

    public void testGetFeatureReaderFilterAutoCommit() throws NoSuchElementException, IOException,
            IllegalAttributeException {
        SimpleFeatureType type = data.getSchema("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertFalse(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE),
                Transaction.AUTO_COMMIT);
        assertFalse(reader.hasNext());

        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));

        reader = data
                .getFeatureReader(new DefaultQuery("road", rd1Filter), Transaction.AUTO_COMMIT);

        // assertTrue(reader instanceof FilteringFeatureReader);
        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));
    }

    public void testGetFeatureReaderFilterTransaction() throws NoSuchElementException, IOException,
            IllegalAttributeException {
        Transaction t = new DefaultTransaction();
        SimpleFeatureType type = data.getSchema("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), t);
        assertFalse(reader.hasNext());
        assertEquals(type, reader.getFeatureType());
        assertEquals(0, count(reader));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t);
        assertEquals(type, reader.getFeatureType());
        assertEquals(roadFeatures.length, count(reader));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), t);
        assertEquals(type, reader.getFeatureType());
        assertEquals(1, count(reader));
        reader.close();

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Filter.INCLUDE, t);
        SimpleFeature feature;

        while (writer.hasNext()) {
            feature = (SimpleFeature) writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                writer.remove();
            }
        }
        writer.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), t);
        assertEquals(0, count(reader));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t);
        assertEquals(roadFeatures.length - 1, count(reader));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), t);
        assertEquals(0, count(reader));
        reader.close();

        t.rollback();
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.EXCLUDE), t);
        assertEquals(0, count(reader));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t);
        assertEquals(roadFeatures.length, count(reader));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", rd1Filter), t);
        assertEquals(1, count(reader));
        reader.close();
        t.close();
    }

    /**
     * Ensure readers contents equal those in the feature array
     * 
     * @param features
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * @throws NoSuchElementException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalAttributeException
     *             DOCUMENT ME!
     */
    void assertCovered(SimpleFeature[] features,  FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws NoSuchElementException,
            IOException, IllegalAttributeException {
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
     * <p>
     * Implemented using match on attribute types, not feature id
     * </p>
     * 
     * @param array
     *            DOCUMENT ME!
     * @param reader
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    void assertMatched(SimpleFeature[] array,  FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws Exception {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = (SimpleFeature) reader.next();
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
     * Ensure that  FeatureReader<SimpleFeatureType, SimpleFeature> reader contains exactly the contents of array.
     * 
     * @param reader
     *            DOCUMENT ME!
     * @param array
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws NoSuchElementException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     * @throws IllegalAttributeException
     *             DOCUMENT ME!
     */
    boolean covers(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array) throws NoSuchElementException,
            IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = (SimpleFeature) reader.next();

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

    boolean covers(SimpleFeatureIterator reader, SimpleFeature[] array) throws NoSuchElementException,
            IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = (SimpleFeature) reader.next();

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

    boolean coversLax(FeatureReader <SimpleFeatureType, SimpleFeature> reader, SimpleFeature[] array) throws NoSuchElementException,
            IOException, IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = (SimpleFeature) reader.next();

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

    void dump(String message,  FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws NoSuchElementException, IOException,
            IllegalAttributeException {
        SimpleFeature feature;
        int count = 0;

        try {
            while (reader.hasNext()) {
                feature = (SimpleFeature) reader.next();

                String msg = message + ": feture " + count + "=" + feature;

                // LOGGER.info( msg );
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

            // LOGGER.info( msg );
            System.out.println(msg);
        }
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, Filter, Transaction)
     */
    public void xtestGetFeatureWriter() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Filter.INCLUDE,
                Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length, count(writer));
    }

    public void testGetFeatureWriterClose() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriter("road", Filter.INCLUDE,
                Transaction.AUTO_COMMIT);

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
            feature = (SimpleFeature) writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                writer.remove();
            }
        }
        writer.close();

        assertEquals(roadFeatures.length - 1, count("road"));
    }

    public void testGetFeatureWriterRemoveAll() throws IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = writer("road");
        SimpleFeature feature;

        try {
            while (writer.hasNext()) {
                feature = (SimpleFeature) writer.next();
                writer.remove();
            }
        } finally {
            writer.close();
        }

        assertEquals(0, count("road"));
    }

    public int count(String typeName) throws IOException {
        // return count(reader(typeName));
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
        writer.close();
        assertEquals(roadFeatures.length + 1, count("road"));
    }

    /**
     * Search for feature based on AttributeDescriptor.
     * <p>
     * If attributeName is null, we will search by feature.getID()
     * </p>
     * <p>
     * The provided reader will be closed by this operations.
     * </p>
     * 
     * @param reader
     *            reader to search through
     * @param attributeName
     *            attributeName, or null for featureID
     * @param value
     *            value to match
     * @return Feature
     * @throws NoSuchElementException
     *             if a match could not be found
     * @throws IOException
     *             We could not use reader
     * @throws IllegalAttributeException
     *             if attributeName did not match schema
     */
    public SimpleFeature findFeature(FeatureReader <SimpleFeatureType, SimpleFeature> reader, String attributeName, Object value)
            throws NoSuchElementException, IOException, IllegalAttributeException {
        SimpleFeature f;

        try {
            while (reader.hasNext()) {
                f = (SimpleFeature) reader.next();

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

    public SimpleFeature feature(String typeName, String fid) throws NoSuchElementException, IOException,
            IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = reader(typeName);
        SimpleFeature f;

        try {
            while (reader.hasNext()) {
                f = (SimpleFeature) reader.next();

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
            feature = (SimpleFeature) writer.next();

            if (feature.getID().equals(roadFeatures[0].getID())) {
                feature.setAttribute("name", "changed");
                writer.write();
            }
        }
        writer.close();

        feature = (SimpleFeature) feature("road", roadFeatures[0].getID());
        assertNotNull(feature);
        assertEquals("changed", feature.getAttribute("name"));
    }

    public void testGetFeatureWriterTypeNameTransaction() throws NoSuchElementException,
            IOException, IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriter("road", Transaction.AUTO_COMMIT);
        assertEquals(roadFeatures.length, count(writer));

        // writer.close(); called by count.
    }

    public void testGetFeatureWriterAppendTypeNameTransaction() throws Exception {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;

        writer = data.getFeatureWriterAppend("road", Transaction.AUTO_COMMIT);
        assertEquals(0, count(writer));

        // writer.close(); called by count
    }

    /*
     * Test for FeatureWriter getFeatureWriter(String, boolean, Transaction) @task REVISIT:
     * JDBCDataStore currently does not return these proper instanceof's. If we want to guarantee
     * that people can't append to a request with a FeatureWriter then we could add the
     * functionality to JDBCDataStore by having getFeatureWriter(.. Filter ...) check to see if the
     * FeatureWriter returned is instanceof FilteringFeatureWriter, and if not then just wrap it in
     * a FilteringFeatureWriter(writer, Filter.INCLUDE). I think it'd be a bit of unnecessary
     * overhead, but if we want it it's easy to do. It will guarantee that calls with Filter won't
     * ever append. Doing with Filter.INCLUDE, however, would require a bit of reworking, as the
     * Filter getFeatureWriter is currently where we do the bulk of the work.
     */
    public void testGetFeatureWriterFilter() throws NoSuchElementException, IOException,
            IllegalAttributeException {
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        writer = data.getFeatureWriter("road", Filter.EXCLUDE, Transaction.AUTO_COMMIT);

        // see task above
        // assertTrue(writer instanceof EmptyFeatureWriter);
        assertEquals(0, count(writer));

        writer = data.getFeatureWriter("road", Filter.INCLUDE, Transaction.AUTO_COMMIT);

        // assertFalse(writer instanceof FilteringFeatureWriter);
        assertEquals(roadFeatures.length, count(writer));

        writer = data.getFeatureWriter("road", rd1Filter, Transaction.AUTO_COMMIT);

        // assertTrue(writer instanceof FilteringFeatureWriter);
        assertEquals(1, count(writer));
    }

    /**
     * Test two transactions one removing feature, and one adding a feature.
     * 
     * @throws IllegalAttributeException
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testGetFeatureWriterTransaction() throws Exception {
        Transaction t1 = new DefaultTransaction();
        Transaction t2 = new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 = data.getFeatureWriter("road", rd1Filter, t1);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 = data.getFeatureWriterAppend("road", t2);

        SimpleFeatureType road = data.getSchema("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        SimpleFeature feature;
        SimpleFeature[] ORIGINAL = roadFeatures;
        SimpleFeature[] REMOVE = new SimpleFeature[ORIGINAL.length - 1];
        SimpleFeature[] ADD = new SimpleFeature[ORIGINAL.length + 1];
        SimpleFeature[] FINAL = new SimpleFeature[ORIGINAL.length];
        int i;
        int index;
        index = 0;

        for (i = 0; i < ORIGINAL.length; i++) {
            feature = ORIGINAL[i];

            if (!feature.getID().equals(roadFeatures[0].getID())) {
                REMOVE[index++] = feature;
            }
        }

        for (i = 0; i < ORIGINAL.length; i++) {
            ADD[i] = ORIGINAL[i];
        }

        ADD[i] = newRoad; // will need to update with Fid from database

        for (i = 0; i < REMOVE.length; i++) {
            FINAL[i] = REMOVE[i];
        }

        FINAL[i] = newRoad; // will need to update with Fid from database

        // start off with ORIGINAL
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue("Sanity check failed: before modification reader didn't match original content",
                covers(reader, ORIGINAL));
        reader.close();

        // writer 1 removes road.rd1 on t1
        // -------------------------------
        // - tests transaction independence from DataStore
        while (writer1.hasNext()) {
            feature = (SimpleFeature) writer1.next();
            assertEquals(roadFeatures[0].getID(), feature.getID());
            writer1.remove();
        }

        // still have ORIGINAL and t1 has REMOVE
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue("Feature deletion managed to leak out of transaction?", covers(reader, ORIGINAL));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t1);
        assertTrue(covers(reader, REMOVE));
        reader.close();

        // close writer1
        // --------------
        // ensure that modification is left up to transaction commmit
        writer1.close();

        // We still have ORIGIONAL and t1 has REMOVE
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue(covers(reader, ORIGINAL));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t1);
        assertTrue(covers(reader, REMOVE));
        reader.close();

        // writer 2 adds road.rd4 on t2
        // ----------------------------
        // - tests transaction independence from each other
        feature = (SimpleFeature) writer2.next();
        feature.setAttributes(newRoad.getAttributes());
        writer2.write();

        // HACK: ?!? update ADD and FINAL with new FID from database
        //
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t2);
        newRoad = findFeature(reader, "id", new Integer(4));
        System.out.println("newRoad:" + newRoad);
        ADD[ADD.length - 1] = newRoad;
        FINAL[FINAL.length - 1] = newRoad;
        reader.close();

        // We still have ORIGINAL and t2 has ADD
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue(covers(reader, ORIGINAL));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t2);
        assertMatched(ADD, reader); // broken due to FID problem
        reader.close();

        writer2.close();

        // Still have ORIGIONAL and t2 has ADD
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue(covers(reader, ORIGINAL));
        reader.close();
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t2);
        assertTrue(coversLax(reader, ADD));
        reader.close();

        // commit t1
        // ---------
        // -ensure that delayed writing of transactions takes place
        //
        t1.commit();

        // We now have REMOVE, as does t1 (which has not additional diffs)
        // t2 will have FINAL
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue(covers(reader, REMOVE));
        reader.close();
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t1);
        assertTrue(covers(reader, REMOVE));
        reader.close();
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t2);
        assertTrue(coversLax(reader, FINAL));
        reader.close();

        // commit t2
        // ---------
        // -ensure that everyone is FINAL at the end of the day
        t2.commit();

        // We now have Number( remove one and add one)
        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE),
                Transaction.AUTO_COMMIT);
        assertTrue(coversLax(reader, FINAL));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t1);
        assertTrue(coversLax(reader, FINAL));
        reader.close();

        reader = data.getFeatureReader(new DefaultQuery("road", Filter.INCLUDE), t2);
        assertTrue(coversLax(reader, FINAL));
        reader.close();

        t1.close();
        t2.close();
    }

    /**
     * Tests that if 2 transactions attempt to modify the same feature without committing, that the
     * second transaction does not lock up waiting to obtain the lock.
     * 
     * @author chorner
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws SQLException
     */
    public void testGetFeatureWriterConcurrency() throws Exception {
        // if we don't have postgres >= 8.1, don't bother testing (it WILL block)
        Connection conn = null;
        try {
            conn = pool.getConnection();
            int major = conn.getMetaData().getDatabaseMajorVersion();
            int minor = conn.getMetaData().getDatabaseMinorVersion();
            if (!((major > 8) || ((major == 8) && minor >= 1))) {
                return; // concurrency support is weak
            }
        } finally {
            if (conn != null)
                conn.close();
        }
        Transaction t1 = new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer1 = data.getFeatureWriter("road", rd1Filter, t1);
        SimpleFeature f1 = (SimpleFeature) writer1.next();
        f1.setAttribute("name", new String("r1_"));
        writer1.write();

        Transaction t2 = new DefaultTransaction();
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer2 = data.getFeatureWriter("road", rd1Filter, t2);
        SimpleFeature f2 = (SimpleFeature) writer2.next();
        f2.setAttribute("name", new String("r1__"));
        try {
            writer2.write(); // this will either lock up or toss chunks
            fail("Feature lock should have failed");
        } catch (FeatureLockException e) {
            // success (test-wise... our write failed quite well too)
            assertEquals("road.rd1", e.getFeatureID());
        }

        t1.rollback(); // don't save
        writer1.close();
        t1.close();

        t2.rollback();
        writer2.close();
        t2.close();
    }

    // Feature Source Testing
    public void testGetFeatureSourceRoad() throws IOException {
        SimpleFeatureSource road = data.getFeatureSource("road");

        assertEquals(roadType, road.getSchema());
        assertSame(data, road.getDataStore());

        int count = road.getCount(Query.ALL);
        assertTrue((count == 3) || (count == -1));

        ReferencedEnvelope bounds = road.getBounds(Query.ALL);
        assertTrue((bounds == null) || new Envelope(bounds).equals(roadBounds));

        SimpleFeatureCollection all = road.getFeatures();
        assertEquals(3, all.size());
        assertEquals(roadBounds, new Envelope(all.getBounds()));

        SimpleFeatureCollection expected = DataUtilities.collection(roadFeatures);

        assertCovers("all", expected, all);
        assertEquals(roadBounds, new Envelope(all.getBounds()));

        SimpleFeatureCollection some = road.getFeatures(rd12Filter);
        assertEquals(2, some.size());

        ReferencedEnvelope e = new ReferencedEnvelope();
        e.include(roadFeatures[0].getBounds());
        e.include(roadFeatures[1].getBounds());
        assertEquals(e, new Envelope(some.getBounds()));
        assertEquals(some.getSchema(), road.getSchema());

        DefaultQuery query = new DefaultQuery("road", rd12Filter, new String[] { "name" });

        SimpleFeatureCollection half = road.getFeatures(query);
        assertEquals(2, half.size());
        assertEquals(1, half.getSchema().getAttributeCount());

        SimpleFeatureIterator reader = half.features();
        SimpleFeatureType type = half.getSchema();
        reader.close();

        SimpleFeatureType actual = half.getSchema();

        assertEquals(type.getName(), actual.getName());
        assertEquals(type.getAttributeCount(), actual.getAttributeCount());

        for (int i = 0; i < type.getAttributeCount(); i++) {
            assertEquals(type.getDescriptor(i), actual.getDescriptor(i));
        }

        assertNull(type.getGeometryDescriptor()); // geometry is null, therefore no bounds
        assertEquals(type.getGeometryDescriptor(), actual.getGeometryDescriptor());
        assertEquals(type, actual);

        ReferencedEnvelope b = half.getBounds();
        ReferencedEnvelope expectedBounds = isEnvelopeComputingEnabled() ? roadBounds : new ReferencedEnvelope();
        assertEquals(expectedBounds, new Envelope(b)); 
    }
    
    public void testBoundsReproject() throws Exception {
        SimpleFeatureSource road = data.getFeatureSource("road");
        assertEquals(new Envelope(roadBounds), road.getBounds());
        
        CoordinateReferenceSystem epsg4326 = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem epsg3003 = CRS.decode("EPSG:3003");
        ReferencedEnvelope rbOriginal = new ReferencedEnvelope(roadBounds, epsg4326);
        ReferencedEnvelope rbReprojected = rbOriginal.transform(epsg3003, true);
        DefaultQuery q = new DefaultQuery("road");
        q.setCoordinateSystem(epsg4326);
        q.setCoordinateSystemReproject(epsg3003);
        assertEquals(rbReprojected, road.getBounds(q));
    }

    /**
     * Return true if the datastore is capable of computing the road bounds given a query
     * 
     * @return
     */
    protected boolean isEnvelopeComputingEnabled() {
        if ( data instanceof PostgisDataStore && ((PostgisDataStore)data).isEstimatedExtent()) {
        	return true;
        }
        
    	return false;
    }

    public void testGetFeatureSourceRiver() throws NoSuchElementException, IOException,
            IllegalAttributeException {
        SimpleFeatureSource river = data.getFeatureSource("river");

        assertEquals(riverType, river.getSchema());
        assertSame(data, river.getDataStore());

        SimpleFeatureCollection all = river.getFeatures();
        assertEquals(2, all.size());
        assertEquals(riverBounds, new Envelope(all.getBounds()));
        assertTrue("rivers", covers(all.features(), riverFeatures));

        SimpleFeatureCollection expected = DataUtilities.collection(riverFeatures);
        assertCovers("all", expected, all);
        assertEquals(riverBounds, new Envelope(all.getBounds()));
    }

    public void testGetFeaturesSortBy() throws NoSuchElementException, IOException,
    IllegalAttributeException {
        final org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        
        try{
            DefaultQuery query = new DefaultQuery(riverType.getTypeName());
            query.setSortBy(new SortBy[]{ff.sort("nonExistentProperty", SortOrder.ASCENDING)});
            final SimpleFeatureSource river = data.getFeatureSource("river");            
            river.getFeatures(query);
            fail("Expected a datasource exception while asking to sort by a non existent attribute");
        }catch(DataSourceException e){
            assertTrue(true);
        }

        final String rv1 = "rv1";
        final String rv2 = "rv2";

        testSortBy(new SortBy[]{ff.sort("river", SortOrder.ASCENDING)}, rv1, rv2);
        
        testSortBy(new SortBy[]{ff.sort("river", SortOrder.DESCENDING)}, rv2, rv1);

        testSortBy(new SortBy[]{SortBy.NATURAL_ORDER}, rv1, rv2);

        testSortBy(new SortBy[]{SortBy.REVERSE_ORDER}, rv2, rv1);

        //may the attribute name contain a namespace prefix
        testSortBy(new SortBy[]{ff.sort("geot:river", SortOrder.ASCENDING)}, rv1, rv2);
    }

    /**
     * Fetches the features from the river test data using the provided sort order and asserts
     * the "river" property matches the <code>riverAtt1</code> and <code>riverAtt2</code> values,
     * in that order.
     */
    private void testSortBy(final SortBy[]sortBy, final String riverAtt1, final String riverAtt2) throws IOException {

        SimpleFeatureSource river = data.getFeatureSource("river");
        
        DefaultQuery query = new DefaultQuery(riverType.getTypeName());
        query.setSortBy(sortBy);
        
        SimpleFeatureCollection features;
        SimpleFeature f1;
        SimpleFeature f2;
        Iterator<SimpleFeature> iterator;
        features = river.getFeatures(query);
        iterator = features.iterator();
        try{
            f1 = iterator.next();
            f2 = iterator.next();
            assertEquals(riverAtt1, f1.getAttribute("river"));
            assertEquals(riverAtt2, f2.getAttribute("river"));
        }finally{
           features.close(iterator);
        }
    }

    public void testGetFeaturesPaging() throws NoSuchElementException, IOException,
    IllegalAttributeException {
        SimpleFeatureSource river = data.getFeatureSource("river");
        
        org.opengis.filter.FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        
        DefaultQuery query = new DefaultQuery(riverType.getTypeName());

        SimpleFeatureCollection features;
        SimpleFeature f1, f2;
        
        query.setStartIndex(0);
        query.setMaxFeatures(1);
        
        features = river.getFeatures(query);
        Iterator<SimpleFeature> iterator = features.iterator();
        try{
            f1 = iterator.next();
            assertEquals(1, ((Integer)f1.getAttribute("id")).intValue());
            assertFalse(iterator.hasNext());
        }finally{
           features.close(iterator);
        }

        query.setStartIndex(1);
        query.setMaxFeatures(1);
        
        features = river.getFeatures(query);
        iterator = features.iterator();
        try{
            f1 = iterator.next();
            assertEquals(2, ((Integer)f1.getAttribute("id")).intValue());
            assertFalse(iterator.hasNext());
        }finally{
           features.close(iterator);
        }

        //now with order by
        query.setStartIndex(1);
        query.setMaxFeatures(1);
        query.setSortBy(new SortBy[]{ff.sort("id", SortOrder.DESCENDING)});
        
        features = river.getFeatures(query);
        iterator = features.iterator();
        try{
            f1 = iterator.next();
            assertEquals(1, ((Integer)f1.getAttribute("id")).intValue());
            assertFalse(iterator.hasNext());
        }finally{
           features.close(iterator);
        }
    }

    //
    // Feature Store Testing
    //
    public void testGetFeatureStoreModifyFeatures1() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        // FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        // rd1Filter = factory.createFidFilter( roadFeatures[0].getID() );
        Object changed = new Integer(5);
        AttributeDescriptor name = roadType.getDescriptor("id");
        road.modifyFeatures(name, changed, rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        SimpleFeatureIterator features = results.features();
        assertEquals(changed, features.next().getAttribute("id"));
        results.close(features);
    }

    public void testGetFeatureStoreModifyFeatures2() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        rd1Filter = factory.createFidFilter(roadFeatures[0].getID());

        AttributeDescriptor name = roadType.getDescriptor("name");
        road.modifyFeatures(new AttributeDescriptor[] { name, }, new Object[] { "changed", }, rd1Filter);

        SimpleFeatureCollection results = road.getFeatures(rd1Filter);
        SimpleFeatureIterator features = results.features();
        assertEquals("changed", features.next().getAttribute("name"));
        results.close(features);
    }

    /**
     * Test with a filter that won't be matched after the modification is done, was throwing an NPE
     * before the fix
     * 
     * @throws IOException
     */
    public void testGetFeatureStoreModifyFeatures3() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        PropertyIsEqualTo filter = ff.equals(ff.property("name"), ff.literal("r1"));

        AttributeDescriptor name = roadType.getDescriptor("name");
        road.modifyFeatures(new AttributeDescriptor[] { name, }, new Object[] { "changed", }, filter);
    }

    public void testGetFeatureStoreRemoveFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        road.removeFeatures(rd1Filter);
        assertEquals(0, road.getFeatures(rd1Filter).size());
        assertEquals(roadFeatures.length - 1, road.getFeatures().size());
    }

    public void testGetFeatureStoreRemoveAllFeatures() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        road.removeFeatures(Filter.INCLUDE);
        assertEquals(0, road.getFeatures().size());
    }
    
    public void testGetFeatureStoreRemoveIntersects() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
        final PropertyName property = ff.property(roadType.getGeometryDescriptor().getLocalName());
        // this should match only rd1
        final Literal polygon = ff.literal(JTS.toGeometry(new Envelope(1, 2, 1, 2)));

        int size = road.getFeatures().size();
        road.removeFeatures(ff.intersects(property, polygon));
        assertEquals(size - 1, road.getFeatures().size());
    }
    
    public void testGetFeatureStoreUpdateIntersects() throws IOException {
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
        final PropertyName property = ff.property(roadType.getGeometryDescriptor().getLocalName());
        // this should match only rd1
        final Literal polygon = ff.literal(JTS.toGeometry(new Envelope(1, 2, 1, 2)));

        road.modifyFeatures(roadType.getDescriptor("name"), "r1.updated", ff.intersects(property, polygon));
        FeatureCollection fc = road.getFeatures(ff.id(Collections.singleton(ff.featureId("road.rd1"))));
        SimpleFeature rd1 = (SimpleFeature) fc.toArray()[0];
        assertEquals("r1.updated", rd1.getAttribute("name"));
    }
    

    public void testGetFeatureStoreAddFeatures() throws IOException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(new SimpleFeature[] { newRoad, });
        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        road.addFeatures(DataUtilities.collection(reader));
        assertEquals(roadFeatures.length + 1, count("road"));
    }

    public void testGetFeatureStoreSetFeatures() throws NoSuchElementException, IOException,
            IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(new SimpleFeature[] { newRoad, });

        SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");

        assertEquals(3, count("road"));

        road.setFeatures(reader);

        assertEquals(1, count("road"));
    }

    // public void testGetFeatureStoreTransactionSupport()
    // throws Exception {
    // Transaction t1 = new DefaultTransaction();
    // Transaction t2 = new DefaultTransaction();
    //
    // SimpleFeatureStore road = (SimpleFeatureStore) data.getFeatureSource("road");
    // FeatureStore road1 = (SimpleFeatureStore) data.getFeatureSource("road");
    // FeatureStore road2 = (SimpleFeatureStore) data.getFeatureSource("road");
    //
    // road1.setTransaction(t1);
    // road2.setTransaction(t2);
    //
    // Feature feature;
    // Feature[] ORIGIONAL = roadFeatures;
    // Feature[] REMOVE = new Feature[ORIGIONAL.length - 1];
    // Feature[] ADD = new Feature[ORIGIONAL.length + 1];
    // Feature[] FINAL = new Feature[ORIGIONAL.length];
    // int i;
    // int index;
    // index = 0;
    //
    // for (i = 0; i < ORIGIONAL.length; i++) {
    // feature = ORIGIONAL[i];
    // LOGGER.info("id is " + feature.getID());
    //
    // if (!feature.getID().equals("road.rd1")) {
    // REMOVE[index++] = feature;
    // }
    // }
    //
    // for (i = 0; i < ORIGIONAL.length; i++) {
    // ADD[i] = ORIGIONAL[i];
    // }
    //
    // ADD[i] = newRoad;
    //
    // for (i = 0; i < REMOVE.length; i++) {
    // FINAL[i] = REMOVE[i];
    // }
    //
    // FINAL[i] = newRoad;
    //
    // // start of with ORIGINAL
    // assertTrue(covers(road.getFeatures().reader(), ORIGIONAL));
    //
    // // road1 removes road.rd1 on t1
    // // -------------------------------
    // // - tests transaction independence from DataStore
    // road1.removeFeatures(rd1Filter);
    //
    // // still have ORIGIONAL and t1 has REMOVE
    // assertTrue(covers(road.getFeatures().reader(), ORIGIONAL));
    // assertTrue(covers(road1.getFeatures().reader(), REMOVE));
    //
    // // road2 adds road.rd4 on t2
    // // ----------------------------
    // // - tests transaction independence from each other
    //  FeatureReader<SimpleFeatureType, SimpleFeature> reader = DataUtilities.reader(new Feature[] { newRoad, });
    // road2.addFeatures(reader);
    //
    // // We still have ORIGIONAL, t1 has REMOVE, and t2 has ADD
    // assertTrue(covers(road.getFeatures().reader(), ORIGIONAL));
    // assertTrue(covers(road1.getFeatures().reader(), REMOVE));
    // assertTrue(coversLax(road2.getFeatures().reader(), ADD));
    //
    // // commit t1
    // // ---------
    // // -ensure that delayed writing of transactions takes place
    // //
    // t1.commit();
    //
    // // We now have REMOVE, as does t1 (which has not additional diffs)
    // // t2 will have FINAL
    // assertTrue(covers(road.getFeatures().reader(), REMOVE));
    // assertTrue(covers(road1.getFeatures().reader(), REMOVE));
    // assertTrue(coversLax(road2.getFeatures().reader(), FINAL));
    //
    // // commit t2
    // // ---------
    // // -ensure that everyone is FINAL at the end of the day
    // t2.commit();
    //
    // // We now have Number( remove one and add one)
    // assertTrue(coversLax(road.getFeatures().reader(), FINAL));
    // assertTrue(coversLax(road1.getFeatures().reader(), FINAL));
    // assertTrue(coversLax(road2.getFeatures().reader(), FINAL));
    // }

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
        t.close();
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

        t1.close();
        t2.close();
    }

    public void testGetFeatureLockingExpire() throws Exception {
        FeatureLock lock = FeatureLockFactory.generate("Timed", 1000);

        FeatureLocking<SimpleFeatureType, SimpleFeature> road = (FeatureLocking<SimpleFeatureType, SimpleFeature>) data.getFeatureSource("road");
        road.setFeatureLock(lock);
        assertFalse(isLocked("road", "road.rd1"));

        road.lockFeatures(rd1Filter);
        assertTrue(isLocked("road", "road.rd1"));
        long then = System.currentTimeMillis();
        do {
            Thread.sleep( 1000 );
        } while ( System.currentTimeMillis() - then < 1000 ); 
        assertFalse(isLocked("road", "road.rd1"));
    }

    public void testOidFidMapper() throws IOException, IllegalAttributeException {
        // get the schema and make sure the FID mapper is an OID one
        FIDMapper mapper = ((PostgisDataStore) data).getFIDMapper("lake");
        FIDMapper base = null;
        if (mapper instanceof TypedFIDMapper) {
            base = ((TypedFIDMapper) mapper).getWrappedMapper();
        } else
            base = mapper;

        assertTrue(base instanceof OIDFidMapper);

        // read features from the database, just check we don't crash and that id's are not null
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = ((PostgisDataStore) data).getFeatureReader(data.getSchema("lake"),
                Filter.INCLUDE, Transaction.AUTO_COMMIT);

        while (reader.hasNext()) {
            SimpleFeature f = (SimpleFeature) reader.next();
            assertNotNull(f.getID());
        }
        reader.close();

        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = data.getFeatureWriterAppend("lake", Transaction.AUTO_COMMIT);
        SimpleFeature f = (SimpleFeature) writer.next();
        f.setAttributes(lakeFeatures[0].getAttributes());
        writer.write();
        writer.close();

        String id = f.getID();
        assertNotNull(id);
        assertTrue(!id.trim().equals(""));
        Long.parseLong(id.substring(5)); // make sure it's a number
    }

}
