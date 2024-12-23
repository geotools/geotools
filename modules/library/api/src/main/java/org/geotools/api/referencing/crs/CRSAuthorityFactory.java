/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.crs;

import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException; // For javadoc

/**
 * Creates {@linkplain CoordinateReferenceSystem coordinate reference systems} using authority codes. External
 * authorities are used to manage definitions of objects used in this interface. The definitions of these objects are
 * referenced using code strings. A commonly used authority is <A HREF="http://www.epsg.org">EPSG</A>, which is also
 * used in the <A HREF="http://www.remotesensing.org/geotiff/geotiff.html">GeoTIFF</A> standard.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see org.geotools.api.referencing.cs.CSAuthorityFactory
 * @see org.geotools.api.referencing.datum.DatumAuthorityFactory
 */
public interface CRSAuthorityFactory extends AuthorityFactory {
    /**
     * Returns an arbitrary {@linkplain CoordinateReferenceSystem coordinate reference system} from a code. If the
     * coordinate reference system type is know at compile time, it is recommended to invoke the most precise method
     * instead of this one (for example <code>
     * &nbsp;{@linkplain #createGeographicCRS createGeographicCRS}(code)&nbsp;</code> instead of <code>
     * &nbsp;createCoordinateReferenceSystem(code)&nbsp;</code> if the caller know he is asking for a
     * {@linkplain GeographicCRS geographic coordinate reference system}).
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see #createGeographicCRS
     * @see #createProjectedCRS
     * @see #createVerticalCRS
     * @see #createTemporalCRS
     * @see #createCompoundCRS
     */
    CoordinateReferenceSystem createCoordinateReferenceSystem(String code)
            throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Creates a 3D coordinate reference system from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    CompoundCRS createCompoundCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Creates a derived coordinate reference system from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    DerivedCRS createDerivedCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Create a {@linkplain EngineeringCRS engineering coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    EngineeringCRS createEngineeringCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns a {@linkplain GeographicCRS geographic coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.datum.DatumAuthorityFactory#createGeodeticDatum
     */
    GeographicCRS createGeographicCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns a {@linkplain GeocentricCRS geocentric coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed.
     * @see org.geotools.api.referencing.datum.DatumAuthorityFactory#createGeodeticDatum
     */
    GeocentricCRS createGeocentricCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Create a {@linkplain ImageCRS image coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     */
    ImageCRS createImageCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Returns a {@linkplain ProjectedCRS projected coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.datum.DatumAuthorityFactory#createGeodeticDatum
     */
    ProjectedCRS createProjectedCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Create a {@linkplain TemporalCRS temporal coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.datum.DatumAuthorityFactory#createTemporalDatum
     */
    TemporalCRS createTemporalCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;

    /**
     * Create a {@linkplain VerticalCRS vertical coordinate reference system} from a code.
     *
     * @param code Value allocated by authority.
     * @return The coordinate reference system for the given code.
     * @throws NoSuchAuthorityCodeException if the specified {@code code} was not found.
     * @throws FactoryException if the object creation failed for some other reason.
     * @see org.geotools.api.referencing.datum.DatumAuthorityFactory#createVerticalDatum
     */
    VerticalCRS createVerticalCRS(String code) throws NoSuchAuthorityCodeException, FactoryException;
}
