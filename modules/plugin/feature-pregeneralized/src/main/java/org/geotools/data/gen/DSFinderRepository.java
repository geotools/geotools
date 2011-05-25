/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.data.gen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Repository;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

/**
 * Implementation of {@link Repository} This class interprets the data source name as a file name or
 * an URL for a property file containing the ds creation parameters
 * 
 * For shape files ending with .shp or SHP, the shape file could be passed as name
 * 
 * 
 * @author Christian Mueller
 * 
 *
 *
 * @source $URL$
 */
public class DSFinderRepository implements Repository {

    Map<String, DataStore> map = new HashMap<String, DataStore>();

    Logger log = Logging.getLogger(this.getClass());

    public void clear() {
        map = new HashMap<String, DataStore>();
    }

    protected URL getURLForLocation(String location) throws IOException {
        URL url = null;
        File f = new File(location);
        if (f.exists()) {
            url = f.toURI().toURL();
        } else {
            url = new URL(location);
        }

        url = new URL(URLDecoder.decode(url.toExternalForm(), "UTF8"));
        return url;
    }

    private Map<String, Serializable> getMapForShapeFile(URL shapeFileURL) throws IOException {
        Map<String, Serializable> result = new HashMap<String, Serializable>();
        result.put(ShapefileDataStoreFactory.URLP.key, shapeFileURL);
        return result;
    }

    private Map<String, Serializable> getMapFromPropetryLocation(String location)
            throws IOException {

        URL url = getURLForLocation(location);

        // for convenience, handle shape files in a short way
        if (location.endsWith(".shp") || location.endsWith(".SHP"))
            return getMapForShapeFile(url);

        Map<String, Serializable> result = new HashMap<String, Serializable>();

        Properties properties = new Properties();
        InputStream in = url.openStream();
        properties.load(in);
        for (Object key : properties.keySet()) {
            result.put((String) key, (Serializable) properties.get(key));
        }
        in.close();
        return result;
    }

    public void initialize(Object source) {
        clear();
    }

    public DataAccess<?, ?> access(Name name) {
        return dataStore(name);
    }

    public DataStore dataStore(Name name) {
        String localName = name.getLocalPart();
        DataStore ds = map.get(localName);
        if (ds != null)
            return ds;

        try {
            Map<String, Serializable> params = getMapFromPropetryLocation(localName);
            ds = DataStoreFinder.getDataStore(params);
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
        map.put(localName, ds);
        return ds;
    }

    
    /* (non-Javadoc)
     * @see org.geotools.data.Repository#getDataStores()
     * 
     * These datastores are for internal use only 
     */
    public List<DataStore> getDataStores(){
        return Collections.emptyList();
//        List<DataStore> available = new ArrayList<DataStore>( this.map.values() );
//        return available;
    }
}
