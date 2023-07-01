/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.InternalFunction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.expression.InternalVolatileFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for DuplicatorFilterVisitor.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class DuplicatorFilterVisitorTest {
    FilterFactory fac;

    @Before
    public void setUp() throws Exception {
        fac = CommonFactoryFinder.getFilterFactory(null);
    }

    @Test
    public void testLogicFilterDuplication() throws IllegalFilterException {
        List<Filter> filters = new ArrayList<>();
        // create a filter
        Filter filter1 = fac.greater(fac.literal(2), fac.literal(1));
        filters.add(filter1);
        Filter filter2 = fac.greater(fac.literal(4), fac.literal(3));
        filters.add(filter2);

        And oldFilter = fac.and(filters);
        // duplicate it
        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor((FilterFactory2) fac);
        Filter newFilter = (Filter) oldFilter.accept(visitor, null);

        // compare it
        Assert.assertNotNull(newFilter);
        // TODO: a decent comparison
    }

    @Test
    public void testDuplicateInternalFunction() throws IllegalFilterException {
        class TestInternalFunction extends InternalVolatileFunction {

            @Override
            public Object evaluate(Object object) {
                return null;
            }

            @Override
            public InternalFunction duplicate(Expression... parameters) {
                return new TestInternalFunction();
            }
        }

        Expression internalFunction = new TestInternalFunction();
        Filter filter = fac.isNull(internalFunction);

        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor((FilterFactory2) fac);
        Filter newFilter = (Filter) filter.accept(visitor, null);

        Assert.assertTrue(newFilter instanceof PropertyIsNull);
        Expression newExpression = ((PropertyIsNull) newFilter).getExpression();
        Assert.assertNotNull(newExpression);
        Assert.assertTrue(newExpression instanceof TestInternalFunction);
        Assert.assertNotSame(internalFunction, newExpression);
    }
}
