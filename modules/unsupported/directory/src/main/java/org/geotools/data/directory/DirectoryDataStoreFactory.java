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
package org.geotools.data.directory;

import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;

/**
 * Creates a Directory DataStore
 * 
 * @author Andrea Aime
 * 
 * @see DataStoreFactorySpi
 * @source $URL:
 *         http://svn.geotools.org/trunk/modules/unsupported/directory/src/
 *         main/java/org/geotools/data/dir/DirectoryDataStoreFactory.java $
 */
public class DirectoryDataStoreFactory implements DataStoreFactorySpi {
    /** The directory to be scanned for file data stores */
    public static final Param URLP = new Param("url",
            URL.class, "Directory containing geospatial files", true);

    public static final Param NAMESPACE = new Param("namespace", URI.class,
            "uri to a the namespace", false); // not required

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        if (!canProcess(params)) {
            throw new IOException("Invalid parameters for DirectoryDataStore");
        }

        URL url = (URL) URLP.lookUp(params);
        File f = DataUtilities.urlToFile(url);
        
        if (!f.isDirectory()) {
            throw new IOException("Invalid parameter " + URLP.key
                    + " : is not a valid directory");
        }

        URI namespace = (URI) NAMESPACE.lookUp(params);
        return new DirectoryDataStore(f, namespace);
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        if (!canProcess(params)) {
            throw new IOException("Invalid parameters for DirectoryDataStore");
        }

        return createDataStore(params);
    }

    public String getDisplayName() {
        return "Directory of spatial files";
    }

    public String getDescription() {
        return "Takes a directory of spatial data files and exposes it as a data store";
    }

    public Param[] getParametersInfo() {
        return new Param[] { URLP, NAMESPACE };
    }

    public boolean canProcess(Map<String, Serializable> params) {
        try {
            URL url = (URL) URLP.lookUp(params);
        	File f = DataUtilities.urlToFile(url);
            return f != null && f.exists() && f.isDirectory();
        } catch (Exception e) {
            return false;
        }
    }
    
    

    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns the implementation hints. The default implementation returns en
     * empty map.
     */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
