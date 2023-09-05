/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.datum;

import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;

/**
 * Creates {@linkplain Datum datum} objects using authority codes. External authorities are used to
 * manage definitions of objects used in this interface. The definitions of these objects are
 * referenced using code strings. A commonly used authority is <A
 * HREF="http://www.epsg.org">EPSG</A>, which is also used in the <A
 * HREF="http://www.remotesensing.org/geotiff/geotiff.html">GeoTIFF</A> standard.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see org.geotools.api.referencing.cs.CSAuthorityFactory
 * @see org.geotools.api.referencing.crs.CRSAuthorityFactory
 */
public interface DatumAuthorityFactory extends AuthorityFactory {
    /**
     * Returns an arbitrary {@linkplain Datum datum} from a code. If the datum type is know at
     * compile time, it is recommended to invoke the most precise method instead of this one (for
     * example <code>&nbsp;{@linkplain #createGeodeticDatum createGeodeticDatum}(code)&nbsp;</code>
     * instead of <code>&nbsp;createDatum(code)&nbsp;</code> if the caller know he is asking for a
     * {@linkplain GeodeticDatum geodetic datum}).
     *
     * @param code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see #createGeodeticDatum
     * @see #createVerticalDatum
     * @see #createTemporalDatum
     */
    Datum createDatum(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Creates a {@linkplain EngineeringDatum engineering datum} from a code.
     *
     * @param code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createEngineeringCRS
     */
    EngineeringDatum createEngineeringDatum(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Creates a {@linkplain ImageDatum image datum} from a code.
     *
     * @param code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createImageCRS
     */
    ImageDatum createImageDatum(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Creates a {@linkplain VerticalDatum vertical datum} from a code.
     *
     * @param code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createVerticalCRS
     */
    VerticalDatum createVerticalDatum(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Creates a {@linkplain TemporalDatum temporal datum} from a code.
     *
     * @param code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createTemporalCRS
     */
    TemporalDatum createTemporalDatum(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns a {@linkplain GeodeticDatum geodetic datum} from a code.
     *
     * @param code Value allocated by authority.
     * @return The datum for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see #createEllipsoid
     * @see #createPrimeMeridian
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createGeographicCRS
     * @see org.geotools.api.referencing.crs.CRSAuthorityFactory#createProjectedCRS
     */
    GeodeticDatum createGeodeticDatum(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns an {@linkplain Ellipsoid ellipsoid} from a code.
     *
     * @param code Value allocated by authority.
     * @return The ellipsoid for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see #createGeodeticDatum
     */
    Ellipsoid createEllipsoid(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns a {@linkplain PrimeMeridian prime meridian} from a code.
     *
     * @param code Value allocated by authority.
     * @return The prime meridian for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see #createGeodeticDatum
     */
    PrimeMeridian createPrimeMeridian(String code)
            throws NoSuchAuthorityCodeException, FactoryException;
}
