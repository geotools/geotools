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

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.test.OnlineTestCase;

/**
 * Abstract class for PostGIS online test cases.
 * 
 * @since 2.4
 * @author Cory Horner, Refractions Research
 *
 *
 * @source $URL$
 */
public abstract class PostgisOnlineTestCase extends OnlineTestCase {

    protected DataStore dataStore;

    protected abstract String getFixtureId();

    protected void connect() throws Exception {
        Map params = getParams();
        dataStore = new PostgisDataStoreFactory().createDataStore(params);
    }
    
    public Map getParams() {
        Map params = new HashMap();

        params.put(PostgisDataStoreFactory.DBTYPE.key, "postgis");
        params.put(PostgisDataStoreFactory.HOST.key, fixture
                .getProperty("host"));
        params.put(PostgisDataStoreFactory.PORT.key, fixture
                .getProperty("port"));
        params.put(PostgisDataStoreFactory.SCHEMA.key, fixture
                .getProperty("schema"));
        params.put(PostgisDataStoreFactory.DATABASE.key, fixture
                .getProperty("database"));
        params.put(PostgisDataStoreFactory.USER.key, fixture
                .getProperty("user"));
        params.put(PostgisDataStoreFactory.PASSWD.key, fixture
                .getProperty("password"));

        if (fixture.containsKey("wkbEnabled")) {
            params.put(PostgisDataStoreFactory.WKBENABLED.key, fixture
                    .getProperty("wkbEnabled"));
        }
        if (fixture.containsKey("looseBbox")) {
            params.put(PostgisDataStoreFactory.LOOSEBBOX.key, fixture
                    .getProperty("looseBbox"));
        }

        return params;
    }


    protected void disconnect() throws Exception {
        dataStore = null;
    }

}
