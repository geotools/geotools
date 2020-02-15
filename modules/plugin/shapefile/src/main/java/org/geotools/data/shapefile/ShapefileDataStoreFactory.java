/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.directory.DirectoryDataStore;
import org.geotools.data.directory.FileStoreFactory;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.util.KVP;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;

/** Builds instances of the shapefile data store */
public class ShapefileDataStoreFactory implements FileDataStoreFactorySpi {

    static final Logger LOGGER = Logging.getLogger(ShapefileDataStoreFactory.class);

    /** url to the .shp file. */
    public static final Param URLP =
            new Param(
                    "url", URL.class, "url to a .shp file", true, null, new KVP(Param.EXT, "shp"));

    /** Optional - uri of the FeatureType's namespace */
    public static final Param NAMESPACEP =
            new Param(
                    "namespace",
                    URI.class,
                    "uri to a the namespace",
                    false,
                    null, // not required
                    new KVP(Param.LEVEL, "advanced"));

    /** Optional - enable/disable the use of memory-mapped io */
    public static final Param MEMORY_MAPPED =
            new Param(
                    "memory mapped buffer",
                    Boolean.class,
                    "enable/disable the use of memory-mapped io",
                    false,
                    false,
                    new KVP(Param.LEVEL, "advanced"));

    /** Optional - enable/disable the use of memory-mapped io */
    public static final Param CACHE_MEMORY_MAPS =
            new Param(
                    "cache and reuse memory maps",
                    Boolean.class,
                    "only memory map a file one, then cache and reuse the map",
                    false,
                    true,
                    new KVP(Param.LEVEL, "advanced"));

    /** Optional - discriminator for directory stores */
    public static final Param FILE_TYPE =
            new Param(
                    "filetype",
                    String.class,
                    "Discriminator for directory stores",
                    false,
                    "shapefile",
                    new KVP(Param.LEVEL, "program"));

    /** Optional - Enable/disable the automatic creation of spatial index */
    public static final Param CREATE_SPATIAL_INDEX =
            new Param(
                    "create spatial index",
                    Boolean.class,
                    "enable/disable the automatic creation of spatial index",
                    false,
                    true,
                    new KVP(Param.LEVEL, "advanced"));

    /** Optional - character used to decode strings from the DBF file */
    public static final Param DBFCHARSET =
            new Param(
                    "charset",
                    Charset.class,
                    "character used to decode strings from the DBF file",
                    false,
                    Charset.forName("ISO-8859-1"),
                    new KVP(Param.LEVEL, "advanced")) {
                /*
                 * This is an example of a non simple Param type where a custom parse method is required.
                 *
                 * @see org.geotools.data.DataStoreFactorySpi.Param#parse(java.lang.String)
                 */
                public Object parse(String text) throws IOException {
                    return Charset.forName(text);
                }

                public String text(Object value) {
                    return ((Charset) value).name();
                }
            };
    /**
     * Optional parameter used to indicate 'shape-ng' (as a marker to select the implementation of
     * DataStore to use).
     */
    public static final Param FSTYPE =
            new Param(
                    "fstype",
                    String.class,
                    "Enable using a setting of 'shape'.",
                    false,
                    "shape",
                    new KVP(
                            Param.LEVEL,
                            "advanced",
                            Param.OPTIONS,
                            Arrays.asList(new String[] {"shape-ng", "shape", "index"})));
    /** Optional - timezone to decode dates from the DBF file */
    public static final Param DBFTIMEZONE =
            new Param(
                    "timezone",
                    TimeZone.class,
                    "time zone used to read dates from the DBF file",
                    false,
                    TimeZone.getDefault(),
                    new KVP(Param.LEVEL, "advanced")) {

                public Object parse(String text) throws IOException {
                    return TimeZone.getTimeZone(text);
                }

                public String text(Object value) {
                    return ((TimeZone) value).getID();
                }
            };

    /** Optional - enable spatial index for local files */
    public static final Param ENABLE_SPATIAL_INDEX =
            new Param(
                    "enable spatial index",
                    Boolean.class,
                    "enable/disable the use of spatial index for local shapefiles",
                    false,
                    true,
                    new KVP(Param.LEVEL, "advanced"));

    public String getDisplayName() {
        return "Shapefile";
    }

    public String getDescription() {
        return "ESRI(tm) Shapefiles (*.shp)";
    }

    public Param[] getParametersInfo() {
        return new Param[] {
            URLP,
            NAMESPACEP,
            ENABLE_SPATIAL_INDEX,
            CREATE_SPATIAL_INDEX,
            DBFCHARSET,
            DBFTIMEZONE,
            MEMORY_MAPPED,
            CACHE_MEMORY_MAPS,
            FILE_TYPE,
            FSTYPE
        };
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return Collections.EMPTY_MAP;
    }

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        URL url = lookup(URLP, params, URL.class);
        Boolean isMemoryMapped = lookup(MEMORY_MAPPED, params, Boolean.class);
        Boolean cacheMemoryMaps = lookup(CACHE_MEMORY_MAPS, params, Boolean.class);
        URI namespace = lookup(NAMESPACEP, params, URI.class);
        Charset dbfCharset = lookup(DBFCHARSET, params, Charset.class);
        TimeZone dbfTimeZone = lookup(DBFTIMEZONE, params, TimeZone.class);
        Boolean isCreateSpatialIndex = lookup(CREATE_SPATIAL_INDEX, params, Boolean.class);
        Boolean isEnableSpatialIndex = (Boolean) ENABLE_SPATIAL_INDEX.lookUp(params);
        if (isEnableSpatialIndex == null) {
            // should not be needed as default is TRUE
            isEnableSpatialIndex = Boolean.TRUE;
        }

