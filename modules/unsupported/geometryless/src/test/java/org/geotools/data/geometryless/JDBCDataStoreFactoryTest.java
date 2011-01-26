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
package org.geotools.data.geometryless;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

import junit.framework.TestCase;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FeatureSource;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Point;

/**
 * Test Params used by JDBCDataStoreFactory.
 * 
 * @auther Rob Atkinson, Social Change Online
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: aaime $ (last modification)
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/geometryless/src/test/java/org/geotools/data/geometryless/JDBCDataStoreFactoryTest.java $
 * @version $Id: JDBCDataStoreFactoryTest.java 17707 2006-01-23 03:45:14Z
 *          desruisseaux $
 */
public class JDBCDataStoreFactoryTest extends TestCase {
    static JDBCDataStoreFactory factory = new JDBCDataStoreFactory(null);

    Map local;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {

        local = new HashMap();
        local.put("dbtype", "jdbc");

        PropertyResourceBundle resource = new PropertyResourceBundle(this.getClass()
                .getResourceAsStream("fixture.properties"));

        String namespace = resource.getString("namespace");

        String user = resource.getString("user");
        local.put("user", user);
        String password = resource.getString("password");
        local.put("passwd", password);

        String Driver = resource.getString("driver");
        local.put("driver", Driver);

        String urlprefix = resource.getString("urlprefix");
        local.put("urlprefix", urlprefix);

        if (namespace.equals("http://www.geotools.org/data/postgis")) {
            throw new IllegalStateException(
                    "The fixture.properties file needs to be configured for your own database");
        }

        super.setUp();

    }

    public void testParamCHARSET() throws Throwable {
        Param p = JDBCDataStoreFactory.CHARSET;
        try {
            p.parse(null);
            fail("expected error for parse null");
        } catch (Exception e) {
        }

        try {
            p.parse("");
            fail("expected error for parse empty");
        } catch (Exception e) {
        }
        assertNotNull("parse ISO-8859-1", p.parse("ISO-8859-1"));

        assertNull("handle null", p.handle(null));
        assertNull("handle empty", p.handle(""));
        assertNotNull("handle ISO-8859-1", p.handle("ISO-8859-1"));

        Map map = new HashMap();
        Charset latin1 = Charset.forName("ISO-8859-1");
        map.put("charset", latin1);
        assertEquals(latin1, p.lookUp(map));

        try {
            assertNotNull("handle ISO-LATIN-1", p.handle("ISO-LATIN-1"));
        } catch (IOException expected) {
        }
        System.out.println(latin1.toString());
        System.out.println(latin1.name());
        System.out.println(p.text(latin1));
        assertEquals("ISO-8859-1", p.text(latin1));
        try {
            assertEquals("ISO-8859-1", p.text("ISO-8859-1"));
            fail("Should not handle bare text");
        } catch (ClassCastException expected) {
        }
    }

    public void testLocal() throws Exception {
        Map map = local;
        System.out.println("local:" + map);

        assertTrue("canProcess", factory.canProcess(map));
        try {
            DataStore temp = factory.createDataStore(map);
            assertNotNull("created", temp);
        } catch (DataSourceException expected) {
            assertEquals("Could not get connection", expected.getMessage());
        }
    }

    public void testRegisterViewsJDBCDS() throws Exception {
        testRegisterViews(factory);
    }

    public void testRegisterViewsLocationXY() throws Exception {
        Map params = new HashMap(this.local);

        DataStoreFactorySpi factory = new LocationsXYDataStoreFactory();
        params.put(LocationsXYDataStoreFactory.DBTYPE.key, "locationsxy");
        params.put(LocationsXYDataStoreFactory.XCOLUMN.key, "x");
        params.put(LocationsXYDataStoreFactory.YCOLUMN.key, "y");
        params.put(LocationsXYDataStoreFactory.GEOMNAME.key, "location");

        assertTrue(factory.canProcess(params));

        this.local = params;
        DataStore ds = testRegisterViews(factory);
        assertTrue(ds instanceof LocationsXYDataStore);
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = ds.getFeatureSource("ViewType1");
        assertNotNull(fs);
        SimpleFeatureType schema = fs.getSchema();
        assertNotNull(schema);
        GeometryDescriptor defaultGeometry = (GeometryDescriptor) schema.getGeometryDescriptor();
        assertNotNull("No default geometry: " + schema.toString(), defaultGeometry);
        assertEquals("location", defaultGeometry.getName().getLocalPart());
        
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = fs.getFeatures();
        assertNotNull(features);
        FeatureIterator<SimpleFeature> iterator = features.features();
        while(iterator.hasNext()){
            SimpleFeature next = iterator.next();
            assertNotNull(next);
            Object location = next.getAttribute("location");
            assertNotNull(location);
            assertTrue(location instanceof Point);
        }
        features.close(iterator);
    }

