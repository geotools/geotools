/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.util.GenericName;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;

/**
 * Looks up an object from an {@linkplain AuthorityFactory authority factory} which is
 * {@linkplain CRS#equalsIgnoreMetadata equals, ignoring metadata}, to the specified object. The main purpose of this
 * class is to get a fully {@linkplain IdentifiedObject identified object} from an incomplete one, for example from an
 * object without {@linkplain IdentifiedObject#getIdentifiers identifiers} or "{@code AUTHORITY[...]}" element in
 * <cite>Well Known Text</cite> terminology.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class IdentifiedObjectFinder {
    public static final Logger LOGGER = Logging.getLogger(IdentifiedObjectFinder.class);

    /** The proxy for object creation. */
    private AuthorityFactoryProxy proxy;

    /** {@code true} for performing full scans, or {@code false} otherwise. */
    private boolean fullScan = true;

    /** Default constructor, subclass should provide an override for getProxy */
    protected IdentifiedObjectFinder() {}

    /** Creates a finder using the same proxy than the specified finder. */
    IdentifiedObjectFinder(final IdentifiedObjectFinder finder) {
        this.setProxy(finder.getProxy());
    }

    /**
     * Creates a finder using the specified factory. This constructor is protected because instances of this class
     * should not be created directly. Use {@link AbstractAuthorityFactory#getIdentifiedObjectFinder} instead.
     *
     * @param factory The factory to scan for the identified objects.
     * @param type The type of objects to lookup.
     */
    protected IdentifiedObjectFinder(final AuthorityFactory factory, final Class<? extends IdentifiedObject> type) {
        setProxy(AuthorityFactoryProxy.getInstance(factory, type));
    }

    /*
     * Do NOT provide the following method:
     *
     *     public AuthorityFactory getAuthorityFactory() {
     *         return proxy.getAuthorityFactory();
     *     }
     *
     * because the returned factory may not be the one the user would expect. Some of our
     * AbstractAuthorityFactory implementations create proxy to the underlying backing
     * store rather than to the factory on which 'getIdentifiedObjectFinder()' was invoked.
     */

    /** @return the proxy */
    protected AuthorityFactoryProxy getProxy() {
        return proxy;
    }

    /**
     * If {@code true}, an exhaustive full scan against all registered objects will be performed (may be slow).
     * Otherwise only a fast lookup based on embedded identifiers and names will be performed. The default value is
     * {@code true}.
     */
    public boolean isFullScanAllowed() {
        return fullScan;
    }

    /** Set whatever an exhaustive scan against all registered objects is allowed. The default value is {@code true}. */
    public void setFullScanAllowed(final boolean fullScan) {
        this.fullScan = fullScan;
    }

    /**
     * Lookups an object which is {@linkplain CRS#equalsIgnoreMetadata equals, ignoring metadata}, to the specified
     * object. The default implementation tries to instantiate some {@linkplain IdentifiedObject identified objects}
     * from the authority factory specified at construction time, in the following order:
     *
     * <p>
     *
     * <ul>
     *   <li>If the specified object contains {@linkplain IdentifiedObject#getIdentifiers identifiers} associated to the
     *       same authority than the factory, then those identifiers are used for
     *       {@linkplain AuthorityFactory#createObject creating objects} to be tested.
     *   <li>If the authority factory can create objects from their {@linkplain IdentifiedObject#getName name} in
     *       addition of identifiers, then the name and {@linkplain IdentifiedObject#getAlias aliases} are used for
     *       creating objects to be tested.
     *   <li>If {@linkplain #isFullScanAllowed full scan is allowed}, then full {@linkplain #getCodeCandidates set of
     *       authority codes} are used for creating objects to be tested.
     * </ul>
     *
     * <p>The first of the above created objects which is equals to the specified object in the the sense of
     * {@link CRS#equalsIgnoreMetadata equalsIgnoreMetadata} is returned.
     *
     * @param object The object looked up.
     * @return The identified object, or {@code null} if not found.
     * @throws FactoryException if an error occured while creating an object.
     */
    public IdentifiedObject find(final IdentifiedObject object) throws FactoryException {
        /*
         * First check if one of the identifiers can be used to spot directly an
         * identified object (and check it's actually equal to one in the factory).
         */
        IdentifiedObject candidate = createFromIdentifiers(object);
        if (candidate != null) {
            return candidate;
        }
        /*
         * We are unable to find the object from its identifiers. Try a quick name lookup.
         * Some implementations like the one backed by the EPSG database are capable to find
         * an object from its name.
         */
        candidate = createFromNames(object);
        if (candidate != null) {
            return candidate;
        }

        candidate = createFromCodes(object, true);
        if (candidate != null) {
            return candidate;
        }

        /*
         * Here we exhausted the quick paths. Bail out if the user does not want a full scan.
         */
        return fullScan ? createFromCodes(object, false) : null;
    }

    /**
     * Returns the identifier of the specified object, or {@code null} if none. The default implementation invokes
     * <code>{@linkplain #find find}(object)</code> and extracts the code from the returned {@linkplain IdentifiedObject
     * identified object}.
     */
    public String findIdentifier(final IdentifiedObject object) throws FactoryException {
        final IdentifiedObject candidate = find(object);
        return candidate != null ? getIdentifier(candidate) : null;
    }

    /**
     * The Authority for this Finder; used during get Identifier.
     *
     * @return Citation for the authority being represented.
     */
    protected Citation getAuthority() {
        return getProxy().getAuthorityFactory().getAuthority();
    }
    /** Returns the identifier for the specified object. */
    final String getIdentifier(final IdentifiedObject object) {
        Citation authority = getAuthority();
        if (ReferencingFactory.ALL.equals(authority)) {
            /*
             * "All" is a pseudo-authority declared by AllAuthoritiesFactory. This is not a real
             * authority, so we will not find any identifier if we search for this authority. We
             * will rather pickup the first identifier, regardless its authority.
             */
            authority = null;
        }
        ReferenceIdentifier identifier = AbstractIdentifiedObject.getIdentifier(object, authority);
        if (identifier == null) {
            identifier = object.getName();
            // Should never be null past this point, since 'name' is a mandatory attribute.
        }
        final String codespace = identifier.getCodeSpace();
        final String code = identifier.getCode();
        if (codespace != null) {
            return codespace + org.geotools.util.GenericName.DEFAULT_SEPARATOR + code;
        } else {
            return code;
        }
    }

    /**
     * Creates an object {@linkplain CRS#equalsIgnoreMetadata equals, ignoring metadata}, to the specified object using
     * only the {@linkplain IdentifiedObject#getIdentifiers identifiers}. If no such object is found, returns
     * {@code null}.
     *
     * <p>This method may be used in order to get a fully identified object from a partially identified one.
     *
     * @param object The object looked up.
     * @return The identified object, or {@code null} if not found.
     * @see #createFromCodes
     * @see #createFromNames
     * @throws FactoryException if an error occured while creating an object.
     */
    final IdentifiedObject createFromIdentifiers(final IdentifiedObject object) throws FactoryException {
        final Citation authority = getProxy().getAuthorityFactory().getAuthority();
        final boolean isAll = ReferencingFactory.ALL.equals(authority);
        for (ReferenceIdentifier referenceIdentifier : object.getIdentifiers()) {
            final Identifier id = referenceIdentifier;
            if (!isAll && !Citations.identifierMatches(authority, id.getAuthority())) {
                // The identifier is not for this authority. Looks the other ones.
                continue;
            }
            IdentifiedObject candidate;
            try {
                candidate = getProxy().create(id.getCode());
            } catch (NoSuchAuthorityCodeException e) {
                // The identifier was not recognized. No problem, let's go on.
                continue;
            }
            candidate = deriveEquivalent(candidate, object);
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Creates an object {@linkplain CRS#equalsIgnoreMetadata equals, ignoring metadata}, to the specified object using
     * only the {@linkplain IdentifiedObject#getName name} and {@linkplain IdentifiedObject#getAlias aliases}. If no
     * such object is found, returns {@code null}.
     *
     * <p>This method may be used with some {@linkplain AuthorityFactory authority factory} implementations like the one
     * backed by the EPSG database, which are capable to find an object from its name when the identifier is unknown.
     *
     * @param object The object looked up.
     * @return The identified object, or {@code null} if not found.
     * @see #createFromCodes
     * @see #createFromIdentifiers
     * @throws FactoryException if an error occured while creating an object.
     */
    final IdentifiedObject createFromNames(final IdentifiedObject object) throws FactoryException {
        IdentifiedObject candidate;
        try {
            candidate = getProxy().create(object.getName().getCode());
            candidate = deriveEquivalent(candidate, object);
            if (candidate != null) {
                return candidate;
            }
        } catch (FactoryException e) {
            /*
             * The identifier was not recognized. No problem, let's go on.
             * Note: we catch a more generic exception than NoSuchAuthorityCodeException
             *       because this attempt may fail for various reasons (character string
             *       not supported by the underlying database for primary key, duplicated
             *       name found, etc.).
             */
        }
        for (final GenericName id : object.getAlias()) {
            try {
                candidate = getProxy().create(id.toString());
            } catch (FactoryException e) {
                // The name was not recognized. No problem, let's go on.
                continue;
            }
            candidate = deriveEquivalent(candidate, object);
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Creates an object {@linkplain CRS#equalsIgnoreMetadata equals, ignoring metadata}, to the specified object. This
     * method scans the {@linkplain #getAuthorityCodes authority codes}, create the objects and returns the first one
     * which is equals to the specified object in the sense of {@link CRS#equalsIgnoreMetadata equalsIgnoreMetadata}.
     *
     * <p>This method may be used in order to get a fully {@linkplain IdentifiedObject identified object} from an object
     * without {@linkplain IdentifiedObject#getIdentifiers identifiers}.
     *
     * <p>Scaning the whole set of authority codes may be slow. Users should try <code>
     * {@linkplain #createFromIdentifiers createFromIdentifiers}(object)</code> and/or <code>
     * {@linkplain #createFromNames createFromNames}(object)</code> before to fallback on this method.
     *
     * @param object The object looked up.
     * @return The identified object, or {@code null} if not found.
     * @throws FactoryException if an error occured while scanning through authority codes.
     * @see #createFromIdentifiers
     * @see #createFromNames
     */
    final IdentifiedObject createFromCodes(final IdentifiedObject object, boolean specific) throws FactoryException {
        @SuppressWarnings("unchecked")
        final Set<String> codes = specific ? getSpecificCodeCandidates(object) : getCodeCandidates(object);
        for (final String code : codes) {
            IdentifiedObject candidate;
            try {
                candidate = getProxy().create(code);
            } catch (FactoryException e) {
                LOGGER.log(Level.FINEST, "Could not create '" + code + "':" + e);
                // Some object cannot be created properly.
                continue;
            } catch (Exception problemCode) {
                LOGGER.log(Level.FINEST, "Could not create '" + code + "':" + problemCode, problemCode);
                continue;
            }

            candidate = deriveEquivalent(candidate, object);
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    protected Set getSpecificCodeCandidates(final IdentifiedObject object) throws FactoryException {
        return Collections.emptySet();
    }

    /**
     * Returns a set of authority codes that <strong>may</strong> identify the same object than the specified one. The
     * returned set must contains the code of every objects that are {@linkplain CRS#equalsIgnoreMetadata equals,
     * ignoring metadata}, to the specified one. However the set is not required to contains only the codes of those
     * objects; it may conservatively contains the code for more objects if an exact search is too expensive.
     *
     * <p>This method is invoked by the default {@link #find find} method implementation. The caller may iterates
     * through every returned codes, instantiate the objects and compare them with the specified one in order to
     * determine which codes are really applicable.
     *
     * <p>The default implementation returns the same set than <code>
     * {@linkplain AuthorityFactory#getAuthorityCodes getAuthorityCodes}(type)</code> where {@code type} is the
     * interface specified at construction type. Subclasses should override this method in order to return a smaller
     * set, if they can.
     *
     * @param object The object looked up.
     * @return A set of code candidates.
     * @throws FactoryException if an error occured while fetching the set of code candidates.
     */
    protected Set<String> getCodeCandidates(final IdentifiedObject object) throws FactoryException {
        return getProxy().getAuthorityCodes();
    }

    /*
     * Do NOT define the following method in IdentifiedObjectFinder's API:
     *
     *     protected IdentifiedObject create(String code) throws FactoryException {
     *         return proxy.create(code);
     *     }
     *
     * We may be tempted to put such method in order to allow BufferedAuthorityFactory to
     * override it with caching service,  but it conflicts with AuthorityFactoryAdapter's
     * work. The later (or to be more accurate, OrderedAxisAuthorityFactory) expects axis
     * in (latitude,longitude) order first, in order to test this CRS before to switch to
     * (longitude,latitude) order and test again. If the BufferedAuthorityFactory's cache
     * is in the way, we get directly (longitude,latitude) order and miss an opportunity
     * to identify the user's CRS.
     *
     * We should invoke directly AuthorityFactoryProxy.create(String) instead.
     */

    /**
     * Returns {@code candidate}, or an object derived from {@code candidate}, if it is
     * {@linkplain CRS#equalsIgnoreMetadata equals ignoring metadata} to the specified model. Otherwise returns
     * {@code null}.
     *
     * <p>This method is overriden by factories that may test many flavors of {@code candidate}, for example
     * {@link TransformedAuthorityFactory}.
     *
     * @param candidate An object created by the factory specified at construction time.
     * @return {@code candidate}, or an object derived from {@code candidate} (for example with axis order forced to
     *     (<var>longitude</var>, <var>latitude</var>), or {@code null} if none of the above is
     *     {@linkplain CRS#equalsIgnoreMetadata equals ignoring metadata} to the specified model.
     * @throws FactoryException if an error occured while creating a derived object.
     */
    protected IdentifiedObject deriveEquivalent(final IdentifiedObject candidate, final IdentifiedObject model)
            throws FactoryException {
        return CRS.equalsIgnoreMetadata(candidate, model) ? candidate : null;
    }

    /** Returns a string representation of this finder, for debugging purpose only. */
    @Override
    public String toString() {
        return getProxy().toString(IdentifiedObjectFinder.class);
    }

    /** @param proxy the proxy to set */
    public void setProxy(AuthorityFactoryProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * A finder which delegate part of its work to an other finder. This adapter forwards some method calls to the
     * underlying finder. This class should not be public, because not all method are overriden. The choice is tuned for
     * {@link BufferedAuthorityFactory} and {@link AuthorityFactoryAdapter} needs and may not be appropriate in the
     * general case.
     *
     * @author Martin Desruisseaux
     */
    static class Adapter extends IdentifiedObjectFinder {
        /** The finder on which to delegate the work. */
        protected final IdentifiedObjectFinder finder;

        /** Creates an adapter for the specified finder. */
        protected Adapter(final IdentifiedObjectFinder finder) {
            super(finder);
            this.finder = finder;
        }

        /** Set whatever an exhaustive scan against all registered objects is allowed. */
        @Override
        public void setFullScanAllowed(final boolean fullScan) {
            finder.setFullScanAllowed(fullScan);
            super.setFullScanAllowed(fullScan);
        }

        /**
         * Returns a set of authority codes that <strong>may</strong> identify the same object than the specified one.
         * The default implementation delegates to the backing finder.
         */
        @Override
        protected Set<String> getCodeCandidates(final IdentifiedObject object) throws FactoryException {
            return finder.getCodeCandidates(object);
        }

        @Override
        protected Set getSpecificCodeCandidates(IdentifiedObject object) throws FactoryException {
            return finder.getSpecificCodeCandidates(object);
        }

        /**
         * Returns {@code candidate}, or an object derived from {@code candidate}, if it is
         * {@linkplain CRS#equalsIgnoreMetadata equals ignoring metadata} to the specified model. The default
         * implementation delegates to the backing finder.
         */
        @Override
        protected IdentifiedObject deriveEquivalent(final IdentifiedObject candidate, final IdentifiedObject model)
                throws FactoryException {
            return finder.deriveEquivalent(candidate, model);
        }
    }
}
