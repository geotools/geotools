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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.geojson.GeoJSONDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 *
 * <p>This is the GeoTools Quickstart application used in documentation and tutorials. *
 */
public class QuickTileViewer {

    public static final String APPLICATION_JSON = "application/json";
    private static JsonFactory factory = new JsonFactory();
    private URL featureURL;
    private CoordinateReferenceSystem crs;
    private CollectionsType collections;
    private MapContent map;

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
     * contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {
        QuickTileViewer viewer = new QuickTileViewer();
        viewer.init();
        String baseURL = "http://localhost:9090/geoserver/ogc/";
        viewer.setBaseURL(baseURL);
        viewer.addLayer("topp:states");
    }

    private void addLayer(String identifier) throws IOException {
        CollectionType col = collections.collections.get(identifier);
        for (Link l : col.links) {
            if ("items".equalsIgnoreCase(l.rel) && APPLICATION_JSON.equalsIgnoreCase(l.type)) {
                DataStore ds = new GeoJSONDataStore(new URL(l.href));
                String name = ds.getTypeNames()[0];
                SimpleFeatureSource featureSource = ds.getFeatureSource(name);
                Style style = col.styles.get(0).sld;
                Layer layer = new FeatureLayer(featureSource, style);
                map.addLayer(layer);
            }
        }
    }

    private void setBaseURL(String baseURL) throws Exception {
        new URL(baseURL);
        featureURL = new URL(baseURL + "features?f=json");
        new URL(baseURL + "tiles");
        FeaturesType contents = fetchContents();
        // System.out.println(contents);
        // fetch the TileMatrix sets
        ArrayList<TileMatrixSet> matrixSets = fetchMatrixSets(contents);
        for (TileMatrixSet tms : matrixSets) {
            if (CRS.equalsIgnoreMetadata(tms.getCoordinateReferenceSystem(), crs)) {
                fetchTileMatrixSet(crs, tms);
                break;
            }
        }
        // Fetch Feature Collections
        collections = fetchCollections(contents);
    }

    private CollectionsType fetchCollections(FeaturesType contents) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        for (Link link : contents.links) {
            if ("data".equalsIgnoreCase(link.rel) && APPLICATION_JSON.equalsIgnoreCase(link.type)) {
                URL url = new URL(link.href);
                return buildCollections(url);
            }
        }

        return null;
    }

    private CollectionsType buildCollections(URL url) throws Exception {
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
                                            buildRealCollection(new URL(link.get("href").asText()));
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

    private CollectionType buildRealCollection(URL url) throws IOException {
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
                return buildCollection(node);
            }
        }
        return null;
    }

    private CollectionType buildCollection(ObjectNode node) throws IOException {
        CollectionType ret = new CollectionType();

        if (node.has("links")) {
            JsonNode links = node.get("links");
            for (JsonNode link : links) {
                ret.links.add(buildLink((ObjectNode) link));
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

    private ArrayList<TileMatrix> fetchTileMatrixSet(
            CoordinateReferenceSystem crs2, TileMatrixSet tms)
            throws JsonProcessingException, IOException {
        for (Link l : tms.getLinks()) {
            if ("tileMatrixSet".equalsIgnoreCase(l.getRel())
                    && APPLICATION_JSON.equalsIgnoreCase(l.getType())) {
                return buildTileMatrixs(new URL(l.getHref()));
            }
        }
        return null;
    }

    private ArrayList<TileMatrix> buildTileMatrixs(URL url)
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

    private TileMatrix buildMatrix(ObjectNode node) {
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

    static GeometryFactory gf = new GeometryFactory();

    private ArrayList<TileMatrixSet> fetchMatrixSets(FeaturesType contents) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        for (Link link : contents.links) {
            if ("tiling-schemes".equalsIgnoreCase(link.rel)
                    && APPLICATION_JSON.equalsIgnoreCase(link.type)) {
                URL url = new URL(link.href);
                return buildTileMatrixSet(url);
            }
        }
        return null;
    }

    /**
     * @param url
     * @throws IOException
     * @throws JsonParseException
     */
    public ArrayList<TileMatrixSet> buildTileMatrixSet(URL url)
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
                        && "links".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException("Was expecting an array of links");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {

                        ObjectNode node = mapper.readTree(parser);

                        buildLink(node);
                    }
                }
                if (JsonToken.FIELD_NAME.equals(token)
                        && "tileMatrixSets".equalsIgnoreCase(parser.currentName())) {
                    token = parser.nextToken();
                    if (!JsonToken.START_ARRAY.equals(token)) {
                        throw new UnsupportedOperationException("Was expecting an array of links");
                    }
                    while (parser.nextToken() == JsonToken.START_OBJECT) {

                        ObjectNode node = mapper.readTree(parser);

                        ret.add(buildMatrixSet(node));
                    }
                }
            }
            return ret;
        }
    }

    private FeaturesType fetchContents() throws JsonParseException, IOException {
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

                        ret.links.add(buildLink(node));
                    }
                }
            }
            return ret;
        }
    }

    private TileMatrixSet buildMatrixSet(ObjectNode node) {
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
                tileMatrix.getLinks().add(buildLink((ObjectNode) l));
            }
        }
        return tileMatrix;
    }

    private Link buildLink(ObjectNode node) {
        Link link = new Link();
        link.href = node.get("href").asText();
        link.type = node.get("type").asText();
        if (node.has("title")) {
            link.title = node.get("title").asText();
        }
        if (node.has("rel")) {
            link.rel = node.get("rel").asText();
        }

        return link;
    }

    /**
     * @throws MalformedURLException
     * @throws IOException
     */
    public void init() throws IOException {
        map = new MapContent();

        map.setTitle("Quickstart");

        crs = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope env = new ReferencedEnvelope(-180, 180, -90, 90, crs);

        map.getViewport().setCoordinateReferenceSystem(crs);
        map.getViewport().setBounds(env);

        File file = new File("/data/natural_earth/110m_physical/110m_coastline.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        JMapFrame frame = new JMapFrame();
        frame.setSize(1000, 450);
        // map.getViewport().setScreenArea(new Rectangle(800,400));
        // Now display the map
        frame.setMapContent(map);
        frame.setTitle("API Test Viewer");
        frame.enableToolBar(true);
        frame.enableStatusBar(true);
        frame.setVisible(true);
    }
}
