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
package org.geotools.stac.store;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.stac.client.SearchQuery;
import org.junit.Before;
import org.junit.Test;

public class SearchQueryBuilderTest extends AbstractSTACStoreTest {

    private SearchQueryBuilder builder;
    private SimpleFeatureType schema;

    @Before
    public void setupBuilder() throws IOException {
        this.schema = store.getSchema(MAJA);
        this.builder = new SearchQueryBuilder(schema, store);
    }

    @Test
    public void testSpatial() throws Exception {
        Query q = new Query(schema.getTypeName());
        q.setFilter(ECQL.toFilter("BBOX(geometry, -180, -90, 180, 90, 'CRS:84')"));

        Pair<SearchQuery, Filter> pair = builder.toSearchQuery(q, true);
        assertEquals(Filter.INCLUDE, pair.getValue());
        SearchQuery sq = pair.getKey();
        assertEquals(Arrays.asList(MAJA), sq.getCollections());
        assertArrayEquals(new double[] {-180, -90, 180, 90}, sq.getBbox(), 0d);
        assertNull(sq.getFields());
        assertNull(sq.getFilter());
        assertNull(sq.getDatetime());
    }

    @Test
    public void testTemporal() throws Exception {
        Query q = new Query(schema.getTypeName());
        q.setFilter(ECQL.toFilter("datetime BEFORE 2006-11-30T01:30:00Z"));

        Pair<SearchQuery, Filter> pair = builder.toSearchQuery(q, true);
        assertEquals(Filter.INCLUDE, pair.getValue());
        SearchQuery sq = pair.getKey();
        assertEquals(Arrays.asList(MAJA), sq.getCollections());
        assertNull(sq.getBbox());
        assertNull(sq.getFields());
        assertNull(sq.getFilter());
        assertEquals("../2006-11-30T01:30:00Z", sq.getDatetime());
    }

    @Test
    public void testTemporalLower() throws Exception {
        Query q = new Query(schema.getTypeName());
        q.setFilter(ECQL.toFilter("datetime < 2006-11-30T01:30:00Z"));

        Pair<SearchQuery, Filter> pair = builder.toSearchQuery(q, true);
        assertEquals(Filter.INCLUDE, pair.getValue());
        SearchQuery sq = pair.getKey();
        assertEquals(Arrays.asList(MAJA), sq.getCollections());
        assertNull(sq.getBbox());
        assertNull(sq.getFields());
        assertNull(sq.getFilter());
        assertEquals("../2006-11-30T01:30:00Z", sq.getDatetime());
    }

    @Test
    public void testSpatioTemporal() throws Exception {
        Query q = new Query(schema.getTypeName());
        q.setFilter(
                ECQL.toFilter("BBOX(geometry, -180, -90, 180, 90, 'CRS:84') and datetime BEFORE 2006-11-30T01:30:00Z"));

        Pair<SearchQuery, Filter> pair = builder.toSearchQuery(q, true);
        assertEquals(Filter.INCLUDE, pair.getValue());
        SearchQuery sq = pair.getKey();
        assertEquals(Arrays.asList(MAJA), sq.getCollections());
        assertArrayEquals(new double[] {-180, -90, 180, 90}, sq.getBbox(), 0d);
        assertNull(sq.getFields());
        assertNull(sq.getFilter());
        assertEquals("../2006-11-30T01:30:00Z", sq.getDatetime());
    }

    @Test
    public void testMixed() throws Exception {
        Query q = new Query(schema.getTypeName());
        q.setFilter(ECQL.toFilter(
                "BBOX(geometry, -180, -90, 180, 90, 'CRS:84') and datetime BEFORE 2006-11-30T01:30:00Z and foo > 10"));
        q.setPropertyNames("geometry", "datetime");

        Pair<SearchQuery, Filter> pair = builder.toSearchQuery(q, true);
        assertEquals(Filter.INCLUDE, pair.getValue());
        SearchQuery sq = pair.getKey();
        assertEquals(Arrays.asList(MAJA), sq.getCollections());
        assertArrayEquals(new double[] {-180, -90, 180, 90}, sq.getBbox(), 0d);
        assertEquals(
                Arrays.asList(
                        "geometry", "properties.datetime", "type", "id", "-bbox", "-properties", "-assets", "-links"),
                sq.getFields());
        assertEquals(ECQL.toFilter("foo > 10"), sq.getFilter());
        assertEquals("../2006-11-30T01:30:00Z", sq.getDatetime());
    }
}
