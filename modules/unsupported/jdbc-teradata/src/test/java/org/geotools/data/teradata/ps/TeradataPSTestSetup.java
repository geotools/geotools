/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata.ps;

import org.geotools.data.teradata.TeradataGISDialect;
import org.geotools.data.teradata.TeradataPSDialect;
import org.geotools.data.teradata.TeradataTestSetup;
import org.geotools.jdbc.JDBCDataStore;

public class TeradataPSTestSetup extends TeradataTestSetup {

    private static boolean first = true;

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);

        if (first) {
            // uncomment to turn up logging
            // java.util.logging.ConsoleHandler handler = new
            // java.util.logging.ConsoleHandler();
            // handler.setLevel(java.util.logging.Level.FINE);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").setLevel(java.util.logging.Level.FINE);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").addHandler(handler);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").setLevel(java.util.logging.Level.FINE);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").addHandler(handler);
            first = false;
        }
        // for this test we need a PS based dialect
        TeradataPSDialect dialect = new TeradataPSDialect(dataStore, (TeradataGISDialect) dataStore
                .getSQLDialect());
        dialect.setLooseBBOXEnabled(false);
        dataStore.setSQLDialect(dialect);
    }
}
