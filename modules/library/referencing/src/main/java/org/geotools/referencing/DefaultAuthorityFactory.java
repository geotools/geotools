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
package org.geotools.referencing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.ManyAuthoritiesFactory;
import org.geotools.referencing.factory.ThreadedAuthorityFactory;
import org.geotools.util.UnmodifiableArrayList;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * The default authority factory to be used by {@link CRS#decode}.
 *
 * <p>This class gathers together a lot of logic in order to capture the following ideas:
 *
 * <ul>
 *   <li>Uses {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER} to swap ordinate order if needed.
 *   <li>Uses {@link ManyAuthoritiesFactory} to access CRSAuthorities in the environment.
 * </ul>
 *
 * @since 2.3
 * @version $Id$
 * @author Martin Desruisseaux
 * @author Andrea Aime
 */
final class DefaultAuthorityFactory extends ThreadedAuthorityFactory implements CRSAuthorityFactory {
    /** List of codes without authority space. We can not defines them in an ordinary authority factory. */
    private static List<String> AUTHORITY_LESS = UnmodifiableArrayList.wrap("WGS84(DD)");

    private static Logger LOGGER = Logging.getLogger(DefaultAuthorityFactory.class);

    /** Creates a new authority factory. */
    DefaultAuthorityFactory(final boolean longitudeFirst) {
        super(getBackingFactory(longitudeFirst));
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database ("Relax constraint on placement of this()/super() call in
     * constructors").
     */
    private static AbstractAuthorityFactory getBackingFactory(final boolean longitudeFirst) {
        final Hints hints = GeoTools.getDefaultHints();
        if (longitudeFirst) {
            hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            /*
             * Otherwise do NOT set the hint to false. If 'longitudeFirst' is false, this means
             * "use the system default", not "latitude first". The longitude may or may
             * not be first depending the value of "org.geotools.referencing.forcexy"
             * system property. This state is included in GeoTools.getDefaultHints().
             */
        }

        Collection<CRSAuthorityFactory> factories =
                new ArrayList<>(ReferencingFactoryFinder.getCRSAuthorityFactories(hints));
        if (Boolean.TRUE.equals(hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE))) {
            /*
             * If hints contain a requirement for "longitude first", then we may loose some
             * authorities like "URN:OGC:...". Search again without such requirement and add
             * any new authorities found.
             */

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Factories with FORCE_LONGITUDE_FIRST_AXIS_ORDER=true :\n" + logClassNames(factories));
            }
            factories.addAll(ReferencingFactoryFinder.getCRSAuthorityFactories(hints));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Factories used as backingFactory :\n" + logClassNames(factories));
        }
        return new ManyAuthoritiesFactory(factories);
    }

    private static String logClassNames(final Collection<CRSAuthorityFactory> factories) {
        return String.join(
                "\n",
                factories.stream()
                        .map(factory -> "  " + factory.getClass().getName())
                        .toArray(String[]::new));
    }

    /**
     * Implementation of {@link CRS#getSupportedCodes}. Provided here in order to reduce the amount of class loading
     * when using {@link CRS} for other purpose than CRS decoding.
     */
    static Set<String> getSupportedCodes(final String authority) {
        final Set<String> result = new LinkedHashSet<>(AUTHORITY_LESS);
        for (final CRSAuthorityFactory factory : ReferencingFactoryFinder.getCRSAuthorityFactories(null)) {
            if (Citations.identifierMatches(factory.getAuthority(), authority)) {
                final Set<String> codes;
                try {
                    codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
                } catch (Exception exception) {
                    /*
                     * Failed to fetch the codes either because of a database connection problem
                     * (FactoryException), or because we are using a simple factory that doesn't
                     * support this operation (UnsupportedOperationException), or any unexpected
                     * reason. No codes from this factory will be added to the set.
                     */
                    CRS.unexpectedException("getSupportedCodes", exception);
                    continue;
                }
                if (codes != null) {
                    result.addAll(codes);
                }
            }
        }
        return result;
    }

    /**
     * Implementation of {@link CRS#getSupportedAuthorities}. Provided here in order to reduce the amount of class
     * loading when using {@link CRS} for other purpose than CRS decoding.
     */
    static Set<String> getSupportedAuthorities(final boolean returnAliases) {
        final Set<String> result = new LinkedHashSet<>();
        for (final CRSAuthorityFactory factory : ReferencingFactoryFinder.getCRSAuthorityFactories(null)) {
            for (final Identifier id : factory.getAuthority().getIdentifiers()) {
                result.add(id.getCode());
                if (!returnAliases) {
                    break;
                }
            }
        }
        return result;
    }

    /** Returns the coordinate reference system for the given code. */
    @Override
    @SuppressWarnings("UnsynchronizedOverridesSynchronized")
    public CoordinateReferenceSystem createCoordinateReferenceSystem(String code) throws FactoryException {
        if (code != null) {
            code = code.trim();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Create CRS with code:" + code);
            }
            if (code.equalsIgnoreCase("WGS84(DD)")) {
                return DefaultGeographicCRS.WGS84;
            }
        }
        assert !AUTHORITY_LESS.contains(code) : code;
        return super.createCoordinateReferenceSystem(code);
    }
}
