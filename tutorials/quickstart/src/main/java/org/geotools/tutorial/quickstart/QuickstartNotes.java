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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.util.logging.Logging;
import org.geotools.swing.data.JFileDataStoreChooser;

/**
 * Additional code examples used by the quickstart tutorial; mostly for the things to try section at
 * the end.
 */
class QuickstartNotes {
    
    private static Logger LOG = Logging.getLogger(QuickstartNotes.class);
    
    public void snipetDataStoreFinder() throws Exception {
        // start datastore
        File file = JFileDataStoreChooser.showOpenFile("shp", null);

        Map<String, Object> params = new HashMap<>();
        params.put("url", file.toURI().toURL());
        params.put("create spatial index", false);
        params.put("memory mapped buffer", false);
        params.put("charset", "ISO-8859-1");

        DataStore store = DataStoreFinder.getDataStore(params);
        SimpleFeatureSource featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        // end datastore
        
        LOG.info("Connected to:"+featureSource);
    }
}
