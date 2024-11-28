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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_38">table 14 from
 * section A.9.9 Conformance test 38.</a>
 */
public class ConformanceTest38OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest38OnlineTest {

    private String criteria;

    public ConformanceTest38OnlineTest(String dataset, String criteria, int expectedFeatures)
            throws CQLException {
        super(dataset, criteria, expectedFeatures);
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
                        "{\"op\":\"s_equals\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[6.1300028, 49.6116604]}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[6.043073357781111, 50.128051662794235], [6.242751092156993, 49.90222565367873], [6.186320428094177, 49.463802802114515], [5.897759230176348, 49.44266714130711], [5.674051954784829, 49.529483547557504], [5.782417433300907, 50.09032786722122], [6.043073357781111, 50.128051662794235]]]}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[6.043073357781111, 50.128051662794235]}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[6.242751092156993, 49.90222565367873]}]}",
                        2
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_touches\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[6.043073357781111, 50.128051662794235], [6.242751092156993, 49.90222565367873]]}]}",
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

    @Test
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
