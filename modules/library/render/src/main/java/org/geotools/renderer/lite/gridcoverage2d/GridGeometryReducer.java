/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

import java.awt.Rectangle;
import java.util.logging.Logger;
import org.geotools.api.coverage.Coverage;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.logging.Logging;

/**
 * Helper class used to reduce a {@link Coverage} {@link GridGeometry2D} to be completely inside a given valid area,
 * assuming the coverage is already cut to it, but might have portions of pixel going beyond the valid area limits
 *
 * @author Andrea Aime - GeoSolutions
 */
class GridGeometryReducer {
    static final Logger LOGGER = Logging.getLogger(GridGeometryReducer.class);

    /**
     * The side of the valid area we are inspecting, or the side of the grid geometry range that we are reducing (if the
     * grid to world does not contain rotation, they are the same, otherwise not)
     *
     * @author Andrea Aime - GeoSolutions
     */
    static enum Side {
        TOP(0),
        RIGHT(1),
        BOTTOM(2),
        LEFT(3);

        private int value;

        Side(int value) {
            this.value = value;
        }

        public Side next() {
            int next = (value + 1) % 4;
            return values()[next];
        }
    }

    ReferencedEnvelope validArea;

    /** Builds a reduce with a given valid area */
    public GridGeometryReducer(ReferencedEnvelope validArea) {
        super();
        this.validArea = validArea;
    }

    /** Reduces the given grid geometry by at most one pixel on each side, in an attempt to make it fit the */
    public GridGeometry2D reduce(GridGeometry2D gg) {
        if (gg.getEnvelope().getMaximum(1) > validArea.getMaximum(1)) {
            gg = reduceGridGeometrySide(gg, Side.TOP);
        }
        if (gg.getEnvelope().getMaximum(0) > validArea.getMaximum(0)) {
            gg = reduceGridGeometrySide(gg, Side.RIGHT);
        }
        if (gg.getEnvelope().getMinimum(1) < validArea.getMinimum(1)) {
            gg = reduceGridGeometrySide(gg, Side.BOTTOM);
        }
        if (gg.getEnvelope().getMinimum(0) < validArea.getMinimum(0)) {
            gg = reduceGridGeometrySide(gg, Side.LEFT);
        }

        return gg;
    }

    private GridGeometry2D reduceGridGeometrySide(GridGeometry2D gg, Side side) {
        Side curr = side;

        for (int i = 0; i <= 4; i++) {
            for (int step = 1; step < 10; step++) {
                GridEnvelope2D gridRange = gg.getGridRange2D();
                Rectangle bounds = gridRange.getBounds();
                GridEnvelope2D reducedRange = new GridEnvelope2D(gridRange);
                switch (curr) {
                    case TOP:
                        reducedRange.setBounds(bounds.x, bounds.y + step, bounds.width, bounds.height - step);
                        break;
                    case RIGHT:
                        reducedRange.setBounds(bounds.x, bounds.y, bounds.width - step, bounds.height);
                        break;
                    case BOTTOM:
                        reducedRange.setBounds(bounds.x, bounds.y, bounds.width, bounds.height - step);
                        break;
                    case LEFT:
                        reducedRange.setBounds(bounds.x + step, bounds.y, bounds.width - step, bounds.height);
                        break;
                    default:
                        throw new RuntimeException("Unexpected side " + side);
                }

                GridGeometry2D reducedGeometry =
                        new GridGeometry2D(reducedRange, gg.getGridToCRS(), gg.getCoordinateReferenceSystem());

                // see if we actually reduced the grid geometry on the side we needed it to
                Bounds reducedEnvelope = reducedGeometry.getEnvelope();
                if (!reducedGeometry.getGridRange2D().isEmpty()) {
                    switch (side) {
                        case TOP:
                            if (reducedEnvelope.getMaximum(1) <= validArea.getMaximum(1)) {
                                return reducedGeometry;
                            }
                            break;
                        case RIGHT:
                            if (reducedEnvelope.getMaximum(0) <= validArea.getMaximum(0)) {
                                return reducedGeometry;
                            }
                            break;
                        case BOTTOM:
                            if (reducedEnvelope.getMinimum(1) >= validArea.getMinimum(1)) {
                                return reducedGeometry;
                            }
                            break;
                        case LEFT:
                            if (reducedEnvelope.getMinimum(0) >= validArea.getMinimum(0)) {
                                return reducedGeometry;
                            }
                            break;
                        default:
                            throw new RuntimeException("Unexpected side " + side);
                    }
                }

                // we did not... oh well, the grid to world might contain a rotation, try the other
                // sides
                curr = curr.next();
            }
        }

        // if we got here, we could not perform the reduction, might not be fatal, so let's just log
        LOGGER.warning("Could not reduce the grid geometry inside the valid area bounds: "
                + validArea
                + "\nGrid geometry is"
                + gg);
        return gg;
    }

    /**
     * Builds a cut envelope for the Crop operation. Since the crop internals will add back the pixels we just removed
     * due to numerical issues, we keep the envelope a bit on the safe side
     */
    public GeneralBounds getCutEnvelope(GridGeometry2D reduced) {
        GeneralBounds result;
        MathTransform2D mt = reduced.getGridToCRS2D();
        if (mt instanceof AffineTransform2D) {
            AffineTransform2D at = (AffineTransform2D) mt;
            double scaleX = Math.abs(at.getScaleX());
            double scaleY = Math.abs(at.getScaleY());
            double step = (scaleX + scaleY) / 2. / 10.;

            ReferencedEnvelope envelope = reduced.getEnvelope2D();
            result = new GeneralBounds(envelope.getCoordinateReferenceSystem());
            result.setEnvelope(
                    envelope.getMinX() + step,
                    envelope.getMinY() + step,
                    envelope.getMaxX() - step,
                    envelope.getMaxY() - step);
        } else {
            // general transform, keep it as is
            result = new GeneralBounds(reduced.getEnvelope());
        }

        return result;
    }
}
