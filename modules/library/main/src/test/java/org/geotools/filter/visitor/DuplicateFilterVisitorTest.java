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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Collectors;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Unit test for DuplicatorFilterVisitor.
 *
 * @author Cory Horner, Refractions Research Inc.
 */
public class DuplicateFilterVisitorTest {

    private org.opengis.filter.FilterFactory2 fac;

    @Before
    public void setUp() throws Exception {
        fac = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    }

    @Test
    public void testLogicFilterDuplication() throws IllegalFilterException {
        // create a filter
        PropertyIsGreaterThan greater = fac.greater(fac.literal(2), fac.literal(1));
        PropertyIsLessThan less = fac.less(fac.literal(3), fac.literal(4));
        And and = fac.and(greater, less);

        // duplicate it
        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor();
        Filter newFilter = (Filter) and.accept(visitor, fac);

        // compare it
        Assert.assertNotNull(newFilter);
        Assert.assertEquals(and, newFilter);
    }

    @Test
    public void testOptimizationExample() {
        Expression add = fac.add(fac.literal(1), fac.literal(2));
        class Optimization extends DuplicatingFilterVisitor {
            @Override
            public Object visit(Add expression, Object extraData) {
                Expression expr1 = expression.getExpression1();
                Expression expr2 = expression.getExpression2();
                if (expr1 instanceof Literal && expr2 instanceof Literal) {
                    Double number1 = expr1.evaluate(null, Double.class);
                    Double number2 = expr2.evaluate(null, Double.class);

                    return ff.literal(number1.doubleValue() + number2.doubleValue());
                }
                return super.visit(expression, extraData);
            }
        };
        Expression modified = (Expression) add.accept(new Optimization(), null);
        Assert.assertTrue(modified instanceof Literal);
    }

    @Test
    public void testNotFilter() {
        // set GEOT-1566
        PropertyIsLike like = fac.like(fac.property("stringProperty"), "ab*");
        Not not = fac.not(like);
        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor(fac);
        Not clone = (Not) not.accept(visitor, null);
        Assert.assertEquals(not, clone);
        Assert.assertNotSame(not, clone);
        Assert.assertNotSame(like, clone.getFilter());
    }

    @Test
    public void testPreservedNamespaceContext() {
        // set GEOT-3756
        NamespaceSupport nsContext = new NamespaceSupport();
        nsContext.declarePrefix("f", "http://feature.example.org");
        Expression geometry = fac.property("f:name", nsContext);
        BBOX bbox = fac.bbox(geometry, 0, 0, 1, 1, "EPSG:4326");
        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor(fac);
        BBOX clone = (BBOX) bbox.accept(visitor, null);
        Assert.assertEquals(bbox, clone);
        Assert.assertNotSame(bbox, clone);
        Assert.assertSame(nsContext, ((PropertyName) clone.getExpression1()).getNamespaceContext());
    }

    @Test
    public void testNativeFilterIsDuplicated() {
        // build a filter that uses a native filter
        BBOX boundingBoxFilter =
                fac.bbox("geometry", -5, -5, 5, 5, DefaultGeographicCRS.WGS84.toString());
        NativeFilter nativeFilter = fac.nativeFilter("SOME NATIVE FILTER");
        Filter filter = fac.and(boundingBoxFilter, nativeFilter);
        // duplicate the filter
        DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor(fac);
        Filter duplicated = (Filter) filter.accept(visitor, null);
        // let's see if the native filter was correctly duplicated
        assertThat(duplicated, instanceOf(And.class));
        And andFilter = (And) duplicated;
        assertThat(andFilter.getChildren().size(), is(2));
        List<Filter> found =
                andFilter.getChildren().stream()
                        .filter(child -> child instanceof NativeFilter)
                        .collect(Collectors.toList());
        assertThat(found.size(), is(1));
        assertThat(((NativeFilter) found.get(0)).getNative(), is("SOME NATIVE FILTER"));
    }
}
