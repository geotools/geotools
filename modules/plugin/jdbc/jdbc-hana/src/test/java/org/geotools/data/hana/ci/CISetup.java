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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/** @author Stefan Uhrig, SAP SE */
public final class CISetup extends CIBase {

    private static Logger LOGGER = Logging.getLogger(CISetup.class);

    private static final String HOST =
            "917df316-4e01-4a10-be54-eac1b6ab15fb.hana.prod-us10.hanacloud.ondemand.com";

    private static final int PORT = 443;

    private static final String USER = "GEOTOOLSCI";

    private static final boolean ENCRYPT = true;

    public static void main(String[] args) throws Exception {
        CISetup instance = new CISetup();
        instance.run();
    }

    private CISetup() {}

    private void run() throws Exception {
        writeTestFixture();
        Properties fixture = readFixture();
        ArrayList<String> oldSchemas;
        try (Connection conn = connectUsingFixture(readFixture())) {
            oldSchemas = getOldTestSchemas(conn);
        }
        dropSchemasInParallel(fixture, oldSchemas);
    }

    private void writeTestFixture() throws IOException {
        File dir = new File(new File(System.getProperty("user.home")), ".geotools");
        dir.mkdir();
        File file = new File(dir, "hana.properties");
        try (FileOutputStream os = new FileOutputStream(file)) {
            Properties fixture = generateTestFixture();
            fixture.store(os, "HANA test fixture");
        }
    }

    private Properties generateTestFixture() {
        Properties ret = new Properties();
        ret.setProperty("host", HOST);
        ret.setProperty("port", Integer.toString(PORT));
        ret.setProperty("user", USER);
        ret.setProperty("password", "id76Tn!klz81UT");
        ret.setProperty("pollution", "off");
        ret.setProperty("use ssl", Boolean.toString(ENCRYPT));
        String schemaBase = generateSchemaName();
        LOGGER.info("Using schemabase " + schemaBase);
        ret.setProperty("schemabase", schemaBase);
        return ret;
    }

    private ArrayList<String> getOldTestSchemas(Connection conn) throws SQLException {
        ArrayList<String> oldSchemas = new ArrayList<>();
        try (PreparedStatement psGetOldSchemas =
                conn.prepareStatement(
                        "SELECT SCHEMA_NAME FROM SYS.SCHEMAS WHERE SCHEMA_NAME LIKE 'geotools__%' ESCAPE '_' AND LOCALTOUTC(CREATE_TIME) < ?")) {
            Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
            String s = oneDayAgo.toString();
            LOGGER.info("Searching for geotools schemas created before " + s);
            psGetOldSchemas.setString(1, s);
            try (ResultSet rs = psGetOldSchemas.executeQuery()) {
                while (rs.next()) {
                    oldSchemas.add(rs.getString(1));
                }
                return oldSchemas;
            }
        }
    }

    private String generateSchemaName() {
        StringBuilder sb = new StringBuilder();
        sb.append("geotools_");
        sb.append(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                        .format(OffsetDateTime.now(ZoneOffset.UTC)));
        sb.append("_");
        sb.append(Long.toHexString(new Random().nextLong()));
        return sb.toString();
    }
}
