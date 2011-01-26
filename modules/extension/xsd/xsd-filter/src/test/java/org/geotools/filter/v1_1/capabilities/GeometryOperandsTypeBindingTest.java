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
import org.opengis.filter.capability.GeometryOperand;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:GeometryOperandsType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="GeometryOperandsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="GeometryOperand" type="ogc:GeometryOperandType"/&gt;
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
public class GeometryOperandsTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(GeometryOperand[].class, binding(OGC.GeometryOperandsType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.GeometryOperandsType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.geometryOperands(document, document);

        GeometryOperand[] operands = (GeometryOperand[]) parse(OGC.GeometryOperandsType);
        assertEquals(2, operands.length);
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.geometryOperands(),
                new QName(OGC.NAMESPACE, "GeometryOperands"), OGC.GeometryOperandsType);
        assertEquals(2,
            getElementsByQName(dom, new QName(OGC.NAMESPACE, "GeometryOperand")).getLength());
    }
}
