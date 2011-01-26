/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.swing.JMapFrame;

/**
 * The following code examples supplement those provided in the Quickstart class.
 * <p>
 * They are intended to answer the question on how to connect to a web feature server.
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/demo/example/src/main/java/org/geotools/demo/Quickstart2
 *         .java $
 */
public class QuickstartWFS {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile and displays its
     * contents on the screen in a map frame
     */
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        String service = JOptionPane.showInputDialog("WFS Capabilities URL", "http://localhost:8080/geoserver/ows?service=WFS&request=GetCapabilities");
        if( service == null ){
            return;
        }
        URL url = new URL( service );

        Map connectionParameters = new HashMap();
        connectionParameters.put(WFSDataStoreFactory.URL.key, url);

        DataStore store = DataStoreFinder.getDataStore(connectionParameters);
        if( store == null ){
            System.out.println("Could not connect to "+url);
            return; // could not connect
        }
        Object[] selectionValues = store.getTypeNames();
            
        String typeName = (String) JOptionPane.showInputDialog(null,"Choose FeatureTypes", "WFS Quickstart", JOptionPane.QUESTION_MESSAGE, null, selectionValues, selectionValues[0]);
        if( typeName == null ){
            return; // user canceled
        }
        FeatureSource featureSource = store.getFeatureSource(typeName);

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.addLayer(featureSource, null);

        // Now display the map
        JMapFrame.showMap(map);
    }
}
