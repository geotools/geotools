/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.VendorOptionParser;
import org.geotools.renderer.composite.BlendComposite;
import org.geotools.renderer.composite.BlendComposite.BlendingMode;
import org.geotools.renderer.style.RandomFillBuilder.PositionRandomizer;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacement;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.util.Range;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.style.GraphicalSymbol;

/**
 * Factory object that converts SLD style into rendered styles.
 *
 * @author aaime
 * @author dblasby
 */
public class SLDStyleFactory {
    /** The logger for the rendering module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SLDStyleFactory.class);

    /** Java System Property to enable legacy default anchor point. */
    public static final String USE_LEGACY_ANCHOR_POINT_KEY =
            "org.geotools.renderer.style.legacyAnchorPoint";

    /**
     * The threshold at which we switch from pre-rasterized icons to dynamically painted ones (to
     * avoid OOM)
     */
    private static final int MAX_RASTERIZATION_SIZE = 512;

    /** Holds a lookup bewteen SLD names and java constants. */
    private static final java.util.Map JOIN_LOOKUP = new java.util.HashMap();

    /** Holds a lookup bewteen SLD names and java constants. */
    private static final java.util.Map CAP_LOOKUP = new java.util.HashMap();

    /** Holds a lookup bewteen SLD names and java constants. */
    private static final java.util.Map FONT_STYLE_LOOKUP = new java.util.HashMap();

    /** Holds a lookup bewteen alpha composite names and AlphaComposite constants. */
    private static final java.util.Map<String, Integer> ALPHA_COMPOSITE_LOOKUP =
            new java.util.LinkedHashMap<String, Integer>();

    private static final FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /** This one is used as the observer object in image tracks */
    private static final Canvas obs = new Canvas();

    /**
     * The default size for Marks when a mark is used, but no size is provided (got from the default
     * size to be used for SVGs and other scalable graphics when no size is provided)
     */
    public static final int DEFAULT_MARK_SIZE = 16;

    static { // static block to populate the lookups
        JOIN_LOOKUP.put("miter", new Integer(BasicStroke.JOIN_MITER));
        JOIN_LOOKUP.put("bevel", new Integer(BasicStroke.JOIN_BEVEL));
        JOIN_LOOKUP.put("round", new Integer(BasicStroke.JOIN_ROUND));

        CAP_LOOKUP.put("butt", new Integer(BasicStroke.CAP_BUTT));
        CAP_LOOKUP.put("round", new Integer(BasicStroke.CAP_ROUND));
        CAP_LOOKUP.put("square", new Integer(BasicStroke.CAP_SQUARE));

        FONT_STYLE_LOOKUP.put("normal", new Integer(java.awt.Font.PLAIN));
        FONT_STYLE_LOOKUP.put("italic", new Integer(java.awt.Font.ITALIC));
        FONT_STYLE_LOOKUP.put("oblique", new Integer(java.awt.Font.ITALIC));
        FONT_STYLE_LOOKUP.put("bold", new Integer(java.awt.Font.BOLD));

        ALPHA_COMPOSITE_LOOKUP.put("copy", AlphaComposite.SRC);
        ALPHA_COMPOSITE_LOOKUP.put("destination", AlphaComposite.DST);
        ALPHA_COMPOSITE_LOOKUP.put("source-over", AlphaComposite.SRC_OVER);
        ALPHA_COMPOSITE_LOOKUP.put("destination-over", AlphaComposite.DST_OVER);
        ALPHA_COMPOSITE_LOOKUP.put("source-in", AlphaComposite.SRC_IN);
        ALPHA_COMPOSITE_LOOKUP.put("destination-in", AlphaComposite.DST_IN);
        ALPHA_COMPOSITE_LOOKUP.put("source-out", AlphaComposite.SRC_OUT);
        ALPHA_COMPOSITE_LOOKUP.put("destination-out", AlphaComposite.DST_OUT);
        ALPHA_COMPOSITE_LOOKUP.put("source-atop", AlphaComposite.SRC_ATOP);
        ALPHA_COMPOSITE_LOOKUP.put("destination-atop", AlphaComposite.DST_ATOP);
        ALPHA_COMPOSITE_LOOKUP.put("xor", AlphaComposite.XOR);
    }

    /** Symbolizers that depend on attributes */
    Map dynamicSymbolizers = new SoftValueHashMap();

    /** Symbolizers that do not depend on attributes */
    Map staticSymbolizers = new SoftValueHashMap();

