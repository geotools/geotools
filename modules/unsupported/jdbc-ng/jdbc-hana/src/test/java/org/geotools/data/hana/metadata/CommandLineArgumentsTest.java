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
import junit.framework.TestCase;
import org.geotools.data.hana.HanaConnectionParameters;

/** @author Stefan Uhrig, SAP SE */
public class CommandLineArgumentsTest extends TestCase {

    public void testValidArgs() {
        String[] args = {"user", "host", "0"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        assertEquals(0, hcp.getInstance());
        assertNull(hcp.getDatabase());
    }

    public void testValidArgsWithDatabase() {
        String[] args = {"user", "host", "0", "DTB"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        assertEquals(0, hcp.getInstance());
        assertEquals("DTB", hcp.getDatabase());
    }

    public void testInvalidArgs() {
        PrintStream ps = new PrintStream(new ByteArrayOutputStream());
        PrintStream out = System.out;
        System.setOut(ps);
        try {
            String[] twoArgs = {"a", "b"};
            String[] fiveArgs = {"a", "b", "c", "d", "e"};
            String[] invalidInstance = {"user", "host", "a"};
            assertNull(CommandLineArguments.parse(twoArgs));
            assertNull(CommandLineArguments.parse(fiveArgs));
            assertNull(CommandLineArguments.parse(invalidInstance));
        } finally {
            System.setOut(out);
        }
    }
}
