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

import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.PropertyName;

/**
 * Abstract superclass of the symbolizers defined by the Symbology Encoding specification.
 *
 * <p>Please note you are not free to create your own subtype o Symbolizer - we are limited to LineSymbolizer,
 * PointSymbolizer, PolygonSymbolizer, RasterSymbolizer and TextSymbolizer.
 *
 * <p>Geometry types other than inherently linear types can also be used. If a point geometry is used, it should be
 * interpreted as a line of "epsilon" (arbitrarily small) length with a horizontal orientation centered on the point,
 * and should be rendered with two end caps. If a polygon is used (or other "area" type), then its closed outline is
 * used as the line string (with no end caps). If a raster geometry is used, its coverage-area outline is used for the
 * line, rendered with no end caps.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding Implementation Specification
 *     1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Symbolizer {

    /**
     * Returns a measure unit. This parameter is inherited from GML. Renderers shall use the unit to correctly render
     * symbols.
     *
     * <p>Recommended uom definitions are :
     *
     * <p>
     *
     * <ul>
     *   <li>{@code metre}
     *   <li>{@code foot}
     *   <li>{@code pixel}
     * </ul>
     *
     * <p>
     *
     * @return can be null. If the unit is null than we shall use a the pixel unit
     */
    Unit<Length> getUnitOfMeasure();

    /**
     * Tile and Abstract of Symbolzer.
     *
     * @since SymbologyEncoding 1.1
     */
    void setDescription(Description description);

    /**
     * Name of symbolizer; not always human readable.
     *
     * <p>Please consider getDescription().getTitle() as an alternative if presenting this symbolizer in a user
     * interface.
     *
     * @since SymbologyEncoding 1.1
     */
    void setName(String name);

    /**
     * Returns the name of the geometry feature attribute to use for drawing. May return null (or Expression.NIL) if
     * this symbol is to use the default geometry attribute, whatever it may be. Using null in this fashion is similar
     * to a PropertyName using the XPath expression ".".
     *
     * <p>The content of the element gives the property name in XPath syntax. In principle, a fixed geometry could be
     * defined using GML or operators could be defined for computing the geometry from references or literals. However,
     * using a feature property directly is by far the most commonly useful method.
     *
     * @return Geometry attribute name, or <code>null</code> to indicate default geometry
     */
    String getGeometryPropertyName();

    /**
     * Exrepssion used to define a geometry for drawing. May return null if the default geometry attribute should be
     * used. This expression is often a PropertyName.
     *
     * @return Expression used to define a geometry for drawing, or Expression.NIL if the default geometry should be
     *     used.
     */
    /* Expression getGeometry(); */

    /**
     * Returns a name for this symbolizer. This can be any string that uniquely identifies this style within a given
     * canvas. It is not meant to be human-friendly. (The "title" property is meant to be human friendly.)
     *
     * @return a name for this style.
     */
    String getName();

    void accept(StyleVisitor visitor);

    /**
     * Defines a measure unit for the symbolizer. This parameter is inherited from GML. Renderers shall use the unit to
     * correctly render symbols.
     *
     * <p>Recommended uom definitions are :
     *
     * <p>
     *
     * <ul>
     *   <li>{@code metre}
     *   <li>{@code foot}
     *   <li>{@code pixel}
     * </ul>
     *
     * <p>
     *
     * @since SymbologyEncoding 1.1
     * @param uom can be null, which indicates usage of the pixel unit.
     */
    void setUnitOfMeasure(Unit<Length> uom);

    /**
     * Returns the description of this symbolizer.
     *
     * @return Description with usual informations used for user interfaces.
     */
    Description getDescription();

    /**
     * Calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     * @return value produced
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * A shortcut to define the geometry expression as a {@link PropertyName} Typically, features only have one geometry
     * so, in general, the need to select one is not required. Note: this moves a little away from the SLD spec which
     * provides an XPath reference to a Geometry object, but does follow it in spirit.
     */
    void setGeometryPropertyName(String geometryPropertyName);

    /**
     * This defines the geometry to be used for styling.<br>
     * The property is optional and if it is absent (null) then the "default" geometry property of the feature should be
     * used.<br>
     * Typically, features only have one geometry so, in general, the need to select one is not required.<br>
     * The expression can also build a new geometry out of existing attributes or transform an existing geometry. For
     * geometry transformations that do change the geometry locations or that make up geometries out of non geometric
     * attributes it is advised that the Expression implements the SpatialTransformationFunction interface
     */
    Expression getGeometry();

    /** Sets the expression used for styling. See {@link #getGeometry()} for further details. */
    void setGeometry(Expression geometry);

    /** Determines if a vendor option with the specific key has been set on this symbolizer. */
    boolean hasOption(String key);

    /**
     * Map of vendor options for the symbolizer.
     *
     * <p>Client code looking for the existence of a single option should use {@link #hasOption(String)}
     */
    Map<String, String> getOptions();
}
