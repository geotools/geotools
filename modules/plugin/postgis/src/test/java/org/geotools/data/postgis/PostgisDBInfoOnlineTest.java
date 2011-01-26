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


public class PostgisDBInfoOnlineTest extends PostgisOnlineTestCase {
    
    public void testVersions() {
        PostgisDBInfo dbInfo = ((PostgisDataStore) dataStore).getDBInfo();
        assertNotNull(dbInfo);
        
        //postgis version should be 0.* or 1.*
        assertTrue(dbInfo.getVersion().length() > 2);
        assertTrue(dbInfo.getMajorVersion() == 0 || dbInfo.getMajorVersion() == 1);
        
        //postgres version should be 7.* or 8.*
        assertTrue(dbInfo.getPostgresVersion().length() > 2);
        assertTrue(dbInfo.getPostgresMajorVersion() == 7 || dbInfo.getPostgresMajorVersion() == 8);
  
        dbInfo = null;
    }

    protected String getFixtureId() {
        return "postgis.typical";
    }
}
