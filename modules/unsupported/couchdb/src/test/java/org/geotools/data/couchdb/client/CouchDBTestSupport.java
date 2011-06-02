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

import org.geotools.data.couchdb.client.CouchDBUtils;
import org.geotools.data.couchdb.client.CouchDBClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.json.simple.JSONValue;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author Ian Schneider
 */
public class CouchDBTestSupport {
    protected CouchDBClient client;
    protected String TEST_DB_NAME = "gttestdb";
    protected Logger logger;

    @Before
    public void setUp() throws Exception {
        client = new CouchDBClient("http://127.0.0.1:5984/");
    }
    
    @After
    public void tearDown() throws Exception {
        debug(false);
    }
    
    public void debug(boolean on) {
        logger = Logging.getLogger(getClass());
        logger.setLevel(Level.FINEST);
        Logger.getLogger("").getHandlers()[0].setLevel(on ? Level.FINEST : Level.INFO);
    }
    
    protected File resolveFile(String path) {
        return new File("src/test/resources/org/geotools/data/couchdb/" + path);
    }
    
    protected JSONArray loadJSON(String path,String featureClass) throws Exception {
        String content = resolveContent(path);
        JSONObject data = (JSONObject) JSONValue.parseWithException(content);
        JSONArray features = (JSONArray) data.get("features");
        if (featureClass == null) {
            featureClass = path.split("\\.")[0];
        }
        for (int i = 0; i < features.size(); i++) {
            JSONObject f = (JSONObject) features.get(i);
            f.put("featureClass", featureClass);
        }
        return features;
    }
    
    protected String resolveContent(String path) throws FileNotFoundException {
        return CouchDBUtils.read(resolveFile(path));
    }
    static String stripComments(String json) {
        Pattern pat = Pattern.compile("/\\*.*\\*/",Pattern.MULTILINE | Pattern.DOTALL);
        return pat.matcher(json).replaceAll("");
    }
    
    protected void deleteIfExists(String db) throws Exception {
        List<String> databaseNames = client.getDatabaseNames();
        if (databaseNames.indexOf(db) >= 0) {
            client.openDBConnection(db).delete();
        }
        assertTrue("Expected db to be deleted", client.getDatabaseNames().indexOf(db) < 0);
    }
}
