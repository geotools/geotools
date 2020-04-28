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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class TileMatrixSet extends org.geotools.ows.wmts.model.TileMatrixSet {
    static JsonFactory factory = new JsonFactory();
    private String title;

    private String supportedCRS;
    private ArrayList<Link> links = new ArrayList<>();

    /**
     * Get the links list
     *
     * @return the list of links
     */
    public ArrayList<Link> getLinks() {
        return links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSupportedCRS(String supportedCRS) {
        this.supportedCRS = supportedCRS;
    }
    /**
     * @param url
     * @throws IOException
     * @throws JsonParseException
     */
    public static ArrayList<TileMatrixSet> buildTileMatrixSetList(URL url)
            throws IOException, JsonParseException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());

        try (JsonParser parser = factory.createParser(url)) {
            JsonToken token = parser.nextToken(); // start 1st object
            ArrayList<TileMatrixSet> ret = new ArrayList<>();
            while (!parser.isClosed()) {
                // We can now see title, description and links array
                token = parser.nextToken();
                if (token == null) {
                    break;
                }

                if (JsonToken.FIELD_NAME.equals(token)
                        && "tileMatrixSets".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException("Was expecting an array of links");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {

                        ObjectNode node = mapper.readTree(parser);

                        ret.add(TileMatrixSet.buildMatrixSet(node));
                    }
                }
            }
            return ret;
        }
    }

    static TileMatrixSet buildMatrixSet(ObjectNode node) {
        TileMatrixSet tileMatrix = new TileMatrixSet();
        // System.out.println(node);
        tileMatrix.setIdentifier(node.get("identifier").asText());

        if (node.has("title")) {
            tileMatrix.setTitle(node.get("title").asText());
        }
        if (node.has("supportedCRS")) {
            tileMatrix.setSupportedCRS(node.get("supportedCRS").asText());
        }
        if (node.has("links")) {
            for (JsonNode l : node.get("links")) {
                tileMatrix.getLinks().add(Link.buildLink((ObjectNode) l));
            }
        }
        return tileMatrix;
    }

    @Override
    public String toString() {

        String name;
        if (getCoordinateReferenceSystem() != null) {
            name = getCoordinateReferenceSystem().getName().toString();
        } else {
            name = supportedCRS;
        }

        return getIdentifier() + ", " + name + ", " + title;
    }
}
