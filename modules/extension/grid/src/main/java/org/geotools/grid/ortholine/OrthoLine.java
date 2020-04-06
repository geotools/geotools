/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.grid.ortholine;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.LineElement;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * An ortho-line grid element.
 *
 * @author mbedward
 * @since 8.0
 * @version $Id$
 */
public class OrthoLine implements LineElement {
    private static final GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);

    private final LineOrientation orientation;
    private final int level;
    private final CoordinateReferenceSystem crs;
    private final Coordinate v0;
    private final Coordinate v1;

    /**
     * Creates a new ortho-line element. The line position is specified by a single ordinate which
     * will be its X-ordinate if vertical, or its Y-ordinate if horizontal.
     *
     * @param gridBounds bounds of the area containing this line
     * @param orientation line orientation
     * @param ordinate position of the line
     * @param level integer level associated with this line
     */
    public OrthoLine(
            ReferencedEnvelope gridBounds,
            LineOrientation orientation,
            double ordinate,
            int level) {

        this.crs = gridBounds.getCoordinateReferenceSystem();
        this.orientation = orientation;
        this.level = level;

        if (orientation == LineOrientation.HORIZONTAL) {
            v0 = new Coordinate(gridBounds.getMinX(), ordinate);
            v1 = new Coordinate(gridBounds.getMaxX(), ordinate);
        } else {
            v0 = new Coordinate(ordinate, gridBounds.getMinY());
            v1 = new Coordinate(ordinate, gridBounds.getMaxY());
        }
    }

    /**
     * {@inheritDoc} Note that this will be a degenerate rectangle with either 0 width, if the line
     * is vertical, or zero height, if horizontal.
     */
    public ReferencedEnvelope getBounds() {
        return new ReferencedEnvelope(v0.x, v1.x, v0.y, v1.y, crs);
    }

    /** {@inheritDoc} These will be the end-points of the line element. */
    public Coordinate[] getVertices() {
        Coordinate[] vertices = new Coordinate[2];
        vertices[0] = v0;
        vertices[1] = v1;
        return vertices;
    }

    /**
     * Gets the orientation of this line.
     *
     * @return the orientation
     */
    public LineOrientation getOrientation() {
        return orientation;
    }

    /**
     * Gets the level (precedence) associated with this line.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    public Geometry toGeometry() {
        return geomFactory.createLineString(new Coordinate[] {v0, v1});
    }

    public Geometry toDenseGeometry(double maxSpacing) {
        if (maxSpacing <= 0.0) {
            throw new IllegalArgumentException("maxSpacing must be a positive value");
        }

        return Densifier.densify(this.toGeometry(), maxSpacing);
    }
}
