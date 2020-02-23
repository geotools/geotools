/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import org.geotools.geometry.jts.Decimator;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.image.io.ImageIOExt;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.LineStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.PointStyle2D;
import org.geotools.renderer.style.PolygonStyle2D;
import org.geotools.renderer.style.Style2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.filter.expression.Literal;
import org.opengis.style.ExternalGraphic;
import org.opengis.style.GraphicLegend;
import org.opengis.style.GraphicalSymbol;

/**
 * A simple class that knows how to paint a Shape object onto a Graphic given a Style2D. It's the
 * last step of the rendering engine, and has been factored out since both renderers do use the same
 * painting logic.
 *
 * @author Andrea Aime
 */
public class StyledShapePainter {
    public static final Key TEXTURE_ANCHOR_HINT_KEY = new TextureAnchorKey();
    private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

    /** The logger for the rendering module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyledShapePainter.class);
    /** Whether icon centers should be matched to a pixel center, or not */
    public static boolean ROUND_ICON_COORDS =
            Boolean.parseBoolean(
                    System.getProperty("org.geotools.renderer.lite.roundIconCoords", "true"));

    /**
     * Whether to apply the new vector hatch fill optimization, or not (on by default, this is just
     * a safeguard)
     */
    public static boolean OPTIMIZE_VECTOR_HATCH_FILLS =
            Boolean.parseBoolean(
                    System.getProperty(
                            "org.geotools.renderer.lite.optimizeVectorHatchFills", "true"));

    /**
     * the label cache, used to populate the label cache with reserved areas for labeling obstacles
     */
    LabelCache labelCache;

    public StyledShapePainter() {
        // nothing do do, just needs to exist
    }

    public StyledShapePainter(LabelCache cache) {
        this.labelCache = cache;
    }

    public void paint(
            final Graphics2D graphics,
            final LiteShape2 shape,
            final Style2D style,
            final double scale) {
        paint(graphics, shape, style, scale, false);
    }

