/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.geotools.data.DataTestCase;
import org.geotools.data.util.NullProgressListener;
import org.junit.Test;

public class VisitorResultTypeTest extends DataTestCase {

    @Test
    public void testAverageInteger() {
        AverageVisitor visitor = new AverageVisitor(ff.property("id"));
        assertEquals(Arrays.asList(Double.class), getResultTypes(visitor, Integer.class));
    }

    @Test
    public void testAverageDouble() {
        AverageVisitor visitor = new AverageVisitor(ff.property("flow"));
        assertEquals(Arrays.asList(Double.class), getResultTypes(visitor, Double.class));
    }

    @Test
    public void testMaxInteger() {
        MaxVisitor visitor = new MaxVisitor(ff.property("id"));
        assertEquals(Arrays.asList(Integer.class), getResultTypes(visitor, Integer.class));
    }

    @Test
    public void testMaxDouble() {
        MaxVisitor visitor = new MaxVisitor(ff.property("flow"));
        assertEquals(Arrays.asList(Double.class), getResultTypes(visitor, Double.class));
    }

    @Test
    public void testGroupByAverageInteger() {
        GroupByVisitor visitor = new GroupByVisitor(
                Aggregate.AVERAGE, ff.property("id"), Arrays.asList(ff.property("id")), new NullProgressListener());
        assertEquals(Arrays.asList(Double.class), getResultTypes(visitor, Integer.class, Integer.class));
    }

    @Test
    public void testGroupByAverageDouble() {
        GroupByVisitor visitor = new GroupByVisitor(
                Aggregate.AVERAGE, ff.property("id"), Arrays.asList(ff.property("flow")), new NullProgressListener());
        assertEquals(Arrays.asList(Double.class), getResultTypes(visitor, Integer.class, Double.class));
    }

    @Test
    public void testGroupByMaxInteger() {
        GroupByVisitor visitor = new GroupByVisitor(
                Aggregate.MAX, ff.property("id"), Arrays.asList(ff.property("id")), new NullProgressListener());
        assertEquals(Arrays.asList(Integer.class), getResultTypes(visitor, Integer.class, Integer.class));
    }

    @Test
    public void testGroupByMaxDouble() {
        GroupByVisitor visitor = new GroupByVisitor(
                Aggregate.MAX, ff.property("id"), Arrays.asList(ff.property("flow")), new NullProgressListener());
        assertEquals(Arrays.asList(Double.class), getResultTypes(visitor, Integer.class, Double.class));
    }

    private List<Class> getResultTypes(FeatureAttributeVisitor visitor, Class... inputTypes) {
        return visitor.getResultType(Arrays.asList(inputTypes)).get();
    }
}
