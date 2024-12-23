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

import org.geotools.api.referencing.operation.Conversion;

/**
 * A coordinate reference system that is defined by its coordinate {@linkplain Conversion conversion} from another
 * coordinate reference system (not by a {@linkplain org.geotools.api.referencing.datum.Datum datum}).
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface GeneralDerivedCRS extends SingleCRS {
    /**
     * Returns the base coordinate reference system.
     *
     * @return The base coordinate reference system.
     */
    CoordinateReferenceSystem getBaseCRS();

    /**
     * Returns the conversion from the {@linkplain #getBaseCRS base CRS} to this CRS.
     *
     * @return The conversion from the base CRS.
     * @rename {@code definedByConversion} may be a precise description of the association, but may be confusing as a
     *     method name since it doesn't said which CRS is the source or which one is the target. OGC document 01-009
     *     used {@code toBase()} method name. By analogy with 01-009, this new interface specifies a method name which
     *     contains the {@code FromBase} words.
     */
    Conversion getConversionFromBase();
}
