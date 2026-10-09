/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.data.jdbc.SQLFilterTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.junit.Before;
import org.junit.Test;

public class MySQLFilterToSQLTest extends SQLFilterTestSupport {

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    private MySQLFilterToSQL filterToSql;

    @Override
    @Before
    public void setUp() throws SchemaException {
        filterToSql = (MySQLFilterToSQL) new MySQLDialectBasic(null).createFilterToSQL();
        filterToSql.setFeatureType(testSchema);
    }

    @Test
    public void testEncodeEqualToWithSpecialCharacters() throws Exception {
        PropertyIsEqualTo expr = ff.equals(ff.property("testString"), ff.literal("\\'FOO"));
        String actual = filterToSql.encodeToString(expr);
        assertEquals("WHERE testString = '\\\\''FOO'", actual);
    }

    @Test
    public void testBboxLiteralCarriesAxisOrderOnMySql8() throws Exception {
        // MySQL 8 defaults geographic SRS to lat/lon; the dialect must declare east/north order.
        String sql = encodeBbox(true, true);
        assertTrue(sql, sql.contains("ST_GeomFromText"));
        assertTrue(sql, sql.contains("'axis-order=long-lat'"));
    }

    @Test
    public void testBboxLiteralOmitsAxisOrderBeforeMySql8() throws Exception {
        String sql = encodeBbox(true, false);
        assertTrue(sql, sql.contains("ST_GeomFromText"));
        assertFalse(sql, sql.contains("axis-order"));
    }

    private String encodeBbox(boolean usePreciseSpatialOps, boolean mySqlVersion80OrAbove) throws Exception {
        MySQLFilterToSQL toSql = new MySQLFilterToSQL(usePreciseSpatialOps, mySqlVersion80OrAbove);
        toSql.setFeatureType(testSchema);
        BBOX bbox = ff.bbox("testGeometry", 0, 0, 1, 1, "EPSG:4326");
        return toSql.encodeToString(bbox);
    }
}
