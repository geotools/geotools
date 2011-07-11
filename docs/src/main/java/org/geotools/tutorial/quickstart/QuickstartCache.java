package org.geotools.tutorial.quickstart;

import java.io.File;

import org.geotools.data.CachingFeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * Example used in Quickstart workbook showing how to use a CachingFeatureSource.
 * <p>
 * This is the GeoTools Quickstart application used in documentationa and tutorials. *
 */
public class QuickstartCache {

    // docs start cache
    /**
     * This method demonstrates using a memory-based cache to speed up the display (e.g. when
     * zooming in and out).
     * 
     * There is just one line extra compared to the main method, where we create an instance of
     * CachingFeatureStore.
     */
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        CachingFeatureSource cache = new CachingFeatureSource(featureSource);

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("Using cached features");
        Layer layer = new FeatureLayer(cache, null);
        map.addLayer(layer);

        // Now display the map
        JMapFrame.showMap(map);
    }
    // docs end cache
}
