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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_13">table 9 from
 * section A.4.4 Conformance test 13.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest13OnlineTest extends ATSOnlineTest {

    private final String criteria;
    private final int expectedFeatures;

    public ConformanceTest13OnlineTest(String criteria, int expectedFeatures) {
        this.criteria = criteria;
        this.expectedFeatures = expectedFeatures;
    }

    @Parameterized.Parameters(name = "{index} {0}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"name LIKE 'B_r%'", 3},
                    {"name NOT LIKE 'B_r%'", 240},
                    {"pop_other between 1000000 and 3000000", 75},
                    {"pop_other not between 1000000 and 3000000", 168},
                    {"name IN ('Kiev','kobenhavn','Berlin','athens','foo')", 2},
                    {"name NOT IN ('Kiev','kobenhavn','Berlin','athens','foo')", 241},
                    {"pop_other in (1038288,1611692,3013258,3013257,3013259)", 3},
                    {"pop_other not in (1038288,1611692,3013258,3013257,3013259)", 240},
                    {"\"date\" in (DATE('2021-04-16'),DATE('2022-04-16'),DATE('2022-04-18'))", 2},
                    {
                        "\"date\" not in (DATE('2021-04-16'),DATE('2022-04-16'),DATE('2022-04-18'))",
                        1
                    },
                    {"start in (TIMESTAMP('2022-04-16T10:13:19Z'))", 1},
                    {"start not in (TIMESTAMP('2022-04-16T10:13:19Z'))", 2},
                    {"boolean in (true)", 2},
                    {"boolean not in (false)", 2}
                });
    }

    public @Test void testConformance() throws IOException, CQLException {
        DataStore ds = naturalEarthData();
        int feat = featuresReturned(ds);
        ds.dispose();

        assertEquals(this.expectedFeatures, feat);
    }

    private int featuresReturned(DataStore ds) throws CQLException, IOException {
        Filter filter = CQL2.toFilter(this.criteria);

        return ds.getFeatureSource("ne_110m_populated_places_simple").getFeatures(filter).size();
    }
}
