/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing.operation;

import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19111;

import java.util.Collection;
import org.geotools.api.annotation.UML;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.metadata.quality.PositionalAccuracy;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;

/**
 * A mathematical operation on coordinates that transforms or converts coordinates to another
 * coordinate reference system. Many but not all coordinate operations (from {@linkplain
 * CoordinateReferenceSystem coordinate reference system} <VAR>A</VAR> to {@linkplain
 * CoordinateReferenceSystem coordinate reference system} <VAR>B</VAR>) also uniquely define the
 * inverse operation (from {@linkplain CoordinateReferenceSystem coordinate reference system}
 * <VAR>B</VAR> to {@linkplain CoordinateReferenceSystem coordinate reference system} <VAR>A</VAR>).
 * In some cases, the operation method algorithm for the inverse operation is the same as for the
 * forward algorithm, but the signs of some operation parameter values must be reversed. In other
 * cases, different algorithms are required for the forward and inverse operations, but the same
 * operation parameter values are used. If (some) entirely different parameter values are needed, a
 * different coordinate operation shall be defined.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface CoordinateOperation extends IdentifiedObject {
    /**
     * Key for the <code>{@value}</code> property. This is used for setting the value to be returned
     * by {@link #getOperationVersion}.
     *
     * @see #getOperationVersion
     */
    String OPERATION_VERSION_KEY = "operationVersion";

    /**
     * Key for the <code>{@value}</code> property. This is used for setting the value to be returned
     * by {@link #getCoordinateOperationAccuracy}.
     *
     * @see #getCoordinateOperationAccuracy
     * @since GeoAPI 2.1
     */
    String COORDINATE_OPERATION_ACCURACY_KEY = "coordinateOperationAccuracy";

    /**
     * Key for the <code>{@value}</code> property. This is used for setting the value to be returned
     * by {@link #getDomainOfValidity}.
     *
     * @see #getDomainOfValidity
     * @since GeoAPI 2.1
     */
    String DOMAIN_OF_VALIDITY_KEY = "domainOfValidity";

    /**
     * Key for the <code>{@value}</code> property. This is used for setting the value to be returned
     * by {@link #getScope}.
     *
     * @see #getScope
     */
    String SCOPE_KEY = "scope";

    /**
     * Returns the source CRS. The source CRS is mandatory for {@linkplain Transformation
     * transformations} only. {@linkplain Conversion Conversions} may have a source CRS that is not
     * specified here, but through {@link
     * org.geotools.api.referencing.crs.GeneralDerivedCRS#getBaseCRS} instead.
     *
     * @return The source CRS, or {@code null} if not available.
     * @see Conversion#getSourceCRS
     * @see Transformation#getSourceCRS
     */
    CoordinateReferenceSystem getSourceCRS();

    /**
     * Returns the target CRS. The target CRS is mandatory for {@linkplain Transformation
     * transformations} only. {@linkplain Conversion Conversions} may have a target CRS that is not
     * specified here, but through {@link org.geotools.api.referencing.crs.GeneralDerivedCRS}
     * instead.
     *
     * @return The target CRS, or {@code null} if not available.
     * @see Conversion#getTargetCRS
     * @see Transformation#getTargetCRS
     */
    CoordinateReferenceSystem getTargetCRS();

    /**
     * Version of the coordinate transformation (i.e., instantiation due to the stochastic nature of
     * the parameters). Mandatory when describing a transformation, and should not be supplied for a
     * conversion.
     *
     * @return The coordinate operation version, or {@code null} in none.
     */
    String getOperationVersion();

    /**
     * Estimate(s) of the impact of this operation on point accuracy. Gives position error estimates
     * for target coordinates of this coordinate operation, assuming no errors in source
     * coordinates.
     *
     * @return The position error estimates, or an empty collection if not available.
     * @since GeoAPI 2.1
     */
    @UML(
            identifier = "coordinateOperationAccuracy",
            obligation = OPTIONAL,
            specification = ISO_19111)
    Collection<PositionalAccuracy> getCoordinateOperationAccuracy();

    /**
     * Area or region or timeframe in which this coordinate operation is valid.
     *
     * @return The coordinate operation valid domain, or {@code null} if not available.
     * @since GeoAPI 2.1
     */
    Extent getDomainOfValidity();

    /**
     * Description of domain of usage, or limitations of usage, for which this operation is valid.
     *
     * @return A description of domain of usage, or {@code null} if none.
     */
    InternationalString getScope();

    /**
     * Gets the math transform. The math transform will transform positions in the {@linkplain
     * #getSourceCRS source coordinate reference system} into positions in the {@linkplain
     * #getTargetCRS target coordinate reference system}. It may be {@code null} in the case of
     * {@linkplain CoordinateOperationFactory#createDefiningConversion defining conversions}.
     *
     * @return The transform from source to target CRS, or {@code null} if not applicable.
     */
    MathTransform getMathTransform();
}
