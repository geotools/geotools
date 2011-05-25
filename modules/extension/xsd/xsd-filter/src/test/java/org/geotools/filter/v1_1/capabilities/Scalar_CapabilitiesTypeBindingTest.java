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
import org.opengis.filter.capability.ScalarCapabilities;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:Scalar_CapabilitiesType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="Scalar_CapabilitiesType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="1" minOccurs="0" ref="ogc:LogicalOperators"/&gt;
 *          &lt;xsd:element maxOccurs="1" minOccurs="0"
 *              name="ComparisonOperators" type="ogc:ComparisonOperatorsType"/&gt;
 *          &lt;xsd:element maxOccurs="1" minOccurs="0"
 *              name="ArithmeticOperators" type="ogc:ArithmeticOperatorsType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class Scalar_CapabilitiesTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(ScalarCapabilities.class, binding(OGC.Scalar_CapabilitiesType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Scalar_CapabilitiesType).getExecutionMode());
    }

    public void testParse1() throws Exception {
        FilterMockData.scalarCapabilities(document, document);

        ScalarCapabilities scalar = (ScalarCapabilities) parse(OGC.Scalar_CapabilitiesType);

        assertTrue(scalar.hasLogicalOperators());
        assertNotNull(scalar.getComparisonOperators());
        assertNotNull(scalar.getArithmeticOperators());
    }

    public void testParse2() throws Exception {
        FilterMockData.scalarCapabilities(document, document, false);

        ScalarCapabilities scalar = (ScalarCapabilities) parse(OGC.Scalar_CapabilitiesType);

        assertFalse(scalar.hasLogicalOperators());
        assertNotNull(scalar.getComparisonOperators());
        assertNotNull(scalar.getArithmeticOperators());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.scalarCapabilities(true),
                new QName(OGC.NAMESPACE, "Scalar_Capabilities"), OGC.Scalar_CapabilitiesType);

        assertNotNull(getElementByQName(dom, OGC.LogicalOperators));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "ComparisonOperators")));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "ArithmeticOperators")));

        dom = encode(FilterMockData.scalarCapabilities(false),
                new QName(OGC.NAMESPACE, "Scalar_Capabilities"), OGC.Scalar_CapabilitiesType);
        assertNull(getElementByQName(dom, OGC.LogicalOperators));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "ComparisonOperators")));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "ArithmeticOperators")));
    }
}
