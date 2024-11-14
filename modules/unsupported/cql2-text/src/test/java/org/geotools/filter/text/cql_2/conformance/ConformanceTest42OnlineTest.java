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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_42">table 15 from
 * section A.10.3 Conformance test 42.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest42OnlineTest extends ATSOnlineTest {

    public ConformanceTest42OnlineTest(String criteria, int expectedFeatures) throws CQLException {
        super("ne_110m_populated_places_simple", criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"t_after(\"date\",date('2022-04-16'))", 1},
                    {"t_before(\"date\",date('2022-04-16'))", 1},
                    // TODO not implemented {"t_disjoint(\"date\",date('2022-04-16'))", 2},
                    {"t_equals(\"date\",date('2022-04-16'))", 1},
                    // TODO not implemented {"t_intersects(\"date\",date('2022-04-16'))", 1},
                    // TODO not implemented
                    // {"t_after(\"date\",interval('2022-01-01','2022-12-31'))", 1},
                    // TODO not implemented
                    // {"t_before(\"date\",interval('2022-01-01','2022-12-31'))", 1},
                    // TODO not implemented
                    // TODO not implemented
                    // {"t_disjoint(\"date\",interval('2022-01-01','2022-12-31'))", 2},
                    // TODO not implemented
                    // {"t_equals(\"date\",interval('2022-01-01','2022-12-31'))", 0},
                    // TODO not implemented
                    // {"t_equals(\"date\",interval('2022-04-16','2022-04-16'))", 1},
                    // TODO not implemented
                    // {"t_intersects(\"date\",interval('2022-01-01','2022-12-31'))", 1},
                    {"t_after(start,timestamp('2022-04-16T10:13:19Z'))", 1},
                    {"t_before(start,timestamp('2022-04-16T10:13:19Z'))", 1},
                    // TODO not implemented {"t_disjoint(start,timestamp('2022-04-16T10:13:19Z'))",
                    // 2},
                    {"t_equals(start,timestamp('2022-04-16T10:13:19Z'))", 1},
                    // TODO not implemented
                    // {"t_intersects(start,timestamp('2022-04-16T10:13:19Z'))", 1},
                    // TODO not implemented
                    // {"t_after(start,interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'))",
                    // 0},
                    // TODO not implemented
                    // {"t_before(start,interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'))",
                    // 1},
                    /* TODO not implemented
                        {
                        "t_disjoint(start,interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'))",
                        1
                    }, */
                    // TODO not implemented
                    // {"t_equals(start,interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'))",
                    // 0},
                    /* TODO not implemented
                    {
                        "t_intersects(start,interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'))",
                        2
                    },
                    {"t_after(interval(start,end),interval('..','2022-04-16T10:13:19Z'))", 1},
                    {"t_before(interval(start,end),interval('2023-01-01T00:00:00Z','..'))", 2},
                    {
                        "t_disjoint(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:09Z'))",
                        1
                    },
                    {
                        "t_equals(interval(start,end),interval('2021-04-16T10:15:59Z','2022-04-16T10:16:06Z'))",
                        1
                    },
                    {
                        "t_intersects(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:09Z'))",
                        2
                    },
                    {
                        "T_CONTAINS(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'))",
                        1
                    },
                    {
                        "T_DURING(interval(start,end),interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'))",
                        1
                    },
                    {
                        "T_FINISHES(interval(start,end),interval('2020-04-16T10:13:19Z','2022-04-16T10:16:06Z'))",
                        1
                    },
                    {
                        "T_FINISHEDBY(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:16:06Z'))",
                        1
                    },
                    {
                        "T_MEETS(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'))",
                        0
                    },
                    {
                        "T_METBY(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'))",
                        1
                    },
                    {
                        "T_OVERLAPPEDBY(interval(start,end),interval('2020-04-16T10:13:19Z','2022-04-16T10:15:10Z'))",
                        2
                    },
                    {
                        "T_OVERLAPS(interval(start,end),interval('2022-04-16T10:13:19Z','2023-04-16T10:15:10Z'))",
                        1
                    },
                    {
                        "T_STARTEDBY(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'))",
                        1
                    },
                    {
                        "T_STARTS(interval(start,end),interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'))",
                        0
                    }*/
                });
    }
}
