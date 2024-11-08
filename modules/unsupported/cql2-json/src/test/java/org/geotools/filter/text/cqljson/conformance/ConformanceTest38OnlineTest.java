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

import java.util.Arrays;
import java.util.Collection;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_38">table 14 from
 * section A.9.9 Conformance test 38.</a>
 */
public class ConformanceTest38OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest38OnlineTest {

    public ConformanceTest38OnlineTest(String dataset, String criteria, int expectedFeatures)
            throws CQLException {
        super(dataset, criteria, expectedFeatures);
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
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]}]}",
                        8
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[0,40],[10,50]]}]}",
                        4
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]}]}",
                        7
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[-60,-90],[-60,90]]}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]}",
                        169
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]}]}",
                        169
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[0,40],[10,50]]}]}",
                        173
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[7.02,49.92]}]}",
                        176
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]}",
                        236
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[10,40],[10,50],[0,50],[0,40]]]}]}",
                        236
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]}]}",
                        9
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_disjoint\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[-60,-90],[-60,90]]}]}",
                        11
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_equals\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[6.130003,49.61166]}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[6.043073,50.128052],[6.242751,49.902226],[6.18632,49.463803],[5.897759,49.442667],[5.674052,49.529484],[5.782417,50.090328],[6.043073,50.128052]]]}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[6.043073,50.128052]}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[6.242751,49.902226]}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[6.043073,50.128052],[6.242751,49.902226]]}]}",
                        3
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_crosses\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]}",
                        1
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_crosses\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[-60,-90],[-60,90]]}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_within\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]}]}",
                        44
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_within\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]}]}",
                        74
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_within\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]}]}",
                        4
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_contains\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[7,50],[7,51],[8,51],[8,50],[7,50]]]}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_contains\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[7,50],[8,51]]}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_contains\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[7.02,49.92]}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_overlaps\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]}]}",
                        11
                    }
                });
    }
}
