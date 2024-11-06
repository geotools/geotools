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
 * See <a href="https://docs.ogc.org/is/21-065r2/21-065r2.html#_conformance_test_26">table 12 from
 * section A.7.2 Conformance test 26.</a>
 */
@RunWith(Parameterized.class)
public class ConformanceTest26OnlineTest
        extends org.geotools.filter.text.cql_2.conformance.ConformanceTest26OnlineTest {

    public ConformanceTest26OnlineTest(String dataset, String criteria, int expectedFeatures) {
        super(dataset, criteria, expectedFeatures);
    }

    @Parameterized.Parameters(name = "{index} {0} {1}")
    public static Collection<Object[]> params() {
        return Arrays.asList(
                new Object[][] {
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]}",
                        8
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[150,-90],[150,90],[-150,90],[-150,-90],[150,-90]]]}]}",
                        10
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Point\",\"coordinates\":[7.02,49.92]}]}",
                        1
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"and\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]},{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[5,50],[5,60],[10,60],[10,50],[5,50]]]}]}]}",
                        3
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"and\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]},{\"op\":\"not\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[5,50],[5,60],[10,60],[10,50],[5,50]]]}]}]}]}",
                        5
                    },
                    {
                        "ne_110m_admin_0_countries",
                        "{\"op\":\"or\",\"args\":[{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]},{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-90,40],[-90,50],[-60,50],[-60,40],[-90,40]]]}]}]}",
                        10
                    },
                    {
                        "ne_110m_populated_places_simple",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[0,40],[0,50],[10,50],[10,40],[0,40]]]}]}",
                        7
                    },
                    {
                        "ne_110m_rivers_lake_centerlines",
                        "{\"op\":\"s_intersects\",\"args\":[{\"property\":\"geom\"},{\"type\":\"Polygon\",\"coordinates\":[[[-180,-90],[-180,90],[0,90],[0,-90],[-180,-90]]]}]}",
                        4
                    }
                });
    }

    @Override
    protected int featuresReturned(DataStore ds) throws CQLException, IOException {
        Filter filter = CQL2Json.toFilter(this.criteria);
        return ds.getFeatureSource(this.dataset).getFeatures(filter).size();
    }
}
