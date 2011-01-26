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

import org.eclipse.xsd.XSDSimpleTypeDefinition;
import javax.xml.namespace.QName;
import org.geotools.xml.SimpleBinding;
import org.geotools.xs.TestSchema;
import org.geotools.xs.XS;


public class XSAnySimpleTypeStrategyTest extends TestSchema {
    private XSDSimpleTypeDefinition typeDef;
    private SimpleBinding stratagy;

    protected void setUp() throws Exception {
        super.setUp();
        typeDef = xsdSimple(XS.ANYSIMPLETYPE.getLocalPart());
        stratagy = (SimpleBinding) stratagy(XS.ANYSIMPLETYPE);
    }

    public void testSetUp() {
        assertNotNull("XSD typedef", typeDef);
        assertNotNull("found anySimpleType", stratagy);
    }

    public void testAnyTypeParse() throws Exception {
        assertEquals("  hello world",
            stratagy.parse(element("  hello world", XS.ANYSIMPLETYPE), "  hello world"));
    }

    public void testHandlingOfWhiteSpace() throws Exception {
        assertEquals("123", stratagy.parse(element("  123", XS.DECIMAL), "123"));
    }

    protected QName getQName() {
        // TODO Auto-generated method stub
        return null;
    }
}
