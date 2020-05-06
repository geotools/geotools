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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class FeaturesType {
    String title;
    String description;
    ArrayList<Link> links = new ArrayList<>();

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

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public static FeaturesType fetchContents(URL featureURL)
            throws JsonParseException, IOException {
        FeaturesType ret = new FeaturesType();
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        try (JsonParser parser = factory.createParser(featureURL)) {
            JsonToken token = parser.nextToken(); // start 1st object

            while (!parser.isClosed()) {
                // We can now see title, description and links array
                token = parser.nextToken();
                if (token == null) {
                    break;
                }
                if (JsonToken.FIELD_NAME.equals(token)
                        && "title".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    ret.setTitle(parser.getValueAsString());
                }
                if (JsonToken.FIELD_NAME.equals(token)
                        && "description".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    ret.setDescription(parser.getValueAsString());
                }
                if (JsonToken.FIELD_NAME.equals(token)
                        && "links".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException("Was expecting an array of links");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {

                        ObjectNode node = mapper.readTree(parser);

                        ret.links.add(Link.buildLink(node));
                    }
                }
            }
            return ret;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OGCAPI Features:\n")
                .append("\tTitle: " + title + "\n")
                .append("\tDesc: " + description + "\n");
        sb.append("\tLinks:\n");
        for (Link l : links) {
            sb.append("\t\t" + l.toString()).append("\n");
        }
        return sb.toString();
    }
}
