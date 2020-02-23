/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.Icon;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.VendorOptionParser;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Symbolizer;
import org.locationtech.jts.algorithm.MinimumDiameter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.index.quadtree.Quadtree;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;

/**
 * Helper class that helps building (and caching) a texture fill built off a random symbol
 * distribution
 *
 * @author Andrea Aime - GeoSolutions
 */
class RandomFillBuilder {

    public enum PositionRandomizer {
        /** No random symbol distribution */
        NONE,
        /** Freeform random distribution */
        FREE,
        /** Grid based random distribution */
        GRID
    };

    public enum RotationRandomizer {
        /** No angle randomization */
        NONE,
        /** Freeform angle randomizer */
        FREE
    }

    private static final int MAX_RANDOM_COUNT =
            Integer.getInteger("org.geotools.render.random.maxCount", 1024);

    private static final int MAX_RANDOM_ATTEMPTS_MULTIPLIER =
            Integer.getInteger("org.geotools.render.random.maxAttemptsMultiplier", 5);

    private static final boolean RANDOM_VISUAL_DEBUGGER =
            Boolean.getBoolean("org.geotools.render.random.visualDebugger");

    private VendorOptionParser voParser;

    private SLDStyleFactory factory;

    public RandomFillBuilder(VendorOptionParser voParser, SLDStyleFactory factory) {
        this.voParser = voParser;
        this.factory = factory;
    }

