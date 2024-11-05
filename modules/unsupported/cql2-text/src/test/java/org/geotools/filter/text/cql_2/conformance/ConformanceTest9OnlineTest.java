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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_9">table 8 from
 * section A.3.6 Conformance test 9.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest9OnlineTest extends ATSOnlineTest {

    private final String p1;
    private final String p2;
    private final String p3;
    private final String p4;
    private final int expectedFeatures;

    public ConformanceTest9OnlineTest(
            String p1, String p2, String p3, String p4, int expectedFeatures) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.expectedFeatures = expectedFeatures;
    }

    @Parameterized.Parameters(name = "{index} {0} {1} {2} {3}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {
                        "pop_other<>1038288",
                        "name<>'København'",
                        "pop_other IS NULL",
                        "name<'København'",
                        1
                    },
                    {
                        "pop_other<>1038288",
                        "name>'København'",
                        "name<='København'",
                        "boolean=true",
                        107
                    },
                    {
                        "start IS NULL",
                        "pop_other IS NOT NULL",
                        "pop_other IS NOT NULL",
                        "pop_other>1038288",
                        124
                    },
                    {
                        "pop_other<1038288",
                        "pop_other>1038288",
                        "pop_other IS NULL",
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        121
                    },
                    {
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other<1038288",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<>'København'",
                        2
                    },
                    {
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<>'København'",
                        "boolean=true",
                        "name<'København'",
                        2
                    },
                    {
                        "pop_other=1038288",
                        "start IS NULL",
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean IS NOT NULL",
                        242
                    },
                    {
                        "start IS NULL",
                        "pop_other>1038288",
                        "start IS NOT NULL",
                        "name>'København'",
                        122
                    },
                    {
                        "pop_other<1038288",
                        "name<>'København'",
                        "name='København'",
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        2
                    },
                    {
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name IS NOT NULL",
                        "start IS NULL",
                        "pop_other<1038288",
                        120
                    },
                    {
                        "name>='København'",
                        "start IS NOT NULL",
                        "boolean=true",
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        137
                    },
                    {
                        "start IS NOT NULL",
                        "name>='København'",
                        "start IS NOT NULL",
                        "name IS NOT NULL",
                        3
                    },
                    {
                        "name IS NULL",
                        "name<'København'",
                        "pop_other IS NOT NULL",
                        "boolean IS NOT NULL",
                        243
                    },
                    {
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name>'København'",
                        "pop_other=1038288",
                        "name<'København'",
                        3
                    },
                    {
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<='København'",
                        "boolean IS NULL",
                        "name>'København'",
                        138
                    },
                    {
                        "pop_other IS NOT NULL",
                        "start IS NULL",
                        "pop_other>=1038288",
                        "name>'København'",
                        62
                    },
                    {
                        "name='København'",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean=true",
                        "pop_other IS NULL",
                        243
                    },
                    {
                        "name>'København'",
                        "pop_other<1038288",
                        "pop_other>1038288",
                        "name<='København'",
                        122
                    },
                    {
                        "pop_other<>1038288",
                        "name='København'",
                        "name<='København'",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        243
                    },
                    {
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other=1038288",
                        "start IS NULL",
                        3
                    },
                    {
                        "name<>'København'",
                        "boolean=true",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start IS NULL",
                        2
                    },
                    {
                        "name IS NULL",
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name IS NULL",
                        243
                    },
                    {
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name>'København'",
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name IS NOT NULL",
                        3
                    },
                    {
                        "name<>'København'",
                        "pop_other<>1038288",
                        "pop_other<1038288",
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        2
                    },
                    {
                        "boolean IS NULL",
                        "pop_other>1038288",
                        "boolean IS NOT NULL",
                        "pop_other IS NULL",
                        122
                    },
                    {
                        "pop_other=1038288",
                        "start IS NULL",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other IS NOT NULL",
                        2
                    },
                    {"pop_other<>1038288", "start IS NULL", "pop_other>1038288", "boolean=true", 2},
                    {
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other<1038288",
                        "name<='København'",
                        "pop_other=1038288",
                        2
                    },
                    {
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<='København'",
                        "name<>'København'",
                        107
                    },
                    {"boolean=true", "name IS NOT NULL", "boolean IS NULL", "pop_other=1038288", 1},
                    {
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other=1038288",
                        "pop_other<1038288",
                        "name<>'København'",
                        122
                    },
                    {
                        "pop_other<>1038288",
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start IS NOT NULL",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        3
                    },
                    {
                        "name<>'København'",
                        "pop_other<>1038288",
                        "pop_other IS NOT NULL",
                        "name IS NOT NULL",
                        243
                    },
                    {
                        "name='København'",
                        "pop_other<1038288",
                        "start IS NOT NULL",
                        "pop_other<>1038288",
                        3
                    },
                    {
                        "name<'København'",
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        2
                    },
                    {
                        "boolean=true",
                        "pop_other<1038288",
                        "name IS NOT NULL",
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        3
                    },
                    {
                        "pop_other<=1038288",
                        "name<'København'",
                        "pop_other<1038288",
                        "pop_other<1038288",
                        243
                    },
                    {
                        "pop_other IS NULL",
                        "name<='København'",
                        "name='København'",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        2
                    },
                    {
                        "pop_other<1038288",
                        "name<>'København'",
                        "pop_other<>1038288",
                        "name<>'København'",
                        243
                    },
                    {
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other IS NULL",
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name IS NOT NULL",
                        2
                    },
                    {
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name='København'",
                        "boolean IS NULL",
                        "pop_other<>1038288",
                        241
                    },
                    {
                        "boolean=true",
                        "pop_other<=1038288",
                        "name<>'København'",
                        "pop_other IS NULL",
                        2
                    },
                    {
                        "name IS NOT NULL",
                        "pop_other<=1038288",
                        "start IS NOT NULL",
                        "boolean IS NOT NULL",
                        124
                    },
                    {
                        "pop_other<=1038288",
                        "pop_other<1038288",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other>1038288",
                        1
                    },
                    {
                        "start IS NOT NULL",
                        "boolean IS NOT NULL",
                        "name>='København'",
                        "pop_other IS NOT NULL",
                        137
                    },
                    {
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start IS NOT NULL",
                        "pop_other>1038288",
                        "pop_other<1038288",
                        1
                    },
                    {
                        "pop_other<=1038288",
                        "name<='København'",
                        "boolean IS NULL",
                        "start IS NOT NULL",
                        198
                    },
                    {
                        "name>='København'",
                        "name>='København'",
                        "name<='København'",
                        "name>='København'",
                        107
                    },
                    {
                        "boolean=true",
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean IS NOT NULL",
                        "name<'København'",
                        2
                    },
                    {
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other IS NULL",
                        "pop_other<=1038288",
                        1
                    },
                    {
                        "pop_other<1038288",
                        "name='København'",
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<'København'",
                        181
                    },
                    {
                        "pop_other<1038288",
                        "pop_other<=1038288",
                        "pop_other IS NULL",
                        "start IS NOT NULL",
                        121
                    },
                    {
                        "name>='København'",
                        "pop_other>=1038288",
                        "boolean=true",
                        "name IS NOT NULL",
                        79
                    },
                    {
                        "boolean IS NULL",
                        "name<>'København'",
                        "boolean IS NULL",
                        "pop_other IS NOT NULL",
                        240
                    },
                    {
                        "pop_other<1038288",
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name>'København'",
                        "pop_other<=1038288",
                        199
                    },
                    {
                        "name<='København'",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<'København'",
                        "boolean IS NULL",
                        106
                    },
                    {
                        "pop_other IS NOT NULL",
                        "name<>'København'",
                        "pop_other<1038288",
                        "pop_other<=1038288",
                        121
                    },
                    {
                        "name>='København'",
                        "start IS NOT NULL",
                        "name>='København'",
                        "name IS NOT NULL",
                        137
                    },
                    {
                        "pop_other<1038288",
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name IS NULL",
                        "pop_other>=1038288",
                        1
                    },
                    {
                        "pop_other>=1038288",
                        "name>'København'",
                        "boolean IS NOT NULL",
                        "start IS NOT NULL",
                        184
                    },
                    {
                        "start IS NOT NULL",
                        "name<>'København'",
                        "name<='København'",
                        "name IS NULL",
                        241
                    },
                    {
                        "name>='København'",
                        "pop_other<>1038288",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<>'København'",
                        2
                    },
                    {
                        "boolean IS NOT NULL",
                        "pop_other<=1038288",
                        "pop_other=1038288",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        1
                    },
                    {
                        "name IS NOT NULL",
                        "start IS NOT NULL",
                        "start IS NOT NULL",
                        "name>='København'",
                        241
                    },
                    {
                        "pop_other=1038288",
                        "pop_other IS NOT NULL",
                        "start IS NOT NULL",
                        "name<>'København'",
                        2
                    },
                    {
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start IS NULL",
                        "pop_other>1038288",
                        "pop_other<=1038288",
                        1
                    },
                    {
                        "name IS NULL",
                        "start IS NOT NULL",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name IS NOT NULL",
                        1
                    },
                    {
                        "boolean IS NOT NULL",
                        "name='København'",
                        "boolean IS NOT NULL",
                        "name IS NOT NULL",
                        3
                    },
                    {
                        "pop_other<>1038288",
                        "pop_other<>1038288",
                        "pop_other=1038288",
                        "pop_other<=1038288",
                        1
                    },
                    {
                        "pop_other IS NULL",
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean IS NOT NULL",
                        241
                    },
                    {
                        "start<TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean IS NULL",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<'København'",
                        2
                    },
                    {
                        "pop_other>1038288",
                        "pop_other<>1038288",
                        "start<>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "name<>'København'",
                        2
                    },
                    {
                        "start>=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other=1038288",
                        "name IS NOT NULL",
                        2
                    },
                    {
                        "pop_other<=1038288",
                        "start IS NOT NULL",
                        "start<=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean IS NOT NULL",
                        242
                    },
                    {
                        "boolean=true",
                        "start>TIMESTAMP('2022-04-16T10:13:19Z')",
                        "pop_other<1038288",
                        "pop_other<>1038288",
                        122
                    },
                    {
                        "pop_other>=1038288",
                        "pop_other>1038288",
                        "boolean IS NULL",
                        "pop_other=1038288",
                        121
                    },
                    {
                        "name<'København'",
                        "pop_other>1038288",
                        "start=TIMESTAMP('2022-04-16T10:13:19Z')",
                        "boolean=true",
                        44
                    }
                });
    }

    public @Test void testConformance() throws IOException, CQLException {
        DataStore ds = naturalEarthData();
        int feat = featuresReturned(ds);
        ds.dispose();

        assertEquals(this.expectedFeatures, feat);
    }

    private int featuresReturned(DataStore ds) throws CQLException, IOException {
        Filter filter =
                CQL2.toFilter(
                        String.format(
                                "(NOT (%s) AND %s) OR (%s and %s) or not (%s OR %s)",
                                p2, p1, p3, p4, p1, p4));

        return ds.getFeatureSource("ne_110m_populated_places_simple").getFeatures(filter).size();
    }
}
