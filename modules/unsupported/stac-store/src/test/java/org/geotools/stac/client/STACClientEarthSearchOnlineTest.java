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
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

@SuppressWarnings("unchecked")
public class STACClientEarthSearchOnlineTest extends AbstractSTACClientOnlineTest {

    @Override
    protected String getServerURL() {
        return "https://earth-search.aws.element84.com/v1";
    }

    @Test
    public void testConformance() {
        STACLandingPage landingPage = this.client.getLandingPage();
        List<String> conformance = landingPage.getConformance();
        assertTrue(STACConformance.CORE.matches(conformance));
        assertTrue(STACConformance.COLLECTIONS.matches(conformance));
        assertTrue(STACConformance.FEATURES.matches(conformance));
        assertTrue(STACConformance.ITEM_SEARCH.matches(conformance));
        // assertTrue(ConformanceClasses.FILTER.matches(conformance)); not supported
        assertTrue(STACConformance.FIELDS.matches(conformance));
        assertTrue(STACConformance.QUERY.matches(conformance));
        assertTrue(STACConformance.SORT.matches(conformance));

        assertTrue(FeaturesConformance.CORE.matches(conformance));
        assertTrue(FeaturesConformance.GEOJSON.matches(conformance));
    }

    @Test
    public void testCollections() throws IOException {
        List<Collection> collections = this.client.getCollections();

        List<String> names = collections.stream().map(c -> c.getId()).collect(Collectors.toList());
        assertThat(names, CoreMatchers.hasItems("sentinel-2-l1c", "sentinel-2-l2a"));

        // check basics of one collection
        Collection s2 = collections.stream()
                .filter(c -> "sentinel-2-l2a".equals(c.getId()))
                .findFirst()
                .get();
        assertEquals(
                "Global Sentinel-2 data from the Multispectral Instrument (MSI) onboard Sentinel-2",
                s2.getDescription());
        assertEquals("Sentinel-2 Level-2A", s2.getTitle());
        CollectionExtent extent = s2.getExtent();
        assertEquals(
                Arrays.asList(-180d, -90d, 180d, 90d),
                extent.getSpatial().getBbox().get(0));
        List<Date> interval = extent.getTemporal().getInterval().get(0);
        assertNotNull(interval.get(0));
        assertNull(interval.get(1));
    }

    @Test
    public void testSearchAllGet() throws IOException {
        SearchQuery search = new SearchQuery();
        search.setCollections(Arrays.asList("sentinel-2-l2a"));
        search.setLimit(5);
        SimpleFeatureCollection fc = this.client.search(search, STACClient.SearchMode.GET);
        assertFalse(fc.isEmpty());

        // test some basic properties, including arrays and objects
        SimpleFeature feature = DataUtilities.first(fc);
        assertThat((String) feature.getAttribute("platform"), startsWith("sentinel-2"));
        assertThat((List<String>) feature.getAttribute("instruments"), hasItem("msi"));
        assertThat(feature.getAttribute("view:sun_azimuth"), instanceOf(Double.class));
        assertThat(feature.getAttribute("geometry"), instanceOf(Polygon.class));
        assertThat(feature.getAttribute("created"), instanceOf(Date.class));
        assertThat(feature.getAttribute("updated"), instanceOf(Date.class));
        assertThat(feature.getAttribute("datetime"), instanceOf(Date.class));
        assertThat(feature.getAttribute("processing:software"), instanceOf(ObjectNode.class));
        ObjectNode processing = (ObjectNode) feature.getAttribute("processing:software");
        assertTrue(processing.has("sentinel2-to-stac"));

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
        assertEquals("https://sentinel.esa.int/documents/247904/690755/Sentinel_Data_Legal_Notice", license);

        // check  assets
        ObjectNode assets = (ObjectNode) top.get("assets");
        assertNotNull(assets);
        ObjectNode visual = (ObjectNode) assets.get("visual");
        assertEquals("True color image", visual.get("title").asString());
        assertNotNull(visual.get("href"));
    }

    /**
     * Hits a very specific instant, only one item in it
     *
     * @throws IOException
     */
    @Test
    public void testSearchItemGet() throws IOException {
        SearchQuery search = new SearchQuery();
        search.setCollections(Arrays.asList("sentinel-2-l2a"));
        search.setDatetime("2022-07-18T12:27:16.885000Z");
        SimpleFeatureCollection fc = this.client.search(search, STACClient.SearchMode.GET);
        assertEquals(1, fc.size());

        // test some basic properties, including arrays and objects
        SimpleFeature feature = DataUtilities.first(fc);
        assertThat((String) feature.getAttribute("platform"), equalTo("sentinel-2b"));
        assertThat((List<String>) feature.getAttribute("instruments"), hasItem("msi"));

        // test the extra properties outside the properties node
        Map<String, JsonNode> top =
                (Map<String, JsonNode>) feature.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);

        // check  assets
        ObjectNode assets = (ObjectNode) top.get("assets");
        assertNotNull(assets);
        ObjectNode visual = (ObjectNode) assets.get("visual");
        assertEquals("True color image", visual.get("title").asString());
        assertNotNull(visual.get("href"));

        // check id
        assertEquals("S2B_26QLD_20220718_0_L2A", top.get("id").asString());
    }
}
