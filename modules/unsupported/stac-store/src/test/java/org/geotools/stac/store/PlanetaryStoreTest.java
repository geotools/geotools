/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.util.FeatureStreams;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.HTTPClient;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.referencing.CRS;
import org.geotools.stac.client.STACClient;
import org.geotools.stac.client.STACClient.SearchMode;
import org.junit.Assert;
import org.junit.Test;

public class PlanetaryStoreTest {

    @Test
    public void testPlanetaryStore() throws Exception {
        String url = "https://planetarycomputer.microsoft.com/api/stac/v1";
        String id = "sentinel-2-l2a";
        ReferencedEnvelope extent = new ReferencedEnvelope(599837, 607683, 6646573, 6660078, CRS.decode("EPSG:32632"));
        try (STACClient client = new STACClient(new URL(url), createMockHttpClient())) {

            STACDataStore store = new STACDataStore(client);
            store.setSearchMode(SearchMode.GET);
            FilterFactory filterFact = CommonFactoryFinder.getFilterFactory();

            try {
                SimpleFeatureSource source = store.getFeatureSource(id);
                Query query = new Query();
                query.setFilter(filterFact.bbox(filterFact.property("geometry"), extent));

                SimpleFeatureCollection features = source.getFeatures(query);
                Set<String> uniqueProductUri = new HashSet<>();

                FeatureStreams.toFeatureStream(features)
                        .map(f -> (String) f.getAttribute("s2:product_uri"))
                        .filter(u -> !uniqueProductUri.add(u))
                        .findFirst()
                        .ifPresent(u -> Assert.fail("Same uri reappeared: " + u));

            } finally {
                store.dispose();
            }
        }
    }

    private HTTPClient createMockHttpClient() throws Exception {
        MockHttpClient client = new MockHttpClient();
        client.expectGet(
                new URL("https://planetarycomputer.microsoft.com/api/stac/v1"),
                new MockHttpResponse(getClass().getResourceAsStream("planetary_lp.json"), "application/json"));
        client.expectGet(
                new URL("https://planetarycomputer.microsoft.com/api/stac/v1/collections"),
                new MockHttpResponse(getClass().getResourceAsStream("planetary_coll.json"), "application/json"));
        client.expectGet(
                new URL(
                        "https://planetarycomputer.microsoft.com/api/stac/v1/search?collections=sentinel-2-l2a&limit=100"),
                new MockHttpResponse(getClass().getResourceAsStream("planetary_schema.json"), "application/geo+json"));
        client.expectGet(
                new URL(
                        "https://planetarycomputer.microsoft.com/api/stac/v1/search?bbox=10.787059171304284%2C59.94247807858423%2C10.934477618156766%2C60.06565996363739&collections=sentinel-2-l2a&limit=1000"),
                new MockHttpResponse(getClass().getResourceAsStream("planetary_first.json"), "application/geo+json"));
        client.expectGet(
                new URL(
                        "https://planetarycomputer.microsoft.com/api/stac/v1/search?bbox=10.787059171304284,59.94247807858423,10.934477618156766,60.06565996363739&collections=sentinel-2-l2a&limit=1000&token=next:sentinel-2-l2a:S2B_MSIL2A_20220711T103629_R008_T32VNM_20220713T054613"),
                new MockHttpResponse(getClass().getResourceAsStream("planetary_second.json"), "application/geo+json"));
        client.expectGet(
                new URL(
                        "https://planetarycomputer.microsoft.com/api/stac/v1/search?bbox=10.787059171304284,59.94247807858423,10.934477618156766,60.06565996363739&collections=sentinel-2-l2a&limit=1000&token=next:sentinel-2-l2a:S2A_MSIL2A_20190116T105401_R051_T32VNM_20201008T015942"),
                new MockHttpResponse(getClass().getResourceAsStream("planetary_third.json"), "application/geo+json"));

        return client;
    }
}
