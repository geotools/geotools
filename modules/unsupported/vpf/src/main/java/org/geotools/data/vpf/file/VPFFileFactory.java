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

import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * A factory for VPFFileStore. The file store is a singleton and the factory acts as the container.
 * This class does not do anything special at all and could easily be circumvented, but is here for
 * completeness.
 *
 * @author jeff yutzler
 * @source $URL$
 */
public class VPFFileFactory implements DataStoreFactorySpi {
    // private final VPFFileStore store = new VPFFileStore();
    private VPFFileStore store = null;
    private static VPFFileFactory instance = null;
    /** Default constructor. Does nothing! */
    private VPFFileFactory() {}
    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        this.store = new VPFFileStore(file.getPath());
        return this.store;
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
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        throw new UnsupportedOperationException("Only existing data stores may be created.");
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

    /** Parameter description of information required to connect */
    public static final Param FILE_PARAM =
            new Param(
                    "file", File.class, "VPF file", true, null
                    // ,(KVP)null
                    /* new KVP(Param.EXT, "") */
                    );

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM};
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#canProcess(java.util.Map)
     */
    public boolean canProcess(Map<String, Serializable> params) {
        try {
            String filePath = (String) FILE_PARAM.lookUp(params);
            if (filePath != null) {
                return true;
            }
        } catch (IOException e) {
            // ignore as we are expected to return true or false
        }
        return false;
    }

    /* (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }
    /**
     * Returns the singleton instance
     *
     * @return Returns the instance.
     */
    public static VPFFileFactory getInstance() {
        if (instance == null) {
            instance = new VPFFileFactory();
        }
        return instance;
    }

    public VPFFile getFile(String pathName) throws IOException {
        if (this.store == null) {
            this.store = new VPFFileStore(pathName);
        }
        store.getTypeSchema(pathName);
        SimpleFeatureType schema = store.getSchema(pathName);

        VPFFile file = (VPFFile) schema.getUserData().get(VPFFile.class);
        return file;
    }

    /** Returns the implementation hints. The default implementation returns en empty map. */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    /** Close all currently open files. */
    public void reset() {
        store.reset();
    }
}
