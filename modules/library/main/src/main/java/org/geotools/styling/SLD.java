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
package org.geotools.styling;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.*;
import org.geotools.data.DataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Filters;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Utility class that provides static helper methods for common operations on GeoTools styling
 * objects (e.g. StyledLayerDescriptor, Style, FeatureTypeStyle, Rule, Symbolizer, Stroke and Fill).
 *
 * @author Jody Garnett
 * @version $Id$
 */
public class SLD {

    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /** <code>NOTFOUND</code> indicates int value was unavailable */
    public static final int NOTFOUND = Filters.NOTFOUND;

    public static final double ALIGN_LEFT = 1.0;
    public static final double ALIGN_CENTER = 0.5;
    public static final double ALIGN_RIGHT = 0.0;
    public static final double ALIGN_BOTTOM = 1.0;
    public static final double ALIGN_MIDDLE = 0.5;
    public static final double ALIGN_TOP = 0.0;

    /**
     * Retrieve linestring color from linesymbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     * @return Color of linestring, or null if unavailable.
     */
    public static Color lineColor(LineSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        return color(stroke);
    }

    /**
     * Retrieve the color of a stroke object
     *
     * @param stroke a Stroke object
     * @return color or null if stroke was null
     */
    public static Color color(StrokeImpl stroke) {
        if (stroke == null) {
            return null;
        }
        return color(stroke.getColor());
    }

    /**
     * Retrieve the color of a fill object
     *
     * @param fill a Fill object
     * @return color or null if fill was null
     */
    public static Color color(FillImpl fill) {
        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Updates the color for line symbolizers in the current style.
     *
     * <p>This method will update the Style in place; some of the symbolizers will be replaced with
     * modified copies.
     *
     * @param style the Style object to be updated
     * @param colour Color to to use
     */
    public static void setLineColour(StyleImpl style, final Color colour) {
        if (style == null) {
            return;
        }
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            for (int i = 0; i < featureTypeStyle.rules().size(); i++) {
                Rule rule =  featureTypeStyle.rules().get(i);
                DuplicatingStyleVisitor update =
                        new DuplicatingStyleVisitor() {

                            StrokeImpl update(StrokeImpl stroke) {
                                Expression color = ff.literal(colour);
                                Expression width = copy(stroke.getWidth());
                                Expression opacity = copy(stroke.getOpacity());
                                Expression lineJoin = copy(stroke.getLineJoin());
                                Expression lineCap = copy(stroke.getLineCap());
                                float[] dashArray = copy(stroke.getDashArray());
                                Expression dashOffset = copy(stroke.getDashOffset());
                                Graphic graphicStroke = copy(stroke.getGraphicStroke());
                                Graphic graphicFill = copy(stroke.getGraphicFill());
                                return (StrokeImpl)
                                        sf.createStroke(
                                                color,
                                                width,
                                                opacity,
                                                lineJoin,
                                                lineCap,
                                                dashArray,
                                                dashOffset,
                                                graphicFill,
                                                graphicStroke);
                            }
                        };
                rule.accept(update);
                RuleImpl updatedRule = (RuleImpl) update.getCopy();
                featureTypeStyle.rules().set(i, updatedRule);
            }
        }
    }

