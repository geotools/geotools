package org.geotools.ows.wmts.internal;

import java.io.File;
import java.net.URL;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 *
 * <p>This is the GeoTools Quickstart application used in documentation and tutorials. *
 */
public class Quickstart {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
     * contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {
        MapContent map = new MapContent();

        map.setTitle("Quickstart");

        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        ReferencedEnvelope env = new ReferencedEnvelope(-180, 180, -90, 90, crs);
        /*
         * crs = CRS.decode("epsg:3857"); env = env.transform(crs, true);
         */

        map.getViewport().setCoordinateReferenceSystem(crs);
        map.getViewport().setBounds(env);
        URL serverURL =
                new URL(
                        "http://astun-desktop:8080/geoserver/gwc/service/wmts?REQUEST=GetCapabilities");
        serverURL = new URL("http://raspberrypi:9000/wmts/1.0.0/WMTSCapabilities.xml");
        WebMapTileServer server = new WebMapTileServer(serverURL);
        String name = "topp:states";
        name = "osm";
        WMTSLayer wlayer = (WMTSLayer) server.getCapabilities().getLayer(name);
        // System.out.println(wlayer.getLatLonBoundingBox());
        WMTSMapLayer mapLayer = new WMTSMapLayer(server, wlayer);
        map.addLayer(mapLayer);
        // System.out.println(mapLayer.getBounds());
        File file = new File("/data/natural_earth/110m_physical/110m_coastline.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        JMapFrame frame = new JMapFrame();
        frame.setSize(800, 450);
        // map.getViewport().setScreenArea(new Rectangle(800,400));
        // Now display the map
        frame.showMap(map);
    }
}
