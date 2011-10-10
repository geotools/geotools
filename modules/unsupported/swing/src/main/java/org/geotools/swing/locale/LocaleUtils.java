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

package org.geotools.swing.locale;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

/** 
 * Provides localized text strings to GUI elements. This class hides most of the fiddly bits
 * associated with {@linkplain Locale} and {@linkplain ResourceBundle} from other gt-swing classes.
 * You do not create instances of this class, it is basically just a wrapper around static data.
 * Text strings are stored in properties files as per standard Java internationalization. All files
 * are located in the {@code org/geotools/swing/locale} directory of the swing module.
 * <p>
 * 
 * An application wishing to display GUI elements in a non-default locale must call either
 * {@linkplain #setLocale(Locale)} or {@linkplain #setLocale(List)} before constructing any GUI
 * elements which display localized text. At present, this class does not support switching locales
 * for components that have already been constructed.  
 * <p>
 * 
 * Clients retrieve text strings by specifying the base name of the relevant properties file and a
 * key as shown here:
 * 
 * <pre><code> 
 * String localizedText = LocaleUtils.getValue("CursorTool", "ZoomInTool");
 * </code></pre>
 * 
 * Adding support for a new language simply involves adding the new locale to all or some of the
 * properties files.  If the locale is only provided for some files, it will be recorded by as 
 * partially supported and used where available. 
 * <p>
 * 
 * You can set a single working locale with {@linkplain #setLocale(Locale)}.  If the specified
 * locale is only partially supported, the {@linkplain #getValue(String, String)} method will fall
 * back to the default {@linkplain Locale#ROOT} when retrieving text strings lacking this locale.
 * Alternatively, you can specify a list of locales in order of preference using the {@linkplain
 * #setLocale(List)} method.
 * <p>
 *
 * If the {@code setLocale} method is not called, the locale defaults to
 * {@linkplain Locale#ROOT} (which is English language in the properties files distributed
 * with GeoTools).
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class LocaleUtils {
    private static final Logger LOGGER = Logging.getLogger("org.geotools.swing");
    private static final String PREFIX = "org/geotools/swing/locale/";

    /*
     * Represents a supported locale.
     */
    private static class LocaleInfo {
        Locale locale;
        boolean fullySupported;

        public LocaleInfo(Locale locale, boolean fullySupported) {
            this.locale = locale;
            this.fullySupported = fullySupported;
        }
    }

    private static final List<LocaleInfo> supportedLocales;
    
    /*
     * Locales in order of client preference. Always has Locale.ROOT
     * as the final element.
     */
    private static final List<Locale> workingLocales;
    
    /* 
     * Cached resource bundles. Each bundle will be that of the 
     * highest available preference locale.
     */
    private static final Map<String, ResourceBundle> bundles = 
            new ConcurrentHashMap<String, ResourceBundle>();
    
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    /*
     * Scan properties files for GUI text strings to record the 
     * locales present and, for each, whether it is fully or partially
     * supported.
     */
    static {
        try {
            supportedLocales = loadLocaleInfo();

            workingLocales = new ArrayList<Locale>();
            workingLocales.add(Locale.ROOT);
            
        } catch (IOException ex) {
            throw new RuntimeException("Unable to read the locale directory", ex);
        }
    }

    /**
     * Private constructor to prevent clients creating instances.
     */
    private LocaleUtils() {}
    
    /**
     * Tests whether the given {@code Locale} is fully supported (ie. has been
     * provided for every GUI text string properties file.
     * 
     * @param locale the locale
     * @return {@code true} if fully supported; {@code false} if partially or
     *     not supported
     */
    public static boolean isFullySupportedLocale(Locale locale) {
        for (LocaleInfo li : supportedLocales) {
            if (li.locale.equals(locale)) {
                return li.fullySupported;
            }
        }
        
        return false;
    }
    
    /**
     * Tests whether the given {@code Locale} is supported. A locale is treated
     * as supported if it has been provided for at least one GUI text string
     * properties file.
     * 
     * @param locale the locale
     * @return {@code true} if the locale is at least partially supported
     * 
     * @see #isFullySupportedLocale(Locale) 
     */
    public static boolean isSupportedLocale(Locale locale) {
        for (LocaleInfo li : supportedLocales) {
            if (li.locale.equals(locale)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Sets a single preferred locale for text string retrieval. Any text
     * strings for which this locale has not been provided will fall back
     * to the default {@linkplain Locale#ROOT} (English language). If
     * {@code preferredLocale} is {@code null} the working locale will be set
     * to {@linkplain  Locale#ROOT}.
     * 
     * @param preferredLocale the locale
     */
    public static void setLocale(Locale preferredLocale) {
        setLocale(Collections.singletonList(preferredLocale));
    }
    
    /**
     * Sets the preferred locales for text string retrieval. The input list
     * is ordered from highest (first element) to lowest (last element) preference. 
     * There is no need to include {@linkplain Locale#ROOT} in the input list.
     * It will be added automatically. If {@code preferredLocales} is {@code null}
     * or empty, the working locale will be set to {@linkplain  Locale#ROOT}.
     * 
     * @param preferredLocales locales in descending order of preference
     */
    public static void setLocale(List<Locale> preferredLocales) {
        lock.writeLock().lock();
        try {
            filterAndCopy(preferredLocales);
            bundles.clear();
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Retrieves a GUI text string identified by the base name of a
     * properties file and a key within that file.
     * <pre><code>
     * String localName = LocaleUtils.getValue("CursorTool", "ZoomIn");
     * </code></pre>
     * 
     * @param baseFileName base name of the properties file containing the text string
     * @param key key for the text string
     * @return the localized text string
     * 
     * @throws MissingResourceException if the {@code baseFileName:key} pair
     *     cannot be found
     * @throws IllegalArgumentException if either argument is {@code null}
     */
    public static String getValue(String baseFileName, String key) {
        lock.readLock().lock();
        try {
            if (baseFileName == null || key == null) {
                throw new IllegalArgumentException("arguments must not be null");
            }
            return getBundle(baseFileName).getString(key);
            
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Loads a {@linkplain ResourceBundle} into the cache according to
     * the current order of locale preferences.
     * 
     * @param resBundleName bundle name
     * @return the resource bundle
     * @throws MissingResourceException if the bundle is not found
     */
    private static ResourceBundle getBundle(String resBundleName) {
        ResourceBundle rb = bundles.get(resBundleName);
        if (rb == null) {
            rb = ResourceBundle.getBundle(PREFIX + resBundleName,
                    new ResourceBundle.Control() {
                        @Override
                        public List<Locale> getCandidateLocales(String baseName, Locale locale) {
                            return workingLocales;
                        }
                    });
                    
            bundles.put(resBundleName, rb);
        }
        
        return rb;
    }
    
    /**
     * TAkes a list of {@code Locales} in preference order and copies them to the list of
     * working locales, ensuring that only supported locales are present and that 
     * {@code Locale.ROOT} appears last.
     * 
     * @param requestedLocales list of locales in preference order
     */
    private static void filterAndCopy(List<Locale> requestedLocales) {
        workingLocales.clear();
        for (Locale locale : requestedLocales) {
            if (!locale.equals(Locale.ROOT)) {
                if (isSupportedLocale(locale)) {
                    // Locale is immutable so no need to create copy
                    workingLocales.add(locale);
                } else {
                    LOGGER.log(Level.WARNING, "{0} is not currently supported", locale);
                }
            }
        }
        workingLocales.add(Locale.ROOT);
    }
    
    /**
     * Scans the properties files in the resource directory, compiles a list
     * of locales, and determines whether each of them is fully or partially
     * supported.
     * 
     * @return list of locale info
     * @throws IOException on error scanning resources directory
     */
    private static List<LocaleInfo> loadLocaleInfo() throws IOException {
        PropertiesFileFinder finder = new PropertiesFileFinder();
        List<PropertiesFileInfo> infoList = finder.scan(PREFIX);
        
        Set<Locale> allLocales = new HashSet<Locale>();
        for (PropertiesFileInfo info : infoList) {
            allLocales.addAll(info.getLocales());
        }
        
        List<LocaleInfo> localeInfoList = new ArrayList<LocaleInfo>();
        for (Locale l : allLocales) {
            localeInfoList.add(new LocaleInfo(l, true));
        }
        
        for (PropertiesFileInfo info : infoList) {
            List<Locale> locales = info.getLocales();
            for (LocaleInfo li : localeInfoList) {
                if (!locales.contains(li.locale)) {
                    li.fullySupported = false;
                }
            }
        }
        
        return localeInfoList;
    }
    
}
