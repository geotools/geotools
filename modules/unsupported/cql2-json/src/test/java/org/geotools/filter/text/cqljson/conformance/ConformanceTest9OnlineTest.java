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
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_9">table 8 from
 * section A.3.6 Conformance test 9.</a>
 */
public class ConformanceTest9OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest9OnlineTest {

    private String criteria;

    public ConformanceTest9OnlineTest(String jsonCriteria, int expectedFeatures)
            throws CQLException {
        super(CQL2Json.toFilter(jsonCriteria), expectedFeatures);
        this.criteria = jsonCriteria;
    }

    @Parameterized.Parameters(name = "{index}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]}]}]}",
                        107
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]},{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        124
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        121
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]}]}",
                        242
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        122
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        120
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        137
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        138
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        62
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        122
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]}",
                        122
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        107
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        122
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]},{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        243
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}]}",
                        241
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]}]}",
                        124
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]}]}",
                        137
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]}]}",
                        198
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        107
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        181
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]}]}",
                        121
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        79
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]}]}]}",
                        240
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        199
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]}",
                        106
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        121
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        137
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]},{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]}]}]}",
                        184
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}",
                        241
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        241
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        3
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        1
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]}]},{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"pop_other\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]}]}",
                        241
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]},{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"name\"},\"København\"]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"name\"}]}]}]}]}]}",
                        2
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"start\"}]}]}]},{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]}]}]}]}]}",
                        242
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]},{\"op\":\"and\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]},{\"op\":\"not\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}]}",
                        122
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"and\",\"args\":[{\"op\":\"isNull\",\"args\":[{\"property\":\"boolean\"}]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\">=\",\"args\":[{\"property\":\"pop_other\"},1038288]},{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},1038288]}]}]}]}",
                        121
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"and\",\"args\":[{\"op\":\"not\",\"args\":[{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},1038288]}]},{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]}]},{\"op\":\"and\",\"args\":[{\"op\":\"=\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]}]},{\"op\":\"not\",\"args\":[{\"op\":\"or\",\"args\":[{\"op\":\"<\",\"args\":[{\"property\":\"name\"},\"København\"]},{\"op\":\"=\",\"args\":[{\"property\":\"boolean\"},true]}]}]}]}",
                        44
                    }
                });
    }

    @Test
    @Ignore("too memory-intensive")
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
