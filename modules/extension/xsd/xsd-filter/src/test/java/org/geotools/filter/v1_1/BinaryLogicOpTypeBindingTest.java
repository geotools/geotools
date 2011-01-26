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
package org.geotools.filter.v1_1;

import org.w3c.dom.Document;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Or;
import org.geotools.xml.Binding;


public class BinaryLogicOpTypeBindingTest extends FilterTestSupport {
    public void testBinaryLogicOpType() {
        assertEquals(BinaryLogicOperator.class, binding(OGC.BinaryLogicOpType).getType());
    }

    public void testAndType() {
        assertEquals(And.class, binding(OGC.And).getType());
    }

    public void testAndExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.And).getExecutionMode());
    }

    public void testAndParse() throws Exception {
        FilterMockData.and(document, document);

        And and = (And) parse();

        assertEquals(2, and.getChildren().size());
    }

    public void testAndEncode() throws Exception {
        Document dom = encode(FilterMockData.and(), OGC.And);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsEqualTo.getLocalPart())
               .getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsNotEqualTo.getLocalPart())
               .getLength());
    }

    public void testOrType() {
        assertEquals(Or.class, binding(OGC.Or).getType());
    }

    public void testOrExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.Or).getExecutionMode());
    }

    public void testOrParse() throws Exception {
        FilterMockData.or(document, document);

        Or or = (Or) parse();

        assertEquals(2, or.getChildren().size());
    }

    public void testOrEncode() throws Exception {
        Document dom = encode(FilterMockData.or(), OGC.Or);
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsEqualTo.getLocalPart())
               .getLength());
        assertEquals(1,
            dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyIsNotEqualTo.getLocalPart())
               .getLength());
    }
}
