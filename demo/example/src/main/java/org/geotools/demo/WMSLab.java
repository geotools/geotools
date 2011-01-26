package org.geotools.demo;

import java.net.URL;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.map.WMSMapLayer;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.wms.WMSChooser;
import org.geotools.swing.wms.WMSLayerChooser;

/**
 * This is a Web Map Server "quickstart" doing the minimum required to display
 * something on screen.
 */
@SuppressWarnings("serial")
public class WMSLab extends JFrame {
    /**
     * Prompts the user for a wms service, connects, and asks for a layer and then
     * and displays its contents on the screen in a map frame.
     */
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        URL capabilitiesURL = WMSChooser.showChooseWMS();
        if( capabilitiesURL == null ){
            System.exit(0); // canceled
        }
        WebMapServer wms = new WebMapServer( capabilitiesURL );        
        
        List<Layer> wmsLayers = WMSLayerChooser.showSelectLayer( wms );
        if( wmsLayers == null ){
            JOptionPane.showMessageDialog(null, "Could not connect - check url");
            System.exit(0);
        }
        MapContext mapcontext = new DefaultMapContext();
        mapcontext.setTitle( wms.getCapabilities().getService().getTitle() );
        
        for( Layer wmsLayer : wmsLayers ){
            WMSMapLayer displayLayer = new WMSMapLayer(wms, wmsLayer );
            mapcontext.addLayer( displayLayer );
        }
        // Now display the map
        JMapFrame.showMap(mapcontext);
    }
}