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
package org.geotools.referencing;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;

import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.util.GenericName;
import org.opengis.util.InternationalString;
import org.opengis.util.LocalName;
import org.opengis.util.NameSpace;
import org.opengis.util.ScopedName;
import static org.opengis.referencing.IdentifiedObject.REMARKS_KEY;

import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.GrowableInternationalString;
import org.geotools.util.WeakValueHashMap;
import org.geotools.util.logging.Logging;
import org.geotools.util.Utilities;


/**
 * An identification of a CRS object. The main interface implemented by this class is
 * {@link ReferenceIdentifier}. However, this class also implements {@link GenericName}
 * in order to make it possible to reuse the same identifiers in the list of
 * {@linkplain AbstractIdentifiedObject#getAlias aliases}. Casting an alias's
 * {@linkplain GenericName generic name} to an {@linkplain ReferenceIdentifier identifier}
 * gives access to more informations, like the URL of the authority.
 * <P>
 * The {@linkplain GenericName generic name} will be infered from
 * {@linkplain ReferenceIdentifier identifier} attributes. More specifically, a
 * {@linkplain ScopedName scoped name} will be constructed using the shortest authority's
 * {@linkplain Citation#getAlternateTitles alternate titles} (or the
 * {@linkplain Citation#getTitle main title} if there is no alternate titles) as the
 * {@linkplain ScopedName#getScope scope}, and the {@linkplain #getCode code} as the
 * {@linkplain ScopedName#asLocalName head}. This heuristic rule seems raisonable
 * since, according ISO 19115, the {@linkplain Citation#getAlternateTitles alternate
 * titles} often contains abreviation (for example "DCW" as an alternative title for
 * "<cite>Digital Chart of the World</cite>").
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class NamedIdentifier implements ReferenceIdentifier, GenericName,
                                        Comparable<GenericName>, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8474731565582774497L;

    /**
     * A pool of {@link LocalName} values for given {@link InternationalString}.
     * Will be constructed only when first needed.
     */
    private static Map<CharSequence,GenericName> SCOPES;

    /**
     * Identifier code or name, optionally from a controlled list or pattern
     * defined by a code space.
     */
    private final String code;

    /**
     * Name or identifier of the person or organization responsible for namespace.
     */
    private final String codespace;

    /**
     * Organization or party responsible for definition and maintenance of the
     * code space or code.
     */
    private final Citation authority;

    /**
     * Identifier of the version of the associated code space or code, as specified
     * by the code space or code authority. This version is included only when the
     * {@linkplain #getCode code} uses versions. When appropriate, the edition is
     * identified by the effective date, coded using ISO 8601 date format.
     */
    private final String version;

    /**
     * Comments on or information about this identifier, or {@code null} if none.
     */
    private final InternationalString remarks;

    /**
     * The name of this identifier as a generic name. If {@code null}, will
     * be constructed only when first needed. This field is serialized (instead
     * of being recreated after deserialization) because it may be a user-supplied
     * value.
     */
    private GenericName name;

    /**
     * Constructs an identifier from a set of properties. Keys are strings from the table below.
     * Key are case-insensitive, and leading and trailing spaces are ignored. The map given in
     * argument shall contains at least a <code>"code"</code> property. Other properties listed
     * in the table below are optional.
     * <p>
     * <table border='1'>
     *   <tr bgcolor="#CCCCFF" class="TableHeadingColor">
     *     <th nowrap>Property name</th>
     *     <th nowrap>Value type</th>
     *     <th nowrap>Value given to</th>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #CODE_KEY "code"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getCode}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #CODESPACE_KEY "code"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getCodeSpace}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #AUTHORITY_KEY "authority"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String} or {@link Citation}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getAuthority}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #VERSION_KEY "version"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getVersion}</td>
     *   </tr>
     *   <tr>
     *     <td nowrap>&nbsp;{@link #REMARKS_KEY "remarks"}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link String} or {@link InternationalString}&nbsp;</td>
     *     <td nowrap>&nbsp;{@link #getRemarks}</td>
     *   </tr>
     * </table>
     *
     * <P><code>"remarks"</code> is a localizable attributes which may have a language and country
     * code suffix. For example the <code>"remarks_fr"</code> property stands for remarks in
     * {@linkplain Locale#FRENCH French} and the <code>"remarks_fr_CA"</code> property stands
     * for remarks in {@linkplain Locale#CANADA_FRENCH French Canadian}.</P>
     *
     * @param properties The properties to be given to this identifier.
     * @throws InvalidParameterValueException if a property has an invalid value.
     * @throws IllegalArgumentException if a property is invalid for some other reason.
     */
    public NamedIdentifier(final Map<String,?> properties) throws IllegalArgumentException {
        this(properties, true);
    }

    /**
     * Constructs an identifier from an authority and code informations. This is a convenience
     * constructor for commonly-used parameters. If more control are wanted (for example adding
     * remarks), use the {@linkplain #NamedIdentifier(Map) constructor with a properties map}.
     *
     * @param authority The authority (e.g. {@link Citations#OGC OGC} or {@link Citations#EPSG EPSG}).
     * @param code      The code. The {@linkplain Locale#US English name} is used
     *                  for the code, and the international string is used for the
     *                  {@linkplain GenericName generic name}.
     */
    public NamedIdentifier(final Citation authority, final InternationalString code) {
        // The "null" locale argument is required for getting the unlocalized version.
        this(authority, code.toString(null));
        name = getName(authority, code);
    }

    /**
     * Constructs an identifier from an authority and code informations. This is a convenience
     * constructor for commonly-used parameters. If more control are wanted (for example adding
     * remarks), use the {@linkplain #NamedIdentifier(Map) constructor with a properties map}.
     *
     * @param authority The authority (e.g. {@link Citations#OGC OGC} or {@link Citations#EPSG EPSG}).
     * @param code      The code. This parameter is mandatory.
     */
    public NamedIdentifier(final Citation authority, final String code) {
        this(authority, code, null);
    }

    /**
     * Constructs an identifier from an authority and code informations. This is a convenience
     * constructor for commonly-used parameters. If more control are wanted (for example adding
     * remarks), use the {@linkplain #NamedIdentifier(Map) constructor with a properties map}.
     *
     * @param authority The authority (e.g. {@link Citations#OGC OGC} or {@link Citations#EPSG EPSG}).
     * @param code      The code. This parameter is mandatory.
     * @param version   The version, or {@code null} if none.
     */
    public NamedIdentifier(final Citation authority, final String code, final String version) {
        this(toMap(authority, code, version));
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private static Map<String,?> toMap(final Citation authority,
                                       final String   code,
                                       final String   version)
    {
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        if (authority != null) properties.put(AUTHORITY_KEY, authority);
        if (code      != null) properties.put(     CODE_KEY, code     );
        if (version   != null) properties.put(  VERSION_KEY, version  );
        return properties;
    }

    /**
     * Implementation of the constructor. The remarks in the {@code properties} will be
     * parsed only if the {@code standalone} argument is set to {@code true}, i.e.
     * this identifier is being constructed as a standalone object. If {@code false}, then
     * this identifier is assumed to be constructed from inside the {@link AbstractIdentifiedObject}
     * constructor.
     *
     * @param properties The properties to parse, as described in the public constructor.
     * @param standalone {@code true} for parsing "remarks" as well.
     *
     * @throws InvalidParameterValueException if a property has an invalid value.
     * @throws IllegalArgumentException if a property is invalid for some other reason.
     */
    NamedIdentifier(final Map<String,?> properties, final boolean standalone)
            throws IllegalArgumentException
    {
        ensureNonNull("properties", properties);
        Object code      = null;
        Object codespace = null;
        Object version   = null;
        Object authority = null;
        Object remarks   = null;
        GrowableInternationalString growable = null;
        /*
         * Iterate through each map entry. This have two purposes:
         *
         *   1) Ignore case (a call to properties.get("foo") can't do that)
         *   2) Find localized remarks.
         *
         * This algorithm is sub-optimal if the map contains a lot of entries of no interest to
         * this identifier. Hopefully, most users will fill a map only with usefull entries.
         */
        String key   = null;
        Object value = null;
        for (final Map.Entry<String,?> entry : properties.entrySet()) {
            key   = entry.getKey().trim().toLowerCase();
            value = entry.getValue();
            /*
             * Note: String.hashCode() is part of J2SE specification,
             *       so it should not change across implementations.
             */
            switch (key.hashCode()) {
                case 3373707: {
                    if (!standalone && key.equals("name")) {
                        code = value;
                        continue;
                    }
                    break;
                }
                case 3059181: {
                    if (key.equals(CODE_KEY)) {
                        code = value;
                        continue;
                    }
                    break;
                }
                case -1108676807: {
                    if (key.equals(CODESPACE_KEY)) {
                        codespace = value;
                        continue;
                    }
                    break;

                }
                case 351608024: {
                    if (key.equals(VERSION_KEY)) {
                        version = value;
                        continue;
                    }
                    break;
                }
                case 1475610435: {
                    if (key.equals(AUTHORITY_KEY)) {
                        if (value instanceof String) {
                            value = Citations.fromName(value.toString());
                        }
                        authority = value;
                        continue;
                    }
                    break;
                }
                case 1091415283: {
                    if (standalone && key.equals(REMARKS_KEY)) {
                        if (value instanceof InternationalString) {
                            remarks = value;
                            continue;
                        }
                    }
                    break;
                }
            }
            /*
             * Search for additional locales (e.g. "remarks_fr").
             */
            if (standalone && value instanceof String) {
                if (growable == null) {
                    if (remarks instanceof GrowableInternationalString) {
                        growable = (GrowableInternationalString) remarks;
                    } else {
                        growable = new GrowableInternationalString();
                    }
                }
                growable.add(REMARKS_KEY, key, value.toString());
            }
        }
        /*
         * Get the localized remarks, if it was not yet set. If a user specified remarks
         * both as InternationalString and as String for some locales (which is a weird
         * usage...), then current implementation discart the later with a warning.
         */
        if (growable!=null && !growable.getLocales().isEmpty()) {
            if (remarks == null) {
                remarks = growable;
            } else {
                final Logger logger = Logging.getLogger(NamedIdentifier.class);
                final LogRecord record = Loggings.format(Level.WARNING, LoggingKeys.LOCALES_DISCARTED);
                record.setLoggerName(logger.getName());
                logger.log(record);
            }
        }
        /*
         * Completes the code space if it was not explicitly set. We take the first
         * identifier if there is any, otherwise we take the shortest title.
         */
        if (codespace == null && authority instanceof Citation) {
            codespace = getCodeSpace((Citation) authority);
        }
        /*
         * Stores the definitive reference to the attributes. Note that casts are performed only
         * there (not before). This is a wanted feature, since we want to catch ClassCastExceptions
         * are rethrown them as more informative exceptions.
         */
        try {
            key=      CODE_KEY; this.code      = (String)              (value=code);
            key=   VERSION_KEY; this.version   = (String)              (value=version);
            key= CODESPACE_KEY; this.codespace = (String)              (value=codespace);
            key= AUTHORITY_KEY; this.authority = (Citation)            (value=authority);
            key=   REMARKS_KEY; this.remarks   = (InternationalString) (value=remarks);
        } catch (ClassCastException exception) {
            InvalidParameterValueException e = new InvalidParameterValueException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, key, value), key, value);
            e.initCause(exception);
            throw e;
        }
        ensureNonNull(CODE_KEY, code);
    }

    /**
     * Makes sure an argument is non-null. This is method duplicate
     * {@link AbstractIdentifiedObject#ensureNonNull(String, Object)}
     * except for the more accurate stack trace. It is duplicated
     * there in order to avoid a dependency to {@link AbstractIdentifiedObject}.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws InvalidParameterValueException if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object)
        throws IllegalArgumentException
    {
        if (object == null) {
            throw new InvalidParameterValueException(Errors.format(
                        ErrorKeys.NULL_ARGUMENT_$1, name), name, object);
        }
    }

    /**
     * Identifier code or name, optionally from a controlled list or pattern.
     *
     * @return The code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Name or identifier of the person or organization responsible for namespace.
     *
     * @return The codespace, or {@code null} if not available.
     */
    public String getCodeSpace() {
        return codespace;
    }

    /**
     * Organization or party responsible for definition and maintenance of the
     * {@linkplain #getCode code}.
     *
     * @return The authority, or {@code null} if not available.
     */
    public Citation getAuthority() {
        return authority;
    }

    /**
     * Identifier of the version of the associated code space or code, as specified by the
     * code authority. This version is included only when the {@linkplain #getCode code}
     * uses versions. When appropriate, the edition is identified by the effective date,
     * coded using ISO 8601 date format.
     *
     * @return The version, or {@code null} if not available.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Comments on or information about this identifier, or {@code null} if none.
     *
     * @return Optional comments about this identifier.
     */
    public InternationalString getRemarks() {
        return remarks;
    }

    /**
     * Returns the generic name of this identifier. The name will be constructed
     * automatically the first time it will be needed. The name's scope is infered
     * from the shortest alternative title (if any). This heuristic rule seems raisonable
     * since, according ISO 19115, the {@linkplain Citation#getAlternateTitles alternate
     * titles} often contains abreviation (for example "DCW" as an alternative title for
     * "Digital Chart of the World"). If no alternative title is found or if the main title
     * is yet shorter, then it is used.
     */
    private synchronized GenericName getName() {
        if (name == null) {
            name = getName(authority, code);
        }
        return name;
    }

    /**
     * Constructs a generic name from the specified authority and code.
     */
    private GenericName getName(final Citation authority, final CharSequence code) {
        if (authority == null) {
            return new org.geotools.util.LocalName(code);
        }
        final CharSequence title;
        if (codespace != null) {
            title = codespace;
        } else {
            title = getShortestTitle(authority);
        }
        GenericName scope;
        synchronized (NamedIdentifier.class) {
            if (SCOPES == null) {
                SCOPES = new WeakValueHashMap<CharSequence,GenericName>();
            }
            scope = SCOPES.get(title);
            if (scope == null) {
                scope = new org.geotools.util.LocalName(title);
                SCOPES.put(title, scope);
            }
        }
        return new org.geotools.util.ScopedName(scope, code);
    }

    /**
     * Returns the shortest title inferred from the specified authority.
     */
    private static InternationalString getShortestTitle(final Citation authority) {
        InternationalString title = authority.getTitle();
        int length = title.length();
        final Collection<? extends InternationalString> alt = authority.getAlternateTitles();
        if (alt != null) {
            for (final InternationalString candidate : alt) {
                final int candidateLength = candidate.length();
                if (candidateLength>0 && candidateLength<length) {
                    title = candidate;
                    length = candidateLength;
                }
            }
        }
        return title;
    }

    /**
     * Tries to get a codespace from the specified authority. This method scan first
     * through the identifier, then through the titles if no suitable identifier were found.
     */
    private static String getCodeSpace(final Citation authority) {
        final Collection<? extends Identifier> identifiers = authority.getIdentifiers();
        if (identifiers != null) {
            for (final Identifier id : identifiers) {
                final String identifier = id.getCode();
                if (isValidCodeSpace(identifier)) {
                    return identifier;
                }
            }
        }
        // The "null" locale argument is required for getting the unlocalized version.
        final String title = getShortestTitle(authority).toString(null);
        if (isValidCodeSpace(title)) {
            return title;
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified string looks like a valid code space.
     * This method, together with {@link #getShortestTitle}, uses somewhat heuristic
     * rules that may change in future Geotools versions.
     */
    private static boolean isValidCodeSpace(final String codespace) {
        if (codespace == null) {
            return false;
        }
        for (int i=codespace.length(); --i>=0;) {
            if (!Character.isJavaIdentifierPart(codespace.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the first element in the sequence of {@linkplain #getParsedNames parsed names}.
     *
     * @since 2.6
     */
    public LocalName head() {
        return getName().head();
    }

    /**
     * Returns the last element in the sequence of {@linkplain #getParsedNames parsed names}.
     *
     * @since 2.3
     */
    public LocalName tip() {
        return getName().tip();
    }

    /**
     * @deprecated Replaced by {@link #tip()}.
     */
    @Deprecated
    public LocalName name() {
        return tip();
    }

    /**
     * Returns a view of this object as a local name. The local name returned by this method
     * will have the same {@linkplain LocalName#getScope scope} than this generic name.
     *
     * @deprecated Replaced by {@link #tip()}.
     */
    @Deprecated
    public LocalName asLocalName() {
        return tip();
    }

    /**
     * Returns the scope (name space) in which this name is local.
     *
     * @since 2.3
     */
    public NameSpace scope() {
        return getName().scope();
    }

    /**
     * Returns the scope (name space) of this generic name. If this name has no scope
     * (e.g. is the root), then this method returns {@code null}.
     *
     * @deprecated Replaced by {@link #scope()}.
     */
    public GenericName getScope() {
        return getName().scope().name();
    }

    /**
     * Returns the depth of this name within the namespace hierarchy.
     *
     * @since 2.3
     */
    public int depth() {
        return getName().depth();
    }

    /**
     * Returns the sequence of {@linkplain LocalName local names} making this generic name.
     * Each element in this list is like a directory name in a file path name.
     * The length of this sequence is the generic name depth.
     */
    public List<LocalName> getParsedNames() {
        // TODO: temporary hack to be removed after GeoAPI update.
        return (List) getName().getParsedNames();
    }

    /**
     * Returns this name expanded with the specified scope. One may represent this operation
     * as a concatenation of the specified {@code name} with {@code this}.
     *
     * @since 2.3
     */
    public ScopedName push(final GenericName scope) {
        return getName().push(scope);
    }

    /**
     * Returns a view of this name as a fully-qualified name.
     *
     * @since 2.3
     */
    public GenericName toFullyQualifiedName() {
        return getName().toFullyQualifiedName();
    }

    /**
     * Returns a view of this object as a scoped name,
     * or {@code null} if this name has no scope.
     *
     * @deprecated Replaced by {@link #toFullyQualifiedName()}.
     */
    @Deprecated
    public ScopedName asScopedName() {
        final GenericName name = toFullyQualifiedName();
        return (name instanceof ScopedName) ? (ScopedName) name : null;
    }

    /**
     * Returns a local-dependent string representation of this generic name. This string
     * is similar to the one returned by {@link #toString} except that each element has
     * been localized in the {@linkplain InternationalString#toString(Locale) specified locale}.
     * If no international string is available, then this method returns an implementation mapping
     * to {@link #toString} for all locales.
     */
    public InternationalString toInternationalString() {
        return getName().toInternationalString();
    }

    /**
     * Returns a string representation of this generic name. This string representation
     * is local-independant. It contains all elements listed by {@link #getParsedNames}
     * separated by an arbitrary character (usually {@code :} or {@code /}).
     */
    @Override
    public String toString() {
        return getName().toString();
    }

    /**
     * Compares this name with the specified object for order. Returns a negative integer,
     * zero, or a positive integer as this name lexicographically precedes, is equals to,
     * or follows the specified object.
     *
     * @param object The object to compare with.
     * @return -1 if this identifier precedes the given object, +1 if it follows it.
     */
    public int compareTo(final GenericName object) {
        return getName().compareTo(object);
    }

    /**
     * Compares this identifier with the specified object for equality.
     *
     * @param object The object to compare with this name.
     * @return {@code true} if the given object is equals to this name.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final NamedIdentifier that = (NamedIdentifier) object;
            return Utilities.equals(this.code,      that.code     ) &&
                   Utilities.equals(this.codespace, that.codespace) &&
                   Utilities.equals(this.version,   that.version  ) &&
                   Utilities.equals(this.authority, that.authority) &&
                   Utilities.equals(this.remarks,   that.remarks  );
        }
        return false;
    }

    /**
     * Returns a hash code value for this identifier.
     */
    @Override
    public int hashCode() {
        int hash = (int) serialVersionUID;
        if (code != null) {
            hash ^= code.hashCode();
        }
        if (version != null) {
            hash = hash*37 + version.hashCode();
        }
        return hash;
    }
}
