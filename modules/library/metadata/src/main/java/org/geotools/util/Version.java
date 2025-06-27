/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Holds a version number. Versions are often of the form <code>{@linkplain #getMajor
 * major}.{@linkplain #getMinor minor}.{@linkplain #getRevision revision}</code>, but are not required to. For example
 * an EPSG database version is {@code "6.11.2"}. The separator character is the dot.
 *
 * <p>This class provides convenience methods for fetching the major, minor and reversion numbers, and for performing
 * comparaisons.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 * @see org.geotools.util.factory.GeoTools#getVersion
 */
public class Version implements CharSequence, Comparable<Version>, Serializable {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = -6793384507333713770L;

    /** The pattern to use for splitting version numbers. */
    private static final Pattern PATTERN = Pattern.compile("(\\.|\\-)");

    /** The version in string form, with leading and trailing spaces removed. */
    private final String version;

    /** The components of the version string. Will be created when first needed. */
    private transient String[] components;

    /** The parsed components of the version string. Will be created when first needed. */
    private transient Comparable<?>[] parsed;

    /** The hash code value. Will be computed when first needed. */
    private transient int hashCode;

    /**
     * Creates a new version object from the supplied string.
     *
     * @param version The version as a string.
     */
    public Version(final String version) {
        this.version = version.trim();
    }

    /**
     * Returns the major version number. This method returns an {@link Integer} if possible, or a {@link String}
     * otherwise.
     *
     * @return The major version number.
     */
    public Comparable<?> getMajor() {
        return getComponent(0);
    }

    /**
     * Returns the minor version number. This method returns an {@link Integer} if possible, or a {@link String}
     * otherwise. If there is no minor version number, then this method returns {@code null}.
     *
     * @return The minor version number, or {@code null} if none.
     */
    public Comparable<?> getMinor() {
        return getComponent(1);
    }

    /**
     * Returns the revision number. This method returns an {@link Integer} if possible, or a {@link String} otherwise.
     * If there is no revision number, then this method returns {@code null}.
     *
     * @return The revision number, or {@code null} if none.
     */
    public Comparable<?> getRevision() {
        return getComponent(2);
    }

    /**
     * Returns the specified components of this version string. For a version of the {@code major.minor.revision} form,
     * index 0 stands for the major version number, 1 stands for the minor version number and 2 stands for the revision
     * number.
     *
     * <p>The return value is an {@link Integer} if the component is parsable as an integer, or a {@link String}
     * otherwise. If there is no component at the specified index, then this method returns {@code null}.
     *
     * @param index The index of the component to fetch.
     * @return The value at the specified index, or {@code null} if none.
     * @throws IndexOutOfBoundsException if {@code index} is negative.
     */
    public synchronized Comparable<?> getComponent(final int index) {
        if (parsed == null) {
            if (components == null) {
                components = PATTERN.split(version);
            }
            parsed = new Comparable[components.length];
        }
        if (index >= parsed.length) {
            return null;
        }
        Comparable<?> candidate = parsed[index];
        if (candidate == null) {
            final String value = components[index].trim();
            try {
                candidate = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                candidate = value;
            }
            parsed[index] = candidate;
        }
        return candidate;
    }

    /**
     * Get the rank of the specified object according this type. This is for {@link #compareTo(Version, int)} internal
     * only.
     */
    private static int getTypeRank(final Object value) {
        if (value instanceof CharSequence) {
            return 0;
        }
        if (value instanceof Number) {
            return 1;
        }
        throw new IllegalArgumentException(String.valueOf(value));
    }

    /**
     * Compares this version with an other version object, up to the specified limit. A limit of 1 compares only the
     * {@linkplain #getMajor major} version number. A limit of 2 compares the major and {@linkplain #getMinor minor}
     * version numbers, <cite>etc</cite>. The comparaisons are performed as {@link Integer} object if possible, or as
     * {@link String} otherwise.
     *
     * @param other The other version object to compare with.
     * @param limit The maximum number of components to compare.
     * @return A negative value if this version is lower than the supplied version, a positive value if it is higher, or
     *     0 if they are equal.
     */
    public int compareTo(final Version other, final int limit) {
        for (int i = 0; i < limit; i++) {
            final Comparable<?> v1 = this.getComponent(i);
            final Comparable<?> v2 = other.getComponent(i);
            if (v1 == null) {
                return v2 == null ? 0 : -1;
            } else if (v2 == null) {
                return +1;
            }
            final int dr = getTypeRank(v1) - getTypeRank(v2);
            if (dr != 0) {
                /*
                 * One value is a text while the other value is a number.  We could be tempted to
                 * force a comparaison by converting the number to a String and then invoking the
                 * String.compareTo(String) method, but this strategy would violate the following
                 * contract from Comparable.compareTo(Object):  "The implementor must also ensure
                 * that the relation is transitive". Use case:
                 *
                 *    A is the integer 10
                 *    B is the string "8Z"
                 *    C is the integer 5.
                 *
                 * If mismatched types are converted to String before being compared, then we
                 * would have A < B < C. Transitivity implies that A < C, but if we compare A
                 * and C directly we get A > C because they are compared as numbers.  An easy
                 * way to fix this inconsistency is to define all String as lexicographically
                 * preceding Integer, no matter their content. This is what we do here.
                 */
                return dr;
            }
            @SuppressWarnings("unchecked")
            final int c = ((Comparable) v1).compareTo(v2);
            if (c != 0) {
                return c;
            }
        }
        return 0;
    }

    /**
     * Compares this version with an other version object. This method performs the same comparaison than
     * {@link #compareTo(Version, int)} with no limit.
     *
     * @param other The other version object to compare with.
     * @return A negative value if this version is lower than the supplied version, a positive value if it is higher, or
     *     0 if they are equal.
     */
    @Override
    public int compareTo(final Version other) {
        return compareTo(other, Integer.MAX_VALUE);
    }

    /**
     * Compare this version string with the specified object for equality. Two version are considered equal if <code>
     * {@linkplain #compareTo(Object) compareTo}(other) == 0</code>.
     *
     * @param other The object to compare with this version for equality.
     */
    @Override
    public boolean equals(final Object other) {
        if (other != null && getClass().equals(other.getClass())) {
            return compareTo((Version) other) == 0;
        }
        return false;
    }

    /** Returns the length of the version string. */
    @Override
    public int length() {
        return version.length();
    }

    /** Returns the {@code char} value at the specified index. */
    @Override
    public char charAt(final int index) {
        return version.charAt(index);
    }

    /** Returns a new version string that is a subsequence of this sequence. */
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return version.subSequence(start, end);
    }

    /** Returns the version string. This is the string specified at construction time. */
    @Override
    public String toString() {
        return version;
    }

    /** Returns a hash code value for this version. */
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            int code = (int) serialVersionUID;
            int index = 0;
            Comparable<?> component;
            while ((component = getComponent(index)) != null) {
                code = code * 37 + component.hashCode();
                index++;
            }
            hashCode = code;
        }
        return hashCode;
    }
}
