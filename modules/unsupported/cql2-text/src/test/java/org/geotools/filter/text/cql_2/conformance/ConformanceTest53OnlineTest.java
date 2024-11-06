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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_53">table 17 from
 * section A.14.2 Conformance test 53.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest53OnlineTest extends ATSOnlineTest {

    protected final String criteria;
    protected final int expectedFeatures;

    public ConformanceTest53OnlineTest(String criteria, int expectedFeatures) {
        this.criteria = criteria;
        this.expectedFeatures = expectedFeatures;
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"pop_other=1038280+8", 1},
                    {"pop_other>=1038290-2*2^0", 123},
                    {"pop_other>1038290-20/10", 122},
                    {"pop_other>1038290-21 div 10", 122},
                    {"pop_other>1038290-5%2", 122},
                    {"pop_other<=1038200+8*11", 121},
                    {"pop_other<1038280+2^3", 120},
                    {"pop_other<>1038290-2^1", 242},
                    {"pop_other between 4000000/4 and (3*(900000+100000))", 75},
                    {"pop_other not between 4000000/4 and (3*(900000+100000))", 168},
                    {
                        "pop_other in (1000000+38288,1000000+600000+11692,3*1000000+13258,3*1000000+13257,30*100000+13259)",
                        3
                    },
                    {
                        "pop_other not in (1000000+38288,1000000+600000+11692,3*1000000+13258,3*1000000+13257,30*100000+13259)",
                        240
                    },
                    {"1038280+8=pop_other", 1}
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
        return ds.getFeatureSource("ne_110m_populated_places_simple").getFeatures(filter).size();
    }
}
