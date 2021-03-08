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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.xsd.Binding;
import org.junit.Test;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.w3c.dom.Document;

public class BinaryComparisonOpTypeBindingTest
        extends org.geotools.filter.v1_0.BinaryComparisonOpTypeBindingTest {

    @Override
    @Test
    public void testBinaryComparisonOpType() {
        assertEquals(BinaryComparisonOperator.class, binding(OGC.BinaryComparisonOpType).getType());
    }

    @Override
    @Test
    public void testPropertyIsEqualToType() {
        assertEquals(PropertyIsEqualTo.class, binding(OGC.PropertyIsEqualTo).getType());
    }

    @Override
    @Test
    public void testPropertyIsEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsEqualTo).getExecutionMode());
    }

    @Override
    @Test
    public void testPropertyIsEqualToParse() throws Exception {
        FilterMockData.propertyIsEqualTo(document, document);

        PropertyIsEqualTo equalTo = (PropertyIsEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    @Override
    @Test
    public void testPropertyIsEqualToEncode() throws Exception {
        PropertyIsEqualTo equalTo = FilterMockData.propertyIsEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsEqualTo);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    @Override
    @Test
    public void testPropertyIsNotEqualToType() {
        assertEquals(PropertyIsNotEqualTo.class, binding(OGC.PropertyIsNotEqualTo).getType());
    }

    @Override
    @Test
    public void testPropertyIsNotEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsNotEqualTo).getExecutionMode());
    }

    @Override
    @Test
    public void testPropertyIsNotEqualToParse() throws Exception {
        FilterMockData.propertyIsNotEqualTo(document, document);

        PropertyIsNotEqualTo equalTo = (PropertyIsNotEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    @Override
    @Test
    public void testPropertyIsNotEqualToEncode() throws Exception {
        PropertyIsNotEqualTo equalTo = FilterMockData.propertyIsNotEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsNotEqualTo);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    @Override
    @Test
    public void testPropertyIsLessThanType() {
        assertEquals(PropertyIsLessThan.class, binding(OGC.PropertyIsLessThan).getType());
    }

    @Override
    @Test
    public void testPropertyIsLessThanExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsLessThan).getExecutionMode());
    }

    @Override
    @Test
    public void testPropertyIsLessThanParse() throws Exception {
        FilterMockData.propertyIsLessThan(document, document);

        PropertyIsLessThan equalTo = (PropertyIsLessThan) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    @Override
    @Test
    public void testPropertyIsLessThanEncode() throws Exception {
        PropertyIsLessThan equalTo = FilterMockData.propertyIsLessThan();

        Document dom = encode(equalTo, OGC.PropertyIsLessThan);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    @Override
    @Test
    public void testPropertyIsLessThanOrEqualToType() {
        assertEquals(
                PropertyIsLessThanOrEqualTo.class,
                binding(OGC.PropertyIsLessThanOrEqualTo).getType());
    }

    @Override
    @Test
    public void testPropertyIsLessThanOrEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsLessThanOrEqualTo).getExecutionMode());
    }

    @Override
    @Test
    public void testPropertyIsLessThanOrEqualToParse() throws Exception {
        FilterMockData.propertyIsLessThanOrEqualTo(document, document);

        PropertyIsLessThanOrEqualTo equalTo = (PropertyIsLessThanOrEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    @Override
    @Test
    public void testPropertyIsLessThanOrEqualToEncode() throws Exception {
        PropertyIsLessThanOrEqualTo equalTo = FilterMockData.propertyIsLessThanOrEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsLessThanOrEqualTo);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanType() {
        assertEquals(PropertyIsGreaterThan.class, binding(OGC.PropertyIsGreaterThan).getType());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsGreaterThan).getExecutionMode());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanParse() throws Exception {
        FilterMockData.propertyIsGreaterThan(document, document);

        PropertyIsGreaterThan equalTo = (PropertyIsGreaterThan) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanEncode() throws Exception {
        PropertyIsGreaterThan equalTo = FilterMockData.propertyIsGreaterThan();

        Document dom = encode(equalTo, OGC.PropertyIsGreaterThan);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanOrEqualToType() {
        assertEquals(
                PropertyIsGreaterThanOrEqualTo.class,
                binding(OGC.PropertyIsGreaterThanOrEqualTo).getType());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanOrEqualToExecutionMode() {
        assertEquals(Binding.AFTER, binding(OGC.PropertyIsGreaterThanOrEqualTo).getExecutionMode());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanOrEqualToParse() throws Exception {
        FilterMockData.propertyIsGreaterThanOrEqualTo(document, document);

        PropertyIsGreaterThanOrEqualTo equalTo = (PropertyIsGreaterThanOrEqualTo) parse();
        assertNotNull(equalTo);

        assertNotNull(equalTo.getExpression1());
        assertNotNull(equalTo.getExpression2());
    }

    @Override
    @Test
    public void testPropertyIsGreaterThanOrEqualToEncode() throws Exception {
        PropertyIsGreaterThanOrEqualTo equalTo = FilterMockData.propertyIsGreaterThanOrEqualTo();

        Document dom = encode(equalTo, OGC.PropertyIsGreaterThanOrEqualTo);
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.PropertyName.getLocalPart())
                        .getLength());
        assertEquals(
                1,
                dom.getElementsByTagNameNS(OGC.NAMESPACE, OGC.Literal.getLocalPart()).getLength());
    }
}
