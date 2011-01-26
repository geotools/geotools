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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;
import javax.swing.Icon;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.ColorReplacement;
import org.opengis.style.ContrastMethod;
import org.opengis.style.Description;
import org.opengis.style.ExternalMark;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicLegend;
import org.opengis.style.GraphicStroke;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.OverlapBehavior;
import org.opengis.style.SemanticType;
import org.opengis.util.InternationalString;


/**
 * Factory for creating Styles; based on the GeoAPI StyleFactory interface.
 * <p>
 * This factory is simple; it just creates styles with no logic or magic default values.
 * For magic default values please read the SE or SLD specification; or use an appropriate
 * builder.
 * 
 * @author Jody Garnett
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/main/src/main/java/org/geotools/styling/StyleFactoryImpl.java $
 * @version $Id: StyleFactoryImpl2.java 32736 2009-04-04 06:51:02Z jive $
 */
public class StyleFactoryImpl2 implements org.opengis.style.StyleFactory {
    private FilterFactory2 filterFactory;
	    
    public StyleFactoryImpl2() {
        this( CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints()));
    }

    protected StyleFactoryImpl2(FilterFactory2 factory) {
        filterFactory = factory;
    }
    
    public AnchorPoint anchorPoint(Expression x, Expression y) {
        return new AnchorPointImpl( filterFactory, x, y );
    }

    public ChannelSelection channelSelection(
            org.opengis.style.SelectedChannelType gray) {
        ChannelSelectionImpl channelSelection = new ChannelSelectionImpl();
        channelSelection.setGrayChannel( gray );
        return channelSelection;
    }
    public ChannelSelectionImpl channelSelection(
            org.opengis.style.SelectedChannelType red, org.opengis.style.SelectedChannelType green,
            org.opengis.style.SelectedChannelType blue) {
        ChannelSelectionImpl channelSelection = new ChannelSelectionImpl();
        channelSelection.setRGBChannels(red, green,blue);
        return channelSelection;
    }

    public ColorMapImpl colorMap(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        for( int i=0; i<mapping.length;i++){
            arguments[i+1] = mapping[i];
        }
        Function function = filterFactory.function("Categorize", arguments );
        ColorMapImpl colorMap = new ColorMapImpl( function );
        
        return colorMap;
    }

    public ColorReplacementImpl colorReplacement(Expression propertyName, Expression... mapping) {
        Expression[] arguments = new Expression[mapping.length + 2];
        arguments[0] = propertyName;
        for( int i=0; i<mapping.length;i++){
            arguments[i+1] = mapping[i];
        }
        Function function = filterFactory.function("Recode", arguments );
        ColorReplacementImpl colorMap = new ColorReplacementImpl( function );
        
        return colorMap;
    }

    public ContrastEnhancementImpl contrastEnhancement(Expression gamma,
            ContrastMethod method) {
        return new ContrastEnhancementImpl( filterFactory, gamma, method );
    }

    public DescriptionImpl description(InternationalString title, InternationalString description) {
        return new DescriptionImpl( title, description );
    }

    public DisplacementImpl displacement(Expression dx, Expression dy) {
        return new DisplacementImpl( dx, dy );
    }

    public ExternalGraphicImpl externalGraphic(Icon inline,
            Collection<ColorReplacement> replacements) {
        ExternalGraphicImpl externalGraphic = new ExternalGraphicImpl( inline, replacements, null );
        return externalGraphic;
    }

    public ExternalGraphicImpl externalGraphic(OnLineResource resource,
            String format, Collection<ColorReplacement> replacements) {
        ExternalGraphicImpl externalGraphic = new ExternalGraphicImpl( null, replacements, resource);
        externalGraphic.setFormat( format );
        return externalGraphic;
    }

    public ExternalMarkImpl externalMark(Icon inline) {
        return new ExternalMarkImpl( inline );
    }

    public ExternalMarkImpl externalMark(OnLineResource resource, String format, int markIndex) {
        return new ExternalMarkImpl( resource, format, markIndex );
    }

    public FeatureTypeStyleImpl featureTypeStyle(String name,
            Description description, Id definedFor, Set<Name> featureTypeNames,
            Set<SemanticType> types, List<org.opengis.style.Rule> rules) {
        FeatureTypeStyleImpl featureTypeStyle = new FeatureTypeStyleImpl();
        featureTypeStyle.setName( name );
        
        if( description != null && description.getTitle() != null ){
            featureTypeStyle.setTitle(description.getTitle().toString());
        }
        if( description != null && description.getAbstract() != null ){
            featureTypeStyle.setAbstract( description.getAbstract().toString());
        }
        //featureTypeStyle.setFeatureInstanceIDs( defainedFor );
        featureTypeStyle.featureTypeNames().addAll( featureTypeNames );
        featureTypeStyle.semanticTypeIdentifiers().addAll( types );
        
        for( org.opengis.style.Rule rule : rules ){
            if( rule instanceof RuleImpl ){
                featureTypeStyle.rules().add( (RuleImpl) rule );
            }
            else {
                featureTypeStyle.rules().add( new RuleImpl( rule ));
            }
        }
        return featureTypeStyle;
    }

    public FillImpl fill(GraphicFill graphicFill, Expression color, Expression opacity) {
        FillImpl fill = new FillImpl( filterFactory);
        fill.setGraphicFill(graphicFill);
        fill.setColor(color);
        fill.setOpacity(opacity);
        return fill;
    }

    public FontImpl font(List<Expression> family, Expression style,
            Expression weight, Expression size) {
        FontImpl font = new FontImpl();
        font.getFamily().addAll( family );
        font.setStyle(style);
        font.setWeight(weight);
        font.setSize(size);
        
        return font;
    }

    public GraphicImpl graphic(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchor,
            org.opengis.style.Displacement disp) {
        
        GraphicImpl graphic = new GraphicImpl( filterFactory );
        if( symbols != null ){
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if( graphicalSymbol instanceof ExternalGraphic){
                    graphic.graphicalSymbols().add( ExternalGraphicImpl.cast( graphicalSymbol ) );                  
                }
                else if ( graphicalSymbol instanceof Mark ){
                    graphic.graphicalSymbols().add( MarkImpl.cast(graphicalSymbol));
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
    public GraphicImpl graphicFill(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement) {
        
        GraphicImpl graphicFill = new GraphicImpl( filterFactory );
        if( symbols != null ){
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if( graphicalSymbol instanceof ExternalGraphic){
                    graphicFill.graphicalSymbols().add( ExternalGraphicImpl.cast( graphicalSymbol ) );                  
                }
                else if ( graphicalSymbol instanceof Mark ){
                    graphicFill.graphicalSymbols().add( MarkImpl.cast(graphicalSymbol));
                }
            }
        }
        graphicFill.setOpacity(opacity);
        graphicFill.setSize(size);
        graphicFill.setRotation(rotation);
        graphicFill.setAnchorPoint(anchorPoint);
        graphicFill.setDisplacement(displacement);
        
        return graphicFill;
    }
    public GraphicImpl graphicLegend(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement) {
        GraphicImpl graphicLegend = new GraphicImpl( filterFactory );
        if( symbols != null ){
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if( graphicalSymbol instanceof ExternalGraphic){
                    graphicLegend.graphicalSymbols().add( ExternalGraphicImpl.cast( graphicalSymbol ) );                  
                }
                else if ( graphicalSymbol instanceof Mark ){
                    graphicLegend.graphicalSymbols().add( MarkImpl.cast(graphicalSymbol));
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
    public GraphicImpl graphicStroke(List<GraphicalSymbol> symbols, Expression opacity,
            Expression size, Expression rotation, org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement, Expression initialGap, Expression gap) {
        GraphicImpl graphicStroke = new GraphicImpl( filterFactory );
        if( symbols != null ){
            for (GraphicalSymbol graphicalSymbol : symbols) {
                if( graphicalSymbol instanceof ExternalGraphic){
                    graphicStroke.graphicalSymbols().add( ExternalGraphicImpl.cast( graphicalSymbol ) );                  
                }
                else if ( graphicalSymbol instanceof Mark ){
                    graphicStroke.graphicalSymbols().add( MarkImpl.cast(graphicalSymbol));
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
        
        return graphicStroke;
    }
    public HaloImpl halo(org.opengis.style.Fill fill, Expression radius) {
        HaloImpl halo = new HaloImpl();
        halo.setFill( fill );
        halo.setRadius( radius );
        
        return halo;
    }

    public LinePlacementImpl linePlacement(Expression offset, Expression initialGap,
            Expression gap, boolean repeated, boolean aligned, boolean generalizedLine) {
        LinePlacementImpl placement = new LinePlacementImpl( filterFactory );
        placement.setPerpendicularOffset( offset );
        placement.setInitialGap(initialGap);
        placement.setGap(gap);
        placement.setRepeated( repeated );
        placement.setAligned(aligned);
        placement.setGeneralized(generalizedLine);
        
        return placement;
    }

    @SuppressWarnings("unchecked")
    public LineSymbolizerImpl lineSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, org.opengis.style.Stroke stroke,
            Expression offset) {
        LineSymbolizerImpl copy = new LineSymbolizerImpl();
        copy.setDescription( description );
        copy.setGeometry(geometry);
        copy.setName( name );
        copy.setPerpendicularOffset( offset );
        copy.setStroke( stroke );
        copy.setUnitOfMeasure( (Unit<Length>) unit );
        return copy;
    }
    public MarkImpl mark(Expression wellKnownName, org.opengis.style.Fill fill,
            org.opengis.style.Stroke stroke) {
        
        MarkImpl mark = new MarkImpl(filterFactory, null);
        mark.setWellKnownName(wellKnownName);
        mark.setFill(fill);
        mark.setStroke(stroke);
        
        return mark;
    }

    public MarkImpl mark(ExternalMark externalMark, org.opengis.style.Fill fill,
            org.opengis.style.Stroke stroke) {
        MarkImpl mark = new MarkImpl();
        mark.setExternalMark(externalMark);
        mark.setFill(fill);
        mark.setStroke(stroke);
        
        return mark;
    }

    public PointPlacementImpl pointPlacement(org.opengis.style.AnchorPoint anchor,
            org.opengis.style.Displacement displacement, Expression rotation) {
        PointPlacementImpl pointPlacment = new PointPlacementImpl( filterFactory );
        pointPlacment.setAnchorPoint(anchor);
        pointPlacment.setDisplacement(displacement);
        pointPlacment.setRotation(rotation);
        return pointPlacment;
    }

    @SuppressWarnings("unchecked")
    public PointSymbolizerImpl pointSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, org.opengis.style.Graphic graphic) {
        PointSymbolizerImpl copy = new PointSymbolizerImpl();
        copy.setDescription( description );
        copy.setGeometryPropertyName( ((PropertyName)geometry).getPropertyName() );
        copy.setGraphic( graphic );
        copy.setName( name );
        copy.setUnitOfMeasure( (Unit<Length>) unit);
        return copy;
    }
    @SuppressWarnings("unchecked")
    public PolygonSymbolizerImpl polygonSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, org.opengis.style.Stroke stroke,
            org.opengis.style.Fill fill, org.opengis.style.Displacement displacement,
            Expression offset) {
        PolygonSymbolizerImpl polygonSymbolizer = new PolygonSymbolizerImpl();
        polygonSymbolizer.setStroke( stroke );
        polygonSymbolizer.setDescription( description );
        polygonSymbolizer.setDisplacement(displacement );
        polygonSymbolizer.setFill(fill);
        polygonSymbolizer.setGeometryPropertyName( ((PropertyName)geometry).getPropertyName());
        polygonSymbolizer.setName( name );
        polygonSymbolizer.setPerpendicularOffset( offset);
        polygonSymbolizer.setUnitOfMeasure( (Unit<Length>) unit);
        return polygonSymbolizer;
    }
    
    @SuppressWarnings("unchecked")
    public RasterSymbolizerImpl rasterSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Expression opacity,
            org.opengis.style.ChannelSelection channelSelection, OverlapBehavior overlapsBehaviour,
            org.opengis.style.ColorMap colorMap, org.opengis.style.ContrastEnhancement contrast,
            org.opengis.style.ShadedRelief shaded, org.opengis.style.Symbolizer outline) {
        RasterSymbolizerImpl rasterSymbolizer = new RasterSymbolizerImpl( filterFactory );
        rasterSymbolizer.setChannelSelection(channelSelection);
        rasterSymbolizer.setColorMap( colorMap );
        rasterSymbolizer.setContrastEnhancement( contrast );
        rasterSymbolizer.setDescription(  description );
        if( geometry != null ){
            rasterSymbolizer.setGeometryPropertyName( ((PropertyName)geometry).getPropertyName() );
        }
        else {
            rasterSymbolizer.setGeometryPropertyName(null);
        }
        rasterSymbolizer.setImageOutline( outline );
        rasterSymbolizer.setName( name );
        rasterSymbolizer.setOpacity( opacity );
        rasterSymbolizer.setOverlapBehavior( overlapsBehaviour );
        rasterSymbolizer.setShadedRelief( shaded );
        rasterSymbolizer.setUnitOfMeasure((Unit<Length>) unit);
        return rasterSymbolizer;        
    }
    
    @SuppressWarnings("unchecked")
    public ExtensionSymbolizer extensionSymbolizer(String name, String propertyName,
            Description description, Unit<?> unit, String extensionName,
            Map<String, Expression> parameters) {
        // We need a factory extension mechanism here to register additional
        // symbolizer implementations
        VendorSymbolizerImpl extension = new VendorSymbolizerImpl();
        extension.setName(name);
        extension.setGeometryPropertyName(propertyName);
        extension.setDescription(description);
        extension.setUnitOfMeasure((Unit<Length>)unit);
        extension.setExtensionName(extensionName);
        extension.getParameters().putAll( parameters );
        
        return extension;
    }    
  
    static org.geotools.styling.Symbolizer cast( org.opengis.style.Symbolizer symbolizer ){
        if( symbolizer instanceof org.opengis.style.PolygonSymbolizer){
            return PolygonSymbolizerImpl.cast( symbolizer );
        }
        else if( symbolizer instanceof org.opengis.style.LineSymbolizer){
            return LineSymbolizerImpl.cast( symbolizer );                    
        }
        else if( symbolizer instanceof org.opengis.style.PointSymbolizer){
            return PointSymbolizerImpl.cast( symbolizer );
        }
        else if( symbolizer instanceof org.opengis.style.RasterSymbolizer){
            return RasterSymbolizerImpl.cast( symbolizer );                    
        }
        else if( symbolizer instanceof org.opengis.style.TextSymbolizer){
            return TextSymbolizerImpl.cast( symbolizer );                    
        }
        else if( symbolizer instanceof org.opengis.style.ExtensionSymbolizer){
            // here is where we can hook up experimental symbolizers
            ExtensionSymbolizer extensionSymbolizer = (ExtensionSymbolizer) symbolizer;
            String name = extensionSymbolizer.getExtensionName();
            // use name to look up implementation
            
            return null;
        }
        return null; // must be some new extension?   
    }
    public RuleImpl rule(String name, Description description, GraphicLegend legend,
            double min, double max, List<org.opengis.style.Symbolizer> symbolizers, Filter filter) {
        RuleImpl rule = new RuleImpl();
        rule.setName(name);
        rule.setDescription(description);
        rule.setLegend(legend);
        rule.setMinScaleDenominator(min);
        rule.setMaxScaleDenominator(max);
        if( symbolizers != null ){
            for (org.opengis.style.Symbolizer symbolizer : symbolizers) {
                rule.symbolizers().add( cast( symbolizer ) );
            }
        }
        return rule;
    }
    
    public SelectedChannelTypeImpl selectedChannelType(String channelName,
            org.opengis.style.ContrastEnhancement contrastEnhancement)
    {
        SelectedChannelTypeImpl selectedChannelType = new SelectedChannelTypeImpl( filterFactory );
        selectedChannelType.setChannelName(channelName);
        selectedChannelType.setContrastEnhancement( contrastEnhancement );
        return selectedChannelType;
    }
    
    public ShadedReliefImpl shadedRelief(Expression reliefFactor,
            boolean brightnessOnly) {
        ShadedReliefImpl shadedRelief = new ShadedReliefImpl( filterFactory );
        shadedRelief.setReliefFactor(reliefFactor);
        shadedRelief.setBrightnessOnly(brightnessOnly);
        return shadedRelief;
    }

    public StrokeImpl stroke(Expression color, Expression opacity, Expression width,
            Expression join, Expression cap, float[] dashes, Expression offset) {
        StrokeImpl stroke = new StrokeImpl( filterFactory );
        stroke.setColor(color);
        stroke.setOpacity(opacity);
        stroke.setWidth(width);
        stroke.setLineJoin(join);
        stroke.setLineCap(cap);
        stroke.setDashArray(dashes);
        stroke.setDashOffset(offset);
        return stroke;
    }
    
    public StrokeImpl stroke(GraphicFill fill, Expression color, Expression opacity,
            Expression width, Expression join, Expression cap, float[] dashes, Expression offset) {
        StrokeImpl stroke = new StrokeImpl( filterFactory );
        stroke.setGraphicFill( fill );
        stroke.setColor(color);
        stroke.setOpacity(opacity);
        stroke.setWidth(width);
        stroke.setLineJoin(join);
        stroke.setLineCap(cap);
        stroke.setDashArray(dashes);
        stroke.setDashOffset(offset);
        return stroke;
    }
    
    public StrokeImpl stroke(GraphicStroke stroke, Expression color,
            Expression opacity, Expression width, Expression join, Expression cap, float[] dashes,
            Expression offset) {
        StrokeImpl s = new StrokeImpl(filterFactory);
        s.setColor(color);
        s.setWidth(width);
        s.setOpacity(opacity);
        s.setLineJoin(join);
        s.setLineCap(cap);
        s.setDashArray(dashes);
        s.setDashOffset(offset);
        s.setGraphicStroke(GraphicImpl.cast(stroke));
        
        return s;
    }
    
    public StyleImpl style(String name, Description description, boolean isDefault,
            List<org.opengis.style.FeatureTypeStyle> featureTypeStyles,
            org.opengis.style.Symbolizer defaultSymbolizer) {
        StyleImpl style = new StyleImpl();
        style.setName(name);
        style.setDescription( description );
        style.setDefault( isDefault );
        if( featureTypeStyles != null ){
            for (org.opengis.style.FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
                style.featureTypeStyles().add( FeatureTypeStyleImpl.cast( featureTypeStyle));                
            }
        }
        style.setDefaultSpecification( cast( defaultSymbolizer ));
        return style;
    }
    
    @SuppressWarnings("unchecked")
    public TextSymbolizerImpl textSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Expression label, org.opengis.style.Font font,
            org.opengis.style.LabelPlacement placement, org.opengis.style.Halo halo,
            org.opengis.style.Fill fill) {

        TextSymbolizerImpl tSymb = new TextSymbolizerImpl(filterFactory);
        tSymb.setName(name);
        tSymb.setFill(fill);
        tSymb.setUnitOfMeasure( (Unit<Length>) unit );
        tSymb.setFont( font );
        tSymb.setGeometryPropertyName(((PropertyName)geometry).getPropertyName());
        tSymb.setHalo(halo);
        tSymb.setLabel(label);
        tSymb.setLabelPlacement(placement);
        //tSymb.setGraphic( GraphicImpl.cast(graphic));
        tSymb.setDescription(description);        
        return tSymb;
    }
}
