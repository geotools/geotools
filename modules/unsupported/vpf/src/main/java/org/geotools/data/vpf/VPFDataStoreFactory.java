/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf;

import static org.geotools.data.vpf.ifc.FileConstants.*;
import static org.geotools.data.vpf.ifc.VPFLibraryIfc.*;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.vpf.file.VPFFile;
import org.geotools.data.vpf.file.VPFFileFactory;
import org.geotools.data.vpf.ifc.FileConstants;
import org.geotools.feature.SchemaException;
import org.geotools.util.KVP;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Class VPFDataSourceFactory.java is responsible for constructing appropriate VPFDataStore
 * (actually VPFLibrary) objects. VPFDataStoreFactory - factory for VPFLibrary - factory for
 * VPFCoverage - factory for VPFFeatureClass - implements FeatureType by delegation to contained
 * DefaultFeatureType - contains VPFFiles - retrieves VPFColumns from VPFFiles for use in
 * constructing DefaultFeatureType - contains joins (column pairs) - factory for VPFFeatureType -
 * implements FeatureType by delegation to contained VPFFeatureClass
 *
 * <p>VPFFile - contains VPFInputStream - factory for VPFColumn - implements AttributeType by
 * delegation to contained DefaultFeatureType
 *
 * <p>Created: Fri Mar 28 15:54:32 2003
 *
 * @author <a href="mailto:kobit@users.sourceforge.net">Artur Hefczyc</a>
 * @author <a href="mailto:knuterik@onemap.org">Knut-Erik Johnsen</a>, Project OneMap
 * @author Chris Holmes, Fulbright
 * @source $URL$
 * @version 2.1.0
 */
public class VPFDataStoreFactory implements DataStoreFactorySpi {
    /** Default Constructor */
    public VPFDataStoreFactory() {}
    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getDisplayName()
     */
    public String getDisplayName() {
        return "Vector Product Format Library";
    }
    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getDescription()
     */
    public String getDescription() {
        return "Vector Product Format Library data store implementation.";
    }
    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#canProcess(java.util.Map)
     */
    public boolean canProcess(Map params) {

        boolean result = false;
        try {

            getLhtFile(params);
            // if getLhtFile didn't throw an exception then we're good.
            result = true;

        } catch (IOException exc) {
            // catch io exception, false will return
        }
        return result;
    }

    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#createDataStore(java.util.Map)
     */
    @Override
    public DataStore createDataStore(Map params) throws IOException {
        return create(params);
    }

    public DataStore createDataStore(URL url) throws IOException {
        Map<String, Object> params = new KVP("url", url);
        return createDataStore(params);
    }

    /**
     * Creates a data store.
     *
     * @param params A <code>Map</code> of parameters which must be verified and
     */
    private DataStore create(Map params) throws IOException {
        DataStore result = null;

        File file = getLhtFile(params);
        // CH I'd like to check existence here, so that geoserver can get
        // a better error message, but I've spent way too long on this, so
        // I'm giving up for now.  Ideally canProcess just figures out if
        // the params are valid, and doesn't throw the not existing error, but
        // since we need a directory, not the actual file, it's hard to check
        // for anything.
        if (!file.exists() || !file.canRead()) {
            throw new IOException("File either doesn't exist or is unreadable : " + file);
        }
        URI namespace = (URI) NAMESPACEP.lookUp(params); // null if not exist
        // LOGGER.finer("creating new vpf datastore with params: " + params);

        boolean debug = VPFLogger.isLoggable(Level.FINEST);

        Path lhtPath = Paths.get(file.getPath());
        Path lhtRealPath = lhtPath.toRealPath();
        Path lhtParentPath = lhtRealPath.getParent();

        if (lhtParentPath == null) {
            throw new IOException(
                    "Fileparent either doesn't exist or is unreadable : " + lhtRealPath);
        }

        String rootDir = lhtParentPath.toString();
        String latTableName = new File(rootDir, LIBRARY_ATTTIBUTE_TABLE).toString();

        if (debug) {
            VPFLogger.log("open vpf datastore with params: " + params);
            VPFLogger.log("vpf datastore path: " + file.getPath());
            VPFLogger.log("LAT path: " + latTableName);
        }

        VPFFile latTable = VPFFileFactory.getInstance().getFile(latTableName);
        Iterator iter = latTable.readAllRows().iterator();
        SimpleFeature feature = iter.hasNext() ? (SimpleFeature) iter.next() : null;

        String directoryName = file.getPath();
        String folderName = directoryName.substring(directoryName.lastIndexOf(File.separator) + 1);

        SimpleFeature libraryFeature = null;

        while (feature != null) {
            String libraryName = feature.getAttribute(FIELD_LIB_NAME).toString();

            if (libraryName.equalsIgnoreCase(folderName)) {
                libraryFeature = feature;
                if (debug) {
                    VPFLogger.log("found library feature: " + folderName);
                }
                break;
            }

            if (VPFLogger.isLoggable(Level.FINEST)) {
                VPFLogger.log("----------- LAT feature: " + feature.getID());
                VPFFeatureType.debugFeature(feature);
            }

            feature = iter.hasNext() ? (SimpleFeature) iter.next() : null;
        }

        try {
            result = new VPFLibrary(libraryFeature, file, namespace);
        } catch (SchemaException exc) {
            throw new IOException(
                    "There was a problem making one of " + "the feature classes as a FeatureType.");
        }

        return result;
    }

    /*
     * private method to get the lht file from the map of params, to avoid
     * code duplication in canProcess and create, since they both need the
     * file - canProcess just returns true if it's there, and eats the
     * exception, create makes the store.
     */
    private File getLhtFile(Map params) throws IOException {
        URL url = (URL) DIR.lookUp(params);
        File file = null;
        if (url.getProtocol().equals("file")) {

            if (url.getHost() != null && !url.getHost().equals("")) {
                // win
                file = new File(url.getHost() + ":" + url.getFile());
            } else {
                // linux
                file = new File(url.getFile());
            }
            if (file.isDirectory()) {
                file = new File(file, FileConstants.LIBRARY_HEADER_TABLE);
            } else {
                if (!file.getName().equalsIgnoreCase(FileConstants.LIBRARY_HEADER_TABLE)) {
                    throw new IOException("File: " + file + "is not a lht file");
                }
            }

        } else {
            throw new IOException("only file protocol supported");
        }
        return file;
    }

    /*
     *
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#createNewDataStore(java.util.Map)
     */
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {

        return create(params);
    }
    /** A parameter which is the directory containing the LHT file */
    public static final Param DIR =
            new Param("url", URL.class, "Directory containing lht file", true);

    public static final Param NAMESPACEP =
            new Param("namespace", URI.class, "uri to a the namespace", false); // not required

    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        return new Param[] {
            DIR,
        };
    }
    /*
     *  (non-Javadoc)
     * @see org.geotools.data.DataStoreFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /** Returns the implementation hints. The default implementation returns en empty map. */
    /*public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }*/
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