        // are we creating a directory of shapefiles store, or a single one?
        File dir = URLs.urlToFile(url);
        if (dir != null && dir.isDirectory()) {
            return new DirectoryDataStore(
                    URLs.urlToFile(url), new ShpFileStoreFactory(this, params));
        } else {
            ShpFiles shpFiles = new ShpFiles(url);

            boolean isLocal = shpFiles.isLocal();
            boolean useMemoryMappedBuffer = isLocal && isMemoryMapped.booleanValue();
            boolean enableIndex = isEnableSpatialIndex.booleanValue() && isLocal;
            boolean createIndex = isCreateSpatialIndex.booleanValue() && enableIndex;

            // build the store
            ShapefileDataStore store = new ShapefileDataStore(url);
            if (namespace != null) {
                store.setNamespaceURI(namespace.toString());
            }
            store.setMemoryMapped(useMemoryMappedBuffer);
            store.setBufferCachingEnabled(cacheMemoryMaps);
            store.setCharset(dbfCharset);
            store.setTimeZone(dbfTimeZone);
            store.setIndexed(enableIndex);
            store.setIndexCreationEnabled(createIndex);
            return store;
        }
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return createDataStore(params);
    }

    /**
     * Looks up a parameter, if not found it returns the default value, assuming there is one, or
     * null otherwise
     *
     * @param <T>
     */
    <T> T lookup(Param param, Map<String, Serializable> params, Class<T> target)
            throws IOException {
        T result = (T) param.lookUp(params);
        if (result == null) {
            return (T) param.getDefaultValue();
        } else {
            return result;
        }
    }

    @Override
    public boolean canProcess(Map params) {
        if (!DataUtilities.canProcess(params, getParametersInfo())) {
            return false; // fail basic param check
        }
        try {
            URL url = (URL) URLP.lookUp(params);
            if (canProcess(url)) {
                return true;
            } else {
                // maybe it's a directory?
                Object fileType = FILE_TYPE.lookUp(params);
                File dir = URLs.urlToFile(url);

                // check for null fileType for backwards compatibility

                // Return false if this is a VPF directory
                if (dir != null && dir.isDirectory() && fileType == null) {
                    String dirPath = dir.getPath();

                    String[] vpfTables = {"LAT", "LHT", "DHT", "lat", "lht", "dht"};

                    for (int itab = 0; itab < vpfTables.length; itab++) {

                        String tabFilename = vpfTables[itab];

                        String pathTab = dirPath.concat(File.separator).concat(tabFilename);

                        if (new File(pathTab).exists()) {
                            return false;
                        }
                    }
                }

                return dir != null
                        && dir.isDirectory()
                        && (fileType == null || "shapefile".equals(fileType));
            }
        } catch (IOException e) {
            return false;
        }
    }

    public boolean canProcess(URL f) {
        return f != null && f.getFile().toUpperCase().endsWith("SHP");
    }

    /**
     * A delegates that allow to build a directory of shapfiles store
     *
     * @author Andrea Aime - OpenGeo
     */
    public static class ShpFileStoreFactory implements FileStoreFactory {

        ShapefileDataStoreFactory shpFactory;

        Map originalParams;

        public ShpFileStoreFactory(ShapefileDataStoreFactory factory, Map originalParams) {
            this.shpFactory = factory;
            this.originalParams = originalParams;
        }

        public DataStore getDataStore(File file) throws IOException {
            final URL url = URLs.fileToUrl(file);
            if (shpFactory.canProcess(url)) {
                Map<String, Serializable> params =
                        new HashMap<String, Serializable>(originalParams);
                params.put(URLP.key, url);
                return shpFactory.createDataStore(params);
            } else {
                return null;
            }
        }
    }

    @Override
    public String[] getFileExtensions() {
        return new String[] {".shp"};
    }

    @Override
    public FileDataStore createDataStore(URL url) throws IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put(URLP.key, url);

        boolean isLocal = url.getProtocol().equalsIgnoreCase("file");
        File file = URLs.urlToFile(url);
        if (file != null && file.isDirectory()) {
            return null;
        } else {
            if (isLocal && !file.exists()) {
                return (FileDataStore) createNewDataStore(params);
            } else {
                return (FileDataStore) createDataStore(params);
            }
        }
    }

    @Override
    public String getTypeName(URL url) throws IOException {
        DataStore ds = createDataStore(url);
        String[] names = ds.getTypeNames(); // should be exactly one
        ds.dispose();
        return ((names == null || names.length == 0) ? null : names[0]);
    }
}
