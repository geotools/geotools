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
    static final FillImpl DEFAULT_FILL = (FillImpl) ConstantFill.DEFAULT;
    static final StrokeImpl DEFAULT_STROKE = ConstantStroke.DEFAULT;
    private final FilterFactory filterFactory;

    public StyleFactory() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    protected StyleFactory(FilterFactory factory) {
        filterFactory = factory;
    }

    @Override
    public TextSymbolizerImpl createTextSymbolizer() {
        return new TextSymbolizerImpl(filterFactory);
    }

    @Override
    public org.geotools.api.style.PointPlacement createPointPlacement(
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        PointPlacementImpl pointPlacement = new PointPlacementImpl(filterFactory);
        pointPlacement.setAnchorPoint(anchorPoint);
        pointPlacement.setDisplacement(displacement);
        pointPlacement.setRotation(rotation);
        return pointPlacement;
    }

    @Override
    public TextSymbolizerImpl createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            org.geotools.api.style.LabelPlacement labelPlacement,
            String geometryPropertyName) {
        TextSymbolizerImpl tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setFill(fill);
        Collection<FontImpl> nFonts = new ArrayList<>();

        if (fonts != null) {
            for (org.geotools.api.style.Font font : fonts) {
                nFonts.add((FontImpl) font);
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
    public TextSymbolizerImpl createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            org.geotools.api.style.LabelPlacement labelPlacement,
            String geometryPropertyName,
            org.geotools.api.style.Graphic graphic) {
        TextSymbolizerImpl tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setFill(fill);
        Collection<FontImpl> nFonts = new ArrayList<>();

        if (fonts != null) {
            for (org.geotools.api.style.Font font : fonts) {
                nFonts.add((FontImpl) font);
            }
            tSymb.fonts().addAll(nFonts);
        }
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(labelPlacement);
        tSymb.setGraphic((GraphicImpl) graphic);

        return tSymb;
    }

    @Override
    public ExtentImpl createExtent(String name, String value) {
        ExtentImpl extent = new ExtentImpl();
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
    public FeatureTypeConstraintImpl createFeatureTypeConstraint(
            String featureTypeName, Filter filter, org.geotools.api.style.Extent... extents) {
        FeatureTypeConstraintImpl constraint = new FeatureTypeConstraintImpl();
        constraint.setFeatureTypeName(featureTypeName);
        constraint.setFilter(filter);
        constraint.setExtents(extents);

        return constraint;
    }

    @Override
    public LayerFeatureConstraintsImpl createLayerFeatureConstraints(
            org.geotools.api.style.FeatureTypeConstraint[] featureTypeConstraints) {
        LayerFeatureConstraintsImpl constraints = new LayerFeatureConstraintsImpl();
        constraints.setFeatureTypeConstraints(featureTypeConstraints);

        return constraints;
    }

    @Override
    public FeatureTypeStyleImpl createFeatureTypeStyle() {
        return new FeatureTypeStyleImpl();
    }

    @Override
    public Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        GraphicImpl g = new GraphicImpl();
        g.graphicalSymbols().addAll(List.of(externalGraphics));
        g.graphicalSymbols().addAll(List.of(marks));
        g.graphicalSymbols().addAll(List.of(symbols));
        g.setOpacity(opacity);
        g.setRotation(rotation);
        g.setSize(size);
        return g;
    }

    @Override
    public FeatureTypeStyle createFeatureTypeStyle(Rule rule) {
        FeatureTypeStyleImpl fts = new FeatureTypeStyleImpl();
        fts.rules().add(rule);
        return fts;
    }

    @Override
    public FeatureTypeStyle createFeatureTypeStyle(Rule[] rules) {
        return new FeatureTypeStyleImpl((RuleImpl[]) rules);
    }

    @Override
    public Rule createRule() {
        return new RuleImpl();
    }

    @Override
    public LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName) {
        LineSymbolizerImpl ls = new LineSymbolizerImpl();
        ls.setStroke(stroke);
        ls.setGeometryPropertyName(geometryPropertyName);
        return ls;
    }

    public RuleImpl createRule(
            Symbolizer[] symbolizers,
            Description desc,
            GraphicLegend legend,
            String name,
            Filter filter,
            boolean isElseFilter,
            double maxScale,
            double minScale) {

        RuleImpl r =
                new RuleImpl(
                        symbolizers, desc, legend, name, filter, isElseFilter, maxScale, minScale);

        return r;
    }

    @Override
    public ImageOutlineImpl createImageOutline(Symbolizer symbolizer) {
        ImageOutlineImpl outline = new ImageOutlineImpl();
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
    public StrokeImpl createStroke(Expression color, Expression width) {
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
    public StrokeImpl createStroke(Expression color, Expression width, Expression opacity) {
        return (StrokeImpl)
                createStroke(
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
        StrokeImpl stroke = new StrokeImpl(filterFactory);

        if (color == null) {
            // use default
            color = StrokeImpl.DEFAULT.getColor();
        }
        stroke.setColor(color);

        if (width == null) {
            // use default
            width = StrokeImpl.DEFAULT.getWidth();
        }
        stroke.setWidth(width);

        if (opacity == null) {
            opacity = StrokeImpl.DEFAULT.getOpacity();
        }
        stroke.setOpacity(opacity);

        if (lineJoin == null) {
            lineJoin = StrokeImpl.DEFAULT.getLineJoin();
        }
        stroke.setLineJoin(lineJoin);

        if (lineCap == null) {
            lineCap = StrokeImpl.DEFAULT.getLineCap();
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
        FillImpl fill = new FillImpl(filterFactory);

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
        return new LineSymbolizerImpl();
    }

    @Override
    public PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName) {
        PointSymbolizerImpl pSymb = new PointSymbolizerImpl();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setGraphic(graphic);

        return pSymb;
    }

    @Override
    public StyleImpl createStyle() {
        return new StyleImpl();
    }

    @Override
    public NamedStyleImpl createNamedStyle() {
        return new NamedStyleImpl();
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
    public MarkImpl createMark(
            Expression wellKnownName,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            Expression size,
            Expression rotation) {
        MarkImpl mark = new MarkImpl(filterFactory, null);

        if (wellKnownName == null) {
            throw new IllegalArgumentException("WellKnownName can not be null in mark");
        }

        mark.setWellKnownName(wellKnownName);
        mark.setStroke(stroke);
        mark.setFill(fill);

        return mark;
    }

    @Override
    public MarkImpl getSquareMark() {
        FillImpl fill = (FillImpl) getDefaultFill();
        MarkImpl mark =
                createMark(
                        filterFactory.literal("Square"),
                        getDefaultStroke(),
                        fill,
                        filterFactory.literal(6),
                        filterFactory.literal(0));

        return mark;
    }

    @Override
    public MarkImpl getCircleMark() {
        MarkImpl mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Circle"));

        return mark;
    }

    @Override
    public MarkImpl getCrossMark() {
        MarkImpl mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Cross"));

        return mark;
    }

    @Override
    public MarkImpl getXMark() {
        MarkImpl mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("X"));

        return mark;
    }

    @Override
    public MarkImpl getTriangleMark() {
        MarkImpl mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Triangle"));

        return mark;
    }

    @Override
    public MarkImpl getStarMark() {
        MarkImpl mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Star"));

        return mark;
    }

    @Override
    public MarkImpl createMark() {
        MarkImpl mark = new MarkImpl(filterFactory, null);

        return mark;
    }

    @Override
    public PolygonSymbolizer createPolygonSymbolizer(
            Stroke stroke, Fill fill, String geometryPropertyName) {
        PolygonSymbolizerImpl poly = createPolygonSymbolizer();
        poly.setFill(fill);
        poly.setStroke(stroke);
        poly.setGeometryPropertyName(geometryPropertyName);
        return  poly;
    }

    @Override
    public GraphicImpl createGraphic(
            ExternalGraphicImpl[] externalGraphics,
            MarkImpl[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        GraphicImpl graphic = new GraphicImpl(filterFactory);

        symbols = symbols != null ? symbols : Symbol.SYMBOLS_EMPTY;
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
    public ExternalGraphicImpl createExternalGraphic(String uri, String format) {
        ExternalGraphicImpl extg = new ExternalGraphicImpl();
        extg.setURI(uri);
        extg.setFormat(format);

        return extg;
    }

    @Override
    public ExternalGraphicImpl createExternalGraphic(Icon inlineContent, String format) {
        ExternalGraphicImpl extg = new ExternalGraphicImpl();
        extg.setInlineContent(inlineContent);
        extg.setFormat(format);
        return extg;
    }

    @Override
    public ExternalGraphicImpl createExternalGraphic(java.net.URL url, String format) {
        ExternalGraphicImpl extg = new ExternalGraphicImpl();
        extg.setLocation(url);
        extg.setFormat(format);

        return extg;
    }

    @Override
    public FontImpl createFont(
            Expression fontFamily,
            Expression fontStyle,
            Expression fontWeight,
            Expression fontSize) {
        FontImpl font = new FontImpl();

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
    public LinePlacementImpl createLinePlacement(Expression offset) {
        LinePlacementImpl linep = new LinePlacementImpl(filterFactory);
        linep.setPerpendicularOffset(offset);

        return linep;
    }

    @Override
    public PolygonSymbolizerImpl createPolygonSymbolizer() {
        return new PolygonSymbolizerImpl();
    }

    @Override
    public AnchorPointImpl createAnchorPoint(Expression x, Expression y) {
        AnchorPointImpl anchorPoint = new AnchorPointImpl(filterFactory);
        anchorPoint.setAnchorPointX(x);
        anchorPoint.setAnchorPointY(y);

        return anchorPoint;
    }

    @Override
    public DisplacementImpl createDisplacement(Expression x, Expression y) {
        DisplacementImpl displacement = new DisplacementImpl(filterFactory);
        displacement.setDisplacementX(x);
        displacement.setDisplacementY(y);

        return displacement;
    }

    @Override
    public PointSymbolizerImpl createPointSymbolizer() {
        PointSymbolizerImpl copy = new PointSymbolizerImpl();

        return copy;
    }

    @Override
    public Halo createHalo(Fill fill, Expression radius) {
        HaloImpl halo = new HaloImpl(filterFactory);
        halo.setFill(fill);
        halo.setRadius(radius);

        return halo;
    }

    @Override
    public Fill getDefaultFill() {
        FillImpl fill = new FillImpl(filterFactory);

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
    public MarkImpl getDefaultMark() {
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
    public StrokeImpl getDefaultStroke() {
        try {
            StrokeImpl stroke =
                    createStroke(
                            filterFactory.literal("#000000"),
                            filterFactory.literal(Integer.valueOf(1)));

            stroke.setDashOffset(filterFactory.literal(Integer.valueOf(0)));
            stroke.setDashArray(StrokeImpl.DEFAULT.getDashArray());
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
    public StyleImpl getDefaultStyle() {
        StyleImpl style = createStyle();

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
    public TextSymbolizerImpl getDefaultTextSymbolizer() {
        return createTextSymbolizer(
                getDefaultFill(),
                new FontImpl[] {getDefaultFont()},
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
    public FontImpl getDefaultFont() {
        return FontImpl.createDefault(filterFactory);
    }

    @Override
    public GraphicImpl createDefaultGraphic() {
        GraphicImpl graphic = new GraphicImpl(filterFactory);
        graphic.graphicalSymbols()
                .add(createMark()); // a default graphic is assumed to have a single Mark
        graphic.setSize(Expression.NIL);
        graphic.setOpacity(filterFactory.literal(1.0));
        graphic.setRotation(filterFactory.literal(0.0));

        return graphic;
    }

    @Override
    public GraphicImpl getDefaultGraphic() {
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
                PointPlacementImpl.DEFAULT_ANCHOR_POINT,
                this.createDisplacement(filterFactory.literal(0), filterFactory.literal(0)),
                filterFactory.literal(0));
    }

    @Override
    public RasterSymbolizerImpl createRasterSymbolizer() {
        return new RasterSymbolizerImpl(filterFactory);
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
        RasterSymbolizerImpl rastersym = new RasterSymbolizerImpl(filterFactory);

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
    public ChannelSelection createChannelSelection(SelectedChannelType... channels) {
        return null;
    }

    @Override
    public ChannelSelectionImpl createChannelSelection(SelectedChannelTypeImpl[] channels) {
        ChannelSelectionImpl channelSel = new ChannelSelectionImpl();

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
    public ColorMapImpl createColorMap() {
        return new ColorMapImpl();
    }

    @Override
    public ColorMapEntryImpl createColorMapEntry() {
        return new ColorMapEntryImpl();
    }

    @Override
    public ContrastEnhancementImpl createContrastEnhancement() {
        return new ContrastEnhancementImpl(filterFactory);
    }

    @Override
    public ContrastEnhancementImpl createContrastEnhancement(Expression gammaValue) {
        ContrastEnhancementImpl ce = new ContrastEnhancementImpl();
        ce.setGammaValue(gammaValue);

        return ce;
    }

    @Override
    public SelectedChannelType createSelectedChannelType(
            Expression name, ContrastEnhancement enhancement) {
        SelectedChannelTypeImpl sct = new SelectedChannelTypeImpl(filterFactory);
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
    public SelectedChannelTypeImpl createSelectedChannelType(
            Expression name, Expression gammaValue) {
        SelectedChannelTypeImpl sct = new SelectedChannelTypeImpl(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(createContrastEnhancement(gammaValue));

        return sct;
    }

    @Override
    public StyledLayerDescriptor createStyledLayerDescriptor() {
        return new StyledLayerDescriptor();
    }

    @Override
    public UserLayerImpl createUserLayer() {
        return new UserLayerImpl();
    }

    @Override
    public NamedLayerImpl createNamedLayer() {
        return new NamedLayerImpl();
    }

    @Override
    public RemoteOWSImpl createRemoteOWS(String service, String onlineResource) {
        RemoteOWSImpl remoteOWS = new RemoteOWSImpl();
        remoteOWS.setService(service);
        remoteOWS.setOnlineResource(onlineResource);

        return remoteOWS;
    }

    @Override
    public ShadedReliefImpl createShadedRelief(Expression reliefFactor) {
        ShadedReliefImpl relief = new ShadedReliefImpl(filterFactory);
        relief.setReliefFactor(reliefFactor);

        return relief;
    }
    //
    // Start of GeoAPI StyleFacstory implementation
    //

    @Override
    public AnchorPointImpl anchorPoint(Expression x, Expression y) {
        return createAnchorPoint(x, y);
    }

    @Override
    public ChannelSelectionImpl channelSelection(org.geotools.api.style.SelectedChannelType gray) {
        return (ChannelSelectionImpl) createChannelSelection(gray);
    }

    @Override
    public ChannelSelectionImpl channelSelection(
            org.geotools.api.style.SelectedChannelType red,
            org.geotools.api.style.SelectedChannelType green,
            org.geotools.api.style.SelectedChannelType blue) {
        return (ChannelSelectionImpl) createChannelSelection(red, green, blue);
    }

    @Override
    public ColorMapImpl colorMap(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        System.arraycopy(mapping, 0, arguments, 1, mapping.length);
        Function function = filterFactory.function("Categorize", arguments);
        ColorMapImpl colorMap = new ColorMapImpl(function);
        return colorMap;
    }

    @Override
    public ColorReplacementImpl colorReplacement(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        System.arraycopy(mapping, 0, arguments, 1, mapping.length);
        Function function = filterFactory.function("Recode", arguments);
        ColorReplacement colorRep = new ColorReplacementImpl(function);

        return (ColorReplacementImpl) colorRep;
    }

    @Override
    public ContrastEnhancementImpl contrastEnhancement(
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
        return new ContrastEnhancementImpl(filterFactory, gamma, meth);
    }

    @Override
    public DescriptionImpl description(InternationalString title, InternationalString description) {
        return new DescriptionImpl(title, description);
    }

    @Override
    public DisplacementImpl displacement(Expression dx, Expression dy) {
        return new DisplacementImpl(dx, dy);
    }

    @Override
    public ExternalGraphicImpl externalGraphic(
            Icon inline, Collection<ColorReplacement> replacements) {
        return new ExternalGraphicImpl(inline, replacements, null);
    }

    @Override
    public ExternalGraphicImpl externalGraphic(
            OnLineResource resource, String format, Collection<ColorReplacement> replacements) {
        ExternalGraphicImpl externalGraphic = new ExternalGraphicImpl(null, replacements, resource);
        externalGraphic.setFormat(format);
        return externalGraphic;
    }

    @Override
    public ExternalMarkImpl externalMark(Icon inline) {
        return new ExternalMarkImpl(inline);
    }

    @Override
    public ExternalMarkImpl externalMark(OnLineResource resource, String format, int markIndex) {
        return new ExternalMarkImpl(resource, format, markIndex);
    }

    @Override
    public FeatureTypeStyleImpl featureTypeStyle(
            String name,
            Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<SemanticType> types,
            List<org.geotools.api.style.Rule> rules) {
        FeatureTypeStyleImpl featureTypeStyle = new FeatureTypeStyleImpl();
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
            if (rule instanceof RuleImpl) {
                featureTypeStyle.rules().add(rule);
            } else {
                featureTypeStyle.rules().add(new RuleImpl(rule));
            }
        }
        return featureTypeStyle;
    }

    @Override
    public FillImpl fill(GraphicFill graphicFill, Expression color, Expression opacity) {
        FillImpl fill = new FillImpl(filterFactory);
        fill.setGraphicFill(graphicFill);
        fill.setColor(color);
        fill.setOpacity(opacity);
        return fill;
    }

    @Override
    public FontImpl font(
            List<Expression> family, Expression style, Expression weight, Expression size) {
        FontImpl font = new FontImpl();
        font.getFamily().addAll(family);
        font.setStyle(style);
        font.setWeight(weight);
        font.setSize(size);

        return font;
    }

    @Override
    public GraphicImpl graphic(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp) {
        GraphicImpl graphic = new GraphicImpl(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphicImpl) {
                    graphic.graphicalSymbols().add(ExternalGraphicImpl.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof MarkImpl) {
                    graphic.graphicalSymbols().add(MarkImpl.cast(graphicalSymbol));
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
        GraphicImpl graphicFill = new GraphicImpl(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphicImpl) {
                    graphicFill.graphicalSymbols().add(ExternalGraphicImpl.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof MarkImpl) {
                    graphicFill.graphicalSymbols().add(MarkImpl.cast(graphicalSymbol));
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
        GraphicLegend graphicLegend = (GraphicLegend) new GraphicImpl(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphicImpl) {
                    graphicLegend.graphicalSymbols().add(ExternalGraphicImpl.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof MarkImpl) {
                    graphicLegend.graphicalSymbols().add(MarkImpl.cast(graphicalSymbol));
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
        GraphicImpl graphicStroke = new GraphicImpl(filterFactory);
        if (symbols != null) {
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if (graphicalSymbol instanceof ExternalGraphicImpl) {
                    graphicStroke.graphicalSymbols().add(ExternalGraphicImpl.cast(graphicalSymbol));
                } else if (graphicalSymbol instanceof MarkImpl) {
                    graphicStroke.graphicalSymbols().add(MarkImpl.cast(graphicalSymbol));
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
    public HaloImpl halo(org.geotools.api.style.Fill fill, Expression radius) {
        HaloImpl halo = new HaloImpl();
        halo.setFill(fill);
        halo.setRadius(radius);
        return halo;
    }

    @Override
    public LinePlacementImpl linePlacement(
            Expression offset,
            Expression initialGap,
            Expression gap,
            boolean repeated,
            boolean aligned,
            boolean generalizedLine) {
        LinePlacementImpl placement = new LinePlacementImpl(filterFactory);
        placement.setPerpendicularOffset(offset);
        placement.setInitialGap(initialGap);
        placement.setGap(gap);
        placement.setRepeated(repeated);
        placement.setAligned(aligned);
        placement.setGeneralized(generalizedLine);

        return placement;
    }

    @Override
    public LineSymbolizerImpl lineSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset) {
        LineSymbolizerImpl copy = new LineSymbolizerImpl();
        copy.setDescription(description);
        copy.setGeometry(geometry);
        copy.setName(name);
        copy.setPerpendicularOffset(offset);
        copy.setStroke(stroke);
        copy.setUnitOfMeasure((Unit<Length>) unit);
        return copy;
    }

    @Override
    public MarkImpl mark(
            Expression wellKnownName,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        MarkImpl mark = new MarkImpl(filterFactory, null);
        mark.setWellKnownName(wellKnownName);
        mark.setFill(fill);
        mark.setStroke(stroke);

        return mark;
    }

    @Override
    public MarkImpl mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        MarkImpl mark = new MarkImpl();
        mark.setExternalMark(externalMark);
        mark.setFill(fill);
        mark.setStroke(stroke);

        return mark;
    }

    @Override
    public PointPlacementImpl pointPlacement(
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        PointPlacementImpl pointPlacement = new PointPlacementImpl(filterFactory);
        pointPlacement.setAnchorPoint(anchor);
        pointPlacement.setDisplacement(displacement);
        pointPlacement.setRotation(rotation);
        return pointPlacement;
    }

    @Override
    public PointSymbolizerImpl pointSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Graphic graphic) {
        PointSymbolizerImpl copy = new PointSymbolizerImpl();
        copy.setDescription(description);
        copy.setGeometryPropertyName(((PropertyName) geometry).getPropertyName());
        copy.setGraphic(graphic);
        copy.setName(name);
        copy.setUnitOfMeasure((Unit<Length>) unit);
        return copy;
    }

    @Override
    public PolygonSymbolizerImpl polygonSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Displacement displacement,
            Expression offset) {
        PolygonSymbolizerImpl polygonSymbolizer = new PolygonSymbolizerImpl();
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
    public RasterSymbolizerImpl rasterSymbolizer(
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
        RasterSymbolizerImpl rasterSymbolizer = new RasterSymbolizerImpl(filterFactory);
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
        return (ExtensionSymbolizer) extension;
    }

    @Override
    public RuleImpl rule(
            String name,
            Description description,
            org.geotools.api.style.GraphicLegend legend,
            double min,
            double max,
            List<org.geotools.api.style.Symbolizer> symbolizers,
            Filter filter) {
        RuleImpl rule = new RuleImpl();
        rule.setName(name);
        rule.setDescription(description);
        rule.setLegend(legend);
        rule.setMinScaleDenominator(min);
        rule.setMaxScaleDenominator(max);
        if (symbolizers != null) {
            for (org.geotools.api.style.Symbolizer symbolizer : symbolizers) {
                rule.symbolizers().add(symbolizer);
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
    public SelectedChannelTypeImpl selectedChannelType(
            Expression channelName,
            org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        SelectedChannelTypeImpl selectedChannelType = new SelectedChannelTypeImpl(filterFactory);
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement(contrastEnhancement);
        return selectedChannelType;
    }

    @Override
    public SelectedChannelTypeImpl selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        SelectedChannelTypeImpl selectedChannelType = new SelectedChannelTypeImpl(filterFactory);
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement(contrastEnhancement);
        return selectedChannelType;
    }

    @Override
    public ShadedReliefImpl shadedRelief(Expression reliefFactor, boolean brightnessOnly) {
        ShadedReliefImpl shadedRelief = new ShadedReliefImpl(filterFactory);
        shadedRelief.setReliefFactor(reliefFactor);
        shadedRelief.setBrightnessOnly(brightnessOnly);
        return shadedRelief;
    }

    @Override
    public StrokeImpl stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        StrokeImpl stroke = new StrokeImpl(filterFactory);
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
    public StrokeImpl stroke(
            GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        StrokeImpl stroke = new StrokeImpl(filterFactory);
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
    public StrokeImpl stroke(
            GraphicStroke stroke,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        StrokeImpl s = new StrokeImpl(filterFactory);
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
    public StyleImpl style(
            String name,
            Description description,
            boolean isDefault,
            List<org.geotools.api.style.FeatureTypeStyle> featureTypeStyles,
            org.geotools.api.style.Symbolizer defaultSymbolizer) {
        StyleImpl style = new StyleImpl();
        style.setName(name);
        style.setDescription((DescriptionImpl) description);
        style.setDefault(isDefault);
        if (featureTypeStyles != null) {
            for (org.geotools.api.style.FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
                style.featureTypeStyles()
                        .add(org.geotools.styling.FeatureTypeStyleImpl.cast(featureTypeStyle));
            }
        }
        style.setDefaultSpecification(defaultSymbolizer);
        return style;
    }

    @Override
    public TextSymbolizerImpl textSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            Expression label,
            org.geotools.api.style.Font font,
            org.geotools.api.style.LabelPlacement placement,
            org.geotools.api.style.Halo halo,
            org.geotools.api.style.Fill fill) {
        TextSymbolizerImpl tSymb = new TextSymbolizerImpl(filterFactory);
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
        return new ContrastEnhancementImpl(filterFactory, gamma, meth);
    }

    @Override
    public ContrastMethod createContrastMethod(org.geotools.api.style.ContrastMethod method) {
        return method;
    }
}
