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

/**
 * A data type that shall be used for identifying temporal position within a temporal coordinate system.
 *
 * @author Stephane Fellah (Image Matters)
 * @author Alexander Petkov
 */
public interface TemporalCoordinate extends TemporalPosition {
    /**
     * Returns the distance from the scale origin expressed as a multiple of the standard interval associated with the
     * temporal coordinate system.
     *
     * @todo Should we return a primitive type?
     */
    Number getCoordinateValue();
}
