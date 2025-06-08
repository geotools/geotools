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
import java.util.Arrays;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Halo;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbol;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.filter.IllegalFilterException;
import org.geotools.util.factory.GeoTools;

/**
 * An utility class designed to ease style building with convenience methods.
 *
 * @author aaime
 */
public class StyleBuilder {
    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyleBuilder.class);
    public static final String LINE_JOIN_MITRE = "mitre";
    public static final String LINE_JOIN_ROUND = "round";
    public static final String LINE_JOIN_BEVEL = "bevel";
    public static final String LINE_CAP_BUTT = "butt";
    public static final String LINE_CAP_ROUND = "round";
    public static final String LINE_CAP_SQUARE = "square";
    public static final String MARK_SQUARE = "square";
    public static final String MARK_CIRCLE = "circle";
    public static final String MARK_TRIANGLE = "triangle";
    public static final String MARK_STAR = "star";
    public static final String MARK_CROSS = "cross";
    public static final String MARK_ARROW = "arrow";
    public static final String MARK_X = "x";
    public static final String FONT_STYLE_NORMAL = "normal";
    public static final String FONT_STYLE_ITALIC = "italic";
    public static final String FONT_STYLE_OBLIQUE = "oblique";
    public static final String FONT_WEIGHT_NORMAL = "normal";
    public static final String FONT_WEIGHT_BOLD = "bold";

    private StyleFactory sf;
    private FilterFactory ff;

    /** use the default StyleFactory and FilterFactory */
    public StyleBuilder() {
        this(CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints()));
    }

    /**
     * Use the supplied StyleFactory when building styles
     *
     * @param styleFactory the StyleFactory to use in building Styles
     */
    public StyleBuilder(StyleFactory styleFactory) {
        this(styleFactory, CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    /**
     * Use the supplied FilterFactory when building styles
     *
     * @param filterFactory Use this FilterFactory to build the style
     */
    public StyleBuilder(FilterFactory filterFactory) {
        this(CommonFactoryFinder.getStyleFactory(GeoTools.getDefaultHints()), filterFactory);
    }

    /**
     * Use the supplied StyleFactory and FilterFactory when building styles
     *
     * @param styleFactory the StyleFactory to use
     * @param filterFactory the FilterFactory to use
     */
    public StyleBuilder(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.sf = styleFactory;
        this.ff = filterFactory;
    }

    /** Documented setter injection, StyleBuilder uses a StyleFactory for creation. */
    public void setStyleFactory(StyleFactory factory) {
        sf = factory;
    }
    /**
     * getter for StyleFactory
     *
     * @return the StyleFactory being used
     */
    public StyleFactory getStyleFactory() {
        return sf;
    }

    /** Documented setter injection, StyleBuilder uses a StyleFactory for creation. */
    public void setFilterFactory(FilterFactory factory) {
        ff = factory;
    }

    /**
     * getter for filterFactory
     *
     * @return the FilterFactory being used
     */
    public FilterFactory getFilterFactory() {
        return ff;
    }

    /**
     * create a default Stroke
     *
     * @return the Stroke created
     */
    public Stroke createStroke() {
        return sf.getDefaultStroke();
    }

    /**
     * create a default stroke with the supplied width
     *
     * @param width the width of the line
     * @return the stroke created
     */
    public Stroke createStroke(double width) {
        return createStroke(Color.BLACK, width);
    }

    /**
     * Create a default stroke with the supplied color
     *
     * @param color the color of the line
     * @return the created stroke
     */
    public Stroke createStroke(Color color) {
        return createStroke(color, 1);
    }

    /**
     * create a stroke with the supplied width and color
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the created stroke
     */
    public Stroke createStroke(Color color, double width) {
        return sf.createStroke(colorExpression(color), literalExpression(width));
    }

    /**
     * create a stroke with color, width, linejoin type and lineCap type.
     *
     * @param color the color of the line
     * @param width the width of the line
     * @param lineJoin the type of join to be used at points along the line
     * @param lineCap the type of cap to be used at the end of the line
     * @return the stroke created
     */
    public Stroke createStroke(Color color, double width, String lineJoin, String lineCap) {
        Stroke stroke = createStroke(color, width);
        stroke.setLineJoin(literalExpression(lineJoin));
        stroke.setLineCap(literalExpression(lineCap));

        return stroke;
    }

    /**
     * create a dashed line of color and width
     *
     * @param color the color of the line
     * @param width the width of the line
     * @param dashArray an array of floats describing the length of line and spaces
     * @return the stroke created
     */
    public Stroke createStroke(Color color, double width, float[] dashArray) {
        Stroke stroke = createStroke(color, width);
        stroke.setDashArray(dashArray);

        return stroke;
    }

    /**
     * create a stroke with the color and width supplied
     *
     * @param color an Expression representing the color of the line
     * @param width an Expression representing the width of the line
     * @return the Stroke created
     */
    public Stroke createStroke(Expression color, Expression width) {
        return sf.createStroke(color, width);
    }

    /**
     * create a stroke with color, width and opacity supplied
     *
     * @param color the color of the line
     * @param width the width of the line
     * @param opacity the opacity or <I>see throughness</I> of the line, 0 - is transparent, 1 is completely drawn
     * @return the stroke created
     */
    public Stroke createStroke(Color color, double width, double opacity) {
        return sf.createStroke(colorExpression(color), literalExpression(width), literalExpression(opacity));
    }

    /**
     * create a stroke with color, width and opacity supplied
     *
     * @param color an Expression representing the color of the line
     * @param width an Expression representing the width of the line
     * @param opacity an Expression representing opacity the opacity or <I>see throughness</I> of the line, 0 - is
     *     transparent, 1 is completely drawn
     * @return the stroke created
     */
    public Stroke createStroke(Expression color, Expression width, Expression opacity) {
        return sf.createStroke(color, width, opacity);
    }

    /**
     * create a default fill 50% gray
     *
     * @return the fill created
     */
    public Fill createFill() {
        Fill f = sf.getDefaultFill();
        f.setColor(literalExpression("#808080"));
        f.setOpacity(literalExpression(1.0));

        return f;
    }

    /**
     * create a fill of color
     *
     * @param fillColor the color of the fill
     * @return the fill created
     */
    public Fill createFill(Color fillColor) {
        return createFill(colorExpression(fillColor));
    }

    /**
     * create a fill of color
     *
     * @param fillColor an Expression representing the color of the fill
     * @return the fill constructed
     */
    public Fill createFill(Expression fillColor) {
        return sf.createFill(fillColor);
    }

    /**
     * create a fill with the supplied color and opacity
     *
     * @param fillColor the color to fill with
     * @param opacity the opacity of the fill 0 - transparent, 1 - completly filled
     * @return the fill created
     */
    public Fill createFill(Color fillColor, double opacity) {
        return sf.createFill(colorExpression(fillColor), literalExpression(opacity));
    }

    /**
     * create a fill with the supplied color and opacity
     *
     * @param color an expression representing the color to fill with
     * @param opacity an expression representing the opacity of the fill 0 - transparent, 1 - completly filled
     * @return the fill created
     */
    public Fill createFill(Expression color, Expression opacity) {
        return sf.createFill(color, opacity);
    }

    /**
     * create a fill with color, background color and opacity supplied and uses the graphic supplied for the fill
     *
     * @param color the foreground color
     * @param backgroundColor the background color
     * @param opacity the opacity of the fill
     * @param fill the graphic object to use to fill the fill
     * @return the fill created
     */
    public Fill createFill(Color color, Color backgroundColor, double opacity, Graphic fill) {
        return sf.createFill(
                colorExpression(color), colorExpression(backgroundColor), literalExpression(opacity), fill);
    }

    /**
     * create a fill with color, background color and opacity supplied and uses the graphic supplied for the fill
     *
     * @param color an Expression representing the foreground color
     * @param backgroundColor an Expression representing the background color
     * @param opacity an Expression representing the opacity of the fill
     * @param fill the graphic object to use to fill the fill
     * @return the fill created
     */
    public Fill createFill(Expression color, Expression backgroundColor, Expression opacity, Graphic fill) {
        return sf.createFill(color, backgroundColor, opacity, fill);
    }

    /** Returns the array of all the well known mark names */
    public String[] getWellKnownMarkNames() {
        return new String[] {MARK_SQUARE, MARK_CIRCLE, MARK_TRIANGLE, MARK_STAR, MARK_CROSS, MARK_ARROW, MARK_X};
    }

    /**
     * create the named mark
     *
     * @param wellKnownName the wellknown name of the mark
     * @return the mark created
     */
    public Mark createMark(String wellKnownName) {
        Mark mark = sf.createMark();
        mark.setWellKnownName(literalExpression(wellKnownName));

        return mark;
    }

    /**
     * create the named mark with the colors etc supplied
     *
     * @param wellKnownName the well known name of the mark
     * @param fillColor the color of the mark
     * @param borderColor the outline color of the mark
     * @param borderWidth the width of the outline
     * @return the mark created
     */
    public Mark createMark(String wellKnownName, Color fillColor, Color borderColor, double borderWidth) {
        Mark mark = sf.createMark();
        mark.setWellKnownName(literalExpression(wellKnownName));
        mark.setStroke(createStroke(borderColor, borderWidth));
        mark.setFill(createFill(fillColor));

        return mark;
    }

    /**
     * create a mark with default fill (50% gray) and the supplied outline
     *
     * @param wellKnownName the well known name of the mark
     * @param borderColor the outline color
     * @param borderWidth the outline width
     * @return the mark created
     */
    public Mark createMark(String wellKnownName, Color borderColor, double borderWidth) {
        Mark mark = sf.createMark();
        mark.setWellKnownName(literalExpression(wellKnownName));
        mark.setStroke(createStroke(borderColor, borderWidth));

        return mark;
    }

    /**
     * create a mark of the supplied color and a default outline (black)
     *
     * @param wellKnownName the well known name of the mark
     * @param fillColor the color of the mark
     * @return the created mark
     */
    public Mark createMark(String wellKnownName, Color fillColor) {
        Mark mark = sf.createMark();
        mark.setWellKnownName(literalExpression(wellKnownName));
        mark.setFill(createFill(fillColor, 1.0));
        mark.setStroke(null);

        return mark;
    }

    /**
     * create a mark with the supplied fill and stroke
     *
     * @param wellKnownName the well known name of the mark
     * @param fill the fill to use
     * @param stroke the stroke to use
     * @return the mark created
     */
    public Mark createMark(String wellKnownName, Fill fill, Stroke stroke) {
        Mark mark = sf.createMark();
        mark.setWellKnownName(literalExpression(wellKnownName));
        mark.setStroke(stroke);
        mark.setFill(fill);

        return mark;
    }

    /**
     * create a mark with the supplied fill and stroke
     *
     * @param wellKnownName an Expression representing the well known name of the mark
     * @param fill the fill to use
     * @param stroke the stroke to use
     * @return the mark created
     */
    public Mark createMark(Expression wellKnownName, Fill fill, Stroke stroke) {
        Mark mark = sf.createMark();
        mark.setWellKnownName(wellKnownName);
        mark.setStroke(stroke);
        mark.setFill(fill);

        return mark;
    }

    /**
     * wrapper for stylefactory method
     *
     * @param uri the uri of the image
     * @param format mime type of the image
     * @return the external graphic
     */
    public ExternalGraphic createExternalGraphic(String uri, String format) {
        return sf.createExternalGraphic(uri, format);
    }

    /**
     * wrapper for stylefactory method
     *
     * @param url the url of the image
     * @param format mime type of the image
     * @return the external graphic
     */
    public ExternalGraphic createExternalGraphic(java.net.URL url, String format) {
        return sf.createExternalGraphic(url, format);
    }

    /**
     * Creates the default graphic object
     *
     * @return the graphic object
     */
    public Graphic createGraphic() {
        Graphic gr = sf.getDefaultGraphic();

        Mark mark = createMark(MARK_SQUARE, Color.decode("#808080"), Color.BLACK, 1);
        gr.graphicalSymbols().add(mark);
        gr.setSize(Expression.NIL);

        return gr;
    }

    /**
     * creates a graphic object
     *
     * @param externalGraphic an external graphic to use if displayable
     * @param mark a mark to use
     * @param symbol a symbol to use
     * @return the graphic object
     */
    public Graphic createGraphic(ExternalGraphic externalGraphic, Mark mark, Symbol symbol) {
        Graphic gr = sf.getDefaultGraphic();
        gr.graphicalSymbols().clear();

        if (symbol != null) {
            gr.graphicalSymbols().add(symbol);
        }

        if (externalGraphic != null) {
            gr.graphicalSymbols().add(externalGraphic);
        }

        if (mark != null) {
            gr.graphicalSymbols().add(mark);
        }

        return gr;
    }

    /**
     * creates a graphic object
     *
     * @param externalGraphic an external graphic to use if displayable
     * @param mark a mark to use
     * @param symbol a symbol to use
     * @param opacity - the opacity of the graphic
     * @param size - the size of the graphic
     * @param rotation - the rotation from the top of the page of the graphic
     * @return the graphic created
     */
    public Graphic createGraphic(
            ExternalGraphic externalGraphic, Mark mark, Symbol symbol, double opacity, double size, double rotation) {
        ExternalGraphic[] egs = null;
        Mark[] marks = null;
        Symbol[] symbols = null;

        if (externalGraphic != null) {
            egs = new ExternalGraphic[] {externalGraphic};
        }

        if (mark != null) {
            marks = new Mark[] {mark};
        }

        if (symbol != null) {
            symbols = new Symbol[] {symbol};
        }

        return createGraphic(
                egs, marks, symbols, literalExpression(opacity), literalExpression(size), literalExpression(rotation));
    }

    /**
     * creates a graphic object
     *
     * @param externalGraphics an array of external graphics to use if displayable
     * @param marks an array of marks to use
     * @param symbols an array of symbols to use
     * @param opacity - the opacity of the graphic
     * @param size - the size of the graphic
     * @param rotation - the rotation from the top of the page of the graphic
     * @return the graphic created
     */
    public Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            double opacity,
            double size,
            double rotation) {
        return createGraphic(
                externalGraphics,
                marks,
                symbols,
                literalExpression(opacity),
                literalExpression(size),
                literalExpression(rotation));
    }

    /**
     * creates a graphic object
     *
     * @param externalGraphics an array of external graphics to use if displayable
     * @param marks an array of marks to use
     * @param symbols an array of symbols to use
     * @param opacity - an Expression representing the opacity of the graphic
     * @param size - an Expression representing the size of the graphic
     * @param rotation - an Expression representing the rotation from the top of the page of the graphic
     * @return the graphic created
     */
    public Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        ExternalGraphic[] exg = externalGraphics;

        if (exg == null) {
            exg = new ExternalGraphic[0];
        }

        Mark[] m = marks;

        if (m == null) {
            m = new Mark[0];
        }

        Symbol[] s = symbols;

        if (s == null) {
            s = new Symbol[0];
        }

        return sf.createGraphic(exg, m, s, opacity, size, rotation);
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param x - the x coordinate of the anchor
     * @param y - the y coordinate of the anchor
     * @return the AnchorPoint created
     */
    public AnchorPoint createAnchorPoint(double x, double y) {
        return sf.createAnchorPoint(literalExpression(x), literalExpression(y));
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param x - an Expression representing the x coordinate of the anchor
     * @param y - an Expression representing the y coordinate of the anchor
     * @return the AnchorPoint created
     */
    public AnchorPoint createAnchorPoint(Expression x, Expression y) {
        return sf.createAnchorPoint(x, y);
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param x - the x displacement
     * @param y - the y displacement
     * @return the Displacement created
     */
    public Displacement createDisplacement(double x, double y) {
        return sf.createDisplacement(literalExpression(x), literalExpression(y));
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param x - an Expression representing the x displacement
     * @param y - an Expression representing the y displacement
     * @return the Displacement created
     */
    public Displacement createDisplacement(Expression x, Expression y) {
        return sf.createDisplacement(x, y);
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @return the default pointplacement
     */
    public PointPlacement createPointPlacement() {
        return sf.getDefaultPointPlacement();
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param anchorX - the X coordinate
     * @param anchorY - the Y coordinate
     * @param rotation - the rotaion of the label
     * @return the pointplacement created
     */
    public PointPlacement createPointPlacement(double anchorX, double anchorY, double rotation) {
        AnchorPoint anchorPoint = createAnchorPoint(anchorX, anchorY);

        return sf.createPointPlacement(anchorPoint, null, literalExpression(rotation));
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param anchorX - the X coordinate
     * @param anchorY - the Y coordinate
     * @param displacementX - the X distance from the anchor
     * @param displacementY - the Y distance from the anchor
     * @param rotation - the rotaion of the label
     * @return the pointplacement created
     */
    public PointPlacement createPointPlacement(
            double anchorX, double anchorY, double displacementX, double displacementY, double rotation) {
        AnchorPoint anchorPoint = createAnchorPoint(anchorX, anchorY);
        Displacement displacement = createDisplacement(displacementX, displacementY);

        return sf.createPointPlacement(anchorPoint, displacement, literalExpression(rotation));
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param anchorPoint - the anchor point of the label
     * @param displacement - the displacement of the label
     * @param rotation - an Expresson representing the rotation of the label
     * @return the pointplacement created
     */
    public PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, Displacement displacement, Expression rotation) {
        return sf.createPointPlacement(anchorPoint, displacement, rotation);
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param offset - the distance between the line and the label
     * @return the LinePlacement created
     */
    public LinePlacement createLinePlacement(double offset) {
        return sf.createLinePlacement(literalExpression(offset));
    }

    /**
     * wrapper round Stylefactory Method
     *
     * @param offset - an Expression representing the distance between the line and the label
     * @return the LinePlacement created
     */
    public LinePlacement createLinePlacement(Expression offset) {
        return sf.createLinePlacement(offset);
    }

    /**
     * create a geotools font object from a java font
     *
     * @param font - the font to be converted
     * @return - the geotools font
     */
    public Font createFont(java.awt.Font font) {
        Expression family = literalExpression(font.getFamily());
        Expression style;
        Expression weight;

        if (font.isBold()) {
            weight = literalExpression(FONT_WEIGHT_BOLD);
        } else {
            weight = literalExpression(FONT_WEIGHT_NORMAL);
        }

        if (font.isItalic()) {
            style = literalExpression(FONT_STYLE_ITALIC);
        } else {
            style = literalExpression(FONT_STYLE_NORMAL);
        }

        return sf.createFont(family, style, weight, literalExpression(font.getSize2D()));
    }

    /**
     * create font of supplied family and size
     *
     * @param fontFamily - the font family
     * @param fontSize - the size of the font in points
     * @return the font object created
     */
    public Font createFont(String fontFamily, double fontSize) {
        Expression family = literalExpression(fontFamily);
        Expression style = literalExpression(FONT_STYLE_NORMAL);
        Expression weight = literalExpression(FONT_WEIGHT_NORMAL);

        return sf.createFont(family, style, weight, literalExpression(fontSize));
    }

    /**
     * create font of supplied family, size and weight/style
     *
     * @param fontFamily - the font family
     * @param italic - should the font be italic?
     * @param bold - should the font be bold?
     * @param fontSize - the size of the font in points
     * @return the new font object
     */
    public Font createFont(String fontFamily, boolean italic, boolean bold, double fontSize) {
        Expression family = literalExpression(fontFamily);
        Expression style;
        Expression weight;

        if (bold) {
            weight = literalExpression(FONT_WEIGHT_BOLD);
        } else {
            weight = literalExpression(FONT_WEIGHT_NORMAL);
        }

        if (italic) {
            style = literalExpression(FONT_STYLE_ITALIC);
        } else {
            style = literalExpression(FONT_STYLE_NORMAL);
        }

        return sf.createFont(family, style, weight, literalExpression(fontSize));
    }

    /**
     * wrapper round StyleFactory method
     *
     * @param fontFamily - Expression representing Font family
     * @param fontStyle - Expression representing Font style
     * @param fontWeight - Expression representing Font weight
     * @param fontSize - Expression representing Font size
     * @return the new font object
     */
    public Font createFont(Expression fontFamily, Expression fontStyle, Expression fontWeight, Expression fontSize) {
        return sf.createFont(fontFamily, fontStyle, fontWeight, fontSize);
    }

    /**
     * wrapper round StyleFactory method to create default halo
     *
     * @return the new halo
     */
    public Halo createHalo() {
        return sf.createHalo(createFill(Color.WHITE), literalExpression(1));
    }

    /**
     * wrapper round StyleFactory method to create halo
     *
     * @param color - the color of the halo
     * @param radius - the width of the halo
     * @return the new halo
     */
    public Halo createHalo(Color color, double radius) {
        return sf.createHalo(createFill(color), literalExpression(radius));
    }

    /**
     * wrapper round StyleFactory method to create halo
     *
     * @param color - the color of the halo
     * @param opacity - the opacity of the halo fill 0 - transparent 1 - solid
     * @param radius - the width of the halo
     * @return the new halo
     */
    public Halo createHalo(Color color, double opacity, double radius) {
        return sf.createHalo(createFill(color, opacity), literalExpression(radius));
    }

    /**
     * wrapper round StyleFactory method to create halo
     *
     * @param fill - the fill of the halo
     * @param radius - the width of the halo
     * @return the new halo
     */
    public Halo createHalo(Fill fill, double radius) {
        return sf.createHalo(fill, literalExpression(radius));
    }

    /**
     * wrapper round StyleFactory method to create halo
     *
     * @param fill - the fill of the halo
     * @param radius - an Expression representing the width of the halo
     * @return the new halo
     */
    public Halo createHalo(Fill fill, Expression radius) {
        return sf.createHalo(fill, radius);
    }

    /**
     * create a default line symboliser
     *
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer() {
        return sf.getDefaultLineSymbolizer();
    }

    /**
     * create a new line symbolizer
     *
     * @param width the width of the line
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer(double width) {
        return createLineSymbolizer(createStroke(width), null);
    }

    /**
     * create a LineSymbolizer
     *
     * @param color - the color of the line
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer(Color color) {
        return createLineSymbolizer(createStroke(color), null);
    }

    /**
     * create a LineSymbolizer
     *
     * @param color - the color of the line
     * @param width - the width of the line
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer(Color color, double width) {
        return createLineSymbolizer(createStroke(color, width), null);
    }

    /**
     * create a LineSymbolizer
     *
     * @param color - the color of the line
     * @param width - the width of the line
     * @param geometryPropertyName - the name of the geometry to be drawn
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer(Color color, double width, String geometryPropertyName) {
        return createLineSymbolizer(createStroke(color, width), geometryPropertyName);
    }

    /**
     * create a LineSymbolizer
     *
     * @param stroke - the stroke to be used to draw the line
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer(Stroke stroke) {
        return sf.createLineSymbolizer(stroke, null);
    }

    /**
     * create a LineSymbolizer
     *
     * @param stroke - the stroke to be used to draw the line
     * @param geometryPropertyName - the name of the geometry to be drawn
     * @return the new line symbolizer
     */
    public LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName) {
        return sf.createLineSymbolizer(stroke, geometryPropertyName);
    }

    /**
     * create a default polygon symbolizer
     *
     * @return the new polygon symbolizer
     */
    public PolygonSymbolizer createPolygonSymbolizer() {
        PolygonSymbolizer ps = sf.createPolygonSymbolizer();
        ps.setFill(createFill());
        ps.setStroke(createStroke());

        return ps;
    }

    /**
     * create a polygon symbolizer
     *
     * @param fillColor - the color to fill the polygon
     * @return the new polygon symbolizer
     */
    public PolygonSymbolizer createPolygonSymbolizer(Color fillColor) {
        return createPolygonSymbolizer(null, createFill(fillColor));
    }

    /**
     * create a polygon symbolizer
     *
     * @param fillColor - the color to fill the polygon
     * @param borderColor - the outline color of the polygon
     * @param borderWidth - the width of the outline
     * @return the new polygon symbolizer
     */
    public PolygonSymbolizer createPolygonSymbolizer(Color fillColor, Color borderColor, double borderWidth) {
        return createPolygonSymbolizer(createStroke(borderColor, borderWidth), createFill(fillColor));
    }

    /**
     * create a polygon symbolizer
     *
     * @param borderColor - the outline color of the polygon
     * @param borderWidth - the width of the outline
     * @return the new polygon symbolizer
     */
    public PolygonSymbolizer createPolygonSymbolizer(Color borderColor, double borderWidth) {
        return createPolygonSymbolizer(createStroke(borderColor, borderWidth), null);
    }

    /**
     * create a polygon symbolizer
     *
     * @param stroke - the stroke to use to outline the polygon
     * @param fill - the fill to use to color the polygon
     * @return the new polygon symbolizer
     */
    public PolygonSymbolizer createPolygonSymbolizer(Stroke stroke, Fill fill) {
        return createPolygonSymbolizer(stroke, fill, null);
    }

    /**
     * create a polygon symbolizer
     *
     * @param stroke - the stroke to use to outline the polygon
     * @param fill - the fill to use to color the polygon
     * @param geometryPropertyName - the name of the geometry to be drawn
     * @return the new polygon symbolizer
     */
    public PolygonSymbolizer createPolygonSymbolizer(Stroke stroke, Fill fill, String geometryPropertyName) {
        return sf.createPolygonSymbolizer(stroke, fill, geometryPropertyName);
    }

    /**
     * create a default point symbolizer
     *
     * @return the new point symbolizer
     */
    public PointSymbolizer createPointSymbolizer() {
        return sf.getDefaultPointSymbolizer();
    }

    /**
     * create a point symbolizer
     *
     * @param graphic - the graphic object to draw at the point
     * @return the new point symbolizer
     */
    public PointSymbolizer createPointSymbolizer(Graphic graphic) {
        PointSymbolizer ps = sf.createPointSymbolizer();
        ps.setGraphic(graphic);

        return ps;
    }

    /**
     * create a point symbolizer
     *
     * @param graphic - the graphic object to draw at the point
     * @param geometryPropertyName - the name of the geometry to be drawn
     * @return the new point symbolizer
     */
    public PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName) {
        return sf.createPointSymbolizer(graphic, geometryPropertyName);
    }

    /**
     * Creates a default text symbolizer. Warning: there is no definition of a default text symbolizer in the SLD
     * standard, this is provided just for convenience and uniformity with the other symbolizers
     *
     * @return the default text symbolizer
     */
    public TextSymbolizer createTextSymbolizer() {
        TextSymbolizer ts = sf.createTextSymbolizer();

        ts.setFill(createFill(Color.BLACK));
        ts.setLabel(literalExpression("Label"));
        ts.fonts().add(createFont("Lucida Sans", 10));

        return ts;
    }

    /**
     * create a textsymbolizer
     *
     * @param color the color of the text
     * @param font the font to use
     * @param attributeName the attribute to use for the label
     * @return the new textsymbolizer
     * @throws org.geotools.filter.IllegalFilterException - if the attribute name does not exist
     */
    public TextSymbolizer createTextSymbolizer(Color color, Font font, String attributeName)
            throws org.geotools.filter.IllegalFilterException {
        return createTextSymbolizer(
                createFill(color), new Font[] {font}, null, attributeExpression(attributeName), null, null);
    }

    /**
     * create a textsymbolizer
     *
     * @param color the color of the text
     * @param fonts an array of fonts to use from the first to last
     * @param attributeName the attribute to use for the label
     * @return the new textsymbolizer
     * @throws org.geotools.filter.IllegalFilterException - if the attribute name does not exist
     */
    public TextSymbolizer createTextSymbolizer(Color color, Font[] fonts, String attributeName)
            throws org.geotools.filter.IllegalFilterException {
        return createTextSymbolizer(createFill(color), fonts, null, attributeExpression(attributeName), null, null);
    }

    /**
     * create a textsymbolizer which doesn't change
     *
     * @param color the color of the text
     * @param font the font to use
     * @param label the label to use
     * @return the new textsymbolizer
     */
    public TextSymbolizer createStaticTextSymbolizer(Color color, Font font, String label) {
        return createTextSymbolizer(createFill(color), new Font[] {font}, null, literalExpression(label), null, null);
    }

    /**
     * create a textsymbolizer which doesn't change
     *
     * @param color the color of the text
     * @param fonts an array of fonts to use from the first to last
     * @param label the label to use
     * @return the new textsymbolizer
     */
    public TextSymbolizer createStaticTextSymbolizer(Color color, Font[] fonts, String label) {
        return createTextSymbolizer(createFill(color), fonts, null, literalExpression(label), null, null);
    }

    /**
     * create a text symbolizer
     *
     * @param fill - the fill to color the text
     * @param fonts - an array of fonts to use from the first to last
     * @param halo - the halo to be applied to the text
     * @param label - Expression representing the label
     * @param labelPlacement - where to place the label
     * @param geometryPropertyName - the name of the geometry to use
     * @return the new textsymbolizer
     */
    public TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName) {
        TextSymbolizer ts = sf.createTextSymbolizer();

        if (fill != null) {
            ts.setFill(fill);
        }

        if (halo != null) {
            ts.setHalo(halo);
        }

        if (label != null) {
            ts.setLabel(label);
        }

        if (labelPlacement != null) {
            ts.setLabelPlacement(labelPlacement);
        }

        if (geometryPropertyName != null) {
            ts.setGeometryPropertyName(geometryPropertyName);
        }

        if (fonts != null) {
            for (Font font : fonts) {
                if (font != null) {
                    ts.fonts().add(font);
                }
            }
        }

        return ts;
    }

    /**
     * create a SimpleFeature type styler
     *
     * @param symbolizer - the symbolizer to use
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(Symbolizer symbolizer) {
        return createFeatureTypeStyle(null, symbolizer, Double.NaN, Double.NaN);
    }

    /**
     * create a simple styling rule
     *
     * @param symbolizer - the symbolizer to use
     * @return the new rule
     */
    public Rule createRule(Symbolizer symbolizer) {
        return createRule(symbolizer, Double.NaN, Double.NaN);
    }

    /**
     * reate a simple styling rule
     *
     * @param symbolizers - an array of symbolizers to use
     * @return the new rule
     */
    public Rule createRule(Symbolizer... symbolizers) {
        return createRule(symbolizers, Double.NaN, Double.NaN);
    }

    /**
     * create a simple styling rule, see the SLD Spec for more details of scaleDenominators
     *
     * @param symbolizer - the symbolizer to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new rule
     */
    public Rule createRule(Symbolizer symbolizer, double minScaleDenominator, double maxScaleDenominator) {
        return createRule(new Symbolizer[] {symbolizer}, minScaleDenominator, maxScaleDenominator);
    }

    /**
     * create a simple styling rule, see the SLD Spec for more details of scaleDenominators
     *
     * @param symbolizers - an array of symbolizers to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new rule
     */
    public Rule createRule(Symbolizer[] symbolizers, double minScaleDenominator, double maxScaleDenominator) {
        Rule r = sf.createRule();
        r.symbolizers().addAll(Arrays.asList(symbolizers));

        if (!Double.isNaN(maxScaleDenominator)) {
            r.setMaxScaleDenominator(maxScaleDenominator);
        } else {
            r.setMaxScaleDenominator(Double.POSITIVE_INFINITY);
        }

        if (!Double.isNaN(minScaleDenominator)) {
            r.setMinScaleDenominator(minScaleDenominator);
        } else {
            r.setMinScaleDenominator(0.0);
        }

        return r;
    }

    /**
     * create a SimpleFeature type styler see the SLD Spec for more details of scaleDenominators
     *
     * @param symbolizer - the symbolizer to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(
            Symbolizer symbolizer, double minScaleDenominator, double maxScaleDenominator) {
        return createFeatureTypeStyle(null, symbolizer, minScaleDenominator, maxScaleDenominator);
    }

    /**
     * create a SimpleFeature type styler see the SLD Spec for more details of scaleDenominators
     *
     * @param symbolizers - an array of symbolizers to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(
            Symbolizer[] symbolizers, double minScaleDenominator, double maxScaleDenominator) {
        return createFeatureTypeStyle(null, symbolizers, minScaleDenominator, maxScaleDenominator);
    }

    /**
     * create a SimpleFeature type styler
     *
     * @param featureTypeName - name of the feature type
     * @param symbolizer - the symbolizer to use
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(String featureTypeName, Symbolizer symbolizer) {
        return createFeatureTypeStyle(featureTypeName, symbolizer, Double.NaN, Double.NaN);
    }

    /**
     * create a SimpleFeature type styler
     *
     * @param featureTypeName - name of the feature type
     * @param symbolizers - an array of symbolizers to use
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(String featureTypeName, Symbolizer... symbolizers) {
        return createFeatureTypeStyle(featureTypeName, symbolizers, Double.NaN, Double.NaN);
    }

    /**
     * create a SimpleFeature type styler see the SLD Spec for more details of scaleDenominators
     *
     * @param typeName - The feature typeName you want to draw (use "Feature" as a wild card to match all)
     * @param symbolizer - the symbolizer to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(
            String typeName, Symbolizer symbolizer, double minScaleDenominator, double maxScaleDenominator) {
        return createFeatureTypeStyle(
                typeName, new Symbolizer[] {symbolizer}, minScaleDenominator, maxScaleDenominator);
    }

    /**
     * create a SimpleFeature type styler see the SLD Spec for more details of scaleDenominators
     *
     * @param typeName - The feature typeName you want to draw (use "Feature" as a wild card to match all)
     * @param symbolizers - an array of symbolizers to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(
            String typeName, Symbolizer[] symbolizers, double minScaleDenominator, double maxScaleDenominator) {
        Rule r = createRule(symbolizers, minScaleDenominator, maxScaleDenominator);

        // setup the feature type style
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(r);

        if (typeName != null) {
            fts.featureTypeNames().add(new NameImpl(typeName));
        }

        return fts;
    }

    /**
     * create a SimpleFeature type styler
     *
     * @param typeName - The feature typeName you want to draw (use "Feature" as a wild card to match all)
     * @param r - the rule that driver this feature typ style
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(String typeName, Rule r) {
        // setup the feature type style
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(r);

        if (typeName != null) {
            fts.featureTypeNames().add(new NameImpl(typeName));
        }

        return fts;
    }

    /**
     * create a SimpleFeature type styler see the SLD Spec for more details of scaleDenominators
     *
     * @param typeName - The feature typeName you want to draw (use "Feature" as a wild card to match all)
     * @param rules - the rules that make up the FeatureTypeStyle
     * @return the new feature type styler
     */
    public FeatureTypeStyle createFeatureTypeStyle(String typeName, Rule... rules) {
        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().addAll(Arrays.asList(rules));

        if (typeName != null) {
            fts.featureTypeNames().add(new NameImpl(typeName));
        }

        return fts;
    }

    /**
     * create a new style
     *
     * @param symbolizer - the symbolizer to use
     * @return the new style
     */
    public Style createStyle(Symbolizer symbolizer) {
        return createStyle(null, symbolizer, Double.NaN, Double.NaN);
    }

    /**
     * create a new style
     *
     * @param symbolizer - the symbolizer to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new style
     */
    public Style createStyle(Symbolizer symbolizer, double minScaleDenominator, double maxScaleDenominator) {
        return createStyle(null, symbolizer, minScaleDenominator, maxScaleDenominator);
    }

    /**
     * create a new style
     *
     * @param typeName - The feature typeName you want to draw (use "Feature" as a wild card to match all)
     * @param symbolizer - the symbolizer to use
     * @return the new style
     */
    public Style createStyle(String typeName, Symbolizer symbolizer) {
        return createStyle(typeName, symbolizer, Double.NaN, Double.NaN);
    }

    /**
     * create a new style
     *
     * @param typeName - The feature typeName you want to draw (use "Feature" as a wild card to match all)
     * @param symbolizer - the symbolizer to use
     * @param minScaleDenominator - the minimim scale to draw the feature at
     * @param maxScaleDenominator - the maximum scale to draw the feature at
     * @return the new style
     */
    public Style createStyle(
            String typeName, Symbolizer symbolizer, double minScaleDenominator, double maxScaleDenominator) {
        // create the feature type style
        FeatureTypeStyle fts = createFeatureTypeStyle(typeName, symbolizer, minScaleDenominator, maxScaleDenominator);

        // and finally create the style
        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }

    /**
     * create a new default style
     *
     * @return the new style
     */
    public Style createStyle() {
        return sf.createStyle();
    }

    /**
     * convert an awt color in to a literal expression representing the color
     *
     * @param color the color to encode
     * @return the expression
     */
    public Expression colorExpression(Color color) {
        if (color == null) {
            return null;
        }

        String redCode = Integer.toHexString(color.getRed());
        String greenCode = Integer.toHexString(color.getGreen());
        String blueCode = Integer.toHexString(color.getBlue());

        if (redCode.length() == 1) {
            redCode = "0" + redCode;
        }

        if (greenCode.length() == 1) {
            greenCode = "0" + greenCode;
        }

        if (blueCode.length() == 1) {
            blueCode = "0" + blueCode;
        }

        String colorCode = "#" + redCode + greenCode + blueCode;

        return ff.literal(colorCode.toUpperCase());
    }

    /**
     * create a literal expression representing the value
     *
     * @param value the value to be encoded
     * @return the expression
     */
    public Expression literalExpression(double value) {
        return ff.literal(value);
    }

    /**
     * create a literal expression representing the value
     *
     * @param value the value to be encoded
     * @return the expression
     */
    public Expression literalExpression(int value) {
        return ff.literal(value);
    }

    /**
     * create a literal expression representing the value
     *
     * @param value the value to be encoded
     * @return the expression
     */
    public Expression literalExpression(String value) {
        Expression result = null;

        if (value != null) {
            result = ff.literal(value);
        }

        return result;
    }

    /**
     * create a literal expression representing the value
     *
     * @param value the value to be encoded
     * @return the expression
     */
    public Expression literalExpression(Object value) throws IllegalFilterException {
        Expression result = null;

        if (value != null) {
            result = ff.literal(value);
        }

        return result;
    }

    /**
     * create an attribute expression
     *
     * @param attributeName the attribute to use
     * @return the new expression
     * @throws org.geotools.filter.IllegalFilterException if the attribute name does not exist
     */
    public Expression attributeExpression(String attributeName) throws org.geotools.filter.IllegalFilterException {
        return ff.property(attributeName);
    }

    /**
     * given a feature collection and an array of colours build a style with the given number of classes on the named
     * column
     */
    public Style buildClassifiedStyle(
            SimpleFeatureCollection fc, String name, String[] colors, SimpleFeatureType schema)
            throws IllegalFilterException {
        // grab attribute col
        PropertyName value = ff.property(name);
        String geomName = schema.getGeometryDescriptor().getLocalName();

        double[] values = new double[fc.size()];
        int count = 0;

        try (SimpleFeatureIterator it = fc.features()) {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                values[count++] = ((Number) f.getAttribute(name)).doubleValue();
            }
        }

        // pass to classification algorithm
        EqualClasses ec = new EqualClasses(colors.length, values);

        // build style
        double[] breaks = ec.getBreaks();
        Style ret = createStyle();

        //        ret.setName(name);
        Rule[] rules = new Rule[colors.length + 1];

        PropertyIsLessThan cf1 = ff.less(value, ff.literal(breaks[0]));

        LOGGER.fine(cf1.toString());
        rules[0] = sf.createRule();
        rules[0].setFilter(cf1);

        //        rules[0].setName("lowest");
        Color c = this.createColor(colors[0]);
        PolygonSymbolizer symb1 = createPolygonSymbolizer(c, Color.black, 1.0);

        // @todo: this should set the geometry name but currently this breaks the legend
        //        symb1.setGeometryPropertyName(geomName);
        rules[0].symbolizers().add(symb1);
        LOGGER.fine("added low class " + breaks[0] + " " + colors[0]);

        //        LOGGER.fine(rules[0].toString());
        for (int i = 1; i < colors.length - 1; i++) {
            rules[i] = sf.createRule();

            Expression expr = value;
            Expression lower = ff.literal(breaks[i - 1]);
            Expression upper = ff.literal(breaks[i]);
            PropertyIsBetween cf = ff.between(expr, lower, upper);

            LOGGER.fine(cf.toString());
            c = this.createColor(colors[i]);
            LOGGER.fine("color " + c.toString());

            PolygonSymbolizer symb = createPolygonSymbolizer(c, Color.black, 1.0);

            //            symb.setGeometryPropertyName(geomName);
            rules[i].symbolizers().add(symb);
            rules[i].setFilter(cf);

            //            rules[i].setName("class "+i);
            LOGGER.fine("added class " + breaks[i - 1] + "->" + breaks[i] + " " + colors[i]);
        }

        PropertyIsGreaterThan cf2 = ff.greater(value, ff.literal(breaks[colors.length - 2]));

        LOGGER.fine(cf2.toString());
        rules[colors.length - 1] = sf.createRule();
        rules[colors.length - 1].setFilter(cf2);
        rules[colors.length - 1].setName(geomName);
        c = this.createColor(colors[colors.length - 1]);

        PolygonSymbolizer symb2 = createPolygonSymbolizer(c, Color.black, 1.0);

        //        symb2.setGeometryPropertyName(geomName);
        rules[colors.length - 1].symbolizers().add(symb2);
        LOGGER.fine("added upper class " + breaks[colors.length - 2] + "  " + colors[colors.length - 1]);
        rules[colors.length] = sf.createRule();

        PolygonSymbolizer elsePoly = createPolygonSymbolizer(Color.black, 1.0);
        rules[colors.length].symbolizers().add(elsePoly);
        rules[colors.length].setElseFilter(true);

        FeatureTypeStyle ft = sf.createFeatureTypeStyle(rules);
        ft.featureTypeNames().add(new NameImpl("Feature"));
        ft.setName(name);
        ret.featureTypeStyles().add(ft);

        return ret;
    }

    private Color createColor(String text) {
        int i = Integer.decode("0x" + text).intValue();

        return Color.decode("" + i);
    }

    /** Creates the default raster symbolizer */
    public RasterSymbolizer createRasterSymbolizer() {
        return sf.getDefaultRasterSymbolizer();
    }

    /**
     * Creates a raster symbolizer
     *
     * @param colorMap The symbolizer color map
     * @param opacity The whole layer opacity
     */
    public RasterSymbolizer createRasterSymbolizer(ColorMap colorMap, double opacity) {
        RasterSymbolizer rs = sf.getDefaultRasterSymbolizer();
        rs.setColorMap(colorMap);
        rs.setOpacity(literalExpression(opacity));

        return rs;
    }

    /**
     * Creates a color map based on fixed quantities and colors.
     *
     * @param quantities The values that begin a category, or break points in a ramp, or isolated values, according to
     *     the type of color map specified by Type
     * @param colors The colors that will be associated to the categories, break points, or isolated values
     * @param type Either @link ColorMap#TYPE_RAMP, @link ColorMap#TYPE_INTERVALS or @link ColorMap#TYPE_VALUES
     */
    public ColorMap createColorMap(String[] labels, double[] quantities, Color[] colors, int type) {
        ColorMap colorMap = sf.createColorMap();
        colorMap.setType(type);

        if (labels == null
                || quantities == null
                || colors == null
                || labels.length != quantities.length
                || quantities.length != colors.length) {
            throw new IllegalArgumentException(
                    "Labels, quantities and colors arrays should be not null and have the same size");
        }

        for (int i = 0; i < colors.length; i++) {
            colorMap.addColorMapEntry(createColorMapEntry(labels[i], quantities[i], colors[i]));
        }

        return colorMap;
    }

    /**
     * Creates a simple color entity based on a fixed value and a color.<br>
     * The color alpha will be used as the entry's opacity
     *
     * @param quantity The entry's quantity
     * @param color The entry's color.
     */
    private ColorMapEntry createColorMapEntry(String label, double quantity, Color color) {
        ColorMapEntry entry = sf.createColorMapEntry();
        entry.setQuantity(literalExpression(quantity));
        entry.setColor(colorExpression(color));
        entry.setOpacity(literalExpression(color.getAlpha() / 255.0));
        entry.setLabel(label);
        return entry;
    }

    public class EqualClasses {
        int numberClasses;
        double[] breaks;
        double[] collection;

        /** Creates a new instance of EqualClasses */
        public EqualClasses(int numberClasses, double[] fc) {

            breaks = new double[numberClasses - 1];
            setCollection(fc);
            setNumberClasses(numberClasses);
        }

        /**
         * Getter for property numberClasses.
         *
         * @return Value of property numberClasses.
         */
        public int getNumberClasses() {
            return numberClasses;
        }

        /**
         * Setter for property numberClasses.
         *
         * @param numberClasses New value of property numberClasses.
         */
        public void setNumberClasses(int numberClasses) {
            this.numberClasses = numberClasses;
            if (breaks == null) {
                breaks = new double[numberClasses - 1];
            }

            Arrays.sort(collection);

            int step = collection.length / numberClasses;
            for (int i = step, j = 0; j < breaks.length; j++, i += step) {
                breaks[j] = collection[i];
            }
        }

        /**
         * returns the the break points between the classes <b>Note</b> You get one less breaks than number of classes.
         *
         * @return Value of property breaks.
         */
        public double[] getBreaks() {
            return this.breaks;
        }

        /**
         * Setter for property collection.
         *
         * @param collection New value of property collection.
         */
        public void setCollection(double[] collection) {
            this.collection = collection;
        }
    }
}
