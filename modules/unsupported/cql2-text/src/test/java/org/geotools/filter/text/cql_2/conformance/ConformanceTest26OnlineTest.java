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

import java.util.Arrays;
import java.util.Collection;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_26">table 12 from
 * section A.7.2 Conformance test 26.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest26OnlineTest extends ATSOnlineTest {

    public ConformanceTest26OnlineTest(String dataset, String criteria, int expectedFeatures)
            throws CQLException {
        super(dataset, criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(geom,BBOX(0,40,10,50))", 8},
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(geom,BBOX(150,-90,-150,90))", 10},
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(geom,POINT(7.02 49.92))", 1},
                    {
                        "ne_110m_admin_0_countries",
                        "S_INTERSECTS(geom,BBOX(0,40,10,50)) and S_INTERSECTS(geom,BBOX(5,50,10,60))",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_INTERSECTS(geom,BBOX(0,40,10,50)) and not S_INTERSECTS(geom,BBOX(5,50,10,60))",
                        5
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_INTERSECTS(geom,BBOX(0,40,10,50)) or S_INTERSECTS(geom,BBOX(-90,40,-60,50))",
                        10
                    },
                    {"ne_110m_populated_places_simple", "S_INTERSECTS(geom,BBOX(0,40,10,50))", 7},
                    {"ne_110m_rivers_lake_centerlines", "S_INTERSECTS(geom,BBOX(-180,-90,0,90))", 4}
                });
    }
}
