/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.resolver;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SchemaResolver}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class SchemaResolverTest {

    /** Test GeoSciML 2.0 canonical URL is correctly converted into a classpath URL. */
    @Test
    public void geosciml20() {
        String path =
                SchemaResolver.getSimpleHttpResourcePath(
                        "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd");
        Assert.assertEquals(path, "/org/geosciml/www/geosciml/2.0/xsd/geosciml.xsd");
        String classpathPath = SchemaResolver.class.getResource(path).toExternalForm();
        Assert.assertTrue(classpathPath.startsWith("jar:file:/"));
        Assert.assertTrue(
                classpathPath.endsWith("!/org/geosciml/www/geosciml/2.0/xsd/geosciml.xsd"));
    }

    /**
     * Test that the example in the javadoc for {@link
     * SchemaResolver#getSimpleHttpResourcePath(java.net.URI)} does in fact work.
     */
    @Test
    public void exampleHttp() {
        String path =
                SchemaResolver.getSimpleHttpResourcePath(
                        "http://schemas.example.org/exampleml/exml.xsd");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /** Test an https URL. */
    @Test
    public void exampleHttps() {
        String path =
                SchemaResolver.getSimpleHttpResourcePath(
                        "https://schemas.example.org/exampleml/exml.xsd");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /** Test that port is ignored. */
    @Test
    public void portIgnored() {
        String path =
                SchemaResolver.getSimpleHttpResourcePath(
                        "http://schemas.example.org:8080/exampleml/exml.xsd");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /** Test that a query is ignored. */
    @Test
    public void queryIgnored() {
        String path =
                SchemaResolver.getSimpleHttpResourcePath(
                        "http://schemas.example.org/exampleml/exml.xsd?q=ignored");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /** Test that a query is converted to an MD5 hash where kept. */
    @Test
    public void getSimpleHttpResourcePath_KeepQueryTrue_ReturnPathHasQueryComponent() {
        String path =
                SchemaResolver.getSimpleHttpResourcePath(
                        "http://schemas.example.org/wfs?request=GetFeature&typename=sa:LocatedSpecimen&featureid=sa_LocatedSpecimen.2110193",
                        true);
        Assert.assertEquals("/org/example/schemas/wfs.0dd5330a4f06ea193985897a2cfd65d3.xsd", path);
    }

    /** Test that an ftp URL (not http/https) returns null. */
    @Test
    public void ftpReturnsNull() {
        Assert.assertNull(
                SchemaResolver.getSimpleHttpResourcePath(
                        "ftp://schemas.example.org/exampleml/exml.xsd"));
    }

    /** Test that a jar URL (not http/https) returns null. */
    @Test
    public void jarReturnsNull() {
        Assert.assertNull(
                SchemaResolver.getSimpleHttpResourcePath(
                        "jar:file:example.jar!/org/example/schemas/exampleml/exml.xsd"));
    }

    /** Test that a URL not http/https return null. */
    @Test
    public void badlyFormattedUrlReturnsNull() {
        Assert.assertNull(
                SchemaResolver.getSimpleHttpResourcePath(
                        "http://schemas.example.org/exampleml/with spaces/exml.xsd"));
    }
}