    /** Build a default rendering hint to avoid NPE */
    RenderingHints renderingHints =
            new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);

    /** Whether to turn all line widths less than 1.5 pixels to 0 to speed up line rendering. */
    private boolean lineOptimizationEnabled = false;

    /**
     * Whether to turn on vector rendering or not. Normal behavior is to have it turned off, which
     * is faster but may not be the best thing when printing due to quality loss.
     */
    private boolean vectorRenderingEnabled = false;

    private long hits;

    private long requests;

    /** Holds value of property mapScaleDenominator. */
    private double mapScaleDenominator = Double.NaN;

    /** Helper to parse significant vendor options */
    VendorOptionParser voParser = new VendorOptionParser();

    /** Helper used to build */
    RandomFillBuilder randomFillBuilder = new RandomFillBuilder(voParser, this);

    /**
     * The factory builds a fair number of buffered images to deal with external graphics that need
     * resizing and the like. This hints will be used in those drawing operations.
     */
    public RenderingHints getRenderingHints() {
        return renderingHints;
    }

    public void setRenderingHints(RenderingHints renderingHints) {
        if (renderingHints == null) return;
        this.renderingHints = renderingHints;
    }

    /**
     * Enabled by default, this optimization speeds up line rendering when the line width is less
     * than 1.5 pixels when antialiasing is disblaed. Unfortunately it also prevents fine line width
     * control when antialiasing is enabled. Given that the optimization has been hard coded for
     * more than six years, we give the user control on this one since turning this off will change
     * the rendering of all existing styles using thin line widths.
     */
    public boolean isLineOptimizationEnabled() {
        return lineOptimizationEnabled;
    }

    public void setLineOptimizationEnabled(boolean lineOptimizationEnabled) {
        this.lineOptimizationEnabled = lineOptimizationEnabled;
    }

    /**
     * Indicates whether vector rendering should be preferred when painting graphic fills (e.g.,
     * using a Mark as stipple) or vector Graphic objects such as SVG ExternalGraphics. The default
     * behavior is to be disabled, meaning that graphic fills are painted as raster images using
     * Java TexturePaint, and SVGs are rendered to a BufferedImage prior to painting on the target
     * Graphics. This common behavior is faster and more suitable for on-screen rendering. Enabling
     * this flag is recommended for rendering to off-screen Graphics such as when printing, cases in
     * which the full quality of the original data should normally be preserved.
     */
    public boolean isVectorRenderingEnabled() {
        return vectorRenderingEnabled;
    }

    /**
     * Sets whether vector rendering should be preferred when painting graphic fills (see {@link
     * #isVectorRenderingEnabled()} for more details).
     *
     * @param vectorRenderingEnabled a boolean value indicating whether vector rendering should be
     *     enabled or not.
     */
    public void setVectorRenderingEnabled(boolean vectorRenderingEnabled) {
        this.vectorRenderingEnabled = vectorRenderingEnabled;
    }

    public double getHitRatio() {
        return (double) hits / (double) requests;
    }

    public long getHits() {
        return hits;
    }

    public long getRequests() {
        return requests;
    }

    /**
     * Creates a rendered style
     *
     * <p>Makes use of a symbolizer cache based on identity to avoid recomputing over and over the
     * same style object and to reduce memory usage. The same Style2D object will be returned by
     * subsequent calls using the same feature independent symbolizer with the same scaleRange.
     *
     * @param drawMe The feature
     * @param symbolizer The SLD symbolizer
     * @return A rendered style equivalent to the symbolizer
     */
    public Style2D createStyle(Object drawMe, Symbolizer symbolizer) {
        return createStyle(drawMe, symbolizer, null);
    }

    /**
     * Creates a rendered style
     *
     * <p>Makes use of a symbolizer cache based on identity to avoid recomputing over and over the
     * same style object and to reduce memory usage. The same Style2D object will be returned by
     * subsequent calls using the same feature independent symbolizer with the same scaleRange.
     *
     * @param drawMe The feature
     * @param symbolizer The SLD symbolizer
     * @param scaleRange The scale range in which the feature should be painted according to the
     *     symbolizer
     * @return A rendered style equivalent to the symbolizer
     */
    public Style2D createStyle(Object drawMe, Symbolizer symbolizer, Range scaleRange) {
        Style2D style = null;

        SymbolizerKey key = new SymbolizerKey(symbolizer, scaleRange);
        style = (Style2D) staticSymbolizers.get(key);

        requests++;

        if (style != null) {
            hits++;
        } else {
            style = createStyleInternal(drawMe, symbolizer, scaleRange);

            // for some legitimate cases some styles cannot be turned into a
            // valid Style2D
            // e.g., point symbolizer that contains no graphic that can be used
            // due to network issues
            if (style == null) {
                return null;
            }

            // if known dynamic symbolizer return the style
            if (dynamicSymbolizers.containsKey(key)) {
                return style;
            } else {
                // lets see if it's static or dynamic
                StyleAttributeExtractorTruncated sae = new StyleAttributeExtractorTruncated();
                sae.visit(symbolizer);

                Set nameSet = sae.getAttributeNameSet();
                boolean noAttributes = (nameSet == null) || (nameSet.size() == 0);
                if (noAttributes && !sae.isUsingVolatileFunctions()) {
                    staticSymbolizers.put(key, style);
                } else {
                    dynamicSymbolizers.put(key, Boolean.TRUE);
                }
            }
        }
        return style;
    }

    /**
     * Really creates the symbolizer
     *
     * @param drawMe DOCUMENT ME!
     * @param symbolizer DOCUMENT ME!
     * @param scaleRange DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private Style2D createStyleInternal(Object drawMe, Symbolizer symbolizer, Range scaleRange) {
        Style2D style = null;

        if (symbolizer instanceof PolygonSymbolizer) {
            style = createPolygonStyle(drawMe, (PolygonSymbolizer) symbolizer, scaleRange);
        } else if (symbolizer instanceof LineSymbolizer) {
            style = createLineStyle(drawMe, (LineSymbolizer) symbolizer, scaleRange);
        } else if (symbolizer instanceof PointSymbolizer) {
            style = createPointStyle(drawMe, (PointSymbolizer) symbolizer, scaleRange);
        } else if (symbolizer instanceof TextSymbolizer) {
            style = createTextStyle(drawMe, (TextSymbolizer) symbolizer, scaleRange);
        }

        return style;
    }

    /**
     * Creates a rendered style
     *
     * @param f The feature
     * @param symbolizer The SLD symbolizer
     * @param scaleRange The scale range in which the feature should be painted according to the
     *     symbolizer
     * @return A rendered style equivalent to the symbolizer
     * @throws UnsupportedOperationException if an unknown symbolizer is passed to this method
     */
    public Style2D createDynamicStyle(SimpleFeature f, Symbolizer symbolizer, Range scaleRange) {
        Style2D style = null;

        if (symbolizer instanceof PolygonSymbolizer) {
            style = createDynamicPolygonStyle(f, (PolygonSymbolizer) symbolizer, scaleRange);
        } else if (symbolizer instanceof LineSymbolizer) {
            style = createDynamicLineStyle(f, (LineSymbolizer) symbolizer, scaleRange);
        } else {
            throw new UnsupportedOperationException("This kind of symbolizer is not yet supported");
        }

        return style;
    }

    PolygonStyle2D createPolygonStyle(
            Object feature, PolygonSymbolizer symbolizer, Range scaleRange) {
        PolygonStyle2D style = new PolygonStyle2D();

        setScaleRange(style, scaleRange);
        style.setStroke(getStroke(symbolizer.getStroke(), feature));
        style.setGraphicStroke(
                getGraphicStroke(symbolizer, symbolizer.getStroke(), feature, scaleRange));
        style.setContour(getStrokePaint(symbolizer.getStroke(), feature));
        style.setContourComposite(getStrokeComposite(symbolizer.getStroke(), feature));
        setPolygonStyleFill(feature, style, symbolizer, scaleRange);

        return style;
    }

    /**
     * Sets a polygon style fill, which includes regular color fill, fill composite, and possibly a
     * Style2D fill.
     *
     * @param feature
     * @param style
     * @param symbolizer
     * @param scaleRange
     */
    void setPolygonStyleFill(
            Object feature, PolygonStyle2D style, PolygonSymbolizer symbolizer, Range scaleRange) {
        Fill fill = symbolizer.getFill();
        if (fill == null) return;

        // sets Style2D fill making sure we don't use too much memory for the
        // rasterization
        if (fill.getGraphicFill() != null) {
            double size = evalToDouble(fill.getGraphicFill().getSize(), feature, 0);
            if (isVectorRenderingEnabled() || size > MAX_RASTERIZATION_SIZE) {
                // sets graphic fill if available and vector rendering is
                // enabled
                Style2D style2DFill =
                        createPointStyle(
                                feature, symbolizer, fill.getGraphicFill(), scaleRange, false);
                if (!(style2DFill instanceof GraphicStyle2D)) {
                    style.setGraphicFill(style2DFill);
                    return;
                }
            }
        }
        // otherwise, sets regular fill using Java raster-based Paint objects
        float opacity = 1f;
        if (symbolizer.getFill() != null) {
            opacity = evalOpacity(symbolizer.getFill().getOpacity(), feature);
        }
        Composite composite = getComposite(symbolizer.getOptions(), opacity);
        if (composite == null) {
            composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        }
        style.setFill(getPaint(symbolizer.getFill(), feature, symbolizer));
        style.setFillComposite(composite);
    }

    Style2D createDynamicPolygonStyle(
            SimpleFeature feature, PolygonSymbolizer symbolizer, Range scaleRange) {
        PolygonStyle2D style = new DynamicPolygonStyle2D(feature, symbolizer);

        setScaleRange(style, scaleRange);

        // setStroke(style, symbolizer.getStroke(), feature);
        // setFill(style, symbolizer.getFill(), feature);
        return style;
    }

    Style2D createLineStyle(Object feature, LineSymbolizer symbolizer, Range scaleRange) {
        // see if we have some information about the composite
        float opacity = 1f;
        if (symbolizer.getStroke() != null) {
            opacity = evalOpacity(symbolizer.getStroke().getOpacity(), feature);
        }
        Composite composite = getComposite(symbolizer.getOptions(), opacity);
        if (composite == null) {
            composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        }

        LineStyle2D style = new LineStyle2D();
        setScaleRange(style, scaleRange);
        style.setStroke(getStroke(symbolizer.getStroke(), feature));
        style.setGraphicStroke(
                getGraphicStroke(symbolizer, symbolizer.getStroke(), feature, scaleRange));
        style.setContour(getStrokePaint(symbolizer.getStroke(), feature));
        style.setContourComposite(composite);

        style.setPerpendicularOffset(
                evalToDouble(symbolizer.getPerpendicularOffset(), feature, 0d));

        return style;
    }

    Style2D createDynamicLineStyle(
            SimpleFeature feature, LineSymbolizer symbolizer, Range scaleRange) {
        LineStyle2D style = new DynamicLineStyle2D(feature, symbolizer);
        setScaleRange(style, scaleRange);

        // setStroke(style, symbolizer.getStroke(), feature);
        return style;
    }

    /**
     * Style used to render the provided feature as a point.
     *
     * <p>Depending on the symbolizers used:
     *
     * <ul>
     *   <li>MarkStyle2D
     *   <li>GraphicStyle2D - used to render a glymph
     * </ul>
     *
     * @param feature
     * @param symbolizer
     * @param scaleRange
     * @return
     */
    Style2D createPointStyle(Object feature, PointSymbolizer symbolizer, Range scaleRange) {
        return createPointStyle(feature, symbolizer, symbolizer.getGraphic(), scaleRange, false);
    }

    /**
     * Style used to render the provided feature as a point.
     *
     * <p>Depending on the symbolizers used:
     *
     * <ul>
     *   <li>MarkStyle2D
     *   <li>GraphicStyle2D - used to render a glymph
     * </ul>
     *
     * @param feature
     * @param symbolizer
     * @param scaleRange
     * @return
     */
    Style2D createPointStyle(
            Object feature,
            Symbolizer symbolizer,
            Graphic sldGraphic,
            Range scaleRange,
            boolean forceVector) {
        Style2D retval = null;

        // extract base properties
        float opacity = evalOpacity(sldGraphic.getOpacity(), feature);

        // see if we have some information about the composite
        Composite composite = getComposite(symbolizer.getOptions(), opacity);
        if (composite == null) {
            composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        }

        float displacementX = 0;
        float displacementY = 0;
        Displacement displacement = sldGraphic.getDisplacement();
        if (displacement != null) {
            displacementX = evalToFloat(displacement.getDisplacementX(), feature, 0f);
            displacementY = evalToFloat(displacement.getDisplacementY(), feature, 0f);
        }
        float anchorPointX = 0.5f;
        float anchorPointY = 0.5f;
        AnchorPoint anchorPoint = sldGraphic.getAnchorPoint();
        if (anchorPoint != null) {
            anchorPointX = evalToFloat(anchorPoint.getAnchorPointX(), feature, 0.5f);
            anchorPointY = evalToFloat(anchorPoint.getAnchorPointY(), feature, 0.5f);
        }
        double size = 0;

        // by spec size is optional, and the default value is context dependend,
        // the natural size of the image for an external graphic is the size of
        // the raster,
        // while:
        // - for a external graphic the default size shall be 16x16
        // - for a mark such as star or square the default size shall be 6x6
        try {
            if (sldGraphic.getSize() != null && !Expression.NIL.equals(sldGraphic.getSize()))
                size = evalToDouble(sldGraphic.getSize(), feature, 0);
        } catch (NumberFormatException nfe) {
            // nothing to do
        }

        float rotation = (float) Math.toRadians(evalToDouble(sldGraphic.getRotation(), feature, 0));

        // Extract the sequence of external graphics and symbols and process
        // them in order
        // to recognize which one will be used for rendering
        List<GraphicalSymbol> symbols = sldGraphic.graphicalSymbols();
        if (symbols == null || symbols.isEmpty()) {
            return null;
        }
        final int length = symbols.size();
        ExternalGraphic eg;
        BufferedImage img = null;
        double dsize;
        AffineTransform scaleTx;
        AffineTransformOp ato;
        BufferedImage scaledImage;
        Mark mark;
        Shape shape;
        MarkStyle2D ms2d;
        for (GraphicalSymbol symbol : symbols) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("trying to render symbol " + symbol);
            }
            // try loading external graphic and creating a GraphicsStyle2D
            if (symbol instanceof ExternalGraphic) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("rendering External graphic");
                }
                eg = (ExternalGraphic) symbol;

                // if the icon size becomes too big we switch to vector
                // rendering too, since
                // pre-rasterizing and caching the result will use too much
                // memory
                if (vectorRenderingEnabled || forceVector || size > MAX_RASTERIZATION_SIZE) {
                    Icon icon = getIcon(eg, feature, -1);
                    if (icon == null) {
                        // no icon -> no image either, there is no raster
                        // fallback
                        continue;
                    } else if (icon instanceof ImageIcon) {
                        // when the icon is an image better use the graphic style, we have
                        // better rendering code for it
                        GraphicStyle2D g2d = getGraphicStyle(eg, feature, size, 1);
                        if (g2d != null) {
                            g2d.setRotation(rotation);
                            retval = g2d;
                            break;
                        }
                    } else {
                        if (icon.getIconHeight() != size && size != 0) {
                            double scale = (size) / icon.getIconHeight();
                            icon = new RescaledIcon(icon, scale);
                        }
                        retval = new IconStyle2D(icon, feature);
                        break;
                    }
                } else {
                    GraphicStyle2D g2d = getGraphicStyle(eg, feature, size, 1);
                    if (g2d != null) {
                        g2d.setRotation(rotation);
                        retval = g2d;
                        break;
                    }
                }
            }
            if (symbol instanceof Mark) {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("rendering mark @ PointRenderer " + symbol.toString());
                }
                retval = createMarkStyle((Mark) symbol, feature, symbolizer, size);
                break;
            }
        }

        if (retval == null) {
            // from SLD spec:
            // The default if neither an ExternalGraphic nor a Mark is specified is to use the
            // default mark of a "square" with a 50%-gray fill and a black outline, with a size of 6
            // pixels,
            // unless an explicit Size is specified
            StyleFactory sf = CommonFactoryFinder.getStyleFactory();
            Mark defaultMark =
                    sf.mark(
                            ff.literal("square"),
                            sf.fill(null, ff.literal("#808080"), null),
                            sf.createStroke(ff.literal("#000000"), ff.literal(1)));
            if (size <= 0) {
                size = 6;
            }
            retval = createMarkStyle(defaultMark, feature, symbolizer, size);
        }
        setScaleRange(retval, scaleRange);
        PointStyle2D ps = (PointStyle2D) retval;
        ps.setDisplacementX(displacementX);
        ps.setDisplacementY(displacementY);
        ps.setAnchorPointX(anchorPointX);
        ps.setAnchorPointY(anchorPointY);
        ps.setRotation(rotation);
        ps.setComposite(composite);

        return retval;
    }

    MarkStyle2D createMarkStyle(Mark mark, Object feature, Symbolizer symbolizer, double size) {
        Shape shape = getShape(mark, feature);

        if (shape == null) {
            throw new IllegalArgumentException(
                    "The specified mark " + mark.getWellKnownName() + " was not found!");
        }

        Composite composite = getComposite(symbolizer.getOptions());

        MarkStyle2D ms2d = new MarkStyle2D();
        ms2d.setShape(shape);
        ms2d.setFill(getPaint(mark.getFill(), feature, symbolizer));
        ms2d.setFillComposite(
                composite != null ? composite : getComposite(mark.getFill(), feature));
        ms2d.setStroke(getStroke(mark.getStroke(), feature));
        ms2d.setContour(getStrokePaint(mark.getStroke(), feature));
        ms2d.setContourComposite(
                composite != null ? composite : getStrokeComposite(mark.getStroke(), feature));
        // in case of Mark we don't have a natural size, so we default
        // to 16
        if (size <= 0) {
            size = DEFAULT_MARK_SIZE;
        }
        ms2d.setSize(size);

        return ms2d;
    }

    /**
     * Turns a floating point style into a integer size useful to specify the size of a
     * BufferedImage. Will return 1 between 0 and 1 (0 excluded), will otherwise round the size to
     * integer.
     *
     * @param size
     * @return
     */
    int toImageSize(double size) {
        if (size == -1) {
            return -1;
        }
        if (size > 0 && size < 0.5d) {
            return 1;
        } else {
            return (int) Math.round(size);
        }
    }

    Style2D createTextStyle(Object feature, TextSymbolizer symbolizer, Range scaleRange) {
        TextStyle2D ts2d = new TextStyle2D();
        setScaleRange(ts2d, scaleRange);

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("creating text style");
        }

        String geomName = symbolizer.getGeometryPropertyName();

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("geomName = " + geomName);
        }

        // extract label (from ows5 extensions, we could have the label element
        // empty)
        String label = evalToString(symbolizer.getLabel(), feature, "");

        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("label is " + label);
        }

        ts2d.setLabel(label);

        // get the sequence of fonts to be used and set the first one available
        List<Font> fonts = symbolizer.fonts();
        java.awt.Font[] javaFonts = getFonts(feature, fonts, symbolizer);
        ts2d.setFonts(javaFonts);

        // compute label position, anchor, rotation and displacement
        LabelPlacement placement = symbolizer.getLabelPlacement();
        double anchorX =
                PointPlacement.DEFAULT_ANCHOR_POINT.getAnchorPointX().evaluate(null, Double.class);
        double anchorY =
                PointPlacement.DEFAULT_ANCHOR_POINT.getAnchorPointY().evaluate(null, Double.class);
        double rotation = 0;
        double dispX = 0;
        double dispY = 0;

        // Set the defaults to legacy values if the Java System property is set to true.
        if (Boolean.getBoolean(USE_LEGACY_ANCHOR_POINT_KEY)) {
            anchorX = 0;
            anchorY = 0;
        }

        if (placement instanceof PointPlacement) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setting pointPlacement");
            }

            // compute anchor point and displacement
            PointPlacement p = (PointPlacement) placement;
            if (p.getAnchorPoint() != null) {
                anchorX = evalToDouble(p.getAnchorPoint().getAnchorPointX(), feature, anchorX);
                anchorY = evalToDouble(p.getAnchorPoint().getAnchorPointY(), feature, anchorY);
            }

            if (p.getDisplacement() != null) {
                dispX = evalToDouble(p.getDisplacement().getDisplacementX(), feature, 0);
                dispY = evalToDouble(p.getDisplacement().getDisplacementY(), feature, 0);
                ;
            }

            // rotation
            if ((symbolizer instanceof TextSymbolizer2)
                    && (((TextSymbolizer2) symbolizer).getGraphic() != null)) {
                // don't rotate labels that are being placed on shields.
                rotation = 0.0;
            } else {
                rotation = Math.toRadians(evalToDouble(p.getRotation(), feature, 0));
            }

            ts2d.setPointPlacement(true);
        } else if (placement instanceof LinePlacement) {
            // this code used to really really really really suck, so I removed
            // it!
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("setting pointPlacement");
            }
            ts2d.setPointPlacement(false);
            LinePlacement p = (LinePlacement) placement;
            int displace = evalToInt(p.getPerpendicularOffset(), feature, 0);
            ts2d.setPerpendicularOffset(displace);
        }

        ts2d.setAnchorX(anchorX);
        ts2d.setAnchorY(anchorY);
        ts2d.setRotation((float) rotation);
        ts2d.setDisplacementX(dispX);
        ts2d.setDisplacementY(dispY);

        // setup fill and composite
        ts2d.setFill(getPaint(symbolizer.getFill(), feature, symbolizer));
        ts2d.setComposite(getComposite(symbolizer.getFill(), feature));

        // compute halo parameters
        Halo halo = symbolizer.getHalo();

        if (halo != null) {
            ts2d.setHaloFill(getPaint(halo.getFill(), feature, symbolizer));
            ts2d.setHaloComposite(getComposite(halo.getFill(), feature));
            ts2d.setHaloRadius(evalToFloat(halo.getRadius(), feature, 1));
        }

        Graphic graphicShield = null;
        if (symbolizer instanceof TextSymbolizer2) {
            graphicShield = ((TextSymbolizer2) symbolizer).getGraphic();
            if (graphicShield != null) {
                Style2D shieldStyle =
                        createPointStyle(feature, symbolizer, graphicShield, scaleRange, true);
                ts2d.setGraphic(shieldStyle);
            }
        }

        return ts2d;
    }

    /**
     * Extracts the named geometry from feature. If geomName is null then the feature's default
     * geometry is used. If geomName cannot be found in feature then null is returned.
     *
     * @param feature The feature to find the geometry in
     * @param geomName The name of the geometry to find: null if the default geometry should be
     *     used.
     * @return The geometry extracted from feature or null if this proved impossible.
     */
    private Geometry findGeometry(final Object feature, String geomName) {
        Geometry geom = null;

        if (geomName == null) {
            geomName = ""; // ie default geometry
        }
        PropertyName property = ff.property(geomName);
        return property.evaluate(feature, Geometry.class);
    }

    /**
     * Returns the first font associated to the feature that can be found on the current machine
     *
     * @param feature The feature whose font is to be found
     * @param fonts An array of fonts dependent of the feature, the first that is found on the
     *     current machine is returned
     * @param vendorOptions
     * @return The first of the specified fonts found on this machine (Serif 10 if none found)
     */
    private java.awt.Font[] getFonts(Object feature, List<Font> fonts, TextSymbolizer symbolizer) {

        List<java.awt.Font> result = new ArrayList<>();
        // try to build a font using the full spec
        if (fonts != null) {
            for (Font curr : fonts) {
                for (Expression family : curr.getFamily()) {
                    String requestedFont = evalToString(family, feature, null);
                    java.awt.Font javaFont = FontCache.getDefaultInstance().getFont(requestedFont);

                    if (javaFont != null) {
                        java.awt.Font font = styleFont(feature, curr, javaFont, symbolizer);
                        result.add(font);
                    }
                }
            }
        }

        if (result.isEmpty()) {
            java.awt.Font font = new java.awt.Font("Serif", java.awt.Font.PLAIN, 12);
            if (fonts != null && !fonts.isEmpty()) {
                font = styleFont(feature, fonts.get(0), font, symbolizer);
            } else {
                font = applyKerning(font);
            }
            result.add(font);
        }

        return result.toArray(new java.awt.Font[result.size()]);
    }

    private java.awt.Font applyKerning(java.awt.Font font) {
        return font.deriveFont(
                Collections.singletonMap(TextAttribute.KERNING, TextAttribute.KERNING_ON));
    }

    private java.awt.Font applySpacing(java.awt.Font font, double spacing) {
        // tracking is a percentage of the font size
        double tracking = spacing / font.getSize();
        return font.deriveFont(Collections.singletonMap(TextAttribute.TRACKING, tracking));
    }

    private java.awt.Font styleFont(
            Object feature, Font curr, java.awt.Font javaFont, TextSymbolizer symbolizer) {
        String reqStyle = evalToString(curr.getFontStyle(), feature, null);

        int styleCode;

        if (FONT_STYLE_LOOKUP.containsKey(reqStyle)) {
            styleCode = ((Integer) FONT_STYLE_LOOKUP.get(reqStyle)).intValue();
        } else {
            styleCode = java.awt.Font.PLAIN;
        }

        String reqWeight = evalToString(curr.getFontWeight(), feature, null);

        if ("Bold".equalsIgnoreCase(reqWeight)) {
            styleCode = styleCode | java.awt.Font.BOLD;
        }

        float size = evalToFloat(curr.getSize(), feature, 10);

        // apply basic styling
        javaFont = javaFont.deriveFont(styleCode, size);

        // check vendor options
        boolean kerning =
                voParser.getBooleanOption(
                        symbolizer, TextSymbolizer.KERNING_KEY, TextSymbolizer.DEFAULT_KERNING);
        if (kerning) {
            javaFont = applyKerning(javaFont);
        }
        double spacing =
                voParser.getDoubleOption(
                        symbolizer,
                        TextSymbolizer.CHAR_SPACING_KEY,
                        TextSymbolizer.DEFAULT_CHAR_SPACING);
        if (spacing != 0) {
            javaFont = applySpacing(javaFont, spacing);
        }

        return javaFont;
    }

    void setScaleRange(Style style, Range scaleRange) {
        if (scaleRange != null) {
            double min = ((Number) scaleRange.getMinValue()).doubleValue();
            double max = ((Number) scaleRange.getMaxValue()).doubleValue();
            style.setMinMaxScale(min, max);
        }
    }

    // Builds an image version of the graphics with the proper size, no further
    // scaling will
    // be needed during rendering
    private Style2D getGraphicStroke(
            Symbolizer symbolizer,
            org.geotools.styling.Stroke stroke,
            Object feature,
            Range scaleRange) {
        if ((stroke == null) || (stroke.getGraphicStroke() == null)) {
            return null;
        }

        // sets graphic stroke if available and vector rendering is enabled
        return createPointStyle(feature, symbolizer, stroke.getGraphicStroke(), scaleRange, false);
    }

    Stroke getStroke(org.geotools.styling.Stroke stroke, Object feature) {
        if (stroke == null) {
            return null;
        }

        // resolve join type into a join code
        String joinType;
        int joinCode;

        joinType = evalToString(stroke.getLineJoin(), feature, "miter");

        if (JOIN_LOOKUP.containsKey(joinType)) {
            joinCode = ((Integer) JOIN_LOOKUP.get(joinType)).intValue();
        } else {
            joinCode = java.awt.BasicStroke.JOIN_MITER;
        }

        // resolve cap type into a cap code
        String capType;
        int capCode;

        capType = evalToString(stroke.getLineCap(), feature, "square");

        if (CAP_LOOKUP.containsKey(capType)) {
            capCode = ((Integer) CAP_LOOKUP.get(capType)).intValue();
        } else {
            capCode = java.awt.BasicStroke.CAP_SQUARE;
        }

        // get the other properties needed for the stroke
        float[] dashes = evaluateDashArray(stroke, feature);
        float width = evalToFloat(stroke.getWidth(), feature, 1);
        float dashOffset = evalToFloat(stroke.getDashOffset(), feature, 0);

        // Simple optimization: let java2d use the fast drawing path if the line
        // width
        // is small enough...
        if (width < 1.5 & lineOptimizationEnabled) {
            width = 0;
        }

        // now set up the stroke
        BasicStroke stroke2d;

        if ((dashes != null) && (dashes.length > 0)) {
            stroke2d = new BasicStroke(width, capCode, joinCode, 1, dashes, dashOffset);
        } else {
            stroke2d = new BasicStroke(width, capCode, joinCode, 1);
        }

        return stroke2d;
    }

    public static float[] evaluateDashArray(org.geotools.styling.Stroke stroke, Object feature)
            throws NumberFormatException {
        if (stroke.dashArray() != null && !stroke.dashArray().isEmpty()) {
            List<Float> dashArrayValues = new ArrayList<>();
            for (Expression expression : stroke.dashArray()) {
                dashArrayValues.addAll(dashArrayExpressionToFloatList(expression, feature));
            }
            float[] dashArray = new float[dashArrayValues.size()];
            for (int i = 0; i < dashArray.length; i++) {
                dashArray[i] = dashArrayValues.get(i);
            }
            return dashArray;
        }
        return null;
    }

    private static final Pattern WHITE_SPACE_SPLIT = Pattern.compile("\\s+");

    private static List<Float> dashArrayExpressionToFloatList(
            Expression expression, Object feature) {
        String dashString = expression.evaluate(feature, String.class);
        if (dashString == null || dashString.isEmpty()) {
            return Collections.emptyList();
        }
        List<Float> dashValues = new ArrayList<>();
        for (String dashItem : WHITE_SPACE_SPLIT.split(dashString)) {
            dashValues.add(Float.parseFloat(dashItem));
        }
        return dashValues;
    }

    private Paint getStrokePaint(org.geotools.styling.Stroke stroke, Object feature) {
        if (stroke == null) {
            return null;
        }

        // the foreground color
        Paint contourPaint = evalToColor(stroke.getColor(), feature, Color.BLACK);

        // if a graphic fill is to be used, prepare the paint accordingly....
        org.geotools.styling.Graphic gr = stroke.getGraphicFill();

        if (gr != null && gr.graphicalSymbols() != null && gr.graphicalSymbols().size() > 0) {
            contourPaint = getTexturePaint(gr, feature, null);
        }

        return contourPaint;
    }

    private Composite getStrokeComposite(org.geotools.styling.Stroke stroke, Object feature) {
        if (stroke == null) {
            return null;
        }

        // get the opacity and prepare the composite
        float opacity = evalOpacity(stroke.getOpacity(), feature);
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);

        return composite;
    }

    protected Paint getPaint(Fill fill, Object feature, Symbolizer symbolizer) {
        if (fill == null) {
            return null;
        }

        // get fill color
        Paint fillPaint = evalToColor(fill.getColor(), feature, null);

        // if a graphic fill is to be used, prepare the paint accordingly....
        org.geotools.styling.Graphic gr = fill.getGraphicFill();

        if (gr != null && gr.graphicalSymbols() != null && gr.graphicalSymbols().size() > 0) {
            fillPaint = getTexturePaint(gr, feature, symbolizer);
        }

        return fillPaint;
    }

    /**
     * Computes the Composite equivalent to the opacity in the SLD Fill
     *
     * @param fill
     * @param feature
     */
    protected Composite getComposite(Fill fill, Object feature) {
        if (fill == null) {
            return null;
        }

        // get the opacity and prepare the composite
        float opacity = evalOpacity(fill.getOpacity(), feature);
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);

        return composite;
    }

    TexturePaint getTexturePaint(
            org.geotools.styling.Graphic gr, Object feature, Symbolizer symbolizer) {
        // -1 to have the image use its natural size if none was provided by the
        // user
        double graphicSize = evalToDouble(gr.getSize(), feature, -1);

        // search for the first valid external graphic or mark
        Icon icon = null;
        Mark mark = null;
        Shape shape = null;
        for (GraphicalSymbol symbol : gr.graphicalSymbols()) {
            if (symbol instanceof ExternalGraphic) {
                ExternalGraphic eg = (ExternalGraphic) symbol;
                icon = getIcon(eg, feature, graphicSize);
                if (icon != null) {
                    break;
                }
            } else if (symbol instanceof Mark) {
                mark = (Mark) symbol;
                shape = getShape(mark, feature);
                if (shape != null) {
                    break;
                }
            }
        }

        if (icon == null && shape == null) {
            return null;
        }

        // grab the major vendor options
        PositionRandomizer randomizer =
                (PositionRandomizer)
                        voParser.getEnumOption(symbolizer, "random", PositionRandomizer.NONE);

        BufferedImage image;
        if (randomizer != null && randomizer != PositionRandomizer.NONE) {
            // we pass the mark instead of turning it into an image as an attempt to get
            // a higher quality result, if we turn it into an image and then start moving/rotating
            // it around we're going to antialias it more than once
            image =
                    randomFillBuilder.buildRandomTilableImage(
                            symbolizer, gr, icon, mark, shape, graphicSize, feature);
        } else {
            int[] margin = voParser.getGraphicMargin(symbolizer, "graphic-margin");
            GraphicStyle2D gs = getGraphicStyle(icon, 1);
            if (gs != null) {
                image = gs.getImage();
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("got an image in graphic fill");
                }
            } else {
                if (LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("going for the mark from graphic fill");
                }

                image = markToTilableImage(gr, feature, mark, shape);
            }

            if (margin != null) {
                // apply the margin around the image
                int extraY = margin[0] + margin[2];
                int extraX = margin[1] + margin[3];
                int type = image.getType() == 0 ? BufferedImage.TYPE_4BYTE_ABGR : image.getType();
                BufferedImage imageWithMargin =
                        new BufferedImage(
                                image.getWidth() + extraX, image.getHeight() + extraY, type);
                int tx = margin[1];
                int ty = margin[0];
                AffineTransform at = AffineTransform.getTranslateInstance(tx, ty);
                Graphics2D graphics = imageWithMargin.createGraphics();
                graphics.drawRenderedImage(image, at);
                graphics.dispose();

                image = imageWithMargin;
            }
        }

        // turn the image into a texture paint
        Rectangle2D.Double rect =
                new Rectangle2D.Double(0.0, 0.0, image.getWidth(), image.getHeight());
        TexturePaint imagePaint = new TexturePaint(image, rect);
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer("applied TexturePaint " + imagePaint);
        }

        return imagePaint;
    }

    private BufferedImage markToTilableImage(
            org.geotools.styling.Graphic gr, Object feature, Mark mark, Shape shape) {
        BufferedImage image;
        Rectangle2D shapeBounds = shape.getBounds2D();

        // The aspect ratio is the relation between the width and height of
        // this mark (x width units per y height units or width/height). The
        // aspect ratio is used to render non isometric sized marks (where
        // width != height). To discover the <code>width</code> of a non
        // isometric
        // mark, simply calculate <code>height * aspectRatio</code>, where
        // height is given by getSize().
        double shapeAspectRatio =
                (shapeBounds.getHeight() > 0 && shapeBounds.getWidth() > 0)
                        ? shapeBounds.getWidth() / shapeBounds.getHeight()
                        : 1.0;

        double size = evalToDouble(gr.getSize(), feature, 16);
        final double sizeX = size * shapeAspectRatio; // apply the aspect
        // ratio to fix the
        // sample's width.
        final double sizeY = size;

        // we need to paint the mark in a 3x3 grid to account for border effects
        // due to antialiasing (e.g., even if the mark is 10 pixels wide, due to the
        // antialiasing graphically it occupies 12 or so pixels)

        // check if this will cause an overflow in the image creation GEOT-5878
        int repeat = 3;
        if ((sizeX * repeat) * (sizeY * repeat) > Integer.MAX_VALUE) {
            LOGGER.warning("Size of graphic (" + sizeX + " * " + sizeY + ") is too large");
            if (sizeX * sizeY > Integer.MAX_VALUE) {
                LOGGER.severe(
                        "Size of graphic ("
                                + sizeX
                                + " * "
                                + sizeY
                                + ") is too large will not draw");
                return new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
            } else {
                LOGGER.fine(
                        "Size of metatiled graphic ("
                                + (repeat * sizeX)
                                + " * "
                                + (repeat * sizeY)
                                + ") is too large, not metatiling it");
                // try without meta-tiling
                repeat = 1;
            }
        }
        image =
                new BufferedImage(
                        (int) Math.ceil(sizeX * repeat),
                        (int) Math.ceil(sizeY * repeat),
                        BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHints(renderingHints);
        double rotation =
                Math.toRadians(evalToDouble(gr.getRotation(), feature, 0.0)); // fix for GEOS-6217
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                double tx = sizeX * (repeat / 2.0) + sizeX * i;
                double ty = sizeY * (repeat / 2.0) + sizeY * j;
                fillDrawMark(g2d, tx, ty, mark, size, rotation, feature);
            }
        }
        g2d.dispose();

        int iSizeX = (int) Math.floor(sizeX);
        int iSizeY = (int) Math.floor(sizeY);
        // updated to use the new sizes
        image = image.getSubimage(iSizeX, iSizeY, Math.max(iSizeX, 1), Math.max(iSizeY, 1));
        return image;
    }

    /**
     * Tries to parse the provided external graphic into a BufferedImage.
     *
     * @param eg
     * @param feature
     * @param size
     * @return the image, or null if the external graphics could not be interpreted
     */
    private GraphicStyle2D getGraphicStyle(
            ExternalGraphic eg, Object feature, double size, int border) {
        Icon icon = getIcon(eg, feature, toImageSize(size));
        return getGraphicStyle(icon, border);
    }

    /**
     * Converts an icon into a {@link GraphicStyle2D}
     *
     * @param icon
     * @param border
     * @return
     */
    private GraphicStyle2D getGraphicStyle(Icon icon, int border) {
        if (icon == null) {
            return null;
        }
        // optimization, if this is an IconImage based on a BufferedImage,
        // just return the
        // wrapped one
        if (icon instanceof ImageIcon) {
            ImageIcon img = (ImageIcon) icon;
            if (img.getImage() instanceof BufferedImage) {
                // return the image as is, no border
                BufferedImage image = (BufferedImage) img.getImage();
                return new GraphicStyle2D(image, 0, 0);
            }
        }

        // otherwise have the icon draw itself on a BufferedImage
        BufferedImage result =
                new BufferedImage(
                        icon.getIconWidth() + border * 2,
                        icon.getIconHeight() + border * 2,
                        BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) result.getGraphics();
        g.setRenderingHints(renderingHints);
        icon.paintIcon(null, g, 1, 1);
        g.dispose();

        return new GraphicStyle2D(result, 0, border);
    }

    /**
     * Tries to parse the provided external graphic into an Icon
     *
     * @param eg
     * @param feature
     * @param size
     * @return the image, or null if the external graphics could not be interpreted
     */
    private Icon getIcon(ExternalGraphic eg, Object feature, double size) {
        if (eg == null) return null;

        Icon inlineContent = eg.getInlineContent();
        if (inlineContent != null) {
            return inlineContent;
        }

        // extract the url
        String strLocation;
        try {
            strLocation = eg.getLocation().toExternalForm();
        } catch (MalformedURLException e) {
            LOGGER.log(Level.INFO, "Malformed URL processing external graphic", e);
            return null;
        }
        // parse the eventual ${cqlExpression} embedded in the URL
        Expression location;
        try {
            location = ExpressionExtractor.extractCqlExpressions(strLocation);
        } catch (IllegalArgumentException e) {
            // in the unlikely event that a URL is using one of the chars
            // reserved for ${cqlExpression}
            // let's try and use the location as a literal
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Could not parse cql expressions out of " + strLocation, e);
            location = ff.literal(strLocation);
        }

        // scan the external graphic factories and see which one can be used
        Iterator<ExternalGraphicFactory> it =
                DynamicSymbolFactoryFinder.getExternalGraphicFactories(new Hints(renderingHints));
        while (it.hasNext()) {
            ExternalGraphicFactory egf = it.next();
            try {
                String format = null;
                if (eg.getFormat() != null) {
                    Expression formatExpression =
                            ExpressionExtractor.extractCqlExpressions(eg.getFormat());
                    format = formatExpression.evaluate(feature, String.class);
                }
                Icon icon = egf.getIcon((Feature) feature, location, format, toImageSize(size));
                if (icon != null) {
                    return icon;
                }
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Error occurred evaluating external graphic", e);
            }
        }

        return null;
    }

    /**
     * Given a mark and a feature, returns the Shape provided by the first {@link MarkFactory} that
     * was able to handle the Mark
     *
     * @param mark
     * @param feature
     * @return
     */
    private Shape getShape(Mark mark, Object feature) {
        if (mark == null) return null;

        // handle the TTF references in SE 1.1
        // TODO: generalize it to a pluggable ExternalMarkFactory when other types
        // of indexed marks show up in the wild
        if (mark.getExternalMark() != null) {
            Shape shape = TTFMarkFactory.INSTANCE.getShape(mark.getExternalMark());
            if (shape != null) {
                return shape;
            }
        }

        Expression name = mark.getWellKnownName();
        // expand eventual cql expressions embedded in the name
        if (name instanceof Literal) {
            String expression = evalToString(name, null, null);
            if (expression != null) name = ExpressionExtractor.extractCqlExpressions(expression);
        }

        Iterator<MarkFactory> it = DynamicSymbolFactoryFinder.getMarkFactories();
        while (it.hasNext()) {
            MarkFactory factory = it.next();
            try {
                Shape shape = factory.getShape(null, name, (Feature) feature);
                if (shape != null) return shape;
            } catch (Exception e) {
                LOGGER.log(
                        Level.FINE,
                        "Exception while scanning for " + "the appropriate mark factory",
                        e);
            }
        }

        return null;
    }
    /**
     * @param g2d graphics context
     * @param tx x offset
     * @param ty y offset
     * @param mark mark used for fill pattern
     * @param size size of mark
     * @param rotation rotation in radians
     * @param feature feature used for expression evaulation
     */
    void fillDrawMark(
            Graphics2D g2d,
            double tx,
            double ty,
            Mark mark,
            double size,
            double rotation,
            Object feature) {
        if (mark == null) return;

        Shape originalShape = getShape(mark, feature);

        // rescale and reposition the original shape so it's centered at tx, ty
        // and has the desired size
        AffineTransform markAT = new AffineTransform();
        markAT.translate(tx, ty);
        markAT.rotate(rotation);
        markAT.scale(size, -size);

        // resize/rotate/rescale the shape
        Shape shape = markAT.createTransformedShape(originalShape);

        // cascade the rendering hints configured in the renderer
        g2d.setRenderingHints(renderingHints);

        if (mark.getFill() != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("applying fill to mark");
            }

            g2d.setPaint(getPaint(mark.getFill(), feature, null));
            g2d.setComposite(getComposite(mark.getFill(), feature));
            g2d.fill(shape);
        }

        if (mark.getStroke() != null) {
            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("applying stroke to mark");
            }

            g2d.setPaint(getStrokePaint(mark.getStroke(), feature));
            g2d.setComposite(getStrokeComposite(mark.getStroke(), feature));
            g2d.setStroke(getStroke(mark.getStroke(), feature));
            g2d.draw(shape);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param joinType DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static int lookUpJoin(String joinType) {
        if (SLDStyleFactory.JOIN_LOOKUP.containsKey(joinType)) {
            return ((Integer) JOIN_LOOKUP.get(joinType)).intValue();
        } else {
            return java.awt.BasicStroke.JOIN_MITER;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param capType DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public static int lookUpCap(String capType) {
        if (SLDStyleFactory.CAP_LOOKUP.containsKey(capType)) {
            return ((Integer) CAP_LOOKUP.get(capType)).intValue();
        } else {
            return java.awt.BasicStroke.CAP_SQUARE;
        }
    }

    /**
     * Looks up the composite from the vendor options, or returns null if no composite operation has
     * been specified in the options
     *
     * @param options
     * @return
     */
    public static Composite getComposite(Map<String, String> options) {
        return getComposite(options, 1f);
    }

    /**
     * Looks up the composite from the vendor options, or returns null if no composite operation has
     * been specified in the options
     *
     * @param options
     * @return
     */
    public static Composite getComposite(Map<String, String> options, float defaultOpacity) {
        // get the spec, if no spec, no composite
        String spec = options.get(FeatureTypeStyle.COMPOSITE);
        if (spec == null) {
            return null;
        }

        // parse name or name,opacity
        String name;
        float opacity = defaultOpacity;
        if (spec.indexOf(',') != -1) {
            String[] split = spec.split("\\s*,\\s*");
            if (split.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid syntax for "
                                + FeatureTypeStyle.COMPOSITE
                                + " key, expecting 'name' or 'name,opacity' but got "
                                + spec);
            }
            name = split[0].trim();
            boolean invalidOpacity = false;
            try {
                opacity = Float.parseFloat(split[1]);
            } catch (NumberFormatException e) {
                invalidOpacity = true;
            }
            if (invalidOpacity || opacity < 0 || opacity > 1) {
                throw new IllegalArgumentException(
                        "Invalid value for composite opacity, expecting a number between 0 and 1, but got '"
                                + split[1]
                                + "' instead");
            }
        } else {
            name = spec;
        }

        // lookup the composition operation, see if it's an alpha composite
        if (ALPHA_COMPOSITE_LOOKUP.containsKey(name)) {
            Integer rule = ALPHA_COMPOSITE_LOOKUP.get(name);
            return AlphaComposite.getInstance(rule, opacity);
        }

        // see if it's a blending mode instead
        BlendingMode blend = BlendingMode.lookupByName(name);
        if (blend == null) {
            // ok, this is unknown then
            throw new IllegalArgumentException(
                    "Invalid composite name, not part of the supported alpha composite ones "
                            + ALPHA_COMPOSITE_LOOKUP.keySet()
                            + ", nor the blending ones "
                            + BlendingMode.getStandardNames());
        }
        return BlendComposite.getInstance(blend, opacity);
    }

    /**
     * Returns the sorting directions found in the feature type style
     *
     * @param options
     * @return
     */
    public static SortBy[] getSortBy(Map<String, String> options) {
        // get the spec, if no spec, no sorting
        String sortBySpec = options.get(FeatureTypeStyle.SORT_BY);
        if (sortBySpec == null) {
            return null;
        }

        // parse it
        List<SortBy> result = new ArrayList<>();
        String[] attributeSpecs = sortBySpec.split("\\s*,\\s*");
        for (String attributeSpec : attributeSpecs) {
            String[] items = attributeSpec.split("\\s+");
            if (items.length < 1 || items.length > 2) {
                throw new IllegalArgumentException(
                        "Invalid sortBy specification, it should "
                                + "be either in the form 'attribute' or 'attribute direction' "
                                + "but instead it is: '"
                                + attributeSpec
                                + "'");
            }
            String attribute = items[0];
            SortOrder order = SortOrder.ASCENDING;
            if (items.length == 2) {
                String direction = items[1];
                if ("D".equalsIgnoreCase(direction) || "DESC".equalsIgnoreCase(direction)) {
                    order = SortOrder.DESCENDING;
                } else if (!"A".equalsIgnoreCase(direction) || "ASC".equalsIgnoreCase(direction)) {
                    throw new IllegalArgumentException(
                            "Unknown sort order '" + direction + "' in: '" + attributeSpec + "'");
                }
            }
            SortBy sort = ff.sort(attribute, order);
            result.add(sort);
        }

        return result.toArray(new SortBy[result.size()]);
    }

    /**
     * Getter for property mapScaleDenominator.
     *
     * @return Value of property mapScaleDenominator.
     */
    public double getMapScaleDenominator() {

        return this.mapScaleDenominator;
    }

    /**
     * Setter for property mapScaleDenominator.
     *
     * @param mapScaleDenominator New value of property mapScaleDenominator.
     */
    public void setMapScaleDenominator(double mapScaleDenominator) {

        this.mapScaleDenominator = mapScaleDenominator;
    }

    /**
     * Simple key used to cache Style2D objects based on the originating symbolizer and scale range.
     * Will compare symbolizers by identity, avoiding a possibly expensive comparison among their
     * contents
     *
     * @author aaime
     */
    static class SymbolizerKey {
        private Symbolizer symbolizer;
        private double minScale;
        private double maxScale;

        public SymbolizerKey(Symbolizer symbolizer, Range scaleRange) {
            this.symbolizer = symbolizer;
            if (scaleRange == null) {
                minScale = 0;
                maxScale = Double.POSITIVE_INFINITY;
            } else {
                minScale = ((Number) scaleRange.getMinValue()).doubleValue();
                maxScale = ((Number) scaleRange.getMaxValue()).doubleValue();
            }
        }

        /** @see java.lang.Object#equals(java.lang.Object) */
        public boolean equals(Object obj) {
            if (!(obj instanceof SymbolizerKey)) {
                return false;
            }

            SymbolizerKey other = (SymbolizerKey) obj;

            return (other.symbolizer == symbolizer)
                    && (other.minScale == minScale)
                    && (other.maxScale == maxScale);
        }

        /** @see java.lang.Object#hashCode() */
        public int hashCode() {
            return ((((17 + System.identityHashCode(symbolizer)) * 37) + doubleHash(minScale)) * 37)
                    + doubleHash(maxScale);
        }

        private int doubleHash(double value) {
            long bits = Double.doubleToLongBits(value);

            return (int) (bits ^ (bits >>> 32));
        }
    }

    private String evalToString(Expression exp, Object f, String fallback) {
        if (exp == null) {
            return fallback;
        }
        String s = exp.evaluate(f, String.class);
        if (s != null) {
            return s;
        }
        return fallback;
    }

    private float evalToFloat(Expression exp, Object f, float fallback) {
        if (exp == null) {
            return fallback;
        }
        Float fo = exp.evaluate(f, Float.class);
        if (fo != null) {
            return fo.floatValue();
        }
        return fallback;
    }

    private double evalToDouble(Expression exp, Object f, double fallback) {
        if (exp == null) {
            return fallback;
        }
        Double d = exp.evaluate(f, Double.class);
        if (d != null) {
            return d.doubleValue();
        }
        return fallback;
    }

    private int evalToInt(Expression exp, Object f, int fallback) {
        if (exp == null) {
            return fallback;
        }
        Integer i = exp.evaluate(f, Integer.class);
        if (i != null) {
            return i.intValue();
        }
        return fallback;
    }

    private Color evalToColor(Expression exp, Object f, Color fallback) {
        if (exp == null) {
            return fallback;
        }
        Color color = exp.evaluate(f, Color.class);
        if (color != null) {
            return color;
        }
        return fallback;
    }

    private float evalOpacity(Expression e, Object f) {
        return evalToFloat(e, f, 1);
    }
}
