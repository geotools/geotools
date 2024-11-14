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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_42">table 15 from
 * section A.10.3 Conformance test 42.</a>
 */
public class ConformanceTest42OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest42OnlineTest {

    private String criteria;

    public ConformanceTest42OnlineTest(String criteria, int expectedFeatures) throws CQLException {
        super(criteria, expectedFeatures);
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
                        "{\"op\":\"t_after\",\"args\":[{\"property\":\"date\"},{\"date\":\"2022-04-16T00:00:00GMT+00:00\"}]}",
                        1
                    },
                    {
                        "{\"op\":\"t_before\",\"args\":[{\"property\":\"date\"},{\"date\":\"2022-04-16T00:00:00GMT+00:00\"}]}",
                        1
                    },
                    // {"{}", 2}, // TODO broken text filter, cannot be converted to json, see
                    // comment in ConformanceUtils class
                    {
                        "{\"op\":\"t_equals\",\"args\":[{\"property\":\"date\"},{\"date\":\"2022-04-16T00:00:00GMT+00:00\"}]}",
                        1
                    },
                    /* {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    {"{}", 2}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    {"{}", 0}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                    */
                    {
                        "{\"op\":\"t_after\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}",
                        1
                    },
                    {
                        "{\"op\":\"t_before\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}",
                        1
                    },
                    // {"{}", 2}, // TODO broken text filter, cannot be converted to json, see
                    // comment in ConformanceUtils class
                    {
                        "{\"op\":\"t_equals\",\"args\":[{\"property\":\"start\"},{\"date\":\"2022-04-16T10:13:19GMT+00:00\"}]}",
                        1
                    }, /*
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 0}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 0}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 2}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 2}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 2}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 0}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 2}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 1}, // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       {"{}", 0} // TODO broken text filter, cannot be converted to json, see comment in ConformanceUtils class
                       */
                });
    }

    @Test
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
