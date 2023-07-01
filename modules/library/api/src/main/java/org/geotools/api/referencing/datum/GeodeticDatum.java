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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19111;

import org.geotools.api.annotation.UML;

/**
 * Defines the location and precise orientation in 3-dimensional space of a defined ellipsoid (or
 * sphere) that approximates the shape of the earth. Used also for Cartesian coordinate system
 * centered in this ellipsoid (or sphere).
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see Ellipsoid
 * @see PrimeMeridian
 */
@UML(identifier = "CD_GeodeticDatum", specification = ISO_19111)
public interface GeodeticDatum extends Datum {
    /**
     * Returns the ellipsoid.
     *
     * @return The ellipsoid.
     */
    @UML(identifier = "usesEllipsoid", obligation = MANDATORY, specification = ISO_19111)
    Ellipsoid getEllipsoid();

    /**
     * Returns the prime meridian.
     *
     * @return The prime meridian.
     */
    @UML(identifier = "usesPrimeMeridian", obligation = MANDATORY, specification = ISO_19111)
    PrimeMeridian getPrimeMeridian();
}
