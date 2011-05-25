/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.fidmapper.MaxIncFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.AbstractFilter;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.LikeFilter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Oracle XE warning: you might run out of connections and tests might start failing. If you
 * are in that situation, login into the XE instance and run the following command as SYS:
 * alter system set processes = 400 scope=spfile;
 * @author geoghegs
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 *
 * @source $URL$
 */
public class OracleDataStoreOnlineTest extends TestCase {
    private FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
    private Properties properties;
    private GeometryFactory jtsFactory = new GeometryFactory();
    private String schemaName;
    private OracleDataStore dstore;
    private Connection conn;
    
    static {
        DataStoreFinder.scanForPlugins();
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("remote.properties"));
        schemaName = properties.getProperty("schema");
        properties.put("dbtype", "oracle");
        properties.put(OracleDataStoreFactory.MINCONN.key, "1");
        
        dstore =  (OracleDataStore) DataStoreFinder.getDataStore(properties);
        conn = dstore.getConnection(Transaction.AUTO_COMMIT);
        reset();
        dstore.setFIDMapper("ORA_TEST_POINTS", new TypedFIDMapper(new MaxIncFIDMapper("ORA_TEST_POINTS", "ID", Types.INTEGER), "ORA_TEST_POINTS"));
        dstore.setFIDMapper("ORA_TEST_LINES", new TypedFIDMapper(new MaxIncFIDMapper("ORA_TEST_LINES", "ID", Types.INTEGER), "ORA_TEST_LINES"));
        dstore.setFIDMapper("ORA_TEST_POLYGONS", new TypedFIDMapper(new MaxIncFIDMapper("ORA_TEST_POLYGONS", "ID", Types.INTEGER), "ORA_TEST_POLYGONS"));
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {    	
    	//reset();
        if(conn != null) 
            conn.close();
        if(dstore != null)
            dstore.dispose();
    }
    static boolean first  = false;
    
