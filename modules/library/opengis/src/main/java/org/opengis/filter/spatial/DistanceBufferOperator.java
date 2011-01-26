/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.filter.spatial;

// Annotations
import org.opengis.annotation.XmlElement;


/**
 * Abstract superclass for spatial operators that check that one shape satisfies
 * some relation to a buffer around another shape.  This could be used to find,
 * for example, all the geometries that come within 10 meters of a river.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("DistanceBufferType")
public interface DistanceBufferOperator extends BinarySpatialOperator {
    /**
     * Returns the buffer distance around the geometry that will be used when
     * comparing features' geometries.
     */
    @XmlElement("Distance")
    double getDistance();

    /**
     * Gets the units of measure that can be used to interpret the distance
     * value held by this object.  An implementation may throw an exception if
     * these units differ from the units of the coordinate system of its
     * geometry or the feature's geometry.
     */
    String getDistanceUnits();
}
