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
package org.geotools.referencing.factory;

import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.util.InternationalString;
import org.opengis.util.GenericName;

import org.geotools.factory.Hints;
import org.geotools.referencing.wkt.Symbols;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.DerivedSet;
import org.geotools.util.NameFactory;
import org.geotools.util.SimpleInternationalString;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Default implementation for a coordinate reference system authority factory
 * backed by a property file. This gives some of the benificts of using the
 * {@linkplain org.geotools.referencing.factory.epsg.DirectEpsgFactory EPSG database backed
 * authority factory} (for example), in a portable property file.
 * <p>
 * This factory doesn't cache any result. Any call to a {@code createFoo} method will trig a new
 * WKT parsing. For caching, this factory should be wrapped in some buffered factory like
 * {@link BufferedAuthorityFactory}.
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Rueben Schulz
 * @author Martin Desruisseaux
 */
public class PropertyAuthorityFactory extends DirectAuthorityFactory
        implements CRSAuthorityFactory, CSAuthorityFactory, DatumAuthorityFactory
{
    /**
     * The authority for this factory.
     */
    private final Citation authority;

    /**
     * Same as {@link #authority}, but may contains more than one elements in some particular
     * cases.
     */
    private final Citation[] authorities;

    /**
     * The properties object for our properties file. Keys are the authority
     * code for a coordinate reference system and the associated value is a
     * WKT string for the CRS.
     * <p>
     * It is technically possible to add or remove elements after they have been
     * loaded by the constructor. However if such modification are made, then we
     * should update {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER} accordingly.
     * It may be an issue since hints are supposed to be immutable after factory
     * construction. For now, this class do not allow addition of elements.
     */
    private final Properties definitions = new Properties();

    /**
     * An unmodifiable view of the authority keys. This view is always up to date
     * even if entries are added or removed in the {@linkplain #definitions} map.
     */
    @SuppressWarnings("unchecked")
    private final Set<String> codes = Collections.unmodifiableSet((Set) definitions.keySet());

    /**
     * Views of {@link #codes} for different types. Views will be constructed only when first
     * needed. View are always up to date even if entries are added or removed in the
     * {@linkplain #definitions} map.
     */
    private transient Map<Class<? extends IdentifiedObject>, Set<String>> filteredCodes;

    /**
     * A WKT parser.
     */
    private transient Parser parser;

    /**
     * Creates a factory for the specified authority from the specified file.
     *
     * @param  factories   The underlying factories used for objects creation.
     * @param  authority   The organization or party responsible for definition and maintenance of
     *                     the database.
     * @param  definitions URL to the definition file.
     * @throws IOException if the definitions can't be read.
     */
    public PropertyAuthorityFactory(final ReferencingFactoryContainer factories,
                                    final Citation                    authority,
                                    final URL                         definitions)
            throws IOException
    {
        this(factories, new Citation[] {authority}, definitions);
    }

    /**
     * Creates a factory for the specified authorities from the specified file. More than
     * one authority may be specified when the CRS to create should have more than one
     * {@linkplain CoordinateReferenceSystem#getIdentifiers identifier}, each with the same
     * code but different namespace. For example a
     * {@linkplain org.geotools.referencing.factory.epsg.EsriExtension factory for CRS defined
     * by ESRI} uses the {@code "ESRI"} namespace, but also the {@code "EPSG"} namespace
     * because those CRS are used as extension of the EPSG database. Concequently, the same
     * CRS can be identified as {@code "ESRI:53001"} and {@code "EPSG:53001"}, where
     * {@code "53001"} is a unused code in the official EPSG database.
     *
     * @param  factories   The underlying factories used for objects creation.
     * @param  authorities The organizations or party responsible for definition
     *                     and maintenance of the database.
     * @param  definitions URL to the definition file.
     * @throws IOException if the definitions can't be read.
     *
     * @since 2.4
     */
    public PropertyAuthorityFactory(final ReferencingFactoryContainer factories,
                                    final Citation[]                  authorities,
                                    final URL                         definitions)
            throws IOException
    {
        super(factories, MINIMUM_PRIORITY + 10);
        // The following hints have no effect on this class behaviour,
        // but tell to the user what this factory do about axis order.

        // TODO: Following line should not be commented-out.
        // See http://jira.codehaus.org/browse/GEOT-1699
//      hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS,   Boolean.FALSE);
        hints.put(Hints.FORCE_STANDARD_AXIS_UNITS,        Boolean.FALSE);
        ensureNonNull("authorities", authorities);
        if (authorities.length == 0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.EMPTY_ARRAY));
        }
        this.authorities = authorities.clone();
        authority = authorities[0];
        ensureNonNull("authority", authority);
        final InputStream in = definitions.openStream();
        this.definitions.load(in);
        in.close();
        /*
         * If the WKT do not contains any AXIS[...] element, then every CRS will be created with
         * the default (longitude,latitude) axis order. In such case this factory is insensitive
         * to the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint (i.e. every CRS to be created by this
         * instance are invariant under the above-cited hint value) and we can remove it from
         * the hint map. Removing this hint allow the CRS.decode(..., true) convenience method
         * to find this factory (GEOT-1175).
         */
        final Symbols s = Symbols.DEFAULT;
        for (final Object wkt : this.definitions.values()) {
            if (s.containsAxis((String) wkt)) {
                LOGGER.warning("Axis elements found in a wkt definition, the force longitude " +
                        "first axis order hint might not be respected:\n" + wkt);
                return;
            }
        }
        hints.remove(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
    }

    /**
     * Returns the organization or party responsible for definition and maintenance of the
     * database.
     */
    public Citation getAuthority() {
        return authority;
    }

    /**
     * Returns the set of authority codes of the given type. The type
     * argument specify the base class. For example if this factory is
     * an instance of CRSAuthorityFactory, then:
     * <p>
     * <ul>
     *  <li>{@code CoordinateReferenceSystem.class} asks for all authority codes accepted by
     *      {@link #createGeographicCRS createGeographicCRS},
     *      {@link #createProjectedCRS  createProjectedCRS},
     *      {@link #createVerticalCRS   createVerticalCRS},
     *      {@link #createTemporalCRS   createTemporalCRS}
     *      and their friends.</li>
     *  <li>{@code ProjectedCRS.class} asks only for authority codes accepted by
     *      {@link #createProjectedCRS createProjectedCRS}.</li>
     * </ul>
     *
     * The default implementaiton filters the set of codes based on the
     * {@code "PROJCS"} and {@code "GEOGCS"} at the start of the WKT strings.
     *
     * @param  type The spatial reference objects type (may be {@code Object.class}).
     * @return The set of authority codes for spatial reference objects of the given type.
     *         If this factory doesn't contains any object of the given type, then this method
     *         returns an empty set.
     * @throws FactoryException if access to the underlying database failed.
     */
    public Set<String> getAuthorityCodes(final Class<? extends IdentifiedObject> type)
            throws FactoryException
    {
        if (type==null || type.isAssignableFrom(IdentifiedObject.class)) {
            return codes;
        }
        if (filteredCodes == null) {
            filteredCodes = new HashMap<Class<? extends IdentifiedObject>, Set<String>>();
        }
        synchronized (filteredCodes) {
            Set<String> filtered = filteredCodes.get(type);
            if (filtered == null) {
                @SuppressWarnings("unchecked")
                final Map<String,String> map = (Map) definitions;
                filtered = new Codes(map, type);
                filteredCodes.put(type, filtered);
            }
            return filtered;
        }
    }

    /**
     * The set of codes for a specific type of CRS. This set filter the codes set in the
     * enclosing {@link PropertyAuthorityFactory} in order to keep only the codes for the
     * specified type. Filtering is performed on the fly. Consequently, this set is cheap
     * if the user just want to check for the existence of a particular code.
     */
    private static final class Codes extends DerivedSet<String, String> {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 2681905294171687900L;

        /**
         * The spatial reference objects type.
         */
        private final Class<? extends IdentifiedObject> type;

        /**
         * The reference to {@link PropertyAuthorityFactory#definitions}.
         */
        private final Map<String,String> definitions;

        /**
         * Constructs a set of codes for the specified type.
         */
        public Codes(final Map<String,String> definitions,
                     final Class<? extends IdentifiedObject> type)
        {
            super(definitions.keySet(), String.class);
            this.definitions = definitions;
            this.type = type;
        }

        /**
         * Returns the code if the associated key is of the expected type, or {@code null}
         * otherwise.
         */
        protected String baseToDerived(final String key) {
            final String wkt = definitions.get(key);
            final int length = wkt.length();
            int i=0; while (i<length && Character.isJavaIdentifierPart(wkt.charAt(i))) i++;
            Class<?> candidate = Parser.getClassOf(wkt.substring(0,i));
            if (candidate == null) {
                candidate = IdentifiedObject.class;
            }
            return type.isAssignableFrom(candidate) ? key : null;
        }

        /**
         * Transforms a value in this set to a value in the base set.
         */
        protected String derivedToBase(final String element) {
            return element;
        }
    }

    /**
     * Returns the Well Know Text from a code.
     *
     * @param  code Value allocated by authority.
     * @return The Well Know Text (WKT) for the specified code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     */
    public String getWKT(final String code) throws NoSuchAuthorityCodeException {
        ensureNonNull("code", code);
        final String wkt = definitions.getProperty(trimAuthority(code));
        if (wkt == null) {
            throw noSuchAuthorityCode(IdentifiedObject.class, code);
        }
        return wkt.trim();
    }

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @param  code Value allocated by authority.
     * @return A description of the object, or {@code null} if the object
     *         corresponding to the specified {@code code} has no description.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the query failed for some other reason.
     */
    public InternationalString getDescriptionText(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final String wkt = getWKT(code);
        int start = wkt.indexOf('"');
        if (start >= 0) {
            final int end = wkt.indexOf('"', ++start);
            if (end >= 0) {
                return new SimpleInternationalString(wkt.substring(start, end).trim());
            }
        }
        return null;
    }

    /**
     * Returns the parser.
     */
    private Parser getParser() {
        if (parser == null) {
            parser = new Parser();
        }
        return parser;
    }

    /**
     * Returns an arbitrary object from a code. If the object type is know at compile time, it is
     * recommended to invoke the most precise method instead of this one.
     *
     * @param  code Value allocated by authority.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    @Override
    public IdentifiedObject createObject(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final String wkt = getWKT(code);
        final Parser parser = getParser();
        try {
            synchronized (parser) {
                parser.code = code;
                return (IdentifiedObject) parser.parseObject(wkt);
            }
        } catch (ParseException exception) {
            throw new FactoryException(exception);
        }
    }

    /**
     * Returns a coordinate reference system from a code. If the object type is know at compile
     * time, it is recommended to invoke the most precise method instead of this one.
     *
     * @param  code Value allocated by authority.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    @Override
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code)
            throws NoSuchAuthorityCodeException, FactoryException
    {
        final String wkt = getWKT(code);
        final Parser parser = getParser();
        try {
            synchronized (parser) {
                parser.code = code;
                // parseCoordinateReferenceSystem provides a slightly faster path than parseObject.
                return parser.parseCoordinateReferenceSystem(wkt);
            }
        } catch (ParseException exception) {
            throw new FactoryException(exception);
        }
    }

    /**
     * Trims the authority scope, if present. If more than one authority were given at
     * {@linkplain #PropertyAuthorityFactory(ReferencingFactoryContainer, Citation[], URL)
     * construction time}, then any of them may appears as the scope in the supplied code.
     *
     * @param  code The code to trim.
     * @return The code without the authority scope.
     */
    @Override
    protected String trimAuthority(String code) {
        code = code.trim();
        final GenericName name  = NameFactory.create(code);
        final GenericName scope = name.scope().name();
        if (scope == null) {
            return code;
        }
        final String candidate = scope.toString();
        for (int i=0; i<authorities.length; i++) {
            if (Citations.identifierMatches(authorities[i], candidate)) {
                return name.tip().toString().trim();
            }
        }
        return code;
    }

    /**
     * The WKT parser for this authority factory. This parser add automatically the authority
     * code if it was not explicitly specified in the WKT.
     */
    private final class Parser extends org.geotools.referencing.wkt.Parser {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -5910561042299146066L;

        /**
         * The authority code for the WKT to be parsed.
         */
        String code;

        /**
         * Creates the parser.
         */
        public Parser() {
            super(Symbols.DEFAULT, factories);
        }

        /**
         * Add the authority code to the specified properties, if not already present.
         */
        @Override
        protected Map<String,Object> alterProperties(Map<String,Object> properties) {
            Object candidate = properties.get(IdentifiedObject.IDENTIFIERS_KEY);
            if (candidate == null && code != null) {
                properties = new HashMap<String,Object>(properties);
                code = trimAuthority(code);
                final Object identifiers;
                if (authorities.length <= 1) {
                    identifiers = new NamedIdentifier(authority, code);
                } else {
                    final NamedIdentifier[] ids = new NamedIdentifier[authorities.length];
                    for (int i=0; i<ids.length; i++) {
                        ids[i] = new NamedIdentifier(authorities[i], code);
                    }
                    identifiers = ids;
                }
                properties.put(IdentifiedObject.IDENTIFIERS_KEY, identifiers);
            }
            return super.alterProperties(properties);
        }
    }
}