    /** Builds a image with a random distribution of the graphic/mark */
    BufferedImage buildRandomTilableImage(
            Symbolizer symbolizer,
            Graphic gr,
            Icon icon,
            Mark mark,
            Shape shape,
            double markSize,
            Object feature) {
        // grab the random generation options
        PositionRandomizer randomizer =
                (PositionRandomizer)
                        voParser.getEnumOption(symbolizer, "random", PositionRandomizer.NONE);
        int seed = voParser.getIntOption(symbolizer, "random-seed", 0);
        int tileSize = voParser.getIntOption(symbolizer, "random-tile-size", 256);
        int count = voParser.getIntOption(symbolizer, "random-symbol-count", 16);
        int spaceAround = voParser.getIntOption(symbolizer, "random-space-around", 0);
        RotationRandomizer rotation =
                (RotationRandomizer)
                        voParser.getEnumOption(
                                symbolizer, "random-rotation", RotationRandomizer.NONE);
        boolean randomRotation = rotation == RotationRandomizer.FREE;

        // minimum validation
        if (tileSize <= 0) {
            throw new IllegalArgumentException("The random-tile-size parameter must be positive");
        }
        if (count > MAX_RANDOM_COUNT) {
            throw new IllegalArgumentException(
                    "The random-symbol-count exceeds the safety limit "
                            + MAX_RANDOM_COUNT
                            + ". You can override this limit by setting the org.geotools.render.random.maxCount system property");
        }
        if (icon != null && (icon.getIconWidth() > tileSize || icon.getIconHeight() > tileSize)) {
            throw new IllegalArgumentException(
                    "Cannot perform random image disposition, image size "
                            + icon.getIconWidth()
                            + "x"
                            + icon.getIconHeight()
                            + " exceeds randomized tile size: "
                            + tileSize);
        }

        // prepare the rendering surface
        BufferedImage image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = image.createGraphics();

        // prepare the bounds of the tile
        Geometry tileBounds = new GeometryBuilder().box(0, 0, tileSize, tileSize);

        // prepare the bounds of the shape
        Geometry bounds = getGeometryBounds(icon, mark, shape, markSize, feature);
        Geometry conflictBounds = getConflictBounds(bounds, spaceAround);
        ReservedAreaCache rac = buildReservedAreaCache(conflictBounds);

        // establish the bounds for the random symbols
        Rectangle targetArea = new Rectangle(0, 0, tileSize, tileSize);

        // build the point sequence generator
        Random random = new Random(seed);
        PositionSequence ps;
        if (randomizer == PositionRandomizer.GRID) {
            ps = new GridBasedPositionGenerator(random, rac, count, targetArea, randomRotation);
        } else {
            ps =
                    new FullyRandomizedPositionGenerator(
                            random, rac, count, targetArea, randomRotation);
        }

        // if we are going to paint rotated images, better set the interpolation to bicubic
        Object oldInterpolationValue = g2d.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        if (randomRotation && icon != null) {
            g2d.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        AffineTransform originalTransform = g2d.getTransform();
        try {
            try {
                // paint the random symbols
                AffineTransform at = new AffineTransform();
                Position p;
                while ((p = ps.getNextPosition()) != null) {
                    at.setToTranslation(p.x, p.y);
                    at.rotate(Math.toRadians(p.rotation));
                    List<AffineTransform2D> transforms = new ArrayList<AffineTransform2D>();
                    AffineTransform2D at2d = new AffineTransform2D(at);
                    transforms.add(at2d);

                    // do we have to build the other 8 possibilities? Happens only if the
                    // symbol is crossing the bounds
                    Geometry transformed = JTS.transform(bounds, at2d);
                    if (tileBounds.intersects(transformed) && !tileBounds.contains(transformed)) {
                        for (int dx = -tileSize; dx <= tileSize; dx += tileSize) {
                            for (int dy = -tileSize; dy <= tileSize; dy += tileSize) {
                                if (dx == 0 && dy == 0) {
                                    continue;
                                }
                                int mx = p.x + dx;
                                int my = p.y + dy;
                                at.setToTranslation(mx, my);
                                at.rotate(Math.toRadians(p.rotation));
                                AffineTransform2D tx2d = new AffineTransform2D(at);
                                Geometry translatedBounds = JTS.transform(bounds, tx2d);
                                if (tileBounds.intersects(translatedBounds)
                                        || tileBounds.contains(translatedBounds)) {
                                    // System.out.println("Adding  " + translatedBounds);
                                    transforms.add(tx2d);
                                }
                            }
                        }
                    }

                    // do we have a conflict in any of the positions?
                    if (!rac.checkAndReserve(transforms)) {
                        // System.out.println(p + " was busy");
                        ps.lastPositionResults(true);
                        continue;
                    }

                    // paint!
                    for (AffineTransform2D transform : transforms) {
                        if (icon != null) {
                            g2d.setTransform(originalTransform);
                            g2d.transform(transform);
                            icon.paintIcon(null, g2d, 0, 0);
                        } else if (shape != null) {
                            factory.fillDrawMark(
                                    g2d,
                                    transform.getTranslateX(),
                                    transform.getTranslateY(),
                                    mark,
                                    markSize,
                                    Math.toRadians(p.rotation),
                                    feature);
                        }
                    }
                    // System.out.println(p + " was free");
                    ps.lastPositionResults(false);
                }
            } catch (TransformException e) {
                throw new RuntimeException(
                        "Unexpected error happened while paining the random symbols", e);
            }
        } finally {
            g2d.setTransform(originalTransform);
            if (oldInterpolationValue != null) {
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolationValue);
            }
        }

        // draw the conflict boxes
        if (RANDOM_VISUAL_DEBUGGER) {
            rac.paintReservedAreas(g2d);
        }

        g2d.dispose();
        return image;
    }

    private ReservedAreaCache buildReservedAreaCache(Geometry conflictBounds) {
        if (conflictBounds != null) {
            return new DefaultReservedAreaCache(conflictBounds);
        } else {
            return new NoOpReservedAreaCache();
        }
    }

    private Geometry getConflictBounds(Geometry bounds, int spaceAround) {
        // apply the space around (with a negative one we might end up with nothing as the result)
        Geometry conflictBounds = bounds;
        if (spaceAround != 0) {
            conflictBounds = bounds.buffer(spaceAround);
            if (conflictBounds.isEmpty() || conflictBounds.getArea() == 0) {
                conflictBounds = null;
            } else {
                conflictBounds = new MinimumDiameter(conflictBounds).getMinimumRectangle();
            }
        }
        return conflictBounds;
    }

