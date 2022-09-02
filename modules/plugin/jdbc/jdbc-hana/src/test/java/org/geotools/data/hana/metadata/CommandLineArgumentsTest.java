/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.data.hana.metadata;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import org.geotools.data.hana.HanaConnectionParameters;
import org.junit.Assert;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class CommandLineArgumentsTest {

    @Test
    public void testValidArgsUsePort() {
        String[] args = {"user", "host:1234"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        Assert.assertNotNull(cla);
        Assert.assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        Assert.assertEquals("host", hcp.getHost());
        Assert.assertEquals(1234, hcp.getPort().intValue());
    }

    @Test
    public void testValidArgsUsePortSsl() {
        String[] args = {"user", "host:1234", "--ssl"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        Assert.assertNotNull(cla);
        Assert.assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        Assert.assertEquals("host", hcp.getHost());
        Assert.assertEquals(1234, hcp.getPort().intValue());
        Map<String, String> options = hcp.getAdditionalOptions();
        Assert.assertEquals("true", options.get("encrypt"));
    }

    @Test
    public void testValidArgsSingleContainer() {
        String[] args = {"user", "host", "0"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        Assert.assertNotNull(cla);
        Assert.assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        Assert.assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        Assert.assertEquals("0", options.get("instanceNumber"));
    }

    @Test
    public void testValidArgsSingleContainerSsl() {
        String[] args = {"user", "host", "0", "--ssl"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        Assert.assertNotNull(cla);
        Assert.assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        Assert.assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        Assert.assertEquals("0", options.get("instanceNumber"));
        Assert.assertEquals("true", options.get("encrypt"));
    }

    @Test
    public void testValidArgsMultiContainer() {
        String[] args = {"user", "host", "0", "DTB"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        Assert.assertNotNull(cla);
        Assert.assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        Assert.assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        Assert.assertEquals("0", options.get("instanceNumber"));
        Assert.assertEquals("DTB", options.get("databaseName"));
    }

    @Test
    public void testValidArgsMultiContainerSsl() {
        String[] args = {"user", "host", "0", "DTB", "--ssl"};
        CommandLineArguments cla = CommandLineArguments.parse(args);
        Assert.assertNotNull(cla);
        Assert.assertEquals("user", cla.getUser());
        HanaConnectionParameters hcp = cla.getConnectionParameters();
        Assert.assertEquals("host", hcp.getHost());
        Map<String, String> options = hcp.getAdditionalOptions();
        Assert.assertEquals("0", options.get("instanceNumber"));
        Assert.assertEquals("DTB", options.get("databaseName"));
        Assert.assertEquals("true", options.get("encrypt"));
    }

    @Test
    @SuppressWarnings("PMD.CloseResource") // no need to actually close the PrintStream
    public void testInvalidArgs() {
        PrintStream ps = new PrintStream(new ByteArrayOutputStream());
        PrintStream out = System.out;
        System.setOut(ps);
        try {
            String[] twoArgs = {"a", "b"};
            String[] fiveArgs = {"a", "b", "c", "d", "e"};
            String[] invalidInstance = {"user", "host", "a"};
            Assert.assertNull(CommandLineArguments.parse(twoArgs));
            Assert.assertNull(CommandLineArguments.parse(fiveArgs));
            Assert.assertNull(CommandLineArguments.parse(invalidInstance));
        } finally {
            System.setOut(out);
        }
    }
}
