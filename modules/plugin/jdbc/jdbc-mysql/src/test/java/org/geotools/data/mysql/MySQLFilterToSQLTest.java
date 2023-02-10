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

import org.geotools.data.jdbc.SQLFilterTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;

public class MySQLFilterToSQLTest extends SQLFilterTestSupport {

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

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
}
