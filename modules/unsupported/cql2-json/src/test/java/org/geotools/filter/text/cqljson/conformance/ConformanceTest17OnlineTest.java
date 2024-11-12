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
import org.geotools.filter.text.cql_2.conformance.ATSOnlineTest;
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_17">table 10 from
 * section A.5.3 Conformance test 17.</a>
 */
@RunWith(Parameterized.class)
@Ignore("Not implemented yet")
public class ConformanceTest17OnlineTest extends ATSOnlineTest {

    private String criteria;

    public ConformanceTest17OnlineTest(String criteria, int expectedFeatures) throws CQLException {
        super("ne_110m_populated_places_simple", criteria, expectedFeatures);
        this.criteria = criteria;
    }

    @Override
    protected Filter criteriaToFilter(String criteria) throws CQLException {
        return CQL2Json.toFilter(criteria);
    }

    @Parameterized.Parameters(name = "{index} {0}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {"{}", 1},
                    {"{}", 1},
                    {"{}", 1},
                    {"{}", 1},
                    {"{}", 1},
                    {"{}", 1},
                    {"{}", 3},
                    {"{}", 3},
                    {"{}", 3},
                    {"{}", 3}
                });
    }

    @Test
    public void testJsonSchemaValidation() {
        assertNull(ConformanceUtils.jsonSchemaValidate(this.criteria));
    }
}
