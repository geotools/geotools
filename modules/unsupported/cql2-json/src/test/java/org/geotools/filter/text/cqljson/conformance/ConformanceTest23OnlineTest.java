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

import java.util.Arrays;
import java.util.Collection;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.cql_2.conformance.ATSOnlineTest;
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_23">table 11 from
 * section A.6.5 Conformance test 23.</a>
 */
@RunWith(Parameterized.class)
@Ignore("Not implemented yet")
public class ConformanceTest23OnlineTest extends ATSOnlineTest {

    public ConformanceTest23OnlineTest(String criteria, int expectedFeatures) throws CQLException {
        super("ne_110m_populated_places_simple", criteria, expectedFeatures);
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
                    {"{}", 1},
                    {"{}", 2},
                    {"{}", 2},
                    {"{}", 2},
                    {"{}", 4}
                });
    }
}
