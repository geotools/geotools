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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.measure.unit.Unit;
import javax.swing.Icon;

import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.util.InternationalString;

/**
 * Factory used in the production of style objects.
 * <p>
 * This factory is responsible for the production of style objects; where noted
 * these create methods are in agreement with the Symbology Encoding 1.1
 * specification.
 * 
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @since GeoAPI 2.2
 */
public interface StyleFactory {
    /**
     * 
     * @param x
     * @param y
     * @return
     */
    AnchorPoint anchorPoint(Expression x, Expression y);
    /**
     * 
     * @param gray
     * @return
     */
    ChannelSelection channelSelection(SelectedChannelType gray);
    /**
     * 
     * @param red
     * @param green
     * @param blue
     * @return
     */
    ChannelSelection channelSelection(SelectedChannelType red,
            SelectedChannelType green, SelectedChannelType blue);

    /**
     * Wrap up a "Categorize" function using the provided expressions.
     * <p>
     * The function will be created based on:
     * <ol>
     * <li>PropertyName; use "Rasterdata" to indicate this is a color map
     * <li>Literal: lookup value
     * <li>Literal: threshold 1
     * <li>Literal: value 1
     * <li>Literal: threshold 2
     * <li>Literal: value 2
     * <li>Literal: (Optional) succeeding or preceding
     * </ol>
     * @param propertyName
     *            Property name to categorize, or use "Raster"
     * @param mapping
     *            Defined as a series of Expressions
     * @return ColorMap wrapped around the "Cateogize" function
     */
    ColorMap colorMap(Expression propertyName, Expression... mapping);

    /**
     * Wrap up a replacement function using the provided expressions.
     * 
     * @param propertyName
     *            Property name to categorize, or use "Raster"
     * @param mapping
     *            Defined as a series of Expressions
     * @return ColorReplacement wrapped around a Function
     */
    ColorReplacement colorReplacement(Expression propertyName,
            Expression... mapping);
    /**
     * 
     * @param gamma
     * @param method
     * @return
     */
    ContrastEnhancement contrastEnhancement(Expression gamma,
            ContrastMethod method);

    /**
     * 
     * @param title
     * @param description
     * @return
     */
    Description description(InternationalString title,
            InternationalString description);

    /**
     * 
     * @param dx
     * @param dy
     * @return
     */
    Displacement displacement(Expression dx, Expression dy);

    /**
     * 
     * @param resource
     * @param format
     * @param replacements
     * @return
     */
    ExternalGraphic externalGraphic(OnLineResource resource,
            String format, Collection<ColorReplacement> replacements);

    /**
     * 
     * @param inline
     * @param replacements
     * @return
     */
    ExternalGraphic externalGraphic(Icon inline,
            Collection<ColorReplacement> replacements);

    /**
     * 
     * @param resource
     * @param format
     * @param markIndex
     * @return
     */
    ExternalMark externalMark(OnLineResource resource, String format,
            int markIndex);

    /**
     * 
     * @param inline
     * @return
     */
    ExternalMark externalMark(Icon inline);

    /**
     * 
     * @param name
     * @param description
     * @param definedFor
     * @param featureTypeNames
     * @param types
     * @param rules
     *            May not be null or empty
     * @return
     */
    FeatureTypeStyle featureTypeStyle(String name,
            Description description, Id definedFor, Set<Name> featureTypeNames,
            Set<SemanticType> types, List<Rule> rules);

    /**
     * 
     * @param fill
     * @param color
     * @param opacity
     * @return
     */
    Fill fill(GraphicFill fill, Expression color, Expression opacity);

    /**
     * 
     * @param family
     * @param style
     * @param weight
     * @param size
     * @return
     */
    Font font(List<Expression> family, Expression style,
            Expression weight, Expression size);

    Graphic graphic(List<GraphicalSymbol> symbols,
            Expression opacity, Expression size, Expression rotation,
            AnchorPoint anchor, Displacement disp);

    /**
     * 
     * @param symbols
     * @param opacity
     * @param size
     * @param anchorPoint
     * @param displacement
     * @return
     */
    GraphicFill graphicFill(List<GraphicalSymbol> symbols,
            Expression opacity, Expression size, Expression rotation,
            AnchorPoint anchorPoint, Displacement displacement);

    /**
     * 
     * @param symbols
     * @param opacity
     * @param size
     * @param anchorPoint
     * @param displacement
     * @return
     */
    GraphicLegend graphicLegend(List<GraphicalSymbol> symbols,
            Expression opacity, Expression size, Expression rotation,
            AnchorPoint anchorPoint, Displacement displacement);
    /**
     * 
     * @param symbols
     * @param opacity
     * @param size
     * @param anchorPoint
     * @param displacement
     * @param initialGap
     * @param gap
     * @return
     */
    GraphicStroke graphicStroke(List<GraphicalSymbol> symbols,
            Expression opacity, Expression size, Expression rotation,
            AnchorPoint anchorPoint, Displacement displacement,
            Expression initialGap, Expression gap);

