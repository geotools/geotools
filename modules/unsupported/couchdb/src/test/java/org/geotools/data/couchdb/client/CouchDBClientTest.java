/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb.client;

import org.geotools.data.couchdb.CouchDBTestSupport;
import org.junit.Before;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBClientTest extends CouchDBTestSupport {

    @Before
    public void setup() throws Exception {
        deleteIfExists(getTestDB());
    }
    
    @Test
    public void testGetDatabaseNames() throws Exception {
        List<String> databaseNames = client.getDatabaseNames();
        assertTrue("Expected _users db to showup", databaseNames.indexOf("_users") >= 0);
    }

    @Test
    public void testOpenDBConnection() throws Exception {
        // success path
        CouchDBConnection db = client.createDB(getTestDB());
        assertEquals(getTestDB(), client.openDBConnection(getTestDB()).getName());
        db.delete();

        // failure
        try {
            client.openDBConnection("nonexistent");
            fail("expected error");
        } catch (CouchDBException cex) {
            assertTrue("expected useful failure message", cex.getMessage().indexOf("not_found") > 0);
        }
    }

    @Test
    public void testCreateDB() throws Exception {
        CouchDBConnection db = client.createDB(getTestDB());
        assertEquals(getTestDB(), db.getName());
        db.delete();
    }
    
    @Test
    public void testSpatialList() throws Exception {
        // this is more of a conceptual test
        
        /**
        CouchDBConnection db = client.createDB(TEST_DB_NAME);
        db.putDesignDocument(resolveFile("design-doc.json"));
        JSONObject obj = (JSONObject) JSONValue.parseWithException(resolveContent("counties.json"));
        db.postBulk((JSONArray) obj.get("features"));
        
        CouchDBResponse resp;
        
        long time = System.currentTimeMillis();
        String query = "/_design/main/_spatial/_list/query/counties?bbox=-180,-90,180,90&fname=Name&fval=Hon.*";
        resp = client.get(TEST_DB_NAME + query);
        String[] ids = resp.getBody().split("\n");
        
        JSONObject post = new JSONObject();
        JSONArray vals = new JSONArray();
        vals.addAll(Arrays.asList(ids));
        post.put("keys",vals);
        System.out.println(post.toJSONString());
        resp = client.post(TEST_DB_NAME + "/_design/main/_view/counties", post.toJSONString());
        JSONObject bodyAsJSONObject = resp.getBodyAsJSONObject();
        JSONArray rows = (JSONArray) bodyAsJSONObject.get("rows");
        for (int i =0 ;i <rows.size(); i++) {
            JSONObject row = (JSONObject) rows.get(i);
            JSONObject val = (JSONObject) row.get("value");
            System.out.println(val.get("properties"));
        }
        System.out.println(System.currentTimeMillis() - time);
         **/
    }
}
