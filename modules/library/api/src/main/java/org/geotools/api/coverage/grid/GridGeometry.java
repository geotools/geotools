/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.coverage.grid;

import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.MathTransform;

/**
 * Describes the geometry and georeferencing information of the grid coverage. The grid range attribute determines the
 * valid grid coordinates and allows for calculation of grid size. A grid coverage may or may not have georeferencing.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-004.pdf">Grid Coverage specification 1.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
public interface GridGeometry {
    /**
     * The valid coordinate range of a grid coverage. The lowest valid grid coordinate is often (but not always) zero. A
     * grid with 512 cells typically have a minimum coordinate of 0 and maximum of 512, with 511 as the highest valid
     * index.
     *
     * @return The valid coordinate range of a grid coverage.
     */
    GridEnvelope getGridRange();

    /**
     * Returns the conversion from grid coordinates to real world earth coordinates. The transform is often an affine
     * transform. The coordinate reference system of the real world coordinates is given by the
     * {@link org.geotools.api.coverage.Coverage#getCoordinateReferenceSystem} method and maps to
     * {@linkplain PixelInCell#CELL_CENTER pixel center}.
     *
     * @return The conversion from grid coordinates to
     *     {@linkplain org.geotools.api.coverage.Coverage#getCoordinateReferenceSystem real world earth coordinates}.
     * @since GeoAPI 2.1
     */
    MathTransform getGridToCRS();
}