    /**
     * 
     * @param fill
     * @param radius
     * @return
     */
    Halo halo(Fill fill, Expression radius);

    /**
     * 
     * @param offset
     * @param initialGap
     * @param gap
     * @param repeated
     * @param aligned
     * @param generalizedLine
     * @return
     */
    LinePlacement linePlacement(Expression offset, Expression initialGap,
            Expression gap, boolean repeated, boolean aligned,
            boolean generalizedLine);
    /**
     * @param name handle used to refer to this symbolizer (machine readible)
     * @param geometry Expression used to produce the Geometry to renderer; often a PropertyName
     * @param description
     * @param unit Unit of measure used to define this symbolizer
     * @param stroke Definition of how to stroke linework
     * @param offset Offset used to position line relative to origional
     * @return Newly created Line Symbolizer
     */
    LineSymbolizer lineSymbolizer(String name, Expression geometry, 
            Description description, Unit<?> unit, Stroke stroke, Expression offset);

    /**
     * 
     * @param wellKnownName
     * @param fill
     * @param stroke
     * @return
     */
    Mark mark(Expression wellKnownName, Fill fill, Stroke stroke);
    /**
     * 
     * @param externalMark
     * @param fill
     * @param stroke
     * @return
     */
    Mark mark(ExternalMark externalMark, Fill fill, Stroke stroke);
    /**
     * 
     * @param anchor
     * @param displacement
     * @param rotation
     * @return
     */
    PointPlacement pointPlacement(AnchorPoint anchor,
            Displacement displacement, Expression rotation);
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
    PointSymbolizer pointSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Graphic graphic);
    /**
     * 
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer 
     * @param unit Unit of Measure used to interpret symbolizer distances
     * @param stroke
     * @param fill
     * @param displacement
     * @param offset
     * @return
     */
    PolygonSymbolizer polygonSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Stroke stroke, Fill fill,
            Displacement displacement, Expression offset);
    /**
     * 
     * @param name handle used to refer to this symbolizer (machine readable)
     * @param geometry Expression used to extract the Geometry rendered; usually a PropertyName
     * @param description Human readable description of symboizer 
     * @param unit Unit of Measure used to interpret symbolizer distances
     * @param opacity
     * @param channelSelection
     * @param overlapsBehaviour
     * @param colorMap
     * @param contrast
     * @param shaded
     * @param outline
     * @return RasterSymbolizer
     */
    RasterSymbolizer rasterSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Expression opacity,
            ChannelSelection channelSelection,
            OverlapBehavior overlapsBehaviour, ColorMap colorMap,
            ContrastEnhancement contrast, ShadedRelief shaded,
            Symbolizer outline);
    /**
     * Used to represent a symbolizer intended for a vendor specific rendering process. This
     * facility should be used to control subject matter that is beyond the scope of the traditional
     * symbology encoding data structure (subject matter like wind barbs or extra deegrees of
     * freedom like temporal symbolizers are good examples of the use of this facility).
     * 
     * @param name
     *            handle used to refer to this symbolizer (machine readible)
     * @param geometry
     *            Geometry expression to renderer; formally a PropertyName
     * @param description
     *            Description of this symbolizer; human readable
     * @param unit
     *            Unit of measure to use when interpretting this symbolizer
     * @param extensionName
     *            Extension name used to identify the vendor specific extension being controlled
     * @param parameters
     *            Named expressions used to configure the vendor specific rendering process
     * @return newly created ExtensionSymbolizer
     */
    ExtensionSymbolizer extensionSymbolizer(String name, String geometry, Description description,
            Unit<?> unit, String extensionName, Map<String, Expression> parameters);

    /**
     * Create a rule from the provided definition.
     * 
     * @param name handle used to refer to this rule (machine readable)
     * @param description Human readable description of this rule
     * @param legend Graphic used to indicate this rule in a legend or user interface
     * @param min minimum scale denominator used to control when this rule is applied
     * @param max maximum scale denominator used to control when this rule is applied
     * @param symbolizers
     * @param filter
     * @return Newly created Rule
     */
    Rule rule(String name, Description description, GraphicLegend legend,
            double min, double max, List<Symbolizer> symbolizers, Filter filter);

    /**
     * 
     * @param channelName
     * @param contrastEnhancement
     * @return SelectedChannelType
     */
    SelectedChannelType selectedChannelType(String channelName, ContrastEnhancement contrastEnhancement);
    /**
     * 
     * @param reliefFactor
     * @param brightnessOnly
     * @return ShadedRelief
     */
    ShadedRelief shadedRelief(Expression reliefFactor,
            boolean brightnessOnly);

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

    /**
     * 
     * @param name
     * @param description
     * @param isDefault
     * @param featureTypeStyles
     * @param defaultSymbolizer
     * @return
     */
    Style style(String name, Description description, boolean isDefault,
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
    TextSymbolizer textSymbolizer(String name, Expression geometry,
            Description description, Unit<?> unit, Expression label, Font font,
            LabelPlacement placement, Halo halo, Fill fill);
}
