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

import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.datum.Datum;

/**
 * Abstract coordinate reference system, consisting of a single {@linkplain CoordinateSystem
 * Coordinate System} and a single {@linkplain Datum Datum} (as opposed to {@linkplain CompoundCRS
 * Compound CRS}).
 *
 * <p>A coordinate reference system consists of an ordered sequence of coordinate system axes that
 * are related to the earth through a datum. A coordinate reference system is defined by one datum
 * and by one coordinate system. Most coordinate reference system do not move relative to the earth,
 * except for engineering coordinate reference systems defined on moving platforms such as cars,
 * ships, aircraft, and spacecraft.
 *
 * <p>Coordinate reference systems are commonly divided into sub-types. The common classification
 * criterion for sub-typing of coordinate reference systems is the way in which they deal with earth
 * curvature. This has a direct effect on the portion of the earth's surface that can be covered by
 * that type of CRS with an acceptable degree of error. The exception to the rule is the subtype
 * "Temporal" which has been added by analogy.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 * @see org.geotools.api.referencing.cs.CoordinateSystem
 * @see org.geotools.api.referencing.datum.Datum
 */
public interface SingleCRS extends CoordinateReferenceSystem {
    /**
     * Returns the coordinate system.
     *
     * @rename Expanded the "CS" abbreviation into "CoordinateSystem".
     */
    @Override
    CoordinateSystem getCoordinateSystem();

    /**
     * Returns the datum.
     *
     * @return The datum.
     */
    Datum getDatum();
}
