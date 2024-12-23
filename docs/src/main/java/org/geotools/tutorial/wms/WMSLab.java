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
package org.geotools.tutorial.wms;

import java.net.URL;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.geotools.map.MapContent;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.map.WMSLayer;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.wms.WMSChooser;
import org.geotools.swing.wms.WMSLayerChooser;

/** This is a Web Map Server "quickstart" doing the minimum required to display something on screen. */
public class WMSLab extends JFrame {
    /**
     * Prompts the user for a wms service, connects, and asks for a layer and then and displays its contents on the
     * screen in a map frame.
     */
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        URL capabilitiesURL = WMSChooser.showChooseWMS();
        if (capabilitiesURL == null) {
            System.exit(0); // canceled
        }
        WebMapServer wms = new WebMapServer(capabilitiesURL);

        List<Layer> wmsLayers = WMSLayerChooser.showSelectLayer(wms);
        if (wmsLayers == null) {
            JOptionPane.showMessageDialog(null, "Could not connect - check url");
            System.exit(0);
        }
        MapContent mapcontent = new MapContent();
        mapcontent.setTitle(wms.getCapabilities().getService().getTitle());

        for (Layer wmsLayer : wmsLayers) {
            WMSLayer displayLayer = new WMSLayer(wms, wmsLayer);
            mapcontent.addLayer(displayLayer);
        }
        // Now display the map
        JMapFrame.showMap(mapcontent);
    }
}
