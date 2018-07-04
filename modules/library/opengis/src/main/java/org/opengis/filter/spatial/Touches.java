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
 * Concrete {@linkplain BinarySpatialOperator binary spatial operator} that evaluates to {@code
 * true} if the feature's geometry touches, but does not overlap with the geometry held by this
 * object.
 *
 * @source $URL$
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("Touches")
public interface Touches extends BinarySpatialOperator, BoundedSpatialOperator {
    /** Operator name used to check FilterCapabilities */
    public static String NAME = "Touches";
}
