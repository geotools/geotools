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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.AbstractDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Filters;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.SemanticType;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Utility class that provides static helper methods for common operations on
 * GeoTools styling objects (e.g. StyledLayerDescriptor, Style, FeatureTypeStyle,
 * Rule, Symbolizer, Stroke and Fill).
 *
 * @author Jody Garnett
 * @source $URL$
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
     *
     * @return Color of linestring, or null if unavailable.
     */
    public static Color lineColor(LineSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Stroke stroke = symbolizer.getStroke();

        return color(stroke);
    }

    /**
     * Retrieve the color of a stroke object
     * @param stroke a Stroke object
     * @return color or null if stroke was null
     */
    public static Color color(Stroke stroke) {
        if (stroke == null) {
            return null;
        }
        return color(stroke.getColor());
    }
    
    /**
     * Retrieve the color of a fill object
     * @param fill a Fill object
     * @return color or null if fill was null
     */
    public static Color color(Fill fill) {
        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Updates the color for line symbolizers in the current style.
     * <p>
     * This method will update the Style in place; some of the symbolizers
     * will be replaced with modified copies.
     *
     * @param style the Style object to be updated
     * @param colour Color to to use
     */
    public static void setLineColour(Style style, final Color colour) {
        if (style == null) {
            return;
        }
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            for (int i = 0; i < featureTypeStyle.rules().size(); i++) {
                Rule rule = featureTypeStyle.rules().get(i);
                DuplicatingStyleVisitor update = new DuplicatingStyleVisitor() {

                    @Override
                    public void visit(LineSymbolizer line) {
                        String name = line.getGeometryPropertyName();
                        Stroke stroke = update(line.getStroke());
                        LineSymbolizer copy = sf.createLineSymbolizer(stroke, name);
                        pages.push(copy);
                    }

                    Stroke update(Stroke stroke) {
                        Expression color = ff.literal(colour);
                        Expression width = copy(stroke.getWidth());
                        Expression opacity = copy(stroke.getOpacity());
                        Expression lineJoin = copy(stroke.getLineJoin());
                        Expression lineCap = copy(stroke.getLineCap());
                        float[] dashArray = copy(stroke.getDashArray());
                        Expression dashOffset = copy(stroke.getDashOffset());
                        Graphic graphicStroke = copy(stroke.getGraphicStroke());
                        Graphic graphicFill = copy(stroke.getGraphicFill());
                        return sf.createStroke(color, width, opacity, lineJoin, lineCap, dashArray, dashOffset, graphicFill, graphicStroke);
                    }
                };
                rule.accept(update);
                Rule updatedRule = (Rule) update.getCopy();
                featureTypeStyle.rules().set(i, updatedRule);
            }
        }
    }

    /**
     * Sets the Colour for the given Line symbolizer
     *
     * @param symbolizer
     * @param colour
     */
    public static void setLineColour(LineSymbolizer symbolizer, Color colour) {
        if (symbolizer == null || colour == null) {
            return;
        }

        Stroke stroke = symbolizer.getStroke();

        if (stroke == null) {
            stroke = sf.createStroke(ff.literal(colour), Stroke.DEFAULT.getWidth());
            symbolizer.setStroke(stroke);

        } else {
            stroke.setColor(ff.literal(colour));
        }
    }

    /**
     * Retrieve color from linesymbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     *
     * @return Color of linestring, or null if unavailable.
     */
    public static Color color(LineSymbolizer symbolizer) {
        return lineColor(symbolizer);
    }

    /**
     * Retrieve linestring width from symbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     *
     * @return width of linestring, or NOTFOUND
     */
    public static int lineWidth(LineSymbolizer symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        Stroke stroke = symbolizer.getStroke();

        return width(stroke);
    }

    /**
     * Retrieve the width of a Stroke object.
     *
     * @param stroke the Stroke object.
     *
     * @return width or {@linkplain #NOTFOUND} if not available.
     */
    public static int width(Stroke stroke) {
        if (stroke == null) {
            return NOTFOUND;
        }

        return Filters.asInt(stroke.getWidth());
    }

    /**
     * Retrieve the size of a Mark object
     * @param mark the Mark object
     * @return size or {@linkplain #NOTFOUND} if not available
     */
    public static int size(Mark mark) {
        if (mark == null) {
            return NOTFOUND;
        }

        return Filters.asInt(mark.getSize());
    }

    /**
     * Retrieve linestring width from symbolizer if available.
     *
     * @param symbolizer Line symbolizer information.
     *
     * @return width or {@linkplain #NOTFOUND} if not available
     */
    public static int width(LineSymbolizer symbolizer) {
        return lineWidth(symbolizer);
    }

    /**
     * Retrieve the opacity from a LineSymbolizer object.
     *
     * @param symbolizer Line symbolizer information.
     *
     * @return double of the line symbolizer's opacity, or NaN if unavailable.
     */
    public static double lineOpacity(LineSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        Stroke stroke = symbolizer.getStroke();

        return opacity(stroke);
    }

    /**
     * Retrieve the opacity from a Stroke object.
     *
     * @param stroke the Stroke object.
     *
     * @return double of the line stroke's opacity, or NaN if unavailable.
     */
    public static double opacity(Stroke stroke) {
        if (stroke == null) {
            return Double.NaN;
        }
        return opacity( stroke.getOpacity() );
    }

    /**
     * Retrieve the opacity from a RasterSymbolizer object.
     *
     * @param symbolizer raster symbolizer information.
     *
     * @return double of the raster symbolizer's opacity, or 1.0 if unavailable.
     */
    public static double opacity(RasterSymbolizer rasterSymbolizer ){
        if( rasterSymbolizer == null ){
            return 1.0;
        }
        return opacity( rasterSymbolizer.getOpacity() );
    }

    /**
     * Retrieve the opacity value of an Expression.
     *
     * @param opacity Expression object
     *
     * @returna double value or 1.0 if unavailable.
     */
    private static double opacity( Expression opacity ) {
        if( opacity == null ){
            return 1.0;
        }
        double value = Filters.asDouble(opacity);
        return (Double.isNaN(value) ? 1.0 : value);
    }

    /**
     * Retrieves the linejoin from a LineSymbolizer.
     *
     * @param symbolizer Line symbolizer information.
     *
     * @return String of the linejoin, or null if unavailable.
     */
    public static String lineLinejoin(LineSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Stroke stroke = symbolizer.getStroke();

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
     *
     * @return String of the line stroke's line cap, or null if unavailable.
     */
    public static String lineLinecap(LineSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Stroke stroke = symbolizer.getStroke();

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
     *
     * @return float[] of the line dashes array, or null if unavailable.
     */
    public static float[] lineDash(LineSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Stroke stroke = symbolizer.getStroke();

        if (stroke == null) {
            return null;
        }

        float[] linedash = stroke.getDashArray();

        return linedash;
    }

    /**
     * Retrieves the location of the first external graphic in
     * a Style object.
     *
     * @param style SLD style information.
     *
     * @return Location of the first external graphic, or null if unavailabe.
     */
    public static URL pointGraphic(Style style) {
        PointSymbolizer point = pointSymbolizer(style);

        if (point == null) {
            return null;
        }

        Graphic graphic = point.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof ExternalGraphic) {
                ExternalGraphic externalGraphic = (ExternalGraphic) gs;
                try {
                    URL location = externalGraphic.getLocation(); // Should check format is supported by SWT
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
     *
     * @return the first Mark object or null if unavailable.
     */
    public static Mark pointMark(Style style) {
        if (style == null) {
            return null;
        }

        return mark(pointSymbolizer(style));
    }

    /**
     * Retrieves the first Mark used in a PointSymbolizer object.
     *
     * @param sym the PointSymbolizer
     *
     * @return the first Mark object or null if unavailable.
     */
    public static Mark mark(PointSymbolizer sym) {
        return mark(graphic(sym));
    }

    /**
     * Retrieves the first Mark object from the given Graphic object.
     *
     * @param graphic the Graphic object.
     *
     * @return a Mark object or null if unavailable.
     */
    public static Mark mark(Graphic graphic) {
        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                return (Mark) gs;
            }
        }

        return null;
    }

    public static Graphic graphic(PointSymbolizer sym) {
        if (sym == null) {
            return null;
        }

        return sym.getGraphic();
    }

    /**
     * Retrieves the size of the point's graphic, if found.
     * <p>
     * If you are using something fun like symbols you  will need to do your
     * own thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return size of the graphic or {@linkplain #NOTFOUND}
     */
    public static int pointSize(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        Graphic g = symbolizer.getGraphic();

        if (g == null) {
            return NOTFOUND;
        }

        Expression exp = g.getSize();
        return Filters.asInt(exp);
    }

    /**
     * Retrieves the well known name of the first Mark that has one in a
     * PointSymbolizer object
     * <p>
     * If you are using something fun like symbols you  will need to do your
     * own thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return well known name of the first Mark or null if unavailable.
     */
    public static String pointWellKnownName(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;
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
     *
     * @return well known name or null if unavailable.
     */
    public static String wellKnownName(Mark mark) {
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
     * Retrieves the color of the first Mark in a PointSymbolizer object.
     * This method is identical to {@linkplain #color(org.geotools.styling.PointSymbolizer)}.
     * <p>
     * If you are using something fun like symbols you  will need to do your
     * own thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return Color of the point's mark, or null if unavailable.
     */
    public static Color pointColor(PointSymbolizer symbolizer) {
        return color(symbolizer);
    }

    /**
     * Sets the Colour for the point symbolizer
     *
     * @param style
     * @param colour
     */
    public static void setPointColour(Style style, Color colour) {
        if (style == null) {
            return;
        }

        setPointColour(pointSymbolizer(style), colour);
    }

    /**
     * Sets the Colour for the given point symbolizer
     *
     * @param symbolizer the point symbolizer
     *
     * @param colour the new colour
     */
    public static void setPointColour(PointSymbolizer symbolizer, Color colour) {
        if (symbolizer == null || colour == null) {
            return;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            graphic = sf.createDefaultGraphic();
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;

                Stroke stroke = mark.getStroke();
                if (stroke == null) {
                    stroke = sf.createStroke(ff.literal(Color.BLACK), Stroke.DEFAULT.getWidth());
                    mark.setStroke(stroke);
                }

                Fill fill = mark.getFill();
                if (fill != null) {
                    fill.setColor(ff.literal(colour));
                }
            }
        }
    }

    /**
     * Retrieves the color from the first Mark in a PointSymbolizer object.
     * <p>
     * If you are using something fun like symbols you  will need to do your
     * own thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return Color of the point's mark, or null if unavailable.
     */
    public static Color color(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;
                Stroke stroke = mark.getStroke();
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
     * Retrieves the width of the first Mark with a Stroke that has a non-null
     * width.
     * <p>
     * If you are using something fun like symbols you  will need to do your
     * own thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return width of the points border or {@linkplain #NOTFOUND} if unavailable.
     */
    public static int pointWidth(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return NOTFOUND;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;
                Stroke stroke = mark.getStroke();
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
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return double of the point's border opacity, or NaN if unavailable.
     */
    public static double pointBorderOpacity(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return Double.NaN;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;
                Stroke stroke = mark.getStroke();
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
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return double of the point's opacity, or NaN if unavailable.
     */
    public static double pointOpacity(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return Double.NaN;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;
                Fill fill = mark.getFill();
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
     * <p>
     * If you are using something fun like symbols you  will need to do your
     * own thing.
     *
     * @param symbolizer Point symbolizer information.
     *
     * @return Color of the point's fill, or null if unavailable.
     */
    public static Color pointFill(PointSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Graphic graphic = symbolizer.getGraphic();

        if (graphic == null) {
            return null;
        }

        for (GraphicalSymbol gs : graphic.graphicalSymbols()) {
            if (gs != null && gs instanceof Mark) {
                Mark mark = (Mark) gs;
                Fill fill = mark.getFill();
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
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Polygon symbolizer information.
     *
     * @return outline width or {@linkplain #NOTFOUND} if unavailable.
     */
    public static int polyWidth(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return NOTFOUND;
        }

        Stroke stroke = symbolizer.getStroke();
        if (stroke != null) {
            return Filters.asInt(stroke.getWidth());
        }

        return NOTFOUND;
    }

    /**
     * Retrieves the outline (stroke) color from a PolygonSymbolizer.
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Polygon symbolizer information.
     *
     * @return Color of the polygon's stroke, or null if unavailable.
     */
    public static Color polyColor(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Stroke stroke = symbolizer.getStroke();

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
     * <p>
     * <b>Note:</b> This method will update the Style in place; some of the rules
     * and symbolizers will be replaced with modified copies.
     * All symbolizers associated with all rules are modified.
     * 
     * @param style the Style object to be updated
     * @param opacity - a new opacity value between 0 and 1
     */
    public static void setRasterOpacity(Style style, final double opacity) {
        if (style == null) {
            return;
        }
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            for (int i = 0; i < featureTypeStyle.rules().size(); i++) {
                Rule rule = featureTypeStyle.rules().get(i);

                DuplicatingStyleVisitor update = new DuplicatingStyleVisitor() {
                    @Override
                    public void visit(RasterSymbolizer raster) {

                        ChannelSelection channelSelection = copy(raster.getChannelSelection());
                        ColorMap colorMap = copy(raster.getColorMap());
                        ContrastEnhancement ce = copy(raster.getContrastEnhancement());
                        String geometryProperty = raster.getGeometryPropertyName();
                        Symbolizer outline = copy(raster.getImageOutline());
                        Expression overlap = copy(raster.getOverlap());
                        ShadedRelief shadedRelief = copy(raster.getShadedRelief());

                        Expression newOpacity = ff.literal(opacity);

                        RasterSymbolizer copy = sf.createRasterSymbolizer(geometryProperty, newOpacity, channelSelection,
                                overlap, colorMap, ce, shadedRelief, outline);

                        if (STRICT && !copy.equals(raster)) {
                            throw new IllegalStateException("Was unable to duplicate provided raster:" + raster);
                        }
                        pages.push(copy);
                    }
                };

                rule.accept(update);
                Rule updatedRule = (Rule) update.getCopy();
                featureTypeStyle.rules().set(i, updatedRule);
            }
        }
    }

    /**
     * Updates the raster channel selection in a Style object
     * <p>
     * This method will update the Style in place; some of the rules &
     * symbolizers will be replace with modified copies.
     * All symbolizes associated with all rules are updated.
     * 
     * @param style the Style object to be updated
     * @param rgb - an array of the new red, green, blue channels or null if setting the gray channel
     * @param gray - the new gray channel; ignored if rgb is not null
     */
    public static void setChannelSelection(Style style, final SelectedChannelType[] rgb, final SelectedChannelType gray) {
        if (style == null) {
            return;
        }
        for (FeatureTypeStyle featureTypeStyle : style.featureTypeStyles()) {
            for (int i = 0; i < featureTypeStyle.rules().size(); i++) {
                Rule rule = featureTypeStyle.rules().get(i);

                DuplicatingStyleVisitor update = new DuplicatingStyleVisitor() {

                    @Override
                    public void visit(RasterSymbolizer raster) {

                        ChannelSelection channelSelection = createChannelSelection();

                        ColorMap colorMap = copy(raster.getColorMap());
                        ContrastEnhancement ce = copy(raster.getContrastEnhancement());
                        String geometryProperty = raster.getGeometryPropertyName();
                        Symbolizer outline = copy(raster.getImageOutline());
                        Expression overlap = copy(raster.getOverlap());
                        ShadedRelief shadedRelief = copy(raster.getShadedRelief());

                        Expression opacity = copy(raster.getOpacity());

                        RasterSymbolizer copy = sf.createRasterSymbolizer(geometryProperty, opacity,
                                channelSelection, overlap, colorMap, ce,
                                shadedRelief, outline);
                        if (STRICT && !copy.equals(raster)) {
                            throw new IllegalStateException("Was unable to duplicate provided raster:" + raster);
                        }
                        pages.push(copy);
                    }

                    private ChannelSelection createChannelSelection() {
                        if (rgb == null) {
                            return sf.createChannelSelection(new SelectedChannelType[]{gray});
                        } else {
                            return sf.createChannelSelection(rgb);
                        }
                    }
                };

                rule.accept(update);
                Rule updatedRule = (Rule) update.getCopy();
                featureTypeStyle.rules().set(i, updatedRule);
            }
        }
    }
    
    /**
     * Sets the colour for the first polygon symbolizer defined in
     * the provided style
     *
     * @param style the Style object
     * @param colour the colour for polygon outlines and fill
     */
    public static void setPolyColour(Style style, Color colour) {
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
    public static void setPolyColour(PolygonSymbolizer symbolizer, Color colour) {
        if (symbolizer == null || colour == null) {
            return;
        }

        Expression colourExp = ff.literal(colour);
        Stroke stroke = symbolizer.getStroke();
        if (stroke == null) {
            stroke = sf.createStroke(colourExp, Stroke.DEFAULT.getWidth());
            symbolizer.setStroke(stroke);
        } else {
            stroke.setColor(ff.literal(colour));
        }

        Fill fill = symbolizer.getFill();
        if (fill != null) {
            fill.setColor(colourExp);
        }
    }

    /**
     * Retrieves the fill colour from a PolygonSymbolizer.
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Polygon symbolizer information.
     *
     * @return Color of the polygon's fill, or null if unavailable.
     */
    public static Color polyFill(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Fill fill = symbolizer.getFill();

        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Retrieves the border (stroke) opacity from a PolygonSymbolizer.
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Polygon symbolizer information.
     *
     * @return double of the polygon's border opacity, or NaN if unavailable.
     */
    public static double polyBorderOpacity(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        Stroke stroke = symbolizer.getStroke();
        if (stroke == null) {
            return Double.NaN;
        }

        return opacity(stroke);
    }

    /**
     * Retrieves the fill opacity from the first PolygonSymbolizer.
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     *
     * @param symbolizer Polygon symbolizer information.
     *
     * @return double of the polygon's fill opacity, or NaN if unavailable.
     */
    public static double polyFillOpacity(PolygonSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        Fill fill = symbolizer.getFill();

        return opacity(fill);
    }

    /**
     * Retrieve the opacity from the provided fill; or return the default.
     * @param fill
     * @return opacity from the above fill; or return the Fill.DEFAULT value
     */
    public static double opacity(Fill fill) {
        if (fill == null) {
            fill = Fill.DEFAULT;
        }

        Expression opacityExp = fill.getOpacity();
        if( opacityExp == null ){
            opacityExp = Fill.DEFAULT.getOpacity();
        }

        return Filters.asDouble(opacityExp);
    }

    /**
     * Retrieves the opacity from a RasterSymbolizer.
     * <p>
     * If you are using something fun like rules you  will need to do your own
     * thing.
     * </p>
     *
     * @param symbolizer Raster symbolizer information.
     * @return opacity of the first RasterSymbolizer or NaN if unavailable.
     */
    public static double rasterOpacity(RasterSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        return Filters.asDouble(symbolizer.getOpacity());
    }

    /**
     * Retrieves the opacity from the first RasterSymbolizer defined
     * in a style.
     *
     * @param style the Style object
     *
     * @return opacity of the raster symbolizer or NaN if unavailable.
     */
    public static double rasterOpacity(Style style) {
        return rasterOpacity(rasterSymbolizer(style));
    }

    /**
     * Retrieve the first TextSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts FeatureTypeStyle information.
     *
     * @return TextSymbolizer, or null if not found.
     */
    public static TextSymbolizer textSymbolizer(FeatureTypeStyle fts) {
        return (TextSymbolizer) symbolizer(fts, TextSymbolizer.class);
    }

    /**
     * Retrieve the first TextSymbolizer from the provided Style.
     *
     * @param style the Style object
     *
     * @return TextSymbolizer, or null if not found.
     */
    public static TextSymbolizer textSymbolizer(Style style) {
        return (TextSymbolizer) symbolizer(style, TextSymbolizer.class);
    }

    /**
     * Retrieves the font from a TextSymbolizer. Equivalent to
     * {@code symbolizer.getFont()}.
     *
     * @param symbolizer the symbolizer
     *
     * @return the first font defined in the symbolizer or null if unavailable.
     */
    public static Font font( TextSymbolizer symbolizer ) {
        if(symbolizer == null) {
            return null;
        }
        return symbolizer.getFont();
    }

    /**
     * Retrieves the label from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     *
     * @return Expression of the label's text, or null if unavailable.
     */
    public static Expression textLabel(TextSymbolizer symbolizer) {
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
     *
     * @return the label's text, or null if unavailable.
     */
    public static String textLabelString(TextSymbolizer sym) {
        Expression exp = textLabel(sym);

        return (exp == null) ? null : exp.toString();
    }

    /**
     * Retrieves the colour of the font fill from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     *
     * @return Color of the font's fill, or null if unavailable.
     */
    public static Color textFontFill(TextSymbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }

        Fill fill = symbolizer.getFill();

        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Retrieves the colour of the halo fill a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     *
     * @return Color of the halo's fill, or null if unavailable.
     */
    public static Color textHaloFill(TextSymbolizer symbolizer) {
        Halo halo = symbolizer.getHalo();

        if (halo == null) {
            return null;
        }

        Fill fill = halo.getFill();

        if (fill == null) {
            return null;
        }

        return color(fill.getColor());
    }

    /**
     * Retrieves the halo width from a TextSymbolizer.
     *
     * @param symbolizer Text symbolizer information.
     *
     * @return the halo's width, or 0 if unavailable.
     */
    public static int textHaloWidth(TextSymbolizer symbolizer) {
        Halo halo = symbolizer.getHalo();

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
     *
     * @return double of the halo's opacity, or NaN if unavailable.
     */
    public static double textHaloOpacity(TextSymbolizer symbolizer) {
        if (symbolizer == null) {
            return Double.NaN;
        }

        Halo halo = symbolizer.getHalo();

        if (halo == null) {
            return Double.NaN;
        }

        Fill fill = halo.getFill();

        if (fill == null) {
            return Double.NaN;
        }

        Expression expr = fill.getOpacity();
        if( expr == null ){
            return Double.NaN;
        }

        return Filters.asDouble(expr);
    }

    /**
     * Navigate through the expression finding the first mentioned Color.
     * <p>
     * If you have a specific Feature in mind please use:
     * <pre><code>
     * Object value = expr.getValue( feature );
     * return value instanceof Color ? (Color) value : null;
     * </code></pre>
     *
     * @param expr the Expression object
     *
     * @return First available color, or null.
     */
    public static Color color(Expression expr) {
        if (expr == null) {
            return null;
        }
        return expr.evaluate(null, Color.class );
    }

    /**
     * Retrieve the first RasterSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle information.
     *
     * @return RasterSymbolizer, or null if not found.
     */
    public static RasterSymbolizer rasterSymbolizer(FeatureTypeStyle fts) {
        return (RasterSymbolizer) symbolizer(fts, RasterSymbolizer.class);
    }

    /**
     * Retrieve the first RasterSymbolizer from the provided Style.
     *
     * @param style the Style object
     *
     * @return RasterSymbolizer, or null if not found.
     */
    public static RasterSymbolizer rasterSymbolizer(Style style) {
        return (RasterSymbolizer) symbolizer(style, RasterSymbolizer.class);
    }

    /**
     * Retrieve the first LineSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle object
     *
     * @return LineSymbolizer, or null if not found.
     */
    public static LineSymbolizer lineSymbolizer(FeatureTypeStyle fts) {
        return (LineSymbolizer) symbolizer(fts, LineSymbolizer.class);
    }

    /**
     * Retrieve the first LineSymbolizer from the provided Style.
     *
     * @param style the Style object
     *
     * @return LineSymbolizer, or null if not found.
     */
    public static LineSymbolizer lineSymbolizer(Style style) {
        return (LineSymbolizer) symbolizer(style, LineSymbolizer.class);
    }

    /**
     * Retrieves the Stroke from a LineSymbolizer.
     * @param sym the symbolizer
     * @return the Stroke or null if not found.
     */
    public static Stroke stroke(LineSymbolizer sym) {
        if (sym == null) {
            return null;
        }

        return sym.getStroke();
    }

    /**
     * Retrieves the Stroke from a PolygonSymbolizer.
     * @param sym the symbolizer
     * @return the Stroke or null if not found.
     */
    public static Stroke stroke(PolygonSymbolizer sym) {
        if (sym == null) {
            return null;
        }

        return sym.getStroke();
    }

    /**
     * Retrieves the Stroke from a PointSymbolizer.
     * @param sym the symbolizer
     * @return the Stroke or null if not found.
     */
    public static Stroke stroke(PointSymbolizer sym) {
        Mark mark = mark(sym);

        return (mark == null) ? null : mark.getStroke();
    }

    /**
     * Retrieves the Fill from a PolygonSymbolizer.
     * @param sym the symbolizer
     * @return the Fill or null if not found.
     */
    public static Fill fill(PolygonSymbolizer sym) {
        if (sym == null) {
            return null;
        }

        return sym.getFill();
    }

    /**
     * Retrieves the Fill from a PointSymbolizer.
     * @param sym the symbolizer
     * @return the Fill or null if not found.
     */
    public static Fill fill(PointSymbolizer sym) {
        Mark mark = mark(sym);

        return (mark == null) ? null : mark.getFill();
    }

    /**
     * Retrieve the first PointSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts the FeatureTypeStyle object
     *
     * @return PointSymbolizer, or null if not found.
     */
    public static PointSymbolizer pointSymbolizer(FeatureTypeStyle fts) {
        return (PointSymbolizer) symbolizer(fts, PointSymbolizer.class);
    }

    /**
     * Retrieve the first PointSymbolizer from the provided Style.
     *
     * @param style the Style object
     *
     * @return PointSymbolizer, or null if not found.
     */
    public static PointSymbolizer pointSymbolizer(Style style) {
        return (PointSymbolizer) symbolizer(style, PointSymbolizer.class);
    }

    /**
     * Retrieve the first PolygonSymbolizer from the provided FeatureTypeStyle.
     *
     * @param fts FeatureTypeStyle object.
     *
     * @return PolygonSymbolizer, or null if not found.
     */
    public static PolygonSymbolizer polySymbolizer(FeatureTypeStyle fts) {
        return (PolygonSymbolizer) symbolizer(fts, PolygonSymbolizer.class);
    }

    /**
     * Retrieve the first PolygonSymbolizer from the provided Style.
     *
     * @param style the Style object
     *
     * @return PolygonSymbolizer, or null if not found.
     */
    public static PolygonSymbolizer polySymbolizer(Style style) {
        return (PolygonSymbolizer) symbolizer(style, PolygonSymbolizer.class);
    }

    /**
     * Returns the feature type style in the style which matched a particular
     * name.
     *
     * @param style the Style object
     * @param type the feature type (must be non-null)
     *
     * @return The FeatureTypeStyle object if it exists, otherwise false.
     */
    public static FeatureTypeStyle featureTypeStyle(Style style, SimpleFeatureType type) {
        if (style == null) {
            return null;
        }

        if ((type == null) || (type.getTypeName() == null)) {
            return null;
        }

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (type.getTypeName().equalsIgnoreCase(fts.getName())) {
                return fts;
            }
        }

        return null;
    }

    /**
     * Returns the first style object which matches a given schema.
     *
     * @param styles Array of style objects.
     * @param schema Feature schema.
     *
     * @return The first object to match the feature type, otherwise null if no
     *         match.
     */
    public static Style matchingStyle(Style[] styles, SimpleFeatureType schema) {
        if ((styles == null) || (styles.length == 0)) {
            return null;
        }

        for (int i = 0; i < styles.length; i++) {
            Style style = styles[i];

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
     * @param SYMBOLIZER LineSymbolizer.class, PointSymbolizer.class, 
     *        PolygonSymbolizer.class, RasterSymbolizer.class, or TextSymbolizer.class
     *
     * @return symbolizer instance from style, or null if not found.
     */
    protected static Symbolizer symbolizer(Style style, final Class SYMBOLIZER) {
        if (style == null) {
            return null;
        }

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            Symbolizer sym = symbolizer(fts, SYMBOLIZER);
            if (sym != null) {
                return sym;
            }
        }

        return null;
    }

    /**
     * Retrieve the first Symbolizer from the provided FeatureTypeStyle.
     *
     * @param fts        the FeatureTypeStyle SLD style information.
     * @param SYMBOLIZER LineSymbolizer.class, PointSymbolizer.class, 
     *                   PolygonSymbolizer.class, RasterSymbolizer.class, 
     *                   or TextSymbolizer.class
     *        
     * @return symbolizer instance from fts, or null if not found.
     */
    protected static Symbolizer symbolizer(FeatureTypeStyle fts, final Class SYMBOLIZER) {
        if (fts == null) {
            return null;
        }

        for (Rule rule : fts.rules()) {
            for (Symbolizer sym : rule.symbolizers()) {
                if (SYMBOLIZER.isInstance(sym)) {
                    return sym;
                }
            }
        }

        return null;
    }

    /**
     * Convert a Color object to hex representation
     * @param c the Color object
     * @return a hex String
     */
    public static String colorToHex(Color c) {
    	return "#" + Integer.toHexString(c.getRGB() & 0x00ffffff);
    }

    /**
     * Get the Styles defined in the given StyledLayerDescriptor
     * @param sld the StyledLayerDescriptor object
     * @return an array of Styles
     */
    public static Style[] styles(StyledLayerDescriptor sld) {
        StyledLayer[] layers = sld.getStyledLayers();
        List<Style> styles = new ArrayList<Style>();

        for (int i = 0; i < layers.length; i++) {
            if (layers[i] instanceof UserLayer) {
                UserLayer layer = (UserLayer) layers[i];
                styles.addAll(layer.userStyles());

            } else if (layers[i] instanceof NamedLayer) {
                NamedLayer layer = (NamedLayer) layers[i];
                styles.addAll(layer.styles());
            }
        }

        return styles.toArray(new Style[0]);
    }
    
    /**
     * Get the FeatureTypeStyles defined in the given StyledLayerDescriptor
     * @param sld the StyledLayerDescriptor object
     * @return an array of FeatureTypeStyles
     */
    public static FeatureTypeStyle[] featureTypeStyles(StyledLayerDescriptor sld) {
        Style[] style = styles(sld);
        List<FeatureTypeStyle> fts = new ArrayList<FeatureTypeStyle>();
        for (int i = 0; i < style.length; i++) {
            fts.addAll(style[i].featureTypeStyles());
        }
        return fts.toArray(new FeatureTypeStyle[0]);
    }

    /**
     * Retrieve the first FeatureTypeStyle defined in the given StyledLayerDescriptor
     * object that matches the specified feature type
     *
     * @param sld a StyledLayerDescriptor object
     * @param type the feature type to match
     * @return a FeatureTypeStyle or null if there was no match
     */
    public static FeatureTypeStyle featureTypeStyle(StyledLayerDescriptor sld, SimpleFeatureType type) {
        //alternatively, we could use a StyleVisitor here
        Style[] styles = styles(sld);
        for (int i = 0; i < styles.length; i++) {
            for (FeatureTypeStyle fts : styles[i].featureTypeStyles()) {
                if (type.getTypeName().equalsIgnoreCase(fts.getName())) {
                    return fts;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve the default style from the given StyledLayerDescriptor.
     *
     * @param sld the StyledLayerDescriptor object
     *
     * @return the default style; or the first style if no default is
     *         defined; or null if there are not styles
     */
    public static Style defaultStyle(StyledLayerDescriptor sld) {
        Style[] style = styles(sld);
        for (int i = 0; i < style.length; i++) {
            if (style[i].isDefault()) {
                return style[i];
            }
        }
        //no default, so just grab the first one
        if (style.length == 0) {
            return null;
        }
        return style[0];
    }

    /**
     * Retrieves all filters defined in a rule
     *
     * @param rule the rule
     *
     * @return array of filters
     */
    public static Filter[] filters(Rule[] rule) {
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
     *
     * @return array of filters
     */
    public static Filter[] filters(Style style) {
        Rule[] rule = rules(style);
        return filters(rule);
    }

    /**
     * Retrieves all rules defined in a style
     *
     * @param style the style
     *
     * @return an array of unique rules
     */
    public static Rule[] rules(Style style) {
        Set<Rule> ruleSet = new HashSet<Rule>();
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            ruleSet.addAll(fts.rules());
        }

        if (ruleSet.size() > 0) {
            return ruleSet.toArray(new Rule[0]);
        } else {
            return new Rule[0];
        }
    }

    /**
     * Retrieves all symbolizers defined in a style
     *
     * @param style the style
     *
     * @return an array of unique symbolizers
     */
    public static Symbolizer[] symbolizers(Style style) {
        Set<Symbolizer> symbolizers = new HashSet<Symbolizer>();
        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            for (Rule rule : fts.rules()) {
                symbolizers.addAll(rule.symbolizers());
            }
        }

        if (symbolizers.size() > 0) {
            return symbolizers.toArray(new Symbolizer[0]);
        } else {
            return new Symbolizer[0];
        }
    }

    /**
     * Retrieves all symbolizers defined in a rule
     *
     * @param rule the rule
     *
     * @return an array of unique symbolizers
     */
    public static Symbolizer[] symbolizers(Rule rule) {
        Set<Symbolizer> symbolizers = new HashSet<Symbolizer>();
        symbolizers.addAll(rule.symbolizers());

        if (symbolizers.size() > 0) {
            return symbolizers.toArray(new Symbolizer[0]);
        } else {
            return new Symbolizer[0];
        }
    }

    /**
     * Retrieves all colour names defined in a style
     * @param style the style
     * @return an array of unique colour names
     */
    public static String[] colors(Style style) {
        Set<String> colorSet = new HashSet<String>();

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            for (Rule rule : fts.rules()) {
                String[] color = colors(rule);
                for (int j = 0; j < color.length; j++) {
                    colorSet.add(color[j]);
                }
            }
        }

        if (colorSet.size() > 0) {
            return colorSet.toArray(new String[0]);
        } else {
            return new String[0];
        }
    }

    /**
     * Retrieves all colour names defined in a rule
     * @param rule the rule
     * @return an array of unique colour names
     */
    public static String[] colors(Rule rule) {
        Set<String> colorSet = new HashSet<String>();

        Color color = null;
        for (Symbolizer sym : rule.symbolizers()) {
            if (sym instanceof PolygonSymbolizer) {
                PolygonSymbolizer symb = (PolygonSymbolizer) sym;
                color = polyFill(symb);

            } else if (sym instanceof LineSymbolizer) {
                LineSymbolizer symb = (LineSymbolizer) sym;
                color = color(symb);

            } else if (sym instanceof PointSymbolizer) {
                PointSymbolizer symb = (PointSymbolizer) sym;
                color = color(symb);
            }

            if (color != null) {
                colorSet.add(color.toString());
            }
        }

        if (colorSet.size() > 0) {
            return colorSet.toArray(new String[0]);
        } else {
            return new String[0];
        }
    }

    /**
     * Converts a java.awt.Color into an HTML Colour
     *
     * @param color color to convert
     *
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
     *
     * @return a new Color object
     */
    public static Color toColor(String htmlColor) {
        return new Color(Integer.parseInt(htmlColor.substring(1), 16));
    }

    /**
     * Test if the given FeatureTypeStyle object matches a regular expression
     * @param fts the feature type style
     * @param regex regular expression to match
     * @return true if a match is found; false otherwise
     */
    public static boolean isSemanticTypeMatch( FeatureTypeStyle fts, String regex ) {
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
     *
     * @return min scale or 0 if no min scale is set
     */
    public static double minScale( FeatureTypeStyle fts ) {
        if(fts == null || fts.rules().isEmpty())
            return 0.0;

        return fts.rules().get(0).getMinScaleDenominator();
    }

    /**
     * Returns the max scale of the default (first) rule
     *
     * @param fts the feature type style
     *
     * @return min scale or NaN if no max scale is set
     */
    public static double maxScale( FeatureTypeStyle fts ) {
        if(fts == null || fts.rules().isEmpty())
            return Double.NaN;

        return fts.rules().get(0).getMaxScaleDenominator();
    }

    /**
     * Create a PointPlacement object.
     *
     * @param horizAlign horizontal alignment (0 to 1)
     * @param vertAlign vertical alignment (0 to 1)
     * @param rotation rotation angle in degrees
     *
     * @return a new PointPlacement object
     */
    public static PointPlacement getPlacement( double horizAlign, double vertAlign, double rotation ) {
        AnchorPoint anchorPoint = sf.createAnchorPoint(ff.literal(horizAlign), ff.literal(vertAlign));
        return sf.createPointPlacement(anchorPoint, null, ff.literal(rotation));
    }

    /**
     * Create a minimal style to render features of type {@code typeName}
     * read from the given data store
     *
     * @param store the data store containing the features
     *
     * @param typeName the feature type to create the style for
     *
     * @param color single color to use for all components of the Style
     *
     * @return a new Style instance
     *
     * @throws java.io.IOException if the data store cannot be accessed
     */
    public static Style createSimpleStyle(AbstractDataStore store, String typeName, Color color) throws IOException {
        SimpleFeatureType type = store.getSchema(typeName);
        return createSimpleStyle(type, color);
    }

    /**
     * Create a minimal style to render features of type {@code type}.
     *
     * @param type the feature type
     *
     * @return a new Style instance
     */
    public static Style createSimpleStyle(FeatureType type) {
        return createSimpleStyle(type, Color.BLACK);
    }

    /**
     * Create a minimal style to render features of type {@code type}
     *
     * @param store the data store containing the features
     *
     * @param typeName the feature type to create the style for
     *
     * @param color single color to use for all components of the Style
     *
     * @return a new Style instance
     *
     * @throws java.io.IOException if the data store cannot be accessed
     */
    public static Style createSimpleStyle(FeatureType type, Color color) {
        GeometryDescriptor desc = type.getGeometryDescriptor();
        Class<?> clazz = desc.getType().getBinding();
        Color fillColor = null;

        if (Polygon.class.isAssignableFrom(clazz) ||
                MultiPolygon.class.isAssignableFrom(clazz)) {
            if (color.equals(Color.BLACK)) {
                fillColor = null;
            } else {
                fillColor = color;
            }
            return createPolygonStyle(color, fillColor, 0.5f);

        } else if (LineString.class.isAssignableFrom(clazz) ||
                MultiLineString.class.isAssignableFrom(clazz)) {
            return createLineStyle(color, 1.0f);

        } else if (Point.class.isAssignableFrom(clazz) ||
                MultiPoint.class.isAssignableFrom(clazz)) {
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
     *
     * @return a new Style instance
     */
    public static Style createPolygonStyle(Color outlineColor, Color fillColor, float opacity) {
        Stroke stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(1.0f));
        Fill fill = Fill.NULL;
        if (fillColor != null) {
            fill = sf.createFill(ff.literal(fillColor), ff.literal(opacity));
        }
        return wrapSymbolizers( sf.createPolygonSymbolizer(stroke, fill, null) );
    }

    /**
     * Create a polygon style with the given colors, opacity and optional labels.
     *
     * @param outlineColor color of polygon outlines
     * @param fillColor color for the fill
     * @param opacity proportional opacity (0 to 1)
     *
     * @param labelField name of the feature field (attribute) to use for labelling;
     *        mauy be {@code null} for no labels
     *
     * @param labelFont GeoTools Font object to use for labelling; if {@code null}
     *        and {@code labelField} is not {@code null} the default font will be
     *        used
     *
     * @return a new Style instance
     */
    public static Style createPolygonStyle(Color outlineColor, Color fillColor, float opacity,
            String labelField, Font labelFont) {
        Stroke stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(1.0f));
        Fill fill = Fill.NULL;
        if (fillColor != null) {
            fill = sf.createFill(ff.literal(fillColor), ff.literal(opacity));
        }
        PolygonSymbolizer polySym = sf.createPolygonSymbolizer(stroke, fill, null);

        if (labelField == null) {
            return wrapSymbolizers( polySym );

        } else {
            Font font = (labelFont == null ? sf.getDefaultFont() : labelFont);
            Fill labelFill = sf.createFill(ff.literal(Color.BLACK));

            TextSymbolizer textSym = sf.createTextSymbolizer(
                    labelFill, new Font[]{font}, null, ff.property(labelField), null, null);

            return wrapSymbolizers( polySym, textSym );
        }
    }

    /**
     * Create a line style with given color and line width
     *
     * @param lineColor color of lines
     * @param width width of lines
     *
     * @return a new Style instance
     */
    public static Style createLineStyle(Color lineColor, float width) {
        Stroke stroke = sf.createStroke(ff.literal(lineColor), ff.literal(width));
        return wrapSymbolizers( sf.createLineSymbolizer(stroke, null) );
    }

    /**
     * Create a line style with given color, line width and optional labels
     *
     * @param lineColor color of lines
     * @param width width of lines
     *
     * @param labelField name of the feature field (attribute) to use for labelling;
     *        mauy be {@code null} for no labels
     *
     * @param labelFont GeoTools Font object to use for labelling; if {@code null}
     *        and {@code labelField} is not {@code null} the default font will be
     *        used
     *
     * @return a new Style instance
     */
    public static Style createLineStyle(Color lineColor, float width,
            String labelField, Font labelFont) {
        Stroke stroke = sf.createStroke(ff.literal(lineColor), ff.literal(width));
        LineSymbolizer lineSym = sf.createLineSymbolizer(stroke, null);

        if (labelField == null) {
            return wrapSymbolizers( lineSym );

        } else {
            Font font = (labelFont == null ? sf.getDefaultFont() : labelFont);
            Fill labelFill = sf.createFill(ff.literal(Color.BLACK));

            TextSymbolizer textSym = sf.createTextSymbolizer(
                    labelFill, new Font[]{font}, null, ff.property(labelField), null, null);

            return wrapSymbolizers( lineSym, textSym );
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
     *
     * @return a new Style instance
     */
    public static Style createPointStyle(
            String wellKnownName,
            Color lineColor,
            Color fillColor,
            float opacity,
            float size) {

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
     *
     * @param labelField name of the feature field (attribute) to use for labelling;
     *        mauy be {@code null} for no labels
     *
     * @param labelFont GeoTools Font object to use for labelling; if {@code null}
     *        and {@code labelField} is not {@code null} the default font will be
     *        used
     *
     * @return a new Style instance
     */
    public static Style createPointStyle(
            String wellKnownName,
            Color lineColor,
            Color fillColor,
            float opacity,
            float size,
            String labelField,
            Font labelFont) {

        Stroke stroke = sf.createStroke(ff.literal(lineColor), ff.literal(1.0f));
        Fill fill = Fill.NULL;
        if (fillColor != null) {
            fill = sf.createFill(ff.literal(fillColor), ff.literal(opacity));
        }

        Mark mark = sf.createMark(ff.literal(wellKnownName),
                stroke, fill, ff.literal(size), ff.literal(0));

        Graphic graphic = sf.createDefaultGraphic();
        graphic.graphicalSymbols().clear();
        graphic.graphicalSymbols().add(mark);
        graphic.setSize(ff.literal(size));

        PointSymbolizer pointSym = sf.createPointSymbolizer(graphic, null);

        if (labelField == null) {
            return wrapSymbolizers( pointSym );

        } else {
            Font font = (labelFont == null ? sf.getDefaultFont() : labelFont);
            Fill labelFill = sf.createFill(ff.literal(Color.BLACK));
            AnchorPoint anchor = sf.createAnchorPoint(ff.literal(0.5), ff.literal(0.0));
            Displacement disp = sf.createDisplacement(ff.literal(0), ff.literal(5));
            LabelPlacement placement = sf.createPointPlacement(anchor, disp, ff.literal(0));

            TextSymbolizer textSym = sf.createTextSymbolizer(
                    labelFill, new Font[]{font}, null, ff.property(labelField), placement, null);

            return wrapSymbolizers( pointSym, textSym );
        }

    }

    /**
     * Wrap one or more symbolizers into a Rule / FeatureTypeStyle / Style
     *
     * @param symbolizers one or more symbolizer objects
     *
     * @return a new Style instance or null if no symbolizers are provided
     */
    public static Style wrapSymbolizers(Symbolizer ...symbolizers) {
        if (symbolizers == null || symbolizers.length == 0) {
            return null;
        }

        Rule rule = sf.createRule();

        for (Symbolizer sym : symbolizers) {
            rule.symbolizers().add(sym);
        }

        FeatureTypeStyle fts = sf.createFeatureTypeStyle(new Rule[] {rule});

        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

}
