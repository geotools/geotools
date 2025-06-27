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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // For javadoc
import org.geotools.api.metadata.Identifier;
import org.geotools.api.util.GenericName;
import org.geotools.api.util.InternationalString; // For javadoc

/**
 * A factory for {@link GenericName} objects.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class NameFactory {
    /** Do not allows instantiation of instance of this class. */
    private NameFactory() {}

    /**
     * Constructs a generic name from a fully qualified name and the default separator character.
     *
     * @param name The fully qualified name.
     */
    public static GenericName create(final String name) {
        return create(name, org.geotools.util.GenericName.DEFAULT_SEPARATOR);
    }

    /**
     * Constructs a generic name from a fully qualified name and the specified separator character.
     *
     * @param name The fully qualified name.
     * @param separator The separator character.
     */
    public static GenericName create(final String name, final char separator) {
        final List<String> names = new ArrayList<>();
        int lower = 0;
        while (true) {
            final int upper = name.indexOf(separator, lower);
            if (upper >= 0) {
                names.add(name.substring(lower, upper));
                lower = upper + 1;
            } else {
                names.add(name.substring(lower));
                break;
            }
        }
        return create(names.toArray(new String[names.size()]), separator);
    }

    /**
     * Constructs a generic name from an array of local names and the default separator character. If any of the
     * specified names is an {@link InternationalString}, then the <code>
     * {@linkplain InternationalString#toString(Locale) toString}(null)</code> method will be used in order to fetch an
     * unlocalized name. Otherwise, the <code>
     * {@linkplain CharSequence#toString toString}()</code> method will be used.
     *
     * @param names The local names as an array of strings or international strings. This array must contains at least
     *     one element.
     */
    public static GenericName create(final CharSequence... names) {
        return create(names, org.geotools.util.GenericName.DEFAULT_SEPARATOR);
    }

    /**
     * Constructs a generic name from an array of local names and the specified separator character. If any of the
     * specified names is an {@link InternationalString}, then the <code>
     * {@linkplain InternationalString#toString(Locale) toString}(null)</code> method will be used in order to fetch an
     * unlocalized name. Otherwise, the <code>
     * {@linkplain CharSequence#toString toString}()</code> method will be used.
     *
     * @param names The local names as an array of strings. This array must contains at least one element.
     * @param separator The separator character to use.
     */
    public static GenericName create(final CharSequence[] names, final char separator) {
        return create(names, names.length, separator);
    }

    /**
     * Constructs a generic name from an array of local names and the specified separator character.
     *
     * @param names The local names as an array of strings.
     * @param length The valid length of {@code names} array.
     * @param separator The separator character to use.
     */
    private static GenericName create(final CharSequence[] names, final int length, final char separator) {
        if (length <= 0) {
            throw new IllegalArgumentException(String.valueOf(length));
        }
        if (length == 1) {
            return new LocalName(names[0]);
        }
        return new ScopedName(create(names, length - 1, separator), separator, names[length - 1]);
    }

    /**
     * Returns the specified name in an array. The {@code value} may be either a {@link String}, {@code String[]},
     * {@link GenericName} or {@code GenericName[]}. This method is used in
     * {@link org.geotools.referencing.AbstractIdentifiedObject} constructors.
     *
     * @param value The object to cast into an array of generic names.
     * @return The generic names.
     * @throws ClassCastException if {@code value} can't be cast.
     */
    public static GenericName[] toArray(final Object value) throws ClassCastException {
        if (value instanceof GenericName[]) {
            return (GenericName[]) value;
        }
        if (value instanceof GenericName) {
            return new GenericName[] {(GenericName) value};
        }
        if (value instanceof CharSequence) {
            return new GenericName[] {create(value.toString())};
        }
        if (value instanceof CharSequence[]) {
            final CharSequence[] values = (CharSequence[]) value;
            final GenericName[] names = new GenericName[values.length];
            for (int i = 0; i < values.length; i++) {
                final CharSequence v = values[i];
                names[i] = v instanceof GenericName ? (GenericName) v : create(v.toString());
            }
            return names;
        }
        if (value instanceof Identifier[]) {
            final Identifier[] values = (Identifier[]) value;
            final GenericName[] names = new GenericName[values.length];
            for (int i = 0; i < values.length; i++) {
                final Identifier v = values[i];
                names[i] = v instanceof GenericName ? (GenericName) v : create(v.getCode());
            }
            return names;
        }
        // TODO: localize
        throw new ClassCastException("Cannot convert " + Classes.getShortClassName(value) + " to GenericName[]");
    }
}
