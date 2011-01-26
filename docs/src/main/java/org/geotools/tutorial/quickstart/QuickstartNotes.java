package org.geotools.tutorial.quickstart;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * Additional code examples used by the quickstart tutorial; mostly for the things to try
 * section at the end.
 */
class QuickstartNotes {

    public void snipetDataStoreFinder() throws Exception {
        // start datastore
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        
        Map<String,Object> params = new HashMap<String,Object>();
        params.put( "url", file.toURI().toURL() );
        params.put( "create spatial index", false );
        params.put( "memory mapped buffer", false );
        params.put( "charset", "ISO-8859-1" );
        
        DataStore store = DataStoreFinder.getDataStore( params );
        SimpleFeatureSource featureSource = store.getFeatureSource( store.getTypeNames()[0] );
        // end datastore
    }
}