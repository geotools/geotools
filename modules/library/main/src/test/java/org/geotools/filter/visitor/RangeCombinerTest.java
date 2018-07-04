/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;

public class RangeCombinerTest {

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    ExpressionTypeVisitor visitor;

    SimpleFeatureType ft;

    @Before
    public void setup() throws Exception {
        ft =
                DataUtilities.createType(
                        "test",
                        "theGeom:LineString,b:java.lang.Byte,s:java.lang.Short,i:java.lang.Integer,l:java.lang.Long,d:java.lang.Double,label:String");
        visitor = new ExpressionTypeVisitor(ft);
    }

    @Test
    public void testOtherFilter() {
        PropertyName label = ff.property("label");
        Filter f1 = ff.greater(label, ff.literal("abc"));
        Filter f2 = ff.greater(label, ff.literal("adc"));
        Filter f3 = ff.notEqual(ff.function("random"), ff.property("i"));
        RangeCombiner rc = new RangeCombiner.And(ff, ft, Arrays.asList(f1, f2, f3));
        List<Filter> reduced = rc.getReducedFilters();
        assertEquals(2, reduced.size());
        assertTrue(reduced.contains(f2));
        assertTrue(reduced.contains(f3));
    }
}
