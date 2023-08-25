/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.StyledLayerDescriptor;
import org.geotools.api.util.InternationalString;

import javax.measure.Unit;
import javax.swing.*;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Abstract base class for implementing style factories. */
public interface StyleFactory2 extends org.geotools.api.style.StyleFactory {
    /**
     * Label Shield hack, non SLD 1.1
     *
     * @param fill Fill
     * @param fonts Font information (CSS)
     * @param halo Describes Halo
     * @param label Expression for label
     * @param labelPlacement Captures label position
     * @param geometryPropertyName With respect to this geometry
     * @param graphic Used to draw a backdrop behind label
     * @return TextSymbolizer2 allowing for a backdrop behind text label
     */
    public TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName,
            Graphic graphic);

    TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName);

    ExternalGraphic createExternalGraphic(URL url, String format);

    ExternalGraphic createExternalGraphic(String uri, String format);

    ExternalGraphic createExternalGraphic(Icon inlineContent, String format);

    AnchorPoint createAnchorPoint(Expression x, Expression y);

    Displacement createDisplacement(Expression x, Expression y);

    //    public  LinePlacement createLinePlacement();
    PointSymbolizer createPointSymbolizer();

    //    public  PointPlacement createPointPlacement();
    Mark createMark(
            Expression wellKnownName,
            Stroke stroke,
            Fill fill,
            Expression size,
            Expression rotation);

    /**
     * Creates a new extent.
     *
     * @param name The name of the extent.
     * @param value The value of the extent.
     * @return The new extent.
     */
    Extent createExtent(String name, String value);

    /**
     * Creates a new feature type constraint.
     *
     * @param featureTypeName The feature type name.
     * @param filter The filter.
     * @param extents The extents.
     * @return The new feature type constaint.
     */
    FeatureTypeConstraint createFeatureTypeConstraint(
            String featureTypeName, Filter filter, Extent... extents);

    LayerFeatureConstraints createLayerFeatureConstraints(
                    FeatureTypeConstraint... featureTypeConstraints);

    FeatureTypeStyle createFeatureTypeStyle(Rule... rules);

    /**
     * Creates a new ImageOutline.
     *
     * @param symbolizer A line or polygon symbolizer.
     * @return The new image outline.
     */
    ImageOutline createImageOutline(Symbolizer symbolizer);

    LinePlacement createLinePlacement(Expression offset);

    PolygonSymbolizer createPolygonSymbolizer();

    Halo createHalo(Fill fill, Expression radius);

    Fill createFill(
            Expression color, Expression backgroundColor, Expression opacity, Graphic graphicFill);

    /** Create default line symbolizer */
    LineSymbolizer createLineSymbolizer();

    PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName);

    Style createStyle();

    NamedStyle createNamedStyle();

    Fill createFill(Expression color, Expression opacity);

    Fill createFill(Expression color);

    TextSymbolizer createTextSymbolizer();

    PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, Displacement displacement, Expression rotation);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the stroke object
     * @see org.geotools.stroke
     */
    Stroke createStroke(Expression color, Expression width);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
     * @see org.geotools.stroke
     */
    Stroke createStroke(Expression color, Expression width, Expression opacity);

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
    Stroke createStroke(
            Expression color,
            Expression width,
            Expression opacity,
            Expression lineJoin,
            Expression lineCap,
            float[] dashArray,
            Expression dashOffset,
            Graphic graphicFill,
            Graphic graphicStroke);

    Rule createRule();

    LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName);

    FeatureTypeStyle createFeatureTypeStyle();

    Graphic createGraphic(
                    ExternalGraphic[] externalGraphics,
                    Mark[] marks,
                    Symbol[] symbols,
                    Expression opacity,
                    Expression size,
                    Expression rotation);

    Font createFont(
                            Expression fontFamily,
                            Expression fontStyle,
                            Expression fontWeight,
                            Expression fontSize);

    Mark createMark();

    PolygonSymbolizer createPolygonSymbolizer(
                                    Stroke stroke, Fill fill, String geometryPropertyName);

    RasterSymbolizer createRasterSymbolizer();

    RasterSymbolizer createRasterSymbolizer(
                                            String geometryPropertyName,
                                            Expression opacity,
                                            ChannelSelection channel,
                                            Expression overlap,
                                            ColorMap colorMap,
                                            ContrastEnhancement ce,
                                            ShadedRelief relief,
                                            Symbolizer outline);

    ChannelSelection createChannelSelection(SelectedChannelType... channels);

    ContrastEnhancement createContrastEnhancement();

    ContrastEnhancement createContrastEnhancement(Expression gammaValue);

    SelectedChannelType createSelectedChannelType(
                                                    Expression name, ContrastEnhancement enhancement);

    SelectedChannelType createSelectedChannelType(
                                                            String name, ContrastEnhancement enhancement);

    SelectedChannelType createSelectedChannelType(Expression name, Expression gammaValue);

    ColorMap createColorMap();

    ColorMapEntry createColorMapEntry();

    Graphic createDefaultGraphic();

    StyledLayerDescriptor createStyledLayerDescriptor();

    UserLayer createUserLayer();

    NamedLayer createNamedLayer();

    RemoteOWS createRemoteOWS(String service, String onlineResource);

    ShadedRelief createShadedRelief(Expression reliefFactor);

    /** Indicate what part of a Graphic is used to mark the location. */
    @Override
    AnchorPoint anchorPoint(Expression x, Expression y);

    /** */
    @Override
    ChannelSelection channelSelection(org.geotools.api.style.SelectedChannelType gray);

    /** */
    @Override
    ChannelSelection channelSelection(
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
    ColorMap colorMap(Expression propertyName, Expression... mapping);

    /**
     * Wrap up a replacement function using the provided expressions.
     *
     * @param propertyName Property name to categorize, or use "Raster"
     * @param mapping Defined as a series of Expressions
     * @return ColorReplacement wrapped around a Function
     */
    @Override
    ColorReplacement colorReplacement(Expression propertyName, Expression... mapping);

    /** */
    @Override
    ContrastEnhancement contrastEnhancement(
            Expression gamma, org.geotools.api.style.ContrastMethod method);

    /** */
    @Override
    Description description(InternationalString title, InternationalString description);

    /** */
    @Override
    Displacement displacement(Expression dx, Expression dy);

    /** */
    @Override
    ExternalGraphic externalGraphic(
            OnLineResource resource,
            String format,
            Collection<org.geotools.api.style.ColorReplacement> replacements);

    /** */
    @Override
    ExternalGraphic externalGraphic(
            Icon inline, Collection<org.geotools.api.style.ColorReplacement> replacements);

    /** */
    @Override
    ExternalMark externalMark(OnLineResource resource, String format, int markIndex);

    /** */
    @Override
    ExternalMark externalMark(Icon inline);

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
     * @see SimpleFe
     */
    @Override
    FeatureTypeStyle featureTypeStyle(
            String name,
            org.geotools.api.style.Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<org.geotools.api.style.SemanticType> types,
            List<org.geotools.api.style.Rule> rules);

    /** */
    @Override
    Fill fill(org.geotools.api.style.GraphicFill fill, Expression color, Expression opacity);

    /** */
    @Override
    Font font(List<Expression> family, Expression style, Expression weight, Expression size);

    @Override
    Graphic graphic(
            List<org.geotools.api.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchor,
            org.geotools.api.style.Displacement disp);

    /** */
    @Override
    Graphic graphicFill(
            List<org.geotools.api.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement);

    /** */
    @Override
    GraphicLegend graphicLegend(
            List<org.geotools.api.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement);

    /** */
    @Override
    Graphic graphicStroke(
            List<org.geotools.api.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.geotools.api.style.AnchorPoint anchorPoint,
            org.geotools.api.style.Displacement displacement,
            Expression initialGap,
            Expression gap);

    /** */
    @Override
    Halo halo(org.geotools.api.style.Fill fill, Expression radius);

    /** */
    @Override
    LinePlacement linePlacement(
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
    LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            org.geotools.api.style.Description description,
            Unit<?> unit,
            org.geotools.api.style.Stroke stroke,
            Expression offset);

    /** */
    @Override
    Mark mark(
            Expression wellKnownName,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke);

    /** */
    @Override
    Mark mark(
            org.geotools.api.style.ExternalMark externalMark,
            org.geotools.api.style.Fill fill,
            org.geotools.api.style.Stroke stroke);

    /** */
    @Override
    PointPlacement pointPlacement(
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
    PointSymbolizer pointSymbolizer(
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
    PolygonSymbolizer polygonSymbolizer(
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
    RasterSymbolizer rasterSymbolizer(
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
    ExtensionSymbolizer extensionSymbolizer(
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
    Rule rule(
            String name,
            org.geotools.api.style.Description description,
            org.geotools.api.style.GraphicLegend legend,
            double min,
            double max,
            List<org.geotools.api.style.Symbolizer> symbolizers,
            Filter filter);

    /** @return SelectedChannelType */
    @Override
    SelectedChannelType selectedChannelType(
            Expression channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement);

    /** @return SelectedChannelType */
    @Override
    SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement);

    /** @return ShadedRelief */
    @Override
    ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly);

    @Override
    Stroke stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    @Override
    Stroke stroke(
            org.geotools.api.style.GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    @Override
    Stroke stroke(
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
    Style style(
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
    TextSymbolizer textSymbolizer(
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
    ContrastMethod createContrastMethod(ContrastMethod method);
}
