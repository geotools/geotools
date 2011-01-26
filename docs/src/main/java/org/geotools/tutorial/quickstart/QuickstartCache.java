package org.geotools.tutorial.quickstart;

import java.io.File;

import org.geotools.data.CachingFeatureSource;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
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

        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.setTitle("Using cached features");
        map.addLayer(cache, null);

        // Now display the map
        JMapFrame.showMap(map);
    }
    // docs end cache
}
