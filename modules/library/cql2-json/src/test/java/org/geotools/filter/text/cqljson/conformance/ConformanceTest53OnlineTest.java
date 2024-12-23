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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_53">table 17 from section A.14.2
 * Conformance test 53.</a>
 */
public class ConformanceTest53OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest53OnlineTest {

    private String criteria;

    public ConformanceTest53OnlineTest(String criteria, int feat) throws CQLException {
        super(criteria, feat);
        this.criteria = criteria;
    }

    @Override
    protected Filter criteriaToFilter(String criteria) throws CQLException {
        return CQL2Json.toFilter(criteria);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
            {"{\"op\":\"=\",\"args\":[{\"property\":\"pop_other\"},{\"op\":\"+\",\"args\":[1038280,8]}]}", 1},
            // {"{}", 123}, // TODO broken text filter, cannot be converted to json
            {
                "{\"op\":\">\",\"args\":[{\"property\":\"pop_other\"},{\"op\":\"-\",\"args\":[1038290,{\"op\":\"/\",\"args\":[20,10]}]}]}",
                122
            },
            // {"{}", 122}, // TODO broken text filter, cannot be converted to json
            // {"{}", 122}, // TODO broken text filter, cannot be converted to json
            {
                "{\"op\":\"<=\",\"args\":[{\"property\":\"pop_other\"},{\"op\":\"+\",\"args\":[1038200,{\"op\":\"*\",\"args\":[8,11]}]}]}",
                121
            },
            // {"{}", 120}, // TODO broken text filter, cannot be converted to json
            // {"{}", 242}, // TODO broken text filter, cannot be converted to json
            {
                "{\"op\":\"between\",\"args\":[{\"property\":\"pop_other\"},{\"op\":\"/\",\"args\":[4000000,4]},{\"op\":\"*\",\"args\":[3,{\"op\":\"+\",\"args\":[900000,100000]}]}]}",
                75
            },
            {
                "{\"op\":\"not\",\"args\":[{\"op\":\"between\",\"args\":[{\"property\":\"pop_other\"},{\"op\":\"/\",\"args\":[4000000,4]},{\"op\":\"*\",\"args\":[3,{\"op\":\"+\",\"args\":[900000,100000]}]}]}]}",
                168
            },
            {
                "{\"op\":\"in\",\"args\":[{\"property\":\"pop_other\"},[{\"op\":\"+\",\"args\":[1000000,38288]},{\"op\":\"+\",\"args\":[{\"op\":\"+\",\"args\":[1000000,600000]},11692]},{\"op\":\"+\",\"args\":[{\"op\":\"*\",\"args\":[3,1000000]},13258]},{\"op\":\"+\",\"args\":[{\"op\":\"*\",\"args\":[3,1000000]},13257]},{\"op\":\"+\",\"args\":[{\"op\":\"*\",\"args\":[30,100000]},13259]}]]}",
                3
            },
            {
                "{\"op\":\"not\",\"args\":[{\"op\":\"in\",\"args\":[{\"property\":\"pop_other\"},[{\"op\":\"+\",\"args\":[1000000,38288]},{\"op\":\"+\",\"args\":[{\"op\":\"+\",\"args\":[1000000,600000]},11692]},{\"op\":\"+\",\"args\":[{\"op\":\"*\",\"args\":[3,1000000]},13258]},{\"op\":\"+\",\"args\":[{\"op\":\"*\",\"args\":[3,1000000]},13257]},{\"op\":\"+\",\"args\":[{\"op\":\"*\",\"args\":[30,100000]},13259]}]]}]}",
                240
            },
            {"{\"op\":\"=\",\"args\":[{\"op\":\"+\",\"args\":[1038280,8]},{\"property\":\"pop_other\"}]}", 1}
        });
    }

    @Test
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
