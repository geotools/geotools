/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
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

/**
 * This is a Web Map Server "quickstart" doing the minimum required to display something on screen.
 */
public class WMSLab extends JFrame {
    /**
     * Prompts the user for a wms service, connects, and asks for a layer and then and displays its
     * contents on the screen in a map frame.
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
