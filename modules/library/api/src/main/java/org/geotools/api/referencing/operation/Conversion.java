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

import org.geotools.api.referencing.crs.CoordinateReferenceSystem;

/**
 * An operation on coordinates that does not include any change of Datum. The best-known example of
 * a coordinate conversion is a map projection. The parameters describing coordinate conversions are
 * defined rather than empirically derived.
 *
 * <p>Note that some conversions have no parameters.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see Transformation
 */
public interface Conversion extends Operation {
    /**
     * Returns the source CRS. Conversions may have a source CRS that is not specified here, but
     * through {@link org.geotools.api.referencing.crs.GeneralDerivedCRS#getBaseCRS} instead.
     *
     * @return The source CRS, or {@code null} if not available.
     */
    @Override
    CoordinateReferenceSystem getSourceCRS();

    /**
     * Returns the target CRS. {@linkplain Conversion Conversions} may have a target CRS that is not
     * specified here, but through {@link org.geotools.api.referencing.crs.GeneralDerivedCRS}
     * instead.
     *
     * @return The target CRS, or {@code null} if not available.
     */
    @Override
    CoordinateReferenceSystem getTargetCRS();

    /**
     * This attribute is declared in {@link CoordinateOperation} but is not used in a conversion.
     *
     * @return Always {@code null}.
     */
    @Override
    String getOperationVersion();
}
