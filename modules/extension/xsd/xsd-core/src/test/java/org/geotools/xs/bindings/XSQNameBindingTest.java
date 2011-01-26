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

import junit.framework.TestCase;
import org.xml.sax.helpers.NamespaceSupport;
import javax.xml.namespace.QName;
import org.geotools.xml.impl.NamespaceSupportWrapper;


public class XSQNameBindingTest extends TestCase {
    XSQNameBinding binding;

    protected void setUp() throws Exception {
        NamespaceSupport ns = new NamespaceSupport();
        ns.declarePrefix("foo", "http://foo");
        
        binding = new XSQNameBinding(new NamespaceSupportWrapper(ns));
    }

    public void testWithPrefix() throws Exception {
        QName qName = (QName) binding.parse(null, "foo:bar");
        assertNotNull(qName);

        assertEquals("foo", qName.getPrefix());
        assertEquals("http://foo", qName.getNamespaceURI());
        assertEquals("bar", qName.getLocalPart());
    }

    public void testWithNoPrefix() throws Exception {
        QName qName = (QName) binding.parse(null, "bar:foo");
        assertNotNull(qName);

        assertEquals("bar", qName.getPrefix());
        assertEquals("", qName.getNamespaceURI());
        assertEquals("foo", qName.getLocalPart());
    }
}
