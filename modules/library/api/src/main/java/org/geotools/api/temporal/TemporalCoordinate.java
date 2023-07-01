/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.temporal;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19108;

import org.geotools.api.annotation.UML;

/**
 * A data type that shall be used for identifying temporal position within a temporal coordinate
 * system.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
@UML(identifier = "TM_Coordinate", specification = ISO_19108)
public interface TemporalCoordinate extends TemporalPosition {
    /**
     * Returns the distance from the scale origin expressed as a multiple of the standard interval
     * associated with the temporal coordinate system.
     *
     * @todo Should we return a primitive type?
     */
    @UML(identifier = "CoordinateValue", obligation = MANDATORY, specification = ISO_19108)
    Number getCoordinateValue();
}
