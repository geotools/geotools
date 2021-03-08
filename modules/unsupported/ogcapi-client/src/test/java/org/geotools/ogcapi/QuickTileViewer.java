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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.Cursor;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.geojson.GeoJSONDataStore;
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
import org.geotools.util.URLs;
import org.geotools.util.UnsupportedImplementationException;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.SAXException;

public class QuickTileViewer {

    static final Logger LOGGER = Logging.getLogger(QuickTileViewer.class);

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

    JMapFrame frame;

    private LayerDialog layerDialog;

    public static void main(String[] args) throws Exception {

        String baseURL = "http://localhost:9090/geoserver/ogc/";

        new QuickTileViewer(baseURL);

        // viewer.addLayer("topp:states");
        // viewer.addLayer("zoomstack2:roads_national");
    }

    /** */
    public QuickTileViewer(String base) {
        try {
            init();

            setBaseURL(base);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    void addLayer(String identifier)
            throws IOException, SAXException, ParserConfigurationException {
        addLayer(identifier, "", APPLICATION_JSON);
    }

    void addLayer(String identifier, String style)
            throws IOException, SAXException, ParserConfigurationException {
        addLayer(identifier, style, APPLICATION_JSON);
    }

    void addLayer(String identifier, String style, String format)
            throws IOException, SAXException, ParserConfigurationException {
        CollectionType col = collections.collections.get(identifier);
        for (Link l : col.links) {
            if ("items".equalsIgnoreCase(l.rel) && format.equalsIgnoreCase(l.type)) {
                DataStore ds = null;
                URL url = new URL(l.href);
                File tempFile;
                switch (l.type) {
                    case APPLICATION_JSON:
                        ds = new GeoJSONDataStore(url);
                        break;
                    case APPLICATION_GEOPKG:
                        // Fetch file to temp disk
                        tempFile = File.createTempFile("TileViewer", ".gpkg");
                        tempFile.deleteOnExit();
                        try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                                FileOutputStream fos = new FileOutputStream(tempFile)) {
                            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                            Map<String, Object> params = new HashMap<>();
                            params.put(
                                    GeoPkgDataStoreFactory.DBTYPE.key,
                                    GeoPkgDataStoreFactory.DBTYPE.sample);
                            params.put(GeoPkgDataStoreFactory.DATABASE.key, tempFile);
                            params.put(GeoPkgDataStoreFactory.READ_ONLY.key, true);
                            ds = DataStoreFinder.getDataStore(params);
                        }
                        break;
                    case APPLICATION_MVT:
                        throw new UnsupportedImplementationException(
                                APPLICATION_MVT + " is not currently supported");
                    case APPLICATION_KML:
                        throw new UnsupportedImplementationException(
                                APPLICATION_KML + " is not currently supported");
                    case APPLICATION_GML_32:
                        throw new UnsupportedImplementationException(
                                APPLICATION_GML_32 + " is not currently supported");

                    default:
                        throw new RuntimeException("Unknown mime/type requested " + format);
                }
                String name = ds.getTypeNames()[0];
                SimpleFeatureSource featureSource = ds.getFeatureSource(name);
                Style sld = null;
                if (style == null || style.isEmpty()) {
                    sld = col.styles.get(0).sld;
                } else {
                    for (StyleType s : col.styles) {
                        if (s.getIdentifier().equalsIgnoreCase(style)) {
                            sld = s.sld;
                            break;
                        }
                    }
                }
                Layer layer = new FeatureLayer(featureSource, sld);
                map.addLayer(layer);
            }
        }
    }

    private void setBaseURL(String baseURL) throws Exception {
        Cursor cursor = frame.getCursor();
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new URL(baseURL);
        featureURL = new URL(baseURL + "features?f=json");
        new URL(baseURL + "tiles");
        FeaturesType contents = FeaturesType.fetchContents(featureURL);
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
        layerDialog.updateLayers(collections);
        frame.setCursor(cursor);
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
        URL f = this.getClass().getResource("data/110m_coastline.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(URLs.urlToFile(f));
        SimpleFeatureSource featureSource = store.getFeatureSource();
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        frame = new JMapFrame();
        frame.setSize(1000, 450);

        frame.setMapContent(map);
        frame.setTitle("API Test Viewer");
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("add.png"));
        layerDialog = new LayerDialog(this, "Select a layer");
        JButton button = new JButton(imageIcon);
        button.addActionListener(
                e -> {
                    layerDialog.updateLayers(collections);
                    layerDialog.setVisible(true);
                    String newLayer = layerDialog.getLayer();
                    if (newLayer != null && !newLayer.isEmpty()) {
                        SwingUtilities.invokeLater(
                                () -> {
                                    try {
                                        addLayer(newLayer);
                                    } catch (IOException
                                            | SAXException
                                            | ParserConfigurationException ex) {
                                        LOGGER.log(Level.WARNING, "", ex);
                                    }
                                });
                    }
                });
        frame.enableToolBar(true);
        frame.enableStatusBar(true);

        frame.getToolBar().add(button);

        frame.setVisible(true);
    }
}
