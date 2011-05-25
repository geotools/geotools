/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests for {@link AppSchemaResolver}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 *
 * @source $URL$
 */
public class AppSchemaResolverTest {

    /**
     * Test GeoSciML 2.0 canonical URL is correctly converted into a classpath URL.
     */
    @Test
    public void geosciml20() {
        String path = AppSchemaResolver
                .getSimpleHttpResourcePath("http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd");
        Assert.assertEquals(path, "/org/geosciml/www/geosciml/2.0/xsd/geosciml.xsd");
        String classpathPath = AppSchemaResolver.class.getResource(path).toExternalForm();
        Assert.assertTrue(classpathPath.startsWith("jar:file:/"));
        Assert.assertTrue(classpathPath
                .endsWith("!/org/geosciml/www/geosciml/2.0/xsd/geosciml.xsd"));
    }

    /**
     * Test that the example in the javadoc for
     * {@link AppSchemaResolver#getSimpleHttpResourcePath(java.net.URI)} does in fact work.
     */
    @Test
    public void exampleHttp() {
        String path = AppSchemaResolver
                .getSimpleHttpResourcePath("http://schemas.example.org/exampleml/exml.xsd");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /**
     * Test an https URL.
     */
    @Test
    public void exampleHttps() {
        String path = AppSchemaResolver
                .getSimpleHttpResourcePath("https://schemas.example.org/exampleml/exml.xsd");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /**
     * Test that port is ignored.
     */
    @Test
    public void portIgnored() {
        String path = AppSchemaResolver
                .getSimpleHttpResourcePath("http://schemas.example.org:8080/exampleml/exml.xsd");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /**
     * Test that a query is ignored
     */
    @Test
    public void queryIgnored() {
        String path = AppSchemaResolver
                .getSimpleHttpResourcePath("http://schemas.example.org/exampleml/exml.xsd?q=ignored");
        Assert.assertEquals("/org/example/schemas/exampleml/exml.xsd", path);
    }

    /**
     * Test that an ftp URL (not http/https) returns null.
     */
    @Test
    public void ftpReturnsNull() {
        Assert.assertNull(AppSchemaResolver
                .getSimpleHttpResourcePath("ftp://schemas.example.org/exampleml/exml.xsd"));
    }

    /**
     * Test that a jar URL (not http/https) returns null.
     */
    @Test
    public void jarReturnsNull() {
        Assert.assertNull(AppSchemaResolver.getSimpleHttpResourcePath("jar:file:example.jar"
                + "!/org/example/schemas/exampleml/exml.xsd"));
    }

    /**
     * Test that a URL not http/https return null.
     */
    @Test
    public void badlyFormattedUrlReturnsNull() {
        Assert.assertNull(AppSchemaResolver.getSimpleHttpResourcePath("http://schemas.example.org/"
                + "exampleml/with spaces/exml.xsd"));
    }

}
