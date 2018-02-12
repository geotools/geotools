/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** @author Stefan Uhrig, SAP SE */
public final class MetadataImport {

    public static void main(String[] args) {
        try {
            MetadataImport instance = new MetadataImport();
            System.exit(instance.run(args));
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private MetadataImport() {}

    private int run(String[] args) throws Exception {
        CommandLineArguments cla = CommandLineArguments.parse(args);
        if (cla == null) {
            return -1;
        }
        String password = requestPassword();
        Connection conn =
                DriverManager.getConnection(
                        cla.getConnectionParameters().buildUrl(), cla.getUser(), password);
        importUom(conn);
        importSrs(conn);
        return 0;
    }

    private void importUom(Connection conn) throws SQLException, IOException {
        try (PreparedStatement psUomExists =
                        conn.prepareStatement(
                                "SELECT COUNT(*) FROM PUBLIC.ST_UNITS_OF_MEASURE WHERE UNIT_NAME = ?");
                Statement createUom = conn.createStatement();
                InputStream is = MetadataImport.class.getResourceAsStream("uom.csv"); ) {
            UomReader reader = new UomReader(is);
            while (true) {
                Uom uom = reader.readNextUom();
                if (uom == null) {
                    break;
                }
                if (doesUomExist(psUomExists, uom.getName())) {
                    System.out.println(
                            "Skipping UOM \"" + uom.getName() + "\" as it already exists");
                    continue;
                }
                System.out.println("Creating UOM \"" + uom.getName() + "\"");
                createUom.execute(MetadataDdl.getUomDdl(uom));
            }
        }
    }

    private boolean doesUomExist(PreparedStatement psUomExists, String name) throws SQLException {
        psUomExists.setString(1, name);
        try (ResultSet rs = psUomExists.executeQuery()) {
            if (!rs.next()) {
                throw new AssertionError();
            }
            int count = rs.getInt(1);
            return count > 0;
        }
    }

    private void importSrs(Connection conn) throws SQLException, IOException {
        try (PreparedStatement psSrsExists =
                        conn.prepareStatement(
                                "SELECT COUNT(*) FROM PUBLIC.ST_SPATIAL_REFERENCE_SYSTEMS WHERE SRS_NAME = ? OR SRS_ID = ?");
                Statement createSrs = conn.createStatement();
                InputStream is = MetadataImport.class.getResourceAsStream("srs.csv")) {
            SrsReader reader = new SrsReader(is);
            while (true) {
                Srs srs = reader.readNextSrs();
                if (srs == null) {
                    break;
                }
                if (doesSrsExist(psSrsExists, srs.getName(), srs.getSrid())) {
                    System.out.println(
                            "Skipping SRS \"" + srs.getName() + "\" as it already exists");
                    continue;
                }
                System.out.println("Creating SRS \"" + srs.getName() + "\"");
                createSrs.execute(MetadataDdl.getSrsDdl(srs));
            }
        }
    }

    private boolean doesSrsExist(PreparedStatement psSrsExists, String name, int srid)
            throws SQLException {
        psSrsExists.setString(1, name);
        psSrsExists.setInt(2, srid);
        try (ResultSet rs = psSrsExists.executeQuery()) {
            if (!rs.next()) {
                throw new AssertionError();
            }
            int count = rs.getInt(1);
            return count > 0;
        }
    }

    private String requestPassword() {
        String password;
        Console console = System.console();
        if (console == null) {
            password = "dummy";
        } else {
            password = new String(console.readPassword("Password: "));
        }
        return password;
    }
}
