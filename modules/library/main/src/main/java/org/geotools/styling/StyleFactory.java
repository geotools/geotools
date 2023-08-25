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
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.*;
import org.geotools.api.style.Description;
import org.geotools.api.style.OverlapBehavior;
import org.geotools.api.util.InternationalString;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Factory for creating Styles. All style elements are returned as Interfaces from org.geotools.core
 * as opposed to Implementations from org.geotools.defaultcore.
 *
 * <p>This class implements:
 *
 * <ul>
 *   <li>StyleFactory for SLD 1.0
 *   <li>StyleFactory2 for our own extension to text mark allowing a graphic beyond text
 *   <li>org.geotools.api.style.StyleFactory for SE 1.1
 * </ul>
 *
 * @author iant
 * @version $Id$
 */
public class StyleFactory extends AbstractStyleFactory
        implements StyleFactory2, org.geotools.api.style.StyleFactory {

    private FilterFactory filterFactory;
    private StyleFactoryImpl2 delegate;

    public StyleFactory() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    protected StyleFactory(FilterFactory factory) {
        filterFactory = factory;
        delegate = new StyleFactoryImpl2(filterFactory);
    }

    
    public org.geotools.api.style.Style createStyle() {
        return new Style();
    }

    
    public NamedStyle createNamedStyle() {
        return new NamedStyle();
    }

   
    public org.geotools.api.style.PointSymbolizer createPointSymbolizer() {
        return new PointSymbolizer();
    }

   
    public org.geotools.api.style.PointSymbolizer createPointSymbolizer(org.geotools.api.style.Graphic graphic, String geometryPropertyName) {
        org.geotools.api.style.PointSymbolizer pSymb = new PointSymbolizer();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setGraphic(graphic);

        return pSymb;
    }

   
    public org.geotools.api.style.PolygonSymbolizer createPolygonSymbolizer() {
        return new PolygonSymbolizer();
    }

   
    public org.geotools.api.style.PolygonSymbolizer createPolygonSymbolizer(
            org.geotools.api.style.Stroke stroke, org.geotools.api.style.Fill fill, String geometryPropertyName) {
        org.geotools.api.style.PolygonSymbolizer pSymb = new PolygonSymbolizer();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setStroke(stroke);
        pSymb.setFill(fill);

        return pSymb;
    }

   
    public org.geotools.api.style.LineSymbolizer createLineSymbolizer() {
        return new LineSymbolizer();
    }

    
    public org.geotools.api.style.LineSymbolizer createLineSymbolizer(org.geotools.api.style.Stroke stroke, String geometryPropertyName) {
        org.geotools.api.style.LineSymbolizer lSymb = new LineSymbolizer();
        lSymb.setGeometryPropertyName(geometryPropertyName);
        lSymb.setStroke(stroke);

        return lSymb;
    }

    
    public org.geotools.api.style.TextSymbolizer createTextSymbolizer() {
        return new TextSymbolizer(filterFactory);
    }

    
    public org.geotools.api.style.TextSymbolizer createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName) {
        org.geotools.api.style.TextSymbolizer tSymb = new TextSymbolizer(filterFactory);
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

    
    public TextSymbolizer createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName,
            org.geotools.api.style.Graphic graphic) {
        TextSymbolizer tSymb = new TextSymbolizer(filterFactory);
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

    
    public org.geotools.api.style.Extent createExtent(String name, String value) {
        org.geotools.api.style.Extent extent = new Extent();
        extent.setName(name);
        extent.setValue(value);

        return extent;
    }

    
    public org.geotools.api.style.FeatureTypeConstraint createFeatureTypeConstraint(
            String featureTypeName, Filter filter, org.geotools.api.style.Extent[] extents) {
        org.geotools.api.style.FeatureTypeConstraint constraint = new FeatureTypeConstraint();
        constraint.setFeatureTypeName(featureTypeName);
        constraint.setFilter(filter);
        constraint.setExtents(extents);

        return constraint;
    }

    
    public LayerFeatureConstraints createLayerFeatureConstraints(
            org.geotools.api.style.FeatureTypeConstraint[] featureTypeConstraints) {
        LayerFeatureConstraints constraints = new LayerFeatureConstraintsImpl();
        constraints.setFeatureTypeConstraints(featureTypeConstraints);

        return constraints;
    }

    
    public org.geotools.api.style.FeatureTypeStyle createFeatureTypeStyle() {
        return new FeatureTypeStyle();
    }

    
    public org.geotools.api.style.FeatureTypeStyle createFeatureTypeStyle(org.geotools.api.style.Rule[] rules) {
        return new FeatureTypeStyle(rules);
    }

    
    public org.geotools.api.style.Rule createRule() {
        return new Rule();
    }

    public org.geotools.api.style.Rule createRule(
            org.geotools.styling.Symbolizer[] symbolizers,
            Description desc,
            org.geotools.api.style.GraphicLegend legend,
            String name,
            Filter filter,
            boolean isElseFilter,
            double maxScale,
            double minScale) {

        org.geotools.api.style.Rule r =
                new Rule(
                        symbolizers, desc, legend, name, filter, isElseFilter, maxScale, minScale);

        return r;
    }

    
    public org.geotools.api.style.ImageOutline createImageOutline(Symbolizer symbolizer) {
        org.geotools.api.style.ImageOutline outline = new ImageOutline();
        outline.setSymbolizer(symbolizer);

        return outline;
    }

    /**
     * A method to make a simple stroke of a provided color and width.
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the stroke object
     * @see org.geotools.stroke
     */
    
    public org.geotools.api.style.Stroke createStroke(Expression color, Expression width) {
        return createStroke(color, width, filterFactory.literal(1.0));
    }

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
     * @see org.geotools.stroke
     */
    
    public org.geotools.api.style.Stroke createStroke(Expression color, Expression width, Expression opacity) {
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
     * @see org.geotools.stroke
     */
    
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
        org.geotools.api.style.Stroke stroke = new Stroke(filterFactory);

        if (color == null) {
            // use default
            color = ConstantStroke.DEFAULT.getColor();
        }
        stroke.setColor(color);

        if (width == null) {
            // use default
            width = ConstantStroke.DEFAULT.getWidth();
        }
        stroke.setWidth(width);

        if (opacity == null) {
            opacity = ConstantStroke.DEFAULT.getOpacity();
        }
        stroke.setOpacity(opacity);

        if (lineJoin == null) {
            lineJoin = ConstantStroke.DEFAULT.getLineJoin();
        }
        stroke.setLineJoin(lineJoin);

        if (lineCap == null) {
            lineCap = ConstantStroke.DEFAULT.getLineCap();
        }

        stroke.setLineCap(lineCap);
        stroke.setDashArray(dashArray);
        stroke.setDashOffset(dashOffset);
        stroke.setGraphicFill(graphicFill);
        stroke.setGraphicStroke(graphicStroke);

        return stroke;
    }

    
    public org.geotools.api.style.Fill createFill(
            Expression color, Expression backgroundColor, Expression opacity, org.geotools.api.style.Graphic graphicFill) {
        org.geotools.api.style.Fill fill = new Fill(filterFactory);

        if (color == null) {
            color = org.geotools.api.style.Fill.DEFAULT.getColor();
        }
        fill.setColor(color);

        if (opacity == null) {
            opacity = org.geotools.api.style.Fill.DEFAULT.getOpacity();
        }

        // would be nice to check if this was within bounds but we have to wait until use since it
        // may depend on an attribute
        fill.setOpacity(opacity);
        fill.setGraphicFill(graphicFill);

        return fill;
    }

    
    public org.geotools.api.style.Fill createFill(Expression color, Expression opacity) {
        return createFill(color, null, opacity, null);
    }

    
    public org.geotools.api.style.Fill createFill(Expression color) {
        return createFill(color, null, filterFactory.literal(1.0), null);
    }

    
    public org.geotools.api.style.Mark createMark(
            Expression wellKnownName,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            Expression size,
            Expression rotation) {
        org.geotools.api.style.Mark mark = new Mark(filterFactory, null);

        if (wellKnownName == null) {
            throw new IllegalArgumentException("WellKnownName can not be null in mark");
        }

        mark.setWellKnownName(wellKnownName);
        mark.setStroke(stroke);
        mark.setFill(fill);

        return mark;
    }

    
    public org.geotools.api.style.Mark getSquareMark() {
        org.geotools.api.style.Mark mark =
                createMark(
                        filterFactory.literal("Square"),
                        getDefaultStroke(),
                        getDefaultFill(),
                        filterFactory.literal(6),
                        filterFactory.literal(0));

        return mark;
    }

    
    public org.geotools.api.style.Mark getCircleMark() {
        org.geotools.api.style.Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Circle"));

        return mark;
    }

    
    public org.geotools.api.style.Mark getCrossMark() {
        org.geotools.api.style.Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Cross"));

        return mark;
    }

    
    public org.geotools.api.style.Mark getXMark() {
        org.geotools.api.style.Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("X"));

        return mark;
    }

    
    public org.geotools.api.style.Mark getTriangleMark() {
        org.geotools.api.style.Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Triangle"));

        return mark;
    }

    
    public org.geotools.api.style.Mark getStarMark() {
        org.geotools.api.style.Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Star"));

        return mark;
    }

    
    public org.geotools.api.style.Mark createMark() {
        org.geotools.api.style.Mark mark = new Mark(filterFactory, null);

        return mark;
    }

    
    public org.geotools.api.style.Graphic createGraphic(
            org.geotools.api.style.ExternalGraphic[] externalGraphics,
            org.geotools.api.style.Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation) {
        org.geotools.api.style.Graphic graphic = new Graphic(filterFactory);

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

    
    public org.geotools.api.style.ExternalGraphic createExternalGraphic(String uri, String format) {
        org.geotools.api.style.ExternalGraphic extg = new ExternalGraphic();
        extg.setURI(uri);
        extg.setFormat(format);

        return extg;
    }

    
    public org.geotools.api.style.ExternalGraphic createExternalGraphic(Icon inlineContent, String format) {
        ExternalGraphic extg = new ExternalGraphic();
        extg.setInlineContent(inlineContent);
        extg.setFormat(format);
        return extg;
    }

    
    public org.geotools.api.style.ExternalGraphic createExternalGraphic(java.net.URL url, String format) {
        org.geotools.api.style.ExternalGraphic extg = new ExternalGraphic();
        extg.setLocation(url);
        extg.setFormat(format);

        return extg;
    }

    
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
    
    public org.geotools.api.style.LinePlacement createLinePlacement(Expression offset) {
        org.geotools.api.style.LinePlacement linep = new LinePlacement(filterFactory);
        linep.setPerpendicularOffset(offset);

        return linep;
    }

    //    public PointPlacement createPointPlacement(){
    //        return new PointPlacementImpl();
    //    }
    
    public org.geotools.api.style.PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, org.geotools.api.style.Displacement displacement, Expression rotation) {
        org.geotools.api.style.PointPlacement pointp = new PointPlacement(filterFactory);
        pointp.setAnchorPoint(anchorPoint);
        pointp.setDisplacement(displacement);
        pointp.setRotation(rotation);

        return pointp;
    }

    
    public AnchorPoint createAnchorPoint(Expression x, Expression y) {
        AnchorPoint anchorPoint = new AnchorPoint(filterFactory);
        anchorPoint.setAnchorPointX(x);
        anchorPoint.setAnchorPointY(y);

        return anchorPoint;
    }

    
    public org.geotools.api.style.Displacement createDisplacement(Expression x, Expression y) {
        org.geotools.api.style.Displacement displacement = new Displacement(filterFactory);
        displacement.setDisplacementX(x);
        displacement.setDisplacementY(y);

        return displacement;
    }

    
    public org.geotools.api.style.Halo createHalo(org.geotools.api.style.Fill fill, Expression radius) {
        Halo halo = new Halo(filterFactory);
        halo.setFill(fill);
        halo.setRadius(radius);

        return halo;
    }

    
    public org.geotools.api.style.Fill getDefaultFill() {
        org.geotools.api.style.Fill fill = new Fill(filterFactory);

        try {
            fill.setColor(filterFactory.literal("#808080"));
            fill.setOpacity(filterFactory.literal(Double.valueOf(1.0)));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            throw new RuntimeException("Error creating fill", ife);
        }

        return fill;
    }

    
    public org.geotools.api.style.LineSymbolizer getDefaultLineSymbolizer() {
        return createLineSymbolizer(getDefaultStroke(), null);
    }

    
    public org.geotools.api.style.Mark getDefaultMark() {
        return getSquareMark();
    }

    
    public org.geotools.api.style.PointSymbolizer getDefaultPointSymbolizer() {
        return createPointSymbolizer(createDefaultGraphic(), null);
    }

    
    public org.geotools.api.style.PolygonSymbolizer getDefaultPolygonSymbolizer() {
        return createPolygonSymbolizer(getDefaultStroke(), getDefaultFill(), null);
    }

    
    public org.geotools.api.style.Stroke getDefaultStroke() {
        try {
            org.geotools.api.style.Stroke stroke =
                    createStroke(
                            filterFactory.literal("#000000"),
                            filterFactory.literal(Integer.valueOf(1)));

            stroke.setDashOffset(filterFactory.literal(Integer.valueOf(0)));
            stroke.setDashArray(ConstantStroke.DEFAULT.getDashArray());
            stroke.setLineCap(filterFactory.literal("butt"));
            stroke.setLineJoin(filterFactory.literal("miter"));
            stroke.setOpacity(filterFactory.literal(Integer.valueOf(1)));

            return stroke;
        } catch (org.geotools.filter.IllegalFilterException ife) {
            // we should never be in here
            throw new RuntimeException("Error creating stroke", ife);
        }
    }

    
    public org.geotools.api.style.Style getDefaultStyle() {
        org.geotools.api.style.Style style = createStyle();

        return style;
    }

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and
     * defaultPointPlacement, Sets the geometry attribute name to be geometry:text. No Halo is set.
     * <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    
    public org.geotools.api.style.TextSymbolizer getDefaultTextSymbolizer() {
        return createTextSymbolizer(
                getDefaultFill(),
                new org.geotools.api.style.Font[] {getDefaultFont()},
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
    
    public org.geotools.api.style.Font getDefaultFont() {
        return Font.createDefault(filterFactory);
    }

    
    public org.geotools.api.style.Graphic createDefaultGraphic() {
        org.geotools.api.style.Graphic graphic = new Graphic(filterFactory);
        graphic.graphicalSymbols()
                .add(createMark()); // a default graphic is assumed to have a single Mark
        graphic.setSize(Expression.NIL);
        graphic.setOpacity(filterFactory.literal(1.0));
        graphic.setRotation(filterFactory.literal(0.0));

        return graphic;
    }

    
    public org.geotools.api.style.Graphic getDefaultGraphic() {
        return createDefaultGraphic();
    }

    /**
     * returns a default PointPlacement with a 0,0 anchorPoint and a displacement of 0,0 and a
     * rotation of 0
     *
     * @return a default PointPlacement.
     */
    
    public org.geotools.api.style.PointPlacement getDefaultPointPlacement() {
        return this.createPointPlacement(
                PointPlacement.DEFAULT_ANCHOR_POINT,
                this.createDisplacement(filterFactory.literal(0), filterFactory.literal(0)),
                filterFactory.literal(0));
    }

    
    public org.geotools.api.style.RasterSymbolizer createRasterSymbolizer() {
        return new RasterSymbolizer(filterFactory);
    }

    
    public org.geotools.api.style.RasterSymbolizer createRasterSymbolizer(
            String geometryPropertyName,
            Expression opacity,
            ChannelSelection channel,
            Expression overlap,
            ColorMap colorMap,
            org.geotools.api.style.ContrastEnhancement cenhancement,
            org.geotools.api.style.ShadedRelief relief,
            Symbolizer outline) {
        org.geotools.api.style.RasterSymbolizer rastersym = new RasterSymbolizer(filterFactory);

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

    
    public org.geotools.api.style.RasterSymbolizer getDefaultRasterSymbolizer() {
        return createRasterSymbolizer(
                null, filterFactory.literal(1.0), null, null, null, null, null, null);
    }

    
    public ChannelSelection createChannelSelection(org.geotools.api.style.SelectedChannelType[] channels) {
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

    
    public ColorMap createColorMap() {
        return new ColorMapImpl();
    }

    
    public org.geotools.api.style.ColorMapEntry createColorMapEntry() {
        return new ColorMapEntry();
    }

    
    public org.geotools.api.style.ContrastEnhancement createContrastEnhancement() {
        return new ContrastEnhancement(filterFactory);
    }

    
    public org.geotools.api.style.ContrastEnhancement createContrastEnhancement(Expression gammaValue) {
        org.geotools.api.style.ContrastEnhancement ce = new ContrastEnhancement();
        ce.setGammaValue(gammaValue);

        return ce;
    }

    
    public org.geotools.api.style.SelectedChannelType createSelectedChannelType(
            Expression name, org.geotools.api.style.ContrastEnhancement enhancement) {
        org.geotools.api.style.SelectedChannelType sct = new SelectedChannelType(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(enhancement);

        return sct;
    }

    
    public org.geotools.api.style.SelectedChannelType createSelectedChannelType(
            String name, org.geotools.api.style.ContrastEnhancement enhancement) {
        Expression nameExp = filterFactory.literal(name);
        return createSelectedChannelType(nameExp, enhancement);
    }

    
    public org.geotools.api.style.SelectedChannelType createSelectedChannelType(Expression name, Expression gammaValue) {
        org.geotools.api.style.SelectedChannelType sct = new SelectedChannelType(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(createContrastEnhancement(gammaValue));

        return sct;
    }

    
    public StyledLayerDescriptor createStyledLayerDescriptor() {
        return new StyledLayerDescriptorImpl();
    }

    
    public org.geotools.api.style.UserLayer createUserLayer() {
        return new UserLayer();
    }

    
    public org.geotools.api.style.NamedLayer createNamedLayer() {
        return new NamedLayer();
    }

    
    public org.geotools.api.style.RemoteOWS createRemoteOWS(String service, String onlineResource) {
        RemoteOWS remoteOWS = new RemoteOWS();
        remoteOWS.setService(service);
        remoteOWS.setOnlineResource(onlineResource);

        return remoteOWS;
    }

    
    public org.geotools.api.style.ShadedRelief createShadedRelief(Expression reliefFactor) {
        org.geotools.api.style.ShadedRelief relief = new ShadedRelief(filterFactory);
        relief.setReliefFactor(reliefFactor);

        return relief;
    }
    //
    // Start of GeoAPI StyleFacstory implementation
    //

    
    public AnchorPoint anchorPoint(Expression x, Expression y) {
        return delegate.anchorPoint(x, y);
    }

    
    public ChannelSelection channelSelection(org.geotools.api.style.SelectedChannelType gray) {
        return delegate.channelSelection(gray);
    }

    
    public ChannelSelection channelSelection(
            org.geotools.api.style.SelectedChannelType red,
            org.geotools.api.style.SelectedChannelType green,
            org.geotools.api.style.SelectedChannelType blue) {
        return delegate.channelSelection(red, green, blue);
    }

    
    public ColorMap colorMap(Expression propertyName, Expression... mapping) {
        return delegate.colorMap(propertyName, mapping);
    }

    
    public ColorReplacement colorReplacement(Expression propertyName, Expression... mapping) {
        return delegate.colorReplacement(propertyName, mapping);
    }

    
    public org.geotools.api.style.ContrastEnhancement contrastEnhancement(Expression gamma, ContrastMethod method) {
        return delegate.contrastEnhancement(gamma, method);
    }

    
    public org.geotools.styling.Description description(
            InternationalString title, InternationalString description) {
        return delegate.description(title, description);
    }

    
    public org.geotools.api.style.Displacement displacement(Expression dx, Expression dy) {
        return delegate.displacement(dx, dy);
    }

    
    public org.geotools.api.style.ExternalGraphic externalGraphic(Icon inline, Collection<org.geotools.api.style.ColorReplacement> replacements) {
        return delegate.externalGraphic(inline, replacements);
    }

    
    public org.geotools.api.style.ExternalGraphic externalGraphic(
            OnLineResource resource, String format, Collection<org.geotools.api.style.ColorReplacement> replacements) {
        return delegate.externalGraphic(resource, format, replacements);
    }

    
    public ExternalMark externalMark(Icon inline) {
        return delegate.externalMark(inline);
    }

    
    public ExternalMark externalMark(OnLineResource resource, String format, int markIndex) {
        return delegate.externalMark(resource, format, markIndex);
    }

    
    public org.geotools.api.style.FeatureTypeStyle featureTypeStyle(
            String name,
            Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<SemanticType> types,
            List<org.geotools.api.style.Rule> rules) {
        return delegate.featureTypeStyle(
                name, description, definedFor, featureTypeNames, types, rules);
    }

    
    public org.geotools.api.style.Fill fill(GraphicFill fill, Expression color, Expression opacity) {
        return delegate.fill(fill, color, opacity);
    }

    
    public org.geotools.api.style.Font font(
            List<Expression> family, Expression style, Expression weight, Expression size) {
        return delegate.font(family, style, weight, size);
    }

    
    public org.geotools.api.style.Graphic graphic(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp) {
        return delegate.graphic(symbols, opacity, size, rotation, anchor, disp);
    }

    
    public GraphicFill graphicFill(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {
        return delegate.graphicFill(symbols, opacity, size, rotation, anchorPoint, displacement);
    }

    
    public GraphicLegend graphicLegend(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement) {
        return delegate.graphicLegend(symbols, opacity, size, rotation, anchorPoint, displacement);
    }

    
    public GraphicStroke graphicStroke(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression initialGap,
            Expression gap) {
        return delegate.graphicStroke(
                symbols, opacity, size, rotation, anchorPoint, displacement, initialGap, gap);
    }

    
    public org.geotools.api.style.Halo halo(org.geotools.api.style.Fill fill, Expression radius) {
        return delegate.halo(fill, radius);
    }

    
    public org.geotools.api.style.LinePlacement linePlacement(
            Expression offset,
            Expression initialGap,
            Expression gap,
            boolean repeated,
            boolean aligned,
            boolean generalizedLine) {
        return delegate.linePlacement(offset, initialGap, gap, repeated, aligned, generalizedLine);
    }

    
    public org.geotools.api.style.LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset) {
        return delegate.lineSymbolizer(name, geometry, description, unit, stroke, offset);
    }

    
    public org.geotools.api.style.Mark mark(
            Expression wellKnownName,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        return delegate.mark(wellKnownName, fill, stroke);
    }

    
    public Mark mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke) {
        return delegate.mark(externalMark, fill, stroke);
    }

    
    public org.geotools.api.style.PointPlacement pointPlacement(
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement displacement,
            Expression rotation) {
        return delegate.pointPlacement(anchor, displacement, rotation);
    }

    
    public org.geotools.api.style.PointSymbolizer pointSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Graphic graphic) {
        return delegate.pointSymbolizer(name, geometry, description, unit, graphic);
    }

    
    public org.geotools.api.style.PolygonSymbolizer polygonSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Displacement displacement,
            Expression offset) {
        return delegate.polygonSymbolizer(
                name, geometry, description, unit, stroke, fill, displacement, offset);
    }

    
    public org.geotools.api.style.RasterSymbolizer rasterSymbolizer(
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

    
    public ExtensionSymbolizer extensionSymbolizer(
            String name,
            String propertyName,
            Description description,
            Unit<?> unit,
            String extensionName,
            Map<String, Expression> parameters) {
        return delegate.extensionSymbolizer(
                name, propertyName, description, unit, extensionName, parameters);
    }

    
    public org.geotools.api.style.Rule rule(
            String name,
            Description description,
            org.geotools.api.style.GraphicLegend legend,
            double min,
            double max,
            List<org.geotools.api.style.Symbolizer> symbolizers,
            Filter filter) {
        return delegate.rule(name, description, legend, min, max, symbolizers, filter);
    }

    
    public org.geotools.api.style.SelectedChannelType selectedChannelType(
            Expression channelName,
            org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        return delegate.selectedChannelType(channelName, contrastEnhancement);
    }

    
    public org.geotools.api.style.SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement) {
        return delegate.selectedChannelType(channelName, contrastEnhancement);
    }

    
    public org.geotools.api.style.ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly) {
        return delegate.shadedRelief(reliefFactor, brightnessOnly);
    }

    
    public org.geotools.api.style.Stroke stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset) {
        return delegate.stroke(color, opacity, width, join, cap, dashes, offset);
    }

    
    public org.geotools.api.style.Stroke stroke(
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

    
    public org.geotools.api.style.Stroke stroke(
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

    
    public org.geotools.api.style.Style style(
            String name,
            Description description,
            boolean isDefault,
            List<org.geotools.api.style.FeatureTypeStyle> featureTypeStyles,
            org.geotools.api.style.Symbolizer defaultSymbolizer) {
        return delegate.style(name, description, isDefault, featureTypeStyles, defaultSymbolizer);
    }

    
    public org.geotools.api.style.TextSymbolizer textSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            Expression label,
            org.geotools.api.style.Font font,
            org.geotools.api.style.LabelPlacement placement,
            org.geotools.api.style.Halo halo,
            org.geotools.api.style.Fill fill) {
        return delegate.textSymbolizer(
                name, geometry, description, unit, label, font, placement, halo, fill);
    }

    
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

    
    public ContrastMethod createContrastMethod(ContrastMethod method) {
        return method;
    }
}