    protected void reset() throws Exception {
    	if( conn == null ) return;
    	Statement st = conn.createStatement();
    	
    	if( !first ){
    		try {
    			st.execute("DROP TABLE ora_test_points");
    			st.executeUpdate("DELETE FROM user_sdo_geom_metadata WHERE TABLE_NAME='ORA_TEST_POINTS'");    	    	    			
    		}
    		catch( SQLException noPrevRun){}
    		first = true;
    	}
    	try {
			if( st.executeQuery("SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME = 'ORA_TEST_POINTS'").next()){
				try {
		    		st.executeUpdate("DELETE FROM ORA_TEST_POINTS");
		    	}
		    	catch (SQLException fine){
		    		// must be the first time?
		    		fine.printStackTrace();
		    	}    					
			}
			else {
				st.execute( "CREATE TABLE ORA_TEST_POINTS ("+
						    "       NAME VARCHAR2(15),"+
						    "       INTVAL NUMBER(12,0),"+"" +
						    "       ID NUMBER(3,0) PRIMARY KEY,"+
						    "       SHAPE MDSYS.SDO_GEOMETRY )");
		    	st.execute("INSERT INTO user_sdo_geom_metadata (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID )"+
					    "VALUES('ORA_TEST_POINTS','SHAPE',"+
					        "MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',0,-20,0.5),MDSYS.SDO_DIM_ELEMENT('Y',0,10,0.5)),"+
					    "4326)");
	    	
		    	st.execute("CREATE INDEX ORA_TEST_POINTS_SHAPE_IDX "+
		    				"ON ORA_TEST_POINTS (SHAPE) INDEXTYPE IS " +
		    				"MDSYS.SPATIAL_INDEX PARAMETERS (' SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"') ");		    			    	
			}
    	}
    	catch (SQLException fine){
    		fine.printStackTrace();
    	}  	
        
        if( st.executeQuery("SELECT TABLE_NAME FROM USER_TABLES WHERE TABLE_NAME = 'ORA_TEST_LINES'").next()){          
            try {
                st.execute("DROP TABLE ORA_TEST_LINES");
                st.executeUpdate("DELETE FROM user_sdo_geom_metadata WHERE TABLE_NAME='ORA_TEST_LINES'");                               
            }
            catch( SQLException noPrevRun){ 
                noPrevRun.printStackTrace();
            }
        }       
        try {
            st.execute( "CREATE TABLE ORA_TEST_LINES ("+
                        "    name    VARCHAR(255),"+
                        "    intval  NUMBER,"+
                        "    id      NUMBER  PRIMARY KEY,"+
                        "    shape   MDSYS.SDO_GEOMETRY"+
                        ")");               
            st.execute( "INSERT INTO USER_SDO_GEOM_METADATA VALUES ("+
                        "    'ORA_TEST_LINES',"+
                        "    'SHAPE',"+
                        "    MDSYS.SDO_DIM_ARRAY("+
                        "        MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.005),"+
                        "        MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.005)"+
                        "    ),"+
                        "    82465"+
                        ")");
            st.execute("create index test_line_index on ORA_TEST_LINES(SHAPE) INDEXTYPE IS MDSYS.SPATIAL_INDEX");
        }
        catch (SQLException fine){
            fine.printStackTrace();
        }
        
    	//                   + (20,30)
    	//         
    	//  +(20,10)         +(10,10)   + (20,10)   +(30,10)
    	//        
    	st.execute("INSERT INTO ORA_TEST_POINTS VALUES ('point 1',10,1," +
                "MDSYS.SDO_GEOMETRY(2001,4326," +
                "MDSYS.SDO_POINT_TYPE(10.0, 10.0, NULL),"+
                "NULL,NULL))");
    	st.execute("INSERT INTO ORA_TEST_POINTS VALUES ('point 2',20,2," +
                        "MDSYS.SDO_GEOMETRY(2001,4326," +
                        "MDSYS.SDO_POINT_TYPE(20.0, 10.0, NULL),"+
                        "NULL,NULL))");
    	st.execute("INSERT INTO ORA_TEST_POINTS VALUES ('point 3',30,3," +
                        "MDSYS.SDO_GEOMETRY(2001,4326," +
                        "SDO_POINT_TYPE(20.0, 30.0, NULL),"+
                        "NULL,NULL))");
    	st.execute("INSERT INTO ORA_TEST_POINTS VALUES ('point 4',40,4," +
                        "MDSYS.SDO_GEOMETRY(2001,4326," +
                        "SDO_POINT_TYPE(30.0, 10.0, NULL),"+
                        "NULL,NULL))");
    	st.execute("INSERT INTO ORA_TEST_POINTS VALUES ('point 5',50,5," +
                        "MDSYS.SDO_GEOMETRY(2001,4326," +
                        "SDO_POINT_TYPE(-20.0, 10.0, NULL),"+
                        "NULL,NULL))");

    	
    	// two simple lines
    	st.execute("INSERT INTO ORA_TEST_LINES VALUES ('line1',50,2," +
	                "MDSYS.SDO_GEOMETRY(2002,82465,NULL," +
	                "SDO_ELEM_INFO_ARRAY(1,2,1),"+
	                "SDO_ORDINATE_ARRAY(0,0, 10, 10)))");
	       
	st.execute("INSERT INTO ORA_TEST_LINES VALUES ('line2',100,4," +
	                "MDSYS.SDO_GEOMETRY(2002,82465,NULL," +
	                "SDO_ELEM_INFO_ARRAY(1,2,1),"+
	                "SDO_ORDINATE_ARRAY(25,25, 25, 30)))");
    	
    }

    public void testGetFeatureTypes() throws IOException {
    	if( conn == null ) return;
    	
        String[] fts = dstore.getTypeNames();
        List list = Arrays.asList( fts );
        
        System.out.println( list );
        assertTrue( list.contains("ORA_TEST_POINTS"));        
    }

    public void testGetSchema() throws Exception {
    	if( conn == null ) return;    	
            SimpleFeatureType ft = dstore.getSchema("ORA_TEST_POINTS");
            assertNotNull(ft);
            System.out.println(ft);
    }

    public void testGetFeatureReader() throws Exception {
    	if( conn == null ) return;    	
            SimpleFeatureType ft = dstore.getSchema("ORA_TEST_POINTS");
            Query q = new DefaultQuery( "ORA_TEST_POINTS" );
             FeatureReader<SimpleFeatureType, SimpleFeature> fr = dstore.getFeatureReader( q, Transaction.AUTO_COMMIT);
            assertEquals( ft, fr.getFeatureType() );
            int count = 0;

            while (fr.hasNext()) {
                fr.next();
                count++;
            }
            assertEquals(5, count);

            fr.close();
    }

    public void testGetFeatureWriter() throws Exception {
    	if( conn == null ) return;    	
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dstore.getFeatureWriter("ORA_TEST_POINTS", Filter.INCLUDE, Transaction.AUTO_COMMIT);
        assertNotNull(writer);

        SimpleFeature feature = writer.next();
        System.out.println(feature);
        feature.setAttribute(0, "Changed Feature");
        System.out.println(feature);
        writer.write();
        writer.close();

        Query q = new DefaultQuery( "ORA_TEST_POINTS" );
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = dstore.getFeatureReader( q, Transaction.AUTO_COMMIT);
        SimpleFeature readF = reader.next();
        
        assertEquals("Changed Feature", feature.getAttribute(0));
        assertEquals(feature.getID(), readF.getID());
        assertEquals(feature.getAttribute(0), readF.getAttribute((0)));
        assertEquals(feature.getAttribute(1), readF.getAttribute((1)));
        // assertTrue(feature.getAttribute(2).equals(readF.getAttribute((2))));  JTS doesnt override equals.  POS
        
        reader.close();
    }
    
    public void testMaxFeatures() throws Exception {
    	if( conn == null ) return;    	
        DefaultQuery query = new DefaultQuery();
        query.setTypeName("ORA_TEST_POINTS");
        query.setMaxFeatures(3);
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");        
        SimpleFeatureCollection fr = fs.getFeatures(query);
        assertEquals(3, fr.size());
    }

    public void testLikeGetFeatures() throws Exception {
    	if( conn == null ) return;    	
        LikeFilter likeFilter = filterFactory.createLikeFilter();
        Expression pattern = filterFactory.createLiteralExpression("*");
        Expression attr = filterFactory.createAttributeExpression(null, "NAME");
        likeFilter.setPattern(pattern, "*", "?", "\\"); // '*' --> '%' 
        likeFilter.setValue(attr);
        
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");
        SimpleFeatureCollection fr = fs.getFeatures(likeFilter);
        assertEquals(5, fr.size());
        
        pattern = filterFactory.createLiteralExpression("*5");
        likeFilter.setPattern(pattern, "*", "?", "\\");
        fr = fs.getFeatures(likeFilter);
        assertEquals(1, fr.size());
    }
    
    public void testAttributeFilter() throws Exception {
    	if( conn == null ) return;    	
        CompareFilter attributeEquality = filterFactory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        Expression attribute = filterFactory.createAttributeExpression(dstore.getSchema("ORA_TEST_POINTS"), "NAME");
        Expression literal = filterFactory.createLiteralExpression("point 1");
        attributeEquality.addLeftValue(attribute);
        attributeEquality.addRightValue(literal);
        
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");
        SimpleFeatureCollection fr = fs.getFeatures(attributeEquality);
        assertEquals(1, fr.size());
        
        SimpleFeatureCollection fc = fr;
        assertEquals(1, fc.size());
        final SimpleFeatureIterator iterator = fc.features();
        SimpleFeature f = iterator.next();
        iterator.close();
        assertEquals("point 1", f.getAttribute("NAME"));
        
    }
    
    public void testBBoxFilter() throws Exception {
    	if( conn == null ) return;    	
        GeometryFilter filter = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // had to reduce the envelope a little, Oracle has trobles with bbox that span the whole earth
        Expression right = filterFactory.createBBoxExpression(new Envelope(-170, 170, -80, 80));
        Expression left = filterFactory.createAttributeExpression(dstore.getSchema("ORA_TEST_POINTS"), "SHAPE");
        filter.addLeftGeometry(left);
        filter.addRightGeometry(right);
        
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");
        SimpleFeatureCollection fr = fs.getFeatures(filter);        
        assertEquals(5, fr.size()); // we pass this!
        
    	//                   + (20,30)
    	//                            +----------------------+
    	//  +(20,10)         +(10,10) | + (20,10)   +(30,10) |
    	//                            +----------------------+
        right = filterFactory.createBBoxExpression(new Envelope(15, 35, 0, 15));        
        filter.addRightGeometry(right);
        fr = fs.getFeatures(filter);
        assertEquals(2, fr.size()); // we have 4!
        
        // check a filter built changing operands order works the same
        filter.addLeftGeometry(right);
        filter.addRightGeometry(left);
        fr = fs.getFeatures(filter);        
        assertEquals(2, fr.size()); // we pass this!
    }
    
    public void testCaseInsensitiveFilter() throws Exception {
        final String typeName = "ORA_TEST_POINTS";
        SimpleFeatureSource rivers = dstore.getFeatureSource(typeName);
        final Literal literal = filterFactory.literal("PoInT 1");
        final PropertyName property = filterFactory.property("NAME");
        org.opengis.filter.Filter caseSensitive = filterFactory.equal(property, literal, true);
        assertEquals(0, rivers.getCount(new DefaultQuery(typeName, caseSensitive)));
        org.opengis.filter.Filter caseInsensitive = filterFactory.equal(property, literal, false);
        assertEquals(1, rivers.getCount(new DefaultQuery(typeName, caseInsensitive)));
    }
    
    public void testBBoxFilterNoAttribute() throws Exception {
        if( conn == null ) return;      
        GeometryFilter filter = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // had to reduce the envelope a little, Oracle has trobles with bbox that span the whole earth
        Expression right = filterFactory.createBBoxExpression(new Envelope(-170, 170, -80, 80));
        Expression left = filterFactory.createAttributeExpression("");
        filter.addLeftGeometry(left);
        filter.addRightGeometry(right);
        
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");
        SimpleFeatureCollection fr = fs.getFeatures(filter);        
        assertEquals(5, fr.size()); // we pass this!
        
        //                   + (20,30)
        //                            +----------------------+
        //  +(20,10)         +(10,10) | + (20,10)   +(30,10) |
        //                            +----------------------+
        right = filterFactory.createBBoxExpression(new Envelope(15, 35, 0, 15));        
        filter.addRightGeometry(right);
        fr = fs.getFeatures(filter);
        assertEquals(2, fr.size()); // we have 4!
        
        // check a filter built changing operands order works the same
        filter.addLeftGeometry(right);
        filter.addRightGeometry(left);
        fr = fs.getFeatures(filter);        
        assertEquals(2, fr.size()); // we pass this!
    }
    
    public void testDisjointFilter() throws Exception {
        if( conn == null ) return;
        
        //                   + (20,30)
        //                            +----------------------+
        //  +(20,10)         +(10,10) | + (20,10)   +(30,10) |
        //                            +----------------------+
        GeometryFactory gf = new GeometryFactory();
        Polygon p = gf.createPolygon(gf.createLinearRing(new Coordinate[] { new Coordinate(15, 0),
                new Coordinate(35, 0), new Coordinate(35, 15), new Coordinate(15, 15),
                new Coordinate(15, 0) }), null);
        GeometryFilter filter = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_DISJOINT);
        // had to reduce the envelope a little, Oracle has trobles with bbox that span the whole earth
        Expression right = filterFactory.createLiteralExpression(p);
        Expression left = filterFactory.createAttributeExpression(dstore.getSchema("ORA_TEST_POINTS"), "SHAPE");
        filter.addLeftGeometry(left);
        filter.addRightGeometry(right);
        
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");
        SimpleFeatureCollection fr = fs.getFeatures(filter);
        assertEquals(3, fr.size()); // we have 4!
        
        // check a filter built changing operands order works the same
        filter.addLeftGeometry(right);
        filter.addRightGeometry(left);
        fr = fs.getFeatures(filter);        
        assertEquals(3, fr.size()); // we pass this!
    }
    
