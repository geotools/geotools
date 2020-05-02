/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ogcapi;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CollectionsType {
    private static final String APPLICATION_JSON = "application/json";
    static JsonFactory factory = new JsonFactory();
    Map<String, CollectionType> collections = new HashMap<>();
    ArrayList<CoordinateReferenceSystem> crs = new ArrayList<>();

    @Override
    public String toString() {
        return "collections=" + collections;
    }

    static CollectionsType buildCollections(URL url) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        try (JsonParser parser = factory.createParser(url)) {
            JsonToken token = parser.nextToken(); // start 1st object
            CollectionsType ret = new CollectionsType();
            while (!parser.isClosed()) {
                // We can now see title, description and links array
                token = parser.nextToken();
                if (token == null) {
                    break;
                }

                if (JsonToken.FIELD_NAME.equals(token)
                        && "collections".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException(
                                "Was expecting an array of collections");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) { // collections
                        // array

                        ObjectNode node = mapper.readTree(parser);
                        // Actually, we want to read this items child link that is labelled
                        // self
                        if (node.has("links")) {
                            JsonNode links = node.get("links");
                            for (JsonNode link : links) {
                                if ("self".equalsIgnoreCase(link.get("rel").asText())
                                        && APPLICATION_JSON.equalsIgnoreCase(
                                                link.get("type").asText())) {
                                    CollectionType realCollection =
                                            CollectionType.buildRealCollection(
                                                    new URL(link.get("href").asText()));
                                    ret.collections.put(
                                            realCollection.getIdentifier(), realCollection);
                                }
                            }
                        }
                    }
                }
                if (JsonToken.FIELD_NAME.equals(token)
                        && "crs".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException(
                                "Was expecting an array of CRS Strings");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {

                        ObjectNode node = mapper.readTree(parser);
                        for (JsonNode c : node) {
                            ret.crs.add(OgcApiUtils.parseCRS(c.asText()));
                        }
                    }
                }
            }
            return ret;
        }
    }
}
