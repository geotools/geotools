/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.util;

import java.util.Map;
import java.util.Locale;
import org.opengis.annotation.Extension;


/**
 * Factory for {@linkplain GenericName generic names} and
 * {@linkplain InternationalString international strings}.
 * <p>
 * <blockquote><font size=-1><b>Implementation note:</b>
 * despite the "create" name, implementations may return cached instances.
 * </font></blockquote>
 *
 * @author Jesse Crossley (SYS Technologies)
 * @author Martin Desruisseaux (Geomatys)
 * @since GeoAPI 2.0
 */
@Extension
public interface NameFactory {
    /**
     * Creates an international string from a set of strings in different locales.
     *
     * @param strings
     *          String value for each locale key.
     * @return The international string.
     */
    InternationalString createInternationalString(Map<Locale,String> strings);

    /**
     * Creates a namespace having the given name and separators.
     * <p>
     * <blockquote><font size=-1><b>Implementation note:</b>
     * despite the "create" name, implementations may return existing instances.
     * </font></blockquote>
     *
     * @param name
     *          The name of the namespace to be returned. This argument can be created using
     *          <code>{@linkplain #createGenericName createGenericName}(null, parsedNames)</code>.
     * @param headSeparator
     *          The separator to insert between the namespace and the {@linkplain GenericName#head
     *          head}. For HTTP namespace, it is {@code "://"}. For URN namespace, it is typically
     *          {@code ":"}.
     * @param separator
     *          The separator to insert between {@linkplain GenericName#getParsedNames parsed names}
     *          in that namespace. For HTTP namespace, it is {@code "."}. For URN namespace, it is
     *          typically {@code ":"}.
     * @return A namespace having the given name and separators.
     *
     * @since GeoAPI 2.2
     */
    NameSpace createNameSpace(GenericName name, String headSeparator, String separator);

    /**
     * Creates a local name from the given character sequence. The character sequence can be either
     * a {@link String} or an {@link InternationalString} instance. In the later case, implementations
     * can use an arbitrary {@linkplain Locale locale} (typically {@link Locale#ENGLISH ENGLISH},
     * but not necessarly) for the unlocalized string to be returned by {@link LocalName#toString}.
     *
     * @param scope
     *          The {@linkplain GenericName#scope scope} of the local name to be created,
     *          or {@code null} for a global namespace.
     * @param name
     *          The local name as a string or an international string.
     * @return The local name for the given character sequence.
     *
     * @since GeoAPI 2.2
     */
    LocalName createLocalName(NameSpace scope, CharSequence name);

    /**
     * Creates a local or scoped name from an array of parsed names. The array elements can be either
     * {@link String} or {@link InternationalString} instances. In the later case, implementations
     * can use an arbitrary {@linkplain Locale locale} (typically {@link Locale#ENGLISH ENGLISH},
     * but not necessarly) for the unlocalized string to be returned by {@link GenericName#toString}.
     * <p>
     * If the length of the {@code parsedNames} array is 1, then this method returns an instance
     * of {@link LocalName}. If the length is 2 or more, then this method returns an instance of
     * {@link ScopedName}.
     *
     * @param scope
     *          The {@linkplain GenericName#scope scope} of the generic name to be created,
     *          or {@code null} for a global namespace.
     * @param parsedNames
     *          The local names as an array of strings or international strings.
     *          This array must contains at least one element.
     * @return The generic name for the given parsed names.
     *
     * @since GeoAPI 2.2
     */
    GenericName createGenericName(NameSpace scope, CharSequence[] parsedNames);

    /**
     * Constructs a generic name from a qualified name. This method splits the given name around a
     * separator inferred from the given scope, or an implementation-dependant default separator if
     * the given scope is null.
     * <p>
     * For example if the {@code scope} argument is the namespace {@code "urn:ogc:def"}
     * with {@code ":"} as the separator, and if the {@code name} argument is the string
     * {@code "crs:epsg:4326"}, then the result is a {@linkplain ScopedName scoped name}
     * having a {@linkplain GenericName#depth depth} of 3, which is the length of the list
     * of {@linkplain GenericName#getParsedNames parsed names} ({@code "crs"}, {@code "epsg"},
     * {@code "4326"}).
     *
     * @param scope
     *          The {@linkplain AbstractName#scope scope} of the generic name to
     *          be created, or {@code null} for a global namespace.
     * @param name
     *          The qualified name, as a sequence of names separated by a scope-dependant separator.
     * @return A name parsed from the given string.
     *
     * @since GeoAPI 2.2
     */
    GenericName parseGenericName(NameSpace scope, CharSequence name);

    /**
     * Creates a local name from a {@linkplain LocalName#scope scope} and a
     * {@linkplain LocalName#toString name}. The {@code scope} argument identifies the
     * {@linkplain NameSpace name space} in which the local name will be created.
     * The {@code name} argument is taken verbatism as the string representation
     * of the local name.
     * <p>
     * This method
     *
     * @param scope
     *          The scope, or {@code null} for the global one.
     * @param name
     *          The unlocalized name.
     * @param localizedName
     *          A localized version of the name, or {@code null} if none.
     * @return The local name.
     *
     * @deprecated Replaced by {@link #createNameSpace createNameSpace} for the scope argument,
     *             and {@link #createLocalName(NameSpace,CharSequence) createLocalName} for the
     *             name and localized name arguments.
     */
    @Deprecated
    LocalName createLocalName(GenericName scope, String name, InternationalString localizedName);

    /**
     * Creates a scoped name from a {@linkplain ScopedName#scope scope} and a
     * {@linkplain ScopedName#toString name}. The {@code scope} argument identifies the
     * {@linkplain NameSpace name space} in which the scoped name will be created.
     * The {@code name} argument will be parsed in order to construct the list of
     * {@linkplain ScopedName#getParsedNames parsed names}.
     *
     * @param scope
     *          The scope, or {@code null} for the global one.
     * @param name
     *          The unlocalized name.
     * @param localizedName
     *          A localized version of the name, or {@code null} if none.
     * @return The scoped name.
     *
     * @deprecated Replaced by {@link #createNameSpace createNameSpace} for the scope argument,
     *             and {@link #parseGenericName parseGenericName} for the name and localized
     *             name arguments.
     */
    @Deprecated
    ScopedName createScopedName(GenericName scope, String name, InternationalString localizedName);
}
