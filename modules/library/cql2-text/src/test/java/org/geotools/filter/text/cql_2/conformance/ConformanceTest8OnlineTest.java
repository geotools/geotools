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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_8">table 7 from section A.3.5
 * Conformance test 8.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest8OnlineTest extends ATSOnlineTest {

    public ConformanceTest8OnlineTest(String dataset, String criteria, int expectedFeatures) throws CQLException {
        super(dataset, criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
            {"ne_110m_admin_0_countries", "NAME='Luxembourg'", 1},
            {"ne_110m_admin_0_countries", "NAME>='Luxembourg'", 84},
            {"ne_110m_admin_0_countries", "NAME>'Luxembourg'", 83},
            {"ne_110m_admin_0_countries", "NAME<='Luxembourg'", 94},
            {"ne_110m_admin_0_countries", "NAME<'Luxembourg'", 93},
            {"ne_110m_admin_0_countries", "NAME<>'Luxembourg'", 176},
            {"ne_110m_admin_0_countries", "POP_EST=37589262", 1},
            {"ne_110m_admin_0_countries", "POP_EST>=37589262", 39},
            {"ne_110m_admin_0_countries", "POP_EST>37589262", 38},
            {"ne_110m_admin_0_countries", "POP_EST<=37589262", 139},
            {"ne_110m_admin_0_countries", "POP_EST<37589262", 138},
            {"ne_110m_admin_0_countries", "POP_EST<>37589262", 176},
            {"ne_110m_populated_places_simple", "name IS NOT NULL", 243},
            {"ne_110m_populated_places_simple", "name IS NULL", 0},
            {"ne_110m_populated_places_simple", "name='København'", 1},
            {"ne_110m_populated_places_simple", "name>='København'", 137},
            {"ne_110m_populated_places_simple", "name>'København'", 136},
            {"ne_110m_populated_places_simple", "name<='København'", 107},
            {"ne_110m_populated_places_simple", "name<'København'", 106},
            {"ne_110m_populated_places_simple", "name<>'København'", 242},
            {"ne_110m_populated_places_simple", "pop_other IS NOT NULL", 243},
            {"ne_110m_populated_places_simple", "pop_other IS NULL", 0},
            {"ne_110m_populated_places_simple", "pop_other=1038288", 1},
            {"ne_110m_populated_places_simple", "pop_other>=1038288", 123},
            {"ne_110m_populated_places_simple", "pop_other>1038288", 122},
            {"ne_110m_populated_places_simple", "pop_other<=1038288", 121},
            {"ne_110m_populated_places_simple", "pop_other<1038288", 120},
            {"ne_110m_populated_places_simple", "pop_other<>1038288", 242},
            {"ne_110m_populated_places_simple", "\"date\" IS NOT NULL", 3},
            {"ne_110m_populated_places_simple", "\"date\" IS NULL", 240},
            {"ne_110m_populated_places_simple", "\"date\"=DATE('2022-04-16')", 1},
            {"ne_110m_populated_places_simple", "\"date\">=DATE('2022-04-16')", 2},
            {"ne_110m_populated_places_simple", "\"date\">DATE('2022-04-16')", 1},
            {"ne_110m_populated_places_simple", "\"date\"<=DATE('2022-04-16')", 2},
            {"ne_110m_populated_places_simple", "\"date\"<DATE('2022-04-16')", 1},
            {"ne_110m_populated_places_simple", "\"date\"<>DATE('2022-04-16')", 2
            }, // TODO should return 2 relying on the official specs, but what about NULL
            // values ? see https://github.com/opengeospatial/ogcapi-features/issues/963
            {"ne_110m_populated_places_simple", "start IS NOT NULL", 3},
            {"ne_110m_populated_places_simple", "start IS NULL", 240},
            {"ne_110m_populated_places_simple", "start=TIMESTAMP('2022-04-16T10:13:19Z')", 1},
            {"ne_110m_populated_places_simple", "start<=TIMESTAMP('2022-04-16T10:13:19Z')", 2},
            {"ne_110m_populated_places_simple", "start<TIMESTAMP('2022-04-16T10:13:19Z')", 1},
            {"ne_110m_populated_places_simple", "start>=TIMESTAMP('2022-04-16T10:13:19Z')", 2},
            {"ne_110m_populated_places_simple", "start>TIMESTAMP('2022-04-16T10:13:19Z')", 1},
            {"ne_110m_populated_places_simple", "start<>TIMESTAMP('2022-04-16T10:13:19Z')", 2 // TODO: see TODO above
            },
            {"ne_110m_populated_places_simple", "boolean IS NOT NULL", 3},
            {"ne_110m_populated_places_simple", "boolean IS NULL", 240},
            {"ne_110m_populated_places_simple", "boolean=true", 2},
            {"ne_110m_populated_places_simple", "boolean=false", 1}
        });
    }
}