    public void testPointGeometryConversion() throws Exception {
    	if( conn == null ) return;    	
        CompareFilter filter = filterFactory.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
        Expression left = filterFactory.createAttributeExpression(dstore.getSchema("ORA_TEST_POINTS"), "NAME");
        Expression right = filterFactory.createLiteralExpression("point 1");
        filter.addLeftValue(left);
        filter.addRightValue(right);
        
        
        SimpleFeatureSource fs = dstore.getFeatureSource("ORA_TEST_POINTS");
        SimpleFeatureCollection fr = fs.getFeatures(filter);        
        assertEquals(1, fr.size());
        
        final Iterator iterator = fr.iterator();
        SimpleFeature feature = (SimpleFeature) iterator.next();
        fr.close(iterator);
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        assertEquals(Point.class.getName(), geom.getClass().getName());
        Point point = (Point) geom;
        assertEquals(10.0, point.getX(), 0.001);
        assertEquals(10.0, point.getY(), 0.001);
    }
    
    public void testAddFeatures() throws Exception {
    	if( conn == null ) return;    	
        Map fidGen = new HashMap();
        fidGen.put("ORA_TEST_POINTS", JDBCDataStoreConfig.FID_GEN_MANUAL_INC);
        JDBCDataStoreConfig config = JDBCDataStoreConfig.createWithSchemaNameAndFIDGenMap(schemaName, fidGen);
        
        String name = "add_name";
        BigDecimal intval = new BigDecimal(70);
        Point point = jtsFactory.createPoint(new Coordinate(-15.0, -25));
        SimpleFeatureType type = dstore.getSchema("ORA_TEST_POINTS");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add(name);
        builder.add(intval);
        builder.add(point);
        SimpleFeature feature = builder.buildFeature("fid.1");
         
        SimpleFeatureStore fs = (SimpleFeatureStore) dstore.getFeatureSource("ORA_TEST_POINTS");
        fs.addFeatures(DataUtilities.collection(feature));

        // Select is directly from the DB
        Connection conn = dstore.getConnection(Transaction.AUTO_COMMIT);
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM ORA_TEST_POINTS WHERE NAME = 'add_name'");

        if (rs.next()) {
            assertEquals(rs.getString("NAME"), name);
            assertEquals(70, rs.getInt("INTVAL"));            
        } else {
            fail("Feature was not added correctly");
        }
    }
    
