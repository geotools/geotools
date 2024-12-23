/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import org.geotools.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class URConverterFactoryTest {

    URConverterFactory f;

    @Before
    public void setUp() throws Exception {
        f = new URConverterFactory();
    }

    @Test
    public void testStringToURL() throws Exception {
        Converter c = f.createConverter(String.class, URL.class, null);
        Assert.assertNotNull(c);

        Assert.assertEquals(new URL("http://foo.com"), c.convert("http://foo.com", URL.class));
        Assert.assertEquals(new File("/foo/bar").toURI().toURL(), c.convert("/foo/bar", URL.class));
    }

    @Test
    public void testStringToURI() throws Exception {
        Converter c = f.createConverter(String.class, URI.class, null);
        Assert.assertNotNull(c);

        Assert.assertEquals(new URI("http://foo.com"), c.convert("http://foo.com", URI.class));
        // assertEquals( new File( "/foo/bar" ).toURI() , c.convert( "/foo/bar", URI.class ) );
    }

    @Test
    public void testURIToURL() throws Exception {
        Converter c = f.createConverter(URL.class, URI.class, null);
        Assert.assertNotNull(c);

        Assert.assertEquals(new URI("http://foo.com"), c.convert(new URL("http://foo.com"), URI.class));
    }

    @Test
    public void testURLToURI() throws Exception {
        Converter c = f.createConverter(URI.class, URL.class, null);
        Assert.assertNotNull(c);

        Assert.assertEquals(new URL("http://foo.com"), c.convert(new URI("http://foo.com"), URL.class));
    }
    // JD: enable when factory registered
    //    public void testRegistered() throws Exception {
    //        assertEquals( new File( "/foo/bar").toURI().toURL() , Converters.convert( "/foo/bar",
    // URL.class ));
    //    }

}
