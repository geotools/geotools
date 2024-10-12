/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.client;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.stac.STACOfflineTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;

public class SearchGetBuilderTest {

    private static final String BASE = "https://geoservice.dlr.de/eoc/ogc/stac/search?f=application%2Fgeo%2Bjson";
    private static SearchGetBuilder BUILDER;

    @BeforeClass
    public static void setupBuilder() throws IOException {
        try (InputStream stream = STACOfflineTest.class.getResourceAsStream("landingPage.json")) {
            STACLandingPage landingPage = STACClient.OBJECT_MAPPER.readValue(stream, STACLandingPage.class);
            BUILDER = new SearchGetBuilder(landingPage);
        }
    }

    @Test
    public void testSimple() throws Exception {
        final SearchQuery search = new SearchQuery();
        assertURLEquals(BASE, BUILDER.toGetURL(search));
    }

    @Test
    public void testCollections() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setCollections(Arrays.asList("c1", "c2"));
        assertURLEquals(BASE + "&collections=c1%2Cc2", BUILDER.toGetURL(search));
    }

    @Test
    public void testBBOX() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setBbox(new double[] {-180, -90, 180, 90});
        assertURLEquals(BASE + "&bbox=-180.0%2C-90.0%2C180.0%2C90.0", BUILDER.toGetURL(search));
    }

    @Test
    public void testIntersects() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setIntersects(new WKTReader().read("POINT(0 0)"));
        assertURLEquals(
                BASE + "&intersects=%7B%22type%22%3A%22Point%22%2C%22coordinates%22%3A%5B0%2C0%5D%7D",
                BUILDER.toGetURL(search));
    }

    @Test
    public void testTextFilter() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setFilter(CQL.toFilter("A = 10"));
        assertURLEquals(BASE + "&filter=A+%3D+10&filter-lang=cql2-text", BUILDER.toGetURL(search));
    }

    @Test
    public void testJSONFilter() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setFilter(CQL.toFilter("A = 10"));
        search.setFilterLang(FilterLang.CQL2_JSON);
        assertURLEquals(
                BASE
                        + "&filter=%7B%22op%22%3A%22%3D%22%2C%22args%22%3A%5B%7B%22property%22%3A%22A%22"
                        + "%7D%2C10%5D%7D&filter-lang=cql2-json",
                BUILDER.toGetURL(search));
    }

    private void assertURLEquals(String expected, URL actual) throws MalformedURLException {
        // thankfully URLBuilder encodes parameters in predictable order
        assertEquals(new URL(expected), actual);
    }
}
