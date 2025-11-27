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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.geotools.data.geojson.PagingFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.http.HTTPClient;
import tools.jackson.databind.JsonNode;

class STACPagingFeatureCollection extends PagingFeatureCollection {

    private final HTTPClient http;

    public STACPagingFeatureCollection(SimpleFeatureCollection first, JsonNode next, Integer matched, HTTPClient http) {
        super(first, next, matched);
        this.http = http;
    }

    @Override
    protected SimpleFeatureCollection readNext(JsonNode next) throws IOException {
        if (next == null) return null;
        JsonNode href = next.get("href");
        if (href == null) return null;

        // TODO: consider complex request merge logic for POST requests, described at
        // https://github.com/radiantearth/stac-api-spec/tree/main/item-search#pagination

        LOGGER.fine(() -> "Fetching next page of data at " + href.asString());
        try (InputStream is = http.get(new URL(href.asString())).getResponseStream();
                STACGeoJSONReader reader = new STACGeoJSONReader(is, http)) {
            return reader.getFeatures();
        }
    }
}
