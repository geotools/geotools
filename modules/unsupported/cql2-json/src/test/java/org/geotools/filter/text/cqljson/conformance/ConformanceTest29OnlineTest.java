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
import org.geotools.filter.text.cqljson.CQL2Json;
import org.junit.runners.Parameterized;

/**
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_29">table 13 from
 * section A.8.2 Conformance test 29.</a>
 */
public class ConformanceTest29OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest29OnlineTest {

    public ConformanceTest29OnlineTest(String criteria, int expectedFeatures) throws CQLException {
        super(criteria, expectedFeatures);
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
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"LineString\",\"coordinates\":[[-180,-45],[0,-45]]}]}",
                        2
                    },
                    {
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"MultiLineString\",\"coordinates\":[[[-180,-45],[0,-45]],[[0,45],[180,45]]]}]}",
                        14
                    },
                    {
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-90,-90],[-90,90],[-180,90],[-180,-90]],[[-120,-50],[-100,-50],[-100,-40],[-120,-40],[-120,-50]]]}]}",
                        8
                    },
                    {
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"MultiPolygon\",\"coordinates\":[[[[-180,-90],[-90,-90],[-90,90],[-180,90],[-180,-90]],[[-120,-50],[-100,-50],[-100,-40],[-120,-40],[-120,-50]]],[[[0,0],[10,0],[10,10],[0,10],[0,0]]]]}]}",
                        15
                    },
                    {
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[7.02,49.92]},{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[10,0],[10,10],[0,10],[0,0]]]}]}]}",
                        8
                    },
                    {
                        "{\"op\":\"or\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-90,-90],[-90,90],[-180,90],[-180,-90]],[[-120,-50],[-100,-50],[-100,-40],[-120,-40],[-120,-50]]]}]},{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,0],[10,0],[10,10],[0,10],[0,0]]]}]}]}",
                        15
                    },
                    {
                        "{\"op\":\"and\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-90,-90],[-90,90],[-180,90],[-180,-90]],[[-120,-50],[-100,-50],[-100,-40],[-120,-40],[-120,-50]]]}]},{\"op\":\"not\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-130,0],[0,0],[0,50],[-130,50],[-130,0]]]}]}]}]}",
                        3
                    }
                });
    }
}
