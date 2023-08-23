/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.extent;

import java.util.Collection;
import org.locationtech.jts.geom.Polygon;

/**
 * Boundary enclosing the dataset, expressed as the closed set of (<var>x</var>,<var>y</var>)
 * coordinates of the polygon. The last point replicates first point.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface BoundingPolygon extends GeographicExtent {
    /**
     * Returns the sets of points defining the bounding polygon.
     *
     * @return The sets of points defining the bounding polygon.
     */
    Collection<Polygon> getPolygons();
}
