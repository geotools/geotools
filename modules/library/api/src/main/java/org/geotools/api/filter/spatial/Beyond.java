/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.filter.spatial;

// Annotations

/**
 * Concrete {@linkplain DistanceBufferOperator distance buffer operator} that evaluates as {@code
 * true} when all of a feature's geometry lies beyond (i.e. is more distant) than the given distance
 * from this object's geometry.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
public interface Beyond extends DistanceBufferOperator {
    /** Operator name used to check FilterCapabilities */
    public static String NAME = "Beyond";
}
