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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_38">table 14 from section A.9.9
 * Conformance test 38.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest38OnlineTest extends ATSOnlineTest {

    public ConformanceTest38OnlineTest(String dataset, String criteria, int expectedFeatures) throws CQLException {
        super(dataset, criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
            {"ne_110m_admin_0_countries", "S_INTERSECTS(geom,POLYGON((0 40,10 40,10 50,0 50,0 40)))", 8},
            {"ne_110m_admin_0_countries", "S_INTERSECTS(geom,LINESTRING(0 40,10 50))", 4},
            {"ne_110m_populated_places_simple", "S_INTERSECTS(geom,POLYGON((0 40,10 40,10 50,0 50,0 40)))", 7},
            {"ne_110m_rivers_lake_centerlines", "S_INTERSECTS(geom,LINESTRING(-60 -90,-60 90))", 2},
            {"ne_110m_admin_0_countries", "S_DISJOINT(geom,BBOX(0,40,10,50))", 169},
            {"ne_110m_admin_0_countries", "S_DISJOINT(geom,POLYGON((0 40,10 40,10 50,0 50,0 40)))", 169},
            {"ne_110m_admin_0_countries", "S_DISJOINT(geom,LINESTRING(0 40,10 50))", 173},
            {"ne_110m_admin_0_countries", "S_DISJOINT(geom,POINT(7.02 49.92))", 176},
            {"ne_110m_populated_places_simple", "S_DISJOINT(geom,BBOX(0,40,10,50))", 236},
            {"ne_110m_populated_places_simple", "S_DISJOINT(geom,POLYGON((0 40,10 40,10 50,0 50,0 40)))", 236},
            {"ne_110m_rivers_lake_centerlines", "S_DISJOINT(geom,BBOX(-180,-90,0,90))", 9},
            {"ne_110m_rivers_lake_centerlines", "S_DISJOINT(geom,LINESTRING(-60 -90,-60 90))", 11},
            {"ne_110m_populated_places_simple", "S_EQUALS(geom,POINT(6.1300028 49.6116604))", 1},
            {
                "ne_110m_admin_0_countries",
                "S_TOUCHES(geom,POLYGON((6.043073357781111 50.128051662794235,6.242751092156993 49.90222565367873,6.186320428094177 49.463802802114515,5.897759230176348 49.44266714130711,5.674051954784829 49.529483547557504,5.782417433300907 50.09032786722122,6.043073357781111 50.128051662794235)))",
                3
            },
            {"ne_110m_admin_0_countries", "S_TOUCHES(geom,POINT(6.043073357781111 50.128051662794235))", 3},
            {"ne_110m_admin_0_countries", "S_TOUCHES(geom,POINT(6.242751092156993 49.90222565367873))", 2},
            {
                "ne_110m_admin_0_countries",
                "S_TOUCHES(geom,LINESTRING(6.043073357781111 50.128051662794235,6.242751092156993 49.90222565367873))",
                3
            },
            {"ne_110m_rivers_lake_centerlines", "S_CROSSES(geom,BBOX(0,40,10,50))", 1},
            {"ne_110m_rivers_lake_centerlines", "S_CROSSES(geom,LINESTRING(-60 -90,-60 90))", 2},
            {"ne_110m_admin_0_countries", "S_WITHIN(geom,BBOX(-180,-90,0,90))", 44},
            {"ne_110m_populated_places_simple", "S_WITHIN(geom,BBOX(-180,-90,0,90))", 74},
            {"ne_110m_rivers_lake_centerlines", "S_WITHIN(geom,BBOX(-180,-90,0,90))", 4},
            {"ne_110m_admin_0_countries", "S_CONTAINS(geom,BBOX(7,50,8,51))", 1},
            {"ne_110m_admin_0_countries", "S_CONTAINS(geom,LINESTRING(7 50,8 51))", 1},
            {"ne_110m_admin_0_countries", "S_CONTAINS(geom,POINT(7.02 49.92))", 1},
            {"ne_110m_admin_0_countries", "S_OVERLAPS(geom,BBOX(-180,-90,0,90))", 11}
        });
    }
}
