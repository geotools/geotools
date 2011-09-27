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
package org.geotools.renderer.markwkt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.WKTReader2;
import org.opengis.feature.Feature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import org.geotools.renderer.style.ExpressionExtractor;
import org.geotools.renderer.style.MarkFactory;
import org.geotools.util.SoftValueHashMap;

/**
 * Factory to produce marks based on WKT representation of symbols. WKT geometries may be defined
 * directly in the SLD (prefixing it with @see {@value #WKT_PREFIX}, or in a WKT library stored in a
 * properties file (prefixing it with @see {@link #WKTLIB_PREFIX}).
 * 
 * The symbols stored in properties files are cached in soft references for better
 * performance. The root directory for properties files can be set using the {@link setRoot}
 * method.
 * 
 * @author Luca Morandini lmorandini@ieee.org
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * 
 */
public class WKTMarkFactory implements MarkFactory {

    public static final String WKT_PREFIX = "wkt://";

    public static final String WKTLIB_PREFIX = "wktlib://";

    public static final String WKT_SEPARATOR = "#";

    protected static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    protected static URL rootDir = null;

    /** The logger for the rendering module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.rendering");

    // Cache used to store libraries of WKT geometries
    protected static SoftValueHashMap<String, Map<String, String>> libraryCache;

    // Initializes linrary cache
    {
        libraryCache = new SoftValueHashMap<String, Map<String, String>>();
    }

    /*
     * Clears cache. While the cache uses {@link
     * http://download.oracle.com/javase/1.5.0/docs/api/java/lang/ref/SoftReference.html soft
     * references}, a clealring of the cache may be useful when the WKT libraries (stored in
     * properties file) are updated.
     */
    public void clearCache() {
        libraryCache.clear();
    }

    /*
     * Sets the root dir for WKT libraries
     * 
     * @param root Directory from which the search for WKT libraries starts
     */
    public void setRoot(URL root) {
        rootDir = root;
    }

    /*
     * Returns a WKT geometry from cache given its URL
     * 
     * @param urlLib URL of the WKT library
     * 
     * @param wktName name of the WKT shape
     */
    protected String getFromCache(String urlLib, String wktName) {
        Map<String, String> library = libraryCache.get(urlLib);
        if (library != null) {
            return (String) library.get(wktName);
        }
        return null;
    }

    /*
     * Adds the shapes contained in a WKT library to the cache; if the url already exists in the
     * cache, the shapes are not added
     * 
     * @param urlLib URL of the WKT library as a properties file URL
     */
    protected void addToCache(String urlLib) {
        Map<String, String> library = libraryCache.get(urlLib);
        if (library == null) {
            library = new HashMap<String, String>();
            Properties propLib = null;
            try {
                propLib = this.loadLibrary(urlLib);
            } catch (IOException e1) {
                LOGGER.log(Level.FINER, e1.getMessage(), e1);
            }
            for (Enumeration<String> e = (Enumeration<String>) propLib.propertyNames(); e
                    .hasMoreElements();) {
                String shpName = (String) (e.nextElement());
                library.put(shpName, (String) (propLib.get(shpName)));
            }
            libraryCache.put(urlLib, library);
        }
    }

    /*
     * Returns a WKT shaoe given that its URL specifies a WKT geometry or contains a reference to a
     * WKT geometry specfied in a properties file
     * 
     * @see org.geotools.renderer.style.MarkFactory#getShape(java.awt.Graphics2D,
     * org.opengis.filter.expression.Expression, org.opengis.feature.Feature)
     */
    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature)
            throws Exception {

        // cannot handle a null url
        if (symbolUrl == null) {
            return null;
        }

        // If it does not start with either prefix, it cannot create symbols
        String wellKnown = symbolUrl.evaluate(feature, String.class);
        if (!wellKnown.startsWith(WKT_PREFIX) && !wellKnown.startsWith(WKTLIB_PREFIX)) {
            return null;
        }

        // See if it is a simple WKT, so that i can extract the WKT symbols
        // directly
        String wkt = null;
        if (wellKnown.startsWith(WKT_PREFIX)) {
            wkt = wellKnown.substring(WKT_PREFIX.length());
        }

        // See if it is a WKT library reference
        if (wellKnown.startsWith(WKTLIB_PREFIX)) {
            String[] urlComponents = wellKnown.substring(WKTLIB_PREFIX.length()).split(
                    WKT_SEPARATOR);
            wkt = this.getFromCache(urlComponents[0], urlComponents[1]);
            if (wkt == null) {
                this.addToCache(urlComponents[0]);
                wkt = this.getFromCache(urlComponents[0], urlComponents[1]);
            }
        }

        if (wkt == null) {
            LOGGER.info("This is not recognised a WKT symbol: " + wellKnown);
        }

        // Creates and return the symbol
        WKTReader2 reader = new WKTReader2();
        LiteShape shape = new LiteShape(reader.read(wkt), null, false);
        LOGGER.info("Created symbol from WKT " + wkt);
        return shape;
    }

    /*
     * Loads a WKT symbol library as a properties file
     * 
     * @param libFilee Location of the properties file (it could be a CQL expression)
     */
    protected Properties loadLibrary(String libFile) throws IOException {

        final Properties properties = new Properties();

        URL libUrl = null;
        try {
            libUrl = new URL(rootDir.toString() + "/" + libFile);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.FINE, "Could not parse WKT library URL: " + rootDir + "/" + libFile, e);
        }

        InputStream in = null;
        try {
            in = libUrl.openStream();
            properties.load(in);
        } catch (FileNotFoundException e1) {
            LOGGER.log(Level.FINER, e1.getMessage(), e1);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        } finally {
            in.close();
        }

        return properties;
    }
}
