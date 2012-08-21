/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Wraps {@linkplain AllAuthoritiesFactory all factories} in a {@code "urn:ogc:def"} name space. An example of complete URN is
 * {@code "urn:ogc:def:crs:EPSG:6.8:4326"}.
 * <p>
 * Users don't need to create an instance of this class, since one is automatically registered for use in
 * {@link org.opengis.referencing.ReferencingFactoryFinder}.
 * 
 * @author Justin Deoliveira
 * @author Martin Desruisseaux
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * 
 * @see <A HREF="https://portal.opengeospatial.org/files/?artifact_id=8814">URNs of definitions in OGC namespace</A>
 * 
 * @source $URL$
 */
public class URN_AuthorityFactory extends Abstract_URI_AuthorityFactory {

    /**
     * The name used in {@link Hints#FORCE_AXIS_ORDER_HONORING} for this factory.
     */
    public static final String HINTS_AUTHORITY = "urn";

    /**
     * Constructor.
     * 
     * @see Abstract_URI_AuthorityFactory#Abstract_URI_AuthorityFactory(String)
     */
    public URN_AuthorityFactory() {
        super(HINTS_AUTHORITY);
    }

    /**
     * Constructor.
     * 
     * @see Abstract_URI_AuthorityFactory#Abstract_URI_AuthorityFactory(Hints, String)
     */
    public URN_AuthorityFactory(Hints userHints) {
        super(userHints, HINTS_AUTHORITY);
    }

    /**
     * Constructor.
     * 
     * @see Abstract_URI_AuthorityFactory#Abstract_URI_AuthorityFactory(AllAuthoritiesFactory)
     */
    public URN_AuthorityFactory(AllAuthoritiesFactory factory) {
        super(factory);
    }

    /**
     * @see org.geotools.referencing.factory.Abstract_URI_AuthorityFactory#getAuthority()
     */
    @Override
    public Citation getAuthority() {
        return Citations.URN_OGC;
    }

    /**
     * @see org.geotools.referencing.factory.Abstract_URI_AuthorityFactory#buildParser(java.lang.String)
     */
    @Override
    protected URI_Parser buildParser(String code) throws NoSuchAuthorityCodeException {
        return URN_Parser.buildParser(code);
    }

}
