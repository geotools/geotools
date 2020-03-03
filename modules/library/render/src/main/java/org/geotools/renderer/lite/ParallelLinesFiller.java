/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.style.LineStyle2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * Support class that fills a given rectangle with a set of parallel lines derived from a sample
 * mark that would generate sets of parallel lines when tiled
 *
 * @author Andrea Aime
 */
public class ParallelLinesFiller {

    /**
     * The four types of orientation that can give birth to a parallel line set from a given mark
     */
    enum LineType {
        HORIZONTAL,
        VERTICAL,
        SLASH,
        BACKSLASH
    };

    static class Line {
        LineType type;
        double initialOffset;

        public Line(LineType type, double initialOffset) {
            this.type = type;
            this.initialOffset = initialOffset;
        }
    }

    /** The set of parallel line definitions to paint */
    List<Line> lines;
    /** The repetition pattern */
    double xStep;

    double yStep;

    public ParallelLinesFiller(List<Line> lines, double xStep, double yStep) {
        this.lines = lines;
        this.xStep = xStep;
        this.yStep = yStep;
    }

    /** Builds a line filler from a stipple shape, if at all possible, or returns null otherwise */
    public static ParallelLinesFiller fromStipple(Shape shape) {
        Rectangle2D bounds = shape.getBounds2D();

        // turn to a geometry, it's easier to visit, and see if we can extract only
        // line segments
        Geometry geometry = JTS.toGeometry(shape);
        LinesExtractor extractor = new LinesExtractor();
        geometry.apply(extractor);

        // if not only straight line segments included, we cannot turn this into a filler
        List<Line2D> segments = extractor.getLines();
        if (!extractor.isSimple() || segments.isEmpty()) {
            return null;
        }

        // check each line and see if it forms a repeating pattern
        double by1 = bounds.getMinY();
        double by2 = bounds.getMaxY();
        double bx1 = bounds.getMinX();
        double bx2 = bounds.getMaxX();
        if (equals(by1, by2)) {
            double w = bx2 - bx1;
            by1 -= w / 2;
            by2 += w / 2;
        } else if (equals(bx1, bx2)) {
            double h = by2 - by1;
            bx1 -= h / 2;
            bx2 += h / 2;
        }
        List<Line> lines = new ArrayList<>();
        for (Line2D segment : segments) {
            // vertical line?
            final double x1 = segment.getX1();
            final double x2 = segment.getX2();
            final double y1 = segment.getY1();
            final double y2 = segment.getY2();

            if (equals(x1, x2)) {
                if (equals(y1, by1) && equals(y2, by2)) {
                    lines.add(new Line(LineType.VERTICAL, x1 - bx1));
                } else {
                    // not a line crossing the bounds, does not form a repeating pattern
                    return null;
                }
            } else {
                if (equals(y1, y2)) {
                    if (equals(x1, bx1) && equals(x2, bx2)) {
                        lines.add(new Line(LineType.HORIZONTAL, y1 - by1));
                    } else {
                        // not a line crossing the bounds, does not form a repeating pattern
                        return null;
                    }
                } else if ((equals(x1, bx1)
                                && equals(y1, by1)
                                && equals(x2, bx2)
                                && equals(y2, by2))
                        || (equals(x1, bx2) && equals(y1, by2))
                                && equals(x2, bx1)
                                && equals(y2, by1)) {
                    lines.add(new Line(LineType.SLASH, 0));
                } else if ((equals(x1, bx1)
                                && equals(y1, by2)
                                && equals(x2, bx2)
                                && equals(y2, by1))
                        || (equals(x1, bx2) && equals(y1, by1))
                                && equals(x2, bx1)
                                && equals(y2, by2)) {
                    lines.add(new Line(LineType.BACKSLASH, 0));
                } else {
                    // not a line crossing the bounds and forming a repeating pattern
                    return null;
                }
            }
        }

        double stepX = bx2 - bx1;
        double stepY = by2 - by1;
        if (stepX > 0 && stepY > 0) {
            return new ParallelLinesFiller(lines, stepX, stepY);
        } else {
            return null;
        }
    }

    /** Checks if two doubles are equal with a small tolerance */
    private static boolean equals(double d1, double d2) {
        return Math.abs(d1 - d2) < 1e-3;
    }

