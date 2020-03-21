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
import org.opengis.geometry.BoundingBox;

/**
 * {@linkplain SpatialOperator Spatial operator} that evaluates to {@code true} when the bounding
 * box of the feature's geometry overlaps the bounding box provided in this object's properties. An
 * implementation may choose to throw an exception if one attempts to test features that are in a
 * different SRS than the SRS contained here.
 *
 * @version <A HREF="http://www.opengis.org/docs/02-059.pdf">Implementation specification 1.0</A>
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.0
 */
@XmlElement("BBOX")
public interface BBOX extends BinarySpatialOperator {
    /** Operator name used to check FilterCapabilities */
    String NAME = "BBOX";

    /** Return Bounding Box object representing the bounds of the filter @Return Bounds of Filter */
    BoundingBox getBounds();
}
