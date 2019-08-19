/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import junit.framework.TestCase;
import org.xml.sax.helpers.NamespaceSupport;

public class ParserNamespaceSupportTest extends TestCase {

    public void testLookup() {
        ParserNamespaceSupport nsSupport = new ParserNamespaceSupport();
        assertNull(nsSupport.getURI("foo"));

        NamespaceSupport delegate = new NamespaceSupport();
        delegate.declarePrefix("foo", "http://foo.org");
        nsSupport.add(delegate);

        assertEquals("http://foo.org", nsSupport.getURI("foo"));

        nsSupport.declarePrefix("foo", "http://bar.org");
        assertEquals("http://bar.org", nsSupport.getURI("foo"));
    }

    public void testGetPrefixes() {
        ParserNamespaceSupport nsSupport = new ParserNamespaceSupport();
        nsSupport.declarePrefix("foo", "http://foo.org");

        NamespaceSupport delegate = new NamespaceSupport();
        delegate.declarePrefix("bar", "http://bar.org");

        nsSupport.add(delegate);

        List<String> prefixes = list(nsSupport.getPrefixes());
        assertTrue(prefixes.contains("foo"));
        assertTrue(prefixes.contains("bar"));

        assertTrue(prefixes.indexOf("foo") < prefixes.indexOf("bar"));
    }

    List list(Enumeration e) {
        List<Object> l = new ArrayList<Object>();
        while (e.hasMoreElements()) {
            l.add(e.nextElement());
        }
        return l;
    }
}
