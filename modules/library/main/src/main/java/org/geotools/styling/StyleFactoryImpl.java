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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.measure.Unit;
import javax.swing.Icon;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ColorReplacement;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.Description;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExtensionSymbolizer;
import org.geotools.api.style.Extent;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Font;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicFill;
import org.geotools.api.style.GraphicLegend;
import org.geotools.api.style.GraphicStroke;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Halo;
import org.geotools.api.style.ImageOutline;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.LayerFeatureConstraints;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Mark;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.NamedStyle;
import org.geotools.api.style.OverlapBehaviorEnum;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.RemoteOWS;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.SemanticType;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.style.Symbol;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;
import org.geotools.api.style.UserLayer;
import org.geotools.api.util.InternationalString;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Factory for creating Styles. All style elements are returned as Interfaces from {@code org.geotools.api.style} as
 * opposed to Implementations from org.geotools.styling.
 *
 * <p>Implements {@link StyleFactory} to provide:
 *
 * <ul>
 *   <li>SLD 1.0
 *   <li>SE 1.1
 *   <li>GeoTools extensions (such as a text mark allowing a graphic beyond text)
 * </ul>
 *
 * @author iant
 * @version $Id$
 */
public class StyleFactoryImpl extends AbstractStyleFactory implements StyleFactory {

    private FilterFactory filterFactory;
    private StyleFactoryImpl2 delegate;

    public StyleFactoryImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    protected StyleFactoryImpl(FilterFactory factory) {
        filterFactory = factory;
        delegate = new StyleFactoryImpl2(filterFactory);
    }

    @Override
    public Style createStyle() {
        return new StyleImpl();
    }

    @Override
    public NamedStyle createNamedStyle() {
        return new NamedStyleImpl();
    }

    @Override
    public PointSymbolizer createPointSymbolizer() {
        return new PointSymbolizerImpl();
    }

