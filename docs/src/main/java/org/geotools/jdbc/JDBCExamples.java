/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jdbc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;

public class JDBCExamples {

    void h2Example() throws IOException {
        // h2Example start
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "h2");
        params.put("database", "geotools");

        DataStore datastore = DataStoreFinder.getDataStore(params);
        // h2Example end
    }

    void h2AbsPathExample() throws IOException {
        // h2AbsPathExample start
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "h2");
        params.put("database", "/abs/path/to/geotools");

        DataStore datastore = DataStoreFinder.getDataStore(params);
        // h2AbsPathExample end
    }

    void h2TcpExample() throws IOException {
        // h2TcpExample start
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "h2");
        params.put("host", "localhost");
        params.put("port", 9902);
        params.put("database", "geotools");
        params.put("passwd", "geotools");
        params.put("passwd", "geotools");

        DataStore datastore = DataStoreFinder.getDataStore(params);
        // h2TcpExample end
    }

    void postgisExample() throws IOException {
        // postgisExample start
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "postgis");
        params.put("host", "localhost");
        params.put("port", 5432);
        params.put("schema", "public");
        params.put("database", "database");
        params.put("user", "postgres");
        params.put("passwd", "postgres");

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        // postgisExample end
    }
}
