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
package org.geotools.geopkg;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import org.geotools.data.jdbc.SQLFilterTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsLike;

public class GeoPkgFilterToSQLTest extends SQLFilterTestSupport {

    private static FilterFactory2 ff;

    private GeoPkgDialect dialect;

    GeoPkgFilterToSQL filterToSql;

    StringWriter writer;

    @Override
    @Before
    public void setUp() throws IllegalAttributeException, SchemaException {
        ff = CommonFactoryFinder.getFilterFactory2();
        dialect = new GeoPkgDialect(null);
        filterToSql = new GeoPkgFilterToSQL(dialect);
        writer = new StringWriter();
        filterToSql.setWriter(writer);

        prepareFeatures();
    }

    @Test
    public void testFunctionLike() throws Exception {
        filterToSql.setFeatureType(testSchema);
        PropertyIsLike like =
                ff.like(
                        ff.function("strToLowerCase", ff.property("testString")),
                        "a_literal",
                        "%",
                        "-",
                        "\\",
                        true);

        filterToSql.encode(like);
        String sql = writer.toString().toLowerCase().trim();
        assertEquals("where strtolowercase(teststring) like 'a_literal'", sql);
    }
}
