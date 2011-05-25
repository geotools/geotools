/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.file;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;


/**
 * A factory for VPFFileStore.
 * The file store is a singleton and the factory
 * acts as the container.
 * This class does not do anything special at all
 * and could easily be circumvented, 
 * but is here for completeness.
 *
 * @author jeff yutzler
 *
 * @source $URL$
 */
public class VPFFileFactory implements DataStoreFactorySpi {
    private final VPFFileStore store = new VPFFileStore();
    private static VPFFileFactory instance = null;
    /**
     * Default constructor. Does nothing!
     */
    private VPFFileFactory() {
    }
    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    public DataStore createDataStore(Map params) throws IOException {
        return store;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#createMetadata(java.util.Map)
     */
//    public DataSourceMetadataEnity createMetadata(Map params)
//        throws IOException {
//        // TODO Auto-generated method stub
//        return null;
//    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException(
            "Only existing data stores may be created.");
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getDisplayName()
     */
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getDescription()
     */
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
    

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#canProcess(java.util.Map)
     */
    public boolean canProcess(Map params) {
        //        boolean result = false;
        //        Object object;
        //        File file;
        //        if (params.containsKey("Path Name")){
        //            object = params.get("Path Name");
        //            if(object instanceof File){
        //                file = (File)object;
        //            }else {
        //                file = new File(object.toString());
        //            }
        //            if(file.exists() && file.isFile() && !file.isDirectory()) {
        //                result = true;
        //            }
        //        }
        //        return result;
        return true;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }
    /**
     * Returns the singleton instance
     * @return Returns the instance.
     */
    public static VPFFileFactory getInstance() {
        if(instance == null){
            instance = new VPFFileFactory();
        }
        return instance;
    }
    public VPFFile getFile(String pathName) throws IOException{
        return (VPFFile)store.getSchema(pathName);
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }

    /**
     * Close all currently open files.
     */
    public void reset() {
        store.reset();
    }
}
