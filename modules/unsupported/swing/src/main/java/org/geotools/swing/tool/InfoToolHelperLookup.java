/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.map.Layer;

/**
 * A lookup facility for {@code InfoToolHelper} classes.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
class InfoToolHelperLookup {
    private static final Logger LOGGER = Logger.getLogger("org.geotools.swing");

    private static final String SPI_NAME =
            "META-INF/services/org.geotools.swing.tool.InfoToolHelper";
    
    private static WeakReference<List<Class>> cache;

    public static InfoToolHelper getHelper(Layer layer) {
        List<Class> helperClasses = InfoToolHelperLookup.getProviders();
        for (Class c : helperClasses) {
            try {
                InfoToolHelper helper = (InfoToolHelper) c.newInstance();
                if (helper.isSupportedLayer(layer)) {
                    return helper;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        return null;
    }

    /**
     * Gets classes that implement the InfoToolHelper SPI.
     *
     * @return list of implementing classes
     */
    private static List<Class> getProviders() {
        List<Class> providers = null;
        
        if (cache != null) {
            providers = cache.get();
        }

        if (providers == null) {
            providers = getProvidersFromSpiFile();
            cache = new WeakReference<List<Class>>(providers);
        }

        return providers;
    }

    /**
     * Read class names from a registry file and return the list of
     * implementing classes.
     *
     * @param SPI_NAME a fully qualified interface name
     *
     * @return list of implementing classes
     */
    private static List<Class> getProvidersFromSpiFile() {
        List<Class> providers = new ArrayList<Class>();

        ClassLoader cl = InfoToolHelperLookup.class.getClassLoader();
        if (cl != null) {
            InputStream str = cl.getResourceAsStream(SPI_NAME);
            if (str != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(str));
                String line = null;

                try {
                    while ((line = reader.readLine()) != null) {
                        String text = line.trim();
                        if (text.length() > 0 && !text.startsWith("#")) {
                            try {
                                providers.add(Class.forName(text));
                            } catch (ClassNotFoundException ex) {
                                LOGGER.log(Level.WARNING, "Class not found: {0}", text);
                            }
                        }
                    }
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Problem reading services file: {0}", SPI_NAME);

                } finally {
                    try {
                        str.close();
                    } catch (Throwable e) {
                        // ignore
                    }

                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (Throwable e) {
                        // ignore
                    }
                }

            } else {
                LOGGER.log(Level.SEVERE, "Could not find {0}", SPI_NAME);
            }
        }

        return providers;
    }
}
