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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.geotools.api.data.DataStore;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_13">table 9 from
 * section A.4.4 Conformance test 13.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest13OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest13OnlineTest {

    public ConformanceTest13OnlineTest(String criteria, int expectedFeatures) {
        super(criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"{\"op\":\"like\",\"args\":[{\"property\":\"name\"},\"B_r%\"]}", 3},
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"like\",\"args\":[{\"property\":\"name\"},\"B_r%\"]}]}",
                        240
                    },
                    {
                        "{\"op\":\"between\",\"args\":[{\"property\":\"pop_other\"},1000000,3000000]}",
                        75
                    },
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"between\",\"args\":[{\"property\":\"pop_other\"},1000000,3000000]}]}",
                        168
                    },
                    {
                        "{\"op\":\"in\",\"args\":[{\"property\":\"name\"},[\"Kiev\",\"kobenhavn\",\"Berlin\",\"athens\",\"foo\"]]}",
                        2
                    },
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"in\",\"args\":[{\"property\":\"name\"},[\"Kiev\",\"kobenhavn\",\"Berlin\",\"athens\",\"foo\"]]}]}",
                        241
                    },
                    {
                        "{\"op\":\"in\",\"args\":[{\"property\":\"pop_other\"},[1038288,1611692,3013258,3013257,3013259]]}",
                        3
                    },
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"in\",\"args\":[{\"property\":\"pop_other\"},[1038288,1611692,3013258,3013257,3013259]]}]}",
                        240
                    },
                    {
                        "{\"op\":\"in\",\"args\":[{\"property\":\"date\"},[{\"date\":\"2021-04-16T00:00:00GMT+00:00\"},{\"date\":\"2022-04-16T00:00:00GMT+00:00\"},{\"date\":\"2022-04-18T00:00:00GMT+00:00\"}]]}",
                        2
                    },
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"in\",\"args\":[{\"property\":\"date\"},[{\"date\":\"2021-04-16T00:00:00GMT+00:00\"},{\"date\":\"2022-04-16T00:00:00GMT+00:00\"},{\"date\":\"2022-04-18T00:00:00GMT+00:00\"}]]}]}",
                        1
                    },
                    {
                        "{\"op\":\"in\",\"args\":[{\"property\":\"start\"},[{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]]}",
                        1
                    },
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"in\",\"args\":[{\"property\":\"start\"},[{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]]}]}",
                        2
                    },
                    {"{\"op\":\"in\",\"args\":[{\"property\":\"boolean\"},[true]]}", 2},
                    {
                        "{\"op\":\"not\",\"args\":[{\"op\":\"in\",\"args\":[{\"property\":\"boolean\"},[false]]}]}",
                        2
                    }
                });
    }

    @Override
    protected int featuresReturned(DataStore ds) throws CQLException, IOException {
        Filter filter = CQL2Json.toFilter(this.criteria);
        return ds.getFeatureSource("ne_110m_populated_places_simple").getFeatures(filter).size();
    }
}
