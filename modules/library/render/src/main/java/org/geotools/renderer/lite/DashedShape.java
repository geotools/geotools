/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * A shape wrapper that generates a stroked version of the shape without actually holding it all in memory (it is
 * streamed through the path iterator)
 *
 * @author Andrea Aime - OpenGeo
 */
public class DashedShape implements Shape {
    Shape shape;

    float[] dashArray;

    float dashPhase;

    public DashedShape(Shape shape, float[] dashArray, float dashPhase) {
        this.shape = shape;
        this.dashArray = dashArray;
        this.dashPhase = dashPhase;
    }

    @Override
    public boolean contains(double x, double y, double w, double h) {
        return shape.contains(x, y, w, h);
    }

    @Override
    public boolean contains(double x, double y) {
        return shape.contains(x, y);
    }

    @Override
    public boolean contains(Point2D p) {
        return shape.contains(p);
    }

    @Override
    public boolean contains(Rectangle2D r) {
        return shape.contains(r);
    }

    @Override
    public Rectangle getBounds() {
        return shape.getBounds();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return shape.getBounds2D();
    }

    @Override
    public boolean intersects(double x, double y, double w, double h) {
        return shape.intersects(x, y, w, h);
    }

    @Override
    public boolean intersects(Rectangle2D r) {
        return shape.intersects(r);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at) {
        // we need to work against a flattened iterator, the dashed iterator
        // cannot handle curved segments
        return new DashedIterator(shape.getPathIterator(at, 1), dashArray, dashPhase);
    }

