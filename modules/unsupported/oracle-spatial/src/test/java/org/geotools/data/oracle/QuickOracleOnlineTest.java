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
 */
package org.geotools.data.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

import oracle.sql.ARRAY;
import oracle.sql.Datum;
import oracle.sql.STRUCT;

import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataTestCase;
import org.geotools.data.jdbc.ConnectionPool;
import org.geotools.data.jdbc.ConnectionPoolManager;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * This class provides a quick test of OracleDataStore.
 * <p>
 * We are using this class to test the internal workings of the
 * OracleDataStore.  To test the public "normal" api please
 * refer to OracleDataStoreTest.
 * </p>
 * <p>
 * Several of these tests bay be "X"ed out, in such cases
 * the tests were serving as a scratch pad as we learned where oracle
 * keeps everything.
 * </p>
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 */
public class QuickOracleOnlineTest extends DataTestCase {
    
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");
    
    OracleDataStore data;
    ConnectionPool pool;

	private Connection conn;    

    /**
     * Constructor for MemoryDataStoreTest.
     *
     * @param test
     *
     * @throws AssertionError DOCUMENT ME!
     */
    public QuickOracleOnlineTest(String test) {
        super(test);
    }

    protected void setUp() throws Exception {
    	super.setUp();

        Properties resource = new Properties();
        resource.load(this.getClass().getResourceAsStream("remote.properties"));
        
        data = (OracleDataStore) DataStoreFinder.getDataStore(resource);
        
        //BasicFIDMapper basic = new BasicFIDMapper("tid", 255, false);
        //TypedFIDMapper typed = new TypedFIDMapper( basic, "trim_utm10");
        //data.setFIDMapper("trim_utm10", typed );        
    }
    
    boolean create = true;
    protected void reset() throws Exception {
    	if( conn == null ) return;
    	
    	if( !create ) return;    	
    	Statement st = conn.createStatement();
    	    	
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
	    	//
	    	// If we ever need data we can do it here!
	    	// but right now we are just hammering Schema
    	}
    	catch (SQLException fine){
    		fine.printStackTrace();
    	}
    	create = false;
    }
    protected void tearDown() throws Exception {
    	if( conn != null ) conn.close();
    	if( pool != null ) pool.close();
    	
        ConnectionPoolManager manager = ConnectionPoolManager.getInstance();
        manager.closeAll();
        
        
        data.dispose();
        
    	conn = null;
    	pool = null;
    	data = null;
    	super.tearDown();
    	
    	// tests with oracle xe fail without these... it seems the oracle poolable connections
        // are not closed right away even if I traced the dbcp code and it actually closes
        // the connections...
    	System.gc(); System.gc(); System.gc();
    }
    public void testSRIDLookup() throws Exception {
    	if( conn == null ) return;
    	
    	Statement st = conn.createStatement();
    	st.execute("select cs_name, wktext from cs_srs where srid = 82465");
    	
    	ResultSet set = st.getResultSet();
    	ResultSetMetaData meta = set.getMetaData();
    	assertEquals( 2, meta.getColumnCount() );
    	assertTrue( set.next() );
    	
    	String name = set.getString(1);
    	String wkt = set.getString(2);
    	assertEquals( "MGA94 Zone 52", name );
    	assertTrue( wkt.indexOf("Geodetic Datum of Australia 1994") != -1 );
    	
    	//System.out.println( wkt );
    	CoordinateReferenceSystem crs = CRS.parseWKT( wkt );
    	//System.out.println( crs );
    	assertNotNull( crs );    	    	
    }
    public void testMetadataSRID() throws Exception {
    	if( conn == null ) return;
    	
    	Statement st = conn.createStatement();
    	st.execute("SELECT srid FROM USER_SDO_GEOM_METADATA where TABLE_NAME = 'ORA_TEST_LINES'");    	
    	ResultSet set = st.getResultSet();    	
    	assertTrue( set.next() );
    	int srid = set.getInt( 1 );
    	assertEquals( 82465, srid );
    }
    public void testMetadataDIMInfo() throws Exception {
    	if( conn == null ) return;
    	
    	Statement st = conn.createStatement();
    	st.execute("SELECT srid,diminfo FROM USER_SDO_GEOM_METADATA where TABLE_NAME = 'ORA_TEST_LINES'");    	
    	ResultSet set = st.getResultSet();    	
    	assertTrue( set.next() );
    	int srid = set.getInt( 1 );
    	CoordinateReferenceSystem crs = data.determineCRS( srid );
    	ARRAY array= (ARRAY) set.getObject(2);    	
    	Datum data[] = array.getOracleArray();
    	
    	double minx = Double.NaN;
    	double miny = Double.NaN;
    	double maxx = Double.NaN;
    	double maxy = Double.NaN;
    	
    	for( int i =0; i<data.length; i++){
    		Datum datum = data[i]; 
    		System.out.println( datum.getClass() );
    		STRUCT diminfo = (STRUCT) datum;
    		Datum info[] = diminfo.getOracleAttributes();
    		String ord = info[0].stringValue();
    		double min = info[1].doubleValue();
    		double max = info[2].doubleValue();
    		double precision = info[3].doubleValue(); // TODO use this for accurate JTS PercisionModel!
    		if( "X".equalsIgnoreCase( ord )){
    			minx = min; maxx= max;
    		}
    		if( "Y".equalsIgnoreCase( ord )){
    			miny = min; maxy= max;
    		}    		
    	}
    	Envelope extent = new Envelope(minx,maxx, miny,maxy );
    	ReferencedEnvelope ref = new ReferencedEnvelope( extent, crs );
    	assertFalse( ref.isNull() );
    }    
    public void testDSSridMethod() throws Exception {
    	if( conn == null ) return;
    	
    	int srid = data.determineSRID("ORA_TEST_LINES","SHAPE");
    	assertEquals( 82465, srid );    	
    	CoordinateReferenceSystem crs = data.determineCRS( srid );
    	assertNotNull( crs );
    }
    public void testGeometryCRS() throws Exception {
    	if( conn == null ) return;
    	FeatureType schema = data.getSchema("ORA_TEST_LINES");
    	GeometryDescriptor geom = schema.getGeometryDescriptor();
    	
    	assertNotNull( geom.getCoordinateReferenceSystem() );
    	assertEquals( "SHAPE", geom.getLocalName() );
    }    
}
