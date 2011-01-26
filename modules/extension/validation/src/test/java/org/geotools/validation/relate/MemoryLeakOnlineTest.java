/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.relate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.geotools.data.DataTestCase;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.datasource.DataSourceUtil;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.validation.ValidationResults;

/**
 * This class tests the PostgisDataStoreAPI, against the same tests as
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
 * @source $URL$
 */
public class MemoryLeakOnlineTest extends DataTestCase {
    
    PostgisDataStore data;
    String database;    

    /**
     * Constructor for MemoryDataStoreTest.
     *
     * @param test
     *
     * @throws AssertionError DOCUMENT ME!
     */
    public MemoryLeakOnlineTest(String test) {
        super(test);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        PropertyResourceBundle resource;
        resource =
            new PropertyResourceBundle(this.getClass().getResourceAsStream("fixture.properties"));

        String namespace = resource.getString("namespace");
        String host = resource.getString("host");
        int port = Integer.parseInt(resource.getString("port"));
        String database = resource.getString("database");
        String schema = resource.getString("schema");
        this.database = database;

        String user = resource.getString("user");
        String password = resource.getString("password");

        if (namespace.equals("http://www.geotools.org/data/postgis")) {
            throw new IllegalStateException(
                "The fixture.properties file needs to be configured for your own database");
        }

        DataSource ds = DataSourceUtil.buildDefaultDataSource("jdbc:postgresql://" + host + ":" + port + "/" + database, "org.postgresql.Driver", user, password, null);
        JDBCDataStoreConfig config = JDBCDataStoreConfig.createWithNameSpaceAndSchemaName( namespace, schema );        
        data = new PostgisDataStore(ds, config, PostgisDataStore.OPTIMIZE_SAFE);
        BasicFIDMapper basic = new BasicFIDMapper("tid", 255, false);
        TypedFIDMapper typed = new TypedFIDMapper( basic, "trim_utm10");
        data.setFIDMapper("trim_utm10", typed );        
    }
    public void testMe() throws Exception {
    	String typeNames[] = data.getTypeNames();
    	assertNotNull( typeNames );
    	assertEquals( 1, typeNames.length );
    }
    public void testSlap() throws Exception {
    	OverlapsIntegrity overlap = new OverlapsIntegrity();
    	overlap.setExpected(false);
    	overlap.setGeomTypeRefA("my:line");
	
    	Map map = new HashMap();
    	try
		{
    		map.put("my:line", data.getFeatureSource("trim_utm10"));
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
	
		ValidationResults results = new TempFeatureResults();
		try
		{
			results.setValidation( overlap );
			//NOTE: seems to die on line 301: fr1 = featureResults.features(); in OverlapsIntegrity.java - bowens
			assertFalse(overlap.validate(map, null, results));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
    }
}
