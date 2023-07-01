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
package org.geotools.filter;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.expression.Expression;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Assert;
import org.junit.Test;

public class FilterFactoryBeforeTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testAfter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        Expression left = ff.literal(2);
        Expression right = ff.literal(1);

        PropertyIsGreaterThan filter = ff.greater(left, right);

        Assert.assertTrue(filter.evaluate(null));
        Assert.assertTrue(filter instanceof PropertyIsGreaterThan);
    }
}
