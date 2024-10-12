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

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;
import org.geotools.api.filter.capability.GeometryOperand;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Binding test case for http://www.opengis.net/ogc:GeometryOperandsType.
 *
 * <p>
 *
 * <pre>
 *   <code>
 *  &lt;xsd:complexType name="GeometryOperandsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="GeometryOperand" type="ogc:GeometryOperandType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 *
 * @generated
 */
public class GeometryOperandsTypeBindingTest extends OGCTestSupport {
    @Test
    public void testType() {
        assertEquals(GeometryOperand[].class, binding(OGC.GeometryOperandsType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.GeometryOperandsType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.geometryOperands(document, document);

        GeometryOperand[] operands = (GeometryOperand[]) parse(OGC.GeometryOperandsType);
        assertEquals(2, operands.length);
    }

    @Test
    public void testEncode() throws Exception {
        Document dom = encode(
                FilterMockData.geometryOperands(),
                new QName(OGC.NAMESPACE, "GeometryOperands"),
                OGC.GeometryOperandsType);
        assertEquals(
                2,
                getElementsByQName(dom, new QName(OGC.NAMESPACE, "GeometryOperand"))
                        .getLength());
    }
}
