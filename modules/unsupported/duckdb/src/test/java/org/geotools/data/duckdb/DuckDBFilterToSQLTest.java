/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.NativeFilter;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.FilterFunction_strToLowerCase;
import org.junit.Test;

public class DuckDBFilterToSQLTest {
    private final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    @Test
    public void testCapabilitiesDoNotSupportNativeFilter() {
        DuckDBFilterToSQL encoder = new DuckDBFilterToSQL();
        NativeFilter nativeFilter = CommonFactoryFinder.getFilterFactory(null).nativeFilter("1 = 1");

        assertFalse(encoder.getCapabilities().supports(nativeFilter));
        assertFalse(encoder.getCapabilities().fullySupports(nativeFilter));
    }

    @Test
    public void testEncodingNativeFilterIsRejected() {
        DuckDBFilterToSQL encoder = new DuckDBFilterToSQL();
        NativeFilter nativeFilter = CommonFactoryFinder.getFilterFactory(null).nativeFilter("1 = 1");

        try {
            encoder.encodeToString(nativeFilter);
            fail("Expected native filter encoding to be rejected");
        } catch (FilterToSQLException e) {
            assertTrue(e.getMessage().contains("not supported"));
        }
    }

    @Test
    public void testDirectNativeFilterVisitIsRejected() {
        DuckDBFilterToSQL encoder = new DuckDBFilterToSQL();
        NativeFilter nativeFilter = CommonFactoryFinder.getFilterFactory(null).nativeFilter("1 = 1");

        try {
            encoder.visit(nativeFilter, null);
            fail("Expected direct native filter visit to be rejected");
        } catch (UnsupportedOperationException e) {
            assertTrue(e.getMessage().contains("disabled"));
        }
    }

    @Test
    public void testEncodingContainsUsesStContains() throws Exception {
        Filter filter = ff.contains(ff.property("geom_a"), ff.property("geom_b"));

        assertEquals("WHERE ST_Contains(geom_a, geom_b)", encode(filter));
    }

    @Test
    public void testEncodingIntersectsUsesStIntersects() throws Exception {
        Filter filter = ff.intersects(ff.property("geom_a"), ff.property("geom_b"));

        assertEquals("WHERE ST_Intersects(geom_a, geom_b)", encode(filter));
    }

    @Test
    public void testEncodingDWithinUsesStDWithin() throws Exception {
        Filter filter = ff.dwithin(ff.property("geom_a"), ff.property("geom_b"), 12.5, "meters");

        assertEquals("WHERE ST_DWithin(geom_a, geom_b, 12.500000)", encode(filter));
    }

    @Test
    public void testEncodingBeyondUsesNegatedStDWithin() throws Exception {
        Filter filter = ff.beyond(ff.property("geom_a"), ff.property("geom_b"), 50.0, "meters");

        assertEquals("WHERE NOT ST_DWithin(geom_a, geom_b, 50.000000)", encode(filter));
    }

    @Test
    public void testEncodingBboxUsesStIntersects() throws Exception {
        Filter filter = ff.bbox("geom_a", -10, -20, 10, 20, null);
        String sql = encode(filter);

        assertTrue(sql.startsWith("WHERE ST_Intersects(geom_a,"));
        assertTrue(sql.contains("ST_GeomFromHEXEWKB('"));
        assertTrue(sql.endsWith("))"));
    }

    @Test
    public void testFunctionCapabilitiesRemainDisabledByDefault() throws Exception {
        DuckDBFilterToSQL encoder = new DuckDBFilterToSQL();
        PropertyIsLike like =
                ff.like(ff.function("strToLowerCase", ff.property("name")), "a_literal", "%", "-", "\\", true);

        assertFalse(encoder.getCapabilities().supports(FilterFunction_strToLowerCase.class));
        assertTrue(encoder.getCapabilities().fullySupports(like));
        assertTrue(encode(like).contains("strToLowerCase"));
    }

    @Test
    public void testEncodingStrEndsWithEscapesLiteralQuotes() throws Exception {
        Filter filter =
                ff.equals(ff.literal(true), ff.function("strEndsWith", ff.property("name"), ff.literal("'FOO")));

        String sql = encode(filter);
        assertTrue(sql.contains("strEndsWith("));
        assertTrue(sql.contains("''FOO"));
    }

    @Test
    public void testEncodingStrStartsWithEscapesLiteralQuotes() throws Exception {
        Filter filter =
                ff.equals(ff.literal(true), ff.function("strStartsWith", ff.property("name"), ff.literal("'FOO")));

        String sql = encode(filter);
        assertTrue(sql.contains("strStartsWith("));
        assertTrue(sql.contains("''FOO"));
    }

    private String encode(Filter filter) throws Exception {
        return new DuckDBFilterToSQL().encodeToString(filter);
    }
}
