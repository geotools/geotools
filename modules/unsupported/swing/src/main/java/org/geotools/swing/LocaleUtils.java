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

package org.geotools.swing;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Centralizes {@linkplain Locale} and {@linkplain ResourceBundle} handling for
 * GUI elements.
 * <p>
 * Please note: This class is little more than a stub at the moment.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class LocaleUtils {
    
    private static final String PREFIX = "org/geotools/swing/";
    
    private static Locale locale = Locale.getDefault();
    
    private static Map<String, ResourceBundle> bundles = 
            new ConcurrentHashMap<String, ResourceBundle>();
    
    private LocaleUtils() {}
    
    /**
     * Sets the {@linkplain Locale} to be used by GUI elements.
     * If both arguments are {@code null} or empty, the default locale is set.
     * <p>
     * @param language lowercase two-letter ISO-639 code.
     * @param country uppercase two-letter ISO-3166 code.
     */
    public static void setLocale(String language, String country) {
        if (language == null) {
            language = "";
        }
        if (country == null) {
            country = "";
        }
        
        LocaleUtils.locale = new Locale(language, country);
        
        if (bundles != null) {
            bundles.clear();
        }
    }
    
    public static String getValue(String resBundleName, String key) {
        return getBundle(resBundleName).getString(key);
    }

    private static ResourceBundle getBundle(String resBundleName) {
        ResourceBundle rb = bundles.get(resBundleName);
        if (rb == null) {
            rb = ResourceBundle.getBundle(PREFIX + resBundleName, locale);
            bundles.put(resBundleName, rb);
        }
        
        return rb;
    }
}
