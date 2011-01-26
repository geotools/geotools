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
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ScalarCapabilities;
import org.geotools.xml.Binding;


public class Scalar_CapabilitiesTypeBindingTest extends FilterCapabilitiesTestSupport {
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

        assertNotNull(getElementByQName(dom, OGC.Logical_Operators));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "Comparison_Operators")));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "Arithmetic_Operators")));

        dom = encode(FilterMockData.scalarCapabilities(false),
                new QName(OGC.NAMESPACE, "Scalar_Capabilities"), OGC.Scalar_CapabilitiesType);
        assertNull(getElementByQName(dom, OGC.Logical_Operators));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "Comparison_Operators")));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "Arithmetic_Operators")));
    }
}
