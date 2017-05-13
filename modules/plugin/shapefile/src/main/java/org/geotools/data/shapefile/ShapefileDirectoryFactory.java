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
package org.geotools.data.shapefile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.directory.DirectoryDataStore;
import org.geotools.data.directory.FileStoreFactory;

/**
 * Creates a directory datastore pointing to a directory of shapefiles
 * 
 * @author Andrea Aime
 * 
 *
 *
 * @source $URL$
 *         main/java/org/geotools/data/dir/DirectoryDataStoreFactory.java $
 */
public class ShapefileDirectoryFactory extends AbstractDataStoreFactory implements FileStoreFactory  {
    /** The directory to be scanned for file data stores */
    public static final Param URLP = new Param("url", URL.class,
            "Directory containing geospatial files", true);
    
    ShapefileDataStoreFactory shpFactory;

    Map<String, Serializable> shpParams = new HashMap<String, Serializable>();
    
    public ShapefileDirectoryFactory() {
    	this(new ShapefileDataStoreFactory());
    }
    
    public ShapefileDirectoryFactory(ShapefileDataStoreFactory shpFactory) {
        this.shpFactory = shpFactory;
    }

    public String getDisplayName() {
        return "Directory of spatial files (shapefiles)";
    }

    public String getDescription() {
        return "Takes a directory of shapefiles and exposes it as a data store";
    }


	@Override
	public Param[] getParametersInfo() {
		Param[] params = shpFactory.getParametersInfo();
		for (int i = 0; i < params.length; i++) {
			if (params[i].key.equals(URLP.key)) {
				params[i] = URLP;
				break;
			}
		}
				
		return params;
	}
    
    
    public boolean canProcess(Map params) {
        
        if (super.canProcess(params)) {
            try {
                URL url = (URL) URLP.lookUp(params);
                File f = DataUtilities.urlToFile(url);
                return f != null && f.exists() && f.isDirectory();
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

	@Override
	public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
		URL url = lookup(URLP, params, URL.class);
        // are we creating a directory of shapefiles store, or a single one?
        File dir = DataUtilities.urlToFile(url);
        if (dir != null && dir.isDirectory()) {
        	shpParams = new HashMap<String, Serializable>(params);
        	shpParams.remove(URLP.key);
            return new DirectoryDataStore(DataUtilities.urlToFile(url), this);
        } else {
        	return null;
        }
	}

	@Override
	public DataStore createNewDataStore(Map<String, Serializable> params)
			throws IOException {
		return createDataStore(params);
	}
	

	
    public DataStore getDataStore(File file) throws IOException {
        final URL url = DataUtilities.fileToURL(file);
        if (shpFactory.canProcess(url)) {
            Map<String,Serializable> params = new HashMap<String,Serializable>(shpParams);
            params.put(URLP.key, url);
            return shpFactory.createDataStore(params);
        } else {
            return null;
        }
    }
    
    /**
     * Looks up a parameter, if not found it returns the default value, assuming there is one, or
     * null otherwise
     * 
     * @param <T>
     * @param param
     * @param params
     * @param target
     * @return
     * @throws IOException
     */
    <T> T lookup(Param param, Map<String, Serializable> params, Class<T> target) throws IOException {
        T result = (T) param.lookUp(params);
        if (result == null) {
            return (T) param.getDefaultValue();
        } else {
            return result;
        }

    }

}
