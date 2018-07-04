/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.styling.css.util;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;

public class FilterTypeVisitorTest {

    private FilterTypeVisitor visitor;

    private TypeAggregator aggregator;

    @Before
    public void setup() {
        aggregator = new TypeAggregator();
        visitor = new FilterTypeVisitor(aggregator);
    }

    @Test
    public void testGreterThan() throws Exception {
        Filter filter = CQL.toFilter("myAtt > 10");
        filter.accept(visitor, null);
        assertEquals(1, aggregator.types.size());
        assertEquals(Long.class, aggregator.types.get("myAtt"));
    }

    @Test
    public void testTimeEquals() throws Exception {
        Filter filter = CQL.toFilter("time TEQUALS 2006-11-30T01:30:00Z");
        filter.accept(visitor, null);
        assertEquals(1, aggregator.types.size());
        assertEquals(Date.class, aggregator.types.get("time"));
    }

    @Test
    public void testBetween() throws Exception {
        Filter filter = CQL.toFilter("a between 10 and 20.5");
        filter.accept(visitor, null);
        assertEquals(1, aggregator.types.size());
        assertEquals(Double.class, aggregator.types.get("a"));
    }

    @Test
    public void testMath() throws Exception {
        Filter filter = ECQL.toFilter("a + 3 > 5");
        filter.accept(visitor, null);
        assertEquals(1, aggregator.types.size());
        assertEquals(Double.class, aggregator.types.get("a"));
    }

    @Test
    public void testAnd() throws Exception {
        Filter filter = ECQL.toFilter("a > 5 and a < 10.0");
        filter.accept(visitor, null);
        assertEquals(1, aggregator.types.size());
        assertEquals(Double.class, aggregator.types.get("a"));
    }

    @Test
    public void testGeometry() throws Exception {
        Filter filter = ECQL.toFilter("CONTAINS(geom, POINT(1 2))");
        filter.accept(visitor, null);
        assertEquals(1, aggregator.types.size());
        assertEquals(Geometry.class, aggregator.types.get("geom"));
    }

    @Test
    public void testFunction() throws Exception {
        Filter filter = ECQL.toFilter("CONTAINS(buffer(geom, distance), POINT(1 2))");
        filter.accept(visitor, null);
        assertEquals(2, aggregator.types.size());
        assertEquals(Geometry.class, aggregator.types.get("geom"));
        assertEquals(Number.class, aggregator.types.get("distance"));
    }
}
