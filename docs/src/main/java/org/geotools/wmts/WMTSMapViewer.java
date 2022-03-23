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
package org.geotools.wmts;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.geotools.map.MapContent;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.swing.JMapFrame;

/**
 * Displays one layer of a WMTS server within a map.
 *
 * <p>The available WMTS server's are given by the array WMTS_SERVERS.
 *
 * @author Roar Brænden
 */
public class WMTSMapViewer {
    private static final String[] WMTS_SERVERS =
            new String[] {
                "https://geodata.npolar.no/arcgis/rest/services/Basisdata/NP_Ortofoto_Svalbard_WMTS_25833/MapServer/WMTS/1.0.0/WMTSCapabilities.xml",
                "https://geodata.npolar.no/arcgis/rest/services/Basisdata/NP_Basiskart_Svalbard_WMTS_25833/MapServer/WMTS/1.0.0/WMTSCapabilities.xml"
            };

    public static void main(String[] args) throws Exception {
        URL serverURL = showChooseWMTS();

        // start wmtsMapViewer example
        WebMapTileServer server = new WebMapTileServer(serverURL);
        List<WMTSLayer> layers = server.getCapabilities().getLayerList();

        WMTSLayer chosenLayer = showChooseWMTSLayer(layers);

        MapContent map = new MapContent();
        map.setTitle(server.getCapabilities().getService().getTitle());

        WMTSMapLayer mapLayer = new WMTSMapLayer(server, chosenLayer);
        map.addLayer(mapLayer);

        JMapFrame.showMap(map);
        // end wmtsMapViewer example

    }

    private static URL showChooseWMTS() {
        JComboBox<String> combo = new JComboBox<>(WMTS_SERVERS);
        combo.setEditable(true);

        Object[] message = {"Choose a WMTS Server", combo};
        do {
            int done =
                    JOptionPane.showConfirmDialog(
                            null,
                            message,
                            "Web Map Tile Server",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
            if (done == JOptionPane.CANCEL_OPTION) {
                return null;
            }
            Object input = combo.getSelectedItem();
            try {
                return new URL((String) input);
            } catch (MalformedURLException t) {
                message = new Object[] {"Choose a WMTS Service", combo, t.getMessage()};
            }
        } while (true);
    }

    private static WMTSLayer showChooseWMTSLayer(List<WMTSLayer> layers) {
        String[] names = new String[layers.size()];
        for (int i = 0; i < layers.size(); i++) {
            names[i] = layers.get(i).getName();
        }
        JComboBox<String> combo = new JComboBox<>(names);
        int done =
                JOptionPane.showConfirmDialog(
                        null,
                        new Object[] {"Choose a WMTS Layer", combo},
                        "Layer",
                        JOptionPane.OK_CANCEL_OPTION);
        if (done == JOptionPane.CANCEL_OPTION) {
            return null;
        }
        return layers.get(combo.getSelectedIndex());
    }
}
