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

import static java.util.Collections.singletonMap;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.util.logging.Logging;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Minimal STAC API client supporting the needs of the STAC datastore. Not currently meant to be a generic STAC client.
 */
public class STACClient implements Closeable {

    static final Logger LOGGER = Logging.getLogger(STACClient.class);

    /** Used to indicate how to perform search queries. */
    public enum SearchMode {
        /** Forces usage of GET requests (might fail if parameters are too big/long) */
        GET,
        /** Forces usage of POST requests (might fail if not supported by the server) */
        POST
    }

    public static final String JSON_MIME = "application/json";
    public static final String GEOJSON_MIME = "application/geo+json";

    private static final Map<String, String> ACCEPTS_JSON = singletonMap("Accepts", JSON_MIME);
    static ObjectMapper OBJECT_MAPPER;

    /** Initialize an ObjectMapper that's tolerant, won't generate missing fields, and can parse GeoJSON geometries */
    static {
        OBJECT_MAPPER = JsonMapper.builder()
                .addModule(new JtsModule())
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_EMPTY))
                .build();
    }

    private final HTTPClient http;

    private final STACLandingPage landingPage;

    public STACClient(URL landingPageURL, HTTPClient http) throws IOException {
        this.http = http;
        HTTPResponse response = http.get(landingPageURL, ACCEPTS_JSON);
        checkJSONResponse(response);
        try (InputStream is = response.getResponseStream()) {
            this.landingPage = OBJECT_MAPPER.readValue(is, STACLandingPage.class);
        }
    }

    private void checkJSONResponse(HTTPResponse response) {
        String mime = response.getContentType();
        if (mime == null || !mime.startsWith(JSON_MIME))
            throw new IllegalArgumentException("Was expecting a JSON response, got a different mime type: " + mime);
    }

    private void checkGeoJSONResponse(HTTPResponse response) {
        String mime = response.getContentType();
        if (mime == null || !mime.startsWith(GEOJSON_MIME))
            throw new IllegalArgumentException("Was expecting a GeoJSON response, got a different mime type: " + mime);
    }

    /**
     * Returns the landing page of the STAC service
     *
     * @return
     */
    public STACLandingPage getLandingPage() {
        return this.landingPage;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource") // Closeable is closed, PMD just doesn't see it
    public void close() throws IOException {
        if (http instanceof Closeable closeable) {
            closeable.close();
        }
    }

    /**
     * Returns the collection description, if a "data" link is available in the home page
     *
     * @return
     * @throws IOException
     */
    public List<Collection> getCollections() throws IOException {
        Optional<Link> maybeData =
                landingPage.getLinks().stream().filter(this::isDataJSONLink).findFirst();
        if (maybeData.isEmpty()) {
            maybeData = landingPage.getLinks().stream()
                    .filter(this::isChildrenJSONLink)
                    .findFirst();
        }
        if (maybeData.isEmpty()) return Collections.emptyList();

        HTTPResponse response = http.get(new URL(maybeData.get().getHref()));
        checkJSONResponse(response);
        try (InputStream is = response.getResponseStream()) {
            return OBJECT_MAPPER.readValue(is, CollectionList.class).getCollections().stream()
                    // discard eventual sub-catalogs
                    .filter(c -> c.getType() == null || "collection".equalsIgnoreCase(c.getType()))
                    .collect(Collectors.toList());
        }
    }

    private boolean isDataJSONLink(Link l) {
        return l.getRel().equals("data")
                && (StringUtils.isEmpty(l.getType()) || l.getType().equals(JSON_MIME));
    }

    private boolean isChildrenJSONLink(Link l) {
        return l.getRel().equals("children")
                && (StringUtils.isEmpty(l.getType()) || l.getType().equals(JSON_MIME));
    }

    /**
     * Retrieves data using the Search API
     *
     * @param search The search request
     * @param mode The search mode
     * @throws IOException
     */
    public SimpleFeatureCollection search(SearchQuery search, SearchMode mode) throws IOException {
        return this.search(search, mode, null);
    }

    /**
     * Retrieves data using the Search API
     *
     * @param search The search request
     * @param mode The search mode
     * @param schema An optional base schema, can be used to ensure at least those properties are present
     * @throws IOException
     */
    public SimpleFeatureCollection search(SearchQuery search, SearchMode mode, SimpleFeatureType schema)
            throws IOException {
        if (!STACConformance.ITEM_SEARCH.matches(landingPage.getConformance()))
            // might want to take a different path and see if OGC API - Features is supported
            // instead
            throw new IllegalStateException(
                    "The server does not support the item-search conformance class, cannot query it");

        try {
            HTTPResponse response = null;
            if (mode == SearchMode.GET) {
                URL getURL = new SearchGetBuilder(landingPage).toGetURL(search);

                LOGGER.log(Level.FINE, () -> "STAC GET search request: " + getURL);
                response = this.http.get(getURL);
            } else {
                String url = landingPage.getSearchLink(HttpMethod.POST);
                if (url == null) {
                    throw new IllegalArgumentException("Cannot find GeoJSON search POST link");
                }
                URL postURL = new URL(url);

                String body = OBJECT_MAPPER.writeValueAsString(search);

                LOGGER.log(Level.FINE, () -> "STAC POST search request: " + postURL + " with body:\n" + body);
                response = this.http.post(
                        postURL, new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)), "application/json");
            }
            checkGeoJSONResponse(response);

            try (STACGeoJSONReader reader =
                    new STACGeoJSONReader(new BufferedInputStream(response.getResponseStream(), 1024 * 32), http)) {
                if (schema != null) reader.setSchema(schema);
                return reader.getFeatures();
            }
        } catch (URISyntaxException e) {
            throw new IOException("Failed to build the search query URL", e);
        }
    }

    /** Returns the HTTP client used by the this STAC client */
    public HTTPClient getHttp() {
        return http;
    }
}