    /** Sets the Colour for the given Line symbolizer */
    public static void setLineColour(LineSymbolizerImpl symbolizer, Color colour) {
        if (symbolizer == null || colour == null) {
            return;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        if (stroke == null) {
            stroke = sf.createStroke(ff.literal(colour), ConstantStroke.DEFAULT.getWidth());
            symbolizer.setStroke(stroke);

        } else {
            stroke.setColor(ff.literal(colour));
        }
    }

    /**
     * Retrieve color from linesymbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     * @return Color of linestring, or null if unavailable.
     */
    public static Color color(LineSymbolizerImpl symbolizer) {
        return lineColor(symbolizer);
    }

    /**
     * Retrieve linestring width from symbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     * @return width of linestring, or NOTFOUND
     */
    public static int lineWidth(LineSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        return width(stroke);
    }

    /**
     * Retrieve the width of a Stroke object.
     *
     * @param stroke the Stroke object.
     * @return width or {@linkplain #NOTFOUND} if not available.
     */
    public static int width(StrokeImpl stroke) {
        if (stroke == null) {
            return NOTFOUND;
        }

        return Filters.asInt(stroke.getWidth());
    }

    /**
     * Retrieve the size of a Mark object
     *
     * @param mark the Mark object
     * @return size or {@linkplain #NOTFOUND} if not available
     */
    public static int size(MarkImpl mark) {
        return NOTFOUND;
    }

    /**
     * Retrieve linestring width from symbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     * @return width or {@linkplain #NOTFOUND} if not available
     */
    public static int width(LineSymbolizerImpl symbolizer) {
        return lineWidth(symbolizer);
    }

    /**
     * Retrieve the opacity from a LineSymbolizer object.
     *
     * @param symbolizer Line symbolizer information.
     * @return double of the line symbolizer's opacity, or NaN if unavailable.
     */
    public static double lineOpacity(LineSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        return opacity(stroke);
    }

    /**
     * Retrieve the opacity from a Stroke object.
     *
     * @param stroke the Stroke object.
     * @return double of the line stroke's opacity, or NaN if unavailable.
     */
    public static double opacity(StrokeImpl stroke) {
        if (stroke == null) {
            return Double.NaN;
        }
        return opacity(stroke.getOpacity());
    }

    /**
     * Retrieve the opacity from a RasterSymbolizer object.
     *
     * @param rasterSymbolizer raster symbolizer information.
     * @return double of the raster symbolizer's opacity, or 1.0 if unavailable.
     */
    public static double opacity(RasterSymbolizerImpl rasterSymbolizer) {
        if (rasterSymbolizer == null) {
            return 1.0;
        }
        return opacity(rasterSymbolizer.getOpacity());
    }

    /**
     * Retrieve the opacity value of an Expression.
     *
     * @param opacity Expression object
     * @returna double value or 1.0 if unavailable.
     */
    private static double opacity(Expression opacity) {
        if (opacity == null) {
            return 1.0;
        }
        double value = Filters.asDouble(opacity);
        return (Double.isNaN(value) ? 1.0 : value);
    }

    /**
     * Retrieves the linejoin from a LineSymbolizer.
     *
     * @param symbolizer Line symbolizer information.
     * @return String of the linejoin, or null if unavailable.
     */
    public static String lineLinejoin(LineSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        if (stroke == null) {
            return null;
        }

        Expression linejoinExp = stroke.getLineJoin();

        return linejoinExp.toString();
    }

    /**
     * Retrieves the line cap from a LineSymbolizer.
     *
     * @param symbolizer Line symbolizer information.
     * @return String of the line stroke's line cap, or null if unavailable.
     */
    public static String lineLinecap(LineSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        if (stroke == null) {
            return null;
        }

        Expression linecapExp = stroke.getLineCap();

        return linecapExp.toString();
    }

    /**
     * Retrieves the dashes array from a LineSymbolizer.
     *
     * @param symbolizer Line symbolizer information.
     * @return float[] of the line dashes array, or null if unavailable.
     */
    public static float[] lineDash(LineSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        if (stroke == null) {
            return null;
        }

        float[] linedash = stroke.getDashArray();

        return linedash;
    }

    /**
     * Retrieves the location of the first external graphic in a Style object.
     *
     * @param style SLD style information.
     * @return Location of the first external graphic, or null if unavailabe.
     */
    public static URL pointGraphic(StyleImpl style) {
        PointSymbolizerImpl point = pointSymbolizer(style);

        if (point == null) {
            return null;
        }

        GraphicImpl graphic = point.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof ExternalGraphicImpl) {
                ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) gs;
                try {
                    URL location =
                            externalGraphic
                                    .getLocation(); // Should check format is supported by SWT
                    if (location != null) {
                        return location;
                    }
                } catch (MalformedURLException e) {
                    // ignore, try the next one
                }
            }
        }

        return null;
    }

    /**
     * Retrieves the first Mark used in a Style object.
     *
     * @param style the Style
     * @return the first Mark object or null if unavailable.
     */
    public static MarkImpl pointMark(StyleImpl style) {
        if (style == null) {
            return null;
        }

        return mark(pointSymbolizer(style));
    }

    /**
     * Retrieves the first Mark used in a PointSymbolizer object.
     *
     * @param sym the PointSymbolizer
     * @return the first Mark object or null if unavailable.
     */
    public static MarkImpl mark(PointSymbolizerImpl sym) {
        return mark(graphic(sym));
    }

    /**
     * Retrieves the first Mark object from the given Graphic object.
     *
     * @param graphic the Graphic object.
     * @return a Mark object or null if unavailable.
     */
    public static MarkImpl mark(GraphicImpl graphic) {
        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                return (MarkImpl) gs;
            }
        }

        return null;
    }

    public static GraphicImpl graphic(PointSymbolizerImpl sym) {
        if (sym == null) {
            return null;
        }

        return sym.getGraphic();
    }

    /**
     * Retrieves the size of the point's graphic, if found.
     *
     * <p>If you are using something fun like symbols you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return size of the graphic or {@linkplain #NOTFOUND}
     */
    public static int pointSize(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        GraphicImpl g = symbolizer.getGraphic();

        if (g == null) {
            return NOTFOUND;
        }

        Expression exp = g.getSize();
        return Filters.asInt(exp);
    }

    /**
     * Retrieves the well known name of the first Mark that has one in a PointSymbolizer object
     *
     * <p>If you are using something fun like symbols you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return well known name of the first Mark or null if unavailable.
     */
    public static String pointWellKnownName(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;
                String s = wellKnownName(mark);
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    /**
     * Retrieves the "well known name" of a Mark object.
     *
     * @param mark the Mark object
     * @return well known name or null if unavailable.
     */
    public static String wellKnownName(MarkImpl mark) {
        if (mark == null) {
            return null;
        }

        Expression exp = mark.getWellKnownName();

        if (exp == null) {
            return null;
        }

        return Filters.asString(exp);
    }

    /**
     * Retrieves the color of the first Mark in a PointSymbolizer object. This method is identical
     * to {@linkplain #color(PointSymbolizerImpl)}.
     *
     * <p>If you are using something fun like symbols you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return Color of the point's mark, or null if unavailable.
     */
    public static Color pointColor(PointSymbolizerImpl symbolizer) {
        return color(symbolizer);
    }

    /** Sets the Colour for the point symbolizer */
    public static void setPointColour(StyleImpl style, Color colour) {
        if (style == null) {
            return;
        }

        setPointColour(pointSymbolizer(style), colour);
    }

    /**
     * Sets the Colour for the given point symbolizer
     *
     * @param symbolizer the point symbolizer
     * @param colour the new colour
     */
    public static void setPointColour(PointSymbolizerImpl symbolizer, Color colour) {
        if (symbolizer == null || colour == null) {
            return;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            graphic = sf.createDefaultGraphic();
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;

                StrokeImpl stroke = mark.getStroke();
                if (stroke == null) {
                    stroke =
                            sf.createStroke(
                                    ff.literal(Color.BLACK), ConstantStroke.DEFAULT.getWidth());
                    mark.setStroke(stroke);
                }

                FillImpl fill = mark.getFill();
                if (fill != null) {
                    fill.setColor(ff.literal(colour));
                }
            }
        }
    }

    /**
     * Retrieves the color from the first Mark in a PointSymbolizer object.
     *
     * <p>If you are using something fun like symbols you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return Color of the point's mark, or null if unavailable.
     */
    public static Color color(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;
                StrokeImpl stroke = mark.getStroke();
                if (stroke != null) {
                    Color colour = color(stroke);
                    if (colour != null) {
                        return colour;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Retrieves the width of the first Mark with a Stroke that has a non-null width.
     *
     * <p>If you are using something fun like symbols you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return width of the points border or {@linkplain #NOTFOUND} if unavailable.
     */
    public static int pointWidth(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return NOTFOUND;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;
                StrokeImpl stroke = mark.getStroke();
                if (stroke != null) {
                    Expression exp = stroke.getWidth();
                    if (exp != null) {
                        int width = Filters.asInt(exp);
                        if (width != NOTFOUND) {
                            return width;
                        }
                    }
                }
            }
        }

        return NOTFOUND;
    }

    /**
     * Retrieves the point border opacity from a PointSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return double of the point's border opacity, or NaN if unavailable.
     */
    public static double pointBorderOpacity(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return Double.NaN;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;
                StrokeImpl stroke = mark.getStroke();
                if (stroke != null) {
                    Expression exp = stroke.getOpacity();
                    return Filters.asDouble(exp);
                }
            }
        }

        return Double.NaN;
    }

    /**
     * Retrieves the point opacity from a PointSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return double of the point's opacity, or NaN if unavailable.
     */
    public static double pointOpacity(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return Double.NaN;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;
                FillImpl fill = mark.getFill();
                if (fill != null) {
                    Expression expr = fill.getOpacity();
                    if (expr != null) {
                        return SLD.opacity(expr);
                    }
                }
            }
        }

        return Double.NaN;
    }

    /**
     * Retrieves the fill from the first Mark defined in a PointSymbolizer.
     *
     * <p>If you are using something fun like symbols you will need to do your own thing.
     *
     * @param symbolizer Point symbolizer information.
     * @return Color of the point's fill, or null if unavailable.
     */
    public static Color pointFill(PointSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        GraphicImpl graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof MarkImpl) {
                MarkImpl mark = (MarkImpl) gs;
                FillImpl fill = mark.getFill();
                if (fill != null) {
                    Color colour = color(fill.getColor());
                    if (colour != null) {
                        return colour;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Retrieves the outline width from a PolygonSymbolizer
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Polygon symbolizer information.
     * @return outline width or {@linkplain #NOTFOUND} if unavailable.
     */
    public static int polyWidth(PolygonSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        StrokeImpl stroke = symbolizer.getStroke();
        if (stroke != null) {
            return Filters.asInt(stroke.getWidth());
        }

        return NOTFOUND;
    }

    /**
     * Retrieves the outline (stroke) color from a PolygonSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Polygon symbolizer information.
     * @return Color of the polygon's stroke, or null if unavailable.
     */
    public static Color polyColor(PolygonSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        StrokeImpl stroke = symbolizer.getStroke();

        if (stroke == null) {
            return null;
        }

        Color colour = color(stroke.getColor());

        if (colour != null) {
            return colour;
        }

        return null;
    }

    /**
     * Updates the raster opacity in the current style
     *
     * <p><b>Note:</b> This method will update the Style in place; some of the rules and symbolizers
     * will be replaced with modified copies. All symbolizers associated with all rules are
     * modified.
     *
     * @param style the Style object to be updated
     * @param opacity - a new opacity value between 0 and 1
     */
    public static void setRasterOpacity(StyleImpl style, final double opacity) {
        if (style == null) {
            return;
        }
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            for (int i = 0; i < featureTypeStyle.rules().size(); i++) {
                RuleImpl rule = (RuleImpl) featureTypeStyle.rules().get(i);

                DuplicatingStyleVisitor update =
                        new DuplicatingStyleVisitor() {
                            @Override
                            public void visit(org.geotools.api.style.RasterSymbolizer raster) {
                                RasterSymbolizerImpl input = (RasterSymbolizerImpl) raster;

                                ChannelSelectionImpl channelSelection =
                                        copy(input.getChannelSelection());
                                ColorMapImpl colorMap = (ColorMapImpl) copy(input.getColorMap());
                                ContrastEnhancementImpl ce =
                                        (ContrastEnhancementImpl)
                                                copy(input.getContrastEnhancement());
                                String geometryProperty = input.getGeometryPropertyName();
                                Symbolizer outline = (Symbolizer) copy(input.getImageOutline());
                                Expression overlap = copy(input.getOverlap());
                                ShadedReliefImpl shadedRelief =
                                        (ShadedReliefImpl) copy(input.getShadedRelief());

                                Expression newOpacity = ff.literal(opacity);

                                RasterSymbolizerImpl copy =
                                        (RasterSymbolizerImpl)
                                                sf.createRasterSymbolizer(
                                                        geometryProperty,
                                                        newOpacity,
                                                        channelSelection,
                                                        overlap,
                                                        colorMap,
                                                        ce,
                                                        shadedRelief,
                                                        outline);

                                if (STRICT && !copy.equals(raster)) {
                                    throw new IllegalStateException(
                                            "Was unable to duplicate provided raster:" + raster);
                                }
                                pages.push(copy);
                            }
                        };

                rule.accept(update);
                Rule updatedRule = (RuleImpl) update.getCopy();
                featureTypeStyle.rules().set(i, updatedRule);
            }
        }
    }

    /**
     * Updates the raster channel selection in a Style object
     *
     * <p>This method will update the Style in place; some of the rules & symbolizers will be
     * replace with modified copies. All symbolizes associated with all rules are updated.
     *
     * @param style the Style object to be updated
     * @param rgb - an array of the new red, green, blue channels or null if setting the gray
     *     channel
     * @param gray - the new gray channel; ignored if rgb is not null
     */
    public static void setChannelSelection(
            StyleImpl style,
            final SelectedChannelTypeImpl[] rgb,
            final SelectedChannelTypeImpl gray) {
        if (style == null) {
            return;
        }
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            for (int i = 0; i < featureTypeStyle.rules().size(); i++) {
                RuleImpl rule = (RuleImpl) featureTypeStyle.rules().get(i);

                DuplicatingStyleVisitor update =
                        new DuplicatingStyleVisitor() {

                            @Override
                            public void visit(org.geotools.api.style.RasterSymbolizer raster) {
                                RasterSymbolizerImpl input = (RasterSymbolizerImpl) raster;
                                ChannelSelectionImpl channelSelection = createChannelSelection();

                                ColorMapImpl colorMap = (ColorMapImpl) copy(input.getColorMap());
                                ContrastEnhancementImpl ce =
                                        (ContrastEnhancementImpl)
                                                copy(input.getContrastEnhancement());
                                String geometryProperty = input.getGeometryPropertyName();
                                Symbolizer outline = (Symbolizer) copy(input.getImageOutline());
                                Expression overlap = copy(input.getOverlap());
                                ShadedReliefImpl shadedRelief =
                                        (ShadedReliefImpl) copy(input.getShadedRelief());

                                Expression opacity = copy(raster.getOpacity());

                                RasterSymbolizerImpl copy =
                                        (RasterSymbolizerImpl)
                                                sf.createRasterSymbolizer(
                                                        geometryProperty,
                                                        opacity,
                                                        channelSelection,
                                                        overlap,
                                                        colorMap,
                                                        ce,
                                                        shadedRelief,
                                                        outline);
                                if (STRICT && !copy.equals(raster)) {
                                    throw new IllegalStateException(
                                            "Was unable to duplicate provided raster:" + raster);
                                }
                                pages.push(copy);
                            }

                            private ChannelSelectionImpl createChannelSelection() {
                                if (rgb != null) {
                                    return sf.createChannelSelection(rgb);
                                } else {
                                    return (ChannelSelectionImpl) sf.createChannelSelection(gray);
                                }
                            }
                        };

                rule.accept(update);
                RuleImpl updatedRule = (RuleImpl) update.getCopy();
                featureTypeStyle.rules().set(i, updatedRule);
            }
        }
    }

    /**
     * Sets the colour for the first polygon symbolizer defined in the provided style
     *
     * @param style the Style object
     * @param colour the colour for polygon outlines and fill
     */
    public static void setPolyColour(StyleImpl style, Color colour) {
        if (style == null) {
            return;
        }
        setPolyColour(polySymbolizer(style), colour);
    }

    /**
     * Sets the colour to use for outline (stroke) and fill in a polygon symbolizer
     *
     * @param symbolizer the polygon symbolizer
     * @param colour the colour for polygon outlines and fill
     */
    public static void setPolyColour(PolygonSymbolizerImpl symbolizer, Color colour) {
        if (symbolizer == null || colour == null) {
            return;
        }

        Expression colourExp = ff.literal(colour);
        StrokeImpl stroke = symbolizer.getStroke();
        if (stroke == null) {
            stroke = sf.createStroke(colourExp, ConstantStroke.DEFAULT.getWidth());
            symbolizer.setStroke(stroke);
        } else {
            stroke.setColor(ff.literal(colour));
        }

        FillImpl fill = symbolizer.getFill();
        if (fill != null) {
            fill.setColor(colourExp);
        }
    }

    /**
     * Retrieves the fill colour from a PolygonSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Polygon symbolizer information.
     * @return Color of the polygon's fill, or null if unavailable.
     */
    public static Color polyFill(PolygonSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        FillImpl fill = symbolizer.getFill();

        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Retrieves the border (stroke) opacity from a PolygonSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Polygon symbolizer information.
     * @return double of the polygon's border opacity, or NaN if unavailable.
     */
    public static double polyBorderOpacity(PolygonSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        StrokeImpl stroke = symbolizer.getStroke();
        if (stroke == null) {
            return Double.NaN;
        }

        return opacity(stroke);
    }

    /**
     * Retrieves the fill opacity from the first PolygonSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Polygon symbolizer information.
     * @return double of the polygon's fill opacity, or NaN if unavailable.
     */
    public static double polyFillOpacity(PolygonSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        FillImpl fill = symbolizer.getFill();

        return opacity(fill);
    }

    /**
     * Retrieve the opacity from the provided fill; or return the default.
     *
     * @return opacity from the above fill; or return the Fill.DEFAULT value
     */
    public static double opacity(FillImpl fill) {
        if (fill == null) {
            fill = (FillImpl) ConstantFill.DEFAULT;
        }

        Expression opacityExp = fill.getOpacity();
        if (opacityExp == null) {
            opacityExp = ConstantFill.DEFAULT.getOpacity();
        }

        return Filters.asDouble(opacityExp);
    }

    /**
     * Retrieves the opacity from a RasterSymbolizer.
     *
     * <p>If you are using something fun like rules you will need to do your own thing.
     *
     * @param symbolizer Raster symbolizer information.
     * @return opacity of the first RasterSymbolizer or NaN if unavailable.
     */
    public static double rasterOpacity(RasterSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        return Filters.asDouble(symbolizer.getOpacity());
    }

    /**
     * Retrieves the opacity from the first RasterSymbolizer defined in a style.
     *
     * @param style the Style object
     * @return opacity of the raster symbolizer or NaN if unavailable.
     */
    public static double rasterOpacity(StyleImpl style) {
        return rasterOpacity(rasterSymbolizer(style));
    }

    /**
     * Retrieve the first TextSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts FeatureTypeStyle information.
     * @return TextSymbolizer, or null if not found.
     */
    public static TextSymbolizerImpl textSymbolizer(FeatureTypeStyleImpl fts) {
        return (TextSymbolizerImpl) symbolizer(fts, TextSymbolizerImpl.class);
    }

    /**
     * Retrieve the first TextSymbolizer from the provided Style.
     *
     * @param style the Style object
     * @return TextSymbolizer, or null if not found.
     */
    public static TextSymbolizerImpl textSymbolizer(StyleImpl style) {
        return (TextSymbolizerImpl) symbolizer(style, TextSymbolizerImpl.class);
    }

    /**
     * Retrieves the font from a TextSymbolizer. Equivalent to {@code symbolizer.getFont()}.
     *
     * @param symbolizer the symbolizer
     * @return the first font defined in the symbolizer or null if unavailable.
     */
    public static FontImpl font(TextSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }
        return symbolizer.getFont();
    }

    /**
     * Retrieves the label from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     * @return Expression of the label's text, or null if unavailable.
     */
    public static Expression textLabel(TextSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Expression exp = symbolizer.getLabel();

        if (exp == null) {
            return null;
        }

        return exp;
    }

    /**
     * Retrieves the label from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     * @return the label's text, or null if unavailable.
     */
    public static String textLabelString(TextSymbolizerImpl symbolizer) {
        Expression exp = textLabel(symbolizer);

        return (exp == null) ? null : exp.toString();
    }

    /**
     * Retrieves the colour of the font fill from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     * @return Color of the font's fill, or null if unavailable.
     */
    public static Color textFontFill(TextSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        FillImpl fill = symbolizer.getFill();

        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Retrieves the colour of the halo fill a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     * @return Color of the halo's fill, or null if unavailable.
     */
    public static Color textHaloFill(TextSymbolizerImpl symbolizer) {
        HaloImpl halo = symbolizer.getHalo();

        if (halo == null) {
            return null;
        }

        FillImpl fill = halo.getFill();

        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Retrieves the halo width from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     * @return the halo's width, or 0 if unavailable.
     */
    public static int textHaloWidth(TextSymbolizerImpl symbolizer) {
        HaloImpl halo = symbolizer.getHalo();

        if (halo == null) {
            return 0;
        }

        Expression exp = halo.getRadius();

        if (exp == null) {
            return 0;
        }

        int width = (int) Float.parseFloat(exp.toString());

        if (width != 0) {
            return width;
        }

        return 0;
    }

    /**
     * Retrieves the halo opacity from the first TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     * @return double of the halo's opacity, or NaN if unavailable.
     */
    public static double textHaloOpacity(TextSymbolizerImpl symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        HaloImpl halo = symbolizer.getHalo();

        if (halo == null) {
            return Double.NaN;
        }

        FillImpl fill = halo.getFill();

        if (fill == null) {
            return Double.NaN;
        }

        Expression expr = fill.getOpacity();
        if (expr == null) {
            return Double.NaN;
        }

        return Filters.asDouble(expr);
    }

    /**
     * Navigate through the expression finding the first mentioned Color.
     *
     * <p>If you have a specific Feature in mind please use:
     *
     * <pre><code>
     * Object value = expr.getValue( feature );
     * return value instanceof Color ? (Color) value : null;
     * </code></pre>
     *
     * @param expr the Expression object
     * @return First available color, or null.
     */
    public static Color color(Expression expr) {
        if (expr == null) {
            return null;
        }
        return expr.evaluate(null, Color.class);
    }

    /**
     * Retrieve the first RasterSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle information.
     * @return RasterSymbolizer, or null if not found.
     */
    public static RasterSymbolizerImpl rasterSymbolizer(FeatureTypeStyleImpl fts) {
        return (RasterSymbolizerImpl) symbolizer(fts, RasterSymbolizerImpl.class);
    }

    /**
     * Retrieve the first RasterSymbolizer from the provided Style.
     *
     * @param style the Style object
     * @return RasterSymbolizer, or null if not found.
     */
    public static RasterSymbolizerImpl rasterSymbolizer(StyleImpl style) {
        return (RasterSymbolizerImpl) symbolizer(style, RasterSymbolizerImpl.class);
    }

    /**
     * Retrieve the first LineSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle object
     * @return LineSymbolizer, or null if not found.
     */
    public static LineSymbolizerImpl lineSymbolizer(FeatureTypeStyleImpl fts) {
        return (LineSymbolizerImpl) symbolizer(fts, LineSymbolizerImpl.class);
    }

    /**
     * Retrieve the first LineSymbolizer from the provided Style.
     *
     * @param style the Style object
     * @return LineSymbolizer, or null if not found.
     */
    public static LineSymbolizerImpl lineSymbolizer(StyleImpl style) {
        return (LineSymbolizerImpl) symbolizer(style, LineSymbolizerImpl.class);
    }

    /**
     * Retrieves the Stroke from a LineSymbolizer.
     *
     * @param sym the symbolizer
     * @return the Stroke or null if not found.
     */
    public static StrokeImpl stroke(LineSymbolizerImpl sym) {
        if (sym == null) {
            return null;
        }

        return sym.getStroke();
    }

    /**
     * Retrieves the Stroke from a PolygonSymbolizer.
     *
     * @param sym the symbolizer
     * @return the Stroke or null if not found.
     */
    public static StrokeImpl stroke(PolygonSymbolizerImpl sym) {
        if (sym == null) {
            return null;
        }

        return sym.getStroke();
    }

    /**
     * Retrieves the Stroke from a PointSymbolizer.
     *
     * @param sym the symbolizer
     * @return the Stroke or null if not found.
     */
    public static StrokeImpl stroke(PointSymbolizerImpl sym) {
        MarkImpl mark = mark(sym);

        return (mark == null) ? null : mark.getStroke();
    }

    /**
     * Retrieves the Fill from a PolygonSymbolizer.
     *
     * @param sym the symbolizer
     * @return the Fill or null if not found.
     */
    public static FillImpl fill(PolygonSymbolizerImpl sym) {
        if (sym == null) {
            return null;
        }

        return sym.getFill();
    }

    /**
     * Retrieves the Fill from a PointSymbolizer.
     *
     * @param sym the symbolizer
     * @return the Fill or null if not found.
     */
    public static FillImpl fill(PointSymbolizerImpl sym) {
        MarkImpl mark = mark(sym);

        return (mark == null) ? null : mark.getFill();
    }

    /**
     * Retrieve the first PointSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle object
     * @return PointSymbolizer, or null if not found.
     */
    public static PointSymbolizerImpl pointSymbolizer(FeatureTypeStyleImpl fts) {
        return (PointSymbolizerImpl) symbolizer(fts, PointSymbolizerImpl.class);
    }

    /**
     * Retrieve the first PointSymbolizer from the provided Style.
     *
     * @param style the Style object
     * @return PointSymbolizer, or null if not found.
     */
    public static PointSymbolizerImpl pointSymbolizer(StyleImpl style) {
        return (PointSymbolizerImpl) symbolizer(style, PointSymbolizerImpl.class);
    }

    /**
     * Retrieve the first PolygonSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts FeatureTypeStyle object.
     * @return PolygonSymbolizer, or null if not found.
     */
    public static PolygonSymbolizerImpl polySymbolizer(FeatureTypeStyleImpl fts) {
        return (PolygonSymbolizerImpl) symbolizer(fts, PolygonSymbolizerImpl.class);
    }

    /**
     * Retrieve the first PolygonSymbolizer from the provided Style.
     *
     * @param style the Style object
     * @return PolygonSymbolizer, or null if not found.
     */
    public static PolygonSymbolizerImpl polySymbolizer(StyleImpl style) {
        return (PolygonSymbolizerImpl) symbolizer(style, PolygonSymbolizerImpl.class);
    }

    /**
     * Returns the feature type style in the style which matched a particular name.
     *
     * @param style the Style object
     * @param type the feature type (must be non-null)
     * @return The FeatureTypeStyle object if it exists, otherwise false.
     */
    public static FeatureTypeStyleImpl featureTypeStyle(StyleImpl style, SimpleFeatureType type) {
        if (style == null) {
            return null;
        }

        if ((type == null) || (type.getTypeName() == null)) {
            return null;
        }

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (type.getTypeName().equalsIgnoreCase(fts.getName())) {
                return (FeatureTypeStyleImpl) fts;
            }
        }

        return null;
    }

    /**
     * Returns the first style object which matches a given schema.
     *
     * @param styles Array of style objects.
     * @param schema Feature schema.
     * @return The first object to match the feature type, otherwise null if no match.
     */
    public static StyleImpl matchingStyle(StyleImpl[] styles, SimpleFeatureType schema) {
        if ((styles == null) || (styles.length == 0)) {
            return null;
        }

        for (StyleImpl style : styles) {
            if (featureTypeStyle(style, schema) != null) {
                return style;
            }
        }

        return null;
    }

    /**
     * Retrieve the first Symbolizer from the provided Style.
     *
     * @param style SLD style information.
     * @param SYMBOLIZER LineSymbolizer.class, PointSymbolizer.class, PolygonSymbolizer.class,
     *     RasterSymbolizer.class, or TextSymbolizer.class
     * @return symbolizer instance from style, or null if not found.
     */
    protected static Symbolizer symbolizer(StyleImpl style, final Class SYMBOLIZER) {
        if (style == null) {
            return null;
        }

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            Symbolizer sym = symbolizer((FeatureTypeStyleImpl) fts, SYMBOLIZER);
            if (sym != null) {
                return sym;
            }
        }

        return null;
    }

    /**
     * Retrieve the first Symbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle SLD style information.
     * @param SYMBOLIZER LineSymbolizer.class, PointSymbolizer.class, PolygonSymbolizer.class,
     *     RasterSymbolizer.class, or TextSymbolizer.class
     * @return symbolizer instance from fts, or null if not found.
     */
    protected static Symbolizer symbolizer(FeatureTypeStyleImpl fts, final Class SYMBOLIZER) {
        if (fts == null) {
            return null;
        }

        for (Rule rule : fts.rules()) {
            for (org.geotools.api.style.Symbolizer sym : rule.symbolizers()) {
                if (SYMBOLIZER.isInstance(sym)) {
                    return (Symbolizer) sym;
                }
            }
        }

        return null;
    }

    /**
     * Convert a Color object to hex representation
     *
     * @param c the Color object
     * @return a hex String
     */
    public static String colorToHex(Color c) {
        return "#" + Integer.toHexString(c.getRGB() & 0x00ffffff);
    }

    /**
     * Get the Styles defined in the given StyledLayerDescriptor
     *
     * @param sld the StyledLayerDescriptor object
     * @return an array of Styles
     */
    public static StyleImpl[] styles(StyledLayerDescriptor sld) {
        StyledLayerImpl[] layers = (StyledLayerImpl[]) sld.getStyledLayers();
        List<StyleImpl> styles = new ArrayList<>();

        for (StyledLayerImpl styledLayer : layers) {
            if (styledLayer instanceof UserLayerImpl) {
                UserLayerImpl layer = (UserLayerImpl) styledLayer;
                styles.addAll(layer.userStyles());

            } else if (styledLayer instanceof NamedLayerImpl) {
                NamedLayerImpl layer = (NamedLayerImpl) styledLayer;
                styles.addAll(layer.styles());
            }
        }

        return styles.toArray(new StyleImpl[0]);
    }

    /**
     * Get the FeatureTypeStyles defined in the given StyledLayerDescriptor
     *
     * @param sld the StyledLayerDescriptor object
     * @return an array of FeatureTypeStyles
     */
    public static FeatureTypeStyleImpl[] featureTypeStyles(StyledLayerDescriptor sld) {
        StyleImpl[] style = styles(sld);
        List<FeatureTypeStyle> fts = new ArrayList<>();
        for (StyleImpl value : style) {
            fts.addAll(value.featureTypeStyles());
        }
        return fts.toArray(new FeatureTypeStyleImpl[0]);
    }

    /**
     * Retrieve the first FeatureTypeStyle defined in the given StyledLayerDescriptor object that
     * matches the specified feature type
     *
     * @param sld a StyledLayerDescriptor object
     * @param type the feature type to match
     * @return a FeatureTypeStyle or null if there was no match
     */
    public static FeatureTypeStyleImpl featureTypeStyle(
            StyledLayerDescriptor sld, SimpleFeatureType type) {
        // alternatively, we could use a StyleVisitor here
        StyleImpl[] styles = styles(sld);
        for (StyleImpl style : styles) {
            for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                if (type.getTypeName().equalsIgnoreCase(fts.getName())) {
                    return (FeatureTypeStyleImpl) fts;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the default style from the given StyledLayerDescriptor.
     *
     * @param sld the StyledLayerDescriptor object
     * @return the default style; or the first style if no default is defined; or {@code null} if
     *     there are no styles
     */
    public static StyleImpl defaultStyle(StyledLayerDescriptor sld) {
        StyleImpl[] style = styles(sld);
        for (StyleImpl value : style) {
            if (value.isDefault()) {
                return value;
            }
        }
        // no default, so just grab the first one
        if (style.length == 0) {
            return null;
        }
        return style[0];
    }

    /**
     * Retrieves all filters defined in a rule
     *
     * @param rule the rule
     * @return array of filters
     */
    public static Filter[] filters(RuleImpl... rule) {
        Filter[] filter = new Filter[rule.length];
        for (int i = 0; i < rule.length; i++) {
            filter[i] = rule[0].getFilter();
        }
        return filter;
    }

    /**
     * Retrieves all filters defined in a style
     *
     * @param style the style
     * @return array of filters
     */
    public static Filter[] filters(StyleImpl style) {
        RuleImpl[] rule = rules(style);
        return filters(rule);
    }

    /**
     * Retrieves all rules defined in a style
     *
     * @param style the style
     * @return an array of unique rules
     */
    public static RuleImpl[] rules(StyleImpl style) {
        Set<Rule> ruleSet = new HashSet<>();
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            ruleSet.addAll(fts.rules());
        }

        if (ruleSet.isEmpty()) {
            return new RuleImpl[0];
        } else {
            return ruleSet.toArray(new RuleImpl[0]);
        }
    }

    /**
     * Retrieves all symbolizers defined in a style
     *
     * @param style the style
     * @return an array of unique symbolizers
     */
    public static Symbolizer[] symbolizers(StyleImpl style) {
        Set<Symbolizer> symbolizers = new HashSet<>();
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            fts.rules().stream()
                    .map(Rule::symbolizers)
                    .forEach(
                            symbolizers1 ->
                                    symbolizers.addAll(
                                            (Collection<? extends Symbolizer>) symbolizers1));
        }

        if (symbolizers.isEmpty()) {
            return new Symbolizer[0];
        } else {
            return symbolizers.toArray(new Symbolizer[0]);
        }
    }

    /**
     * Retrieves all symbolizers defined in a rule
     *
     * @param rule the rule
     * @return an array of unique symbolizers
     */
    public static Symbolizer[] symbolizers(RuleImpl rule) {
        Set<Symbolizer> symbolizers = new HashSet<>();
        symbolizers.addAll(rule.symbolizers());

        if (symbolizers.isEmpty()) {
            return new Symbolizer[0];
        } else {
            return symbolizers.toArray(new Symbolizer[0]);
        }
    }

    /**
     * Retrieves all colour names defined in a style
     *
     * @param style the style
     * @return an array of unique colour names
     */
    public static String[] colors(StyleImpl style) {
        Set<String> colorSet = new HashSet<>();

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            fts.rules().stream()
                    .map(rule -> colors((RuleImpl) rule))
                    .flatMap(color -> Arrays.stream(color))
                    .forEach(colorSet::add);
        }

        if (colorSet.isEmpty()) {
            return new String[0];
        } else {
            return colorSet.toArray(new String[0]);
        }
    }

    /**
     * Retrieves all colour names defined in a rule
     *
     * @param rule the rule
     * @return an array of unique colour names
     */
    public static String[] colors(RuleImpl rule) {
        Set<String> colorSet = new HashSet<>();

        Color color = null;
        for (Symbolizer sym : rule.symbolizers()) {
            if (sym instanceof PolygonSymbolizerImpl) {
                PolygonSymbolizerImpl symb = (PolygonSymbolizerImpl) sym;
                color = polyFill(symb);

            } else if (sym instanceof LineSymbolizerImpl) {
                LineSymbolizerImpl symb = (LineSymbolizerImpl) sym;
                color = color(symb);

            } else if (sym instanceof PointSymbolizerImpl) {
                PointSymbolizerImpl symb = (PointSymbolizerImpl) sym;
                color = color(symb);
            }

            if (color != null) {
                colorSet.add(color.toString());
            }
        }

        if (colorSet.isEmpty()) {
            return new String[0];
        } else {
            return colorSet.toArray(new String[0]);
        }
    }

    /**
     * Converts a java.awt.Color into an HTML Colour
     *
     * @param color color to convert
     * @return HTML Color (fill) in hex #RRGGBB
     */
    public static String toHTMLColor(Color color) {
        if (color == null) {
            return null;
        }

        String red = "0" + Integer.toHexString(color.getRed());
        red = red.substring(red.length() - 2);

        String grn = "0" + Integer.toHexString(color.getGreen());
        grn = grn.substring(grn.length() - 2);

        String blu = "0" + Integer.toHexString(color.getBlue());
        blu = blu.substring(blu.length() - 2);

        return ("#" + red + grn + blu).toUpperCase();
    }

    /**
     * Convert an HTML colour string to a java.awt.Color object
     *
     * @param htmlColor the colour string
     * @return a new Color object
     */
    public static Color toColor(String htmlColor) {
        return new Color(Integer.parseInt(htmlColor.substring(1), 16));
    }

    /**
     * Test if the given FeatureTypeStyle object matches a regular expression
     *
     * @param fts the feature type style
     * @param regex regular expression to match
     * @return true if a match is found; false otherwise
     */
    public static boolean isSemanticTypeMatch(FeatureTypeStyleImpl fts, String regex) {
        for (SemanticType id : fts.semanticTypeIdentifiers()) {
            if (id.matches(regex)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the min scale of the default (first) rule
     *
     * @param fts the feature type style
     * @return min scale or 0 if no min scale is set
     */
    public static double minScale(FeatureTypeStyleImpl fts) {
        if (fts == null || fts.rules().isEmpty()) return 0.0;

        return fts.rules().get(0).getMinScaleDenominator();
    }

    /**
     * Returns the max scale of the default (first) rule
     *
     * @param fts the feature type style
     * @return min scale or NaN if no max scale is set
     */
    public static double maxScale(FeatureTypeStyleImpl fts) {
        if (fts == null || fts.rules().isEmpty()) return Double.NaN;

        return fts.rules().get(0).getMaxScaleDenominator();
    }

    /**
     * Create a PointPlacement object.
     *
     * @param horizAlign horizontal alignment (0 to 1)
     * @param vertAlign vertical alignment (0 to 1)
     * @param rotation rotation angle in degrees
     * @return a new PointPlacement object
     */
    public static PointPlacementImpl getPlacement(
            double horizAlign, double vertAlign, double rotation) {
        AnchorPointImpl anchorPoint =
                sf.createAnchorPoint(ff.literal(horizAlign), ff.literal(vertAlign));
        return (PointPlacementImpl)
                sf.createPointPlacement(anchorPoint, null, ff.literal(rotation));
    }

    /**
     * Create a minimal style to render features of type {@code typeName} read from the given data
     * store
     *
     * @param store the data store containing the features
     * @param typeName the feature type to create the style for
     * @param color single color to use for all components of the Style
     * @return a new Style instance
     * @throws java.io.IOException if the data store cannot be accessed
     */
    public static StyleImpl createSimpleStyle(DataStore store, String typeName, Color color)
            throws IOException {
        SimpleFeatureType type = store.getSchema(typeName);
        return createSimpleStyle(type, color);
    }

    /**
     * Create a minimal style to render features of type {@code type}.
     *
     * @param type the feature type
     * @return a new Style instance
     */
    public static StyleImpl createSimpleStyle(FeatureType type) {
        return createSimpleStyle(type, Color.BLACK);
    }

    /**
     * Create a minimal style to render features of type {@code type}
     *
     * @param type the feature type to create the style for
     * @param color single color to use for all components of the Style
     * @return a new Style instance
     * @throws java.io.IOException if the data store cannot be accessed
     */
    public static StyleImpl createSimpleStyle(FeatureType type, Color color) {
        GeometryDescriptor desc = type.getGeometryDescriptor();
        Class<?> clazz = desc.getType().getBinding();
        Color fillColor = null;

        if (Polygon.class.isAssignableFrom(clazz) || MultiPolygon.class.isAssignableFrom(clazz)) {
            if (color.equals(Color.BLACK)) {
                fillColor = null;
            } else {
                fillColor = color;
            }
            return createPolygonStyle(color, fillColor, 0.5f);

        } else if (LineString.class.isAssignableFrom(clazz)
                || MultiLineString.class.isAssignableFrom(clazz)) {
            return createLineStyle(color, 1.0f);

        } else if (Point.class.isAssignableFrom(clazz)
                || MultiPoint.class.isAssignableFrom(clazz)) {
            if (color.equals(Color.BLACK)) {
                fillColor = null;
            } else {
                fillColor = color;
            }
            return createPointStyle("Circle", color, fillColor, 0.5f, 3.0f);
        }

        throw new UnsupportedOperationException("No style method for " + clazz.getName());
    }

    /**
     * Create a polygon style with the given colors and opacity.
     *
     * @param outlineColor color of polygon outlines
     * @param fillColor color for the fill
     * @param opacity proportional opacity (0 to 1)
     * @return a new Style instance
     */
    public static StyleImpl createPolygonStyle(Color outlineColor, Color fillColor, float opacity) {
        StrokeImpl stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(1.0f));
        FillImpl fill = (FillImpl) ConstantFill.NULL;
        if (fillColor != null) {
            fill = (FillImpl) sf.createFill(ff.literal(fillColor), ff.literal(opacity));
        }
        return wrapSymbolizers(sf.createPolygonSymbolizer(stroke, fill, null));
    }

    /**
     * Create a polygon style with the given colors, opacity and optional labels.
     *
     * @param outlineColor color of polygon outlines
     * @param fillColor color for the fill
     * @param opacity proportional opacity (0 to 1)
     * @param labelField name of the feature field (attribute) to use for labelling; mauy be {@code
     *     null} for no labels
     * @param labelFont GeoTools Font object to use for labelling; if {@code null} and {@code
     *     labelField} is not {@code null} the default font will be used
     * @return a new Style instance
     */
    public static StyleImpl createPolygonStyle(
            Color outlineColor,
            Color fillColor,
            float opacity,
            String labelField,
            FontImpl labelFont) {
        StrokeImpl stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(1.0f));
        FillImpl fill = (FillImpl) ConstantFill.NULL;
        if (fillColor != null) {
            fill = (FillImpl) sf.createFill(ff.literal(fillColor), ff.literal(opacity));
        }
        PolygonSymbolizerImpl polySym =
                (PolygonSymbolizerImpl) sf.createPolygonSymbolizer(stroke, fill, null);

        if (labelField == null) {
            return wrapSymbolizers(polySym);

        } else {
            FontImpl font = (labelFont == null ? sf.getDefaultFont() : labelFont);
            FillImpl labelFill = (FillImpl) sf.createFill(ff.literal(Color.BLACK));

            TextSymbolizerImpl textSym =
                    sf.createTextSymbolizer(
                            labelFill,
                            new FontImpl[] {font},
                            null,
                            ff.property(labelField),
                            null,
                            null);

            return wrapSymbolizers(polySym, textSym);
        }
    }

    /**
     * Create a line style with given color and line width
     *
     * @param lineColor color of lines
     * @param width width of lines
     * @return a new Style instance
     */
    public static StyleImpl createLineStyle(Color lineColor, float width) {
        StrokeImpl stroke = sf.createStroke(ff.literal(lineColor), ff.literal(width));
        return wrapSymbolizers(sf.createLineSymbolizer(stroke, null));
    }

    /**
     * Create a line style with given color, line width and optional labels
     *
     * @param lineColor color of lines
     * @param width width of lines
     * @param labelField name of the feature field (attribute) to use for labelling; mauy be {@code
     *     null} for no labels
     * @param labelFont GeoTools Font object to use for labelling; if {@code null} and {@code
     *     labelField} is not {@code null} the default font will be used
     * @return a new Style instance
     */
    public static StyleImpl createLineStyle(
            Color lineColor, float width, String labelField, FontImpl labelFont) {
        StrokeImpl stroke = sf.createStroke(ff.literal(lineColor), ff.literal(width));
        LineSymbolizerImpl lineSym = (LineSymbolizerImpl) sf.createLineSymbolizer(stroke, null);

        if (labelField == null) {
            return wrapSymbolizers(lineSym);

        } else {
            FontImpl font = (labelFont == null ? sf.getDefaultFont() : labelFont);
            FillImpl labelFill = (FillImpl) sf.createFill(ff.literal(Color.BLACK));

            TextSymbolizerImpl textSym =
                    sf.createTextSymbolizer(
                            labelFill,
                            new FontImpl[] {font},
                            null,
                            ff.property(labelField),
                            null,
                            null);

            return wrapSymbolizers(lineSym, textSym);
        }
    }

    /**
     * Create a point style without labels
     *
     * @param wellKnownName one of: Circle, Square, Cross, X, Triangle or Star
     * @param lineColor color for the point symbol outline
     * @param fillColor color for the point symbol fill
     * @param opacity a value between 0 and 1 for the opacity of the fill
     * @param size size of the point symbol
     * @return a new Style instance
     */
    public static StyleImpl createPointStyle(
            String wellKnownName, Color lineColor, Color fillColor, float opacity, float size) {

        return createPointStyle(wellKnownName, lineColor, fillColor, opacity, size, null, null);
    }

    /**
     * Create a point style, optionally with text labels
     *
     * @param wellKnownName one of: Circle, Square, Cross, X, Triangle or Star
     * @param lineColor color for the point symbol outline
     * @param fillColor color for the point symbol fill
     * @param opacity a value between 0 and 1 for the opacity of the fill
     * @param size size of the point symbol
     * @param labelField name of the feature field (attribute) to use for labelling; mauy be {@code
     *     null} for no labels
     * @param labelFont GeoTools Font object to use for labelling; if {@code null} and {@code
     *     labelField} is not {@code null} the default font will be used
     * @return a new Style instance
     */
    public static StyleImpl createPointStyle(
            String wellKnownName,
            Color lineColor,
            Color fillColor,
            float opacity,
            float size,
            String labelField,
            FontImpl labelFont) {

        StrokeImpl stroke = sf.createStroke(ff.literal(lineColor), ff.literal(1.0f));
        FillImpl fill = (FillImpl) ConstantFill.NULL;
        if (fillColor != null) {
            fill = (FillImpl) sf.createFill(ff.literal(fillColor), ff.literal(opacity));
        }

        MarkImpl mark =
                sf.createMark(
                        ff.literal(wellKnownName), stroke, fill, ff.literal(size), ff.literal(0));

        GraphicImpl graphic = sf.createDefaultGraphic();
        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(mark);
        graphic.setSize(ff.literal(size));

        PointSymbolizerImpl pointSym =
                (PointSymbolizerImpl) sf.createPointSymbolizer(graphic, null);

        if (labelField == null) {
            return wrapSymbolizers(pointSym);

        } else {
            FontImpl font = (labelFont == null ? sf.getDefaultFont() : labelFont);
            FillImpl labelFill = (FillImpl) sf.createFill(ff.literal(Color.BLACK));
            AnchorPointImpl anchor = sf.createAnchorPoint(ff.literal(0.5), ff.literal(0.0));
            DisplacementImpl disp = sf.createDisplacement(ff.literal(0), ff.literal(5));
            LabelPlacement placement = sf.createPointPlacement(anchor, disp, ff.literal(0));

            TextSymbolizerImpl textSym =
                    sf.createTextSymbolizer(
                            labelFill,
                            new FontImpl[] {font},
                            null,
                            ff.property(labelField),
                            placement,
                            null);

            return wrapSymbolizers(pointSym, textSym);
        }
    }

    /**
     * Wrap one or more symbolizers into a Rule / FeatureTypeStyle / Style
     *
     * @param symbolizers one or more symbolizer objects
     * @return a new Style instance or null if no symbolizers are provided
     */
    public static StyleImpl wrapSymbolizers(Symbolizer... symbolizers) {
        if (symbolizers == null || symbolizers.length == 0) {
            return null;
        }

        Rule rule = sf.createRule();

        for (Symbolizer sym : symbolizers) {
            ((RuleImpl) rule).symbolizers().add(sym);
        }

        FeatureTypeStyleImpl fts = (FeatureTypeStyleImpl) sf.createFeatureTypeStyle();

        StyleImpl style = sf.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
}
