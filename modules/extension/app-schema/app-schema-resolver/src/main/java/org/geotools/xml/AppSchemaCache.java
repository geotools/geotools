/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2012, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.geotools.xml.resolver.SchemaCache;

/**
 * Cache containing application schemas. (Should also work for other file types.)
 * 
 * <p>
 * 
 * If configured to permit downloading, schemas not present in the cache are downloaded from the
 * network.
 * 
 * <p>
 * 
 * Only http/https URLs are supported.
 * 
 * <p>
 * 
 * Files are stored according to the Simple HTTP Resource Path (see
 * {@link AppSchemaResolver#getSimpleHttpResourcePath(URI))}.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * 
 * 
 * 
 * @source $URL$
 */
public class AppSchemaCache extends SchemaCache {

    

    /**
     * A cache of application schemas (or other file types) rooted in the given directory, with
     * optional downloading.
     * 
     * @param directory
     *            the directory in which downloaded schemas are stored
     * @param download
     *            is downloading of schemas permitted. If false, only schemas already present in the
     *            cache will be resolved.
     */
    public AppSchemaCache(File directory, boolean download) {
        super(directory, download);        
    }
    
    /**
     * A cache of XML schemas (or other file types) rooted in the given directory, with optional downloading.
     * 
     * @param directory the directory in which downloaded schemas are stored
     * @param download is downloading of schemas permitted. If false, only schemas already present in the cache will be resolved.
     * @param keepQuery indicates whether or not the query components should be included in the path. If this is set to true then the query portion is
     *        converted to an MD5 message digest and that string is used to identify the file in the cache.
     */
    public AppSchemaCache(File directory, boolean download, boolean keepQuery) {
        super(directory, download, keepQuery);        
    }
   
    /**
     * Recursively delete a directory or file.
     * 
     * @param file
     */
    protected static void delete(File file) {
        SchemaCache.delete(file);
    }

    /**
     * Store the bytes in the given file, creating any necessary intervening directories.
     * 
     * @param file
     * @param bytes
     */
    protected static void store(File file, byte[] bytes) {
        SchemaCache.store(file, bytes);
    }
    
    /**
     * Retrieve the contents of a remote URL.
     * 
     * @param location and absolute http/https URL.
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    protected static byte[] download(String location) {        
       return SchemaCache.download(location);
    }

    /**
     * Retrieve the contents of a remote URL.
     * 
     * @param location and absolute http/https URL.
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    protected static byte[] download(URI location) {
        return SchemaCache.download(location);
    }

    /**
     * Retrieve the contents of a remote URL.
     * 
     * @param location and absolute http/https URL.
     * @param blockSize download block size
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    protected static byte[] download(URI location, int blockSize) {        
        return SchemaCache.download(location, blockSize);
    }
    
    /**
     * If automatic configuration is enabled, recursively search parent directories of file url for a GeoServer data directory or directory containing
     * an existing cache. If found, use it to create a cache in the "app-schema-cache" subdirectory with downloading enabled.
     * 
     * @param url a URL for a file in a GeoServer data directory.
     * @return a cache in the "app-schema-cache" subdirectory or null if not found or automatic configuration disabled.
     */
    public static AppSchemaCache buildAutomaticallyConfiguredUsingFileUrl(URL url) {
        SchemaCache cache = SchemaCache
                .buildAutomaticallyConfiguredUsingFileUrl(url);
        if (cache != null) {
            return new AppSchemaCache(cache.getDirectory(), true, true);
        }
        return null;
    }
    
}
