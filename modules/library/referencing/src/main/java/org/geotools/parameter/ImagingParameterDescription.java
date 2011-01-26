/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.parameter;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.io.Serializable;

import javax.media.jai.OperationDescriptor;

import org.geotools.util.Utilities;
import org.geotools.util.AbstractInternationalString;


/**
 * A localized string for a JAI's operation parameter.
 * This is used by {@link ImagingParameterDescriptors}.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ImagingParameterDescription extends AbstractInternationalString implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -325584046563057577L;

    /**
     * Shared keys for arguments. Will be completed only as needed.
     */
    private static final String[] argumentKeys = new String[12];

    /**
     * The operation to fetch localized resource from.
     */
    private final OperationDescriptor operation;

    /**
     * The key for the resource to fetch.
     */
    private final String key;

    /**
     * Prefix to removes from the value associated to the {@code key}, or {@code null} if none.
     * This is usually the vendor key.
     */
    private final String prefixKey;

    /**
     * Creates a new international string from the specified operation and argument number.
     *
     * @param operation The operation to fetch localized resource from.
     * @param arg The argument number.
     */
    public ImagingParameterDescription(final OperationDescriptor operation, final int arg) {
        this.operation = operation;
        this.prefixKey = null;
        if (arg < argumentKeys.length) {
            String candidate = argumentKeys[arg];
            if (candidate != null) {
                key = candidate;
                return;
            }
        }
        key = "arg" + arg + "Desc";
        if (arg < argumentKeys.length) {
            argumentKeys[arg] = key;
        }
    }

    /**
     * Creates a new international string from the specified operation and key.
     *
     * @param operation The operation to fetch localized resource from.
     * @param key The key for the resource to fetch.
     */
    public ImagingParameterDescription(final OperationDescriptor operation,
                                       final String              key,
                                       final String              prefixKey)
    {
        this.operation = operation;
        this.key       = key;
        this.prefixKey = prefixKey;
    }

    /**
     * Tests if the resource exists.
     */
    public boolean exists() {
        try {
            // AbstractInternationalString.toString() never returns null.
            return toString().length() != 0;
        } catch (MissingResourceException exception) {
            return false;
        }
    }

    /**
     * Returns a string in the specified locale.
     *
     * @param  locale The locale to look for, or {@code null} for the default locale.
     * @return The string in the specified locale, or in a default locale.
     * @throws MissingResourceException is the key given to the constructor is invalid.
     */
    public String toString(Locale locale) throws MissingResourceException {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        final ResourceBundle resources = operation.getResourceBundle(locale);
        String name = resources.getString(key);
        if (prefixKey != null) {
            name = trimPrefix(name, resources.getString(prefixKey));
        }
        return name;
    }

    /**
     * If the specified name starts with the specified prefix, removes the prefix from
     * the name. This is used for removing the "org.geotools" part in operation name
     * like "org.geotools.NodataFilter" for example.
     */
    static String trimPrefix(String name, String prefix) {
        name = name.trim();
        if (prefix != null) {
            prefix = prefix.trim();
            final int offset = prefix.length();
            if (offset != 0) {
                if (name.startsWith(prefix)) {
                    final int length = name.length();
                    if (offset<length && name.charAt(offset)=='.') {
                        name = name.substring(offset + 1);
                    }
                }
            }
        }
        return name;
    }

    /**
     * Compares this international string with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final ImagingParameterDescription that = (ImagingParameterDescription) object;
            return Utilities.equals(this.key,       that.key)       &&
                   Utilities.equals(this.prefixKey, that.prefixKey) &&
                   Utilities.equals(this.operation, that.operation);
        }
        return false;
    }

    /**
     * Returns a hash code value for this international text.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ key.hashCode() ^ operation.hashCode();
    }
}
