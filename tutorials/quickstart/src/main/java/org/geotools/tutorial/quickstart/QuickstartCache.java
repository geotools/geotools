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
package org.geotools.tutorial.quickstart;

import java.io.File;
import org.geotools.data.DataUtilities;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.data.collection.SpatialIndexFeatureCollection;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.api.style.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * Example used in Quickstart workbook showing how to use a CachingFeatureSource.
 *
 * <p>This is the GeoTools Quickstart application used in documentationa and tutorials. *
 */
public class QuickstartCache {

    // docs start cache
    /**
     * This method demonstrates using a memory-based cache to speed up the display (e.g. when
     * zooming in and out).
     *
     * <p>There is just one line extra compared to the main method, where we create an instance of
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
        SimpleFeatureSource cachedSource =
                DataUtilities.source(
                        new SpatialIndexFeatureCollection(featureSource.getFeatures()));

        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("Using cached features");
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(cachedSource, style);
        map.addLayer(layer);

        // Now display the map
        JMapFrame.showMap(map);
    }
    // docs end cache
}
