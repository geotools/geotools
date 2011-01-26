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
package org.geotools.xs;

import org.eclipse.xsd.XSDSimpleTypeDefinition;
import java.lang.reflect.Field;
import javax.xml.namespace.QName;
import org.geotools.xml.ElementInstance;


public class TestSchemaTest extends TestSchema {
    public void testInitialize() {
        assertNotNull(url);
        assertNotNull(schema);
        assertNotNull(factory);
    }

    public void testStratagyQName() throws Exception {
        assertNotNull(stratagy(XS.ANYSIMPLETYPE));
    }

    public void testStratagyName() throws Exception {
        assertNotNull(stratagy("anySimpleType"));

        try {
            stratagy("bork");
            fail("bork should not be found");
        } catch (Exception expected) {
            // good!
        }
    }

    public void testXS() throws Exception {
        assertEquals(XS.ANYSIMPLETYPE, xs("anySimpleType"));

        try {
            xs("bork");
            fail("bork should not be found");
        } catch (Exception expected) {
            // good!
        }
    }

    public void testSchemaIdentiy() {
        assertNotNull(schema);
        assertNotNull(xsd);
        assertEquals("1.0", xsd.getVersion());
    }

    /** Look into "builtin" schema for schema (aka xsd ?) */
    public void testXSDSimpleTypes() throws Exception {
        XSDSimpleTypeDefinition any = xsdSimple("anySimpleType");
        assertNotNull("Found", any);
    }

    /** Look into parsed schema - should agree with XMLSchema */
    public void testSchemaSimpleTypes() throws Exception {
        XSDSimpleTypeDefinition any = xsdSimple("anySimpleType");
        assertNotNull("Found", any);
    }

    public void testAllSimpleTypes() throws Exception {
        Class xs = XS.class;
        Field[] fields = xs.getFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            if (field.getType() != QName.class) {
                continue;
            }

            QName name = (QName) field.get(null);
            XSDSimpleTypeDefinition aXsd = xsdSimple(name.getLocalPart());

            if (aXsd == null) {
                //System.out.println( "Could not find binding for " + name.getLocalPart() );
            }
        }
    }

    public void testElement() {
        ElementInstance element = element(" hello world ", XS.ANYSIMPLETYPE);
        assertEquals(" hello world ", element.getText());
        assertEquals(xsdSimple(XS.ANYSIMPLETYPE.getLocalPart()),
            element.getElementDeclaration().getType());
    }

    protected QName getQName() {
        // TODO Auto-generated method stub
        return null;
    }
}
