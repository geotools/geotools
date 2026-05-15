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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.And;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.data.DataUtilities;
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.stac.STACOfflineTest;
import org.geotools.stac.client.STACClient;
import org.geotools.util.Converters;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
import tools.jackson.databind.JsonNode;

public class STACFeatureSourceTest extends AbstractSTACStoreTest {

    private static String MAJA_1000 =
            BASE_URL + "/search?f=application%2Fgeo%2Bjson&collections=S2_L2A_MAJA&limit=1000";
    private static final String MAJA_100_POST = "{\"collections\":[\"S2_L2A_MAJA\"],\"limit\":100}";
    private static final String APPLICATION_JSON = "application/json";

    private static FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    private static String MAJA_SPACE_TIME = BASE_URL
            + "/search?bbox=50.0,12.0,50.001,12.001&collections=S2_L2A_MAJA&datetime=2022-07-22T10:05:59.024Z/2022-07-22T10:05:59.025Z&f=application/geo%2Bjson&limit=1000";

    private static final String DATETIME_FIELDS =
            "&fields=geometry,properties.datetime,type,id,-bbox,-properties,-assets,-links";
    private static String MAJA_PROPERTY_SELECTED = MAJA_SPACE_TIME + DATETIME_FIELDS;

    private static final String DATETIME_MINMAX_FIELDS =
            "&fields=properties.datetime,type,id,-bbox,-properties,-assets,-links";

    private static String MAJA_MIN = BASE_URL
            + "/search?collections=S2_L2A_MAJA&f=application/geo%2Bjson&limit=1"
            + DATETIME_MINMAX_FIELDS
            + "&sortby=datetime";

    private static String MAJA_MAX = BASE_URL
            + "/search?collections=S2_L2A_MAJA&f=application/geo%2Bjson&limit=1"
            + DATETIME_MINMAX_FIELDS
            + "&sortby=-datetime";

    private static String MAJA_PAGE_1 = BASE_URL + "/search?collections=S2_L2A_MAJA&f=application/geo%2Bjson&limit=10";

    private static String MAJA_PAGE_2 =
            "https://geoservice.dlr.de/eoc/ogc/stac/search?collections=S2_L2A_MAJA&requestId=1234&page=2";

    private static String CLOUD_50_GET =
            "https://geoservice.dlr.de/eoc/ogc/stac/search?collections=S2_L2A_MAJA&filter=\"eo:cloud_cover\" <"
                    + " 50&filter-lang=cql2-text&limit=1000&f=application/geo%2Bjson";

    private static String CLOUD_0_GET =
            "https://geoservice.dlr.de/eoc/ogc/stac/search?collections=S2_L2A_MAJA&filter=\"eo:cloud_cover\" <"
                    + " 0&filter-lang=cql2-text&limit=1000&f=application/geo%2Bjson";

    private static final String MAJA_CC_50_POST =
            "{\"collections\":[\"S2_L2A_MAJA\"],\"filter\":{\"op\":\"<\",\"args\":[{\"property\":\"eo:cloud_cover\"},50]},\"filter-lang\":\"cql2-json\",\"limit\":1000}";
    private static final String NS_URI = "https://stacspec.org/store";
    private static final String MAJA = "S2_L2A_MAJA";

    STACDataStore store;

