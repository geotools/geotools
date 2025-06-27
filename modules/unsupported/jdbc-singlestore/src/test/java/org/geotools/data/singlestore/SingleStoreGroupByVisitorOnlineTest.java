/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;
import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.jdbc.JDBCGroupByVisitorOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

public class SingleStoreGroupByVisitorOnlineTest extends JDBCGroupByVisitorOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SingleStoreGroupByVisitorTestSetup();
    }

    @Override
    @Test
    @SuppressWarnings("unchecked")
    public void testGroupByGeometryFunction() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName aggProperty = ff.property(aname("intProperty"));
        Expression groupProperty = ff.function("buffer", ff.property(aname("geometry")), ff.literal(1));
        List<Object[]> value =
                genericGroupByTestTest("ft1_group_by", Query.ALL, Aggregate.SUM, aggProperty, false, groupProperty);
        assertEquals(3, value.size());
        // get them in predictable order
        value.sort(Comparator.comparing(v -> ((Geometry) v[0])));
        // geometries have been parsed, sums have the expected value
        Object[] v0 = value.get(0);
        Geometry expectedGeometry = new WKTReader().read("POINT(0 0)").buffer(1);
        assertTrue(expectedGeometry.equalsExact((Geometry) v0[0], 1e-6));
        assertEquals(3, ((Number) v0[1]).intValue());
        Object[] v1 = value.get(1);
        expectedGeometry = new WKTReader().read("POINT(1 1)").buffer(1);
        assertTrue(expectedGeometry.equalsExact((Geometry) v1[0], 1e-6));
        assertEquals(33, ((Number) v1[1]).intValue());
        Object[] v2 = value.get(2);
        expectedGeometry = new WKTReader().read("POINT(2 2)").buffer(1);
        assertTrue(expectedGeometry.equalsExact((Geometry) v2[0], 1e-6));
        assertEquals(63, ((Number) v2[1]).intValue());
    }

    @Override
    @Test
    @SuppressWarnings("unchecked")
    public void testGroupByGeometry() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName aggProperty = ff.property(aname("intProperty"));
        PropertyName groupProperty = ff.property(aname("geometry"));
        boolean expectOptimized = dataStore.getSQLDialect().canGroupOnGeometry();
        List<Object[]> value = genericGroupByTestTest(
                "ft1_group_by", Query.ALL, Aggregate.SUM, aggProperty, expectOptimized, groupProperty);
        assertEquals(3, value.size());
        // get them in predictable order
        value.sort(Comparator.comparing(v -> ((Geometry) v[0])));
        // geometries have been parsed, sums have the expected value
        Object[] v0 = value.get(0);
        Geometry expectedGeometry = new WKTReader().read("POINT(0 0)");
        assertTrue(expectedGeometry.equalsExact((Geometry) v0[0], 1e-6));
        assertEquals(3, ((Number) v0[1]).intValue());
        Object[] v1 = value.get(1);
        expectedGeometry = new WKTReader().read("POINT(1 1)");
        assertTrue(expectedGeometry.equalsExact((Geometry) v1[0], 1e-6));
        assertEquals(33, ((Number) v1[1]).intValue());
        Object[] v2 = value.get(2);
        expectedGeometry = new WKTReader().read("POINT(2 2)");
        assertTrue(expectedGeometry.equalsExact((Geometry) v2[0], 1e-6));
        assertEquals(63, ((Number) v2[1]).intValue());
    }
}
