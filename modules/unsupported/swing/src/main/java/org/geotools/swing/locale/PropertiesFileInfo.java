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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Records the base name of a properties file and the {@code Locales}
 * that it supports.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class PropertiesFileInfo {
    private final String baseName;
    private final List<Locale> locales;

    /**
     * Creates a new instance.
     * 
     * @param baseName file base name
     * @param locales supported locales
     */
    public PropertiesFileInfo(String baseName, List<Locale> locales) {
        this.baseName = baseName;
        this.locales = new ArrayList<Locale>(locales);
    }

    /**
     * Gets the file base name.
     * 
     * @return file base name
     */
    public String getBaseName() {
        return baseName;
    }

    
    /**
     * Gets an unmodifiable view of the list of supported locales. This
     * will always contain at least {@linkplain Locale#ROOT}.
     * 
     * @return list of supported locales
     */
    public List<Locale> getLocales() {
        return Collections.unmodifiableList(locales);
    }
    
}
