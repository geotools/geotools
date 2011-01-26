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


public class Function_NamesTypeBindingTest extends FilterCapabilitiesTestSupport {
    public void testType() {
        assertEquals(FunctionName[].class, binding(OGC.Function_NamesType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Function_NamesType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.functionNames(document, document);

        FunctionName[] functions = (FunctionName[]) parse(OGC.Function_NamesType);

        assertEquals(2, functions.length);
        assertEquals("foo", functions[0].getName());
        assertEquals("bar", functions[1].getName());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.functionNames(),
                new QName(OGC.NAMESPACE, "Function_Names"), OGC.Function_NamesType);

        assertEquals(2, dom.getElementsByTagNameNS(OGC.NAMESPACE, "Function_Name").getLength());
    }
}
