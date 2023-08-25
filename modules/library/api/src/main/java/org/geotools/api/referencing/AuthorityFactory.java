/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing;

import java.util.Set;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.util.InternationalString;

/**
 * Base interface for all authority factories. An <cite>authority</cite> is an organization that
 * maintains definitions of authority codes. An <cite>authority code</cite> is a compact string
 * defined by an authority to reference a particular spatial reference object. For example the <A
 * HREF="http://www.epsg.org">European Petroleum Survey Group (EPSG)</A> maintains a database of
 * coordinate systems, and other spatial referencing objects, where each object has a code number
 * ID. For example, the EPSG code for a WGS84 Lat/Lon coordinate system is '4326'.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface AuthorityFactory extends Factory {
    /**
     * Returns the organization or party responsible for definition and maintenance of the database.
     *
     * @return The organization reponsible for definition of the database.
     */
    Citation getAuthority();

    /**
     * Returns the set of authority codes of the given type. The {@code type} argument specify the
     * base class. For example if this factory is an instance of {@link
     * org.geotools.api.referencing.crs.CRSAuthorityFactory}, then:
     *
     * <ul>
     *   <li><b><code>
     *       {@linkplain org.geotools.api.referencing.crs.CoordinateReferenceSystem}.class&nbsp;
     *       </code></b> asks for all authority codes accepted by one of {@link
     *       org.geotools.api.referencing.crs.CRSAuthorityFactory#createGeographicCRS
     *       createGeographicCRS}, {@link
     *       org.geotools.api.referencing.crs.CRSAuthorityFactory#createProjectedCRS
     *       createProjectedCRS}, {@link
     *       org.geotools.api.referencing.crs.CRSAuthorityFactory#createVerticalCRS
     *       createVerticalCRS}, {@link
     *       org.geotools.api.referencing.crs.CRSAuthorityFactory#createTemporalCRS
     *       createTemporalCRS} and their friends.
     *   <li><b><code>{@linkplain org.geotools.api.referencing.crs.ProjectedCRS}.class&nbsp;</code>
     *       </b> asks only for authority codes accepted by {@link
     *       org.geotools.api.referencing.crs.CRSAuthorityFactory#createProjectedCRS
     *       createProjectedCRS}.
     * </ul>
     *
     * @param type The spatial reference objects type.
     * @return The set of authority codes for spatial reference objects of the given type. If this
     *     factory doesn't contains any object of the given type, then this method returns an
     *     {@linkplain java.util.Collections.emptySet() empty set}.
     * @throws FactoryException if access to the underlying database failed.
     */
    Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) throws FactoryException;

    /**
     * Gets a description of the object corresponding to a code.
     *
     * @param code Value allocated by authority.
     * @return A description of the object, or {@code null} if the object corresponding to the
     *     specified {@code code} has no description.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the query failed for some other reason.
     */
    InternationalString getDescriptionText(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns an arbitrary object from a code. The returned object will typically be an instance of
     * {@link org.geotools.api.referencing.datum.Datum}, {@link
     * org.geotools.api.referencing.cs.CoordinateSystem}, {@link
     * org.geotools.api.referencing.ReferenceSystem} or {@link
     * org.geotools.api.referencing.operation.CoordinateOperation}. If the type of the object is
     * know at compile time, it is recommended to invoke the most precise method instead of this one
     * (for example <code>
     * &nbsp;{@linkplain org.geotools.api.referencing.crs.CRSAuthorityFactory#createCoordinateReferenceSystem
     * createCoordinateReferenceSystem}(code)&nbsp;</code> instead of <code>
     * &nbsp;createObject(code)&nbsp;</code> if the caller know he is asking for a {@linkplain
     * org.geotools.api.referencing.crs.CoordinateReferenceSystem coordinate reference system}).
     *
     * @param code Value allocated by authority.
     * @return The object for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.datum.DatumAuthorityFactory#createDatum
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createCoordinateReferenceSystem
     */
    IdentifiedObject createObject(String code)
            throws NoSuchAuthorityCodeException, FactoryException;
}