    public void testRemoveFeatures() throws Exception {
    	if( conn == null ) return;    	
    	SimpleFeatureStore fs = (SimpleFeatureStore) dstore.getFeatureSource("ORA_TEST_POINTS");
        fs.removeFeatures(Filter.INCLUDE);
        SimpleFeatureCollection fr = fs.getFeatures();
        assertEquals(0, fr.size());
    }
    
    public void testPropertySelect() throws Exception {
    	if( conn == null ) return;    	
        DefaultQuery q = new DefaultQuery("ORA_TEST_POINTS",Filter.INCLUDE);
        q.setPropertyNames(new String[]{"NAME"});
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = dstore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        SimpleFeature f = fr.next();
        fr.close();
        SimpleFeatureType ft = f.getFeatureType();
        assertEquals(1, ft.getAttributeCount());
        assertEquals("NAME", ft.getDescriptor(0).getLocalName());        
    }
    
    public void testBoundsWgs84() throws IOException {
        if( conn == null ) return;              
        Envelope extent = dstore.getEnvelope("ORA_TEST_POINTS");
        assertNotNull( extent );
        assertTrue( extent instanceof ReferencedEnvelope );
        ReferencedEnvelope envelope = (ReferencedEnvelope) extent;
        assertFalse( envelope.isNull() );
        
        Envelope extent2 = dstore.getFeatureSource("ORA_TEST_POINTS").getBounds();
        assertEquals(extent, extent2);
     }
     