    /**
     * Invoked automatically when a polyline is about to be draw. This implementation paints the
     * polyline according to the rendered style
     *
     * @param graphics The graphics in which to draw.
     * @param shape The polygon to draw.
     * @param style The style to apply, or <code>null</code> if none.
     * @param scale The scale denominator for the current zoom level
     */
    public void paint(
            final Graphics2D graphics,
            final LiteShape2 shape,
            final Style2D style,
            final double scale,
            boolean isLabelObstacle) {
        if (style == null) {
            // TODO: what's going on? Should not be reached...
            LOGGER.severe("ShapePainter has been asked to paint a null style!!");

            return;
        }

        // Is the current scale within the style scale range?
        if (!style.isScaleInRange(scale)) {
            LOGGER.fine("Out of scale");
            return;
        }

        if (style instanceof IconStyle2D) {
            AffineTransform temp = graphics.getTransform();
            try {
                IconStyle2D icoStyle = (IconStyle2D) style;
                Icon icon = icoStyle.getIcon();
                graphics.setComposite(icoStyle.getComposite());

                // the displacement to be applied to all points, centers the icon and applies the
                // Graphic displacement as well
                float dx = icoStyle.getDisplacementX();
                float dy = icoStyle.getDisplacementY();

                // iterate over all points
                float[] coords = new float[2];
                PathIterator citer = getPathIterator(shape);
                AffineTransform at = new AffineTransform(temp);
                while (!(citer.isDone())) {
                    if (citer.currentSegment(coords) != PathIterator.SEG_MOVETO) {
                        at.setTransform(temp);

                        double x = coords[0] + dx;
                        double y = coords[1] + dy;
                        at.translate(x, y);
                        at.rotate(icoStyle.getRotation());
                        at.translate(
                                -(icon.getIconWidth() * icoStyle.getAnchorPointX()),
                                (icon.getIconHeight() * (icoStyle.getAnchorPointY() - 1)));
                        graphics.setTransform(at);

                        icon.paintIcon(null, graphics, 0, 0);

                        if (isLabelObstacle) {
                            // TODO: rotation?
                            labelCache.put(
                                    new Rectangle2D.Double(
                                            x, y, icon.getIconWidth(), icon.getIconHeight()));
                        }
                    }
                    citer.next();
                }
            } finally {
                graphics.setTransform(temp);
            }
        } else if (style instanceof MarkStyle2D) {
            PathIterator citer = getPathIterator(shape);

            // get the point onto the shape has to be painted
            float[] coords = new float[2];
            MarkStyle2D ms2d = (MarkStyle2D) style;

            Shape transformedShape;
            while (!(citer.isDone())) {
                if (citer.currentSegment(coords) != PathIterator.SEG_MOVETO) {
                    transformedShape = ms2d.getTransformedShape(coords[0], coords[1]);
                    if (transformedShape != null) {
                        if (ms2d.getFill() != null) {
                            graphics.setPaint(ms2d.getFill());
                            graphics.setComposite(ms2d.getFillComposite());
                            graphics.fill(transformedShape);
                        }

                        if (ms2d.getContour() != null) {
                            graphics.setPaint(ms2d.getContour());
                            graphics.setStroke(ms2d.getStroke());
                            graphics.setComposite(ms2d.getContourComposite());
                            graphics.draw(transformedShape);
                        }

                        if (isLabelObstacle) {
                            labelCache.put(transformedShape.getBounds2D());
                        }
                    }
                }
                citer.next();
            }
        } else if (style instanceof GraphicStyle2D) {
            float[] coords = new float[2];
            PathIterator iter = getPathIterator(shape);
            iter.currentSegment(coords);

            GraphicStyle2D gs2d = (GraphicStyle2D) style;

            BufferedImage image = gs2d.getImage();
            double dx = gs2d.getDisplacementX() - gs2d.getAnchorPointX() * image.getWidth();
            double dy =
                    gs2d.getDisplacementY() - ((1 - gs2d.getAnchorPointY()) * image.getHeight());
            while (!(iter.isDone())) {
                if (iter.currentSegment(coords) != PathIterator.SEG_MOVETO) {
                    renderImage(
                            graphics,
                            coords[0],
                            coords[1],
                            dx,
                            dy,
                            image,
                            gs2d.getRotation(),
                            gs2d.getComposite(),
                            isLabelObstacle);
                }
                iter.next();
            }
        } else {
            if (isLabelObstacle) {
                labelCache.put(shape.getBounds2D());
            }
            // if the style is a polygon one, process it even if the polyline is
            // not closed (by SLD specification)
            if (style instanceof PolygonStyle2D
                    && !optimizeOutFill((PolygonStyle2D) style, shape)) {
                PolygonStyle2D ps2d = (PolygonStyle2D) style;

                if (ps2d.getFill() != null) {
                    Paint paint = ps2d.getFill();

                    if (paint instanceof TexturePaint) {
                        TexturePaint tp = (TexturePaint) paint;
                        BufferedImage image = tp.getImage();
                        Rectangle2D cornerRect = tp.getAnchorRect();
                        Point2D anchorPoint =
                                (Point2D) graphics.getRenderingHint(TEXTURE_ANCHOR_HINT_KEY);
                        Rectangle2D alignedRect = null;
                        if (anchorPoint != null) {
                            alignedRect =
                                    new Rectangle2D.Double(
                                            Math.round(anchorPoint.getX()),
                                            Math.round(anchorPoint.getY()),
                                            cornerRect.getWidth(),
                                            cornerRect.getHeight());
                        } else {
                            alignedRect =
                                    new Rectangle2D.Double(
                                            0.0,
                                            0.0,
                                            cornerRect.getWidth(),
                                            cornerRect.getHeight());
                        }
                        paint = new TexturePaint(image, alignedRect);
                    }

                    graphics.setPaint(paint);
                    graphics.setComposite(ps2d.getFillComposite());
                    fillLiteShape(graphics, shape);
                }
                if (ps2d.getGraphicFill() != null) {
                    Shape oldClip = graphics.getClip();
                    try {
                        paintGraphicFill(graphics, shape, ps2d.getGraphicFill(), scale);
                    } finally {
                        graphics.setClip(oldClip);
                    }
                }
            }

            if (style instanceof LineStyle2D) {
                LineStyle2D ls2d = (LineStyle2D) style;
                paintLineStyle(graphics, shape, ls2d, isLabelObstacle, 0.5f);
            }
        }
    }

