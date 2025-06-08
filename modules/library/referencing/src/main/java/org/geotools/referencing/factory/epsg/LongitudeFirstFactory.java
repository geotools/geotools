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
package org.geotools.referencing.factory.epsg;

import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.cs.CSAuthorityFactory;
import org.geotools.api.referencing.datum.DatumAuthorityFactory;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.DeferredAuthorityFactory;
import org.geotools.referencing.factory.OrderedAxisAuthorityFactory;
import org.geotools.util.factory.FactoryNotFoundException;
import org.geotools.util.factory.FactoryRegistryException;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * An EPSG authority factory using (<var>longitude</var>, <var>latitude</var>) axis order. This factory wraps a
 * {@link ThreadedEpsgFactory} into an {@link OrderedAxisAuthorityFactory} when first needed.
 *
 * <p>Users don't need to create explicitly an instance of this class. Instead, one can get an instance using the
 * following code:
 *
 * <blockquote>
 *
 * <pre>
 * Hints hints = new Hints({@linkplain Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER}, Boolean.TRUE);
 * CRSAuthorityFactory factory = {@linkplain ReferencingFactoryFinder}.getCRSAuthorityFactory("EPSG", hints);
 * </pre>
 *
 * </blockquote>
 *
 * @since 2.3
 * @author Martin Desruisseaux
 * @see OrderedAxisAuthorityFactory
 * @see Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
 */
public class LongitudeFirstFactory extends DeferredAuthorityFactory
        implements CRSAuthorityFactory, CSAuthorityFactory, CoordinateOperationAuthorityFactory, DatumAuthorityFactory {
    /*
     * Implementation note: in theory the DatumAuthorityFactory interface is useless here, since
     * "axis order" doesn't make any sense for them. However if we do not register this class for
     * the DatumAuthorityFactory as well, user will get a FactoryNotFoundException when asking for
     * a factory with the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint set.
     */

    /**
     * Creates a default factory. The {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER FORCE_LONGITUDE_FIRST_AXIS_ORDER}
     * hint is always set to {@link Boolean#TRUE TRUE}. The {@link Hints#FORCE_STANDARD_AXIS_DIRECTIONS
     * FORCE_STANDARD_AXIS_DIRECTIONS} and {@link Hints#FORCE_STANDARD_AXIS_UNITS FORCE_STANDARD_AXIS_UNITS} hints are
     * set to {@link Boolean#FALSE FALSE} by default. A different value for those two hints can be specified using the
     * {@linkplain LongitudeFirstFactory(Hints) constructor below}.
     */
    public LongitudeFirstFactory() {
        this(null);
    }

    /**
     * Creates a factory from the specified set of hints.
     *
     * @param userHints An optional set of hints, or {@code null} for the default values.
     */
    public LongitudeFirstFactory(final Hints userHints) {
        super(userHints, ThreadedEpsgFactory.PRIORITY + relativePriority());
        // See comment in createBackingStore() method body.
        hints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        put(userHints, Hints.FORCE_STANDARD_AXIS_DIRECTIONS);
        put(userHints, Hints.FORCE_STANDARD_AXIS_UNITS);
    }

    /** Stores a value from the specified hints. */
    private void put(final Hints userHints, final Hints.Key key) {
        Object value = null;
        if (userHints != null) {
            value = userHints.get(key);
        }
        if (value == null) {
            value = Boolean.FALSE;
        }
        hints.put(key, value);
    }

    /**
     * Returns the priority to use relative to the {@link ThreadedEpsgFactory} priority. The default priority should be
     * lower, except if the <code>
     * {@value GeoTools#FORCE_LONGITUDE_FIRST_AXIS_ORDER}</code> system property is set to {@code true}.
     */
    private static int relativePriority() {
        try {
            if (Boolean.getBoolean(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER)) {
                return +7;
            }
        } catch (SecurityException e) {
            // Fall back on default value.
        }
        return -7;
    }

    /**
     * Returns the authority for this EPSG database. This authority will contains the database version in the
     * {@linkplain Citation#getEdition edition} attribute, together with the {@linkplain Citation#getEditionDate edition
     * date}.
     */
    @Override
    public Citation getAuthority() {
        final Citation authority = super.getAuthority();
        return authority != null ? authority : Citations.EPSG;
    }

    /**
     * Returns the factory instance (usually {@link ThreadedEpsgFactory}) to be used as the backing store.
     *
     * @throws FactoryException If no suitable factory instance was found.
     */
    @Override
    protected AbstractAuthorityFactory createBackingStore() throws FactoryException {
        /*
         * Set the hints for the backing store to fetch. I'm not sure that we should request a
         * org.geotools.referencing.factory.epsg.ThreadedEpsgFactory implementation; for now we are
         * making this requirement mostly as a safety in order to get an implementation that is
         * known to work, but we could relax that in a future version. AbstractAuthorityFactory
         * is the minimal class required with current OrderedAxisAuthorityFactory API.
         *
         * The really important hints are the FORCE_*_AXIS_* handled by this class, which MUST
         * be set to FALSE. This is especially important for FORCE_LONGITUDE_FIRST_AXIS_ORDER,
         * which must be set to a different value than the value set by the constructor in order
         * to prevent LongitudeFirstFactory to fetch itself. The other hints must also be set to
         * false since forcing axis directions / units is handled by OrderedAxisAuthorityFactory
         * and we don't want the backing store to interfer with that.
         */
        final Hints backingStoreHints = new Hints(Hints.CRS_AUTHORITY_FACTORY, ThreadedEpsgFactory.class);
        backingStoreHints.put(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.FALSE);
        backingStoreHints.put(Hints.FORCE_STANDARD_AXIS_DIRECTIONS, Boolean.FALSE);
        backingStoreHints.put(Hints.FORCE_STANDARD_AXIS_UNITS, Boolean.FALSE);
        final AbstractAuthorityFactory factory;
        try {
            factory = (AbstractAuthorityFactory)
                    ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", backingStoreHints);
        } catch (FactoryNotFoundException exception) {
            throw new org.geotools.referencing.factory.FactoryNotFoundException(exception);
        } catch (FactoryRegistryException exception) {
            throw new FactoryException(exception);
        }
        return new OrderedAxisAuthorityFactory(factory, new Hints(hints));
    }
}
