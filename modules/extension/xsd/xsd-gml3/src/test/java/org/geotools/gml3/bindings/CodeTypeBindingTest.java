/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gml3.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.api.feature.ComplexAttribute;
import org.geotools.gml3.ComplexAttributeTestSupport;
import org.geotools.gml3.GML;
import org.junit.Test;
import org.w3c.dom.Document;

public class CodeTypeBindingTest extends ComplexAttributeTestSupport {

    private static String TEST_NAME_VALUE = "Test name";

    private static String TEST_NAME_CODESPACE = "urn:x-test:SomeNamingAuthority";

    @Test
    public void testEncode() throws Exception {
        ComplexAttribute myCode = gmlCodeType(GML.name, TEST_NAME_VALUE, TEST_NAME_CODESPACE);
        Document dom = encode(myCode, GML.name);
        // print(dom);
        assertEquals("gml:name", dom.getDocumentElement().getNodeName());
        assertEquals(TEST_NAME_VALUE, dom.getDocumentElement().getFirstChild().getNodeValue());
        assertNotNull(dom.getDocumentElement().getAttribute("codeSpace"));
        assertEquals(TEST_NAME_CODESPACE, dom.getDocumentElement().getAttribute("codeSpace"));
    }
}
