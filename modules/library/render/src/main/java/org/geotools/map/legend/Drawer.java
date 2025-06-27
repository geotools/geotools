/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map.legend;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Style;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.SLD;
import org.geotools.styling.StyleBuilder;
import org.geotools.util.NumberRange;
import org.geotools.util.factory.GeoTools;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/** This class is used to isolate GeoTools from the specific graphic library being used for rendering. */
public class Drawer {

    private GeometryFactory gf = new GeometryFactory();

    private Drawer() {
        // prevent subclassing
    }

    /**
     * Retrieve the default Drawing implementation.
     *
     * @return Drawing ready for use
     */
    public static Drawer create() {
        return new Drawer();
    }

    /**
     * Used to draw a freature directly onto the provided image.
     *
     * <p>Feature coordintes are in the same coordinates as the image.
     *
     * <p>You may call this method multiple times to draw several features onto the same Image (say for glyph creation).
     *
     * @param bi Image to render on to
     * @param feature Feature to be rendered
     * @param style Style to render feature with
     */
    public void drawDirect(BufferedImage bi, SimpleFeature feature, Style style) {

        drawFeature(bi, feature, style, new AffineTransform());
    }

    public void drawDirect(BufferedImage bi, SimpleFeature feature, Rule rule) {

        AffineTransform worldToScreenTransform = new AffineTransform();

        drawFeature(bi, feature, worldToScreenTransform, false, getSymbolizers(rule), null);
    }

    public void drawFeature(
            BufferedImage bi,
            SimpleFeature feature,
            AffineTransform worldToScreenTransform,
            boolean drawVertices,
            MathTransform mt) {
        if (feature == null) return;
        drawFeature(bi, feature, worldToScreenTransform, drawVertices, getSymbolizers(feature), mt);
    }

    public void drawFeature(BufferedImage bi, SimpleFeature feature, AffineTransform worldToScreenTransform) {
        if (feature == null) return;
        drawFeature(bi, feature, worldToScreenTransform, false, getSymbolizers(feature), null);
    }

    public void drawFeature(
            BufferedImage bi, SimpleFeature feature, AffineTransform worldToScreenTransform, Style style) {
        if (feature == null) return;
        drawFeature(bi, feature, worldToScreenTransform, false, getSymbolizers(style), null);
    }

    public void drawFeature(
            BufferedImage bi, SimpleFeature feature, Style style, AffineTransform worldToScreenTransform) {
        if (feature == null) return;

        drawFeature(bi, feature, worldToScreenTransform, false, getSymbolizers(style), null);
    }

    Symbolizer[] getSymbolizers(Style style) {
        List<Symbolizer> symbs = new ArrayList<>();
        for (FeatureTypeStyle fstyle : style.featureTypeStyles()) {
            for (Rule rule : fstyle.rules()) {
                symbs.addAll(rule.symbolizers());
            }
        }
        return symbs.toArray(new Symbolizer[symbs.size()]);
    }

    Symbolizer[] getSymbolizers(Rule rule) {
        List<Symbolizer> symbs = new ArrayList<>();
        symbs.addAll(rule.symbolizers());
        return symbs.toArray(new Symbolizer[symbs.size()]);
    }

    public static Symbolizer[] getSymbolizers(SimpleFeature feature) {
        return getSymbolizers(((Geometry) feature.getDefaultGeometry()).getClass(), Color.RED);
    }

    public static Symbolizer[] getSymbolizers(Class<? extends Geometry> type, Color baseColor) {
        return getSymbolizers(type, baseColor, true);
    }

