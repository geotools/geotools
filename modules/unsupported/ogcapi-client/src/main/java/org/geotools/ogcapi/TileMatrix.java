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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class TileMatrix extends org.geotools.ows.wmts.model.TileMatrix {
    static JsonFactory factory = new JsonFactory();
    static GeometryFactory gf = new GeometryFactory();
    private String title;
    private CoordinateReferenceSystem crs;
    private String supportedCRS;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSupportedCRS(String supportedCRS) {
        this.supportedCRS = supportedCRS;
        this.crs = OgcApiUtils.parseCRS(supportedCRS);
    }

    public String getSupportedCRS() {
        return supportedCRS;
    }

    public static ArrayList<TileMatrix> buildTileMatrixList(URL url)
            throws JsonProcessingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        try (JsonParser parser = factory.createParser(url)) {
            JsonToken token = parser.nextToken(); // start 1st object
            ArrayList<TileMatrix> ret = new ArrayList<>();
            while (!parser.isClosed()) {
                // We can now see title, identifier, tileMatrix array and links array
                token = parser.nextToken();
                if (token == null) {
                    break;
                }

                if (JsonToken.FIELD_NAME.equals(token)
                        && "tileMatrix".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException("Was expecting an array of links");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {

                        ObjectNode node = mapper.readTree(parser);

                        ret.add(buildMatrix(node));
                    }
                }
            }
            return ret;
        }
    }

    private static TileMatrix buildMatrix(ObjectNode node) {
        TileMatrix tileMatrix = new TileMatrix();

        tileMatrix.setIdentifier(node.get("identifier").asText());

        if (node.has("scaleDenominator")) {
            tileMatrix.setDenominator(node.get("scaleDenominator").asDouble());
        }
        if (node.has("topLeftCorner")) {
            double[] coords = new double[2];
            JsonNode tlCorner = node.get("topLeftCorner");
            int i = 0;
            for (JsonNode c : tlCorner) {
                coords[i++] = c.asDouble();
            }
            tileMatrix.setTopLeft(gf.createPoint(new Coordinate(coords[0], coords[1])));
        }
        if (node.has("tileWidth")) {
            tileMatrix.setTileWidth(node.get("tileWidth").asInt());
        }
        if (node.has("tileHeight")) {
            tileMatrix.setTileHeight(node.get("tileHeight").asInt());
        }
        if (node.has("matrixWidth")) {
            tileMatrix.setMatrixWidth(node.get("matrixWidth").asInt());
        }
        if (node.has("matrixHeight")) {
            tileMatrix.setMatrixHeight(node.get("matrixHeight").asInt());
        }
        return tileMatrix;
    }

    @Override
    public CoordinateReferenceSystem getCrs() {
        return crs;
    }
}
