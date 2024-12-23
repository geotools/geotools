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
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_23">table 11 from section A.6.5
 * Conformance test 23.</a>
 */
@RunWith(Parameterized.class)
@Ignore("Not implemented yet")
public class ConformanceTest23OnlineTest extends ATSOnlineTest {

    public ConformanceTest23OnlineTest(String criteria, int expectedFeatures) throws CQLException {
        super("ne_110m_populated_places_simple", criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0}")
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][] {
            {"ACCENTI(name)=accenti('Chișinău')", 1},
            {"ACCENTI(name)=accenti('Chisinau')", 1},
            {"ACCENTI(name)=accenti('Kiev')", 1},
            {"ACCENTI(CASEI(name))=accenti(casei('chișinău'))", 1},
            {"ACCENTI(CASEI(name))=accenti(casei('chisinau'))", 1},
            {"ACCENTI(CASEI(name))=accenti(casei('CHISINAU'))", 1},
            {"ACCENTI(CASEI(name))=accenti(casei('CHIȘINĂU'))", 1},
            {"ACCENTI(name) LIKE accenti('Ch%')", 2},
            {"ACCENTI(CASEI(name)) LIKE accenti(casei('Chiș%'))", 2},
            {"ACCENTI(CASEI(name)) LIKE accenti(casei('cHis%'))", 2},
            {
                "ACCENTI(CASEI(name)) IN (accenti(casei('Kiev')), accenti(casei('chișinău')), accenti(casei('Berlin')), accenti(casei('athens')), accenti(casei('foo')))",
                4
            }
        });
    }
}
