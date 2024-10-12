/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.tile;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.bing.BingService;
import org.geotools.tile.impl.osm.OSMService;
import org.geotools.tile.impl.osm.OSMTileFactory;
import org.geotools.tile.impl.osm.OSMTileIdentifier;
import org.geotools.tile.util.AsyncTileLayer;
import org.geotools.tile.util.TileLayer;
import org.geotools.util.SuppressFBWarnings;

@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
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
        ReferencedEnvelope env = new ReferencedEnvelope(-180, 180, -80, 80, DefaultGeographicCRS.WGS84);

        env = new ReferencedEnvelope(5, 15, 45, 55, DefaultGeographicCRS.WGS84);

        // Will create the CRS
        try {
            env = env.transform(CRS.decode("EPSG:3857"), true);
        } catch (TransformException | FactoryException e1) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e1);
        }

        map.getViewport().setBounds(env);

        String baseURL =
                "http://ak.dynamic.t2.tiles.virtualearth.net/comp/ch/${code}?mkt=de-de&it=G,VE,BX,L,LA&shading=hill&og=78&n=z";
        map.addLayer(new TileLayer(new BingService("Road", baseURL)));
        map.addLayer(new AsyncTileLayer(new OSMService("Mapnik", "http://tile.openstreetmap.org/")));

        /*
         * String baseURL =
         * "http://raspberrypi:9000/wmts/1.0.0/WMTSCapabilities.xml";
         * TileService service = new WMTSService("states", baseURL, "states",
         * "webmercator",WMTSServiceType.REST);
         */

        /*
         * String baseURL =
         * "http://raspberrypi:9000/service?REQUEST=GetCapabilities&SERVICE=WMTS";
         * TileService service = new WMTSService("states", baseURL, "states",
         * "webmercator",WMTSServiceType.KVP);
         */

        // String baseURL =
        // "http://raspberrypi:8080/geoserver/gwc/service/wmts?REQUEST=GetCapabilities";
        // TileService service = new WMTSService("states", baseURL,
        // "topp:states", "EPSG:900913",WMTSServiceType.KVP, null);
        // map.addLayer(new AsyncTileLayer(service));
        createTestCoverageLayer(map);

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
        TileIdentifier identifier =
                new OSMTileIdentifier(38596, 49269, new WebMercatorZoomLevel(17), service.getName());

        Tile t = new OSMTileFactory().create(identifier, service);

        GridCoverageFactory gf = new GridCoverageFactory();

        ReferencedEnvelope env = t.getExtent();

        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();

        Style rasterStyle = SLD.wrapSymbolizers(sym);

        GridCoverage2D coverage = gf.create("Factory", t.getBufferedImage(), env);
        GridCoverageLayer gcl = new GridCoverageLayer(coverage, rasterStyle);

        // map.addLayer(gcl);

    }

    private void addTestShape(MapContent map, String shapeFilename) {
        try {
            File shpFile = new File(shapeFilename);

            FileDataStore dataStore = FileDataStoreFinder.getDataStore(shpFile);

            SimpleFeatureSource shapefileSource = dataStore.getFeatureSource();
            Style shpStyle = SLD.createPolygonStyle(Color.BLUE, null, 0.50f);
            map.addLayer(new FeatureLayer(shapefileSource, shpStyle));
        } catch (IOException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            new TileViewer(args[0]);
        } else {
            new TileViewer(null);
        }
    }
}
