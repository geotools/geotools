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
package org.geotools.xs.bindings;

import javax.xml.namespace.QName;
import org.geotools.xsd.impl.NamespaceSupportWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.helpers.NamespaceSupport;

public class XSQNameBindingTest {
    XSQNameBinding binding;

    @Before
    public void setUp() throws Exception {
        NamespaceSupport ns = new NamespaceSupport();
        ns.declarePrefix("foo", "http://foo");

        binding = new XSQNameBinding(new NamespaceSupportWrapper(ns));
    }

    @Test
    public void testWithPrefix() throws Exception {
        QName qName = (QName) binding.parse(null, "foo:bar");
        Assert.assertNotNull(qName);

        Assert.assertEquals("foo", qName.getPrefix());
        Assert.assertEquals("http://foo", qName.getNamespaceURI());
        Assert.assertEquals("bar", qName.getLocalPart());
    }

    @Test
    public void testWithNoPrefix() throws Exception {
        QName qName = (QName) binding.parse(null, "bar:foo");
        Assert.assertNotNull(qName);

        Assert.assertEquals("bar", qName.getPrefix());
        Assert.assertEquals("", qName.getNamespaceURI());
        Assert.assertEquals("foo", qName.getLocalPart());
    }
}