    @Override
    public void setup() throws IOException {
        super.setup();

        // GET request support
        Class<?> cls = STACOfflineTest.class;
        httpClient.expectGet(new URL(MAJA_1000), geojsonResponse("majaAll.json", cls));
        httpClient.expectGet(new URL(MAJA_SPACE_TIME), geojsonResponse("majaBoxDateTime.json", cls));
        httpClient.expectGet(new URL(MAJA_PROPERTY_SELECTED), geojsonResponse("majaPropertySelected.json", cls));
        httpClient.expectGet(new URL(MAJA_MIN), geojsonResponse("majaMin.json", cls));
        httpClient.expectGet(new URL(MAJA_MAX), geojsonResponse("majaMax.json", cls));
        httpClient.expectGet(new URL(MAJA_PAGE_1), geojsonResponse("maja10.json", cls));
        httpClient.expectGet(new URL(MAJA_PAGE_2), geojsonResponse("maja20.json", cls));
        httpClient.expectGet(new URL(CLOUD_50_GET), geojsonResponse("majaCloudCover50.json", cls));
        httpClient.expectGet(new URL(CLOUD_0_GET), geojsonResponse("majaNone.json", cls));

        // POST request support
        URL postURL = new URL(BASE_URL + "/search?f=application/geo%2Bjson");
        httpClient.expectPost(postURL, MAJA_100_POST, APPLICATION_JSON, geojsonResponse("majaAll.json", cls));

        httpClient.expectPost(
                postURL, MAJA_CC_50_POST, APPLICATION_JSON, geojsonResponse("majaCloudCover50.json", cls));

        @SuppressWarnings("PMD.CloseResource") // managed by the store, based on mocks
        STACClient client = new STACClient(new URL(LANDING_PAGE_URL), httpClient);
        this.store = new STACDataStore(client);
        this.store.setSearchMode(STACClient.SearchMode.GET);
        this.store.setNamespaceURI(NS_URI);
    }

