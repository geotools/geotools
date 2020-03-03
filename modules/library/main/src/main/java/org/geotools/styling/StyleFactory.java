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
 */
package org.geotools.styling;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.measure.Unit;
import javax.swing.*;
import org.geotools.util.factory.Factory;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.ContrastMethod;
import org.opengis.util.InternationalString;

/** Abstract base class for implementing style factories. */
public interface StyleFactory extends Factory, org.opengis.style.StyleFactory {

    public TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName);

    public ExternalGraphic createExternalGraphic(URL url, String format);

    public ExternalGraphic createExternalGraphic(String uri, String format);

    public ExternalGraphic createExternalGraphic(Icon inlineContent, String format);

    public AnchorPoint createAnchorPoint(Expression x, Expression y);

    public Displacement createDisplacement(Expression x, Expression y);

    //    public  LinePlacement createLinePlacement();
    public PointSymbolizer createPointSymbolizer();

    //    public  PointPlacement createPointPlacement();
    public Mark createMark(
            Expression wellKnownName,
            Stroke stroke,
            Fill fill,
            Expression size,
            Expression rotation);

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public Mark getCircleMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public Mark getXMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public Mark getStarMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public Mark getSquareMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public Mark getCrossMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    public Mark getTriangleMark();

    /**
     * Creates a new extent.
     *
     * @param name The name of the extent.
     * @param value The value of the extent.
     * @return The new extent.
     */
    public Extent createExtent(String name, String value);

    /**
     * Creates a new feature type constraint.
     *
     * @param featureTypeName The feature type name.
     * @param filter The filter.
     * @param extents The extents.
     * @return The new feature type constaint.
     */
    public FeatureTypeConstraint createFeatureTypeConstraint(
            String featureTypeName, Filter filter, Extent[] extents);

    public LayerFeatureConstraints createLayerFeatureConstraints(
            FeatureTypeConstraint[] featureTypeConstraints);

    public FeatureTypeStyle createFeatureTypeStyle(Rule[] rules);

    /**
     * Creates a new ImageOutline.
     *
     * @param symbolizer A line or polygon symbolizer.
     * @return The new image outline.
     */
    public ImageOutline createImageOutline(Symbolizer symbolizer);

    public LinePlacement createLinePlacement(Expression offset);

    public PolygonSymbolizer createPolygonSymbolizer();

    public Halo createHalo(Fill fill, Expression radius);

    public Fill createFill(
            Expression color, Expression backgroundColor, Expression opacity, Graphic graphicFill);

    /** Create default line symbolizer */
    public LineSymbolizer createLineSymbolizer();

    public PointSymbolizer createPointSymbolizer(Graphic graphic, String geometryPropertyName);

    public Style createStyle();

    public NamedStyle createNamedStyle();

    public Fill createFill(Expression color, Expression opacity);

    public Fill createFill(Expression color);

    public TextSymbolizer createTextSymbolizer();

    public PointPlacement createPointPlacement(
            AnchorPoint anchorPoint, Displacement displacement, Expression rotation);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width the width of the line
     * @return the stroke object
     * @see org.geotools.stroke
     */
    public Stroke createStroke(Expression color, Expression width);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
     * @see org.geotools.stroke
     */
    public Stroke createStroke(Expression color, Expression width, Expression opacity);

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
    public Stroke createStroke(
            Expression color,
            Expression width,
            Expression opacity,
            Expression lineJoin,
            Expression lineCap,
            float[] dashArray,
            Expression dashOffset,
            Graphic graphicFill,
            Graphic graphicStroke);

    public Rule createRule();

    public LineSymbolizer createLineSymbolizer(Stroke stroke, String geometryPropertyName);

    public FeatureTypeStyle createFeatureTypeStyle();

    public Graphic createGraphic(
            ExternalGraphic[] externalGraphics,
            Mark[] marks,
            Symbol[] symbols,
            Expression opacity,
            Expression size,
            Expression rotation);

    public Font createFont(
            Expression fontFamily,
            Expression fontStyle,
            Expression fontWeight,
            Expression fontSize);

    public Mark createMark();

    public PolygonSymbolizer createPolygonSymbolizer(
            Stroke stroke, Fill fill, String geometryPropertyName);

    public RasterSymbolizer createRasterSymbolizer();

    public RasterSymbolizer createRasterSymbolizer(
            String geometryPropertyName,
            Expression opacity,
            ChannelSelection channel,
            Expression overlap,
            ColorMap colorMap,
            ContrastEnhancement ce,
            ShadedRelief relief,
            Symbolizer outline);

    public RasterSymbolizer getDefaultRasterSymbolizer();

    public ChannelSelection createChannelSelection(SelectedChannelType[] channels);

    public ContrastEnhancement createContrastEnhancement();

    public ContrastEnhancement createContrastEnhancement(Expression gammaValue);

    public SelectedChannelType createSelectedChannelType(
            Expression name, ContrastEnhancement enhancement);

    public SelectedChannelType createSelectedChannelType(
            String name, ContrastEnhancement enhancement);

    public SelectedChannelType createSelectedChannelType(Expression name, Expression gammaValue);

    public ColorMap createColorMap();

    public ColorMapEntry createColorMapEntry();

    public Style getDefaultStyle();

    public Stroke getDefaultStroke();

    public Fill getDefaultFill();

    public Mark getDefaultMark();

    public PointSymbolizer getDefaultPointSymbolizer();

    public PolygonSymbolizer getDefaultPolygonSymbolizer();

    public LineSymbolizer getDefaultLineSymbolizer();

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and
     * defaultPointPlacement, Sets the geometry attribute name to be geometry:text. No Halo is set.
     * <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    public TextSymbolizer getDefaultTextSymbolizer();

    public Graphic createDefaultGraphic();

    public Graphic getDefaultGraphic();

    public Font getDefaultFont();

    public PointPlacement getDefaultPointPlacement();

    public StyledLayerDescriptor createStyledLayerDescriptor();

    public UserLayer createUserLayer();

    public NamedLayer createNamedLayer();

    public RemoteOWS createRemoteOWS(String service, String onlineResource);

    public ShadedRelief createShadedRelief(Expression reliefFactor);

    //
    // Type Narrow org.opengis.StyleFactory
    //
    /** Indicate what part of a Graphic is used to mark the location. */
    AnchorPoint anchorPoint(Expression x, Expression y);
    /** */
    ChannelSelection channelSelection(org.opengis.style.SelectedChannelType gray);
    /** */
    ChannelSelection channelSelection(
            org.opengis.style.SelectedChannelType red,
            org.opengis.style.SelectedChannelType green,
            org.opengis.style.SelectedChannelType blue);

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
    ColorMap colorMap(Expression propertyName, Expression... mapping);

    /**
     * Wrap up a replacement function using the provided expressions.
     *
     * @param propertyName Property name to categorize, or use "Raster"
     * @param mapping Defined as a series of Expressions
     * @return ColorReplacement wrapped around a Function
     */
    ColorReplacement colorReplacement(Expression propertyName, Expression... mapping);
    /** */
    ContrastEnhancement contrastEnhancement(
            Expression gamma, org.opengis.style.ContrastMethod method);

    /** */
    Description description(InternationalString title, InternationalString description);

    /** */
    Displacement displacement(Expression dx, Expression dy);

    /** */
    ExternalGraphic externalGraphic(
            OnLineResource resource,
            String format,
            Collection<org.opengis.style.ColorReplacement> replacements);

    /** */
    ExternalGraphic externalGraphic(
            Icon inline, Collection<org.opengis.style.ColorReplacement> replacements);

    /** */
    ExternalMark externalMark(OnLineResource resource, String format, int markIndex);

    /** */
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
    FeatureTypeStyle featureTypeStyle(
            String name,
            org.opengis.style.Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<org.opengis.style.SemanticType> types,
            List<org.opengis.style.Rule> rules);

    /** */
    Fill fill(org.opengis.style.GraphicFill fill, Expression color, Expression opacity);

    /** */
    Font font(List<Expression> family, Expression style, Expression weight, Expression size);

    Graphic graphic(
            List<org.opengis.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.opengis.style.AnchorPoint anchor,
            org.opengis.style.Displacement disp);

    /** */
    Graphic graphicFill(
            List<org.opengis.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement);

    /** */
    GraphicLegend graphicLegend(
            List<org.opengis.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement);
    /** */
    Graphic graphicStroke(
            List<org.opengis.style.GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            org.opengis.style.AnchorPoint anchorPoint,
            org.opengis.style.Displacement displacement,
            Expression initialGap,
            Expression gap);

    /** */
    Halo halo(org.opengis.style.Fill fill, Expression radius);

    /** */
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
    LineSymbolizer lineSymbolizer(
            String name,
            Expression geometry,
            org.opengis.style.Description description,
            Unit<?> unit,
            org.opengis.style.Stroke stroke,
            Expression offset);

    /** */
    Mark mark(
            Expression wellKnownName, org.opengis.style.Fill fill, org.opengis.style.Stroke stroke);
    /** */
    Mark mark(
            org.opengis.style.ExternalMark externalMark,
            org.opengis.style.Fill fill,
            org.opengis.style.Stroke stroke);
    /** */
    PointPlacement pointPlacement(
            org.opengis.style.AnchorPoint anchor,
            org.opengis.style.Displacement displacement,
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
    PointSymbolizer pointSymbolizer(
            String name,
            Expression geometry,
            org.opengis.style.Description description,
            Unit<?> unit,
            org.opengis.style.Graphic graphic);
    /**
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer
     * @param unit Unit of Measure used to interpret symbolizer distances
     */
    PolygonSymbolizer polygonSymbolizer(
            String name,
            Expression geometry,
            org.opengis.style.Description description,
            Unit<?> unit,
            org.opengis.style.Stroke stroke,
            org.opengis.style.Fill fill,
            org.opengis.style.Displacement displacement,
            Expression offset);
    /**
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer
     * @param unit Unit of Measure used to interpret symbolizer distances
     * @return RasterSymbolizer
     */
    RasterSymbolizer rasterSymbolizer(
            String name,
            Expression geometry,
            org.opengis.style.Description description,
            Unit<?> unit,
            Expression opacity,
            org.opengis.style.ChannelSelection channelSelection,
            org.opengis.style.OverlapBehavior overlapsBehaviour,
            org.opengis.style.ColorMap colorMap,
            org.opengis.style.ContrastEnhancement contrast,
            org.opengis.style.ShadedRelief shaded,
            org.opengis.style.Symbolizer outline);
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
    ExtensionSymbolizer extensionSymbolizer(
            String name,
            String geometry,
            org.opengis.style.Description description,
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
    Rule rule(
            String name,
            org.opengis.style.Description description,
            org.opengis.style.GraphicLegend legend,
            double min,
            double max,
            List<org.opengis.style.Symbolizer> symbolizers,
            Filter filter);

    /** @return SelectedChannelType */
    SelectedChannelType selectedChannelType(
            Expression channelName, org.opengis.style.ContrastEnhancement contrastEnhancement);

    /** @return SelectedChannelType */
    SelectedChannelType selectedChannelType(
            String channelName, org.opengis.style.ContrastEnhancement contrastEnhancement);

    /** @return ShadedRelief */
    ShadedRelief shadedRelief(Expression reliefFactor, boolean brightnessOnly);

    Stroke stroke(
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    Stroke stroke(
            org.opengis.style.GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    Stroke stroke(
            org.opengis.style.GraphicStroke stroke,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    /** */
    Style style(
            String name,
            org.opengis.style.Description description,
            boolean isDefault,
            List<org.opengis.style.FeatureTypeStyle> featureTypeStyles,
            org.opengis.style.Symbolizer defaultSymbolizer);
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
    TextSymbolizer textSymbolizer(
            String name,
            Expression geometry,
            org.opengis.style.Description description,
            Unit<?> unit,
            Expression label,
            org.opengis.style.Font font,
            org.opengis.style.LabelPlacement placement,
            org.opengis.style.Halo halo,
            org.opengis.style.Fill fill);

    /** @return a deep copy of the method */
    public ContrastMethod createContrastMethod(ContrastMethod method);
}
