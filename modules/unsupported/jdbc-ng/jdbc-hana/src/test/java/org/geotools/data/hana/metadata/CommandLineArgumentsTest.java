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
import java.util.Map;
import junit.framework.TestCase;
import org.geotools.data.hana.HanaConnectionParameters;

/** @author Stefan Uhrig, SAP SE */
public class CommandLineArgumentsTest extends TestCase {

    public void testValidArgsUsePort() {
        String[] args = {"user", "host:1234"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        assertEquals(1234, hcp.getPort().intValue());
    }

    public void testValidArgsUsePortSsl() {
        String[] args = {"user", "host:1234", "--ssl"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        assertEquals(1234, hcp.getPort().intValue());
        Map<String, String> options = hcp.getAdditionalOptions();
        assertEquals("true", options.get("encrypt"));
    }

    public void testValidArgsSingleContainer() {
        String[] args = {"user", "host", "0"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        assertEquals("0", options.get("instanceNumber"));
    }

    public void testValidArgsSingleContainerSsl() {
        String[] args = {"user", "host", "0", "--ssl"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        assertEquals("0", options.get("instanceNumber"));
        assertEquals("true", options.get("encrypt"));
    }

    public void testValidArgsMultiContainer() {
        String[] args = {"user", "host", "0", "DTB"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        assertEquals("0", options.get("instanceNumber"));
        assertEquals("DTB", options.get("databaseName"));
    }

    public void testValidArgsMultiContainerSsl() {
        String[] args = {"user", "host", "0", "DTB", "--ssl"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        assertNotNull(cla);
        assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        assertEquals("0", options.get("instanceNumber"));
        assertEquals("DTB", options.get("databaseName"));
        assertEquals("true", options.get("encrypt"));
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
