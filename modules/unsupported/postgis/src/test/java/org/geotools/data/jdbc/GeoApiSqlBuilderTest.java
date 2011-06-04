/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.jdbc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Add;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;

public class GeoApiSqlBuilderTest extends SQLFilterTestSupport {

    GeoAPISQLBuilder builder;

    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    
    public GeoApiSqlBuilderTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        final FilterToSQL filterToSQL = new FilterToSQL();
        filterToSQL.setSqlNameEscape("\"");
        builder = new GeoAPISQLBuilder(filterToSQL, testSchema, null);
    }

    public void testExpression() throws Exception {
        Add a = ff.add(ff.property("testInteger"), ff.literal(5));
        StringBuffer sb = new StringBuffer();
        builder.encode(sb, a);
        assertEquals("\"testInteger\" + 5", sb.toString());
    }
    
    public void testFilter() throws Exception {
        PropertyIsEqualTo equal = ff.equal(ff.property("testInteger"), ff.literal(5), false);
        StringBuffer sb = new StringBuffer();
        builder.encode(sb, equal);
        assertEquals("\"testInteger\" = 5", sb.toString());
    }

    public void testGetPrePostQueryFilterWithFidValidation() throws Exception {
        final String featureTypeName = "FT.Name";
        FIDMapper mapper = new TypedFIDMapper(new BasicFIDMapper("id", 5), featureTypeName);

        final FilterToSQL filterToSQL = new FilterToSQL();
        filterToSQL.setFIDMapper(mapper);
        filterToSQL.setSqlNameEscape("\"");
        
        builder = new GeoAPISQLBuilder(filterToSQL, testSchema, null);

        Set<Identifier> ids = new HashSet<Identifier>();
        ids.add(ff.featureId("discarded.1"));
        ids.add(ff.gmlObjectId("discarded.2"));
        final FeatureId validFid = ff.featureId(featureTypeName + ".fid-something");
        ids.add(validFid);
        Filter id = ff.id(ids);

        Filter preFilter = builder.getPreQueryFilter(id);
        Filter postFilter = builder.getPostQueryFilter(id);
        
        final Filter expected = ff.id(Collections.singleton(validFid));
        assertEquals(expected, preFilter);
        assertEquals(Filter.INCLUDE, postFilter);
    }
}
