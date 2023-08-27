/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import org.geotools.util.IndexedResourceBundle;

/**
 * Base class for locale-dependent resources. Instances of this class should never been created
 * directly. Use the factory method {@link #getResources} or use static convenience methods instead.
 *
 * @since 2.2
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Errors extends IndexedResourceBundle {
    /**
     * Returns resources in the given locale.
     *
     * @param locale The locale, or {@code null} for the default locale.
     * @return Resources in the given locale.
     * @throws MissingResourceException if resources can't be found.
     */
    public static Errors getResources(Locale locale) throws MissingResourceException {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return (Errors) getBundle(Errors.class.getName(), locale);
        /*
         * We rely on cache capability of ResourceBundle.
         */
    }

    public static String getPattern(int key) {
        return getResources(null).getString(key);
    }
}
