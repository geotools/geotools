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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import java.util.Set;
import org.opengis.util.NameSpace;
import org.opengis.util.LocalName;
import org.opengis.util.ScopedName;  // For javadoc
import org.opengis.util.InternationalString;


/**
 * Base class for {@linkplain ScopedName generic scoped} and
 * {@linkplain LocalName local name} structure for type and attribute
 * name in the context of name spaces.
 * <p>
 * <b>Note:</b> this class has a natural ordering that is inconsistent with {@link #equals equals}.
 * The natural ordering may be case-insensitive and ignores the {@linkplain #DEFAULT_SEPARATOR
 * character separator} between name elements.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see NameFactory
 */
public abstract class GenericName implements org.opengis.util.GenericName, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8685047583179337259L;

    /**
     * The default separator character.
     */
    public static final char DEFAULT_SEPARATOR = ':';

    /**
     * The name space, created on the fly when needed. This is a temporary
     * approach until we replace this class by a better implementation.
     */
    private transient NameSpace namespace;

    /**
     * Creates a new instance of generic name.
     */
    protected GenericName() {
    }

    /**
     * Ensures that the given name is a {@link String} or an {@link InternationalString}.
     * This is used for subclass constructors.
     */
    static CharSequence validate(final CharSequence name) {
        return (name==null || name instanceof InternationalString) ? name : name.toString();
    }

    /**
     * Returns the scope (name space) in which this name is local. The scope is set on creation
     * and is not modifiable. The scope of a name determines where a name "starts". For instance,
     * if a name has a {@linkplain #depth depth} of two ({@code "util.GenericName"}) and is
     * associated with a {@linkplain NameSpace name space} having the name {@code "org.opengis"},
     * then the fully qualified name would be {@code "org.opengis.util.GenericName"}.
     *
     * @return The name space.
     *
     * @since 2.3
     *
     * @todo To be strict, maybe we should returns {@code null} if there is no namespace.
     *       Current implementation returns a namespace instance whith a null name. This
     *       behavior is for transition from legacy API to later ISO 19103 revision and
     *       may change in future GeoTools version.
     */
    public NameSpace scope() {
        if (namespace == null) {
            namespace = new NameSpace() {
                public boolean isGlobal() {
                    return false;
                }

                public org.opengis.util.GenericName name() {
                    return getScope();
                }

                @Deprecated public Set<org.opengis.util.GenericName> getNames() {
                    throw new UnsupportedOperationException();
                }
            };
        }
        return namespace;
    }

    /**
     * Returns the scope (name space) of this generic name. If this name has no scope
     * (e.g. is the root), then this method returns {@code null}.
     *
     * @return The name space of this name.
     * @deprecated Replaced by {@link #scope}.
     */
    @Deprecated
    public abstract org.opengis.util.GenericName getScope();

    /**
     * Returns the depth of this name within the namespace hierarchy.  This indicates the number
     * of levels specified by this name.  For any {@link LocalName}, it is always one.  For a
     * {@link ScopedName} it is some number greater than or equal to 2.
     * <p>
     * The depth is the length of the list returned by the {@link #getParsedNames} method.
     * As such it is a derived parameter.
     *
     * @return The depth of this name.
     *
     * @since 2.3
     */
    public int depth() {
        return getParsedNames().size();
    }

    /**
     * Returns the sequence of {@linkplain LocalName local names} making this generic name.
     * Each element in this list is like a directory name in a file path name.
     * The length of this sequence is the generic name depth.
     *
     * @return The sequence of local names.
     */
    public abstract List<LocalName> getParsedNames();

    /**
     * Returns the first element in the sequence of {@linkplain #getParsedNames parsed names}.
     * For any {@link LocalName}, this is always {@code this}.
     *
     * @return The first element of this name.
     *
     * @since 2.6
     */
    public LocalName head() {
        final List<? extends LocalName> names = getParsedNames();
        return names.get(0);
    }

    /**
     * Returns the last element in the sequence of {@linkplain #getParsedNames parsed names}.
     * For any {@link LocalName}, this is always {@code this}.
     *
     * @return The last element of this name.
     *
     * @since 2.6
     */
    public LocalName tip() {
        final List<? extends LocalName> names = getParsedNames();
        return names.get(names.size() - 1);
    }

    /**
     * Returns a view of this object as a local name.
     *
     * @return The local part of this name.
     * @deprecated Renamed as {@link #tip()}.
     */
    @Deprecated
    public LocalName asLocalName() {
        return tip();
    }

    /**
     * @deprecated Renamed as {@link #tip()}.
     *
     * @since 2.3
     */
    @Deprecated
    public LocalName name() {
        return tip();
    }

    /**
     * Returns a view of this name as a fully-qualified name, or {@code null} if none.
     * The {@linkplain #scope scope} of a fully qualified name must be
     * {@linkplain NameSpace#isGlobal global}.
     * <p>
     * If this name is a {@linkplain LocalName local name} and the {@linkplain #scope scope}
     * is already {@linkplain NameSpace#isGlobal global}, returns {@code null} since it is not
     * possible to derive a scoped name.
     *
     * @return The fully-qualified name.
     * @deprecated Replaced by {@link #toFullyQualifiedName}.
     */
    @Deprecated
    public abstract ScopedName asScopedName();

    /**
     * Returns the separator character. Default to <code>':'</code>.
     * This method is overridden by {@link org.geotools.util.ScopedName}.
     */
    char getSeparator() {
        return DEFAULT_SEPARATOR;
    }

    /**
     * Returns a string representation of this generic name. This string representation
     * is local-independant. It contains all elements listed by {@link #getParsedNames}
     * separated by an arbitrary character (usually {@code :} or {@code /}).
     * This rule implies that the {@code toString()} method for a
     * {@linkplain ScopedName scoped name} will contains the scope, while the
     * {@code toString()} method for the {@linkplain LocalName local version} of
     * the same name will not contains the scope.
     *
     * @return A string representation of this name.
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        final List<? extends LocalName> parsedNames = getParsedNames();
        final char separator = getSeparator();
        for (final Iterator<? extends LocalName> it=parsedNames.iterator(); it.hasNext();) {
            if (buffer.length() != 0) {
                buffer.append(separator);
            }
            buffer.append(it.next());
        }
        return buffer.toString();
    }

    /**
     * Returns a local-dependent string representation of this generic name. This string
     * is similar to the one returned by {@link #toString} except that each element has
     * been localized in the {@linkplain InternationalString#toString(Locale)
     * specified locale}. If no international string is available, then this method should
     * returns an implementation mapping to {@link #toString} for all locales.
     *
     * @return A localizable string representation of this name.
     */
    public InternationalString toInternationalString() {
        return new International(getParsedNames(), getSeparator());
    }

    /**
     * An international string built from a snapshot of {@link GenericName}.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    private static final class International extends AbstractInternationalString
                                          implements Serializable
    {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = -4234089612436334148L;

        /**
         * The sequence of {@linkplain LocalName local names} making this generic name.
         * This is the value returned by {@link GenericName#getParsedNames}.
         */
        private final List<? extends LocalName> parsedNames;

        /**
         * The separator character. This is the value returned by {@link GenericName#getSeparator}.
         */
        private final char separator;

        /**
         * Constructs a new international string from the specified {@link GenericName} fields.
         *
         * @param parsedNames The value returned by {@link GenericName#getParsedNames}.
         * @param separator   The value returned by {@link GenericName#getSeparator}.
         */
        public International(final List<? extends LocalName> parsedNames, final char separator) {
            this.parsedNames = parsedNames;
            this.separator   = separator;
        }

        /**
         * Returns a string representation for the specified locale.
         */
        public String toString(final Locale locale) {
            final StringBuilder buffer = new StringBuilder();
            for (final LocalName name : parsedNames) {
                if (buffer.length() != 0) {
                    buffer.append(separator);
                }
                buffer.append(name.toInternationalString().toString(locale));
            }
            return buffer.toString();
        }

        /**
         * Compares this international string with the specified object for equality.
         */
        @Override
        public boolean equals(final Object object) {
            if (object!=null && object.getClass().equals(getClass())) {
                final International that = (International) object;
                return Utilities.equals(this.parsedNames, that.parsedNames) &&
                                        this.separator == that.separator;
            }
            return false;
        }

        /**
         * Returns a hash code value for this international text.
         */
        @Override
        public int hashCode() {
            return (int)serialVersionUID ^ parsedNames.hashCode();
        }
    }

    /**
     * Compares this name with the specified object for order. Returns a negative integer,
     * zero, or a positive integer as this name lexicographically precedes, is equals to,
     * or follows the specified object. The comparaison is performed in the following
     * order:
     * <ul>
     *   <li>Compares each element in the {@linkplain #getParsedNames list of parsed names}. If an
     *       element of this name lexicographically precedes or follows the corresponding element
     *       of the specified name, returns a negative or a positive integer respectively.</li>
     *   <li>If all elements in both names are lexicographically equal, then if this name has less
     *       or more elements than the specified name, returns a negative or a positive integer
     *       respectively.</li>
     *   <li>Otherwise, returns 0.</li>
     * </ul>
     *
     * @param that The name to compare with this name.
     * @return -1 if this name precedes the given one, +1 if it follows, 0 if equals.
     */
    public int compareTo(final org.opengis.util.GenericName that) {
        final Iterator<? extends LocalName> thisNames = this.getParsedNames().iterator();
        final Iterator<? extends LocalName> thatNames = that.getParsedNames().iterator();
        while (thisNames.hasNext()) {
            if (!thatNames.hasNext()) {
                return +1;
            }
            final LocalName thisNext = thisNames.next();
            final LocalName thatNext = thatNames.next();
            if (thisNext==this && thatNext==that) {
                // Never-ending loop: usually an implementation error
                throw new IllegalStateException();
            }
            final int compare = thisNext.compareTo(thatNext);
            if (compare != 0) {
                return compare;
            }
        }
        return thatNames.hasNext() ? -1 : 0;
    }

    /**
     * Compares this generic name with the specified object for equality.
     *
     * @param object The object to compare with this name.
     * @return {@code true} if the given object is equals to this one.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final GenericName that = (GenericName) object;
            return Utilities.equals(this.getParsedNames(), that.getParsedNames()) &&
                                    this.getSeparator() == that.getSeparator();
        }
        return false;
    }

    /**
     * Returns a hash code value for this generic name.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ getParsedNames().hashCode();
    }
}
