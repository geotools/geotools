/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Test;

public class CQL2PostPreFilterSplitterTest {

    static final String BASIC = "http://www.opengis.net/spec/cql2/1.0/conf/basic-cql2";
    static final String PROPERTY_PROPERTY =
            "http://www.opengis.net/spec/cql2/1.0/conf/property-property";

    static final String BASIC_SPATIAL =
            "http://www.opengis.net/spec/cql2/1.0/conf/basic-spatial-operators";

    static final String SPATIAL = "http://www.opengis.net/spec/cql2/1.0/conf/spatial-operators";

    static final String TEMPORAL = "http://www.opengis.net/spec/cql2/1.0/conf/temporal-operators";

    static final String ARITHMETIC = "http://www.opengis.net/spec/cql2/1.0/conf/arithmetic";

    private Filter[] visit(Filter filter, String... confArray) {
        List<String> conformance;
        if (confArray == null) conformance = Collections.emptyList();
        else conformance = Arrays.asList(confArray);

        CQL2PostPreFilterSplitter splitter = new CQL2PostPreFilterSplitter(conformance);
        filter.accept(splitter, null);
        return new Filter[] {splitter.getFilterPre(), splitter.getFilterPost()};
    }

    @Test
    public void testNoConformance() throws Exception {
        Filter filter = CQL.toFilter("eo:cloud_cover < 30");
        Filter[] filters = visit(filter);
        assertEquals(Filter.INCLUDE, filters[0]);
        assertEquals(filter, filters[1]);
    }

    @Test
    public void testBasicPropertyLiteral() throws Exception {
        Filter filter = ECQL.toFilter("eo:cloud_cover < 30 and 10 > eo:cloud_cover");
        Filter[] filters = visit(filter, BASIC);
        assertEquals(ECQL.toFilter("eo:cloud_cover < 30"), filters[0]);
        assertEquals(ECQL.toFilter("10 > eo:cloud_cover"), filters[1]);
    }

    @Test
    public void testBasicPropertyProperty() throws Exception {
        Filter filter = ECQL.toFilter("cloud_cover < 30 and 10 > cloud_cover");
        Filter[] filters = visit(filter, BASIC, PROPERTY_PROPERTY);
        assertEquals(filter, filters[0]);
        assertEquals(Filter.INCLUDE, filters[1]);
    }

    @Test
    public void testSpatialFiltersBasic() throws Exception {
        Filter filter = ECQL.toFilter("bbox(g, 10, 10, 20, 20) and overlaps(g, POINT(10 10))");
        Filter[] filters = visit(filter, BASIC, BASIC_SPATIAL);
        assertEquals(ECQL.toFilter("bbox(g, 10, 10, 20, 20)"), filters[0]);
        assertEquals(ECQL.toFilter("overlaps(g, POINT(10 10))"), filters[1]);
    }

    @Test
    public void testSpatialFiltersFull() throws Exception {
        Filter filter = ECQL.toFilter("bbox(g, 10, 10, 20, 20) and overlaps(g, POINT(10 10))");
        Filter[] filters = visit(filter, BASIC, SPATIAL);
        assertEquals(filter, filters[0]);
        assertEquals(Filter.INCLUDE, filters[1]);
    }

    @Test
    public void testSpatialFiltersPropertyLiteral() throws Exception {
        Filter filter = ECQL.toFilter("bbox(g, 10, 10, 20, 20) and overlaps(POINT(10 10), g)");
        Filter[] filters = visit(filter, BASIC, SPATIAL);
        assertEquals(ECQL.toFilter("bbox(g, 10, 10, 20, 20)"), filters[0]);
        assertEquals(ECQL.toFilter("overlaps(POINT(10 10), g)"), filters[1]);
    }

    @Test
    public void testSpatialNoTemporal() throws Exception {
        Filter filter =
                ECQL.toFilter("bbox(g, 10, 10, 20, 20) and time AFTER 2006-11-30T01:30:00Z");
        Filter[] filters = visit(filter, BASIC, SPATIAL);
        assertEquals(ECQL.toFilter("bbox(g, 10, 10, 20, 20)"), filters[0]);
        assertEquals(ECQL.toFilter("time AFTER 2006-11-30T01:30:00Z"), filters[1]);
    }

    @Test
    public void testSpatialTemporal() throws Exception {
        Filter filter =
                ECQL.toFilter("bbox(g, 10, 10, 20, 20) and time AFTER 2006-11-30T01:30:00Z");
        Filter[] filters = visit(filter, BASIC, SPATIAL, TEMPORAL);
        assertEquals(filter, filters[0]);
        assertEquals(Filter.INCLUDE, filters[1]);
    }

    @Test
    public void testArithmetics() throws Exception {
        Filter filter = ECQL.toFilter("a * 6 > 2");

        Filter[] filters = visit(filter, BASIC, ARITHMETIC);
        assertEquals(Filter.INCLUDE, filters[0]);
        assertEquals(filter, filters[1]);
    }

    @Test
    public void testArithmeticsPropertyProperty() throws Exception {
        Filter filter = ECQL.toFilter("a * 6 > 2");

        Filter[] filters = visit(filter, BASIC, ARITHMETIC, PROPERTY_PROPERTY);
        assertEquals(filter, filters[0]);
        assertEquals(Filter.INCLUDE, filters[1]);
    }
}
