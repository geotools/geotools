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
package org.geotools.filter.v1_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;

public class OGCPropertyIsBetweenTypeBindingTest extends FilterTestSupport {

    @Test
    public void testLowerBoundaryType() {
        assertEquals(Expression.class, binding(OGC.LowerBoundaryType).getType());
    }

    @Test
    public void testLowerBoundaryExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.LowerBoundaryType).getExecutionMode());
    }

    @Test
    public void testUpperBoundaryType() {
        assertEquals(Expression.class, binding(OGC.UpperBoundaryType).getType());
    }

    @Test
    public void testUpperBoundaryExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.UpperBoundaryType).getExecutionMode());
    }

    @Test
    public void testType() {
        assertEquals(PropertyIsBetween.class, binding(OGC.PropertyIsBetweenType).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.PropertyIsBetweenType).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.propertyIsBetween(document, document);

        PropertyIsBetween between = (PropertyIsBetween) parse();

        assertTrue(between.getExpression() instanceof PropertyName);
        assertTrue(between.getLowerBoundary() instanceof Literal);
        assertTrue(between.getUpperBoundary() instanceof Literal);
    }

    @Test
    public void testEncode() throws Exception {
        Document doc = encode(FilterMockData.propertyIsBetween(), OGC.PropertyIsBetween);
        assertEquals(
                1,
                doc.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "LowerBoundary").getLength());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "UpperBoundary").getLength());
    }

    @Test
    public void testEncodeAsFilter() throws Exception {
        Document doc = encode(FilterMockData.propertyIsBetween(), OGC.Filter);

        assertEquals(
                1,
                doc.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "LowerBoundary").getLength());
        assertEquals(1, doc.getElementsByTagNameNS(OGC.NAMESPACE, "UpperBoundary").getLength());
    }
}
