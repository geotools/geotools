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
package org.geotools.filter.visitor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.IdCollectorFilterVisitor;
import org.geotools.feature.visitor.IdFinderFilterVisitor;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.expression.PropertyName;

/**
 * This test checks that our filter visitor examples on the wiki are in working order.
 *
 * <ul>
 *   <li>DefaultFilterVisitor
 *   <li>NullFilterVisitor
 *   <li>ExtractBoundsFilterVisitor
 *   <li>...
 * </ul>
 *
 * @author Jody Garnett
 */
public class FilterVisitorTest {

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /** Example located on the wiki */
    @Test
    @SuppressWarnings("unchecked")
    public void testDefaultFilterVisitorFeatureIdExample() {
        Filter myFilter = ff.id(Collections.singleton(ff.featureId("fred")));
        FilterVisitor allFids =
                new DefaultFilterVisitor() {
                    @Override
                    public Object visit(Id filter, Object data) {
                        Set<Object> set = (Set) data;
                        set.addAll(filter.getIDs());
                        return set;
                    }
                };
        Set set = (Set) myFilter.accept(allFids, new HashSet());
        Assert.assertEquals(1, set.size());
    }
    /** Example located on the wiki */
    @Test
    @SuppressWarnings("unchecked")
    public void testDefaultFilterVisitorPropertyNameExample() {
        Filter myFilter = ff.greater(ff.add(ff.property("foo"), ff.property("bar")), ff.literal(1));

        class FindNames extends DefaultFilterVisitor {
            @Override
            public Object visit(PropertyName expression, Object data) {
                Set<Object> set = (Set) data;
                set.add(expression.getPropertyName());

                return set;
            }
        }
        Set set = (Set) myFilter.accept(new FindNames(), new HashSet());
        Assert.assertTrue(set.contains("foo"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNullFilterVisitor() {
        Filter filter = ff.isNull(ff.property("name"));
        Assert.assertEquals(Integer.valueOf(1), filter.accept(NullFilterVisitor.NULL_VISITOR, 1));

        filter = Filter.INCLUDE;
        Assert.assertEquals(Integer.valueOf(1), filter.accept(NullFilterVisitor.NULL_VISITOR, 1));

        FilterVisitor allFids =
                new NullFilterVisitor() {
                    @Override
                    public Object visit(Id filter, Object data) {
                        if (data == null) return null;
                        Set<Object> set = (Set) data;
                        set.addAll(filter.getIDs());
                        return set;
                    }
                };
        Filter myFilter = ff.id(Collections.singleton(ff.featureId("fred")));

        Set<Object> set = (Set) myFilter.accept(allFids, new HashSet<>());
        Assert.assertNotNull(set);
        Set<Object> set2 = (Set) myFilter.accept(allFids, null); // set2 will be null
        Assert.assertNull(set2);
    }

    @Test
    public void testIdFinderFilterVisitor() {
        Filter filter = ff.isNull(ff.property("name"));
        boolean found = (Boolean) filter.accept(new IdFinderFilterVisitor(), null);
        Assert.assertFalse(found);

        filter = ff.id(Collections.singleton(ff.featureId("eclesia")));
        found = (Boolean) filter.accept(new IdFinderFilterVisitor(), null);
        Assert.assertTrue(found);
    }

    @Test
    public void testIdCollector() {
        Filter filter = ff.isNull(ff.property("name"));
        Set fids = (Set) filter.accept(IdCollectorFilterVisitor.ID_COLLECTOR, new HashSet());
        Assert.assertTrue(fids.isEmpty());
        Assert.assertFalse(fids.contains("eclesia"));

        filter = ff.id(Collections.singleton(ff.featureId("eclesia")));
        fids = (Set) filter.accept(IdCollectorFilterVisitor.ID_COLLECTOR, new HashSet());
        Assert.assertFalse(fids.isEmpty());
        Assert.assertTrue(fids.contains("eclesia"));
    }
}
