package org.geotools.ows.wmts.internal;

import java.io.File;
import java.net.URL;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.style.Style;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;

/**
 * Prompts the user for a shapefile and displays the contents on the screen in a map frame.
 *
 * <p>This is the GeoTools Quickstart application used in documentation and tutorials. *
 */
public class Quickstart {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its contents on the screen in
     * a map frame
     */
    public static void main(String[] args) throws Exception {
        MapContent map = new MapContent();

        map.setTitle("Quickstart");

        URL serverURL = new URL("http://astun-desktop:8080/geoserver/gwc/service/wmts?REQUEST=GetCapabilities");
        serverURL = new URL("http://spectrum.mapinfoservices.com/rest/Spatial/WMTS/1.0.0/WMTSCapabilities.xml");
        WebMapTileServer server = new WebMapTileServer(serverURL);
        String name = "USA_WMTS_Layer_ID1";
        WMTSLayer wlayer = server.getCapabilities().getLayer(name);
        // System.out.println(wlayer.getLatLonBoundingBox());
        WMTSMapLayer mapLayer = new WMTSMapLayer(server, wlayer);
        map.addLayer(mapLayer);

        File file = new File("/home/ian/Data/states/states.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        if (store != null) {
            SimpleFeatureSource featureSource = store.getFeatureSource();
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            Layer layer = new FeatureLayer(featureSource, style);
            map.addLayer(layer);
        }
        JMapFrame frame = new JMapFrame();
        frame.setSize(800, 450);
        // map.getViewport().setScreenArea(new Rectangle(800,400));
        // Now display the map
        frame.showMap(map);
    }
}