    @Override
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        // we need to work against a flattened iterator, the dashed iterator
        // cannot handle curved segments
        if (flatness < 1) {
            flatness = 1;
        }
        return new DashedIterator(shape.getPathIterator(at, flatness), dashArray, dashPhase);
    }

    /**
     * The iterator that generates the dashed segments in a streaming fashion
     *
     * @author Andrea Aime - OpenGeo
     */
    public static class DashedIterator implements PathIterator {
        static final float EPS = 1e-3f;

        /** The original iterator */
        PathIterator delegate;

        /** The offsets at which each dash segment ends compared to the beginning of the sequence */
        float[] dashOffsets;

        /** The current dash offset */
        int dashIndex = 0;

        /** The offset from the beginning of the sequence */
        float dashOffset;

        /** The previous coordinates */
        float[] prevCoords = new float[2];

        /** The current coordinates */
        float[] currCoords = new float[2];

        /** The length of the current segment */
        float segmentLength;

        /** The current position from the beginning of the current segment */
        float segmentOffset;

        /** The segment type returned by the last call to the delegates currentSegment(...) */
        int lastType;

        /** The coordinate of the next dash segment returned */
        float[] dashedSegment = new float[2];

        /** The type of the next dash segment returned */
        int dashedType;

        /** Are we done? */
        boolean done;

        /** Delta X of curr and prev coordinates */
        private float dy;

        /** Delta Y of the curr and prev coordinates */
        private float dx;

        /** Both used to reset the dash state when doing a MOVE_TO */
        float dashPhase;

        int baseDashIndex;

        public DashedIterator(PathIterator delegate, float[] dashArray, float dashPhase) {
            this.delegate = delegate;
            dashOffsets = new float[dashArray.length];
            dashOffsets[0] = dashArray[0];
            for (int i = 1; i < dashArray.length; i++) {
                dashOffsets[i] = dashOffsets[i - 1] + dashArray[i];
            }
            // adjust the phase so that it's not longer than the dash array itself
            dashPhase = dashPhase % dashOffsets[dashOffsets.length - 1];
            // adjust the current index in the dash array
            // so that we start dashing at the requested phase
            for (int i = 0; i < dashArray.length && dashPhase > dashArray[i]; i++) {
                dashIndex++;
            }
            this.baseDashIndex = dashIndex;
            this.dashPhase = dashPhase;
            this.dashOffset = dashPhase;

            if (delegate.isDone()) {
                done = true;
            } else {
                dashedType = delegate.currentSegment(dashedSegment);
                currCoords[0] = dashedSegment[0];
                currCoords[1] = dashedSegment[1];
                delegate.next();
            }
        }

        @Override
        public int currentSegment(float[] coords) {
            coords[0] = dashedSegment[0];
            coords[1] = dashedSegment[1];
            return dashedType;
        }

        @Override
        public int currentSegment(double[] coords) {
            float[] fcoord = new float[2];
            int retval = currentSegment(fcoord);
            coords[0] = fcoord[0];
            coords[1] = fcoord[1];
            return retval;
        }

        @Override
        public int getWindingRule() {
            return delegate.getWindingRule();
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public void next() {
            // have we exhausted the previous segment?
            if (segmentLength == 0) {
                if (!delegate.isDone()) {
                    prevCoords[0] = currCoords[0];
                    prevCoords[1] = currCoords[1];
                    lastType = delegate.currentSegment(currCoords);
                    if (lastType == PathIterator.SEG_MOVETO) {
                        // start over and move to the next value
                        segmentOffset = 0;
                        dashOffset = dashPhase;
                        dashIndex = baseDashIndex;
                        dashedSegment[0] = currCoords[0];
                        dashedSegment[1] = currCoords[1];
                        dashedType = PathIterator.SEG_MOVETO;
                        dx = 0;
                        dy = 0;
                        delegate.next();
                        // if no segment after move we're done
                        done = delegate.isDone();
                    } else {
                        // prepare for the next round of dash array application
                        dx = currCoords[0] - prevCoords[0];
                        dy = currCoords[1] - prevCoords[1];
                        segmentLength = (float) sqrt(pow(dx, 2) + pow(dy, 2));
                        segmentOffset = 0;
                        delegate.next();
                    }
                } else {
                    done = true;
                }
            }
            // if not done move along the dash array
            if (!done && lastType != PathIterator.SEG_MOVETO) {
                float dashResidual = dashOffsets[dashIndex] - dashOffset;
                float segmentResidual = segmentLength - segmentOffset;
                float residual = min(dashResidual, segmentResidual);
                if (abs(segmentLength) > EPS) {
                    dashedSegment[0] = dashedSegment[0] + dx * residual / segmentLength;
                    dashedSegment[1] = dashedSegment[1] + dy * residual / segmentLength;
                }
                // if the lastType is a line to we need to decide if we're pen down or pen
                // up depending on what of the dash segments we're in
                if (lastType == PathIterator.SEG_LINETO) {
                    dashedType = dashIndex % 2 == 0 ? PathIterator.SEG_LINETO : PathIterator.SEG_MOVETO;
                } else {
                    dashedType = lastType;
                }

                // move forward
                dashOffset += residual;
                segmentOffset += residual;

                // move forward along the dash array
                if (abs(dashOffsets[dashIndex] - dashOffset) < EPS) {
                    dashIndex++;
                    if (dashIndex >= dashOffsets.length) {
                        dashIndex = 0;
                        dashOffset = 0;
                    }
                }

                // more forward along the segment
                if (abs(segmentOffset - segmentLength) < EPS) {
                    segmentLength = 0;
                    segmentOffset = 0;
                }
            }
        }
    }

    // a small main useful for interactive testing
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) throws Exception {
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();

        Shape stroked = new DashedShape(new Rectangle2D.Double(0, 0, 4, 4), new float[] {2, 2}, 0);
        graphics.draw(stroked);

        graphics.dispose();
        ImageIO.write(image, "png", new File("/tmp/image.png"));

        PathIterator pi = stroked.getPathIterator(new AffineTransform());
        float[] point = new float[2];
        while (!pi.isDone()) {
            int type = pi.currentSegment(point);
            System.out.println(type + " - " + Arrays.toString(point));
            pi.next();
        }
    }
}
