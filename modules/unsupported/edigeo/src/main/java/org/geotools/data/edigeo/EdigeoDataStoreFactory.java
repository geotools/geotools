/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;

import com.vividsolutions.jts.geom.Geometry;

public class EdigeoDataStoreFactory implements DataStoreFactorySpi {
	
	// DataStore - specific parameters
	public static final Param PARAM_PATH = new Param("path", String.class,
            "Full path of a single Edigeo file (THF)", true,
            "c:/data/edigeo/lot.thf"); 
	
	public static final Param PARAM_OBJ = new Param("obj", String.class,
            "Edigeo object id", true, "COMMUNE_id"); 

	/**
     * <p>
     * Creates a new EdigeoDataStoreFactory object.
     * </p>
     */
    public EdigeoDataStoreFactory() {
        super();
    }
    
	/**
	 * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
	 */
	public DataStore createDataStore(Map<String, Serializable> params)
			throws IOException {
		
		EdigeoDataStore edigeo = null;
		
		String path = (String) PARAM_PATH.lookUp(params);
		String edigeoObj = (String) PARAM_OBJ.lookUp(params);
		
		edigeo = new EdigeoDataStore(path, edigeoObj);
		
		return edigeo;
	}

	 /**
     * <p>
     * this method calls createDataStore().
     * </p>
     *
     * @param params The parameter map
     *
     * @return the {@link EdigeoDataStore} instance returned by createDataStore(params)
     *
     * @throws IOException
     *
     * @see #createDataStore(Map)
     */
	public DataStore createNewDataStore(Map<String, Serializable> params)
			throws IOException {
		return createDataStore(params);
	}

	/**
     * Takes a map of parameters which describes how to access a DataStore and
     * determines if it can be read by the EdigeoDataStore implementations.
     * 
     * @param params  A map of parameters describing the location of a
     *                datastore.
     * 
     * @return true if params contains a path param which points to an Edigeo file
     *         ending in thf
     */
	public boolean canProcess(Map<String, Serializable> params) {
		boolean accept = false;
        if (params.containsKey(PARAM_PATH.key)) {
            try {
                String path = (String) PARAM_PATH.lookUp(params);
                accept = canProcess(path);
            } catch (IOException ioe) {
                // yes, I am eating this - since it is my job to return a
                // true/false
            }
        }
        return accept;
	}

	private boolean canProcess(String path) {
		// TODO Auto-generated method stub
		boolean accept = false;
		try {
			File file = EdigeoFileFactory.setFile(path, "thf", true);
			accept = file.exists();
		} catch (FileNotFoundException e) {
			// yes, I am eating this - since it is my job to return a
            // true/false
		}
		return accept;
	}

	/**
     * Describes the type of data the datastore returned by this factory works
     * with.
     * 
     * @return String a human readable description of the type of restore
     *         supported by this datastore.
     */
    public String getDescription() {
        return "EDIGéO format files (*.thf)";
    }

    /**
     * @see org.geotools.data.DataStoreFactorySpi#getDisplayName()
     */
    public String getDisplayName() {
		return "EdigeoDataStore";
	}

    /**
     * Describe parameters.
     * 
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
	public Param[] getParametersInfo() {
		Param[] params = { PARAM_PATH, PARAM_OBJ };
	    return params;
	}

	/**
     * Test to see if this datastore is available, if it has all the appropriate
     * libraries to construct a datastore.
     * 
     * This datastore just checks for the EdigeoDataStore and Geometry implementations.
     * 
     * @return <tt>true</tt> if and only if this factory is available to
     *         create DataStores.
     */
	public boolean isAvailable() {
		try {
			EdigeoDataStore.class.getName();
			Geometry.class.getName();
		} catch (Exception e) {
			return false;
		}
		
		return true;
	}

	/**
     * <p>
     * Always return Collections#emptyMap(), because no hints are available for
     * now.
     * </p>
     *
     * @see org.geotools.factory.Factory#getImplementationHints()
     */
	public Map<Key, ?> getImplementationHints() {
		return Collections.emptyMap();
	}
	
}
