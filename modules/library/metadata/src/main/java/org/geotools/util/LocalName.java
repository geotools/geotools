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

import java.util.Collections;
import java.util.List;

import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;
import org.opengis.util.NameSpace;
import org.opengis.util.ScopedName;


/**
 * Identifier within a name space for a local object. This could be the target object of the
 * {@link GenericName}, or a pointer to another name space (with a new {@link GenericName})
 * one step closer to the target of the identifier.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see NameFactory
 */
public class LocalName extends org.geotools.util.GenericName implements org.opengis.util.LocalName {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -5627125375582385822L;

    /**
     * The view of this object as a scoped name.
     */
    private final ScopedName asScopedName;

    /**
     * The name, either as a {@link String} or an {@link InternationalString}.
     */
    private final CharSequence name;

    /**
     * The name as a string.
     * If not provided, will be built only when first needed.
     */
    private transient String asString;

    /**
     * The name as an international string.
     * If not provided, will be built only when first needed.
     */
    private transient InternationalString asInternationalString;

    /**
     * The sequence of local name for this {@linkplain GenericName generic name}.
     * Since this object is itself a locale name, this list is always a singleton
     * containing only {@code this}. It will be built only when first needed.
     */
    private transient List<org.opengis.util.LocalName> parsedNames;

    /**
     * Constructs a local name from the specified string with no scope.
     * If the specified name is an {@link InternationalString}, then the
     * <code>{@linkplain InternationalString#toString(java.util.Locale) toString}(null)</code>
     * method will be used in order to fetch an unlocalized name. Otherwise, the
     * <code>{@linkplain CharSequence#toString toString}()</code> method will be used.
     *
     * @param name The local name (never {@code null}).
     */
    public LocalName(final CharSequence name) {
        this(null, name);
    }

    /**
     * Constructs a local name from the specified international string.
     *
     * This constructor is not public since it can't be used from outside
     * of {@link org.geotools.util.ScopedName} constructor (otherwise some
     * methods in this class may have the wrong semantic).
     *
     * @param asScopedName The view of this object as a scoped name.
     * @param name         The local name (never {@code null}).
     */
    LocalName(final ScopedName asScopedName, final CharSequence name) {
        this.asScopedName = asScopedName;
        this.name         = validate(name);
        AbstractInternationalString.ensureNonNull("name", name);
    }

    /**
     * Returns the scope (name space) of this generic name.
     * This method is protected from overriding by the user.
     */
    private GenericName getInternalScope() {
        if (asScopedName != null) {
            final NameSpace scope = asScopedName.scope();
            if (scope != null) {
                return scope.name();
            }
        }
        return null;
    }

    /**
     * Returns the scope (name space) of this generic name.
     *
     * @deprecated Replaced by {@link #scope}.
     */
    @Deprecated
    public GenericName getScope() {
        return getInternalScope();
    }

    /**
     * Returns the scope (name space) in which this name is local. The scope is set on creation
     * and is not modifiable. The scope of a name determines where a name "starts". For instance,
     * if a name has a {@linkplain #depth depth} of two ({@code "util.GenericName"}) and is
     * associated with a {@linkplain NameSpace name space} having the name {@code "org.opengis"},
     * then the fully qualified name would be {@code "org.opengis.util.GenericName"}.
     *
     * @since 2.3
     *
     * @todo To be strict, maybe we should returns {@code null} if there is no namespace.
     *       Current implementation returns a namespace instance whith a null name. This
     *       behavior is for transition from legacy API to later ISO 19103 revision and
     *       may change in future GeoTools version.
     */
    public NameSpace scope() {
        return (asScopedName!=null) ? asScopedName.scope() : super.scope();
    }

    /**
     * Returns the depth, which is always 1 for a local name.
     *
     * @since 2.3
     */
    public int depth() {
        return 1;
    }

    /**
     * Returns the sequence of local name for this {@linkplain GenericName generic name}.
     * Since this object is itself a locale name, this method always returns a singleton
     * containing only {@code this}.
     */
    public List<org.opengis.util.LocalName> getParsedNames() {
        // No need to sychronize: it is not a big deal if this object is built twice.
        if (parsedNames == null) {
            parsedNames = Collections.singletonList((org.opengis.util.LocalName) this);
        }
        return parsedNames;
    }

    /**
     * Since this object is already a local name, this method always returns {@code this}.
     */
    @Override
    public org.opengis.util.LocalName head() {
        return this;
    }

    /**
     * Since this object is already a local name, this method always returns {@code this}.
     */
    @Override
    public org.opengis.util.LocalName tip() {
        return this;
    }

    /**
     * Returns a view of this object as a scoped name,
     * or {@code null} if this name has no scope.
     *
     * @deprecated Replaced by {@link #toFullyQualifiedName}.
     */
    @Deprecated
    public ScopedName asScopedName() {
        return asScopedName;
    }

    /**
     * Returns a view of this name as a fully-qualified name. The {@linkplain #scope scope}
     * of a fully qualified name must be {@linkplain NameSpace#isGlobal global}. This method
     * never returns {@code null}.
     *
     * @since 2.3
     */
    public GenericName toFullyQualifiedName() {
        if (asScopedName == null) {
            return this;
        }
        return asScopedName;
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
     * </ul>
     * <p>
     * <strong>Note:</strong> Those conditions can be understood in terms of the Java
     * {@link Object#equals equals} method instead of the Java identity comparator {@code ==}.
     *
     * @since 2.3
     *
     * @todo Not yet implemented.
     */
    public ScopedName push(GenericName scope) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns a locale-independant string representation of this local name.
     * This string do not includes the scope, which is consistent with the
     * {@linkplain #getParsedNames parsed names} definition.
     */
    @Override
    public String toString() {
        if (asString == null) {
            if (name instanceof InternationalString) {
                // We really want the 'null' locale, not the system default one.
                asString = ((InternationalString) name).toString(null);
            } else {
                asString = name.toString();
            }
        }
        return asString;
    }

    /**
     * Returns a local-dependent string representation of this locale name.
     */
    @Override
    public InternationalString toInternationalString() {
        if (asInternationalString == null) {
            if (name instanceof InternationalString) {
                asInternationalString = (InternationalString) name;
            } else {
                asInternationalString = new SimpleInternationalString(name.toString());
            }
        }
        return asInternationalString;
    }

    /**
     * Compares this name with the specified object for order. Returns a negative integer,
     * zero, or a positive integer as this name lexicographically precedes, is equals to,
     * or follows the specified object. The comparaison is case-insensitive.
     */
    @Override
    public int compareTo(final GenericName object) {
        return toString().compareToIgnoreCase(object.toString());
    }

    /**
     * Compares this local name with the specified object for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object!=null && object.getClass().equals(getClass())) {
            final LocalName that = (LocalName) object;
            // Do not use 'asScopedName' in order to avoid never-ending loop.
            return Utilities.equals(this.getInternalScope(), that.getInternalScope()) &&
                   Utilities.equals(this.name,               that.name);
        }
        return false;
    }

    /**
     * Returns a hash code value for this local name.
     */
    @Override
    public int hashCode() {
        int code = (int)serialVersionUID;
        // Do not use 'asScopedName' in order to avoid never-ending loop.
        if (name != null) code ^= name.hashCode();
        return code;
    }
}