    @Test
    public void testBounds() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        ReferencedEnvelope bounds = fs.getBounds();
        assertEquals(DefaultGeographicCRS.WGS84, bounds.getCoordinateReferenceSystem());
        // from the collection spatial extent, fast count
        ReferencedEnvelope expected = new ReferencedEnvelope(5.76, 15.16, 46.8, 55.94, DefaultGeographicCRS.WGS84);
        assertTrue(bounds.boundsEquals2D(expected, 0.01));
    }

    @Test
    public void testBoundsQuery() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = new Query();
        q.setMaxFeatures(1000);
        assertNull(fs.getBounds(q));
    }

    @Test
    public void testCountAll() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        int count = fs.getCount(Query.ALL);

        // from MajaAll.json
        assertEquals(20, count);
    }

    @Test
    public void testCountQuery() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = getSpaceTimeFilter();
        int count = fs.getCount(q);

        assertEquals(1, count);
    }

    @Test
    public void testFetchData() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = getSpaceTimeFilter();
        SimpleFeatureCollection fc = fs.getFeatures(q);

        assertEquals(1, fc.size());
        assertEquals(store.getSchema(MAJA), fc.getSchema());
        SimpleFeature f = DataUtilities.first(fc);

        // check a few attributes
        assertEquals(Converters.convert("2022-07-22T10:05:59.024+00:00", Date.class), f.getAttribute("datetime"));
        assertEquals("SENTINEL-2B", f.getAttribute("platform"));
        assertEquals(106.80, (Double) f.getAttribute("view:azimuth"), 0.01);
        JsonNode processing = (JsonNode) f.getAttribute("processing:software");
        assertEquals("1.3", processing.get("CATENA").asString());

        // check the assets can be found in the extra attributes
        Map<String, Object> topLevels = getTopLevelAttributes(f);
        JsonNode assets = (JsonNode) topLevels.get("assets");
        assertEquals("image/jpeg", assets.get("thumbnail").get("type").asString());
    }

    @Test
    public void testFetchDataHardLimit() throws Exception {
        store.setHardLimit(20);
        store.setFetchSize(10);
        try {
            SimpleFeatureSource fs = store.getFeatureSource(MAJA);
            SimpleFeatureCollection fc = fs.getFeatures(Query.ALL);

            assertEquals(20, fc.size());
            assertEquals(store.getSchema(MAJA), fc.getSchema());
            List<SimpleFeature> features = DataUtilities.list(fc);
            assertEquals(20, features.size());
        } finally {
            store.setHardLimit(STACDataStore.DEFAULT_HARD_LIMIT);
            store.setFetchSize(STACDataStore.DEFAULT_FETCH_SIZE);
        }
    }

    private static Query getSpaceTimeFilter() {
        Query q = new Query();
        BBOX bbox = FF.bbox("", 12, 50, 12.001, 50.001, "EPSG:4326");
        PropertyIsEqualTo datetime = FF.equals(FF.property("datetime"), FF.literal("2022-07-22T10:05:59.024Z"));
        And spaceTimeFilter = FF.and(bbox, datetime);
        q.setFilter(spaceTimeFilter);
        return q;
    }

    @Test
    public void testPropertySelection() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = getSpaceTimeFilter();
        q.setPropertyNames("geometry", "datetime");
        SimpleFeatureCollection fc = fs.getFeatures(q);

        assertEquals(1, fc.size());
        SimpleFeatureType schema = fc.getSchema();
        assertEquals(2, schema.getAttributeCount());
        assertEquals(Polygon.class, schema.getDescriptor("geometry").getType().getBinding());
        assertEquals(Date.class, schema.getDescriptor("datetime").getType().getBinding());
        SimpleFeature f = DataUtilities.first(fc);

        // check a few attributes
        assertEquals(Converters.convert("2022-07-22T10:05:59.024+00:00", Date.class), f.getAttribute("datetime"));
        // filtered out fields
        assertNull(f.getAttribute("platform"));
        assertNull(f.getAttribute("view:azimuth"));
        assertNull(f.getAttribute("processing:software"));

        // check the assets can not be found in the extra attributes
        Map<String, Object> topLevels = getTopLevelAttributes(f);
        assertNull(topLevels.get("assets"));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getTopLevelAttributes(SimpleFeature f) {
        return (Map<String, Object>) f.getUserData().get(GeoJSONReader.TOP_LEVEL_ATTRIBUTES);
    }

    @Test
    public void testMinimumVisitor() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        SimpleFeatureCollection fc = fs.getFeatures();
        AtomicBoolean optimized = new AtomicBoolean(false);
        MinVisitor visitor = new MinVisitor("datetime") {
            @Override
            public void setValue(Object result) {
                optimized.set(true);
                super.setValue(result);
            }
        };
        fc.accepts(visitor, null);

        assertTrue("The visitor should have been optimized out", optimized.get());
        assertEquals(Converters.convert("2022-07-22T10:05:59.024Z", Date.class), visitor.getMin());
    }

    @Test
    public void testMaximumVisitor() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        SimpleFeatureCollection fc = fs.getFeatures();
        AtomicBoolean optimized = new AtomicBoolean(false);
        MaxVisitor visitor = new MaxVisitor("datetime") {
            @Override
            public void setValue(Object result) {
                optimized.set(true);
                super.setValue(result);
            }
        };
        fc.accepts(visitor, null);

        assertTrue("The visitor should have been optimized out", optimized.get());
        assertEquals(Converters.convert("2023-07-22T10:05:59.024Z", Date.class), visitor.getMax());
    }

    @Test
    public void testCloudCoverGet() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = new Query();
        q.setFilter(ECQL.toFilter("eo:cloud_cover < 50"));
        SimpleFeatureCollection fc = fs.getFeatures(q);

        assertEquals(10, fc.size());
        try (SimpleFeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                SimpleFeature next = fi.next();
                assertThat((Integer) next.getAttribute("eo:cloud_cover"), Matchers.lessThan(50));
            }
        }
    }

    @Test
    public void testCloudCoverPost() throws Exception {
        store.setSearchMode(STACClient.SearchMode.POST);

        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = new Query();
        q.setFilter(ECQL.toFilter("eo:cloud_cover < 50"));
        SimpleFeatureCollection fc = fs.getFeatures(q);

        assertEquals(10, fc.size());
        try (SimpleFeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                SimpleFeature next = fi.next();
                assertThat((Integer) next.getAttribute("eo:cloud_cover"), Matchers.lessThan(50));
            }
        }
    }

    @Test
    public void testCloudCoverZero() throws Exception {
        SimpleFeatureSource fs = store.getFeatureSource(MAJA);
        Query q = new Query();
        q.setFilter(ECQL.toFilter("eo:cloud_cover < 0")); // impossible filter
        SimpleFeatureCollection fc = fs.getFeatures(q);

        // used to break
        assertEquals(0, fc.size());
        assertEquals(fs.getSchema(), fc.getSchema());
    }
}
