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
import org.geotools.data.geojson.GeoJSONReader;
import org.geotools.data.geojson.PagingFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.http.HTTPClient;
import tools.jackson.databind.JsonNode;

/** A subclass of GeoJSONReader that can perform paging requests using a given {@link HTTPClient} */
class STACGeoJSONReader extends GeoJSONReader {

    private final HTTPClient http;

    public STACGeoJSONReader(InputStream is, HTTPClient http) throws IOException {
        super(is);
        this.http = http;
    }

    @Override
    protected PagingFeatureCollection getPagingFeatureCollection(
            SimpleFeatureCollection result, Integer matched, JsonNode next) {
        return new STACPagingFeatureCollection(result, next, matched, http);
    }
}
