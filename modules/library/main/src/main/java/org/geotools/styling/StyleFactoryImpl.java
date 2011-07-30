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
 *
 * Created on 14 October 2002, 15:50
 */
package org.geotools.styling;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.unit.Unit;
import javax.swing.Icon;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.ColorReplacement;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Description;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicStroke;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.SemanticType;
import org.opengis.util.InternationalString;


/**
 * Factory for creating Styles. All style elements are returned as Interfaces
 * from org.geotools.core as opposed to Implementations from org.geotools.defaultcore.
 * <p>
 * This class implements:
 * <ul>
 * <li>StyleFactory for SLD 1.0
 * <li>StyleFactory2 for our own extension to text mark allowing a graphic beyond text
 * <li>org.opengis.style.StyleFactory for SE 1.1 
 * </ul>
 * 
 * @author iant
 *
 * @source $URL$
 * @version $Id$
 */
public class StyleFactoryImpl extends AbstractStyleFactory
    implements StyleFactory2, org.opengis.style.StyleFactory {
	
    private FilterFactory2 filterFactory;
    private StyleFactoryImpl2 delegate;
    
    public StyleFactoryImpl() {
        this( CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints()));
    }

    protected StyleFactoryImpl(FilterFactory2 factory) {
        filterFactory = factory;
        delegate = new StyleFactoryImpl2( filterFactory );
    }
    
    public Style createStyle() {
        return new StyleImpl();
    }

    public NamedStyle createNamedStyle() {
        return new NamedStyleImpl();
    }

    public PointSymbolizer createPointSymbolizer() {
        return new PointSymbolizerImpl();
    }

    public PointSymbolizer createPointSymbolizer(Graphic graphic,
        String geometryPropertyName) {
        PointSymbolizer pSymb = new PointSymbolizerImpl();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setGraphic(graphic);

        return pSymb;
    }

    public PolygonSymbolizer createPolygonSymbolizer() {
        return new PolygonSymbolizerImpl();
    }

    public PolygonSymbolizer createPolygonSymbolizer(Stroke stroke, Fill fill,
        String geometryPropertyName) {
        PolygonSymbolizer pSymb = new PolygonSymbolizerImpl();
        pSymb.setGeometryPropertyName(geometryPropertyName);
        pSymb.setStroke(stroke);
        pSymb.setFill(fill);

        return pSymb;
    }

    public LineSymbolizer createLineSymbolizer() {
        return new LineSymbolizerImpl();
    }

    public LineSymbolizer createLineSymbolizer(Stroke stroke,
        String geometryPropertyName) {
        LineSymbolizer lSymb = new LineSymbolizerImpl();
        lSymb.setGeometryPropertyName(geometryPropertyName);
        lSymb.setStroke(stroke);

        return lSymb;
    }

    public TextSymbolizer createTextSymbolizer() {
        return new TextSymbolizerImpl(filterFactory);
    }

    public TextSymbolizer createTextSymbolizer(Fill fill, Font[] fonts,
        Halo halo, Expression label, LabelPlacement labelPlacement,
        String geometryPropertyName) {
        TextSymbolizer tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setFill(fill);
        tSymb.setFonts(fonts);
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setPlacement(labelPlacement);

        return tSymb;
    }

    public TextSymbolizer2 createTextSymbolizer(Fill fill, Font[] fonts,
        Halo halo, Expression label, LabelPlacement labelPlacement,
        String geometryPropertyName, Graphic graphic) {
        TextSymbolizer2 tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setFill(fill);
        tSymb.setFonts(fonts);
        tSymb.setGeometryPropertyName(geometryPropertyName);

        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setPlacement(labelPlacement);
        tSymb.setGraphic(graphic);

        return tSymb;
    }
    
    public Extent createExtent(String name, String value) {
        Extent extent = new ExtentImpl();
        extent.setName(name);
        extent.setValue(value);

        return extent;
    }

    public FeatureTypeConstraint createFeatureTypeConstraint(
        String featureTypeName, Filter filter, Extent[] extents) {
        FeatureTypeConstraint constraint = new FeatureTypeConstraintImpl();
        constraint.setFeatureTypeName(featureTypeName);
        constraint.setFilter(filter);
        constraint.setExtents(extents);

        return constraint;
    }
    
    public LayerFeatureConstraints createLayerFeatureConstraints(FeatureTypeConstraint[] featureTypeConstraints) {
    	LayerFeatureConstraints constraints = new LayerFeatureConstraintsImpl();
    	constraints.setFeatureTypeConstraints(featureTypeConstraints);
    	
    	return constraints;
    }

    public FeatureTypeStyle createFeatureTypeStyle() {
        return new FeatureTypeStyleImpl();
    }

    public FeatureTypeStyle createFeatureTypeStyle(Rule[] rules) {
        return new FeatureTypeStyleImpl(rules);
    }
    
    public Rule createRule() {
        return new RuleImpl();
    }
    
    public Rule createRule(org.geotools.styling.Symbolizer[] symbolizers, 
                        Description desc, 
                        Graphic[] legends,
                        String name,
                        Filter filter,
                        boolean isElseFilter,
                        double maxScale,
                        double minScale){
        
        Rule r = new RuleImpl(symbolizers, 
                        desc, 
                        legends,
                        name,
                        filter,
                        isElseFilter,
                        maxScale,
                        minScale);
                
                
        return r;
    }

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
     *
     * @return the stroke object
     *
     * @see org.geotools.stroke
     */
    public Stroke createStroke(Expression color, Expression width) {
        return createStroke(color, width,
            filterFactory.literal(1.0));
    }

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     *
     * @return The stroke
     *
     * @see org.geotools.stroke
     */
    public Stroke createStroke(Expression color, Expression width,
        Expression opacity) {
        return createStroke(color, width, opacity,
            filterFactory.literal("miter"),
            filterFactory.literal("butt"), null,
            filterFactory.literal(0.0), null, null);
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
     *
     * @return The completed stroke.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     *
     * @see org.geotools.stroke
     */
    public Stroke createStroke(Expression color, Expression width,
        Expression opacity, Expression lineJoin, Expression lineCap,
        float[] dashArray, Expression dashOffset, Graphic graphicFill,
        Graphic graphicStroke) {
        Stroke stroke = new StrokeImpl(filterFactory);

        if (color == null) {
        	//use default
        	color = Stroke.DEFAULT.getColor();
        }
        stroke.setColor(color);

        if (width == null) {
        	//use default
        	width = Stroke.DEFAULT.getWidth();
        }
        stroke.setWidth(width);

        if (opacity == null) {
        	opacity = Stroke.DEFAULT.getOpacity();;
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

    public Fill createFill(Expression color, Expression backgroundColor,
        Expression opacity, Graphic graphicFill) {
        Fill fill = new FillImpl(filterFactory);

        if (color == null) {
            color = Fill.DEFAULT.getColor();
        }
        fill.setColor(color);
        if (backgroundColor == null) {
        	backgroundColor = Fill.DEFAULT.getBackgroundColor();
        }
        fill.setBackgroundColor(backgroundColor);

        if (opacity == null) {
        	opacity = Fill.DEFAULT.getOpacity();
        }

        // would be nice to check if this was within bounds but we have to wait until use since it may depend on an attribute
        fill.setOpacity(opacity);
        fill.setGraphicFill(graphicFill);

        return fill;
    }

    public Fill createFill(Expression color, Expression opacity) {
        return createFill(color, null, opacity, null);
    }

    public Fill createFill(Expression color) {
        return createFill(color, null,
            filterFactory.literal(1.0), null);
    }

    public Mark createMark(Expression wellKnownName, Stroke stroke, Fill fill,
        Expression size, Expression rotation) {
        Mark mark = new MarkImpl(filterFactory, null);

        if (wellKnownName == null) {
            throw new IllegalArgumentException(
                "WellKnownName can not be null in mark");
        }

        mark.setWellKnownName(wellKnownName);
        mark.setStroke(stroke);
        mark.setFill(fill);

        if (size == null) {
            throw new IllegalArgumentException("Size can not be null in mark");
        }

        mark.setSize(size);

        if (rotation == null) {
            throw new IllegalArgumentException(
                "Rotation can not be null in mark");
        }

        mark.setRotation(rotation);

        return mark;
    }

    public Mark getSquareMark() {
        Mark mark = createMark(filterFactory.literal("Square"),
                getDefaultStroke(), getDefaultFill(),
                filterFactory.literal(6),
                filterFactory.literal(0));

        return mark;
    }

    public Mark getCircleMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Circle"));

        return mark;
    }

    public Mark getCrossMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Cross"));

        return mark;
    }

    public Mark getXMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("X"));

        return mark;
    }

    public Mark getTriangleMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Triangle"));

        return mark;
    }

    public Mark getStarMark() {
        Mark mark = getDefaultMark();
        mark.setWellKnownName(filterFactory.literal("Star"));

        return mark;
    }

    public Mark createMark() {
        Mark mark = new MarkImpl(filterFactory, null);

        return mark;
    }

    public Graphic createGraphic(ExternalGraphic[] externalGraphics,
        Mark[] marks, Symbol[] symbols, Expression opacity, Expression size,
        Expression rotation) {
        Graphic graphic = new GraphicImpl(filterFactory);
        
        symbols = symbols != null ? symbols : new Symbol[0];
        graphic.setSymbols(symbols);
        
        //externalGraphics = externalGraphics != null ? externalGraphics : new ExternalGraphic[0];
        //graphic.setExternalGraphics(externalGraphics);        
        if( externalGraphics != null ){
            graphic.graphicalSymbols().addAll( Arrays.asList( externalGraphics ) );
        }        
        // marks = marks != null ? marks : new Mark[0];
        // graphic.setMarks(marks);
        if( marks != null ){
            graphic.graphicalSymbols().addAll( Arrays.asList( marks ) );
        }
        if (opacity == null) {
            opacity = Graphic.DEFAULT.getOpacity();
        }
        graphic.setOpacity(opacity);

        if (size == null) {
            size = Graphic.DEFAULT.getSize();
        }
        graphic.setSize(size);

        if (rotation == null) {
            rotation = Graphic.DEFAULT.getRotation();
        }

        graphic.setRotation(rotation);

        return graphic;
    }

    public ExternalGraphic createExternalGraphic(String uri, String format) {
        ExternalGraphic extg = new ExternalGraphicImpl();
        extg.setURI(uri);
        extg.setFormat(format);

        return extg;
    }

    public ExternalGraphic createExternalGraphic(java.net.URL url, String format) {
        ExternalGraphic extg = new ExternalGraphicImpl();
        extg.setLocation(url);
        extg.setFormat(format);

        return extg;
    }

    public Font createFont(Expression fontFamily, Expression fontStyle,
        Expression fontWeight, Expression fontSize) {
        Font font = new FontImpl();

        if (fontFamily == null) {
    		throw new IllegalArgumentException("Null font family specified");	
        }
        font.setFontFamily(fontFamily);

        if (fontSize == null) {
            throw new IllegalArgumentException("Null font size specified");
        }

        font.setFontSize(fontSize);

        if (fontStyle == null) {
            throw new IllegalArgumentException("Null font Style specified");
        }

        font.setFontStyle(fontStyle);

        if (fontWeight == null) {
            throw new IllegalArgumentException("Null font weight specified");
        }

        font.setFontWeight(fontWeight);

        return font;
    }

    //    public LinePlacement createLinePlacement(){
    //        return new LinePlacementImpl();
    //    }
    public LinePlacement createLinePlacement(Expression offset) {
        LinePlacement linep = new LinePlacementImpl(filterFactory);
        linep.setPerpendicularOffset(offset);

        return linep;
    }

    //    public PointPlacement createPointPlacement(){
    //        return new PointPlacementImpl();
    //    }
    public PointPlacement createPointPlacement(AnchorPoint anchorPoint,
        Displacement displacement, Expression rotation) {
        PointPlacement pointp = new PointPlacementImpl(filterFactory);
        pointp.setAnchorPoint(anchorPoint);
        pointp.setDisplacement(displacement);
        pointp.setRotation(rotation);

        return pointp;
    }

    public AnchorPoint createAnchorPoint(Expression x, Expression y) {
        AnchorPoint anchorPoint = new AnchorPointImpl(filterFactory);
        anchorPoint.setAnchorPointX(x);
        anchorPoint.setAnchorPointY(y);

        return anchorPoint;
    }

    public Displacement createDisplacement(Expression x, Expression y) {
        Displacement displacement = new DisplacementImpl(filterFactory);
        displacement.setDisplacementX(x);
        displacement.setDisplacementY(y);

        return displacement;
    }

    public Halo createHalo(Fill fill, Expression radius) {
        Halo halo = new HaloImpl(filterFactory);
        halo.setFill(fill);
        halo.setRadius(radius);

        return halo;
    }

    public Fill getDefaultFill() {
        Fill fill = new FillImpl(filterFactory);

        try {
            fill.setColor(filterFactory.literal("#808080"));
            fill.setOpacity(filterFactory.literal(
                    new Double(1.0)));
            fill.setBackgroundColor(filterFactory.literal("#FFFFFF"));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            throw new RuntimeException("Error creating fill", ife);
        }

        return fill;
    }

    public LineSymbolizer getDefaultLineSymbolizer() {
        return createLineSymbolizer(getDefaultStroke(), null);
    }

    public Mark getDefaultMark() {
        return getSquareMark();
    }

    public PointSymbolizer getDefaultPointSymbolizer() {
        return createPointSymbolizer(createDefaultGraphic(), null);
    }

    public PolygonSymbolizer getDefaultPolygonSymbolizer() {
        return createPolygonSymbolizer(getDefaultStroke(), getDefaultFill(),
            null);
    }

    public Stroke getDefaultStroke() {
        try {
            Stroke stroke = createStroke(filterFactory.literal("#000000"),
                    filterFactory.literal(new Integer(1)));

            stroke.setDashOffset(filterFactory.literal(
                    new Integer(0)));
            stroke.setDashArray(Stroke.DEFAULT.getDashArray());
            stroke.setLineCap(filterFactory.literal("butt"));
            stroke.setLineJoin(filterFactory.literal("miter"));
            stroke.setOpacity(filterFactory.literal(new Integer(1)));

            return stroke;
        } catch (org.geotools.filter.IllegalFilterException ife) {
            //we should never be in here
            throw new RuntimeException("Error creating stroke", ife);
        }
    }

    public Style getDefaultStyle() {
        Style style = createStyle();

        return style;
    }

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont
     * and defaultPointPlacement,  Sets the geometry attribute name to be
     * geometry:text. No Halo is set. <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    public TextSymbolizer getDefaultTextSymbolizer() {
        return createTextSymbolizer(getDefaultFill(),
            new Font[] { getDefaultFont() }, null, null,
            getDefaultPointPlacement(), "geometry:text");
    }

    /**
     * Creates a defaultFont which is valid on all machines. The font is of
     * size 10, Style and Weight normal and uses a serif font.
     *
     * @return the default Font
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Font getDefaultFont() {
        return FontImpl.createDefault( filterFactory );
    }

    public Graphic createDefaultGraphic() {
        Graphic graphic = new GraphicImpl(filterFactory);
        graphic.addMark( createMark() ); // a default graphic is assumed to have a single Mark
        graphic.setSize(Expression.NIL);
        graphic.setOpacity(filterFactory.literal(1.0));
        graphic.setRotation(filterFactory.literal(0.0));

        return graphic;
    }

    public Graphic getDefaultGraphic() {
        return createDefaultGraphic();
    }

    /**
     * returns a default PointPlacement with a 0,0 anchorPoint and a
     * displacement of 0,0 and a rotation of 0
     *
     * @return a default PointPlacement.
     */
    public PointPlacement getDefaultPointPlacement() {
        return this.createPointPlacement(this.createAnchorPoint(
                filterFactory.literal(0),
                filterFactory.literal(0.5)),
            this.createDisplacement(filterFactory.literal(0),
                filterFactory.literal(0)),
            filterFactory.literal(0));
    }

    public RasterSymbolizer createRasterSymbolizer() {
    	return new RasterSymbolizerImpl(filterFactory);
    }
    
    public RasterSymbolizer createRasterSymbolizer(
        String geometryPropertyName, Expression opacity,
        ChannelSelection channel, Expression overlap, ColorMap colorMap,
        ContrastEnhancement cenhancement, ShadedRelief relief,
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

    public RasterSymbolizer getDefaultRasterSymbolizer() {
        return createRasterSymbolizer("geom",
            filterFactory.literal(1.0), null, null, null, null,
            null, null);
    }

    public ChannelSelection createChannelSelection(
        SelectedChannelType[] channels) {
        ChannelSelection channelSel = new ChannelSelectionImpl();

        if ((channels != null) && (channels.length > 0)) {
            channelSel.setSelectedChannels(channels);
        }

        return channelSel;
    }

    public ColorMap createColorMap() {
        return new ColorMapImpl();
    }

    public ColorMapEntry createColorMapEntry() {
        return new ColorMapEntryImpl();
    }

    public ContrastEnhancement createContrastEnhancement() {
        return new ContrastEnhancementImpl(filterFactory);
    }

    public ContrastEnhancement createContrastEnhancement(Expression gammaValue) {
        ContrastEnhancement ce = new ContrastEnhancementImpl();
        ce.setGammaValue(gammaValue);

        return ce;
    }

    public SelectedChannelType createSelectedChannelType(String name,
        ContrastEnhancement enhancement) {
        SelectedChannelType sct = new SelectedChannelTypeImpl(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(enhancement);

        return sct;
    }

    public SelectedChannelType createSelectedChannelType(String name,
        Expression gammaValue) {
        SelectedChannelType sct = new SelectedChannelTypeImpl(filterFactory);
        sct.setChannelName(name);
        sct.setContrastEnhancement(createContrastEnhancement(gammaValue));

        return sct;
    }

    public StyledLayerDescriptor createStyledLayerDescriptor() {
        return new StyledLayerDescriptorImpl();
    }

    public UserLayer createUserLayer() {
        return new UserLayerImpl();
    }

    public NamedLayer createNamedLayer() {
        return new NamedLayerImpl();
    }
    
    public RemoteOWS createRemoteOWS(String service, String onlineResource) {
    	RemoteOWSImpl remoteOWS = new RemoteOWSImpl();
    	remoteOWS.setService(service);
    	remoteOWS.setOnlineResource(onlineResource);
    	
    	return remoteOWS;
    }    
    public ShadedRelief createShadedRelief(Expression reliefFactor) {
    	ShadedRelief relief = new ShadedReliefImpl(filterFactory);
    	relief.setReliefFactor(reliefFactor);
    	
    	return relief;
    }
    //
    // Start of GeoAPI StyleFacstory implementation
    //

    public AnchorPoint anchorPoint(Expression x, Expression y) {
        return delegate.anchorPoint(x, y);
    }

    public ChannelSelection channelSelection(
            org.opengis.style.SelectedChannelType gray) {
        return delegate.channelSelection(gray);
    }
    public ChannelSelection channelSelection(
            org.opengis.style.SelectedChannelType red, org.opengis.style.SelectedChannelType green,
            org.opengis.style.SelectedChannelType blue) {
        return delegate.channelSelection(red, green, blue);
    }

    public ColorMap colorMap(Expression propertyName, Expression... mapping) {
        return delegate.colorMap(propertyName, mapping);
    }

    public ColorReplacementImpl colorReplacement(Expression propertyName, Expression... mapping) {
        return delegate.colorReplacement(propertyName, mapping);
    }

    public ContrastEnhancement contrastEnhancement(Expression gamma,
            ContrastMethod method) {
        return delegate.contrastEnhancement(gamma, method);
    }

    public org.geotools.styling.Description description(InternationalString title, InternationalString description) {
        return delegate.description(title, description);
    }

    public Displacement displacement(Expression dx, Expression dy) {
        return delegate.displacement(dx, dy);
    }

    public ExternalGraphic externalGraphic(Icon inline,
            Collection<ColorReplacement> replacements) {
        return delegate.externalGraphic(inline, replacements);
    }

    public ExternalGraphic externalGraphic(OnLineResource resource,
            String format, Collection<ColorReplacement> replacements) {
        return delegate.externalGraphic(resource, format, replacements);
    }

    public ExternalMarkImpl externalMark(Icon inline) {
        return delegate.externalMark(inline);
    }

    public ExternalMarkImpl externalMark(OnLineResource resource, String format, int markIndex) {
        return delegate.externalMark(resource, format, markIndex);
    }

    public FeatureTypeStyle featureTypeStyle(String name,
            Description description, Id definedFor, Set<Name> featureTypeNames,
            Set<SemanticType> types, List<org.opengis.style.Rule> rules) {
        return delegate.featureTypeStyle(name, description, definedFor, featureTypeNames, types, rules);
    }

    public Fill fill(GraphicFill fill, Expression color, Expression opacity) {
        return delegate.fill(fill, color, opacity);
    }

    public Font font(List<Expression> family, Expression style,
            Expression weight, Expression size) {
        return delegate.font(family, style, weight, size);
    }

    public Graphic graphic(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchor,
            org.opengis.style.Displacement disp) {
        return delegate.graphic(symbols, opacity, size, rotation, anchor, disp);
    }
    public Graphic graphicFill(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement) {
        return delegate.graphicFill(symbols, opacity, size, rotation, anchorPoint, displacement);
    }
    public GraphicLegend graphicLegend(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement) {
        return delegate.graphicLegend(symbols, opacity, size, rotation, anchorPoint, displacement);
    }
    public Graphic graphicStroke(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement, Expression initialGap, Expression gap) {
        return delegate.graphicStroke(symbols, opacity, size, rotation, anchorPoint, displacement, initialGap, gap);
    }
    public Halo halo(org.opengis.style.Fill fill, Expression radius) {        
        return delegate.halo(fill, radius);
    }

    public LinePlacement linePlacement(Expression offset, Expression initialGap,
            Expression gap, boolean repeated, boolean aligned, boolean generalizedLine) {
        return delegate.linePlacement(offset, initialGap, gap, repeated, aligned, generalizedLine);
    }

    public LineSymbolizer lineSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, org.opengis.style.Stroke stroke,
            Expression offset) {
        return delegate.lineSymbolizer(name, geometry, description, unit, stroke, offset);
    }
    public Mark mark(Expression wellKnownName, org.opengis.style.Fill fill,
            org.opengis.style.Stroke stroke) {
        return delegate.mark(wellKnownName, fill, stroke);
    }

    public MarkImpl mark(org.opengis.style.ExternalMark externalMark, org.opengis.style.Fill fill,
            org.opengis.style.Stroke stroke) {
        return delegate.mark(externalMark, fill, stroke);
    }

    public PointPlacement pointPlacement(org.opengis.style.AnchorPoint anchor,
            org.opengis.style.Displacement displacement, Expression rotation) {
        return delegate.pointPlacement(anchor, displacement, rotation);
    }

    public PointSymbolizer pointSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, org.opengis.style.Graphic graphic) {
        return delegate.pointSymbolizer(name, geometry, description, unit, graphic);
    }
    public PolygonSymbolizer polygonSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, org.opengis.style.Stroke stroke,
            org.opengis.style.Fill fill, org.opengis.style.Displacement displacement,
            Expression offset) {
        return delegate.polygonSymbolizer(name, geometry, description, unit, stroke, fill, displacement, offset);
    }
    
    public RasterSymbolizer rasterSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Expression opacity,
            org.opengis.style.ChannelSelection channelSelection, OverlapBehavior overlapsBehaviour,
            org.opengis.style.ColorMap colorMap, org.opengis.style.ContrastEnhancement contrast,
            org.opengis.style.ShadedRelief shaded, org.opengis.style.Symbolizer outline) {
        return delegate.rasterSymbolizer(name, geometry, description, unit, opacity, channelSelection, overlapsBehaviour, colorMap, contrast, shaded, outline);
    }
    
    public ExtensionSymbolizer extensionSymbolizer(String name, String propertyName,
            Description description, Unit<?> unit, String extensionName,
            Map<String, Expression> parameters) {
        return delegate.extensionSymbolizer(name, propertyName, description, unit, extensionName, parameters);
    }    
  
    public Rule rule(String name, Description description, org.opengis.style.GraphicLegend legend,
            double min, double max, List<org.opengis.style.Symbolizer> symbolizers, Filter filter) {
        return delegate.rule(name, description, legend, min, max, symbolizers, filter);
    }
    
    public SelectedChannelType selectedChannelType(String channelName,
            org.opengis.style.ContrastEnhancement contrastEnhancement)
    {
        return delegate.selectedChannelType(channelName, contrastEnhancement);
    }
    
    public ShadedRelief shadedRelief(Expression reliefFactor,
            boolean brightnessOnly) {
        return delegate.shadedRelief(reliefFactor, brightnessOnly);
    }

    public Stroke stroke(Expression color, Expression opacity, Expression width,
            Expression join, Expression cap, float[] dashes, Expression offset) {
        return delegate.stroke(color, opacity, width, join, cap, dashes, offset);
    }
    
    public Stroke stroke(GraphicFill fill, Expression color, Expression opacity,
            Expression width, Expression join, Expression cap, float[] dashes, Expression offset) {
        return delegate.stroke(fill, color, opacity, width, join, cap, dashes, offset);
    }
    
    public Stroke stroke(GraphicStroke stroke, Expression color,
            Expression opacity, Expression width, Expression join, Expression cap, float[] dashes,
            Expression offset) {
        return delegate.stroke(stroke, color, opacity, width, join, cap, dashes, offset);
    }
    
    public Style style(String name, Description description, boolean isDefault,
            List<org.opengis.style.FeatureTypeStyle> featureTypeStyles,
            org.opengis.style.Symbolizer defaultSymbolizer) {
        return delegate.style(name, description, isDefault, featureTypeStyles, defaultSymbolizer);
    }
    
    public TextSymbolizer textSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Expression label, org.opengis.style.Font font,
            org.opengis.style.LabelPlacement placement, org.opengis.style.Halo halo,
            org.opengis.style.Fill fill) {
        return delegate.textSymbolizer(name, geometry, description, unit, label, font, placement, halo, fill);        
    }
}
