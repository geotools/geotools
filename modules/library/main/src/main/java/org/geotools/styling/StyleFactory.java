/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on 14 October 2002, 15:50
 */
package org.geotools.styling;

import java.util.*;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.swing.Icon;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.*;
import org.geotools.api.style.ColorReplacement;
import org.geotools.api.style.Description;
import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.util.InternationalString;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Factory for creating Styles; based on the GeoAPI StyleFactory interface.
 *
 * <p>This factory is simple; it just creates styles with no logic or magic default values. For
 * magic default values please read the SE or SLD specification; or use an appropriate builder.
 *
 * @author Jody Garnett
 * @version $Id$
 */
public class StyleFactory extends AbstractStyleFactory
        implements org.geotools.api.style.StyleFactory {
    static final Fill DEFAULT_FILL = (Fill) ConstantFill.DEFAULT;
    static final Stroke DEFAULT_STROKE = (Stroke) ConstantStroke.DEFAULT;
    private FilterFactory filterFactory;

    public StyleFactory() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    protected StyleFactory(FilterFactory factory) {
        filterFactory = factory;
    }

    @Override
    public TextSymbolizer createTextSymbolizer() {
        return new TextSymbolizer(filterFactory);
    }

    @Override
    public org.geotools.api.style.PointPlacement createPointPlacement(
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        PointPlacement pointPlacement = new PointPlacement(filterFactory);
        pointPlacement.setAnchorPoint(anchorPoint);
        pointPlacement.setDisplacement(displacement);
        pointPlacement.setRotation(rotation);
        return pointPlacement;
    }

    @Override
    public TextSymbolizer createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            org.geotools.api.style.LabelPlacement labelPlacement,
            String geometryPropertyName) {
        TextSymbolizer tSymb = new TextSymbolizer(filterFactory);
        tSymb.setFill(fill);
        Collection<Font> nFonts = new ArrayList<>();

        if (fonts != null) {
            for (org.geotools.api.style.Font font : fonts) {
                nFonts.add((org.geotools.styling.Font) font);
            }
            tSymb.fonts().addAll(nFonts);
        }
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(labelPlacement);

        return tSymb;
    }

    @Override
    public TextSymbolizer createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            org.geotools.api.style.LabelPlacement labelPlacement,
            String geometryPropertyName,
            org.geotools.api.style.Graphic graphic) {
        TextSymbolizer tSymb = new TextSymbolizer(filterFactory);
        tSymb.setFill(fill);
        Collection<Font> nFonts = new ArrayList<>();

        if (fonts != null) {
            for (org.geotools.api.style.Font font : fonts) {
                nFonts.add((org.geotools.styling.Font) font);
            }
            tSymb.fonts().addAll(nFonts);
        }
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(labelPlacement);
        tSymb.setGraphic((Graphic) graphic);

        return tSymb;
    }

    @Override
    public Extent createExtent(String name, String value) {
        Extent extent = new Extent();
        extent.setName(name);
        extent.setValue(value);

        return extent;
    }

    /**
     * Creates a new feature type constraint.
     *
     * @param featureTypeName The feature type name.
     * @param filter The filter.
     * @param extents The extents.
     * @return The new feature type constaint.
     */
    @Override
    public FeatureTypeConstraint createFeatureTypeConstraint(
            String featureTypeName, Filter filter, org.geotools.api.style.Extent... extents) {
        FeatureTypeConstraint constraint = new FeatureTypeConstraint();
        constraint.setFeatureTypeName(featureTypeName);
        constraint.setFilter(filter);
        constraint.setExtents(extents);

        return constraint;
    }

    @Override
    public org.geotools.api.style.FeatureTypeStyle createFeatureTypeStyle(
            org.geotools.api.style.Rule... rules) {
        return null;
    }

    /**
     * Creates a new ImageOutline.
     *
     * @param symbolizer A line or polygon symbolizer.
     * @return The new image outline.
     */
    @Override
    public org.geotools.api.style.ImageOutline createImageOutline(
            org.geotools.api.style.Symbolizer symbolizer) {
        return null;
    }

    @Override
    public LayerFeatureConstraints createLayerFeatureConstraints(
            org.geotools.api.style.FeatureTypeConstraint[] featureTypeConstraints) {
        org.geotools.styling.LayerFeatureConstraints constraints =
                new org.geotools.styling.LayerFeatureConstraints();
        constraints.setFeatureTypeConstraints(featureTypeConstraints);

        return constraints;
    }

    @Override
    public FeatureTypeStyle createFeatureTypeStyle() {
        return new FeatureTypeStyle();
    }

    @Override
    public org.geotools.api.style.Graphic createGraphic(
            org.geotools.api.style.ExternalGraphic[] externalGraphics,
            org.geotools.api.style.Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        return null;
    }

    @Override
    public FeatureTypeStyle createFeatureTypeStyle(Rule[] rules) {
        return new FeatureTypeStyle(rules);
    }

    @Override
    public Rule createRule() {
        return new Rule();
    }

    @Override
    public org.geotools.api.style.LineSymbolizer createLineSymbolizer(
            org.geotools.api.style.Stroke stroke, String geometryPropertyName) {
        return null;
    }

    @Override
    public LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName) {
        return new LineSymbolizer();
    }

    public Rule createRule(
            org.geotools.styling.Symbolizer[] symbolizers,
            Description desc,
            org.geotools.api.style.GraphicLegend legend,
            String name,
            Filter filter,
            boolean isElseFilter,
            double maxScale,
            double minScale) {

        Rule r =
                new Rule(symbolizers, desc, legend, name, filter, isElseFilter, maxScale, minScale);

        return r;
    }

    @Override
    public org.geotools.styling.ImageOutline createImageOutline(Symbolizer symbolizer) {
        org.geotools.styling.ImageOutline outline = new org.geotools.styling.ImageOutline();
        outline.setSymbolizer(symbolizer);

        return outline;
    }

    /**
     * A method to make a simple stroke of a provided color and width.
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the stroke object
     * @see org.geotools.api.style.Stroke
     */
    @Override
    public Stroke createStroke(Expression color, Expression width) {
        return createStroke(color, width, filterFactory.literal(1.0));
    }

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
     * @see org.geotools.api.style.Stroke
     */
    @Override
    public Stroke createStroke(Expression color, Expression width, Expression opacity) {
        return createStroke(
                color,
                width,
                opacity,
                filterFactory.literal("miter"),
                filterFactory.literal("butt"),
                null,
                filterFactory.literal(0.0),
                null,
                null);
    }

    /**
     * creates a stroke
     *
     * @param color The color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @param lineJoin - the type of Line joint
     * @param lineCap - the type of line cap
     * @param dashArray - an array of floats describing the dashes in the line
     * @param dashOffset - where in the dash array to start drawing from
     * @param graphicFill - a graphic object to fill the line with
     * @param graphicStroke - a graphic object to draw the line with
     * @return The completed stroke.
     * @see org.geotools.api.style.Stroke
     */
    @Override
    public org.geotools.api.style.Stroke createStroke(
            Expression color,
            Expression width,
            Expression opacity,
            Expression lineJoin,
            Expression lineCap,
            float[] dashArray,
            Expression dashOffset,
            org.geotools.api.style.Graphic graphicFill,
            org.geotools.api.style.Graphic graphicStroke) {
        return null;
    }

    /**
     * creates a stroke
     *
     * @param color The color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @param lineJoin - the type of Line joint
     * @param lineCap - the type of line cap
     * @param dashArray - an array of floats describing the dashes in the line
     * @param dashOffset - where in the dash array to start drawing from
     * @param graphicFill - a graphic object to fill the line with
     * @param graphicStroke - a graphic object to draw the line with
     * @return The completed stroke.
     * @see org.geotools.api.style.Stroke
     */
    @Override
    public Stroke createStroke(
            Expression color,
            Expression width,
            Expression opacity,
            Expression lineJoin,
            Expression lineCap,
            float[] dashArray,
            Expression dashOffset,
            Graphic graphicFill,
            Graphic graphicStroke) {
        Stroke stroke = new Stroke(filterFactory);

        if (color == null) {
            // use default
            color = Stroke.DEFAULT.getColor();
        }
        stroke.setColor(color);

        if (width == null) {
            // use default
            width = Stroke.DEFAULT.getWidth();
        }
        stroke.setWidth(width);

        if (opacity == null) {
            opacity = Stroke.DEFAULT.getOpacity();
        }
        stroke.setOpacity(opacity);

        if (lineJoin == null) {
            lineJoin = Stroke.DEFAULT.getLineJoin();
        }
        stroke.setLineJoin(lineJoin);

        if (lineCap == null) {
            lineCap = Stroke.DEFAULT.getLineCap();
        }

        stroke.setLineCap(lineCap);
        stroke.setDashArray(dashArray);
        stroke.setDashOffset(dashOffset);
        stroke.setGraphicFill(graphicFill);
        stroke.setGraphicStroke(graphicStroke);

        return stroke;
    }

    @Override
    public Fill createFill(
            Expression color, Expression backgroundColor, Expression opacity, Graphic graphicFill) {
        Fill fill = new Fill(filterFactory);

        if (color == null) {
            color = ConstantFill.DEFAULT.getColor();
        }
        fill.setColor(color);

        if (opacity == null) {
            opacity = ConstantFill.DEFAULT.getOpacity();
        }

        // would be nice to check if this was within bounds but we have to wait until use since it
        // may depend on an attribute
        fill.setOpacity(opacity);
        fill.setGraphicFill(graphicFill);

        return fill;
    }

    @Override
    public LineSymbolizer createLineSymbolizer() {
        return new LineSymbolizer();
    }

    @Override
    public org.geotools.api.style.PointSymbolizer createPointSymbolizer(
            org.geotools.api.style.Graphic graphic, String geometryPropertyName) {
        return null;
    }

    @Override
    public PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName) {
        PointSymbolizer pSymb = new PointSymbolizer();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setGraphic(graphic);

        return pSymb;
    }

    @Override
    public Style createStyle() {
        return new Style();
    }

    @Override
    public NamedStyle createNamedStyle() {
        return new NamedStyle();
    }

    @Override
    public Fill createFill(Expression color, Expression opacity) {
        return createFill(color, null, opacity, null);
    }

    @Override
    public Fill createFill(Expression color) {
        return createFill(color, null, filterFactory.literal(1.0), null);
    }

    @Override
    public Mark createMark(
            Expression wellKnownName,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            Expression size,
            Expression rotation) {
        Mark mark = new Mark(filterFactory, null);

        if (wellKnownName == null) {
            throw new IllegalArgumentException("WellKnownName can not be null in mark");
        }

        mark.setWellKnownName(wellKnownName);
        mark.setStroke(stroke);
        mark.setFill(fill);

        return mark;
    }

    @Override
    public Mark getSquareMark() {
        Fill fill = getDefaultFill();
        Mark mark =
                createMark(
                        filterFactory.literal("Square"),
                        getDefaultStroke(),
                        fill,
                        filterFactory.literal(6),
                        filterFactory.literal(0));

        return mark;
    }

    @Override
    public Mark getCircleMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Circle"));

        return mark;
    }

    @Override
    public Mark getCrossMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Cross"));

        return mark;
    }

    @Override
    public Mark getXMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("X"));

        return mark;
    }

    @Override
    public Mark getTriangleMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Triangle"));

        return mark;
    }

    @Override
    public Mark getStarMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Star"));

        return mark;
    }

    @Override
    public Mark createMark() {
        Mark mark = new Mark(filterFactory, null);

        return mark;
    }

    @Override
    public org.geotools.api.style.PolygonSymbolizer createPolygonSymbolizer(
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            String geometryPropertyName) {
        return null;
    }

    @Override
    public PolygonSymbolizer createPolygonSymbolizer(
            Stroke stroke, Fill fill, String geometryPropertyName) {
        return new PolygonSymbolizer();
    }

    @Override
    public Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        Graphic graphic = new Graphic(filterFactory);

        symbols = symbols != null ? symbols : new Symbol[0];
        graphic.graphicalSymbols().addAll(Arrays.asList(symbols));

        // externalGraphics = externalGraphics != null ? externalGraphics : new ExternalGraphic[0];
        // graphic.setExternalGraphics(externalGraphics);
        if (externalGraphics != null) {
            graphic.graphicalSymbols().addAll(Arrays.asList(externalGraphics));
        }
        // marks = marks != null ? marks : new Mark[0];
        // graphic.setMarks(marks);
        if (marks != null) {
            graphic.graphicalSymbols().addAll(Arrays.asList(marks));
        }
        if (opacity == null) {
            opacity = ConstantGraphic.DEFAULT.getOpacity();
        }
        graphic.setOpacity(opacity);

        if (size == null) {
            size = ConstantGraphic.DEFAULT.getSize();
        }
        graphic.setSize(size);

        if (rotation == null) {
            rotation = ConstantGraphic.DEFAULT.getRotation();
        }

        graphic.setRotation(rotation);

        return graphic;
    }

    @Override
    public ExternalGraphic createExternalGraphic(String uri, String format) {
        ExternalGraphic extg = new ExternalGraphic();
        extg.setURI(uri);
        extg.setFormat(format);

        return extg;
    }

    @Override
    public ExternalGraphic createExternalGraphic(Icon inlineContent, String format) {
        ExternalGraphic extg = new ExternalGraphic();
        extg.setInlineContent(inlineContent);
        extg.setFormat(format);
        return extg;
    }

    @Override
    public ExternalGraphic createExternalGraphic(java.net.URL url, String format) {
        ExternalGraphic extg = new ExternalGraphic();
        extg.setLocation(url);
        extg.setFormat(format);

        return extg;
    }

    @Override
    public Font createFont(
            Expression fontFamily,
            Expression fontStyle,
            Expression fontWeight,
            Expression fontSize) {
        Font font = new Font();

        if (fontFamily == null) {
            throw new IllegalArgumentException("Null font family specified");
        }
        font.getFamily().add(fontFamily);

        if (fontSize == null) {
            throw new IllegalArgumentException("Null font size specified");
        }

        font.setSize(fontSize);

        if (fontStyle == null) {
            throw new IllegalArgumentException("Null font Style specified");
        }

        font.setStyle(fontStyle);

        if (fontWeight == null) {
            throw new IllegalArgumentException("Null font weight specified");
        }

        font.setWeight(fontWeight);

        return font;
    }

    //    public LinePlacement createLinePlacement(){
    //        return new LinePlacementImpl();
    //    }
    @Override
    public LinePlacement createLinePlacement(Expression offset) {
        LinePlacement linep = new LinePlacement(filterFactory);
        linep.setPerpendicularOffset(offset);

        return linep;
    }

    @Override
    public PolygonSymbolizer createPolygonSymbolizer() {
        return new PolygonSymbolizer();
    }

    @Override
    public org.geotools.api.style.Halo createHalo(
            org.geotools.api.style.Fill fill, Expression radius) {
        return null;
    }

    @Override
    public org.geotools.api.style.Fill createFill(
            Expression color,
            Expression backgroundColor,
            Expression opacity,
            org.geotools.api.style.Graphic graphicFill) {
        return null;
    }

    //    public PointPlacement createPointPlacement(){
    //        return new PointPlacementImpl();
    //    }
    @Override
    public PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, Displacement displacement, Expression rotation) {
        PointPlacement pointp = new PointPlacement(filterFactory);
        pointp.setAnchorPoint(anchorPoint);
        pointp.setDisplacement(displacement);
        pointp.setRotation(rotation);

        return pointp;
    }

    @Override
    public AnchorPoint createAnchorPoint(Expression x, Expression y) {
        AnchorPoint anchorPoint = new AnchorPoint(filterFactory);
        anchorPoint.setAnchorPointX(x);
        anchorPoint.setAnchorPointY(y);

        return anchorPoint;
    }

    @Override
    public Displacement createDisplacement(Expression x, Expression y) {
        Displacement displacement = new Displacement(filterFactory);
        displacement.setDisplacementX(x);
        displacement.setDisplacementY(y);

        return displacement;
    }

    @Override
    public PointSymbolizer createPointSymbolizer() {
        PointSymbolizer copy = new PointSymbolizer();

        return copy;
    }

    @Override
    public Halo createHalo(Fill fill, Expression radius) {
        Halo halo = new Halo(filterFactory);
        halo.setFill(fill);
        halo.setRadius(radius);

        return halo;
    }

    @Override
    public Fill getDefaultFill() {
        Fill fill = new Fill(filterFactory);

        try {
            fill.setColor(filterFactory.literal("#808080"));
            fill.setOpacity(filterFactory.literal(Double.valueOf(1.0)));
            fill.setGraphicFill(getDefaultGraphic());
        } catch (org.geotools.filter.IllegalFilterException ife) {
            throw new RuntimeException("Error creating fill", ife);
        }

        return fill;
    }

    @Override
    public LineSymbolizer getDefaultLineSymbolizer() {
        return createLineSymbolizer(getDefaultStroke(), null);
    }

    @Override
    public Mark getDefaultMark() {
        return getSquareMark();
    }

    @Override
    public PointSymbolizer getDefaultPointSymbolizer() {
        return createPointSymbolizer(createDefaultGraphic(), null);
    }

    @Override
    public PolygonSymbolizer getDefaultPolygonSymbolizer() {
        return createPolygonSymbolizer(getDefaultStroke(), getDefaultFill(), null);
    }

    @Override
    public Stroke getDefaultStroke() {
        try {
            Stroke stroke =
                    createStroke(
                            filterFactory.literal("#000000"),
                            filterFactory.literal(Integer.valueOf(1)));

            stroke.setDashOffset(filterFactory.literal(Integer.valueOf(0)));
            stroke.setDashArray(Stroke.DEFAULT.getDashArray());
            stroke.setLineCap(filterFactory.literal("butt"));
            stroke.setLineJoin(filterFactory.literal("miter"));
            stroke.setOpacity(filterFactory.literal(Integer.valueOf(1)));

            return stroke;
        } catch (org.geotools.filter.IllegalFilterException ife) {
            // we should never be in here
            throw new RuntimeException("Error creating stroke", ife);
        }
    }

    @Override
    public Style getDefaultStyle() {
        Style style = createStyle();

        return style;
    }

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and
     * defaultPointPlacement, Sets the geometry attribute name to be geometry:text. No Halo is set.
     * <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    @Override
    public TextSymbolizer getDefaultTextSymbolizer() {
        return createTextSymbolizer(
                getDefaultFill(),
                new Font[] {getDefaultFont()},
                null,
                null,
                getDefaultPointPlacement(),
                "geometry:text");
    }

    /**
     * Creates a defaultFont which is valid on all machines. The font is of size 10, Style and
     * Weight normal and uses a serif font.
     *
     * @return the default Font
     */
    @Override
    public Font getDefaultFont() {
        return Font.createDefault(filterFactory);
    }

    @Override
    public Graphic createDefaultGraphic() {
        Graphic graphic = new Graphic(filterFactory);
        graphic.graphicalSymbols()
                .add(createMark()); // a default graphic is assumed to have a single Mark
        graphic.setSize(Expression.NIL);
        graphic.setOpacity(filterFactory.literal(1.0));
        graphic.setRotation(filterFactory.literal(0.0));

        return graphic;
    }

    @Override
    public Graphic getDefaultGraphic() {
        return createDefaultGraphic();
    }

    /**
     * returns a default PointPlacement with a 0,0 anchorPoint and a displacement of 0,0 and a
     * rotation of 0
     *
     * @return a default PointPlacement.
     */
    @Override
    public PointPlacement getDefaultPointPlacement() {
        return this.createPointPlacement(
                PointPlacement.DEFAULT_ANCHOR_POINT,
                this.createDisplacement(filterFactory.literal(0), filterFactory.literal(0)),
                filterFactory.literal(0));
    }

    @Override
    public RasterSymbolizer createRasterSymbolizer() {
        return new RasterSymbolizer(filterFactory);
    }

    @Override
    public org.geotools.api.style.RasterSymbolizer createRasterSymbolizer(
            String geometryPropertyName,
            Expression opacity,
            org.geotools.api.style.ChannelSelection channel,
            Expression overlap,
            org.geotools.api.style.ColorMap colorMap,
            org.geotools.api.style.ContrastEnhancement ce,
            org.geotools.api.style.ShadedRelief relief,
            org.geotools.api.style.Symbolizer outline) {
        return null;
    }

    @Override
    public RasterSymbolizer createRasterSymbolizer(
            String geometryPropertyName,
            Expression opacity,
            ChannelSelection channel,
            Expression overlap,
            ColorMap colorMap,
            ContrastEnhancement cenhancement,
            ShadedRelief relief,
            Symbolizer outline) {
        RasterSymbolizer rastersym = new RasterSymbolizer(filterFactory);

        if (geometryPropertyName != null) {
            rastersym.setGeometryPropertyName(geometryPropertyName);
        }

        if (opacity != null) {
            rastersym.setOpacity(opacity);
        }

        if (channel != null) {
            rastersym.setChannelSelection(channel);
        }

        if (overlap != null) {
            rastersym.setOverlap(overlap);
        }

        if (colorMap != null) {
            rastersym.setColorMap(colorMap);
        }

        if (cenhancement != null) {
            rastersym.setContrastEnhancement(cenhancement);
        }

        if (relief != null) {
            rastersym.setShadedRelief(relief);
        }

        if (outline != null) {
            rastersym.setImageOutline(outline);
        }

        return rastersym;
    }

    @Override
    public RasterSymbolizer getDefaultRasterSymbolizer() {
        return createRasterSymbolizer(
                null, filterFactory.literal(1.0), null, null, null, null, null, null);
    }

    @Override
    public org.geotools.api.style.ChannelSelection createChannelSelection(
            org.geotools.api.style.SelectedChannelType... channels) {
        return null;
    }

    @Override
    public ChannelSelection createChannelSelection(SelectedChannelType[] channels) {
        ChannelSelection channelSel = new ChannelSelection();

        if ((channels != null) && (channels.length > 0)) {
            if (channels.length == 1) {
                channelSel.setGrayChannel(channels[0]);
            } else {
                channelSel.setRGBChannels(channels);
            }
        }

        return channelSel;
    }

    @Override
    public ColorMap createColorMap() {
        return new ColorMap();
    }

    @Override
    public ColorMapEntry createColorMapEntry() {
        return new ColorMapEntry();
    }

    @Override
    public ContrastEnhancement createContrastEnhancement() {
        return new ContrastEnhancement(filterFactory);
    }

    @Override
    public ContrastEnhancement createContrastEnhancement(Expression gammaValue) {
        ContrastEnhancement ce = new ContrastEnhancement();
        ce.setGammaValue(gammaValue);

        return ce;
    }

    @Override
    public org.geotools.api.style.SelectedChannelType createSelectedChannelType(
            Expression name, org.geotools.api.style.ContrastEnhancement enhancement) {
        return null;
    }

    @Override
    public org.geotools.api.style.SelectedChannelType createSelectedChannelType(
            String name, org.geotools.api.style.ContrastEnhancement enhancement) {
        return null;
    }

    @Override
    public SelectedChannelType createSelectedChannelType(
            Expression name, ContrastEnhancement enhancement) {
        SelectedChannelType sct = new SelectedChannelType(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(enhancement);

        return sct;
    }

    @Override
    public SelectedChannelType createSelectedChannelType(
            String name, ContrastEnhancement enhancement) {
        Expression nameExp = filterFactory.literal(name);
        return createSelectedChannelType(nameExp, enhancement);
    }

    @Override
    public SelectedChannelType createSelectedChannelType(Expression name, Expression gammaValue) {
        SelectedChannelType sct = new SelectedChannelType(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(createContrastEnhancement(gammaValue));

        return sct;
    }

    @Override
    public StyledLayerDescriptor createStyledLayerDescriptor() {
        return new StyledLayerDescriptor();
    }

    @Override
    public UserLayer createUserLayer() {
        return new UserLayer();
    }

    @Override
    public NamedLayer createNamedLayer() {
        return new NamedLayer();
    }

    @Override
    public RemoteOWS createRemoteOWS(String service, String onlineResource) {
        RemoteOWS remoteOWS = new RemoteOWS();
        remoteOWS.setService(service);
        remoteOWS.setOnlineResource(onlineResource);

        return remoteOWS;
    }

    @Override
    public ShadedRelief createShadedRelief(Expression reliefFactor) {
        ShadedRelief relief = new ShadedRelief(filterFactory);
        relief.setReliefFactor(reliefFactor);

        return relief;
    }
    //
    // Start of GeoAPI StyleFacstory implementation
    //

    @Override
    public AnchorPoint anchorPoint(Expression x, Expression y) {
        return createAnchorPoint(x, y);
    }

    @Override
    public ChannelSelection channelSelection(org.geotools.api.style.SelectedChannelType gray) {
        return (ChannelSelection) createChannelSelection(gray);
    }

    @Override
    public ChannelSelection channelSelection(
            org.geotools.api.style.SelectedChannelType red,
            org.geotools.api.style.SelectedChannelType green,
            org.geotools.api.style.SelectedChannelType blue) {
        return (ChannelSelection) createChannelSelection(red, green, blue);
    }

    @Override
    public ColorMap colorMap(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        for (int i = 0; i < mapping.length; i++) {
            arguments[i + 1] = mapping[i];
        }
        Function function = filterFactory.function("Categorize", arguments);
        ColorMap colorMap = new ColorMap(function);
        return colorMap;
    }

    @Override
    public org.geotools.styling.ColorReplacement colorReplacement(
            Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        for (int i = 0; i < mapping.length; i++) {
            arguments[i + 1] = mapping[i];
        }
        Function function = filterFactory.function("Recode", arguments);
        ColorReplacement colorRep = new org.geotools.styling.ColorReplacement(function);

        return (org.geotools.styling.ColorReplacement) colorRep;
    }

    @Override
    public ContrastEnhancement contrastEnhancement(
            Expression gamma, ContrastMethod contrastMethod) {
        ContrastMethod meth = ContrastMethod.NONE;
        if (ContrastMethod.NORMALIZE.equals(contrastMethod)) {
            meth = ContrastMethod.NORMALIZE;
        } else if (ContrastMethod.HISTOGRAM.equals(contrastMethod)) {
            meth = ContrastMethod.HISTOGRAM;
        } else if (ContrastMethod.LOGARITHMIC.equals(contrastMethod)) {
            meth = ContrastMethod.LOGARITHMIC;
        } else if (ContrastMethod.EXPONENTIAL.equals(contrastMethod)) {
            meth = ContrastMethod.EXPONENTIAL;
        }
        return new ContrastEnhancement(filterFactory, gamma, meth);
    }

    @Override
    public org.geotools.styling.Description description(
            InternationalString title, InternationalString description) {
        return new org.geotools.styling.Description(title, description);
    }

    @Override
    public Displacement displacement(Expression dx, Expression dy) {
        return new Displacement(dx, dy);
    }

    @Override
    public ExternalGraphic externalGraphic(Icon inline, Collection<ColorReplacement> replacements) {
        return new ExternalGraphic(inline, replacements, null);
    }

    @Override
    public ExternalGraphic externalGraphic(
            OnLineResource resource, String format, Collection<ColorReplacement> replacements) {
        ExternalGraphic externalGraphic = new ExternalGraphic(null, replacements, resource);
        externalGraphic.setFormat(format);
        return externalGraphic;
    }

    @Override
    public org.geotools.styling.ExternalMark externalMark(Icon inline) {
        return new org.geotools.styling.ExternalMark(inline);
    }

    @Override
    public org.geotools.styling.ExternalMark externalMark(
            OnLineResource resource, String format, int markIndex) {
        return new org.geotools.styling.ExternalMark(resource, format, markIndex);
    }

    @Override
    public FeatureTypeStyle featureTypeStyle(
            String name,
            Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<SemanticType> types,
            List<org.geotools.api.style.Rule> rules) {
        FeatureTypeStyle featureTypeStyle = new FeatureTypeStyle();
        featureTypeStyle.setName(name);

        if (description != null && description.getTitle() != null) {
            featureTypeStyle.getDescription().setTitle(description.getTitle());
        }
        if (description != null && description.getAbstract() != null) {
            featureTypeStyle.getDescription().setAbstract(description.getAbstract());
        }
        // featureTypeStyle.setFeatureInstanceIDs( defainedFor );
        featureTypeStyle.featureTypeNames().addAll(featureTypeNames);
        featureTypeStyle.semanticTypeIdentifiers().addAll(types);

        for (org.geotools.api.style.Rule rule : rules) {
            if (rule instanceof Rule) {
                featureTypeStyle.rules().add((Rule) rule);
            } else {
                featureTypeStyle.rules().add(new Rule(rule));
            }
        }
        return featureTypeStyle;
    }

    @Override
    public Fill fill(GraphicFill graphicFill, Expression color, Expression opacity) {
        Fill fill = new Fill(filterFactory);
        fill.setGraphicFill(graphicFill);
        fill.setColor(color);
        fill.setOpacity(opacity);
        return fill;
    }

    @Override
    public Font font(
            List<Expression> family, Expression style, Expression weight, Expression size) {
        Font font = new Font();
        font.getFamily().addAll(family);
        font.setStyle(style);
        font.setWeight(weight);
        font.setSize(size);

        return font;
    }

    @Override
    public Graphic graphic(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp) {
        Graphic graphic = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphic.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphic.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphic.setOpacity(opacity);
        graphic.setSize(size);
        graphic.setRotation(rotation);
        graphic.setAnchorPoint(anchor);
        graphic.setDisplacement(disp);
        return graphic;
    }

    @Override
    public GraphicFill graphicFill(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {
        Graphic graphicFill = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphicFill.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphicFill.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphicFill.setOpacity(opacity);
        graphicFill.setSize(size);
        graphicFill.setRotation(rotation);
        graphicFill.setAnchorPoint(anchorPoint);
        graphicFill.setDisplacement(displacement);

        return (GraphicFill) graphicFill;
    }

    @Override
    public org.geotools.styling.GraphicLegend graphicLegend(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {
        GraphicLegend graphicLegend = (GraphicLegend) new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphicLegend.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphicLegend.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphicLegend.setOpacity(opacity);
        graphicLegend.setSize(size);
        graphicLegend.setRotation(rotation);
        graphicLegend.setAnchorPoint(anchorPoint);
        graphicLegend.setDisplacement(displacement);

        return graphicLegend;
    }

    @Override
    public GraphicStroke graphicStroke(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression initialGap,
            Expression gap) {
        Graphic graphicStroke = new Graphic(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphic) {
                    graphicStroke.graphicalSymbols().add(ExternalGraphic.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof Mark) {
                    graphicStroke.graphicalSymbols().add(Mark.cast(graphicalSymbol));
                }
            }
        }
        graphicStroke.setOpacity(opacity);
        graphicStroke.setSize(size);
        graphicStroke.setRotation(rotation);
        graphicStroke.setAnchorPoint(anchorPoint);
        graphicStroke.setDisplacement(displacement);
        graphicStroke.setInitialGap(initialGap);
        graphicStroke.setGap(gap);

        return (GraphicStroke) graphicStroke;
    }

    @Override
    public Halo halo(org.geotools.api.style.Fill fill, Expression radius) {
        Halo halo = new Halo();
        halo.setFill(fill);
        halo.setRadius(radius);
        return halo;
    }

    @Override
    public LinePlacement linePlacement(
            Expression offset,
            Expression initialGap,
            Expression gap,
            boolean repeated,
            boolean aligned,
            boolean generalizedLine) {
        LinePlacement placement = new LinePlacement(filterFactory);
        placement.setPerpendicularOffset(offset);
        placement.setInitialGap(initialGap);
        placement.setGap(gap);
        placement.setRepeated(repeated);
        placement.setAligned(aligned);
        placement.setGeneralized(generalizedLine);

        return placement;
    }

    @Override
    public LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset) {
        LineSymbolizer copy = new LineSymbolizer();
        copy.setDescription(description);
        copy.setGeometry(geometry);
        copy.setName(name);
        copy.setPerpendicularOffset(offset);
        copy.setStroke(stroke);
        copy.setUnitOfMeasure((Unit<Length>) unit);
        return copy;
    }

    @Override
    public Mark mark(
            Expression wellKnownName,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        Mark mark = new Mark(filterFactory, null);
        mark.setWellKnownName(wellKnownName);
        mark.setFill(fill);
        mark.setStroke(stroke);

        return mark;
    }

    @Override
    public Mark mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        Mark mark = new Mark();
        mark.setExternalMark(externalMark);
        mark.setFill(fill);
        mark.setStroke(stroke);

        return mark;
    }

    @Override
    public PointPlacement pointPlacement(
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        PointPlacement pointPlacement = new PointPlacement(filterFactory);
        pointPlacement.setAnchorPoint(anchor);
        pointPlacement.setDisplacement(displacement);
        pointPlacement.setRotation(rotation);
        return pointPlacement;
    }

    @Override
    public PointSymbolizer pointSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Graphic graphic) {
        PointSymbolizer copy = new PointSymbolizer();
        copy.setDescription(description);
        copy.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        copy.setGraphic(graphic);
        copy.setName(name);
        copy.setUnitOfMeasure((Unit<Length>) unit);
        return copy;
    }

    @Override
    public PolygonSymbolizer polygonSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Displacement displacement,
            Expression offset) {
        PolygonSymbolizer polygonSymbolizer = new PolygonSymbolizer();
        polygonSymbolizer.setStroke(stroke);
        polygonSymbolizer.setDescription(description);
        polygonSymbolizer.setDisplacement(displacement);
        polygonSymbolizer.setFill(fill);
        polygonSymbolizer.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        polygonSymbolizer.setName(name);
        polygonSymbolizer.setPerpendicularOffset(offset);
        polygonSymbolizer.setUnitOfMeasure((Unit<Length>) unit);
        return polygonSymbolizer;
    }

    @Override
    public RasterSymbolizer rasterSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            Expression opacity,
            org.geotools.api.style.ChannelSelection channelSelection,
            OverlapBehavior overlapsBehaviour,
            org.geotools.api.style.ColorMap colorMap,
            org.geotools.api.style.ContrastEnhancement contrast,
            org.geotools.api.style.ShadedRelief shaded,
            org.geotools.api.style.Symbolizer outline) {
        RasterSymbolizer rasterSymbolizer = new RasterSymbolizer(filterFactory);
        rasterSymbolizer.setChannelSelection(channelSelection);
        rasterSymbolizer.setColorMap(colorMap);
        rasterSymbolizer.setContrastEnhancement(contrast);
        rasterSymbolizer.setDescription(description);
        if (geometry != null) {
            rasterSymbolizer.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        } else {
            rasterSymbolizer.setGeometryPropertyName(null);
        }
        rasterSymbolizer.setImageOutline(outline);
        rasterSymbolizer.setName(name);
        rasterSymbolizer.setOpacity(opacity);
        rasterSymbolizer.setOverlapBehavior(overlapsBehaviour);
        rasterSymbolizer.setShadedRelief(shaded);
        rasterSymbolizer.setUnitOfMeasure((Unit<Length>) unit);
        return rasterSymbolizer;
    }

    @Override
    public ExtensionSymbolizer extensionSymbolizer(
            String name,
            String propertyName,
            Description description,
            Unit<?> unit,
            String extensionName,
            Map<String, Expression> parameters) {
        VendorSymbolizer extension = new VendorSymbolizer();
        extension.setName(name);
        extension.setGeometryPropertyName(propertyName);
        extension.setDescription(description);
        extension.setUnitOfMeasure((Unit<Length>) unit);
        extension.setExtensionName(extensionName);
        extension.getParameters().putAll(parameters);
        return extension;
    }

    @Override
    public Rule rule(
            String name,
            Description description,
            org.geotools.api.style.GraphicLegend legend,
            double min,
            double max,
            List<org.geotools.api.style.Symbolizer> symbolizers,
            Filter filter) {
        Rule rule = new Rule();
        rule.setName(name);
        rule.setDescription(description);
        rule.setLegend(legend);
        rule.setMinScaleDenominator(min);
        rule.setMaxScaleDenominator(max);
        if (symbolizers != null) {
            for (org.geotools.api.style.Symbolizer symbolizer : symbolizers) {
                rule.symbolizers().add((Symbolizer) symbolizer);
            }
        }
        if (filter != null) {
            rule.setFilter(filter);
            rule.setElseFilter(false);
        } else {
            rule.setElseFilter(true);
        }
        return rule;
    }

    @Override
    public SelectedChannelType selectedChannelType(
            Expression channelName,
            org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        SelectedChannelType selectedChannelType = new SelectedChannelType(filterFactory);
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement(contrastEnhancement);
        return selectedChannelType;
    }

    @Override
    public SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        SelectedChannelType selectedChannelType = new SelectedChannelType(filterFactory);
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement(contrastEnhancement);
        return selectedChannelType;
    }

    @Override
    public ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly) {
        ShadedRelief shadedRelief = new ShadedRelief(filterFactory);
        shadedRelief.setReliefFactor(reliefFactor);
        shadedRelief.setBrightnessOnly(brightnessOnly);
        return shadedRelief;
    }

    @Override
    public Stroke stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        Stroke stroke = new Stroke(filterFactory);
        stroke.setColor(color);
        stroke.setOpacity(opacity);
        stroke.setWidth(width);
        stroke.setLineJoin(join);
        stroke.setLineCap(cap);
        stroke.setDashArray(dashes);
        stroke.setDashOffset(offset);
        return stroke;
    }

    @Override
    public Stroke stroke(
            GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        Stroke stroke = new Stroke(filterFactory);
        stroke.setGraphicFill(fill);
        stroke.setColor(color);
        stroke.setOpacity(opacity);
        stroke.setWidth(width);
        stroke.setLineJoin(join);
        stroke.setLineCap(cap);
        stroke.setDashArray(dashes);
        stroke.setDashOffset(offset);
        return stroke;
    }

    @Override
    public Stroke stroke(
            GraphicStroke stroke,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        Stroke s = new Stroke(filterFactory);
        s.setColor(color);
        s.setWidth(width);
        s.setOpacity(opacity);
        s.setLineJoin(join);
        s.setLineCap(cap);
        s.setDashArray(dashes);
        s.setDashOffset(offset);
        s.setGraphicStroke(stroke);

        return s;
    }

    @Override
    public Style style(
            String name,
            Description description,
            boolean isDefault,
            List<org.geotools.api.style.FeatureTypeStyle> featureTypeStyles,
            org.geotools.api.style.Symbolizer defaultSymbolizer) {
        Style style = new Style();
        style.setName(name);
        style.setDescription((org.geotools.styling.Description) description);
        style.setDefault(isDefault);
        if (featureTypeStyles != null) {
            for (org.geotools.api.style.FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
                style.featureTypeStyles().add(FeatureTypeStyle.cast(featureTypeStyle));
            }
        }
        style.setDefaultSpecification((Symbolizer) defaultSymbolizer);
        return style;
    }

    @Override
    public TextSymbolizer textSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            Expression label,
            org.geotools.api.style.Font font,
            org.geotools.api.style.LabelPlacement placement,
            org.geotools.api.style.Halo halo,
            org.geotools.api.style.Fill fill) {
        TextSymbolizer tSymb = new TextSymbolizer(filterFactory);
        tSymb.setName(name);
        tSymb.setFill(fill);
        tSymb.setUnitOfMeasure((Unit<Length>) unit);
        tSymb.setFont(font);
        tSymb.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(placement);
        // tSymb.setGraphic( GraphicImpl.cast(graphic));
        tSymb.setDescription(description);
        return tSymb;
    }

    @Override
    public org.geotools.api.style.ContrastEnhancement contrastEnhancement(
            Expression gamma, String method) {

        ContrastMethod meth = ContrastMethod.NONE;
        if (ContrastMethod.NORMALIZE.matches(method)) {
            meth = ContrastMethod.NORMALIZE;
        } else if (ContrastMethod.HISTOGRAM.matches(method)) {
            meth = ContrastMethod.HISTOGRAM;
        } else if (ContrastMethod.LOGARITHMIC.matches(method)) {
            meth = ContrastMethod.LOGARITHMIC;
        } else if (ContrastMethod.EXPONENTIAL.matches(method)) {
            meth = ContrastMethod.EXPONENTIAL;
        }
        return new ContrastEnhancement(filterFactory, gamma, meth);
    }

    @Override
    public ContrastMethod createContrastMethod(org.geotools.api.style.ContrastMethod method) {
        return method;
    }
}
