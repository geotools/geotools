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
package org.geotools.referencing.factory;

import java.util.*;

import org.opengis.referencing.*;
import org.opengis.referencing.cs.*;
import org.opengis.referencing.crs.*;
import org.opengis.referencing.datum.*;
import org.opengis.referencing.operation.*;
import org.opengis.util.InternationalString;
import org.opengis.metadata.citation.Citation;

import org.geotools.factory.Factory;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.util.GenericName;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * An authority factory that delegates the object creation to an other factory determined from the
 * authority name in the code. This factory requires that every codes given to a {@code createFoo}
 * method are prefixed by the authority name, for example {@code "EPSG:4326"}. This is different
 * from using a factory from a known authority, in which case the authority part was optional (for
 * example when using the {@linkplain org.geotools.referencing.factory.epsg EPSG authority factory},
 * the {@code "EPSG:"} part in {@code "EPSG:4326"} is optional).
 * <p>
 * This class parses the authority name and delegates the work the corresponding factory. For
 * example if any {@code createFoo(...)} method in this class is invoked with a code starting
 * by {@code "EPSG:"}, then this class delegates the object creation to one of the authority
 * factories provided to the constructor.
 * <p>
 * This class is not registered in {@link ReferencingFactoryFinder}, because it is not a real
 * authority factory. There is not a single authority name associated to this factory, but rather
 * a set of names determined from all available authority factories.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ManyAuthoritiesFactory extends AuthorityFactoryAdapter implements CRSAuthorityFactory,
        CSAuthorityFactory, DatumAuthorityFactory, CoordinateOperationAuthorityFactory
{
    /**
     * The types to be recognized for the {@code factories} argument in
     * constructors. Must be consistent with the types expected by the
     * {@link AllAuthoritiesFactory#fromFactoryRegistry(String, Class)} method.
     */
    @SuppressWarnings("unchecked")
    private static final Class<? extends AuthorityFactory>[] FACTORY_TYPES = new Class[] {
        CRSAuthorityFactory.class,
        DatumAuthorityFactory.class,
        CSAuthorityFactory.class,
        CoordinateOperationAuthorityFactory.class
    };

    /**
     * The types created by {@link #FACTORY_TYPES}. For each type {@code OBJECT_TYPES[i]},
     * the factory to be used must be {@code FACTORY_TYPES[i]}.
     */
    @SuppressWarnings("unchecked")
    private static final Class<? extends IdentifiedObject>[] OBJECT_TYPES = new Class[] {
        CoordinateReferenceSystem.class,
        Datum.class,
        CoordinateSystem.class,
        CoordinateOperation.class
    };

    /**
     * The set of user-specified factories, or {@code null} if none.
     * This field should be modified by {@link #setFactories} only.
     */
    private Collection<AuthorityFactory> factories;

    /**
     * Guard against infinite recursivity in {@link #getAuthorityCodes}.
     */
    private final ThreadLocal<Boolean> inProgress;

    /**
     * Creates a new factory using the specified set of user factories. Any call to a
     * {@code createFoo(code)} method will scan the supplied factories in their iteration
     * order. The first factory implementing the appropriate interface and having the expected
     * {@linkplain AuthorityFactory#getAuthority authority name} will be used.
     * <p>
     * If the {@code factories} collection contains more than one factory for the same authority
     * and interface, then all additional factories will be {@linkplain FallbackAuthorityFactory
     * fallbacks}, to be tried in iteration order only if the first acceptable factory failed to
     * create the requested object.
     *
     * @param factories A set of user-specified factories to try before to delegate
     *        to {@link GeometryFactoryFinder}.
     */
    public ManyAuthoritiesFactory(final Collection<? extends AuthorityFactory> factories) {
        super(NORMAL_PRIORITY);
        inProgress = new ThreadLocal<Boolean>();
        if (factories!=null && !factories.isEmpty()) {
            for (final AuthorityFactory factory : factories) {
                if (factory instanceof Factory) {
                    hints.putAll(((Factory) factory).getImplementationHints());
                }
            }
            this.factories = createFallbacks(factories);
        }
    }

    /**
     * Returns the factories. This method should not be public since it returns directly the
     * internal instance. This method is to be overriden by {@link AllAuthoritiesFactory} only.
     */
    Collection<AuthorityFactory> getFactories() {
        return factories;
    }

    /**
     * Sets the factories. This method is invoked by the {@link AllAuthoritiesFactory} subclass
     * only. No one else should invoke this method, since factories should be immutable.
     */
    synchronized final void setFactories(final Collection<AuthorityFactory> factories) {
        this.factories = createFallbacks(factories);
    }

    /**
     * If more than one factory is found for the same authority and interface,
     * then wraps them as a chain of {@link FallbackAuthorityFactory}.
     */
    private static Collection<AuthorityFactory> createFallbacks(
            final Collection<? extends AuthorityFactory> factories)
    {
        /*
         * 'authorities' Will contains the set of all authorities found without duplicate values
         * in the sense of Citations.identifierMatches(...). 'factoriesByAuthority' will contains
         * the collection of factories for each authority.
         */
        int authorityCount = 0;
        final Citation[] authorities = new Citation[factories.size()];
        @SuppressWarnings("unchecked")
        final List<AuthorityFactory>[] factoriesByAuthority = new List[authorities.length];
        for (final AuthorityFactory factory : factories) {
            /*
             * Check if the authority has already been meet previously. If the authority is found
             * then 'authorityIndex' is set to its index. Otherwise the new authority is added to
             * the 'authorities' list.
             */
            Citation authority = factory.getAuthority();
            int authorityIndex;
            for (authorityIndex=0; authorityIndex<authorityCount; authorityIndex++) {
                final Citation candidate = authorities[authorityIndex];
                if (Citations.identifierMatches(candidate, authority)) {
                    authority = candidate;
                    break;
                }
            }
            final List<AuthorityFactory> list;
            if (authorityIndex == authorityCount) {
                authorities[authorityCount++] = authority;
                factoriesByAuthority[authorityIndex] = list = new ArrayList<AuthorityFactory>(4);
            } else {
                list = factoriesByAuthority[authorityIndex];
            }
            if (!list.contains(factory)) {
                list.add(factory);
            }
        }
        /*
         * For each authority, chains the factories into a FallbackAuthorityFactory object.
         */
        final ArrayList<AuthorityFactory> result = new ArrayList<AuthorityFactory>();
        final List<AuthorityFactory> buffer = new ArrayList<AuthorityFactory>(4);
        for (int i=0; i<authorityCount; i++) {
            final Collection<AuthorityFactory> list = factoriesByAuthority[i];
            while (!list.isEmpty()) {
                AuthorityFactory primary = null;
                boolean needOtherChains = false;
                for (final Iterator<AuthorityFactory> it=list.iterator(); it.hasNext();) {
                    final AuthorityFactory fallback = it.next();
                    if (primary == null) {
                        primary = fallback;
                    } else if (!FallbackAuthorityFactory.chainable(primary, fallback)) {
                        needOtherChains = true;
                        continue;
                    }
                    buffer.add(fallback);
                    if (!needOtherChains) {
                        it.remove();
                    }
                }
                result.add(FallbackAuthorityFactory.create(buffer));
                buffer.clear();
            }
        }
        result.trimToSize();
        return result;
    }

    /**
     * If this factory is a wrapper for the specified factory that do not add any additional
     * {@linkplain #getAuthorityCodes authority codes}, returns {@code true}. This method is
     * for {@link FallbackAuthorityFactory} internal use only.
     */
    @Override
    boolean sameAuthorityCodes(final AuthorityFactory factory) {
        // We don't want to inherit AuthorityFactoryAdapter implementation here.
        return factory == this;
    }

    /**
     * Returns the character separator for the specified code. The default implementation returns
     * the {@linkplain GenericName#DEFAULT_SEPARATOR default name separator} {@code ':'}, except
     * if the code looks like a URL (e.g. {@code "http://www.opengis.net/"}), in which case this
     * method returns {@code '/'}.
     * <p>
     * In the current implementation, "looks like a URL" means that the first
     * non-{@linkplain Character#isLetterOrDigit(char) aplhanumeric} characters
     * are {@code "://"}. But this heuristic rule may change in future implementations.
     */
    protected char getSeparator(String code) {
        code = code.trim();
        final int length = code.length();
        for (int i=0; i<length; i++) {
            if (!Character.isLetterOrDigit(code.charAt(i))) {
                if (code.regionMatches(i, "://", 0, 3)) {
                    return '/';
                }
                break;
            }
        }
        return GenericName.DEFAULT_SEPARATOR;
    }

    /**
     * Returns {@code true} if the specified code can be splitted in a (<cite>authority</cite>,
     * <cite>code</cite>) pair at the specified index. The default implementation returns
     * {@code true} if the first non-whitespace character on the left and right side are
     * valid Java identifiers.
     * <p>
     * The purpose of this method is to avoid considering the {@code "//"} part in
     * {@code "http://www.opengis.net/gml/srs/epsg.xml"} as separators. In case of
     * failure to parse the code, this restriction will produce and error message
     * like "<cite>Unknown <code>http://www.opengis.net</code> authority</cite>"
     * instead of "<cite>Unknown <code>http:</code> authority</cite>".
     * <p>
     * We may consider to turn this method into a protected one if the users need to override it.
     */
    private static boolean canSeparateAt(final String code, final int index) {
        char c;
        int i = index;
        do {
            if (--i < 0) {
                return false;
            }
            c = code.charAt(i);
        } while (Character.isWhitespace(c));
        if (!Character.isJavaIdentifierPart(c)) {
            return false;
        }
        final int length = code.length();
        i = index;
        do {
            if (++i >= length) {
                return false;
            }
            c = code.charAt(i);
        } while (Character.isWhitespace(c));
        return Character.isJavaIdentifierPart(c);
    }

    /**
     * Returns the vendor responsible for creating this factory implementation.
     * The default implementation returns {@linkplain Citations#GEOTOOLS Geotools}.
     */
    @Override
    public Citation getVendor() {
        return Citations.GEOTOOLS;
    }

    /**
     * Returns the organization or party responsible for definition and maintenance of the
     * database. The default implementation returns a citation with title "All".
     */
    @Override
    public Citation getAuthority() {
        return ALL;
    }

    /**
     * Returns the authority names of every factories given at construction time.
     */
    public Set<String> getAuthorityNames() {
        final Set<String> names = new HashSet<String>();
        final Collection<AuthorityFactory> factories = getFactories();
        if (factories != null) {
            for (final AuthorityFactory factory : factories) {
                names.add(Citations.getIdentifier(factory.getAuthority()));
            }
        }
        return names;
    }

    /**
     * Returns a description of the underlying backing store, or {@code null} if unknow.
     *
     * @throws FactoryException if a failure occured while fetching the engine description.
     */
    @Override
    public String getBackingStoreDescription() throws FactoryException {
        // We have no authority code, so we can't pick a particular factory.
        return null;
    }

    /**
     * Returns the direct dependencies. Current implementation returns the internal structure
     * because we know that this package will not modifies it. But if the method become public,
     * we will need to returns a unmodifiable view.
     */
    @Override
    Collection<? super AuthorityFactory> dependencies() {
        return getFactories();
    }

    /**
     * Returns {@code true} if the specified factory should be excluded from the search.
     * We exclude adapters around {@link AllAuthoritiesFactory}. This code actually aims
     * to exclude {@link URN_AuthorityFactory} and similar adapters around all factories,
     * since it leads to duplicated search and innacurate identifier to be returned by
     * {@link #findIdentifier}.
     */
    private static boolean exclude(final AuthorityFactory factory) {
        if (ManyAuthoritiesFactory.class.isInstance(factory)) {
            return true;
        }
        if (factory instanceof AuthorityFactoryAdapter) {
            final AuthorityFactoryAdapter adapter = (AuthorityFactoryAdapter) factory;
            return exclude(adapter.crsFactory)   ||
                   exclude(adapter.csFactory)    ||
                   exclude(adapter.datumFactory) ||
                   exclude(adapter.operationFactory);
        }
        return false;
    }

    /**
     * Same as {@link #fromFactoryRegistry(String, Class)}, but returns every factories
     * that fit the given type. The factories are added to the specified set.
     */
    final void fromFactoryRegistry(final String authority,
                                   final Class<? extends AuthorityFactory> type,
                                   final Set<AuthorityFactory> addTo)
    {
        for (int i=0; i<OBJECT_TYPES.length; i++) {
            if (OBJECT_TYPES[i].isAssignableFrom(type)) {
                final AuthorityFactory factory;
                try {
                    factory = fromFactoryRegistry(authority, FACTORY_TYPES[i]);
                } catch (FactoryRegistryException e) {
                    // No factory for the given authority. It may be normal.
                    continue;
                }
                if (!exclude(factory)) {
                    addTo.add(factory);
                }
            }
        }
    }

    /**
     * Returns a factory for the specified authority, or {@code null} if none.
     * To be overriden by {@link AllAuthoritiesFactory} in order to search among
     * factories registered on a system-wide basis.
     *
     * @param  authority The authority to query.
     * @param  type The interface to be implemented.
     * @return The factory.
     * @throws FactoryRegistryException if there is no factory registered for the supplied
     *         authority and hints.
     */
    <T extends AuthorityFactory> T fromFactoryRegistry(final String authority, final Class<T> type)
            throws FactoryRegistryException
    {
        return null;
    }

    /**
     * Searchs for a factory of the given type. This method first search in user-supplied
     * factories. If no user factory is found, then this method request for a factory using
     * {@link GeometryFactoryFinder}. The authority name is inferred from the specified code.
     *
     * @param  type The interface to be implemented.
     * @param  code The code of the object to create.
     * @return The factory.
     * @throws NoSuchAuthorityCodeException if no suitable factory were found.
     */
    @Override
    final <T extends AuthorityFactory> T getAuthorityFactory(final Class<T> type, final String code)
            throws NoSuchAuthorityCodeException
    {
        ensureNonNull("code", code);
        String authority = null;
        FactoryRegistryException cause = null;
        final Collection<AuthorityFactory> factories = getFactories();
        final char separator = getSeparator(code);
        for (int split = code.lastIndexOf(separator); split >= 0;
                 split = code.lastIndexOf(separator, split-1))
        {
            if (!canSeparateAt(code, split)) {
                continue;
            }
            /*
             * Try all possible authority names, begining with the most specific ones.
             * For example if the code is "urn:ogc:def:crs:EPSG:6.8:4326", then we will
             * try "urn:ogc:def:crs:EPSG:6.8" first, "urn:ogc:def:crs:EPSG" next, etc.
             * until a suitable factory is found (searching into user-supplied factories
             * first).
             */
            authority = code.substring(0, split).trim();
            if (factories != null) {
                for (final AuthorityFactory factory : factories) {
                    if (type.isAssignableFrom(factory.getClass())) {
                        if (Citations.identifierMatches(factory.getAuthority(), authority)) {
                            return type.cast(factory);
                        }
                    }
                }
            }
            /*
             * No suitable user-supplied factory. Now query FactoryFinder.
             */
            final AuthorityFactory factory;
            try {
                factory = fromFactoryRegistry(authority, type);
            } catch (FactoryRegistryException exception) {
                cause = exception;
                continue;
            }
            if (factory != null) {
                return type.cast(factory);
            }
        }
        /*
         * No factory found. Creates an error message from the most global authority name
         * (for example "urn" if the code was "urn:ogc:def:crs:EPSG:6.8:4326") and the
         * corresponding cause. Both the authority and cause may be null if the code didn't
         * had any authority part.
         */
        throw noSuchAuthority(code, authority, cause);
    }

    /**
     * Formats the exception to be throw when the user asked for a code from an unknown authority.
     *
     * @param  code      The code with an unknown authority.
     * @param  authority The authority, or {@code null} if none.
     * @param  cause     The cause for the exception to be formatted, or {@code null} if none.
     * @return The formatted exception to be throw.
     */
    private NoSuchAuthorityCodeException noSuchAuthority(final String code, String authority,
                                                         final FactoryRegistryException cause)
    {
        final String message;
        if (authority == null) {
            authority = Vocabulary.format(VocabularyKeys.UNKNOW);
            message   = Errors.format(ErrorKeys.MISSING_AUTHORITY_$1, code);
        } else {
            message = Errors.format(ErrorKeys.UNKNOW_AUTHORITY_$1, authority);
        }
        final NoSuchAuthorityCodeException exception;
        exception = new NoSuchAuthorityCodeException(message, authority, code);
        exception.initCause(cause);
        return exception;
    }

    /**
     * Returns a generic object authority factory for the specified {@code "AUTHORITY:NUMBER"}
     * code.
     *
     * @param  code The code to parse.
     * @return The authority factory.
     * @throws NoSuchAuthorityCodeException if no authority name has been found.
     */
    @Override
    protected AuthorityFactory getAuthorityFactory(final String code)
            throws NoSuchAuthorityCodeException
    {
        return getAuthorityFactory(AuthorityFactory.class, code);
    }

    /**
     * Returns the datum authority factory for the specified {@code "AUTHORITY:NUMBER"} code.
     *
     * @param  code The code to parse.
     * @return The authority factory.
     * @throws NoSuchAuthorityCodeException if no authority name has been found.
     */
    @Override
    protected DatumAuthorityFactory getDatumAuthorityFactory(final String code)
            throws NoSuchAuthorityCodeException
    {
        return getAuthorityFactory(DatumAuthorityFactory.class, code);
    }

    /**
     * Returns the CS authority factory for the specified {@code "AUTHORITY:NUMBER"} code.
     *
     * @param  code The code to parse.
     * @return The authority factory.
     * @throws NoSuchAuthorityCodeException if no authority name has been found.
     */
    @Override
    protected CSAuthorityFactory getCSAuthorityFactory(final String code)
            throws NoSuchAuthorityCodeException
    {
        return getAuthorityFactory(CSAuthorityFactory.class, code);
    }

    /**
     * Returns the CRS authority factory for the specified {@code "AUTHORITY:NUMBER"} code.
     *
     * @param  code The code to parse.
     * @return The authority factory.
     * @throws NoSuchAuthorityCodeException if no authority name has been found.
     */
    @Override
    protected CRSAuthorityFactory getCRSAuthorityFactory(final String code)
            throws NoSuchAuthorityCodeException
    {
        return getAuthorityFactory(CRSAuthorityFactory.class, code);
    }

    /**
     * Returns the operation authority factory for the specified {@code "AUTHORITY:NUMBER"} code.
     *
     * @param  code The code to parse.
     * @return The authority factory.
     * @throws NoSuchAuthorityCodeException if no authority name has been found.
     */
    @Override
    protected CoordinateOperationAuthorityFactory getCoordinateOperationAuthorityFactory(final String code)
            throws NoSuchAuthorityCodeException
    {
        return getAuthorityFactory(CoordinateOperationAuthorityFactory.class, code);
    }

    /**
     * Returns the set of authority codes of the given type.
     *
     * @param  type The spatial reference objects type (may be {@code IdentifiedObject.class}).
     * @return The set of authority codes for spatial reference objects of the given type.
     *         If this factory doesn't contains any object of the given type, then this method
     *         returns an {@linkplain java.util.Collections#EMPTY_SET empty set}.
     * @throws FactoryException if access to the underlying database failed.
     */
    @Override
    public Set<String> getAuthorityCodes(final Class<? extends IdentifiedObject> type)
            throws FactoryException
    {
        if (Boolean.TRUE.equals(inProgress.get())) {
            /*
             * 'getAuthorityCodes' is invoking itself (indirectly). Returns an empty set in order
             * to avoid infinite recursivity. Note that the end result (the output of the caller)
             * will usually not be empty.
             */
            return Collections.emptySet();
        }
        final Set<String> codes = new LinkedHashSet<String>();
        final Set<AuthorityFactory> done = new HashSet<AuthorityFactory>();
        done.add(this); // Safety for avoiding recursive calls.
        try {
            inProgress.set(Boolean.TRUE);
            for (String authority : getAuthorityNames()) {
                authority = authority.trim();
                final char separator = getSeparator(authority);
                /*
                 * Prepares a buffer with the "AUTHORITY:" part in "AUTHORITY:NUMBER".
                 * We will reuse this buffer in order to prefix the authority name in
                 * front of every codes.
                 */
                final StringBuilder code = new StringBuilder(authority);
                int codeBase = code.length();
                if (codeBase != 0 && code.charAt(codeBase - 1) != separator) {
                    code.append(separator);
                    codeBase = code.length();
                }
                code.append("all");
                final String dummyCode = code.toString();
                /*
                 * Now scan over all factories. We will process a factory only if this particular
                 * factory has not already been done in a previous iteration (some implementation
                 * apply to more than one factory).
                 */
scanForType:    for (int i=0; i<FACTORY_TYPES.length; i++) {
                    if (!OBJECT_TYPES[i].isAssignableFrom(type)) {
                        continue;
                    }
                    final Class<? extends AuthorityFactory> factoryType = FACTORY_TYPES[i];
                    final AuthorityFactory factory;
                    try {
                        factory = getAuthorityFactory(factoryType, dummyCode);
                    } catch (NoSuchAuthorityCodeException e) {
                        continue;
                    }
                    if (!done.add(factory)) {
                        continue;
                    }
                    AuthorityFactory wrapped = factory;
                    while (wrapped instanceof AuthorityFactoryAdapter) {
                        final AuthorityFactoryAdapter adapter = (AuthorityFactoryAdapter) wrapped;
                        try {
                            wrapped = adapter.getAuthorityFactory(factoryType, dummyCode);
                        } catch (NoSuchAuthorityCodeException exception) {
                            /*
                             * The factory doesn't understand our dummy code. It happen with
                             * URN_AuthorityFactory, which expect the type ("CRS", etc.) in the URN.
                             */
                            continue scanForType;
                        }
                        if (!done.add(wrapped)) {
                            /*
                             * Avoid the factories that are wrapper around an other factory already
                             * done. If we don't do that, we will duplicate the whole set of EPSG
                             * identifiers (more than 3000 codes) for OrderedAuthorityFactory,
                             * HTTP_AuthorityFactory, URN_AuthorityFactory, etc.
                             */
                            continue scanForType;
                        }
                    }
                    for (String candidate : factory.getAuthorityCodes(type)) {
                        candidate = candidate.trim();
                        if (candidate.length() < codeBase ||
                            Character.isLetterOrDigit (candidate.charAt(codeBase-1)) ||
                           !authority.equalsIgnoreCase(candidate.substring(0, codeBase-1)))
                        {
                            // Prepend the authority code if it was not already presents.
                            code.setLength(codeBase);
                            code.append(candidate);
                            candidate = code.toString();
                        }
                        codes.add(candidate);
                    }
                }
            }
        } finally {
            inProgress.remove();
        }
        return codes;
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
    @Override
    public InternationalString getDescriptionText(final String code) throws FactoryException {
        final Set<AuthorityFactory> done = new HashSet<AuthorityFactory>();
        done.add(this); // Safety for avoiding recursive calls.
        FactoryException failure = null;
        for (int type=0; type<FACTORY_TYPES.length; type++) {
            /*
             * Try all factories, starting with the CRS factory because it is the only one most
             * users care about. If the CRS factory doesn't know about the specified object, then
             * we will try the other factories (datum, CS, ...) before to rethrow the exception.
             */
            final AuthorityFactory factory;
            try {
                factory = getAuthorityFactory(FACTORY_TYPES[type], code);
            } catch (NoSuchAuthorityCodeException exception) {
                if (failure == null) {
                    failure = exception;
                }
                continue;
            }
            if (done.add(factory)) try {
                return factory.getDescriptionText(code);
            } catch (FactoryException exception) {
                /*
                 * Failed to creates an object using the current factory.  We will retain only the
                 * first exception and discart all other ones, except if the first exceptions were
                 * due to unknown authority (we will prefer exception due to unknown code instead).
                 * The first exception is usually thrown by the CRS factory, which is the only
                 * factory most users care about.
                 */
                if (failure==null || failure.getCause() instanceof FactoryRegistryException) {
                    failure = exception;
                }
            }
        }
        if (failure == null) {
            failure = noSuchAuthorityCode(IdentifiedObject.class, code);
        }
        throw failure;
    }

    /**
     * Returns an arbitrary object from a code.
     *
     * @see #createCoordinateReferenceSystem
     * @see #createDatum
     * @see #createEllipsoid
     * @see #createUnit
     */
    @Override
    public IdentifiedObject createObject(final String code) throws FactoryException {
        final Set<AuthorityFactory> done = new HashSet<AuthorityFactory>();
        done.add(this); // Safety for avoiding recursive calls.
        FactoryException failure = null;
        for (int type=0; type<FACTORY_TYPES.length; type++) {
            /*
             * Try all factories, starting with the CRS factory because it is the only one most
             * users care about. If the CRS factory doesn't know about the specified object, then
             * we will try the other factories (datum, CS, ...) before to rethrow the exception.
             */
            final AuthorityFactory factory;
            try {
                factory = getAuthorityFactory(FACTORY_TYPES[type], code);
            } catch (NoSuchAuthorityCodeException exception) {
                if (failure == null) {
                    failure = exception;
                }
                continue;
            }
            if (done.add(factory)) try {
                return factory.createObject(code);
            } catch (FactoryException exception) {
                /*
                 * Failed to creates an object using the current factory.  We will retain only the
                 * first exception and discart all other ones, except if the first exceptions were
                 * due to unknown authority (we will prefer exception due to unknown code instead).
                 * The first exception is usually thrown by the CRS factory, which is the only
                 * factory most users care about.
                 */
                if (failure==null || failure.getCause() instanceof FactoryRegistryException) {
                    failure = exception;
                }
            }
        }
        if (failure == null) {
            failure = noSuchAuthorityCode(IdentifiedObject.class, code);
        }
        throw failure;
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects.
     * The default implementation delegates the lookups to the underlying factories.
     */
    @Override
    public IdentifiedObjectFinder getIdentifiedObjectFinder(Class<? extends IdentifiedObject> type)
            throws FactoryException
    {
        return new Finder(this, type);
    }

    /**
     * A {@link IdentifiedObjectFinder} which tests every factories.
     */
    static class Finder extends IdentifiedObjectFinder {
        /**
         * Creates a finder for the specified type.
         */
        protected Finder(final ManyAuthoritiesFactory factory,
                         final Class<? extends IdentifiedObject> type)
        {
            super(factory, type);
        }

        /**
         * Returns the user-supplied factories.
         */
        final Collection<AuthorityFactory> getFactories() {
            return ((ManyAuthoritiesFactory) getProxy().getAuthorityFactory()).getFactories();
        }

        /**
         * Returns the next finder in the specified set of factories, or {@code null} if none.
         */
        final IdentifiedObjectFinder next(final Iterator<AuthorityFactory> it)
                throws FactoryException
        {
            while (it.hasNext()) {
                final AuthorityFactory factory = it.next();
                if (exclude(factory)) {
                    continue;
                }
                if (factory instanceof AbstractAuthorityFactory) {
                    final IdentifiedObjectFinder finder = ((AbstractAuthorityFactory) factory).
                            getIdentifiedObjectFinder(getProxy().getType());
                    if (finder != null) {
                        finder.setFullScanAllowed(isFullScanAllowed());
                        return finder;
                    }
                }
            }
            return null;
        }

        /**
         * Lookups for the specified object.
         */
        @Override
        public IdentifiedObject find(final IdentifiedObject object) throws FactoryException {
            /*
             * Try to create from the identifier before to scan over every factories,
             * because the identifier may contains the authority name, in which case
             * we can pickup directly the right factory instead of trying them all.
             */
            IdentifiedObject candidate = createFromIdentifiers(object);
            if (candidate != null) {
                return candidate;
            }
            final Collection<AuthorityFactory> factories = getFactories();
            if (factories != null) {
                IdentifiedObjectFinder finder;
                final Iterator<AuthorityFactory> it = factories.iterator();
                while ((finder = next(it)) != null) {
                    candidate = finder.find(object);
                    if (candidate != null) {
                        break;
                    }
                }
            }
            return candidate;
        }

        /**
         * Returns the identifier of the specified object, or {@code null} if none.
         */
        @Override
        public String findIdentifier(final IdentifiedObject object) throws FactoryException {
            /*
             * Try to create from the identifier for the same reason than find(IdentifiedObject).
             * Note that we returns directly the primary name; we don't try to locate a name for
             * a given authority since the "All" authority do not really exists.
             */
            IdentifiedObject candidate = createFromIdentifiers(object);
            if (candidate != null) {
                return candidate.getName().toString();
            }
            final Collection<AuthorityFactory> factories = getFactories();
            if (factories != null) {
                IdentifiedObjectFinder finder;
                final Iterator<AuthorityFactory> it = factories.iterator();
                while ((finder = next(it)) != null) {
                    final String id = finder.findIdentifier(object);
                    if (id != null) {
                        return id;
                    }
                }
            }
            return null;
        }
    }
}