    public void testBoundsWgs84Filter() throws Exception {
        if( conn == null ) return;
        
        // build a bbox filter
        GeometryFilter filter = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // had to reduce the envelope a little, Oracle has trobles with bbox that span the whole earth
        Expression right = filterFactory.createBBoxExpression(new Envelope(-10, 21, -10, 11));
        Expression left = filterFactory.createAttributeExpression(dstore.getSchema("ORA_TEST_POINTS"), "SHAPE");
        filter.addLeftGeometry(left);
        filter.addRightGeometry(right);
        
        Envelope extent = dstore.getFeatureSource("ORA_TEST_POINTS").getBounds(new DefaultQuery("ORA_TEST_POINTS", filter));
        assertNotNull( extent );
        assertTrue( extent instanceof ReferencedEnvelope );
        ReferencedEnvelope envelope = (ReferencedEnvelope) extent;
        assertEquals(new Envelope(10, 20, 10, 10), new Envelope(envelope));
    }
    
    public void testBoundsProjected(){
        if( conn == null ) return;              
        Envelope extent = dstore.getEnvelope("ORA_TEST_LINES");
        assertNotNull( extent );
        assertTrue( extent instanceof ReferencedEnvelope );
        ReferencedEnvelope envelope = (ReferencedEnvelope) extent;
        assertFalse( envelope.isNull() );
    }
    
    public void testBoundsProjectedFilter() throws IOException {
        if( conn == null ) return;
        
        // build a bbox filter
        GeometryFilter filter = filterFactory.createGeometryFilter(AbstractFilter.GEOMETRY_BBOX);
        // had to reduce the envelope a little, Oracle has trobles with bbox that span the whole earth
        Expression right = filterFactory.createBBoxExpression(new Envelope(-10, 21, -10, 21));
        Expression left = filterFactory.createAttributeExpression(dstore.getSchema("ORA_TEST_LINES"), "SHAPE");
        filter.addLeftGeometry(left);
        filter.addRightGeometry(right);
        
        Envelope extent = dstore.getFeatureSource("ORA_TEST_LINES").getBounds(new DefaultQuery("ORA_TEST_LINES", filter));
        assertNotNull( extent );
        assertTrue( extent instanceof ReferencedEnvelope );
        ReferencedEnvelope envelope = (ReferencedEnvelope) extent;
        assertEquals(new Envelope(0, 10, 0, 10), new Envelope(envelope));
    }
    
    public void testGeometryType() throws IOException {
        if( conn == null ) return; 
        SimpleFeatureType ft = dstore.getSchema("ORA_TEST_POINTS");
        assertEquals(Point.class, ft.getGeometryDescriptor().getType().getBinding());
    }
    
    public void testGeometryType2() throws IOException {
        if( conn == null ) return; 
        // here we did not declare a type
        SimpleFeatureType ft = dstore.getSchema("ORA_TEST_LINES");
        assertEquals(Geometry.class, ft.getGeometryDescriptor().getType().getBinding());
    }
}
