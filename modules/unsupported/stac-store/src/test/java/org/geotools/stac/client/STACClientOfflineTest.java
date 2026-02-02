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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.DataUtilities;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.stac.STACOfflineTest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/** STACClient test using stored responses and a mock HTTPClient, can be run offline */
@SuppressWarnings("unchecked")
public class STACClientOfflineTest extends STACOfflineTest {

    protected STACClient client;

    @Override
    public void setup() throws IOException {
        super.setup();

        this.client = new STACClient(new URL(LANDING_PAGE_URL), httpClient);
    }

    @Test
    public void testConformance() {
        STACLandingPage landingPage = this.client.getLandingPage();
        List<String> conformance = landingPage.getConformance();
        assertTrue(STACConformance.CORE.matches(conformance));
        //  assertTrue(STACConformance.COLLECTIONS.matches(conformance));
        // assertTrue(STACConformance.FEATURES.matches(conformance));
        assertTrue(STACConformance.ITEM_SEARCH.matches(conformance));
        assertTrue(STACConformance.FILTER.matches(conformance));
        // assertTrue(STACConformance.FIELDS.matches(conformance));
        // assertTrue(STACConformance.QUERY.matches(conformance));
        assertTrue(STACConformance.SORT.matches(conformance));

        assertTrue(FeaturesConformance.CORE.matches(conformance));
        assertTrue(FeaturesConformance.GEOJSON.matches(conformance));
    }

    @Test
    public void testCollections() throws IOException {
        List<Collection> collections = this.client.getCollections();

        List<String> names = collections.stream().map(c -> c.getId()).collect(Collectors.toList());
        assertThat(names, CoreMatchers.hasItems("S2_L2A_MAJA", "S2_L3A_WASP"));

        // check basics of one collection
        Collection s2 = collections.stream()
                .filter(c -> "S2_L3A_WASP".equals(c.getId()))
                .findFirst()
                .get();
        assertEquals(
                "Sentinel-2 L3A WASP Products of Germany processed by DLR using the WASP processor",
                s2.getDescription());
        assertEquals("Sentinel-2 L3A Monthly WASP Products", s2.getTitle());
        CollectionExtent extent = s2.getExtent();
        assertEquals(
                Arrays.asList(5.7635451, 46.7936707, 15.1527533, 55.046986),
                extent.getSpatial().getBbox().get(0));
        List<Date> interval = extent.getTemporal().getInterval().get(0);
        assertNotNull(interval.get(0));
        assertNull(interval.get(1));
    }

    @Test
    public void testSearchAllGet() throws IOException {
        SearchQuery search = new SearchQuery();
        search.setCollections(Arrays.asList("S2_L2A_MAJA"));
        SimpleFeatureCollection fc = this.client.search(search, STACClient.SearchMode.GET);
        assertNotEquals(0, fc.size());

        // test some basic properties, including arrays and objects
        SimpleFeature feature = DataUtilities.first(fc);
        assertThat((String) feature.getAttribute("platform"), equalTo("SENTINEL-2B"));
        assertThat((List<String>) feature.getAttribute("instruments"), hasItem("MSI"));
        assertThat(feature.getAttribute("view:sun_azimuth"), instanceOf(Double.class));
        assertThat(feature.getAttribute("geometry"), instanceOf(Polygon.class));
        assertThat(feature.getAttribute("created"), instanceOf(Date.class));
        assertThat(feature.getAttribute("updated"), instanceOf(Date.class));
        assertThat(feature.getAttribute("datetime"), instanceOf(Date.class));
        assertThat(feature.getAttribute("processing:software"), instanceOf(ObjectNode.class));
        ObjectNode processing = (ObjectNode) feature.getAttribute("processing:software");
        assertEquals("1.3", processing.get("CATENA").asString());

        // test the extra properties outside the properties node
        Map<String, Object> top = (Map<String, Object>) feature.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);

        // check the links
        ArrayNode links = (ArrayNode) top.get("links");
        assertNotNull(links);
        String license = StreamSupport.stream(links.spliterator(), false)
                .filter(l -> "license".equals(l.get("rel").asString()))
                .map(l -> l.get("href").asString())
                .findFirst()
                .orElseThrow(() -> new AssertionError("Could not find the license rel"));
        assertEquals("https://spdx.org/licenses/CC-BY-4.0", license);

        // check  assets
        ObjectNode assets = (ObjectNode) top.get("assets");
        assertNotNull(assets);
        ObjectNode sre_b2 = (ObjectNode) assets.get("SRE_B2");
        assertEquals("Surface Reflectance (SRE_B2)", sre_b2.get("title").asString());
        assertEquals("image/tiff; application=geotiff", sre_b2.get("type").asString());
        assertNotNull(sre_b2.get("href"));
    }

    /**
     * Hits a very specific instant, only one item in it
     *
     * @throws IOException
     */
    @Test
    public void testSearchItemGet() throws IOException {
        SearchQuery search = new SearchQuery();
        search.setCollections(Arrays.asList("S2_L2A_MAJA"));
        search.setDatetime("2022-07-14T10:46:29.0240000Z");
        SimpleFeatureCollection fc = this.client.search(search, STACClient.SearchMode.GET);
        assertEquals(1, fc.size());

        // test some basic properties, including arrays and objects
        SimpleFeature feature = DataUtilities.first(fc);
        assertThat((String) feature.getAttribute("platform"), equalTo("SENTINEL-2B"));
        assertThat((List<String>) feature.getAttribute("instruments"), hasItem("MSI"));

        // test the extra properties outside the properties node
        Map<String, JsonNode> top =
                (Map<String, JsonNode>) feature.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);

        // check  assets
        ObjectNode assets = (ObjectNode) top.get("assets");
        assertNotNull(assets);
        ObjectNode visual = (ObjectNode) assets.get("thumbnail");
        assertEquals("Thumbnail", visual.get("title").asString());
        assertNotNull(visual.get("href"));

        // check id
        assertEquals(
                "SENTINEL2B_20220714-105646-098_L2A_T32ULB_C", top.get("id").asString());
    }

    @Test
    public void testSearchCollectionsTolerance() throws IOException {
        this.client = new STACClient(new URL(LANDING_AU), httpClient);
        List<String> collections =
                client.getCollections().stream().map(c -> c.getId()).collect(Collectors.toList());
        // just checking a few items are there
        assertThat(collections, Matchers.hasItems("aster_aloh_group_composition", "aster_aloh_group_content"));

        // also check the search page is found, despite using the wrong mime
        assertEquals(
                "https://explorer.sandbox.dea.ga.gov.au/stac/search",
                client.getLandingPage().getSearchLink(HttpMethod.GET));
    }
}