    private Geometry getGeometryBounds(
            Icon icon, Mark mark, Shape shape, double markSize, Object feature) {
        Geometry bounds;
        if (icon != null) {
            bounds = new GeometryBuilder().box(0, 0, icon.getIconWidth(), icon.getIconHeight());
        } else {
            // the shape can be very complicated, go for the MBR. Wanted to use ShapeReader, but it
            // blindly assumes the shape is a polygon, while it may not be. Building a multipoint
            // instead
            AffineTransform at = AffineTransform.getScaleInstance(markSize, -markSize);
            Shape ts = at.createTransformedShape(shape);
            Geometry shapeGeometry = JTS.toGeometry(ts);
            bounds = new MinimumDiameter(shapeGeometry).getMinimumRectangle();
        }
        // grow by the stroke size, if this is a mark
        if (icon == null && mark != null) {
            Stroke stroke = factory.getStroke(mark.getStroke(), feature);
            if (stroke instanceof BasicStroke) {
                float width = ((BasicStroke) stroke).getLineWidth() / 2 + 1;
                if (width > 0) {
                    Geometry buffered = bounds.buffer(width);
                    bounds = new MinimumDiameter(buffered).getMinimumRectangle();
                }
            }
        }
        return bounds;
    }

    /**
     * Checks and reserves areas to avoid overlaps between painted symbols
     *
     * @author Andrea Aime - GeoSolutions
     */
    interface ReservedAreaCache {
        public boolean checkAndReserve(List<AffineTransform2D> positions)
                throws MismatchedDimensionException, TransformException;

        public void paintReservedAreas(Graphics2D g2d);
    }

    /**
     * No op implementation, does not do conflict resolution
     *
     * @author Andrea Aime - GeoSolutions
     */
    private static class NoOpReservedAreaCache implements ReservedAreaCache {

        @Override
        public boolean checkAndReserve(List<AffineTransform2D> positions) {
            return true;
        }

        @Override
        public void paintReservedAreas(Graphics2D g2d) {
            // nothing to paint
        }
    }

    /**
     * Stores the various positions of the reserved areas and checks for conflicts
     *
     * @author Andrea Aime - GeoSolutions
     */
    private static class DefaultReservedAreaCache implements ReservedAreaCache {
        Quadtree qt = new Quadtree();

        Geometry conflictBounds;

        public DefaultReservedAreaCache(Geometry conflictBounds) {
            this.conflictBounds = conflictBounds;
        }

        @Override
        public boolean checkAndReserve(List<AffineTransform2D> transforms)
                throws MismatchedDimensionException, TransformException {
            List<Geometry> transformedConflictBounds = new ArrayList<Geometry>();
            boolean conflict = false;
            for (AffineTransform2D tx2d : transforms) {
                if (conflict) {
                    break;
                }
                Geometry cbTransformed = JTS.transform(conflictBounds, tx2d);
                transformedConflictBounds.add(cbTransformed);
                List results = qt.query(cbTransformed.getEnvelopeInternal());
                for (Iterator it = results.iterator(); it.hasNext(); ) {
                    Geometry candidate = (Geometry) it.next();
                    if (candidate.intersects(cbTransformed)) {
                        // location conflict
                        conflict = true;
                        break;
                    }
                }
            }

            // reserve the area if no conflict
            if (!conflict) {
                for (Geometry tcb : transformedConflictBounds) {
                    qt.insert(tcb.getEnvelopeInternal(), tcb);
                }
            }
            return !conflict;
        }

