/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/**
 * Custom stroke to drape shapes along Line Strings
 *
 * @author ImranR
 */
public class MarkAlongLine implements Stroke {

    public static final String VENDOR_OPTION_NAME = "markAlongLine";
    public static final String VENDOR_OPTION_SCALE_LIMIT = "markAlongLineScaleLimit";
    public static final String VENDOR_OPTION_SIMPLICATION_FACTOR = "markAlongLineSimplify";

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MarkAlongLine.class.getName());

    // if two connecting line segments angle is beyond 180 (+/-) tolarance
    // consider it a proper turn
    private static final int LINEAR_ANGLE_TOLERANCE = 35;

    Stroke delegate;

    // shape`s size cannot be reduced beyond this scale Limit
    // 0 - always scale
    // 1 - never scale
    private float scaleImit = 0.9f;

    // multiplier to calculate simplification distance
    // should be between 0 and 1
    // 0 = dont simplify
    // 1 = use complete height as simplification distance
    private float simplicationFactor = 0.5f;

    MarkAlongLiteShape wktShape;
    AffineTransform at;
    // default size
    double size = 20;

    static GeometryFactory gf = new GeometryFactory();

    public MarkAlongLine(Stroke delegate) {
        this.delegate = delegate;
    }

    public MarkAlongLine(Stroke delegate, double size, Geometry wkt) {
        this.delegate = delegate;
        this.size = size / JTS.toRectangle2D(wkt.getEnvelopeInternal()).getHeight();

        AffineTransformation at = new AffineTransformation();
        at.setToScale(this.size, this.size);
        this.wktShape = new MarkAlongLiteShape(at.transform(wkt), null, false);
    }

    /** @return the scaleImit */
    public float getScaleImit() {
        return scaleImit;
    }

    /**
     * shape`s size cannot be reduced beyond this scale Limit 0 - always scale 1 - never scale
     *
     * @param scaleImit the scaleImit to set
     */
    public void setScaleImit(float scaleImit) {
        if (scaleImit < 0 || scaleImit > 1 || Float.isInfinite(scaleImit) || Float.isNaN(scaleImit)) {
            LOGGER.severe("Invalid Scale limit " + scaleImit + ", should be between 0 and 1");
        }
        this.scaleImit = scaleImit;
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Scale Limit set to " + this.scaleImit);
    }

    /** @return the simplicationTolerance */
    public float getSimplicationFactor() {
        return simplicationFactor;
    }

    /**
     * Multiplier between 0 to 1, used to calculate the distance in pixel to be used for simplification of source linear
     * geometry *
     *
     * <p>0 - never simplify 1 - use complete height as simplification distance default - 0.5 (half the height)
     *
     * @param simplicationFactor to scale WKT shape height and use it for simplification
     */
    public void setSimplicationFactor(float simplicationFactor) {
        if (simplicationFactor < 0
                || simplicationFactor > 1
                || Float.isInfinite(simplicationFactor)
                || Float.isNaN(simplicationFactor)) {
            LOGGER.severe("Invalid Simplification Factor " + simplicationFactor + ", should be between 0 and 1");
        }
        this.simplicationFactor = simplicationFactor;
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Simplication factor set to " + this.simplicationFactor);
    }

    @Override
    public Shape createStrokedShape(Shape shape) {
        GeneralPath newshape = new GeneralPath(); // Start with an empty shape

        // array to store cuurent coordinates
        float[] coords = new float[6];
        // array to store coordinates from prev iteration
        // used to define a line segment which will be used to create shape to drape
        float[] prevcoords = new float[6];
        boolean connectPrevious = false;
        Coordinate lastShapeEndedAtCoordinate = null;
        LineSegment nextLineSegment = null;
        LineSegment previousLineSegment = null;
        MarkAlongLiteShape drapeMe = null;
        MarkAlongLiteShape previousDrapeMe = null;
        double time = new Date().getTime();
        boolean segmentsTouch = false;
        int segments = 0;
        int type = 0;
        float projFactor = 0;
        float turnAngle;
        boolean insideTurn = false;
        boolean isLinear = false;
        // in case of multipolygon
        // draw each polygon as a separate shape
        // because we want to able to close the final path
        List<LiteShape2> unpackedList;
        try {
            unpackedList = unPackMultiPolygon(shape);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error unpacking Multi Polygon", e);
            unpackedList = Arrays.asList((LiteShape2) shape);
        }

        for (LiteShape2 innerShape : unpackedList) {
            for (PathIterator i = innerShape.getPathIterator(null); !i.isDone(); i.next()) {
                type = i.currentSegment(coords);
                segments++;
                switch (type) {
                    case PathIterator.SEG_MOVETO:
                        newshape.moveTo(coords[0], coords[1]);
                        break;
                    case PathIterator.SEG_LINETO:
                        if (LOGGER.isLoggable(Level.FINER)) LOGGER.finer("------------------------------------");
                        nextLineSegment = new LineSegment(
                                new Coordinate(prevcoords[0], prevcoords[1]), new Coordinate(coords[0], coords[1]));
                        if (previousLineSegment != null)
                            if (LOGGER.isLoggable(Level.FINER))
                                LOGGER.finer("Segments "
                                        + previousLineSegment.toString()
                                        + " --> "
                                        + nextLineSegment.toString());

                        // do they touch
                        if (previousLineSegment == null) segmentsTouch = false;
                        else segmentsTouch = segmentsTouch(nextLineSegment, previousLineSegment);
                        // checking for sloppy connections
                        // where previous shape and new shape might overlap
                        // this will not occur on first segment
                        // or if the segments dont touch
                        if (previousDrapeMe != null && segmentsTouch) {
                            if (previousDrapeMe.getLeftOver() != null && previousDrapeMe.isClipped()) {
                                connectPrevious = true;
                                // project last coods to on to next segment to see if there is bad
                                // overlap
                                // bad overlap = tight turn angles (well below 90) and part of
                                // next shape overlapping previous shape
                                // but we have not drawn the next shape yet so only way to check is
                                // to check projection factor of last drawn vertex on next segment
                                // if its below 1,means the point lies around the next segment
                                // also check the the angle between connecting segments
                                // and if the point ended up inside the turn
                                projFactor = (float) nextLineSegment.projectionFactor(lastShapeEndedAtCoordinate);
                                Coordinate pointOnLine = nextLineSegment.project(lastShapeEndedAtCoordinate);
                                turnAngle = angleBetweenSegments(previousLineSegment, nextLineSegment);
                                insideTurn =
                                        isInsideTurn(previousLineSegment, nextLineSegment, lastShapeEndedAtCoordinate);
                                isLinear = Math.abs(turnAngle - 180) < LINEAR_ANGLE_TOLERANCE;
                                if (LOGGER.isLoggable(Level.FINER)) {
                                    LOGGER.finer(
                                            "projection factor " + projFactor + " winding rule:" + i.getWindingRule());
                                    LOGGER.finer("segments turning to " + turnAngle);
                                    LOGGER.finer("is inside the turn " + insideTurn);
                                    LOGGER.finer("is linear " + isLinear);
                                }

                                if (projFactor < 1 || insideTurn && !isLinear) {

                                    if (LOGGER.isLoggable(Level.FINER)) {
                                        LOGGER.finer("shapes will overlap");
                                        // update left over of previous
                                        // draw line on to next segment to avoid sloppy connections
                                        newshape.lineTo(pointOnLine.x, pointOnLine.y);
                                        Coordinate c = new Coordinate(prevcoords[0], prevcoords[1]);
                                        LOGGER.finer("Draw line " + new LineSegment(c, pointOnLine));
                                    }

                                    // update prev coordinates
                                    prevcoords[0] = (float) pointOnLine.x;
                                    prevcoords[1] = (float) pointOnLine.y;
                                    // re-int next segment
                                    nextLineSegment = new LineSegment(
                                            new Coordinate(prevcoords[0], prevcoords[1]),
                                            new Coordinate(coords[0], coords[1]));
                                    // previousDrapeMe=null;
                                }
                            }
                        }
                        // get shape for drapping over passed segment
                        // the shape might include left over part for previous segment
                        drapeMe = getShapeForSegment(
                                nextLineSegment, // the segment to drape on
                                previousDrapeMe // last drapped shape..which might have left
                                // over
                                );

                        if (drapeMe == null) {
                            // reset
                            connectPrevious = false;
                            previousDrapeMe = null;
                            lastShapeEndedAtCoordinate = null;
                            previousLineSegment = null;
                            break;
                        } else if ((boolean) drapeMe.getHints().getOrDefault(MarkAlongLiteShape.SKIP_ME, false)) break;
                        if (previousLineSegment != null && !connectPrevious) {
                            LOGGER.finer("connect previous " + connectPrevious + ": segments touch:" + segmentsTouch);
                        }

                        // finally draw
                        previousDrapeMe = drape(
                                newshape,
                                drapeMe,
                                nextLineSegment.p0,
                                nextLineSegment.angle(),
                                lastShapeEndedAtCoordinate,
                                connectPrevious);

                        // remember where the shape left off after being affine transformed
                        lastShapeEndedAtCoordinate = new Coordinate(
                                previousDrapeMe.getEndofShapeCoords()[0],
                                previousDrapeMe.getEndofShapeCoords()[1]);
                        // drop the flag
                        connectPrevious = false;
                        break;
                    case PathIterator.SEG_QUADTO:
                        newshape.quadTo(coords[0], coords[1], coords[2], coords[3]);
                        break;
                    case PathIterator.SEG_CUBICTO:
                        newshape.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                        break;
                    case PathIterator.SEG_CLOSE:
                        newshape.closePath();
                        break;
                }
                prevcoords = coords.clone();
                previousLineSegment = nextLineSegment;
            }

            if (innerShape.getGeometry() instanceof Polygon) {
                // close the ring when finished drawing polygons
                if (innerShape.getGeometry().getCoordinates().length > 2) {

                    newshape.moveTo(lastShapeEndedAtCoordinate.x, lastShapeEndedAtCoordinate.y);
                    newshape.lineTo(
                            innerShape.getGeometry().getCoordinates()[0].x,
                            innerShape.getGeometry().getCoordinates()[0].y);
                }
            }
        }

        // Finally, stroke the perturbed shape and return the result
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Created " + segments + " in " + (new Date().getTime() - time) + " milliseconds");
        }
        return delegate.createStrokedShape(newshape);
    }

    // we need to draw multipolygons as different polygons so that they can be closed
    private List<LiteShape2> unPackMultiPolygon(Shape shape) throws Exception {

        LiteShape2 liteShape = (LiteShape2) shape;
        if (MultiPolygon.class.isAssignableFrom(liteShape.getGeometry().getClass())) {
            MultiPolygon multiPolygon = (MultiPolygon) liteShape.getGeometry();
            ArrayList<LiteShape2> polygons = new ArrayList<>();

            for (int i = 0; i < multiPolygon.getNumGeometries(); i++) {
                polygons.add(new LiteShape2(multiPolygon.getGeometryN(i), liteShape.getMathTransform(), null, false));
            }

            return polygons;
        } else return Arrays.asList((LiteShape2) shape);
    }

    private boolean segmentsTouch(LineSegment seg1, LineSegment seg2) {
        if (seg1.p0.equals2D(seg2.p0) || seg1.p0.equals2D(seg2.p1)) return true;
        if (seg1.p1.equals2D(seg2.p0) || seg1.p1.equals2D(seg2.p1)) return true;
        return false;
    }

    // this method assumes that both segment have one common corrdinate
    private float angleBetweenSegments(LineSegment seg1, LineSegment seg2) {
        //        Coordinate tail = seg1.intersection(seg2);
        Coordinate tail = seg1.p0.equals2D(seg2.p0) ? seg1.p0 : seg1.p1;
        Coordinate tip1 = seg1.p0.equals2D(tail) ? seg1.p1 : seg1.p0;
        Coordinate tip2 = seg2.p0.equals2D(tail) ? seg2.p1 : seg2.p0;

        float angle = (float) Math.toDegrees(Angle.angleBetween(tip1, tail, tip2));

        return Float.isFinite(angle) ? angle : 0f;
    }

    private boolean isInsideTurn(LineSegment seg1, LineSegment seg2, Coordinate pt) {
        //        Coordinate tail = seg1.intersection(seg2);
        Coordinate tail = seg1.p0.equals2D(seg2.p0) ? seg1.p0 : seg1.p1;
        Coordinate tip1 = seg1.p0.equals2D(tail) ? seg1.p1 : seg1.p0;
        Coordinate tip2 = seg2.p0.equals2D(tail) ? seg2.p1 : seg2.p0;

        return gf.createPolygon(gf.createLinearRing(new Coordinate[] {tail, tip1, tip2, tail}))
                .intersects(gf.createPoint(pt));
    }

    private MarkAlongLiteShape getClone(AffineTransform at) {

        MarkAlongLiteShape clone = new MarkAlongLiteShape(wktShape.getGeometry(), at, false);
        clone.getHints().putAll(wktShape.getHints());
        return clone;
    }

    private MarkAlongLiteShape getShapeForSegment(LineSegment segmentToDrapOver, MarkAlongLiteShape previousDrapeMe) {
        double length = segmentToDrapOver.getLength();
        return getExtendedTransformedShape(length, previousDrapeMe);
    }

    private MarkAlongLiteShape getExtendedTransformedShape(double length, MarkAlongLiteShape previousDrapeMe) {
        // e.g repetition = 2.5 means drape the shape completey twice and once half
        float repetition = (float) (length / this.wktShape.getBounds2D().getWidth());
        MarkAlongLiteShape repeatedShape = this.getClone(null); // first shape
        if (previousDrapeMe != null)
            if (previousDrapeMe.getLeftOver() != null && previousDrapeMe.isClipped()) {
                repeatedShape.preAppend(previousDrapeMe.getLeftOver());
            }
        repeatedShape.setSegmentLength(length);
        // AT to translate copies of geometry and union them into result shape
        AffineTransformation at = new AffineTransformation();

        Geometry geom;
        Geometry cloneGeom;
        Geometry translatedGeom;
        double translateX = 0;
        for (int i = 1; i < repetition; i++) {
            // translate along x according to the width and repetition number
            // first time use width of final shape, it might have previous part of shape also
            // else increment starting from previous translation + original size of shape
            translateX = i == 1
                    ? repeatedShape.getBounds().getWidth()
                    : translateX + this.wktShape.getBounds2D().getWidth();
            at.setToTranslation(translateX, 0);
            geom = repeatedShape.getGeometry();
            cloneGeom = this.getClone(null).getGeometry();
            translatedGeom = at.transform(cloneGeom);
            repeatedShape.setGeometry(geom.union(translatedGeom));
        }

        // length adjustment
        if (repeatedShape.getBounds().width != length) {
            repeatedShape = MarkAlongLine.FitOnLength(repeatedShape, length, scaleImit);
        }

        return repeatedShape;
    }

    public static synchronized MarkAlongLiteShape FitOnLength(
            MarkAlongLiteShape markAlongLiteShape, double length, double minScaleLimit) {

        double scaleX = length / markAlongLiteShape.getBounds().width;
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("Final Shape needs " + scaleX + " Scale X for Width Correction");
        if (Double.isInfinite(scaleX)) return null;
        AffineTransformation at = new AffineTransformation();
        at.setToScale(scaleX, 1d);

        // resize if scale limit is below the limit
        // scaleX will never be above 1
        if (scaleX >= minScaleLimit) markAlongLiteShape.setGeometry(at.transform(markAlongLiteShape.getGeometry()));
        else {
            // resize envlope to meet line length
            Geometry env = markAlongLiteShape.getGeometry().getEnvelope();
            Geometry transformedEnv = at.transform(env);
            Geometry clipped = markAlongLiteShape.getGeometry().intersection(transformedEnv);

            if (clipped == null || clipped.isEmpty()) {
                markAlongLiteShape.getHints().put(MarkAlongLiteShape.SKIP_ME, true);
                return markAlongLiteShape;
            }
            // the left over part that exceeded the length
            // keep it also, it will be used to continue drawing of the shape
            // where it discontinued from
            Geometry remainingGeom = markAlongLiteShape.getGeometry().difference(transformedEnv);
            markAlongLiteShape.setGeometry(clipped);
            markAlongLiteShape.setLeftOver(remainingGeom);
            markAlongLiteShape.getHints().put(MarkAlongLiteShape.CLIPPED, true);
        }

        return markAlongLiteShape;
    }

    /**
     * @param newshape reference of shape painter
     * @param wktShape shape to drape
     * @param drapeFromCoordinate point to start/continue drawing from (tx,ty)
     * @param rotationRadians rotation the segment being drapped upon
     * @param previousShapeEndCoordinate where previous shape finished drawing
     * @param joinWithLastSegment flag to signal if this shape is to continue previous incomplete shape
     * @return wktShape passed param with populated hints
     */
    private MarkAlongLiteShape drape(
            GeneralPath newshape,
            MarkAlongLiteShape wktShape,
            Coordinate drapeFromCoordinate,
            double rotationRadians,
            Coordinate previousShapeEndCoordinate,
            boolean joinWithLastSegment) {
        // setting AT to anchor wktShape to start of line segnment
        // and rotate to line segment angle
        AffineTransformation at = new AffineTransformation();
        at.rotate(rotationRadians);
        at.translate(drapeFromCoordinate.x, drapeFromCoordinate.y);
        wktShape.setGeometry(at.transform(wktShape.getGeometry()));

        float[] coords = new float[6];
        boolean skipSegment = joinWithLastSegment;

        for (PathIterator i = wktShape.getPathIterator(null); !i.isDone(); i.next()) {
            int type = i.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    // connect this shape with previous shape if necessary
                    if (skipSegment) {
                        newshape.moveTo(previousShapeEndCoordinate.x, previousShapeEndCoordinate.y);
                        newshape.quadTo(
                                previousShapeEndCoordinate.x, previousShapeEndCoordinate.y, coords[0], coords[1]);
                        skipSegment = false;
                    } else newshape.moveTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    newshape.lineTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_QUADTO:
                    newshape.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    newshape.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    newshape.closePath();
                    break;
            }
        }
        // save where drawing of this shape stopped for reference
        wktShape.getHints().put(MarkAlongLiteShape.FINAL_COORDS, coords);
        return wktShape;
    }

    public double getSimplificatorFactor() {
        return this.wktShape.getBounds2D().getHeight() * simplicationFactor;
    }

    //    public boolean doSimplification(Geometry g) {
    //        double simpleicationFactor = getSimplificatorFactor();
    //        float segmentsBelowFactor = 0;
    //        Coordinate prevCord = null;
    //        LineSegment lineSegment;
    //        for (int i = 0; i < g.getCoordinates().length; i++) {
    //            // compare segment length with simplicationFactor
    //            if (i % 2 != 0) {
    //                lineSegment = new LineSegment(prevCord, g.getCoordinates()[i]);
    //                if (lineSegment.getLength() < simpleicationFactor) segmentsBelowFactor++;
    //            }
    //            prevCord = g.getCoordinates()[i];
    //        }
    //
    //        // ratio between 0 to 1, describing how many line segments are below
    //        // the simplifaction factor
    //        // incase they are less than 0.4 (e.g 40%), dont simplify
    //        // since this when map is zoomed in to much and simplification
    //        // slows down rendering adding un-neccessary segments
    //        float percentage = segmentsBelowFactor / (g.getCoordinates().length / 2);
    //        if (LOGGER.isLoggable(Level.FINER))
    //            LOGGER.finer(
    //                    percentage
    //                            + "for are simplication factor "
    //                            + simplicationTolerance
    //                            + " simplification done : "
    //                            + (percentage > simplicationTolerance));
    //
    //        return percentage > simplicationTolerance;
    //    }

    class MarkAlongLiteShape extends LiteShape {

        private static final String CLIPPED = "clipped";
        private static final String FINAL_COORDS = "final_coords";
        private static final String SKIP_ME = "skip_me";

        private Map<String, Object> hints = new HashMap<>();

        private double segmentLength = -1;

        LineSegment projectedSegment = null;
        Geometry leftOver = null;

        public MarkAlongLiteShape(Geometry geom, AffineTransform at, boolean generalize) {
            super(geom, at, generalize);
        }

        public Map<String, Object> getHints() {
            return hints;
        }

        public double getSegmentLength() {
            return segmentLength;
        }

        public void setSegmentLength(double segmentLength) {
            this.segmentLength = segmentLength;
        }

        // this method will be used to pre-fix geometry
        public void preAppend(Geometry geometry) {
            // it is assmed that the incoming geometry will be anchorid to 0,0
            AffineTransformation at = new AffineTransformation();
            ReferencedEnvelope env = JTS.toEnvelope(geometry);
            at.setToTranslation(env.getWidth(), 0);
            // move right to make space of incoming geom
            Geometry tranformedTarget = at.transform(this.getGeometry());
            this.setGeometry(geometry.union(tranformedTarget));
        }

        public Geometry getLeftOver() {
            return leftOver;
        }

        public void setLeftOver(Geometry leftOver) {
            if (leftOver == null) {
                this.leftOver = null;
                return;
            }
            // translate to so that it starts from 0,0
            AffineTransformation at = new AffineTransformation();
            at.setToScale(1d, 1d);
            ReferencedEnvelope env = JTS.toEnvelope(leftOver);
            at.setToTranslation(env.getMinX() * -1, 0);
            this.leftOver = at.transform(leftOver);
        }

        public float[] getEndofShapeCoords() {
            return (float[]) hints.getOrDefault(FINAL_COORDS, new float[6]);
        }

        public boolean isClipped() {
            return (boolean) hints.getOrDefault(CLIPPED, false);
        }

        public LineSegment getProjectedSegment() {
            return projectedSegment;
        }

        public void setProjectedSegment(LineSegment projectedSegment) {
            this.projectedSegment = projectedSegment;
        }
    }
}
