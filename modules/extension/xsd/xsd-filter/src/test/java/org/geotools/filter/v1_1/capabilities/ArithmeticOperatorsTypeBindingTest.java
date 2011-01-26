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
import org.opengis.filter.capability.ArithmeticOperators;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:ArithmeticOperatorsType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="ArithmeticOperatorsType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:SimpleArithmetic"/&gt;
 *          &lt;xsd:element name="Functions" type="ogc:FunctionsType"/&gt;
 *      &lt;/xsd:choice&gt;
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
public class ArithmeticOperatorsTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(ArithmeticOperators.class, binding(OGC.ArithmeticOperatorsType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.ArithmeticOperatorsType).getExecutionMode());
    }

    public void testParse1() throws Exception {
        FilterMockData.arithmetic(document, document);

        ArithmeticOperators arithmetic = (ArithmeticOperators) parse(OGC.ArithmeticOperatorsType);

        assertTrue(arithmetic.hasSimpleArithmetic());
        assertNotNull(arithmetic.getFunctions());
    }

    public void testParse2() throws Exception {
        FilterMockData.arithmetic(document, document, false);

        ArithmeticOperators arithmetic = (ArithmeticOperators) parse(OGC.ArithmeticOperatorsType);

        assertFalse(arithmetic.hasSimpleArithmetic());
        assertNotNull(arithmetic.getFunctions());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.arithmetic(true),
                new QName(OGC.NAMESPACE, "Arithmetic_Operators"), OGC.ArithmeticOperatorsType);

        assertNotNull(getElementByQName(dom, OGC.SimpleArithmetic));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "Functions")));

        dom = encode(FilterMockData.arithmetic(false),
                new QName(OGC.NAMESPACE, "Arithmetic_Operators"), OGC.ArithmeticOperatorsType);
        assertNull(getElementByQName(dom, OGC.SimpleArithmetic));
        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "Functions")));
    }
}
