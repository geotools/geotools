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
import org.geotools.data.hana.HanaTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;

/** @author Stefan Uhrig, SAP SE */
public class MetadataImportTest extends JDBCTestSupport {

    public void testMetadataImport() throws Exception {
        List<String> args = new ArrayList<String>();
        args.add(fixture.getProperty("user"));
        args.add(fixture.getProperty("host"));
        args.add(fixture.getProperty("instance"));
        String database = fixture.getProperty("database");
        if ((database != null) && !database.isEmpty()) {
            args.add(database);
        }
        MetadataImport.IPasswordReader passwordReader =
                new MetadataImport.IPasswordReader() {
                    @Override
                    public char[] readPassword() {
                        return fixture.getProperty("password").toCharArray();
                    }
                };

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
        return new HanaTestSetup();
    }
}
