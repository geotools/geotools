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
package org.geotools.renderer.style.markwkt;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.Feature;
import org.geotools.api.filter.expression.Expression;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.renderer.style.MarkFactory;
import org.geotools.util.SoftValueHashMap;

/**
 * Factory to produce marks based on WKT representation of symbols. WKT geometries may be defined directly in the SLD
 * (prefixing it with @see {@value #WKT_PREFIX}, or in a WKT library stored in a properties file (prefixing it with @see
 * {@link #WKTLIB_PREFIX}).
 *
 * <p>The symbols stored in properties files are cached in soft references for better performance. The root directory
 * for properties files can be set using the {@link setRoot} method.
 *
 * @author Luca Morandini lmorandini@ieee.org
 * @author Simone Giannecchini, GeoSolutions
 * @version $Id$
 */
public class WKTMarkFactory implements MarkFactory {

    /** The logger for the rendering module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(WKTMarkFactory.class);

    public static final String WKT_PREFIX = "wkt://";

    public static final String WKTLIB_PREFIX = "wktlib://";

    public static final String WKT_SEPARATOR = "#";

    protected static URL ROOT_DIRECTORY = null;

    // Cache used to store libraries of WKT geometries
    protected static final SoftValueHashMap<String, Map<String, String>> CACHE = new SoftValueHashMap<>();

    /*
     * Clears cache. While the cache uses {@link
     * http://download.oracle.com/javase/1.5.0/docs/api/java/lang/ref/SoftReference.html soft
     * references}, a clearing of the cache may be useful when the WKT libraries (stored in
     * properties file) are updated.
     */
    public void clearCache() {
        CACHE.clear();
    }

    /**
     * Sets the root dir for WKT libraries
     *
     * @param root Directory from which the search for WKT libraries starts
     */
    public static void setRoot(URL root) {
        ROOT_DIRECTORY = root;
    }

    /**
     * Returns a WKT geometry from cache given its URL
     *
     * @param urlLib URL of the WKT library
     * @param wktName name of the WKT shape
     */
    protected String getFromCache(String urlLib, String wktName) {
        Map<String, String> library = CACHE.get(urlLib);
        if (library != null) {
            return library.get(wktName);
        }
        return null;
    }

    /**
     * Adds the shapes contained in a WKT library to the cache; if the url already exists in the cache, the shapes are
     * not added
     *
     * @param urlLib URL of the WKT library as a properties file URL
     */
    protected void addToCache(String urlLib) {
        Map<String, String> library = CACHE.get(urlLib);
        if (library == null) {
            library = new HashMap<>();
            Properties propLib = null;
            try {
                propLib = this.loadLibrary(urlLib);
            } catch (IOException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
                return;
            }
            @SuppressWarnings("unchecked")
            Enumeration<String> names = (Enumeration<String>) propLib.propertyNames();
            for (Enumeration<String> e = names; e.hasMoreElements(); ) {
                String shpName = e.nextElement();
                library.put(shpName, (String) propLib.get(shpName));
            }
            CACHE.put(urlLib, library);
        }
    }

    /**
     * Returns a WKT shaoe given that its URL specifies a WKT geometry or contains a reference to a WKT geometry
     * specfied in a properties file
     *
     * @see org.geotools.renderer.style.MarkFactory#getShape(java.awt.Graphics2D,
     *     org.geotools.api.filter.expression.Expression, org.geotools.api.feature.Feature)
     */
    @Override
    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature) throws Exception {

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
            String[] urlComponents = wellKnown.substring(WKTLIB_PREFIX.length()).split(WKT_SEPARATOR);
            synchronized (this) {
                wkt = this.getFromCache(urlComponents[0], urlComponents[1]);
                if (wkt == null) {
                    this.addToCache(urlComponents[0]);
                    wkt = this.getFromCache(urlComponents[0], urlComponents[1]);
                }
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

    /**
     * Loads a WKT symbol library as a properties file
     *
     * @param libFile Location of the properties file (it could be a CQL expression)
     */
    protected Properties loadLibrary(String libFile) throws IOException {

        final Properties properties = new Properties();
        URL libUrl = null;
        try {
            libUrl = new URL(ROOT_DIRECTORY.toString() + "/" + libFile);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.WARNING, "Could not parse WKT library URL: " + ROOT_DIRECTORY + "/" + libFile, e);
            return properties;
        }

        try (InputStream in = libUrl.openStream()) {
            properties.load(in);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return properties;
    }
}
