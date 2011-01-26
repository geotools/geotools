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
package org.geotools.referencing.factory.wms;

// J2SE dependencies
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.Set;
import java.util.LinkedHashSet;

// OpenGIS dependencies
import org.opengis.util.InternationalString;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.ProjectedCRS;

// Geotools dependencies
import org.geotools.factory.Hints;
import org.geotools.util.SimpleInternationalString;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.referencing.factory.DirectAuthorityFactory;


/**
 * The factory for {@linkplain ProjectedCRS projected CRS} in the {@code AUTO} and {@code AUTO2}
 * space.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Rueben Schulz
 * @author Martin Desruisseaux
 *
 * @deprecated This class will move in a <code>org.geotools.referencing.factory.<strong>web</strong></code>
 *             package in a future Geotools version, in order to put together other web-related factories defined
 *             outside the WMS specification. Don't use this class directly. You should not need to
 *             anyway - use {@link org.geotools.referencing.ReferencingFactoryFinder} instead, which
 *             will continue to work no matter where this class is located.
 */
public class AutoCRSFactory extends DirectAuthorityFactory implements CRSAuthorityFactory {
    /**
     * The authority code. We use {@code AUTO2} citation, but merge {@code AUTO} and
     * {@code AUTO2} identifiers in order to use the same factory for both authorities.
     */
    private static final Citation AUTHORITY;
    static {
        final CitationImpl c = new CitationImpl(Citations.AUTO2);
        c.getIdentifiers().addAll(Citations.AUTO.getIdentifiers());
        AUTHORITY = (Citation) c.unmodifiable();
    }

    /**
     * Map of Factlets by integer code (from {@code AUTO:code}).
     *
     * @todo Replace this with full FactorySPI system.
     */
    private final Map<Integer,Factlet> factlets = new TreeMap<Integer,Factlet>();

    /**
     * Constructs a default factory for the {@code AUTO} authority.
     */
    public AutoCRSFactory() {
        this(null);
    }

    /**
     * Constructs a factory for the {@code AUTO} authority using the specified hints.
     */
    public AutoCRSFactory(final Hints hints) {
        super(hints, NORMAL_PRIORITY);
        add(Auto42001.DEFAULT);
        add(Auto42002.DEFAULT);
        add(Auto42003.DEFAULT);
        add(Auto42004.DEFAULT);
        // We don't actually support the Mollweide projection
        // add(Auto42005.DEFAULT);
    }

    /**
     * Add the specified factlet.
     */
    private void add(final Factlet f) {
        final int code = f.code();
        if (factlets.put(code, f) != null) {
            throw new IllegalArgumentException(String.valueOf(code));
        }
    }

    /**
     * Returns the {@link Factlet} for the given code.
     *
     * @param  code The code.
     * @return The fatclet for the specified code.
     * @throws NoSuchAuthorityCodeException if no factlet has been found for the specified code.
     */
    private Factlet findFactlet(final Code code) throws NoSuchAuthorityCodeException {
        if (code.authority.equalsIgnoreCase("AUTO") ||
            code.authority.equalsIgnoreCase("AUTO2"))
        {
            final Integer key = code.code;
            final Factlet fac = factlets.get(key);
            if (fac != null) {
                return fac;
            }
        }
        throw noSuchAuthorityCode(code.type, code.toString());
    }

    /**
     * Returns the authority for this factory.
     */
    public Citation getAuthority() {
        return AUTHORITY;
    }

    /**
     * Provides a complete set of the known codes provided by this authority.
     * The returned set contains only numeric identifiers like {@code "42001"},
     * {@code "42002"}, <cite>etc</cite>. The authority name ({@code "AUTO"})
     * and the {@code lon0,lat0} part are not included. This is consistent with the
     * {@linkplain org.geotools.referencing.factory.epsg.DirectEpsgFactory#getAuthorityCodes
     * codes returned by the EPSG factory} and avoid duplication, since the authority is the
     * same for every codes returned by this factory. It also make it easier for clients to
     * prepend whatever authority name they wish, as for example in the
     * {@linkplain org.geotools.referencing.factory.AllAuthoritiesFactory#getAuthorityCodes
     * all authorities factory}.
     */
    public Set getAuthorityCodes(final Class type) throws FactoryException {
        if (type.isAssignableFrom(ProjectedCRS.class)) {
            final Set set = new LinkedHashSet();
            for (final Iterator it=factlets.keySet().iterator(); it.hasNext();) {
                Integer code = (Integer) it.next();
                set.add(String.valueOf(code));
            }
            return set;
        } else {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Returns the CRS name for the given code.
     */
    public InternationalString getDescriptionText(final String code) throws FactoryException {
        final Code c = new Code(code, ProjectedCRS.class);
        return new SimpleInternationalString(findFactlet(c).getName());
    }

    /**
     * Creates an object from the specified code. The default implementation delegates to
     * <code>{@linkplain #createCoordinateReferenceSystem createCoordinateReferenceSystem}(code)</code>.
     */
    public IdentifiedObject createObject(final String code) throws FactoryException {
        return createCoordinateReferenceSystem(code);
    }

    /**
     * Creates a coordinate reference system from the specified code. The default implementation
     * delegates to <code>{@linkplain #createProjectedCRS createProjectedCRS}(code)</code>.
     */
    public CoordinateReferenceSystem createCoordinateReferenceSystem(final String code)
            throws FactoryException
    {
        return createProjectedCRS(code);
    }

    /**
     * Creates a projected coordinate reference system from the specified code.
     */
    public ProjectedCRS createProjectedCRS(final String code) throws FactoryException {
        final Code c = new Code(code, ProjectedCRS.class);
        return findFactlet(c).create(c, factories);
    }
}
