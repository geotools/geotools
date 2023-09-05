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
import java.util.Collections;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.feature.simple.SimpleFeatureType;

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
     * @see org.geotools.api.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    @Override
    public DataStore createDataStore(Map<String, ?> params) throws IOException {
        File file = (File) FILE_PARAM.lookUp(params);
        this.store = new VPFFileStore(file.getPath());
        return this.store;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.DataStoreFactorySpi#createMetadata(java.util.Map)
     */
    //    public DataSourceMetadataEnity createMetadata(Map params)
    //        throws IOException {
    //        // TODO Auto-generated method stub
    //        return null;
    //    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    @Override
    public DataStore createNewDataStore(Map<String, ?> params) throws IOException {
        throw new UnsupportedOperationException("Only existing data stores may be created.");
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.DataStoreFactorySpi#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.DataStoreFactorySpi#getDescription()
     */
    @Override
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
     * @see org.geotools.api.data.DataStoreFactorySpi#getParametersInfo()
     */
    @Override
    public Param[] getParametersInfo() {
        return new Param[] {FILE_PARAM};
    }

    /* (non-Javadoc)
     * @see org.geotools.api.data.DataStoreFactorySpi#canProcess(java.util.Map)
     */
    @Override
    public boolean canProcess(Map<String, ?> params) {
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
     * @see org.geotools.api.data.DataStoreFactorySpi#isAvailable()
     */
    @Override
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
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    /** Close all currently open files. */
    public void reset() {
        store.reset();
    }
}
