/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;


/**
 * A simple international string consisting of a single string for all locales.
 * For such a particular case, this implementation is the more effective than
 * other implementations provided in this package.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class SimpleInternationalString extends AbstractInternationalString implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 3543963804501667578L;

    /**
     * Creates a new instance of international string.
     *
     * @param message The string for all locales.
     */
    public SimpleInternationalString(final String message) {
        defaultValue = message;
        ensureNonNull("message", message);
    }

    /**
     * If the specified string is null or an instance of
     * {@link AbstractInternationalString}, returns it unchanged.
     * Otherwise, wraps the string value in a {@code SimpleInternationalString}.
     *
     * @param string The string to wrap.
     * @return The given string as an international string.
     */
    public static AbstractInternationalString wrap(final CharSequence string) {
        if (string==null || string instanceof AbstractInternationalString) {
            return (AbstractInternationalString) string;
        }
        return new SimpleInternationalString(string.toString());
    }

    /**
     * Returns the same string for all locales. This is the string given to the constructor.
     */
    public String toString(final Locale locale) {
        return defaultValue;
    }

    /**
     * Compares this international string with the specified object for equality.
     *
     * @param object The object to compare with this international string.
     * @return {@code true} if the given object is equals to this string.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final SimpleInternationalString that = (SimpleInternationalString) object;
            return Utilities.equals(this.defaultValue, that.defaultValue);
        }
        return false;
    }

    /**
     * Returns a hash code value for this international text.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ defaultValue.hashCode();
    }

    /**
     * Write the string. This is required since {@link #defaultValue} is not serialized.
     */
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(defaultValue);
    }

    /**
     * Read the string. This is required since {@link #defaultValue} is not serialized.
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        defaultValue = in.readUTF();
    }
}
