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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.geojson.GeoJSONDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.wfs.GML.Version;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.SAXException;

/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 *
 * <p>This is the GeoTools Quickstart application used in documentation and tutorials. *
 */
public class QuickTileViewer {

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_MVT = "application/vnd.mapbox-vector-tile";
    public static final String APPLICATION_GML_32 = "application/gml+xml;version=3.2";
    public static final String APPLICATION_KML = "application/vnd.google-earth.kml+xml";
    public static final String APPLICATION_GEOPKG = "application/x-gpkg";

    static JsonFactory factory = new JsonFactory();
    private URL featureURL;
    private CoordinateReferenceSystem crs;
    private CollectionsType collections;
    private MapContent map;
    private JMapFrame frame;
    private JList<String> list;

    public static void main(String[] args) throws Exception {
        QuickTileViewer viewer = new QuickTileViewer();
        viewer.init();
        String baseURL = "http://localhost:9090/geoserver/ogc/";
        viewer.setBaseURL(baseURL);
        viewer.addLayer("topp:states", APPLICATION_GML_32);
        // viewer.addLayer("zoomstack2:roads_national");
    }

    void addLayer(String identifier)
            throws IOException, SAXException, ParserConfigurationException {
        addLayer(identifier, APPLICATION_JSON);
    }

    void addLayer(String identifier, String format)
            throws IOException, SAXException, ParserConfigurationException {
        CollectionType col = collections.collections.get(identifier);
        for (Link l : col.links) {
            if ("items".equalsIgnoreCase(l.rel) && format.equalsIgnoreCase(l.type)) {
                DataStore ds = null;
                URL url = new URL(l.href);
                switch (l.type) {
                    case APPLICATION_JSON:
                        ds = new GeoJSONDataStore(url);
                        break;
                    case APPLICATION_GEOPKG:
                        // Fetch file to temp disk
                        File tempFile = File.createTempFile("TileViewer", "gpkg");
                        tempFile.deleteOnExit();
                        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                                FileOutputStream fos = new FileOutputStream(tempFile)) {
                            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put(
                                    GeoPkgDataStoreFactory.DBTYPE.key,
                                    GeoPkgDataStoreFactory.DBTYPE.sample);
                            params.put(GeoPkgDataStoreFactory.DATABASE.key, tempFile);
                            params.put(GeoPkgDataStoreFactory.READ_ONLY.key, true);
                            ds = DataStoreFinder.getDataStore(params);
                        }
                        break;
                    case APPLICATION_GML_32:
                        // Grab the file and parse,

                        org.geotools.wfs.GML gml = new org.geotools.wfs.GML(Version.WFS1_1);
                        SimpleFeatureCollection features =
                                gml.decodeFeatureCollection(url.openStream());
                        ds = DataUtilities.dataStore(features);
                        break;
                    default:
                        throw new RuntimeException("Unknown mime/type requested " + format);
                }
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
        // list.setListData((String[])collections.collections.keySet().toArray(new String[] {}));
    }

    private CollectionsType fetchCollections(FeaturesType contents) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        for (Link link : contents.links) {
            if ("data".equalsIgnoreCase(link.rel) && APPLICATION_JSON.equalsIgnoreCase(link.type)) {
                URL url = new URL(link.href);
                return CollectionsType.buildCollections(url);
            }
        }

        return null;
    }

    private ArrayList<TileMatrix> fetchTileMatrixSet(
            CoordinateReferenceSystem crs2, TileMatrixSet tms)
            throws JsonProcessingException, IOException {
        for (Link l : tms.getLinks()) {
            if ("tileMatrixSet".equalsIgnoreCase(l.getRel())
                    && APPLICATION_JSON.equalsIgnoreCase(l.getType())) {
                return TileMatrix.buildTileMatrixList(new URL(l.getHref()));
            }
        }
        return null;
    }

    private ArrayList<TileMatrixSet> fetchMatrixSets(FeaturesType contents) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());
        for (Link link : contents.links) {
            if ("tiling-schemes".equalsIgnoreCase(link.rel)
                    && APPLICATION_JSON.equalsIgnoreCase(link.type)) {
                URL url = new URL(link.href);
                return TileMatrixSet.buildTileMatrixSetList(url);
            }
        }
        return null;
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

                        ret.links.add(Link.buildLink(node));
                    }
                }
            }
            return ret;
        }
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
        frame = new JMapFrame();
        frame.setSize(1000, 450);
        // map.getViewport().setScreenArea(new Rectangle(800,400));
        // Now display the map
        frame.setMapContent(map);
        frame.setTitle("API Test Viewer");
        frame.enableToolBar(true);
        frame.enableStatusBar(true);

        list = new JList<>();
        JSplitPane splitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, list, frame.getMapPane());
        frame.add(splitPane);
        frame.setVisible(true);
    }
}
