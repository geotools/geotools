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
package org.geotools.renderer.style;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.logging.Logger;
import org.geotools.renderer.util.ExplicitBoundsShape;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Factory with additional "well known" marks for compatibility with other applications.
 *
 * <p>We are doubling up on these providing an aleais to make things easier:
 *
 * <ul>
 *   <li>qgis://star
 *   <li>qgis://regular_star and regular_star
 * </ul>
 *
 * @author Jonathan Moules (LightPear)
 */
public class QGISMarkFactory implements MarkFactory {
    /** QGIS prefix for explicit reference to QGIS wellknown marks */
    private static final String PREFIX = "qgis://";

    /** The logger for the rendering module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(QGISMarkFactory.class);

    /** Tall star for QGIS compatibility */
    private static Shape star = star();

    /** Tall triangle for QGIS compatibility */
    private static Shape triangle = triangle();

    /** Upward facing arrow for QGIS compatibility */
    private static final Shape arrow = arrow();

    private static Shape arrowhead = arrowHead();

    private static Shape filled_arrowhead = filledArrowHead();

    /** diamond */
    private static Shape diamond;

    /** pentagon */
    private static Shape pentagon;

    private static Shape crossFill = crossFill();

    private static Shape diagonalHalfSquare = diagonalHalfSquare();

    private static Shape halfSquare = halfSquare();

    private static Shape hexagon = hexagon();

    private static Shape leftHalfTriangle = leftHalfTriangle();

    private static Shape rightHalfTriangle = rightHalfTriangle();

    private static Shape quarterCricle = quarterCircle();

    private static Shape semiCircle = semiCircle();

    private static Shape thirdCircle = thirdCircle();

    private static Shape quarterSquare = quarterSquare();

