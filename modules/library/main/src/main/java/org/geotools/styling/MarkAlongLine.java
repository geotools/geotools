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
package org.geotools.styling;

import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.WKTReader2;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.io.ParseException;

public class MarkAlongLine implements Stroke {

    // shape`s size cannot be reduced beyound this scale Limit
    private static final float MIN_SCALE_LIMIT = 0.9f;

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(MarkAlongLine.class);

    private Decimator decimator;

    Stroke delegate;

    static final WKTReader2 reader = new WKTReader2();
    static final GeometryFactory geometryFactory = new GeometryFactory();
    MarkAlongLiteShape wktShape;
    AffineTransform at;
    // default
    double size = 20;

    public MarkAlongLine(Stroke delegate) {
        this.delegate = delegate;
    }

    public MarkAlongLine(Stroke delegate, double size, String wkt) {
        this.delegate = delegate;
        this.size = size;
        setWKT(wkt);
        decimator =
                new Decimator(
                        this.wktShape.getBounds().getWidth(),
                        this.wktShape.getBounds().getHeight());
    }

    public void setWKT(String wkt) {
        try {
            AffineTransformation at = new AffineTransformation();
            at.setToScale(size, size);
            this.wktShape = new MarkAlongLiteShape(reader.read(wkt), null, false);
            this.wktShape.setGeometry(at.transform(this.wktShape.getGeometry()));
            String simpleWkt = this.wktShape.getGeometry().toText();
            // simplify complex geom to simple geometry
            if (!simpleWkt.equalsIgnoreCase(wkt)) {
                this.wktShape = new MarkAlongLiteShape(reader.read(simpleWkt), null, false);
            }
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error setting WKT:" + wkt, e);
        }
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
        MarkAlongLiteShape drapeMe = null;
        MarkAlongLiteShape previousDrapeMe = null;
        double time = new Date().getTime();
        int segments = 0;
        for (PathIterator i = shape.getPathIterator(null); !i.isDone(); i.next()) {
            int type = i.currentSegment(coords);
            segments++;
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    newshape.moveTo(coords[0], coords[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    nextLineSegment =
                            new LineSegment(
                                    new Coordinate(prevcoords[0], prevcoords[1]),
                                    new Coordinate(coords[0], coords[1]));

                    // checking for sloppy connections
                    // where previous shape and new shape might overlap
                    // this will not occur on first time
                    if (previousDrapeMe != null)
                        if (previousDrapeMe.getLeftOver() != null && previousDrapeMe.isClipped()) {
                            connectPrevious = true;
                            // project last coods to on to next segment to see if there is bad
                            // overlap
                            // bad overlap = angle with connecting segment will result
                            // in a sloppy connection between clipped shape and the rest of its part
                            double projFactor =
                                    nextLineSegment.projectionFactor(lastShapeEndedAtCoordinate);
                            Coordinate pointOnLine =
                                    nextLineSegment.project(lastShapeEndedAtCoordinate);
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.fine("projection factor " + projFactor);
                            }

                            if (projFactor < 1) {
                                if (LOGGER.isLoggable(Level.FINE))
                                    LOGGER.fine("shapes will overlap");
                                // update left over of previous
                                // draw line on to next segment to avoid sloppy connections
                                newshape.lineTo(pointOnLine.x, pointOnLine.y);
                                // update prev coordinates
                                prevcoords[0] = (float) pointOnLine.x;
                                prevcoords[1] = (float) pointOnLine.y;
                                // re-int next segment
                                nextLineSegment =
                                        new LineSegment(
                                                new Coordinate(prevcoords[0], prevcoords[1]),
                                                new Coordinate(coords[0], coords[1]));
                            }
                        }
                    // get shape for drapping over passed segment
                    // the shape might include left over part for previous segment
                    drapeMe =
                            getShapeForSegment(
                                    nextLineSegment, // the segment to drape on
                                    previousDrapeMe // last drapped shape..which might have left
                                    // over
                                    );
                    if (drapeMe == null) {
                        // reset
                        connectPrevious = false;
                        previousDrapeMe = null;
                        lastShapeEndedAtCoordinate = null;
                        break;
                    }

                    // finally draw
                    previousDrapeMe =
                            drape(
                                    newshape, // reference of shape
                                    drapeMe, // shape to drape
                                    nextLineSegment.p0, // point to start drapping from (tx,ty)
                                    nextLineSegment
                                            .angle(), // rotation the segment being drapped upon
                                    lastShapeEndedAtCoordinate, // where previous shape finished
                                    connectPrevious // flag to signal if this shape is to continue
                                    // previous incomplete shape
                                    );

                    // remember where the shape left off after being affine transformed
                    lastShapeEndedAtCoordinate =
                            new Coordinate(
                                    previousDrapeMe.getEndofShapeCoords()[0],
                                    previousDrapeMe.getEndofShapeCoords()[1]);
                    // drop the flag
                    connectPrevious = false;
                    break;
                case PathIterator.SEG_QUADTO:
                    newshape.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
                case PathIterator.SEG_CUBICTO:
                    newshape.curveTo(
                            coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                    break;
                case PathIterator.SEG_CLOSE:
                    newshape.closePath();
                    break;
            }
            prevcoords = coords.clone();
        }
        newshape.closePath();
        // Finally, stroke the perturbed shape and return the result
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(
                    "Created "
                            + segments
                            + " in "
                            + (new Date().getTime() - time)
                            + " milliseconds");
        }
        return delegate.createStrokedShape(newshape);
    }

