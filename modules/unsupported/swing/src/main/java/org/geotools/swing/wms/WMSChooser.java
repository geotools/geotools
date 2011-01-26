package org.geotools.swing.wms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class WMSChooser {
    public static URL showChooseWMS() {
        return showChooseWMS(deafultServers());
    }

    /**
     * Prompt for a URL to a Web Map Server, providing a list of recommended options.
     * 
     * @param args
     * @return
     * @throws MalformedURLException
     */
    public static URL showChooseWMS(List<String> servers) {
        if (servers == null) {
            servers = deafultServers();
        }
        JComboBox combo = new JComboBox(servers.toArray());
        combo.setEditable(true);

        Object message[] = new Object[] { "Choose a WMS Server", combo };
        do {
            int done = JOptionPane.showConfirmDialog(null, message, "Web Map Server",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (done == JOptionPane.CANCEL_OPTION) {
                return null;
            }
            Object input = combo.getSelectedItem();
            try {
                return new URL((String) input);
            } catch (Throwable t) {
                message = new Object[] { "Choose a WMS Service", combo, t.getMessage() };
            }
        } while (true);
    }

    private static List<String> deafultServers() {
        List<String> servers = new ArrayList<String>();
        servers
                .add("http://wms.jpl.nasa.gov/wms.cgi?Service=WMS&Version=1.1.1&Request=GetCapabilities");
        servers.add("http://localhost:8080/geoserver/wms?service=WMS&request=GetCapabilities");
        servers
                .add("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?Service=WMS&VERSION=1.1.0&REQUEST=GetCapabilities");
        servers
                .add("http://giswebservices.massgis.state.ma.us/geoserver/wms?service=WMS&request=GetCapabilities");
        servers
                .add("http://wms.cits.rncan.gc.ca/cgi-bin/cubeserv.cgi?VERSION=1.1.0&REQUEST=GetCapabilities");
        servers
                .add("http://atlas.gc.ca/cgi-bin/atlaswms_en?VERSION=1.1.1&Request=GetCapabilities&Service=WMS");

        return servers;
    }
}
