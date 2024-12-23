/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.ci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/** @author Stefan Uhrig, SAP SE */
public final class CITeardown extends CIBase {

    private static Logger LOGGER = Logging.getLogger(CITeardown.class);

    public static void main(String[] args) throws Exception {
        CITeardown instance = new CITeardown();
        instance.run();
        LOGGER.info("Teardown done");
    }

    private CITeardown() {}

    private void run() throws Exception {
        Properties fixture = readFixture();
        ArrayList<String> schemas;
        try (Connection conn = connectUsingFixture(fixture)) {
            String schemaBase = fixture.getProperty("schemabase");
            schemas = getSchemas(conn, schemaBase);
        }
        dropSchemasInParallel(fixture, schemas);
    }

    private ArrayList<String> getSchemas(Connection conn, String schemaBase) throws SQLException {
        schemaBase = schemaBase.replace("_", "__") + "%";
        try (PreparedStatement ps =
                conn.prepareStatement("SELECT SCHEMA_NAME FROM SYS.SCHEMAS WHERE SCHEMA_NAME LIKE ? ESCAPE '_'")) {
            ps.setString(1, schemaBase);
            try (ResultSet rs = ps.executeQuery()) {
                ArrayList<String> ret = new ArrayList<>();
                while (rs.next()) {
                    ret.add(rs.getString(1));
                }
                return ret;
            }
        }
    }
}
