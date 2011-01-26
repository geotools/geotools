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
import org.opengis.filter.capability.ComparisonOperators;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:ComparisonOperatorsType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="ComparisonOperatorsType"&gt;
 *      &lt;xsd:sequence maxOccurs="unbounded"&gt;
 *          &lt;xsd:element name="ComparisonOperator" type="ogc:ComparisonOperatorType"/&gt;
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
public class ComparisonOperatorsTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(ComparisonOperators.class, binding(OGC.ComparisonOperatorsType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.ComparisonOperatorsType).getExecutionMode());
    }

    public void testParse1() throws Exception {
        FilterMockData.comparison(document, document);

        ComparisonOperators comparison = (ComparisonOperators) parse(OGC.ComparisonOperatorsType);

        assertNotNull(comparison.getOperator("LessThan"));
        assertNotNull(comparison.getOperator("GreaterThan"));
        assertNotNull(comparison.getOperator("LessThanOrEqualTo"));
        assertNotNull(comparison.getOperator("GreaterThanOrEqualTo"));
        assertNotNull(comparison.getOperator("EqualTo"));
        assertNotNull(comparison.getOperator("NotEqualTo"));
        assertNotNull(comparison.getOperator("Between"));
        assertNotNull(comparison.getOperator("Like"));
        assertNotNull(comparison.getOperator("NullCheck"));
    }

    public void testParse2() throws Exception {
        FilterMockData.comparison(document, document, false);

        ComparisonOperators comparison = (ComparisonOperators) parse(OGC.ComparisonOperatorsType);

        assertNull(comparison.getOperator("LessThan"));
        assertNull(comparison.getOperator("GreaterThan"));
        assertNull(comparison.getOperator("LessThanOrEqualTo"));
        assertNull(comparison.getOperator("GreaterThanOrEqualTo"));
        assertNull(comparison.getOperator("EqualTo"));
        assertNull(comparison.getOperator("NotEqualTo"));
        assertNotNull(comparison.getOperator("Between"));
        assertNotNull(comparison.getOperator("Like"));
        assertNotNull(comparison.getOperator("NullCheck"));
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.comparison(),
                new QName(OGC.NAMESPACE, "Comparison_Operators"), OGC.ComparisonOperatorsType);

        assertEquals(9,
            getElementsByQName(dom, new QName(OGC.NAMESPACE, "ComparisonOperator")).getLength());
    }
}
