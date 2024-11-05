/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.cql_2.conformance;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.geootols.filter.text.cql_2.CQL2;
import org.geotools.api.data.DataStore;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_29">table 13 from
 * section A.8.2 Conformance test 29.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest29OnlineTest extends ATSOnlineTest {

    private final String criteria;
    private final int expectedFeatures;

    public ConformanceTest29OnlineTest(String criteria, int expectedFeatures) {
        this.criteria = criteria;
        this.expectedFeatures = expectedFeatures;
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"S_INTERSECTS(geom,LINESTRING(-180 -45, 0 -45))", 2},
                    {"S_INTERSECTS(geom,MULTILINESTRING((-180 -45, 0 -45), (0 45, 180 45)))", 14},
                    {
                        "S_INTERSECTS(geom,POLYGON((-180 -90, -90 -90, -90 90, -180 90, -180 -90), (-120 -50, -100 -50, -100 -40, -120 -40, -120 -50)))",
                        8
                    },
                    {
                        "S_INTERSECTS(geom,MULTIPOLYGON(((-180 -90, -90 -90, -90 90, -180 90, -180 -90), (-120 -50, -100 -50, -100 -40, -120 -40, -120 -50)),((0 0, 10 0, 10 10, 0 10, 0 0))))",
                        15
                    },
                    {
                        "S_INTERSECTS(geom,GEOMETRYCOLLECTION(POINT(7.02 49.92), POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))))",
                        8
                    },
                    {
                        "S_INTERSECTS(geom,POLYGON((-180 -90, -90 -90, -90 90, -180 90, -180 -90), (-120 -50, -100 -50, -100 -40, -120 -40, -120 -50))) or S_INTERSECTS(geom,POLYGON((0 0, 10 0, 10 10, 0 10, 0 0)))",
                        15
                    },
                    {
                        "S_INTERSECTS(geom,POLYGON((-180 -90, -90 -90, -90 90, -180 90, -180 -90), (-120 -50, -100 -50, -100 -40, -120 -40, -120 -50))) and not S_INTERSECTS(geom,POLYGON((-130 0, 0 0, 0 50, -130 50, -130 0)))",
                        3
                    }
                });
    }

    public @Test void testConformance() throws CQLException, IOException {
        DataStore ds = naturalEarthData();
        int feat = featuresReturned(ds);
        ds.dispose();

        assertEquals(this.expectedFeatures, feat);
    }

    private int featuresReturned(DataStore ds) throws CQLException, IOException {
        Filter filter = CQL2.toFilter(this.criteria);
        return ds.getFeatureSource("ne_110m_admin_0_countries").getFeatures(filter).size();
    }
}
