/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.tile.ServiceTest;
import org.geotools.tile.Tile;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.osm.OSMService;
import org.geotools.tile.impl.osm.OSMTile;
import org.geotools.tile.impl.osm.OSMTileIdentifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

public class TileViewer {

    private JMapFrame frame;

    public TileViewer(String shapeFilename) {

        // MapContent map = createMap(shapeFilename);
        MapContent map = createMap(shapeFilename);

        frame = new JMapFrame(map);

        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        // frame.enableTool(JMapFrame.Tool.ZOOM, JMapFrame.Tool.PAN,
        // JMapFrame.Tool.RESET);
        frame.enableToolBar(true);

        frame.setVisible(true);
    }

    private MapContent createMap(String shapeFilename) {

        final MapContent map = new MapContent();

        map.setTitle("TileLab");
        ReferencedEnvelope env = new ReferencedEnvelope(-180, 180, -80, 80,
                DefaultGeographicCRS.WGS84);

        env = new ReferencedEnvelope(5, 15, 45, 55, DefaultGeographicCRS.WGS84);

        // Will create the CRS
        ServiceTest.beforeClass();
        try {

            env = env.transform(ServiceTest.MERCATOR_CRS, true);
        } catch (TransformException | FactoryException e1) {
            e1.printStackTrace();
        }

        map.getViewport().setBounds(env);

        // String baseURL =
        // "http://ak.dynamic.t2.tiles.virtualearth.net/comp/ch/${code}?mkt=de-de&it=G,VE,BX,L,LA&shading=hill&og=78&n=z";
        // map.addLayer(new TileLayer(new BingService("Road", baseURL)));
        map.addLayer(new AsyncTileLayer(new OSMService("Mapnik",
                "http://tile.openstreetmap.org/")));

        // createTestCoverageLayer(map);

        if (shapeFilename != null && shapeFilename.endsWith(".shp")) {
            // addTestShape(map, shapeFilename);
        }

        return map;
    }

    private void createTestCoverageLayer(MapContent map) {

        // ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0),
        // ContrastMethod.NORMALIZE);
        // SelectedChannelType sct = sf.createSelectedChannelType("1", ce);
        // ChannelSelection sel = sf.channelSelection(sct);
        // sym.setChannelSelection(sel);

        String baseURL = "http://tile.openstreetmap.org/";
        TileService service = new OSMService("OSM", baseURL);

        Tile t = new OSMTile(new OSMTileIdentifier(38596, 49269,
                new WebMercatorZoomLevel(17), service.getName()), service);

        GridCoverageFactory gf = new GridCoverageFactory();

        ReferencedEnvelope env = t.getExtent();

        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();

        Style rasterStyle = SLD.wrapSymbolizers(sym);

        GridCoverage2D coverage = gf.create("Factory", t.getBufferedImage(),
                env);
        GridCoverageLayer gcl = new GridCoverageLayer(coverage, rasterStyle);

        // map.addLayer(gcl);

    }

    private void addTestShape(MapContent map, String shapeFilename) {
        try {
            File shpFile = new File(shapeFilename);
            FileDataStore dataStore;

            dataStore = FileDataStoreFinder.getDataStore(shpFile);

            SimpleFeatureSource shapefileSource = dataStore.getFeatureSource();
            Style shpStyle = SLD.createPolygonStyle(Color.BLUE, null, 0.50f);
            map.addLayer(new FeatureLayer(shapefileSource, shpStyle));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new TileViewer(args[0]);
    }
}