    static {

        // Diamond
        GeneralPath diamondPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        diamondPath.moveTo(0f, 1f);
        diamondPath.lineTo(1f, 0f);
        diamondPath.lineTo(0f, -1f);
        diamondPath.lineTo(-1f, 0f);
        diamondPath.lineTo(0f, 1f);
        AffineTransform at = new AffineTransform();
        at.scale(.5, .5);
        diamondPath.transform(at);

        diamond = new ExplicitBoundsShape(diamondPath);
        ((ExplicitBoundsShape) diamond).setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        // Pentagon
        GeneralPath pentagonPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        pentagonPath.moveTo(0f, 1f);
        pentagonPath.lineTo(0.9511f, 0.3090f);
        pentagonPath.lineTo(0.5878f, -0.8090f);
        pentagonPath.lineTo(-0.5878f, -0.8090f);
        pentagonPath.lineTo(-0.9511f, 0.3090f);
        pentagonPath.lineTo(0f, 1f);
        at = new AffineTransform();
        at.scale(.5, .5);
        pentagonPath.transform(at);

        pentagon = new ExplicitBoundsShape(pentagonPath);
        ((ExplicitBoundsShape) pentagon).setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));
    }

    public Shape getShape(Graphics2D graphics, Expression symbolUrl, Feature feature)
            throws Exception {
        // cannot handle a null url
        if (symbolUrl == null) return null;

        String wellKnownName = symbolUrl.evaluate(feature, String.class);

        LOGGER.finer("fetching mark of name " + wellKnownName);

        if (wellKnownName.equalsIgnoreCase(PREFIX + "cross")) {
            LOGGER.finer("returning qgis cross");

            return ShapeMarkFactory.shapes.get("plus");
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "circle")) {
            LOGGER.finer("returning qgis circle");

            return WellKnownMarkFactory.circle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "triangle")) {
            LOGGER.finer("returning qgis triangle");

            return triangle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "equilateral_triangle")
                || wellKnownName.equalsIgnoreCase("equilateral_triangle")) {
            LOGGER.finer("returning qgis triangle");

            return WellKnownMarkFactory.triangle;
        }

        // Direct conflict with WellKnownMarkFactory star
        if (wellKnownName.equalsIgnoreCase(PREFIX + "star")) {
            LOGGER.finer("returning qgis star");

            return star;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "cross2")
                || wellKnownName.equalsIgnoreCase("cross2")) {
            LOGGER.finer("returning qgis cross2");

            return ShapeMarkFactory.shapes.get("times");
        }

        // This arrow points up, conflicts with WellKnownMarkFactory.arrow pointing right.
        if (wellKnownName.equalsIgnoreCase(PREFIX + "arrow")) {
            LOGGER.finer("returning qgis arrow");

            return arrow;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "diamond")
                || wellKnownName.equalsIgnoreCase("diamond")) {
            LOGGER.finer("returning qgis diamond");

            return diamond;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "pentagon")
                || wellKnownName.equalsIgnoreCase("pentagon")) {
            LOGGER.finer("returning qgis pentagon");

            return pentagon;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "rectangle")
                || wellKnownName.equalsIgnoreCase("rectangle")) {
            LOGGER.finer("returning qgis rectangle");

            return WellKnownMarkFactory.square;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "regular_star")
                || wellKnownName.equalsIgnoreCase("regular_star")) {
            LOGGER.finer("returning qgis regular_star");
            return WellKnownMarkFactory.star;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "line")
                || wellKnownName.equalsIgnoreCase("line")) {
            LOGGER.finer("returning qgis line");

            return ShapeMarkFactory.shapes.get("vertline");
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "arrowhead")
                || wellKnownName.equalsIgnoreCase("arrowhead")) {
            LOGGER.finer("returning qgis arrowhead");

            return arrowhead;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "filled_arrowhead")
                || wellKnownName.equalsIgnoreCase("filled_arrowhead")) {
            LOGGER.finer("returning qgis filled_arrowhead");

            return filled_arrowhead;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "crossfill")
                || wellKnownName.equalsIgnoreCase("crossfill")) {
            LOGGER.finer("returning qgis filled cross");

            return crossFill;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "diagonalhalfsquare")
                || wellKnownName.equalsIgnoreCase("diagonalhalfsquare")) {
            LOGGER.finer("returning qgis diagonal half square");

            return diagonalHalfSquare;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "HalfSquare")
                || wellKnownName.equalsIgnoreCase("HalfSquare")) {
            LOGGER.finer("returning qgis half square");

            return halfSquare;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "hexagon")
                || wellKnownName.equalsIgnoreCase("hexagon")) {
            LOGGER.finer("returning qgis hexagon");

            return hexagon;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "lefthalftriangle")
                || wellKnownName.equalsIgnoreCase("lefthalftriangle")) {
            LOGGER.finer("returning qgis lefthalftriangle");

            return leftHalfTriangle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "righthalftriangle")
                || wellKnownName.equalsIgnoreCase("righthalftriangle")) {
            LOGGER.finer("returning qgis righthalftriangle");

            return rightHalfTriangle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "quartercircle")
                || wellKnownName.equalsIgnoreCase("quartercircle")) {
            LOGGER.finer("returning qgis quartercircle");

            return quarterCricle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "semicircle")
                || wellKnownName.equalsIgnoreCase("semicircle")) {
            LOGGER.finer("returning qgis semicircle");

            return semiCircle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "thirdcircle")
                || wellKnownName.equalsIgnoreCase("thirdcircle")) {
            LOGGER.finer("returning qgis thirdcircle");

            return thirdCircle;
        }

        if (wellKnownName.equalsIgnoreCase(PREFIX + "quartersquare")
                || wellKnownName.equalsIgnoreCase("quartersquare")) {
            LOGGER.finer("returning qgis quartersquare");

            return quarterSquare;
        }

        return null;
    }

    private static Shape arrow() {
        GeneralPath arrowPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        arrowPath.moveTo(.3f, -.3f);
        arrowPath.lineTo(.5f, .0f);
        arrowPath.lineTo(.3f, .3f);
        arrowPath.lineTo(.3f, .1f);
        arrowPath.lineTo(-.5f, .1f);
        arrowPath.lineTo(-.5f, -.1f);
        arrowPath.lineTo(.3f, -.1f);
        arrowPath.lineTo(.3f, -.3f);

        AffineTransform at = new AffineTransform();
        at.rotate(Math.PI / 2.0);
        arrowPath.transform(at);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(arrowPath);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape star() {
        GeneralPath starPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        starPath.moveTo(0f, 0.5f);
        starPath.lineTo(0.2f, 0.1f);
        starPath.lineTo(0.5f, 0.1f);
        starPath.lineTo(0.2f, -0.1f);
        starPath.lineTo(0.5f, -0.5f);
        starPath.lineTo(0f, -0.2f);
        starPath.lineTo(-0.5f, -0.5f); // max = 7.887
        starPath.lineTo(-0.2f, -0.1f);
        starPath.lineTo(-0.5f, 0.1f);
        starPath.lineTo(-0.2f, 0.1f);
        starPath.lineTo(0f, 0.5f);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(starPath);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape arrowHead() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(-0.5f, 0.4f);
        gp.lineTo(0, 0);
        gp.lineTo(-0.5f, -0.4f);

        ExplicitBoundsShape arrowHead = new ExplicitBoundsShape(gp);
        arrowHead.setBounds(new Rectangle2D.Double(-0.5, -0.5, 1, 1));
        return arrowHead;
    }

    private static Shape filledArrowHead() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo(-0.5f, 0.4f);
        gp.lineTo(0, 0);
        gp.lineTo(-0.5f, -0.4f);
        gp.closePath();
        ExplicitBoundsShape filledArrowHead = new ExplicitBoundsShape(gp);
        filledArrowHead.setBounds(new Rectangle2D.Double(-0.5, -0.5, 1, 1));
        return filledArrowHead;
    }

    private static Shape triangle() {
        GeneralPath trianglePath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        trianglePath.moveTo(0f, 1f);
        trianglePath.lineTo(0.866f, -.8f);
        trianglePath.lineTo(-0.866f, -.8f);
        trianglePath.lineTo(0f, 1f);

        AffineTransform at = new AffineTransform();
        at.translate(0, -.10);
        at.scale(.5, .5);
        trianglePath.transform(at);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(trianglePath);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape crossFill() {

        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        path.moveTo(-.5f, -0.1f);
        path.lineTo(-.5f, -0.1f);
        path.lineTo(-.5f, 0.1f);
        path.lineTo(-0.1f, 0.1f);
        path.lineTo(-0.1f, .5f);
        path.lineTo(0.1f, .5f);
        path.lineTo(0.1f, 0.1f);
        path.lineTo(.5f, 0.1f);
        path.lineTo(.5f, -0.1f);
        path.lineTo(0.1f, -0.1f);
        path.lineTo(0.1f, -.5f);
        path.lineTo(-0.1f, -.5f);
        path.lineTo(-0.1f, -0.1f);
        path.lineTo(-.5f, -0.1f);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(path);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape diagonalHalfSquare() {
        // (bottom-left half)

        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        path.moveTo(-.5f, -.5f);
        path.lineTo(.5f, -.5f);
        path.lineTo(-.5f, .5f);
        path.lineTo(-.5f, -.5f);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(path);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape hexagon() {

        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        path.moveTo(-0.4330f, -0.25f);
        path.lineTo(-0.4330f, 0.25f);
        path.lineTo(0f, .5f);
        path.lineTo(0.4330f, 0.25f);
        path.lineTo(0.4330f, -0.25f);
        path.lineTo(0f, -.5f);
        path.lineTo(-0.4330f, -0.25f);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(path);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape leftHalfTriangle() {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        path.moveTo(0f, -.5f);
        path.lineTo(0f, .5f);
        path.lineTo(.5f, -.5f);
        path.lineTo(0f, -.5f);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(path);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape rightHalfTriangle() {
        GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);

        path.moveTo(0f, -.5f);
        path.lineTo(0f, .5f);
        path.lineTo(-.5f, -.5f);
        path.lineTo(0f, -.5f);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(path);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape quarterCircle() {
        // Top-left quarter
        Shape d = new Arc2D.Double(new Rectangle2D.Double(-.5, -.5, 1., 1.), 180, 90, Arc2D.PIE);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(d);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape semiCircle() {
        // Top half
        Arc2D.Double d =
                new Arc2D.Double(new Rectangle2D.Double(-.5, -.5, 1., 1.), 180., 180., Arc2D.PIE);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(d);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape thirdCircle() {
        // Top-left third
        Arc2D.Double d =
                new Arc2D.Double(new Rectangle2D.Double(-.5, -.5, 1., 1.), 150., 120., Arc2D.PIE);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(d);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }

    private static Shape halfSquare() {
        // Left half
        Shape s = new Double(-.5, -.5, .5, 1.);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(s);
        shape.setBounds(new Rectangle2D.Double(-.5, -.5, 1., 1.));

        return shape;
    }

    private static Shape quarterSquare() {
        // Top-left quarter
        Shape s = new Double(-.5, 0, .5, .5);

        ExplicitBoundsShape shape = new ExplicitBoundsShape(s);
        shape.setBounds(new Rectangle2D.Double(-.5, .5, 1., 1.));

        return shape;
    }
}
