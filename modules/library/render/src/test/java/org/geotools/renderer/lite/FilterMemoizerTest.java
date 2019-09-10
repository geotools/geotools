/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import org.geotools.data.DataTestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.expression.Function;

public class FilterMemoizerTest extends DataTestCase {

    static FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    public void testMemoizeFilter() throws Exception {
        SimpleFeature rf0 = Mockito.spy(roadFeatures[0]);
        SimpleFeature rf1 = Mockito.spy(roadFeatures[1]);

        Filter nameR1 = FF.equal(FF.property("name"), FF.literal("r1"), false);
        Filter memoized = FilterMemoizer.memoize(nameR1);

        // evaluate twice on rf0, just one call to getAttribute should be made
        assertTrue(memoized.evaluate(rf0));
        assertTrue(memoized.evaluate(rf0));
        Mockito.verify(rf0, Mockito.times(1)).getAttribute("name");

        // switch feature, not the same, new call should be made on it
        assertFalse(memoized.evaluate(rf1));
        Mockito.verify(rf1, Mockito.times(1)).getAttribute("name");

        // and back, another call expected, for a total of 2
        assertTrue(memoized.evaluate(rf0));
        Mockito.verify(rf0, Mockito.times(2)).getAttribute("name");
    }

    public void testVolatileFunction() throws Exception {
        Function random = FF.function("random");
        Function spy = Mockito.spy(random);

        PropertyIsGreaterThanOrEqualTo filter = FF.greaterOrEqual(spy, FF.literal(0));
        Filter memoized = FilterMemoizer.memoize(filter);

        assertTrue(memoized.evaluate(null));
        assertTrue(memoized.evaluate(null));
        Mockito.verify(spy, Mockito.times(2)).evaluate(null);
    }
}
