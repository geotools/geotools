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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class CollectionType {
    static JsonFactory factory = new JsonFactory();
    static final Logger LOGGER = Logging.getLogger(CollectionType.class);
    String identifier;
    String title;
    String description;
    ReferencedEnvelope extent = new ReferencedEnvelope();
    ArrayList<Link> links = new ArrayList<>();
    ArrayList<CoordinateReferenceSystem> crs = new ArrayList<>(0); // not
    // expecting
    // any of these
    // here
    ArrayList<StyleType> styles = new ArrayList<>();

    public void addStyle(StyleType style) {
        try {
            styles.add(style);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ReferencedEnvelope getExtent() {
        return extent;
    }

    public void setExtent(ReferencedEnvelope extent) {
        this.extent = extent;
    }

    public static CollectionType buildRealCollection(URL url) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        try (JsonParser parser = factory.createParser(url)) {
            JsonToken token = parser.nextToken(); // start 1st object

            while (!parser.isClosed()) {
                // We can now see title, description and links array
                token = parser.nextToken();
                if (token == null) {
                    break;
                }
                ObjectNode node = mapper.readTree(parser);
                return CollectionType.buildCollection(node);
            }
        }
        return null;
    }

    static CollectionType buildCollection(ObjectNode node) throws IOException {
        CollectionType ret = new CollectionType();

        if (node.has("links")) {
            JsonNode links = node.get("links");
            for (JsonNode link : links) {
                ret.links.add(Link.buildLink((ObjectNode) link));
            }
        }
        ret.setIdentifier(node.get("id").asText());
        if (node.has("title")) {
            ret.setTitle(node.get("title").asText());
        }
        if (node.has("description")) {
            ret.setDescription(node.get("description").asText());
        }
        if (node.has("extent")) {
            JsonNode spatial = node.get("extent").get("spatial");
            CoordinateReferenceSystem crs = OgcApiUtils.parseCRS(spatial.get("crs").asText());

            double[] coords = new double[4];
            int i = 0;
            ArrayNode box = (ArrayNode) spatial.get("bbox");
            for (JsonNode c : box.get(0)) { // TODO - figure out how to pick the right
                // element of the array
                coords[i++] = c.asDouble();
            }

            ret.setExtent(new ReferencedEnvelope(coords[0], coords[2], coords[1], coords[3], crs));
        }

        if (node.has("crs")) {
            JsonNode crs = node.get("crs");
            for (JsonNode c : crs) {
                ret.crs.add(OgcApiUtils.parseCRS(c.asText()));
            }
        }
        if (node.has("styles")) {
            // read array of styles
            for (JsonNode s : node.get("styles")) {
                StyleType style = new StyleType();
                style.setIdentifier(s.get("id").asText());
                style.setTitle(s.get("title").asText());
                JsonNode links = s.get("links");
                for (JsonNode l : links) {
                    if ("stylesheet".equalsIgnoreCase(l.get("rel").asText())
                            && "application/vnd.ogc.sld+xml"
                                    .equalsIgnoreCase(l.get("type").asText())) {
                        style.setSld(new URL(l.get("href").asText()));
                    }
                }
                ret.styles.add(style);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "CollectionType [identifier="
                + identifier
                + ", title="
                + title
                + ", description="
                + description
                + ", styles="
                + styles
                + ", extent="
                + extent
                + "]";
    }
}
