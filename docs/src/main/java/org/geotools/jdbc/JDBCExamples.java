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
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;

public class JDBCExamples {

    void gpkgExample() throws IOException {
        // gpkgExample start
        Map<String, Object> params = new HashMap<>();
        params.put("dbtype", "geopkg");
        params.put("database", "geotools.gpkg");
        params.put("SPI", "org.geotools.geopkg.GeoPkgDataStoreFactory");
        DataStore datastore = DataStoreFinder.getDataStore(params);
        // gpkgExample end
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
        params.put("preparedStatements", true);
        params.put("encode functions", true);

        DataStore dataStore = DataStoreFinder.getDataStore(params);
        // postgisExample end
    }
}
