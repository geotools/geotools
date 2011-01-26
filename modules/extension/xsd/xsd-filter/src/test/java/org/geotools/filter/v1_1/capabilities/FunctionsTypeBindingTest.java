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
package org.geotools.filter.v1_1.capabilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.Functions;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:FunctionsType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="FunctionsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="FunctionNames" type="ogc:FunctionNamesType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class FunctionsTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(Functions.class, binding(OGC.FunctionsType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.FunctionsType).getExecutionMode());
    }

    public void testParse() throws Exception {
        Element element = FilterMockData.element(document, document,
                new QName(OGC.NAMESPACE, "Functions"));
        FilterMockData.functionNames(document, element);

        Functions functions = (Functions) parse(OGC.FunctionsType);
        assertEquals(2, functions.getFunctionNames().size());
    }

    public void testEncode() throws Exception {
        Functions functions = FilterMockData.functions();
        Document dom = encode(functions, new QName(OGC.NAMESPACE, "Functions"), OGC.FunctionsType);

        assertEquals(1, dom.getElementsByTagNameNS(OGC.NAMESPACE, "FunctionNames").getLength());
        assertEquals(2, dom.getElementsByTagNameNS(OGC.NAMESPACE, "FunctionName").getLength());
    }
}