    /** Fills the specified rectangle with parallel lines */
    public void fillRectangle(
            Rectangle2D bounds, StyledShapePainter painter, Graphics2D graphics, LineStyle2D ls2d) {
        // the shape painter works only with liteshape, prepare objects so that we don't end up
        // re-creating them
        // over and over
        AffineTransform2D identityTransf = new AffineTransform2D(new AffineTransform());
        Decimator nullDecimator = new Decimator(-1, -1);
        GeometryFactory geomFactory = new GeometryFactory();
        Coordinate stippleCoord1 = new Coordinate(0, 0);
        Coordinate stippleCoord2 = new Coordinate(0, 0);
        LineString stippleLine =
                geomFactory.createLineString(new Coordinate[] {stippleCoord1, stippleCoord2});

        for (Line line : lines) {
            if (line.type == LineType.HORIZONTAL) {
                stippleCoord1.x = bounds.getMinX();
                stippleCoord2.x = bounds.getMaxX();
                for (double y = bounds.getMinY() + line.initialOffset;
                        y < bounds.getMaxY();
                        y += yStep) {
                    stippleCoord1.y = stippleCoord2.y = y;
                    paintLine(painter, graphics, ls2d, identityTransf, nullDecimator, stippleLine);
                }
            } else if (line.type == LineType.VERTICAL) {
                stippleCoord1.y = bounds.getMinY();
                stippleCoord2.y = bounds.getMaxY();
                for (double x = bounds.getMinX() + line.initialOffset;
                        x < bounds.getMaxX();
                        x += xStep) {
                    stippleCoord1.x = stippleCoord2.x = x;
                    paintLine(painter, graphics, ls2d, identityTransf, nullDecimator, stippleLine);
                }
            } else if (line.type == LineType.BACKSLASH) {
                stippleCoord1.x = bounds.getMinX();
                stippleCoord1.y = bounds.getMinY();
                for (double x = bounds.getMinX(); x <= bounds.getMaxX(); x += xStep) {
                    int xSteps = (int) Math.ceil((bounds.getMaxX() - x) / xStep);
                    int ySteps = (int) Math.ceil((bounds.getMaxY() - bounds.getMinY()) / yStep);
                    int steps = Math.min(xSteps, ySteps);
                    stippleCoord1.x = x;
                    stippleCoord2.x = stippleCoord1.x + steps * xStep;
                    stippleCoord2.y = stippleCoord1.y + steps * yStep;
                    paintLine(painter, graphics, ls2d, identityTransf, nullDecimator, stippleLine);
                }
                stippleCoord1.x = bounds.getMinX();
                for (double y = bounds.getMinY() + yStep; y <= bounds.getMaxY(); y += yStep) {
                    int xSteps = (int) Math.ceil((bounds.getMaxX() - bounds.getMinX()) / xStep);
                    int ySteps = (int) Math.ceil((bounds.getMaxY() - y) / yStep);
                    int steps = Math.min(xSteps, ySteps);
                    stippleCoord1.y = y;
                    stippleCoord2.x = stippleCoord1.x + steps * xStep;
                    stippleCoord2.y = stippleCoord1.y + steps * yStep;
                    paintLine(painter, graphics, ls2d, identityTransf, nullDecimator, stippleLine);
                }
            } else if (line.type == LineType.SLASH) {
                stippleCoord1.x = bounds.getMinX();
                stippleCoord1.y = bounds.getMinY();
                for (double x = bounds.getMinX() + xStep; x <= bounds.getMaxX(); x += xStep) {
                    int xSteps = (int) Math.ceil((x - bounds.getMinX()) / xStep);
                    int ySteps = (int) Math.ceil((bounds.getMaxY() - bounds.getMinY()) / yStep);
                    int steps = Math.min(xSteps, ySteps);
                    stippleCoord1.x = x;
                    stippleCoord2.x = stippleCoord1.x - steps * xStep;
                    stippleCoord2.y = stippleCoord1.y + steps * yStep;
                    paintLine(painter, graphics, ls2d, identityTransf, nullDecimator, stippleLine);
                }
                stippleCoord1.x += xStep;
                for (double y = bounds.getMinY(); y <= bounds.getMaxY(); y += yStep) {
                    int xSteps = (int) Math.ceil((stippleCoord1.x - bounds.getMinX()) / xStep);
                    int ySteps = (int) Math.ceil((bounds.getMaxY() - y) / yStep);
                    int steps = Math.min(xSteps, ySteps);
                    stippleCoord1.y = y;
                    stippleCoord2.x = stippleCoord1.x - steps * xStep;
                    stippleCoord2.y = stippleCoord1.y + steps * yStep;
                    paintLine(painter, graphics, ls2d, identityTransf, nullDecimator, stippleLine);
                }
            }
        }
    }

    private void paintLine(
            StyledShapePainter painter,
            Graphics2D graphics,
            LineStyle2D lineStyle,
            AffineTransform2D identityTransf,
            Decimator nullDecimator,
            LineString stippleLine) {
        stippleLine.geometryChanged();
        LiteShape2 stippleShape;
        try {
            stippleShape = new LiteShape2(stippleLine, identityTransf, nullDecimator, false);
        } catch (Exception e) {
            throw new RuntimeException("Unxpected exception building lite shape", e);
        }
        painter.paintLineStyle(graphics, stippleShape, lineStyle, false, 0);
    }
}
