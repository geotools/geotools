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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBConnectionTest extends CouchDBTestSupport {

    @Test
    public void testPutDesignDocument() throws Exception {
        deleteIfExists(getTestDB());
        CouchDBConnection db = client.createDB(getTestDB());

        // failure first
        try {
            db.putDesignDocument("not json");
            fail("expected error");
        } catch (CouchDBException ex) {
            assertTrue("expected reason", ex.getMessage().indexOf("invalid UTF-8 JSON") >= 0);
        }

        // success path
        db.putDesignDocument(resolveFile("design-doc.json"));
    }

    @Test
    public void testBulkUpload() throws Exception {
        CouchDBConnection db = setupDB();

        long count = db.spatialView("counties").count(-180, -90, 180, 90);
        assertEquals("Count of counties", 24, count);

        db.delete();
    }
    

    public CouchDBConnection setupDB() throws Exception {
        deleteIfExists(getTestDB());
        CouchDBConnection db = client.createDB(getTestDB());
        db.putDesignDocument(resolveFile("design-doc.json"));

        db.postBulk(loadJSON("counties.json", null));

        return db;
    }
}
