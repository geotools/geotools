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

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import java.util.Arrays;
import java.util.List;
import org.geotools.filter.text.cql2.CQL;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

/** Tests proper JSON encoding of the Search request */
public class SearchJSONTest {

    /**
     * Check easy stuff natively handled by
     *
     * @throws Exception
     */
    @Test
    public void testBasics() throws Exception {
        SearchQuery search = new SearchQuery();
        List<String> collections = Arrays.asList("c1", "c2");
        search.setCollections(collections);
        String datetime = "2022-10-10T00:00:00";
        search.setDatetime(datetime);
        search.setLimit(10);
        String json = STACClient.OBJECT_MAPPER.writeValueAsString(search);

        DocumentContext doc = JsonPath.parse(json);
        assertEquals(collections, doc.read("collections"));
        assertEquals(10, doc.read("limit", Integer.class).intValue());
        assertEquals(datetime, doc.read("datetime"));
    }

    @Test
    public void testIntersects() throws Exception {
        SearchQuery search = new SearchQuery();
        Geometry point = new WKTReader().read("POINT(0 0)");
        search.setIntersects(point);
        String json = STACClient.OBJECT_MAPPER.writeValueAsString(search);

        DocumentContext doc = JsonPath.parse(json);
        assertEquals("Point", doc.read("intersects.type"));
        assertEquals(Arrays.asList(0, 0), doc.read("intersects.coordinates"));
    }

    @Test
    public void testCQL2Text() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setFilter(CQL.toFilter("A = 10"));
        search.setFilterLang(FilterLang.CQL2_TEXT);
        String json = STACClient.OBJECT_MAPPER.writeValueAsString(search);

        DocumentContext doc = JsonPath.parse(json);
        assertEquals("cql2-text", doc.read("filter-lang"));
        assertEquals("A = 10", doc.read("filter"));
    }

    @Test
    public void testCQL2JSON() throws Exception {
        SearchQuery search = new SearchQuery();
        search.setFilter(CQL.toFilter("A = 10"));
        search.setFilterLang(FilterLang.CQL2_JSON);
        String json = STACClient.OBJECT_MAPPER.writeValueAsString(search);

        DocumentContext doc = JsonPath.parse(json);
        assertEquals("cql2-json", doc.read("filter-lang"));
        assertEquals("=", doc.read("filter.op"));
        assertEquals("A", doc.read("filter.args[0].property"));
        assertEquals(10, doc.read("filter.args[1]", Integer.class).intValue());
    }
}
