/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.spatial;

import static org.geotools.api.annotation.Obligation.OPTIONAL;
import static org.geotools.api.annotation.Specification.ISO_19115;

import java.util.Collection;
import java.util.List;
import org.geotools.api.annotation.UML;
import org.geotools.api.util.InternationalString;
import org.locationtech.jts.geom.Point;

/**
 * Grid whose cells are regularly spaced in a geographic (i.e., lat / long) or map coordinate system
 * defined in the Spatial Referencing System (SRS) so that any cell in the grid can be geolocated
 * given its grid coordinate and the grid origin, cell spacing, and orientation indication of
 * whether or not geographic.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Georectified extends GridSpatialRepresentation {
    /**
     * Indication of whether or not geographic position points are available to test the accuracy of
     * the georeferenced grid data.
     *
     * @return Whether or not geographic position points are available to test accuracy.
     */
    boolean isCheckPointAvailable();

    /**
     * Description of geographic position points used to test the accuracy of the georeferenced grid
     * data.
     *
     * @return Description of geographic position points used to test accuracy, or {@code null}.
     */
    InternationalString getCheckPointDescription();

    /**
     * Earth location in the coordinate system defined by the Spatial Reference System and the grid
     * coordinate of the cells at opposite ends of grid coverage along two diagonals in the grid
     * spatial dimensions. There are four corner points in a georectified grid; at least two corner
     * points along one diagonal are required.
     *
     * @return The corner points.
     */
    List<? extends Point> getCornerPoints();

    /**
     * Earth location in the coordinate system defined by the Spatial Reference System and the grid
     * coordinate of the cell halfway between opposite ends of the grid in the spatial dimensions.
     *
     * @return The center point, or {@code null}.
     */
    Point getCenterPoint();

    /**
     * Point in a pixel corresponding to the Earth location of the pixel.
     *
     * @return Earth location of the pixel.
     */
    PixelOrientation getPointInPixel();

    /**
     * Description of the information about which grid dimensions are the spatial dimensions.
     *
     * @return Description of the information about grid dimensions, or {@code null}.
     */
    @UML(
            identifier = "transformationDimensionDescription",
            obligation = OPTIONAL,
            specification = ISO_19115)
    InternationalString getTransformationDimensionDescription();

    /**
     * Information about which grid dimensions are the spatial dimensions.
     *
     * @return Information about which grid dimensions are the spatial dimensions, or {@code null}.
     */
    @UML(
            identifier = "transformationDimensionMapping",
            obligation = OPTIONAL,
            specification = ISO_19115)
    Collection<? extends InternationalString> getTransformationDimensionMapping();
}
