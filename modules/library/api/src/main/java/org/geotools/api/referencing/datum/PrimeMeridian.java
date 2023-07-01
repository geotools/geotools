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

import static org.geotools.api.annotation.Obligation.CONDITIONAL;
import static org.geotools.api.annotation.Specification.ISO_19111;
import static org.geotools.api.annotation.Specification.OGC_01009;

import javax.measure.Unit;
import javax.measure.quantity.Angle;
import org.geotools.api.annotation.UML;
import org.geotools.api.referencing.IdentifiedObject;

/**
 * A prime meridian defines the origin from which longitude values are determined. The {@link
 * #getName name} initial value is "Greenwich", and that value shall be used when the {@linkplain
 * #getGreenwichLongitude greenwich longitude} value is zero.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract
 *     specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "CD_PrimeMeridian", specification = ISO_19111)
public interface PrimeMeridian extends IdentifiedObject {
    /**
     * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward. The
     * {@code greenwichLongitude} initial value is zero, and that value shall be used when the
     * {@linkplain #getName meridian name} value is "Greenwich".
     *
     * @return The prime meridian Greenwich longitude, in {@linkplain #getAngularUnit angular unit}.
     */
    @UML(identifier = "greenwichLongitude", obligation = CONDITIONAL, specification = ISO_19111)
    double getGreenwichLongitude();

    /**
     * Returns the angular unit of the {@linkplain #getGreenwichLongitude Greenwich longitude}.
     *
     * @return The angular unit of greenwich longitude.
     */
    @UML(identifier = "getAngularUnit", specification = OGC_01009)
    Unit<Angle> getAngularUnit();
}
