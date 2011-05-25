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

import java.awt.RenderingHints;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.opengis.filter.expression.Expression;


/**
 * Abstract base class for implementing style factories.
 *
 * @source $URL$
 */
public abstract class AbstractStyleFactory implements StyleFactory {
    public abstract TextSymbolizer createTextSymbolizer(Fill fill,
        Font[] fonts, Halo halo, Expression label,
        LabelPlacement labelPlacement, String geometryPropertyName);

    public abstract ExternalGraphic createExternalGraphic(URL url, String format);

    public abstract ExternalGraphic createExternalGraphic(String uri,
        String format);

    public abstract AnchorPoint createAnchorPoint(Expression x, Expression y);

    public abstract Displacement createDisplacement(Expression x, Expression y);

    //    public abstract LinePlacement createLinePlacement();
    public abstract PointSymbolizer createPointSymbolizer();

    //    public abstract PointPlacement createPointPlacement();
    public abstract Mark createMark(Expression wellKnownName, Stroke stroke,
        Fill fill, Expression size, Expression rotation);

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public abstract Mark getCircleMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public abstract Mark getXMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public abstract Mark getStarMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public abstract Mark getSquareMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public abstract Mark getCrossMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public abstract Mark getTriangleMark();

    public abstract FeatureTypeStyle createFeatureTypeStyle(Rule[] rules);

    public abstract LinePlacement createLinePlacement(Expression offset);

    public abstract PolygonSymbolizer createPolygonSymbolizer();

    public abstract Halo createHalo(Fill fill, Expression radius);

    public abstract Fill createFill(Expression color,
        Expression backgroundColor, Expression opacity, Graphic graphicFill);

    public abstract LineSymbolizer createLineSymbolizer();

    public abstract PointSymbolizer createPointSymbolizer(Graphic graphic,
        String geometryPropertyName);

    public abstract Style createStyle();

    public abstract NamedStyle createNamedStyle();

    public abstract Fill createFill(Expression color, Expression opacity);

    public abstract Fill createFill(Expression color);

    public abstract TextSymbolizer createTextSymbolizer();

    public abstract PointPlacement createPointPlacement(
        AnchorPoint anchorPoint, Displacement displacement, Expression rotation);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width the width of the line
     *
     * @return the stroke object
     *
     * @see org.geotools.stroke
     */
    public abstract Stroke createStroke(Expression color, Expression width);

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
    public abstract Stroke createStroke(Expression color, Expression width,
        Expression opacity);

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
     * @see org.geotools.stroke
     */
    public abstract Stroke createStroke(Expression color, Expression width,
        Expression opacity, Expression lineJoin, Expression lineCap,
        float[] dashArray, Expression dashOffset, Graphic graphicFill,
        Graphic graphicStroke);

    public abstract Rule createRule();

    public abstract LineSymbolizer createLineSymbolizer(Stroke stroke,
        String geometryPropertyName);

    public abstract FeatureTypeStyle createFeatureTypeStyle();

    public abstract Graphic createGraphic(ExternalGraphic[] externalGraphics,
        Mark[] marks, Symbol[] symbols, Expression opacity, Expression size,
        Expression rotation);

    public abstract Font createFont(Expression fontFamily,
        Expression fontStyle, Expression fontWeight, Expression fontSize);

    public abstract Mark createMark();

    public abstract PolygonSymbolizer createPolygonSymbolizer(Stroke stroke,
        Fill fill, String geometryPropertyName);

    public abstract RasterSymbolizer createRasterSymbolizer(
        String geometryPropertyName, Expression opacity,
        ChannelSelection channel, Expression overlap, ColorMap colorMap,
        ContrastEnhancement ce, ShadedRelief relief, Symbolizer outline);

    public abstract RasterSymbolizer getDefaultRasterSymbolizer();

    public abstract ChannelSelection createChannelSelection(
        SelectedChannelType[] channels);

    public abstract SelectedChannelType createSelectedChannelType(String name,
        Expression enhancement);

    public abstract ColorMap createColorMap();

    public abstract ColorMapEntry createColorMapEntry();

    public abstract Style getDefaultStyle();

    public abstract Stroke getDefaultStroke();

    public abstract Fill getDefaultFill();

    public abstract Mark getDefaultMark();

    public abstract PointSymbolizer getDefaultPointSymbolizer();

    public abstract PolygonSymbolizer getDefaultPolygonSymbolizer();

    public abstract LineSymbolizer getDefaultLineSymbolizer();

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont
     * and defaultPointPlacement,  Sets the geometry attribute name to be
     * geometry:text. No Halo is set. <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    public abstract TextSymbolizer getDefaultTextSymbolizer();

    public abstract Graphic getDefaultGraphic();

    public abstract Font getDefaultFont();

    public abstract PointPlacement getDefaultPointPlacement();

    /**
     * Returns implementation hints for this factory. The default
     * implementation returns an empty map.
     *
     * @return DOCUMENT ME!
     */
    public Map<RenderingHints.Key,?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
