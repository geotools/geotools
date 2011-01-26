// docs start source
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

import java.io.File;

import org.geotools.data.CachingFeatureSource;
import org.geotools.data.FeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * GeoTools Quickstart demo application. Prompts the user for a shapefile
 * and displays its contents on the screen in a map frame
 *
 * @source $URL$
 */
public class Quickstart {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile
     * and displays its contents on the screen in a map frame
     */    
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource featureSource = store.getFeatureSource();

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("Quickstart");
        map.addLayer(featureSource, null);

        // Now display the map
        JMapFrame.showMap(map);
    }
// docs end main

// docs start cache
    /**
     * This method demonstrates using a memory-based cache to speed up
     * the display (e.g. when zooming in and out).
     *
     * There is just one line extra compared to the main method, where
     * we create an instance of CachingFeatureStore.
     *
     * @throws Exception
     */
    public void usingFeatureCaching() throws Exception {
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        FeatureSource featureSource = store.getFeatureSource();

        CachingFeatureSource cache = new CachingFeatureSource(featureSource);

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("Using cached features");
        map.addLayer(cache, null);

        // Now display the map
        JMapFrame.showMap(map);
    }
}
// docs end source