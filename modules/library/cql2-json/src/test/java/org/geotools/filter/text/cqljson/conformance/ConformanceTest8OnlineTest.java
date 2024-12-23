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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_8">table 7 from section A.3.5
 * Conformance test 8.</a>
 */
public class ConformanceTest8OnlineTest extends org.geotools.filter.text.cql_2.conformance.ConformanceTest8OnlineTest {

    private String criteria;

    public ConformanceTest8OnlineTest(String dataset, String criteria, int expectedFeatures) throws CQLException {
        super(dataset, criteria, expectedFeatures);
        this.criteria = criteria;
    }

    @Override
    protected Filter criteriaToFilter(String criteria) throws CQLException {
        return CQL2Json.toFilter(criteria);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
            {"ne_110m_admin_0_countries", "{\"op\":\"=\",\"args\":[{\"property\":\"NAME\"},\"Luxembourg\"]}", 1},
            {"ne_110m_admin_0_countries", "{\"op\":\">=\",\"args\":[{\"property\":\"NAME\"},\"Luxembourg\"]}", 84},
            {"ne_110m_admin_0_countries", "{\"op\":\">\",\"args\":[{\"property\":\"NAME\"},\"Luxembourg\"]}", 83},
            {"ne_110m_admin_0_countries", "{\"op\":\"<=\",\"args\":[{\"property\":\"NAME\"},\"Luxembourg\"]}", 94},
            {"ne_110m_admin_0_countries", "{\"op\":\"<\",\"args\":[{\"property\":\"NAME\"},\"Luxembourg\"]}", 93},
            {
                "ne_110m_admin_0_countries",
                "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"NAME\"},\"Luxembourg\"]}]}",
                176
            },
            {"ne_110m_admin_0_countries", "{\"op\":\"=\",\"args\":[{\"property\":\"POP_EST\"},37589262]}", 1},
            {"ne_110m_admin_0_countries", "{\"op\":\">=\",\"args\":[{\"property\":\"POP_EST\"},37589262]}", 39},
            {"ne_110m_admin_0_countries", "{\"op\":\">\",\"args\":[{\"property\":\"POP_EST\"},37589262]}", 38},
            {"ne_110m_admin_0_countries", "{\"op\":\"<=\",\"args\":[{\"property\":\"POP_EST\"},37589262]}", 139},
            {"ne_110m_admin_0_countries", "{\"op\":\"<\",\"args\":[{\"property\":\"POP_EST\"},37589262]}", 138},
            {
                "ne_110m_admin_0_countries",
                "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"POP_EST\"},37589262]}]}",
                176
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}",
                243
            },
            {"ne_110m_populated_places_simple", "{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}", 0},
            {"ne_110m_populated_places_simple", "{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}", 1},
            {"ne_110m_populated_places_simple", "{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}", 137
            },
            {"ne_110m_populated_places_simple", "{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}", 136},
            {"ne_110m_populated_places_simple", "{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}", 107
            },
            {"ne_110m_populated_places_simple", "{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}", 106},
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}",
                242
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}",
                243
            },
            {"ne_110m_populated_places_simple", "{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}", 0},
            {"ne_110m_populated_places_simple", "{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}", 1},
            {"ne_110m_populated_places_simple", "{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]}", 123},
            {"ne_110m_populated_places_simple", "{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}", 122},
            {"ne_110m_populated_places_simple", "{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}", 121},
            {"ne_110m_populated_places_simple", "{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}", 120},
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}",
                242
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"date\"}]}]}",
                3
            },
            {"ne_110m_populated_places_simple", "{\"op\":\"isNull\",\"args\":[{\"property\":\"date\"}]}", 240},
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"=\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}",
                1
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\">=\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}",
                2
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\">\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}",
                1
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"<=\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}",
                2
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"<\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}",
                1
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"date\"},{\"timestamp\":\"2022-04-16T00:00:00Z\"}]}]}",
                2
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}",
                3
            },
            {"ne_110m_populated_places_simple", "{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}", 240},
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"timestamp\":\"2022-04-16T10:13:19Z\"}]}",
                1
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"timestamp\":\"2022-04-16T10:13:19Z\"}]}",
                2
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"timestamp\":\"2022-04-16T10:13:19Z\"}]}",
                1
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"timestamp\":\"2022-04-16T10:13:19Z\"}]}",
                2
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"timestamp\":\"2022-04-16T10:13:19Z\"}]}",
                1
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"timestamp\":\"2022-04-16T10:13:19Z\"}]}]}",
                2
            },
            {
                "ne_110m_populated_places_simple",
                "{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}",
                3
            },
            {"ne_110m_populated_places_simple", "{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}", 240},
            {"ne_110m_populated_places_simple", "{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}", 2},
            {"ne_110m_populated_places_simple", "{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},false]}", 1}
        });
    }

    @Test
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
