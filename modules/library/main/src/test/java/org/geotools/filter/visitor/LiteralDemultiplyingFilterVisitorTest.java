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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.MultiValuedFilter.MatchAction;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Test;

/**
 * Unit test for LiteralDemultiplyingFilter.
 *
 * @author Niels Charlier
 */
public class LiteralDemultiplyingFilterVisitorTest {

    static final FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testBinaryComparisonFilter() throws IllegalFilterException {

        LiteralDemultiplyingFilterVisitor visitor = new LiteralDemultiplyingFilterVisitor();

        Object object1 = 1;
        Object object2 = 2;
        Object object3 = 3;

        List<Object> values = new ArrayList<>();
        values.add(object1);
        values.add(object2);
        values.add(object3);

        PropertyName property = fac.property("property");
        Literal literal = fac.literal(values);
        Literal literal1 = fac.literal(object1);
        Literal literal2 = fac.literal(object2);
        Literal literal3 = fac.literal(object3);

        // ANY

        Filter filter = fac.greater(property, literal, true, MatchAction.ANY);
        Filter expected = fac.or(new ArrayList<>(Arrays.asList(
                fac.greater(property, literal1, true, MatchAction.ANY),
                fac.greater(property, literal2, true, MatchAction.ANY),
                fac.greater(property, literal3, true, MatchAction.ANY))));

        Filter demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);

        // ALL

        filter = fac.greater(property, literal, true, MatchAction.ALL);
        expected = fac.and(new ArrayList<>(Arrays.asList(
                fac.greater(property, literal1, true, MatchAction.ALL),
                fac.greater(property, literal2, true, MatchAction.ALL),
                fac.greater(property, literal3, true, MatchAction.ALL))));

        demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);

        // ONE

        filter = fac.greater(property, literal, true, MatchAction.ONE);
        expected = fac.or(new ArrayList<>(Arrays.asList(
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.greater(property, literal1, true, MatchAction.ONE),
                        fac.not(fac.greater(property, literal2, true, MatchAction.ONE)),
                        fac.not(fac.greater(property, literal3, true, MatchAction.ONE))))),
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.not(fac.greater(property, literal1, true, MatchAction.ONE)),
                        fac.greater(property, literal2, true, MatchAction.ONE),
                        fac.not(fac.greater(property, literal3, true, MatchAction.ONE))))),
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.not(fac.greater(property, literal1, true, MatchAction.ONE)),
                        fac.not(fac.greater(property, literal2, true, MatchAction.ONE)),
                        fac.greater(property, literal3, true, MatchAction.ONE)))))));

        demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);
    }

    @Test
    public void testBinarySpatialFilter() throws IllegalFilterException {

        GeometryBuilder builder = new GeometryBuilder();

        LiteralDemultiplyingFilterVisitor visitor = new LiteralDemultiplyingFilterVisitor();

        Object object1 = builder.point(1, 2);
        Object object2 = builder.point(3, 4);
        Object object3 = builder.point(5, 6);

        List<Object> values = new ArrayList<>();
        values.add(object1);
        values.add(object2);
        values.add(object3);

        PropertyName property = fac.property("property");
        Literal literal = fac.literal(values);
        Literal literal1 = fac.literal(object1);
        Literal literal2 = fac.literal(object2);
        Literal literal3 = fac.literal(object3);

        // ANY

        Filter filter = fac.beyond(property, literal, 5, "x", MatchAction.ANY);
        Filter expected = fac.or(new ArrayList<>(Arrays.asList(
                fac.beyond(property, literal1, 5, "x", MatchAction.ANY),
                fac.beyond(property, literal2, 5, "x", MatchAction.ANY),
                fac.beyond(property, literal3, 5, "x", MatchAction.ANY))));

        Filter demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);

        // ALL

        filter = fac.beyond(property, literal, 5, "x", MatchAction.ALL);
        expected = fac.and(new ArrayList<>(Arrays.asList(
                fac.beyond(property, literal1, 5, "x", MatchAction.ANY),
                fac.beyond(property, literal2, 5, "x", MatchAction.ANY),
                fac.beyond(property, literal3, 5, "x", MatchAction.ANY))));

        demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);

        // ONE

        filter = fac.beyond(property, literal, 5, "x", MatchAction.ONE);
        expected = fac.or(new ArrayList<>(Arrays.asList(
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.beyond(property, literal1, 5, "x", MatchAction.ONE),
                        fac.not(fac.beyond(property, literal2, 5, "x", MatchAction.ONE)),
                        fac.not(fac.beyond(property, literal3, 5, "x", MatchAction.ONE))))),
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.not(fac.beyond(property, literal1, 5, "x", MatchAction.ONE)),
                        fac.beyond(property, literal2, 5, "x", MatchAction.ONE),
                        fac.not(fac.beyond(property, literal3, 5, "x", MatchAction.ONE))))),
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.not(fac.beyond(property, literal1, 5, "x", MatchAction.ONE)),
                        fac.not(fac.beyond(property, literal2, 5, "x", MatchAction.ONE)),
                        fac.beyond(property, literal3, 5, "x", MatchAction.ONE)))))));

        demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);
    }

    @Test
    public void testBinaryTemporalFilter() throws IllegalFilterException {

        LiteralDemultiplyingFilterVisitor visitor = new LiteralDemultiplyingFilterVisitor();

        Object object1 = "2014-01-01";
        Object object2 = "2014-02-02";
        Object object3 = "2014-03-03";

        List<Object> values = new ArrayList<>();
        values.add(object1);
        values.add(object2);
        values.add(object3);

        PropertyName property = fac.property("property");
        Literal literal = fac.literal(values);
        Literal literal1 = fac.literal(object1);
        Literal literal2 = fac.literal(object2);
        Literal literal3 = fac.literal(object3);

        // ANY

        Filter filter = fac.after(property, literal, MatchAction.ANY);
        Filter expected = fac.or(new ArrayList<>(Arrays.asList(
                fac.after(property, literal1, MatchAction.ANY),
                fac.after(property, literal2, MatchAction.ANY),
                fac.after(property, literal3, MatchAction.ANY))));

        Filter demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);

        // ALL

        filter = fac.after(property, literal, MatchAction.ALL);
        expected = fac.and(new ArrayList<>(Arrays.asList(
                fac.after(property, literal1, MatchAction.ALL),
                fac.after(property, literal2, MatchAction.ALL),
                fac.after(property, literal3, MatchAction.ALL))));

        demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);

        // ONE

        filter = fac.after(property, literal, MatchAction.ONE);
        expected = fac.or(new ArrayList<>(Arrays.asList(
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.after(property, literal1, MatchAction.ONE),
                        fac.not(fac.after(property, literal2, MatchAction.ONE)),
                        fac.not(fac.after(property, literal3, MatchAction.ONE))))),
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.not(fac.after(property, literal1, MatchAction.ONE)),
                        fac.after(property, literal2, MatchAction.ONE),
                        fac.not(fac.after(property, literal3, MatchAction.ONE))))),
                fac.and(new ArrayList<>(Arrays.asList(
                        fac.not(fac.after(property, literal1, MatchAction.ONE)),
                        fac.not(fac.after(property, literal2, MatchAction.ONE)),
                        fac.after(property, literal3, MatchAction.ONE)))))));

        demultiplied = (Filter) filter.accept(visitor, null);
        assertEquals(demultiplied, expected);
    }
}
