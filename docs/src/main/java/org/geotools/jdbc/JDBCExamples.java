/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
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
