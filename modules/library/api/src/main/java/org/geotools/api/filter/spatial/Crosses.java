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
 * Concrete {@linkplain BinarySpatialOperator binary spatial operator} that evaluates to {@code true} if the first
 * geometric operand crosses the second (in the sense defined by the OGC Simple Features specification).
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
public interface Crosses extends BinarySpatialOperator, BoundedSpatialOperator {
    /** Operator name used to check FilterCapabilities */
    public static String NAME = "Crosses";
}