    /*
     * public void testRegisterViewsLocationXYWaterQ()throws Exception{ Map
     * params = new HashMap(this.local);
     * 
     * DataStoreFactorySpi factory = new LocationsXYDataStoreFactory();
     * params.put(LocationsXYDataStoreFactory.DBTYPE.key, "locationsxy");
     * params.put(LocationsXYDataStoreFactory.XCOLUMN.key, "x");
     * params.put(LocationsXYDataStoreFactory.YCOLUMN.key, "y");
     * params.put(LocationsXYDataStoreFactory.GEOMNAME.key, "location");
     * 
     * params.put("sqlView.1.typeName", "wq_derived");
     * params.put("sqlView.1.sqlQuery", "SELECT station_no, station_name as
     * sitename,anzlic_no, " + " project_no, sample_collection_date ||
     * determinand_code as id," + " sample_collection_date,
     * determinand_description, results_value," + " longitude as x, -latitude as
     * y FROM wq_ir_results WHERE 1=1" + " order by station_no");
     * 
     * DataStore ds = DataStoreFinder.getDataStore(params); assertNotNull(ds);
     * assertTrue(ds instanceof LocationsXYDataStore);
     * 
     * FeatureSource<SimpleFeatureType, SimpleFeature> fs = ds.getFeatureSource("wq_derived"); assertNotNull(fs);
     * FeatureType schema = fs.getSchema(); assertNotNull(schema);
     * assertNotNull(schema.toString(), schema.getDefaultGeometry());
     * assertEquals("location",
     * schema.getDefaultGeometry().getName().getLocalPart());
     * 
     * FeatureCollection<SimpleFeatureType, SimpleFeature> fc = fs.getFeatures(Query.ALL); assertNotNull(fc);
     * Iterator features = fc.features(); assertNotNull(features);
     * assertTrue(features.hasNext()); Feature f =(Feature) features.next();
     * 
     * Filter filter = (Filter)ExpressionBuilder.parse("station_no =
     * '41010901'"); features = fs.getFeatures(filter).features();
     * assertNotNull(features); assertTrue(features.hasNext()); f =
     * (Feature)features.next(); assertEquals("41010901",
     * f.getAttribute("station_no"));
     * 
     * Envelope bounds = fs.getBounds(); assertNotNull(bounds);
     * 
     * bounds = fs.getBounds(new DefaultQuery("wq_derived", filter));
     * assertNotNull(bounds); }
     */

    private DataStore testRegisterViews(DataStoreFactorySpi dsFactory) throws IOException {
        String typeName1 = "ViewType1";
        String typeName2 = "ViewType2";
        String sqlDef1 = "select area, perimeter, gid, x, y from testset order by gid";
        String sqlDef2 = "select gid, x, y from testset";

        Map params = new HashMap(this.local);
        params.put("sqlView.1.typeName", typeName1);

        try {
            dsFactory.createDataStore(params);
            fail("should have failed, sql def not provided");
        } catch (IllegalArgumentException e) {
            // ok
        }

        params.put("sqlView.1.sqlQuery", sqlDef1);

        params.put("sqlView.2.typeName", typeName2);
        params.put("sqlView.2.sqlQuery", sqlDef2);

        DataStore dstore = dsFactory.createDataStore(params);

        SimpleFeatureType ft1 = dstore.getSchema(typeName1);
        SimpleFeatureType ft2 = dstore.getSchema(typeName2);
        assertNotNull(ft1);
        assertNotNull(ft2);

        return dstore;
    }

}
