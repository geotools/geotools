/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.referencing.datum;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Defines the location and precise orientation in 3-dimensional space of a defined ellipsoid
 * (or sphere) that approximates the shape of the earth. Used also for Cartesian coordinate
 * system centered in this ellipsoid (or sphere).
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/referencing/datum/GeodeticDatum.java $
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 1.0
 *
 * @see Ellipsoid
 * @see PrimeMeridian
 */
@UML(identifier="CD_GeodeticDatum", specification=ISO_19111)
public interface GeodeticDatum extends Datum {
    /**
     * Returns the ellipsoid.
     *
     * @return The ellipsoid.
     */
    @UML(identifier="usesEllipsoid", obligation=MANDATORY, specification=ISO_19111)
    Ellipsoid getEllipsoid();

    /**
     * Returns the prime meridian.
     *
     * @return The prime meridian.
     */
    @UML(identifier="usesPrimeMeridian", obligation=MANDATORY, specification=ISO_19111)
    PrimeMeridian getPrimeMeridian();
}
