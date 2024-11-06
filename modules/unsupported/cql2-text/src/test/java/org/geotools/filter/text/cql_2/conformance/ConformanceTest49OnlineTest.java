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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_49">table 16 from
 * section A.12.4 Conformance test 49.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest49OnlineTest extends ATSOnlineTest {

    protected final String dataset;
    protected final String criteria;
    protected final int expectedFeatures;

    public ConformanceTest49OnlineTest(String dataset, String criteria, int expectedFeatures) {
        this.dataset = dataset;
        this.criteria = criteria;
        this.expectedFeatures = expectedFeatures;
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"ne_110m_populated_places_simple", "'København'=name", 1},
                    {"ne_110m_populated_places_simple", "'København'<=name", 137},
                    {"ne_110m_populated_places_simple", "'København'<name", 136},
                    {"ne_110m_populated_places_simple", "'København'>=name", 107},
                    {"ne_110m_populated_places_simple", "'København'>name", 106},
                    {"ne_110m_populated_places_simple", "'København'<>name", 242},
                    {"ne_110m_populated_places_simple", "name=nameascii", 230},
                    {"ne_110m_populated_places_simple", "name>=nameascii", 243},
                    {"ne_110m_populated_places_simple", "name>nameascii", 13},
                    {"ne_110m_populated_places_simple", "name<=nameascii", 230},
                    {"ne_110m_populated_places_simple", "name<nameascii", 0},
                    {"ne_110m_populated_places_simple", "name<>nameascii", 13},
                    {"ne_110m_populated_places_simple", "1038288=pop_other", 1},
                    {"ne_110m_populated_places_simple", "1038288<=pop_other", 123},
                    {"ne_110m_populated_places_simple", "1038288<pop_other", 122},
                    {"ne_110m_populated_places_simple", "1038288>=pop_other", 121},
                    {"ne_110m_populated_places_simple", "1038288>pop_other", 120},
                    {"ne_110m_populated_places_simple", "1038288<>pop_other", 242},
                    {"ne_110m_populated_places_simple", "pop_min=pop_max", 27},
                    {"ne_110m_populated_places_simple", "pop_min<=pop_max", 243},
                    {"ne_110m_populated_places_simple", "pop_min<pop_max", 216},
                    {"ne_110m_populated_places_simple", "pop_min>=pop_max", 27},
                    {"ne_110m_populated_places_simple", "pop_min>pop_max", 0},
                    {"ne_110m_populated_places_simple", "pop_min<>pop_max", 216},
                    {"ne_110m_populated_places_simple", "start=end", 0},
                    {"ne_110m_populated_places_simple", "start<=end", 3},
                    {"ne_110m_populated_places_simple", "start<end", 3},
                    {"ne_110m_populated_places_simple", "start>=end", 0},
                    {"ne_110m_populated_places_simple", "start>end", 0},
                    {"ne_110m_populated_places_simple", "start<>end", 3},
                    {"ne_110m_populated_places_simple", "'København' LIKE 'K_benhavn'", 243},
                    {"ne_110m_populated_places_simple", "'København' NOT LIKE 'K_benhavn'", 0},
                    {
                        "ne_110m_populated_places_simple",
                        "pop_other between pop_min and pop_max",
                        94
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "pop_other not between pop_min and pop_max",
                        149
                    },
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(BBOX(0,40,10,50),geom)", 8},
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(BBOX(150,-90,-150,90),geom)", 10},
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(POINT(7.02 49.92),geom)", 1},
                    {"ne_110m_populated_places_simple", "S_INTERSECTS(BBOX(0,40,10,50),geom)", 7},
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "S_INTERSECTS(BBOX(-180,-90,0,90),geom)",
                        4
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_INTERSECTS(POLYGON((0 40,10 40,10 50,0 50,0 40)),geom)",
                        8
                    },
                    {"ne_110m_admin_0_countries", "S_INTERSECTS(LINESTRING(0 40,10 50),geom)", 4},
                    {
                        "ne_110m_populated_places_simple",
                        "S_INTERSECTS(POLYGON((0 40,10 40,10 50,0 50,0 40)),geom)",
                        7
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "S_INTERSECTS(LINESTRING(-60 -90,-60 90),geom)",
                        2
                    },
                    {"ne_110m_admin_0_countries", "S_DISJOINT(BBOX(0,40,10,50),geom)", 169},
                    {
                        "ne_110m_admin_0_countries",
                        "S_DISJOINT(POLYGON((0 40,10 40,10 50,0 50,0 40)),geom)",
                        169
                    },
                    {"ne_110m_admin_0_countries", "S_DISJOINT(LINESTRING(0 40,10 50),geom)", 173},
                    {"ne_110m_admin_0_countries", "S_DISJOINT(POINT(7.02 49.92),geom)", 176},
                    {"ne_110m_populated_places_simple", "S_DISJOINT(BBOX(0,40,10,50),geom)", 236},
                    {
                        "ne_110m_populated_places_simple",
                        "S_DISJOINT(POLYGON((0 40,10 40,10 50,0 50,0 40)),geom)",
                        236
                    },
                    {"ne_110m_rivers_lake_centerlines", "S_DISJOINT(BBOX(-180,-90,0,90),geom)", 9},
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "S_DISJOINT(LINESTRING(-60 -90,-60 90),geom)",
                        11
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "S_EQUALS(POINT(6.1300028 49.6116604),geom)",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_TOUCHES(POLYGON((6.043073357781111 50.128051662794235,6.242751092156993 49.90222565367873,6.186320428094177 49.463802802114515,5.897759230176348 49.44266714130711,5.674051954784829 49.529483547557504,5.782417433300907 50.09032786722122,6.043073357781111 50.128051662794235)),geom)",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_TOUCHES(POINT(6.043073357781111 50.128051662794235),geom)",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_TOUCHES(POINT(6.242751092156993 49.90222565367873),geom)",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "S_TOUCHES(LINESTRING(6.043073357781111 50.128051662794235,6.242751092156993 49.90222565367873),geom)",
                        3
                    },
                    {"ne_110m_rivers_lake_centerlines", "S_CROSSES(BBOX(0,40,10,50),geom)", 1},
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "S_CROSSES(LINESTRING(-60 -90,-60 90),geom)",
                        2
                    },
                    {"ne_110m_admin_0_countries", "S_CONTAINS(BBOX(-180,-90,0,90),geom)", 44},
                    {"ne_110m_populated_places_simple", "S_CONTAINS(BBOX(-180,-90,0,90),geom)", 74},
                    {"ne_110m_rivers_lake_centerlines", "S_CONTAINS(BBOX(-180,-90,0,90),geom)", 4},
                    {"ne_110m_admin_0_countries", "S_WITHIN(BBOX(7,50,8,51),geom)", 1},
                    {"ne_110m_admin_0_countries", "S_WITHIN(LINESTRING(7 50,8 51),geom)", 1},
                    {"ne_110m_admin_0_countries", "S_WITHIN(POINT(7.02 49.92),geom)", 1},
                    {"ne_110m_admin_0_countries", "S_OVERLAPS(BBOX(-180,-90,0,90),geom)", 11},
                    {"ne_110m_populated_places_simple", "t_after(date('2022-04-16'),\"date\")", 1},
                    {"ne_110m_populated_places_simple", "t_before(date('2022-04-16'),\"date\")", 1},
                    {
                        "ne_110m_populated_places_simple",
                        "t_disjoint(date('2022-04-16'),\"date\")",
                        2
                    },
                    {"ne_110m_populated_places_simple", "t_equals(date('2022-04-16'),\"date\")", 1},
                    {
                        "ne_110m_populated_places_simple",
                        "t_intersects(date('2022-04-16'),\"date\")",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_after(interval('2022-01-01','2022-12-31'),\"date\")",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_before(interval('2022-01-01','2022-12-31'),\"date\")",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_disjoint(interval('2022-01-01','2022-12-31'),\"date\")",
                        2
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_equals(interval('2022-01-01','2022-12-31'),\"date\")",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_equals(interval('2022-04-16','2022-04-16'),\"date\")",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_intersects(interval('2022-01-01','2022-12-31'),\"date\")",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_after(timestamp('2022-04-16T10:13:19Z'),start)",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_before(timestamp('2022-04-16T10:13:19Z'),start)",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_disjoint(timestamp('2022-04-16T10:13:19Z'),start)",
                        2
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_equals(timestamp('2022-04-16T10:13:19Z'),start)",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_intersects(timestamp('2022-04-16T10:13:19Z'),start)",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_after(interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'),start)",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_before(interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'),start)",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_disjoint(interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'),start)",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_equals(interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'),start)",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_intersects(interval('2022-01-01T00:00:00Z','2022-12-31T23:59:59Z'),start)",
                        2
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_after(interval('2023-01-01T00:00:00Z','..'),interval(start,end))",
                        2
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_before(interval('..','2022-04-16T10:13:19Z'),interval(start,end))",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_disjoint(interval('2022-04-16T10:13:19Z','2022-04-16T10:15:09Z'),interval(start,end))",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_equals(interval('2021-04-16T10:15:59Z','2022-04-16T10:16:06Z'),interval(start,end))",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "t_intersects(interval('2022-04-16T10:13:19Z','2022-04-16T10:15:09Z'),interval(start,end))",
                        2
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_CONTAINS(interval('2021-04-16T10:13:19Z','2023-04-16T10:15:10Z'),interval(start,end))",
                        2
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_DURING(interval('2022-07-01T00:00:00Z','2022-12-31T23:59:59Z'),interval(start,end))",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_FINISHES(interval('2022-04-16T10:13:19Z','2022-04-16T10:16:06Z'),interval(start,end))",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_FINISHEDBY(interval('2022-04-16T10:13:19Z','2022-04-16T10:16:06Z'),interval(start,end))",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_MEETS(interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'),interval(start,end))",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_METBY(interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'),interval(start,end))",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_OVERLAPPEDBY(interval('2020-04-16T10:13:19Z','2022-04-16T10:15:10Z'),interval(start,end))",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_OVERLAPS(interval('2022-04-16T10:13:19Z','2023-04-16T10:15:10Z'),interval(start,end))",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_STARTEDBY(interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'),interval(start,end))",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "T_STARTS(interval('2022-04-16T10:13:19Z','2022-04-16T10:15:10Z'),interval(start,end))",
                        1
                    }
                });
    }

    public @Test void testConformance() throws CQLException, IOException {
        DataStore ds = naturalEarthData();
        int feat = featuresReturned(ds);
        ds.dispose();

        assertEquals(this.expectedFeatures, feat);
    }

    protected int featuresReturned(DataStore ds) throws CQLException, IOException {
        Filter filter = CQL2.toFilter(this.criteria);
        return ds.getFeatureSource(this.dataset).getFeatures(filter).size();
    }
}
