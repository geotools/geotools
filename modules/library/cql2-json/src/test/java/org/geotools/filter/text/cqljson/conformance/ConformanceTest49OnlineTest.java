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

package org.geotools.filter.text.cqljson.conformance;

import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.Test;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_49">table 16 from
 * section A.12.4 Conformance test 49.</a>
 */
public class ConformanceTest49OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest49OnlineTest {

    private String criteria;

    public ConformanceTest49OnlineTest(String dataset, String criteria, int feat)
            throws CQLException {
        super(dataset, criteria, feat);
        this.criteria = criteria;
    }

    @Override
    protected Filter criteriaToFilter(String criteria) throws CQLException {
        return CQL2Json.toFilter(criteria);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"=\",\"args\":[\"København\",{\"property\":\"name\"}]}",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<=\",\"args\":[\"København\",{\"property\":\"name\"}]}",
                        137
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<\",\"args\":[\"København\",{\"property\":\"name\"}]}",
                        136
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">=\",\"args\":[\"København\",{\"property\":\"name\"}]}",
                        107
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">\",\"args\":[\"København\",{\"property\":\"name\"}]}",
                        106
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[\"København\",{\"property\":\"name\"}]}]}",
                        242
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"=\",\"args\":[{\"property\":\"name\"},{\"property\":\"nameascii\"}]}",
                        230
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">=\",\"args\":[{\"property\":\"name\"},{\"property\":\"nameascii\"}]}",
                        243
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">\",\"args\":[{\"property\":\"name\"},{\"property\":\"nameascii\"}]}",
                        13
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},{\"property\":\"nameascii\"}]}",
                        230
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<\",\"args\":[{\"property\":\"name\"},{\"property\":\"nameascii\"}]}",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},{\"property\":\"nameascii\"}]}]}",
                        13
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"=\",\"args\":[1038288,{\"property\":\"pop_other\"}]}",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<=\",\"args\":[1038288,{\"property\":\"pop_other\"}]}",
                        123
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<\",\"args\":[1038288,{\"property\":\"pop_other\"}]}",
                        122
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">=\",\"args\":[1038288,{\"property\":\"pop_other\"}]}",
                        121
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">\",\"args\":[1038288,{\"property\":\"pop_other\"}]}",
                        120
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[1038288,{\"property\":\"pop_other\"}]}]}",
                        242
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"=\",\"args\":[{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}",
                        27
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<=\",\"args\":[{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}",
                        243
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<\",\"args\":[{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}",
                        216
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">=\",\"args\":[{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}",
                        27
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">\",\"args\":[{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}]}",
                        216
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"property\":\"end\"}]}",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"property\":\"end\"}]}",
                        3
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"property\":\"end\"}]}",
                        3
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"property\":\"end\"}]}",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"property\":\"end\"}]}",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"property\":\"end\"}]}]}",
                        3
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"like\",\"args\":[\"København\",\"K_benhavn\"]}",
                        243
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"like\",\"args\":[\"København\",\"K_benhavn\"]}]}",
                        0
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"between\",\"args\":[{\"property\":\"pop_other\"},{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}",
                        94
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"not\",\"args\":[{\"op\":\"between\",\"args\":[{\"property\":\"pop_other\"},{\"property\":\"pop_min\"},{\"property\":\"pop_max\"}]}]}",
                        149
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]},{\"property\":\"geom\"}]}",
                        8
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[150,-90],[150,90],[-150,90],[-150,-90],[150,-90]]]},{\"property\":\"geom\"}]}",
                        10
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Point\",\"coordinates\":[7.02,49.92]},{\"property\":\"geom\"}]}",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]},{\"property\":\"geom\"}]}",
                        7
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]},{\"property\":\"geom\"}]}",
                        4
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]},{\"property\":\"geom\"}]}",
                        8
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[0,40],[10,50]]},{\"property\":\"geom\"}]}",
                        4
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]},{\"property\":\"geom\"}]}",
                        7
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_intersects\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[-60,-90],[-60,90]]},{\"property\":\"geom\"}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]},{\"property\":\"geom\"}]}",
                        169
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]},{\"property\":\"geom\"}]}",
                        169
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[0,40],[10,50]]},{\"property\":\"geom\"}]}",
                        173
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"Point\",\"coordinates\":[7.02,49.92]},{\"property\":\"geom\"}]}",
                        176
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]},{\"property\":\"geom\"}]}",
                        236
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]},{\"property\":\"geom\"}]}",
                        236
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]},{\"property\":\"geom\"}]}",
                        9
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[-60,-90],[-60,90]]},{\"property\":\"geom\"}]}",
                        11
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_equals\",\"args\":[{\"type\":\"Point\",\"coordinates\":[6.130003,49.61166]},{\"property\":\"geom\"}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[6.043073,50.128052],[6.242751,49.902226],[6.18632,49.463803],[5.897759,49.442667],[5.674052,49.529484],[5.782417,50.090328],[6.043073,50.128052]]]},{\"property\":\"geom\"}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"type\":\"Point\",\"coordinates\":[6.043073,50.128052]},{\"property\":\"geom\"}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"type\":\"Point\",\"coordinates\":[6.242751,49.902226]},{\"property\":\"geom\"}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[6.043073,50.128052],[6.242751,49.902226]]},{\"property\":\"geom\"}]}",
                        3
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_crosses\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]},{\"property\":\"geom\"}]}",
                        1
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_crosses\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[-60,-90],[-60,90]]},{\"property\":\"geom\"}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_contains\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]},{\"property\":\"geom\"}]}",
                        44
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_contains\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]},{\"property\":\"geom\"}]}",
                        74
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_contains\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]},{\"property\":\"geom\"}]}",
                        4
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_within\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[7,50],[7,51],[8,51],[8,50],[7,50]]]},{\"property\":\"geom\"}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_within\",\"args\":[{\"type\":\"LineString\",\"coordinates\":[[7,50],[8,51]]},{\"property\":\"geom\"}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_within\",\"args\":[{\"type\":\"Point\",\"coordinates\":[7.02,49.92]},{\"property\":\"geom\"}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_overlaps\",\"args\":[{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]},{\"property\":\"geom\"}]}",
                        11
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"t_after\",\"args\":[{\"timestamp\":\"2022-04-16T00:00:00Z\"},{\"property\":\"date\"}]}",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"t_before\",\"args\":[{\"timestamp\":\"2022-04-16T00:00:00Z\"},{\"property\":\"date\"}]}",
                        1
                    }, /*
                       {
                           "ne_110m_populated_places_simple", "{}", 2
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       */
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"t_equals\",\"args\":[{\"timestamp\":\"2022-04-16T00:00:00Z\"},{\"property\":\"date\"}]}",
                        1
                    }, /*
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 2
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       */
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"t_after\",\"args\":[{\"timestamp\":\"2022-04-16T10:13:19Z\"},{\"property\":\"start\"}]}",
                        1
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"t_before\",\"args\":[{\"timestamp\":\"2022-04-16T10:13:19Z\"},{\"property\":\"start\"}]}",
                        1
                    },
                    /*
                    {
                        "ne_110m_populated_places_simple", "{}", 2
                    }, // TODO text filter cannot be parsed nor translated into JSON
                    */
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"t_equals\",\"args\":[{\"timestamp\":\"2022-04-16T10:13:19Z\"},{\"property\":\"start\"}]}",
                        1
                    }, /*
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 2
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 2
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 2
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 2
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 0
                       }, // TODO text filter cannot be parsed nor translated into JSON
                       {
                           "ne_110m_populated_places_simple", "{}", 1
                       } // TODO text filter cannot be parsed nor translated into JSON
                       */
                });
    }

    @Test
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
