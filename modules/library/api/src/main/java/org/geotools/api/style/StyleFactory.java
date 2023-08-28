/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import java.awt.*;
import java.net.URL;
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
import org.geotools.api.util.InternationalString;

/**
 * Factory used in the production of style objects.
 *
 * <p>This factory is responsible for the production of style objects; where noted these create
 * methods are in agreement with the Symbology Encoding 1.1 specification.
 *
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface StyleFactory {
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
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    Mark getCircleMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    Mark getXMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    Mark getStarMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    Mark getSquareMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    Mark getCrossMark();

    /**
     * Convinence method for obtaining a mark of a fixed shape
     *
     * @return a Mark that matches the name in this method.
     */
    Mark getTriangleMark();

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
     */
    Stroke createStroke(Expression color, Expression width);

    /**
     * A convienice method to make a simple stroke
     *
     * @param color the color of the line
     * @param width The width of the line
     * @param opacity The opacity of the line
     * @return The stroke
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

    RasterSymbolizer getDefaultRasterSymbolizer();

    ChannelSelection createChannelSelection(SelectedChannelType... channels);

    ContrastEnhancement createContrastEnhancement();

    ContrastEnhancement createContrastEnhancement(Expression gammaValue);

    SelectedChannelType createSelectedChannelType(Expression name, ContrastEnhancement enhancement);

    SelectedChannelType createSelectedChannelType(String name, ContrastEnhancement enhancement);

    SelectedChannelType createSelectedChannelType(Expression name, Expression gammaValue);

    ColorMap createColorMap();

    ColorMapEntry createColorMapEntry();

    Style getDefaultStyle();

    Stroke getDefaultStroke();

    Fill getDefaultFill();

    Mark getDefaultMark();

    PointSymbolizer getDefaultPointSymbolizer();

    PolygonSymbolizer getDefaultPolygonSymbolizer();

    LineSymbolizer getDefaultLineSymbolizer();

    /**
     * Creates a default Text Symbolizer, using the defaultFill, defaultFont and
     * defaultPointPlacement, Sets the geometry attribute name to be geometry:text. No Halo is set.
     * <b>The label is not set</b>
     *
     * @return A default TextSymbolizer
     */
    TextSymbolizer getDefaultTextSymbolizer();

    Graphic createDefaultGraphic();

    Graphic getDefaultGraphic();

    Font getDefaultFont();

    PointPlacement getDefaultPointPlacement();

    StyledLayerDescriptor createStyledLayerDescriptor();

    UserLayer createUserLayer();

    NamedLayer createNamedLayer();

    RemoteOWS createRemoteOWS(String service, String onlineResource);

    ShadedRelief createShadedRelief(Expression reliefFactor);

    /** */
    AnchorPoint anchorPoint(Expression x, Expression y);
    /** */
    ChannelSelection channelSelection(SelectedChannelType gray);
    /** */
    ChannelSelection channelSelection(
            SelectedChannelType red, SelectedChannelType green, SelectedChannelType blue);

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
    ContrastEnhancement contrastEnhancement(Expression gamma, ContrastMethod method);
    /** */
    ContrastEnhancement contrastEnhancement(Expression gamma, String method);

    /** */
    Description description(InternationalString title, InternationalString description);

    /** Create Displacement */
    Displacement displacement(Expression dx, Expression dy);

    /** Create externalGraphic */
    ExternalGraphic externalGraphic(
            OnLineResource resource, String format, Collection<ColorReplacement> replacements);

    /**
     * Create ExternalGraphic using a Java Icon.
     *
     * <p>This is used to produce high quality output by allowing you to directly draw each symbol
     * by supplying your own Icon implementation.
     */
    ExternalGraphic externalGraphic(Icon inline, Collection<ColorReplacement> replacements);

    /** */
    ExternalMark externalMark(OnLineResource resource, String format, int markIndex);

    /** */
    ExternalMark externalMark(Icon inline);

    /** @param rules May not be null or empty */
    FeatureTypeStyle featureTypeStyle(
            String name,
            Description description,
            Id definedFor,
            Set<Name> featureTypeNames,
            Set<SemanticType> types,
            List<Rule> rules);

    /** Create fill. */
    Fill fill(GraphicFill fill, Expression color, Expression opacity);

    /**
     * Create font entry; note this captures a list of font families in the preferred order, with
     * the rendering engine choosing the first entry in the list available to the runtime
     * environment.
     *
     * <p>If fonts are not showing up as you expect please review the list of fonts installed into
     * your JRE.
     *
     * @return Font
     */
    Font font(List<Expression> family, Expression style, Expression weight, Expression size);
    /** Create a graphic. */
    Graphic graphic(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            AnchorPoint anchor,
            Displacement disp);

    /** */
    GraphicFill graphicFill(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            AnchorPoint anchorPoint,
            Displacement displacement);

    /** */
    GraphicLegend graphicLegend(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            AnchorPoint anchorPoint,
            Displacement displacement);
    /** */
    GraphicStroke graphicStroke(
            List<GraphicalSymbol> symbols,
            Expression opacity,
            Expression size,
            Expression rotation,
            AnchorPoint anchorPoint,
            Displacement displacement,
            Expression initialGap,
            Expression gap);

    /** */
    Halo halo(Fill fill, Expression radius);

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
            Description description,
            Unit<?> unit,
            Stroke stroke,
            Expression offset);

    /** */
    Mark mark(Expression wellKnownName, Fill fill, Stroke stroke);
    /** */
    Mark mark(ExternalMark externalMark, Fill fill, Stroke stroke);
    /** */
    PointPlacement pointPlacement(
            AnchorPoint anchor, Displacement displacement, Expression rotation);
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
            Description description,
            Unit<?> unit,
            Graphic graphic);
    /**
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer
     * @param unit Unit of Measure used to interpret symbolizer distances
     */
    PolygonSymbolizer polygonSymbolizer(
            String name,
            Expression geometry,
            Description description,
            Unit<?> unit,
            Stroke stroke,
            Fill fill,
            Displacement displacement,
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
            Description description,
            Unit<?> unit,
            Expression opacity,
            ChannelSelection channelSelection,
            OverlapBehaviorEnum overlapsBehaviour,
            ColorMap colorMap,
            ContrastEnhancement contrast,
            ShadedRelief shaded,
            Symbolizer outline);
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
            Description description,
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
            Description description,
            GraphicLegend legend,
            double min,
            double max,
            List<Symbolizer> symbolizers,
            Filter filter);

    /** @return SelectedChannelType */
    SelectedChannelType selectedChannelType(
            Expression channelName, ContrastEnhancement contrastEnhancement);

    /** @return SelectedChannelType */
    SelectedChannelType selectedChannelType(
            String channelName, org.geotools.api.style.ContrastEnhancement contrastEnhancement);

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
            GraphicFill fill,
            Expression color,
            Expression opacity,
            Expression width,
            Expression join,
            Expression cap,
            float[] dashes,
            Expression offset);

    Stroke stroke(
            GraphicStroke stroke,
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
            Description description,
            boolean isDefault,
            List<FeatureTypeStyle> featureTypeStyles,
            Symbolizer defaultSymbolizer);
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
            Description description,
            Unit<?> unit,
            Expression label,
            Font font,
            LabelPlacement placement,
            Halo halo,
            Fill fill);

    /** @return a deep copy of the method */
    ContrastMethod createContrastMethod(ContrastMethod method);

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
    TextSymbolizer createTextSymbolizer(
            Fill fill,
            Font[] fonts,
            Halo halo,
            Expression label,
            LabelPlacement labelPlacement,
            String geometryPropertyName,
            Graphic graphic);

    Map<RenderingHints.Key, ?> getImplementationHints();
}