    /**
     * Checks if the fill can simply be omitted because it's not going to be visible anyways. It
     * takes a style that has a solid outline and a width or height that's less than the stroke
     * width
     */
    private boolean optimizeOutFill(PolygonStyle2D style, LiteShape2 shape) {
        // if we have a graphic stroke the outline might not be solid, so, not covering
        if (style.getGraphicStroke() != null) {
            return false;
        }

        final Stroke stroke = style.getStroke();
        if (stroke == null || !(stroke instanceof BasicStroke)) {
            return false;
        }
        // we need a solid composite to optimize out
        Composite composite = style.getContourComposite();
        if (!(composite instanceof AlphaComposite)) {
            return false;
        }
        AlphaComposite ac = (AlphaComposite) composite;
        if (ac.getAlpha() < 1) {
            return false;
        }

        // if dashed, it's not covering
        BasicStroke basic = (BasicStroke) stroke;
        if (basic.getDashArray() != null) {
            return false;
        }

        float lineWidth = basic.getLineWidth();
        Rectangle2D bounds = shape.getBounds2D();
        return bounds.getWidth() < lineWidth || bounds.getHeight() < lineWidth;
    }

    void paintLineStyle(
            final Graphics2D graphics,
            final LiteShape2 shape,
            final LineStyle2D ls2d,
            boolean isLabelObstacle,
            float strokeWidthAdjustment) {

        if (ls2d.getStroke() != null) {
            // see if a graphic stroke is to be used, the drawing method
            // is completely
            // different in this case
            if (ls2d.getGraphicStroke() != null) {
                drawWithGraphicsStroke(
                        graphics,
                        dashShape(shape, ls2d.getStroke()),
                        ls2d.getGraphicStroke(),
                        isLabelObstacle);
            } else {
                Paint paint = ls2d.getContour();

                if (paint instanceof TexturePaint) {
                    TexturePaint tp = (TexturePaint) paint;
                    BufferedImage image = tp.getImage();
                    Rectangle2D rect = tp.getAnchorRect();
                    AffineTransform at = graphics.getTransform();
                    double width = rect.getWidth() * at.getScaleX();
                    double height = rect.getHeight() * at.getScaleY();
                    Rectangle2D scaledRect = new Rectangle2D.Double(0, 0, width, height);
                    paint = new TexturePaint(image, scaledRect);
                }

                // debugShape(shape);
                Stroke stroke = ls2d.getStroke();
                if (graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING)
                        == RenderingHints.VALUE_ANTIALIAS_ON) {
                    if (stroke instanceof BasicStroke && strokeWidthAdjustment > 0) {
                        BasicStroke bs = (BasicStroke) stroke;
                        stroke =
                                new BasicStroke(
                                        bs.getLineWidth() + strokeWidthAdjustment,
                                        bs.getEndCap(),
                                        bs.getLineJoin(),
                                        bs.getMiterLimit(),
                                        bs.getDashArray(),
                                        bs.getDashPhase());
                    }
                }

                graphics.setPaint(paint);
                graphics.setStroke(stroke);
                graphics.setComposite(ls2d.getContourComposite());
                graphics.draw(shape);
            }
        }
    }

    /**
     * Paints a GraphicLegend in the supplied graphics
     *
     * @param graphics The graphics in which to draw.
     * @param shape The shape to draw.
     * @param legend The legend to apply.
     * @param symbolScale The scale of the symbol, if the legend graphic has to be rescaled
     */
    public void paint(
            final Graphics2D graphics,
            final LiteShape2 shape,
            final GraphicLegend legend,
            final double symbolScale,
            boolean isLabelObstacle) {
        if (legend == null) {
            // TODO: what's going on? Should not be reached...
            throw new NullPointerException("ShapePainter has been asked to paint a null legend!!");
        }
        Iterator<GraphicalSymbol> symbolIter = legend.graphicalSymbols().iterator();

        while (symbolIter.hasNext()) {

            GraphicalSymbol symbol = symbolIter.next();

            if (symbol instanceof ExternalGraphic) {
                float[] coords = new float[2];
                PathIterator iter = getPathIterator(shape);
                iter.currentSegment(coords);

                // Note: Converting to Radians here due to direct use of SLD Expressions which uses
                // degrees
                double rotation =
                        Math.toRadians(
                                ((Literal) legend.getRotation()).evaluate(null, Double.class));
                float opacity = ((Literal) legend.getOpacity()).evaluate(null, Float.class);
                AlphaComposite composite =
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);

                ExternalGraphic graphic = (ExternalGraphic) symbol;

                while (!(iter.isDone())) {
                    iter.currentSegment(coords);
                    try {
                        BufferedImage image =
                                ImageIOExt.readBufferedImage(
                                        graphic.getOnlineResource().getLinkage().toURL());
                        if ((symbolScale > 0.0) && (symbolScale != 1.0)) {
                            int w = (int) (image.getWidth() / symbolScale);
                            int h = (int) (image.getHeight() / symbolScale);
                            int imageType =
                                    image.getType() == 0
                                            ? BufferedImage.TYPE_4BYTE_ABGR
                                            : image.getType();
                            BufferedImage rescaled = new BufferedImage(w, h, imageType);
                            Graphics2D g = rescaled.createGraphics();
                            g.setRenderingHint(
                                    RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            g.drawImage(
                                    image,
                                    0,
                                    0,
                                    w,
                                    h,
                                    0,
                                    0,
                                    image.getWidth(),
                                    image.getHeight(),
                                    null);
                            g.dispose();
                            image = rescaled;
                        }

                        renderImage(
                                graphics,
                                coords[0],
                                coords[1],
                                -image.getWidth() / 2.0,
                                -image.getHeight() / 2.0,
                                // Doesn't seem to work with SVGs
                                // Looking at the SLDStyleFactory, they get the icon from an
                                // ExternalGraphicFactory.
                                image,
                                rotation,
                                composite,
                                isLabelObstacle);
                    } catch (IOException ex) {
                        Logger.getLogger(StyledShapePainter.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                    iter.next();
                }
            }
        }
    }

    Shape dashShape(Shape shape, Stroke stroke) {
        if (!(stroke instanceof BasicStroke)) {
            return shape;
        }

        BasicStroke bs = (BasicStroke) stroke;
        if (bs.getDashArray() == null || bs.getDashArray().length == 0) {
            return shape;
        }

        return new DashedShape(shape, bs.getDashArray(), bs.getDashPhase());
    }

    /** Extracts a ath iterator from the shape */
    private PathIterator getPathIterator(final LiteShape2 shape) {
        return shape.getPathIterator(IDENTITY_TRANSFORM);
    }

    void debugShape(Shape shape) {
        float[] pt = new float[2];
        PathIterator iter = shape.getPathIterator(null);
        while (!(iter.isDone())) {

            int type = iter.currentSegment(pt);
            String event = "unknown";
            if (type == PathIterator.SEG_CLOSE) event = "SEG_CLOSE";
            if (type == PathIterator.SEG_CUBICTO) event = "SEG_CUBIC";
            if (type == PathIterator.SEG_LINETO) event = "SEG_LINETO";
            if (type == PathIterator.SEG_MOVETO) event = "SEG_MOVETO";
            if (type == PathIterator.SEG_QUADTO) event = "SEG_QUADTO";
            LOGGER.fine(event + " " + pt[0] + "," + pt[1]);
            iter.next();
        }
    }

    // draws the image along the path
    private void drawWithGraphicsStroke(
            Graphics2D graphics, Shape shape, Style2D graphicStroke, boolean isLabelObstacle) {
        PathIterator pi = shape.getPathIterator(null);
        double[] coords = new double[4];
        int type;

        // I suppose the image has been already scaled and its square
        double imageSize;
        double graphicRotation = 0; // rotation in radians
        if (graphicStroke instanceof MarkStyle2D) {
            imageSize = ((MarkStyle2D) graphicStroke).getSize();
            graphicRotation = ((MarkStyle2D) graphicStroke).getRotation();
        } else if (graphicStroke instanceof IconStyle2D) {
            imageSize = ((IconStyle2D) graphicStroke).getIcon().getIconWidth();
            graphicRotation = ((IconStyle2D) graphicStroke).getRotation();
        } else {
            GraphicStyle2D gs = (GraphicStyle2D) graphicStroke;
            imageSize = gs.getImage().getWidth() - gs.getBorder();
            graphicRotation = ((GraphicStyle2D) graphicStroke).getRotation();
        }
        Composite composite = ((PointStyle2D) graphicStroke).getComposite();
        if (composite == null) {
            composite = AlphaComposite.SrcOver;
        }

        double[] first = new double[2];
        double[] previous = new double[2];
        type = pi.currentSegment(coords);
        first[0] = coords[0];
        first[1] = coords[1];
        previous[0] = coords[0];
        previous[1] = coords[1];

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("starting at " + first[0] + "," + first[1]);
        }

        pi.next();

        double remainder, dx, dy, len;
        remainder = imageSize / 2.0;

        while (!pi.isDone()) {
            type = pi.currentSegment(coords);

            switch (type) {
                case PathIterator.SEG_MOVETO:

                    // nothing to do?
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest("moving to " + coords[0] + "," + coords[1]);
                    }

                    first[0] = coords[0];
                    first[1] = coords[1];

                    remainder = imageSize / 2.0;
                    break;

                case PathIterator.SEG_CLOSE:

                    // draw back to first from previous
                    coords[0] = first[0];
                    coords[1] = first[1];
                    remainder = imageSize / 2.0;

                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest(
                                "closing from "
                                        + previous[0]
                                        + ","
                                        + previous[1]
                                        + " to "
                                        + coords[0]
                                        + ","
                                        + coords[1]);
                    }

                    // no break here - fall through to next section
                case PathIterator.SEG_LINETO:

                    // draw from previous to coords
                    if (LOGGER.isLoggable(Level.FINEST)) {
                        LOGGER.finest(
                                "drawing from "
                                        + previous[0]
                                        + ","
                                        + previous[1]
                                        + " to "
                                        + coords[0]
                                        + ","
                                        + coords[1]);
                    }

                    dx = coords[0] - previous[0];
                    dy = coords[1] - previous[1];
                    len = Math.sqrt((dx * dx) + (dy * dy)); // - imageWidth;

                    if (len < remainder) {
                        remainder -= len;
                    } else {
                        double theta = Math.atan2(dx, dy);
                        dx = (Math.sin(theta) * imageSize);
                        dy = (Math.cos(theta) * imageSize);

                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest(
                                    "dx = "
                                            + dx
                                            + " dy "
                                            + dy
                                            + " step = "
                                            + Math.sqrt((dx * dx) + (dy * dy)));
                        }

                        double rotation = -(theta - (Math.PI / 2d));
                        double x = previous[0] + (Math.sin(theta) * remainder);
                        double y = previous[1] + (Math.cos(theta) * remainder);

                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest("len =" + len + " imageSize " + imageSize);
                        }

                        double dist = 0;

                        for (dist = remainder; dist < len; dist += imageSize) {
                            renderGraphicsStroke(
                                    graphics,
                                    x,
                                    y,
                                    graphicStroke,
                                    rotation,
                                    graphicRotation,
                                    composite,
                                    isLabelObstacle);

                            x += dx;
                            y += dy;
                        }
                        remainder = dist - len;

                        if (LOGGER.isLoggable(Level.FINEST)) {
                            LOGGER.finest(
                                    "loop end dist " + dist + " len " + len + " " + (len - dist));
                        }
                    }

                    break;

                default:
                    LOGGER.warning("default branch reached in drawWithGraphicStroke");
            }

            previous[0] = coords[0];
            previous[1] = coords[1];
            pi.next();
        }
    }

    /**
     * Renders an image on the device
     *
     * @param graphics the image location on the screen, x coordinate
     * @param x the image location on the screen, y coordinate
     * @param y the image
     * @param dx TODO
     * @param dy TODO
     * @param image image to draw
     * @param rotation the image rotation in radians
     * @param composite the alpha blending/composition operator
     */
    private void renderImage(
            Graphics2D graphics,
            double x,
            double y,
            double dx,
            double dy,
            BufferedImage image,
            double rotation,
            Composite composite,
            boolean isLabelObstacle) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("drawing Image @" + x + "," + y);
        }

        AffineTransform markAT = new AffineTransform();
        if (ROUND_ICON_COORDS && rotation == 0) {
            // this results in sharper images to be painted
            long tx = Math.round(x + dx);
            long ty = Math.round(y + dy);
            markAT.translate(tx, ty);
        } else {
            markAT.translate(x, y);
            markAT.rotate(rotation);
            markAT.translate(dx, dy);
        }
        if (isLabelObstacle) {
            int w = Math.max(image.getWidth() * 1, 1);
            int h = Math.max(image.getHeight() * 1, 1);

            labelCache.put(new Rectangle2D.Double(x + dx, y + dy, w, h));
        }

        graphics.setComposite(composite);

        Object interpolation = graphics.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        if (interpolation == null) {
            interpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        }
        try {
            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawRenderedImage(image, markAT);
        } finally {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
        }
    }

    private void renderGraphicsStroke(
            Graphics2D graphics,
            double x,
            double y,
            Style2D style,
            double rotation,
            double graphicRotation,
            Composite composite,
            boolean isLabelObstacle) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("drawing GraphicsStroke@" + x + "," + y);
        }

        graphics.setComposite(composite);

        if (style instanceof GraphicStyle2D) {
            GraphicStyle2D gstyle = (GraphicStyle2D) style;
            BufferedImage image = gstyle.getImage();
            double dx = -image.getWidth() * gstyle.getAnchorPointX() + gstyle.getDisplacementX();
            double dy = -image.getHeight() * gstyle.getAnchorPointY() + gstyle.getDisplacementY();
            renderImage(graphics, x, y, dx, dy, image, rotation, composite, isLabelObstacle);
        } else if (style instanceof MarkStyle2D) {
            // almost like the code in the main paint method, but
            // here we don't use the mark composite
            MarkStyle2D ms2d = (MarkStyle2D) style;
            Shape transformedShape =
                    ms2d.getTransformedShape(
                            (float) x, (float) y, (float) rotation, (float) graphicRotation);
            if (transformedShape != null) {
                if (ms2d.getFill() != null) {
                    graphics.setPaint(ms2d.getFill());
                    graphics.fill(transformedShape);
                }

                if (ms2d.getContour() != null) {
                    graphics.setPaint(ms2d.getContour());
                    graphics.setStroke(ms2d.getStroke());
                    graphics.draw(transformedShape);
                }

                if (isLabelObstacle) {
                    labelCache.put(transformedShape.getBounds2D());
                }
            }
        } else if (style instanceof IconStyle2D) {
            IconStyle2D icons = (IconStyle2D) style;
            Icon icon = icons.getIcon();

            AffineTransform markAT = new AffineTransform(graphics.getTransform());
            markAT.translate(x, y);
            markAT.rotate(rotation);

            // the displacement to be applied to all points, centers the icon and applies the
            // Graphic displacement as well
            float dx = -(icon.getIconWidth() * icons.getAnchorPointX()) + icons.getDisplacementX();
            float dy = -(icon.getIconHeight() * icons.getAnchorPointY()) + icons.getDisplacementY();
            markAT.translate(dx, dy);

            AffineTransform temp = graphics.getTransform();
            try {
                graphics.setTransform(markAT);
                icon.paintIcon(null, graphics, 0, 0);
            } finally {
                graphics.setTransform(temp);
            }

            if (isLabelObstacle) {
                labelCache.put(
                        new Rectangle2D.Double(
                                x + dx, y + dy, icon.getIconWidth(), icon.getIconHeight()));
            }
        }
    }

    /**
     * Filling multipolygons might result in holes where two polygons overlap. In this method we
     * work around that by drawing each polygon as a separate shape
     */
    void fillLiteShape(Graphics2D g, LiteShape2 shape) {
        if (shape.getGeometry() instanceof MultiPolygon
                && shape.getGeometry().getNumGeometries() > 1) {
            MultiPolygon mp = (MultiPolygon) shape.getGeometry();
            for (int i = 0; i < mp.getNumGeometries(); i++) {
                Polygon p = (Polygon) mp.getGeometryN(i);
                try {
                    g.fill(new LiteShape2(p, null, null, false, false));
                } catch (Exception e) {
                    // should not really happen, but anyways
                    throw new RuntimeException(
                            "Unexpected error occurred while rendering a multipolygon", e);
                }
            }
        } else {
            g.fill(shape);
        }
    }

    /**
     * Paints a graphic fill for a given shape.
     *
     * @param graphics Graphics2D on which to paint.
     * @param shape Shape whose fill is to be painted.
     * @param graphicFill a Style2D that specified the graphic fill.
     * @param scale the scale of the current render.
     */
    protected void paintGraphicFill(
            Graphics2D graphics, Shape shape, Style2D graphicFill, double scale) {
        // retrieves the bounds of the provided shape
        Rectangle2D boundsShape = shape.getBounds2D();

        // retrieves the size of the stipple to be painted based on the provided graphic fill
        Rectangle2D stippleSize = null;
        if (graphicFill instanceof MarkStyle2D) {
            final MarkStyle2D ms2d = (MarkStyle2D) graphicFill;
            final Shape markShape = ms2d.getShape();
            double size = ms2d.getSize();
            Rectangle2D boundsFill = markShape.getBounds2D();
            double aspect =
                    (boundsFill.getHeight() > 0 && boundsFill.getWidth() > 0)
                            ? boundsFill.getWidth() / boundsFill.getHeight()
                            : 1.0;
            stippleSize = new Rectangle2D.Double(0, 0, size * aspect, size);

            double scaleFactor = size;
            if (boundsFill.getHeight() > 0) {
                scaleFactor = size / boundsFill.getHeight();
            }
            if (OPTIMIZE_VECTOR_HATCH_FILLS) {
                final Shape rescaledStipple =
                        AffineTransform.getScaleInstance(scaleFactor, scaleFactor)
                                .createTransformedShape(markShape);
                ParallelLinesFiller filler = ParallelLinesFiller.fromStipple(rescaledStipple);
                if (filler != null) {
                    Graphics2D clippedGraphics = (Graphics2D) graphics.create();
                    // adds the provided shape to the Graphics current clip region
                    clippedGraphics.clip(shape);
                    LineStyle2D lineStyle = new LineStyle2D();
                    lineStyle.setStroke(ms2d.getStroke());
                    lineStyle.setContour(ms2d.getContour());
                    lineStyle.setContourComposite(ms2d.getContourComposite());
                    lineStyle.setGraphicStroke(ms2d.getGraphicStroke());
                    filler.fillRectangle(shape.getBounds2D(), this, clippedGraphics, lineStyle);
                    return;
                }
            }
        } else if (graphicFill instanceof IconStyle2D) {
            Icon icon = ((IconStyle2D) graphicFill).getIcon();
            stippleSize = new Rectangle2D.Double(0, 0, icon.getIconWidth(), icon.getIconHeight());
        } else if (graphicFill instanceof GraphicStyle2D) {
            BufferedImage image = ((GraphicStyle2D) graphicFill).getImage();
            stippleSize = new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight());
        } else {
            // if graphic fill does not provide bounds information, it is considered
            // to be unsupported for stipple painting
            return;
        }

        // computes the number of times the graphic will be painted as a stipple
        int toX = (int) Math.ceil(boundsShape.getWidth() / stippleSize.getWidth());
        int toY = (int) Math.ceil(boundsShape.getHeight() / stippleSize.getHeight());

        // creates a copy of the Graphics so that we can change it freely
        Graphics2D g = (Graphics2D) graphics.create();
        // adds the provided shape to the Graphics current clip region
        g.clip(shape);
        // retrieves the full clip region
        Shape clipShape = g.getClip();
        Rectangle2D boundsClip = clipShape.getBounds2D();

        // adjust the iteration indexes to avoid iterating a lot over areas that we won't be
        // rendering
        int fromX = 0;
        if (boundsClip.getMinX() > boundsShape.getMinX()) {
            fromX =
                    (int)
                            Math.floor(
                                    (boundsClip.getMinX() - boundsShape.getMinX())
                                            / stippleSize.getWidth());
        }
        if (boundsClip.getMaxX() < boundsShape.getMaxX()) {
            toX -=
                    (int)
                            Math.floor(
                                    (boundsShape.getMaxX() - boundsClip.getMaxX())
                                            / stippleSize.getWidth());
        }

        // adjust the iteration indexes to avoid iterating a lot over areas that we won't be
        // rendering
        int fromY = 0;
        if (boundsClip.getMinY() > boundsShape.getMinY()) {
            fromY =
                    (int)
                            Math.floor(
                                    (boundsClip.getMinY() - boundsShape.getMinY())
                                            / stippleSize.getHeight());
        }
        if (boundsClip.getMaxY() < boundsShape.getMaxY()) {
            toY -=
                    (int)
                            Math.floor(
                                    (boundsShape.getMaxY() - boundsClip.getMaxY())
                                            / stippleSize.getHeight());
        }

        // builds the JTS geometry for the translated stipple
        GeometryFactory geomFactory = new GeometryFactory();
        Coordinate stippleCoord =
                new Coordinate(stippleSize.getCenterX(), stippleSize.getCenterY());
        Geometry stipplePoint = geomFactory.createPoint(stippleCoord);

        // builds a LiteShape2 object from the JTS geometry
        AffineTransform2D identityTransf = new AffineTransform2D(new AffineTransform());
        Decimator nullDecimator = new Decimator(-1, -1);

        // paints graphic fill as a stipple
        for (int i = fromX; i <= toX; i++) {
            for (int j = fromY; j <= toY; j++) {
                // computes this stipple's shift in the X and Y directions
                double translateX = boundsShape.getMinX() + i * stippleSize.getWidth();
                double translateY = boundsShape.getMinY() + j * stippleSize.getHeight();

                // translate the stipple point
                stippleCoord.x = stippleSize.getCenterX() + translateX;
                stippleCoord.y = stippleSize.getCenterY() + translateY;
                stipplePoint.geometryChanged();
                LiteShape2 stippleShape;
                try {
                    stippleShape =
                            new LiteShape2(stipplePoint, identityTransf, nullDecimator, false);
                } catch (Exception e) {
                    throw new RuntimeException("Unxpected exception building lite shape", e);
                }
                paint(g, stippleShape, graphicFill, scale);
            }
        }
    }

    public static class TextureAnchorKey extends Key {
        protected TextureAnchorKey() {
            super(0);
        }

        @Override
        public boolean isCompatibleValue(Object val) {
            return val instanceof Point2D;
        }
    }
}