        @Override
        public void paintReservedAreas(Graphics2D g2d) {
            g2d.setStroke(new BasicStroke(0.5f));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR));
            for (Object bound : qt.queryAll()) {
                LiteShape ls = new LiteShape((Geometry) bound, new AffineTransform(), false);
                g2d.draw(ls);
            }
        }
    }

    static final class Position {
        int x;
        int y;
        double rotation;

        public Position(int x, int y, double rotation) {
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }

        @Override
        public String toString() {
            return "Position [x=" + x + ", y=" + y + ", rotation=" + rotation + "]";
        }
    }

    interface PositionSequence {
        Position getNextPosition();

        void lastPositionResults(boolean conflict);
    }

    static class FullyRandomizedPositionGenerator implements PositionSequence {

        int attempts;

        int symbols;

        int targetSymbolCount;

        Random random;

        Rectangle targetArea;

        boolean randomRotation;

        Position position = new Position(0, 0, 0);

        public FullyRandomizedPositionGenerator(
                Random random,
                ReservedAreaCache rac,
                int targetSymbolCount,
                Rectangle targetArea,
                boolean randomRotation) {
            this.targetSymbolCount = targetSymbolCount;
            this.random = random;
            this.targetArea = targetArea;
            this.randomRotation = randomRotation;
        }

        @Override
        public Position getNextPosition() {
            if (attempts > targetSymbolCount * MAX_RANDOM_ATTEMPTS_MULTIPLIER
                    || symbols > targetSymbolCount) {
                return null;
            }

            attempts++;

            position.x = targetArea.x + random.nextInt(targetArea.width);
            position.y = targetArea.y + random.nextInt(targetArea.height);
            if (randomRotation) {
                position.rotation = random.nextDouble() * 360;
            }
            return position;
        }

        @Override
        public void lastPositionResults(boolean conflict) {
            if (!conflict) {
                symbols++;
            }
        }
    }

    static class GridBasedPositionGenerator implements PositionSequence {

        int attempts;

        int symbols;

        int targetSymbolCount;

        double deltaX, deltaY;

        Random random;

        Rectangle targetArea;

        int rows;

        int cols;

        int r;

        int c;

        boolean retry;

        boolean randomRotation;

        Position position = new Position(0, 0, 0);

        public GridBasedPositionGenerator(
                Random random,
                ReservedAreaCache rac,
                int targetSymbolCount,
                Rectangle targetArea,
                boolean randomRotation) {
            this.targetSymbolCount = targetSymbolCount;
            this.random = random;
            this.targetArea = targetArea;
            // first attempt at computing rows and cols
            this.rows = (int) Math.sqrt(targetSymbolCount);
            this.cols = targetSymbolCount / rows;
            // compute deltas, taking into account the symbol size
            this.deltaX = 1d * targetArea.width / cols;
            this.deltaY = 1d * targetArea.height / rows;
            // adapt rows and cols to the deltas just computed
            this.rows = (int) Math.max(Math.round(targetArea.width / deltaX), 1);
            this.cols = (int) Math.max(Math.round(targetArea.height / deltaY), 1);
            this.randomRotation = randomRotation;
        }

        @Override
        public Position getNextPosition() {
            if (symbols > targetSymbolCount || r >= rows) {
                return null;
            }

            attempts++;

            // System.out.println("Grid position: " + c + ", " + r + ", deltas: " + deltaX + "," +
            // deltaY + " target area: " + targetArea);
            position.x =
                    (int)
                            Math.round(
                                    targetArea.getMinX()
                                            + c * deltaX
                                            + random.nextDouble() * deltaX);
            position.y =
                    (int)
                            Math.round(
                                    targetArea.getMinY()
                                            + r * deltaY
                                            + random.nextDouble() * deltaY);
            if (randomRotation) {
                position.rotation = random.nextDouble() * 360;
            }
            // System.out.println("Position: " + position);
            return position;
        }

        @Override
        public void lastPositionResults(boolean conflict) {
            if (!conflict) {
                // move on to the next location
                symbols++;
                moveToNextCell();
            } else {
                // can we retry?
                if (attempts > MAX_RANDOM_ATTEMPTS_MULTIPLIER) {
                    // too many attempts, this cell will be left empty
                    moveToNextCell();
                }
            }
        }

        private void moveToNextCell() {
            c++;
            if (c >= cols) {
                r++;
                c = 0;
            }
            attempts = 0;
        }
    }
}