    public static Symbolizer[] getSymbolizers(
            Class<? extends Geometry> type, Color baseColor, boolean useTransparency) {

        StyleBuilder builder = new StyleBuilder();
        Symbolizer[] syms = new Symbolizer[1];
        if (LineString.class.isAssignableFrom(type) || MultiLineString.class.isAssignableFrom(type))
            syms[0] = builder.createLineSymbolizer(baseColor, 2);
        if (Point.class.isAssignableFrom(type) || MultiPoint.class.isAssignableFrom(type)) {
            PointSymbolizer point = builder.createPointSymbolizer(builder.createGraphic());
            // set graphic size to 10 by default
            point.getGraphic()
                    .setSize(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints())
                            .literal(10));

            // danger assumes a Mark!
            Mark mark = (Mark) point.getGraphic().graphicalSymbols().get(0);
            mark.setFill(builder.createFill(baseColor));
            syms[0] = point;
        }
        if (Polygon.class.isAssignableFrom(type) || MultiPolygon.class.isAssignableFrom(type)) {
            syms[0] = builder.createPolygonSymbolizer(
                    builder.createStroke(baseColor, 2), builder.createFill(baseColor, useTransparency ? .6 : 1.0));
        }
        return syms;
    }

    public void drawFeature(
            BufferedImage bi,
            SimpleFeature feature,
            AffineTransform worldToScreenTransform,
            boolean drawVertices,
            Symbolizer[] symbs,
            MathTransform mt) {

        LiteShape shape = new LiteShape(null, worldToScreenTransform, false);
        if (symbs == null) return;
        for (Symbolizer symb : symbs) {
            drawFeature(bi, feature, worldToScreenTransform, drawVertices, symb, mt, shape);
        }
    }

    public void drawFeature(
            BufferedImage bi,
            SimpleFeature feature,
            AffineTransform worldToScreenTransform,
            boolean drawVertices,
            Symbolizer symbolizer,
            MathTransform mathTransform,
            LiteShape shape) {

        Graphics graphics = bi.getGraphics();

        if (!(symbolizer instanceof RasterSymbolizer)) {
            Geometry g = findGeometry(feature, symbolizer);
            if (g == null) return;
            if (mathTransform != null) {
                try {
                    g = JTS.transform(g, mathTransform);
                } catch (Exception e) {
                    // do nothing
                }
            }
            shape.setGeometry(g);

            paint(bi, feature, shape, symbolizer);
            if (drawVertices) {
                double averageDistance = 0;
                Coordinate[] coords = g.getCoordinates();
                java.awt.Point oldP = worldToPixel(coords[0], worldToScreenTransform);
                for (int i = 1; i < coords.length; i++) {
                    Coordinate coord = coords[i];
                    java.awt.Point p = worldToPixel(coord, worldToScreenTransform);
                    averageDistance += p.distance(oldP) / i;
                    oldP = p;
                }
                int pixels = 1;
                if (averageDistance > 20) pixels = 3;
                if (averageDistance > 60) pixels = 5;
                if (pixels > 1) {
                    graphics.setColor(Color.RED);
                    for (Coordinate coord : coords) {
                        java.awt.Point p = worldToPixel(coord, worldToScreenTransform);
                        graphics.fillRect(p.x - (pixels - 1) / 2, p.y - (pixels - 1) / 2, pixels, pixels);
                    }
                }
            }
        }
    }

    /** Unsure if this is the paint for the border, or the fill? */
    private void paint(BufferedImage bi, SimpleFeature feature, LiteShape shape, Symbolizer symb) {

        Graphics graphics = bi.getGraphics();
        Graphics2D g = (Graphics2D) graphics;

        if (symb instanceof PolygonSymbolizer) {
            PolygonSymbolizer polySymb = (PolygonSymbolizer) symb;
            Color stroke = SLD.polyColor(polySymb);
            double opacity = SLD.polyFillOpacity(polySymb);
            Color fill = SLD.polyFill(polySymb);

            int width = SLD.width(SLD.stroke(polySymb));
            if (width == SLD.NOTFOUND) width = 1;

            if (Double.isNaN(opacity)) opacity = 1.0;
            if (fill != null) {
                fill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), (int) (255 * opacity));
                g.setColor(fill);
                g.fill(shape);
            }
            if (stroke != null) {
                g.setColor(stroke);

                Stroke str = new BasicStroke(width);
                g.setStroke(str);

                g.draw(shape);
            }
        }
        if (symb instanceof LineSymbolizer) {
            LineSymbolizer lineSymbolizer = (LineSymbolizer) symb;
            Color c = SLD.color(lineSymbolizer);
            int w = SLD.width(lineSymbolizer);
            if (c != null && w > 0) {
                g.setColor(c);

                Stroke str = new BasicStroke(w);
                g.setStroke(str);

                g.draw(shape);
            }
        }
        if (symb instanceof PointSymbolizer) {
            PointSymbolizer pointSymbolizer = (PointSymbolizer) symb;

            Color c = SLD.pointColor(pointSymbolizer);
            Color fill = SLD.pointFill(pointSymbolizer);
            int width = SLD.width(SLD.stroke(pointSymbolizer));

            if (width == SLD.NOTFOUND) width = 1;

            float[] point = new float[6];
            shape.getPathIterator(null).currentSegment(point);
            SLDStyleFactory styleFactory = new SLDStyleFactory();
            Style2D tmp = styleFactory.createStyle(
                    feature, pointSymbolizer, NumberRange.create(Double.MIN_VALUE, Double.MAX_VALUE));

            if (tmp instanceof MarkStyle2D) {
                MarkStyle2D style = (MarkStyle2D) tmp;
                Shape shape2 = style.getTransformedShape(point[0], point[1]);

                if (c == null && fill == null) {
                    g.setColor(Color.GRAY);
                    g.fill(shape2);
                }

                if (fill != null) {
                    g.setColor(fill);
                    g.fill(shape2);
                } else {
                    g.setColor(Color.GRAY);
                    g.fill(shape2);
                }
                if (c != null) {
                    Stroke str = new BasicStroke(width);
                    g.setStroke(str);
                    g.setColor(c);
                    g.draw(shape2);
                } else {
                    Stroke str = new BasicStroke(width);
                    g.setStroke(str);
                    g.setColor(Color.DARK_GRAY);
                    g.draw(shape2);
                }
            } else if (tmp instanceof GraphicStyle2D) {
                GraphicStyle2D style = (GraphicStyle2D) tmp;

                float rotation = style.getRotation(); // in radians

                g.setTransform(AffineTransform.getRotateInstance(rotation));

                BufferedImage image = style.getImage();

                g.drawImage(
                        image,
                        (int) ((point[0] - image.getWidth()) / 2d),
                        (int) ((point[1] - image.getHeight()) / 2d),
                        null);
            }
        }
    }

    /**
     * Finds the geometric attribute requested by the symbolizer
     *
     * @param f The victim
     * @param s The symbolizer
     * @return The geometry requested in the symbolizer, or the default geometry if none is specified
     */
    private org.locationtech.jts.geom.Geometry findGeometry(SimpleFeature f, Symbolizer s) {
        String geomName = getGeometryPropertyName(s);
        // get the geometry
        org.locationtech.jts.geom.Geometry geom;
        if (geomName == null) {
            geom = (org.locationtech.jts.geom.Geometry) f.getDefaultGeometry();
        } else {
            geom = (org.locationtech.jts.geom.Geometry) f.getAttribute(geomName);
        }
        // if the symbolizer is a point or text symbolizer generate a suitable
        // location to place the
        // point in order to avoid recomputing that location at each rendering
        // step
        if ((s instanceof PointSymbolizer || s instanceof TextSymbolizer) && !(geom instanceof Point)) {
            if (geom instanceof LineString && !(geom instanceof LinearRing)) {
                // use the mid point to represent the point/text symbolizer
                // anchor
                Coordinate[] coordinates = geom.getCoordinates();
                Coordinate start = coordinates[0];
                Coordinate end = coordinates[1];
                Coordinate mid = new Coordinate((start.x + end.x) / 2, (start.y + end.y) / 2);
                geom = geom.getFactory().createPoint(mid);
            } else {
                // otherwise use the centroid of the polygon
                geom = geom.getCentroid();
            }
        }
        return geom;
    }

    private String getGeometryPropertyName(Symbolizer s) {
        String geomName = null;
        // TODO: fix the styles, the getGeometryPropertyName should probably be
        // moved into an interface...
        if (s instanceof PolygonSymbolizer) {
            geomName = s.getGeometryPropertyName();
        } else if (s instanceof PointSymbolizer) {
            geomName = s.getGeometryPropertyName();
        } else if (s instanceof LineSymbolizer) {
            geomName = s.getGeometryPropertyName();
        } else if (s instanceof TextSymbolizer) {
            geomName = s.getGeometryPropertyName();
        }
        return geomName;
    }

    public java.awt.Point worldToPixel(Coordinate coord, AffineTransform worldToScreenTransform) {
        Point2D w = new Point2D.Double(coord.x, coord.y);
        AffineTransform at = worldToScreenTransform;
        Point2D p = at.transform(w, new Point2D.Double());
        return new java.awt.Point((int) p.getX(), (int) p.getY());
    }

    /** TODO summary sentence for worldToScreenTransform ... */
    public static AffineTransform worldToScreenTransform(Envelope mapExtent, Rectangle screenSize) {
        double scaleX = screenSize.getWidth() / mapExtent.getWidth();
        double scaleY = screenSize.getHeight() / mapExtent.getHeight();

        double tx = -mapExtent.getMinX() * scaleX;
        double ty = mapExtent.getMinY() * scaleY + screenSize.getHeight();

        AffineTransform at = new AffineTransform(scaleX, 0.0d, 0.0d, -scaleY, tx, ty);

        return at;
    }
    /**
     * Create a SimpleFeatureType schema using a type short hand.
     *
     * <p>Code Example:
     *
     * <pre><code>
     * new Drawing().schema("namespace.typename", "id:0,*geom:LineString,name:String,*centroid:Point");
     * </code></pre>
     *
     * <ul>
     *   <li>SimpleFeatureType with identifier "namespace.typename"
     *   <li>Default Geometry "geom" of type LineStirng indicated with a "*"
     *   <li>Three attributes: id of type Integer, name of type String and centroid of type Point
     * </ul>
     *
     * @param name namespace.name
     * @return Generated SimpleFeatureType
     */
    public SimpleFeatureType schema(String name, String spec) {
        try {
            return DataUtilities.createType(name, spec);
        } catch (SchemaException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static SimpleFeatureType pointSchema;
    static SimpleFeatureType lineSchema;
    static SimpleFeatureType polygonSchema;
    static SimpleFeatureType multipointSchema;
    static SimpleFeatureType multilineSchema;
    static SimpleFeatureType multipolygonSchema;

    static {
        try {
            pointSchema = DataUtilities.createType("generated:point", "*point:Point"); // $NON-NLS-1$ //$NON-NLS-2$
            lineSchema = DataUtilities.createType(
                    "generated:linestring", "*linestring:LineString"); // $NON-NLS-1$ //$NON-NLS-2$
            polygonSchema =
                    DataUtilities.createType("generated:polygon", "*polygon:Polygon"); // $NON-NLS-1$ //$NON-NLS-2$
            multipointSchema = DataUtilities.createType(
                    "generated:multipoint", "*multipoint:MultiPoint"); // $NON-NLS-1$ //$NON-NLS-2$
            multilineSchema = DataUtilities.createType(
                    "generated:multilinestring", "*multilinestring:MultiLineString"); // $NON-NLS-1$ //$NON-NLS-2$
            multipolygonSchema = DataUtilities.createType(
                    "generated:multipolygon", "*multipolygon:MultiPolygon"); // $NON-NLS-1$ //$NON-NLS-2$
        } catch (SchemaException unExpected) {
            // System.err.println(unExpected);
        }
    }

    /**
     * Just a convinient method to create feature from geometry.
     *
     * @param geom the geometry to create feature from
     * @return feature instance
     */
    public SimpleFeature feature(Geometry geom) {
        if (geom instanceof Polygon) {
            return feature((Polygon) geom);
        } else if (geom instanceof MultiPolygon) {
            return feature((MultiPolygon) geom);
        } else if (geom instanceof Point) {
            return feature((Point) geom);
        } else if (geom instanceof LineString) {
            return feature((LineString) geom);
        } else if (geom instanceof MultiPoint) {
            return feature((MultiPoint) geom);
        } else if (geom instanceof MultiLineString) {
            return feature((MultiLineString) geom);
        } else {
            throw new IllegalArgumentException("Geometry is not supported to create feature"); // $NON-NLS-1$
        }
    }

    /**
     * Simple feature with one attribute called "point".
     *
     * @return SimpleFeature with a default geometry and no attribtues
     */
    public SimpleFeature feature(Point point) {
        if (point == null) throw new NullPointerException("Point required"); // $NON-NLS-1$
        try {
            return SimpleFeatureBuilder.build(pointSchema, new Object[] {point}, null);
        } catch (IllegalAttributeException e) {
            // this should not happen because we *know* the parameter matches schame
            throw new RuntimeException("Could not generate feature for point " + point); // $NON-NLS-1$
        }
    }
    /**
     * Simple Feature with a default geometry and no attribtues.
     *
     * @return Feature with a default geometry and no attribtues
     */
    public SimpleFeature feature(LineString line) {
        if (line == null) throw new NullPointerException("line required"); // $NON-NLS-1$
        try {
            return SimpleFeatureBuilder.build(lineSchema, new Object[] {line}, null);
        } catch (IllegalAttributeException e) {
            // this should not happen because we *know* the parameter matches schame
            throw new RuntimeException("Could not generate feature for point " + line); // $NON-NLS-1$
        }
    }

    /**
     * Simple Feature with a default geometry and no attribtues.
     *
     * @return Feature with a default geometry and no attribtues
     */
    public SimpleFeature feature(Polygon polygon) {
        if (polygon == null) throw new NullPointerException("polygon required"); // $NON-NLS-1$
        try {
            return SimpleFeatureBuilder.build(polygonSchema, new Object[] {polygon}, null);
        } catch (IllegalAttributeException e) {
            // this should not happen because we *know* the parameter matches schame
            throw new RuntimeException("Could not generate feature for point " + polygon); // $NON-NLS-1$
        }
    }

    /**
     * Simple Feature with a default geometry and no attribtues.
     *
     * @return Feature with a default geometry and no attribtues
     */
    public SimpleFeature feature(MultiPoint multipoint) {
        if (multipoint == null) throw new NullPointerException("multipoint required"); // $NON-NLS-1$
        try {
            return SimpleFeatureBuilder.build(multipointSchema, new Object[] {multipoint}, null);
        } catch (IllegalAttributeException e) {
            // this should not happen because we *know* the parameter matches schame
            throw new RuntimeException("Could not generate feature for point " + multipoint); // $NON-NLS-1$
        }
    }
    /**
     * Simple Feature with a default geometry and no attribtues.
     *
     * @return Feature with a default geometry and no attribtues
     */
    public SimpleFeature feature(MultiLineString multilinestring) {
        if (multilinestring == null) throw new NullPointerException("multilinestring required"); // $NON-NLS-1$
        try {
            return SimpleFeatureBuilder.build(multilineSchema, new Object[] {multilinestring}, null);
        } catch (IllegalAttributeException e) {
            // this should not happen because we *know* the parameter matches schame
            throw new RuntimeException("Could not generate feature for point " + multilinestring); // $NON-NLS-1$
        }
    }
    /**
     * Simple Feature with a default geometry and no attribtues.
     *
     * @return Feature with a default geometry and no attribtues
     */
    public SimpleFeature feature(MultiPolygon multipolygon) {
        if (multipolygon == null) throw new NullPointerException("multipolygon required"); // $NON-NLS-1$
        try {
            return SimpleFeatureBuilder.build(multipolygonSchema, new Object[] {multipolygon}, null);
        } catch (IllegalAttributeException e) {
            // this should not happen because we *know* the parameter matches schame
            throw new RuntimeException("Could not generate feature for point " + multipolygon); // $NON-NLS-1$
        }
    }

    /**
     * Generate Point from two dimensional ordinates
     *
     * @return Point
     */
    public Point point(int x, int y) {
        return gf.createPoint(new Coordinate(x, y));
    }
    /**
     * Generate LineStrings from two dimensional ordinates
     *
     * @return LineStirng
     */
    public LineString line(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLineString(coords);
    }

    /**
     * Generate a MultiLineString from two dimensional ordinates
     *
     * @return MultiLineStirng
     */
    public MultiLineString lines(int[][] xy) {
        LineString[] lines = new LineString[xy.length];

        for (int i = 0; i < xy.length; i++) {
            lines[i] = line(xy[i]);
        }

        return gf.createMultiLineString(lines);
    }
    /**
     * Convience constructor for GeometryFactory.createPolygon.
     *
     * <p>The provided xy ordinates are turned into a linear rings.
     *
     * @param xy Two dimensional ordiantes.
     * @return Polygon
     */
    public Polygon polygon(int[] xy) {
        LinearRing shell = ring(xy);
        return gf.createPolygon(shell, null);
    }

    /**
     * Convience constructor for GeometryFactory.createPolygon.
     *
     * <p>The provided xy and holes are turned into linear rings.
     *
     * @param xy Two dimensional ordiantes.
     * @param holes Holes in polygon or null.
     * @return Polygon
     */
    public Polygon polygon(int[] xy, int[][] holes) {
        if (holes == null || holes.length == 0) {
            return polygon(xy);
        }
        LinearRing shell = ring(xy);

        LinearRing[] rings = new LinearRing[holes.length];

        for (int i = 0; i < xy.length; i++) {
            rings[i] = ring(holes[i]);
        }
        return gf.createPolygon(shell, rings);
    }

    /**
     * Convience constructor for GeometryFactory.createLinearRing.
     *
     * @param xy Two dimensional ordiantes.
     * @return LinearRing for use with polygon
     */
    public LinearRing ring(int[] xy) {
        int length = xy.length / 2;
        if (xy[0] != xy[xy.length - 2] || xy[1] != xy[xy.length - 1]) {
            length++;
        }
        Coordinate[] coords = new Coordinate[length];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }
        if (xy[0] != xy[xy.length - 2] || xy[1] != xy[xy.length - 1]) {
            coords[length - 1] = coords[0];
        }
        return gf.createLinearRing(coords);
    }
}
