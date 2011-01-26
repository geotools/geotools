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
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * The following code examples supplement those provided in the Quickstart class.
 * <p>
 * They are intended to answer some of the "bonus" questions and explore the finer points
 * of using a shape file.
 *
 * @source $URL$
 */
public class Quickstart2 {

    /**
     * GeoTools Quickstart demo application. Prompts the user for a shapefile
     * and displays its contents on the screen in a map frame
     */    
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put( ShapefileDataStoreFactory.URLP.key, file.toURI().toURL() );
        params.put( ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, false );
        params.put( ShapefileDataStoreFactory.MEMORY_MAPPED.key, false );
        params.put( ShapefileDataStoreFactory.DBFCHARSET.key, "ISO-8859-1" );
        
        DataStore store = DataStoreFinder.getDataStore( params );
        FeatureSource featureSource = store.getFeatureSource( store.getTypeNames()[0] );
        
        // Create a map context and add our shapefile to it
        MapContext map = new DefaultMapContext();
        map.addLayer(featureSource, null);

        // Now display the map
        JMapFrame.showMap(map);
    }
}
