/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;

/**
 * Convenience class for postgis testing.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 * deprecated please use PostgisOnlineTestCase
 */
public class PostgisTests {

	public static Fixture newFixture(String props) throws IOException {
		PropertyResourceBundle resource;
        resource = new PropertyResourceBundle(
    		PostgisTests.class.getResourceAsStream(props)
		);

        Fixture f = new Fixture();
        
        f.namespace = resource.getString("namespace");
        f.host = resource.getString("host");
        f.port = Integer.valueOf(resource.getString("port"));
        f.database = resource.getString("database");
        f.user = resource.getString("user");
        f.password = resource.getString("password");	
        f.schema = resource.getString("schema");
        
        if (f.schema == null || "".equals(f.schema.trim()))
        	f.schema = "public";
        
        f.wkbEnabled = null;
        f.looseBbox = null;

        Enumeration keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            if (key.equalsIgnoreCase("wkbEnabled")) {
                f.wkbEnabled = new Boolean(resource.getString("wkbEnabled"));
            } else if (key.equalsIgnoreCase("looseBbox")) {
                f.looseBbox = new Boolean(resource.getString("looseBbox"));
            }
        }
        
        return f;
	}
	
	public static Fixture newFixture() throws IOException {
		return newFixture("fixture.properties");
	}
	
	public static class Fixture {
		public String namespace;
		public String host;
		public String database;
		public Integer port;
		public String user;
		public String password;
		public String schema;
        public Boolean wkbEnabled;
        public Boolean looseBbox;
	}
    
    public static Map getParams(Fixture f) {
        Map params = new HashMap();

        params.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
        params.put(PostgisDataStoreFactory.HOST.key, f.host);
        params.put(PostgisDataStoreFactory.PORT.key, f.port);
        params.put(PostgisDataStoreFactory.DATABASE.key, f.database);
        params.put(PostgisDataStoreFactory.USER.key, f.user);
        params.put(PostgisDataStoreFactory.PASSWD.key, f.password);
        params.put(PostgisDataStoreFactory.SCHEMA.key,f.schema);
        if (f.wkbEnabled != null) {
            params.put(PostgisDataStoreFactory.WKBENABLED.key, f.wkbEnabled);
        }
        if (f.looseBbox != null) {
            params.put(PostgisDataStoreFactory.LOOSEBBOX.key, f.looseBbox);
        }

        return params;
    }
    
    public static Map getParams(String fixtureFile) throws IOException {
        Fixture f = newFixture(fixtureFile);
        return getParams(f);
    }
}