    private MarkAlongLiteShape getClone(AffineTransform at) {

        MarkAlongLiteShape clone = new MarkAlongLiteShape(wktShape.getGeometry(), at, false);
        clone.getHints().putAll(wktShape.getHints());
        return clone;
    }

    private MarkAlongLiteShape getShapeForSegment(
            LineSegment segmentToDrapOver, MarkAlongLiteShape previousDrapeMe) {
        double length = segmentToDrapOver.getLength();
        return getExtendedTransformedShape(length, previousDrapeMe);
    }

    private MarkAlongLiteShape getExtendedTransformedShape(
            double length, MarkAlongLiteShape previousDrapeMe) {
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
            translateX =
                    (i == 1)
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
            repeatedShape = MarkAlongLine.FitOnLength(repeatedShape, length);
        }

        return repeatedShape;
    }

    public static MarkAlongLiteShape FitOnLength(
            MarkAlongLiteShape markAlongLiteShape, double length) {

        double scaleX = length / markAlongLiteShape.getBounds().width;
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Final Shape needs " + scaleX + " Scale X for Width Correction");
        if (Double.isInfinite(scaleX)) return null;
        AffineTransformation at = new AffineTransformation();
        at.setToScale(scaleX, 1d);

        // resize if scale limit is below the limit
        // scaleX will never be above 1
        if (scaleX >= MIN_SCALE_LIMIT)
            markAlongLiteShape.setGeometry(at.transform(markAlongLiteShape.getGeometry()));
        else {
            // resize envlope to meet line length
            Geometry env = markAlongLiteShape.getGeometry().getEnvelope();
            Geometry transformedEnv = at.transform(env);
            // clip the geometry
            Geometry clipped = markAlongLiteShape.getGeometry().intersection(transformedEnv);
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
                                previousShapeEndCoordinate.x,
                                previousShapeEndCoordinate.y,
                                coords[0],
                                coords[1]);
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
                    newshape.curveTo(
                            coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
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

    public Decimator getDecimator() {
        return decimator;
    }

    public double getSimplificatorFactor() {
        return this.wktShape.getBounds2D().getWidth();
    }

    class MarkAlongLiteShape extends LiteShape {

        private static final String CLIPPED = "clipped";
        private static final String FINAL_COORDS = "final_coords";

        private Map<String, Object> hints = new HashMap<String, Object>();

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
