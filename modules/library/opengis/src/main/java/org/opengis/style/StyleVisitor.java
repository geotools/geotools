/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.style;

import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.metadata.citation.OnLineResource;

/**
 * An interface for classes that want to perform operations on a Style
 * hierarchy. It forms part of a GoF Visitor Pattern implementation.
 * <p>
 * A call to style.accept(StyleVisitor) will result in a call to one of the
 * methods in this interface. The responsibility for traversing sub filters is
 * intended to lie with the visitor (this is unusual, but permitted under the
 * Visitor pattern).
 * <p>
 * A typical use would be to transcribe a style into a specific format, e.g. XML or SQL.
 * Alternatively it may be to extract specific information from the Style structure, for example a list of all fills.
 * Finally a a style visitor is often used (in conjunction with a factory) in the production of a
 * copy; or slightly modified copy of the original style.
 * <p>
 * It is common practice for a StyleVisitor to also implement an ExpressionVisitor in
 * order to traverse both data structures.
 * 
 * @see ExpressionVisitor
 * @see StyleFactory
 * @author Open Geospatial Consortium
 * @author James Macgill
 * @author Ian Turton
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface StyleVisitor {
    /**
     * Called when accept is called on a Style.
     *
     * @param style The style to visit
     */
    Object visit(Style style, Object data );
    
    /**
     * Called when accept is called on a FetaureTypeStyle
     *
     * @param fts the feature type styler to visit
     */
    Object visit(FeatureTypeStyle featureTypeStyle, Object data );

    /**
     * Called when accept is called on a rule
     *
     * @param rule the rule to visit
     */
    Object visit(Rule rule, Object data );

    /**
     * Called when accept is called on a pointsymbolizer
     *
     * @param ps the point symbolizer to visit
     */
    Object visit(PointSymbolizer pointSymbolizer, Object data );

    /**
     * Called when accept is called on a linesymbolizer
     *
     * @param line the line symbolizer to visit
     */
    Object visit(LineSymbolizer lineSymbolizer, Object data );

    /**
     * Called when accept is called on a polygon symbolizer
     *
     * @param poly the polygon symbolizer to visit
     */
    Object visit(PolygonSymbolizer polygonSymbolizer, Object data );

    /**
     * Called when accept is called on a textsymbolizer
     *
     * @param text the text symbolizer to visit
     */
    Object visit(TextSymbolizer textSymbolizer, Object data );

    /**
     * Called when accept is called on a rastersymbolizer
     *
     * @param raster the raster symbolizer to visit
     */
    Object visit(RasterSymbolizer rasterSymbolizer, Object data );

    /**
     * Called when accept is called on a extension symbolizer
     *
     * @param extension the extension symbolizer to visit
     */
    Object visit(ExtensionSymbolizer extension, Object data );
    
    /**
     * Called when accept is called on a description
     *
     * @param colorMap the description to visit
     */
    Object visit(Description description, Object data );
    
    /**
     * Called when accept is called on a displacement
     *
     * @param disp the displacement to visit
     */
    Object visit(Displacement displacement, Object data );
    
    /**
     * Called when accept is called on a fill
     *
     * @param fill the fill to be visited
     */
    Object visit(Fill fill, Object data );
    
    /**
     * Called when accept is called on a font
     *
     * @param font the font to be visited
     */
    Object visit(Font font, Object data );

    /**
     * Called when accept is called on a stroke
     *
     * @param stroke the stroke to visit
     */
    Object visit(Stroke stroke, Object data );

    /**
     * Called when accept is called on a graphic
     *
     * @param gr the graphic to visit
     */
    Object visit(Graphic graphic, Object data );
    
    /**
     * Called when accept is called on a graphic fill
     *
     * @param gr the graphic fill to visit
     */
    Object visit(GraphicFill graphicFill, Object data );
    
    /**
     * Called when accept is called on a graphic stroke
     *
     * @param gr the graphic stroke to visit
     */
    Object visit(GraphicStroke graphicStroke, Object data );
    
    /**
     * Called when accept is called on a mark
     *
     * @param mark the mark to visit
     */
    Object visit(Mark mark, Object data );
    
    /**
     * Called when accept is called on a external mark
     *
     * @param exmk the external mark to visit
     */
    Object visit(ExternalMark externalMark, Object data );

    /**
     * Called when accept is called on a external graphic
     *
     * @param exgr the external graphic to visit
     */
    Object visit(ExternalGraphic externalGraphic, Object data );

    /**
     * Called when accept is called on a Point Placement
     *
     * @param pp the point placement to visit
     */
    Object visit(PointPlacement pointPlacement, Object data );

    /**
     * Called when accept is called on a anchor point
     *
     * @param ap the anchor point to visit
     */
    Object visit(AnchorPoint anchorPoint, Object data );

    /**
     * Called when accept is called on a Line Placement
     *
     * @param lp the line placement to visit
     */
    Object visit(LinePlacement linePlacement, Object data );   
    
    /**
     * Called when accept is called on a legend graphic
     *
     * @param lp the legend graphic to visit
     */
    Object visit(GraphicLegend graphicLegend, Object data );
    
    /**
     * Called when accept is called on a halo
     *
     * @param halo the halo to visit
     */
    Object visit(Halo halo, Object data );

    /**
     * Called when accept is called on a raster color map
     *
     * @param colorMap the color map to visit
     */
    Object visit(ColorMap colorMap, Object data );
    
    /**
     * Called when accept is called on a color replacement
     *
     * @param colorMap the color replacement to visit
     */
    Object visit(ColorReplacement colorReplacement, Object data );

    /**
     * Called when accept is called on a raster ContrastEnhancement element
     * @param contrastEnhancement the {@link ContrastEnhancement} to visit.
     */
    Object visit(ContrastEnhancement contrastEnhancement, Object data );

    /**
     * Called when accept is called on a raster {@link ChannelSelection} element
     * @param cs the {@link ChannelSelection} to visit.
     */
    Object visit(ChannelSelection channelSelection, Object data );

    /**
     * Called when accept is called on a raster {@link SelectedChannelType} element
     * @param cs the {@link SelectedChannelType} to visit.
     */
    Object visit(SelectedChannelType selectChannelType, Object data );

    /**
     * Called when accept is called on a raster {@link ShadedRelief} element
     * @param cs the {@link ShadedRelief} to visit.
     */
    Object visit(ShadedRelief shadedRelief, Object data );
}
