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
import java.util.*;
import javax.measure.Unit;
import javax.swing.Icon;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.*;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.util.InternationalString;

/** Abstract base class for implementing style factories. */
public abstract class AbstractStyleFactory implements org.geotools.api.style.StyleFactory {
    public abstract TextSymbolizer createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            org.geotools.api.style.LabelPlacement labelPlacement,
            String geometryPropertyName);

    public abstract ExternalGraphic createExternalGraphic(URL url, String format);

    public abstract ExternalGraphic createExternalGraphic(String uri, String format);

    public abstract ExternalGraphic createExternalGraphic(Icon inlineContent, String format);

    public abstract AnchorPoint createAnchorPoint(Expression x, Expression y);

    public abstract Displacement createDisplacement(Expression x, Expression y);

    //    public abstract LinePlacement createLinePlacement();
    public abstract PointSymbolizer createPointSymbolizer();

    //    public abstract PointPlacement createPointPlacement();
    public abstract Mark createMark(
            Expression wellKnownName,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            Expression size,
            Expression rotation);

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

    public abstract Fill createFill(
            Expression color, Expression backgroundColor, Expression opacity, Graphic graphicFill);

    public abstract LineSymbolizer createLineSymbolizer();

    public abstract PointSymbolizer createPointSymbolizer(
            Graphic graphic, String geometryPropertyName);

    public abstract Style createStyle();

    public abstract NamedStyleImpl createNamedStyle();

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
     * @return the stroke object
     * @see org.geotools.api.style.Stroke
     */
    public abstract Stroke createStroke(Expression color, Expression width);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
     * @see org.geotools.api.style.Stroke
     */
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
     * @see org.geotools.api.style.Stroke
     */
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

    public abstract Rule createRule();

    public abstract LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName);

    public abstract FeatureTypeStyle createFeatureTypeStyle();

    public abstract Graphic createGraphic(
            ExternalGraphicImpl[] externalGraphics,
            MarkImpl[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation);

    public abstract Font createFont(
            Expression fontFamily,
            Expression fontStyle,
            Expression fontWeight,
            Expression fontSize);

    public abstract Mark createMark();

    public abstract PolygonSymbolizer createPolygonSymbolizer(
            Stroke stroke, Fill fill, String geometryPropertyName);

    public abstract RasterSymbolizer createRasterSymbolizer(
            String geometryPropertyName,
            Expression opacity,
            ChannelSelection channel,
            Expression overlap,
            ColorMap colorMap,
            ContrastEnhancement ce,
            ShadedRelief relief,
            Symbolizer outline);

    public abstract RasterSymbolizer getDefaultRasterSymbolizer();

    public abstract SelectedChannelType createSelectedChannelType(
            Expression name, Expression enhancement);

    public abstract SelectedChannelType createSelectedChannelType(
            String name, ContrastEnhancement enhancement);

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
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and
     * defaultPointPlacement, Sets the geometry attribute name to be geometry:text. No Halo is set.
     * <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    public abstract TextSymbolizer getDefaultTextSymbolizer();

    public abstract Graphic getDefaultGraphic();

    public abstract Font getDefaultFont();

    public abstract PointPlacement getDefaultPointPlacement();

    /**
     * Returns implementation hints for this factory. The default implementation returns an empty
     * map.
     */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    public abstract TextSymbolizer createTextSymbolizer(
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Font[] fonts,
            org.geotools.api.style.Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName,
            org.geotools.api.style.Graphic graphic);

    /**
     * Creates a new extent.
     *
     * @param name The name of the extent.
     * @param value The value of the extent.
     * @return The new extent.
     */
    public abstract Extent createExtent(String name, String value);

    /**
     * Creates a new feature type constraint.
     *
     * @param featureTypeName The feature type name.
     * @param filter The filter.
     * @param extents The extents.
     * @return The new feature type constaint.
     */
    public abstract FeatureTypeConstraint createFeatureTypeConstraint(
            String featureTypeName, Filter filter, org.geotools.api.style.Extent... extents);

    public abstract LayerFeatureConstraints createLayerFeatureConstraints(
            org.geotools.api.style.FeatureTypeConstraint... featureTypeConstraints);

    /**
     * Creates a new ImageOutline.
     *
     * @param symbolizer A line or polygon symbolizer.
     * @return The new image outline.
     */
    public abstract ImageOutline createImageOutline(Symbolizer symbolizer);

    public abstract RasterSymbolizer createRasterSymbolizer();

    public abstract ChannelSelection createChannelSelection(SelectedChannelTypeImpl... channels);

    public abstract ContrastEnhancement createContrastEnhancement();

    public abstract ContrastEnhancement createContrastEnhancement(Expression gammaValue);

    public abstract SelectedChannelType createSelectedChannelType(
            Expression name, ContrastEnhancement enhancement);

    public abstract Graphic createDefaultGraphic();

    public abstract StyledLayerDescriptor createStyledLayerDescriptor();

    public abstract UserLayer createUserLayer();

    public abstract NamedLayer createNamedLayer();

    public abstract RemoteOWS createRemoteOWS(String service, String onlineResource);

    public abstract ShadedRelief createShadedRelief(Expression reliefFactor);

    /** Indicate what part of a Graphic is used to mark the location. */
    @Override
    public abstract AnchorPoint anchorPoint(Expression x, Expression y);

    /** */
    @Override
    public abstract ChannelSelection channelSelection(
            org.geotools.api.style.SelectedChannelType gray);

    /** */
    @Override
    public abstract ChannelSelection channelSelection(
            org.geotools.api.style.SelectedChannelType red,
            org.geotools.api.style.SelectedChannelType green,
            org.geotools.api.style.SelectedChannelType blue);

    /**
     * Wrap up a "Categorize" function using the provided expressions.
     *
     * <p>The function will be created based on:
     *
     * <ol>
     *   <li>PropertyName; use "Rasterdata" to indicate this is a color map
     *   <li>Literal: lookup value
     *   <li>Literal: threshold 1
     *   <li>Literal: value 1
     *   <li>Literal: threshold 2
     *   <li>Literal: value 2
     *   <li>Literal: (Optional) succeeding or preceding
     * </ol>
     *
     * @param propertyName Property name to categorize, or use "Raster"
     * @param mapping Defined as a series of Expressions
     * @return ColorMap wrapped around the "Cateogize" function
     */
    @Override
    public abstract ColorMap colorMap(Expression propertyName, Expression... mapping);

    /**
     * Wrap up a replacement function using the provided expressions.
     *
     * @param propertyName Property name to categorize, or use "Raster"
     * @param mapping Defined as a series of Expressions
     * @return ColorReplacement wrapped around a Function
     */
    @Override
    public abstract ColorReplacement colorReplacement(
            Expression propertyName, Expression... mapping);

    /** */
    @Override
    public abstract ContrastEnhancement contrastEnhancement(
            Expression gamma, org.geotools.api.style.ContrastMethod method);

    /** */
    @Override
    public abstract Description description(
            InternationalString title, InternationalString description);

    /** */
    @Override
    public abstract Displacement displacement(Expression dx, Expression dy);

    /** */
    @Override
    public abstract ExternalGraphic externalGraphic(
            OnLineResource resource,
            String format,
            Collection<org.geotools.api.style.ColorReplacement> replacements);

    /** */
    @Override
    public abstract ExternalGraphic externalGraphic(
            Icon inline, Collection<org.geotools.api.style.ColorReplacement> replacements);

    /** */
    @Override
    public abstract ExternalMark externalMark(
            OnLineResource resource, String format, int markIndex);

    /** */
    @Override
    public abstract ExternalMark externalMark(Icon inline);

    /**
     * Direct FeatureTypeStyle creation (with no default SLD values applied).
     *
     * @param name Name
     * @param description Description with title and abstract information
     * @param definedFor Currently unused
     * @param featureTypeNames FeatureTypes this style applies to, use AbstractFeature to match all
     * @param types SemanticType
     * @param rules May not be null or empty
     * @return feature type style
     * @see org.geotools.api.feature.simple.SimpleFeature
     */
    @Override
    public abstract FeatureTypeStyle featureTypeStyle(
            String name,
            org.geotools.api.style.Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<org.geotools.api.style.SemanticType> types,
            List<org.geotools.api.style.Rule> rules);

    /** */
    @Override
    public abstract Fill fill(
            org.geotools.api.style.GraphicFill fill, Expression color, Expression opacity);

    /** */
    @Override
    public abstract Font font(
            List<Expression> family, Expression style, Expression weight, Expression size);

    @Override
    public abstract Graphic graphic(
            List<org.geotools.api.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp);

    /** */
    @Override
    public abstract GraphicFill graphicFill(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement);

    /** */
    @Override
    public abstract GraphicLegend graphicLegend(
            List<org.geotools.api.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement);

    /** */
    @Override
    public abstract GraphicStroke graphicStroke(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression initialGap,
            Expression gap);

    /** */
    @Override
    public abstract Halo halo(org.geotools.api.style.Fill fill, Expression radius);

    /** */
    @Override
    public abstract LinePlacement linePlacement(
            Expression offset,
            Expression initialGap,
            Expression gap,
            boolean repeated,
            boolean aligned,
            boolean generalizedLine);

    /**
     * @param name handle used to refer to this symbolizer (machine readible)
     * @param geometry Expression used to produce the Geometry to renderer; often a PropertyName
     * @param unit Unit of measure used to define this symbolizer
     * @param stroke Definition of how to stroke linework
     * @param offset Offset used to position line relative to origional
     * @return Newly created Line Symbolizer
     */
    @Override
    public abstract LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset);

    /** */
    @Override
    public abstract Mark mark(
            Expression wellKnownName,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke);

    /** */
    @Override
    public abstract Mark mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke);

    /** */
    @Override
    public abstract PointPlacement pointPlacement(
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement displacement,
            Expression rotation);

    /**
     * Creation of a PointSymbolizer to describe how geometry can be rendered as a point.
     *
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer
     * @param unit Unit of Measure used to interpret symbolizer distances
     * @param graphic Graphic used to represent the geometry when rendering
     * @return Newly created PointSymbolizer
     */
    @Override
    public abstract PointSymbolizer pointSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Graphic graphic);

    /**
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer
     * @param unit Unit of Measure used to interpret symbolizer distances
     */
    @Override
    public abstract PolygonSymbolizer polygonSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Displacement displacement,
            Expression offset);

    /**
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer
     * @param unit Unit of Measure used to interpret symbolizer distances
     * @return RasterSymbolizer
     */
    @Override
    public abstract RasterSymbolizer rasterSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            Expression opacity,
            org.geotools.api.style.ChannelSelection channelSelection,
            org.geotools.api.style.OverlapBehavior overlapsBehaviour,
            org.geotools.api.style.ColorMap colorMap,
            org.geotools.api.style.ContrastEnhancement contrast,
            org.geotools.api.style.ShadedRelief shaded,
            org.geotools.api.style.Symbolizer outline);

    /**
     * Used to represent a symbolizer intended for a vendor specific rendering process. This
     * facility should be used to control subject matter that is beyond the scope of the traditional
     * symbology encoding data structure (subject matter like wind barbs or extra deegrees of
     * freedom like temporal symbolizers are good examples of the use of this facility).
     *
     * @param name handle used to refer to this symbolizer (machine readible)
     * @param geometry Geometry expression to renderer; formally a PropertyName
     * @param description Description of this symbolizer; human readable
     * @param unit Unit of measure to use when interpretting this symbolizer
     * @param extensionName Extension name used to identify the vendor specific extension being
     *     controlled
     * @param parameters Named expressions used to configure the vendor specific rendering process
     * @return newly created ExtensionSymbolizer
     */
    @Override
    public abstract ExtensionSymbolizer extensionSymbolizer(
            String name,
            String geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            String extensionName,
            Map<String, Expression> parameters);

    /**
     * Create a rule from the provided definition.
     *
     * @param name handle used to refer to this rule (machine readable)
     * @param description Human readable description of this rule
     * @param legend Graphic used to indicate this rule in a legend or user interface
     * @param min minimum scale denominator used to control when this rule is applied
     * @param max maximum scale denominator used to control when this rule is applied
     * @return Newly created Rule
     */
    @Override
    public abstract Rule rule(
            String name,
            org.geotools.api.style.Description description,
            org.geotools.api.style.GraphicLegend legend,
            double min,
            double max,
            List<org.geotools.api.style.Symbolizer> symbolizers,
            Filter filter);

    /** @return SelectedChannelType */
    @Override
    public abstract SelectedChannelType selectedChannelType(
            Expression channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement);

    /** @return SelectedChannelType */
    @Override
    public abstract SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement);

    /** @return ShadedRelief */
    @Override
    public abstract ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly);

    @Override
    public abstract Stroke stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    @Override
    public abstract Stroke stroke(
            org.geotools.api.style.GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    @Override
    public abstract Stroke stroke(
            org.geotools.api.style.GraphicStroke stroke,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    /** */
    @Override
    public abstract Style style(
            String name,
            org.geotools.api.style.Description description,
            boolean isDefault,
            List<org.geotools.api.style.FeatureTypeStyle> featureTypeStyles,
            org.geotools.api.style.Symbolizer defaultSymbolizer);

    /**
     * Creation of a TextSymbolizer defining how labels are portrayed.
     *
     * @param name Handle used to refer to this symbolizer (machine readable)
     * @param geometry Geometry to be rendered
     * @param description Human readable description
     * @param unit Unit of measure used to interpret symbolizer sizes
     * @param label Text displayed for this symbolizer
     * @param font Font selected to renderer this symbolizer
     * @param placement Placement information relative to orgiginal geometry
     * @param halo definition of a halo or outline surrounding the symbolizer
     * @param fill definition of fill used
     * @return newly created TextSymbolizer
     */
    @Override
    public abstract TextSymbolizer textSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            Expression label,
            org.geotools.api.style.Font font,
            LabelPlacement placement,
            org.geotools.api.style.Halo halo,
            org.geotools.api.style.Fill fill);

    /** @return a deep copy of the method */
    public abstract ContrastMethod createContrastMethod(ContrastMethod method);
}
