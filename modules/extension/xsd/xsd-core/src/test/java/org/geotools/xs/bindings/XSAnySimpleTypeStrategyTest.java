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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;
import org.geotools.xsd.SimpleBinding;
import org.junit.Before;
import org.junit.Test;

public class XSAnySimpleTypeStrategyTest extends TestSchema {
    private XSDSimpleTypeDefinition typeDef;
    private SimpleBinding stratagy;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        typeDef = xsdSimple(XS.ANYSIMPLETYPE.getLocalPart());
        stratagy = (SimpleBinding) stratagy(XS.ANYSIMPLETYPE);
    }

    @Test
    public void testSetUp() {
        assertNotNull("XSD typedef", typeDef);
        assertNotNull("found anySimpleType", stratagy);
    }

    @Test
    public void testAnyTypeParse() throws Exception {
        assertEquals(
                "  hello world",
                stratagy.parse(element("  hello world", XS.ANYSIMPLETYPE), "  hello world"));
    }

    @Test
    public void testHandlingOfWhiteSpace() throws Exception {
        assertEquals("123", stratagy.parse(element("  123", XS.DECIMAL), "123"));
    }

    protected QName getQName() {
        // TODO Auto-generated method stub
        return null;
    }
}
