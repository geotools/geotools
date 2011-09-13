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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/**
 * Provides localized text strings to GUI elements. This class hides the most of 
 * the fiddly bits associated with {@linkplain Locale} and {@linkplain ResourceBundle}
 * from other gt-swing classes.
 * <p>
 * You do not create instances of this class since it is basically just a wrapper
 * around static data. Client code will normally call just two methods: 
 * {@linkplain #setLocale(String, String)} during application startup and before any
 * GUI elements have been displayed; and {@linkplain #getValue(String, String)} to
 * retrieve localized text. 
 * <p>
 * If the {@code setLocale} method is not called, the locale defaults to
 * {@linkplain Locale#ENGLISH} (for no better reason than that is the language 
 * of the module maintainer).
 * <p>
 * Text strings are stored in properties files in 
 * {@code {module.resources.dir}/org/geotools/swing}. Adding support for
 * a new language involves adding the new locale to each of these files and then 
 * updating the {@code {module.resources.dir}/META-INF/LocaleUtils.properties} file
 * to record the newly added locale.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class LocaleUtils {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    
    private static final String PROPERTIES_FILE = "META-INF/LocaleUtils.properties";
    private static final String PREFIX = "org/geotools/swing/";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    
    private static final Properties properties;
    
    private static final Map<String, ResourceBundle> bundles = 
            new ConcurrentHashMap<String, ResourceBundle>();

    private static final List<Locale> supportedLocales;
    
    private static Locale currentLocale = DEFAULT_LOCALE;
    
    private static final Lock localeLock = new ReentrantLock();

    static {
        InputStream in = LocaleUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (in == null) {
            throw new RuntimeException(
                    "Error initializing LocaleUtils: " + PROPERTIES_FILE + " is missing");
        }
        
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException ex) {
            throw new RuntimeException("Error initializing LocaleUtils", ex);
        }
        
        supportedLocales = new ArrayList<Locale>();
        loadSupportedLocales();
    }
    
    /**
     * Private constructor to prevent clients creating instances.
     */
    private LocaleUtils() {}
    
    
    /**
     * Returns the locales supported by the gt-swing module.
     * 
     * @return supported locales as an unmodifiable list
     */
    public static List<Locale> getSupportedLocales() {
        localeLock.lock();
        return Collections.unmodifiableList(supportedLocales);
    }
    
    /**
     * Sets the {@linkplain Locale} to be used by GUI elements.
     * If both arguments are {@code null} or empty, the default locale is set.
     * <p>
     * @param language lowercase two-letter ISO-639 code.
     * @param country uppercase two-letter ISO-3166 code.
     */
    public static void setLocale(String language, String country) {
        localeLock.lock();
        try {
            language = language == null ? "" : language.trim();
            country = country == null ? "" : country.trim();

            Locale lnew;
            if (language.trim().isEmpty() && country.trim().isEmpty()) {
                lnew = DEFAULT_LOCALE;
            } else {
                lnew = new Locale(language, country);
            }

            if (!lnew.equals(currentLocale)) {
                if (!supportedLocales.contains(lnew)) {
                    LOGGER.log(Level.WARNING,
                            "{0} is not currently supported by the gt-swing module", lnew);
                    return;
                }

                currentLocale = lnew;

                if (bundles != null) {
                    bundles.clear();
                }
            }
        } finally {
            localeLock.unlock();
        }
    }
    
    public static String getValue(String resBundleName, String key) {
        localeLock.lock();
        try {
            return getBundle(resBundleName).getString(key);
        } finally {
            localeLock.unlock();
        }
    }

    private static ResourceBundle getBundle(String resBundleName) {
        ResourceBundle rb = bundles.get(resBundleName);
        if (rb == null) {
            rb = ResourceBundle.getBundle(PREFIX + resBundleName, currentLocale);
            bundles.put(resBundleName, rb);
        }
        
        return rb;
    }

    private static void loadSupportedLocales() {
        String propStr = properties.getProperty("supported");
        String[] entries = propStr.split("\\s*\\,\\s*");
        
        for (String s : entries) {
            if (s.length() > 0) {
                String language = "", country = "";
                int delimPos = s.indexOf('|');
                if (delimPos == 2) {
                    language = s.substring(0, delimPos);
                    country = s.substring(delimPos + 1);
                } else if (delimPos < 0) {
                    language = s;
                } else {
                    throw new IllegalArgumentException(
                            "Invalid entry in " + PROPERTIES_FILE +
                            " for supported: " + s);
                }
                
                supportedLocales.add( new Locale(language, country) );
            }
        }
    }
}
