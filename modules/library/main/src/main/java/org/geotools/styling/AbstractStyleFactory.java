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
import javax.swing.Icon;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastEnhancement;
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
import org.geotools.api.style.NamedStyle;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.PointSymbolizer;
import org.geotools.api.style.PolygonSymbolizer;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.Rule;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.ShadedRelief;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.Symbol;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TextSymbolizer;

/** Abstract base class for implementing style factories. */
public abstract class AbstractStyleFactory implements StyleFactory {
    @Override
    public abstract TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName);

    @Override
    public abstract ExternalGraphic createExternalGraphic(URL url, String format);

    @Override
    public abstract ExternalGraphic createExternalGraphic(String uri, String format);

    @Override
    public abstract ExternalGraphic createExternalGraphic(Icon inlineContent, String format);

    @Override
    public abstract AnchorPoint createAnchorPoint(Expression x, Expression y);

    @Override
    public abstract Displacement createDisplacement(Expression x, Expression y);

    //    public abstract LinePlacement createLinePlacement();
    @Override
    public abstract PointSymbolizer createPointSymbolizer();

    //    public abstract PointPlacement createPointPlacement();
    @Override
    public abstract Mark createMark(
            Expression wellKnownName, Stroke stroke, Fill fill, Expression size, Expression rotation);

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    @Override
    public abstract Mark getCircleMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    @Override
    public abstract Mark getXMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    @Override
    public abstract Mark getStarMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    @Override
    public abstract Mark getSquareMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    @Override
    public abstract Mark getCrossMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    @Override
    public abstract Mark getTriangleMark();

    @Override
    public abstract FeatureTypeStyle createFeatureTypeStyle(Rule... rules);

    @Override
    public abstract LinePlacement createLinePlacement(Expression offset);

    @Override
    public abstract PolygonSymbolizer createPolygonSymbolizer();

    @Override
    public abstract Halo createHalo(Fill fill, Expression radius);

    @Override
    public abstract Fill createFill(
            Expression color, Expression backgroundColor, Expression opacity, Graphic graphicFill);

    @Override
    public abstract LineSymbolizer createLineSymbolizer();

    @Override
    public abstract PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName);

    @Override
    public abstract Style createStyle();

    @Override
    public abstract NamedStyle createNamedStyle();

    @Override
    public abstract Fill createFill(Expression color, Expression opacity);

    @Override
    public abstract Fill createFill(Expression color);

    @Override
    public abstract TextSymbolizer createTextSymbolizer();

    @Override
    public abstract PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, Displacement displacement, Expression rotation);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the stroke object
     * @see org.geotools.api.style.Stroke
     */
    @Override
    public abstract Stroke createStroke(Expression color, Expression width);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
     */
    @Override
    public abstract Stroke createStroke(Expression color, Expression width, Expression opacity);

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
    public abstract Stroke createStroke(
            Expression color,
            Expression width,
            Expression opacity,
            Expression lineJoin,
            Expression lineCap,
            float[] dashArray,
            Expression dashOffset,
            Graphic graphicFill,
            Graphic graphicStroke);

    @Override
    public abstract Rule createRule();

    @Override
    public abstract LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName);

    @Override
    public abstract FeatureTypeStyle createFeatureTypeStyle();

    @Override
    public abstract Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation);

    @Override
    public abstract Font createFont(
            Expression fontFamily, Expression fontStyle, Expression fontWeight, Expression fontSize);

    @Override
    public abstract Mark createMark();

    @Override
    public abstract PolygonSymbolizer createPolygonSymbolizer(Stroke stroke, Fill fill, String geometryPropertyName);

    @Override
    public abstract RasterSymbolizer createRasterSymbolizer(
            String geometryPropertyName,
            Expression opacity,
            ChannelSelection channel,
            Expression overlap,
            ColorMap colorMap,
            ContrastEnhancement ce,
            ShadedRelief relief,
            Symbolizer outline);

    @Override
    public abstract RasterSymbolizer getDefaultRasterSymbolizer();

    @Override
    public abstract SelectedChannelType createSelectedChannelType(Expression name, Expression enhancement);

    @Override
    public abstract SelectedChannelType createSelectedChannelType(String name, ContrastEnhancement enhancement);

    @Override
    public abstract ColorMap createColorMap();

    @Override
    public abstract ColorMapEntry createColorMapEntry();

    @Override
    public abstract Style getDefaultStyle();

    @Override
    public abstract Stroke getDefaultStroke();

    @Override
    public abstract Fill getDefaultFill();

    @Override
    public abstract Mark getDefaultMark();

    @Override
    public abstract PointSymbolizer getDefaultPointSymbolizer();

    @Override
    public abstract PolygonSymbolizer getDefaultPolygonSymbolizer();

    @Override
    public abstract LineSymbolizer getDefaultLineSymbolizer();

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and defaultPointPlacement, Sets the
     * geometry attribute name to be geometry:text. No Halo is set. <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    @Override
    public abstract TextSymbolizer getDefaultTextSymbolizer();

    @Override
    public abstract Graphic getDefaultGraphic();

    @Override
    public abstract Font getDefaultFont();

    @Override
    public abstract PointPlacement getDefaultPointPlacement();

    /** Returns implementation hints for this factory. The default implementation returns an empty map. */
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
