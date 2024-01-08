/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;
import org.geotools.api.filter.BinaryComparisonOperator;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.JsonPointerFunction;
import org.junit.Before;
import org.junit.Test;

public class JsonPointerFilterSplitterVisitorTest {

    private static final String PATH = "/some/test/path";
    private static final String TEST_VALUE = "testValue";
    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    JsonPointerFilterSplittingVisitor visitor;

    @Before
    public void setUp() {
        FilterCapabilities caps = new FilterCapabilities();
        caps.addType(JsonPointerFilterSplittingVisitor.class);
        caps.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
        visitor = new JsonPointerFilterSplittingVisitor(caps, null, null);
    }

    /** Tests that jsonPointer function is being delegated to DB */
    @Test
    public void jsonPointerSplitTest() {
        Function function = ff.function("jsonPointer", ff.property("TestColumn"), ff.literal(PATH));

        PropertyIsEqualTo testFilter = ff.equals(function, ff.literal(TEST_VALUE));

        visitor.visit(testFilter, null);
        assertEquals(Filter.INCLUDE, visitor.getFilterPost());
        BinaryComparisonOperator preFilter = (BinaryComparisonOperator) visitor.getFilterPre();
        JsonPointerFunction jsonPointerFunction = (JsonPointerFunction) preFilter.getExpression1();
        List<Expression> parameters = jsonPointerFunction.getParameters();
        PropertyName propertyName = (PropertyName) parameters.get(0);
        assertEquals("TestColumn", propertyName.getPropertyName());
        Literal literal = (Literal) parameters.get(1);
        assertEquals(PATH, literal.getValue());
    }

    /** Tests that visitor does not apply json pointer function if it is not being used */
    @Test
    public void nonJsonPointerSplittingTest() {
        PropertyIsEqualTo testFilter =
                ff.equals(ff.property("testProperty"), ff.literal(TEST_VALUE));

        visitor.visit(testFilter, null);
        assertEquals(Filter.INCLUDE, visitor.getFilterPost());
        BinaryComparisonOperator preFilter = (BinaryComparisonOperator) visitor.getFilterPre();
        assertFalse(preFilter.getExpression2() instanceof JsonPointerFunction);
    }
}
