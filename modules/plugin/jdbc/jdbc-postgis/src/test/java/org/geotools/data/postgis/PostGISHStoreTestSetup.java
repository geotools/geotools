/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Logger;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.postgresql.util.PSQLException;

public class PostGISHStoreTestSetup extends JDBCDelegatingTestSetup {

    private static final Logger LOGGER = Logger.getLogger(PostGISHStoreTestSetup.class.getName());

    protected boolean hasException = false;

    static boolean LOGGED = false;

    protected PostGISHStoreTestSetup() {
        super(new PostGISTestSetup());
    }

    @Override
    protected void setUpData() throws Exception {
        dropTestHStoreTable();
        try {
            createHStoreExtension();
        } catch (PSQLException psqle) {
            // Catching the org.postgresql.util.PSQLException:
            // Typically: ERROR: permission denied to create extension "hstore"
            // when creating the extension
            if (!LOGGED) {
                LOGGER.warning(
                        "HSTORE tests will be skipped due to:\n >>>> "
                                + psqle.getLocalizedMessage());
                LOGGED = true;
            }
            hasException = true;
            return;
        }
        createTestHStoreTable();
    }

    private void createHStoreExtension() throws Exception {
        run("CREATE EXTENSION IF NOT EXISTS HSTORE;");
    }

    private void createTestHStoreTable() throws Exception {

        String sql =
                "CREATE TABLE \"hstoretest\" ("
                        + "\"id\" INT, \"name\" VARCHAR, \"mapping\" HSTORE, PRIMARY KEY(id))";
        run(sql);

        sql =
                "INSERT INTO \"hstoretest\" VALUES (0, 'singlepair','key1 => value1 ');"
                        + "INSERT INTO \"hstoretest\" VALUES (1, 'doublepair','key2 => value2, key3 => value3 ');"
                        + "INSERT INTO \"hstoretest\" VALUES (2, 'pairwithnullvalue', 'key4 => NULL');"
                        + "INSERT INTO \"hstoretest\" VALUES (3, 'emptycontent', '');"
                        + "INSERT INTO \"hstoretest\" VALUES (4, 'nullcontent', null);";

        run(sql);
    }

    private void dropTestHStoreTable() throws Exception {
        runSafe("DROP TABLE \"hstoretest\" cascade");
    }
}