    @Override
    public PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName) {
        PointSymbolizer pSymb = new PointSymbolizerImpl();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setGraphic(graphic);

        return pSymb;
    }

    @Override
    public PolygonSymbolizer createPolygonSymbolizer() {
        return new PolygonSymbolizerImpl();
    }

    @Override
    public PolygonSymbolizer createPolygonSymbolizer(Stroke stroke, Fill fill, String geometryPropertyName) {
        PolygonSymbolizer pSymb = new PolygonSymbolizerImpl();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setStroke(stroke);
        pSymb.setFill(fill);

        return pSymb;
    }

    @Override
    public LineSymbolizer createLineSymbolizer() {
        return new LineSymbolizerImpl();
    }

    @Override
    public LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName) {
        LineSymbolizer lSymb = new LineSymbolizerImpl();
        lSymb.setGeometryPropertyName(geometryPropertyName);
        lSymb.setStroke(stroke);

        return lSymb;
    }

    @Override
    public TextSymbolizer createTextSymbolizer() {
        return new TextSymbolizerImpl(filterFactory);
    }

    @Override
    public TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName) {
        TextSymbolizer tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setFill(fill);
        if (fonts != null) {
            tSymb.fonts().addAll(Arrays.asList(fonts));
        }
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(labelPlacement);

        return tSymb;
    }

    @Override
    public TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName,
            Graphic graphic) {
        TextSymbolizer tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setFill(fill);
        if (fonts != null) {
            tSymb.fonts().addAll(Arrays.asList(fonts));
        }
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(labelPlacement);
        tSymb.setGraphic(graphic);

        return tSymb;
    }

    @Override
    public Extent createExtent(String name, String value) {
        Extent extent = new ExtentImpl();
        extent.setName(name);
        extent.setValue(value);

        return extent;
    }

    @Override
    public FeatureTypeConstraint createFeatureTypeConstraint(String featureTypeName, Filter filter, Extent... extents) {
        FeatureTypeConstraint constraint = new FeatureTypeConstraintImpl();
        constraint.setFeatureTypeName(featureTypeName);
        constraint.setFilter(filter);
        constraint.setExtents(extents);

        return constraint;
    }

    @Override
    public LayerFeatureConstraints createLayerFeatureConstraints(FeatureTypeConstraint... featureTypeConstraints) {
        LayerFeatureConstraints constraints = new LayerFeatureConstraintsImpl();
        constraints.setFeatureTypeConstraints(featureTypeConstraints);

        return constraints;
    }

    @Override
    public FeatureTypeStyle createFeatureTypeStyle() {
        return new FeatureTypeStyleImpl();
    }

    @Override
    public FeatureTypeStyle createFeatureTypeStyle(Rule... rules) {
        return new FeatureTypeStyleImpl(rules);
    }

    @Override
    public Rule createRule() {
        return new RuleImpl();
    }

    public Rule createRule(
            Symbolizer[] symbolizers,
            Description desc,
            Graphic legend,
            String name,
            Filter filter,
            boolean isElseFilter,
            double maxScale,
            double minScale) {

        Rule r = new RuleImpl(symbolizers, desc, legend, name, filter, isElseFilter, maxScale, minScale);

        return r;
    }

    @Override
    public ImageOutline createImageOutline(Symbolizer symbolizer) {
        ImageOutline outline = new ImageOutlineImpl();
        outline.setSymbolizer(symbolizer);

        return outline;
    }

    /**
     * A method to make a simple stroke of a provided color and width.
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the stroke object
     */
    @Override
    public Stroke createStroke(Expression color, Expression width) {
        return createStroke(color, width, filterFactory.literal(1.0));
    }

    /**
     * A convenience method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
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
        Stroke stroke = new StrokeImpl(filterFactory);

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
    public Fill createFill(Expression color, Expression backgroundColor, Expression opacity, Graphic graphicFill) {
        Fill fill = new FillImpl(filterFactory);

        if (color == null) {
            color = FillImpl.DEFAULT.getColor();
        }
        fill.setColor(color);

        if (opacity == null) {
            opacity = FillImpl.DEFAULT.getOpacity();
        }

        // would be nice to check if this was within bounds, but we have to wait until use since it
        // may depend on an attribute
        fill.setOpacity(opacity);
        fill.setGraphicFill(graphicFill);

        return fill;
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
    public Mark createMark(Expression wellKnownName, Stroke stroke, Fill fill, Expression size, Expression rotation) {
        Mark mark = new MarkImpl(filterFactory, null);

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
        Mark mark = createMark(
                filterFactory.literal("Square"),
                getDefaultStroke(),
                getDefaultFill(),
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
        Mark mark = new MarkImpl(filterFactory, null);

        return mark;
    }

    @Override
    public Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        Graphic graphic = new GraphicImpl(filterFactory);

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
            opacity = GraphicImpl.DEFAULT.getOpacity();
        }
        graphic.setOpacity(opacity);

        if (size == null) {
            size = GraphicImpl.DEFAULT.getSize();
        }
        graphic.setSize(size);

        if (rotation == null) {
            rotation = GraphicImpl.DEFAULT.getRotation();
        }

        graphic.setRotation(rotation);

        return graphic;
    }

    @Override
    public ExternalGraphic createExternalGraphic(String uri, String format) {
        ExternalGraphic extg = new ExternalGraphicImpl();
        extg.setURI(uri);
        extg.setFormat(format);

        return extg;
    }

    @Override
    public ExternalGraphic createExternalGraphic(Icon inlineContent, String format) {
        ExternalGraphicImpl extg = new ExternalGraphicImpl();
        extg.setInlineContent(inlineContent);
        extg.setFormat(format);
        return extg;
    }

    @Override
    public ExternalGraphic createExternalGraphic(java.net.URL url, String format) {
        ExternalGraphic extg = new ExternalGraphicImpl();
        extg.setLocation(url);
        extg.setFormat(format);

        return extg;
    }

    @Override
    public Font createFont(Expression fontFamily, Expression fontStyle, Expression fontWeight, Expression fontSize) {
        Font font = new FontImpl();

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
        LinePlacement linep = new LinePlacementImpl(filterFactory);
        linep.setPerpendicularOffset(offset);

        return linep;
    }

    //    public PointPlacement createPointPlacement(){
    //        return new PointPlacementImpl();
    //    }
    @Override
    public PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, Displacement displacement, Expression rotation) {
        PointPlacement pointp = new PointPlacementImpl(filterFactory);
        pointp.setAnchorPoint(anchorPoint);
        pointp.setDisplacement(displacement);
        pointp.setRotation(rotation);

        return pointp;
    }

    @Override
    public AnchorPoint createAnchorPoint(Expression x, Expression y) {
        AnchorPoint anchorPoint = new AnchorPointImpl(filterFactory);
        anchorPoint.setAnchorPointX(x);
        anchorPoint.setAnchorPointY(y);

        return anchorPoint;
    }

    @Override
    public Displacement createDisplacement(Expression x, Expression y) {
        Displacement displacement = new DisplacementImpl(filterFactory);
        displacement.setDisplacementX(x);
        displacement.setDisplacementY(y);

        return displacement;
    }

    @Override
    public Halo createHalo(Fill fill, Expression radius) {
        Halo halo = new HaloImpl(filterFactory);
        halo.setFill(fill);
        halo.setRadius(radius);

        return halo;
    }

    @Override
    public Fill getDefaultFill() {
        Fill fill = new FillImpl(filterFactory);

        try {
            fill.setColor(filterFactory.literal("#808080"));
            fill.setOpacity(filterFactory.literal(Double.valueOf(1.0)));
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
            Stroke stroke = createStroke(filterFactory.literal("#000000"), filterFactory.literal(Integer.valueOf(1)));

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
    public Style getDefaultStyle() {
        Style style = createStyle();

        return style;
    }

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and defaultPointPlacement, Sets the
     * geometry attribute name to be geometry:text. No Halo is set. <b>The label is not set</b>
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
     * Creates a defaultFont which is valid on all machines. The font is of size 10, Style and Weight normal and uses a
     * serif font.
     *
     * @return the default Font
     */
    @Override
    public Font getDefaultFont() {
        return FontImpl.createDefault(filterFactory);
    }

    @Override
    public Graphic createDefaultGraphic() {
        Graphic graphic = new GraphicImpl(filterFactory);
        graphic.graphicalSymbols().add(createMark()); // a default graphic is assumed to have a single Mark
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
     * returns a default PointPlacement with a 0,0 anchorPoint and a displacement of 0,0 and a rotation of 0
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
    public RasterSymbolizer createRasterSymbolizer() {
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
        RasterSymbolizer rastersym = new RasterSymbolizerImpl(filterFactory);

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
        return createRasterSymbolizer(null, filterFactory.literal(1.0), null, null, null, null, null, null);
    }

    @Override
    public ChannelSelection createChannelSelection(SelectedChannelType... channels) {
        ChannelSelection channelSel = new ChannelSelectionImpl();

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
        return new ColorMapImpl();
    }

    @Override
    public ColorMapEntry createColorMapEntry() {
        return new ColorMapEntryImpl();
    }

    @Override
    public ContrastEnhancement createContrastEnhancement() {
        return new ContrastEnhancementImpl(filterFactory);
    }

    @Override
    public ContrastEnhancement createContrastEnhancement(Expression gammaValue) {
        ContrastEnhancement ce = new ContrastEnhancementImpl();
        ce.setGammaValue(gammaValue);

        return ce;
    }

    @Override
    public SelectedChannelType createSelectedChannelType(Expression name, ContrastEnhancement enhancement) {
        SelectedChannelType sct = new SelectedChannelTypeImpl(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(enhancement);

        return sct;
    }

    @Override
    public SelectedChannelType createSelectedChannelType(String name, ContrastEnhancement enhancement) {
        Expression nameExp = filterFactory.literal(name);
        return createSelectedChannelType(nameExp, enhancement);
    }

    @Override
    public SelectedChannelType createSelectedChannelType(Expression name, Expression gammaValue) {
        SelectedChannelType sct = new SelectedChannelTypeImpl(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(createContrastEnhancement(gammaValue));

        return sct;
    }

    @Override
    public StyledLayerDescriptor createStyledLayerDescriptor() {
        return new StyledLayerDescriptorImpl();
    }

    @Override
    public UserLayer createUserLayer() {
        return new UserLayerImpl();
    }

    @Override
    public NamedLayer createNamedLayer() {
        return new NamedLayerImpl();
    }

    @Override
    public RemoteOWS createRemoteOWS(String service, String onlineResource) {
        RemoteOWSImpl remoteOWS = new RemoteOWSImpl();
        remoteOWS.setService(service);
        remoteOWS.setOnlineResource(onlineResource);

        return remoteOWS;
    }

    @Override
    public ShadedRelief createShadedRelief(Expression reliefFactor) {
        ShadedRelief relief = new ShadedReliefImpl(filterFactory);
        relief.setReliefFactor(reliefFactor);

        return relief;
    }
    //
    // Start of GeoAPI StyleFacstory implementation
    //

    @Override
    public AnchorPoint anchorPoint(Expression x, Expression y) {
        return delegate.anchorPoint(x, y);
    }

    @Override
    public ChannelSelection channelSelection(org.geotools.api.style.SelectedChannelType gray) {
        return delegate.channelSelection(gray);
    }

    @Override
    public ChannelSelection channelSelection(
            org.geotools.api.style.SelectedChannelType red,
            org.geotools.api.style.SelectedChannelType green,
            org.geotools.api.style.SelectedChannelType blue) {
        return delegate.channelSelection(red, green, blue);
    }

    @Override
    public ColorMap colorMap(Expression propertyName, Expression... mapping) {
        return delegate.colorMap(propertyName, mapping);
    }

    @Override
    public ColorReplacementImpl colorReplacement(Expression propertyName, Expression... mapping) {
        return delegate.colorReplacement(propertyName, mapping);
    }

    @Override
    public ContrastEnhancement contrastEnhancement(Expression gamma, ContrastMethod method) {
        return delegate.contrastEnhancement(gamma, method);
    }

    @Override
    public Description description(InternationalString title, InternationalString description) {
        return delegate.description(title, description);
    }

    @Override
    public Displacement displacement(Expression dx, Expression dy) {
        return delegate.displacement(dx, dy);
    }

    @Override
    public ExternalGraphic externalGraphic(Icon inline, Collection<ColorReplacement> replacements) {
        return delegate.externalGraphic(inline, replacements);
    }

    @Override
    public ExternalGraphic externalGraphic(
            OnLineResource resource, String format, Collection<ColorReplacement> replacements) {
        return delegate.externalGraphic(resource, format, replacements);
    }

    @Override
    public ExternalMarkImpl externalMark(Icon inline) {
        return delegate.externalMark(inline);
    }

    @Override
    public ExternalMarkImpl externalMark(OnLineResource resource, String format, int markIndex) {
        return delegate.externalMark(resource, format, markIndex);
    }

    @Override
    public FeatureTypeStyle featureTypeStyle(
            String name,
            Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<SemanticType> types,
            List<org.geotools.api.style.Rule> rules) {
        return delegate.featureTypeStyle(name, description, definedFor, featureTypeNames, types, rules);
    }

    @Override
    public Fill fill(GraphicFill fill, Expression color, Expression opacity) {
        return delegate.fill(fill, color, opacity);
    }

    @Override
    public Font font(List<Expression> family, Expression style, Expression weight, Expression size) {
        return delegate.font(family, style, weight, size);
    }

    @Override
    public Graphic graphic(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp) {
        return delegate.graphic(symbols, opacity, size, rotation, anchor, disp);
    }

    @Override
    public GraphicFill graphicFill(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            AnchorPoint anchorPoint,
            Displacement displacement) {
        return delegate.graphicFill(symbols, opacity, size, rotation, anchorPoint, displacement);
    }

    @Override
    public GraphicLegend graphicLegend(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {
        return delegate.graphicLegend(symbols, opacity, size, rotation, anchorPoint, displacement);
    }

    @Override
    public GraphicStroke graphicStroke(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            AnchorPoint anchorPoint,
            Displacement displacement,
            Expression initialGap,
            Expression gap) {
        return delegate.graphicStroke(symbols, opacity, size, rotation, anchorPoint, displacement, initialGap, gap);
    }

    @Override
    public Halo halo(org.geotools.api.style.Fill fill, Expression radius) {
        return delegate.halo(fill, radius);
    }

    @Override
    public LinePlacement linePlacement(
            Expression offset,
            Expression initialGap,
            Expression gap,
            boolean repeated,
            boolean aligned,
            boolean generalizedLine) {
        return delegate.linePlacement(offset, initialGap, gap, repeated, aligned, generalizedLine);
    }

    @Override
    public LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset) {
        return delegate.lineSymbolizer(name, geometry, description, unit, stroke, offset);
    }

    @Override
    public Mark mark(Expression wellKnownName, org.geotools.api.style.Fill fill, org.geotools.api.style.Stroke stroke) {
        return delegate.mark(wellKnownName, fill, stroke);
    }

    @Override
    public MarkImpl mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        return delegate.mark(externalMark, fill, stroke);
    }

    @Override
    public PointPlacement pointPlacement(
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        return delegate.pointPlacement(anchor, displacement, rotation);
    }

    @Override
    public PointSymbolizer pointSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Graphic graphic) {
        return delegate.pointSymbolizer(name, geometry, description, unit, graphic);
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
        return delegate.polygonSymbolizer(name, geometry, description, unit, stroke, fill, displacement, offset);
    }

    @Override
    public RasterSymbolizer rasterSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            Expression opacity,
            org.geotools.api.style.ChannelSelection channelSelection,
            OverlapBehaviorEnum overlapsBehaviour,
            org.geotools.api.style.ColorMap colorMap,
            org.geotools.api.style.ContrastEnhancement contrast,
            org.geotools.api.style.ShadedRelief shaded,
            org.geotools.api.style.Symbolizer outline) {
        return delegate.rasterSymbolizer(
                name,
                geometry,
                description,
                unit,
                opacity,
                channelSelection,
                overlapsBehaviour,
                colorMap,
                contrast,
                shaded,
                outline);
    }

    @Override
    public ExtensionSymbolizer extensionSymbolizer(
            String name,
            String propertyName,
            Description description,
            Unit<?> unit,
            String extensionName,
            Map<String, Expression> parameters) {
        return delegate.extensionSymbolizer(name, propertyName, description, unit, extensionName, parameters);
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
        return delegate.rule(name, description, legend, min, max, symbolizers, filter);
    }

    @Override
    public SelectedChannelType selectedChannelType(
            Expression channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        return delegate.selectedChannelType(channelName, contrastEnhancement);
    }

    @Override
    public SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        return delegate.selectedChannelType(channelName, contrastEnhancement);
    }

    @Override
    public ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly) {
        return delegate.shadedRelief(reliefFactor, brightnessOnly);
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
        return delegate.stroke(color, opacity, width, join, cap, dashes, offset);
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
        return delegate.stroke(fill, color, opacity, width, join, cap, dashes, offset);
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
        return delegate.stroke(stroke, color, opacity, width, join, cap, dashes, offset);
    }

    @Override
    public Style style(
            String name,
            Description description,
            boolean isDefault,
            List<org.geotools.api.style.FeatureTypeStyle> featureTypeStyles,
            org.geotools.api.style.Symbolizer defaultSymbolizer) {
        return delegate.style(name, description, isDefault, featureTypeStyles, defaultSymbolizer);
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
        return delegate.textSymbolizer(name, geometry, description, unit, label, font, placement, halo, fill);
    }

    @Override
    public org.geotools.api.style.ContrastEnhancement contrastEnhancement(Expression gamma, String method) {

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
    public ContrastMethod createContrastMethod(ContrastMethod method) {
        return method;
    }
}
