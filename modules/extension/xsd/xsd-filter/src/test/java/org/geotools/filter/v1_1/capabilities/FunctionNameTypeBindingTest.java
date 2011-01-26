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
import javax.xml.namespace.QName;
import org.opengis.filter.capability.FunctionName;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:FunctionNameType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="FunctionNameType"&gt;
 *      &lt;xsd:simpleContent&gt;
 *          &lt;xsd:extension base="xsd:string"&gt;
 *              &lt;xsd:attribute name="nArgs" type="xsd:string" use="required"/&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:simpleContent&gt;
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
public class FunctionNameTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(FunctionName.class, binding(OGC.FunctionNameType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.FunctionNameType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.functionName(document, document);

        FunctionName function = (FunctionName) parse(OGC.FunctionNameType);

        assertEquals("foo", function.getName());
        assertEquals(2, function.getArgumentCount());
    }

    public void testEncode() throws Exception {
        FunctionName function = FilterMockData.functionName();
        Document dom = encode(function, new QName(OGC.NAMESPACE, "FunctionName"),
                OGC.FunctionNameType);

        assertEquals("foo", dom.getDocumentElement().getFirstChild().getNodeValue());
        assertEquals("2", dom.getDocumentElement().getAttribute("nArgs"));
    }
}
