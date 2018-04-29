/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Literal;

public class BindingFilterVisitorTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    BindingFilterVisitor visitor;

    SimpleFeatureType ft;

    @Before
    public void setup() throws Exception {
        ft =
                DataUtilities.createType(
                        "test",
                        "theGeom:LineString,b:java.lang.Byte,s:java.lang.Short,i:java.lang.Integer,l:java.lang.Long,d:java.lang.Double,label:String");
        visitor = new BindingFilterVisitor(ft);
    }

    @Test
    public void equalsTest() {
        // forward
        PropertyIsEqualTo source = ff.equal(ff.property("i"), ff.literal("10"), true);
        PropertyIsEqualTo bound = (PropertyIsEqualTo) source.accept(visitor, null);
        assertEquals(new Integer(10), ((Literal) bound.getExpression2()).getValue());

        // flip it
        source = ff.equal(ff.literal("10"), ff.property("i"), true);
        bound = (PropertyIsEqualTo) source.accept(visitor, null);
        assertEquals(new Integer(10), ((Literal) bound.getExpression1()).getValue());
    }

    @Test
    public void notEqualsTest() {
        // forward
        PropertyIsNotEqualTo source = ff.notEqual(ff.property("i"), ff.literal("10"), true);
        PropertyIsNotEqualTo bound = (PropertyIsNotEqualTo) source.accept(visitor, null);
        assertEquals(new Integer(10), ((Literal) bound.getExpression2()).getValue());

        // flip it
        source = ff.notEqual(ff.literal("10"), ff.property("i"), true);
        bound = (PropertyIsNotEqualTo) source.accept(visitor, null);
        assertEquals(new Integer(10), ((Literal) bound.getExpression1()).getValue());
    }

    @Test
    public void between() {
        // among two literals
        PropertyIsBetween source = ff.between(ff.property("i"), ff.literal("10"), ff.literal("20"));
        PropertyIsBetween bound = (PropertyIsBetween) source.accept(visitor, null);
        assertEquals(new Integer(10), ((Literal) bound.getLowerBoundary()).getValue());
        assertEquals(new Integer(20), ((Literal) bound.getUpperBoundary()).getValue());

        // among two expression
        source = ff.between(ff.literal("10"), ff.property("i"), ff.property("i"));
        bound = (PropertyIsBetween) source.accept(visitor, null);
        assertEquals(new Integer(10), ((Literal) bound.getExpression()).getValue());

        // among two inconsistent expressions, cannot optimize
        source = ff.between(ff.literal("10"), ff.property("i"), ff.property("d"));
        bound = (PropertyIsBetween) source.accept(visitor, null);
        assertEquals("10", ((Literal) bound.getExpression()).getValue());
    }
}
