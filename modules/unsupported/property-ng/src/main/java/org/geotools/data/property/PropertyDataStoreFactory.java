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
package org.geotools.data.property;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;


/**
 * DataStore factory that creates {@linkplain org.geotools.data.property.PropertyDataStore}s
 *
 * @author Jody garnett
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class PropertyDataStoreFactory implements DataStoreFactorySpi {
    // private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(PropertyDataStoreFactory.class.getPackage().getName());
	
    public static final Param FILE = new Param("file", File.class,
            "Property file", true);

    public static final Param NAMESPACE = new Param("namespace", String.class,
            "namespace of datastore", false);
    
    public DataStore createDataStore(Map params) throws IOException {
    	File dir = fileLookup(params);
        String namespaceURI = (String) NAMESPACE.lookUp( params );
        if (dir.exists() && dir.isDirectory()) {
            return new PropertyDataStore(dir,namespaceURI);
        } else {
            throw new IOException("Directory is required");
        }
    }

    // createNewDataStore start
    public DataStore createNewDataStore(Map params) throws IOException {
    	File dir = (File)FILE.lookUp(params);

        if (dir.exists()) {
            throw new IOException(dir + " already exists");
        }

        String namespaceURI = (String) NAMESPACE.lookUp(params);
        return new PropertyDataStore(dir,namespaceURI);
    }
    // createNewDataStore end

    public String getDisplayName() {
        return "Properties NG";
    }

    public String getDescription() {
        return "Allows access to Java Property files containing Feature information";
    }

    /**
     * @see #DIRECTORY
     * @see PropertyDataStoreFactory#NAMESPACE
     */
    public Param[] getParametersInfo() {
        return new Param[] { FILE, NAMESPACE };
    }

    /**
     * Test to see if this datastore is available, if it has all the
     * appropriate libraries to construct a datastore.  This datastore just
     * returns true for now.  This method is used for gui apps, so as to not
     * advertise data store capabilities they don't actually have.
     *
     * @return <tt>true</tt> if and only if this factory is available to create
     *         DataStores.
     *
     * @task <code>true</code> property datastore is always available
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Works for a file directory or property file
     *
     * @param params Connection parameters
     *
     * @return true for connection parameters indicating a directory or property file
     */
    public boolean canProcess(Map params) {
        try {
            fileLookup(params);
            return true;
        } catch (Exception erp) {
            //can't process, just return false
            return false;
        }
    }
    
    /**
     * No implementation hints are provided at this time.
     */
    public Map getImplementationHints(){
        return java.util.Collections.EMPTY_MAP;
    }
    
    /**
     * Lookups the property file in the params argument, and
     * returns the corresponding <code>java.io.File</code>.
     * <p>
     * The file is first checked for existence as an absolute path in the filesystem. If
     * such a directory is not found, then it is treated as a relative path, taking Java
     * system property <code>"user.dir"</code> as the base.
     * </p>
     * @param params
     * @throws IllegalArgumentException if file is a directory.
     * @throws FileNotFoundException if directory does not exists
     * @throws IOException if {@linkplain #DIRECTORY} doesn't find parameter in <code>params</code>
     * file does not exists.
     */
    private File fileLookup(Map params) throws IOException, FileNotFoundException,
            IllegalArgumentException {
        File file = (File) FILE.lookUp(params);
        if( file.exists() ){
            if( file.isDirectory() ){
                throw new IllegalArgumentException("Property file is required (not a directory) "+file.getAbsolutePath());
            }
            return file;
        }
        else {
            File dir = file.getParentFile();
            if( dir == null || !dir.exists() ){
                // quickly check if it exists relative to the user directory
                File currentDir = new File(System.getProperty("user.dir"));
                
                File file2 = new File(currentDir, file.getPath());
                if (file2.exists()) {
                    return file2;
                }
            }
            return file;
        }
    }
}