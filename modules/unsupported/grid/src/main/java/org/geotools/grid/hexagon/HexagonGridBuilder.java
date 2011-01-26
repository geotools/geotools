/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.hexagon;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.AbstractGridBuilder;
import org.geotools.grid.GridElement;
import org.geotools.grid.Neighbor;
import org.geotools.grid.Orientation;

/**
 * Used by {@code Hexagons} class to build grids.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/grid/src/main/java/org/geotools/grid/hexagon/Hexagon.java $
 * @version $Id: Hexagon.java 35637 2010-06-01 09:24:43Z mbedward $
 */
class HexagonGridBuilder extends AbstractGridBuilder {
    private final double sideLen;
    private final Orientation orientation;
    private final Neighbor[] nextX = new Neighbor[2];
    private final Neighbor[] nextY = new Neighbor[2];
    private int xIndex = 0;
    private int yIndex = 0;

    public HexagonGridBuilder(ReferencedEnvelope gridBounds, double sideLen, Orientation orientation) {
        super(gridBounds);
        this.sideLen = sideLen;
        this.orientation = orientation;

        if (orientation == Orientation.ANGLED) {
            nextX[0] = nextX[1] = Neighbor.RIGHT;
            nextY[0] = Neighbor.UPPER_RIGHT;
            nextY[1] = Neighbor.UPPER_LEFT;

        } else {  // FLAT
            nextX[0] = Neighbor.LOWER_RIGHT;
            nextX[1] = Neighbor.UPPER_RIGHT;
            nextY[0] = nextY[1] = Neighbor.UPPER;
        }
    }

    /**
     * Tests whether a neighbor position is valid for a given orientation.
     * Since the {@code Hexagon} class is intended to work within a grid
     * (ie. a perfect tesselation) some combinations of neighbour position
     * and hexagon orientation are invalid. For example, a {@code FLAT}
     * hexagon does not have a {@code LEFT}, rather it has {@code UPPER_LEFT}
     * and {@code LOWER_LEFT}.
     *
     * @param orientation hexagon orientation
     *
     * @param neighbor neighbor position
     *
     * @return {@code true} if the combination is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidNeighbor(Neighbor neighbor) {
        switch (neighbor) {
            case LEFT:
            case RIGHT:
                return orientation == Orientation.ANGLED;

            case LOWER:
            case UPPER:
                return orientation == Orientation.FLAT;

            case LOWER_LEFT:
            case LOWER_RIGHT:
            case UPPER_LEFT:
            case UPPER_RIGHT:
                return true;

            default:
                throw new IllegalArgumentException("Invalid value for neighbor");
        }
    }

    /**
     * Creates a new {@code Hexagon} positioned at the given neighbor position
     * relative to the reference element.
     *
     * @param el the reference hexagon
     *
     * @param neighbor a valid neighbour position given the reference hexagon's
     *        orientation
     *
     * @return a new {@code Hexagon} object
     *
     * @throws IllegalArgumentException if either argument is {@code null} or
     *         if {@code el} is not an instance of {@code Hexagon} or
     *         if the neighbor position is not valid for the reference hexagon's
     *         orientation
     *
     * @see #isValidNeighbor(Hexagon.Orientation, Hexagon.Neighbor)
     */
    @Override
    public Hexagon createNeighbor(GridElement el, Neighbor neighbor) {
        if (el == null || neighbor == null) {
            throw new IllegalArgumentException(
                    "el and neighbour position must both be non-null");
        }

        if (!(el instanceof Hexagon)) {
            throw new IllegalArgumentException("el must be an instance of Hexagon");
        }

        Hexagon hexagon = (Hexagon) el;

        if (!isValidNeighbor(neighbor)) {
            throw new IllegalArgumentException(
                    neighbor + " is not a valid neighbour position for orientation " +
                    hexagon.getOrientation());
        }

        ReferencedEnvelope bounds = hexagon.getBounds();
        double dx, dy;

        switch (neighbor) {
            case LEFT:
                dx = -bounds.getWidth();
                dy = 0.0;
                break;

            case LOWER:
                dx = 0.0;
                dy = -bounds.getHeight();
                break;

            case LOWER_LEFT:
                if (hexagon.getOrientation() == Orientation.FLAT) {
                    dx = -0.75 * bounds.getWidth();
                    dy = -0.5 * bounds.getHeight();
                } else {  // ANGLED
                    dx = -0.5 * bounds.getWidth();
                    dy = -0.75 * bounds.getHeight();
                }
                break;

            case LOWER_RIGHT:
                if (hexagon.getOrientation() == Orientation.FLAT) {
                    dx = 0.75 * bounds.getWidth();
                    dy = -0.5 * bounds.getHeight();
                } else {  // ANGLED
                    dx = 0.5 * bounds.getWidth();
                    dy = -0.75 * bounds.getHeight();
                }
                break;

            case RIGHT:
                dx = bounds.getWidth();
                dy = 0.0;
                break;

            case UPPER:
                dx = 0.0;
                dy = bounds.getHeight();
                break;

            case UPPER_LEFT:
                if (hexagon.getOrientation() == Orientation.FLAT) {
                    dx = -0.75 * bounds.getWidth();
                    dy = 0.5 * bounds.getHeight();
                } else {  // ANGLED
                    dx = -0.5 * bounds.getWidth();
                    dy = 0.75 * bounds.getHeight();
                }
                break;

            case UPPER_RIGHT:
                if (hexagon.getOrientation() == Orientation.FLAT) {
                    dx = 0.75 * bounds.getWidth();
                    dy = 0.5 * bounds.getHeight();
                } else {  // ANGLED
                    dx = 0.5 * bounds.getWidth();
                    dy = 0.75 * bounds.getHeight();
                }
                break;

            default:
                throw new IllegalArgumentException("Unrecognized value for neighbor");
        }

        return Hexagons.create(bounds.getMinX() + dx, bounds.getMinY() + dy,
                hexagon.getSideLength(), hexagon.getOrientation(),
                bounds.getCoordinateReferenceSystem());
    }

    @Override
    public GridElement getFirstElement() {
        return Hexagons.create(
                gridBounds.getMinX(), gridBounds.getMinY(),
                sideLen, orientation, gridBounds.getCoordinateReferenceSystem());
    }

    @Override
    public GridElement getNextXElement(GridElement el) {
        Hexagon h = createNeighbor(el, nextX[xIndex]);
        xIndex = (xIndex + 1) % 2;
        return h;
    }

    @Override
    public GridElement getNextYElement(GridElement el) {
        Hexagon h = createNeighbor(el, nextY[yIndex]);
        yIndex = (yIndex + 1) % 2;
        xIndex = 0;
        return h;
    }

    @Override
    public boolean isValidDenseVertexSpacing(double v) {
        return v > 0 && v < sideLen / 2.0;
    }


}
