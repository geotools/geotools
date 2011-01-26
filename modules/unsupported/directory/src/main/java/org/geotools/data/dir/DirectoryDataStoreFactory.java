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
package org.geotools.data.dir;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;


/**
 * Creates a Directory DataStore following the DataStoreFactorySpi interface.
 * 
 * @author David Zwiers, Refractions Research, Inc.
 *
 * @see DataStoreFactorySpi
 * @source $URL$
 * @deprecated Use {@link org.geotools.data.directory.DirectoryDataStoreFactory} instead
 */
public class DirectoryDataStoreFactory implements DataStoreFactorySpi {
    /** The Directory parameter which should contain some files to read */
    public static final Param DIRECTORY = new Param("data_directory url", URL.class,
            "Directory containing geospatial vector files", true); 

    /**
     * The suffix parameter to specify the order of creation for new
     * featureTypes
     */
    public static final Param CREATE_SUFFIX_ORDER = new Param("suffix_list",
            String[].class,
            "space delimited list of prefixes to attempt and create new files for.",
            true) {
    	
    	/**
    	 * Overide text to unwap the array of strings back into a space 
    	 * limited text string.
    	 * TODO: (JD) perhaps this should be done by param? I guess we would have to 
    	 * agree on the delimiter for collection or array based parameters.
    	 */
    	public String text(Object value) {
    		if ( value instanceof String[] ) {
    			String[] values = (String[]) value;
    			StringBuffer sb = new StringBuffer();
    			for ( int i = 0; i < values.length; i++ ) {
    				sb.append( values[i] + " " );
    			}
    			sb.setLength( sb.length() - 1);
    			return sb.toString();
    		}
    		
    		return super.text( value );
    	}
    };

    /**
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    public DataStore createDataStore(Map params) throws IOException {
        if (!canProcess(params)) {
            throw new IOException("Invalid parameters for DirectoryDataStore");
        }

        URL url = (URL) DIRECTORY.lookUp(params);
        File f = DataUtilities.urlToFile(url);
        String[] strs = (String[]) CREATE_SUFFIX_ORDER.lookUp(params);

        if (strs == null) {
            throw new IOException("Invalid parameter "
                + CREATE_SUFFIX_ORDER.key + " : is null");
        }

        if (!f.isDirectory()) {
            throw new IOException("Invalid parameter " + DIRECTORY.key
                + " : is not a valid directory");
        }

        return new DirectoryDataStore(f, strs);
    }

//    /**
//     * @see org.geotools.data.DataStoreFactorySpi#createMetadata(java.util.Map)
//     */
//    public DataSourceMetadataEnity createMetadata(Map params)
//        throws IOException {
//        File url = (File) DIRECTORY.lookUp(params);
//        String parent = url.getParent();
//        String name = url.getName();
//
//        return new DataSourceMetadataEnity(parent, name,
//            "Access to Directory " + url.toString());
//    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        if (!canProcess(params)) {
            throw new IOException("Invalid parameters for DirectoryDataStore");
        }

        URL url = (URL) DIRECTORY.lookUp(params);
        File f = DataUtilities.urlToFile(url);

        if (f.exists()) {
            throw new IOException("Invalid parameter " + DIRECTORY.key
                + " : directory already exists");
        }

        if (!f.isDirectory()) {
            throw new IOException("Invalid parameter " + DIRECTORY.key
                + " : is not a valid directory");
        }

        if (!f.createNewFile()) {
            throw new IOException("Invalid parameter " + DIRECTORY.key
                + " : cannot create directory");
        }

        String[] strs = (String[]) CREATE_SUFFIX_ORDER.lookUp(params);

        if (strs == null) {
            throw new IOException("Invalid parameter "
                + CREATE_SUFFIX_ORDER.key + " : is null");
        }

        return new DirectoryDataStore(f, strs);
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDisplayName()
     */
    public String getDisplayName() {
        return "Directory DataStore";
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDescription()
     */
    public String getDescription() {
        return "Propagates to multiple file types representing a directory";
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] { DIRECTORY, CREATE_SUFFIX_ORDER };
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#canProcess(java.util.Map)
     */
    public boolean canProcess(Map params) {
        try {
            URL url = (URL) DIRECTORY.lookUp(params);
        	File f = DataUtilities.urlToFile(url);
            String[] str = (String[]) CREATE_SUFFIX_ORDER.lookUp(params);

            //return ((f != null) && (str != null) && f.isDirectory());
            return (f != null) && (str != null);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
