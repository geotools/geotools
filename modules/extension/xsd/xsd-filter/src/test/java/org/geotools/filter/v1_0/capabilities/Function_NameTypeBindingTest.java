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
package org.geotools.filter.v1_0.capabilities;

import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.FunctionName;
import org.geotools.xml.Binding;


public class Function_NameTypeBindingTest extends FilterCapabilitiesTestSupport {
    public void testType() {
        assertEquals(FunctionName.class, binding(OGC.Function_NameType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Function_NameType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.functionName(document, document);

        FunctionName function = (FunctionName) parse(OGC.Function_NameType);
        assertEquals("foo", function.getName());
        assertEquals(2, function.getArgumentCount());
    }

    public void testEncode() throws Exception {
        FunctionName function = FilterMockData.functionName();
        Document dom = encode(function, new QName(OGC.NAMESPACE, "Function"), OGC.Function_NameType);

        assertEquals("foo", dom.getDocumentElement().getFirstChild().getNodeValue());
        assertEquals("2", dom.getDocumentElement().getAttribute("nArgs"));
    }
}
