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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.postgresql.util.PSQLException;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class PostGISJsonTestSetup extends JDBCDelegatingTestSetup {

    protected PostGISJsonTestSetup() {
        super(new PostGISTestSetup());
    }

    protected boolean supportJsonB = false;

    @Override
    protected void setUpData() throws Exception {
        dropTestJsonTable();

        try (Connection cx = getConnection();
                Statement st = cx.createStatement();
                ResultSet rs = st.executeQuery("SELECT current_setting('server_version_num')")) {
            if (rs.next()) {
                try {
                    // JSONB has been introduced with version 9.4
                    supportJsonB = Integer.parseInt(rs.getString(1)) >= 90004;
                } catch (NumberFormatException nfe) {
                    // unable to determine PostgreSQL version because non-integer returned
                    supportJsonB = false;
                }
            }
        } catch (PSQLException pse) {
            // server_version_num was added in PostgreSQL 8.2, throws this exception if the
            // parameter is
            // not found
            supportJsonB = false;
        }
        createTestJsonTable();
    }

    private void createTestJsonTable() throws Exception {

        String sql =
                "CREATE TABLE \"jsontest\" ("
                        + "\"id\" INT, \"name\" VARCHAR, \"jsonColumn\" JSON, "
                        + (supportJsonB ? "\"jsonbColumn\" JSONB, " : "")
                        + "PRIMARY KEY(id))";
        run(sql);

        // Quoting from PostgreSQL Documentation:
        // --------------------------------------
        // There are two JSON data types: json and jsonb. They accept almost identical sets of
        // values as input.
        // The major practical difference is one of efficiency. The json data type stores an exact
        // copy of the input text [...]
        // while jsonb data is stored in a decomposed binary format [...].
        // Because the json type stores an exact copy of the input text, it will preserve
        // semantically-insignificant
        // white space between tokens, as well as the order of keys within JSON objects. Also, if a
        // JSON object within
        // the value contains the same key more than once, all the key/value pairs are kept.
        // By contrast, jsonb does not preserve white space, does not preserve the order of object
        // keys, and does not keep
        // duplicate object keys. If duplicate keys are specified in the input, only the last value
        // is kept.
        // One semantically-insignificant detail worth noting is that in jsonb, numbers will be
        // printed according
        // to the behavior of the underlying numeric type. In practice this means that numbers
        // entered with E notation
        // will be printed without it, for example:
        sql =
                "INSERT INTO \"jsontest\" VALUES (0, 'numberEntry','{\"weight\": 1e-3 }'"
                        + (supportJsonB ? ",'{\"weight\": 1e-3}'" : "")
                        + " );"
                        + "INSERT INTO \"jsontest\" VALUES (1, 'entryWithSpaces','{\"title\"    :    \"Title\" }'"
                        + (supportJsonB ? ",'{\"title\"    :    \"Title\" }'" : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (2, 'duppedKeyEntry', '{\"title\":\"Title1\", \"title\":\"Title2\"}'"
                        + (supportJsonB ? ",'{\"title\":\"Title1\", \"title\":\"Title2\"}'" : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (3, 'nullKey', '{\"title\":null}'"
                        + (supportJsonB ? ", '{\"title\":null}'" : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (4, 'nullEntry', NULL"
                        + (supportJsonB ? ", NULL" : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (5, 'arrayEntry', '{\"arrayValues\":[3,5,6]}'"
                        + (supportJsonB ? ", '{\"arrayValues\":[3,5,6]}'" : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (6, 'arrayEntry2', '{\"strVal\": \"stringValue\", \"arrayValues\":[3,6,7]}'"
                        + (supportJsonB
                                ? ", '{\"strVal\": \"stringValue\",\"arrayValues\":[3,6,7]}'"
                                : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (7, 'nestedObj', '{\"nestedObj\": {\"nestedProp\":\"stringValue\", \"nestedObj2\": {\"numProp\": 3, \"strProp\": \"stringValue2\"},\"nestedAr\":[3,6,7]}}'"
                        + (supportJsonB
                                ? ", '{\"nestedObj\": {\"nestedProp\":\"stringValue\", \"nestedObj2\": {\"numProp\": 3, \"strProp\": \"stringValue2\"}, \"nestedAr\":[3,6,7]}}'"
                                : "")
                        + ");"
                        + "INSERT INTO \"jsontest\" VALUES (8, 'nestedObj', '{\"nestedObj\": {\"nestedProp\":\"stringValue\", \"nestedObj2\": {\"numProp\": 4, \"strProp\": \"stringValue2\"}, \"nestedAr\":[3,5,7]}}'"
                        + (supportJsonB
                                ? ", '{\"nestedObj\": {\"nestedProp\":\"stringValue\", \"nestedObj2\": {\"numProp\": 4, \"strProp\": \"stringValue2\"}, \"nestedAr\":[3,5,7]}}'"
                                : "")
                        + ");";

        run(sql);
    }

    private void dropTestJsonTable() throws Exception {
        runSafe("DROP TABLE \"jsontest\" cascade");
    }
}
