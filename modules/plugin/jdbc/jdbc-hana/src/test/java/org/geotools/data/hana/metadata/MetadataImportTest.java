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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.geotools.data.hana.HanaTestSetupDefault;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class MetadataImportTest extends JDBCTestSupport {

    @SuppressWarnings("PMD.CloseResource") // no actual need to close the PrintStream
    @Test
    public void testMetadataImport() throws Exception {
        // This test pollutes the test database by creating non-schema specific metadata. Skip if
        // polluting tests are disabled.
        if ("off".equals(getFixture().getProperty("pollution", "on"))) return;

        List<String> args = new ArrayList<>();
        Properties fixture = getFixture();
        args.add(fixture.getProperty("user"));
        args.add(fixture.getProperty("host"));
        args.add(fixture.getProperty("instance"));
        String database = fixture.getProperty("database");
        if (database != null && !database.isEmpty()) {
            args.add(database);
        }
        MetadataImport.IPasswordReader passwordReader =
                () -> fixture.getProperty("password").toCharArray();

        PrintStream out = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        try {
            MetadataImport.execute(args.toArray(new String[args.size()]), passwordReader);
        } finally {
            System.setOut(out);
        }
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new HanaTestSetupDefault();
    }
}
