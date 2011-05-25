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

import java.util.Arrays;
import java.util.List;
import java.util.Locale; // For javadoc

import org.opengis.util.GenericName;
import org.opengis.util.InternationalString; // For javadoc
import org.opengis.util.LocalName;
import org.opengis.util.NameSpace;


/**
 * Fully qualified identifier for an object.
 * A {@code ScopedName} contains a {@link LocalName} as
 * {@linkplain #asLocalName head} and a {@linkplain GenericName},
 * which may be a {@link LocalName} or an other {@link org.opengis.util.ScopedName},
 * as {@linkplain #getScope tail}.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see NameFactory
 */
public class ScopedName extends org.geotools.util.GenericName
                     implements org.opengis.util.ScopedName
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7664125655784137729L;

    /**
     * The scope of this variable (also know as the "tail").
     */
    private final GenericName scope;

    /**
     * The separator character.
     */
    private final char separator;

    /**
     * The head as a local name.
     */
    private final LocalName name;

    /**
     * The list of parsed names. Will be constructed only when first needed.
     */
    private transient List<LocalName> parsedNames;

    /**
     * Constructs a scoped name from the specified international string.
     * If the specified name is an {@link InternationalString}, then the
     * <code>{@linkplain InternationalString#toString(Locale) toString}(null)</code>
     * method will be used in order to fetch an unlocalized name. Otherwise, the
     * <code>{@linkplain CharSequence#toString toString}()</code> method will be used.
     *
     * @param scope The scope (or "tail") of the variable.
     * @param name  The head (never {@code null}).
     */
    public ScopedName(final GenericName scope, final CharSequence name) {
        this(scope, DEFAULT_SEPARATOR, name);
    }

    /**
     * Constructs a scoped name from the specified international string.
     * If the specified name is an {@link InternationalString}, then the
     * <code>{@linkplain InternationalString#toString(Locale) toString}(null)</code>
     * method will be used in order to fetch an unlocalized name. Otherwise, the
     * <code>{@linkplain CharSequence#toString toString}()</code> method will be used.
     *
     * @param scope     The scope (or "tail") of the variable.
     * @param separator The separator character (usually <code>':'</code> or <code>'/'</code>).
     * @param name      The head (never {@code null}).
     */
    public ScopedName(final GenericName scope, final char separator, final CharSequence name) {
        AbstractInternationalString.ensureNonNull("scope", scope);
        AbstractInternationalString.ensureNonNull("name",  name);
        this.scope     = scope;
        this.separator = separator;
        this.name      = new org.geotools.util.LocalName(this, name);
    }

    /**
     * Returns the head of this scoped name. This is the first elements in the sequence of
     * {@linkplain #getParsedNames parsed names}. The head element must exists in the same
     * {@linkplain NameSpace name space} than this scoped name. In other words, the following
     * relationship must holds:
     * <p>
     * <ul>
     *   <li><code>head().scope() == this.{@linkplain #scope scope()}</code></li>
     * </ul>
     *
     * @since 2.3
     *
     * @todo Not yet implemented.
     */
    @Override
    public LocalName head() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Returns the tail of this scoped name. The returned name contains every elements of the
     * {@linkplain #getParsedNames parsed names list} except for the first one, which is the
     * {@linkplain #head head}. In other words, the following relationship must holds:
     * <p>
     * <ul>
     *   <li><code>tail().getParsedNames() == this.{@linkplain #getParsedNames getParsedNames()}.sublist(1,end)</code></li>
     * </ul>
     * <p>
     * <strong>Note:</strong> This condition can be understood in terms of the Java
     * {@link java.util.List#equals equals} method instead of the Java identity
     * comparator {@code ==}.
     *
     * @since 2.3
     *
     * @todo Not yet implemented.
     */
    public GenericName tail() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Returns a name which contains every element of the
     * {@linkplain #getParsedNames parsed names list} except for the last element.
     *
     * @see java.io.File#getPath
     *
     * @since 2.3
     *
     * @todo Not yet implemented.
     */
    public GenericName path() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Returns the scope of this name.
     *
     * @deprecated Replaced by {@link #scope()}.
     */
    @Deprecated
    public GenericName getScope() {
        return scope;
    }

    /**
     * Returns the separator character.
     */
    @Override
    public char getSeparator() {
        return separator;
    }

    /**
     * Returns a view of this object as a scoped name. Since this object is already
     * a scoped name, this method always returns {@code this}.
     *
     * @deprecated Replaced by {@link #toFullyQualifiedName}.
     */
    @Deprecated
    public org.opengis.util.ScopedName asScopedName() {
        return this;
    }

    /**
     * Returns a view of this object as a local name. This is the last element in the
     * sequence of {@linkplain #getParsedNames parsed names}. The local name returned
     * by this method will still have the same {@linkplain LocalName#getScope scope}
     * than this scoped name. Note however that the string returned by
     * {@link LocalName#toString} will differs.
     */
    @Override
    public LocalName tip() {
        return name;
    }

    /**
     * Returns the sequence of local name for this {@linkplain GenericName generic name}.
     */
    public List<LocalName> getParsedNames() {
        if (parsedNames == null) {
            final List<? extends LocalName> parents = scope.getParsedNames();
            final int size = parents.size();
            LocalName[] names = new LocalName[size + 1];
            names = parents.toArray(names);
            names[size] = name;
            parsedNames = Arrays.asList(names);
        }
        return parsedNames;
    }

    /**
     * Returns a view of this name as a fully-qualified name. The {@linkplain #scope scope}
     * of a fully qualified name must be {@linkplain NameSpace#isGlobal global}. This method
     * never returns {@code null}.
     *
     * @since 2.3
     */
    public GenericName toFullyQualifiedName() {
        return this;
    }

    /**
     * Returns this name expanded with the specified scope. One may represent this operation
     * as a concatenation of the specified {@code name} with {@code this}. In pseudo-code,
     * the following relationships must hold:
     * <p>
     * <ul>
     *   <li><code>push(<var>name</var>).getParsedList() ==
     *       <var>name</var>.getParsedList().addAll({@linkplain #getParsedNames()})</code></li>
     *   <li><code>push(<var>name</var>).scope() == <var>name</var>.{@linkplain #scope()}</code></li>
     *   <li><code>push({@linkplain ScopedName#head head()}).{@linkplain ScopedName#tail tail()} == this</code></li>
     * </ul>
     * <p>
     * <strong>Note:</strong> Those conditions can be understood in terms of the Java
     * {@link Object#equals equals} method instead of the Java identity comparator {@code ==}.
     *
     * @since 2.3
     *
     * @todo Not yet implemented.
     */
    public org.opengis.util.ScopedName push(GenericName scope) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    /**
     * Compares this scoped name with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object!=null && object.getClass().equals(getClass())) {
            final ScopedName that = (ScopedName) object;
            return Utilities.equals(this.name,  that.name);
            // No need to checks the scope, since the LocalName implementation
            // should checks it.
        }
        return false;
    }

    /**
     * Returns a hash code value for this generic name.
     */
    @Override
    public int hashCode() {
        return (int)serialVersionUID ^ name.hashCode() ^ scope.hashCode();
    }
}
