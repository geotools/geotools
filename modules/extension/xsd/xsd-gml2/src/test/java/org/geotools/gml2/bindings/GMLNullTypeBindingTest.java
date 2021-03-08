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
package org.geotools.gml2.bindings;

import static org.junit.Assert.assertEquals;

import org.geotools.gml2.GML;
import org.geotools.xsd.ElementInstance;
import org.junit.Before;
import org.junit.Test;

public class GMLNullTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance nul;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        nul = createElement(GML.NAMESPACE, "myNull", GML.NullType, null);
    }

    @Test
    public void testAllowable() throws Exception {
        createNode(nul, null, null, null, null);

        GMLNullTypeBinding s = (GMLNullTypeBinding) getBinding(GML.NullType);

        assertEquals("inapplicable", s.parse(nul, "inapplicable"));
        assertEquals("unknown", s.parse(nul, "unknown"));
        assertEquals("unavailable", s.parse(nul, "unavailable"));
        assertEquals("missing", s.parse(nul, "missing"));
    }
}
