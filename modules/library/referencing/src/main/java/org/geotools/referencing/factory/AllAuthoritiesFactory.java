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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;

import org.geotools.factory.Hints;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.referencing.ReferencingFactoryFinder;


/**
 * An authority factory that delegates the object creation to an other factory determined
 * from the authority name in the code. This is similar to {@link ManyAuthoritiesFactory}
 * except that the set of factories is determined by calls to
 * <code>ReferencingFactoryFinder.{@linkplain ReferencingFactoryFinder#getCRSAuthorityFactory
 * get<var>Foo</var>AuthorityFactory}(<var>authority</var>, {@linkplain #hints hints})</code>.
 * <p>
 * This class is not registered in {@link ReferencingFactoryFinder}. If this "authority" factory
 * is wanted, then users need to refer explicitly to the {@link #DEFAULT} constant or to create
 * their own instance.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class AllAuthoritiesFactory extends ManyAuthoritiesFactory {
    /**
     * An instance of {@code AllAuthoritiesFactory} with the
     * {@linkplain GenericName#DEFAULT_SEPARATOR default name separator} and no hints.
     */
    public static AllAuthoritiesFactory DEFAULT = new AllAuthoritiesFactory(null);

    /**
     * The authority names. Used in order to detect changes in the set of registered factories.
     */
    private Collection<String> authorityNames;

    /**
     * Creates a new factory using the specified hints.
     *
     * @param hints An optional set of hints, or {@code null} if none.
     */
    public AllAuthoritiesFactory(final Hints hints) {
        super(null);
        addImplementationHints(hints);
    }

    /**
     * Returns the set of authority names.
     *
     * @since 2.4
     */
    @Override
    public Set<String> getAuthorityNames() {
        // Do not use 'authorityNames' since it may be out-of-date.
        return ReferencingFactoryFinder.getAuthorityNames();
    }

    /**
     * Returns the factories to be used by {@link ManyAuthoritiesFactory}. If the registered
     * factories changed since the last time this method has been invoked, then this method
     * recreate the set.
     */
    @Override
    Collection<AuthorityFactory> getFactories() {
        final Collection<String> authorities = ReferencingFactoryFinder.getAuthorityNames();
        if (authorities != authorityNames) {
            authorityNames = authorities;
            final Hints hints = getHints();
            final Set<AuthorityFactory> factories = new LinkedHashSet<AuthorityFactory>();
            factories.addAll(ReferencingFactoryFinder.getCRSAuthorityFactories                (hints));
            factories.addAll(ReferencingFactoryFinder.getCSAuthorityFactories                 (hints));
            factories.addAll(ReferencingFactoryFinder.getDatumAuthorityFactories              (hints));
            factories.addAll(ReferencingFactoryFinder.getCoordinateOperationAuthorityFactories(hints));
            setFactories(factories);
        }
        return super.getFactories();
    }

    /**
     * Returns a factory for the specified authority and type.
     */
    @Override
    final <T extends AuthorityFactory> T fromFactoryRegistry(final String authority, final Class<T> type)
            throws FactoryRegistryException
    {
        final AuthorityFactory f;
        if (CRSAuthorityFactory.class.equals(type)) {
            f = ReferencingFactoryFinder.getCRSAuthorityFactory(authority, getHints());
        } else if (CSAuthorityFactory.class.equals(type)) {
            f = ReferencingFactoryFinder.getCSAuthorityFactory(authority, getHints());
        } else if (DatumAuthorityFactory.class.equals(type)) {
            f = ReferencingFactoryFinder.getDatumAuthorityFactory(authority, getHints());
        } else if (CoordinateOperationAuthorityFactory.class.equals(type)) {
            f = ReferencingFactoryFinder.getCoordinateOperationAuthorityFactory(authority, getHints());
        } else {
            f = super.fromFactoryRegistry(authority, type);
        }
        return type.cast(f);
    }

    /**
     * Returns a copy of the hints specified by the user at construction time.
     */
    private Hints getHints() {
        if (hints.isEmpty()) {
            return ReferencingFactoryFinder.EMPTY_HINTS;
        } else {
            // Clones EMPTY_HINTS as a trick for getting a StricHints instance.
            final Hints hints = ReferencingFactoryFinder.EMPTY_HINTS.clone();
            hints.putAll(this.hints);
            return hints;
        }
    }

    /**
     * Returns a finder which can be used for looking up unidentified objects.
     * The default implementation delegates the lookups to the underlying factories.
     *
     * @since 2.4
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
    private static final class Finder extends ManyAuthoritiesFactory.Finder {
        /**
         * Creates a finder for the specified type.
         */
        protected Finder(final ManyAuthoritiesFactory factory,
                         final Class<? extends IdentifiedObject> type)
        {
            super(factory, type);
        }

        /**
         * Returns all factories to try.
         */
        private Set<AuthorityFactory> fromFactoryRegistry() {
            final ManyAuthoritiesFactory factory = (ManyAuthoritiesFactory) getProxy().getAuthorityFactory();
            final Class<? extends AuthorityFactory> type = getProxy().getType();
            final Set<AuthorityFactory> factories = new LinkedHashSet<AuthorityFactory>();
            for (final String authority : ReferencingFactoryFinder.getAuthorityNames()) {
                factory.fromFactoryRegistry(authority, type, factories);
            }
            // Removes the factories already tried by super-class.
            final Collection<AuthorityFactory> done = getFactories();
            if (done != null) {
                factories.removeAll(done);
            }
            return factories;
        }

        /**
         * Lookups for the specified object.
         */
        @Override
        public IdentifiedObject find(final IdentifiedObject object) throws FactoryException {
            IdentifiedObject candidate = super.find(object);
            if (candidate != null) {
                return candidate;
            }
            IdentifiedObjectFinder finder;
            final Iterator<AuthorityFactory> it = fromFactoryRegistry().iterator();
            while ((finder = next(it)) != null) {
                candidate = finder.find(object);
                if (candidate != null) {
                    break;
                }
            }
            return candidate;
        }

        /**
         * Returns the identifier of the specified object, or {@code null} if none.
         */
        @Override
        public String findIdentifier(final IdentifiedObject object) throws FactoryException {
            String candidate = super.findIdentifier(object);
            if (candidate != null) {
                return candidate;
            }
            IdentifiedObjectFinder finder;
            final Iterator<AuthorityFactory> it = fromFactoryRegistry().iterator();
            while ((finder = next(it)) != null) {
                candidate = finder.findIdentifier(object);
                if (candidate != null) {
                    break;
                }
            }
            return candidate;
        }
    }
}
