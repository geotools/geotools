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


/**
 * An interface for classes that want to perform operations on a Style
 * hierarchy. It forms part of a GoF Visitor Patern implementation. A call to
 * style.accept(StyleVisitor) will result in a call to one of the methods in
 * this interface. The responsibility for traversing sub filters is intended
 * to lie with the visitor (this is unusual, but permited under the Visitor
 * pattern). A typical use would be to transcribe a style into a specific
 * format, e.g. XML or SQL.  Alternativly it may be to extract specific
 * information from the Style structure, for example a list of all fills.
 *
 * @author James Macgill
 * @author Ian Turton
 *
 * @source $URL$
 * @version $Id$
 */
public interface StyleVisitor {
    /**
     * Called when accept is called on a StyledLayerDescriptor.
     *
     * @param sld The StyledLayerDescriptor to visit
     */
    void visit(StyledLayerDescriptor sld);

    /**
     * Called when accept is called on a NamedLayer.
     *
     * @param layer The NamedLayer to visit
     */
    void visit(NamedLayer layer);

    /**
     * Called when accept is called on a UserLayer.
     *
     * @param layer The UserLayer to visit
     */
    void visit(UserLayer layer);

    /**
     * Called when accept is called on a FeatureTypeConstraint.
     *
     * @param ftc The FeatureTypeConstraint to visit
     */
    void visit(FeatureTypeConstraint ftc);

    /**
     * Called when accept is called on a Style.
     *
     * @param style The style to visit
     */
    void visit(Style style);

    /**
     * Called when accept is called on a rule
     *
     * @param rule the rule to visit
     */
    void visit(Rule rule);

    /**
     * Called when accept is called on a fetauretypestyle
     *
     * @param fts the feature type styler to visit
     */
    void visit(FeatureTypeStyle fts);

    /**
     * Called when accept is called on a fill
     *
     * @param fill the fill to be visited
     */
    void visit(Fill fill);

    /**
     * Called when accept is called on a stroke
     *
     * @param stroke the stroke to visit
     */
    void visit(Stroke stroke);

    /**
     * since it is impossible to create a Symbolizer this method should
     * generate an exception or warning.
     *
     * @param sym the symbolizer to visit
     */
    void visit(Symbolizer sym);

    /**
     * Called when accept is called on a pointsymbolizer
     *
     * @param ps the point symbolizer to visit
     */
    void visit(PointSymbolizer ps);

    /**
     * Called when accept is called on a linesymbolizer
     *
     * @param line the line symbolizer to visit
     */
    void visit(LineSymbolizer line);

    /**
     * Called when accept is called on a polygon symbolizer
     *
     * @param poly the polygon symbolizer to visit
     */
    void visit(PolygonSymbolizer poly);

    /**
     * Called when accept is called on a textsymbolizer
     *
     * @param text the text symbolizer to visit
     */
    void visit(TextSymbolizer text);

    /**
     * Called when accept is called on a rastersymbolizer
     *
     * @param raster the raster symbolizer to visit
     */
    void visit(RasterSymbolizer raster);

    /**
     * Called when accept is called on a graphic
     *
     * @param gr the graphic to visit
     */
    void visit(Graphic gr);

    /**
     * Called when accept is called on a mark
     *
     * @param mark the mark to visit
     */
    void visit(Mark mark);

    /**
     * Called when accept is called on a external graphic
     *
     * @param exgr the external graphic to visit
     */
    void visit(ExternalGraphic exgr);

    /**
     * Called when accept is called on a Point Placement
     *
     * @param pp the point placement to visit
     */
    void visit(PointPlacement pp);

    /**
     * Called when accept is called on a anchor point
     *
     * @param ap the anchor point to visit
     */
    void visit(AnchorPoint ap);

    /**
     * Called when accept is called on a displacement
     *
     * @param dis the displacement to visit
     */
    void visit(Displacement dis);

    /**
     * Called when accept is called on a Line Placement
     *
     * @param lp the line placement to visit
     */
    void visit(LinePlacement lp);

    /**
     * Called when accept is called on a halo
     *
     * @param halo the halo to visit
     */
    void visit(Halo halo);

    /**
     * Called when accept is called on a raster color map
     *
     * @param colorMap the color map to visit
     */
    void visit(ColorMap colorMap);

    /**
     * Called when accept is called on a raster color map entry
     *
     * @param colorMapEntry the color map to visit
     */
    void visit(ColorMapEntry colorMapEntry);

    /**
     * Called when accept is called on a raster ContrastEnhancement element
     * @param contrastEnhancement the {@link ContrastEnhancement} to visit.
     */
    void visit(ContrastEnhancement contrastEnhancement);

    /**
     * Called when accept is called on a raster {@link ImageOutline} element
     * @param outline the {@link ImageOutline} to visit.
     */
    void visit(ImageOutline outline);

    /**
     * Called when accept is called on a raster {@link ChannelSelection} element
     * @param cs the {@link ChannelSelection} to visit.
     */
    void visit(ChannelSelection cs);

    /**
     * Called when accept is called on a raster {@link OverlapBehavior} element
     * @param cs the {@link OverlapBehavior} to visit.
     */
    void visit(OverlapBehavior ob);

    /**
     * Called when accept is called on a raster {@link SelectedChannelType} element
     * @param cs the {@link SelectedChannelType} to visit.
     */
    void visit(SelectedChannelType sct);

    /**
     * Called when accept is called on a raster {@link ShadedRelief} element
     * @param cs the {@link ShadedRelief} to visit.
     */
    void visit(ShadedRelief sr);
}
