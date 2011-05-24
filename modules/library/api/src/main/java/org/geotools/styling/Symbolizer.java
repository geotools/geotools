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
 *
 */
package org.geotools.styling;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;



/**
 * This is the parent interface of all Symbolizers.
 *
 * <p>
 * A symbolizer describes how a feature should appear on a map. The symbolizer
 * describes not just the shape that should appear but also  such graphical
 * properties as color and opacity.
 * </p>
 *
 * <p>
 * A symbolizer is obtained by specifying one of a small number of  different
 * types of symbolizer and then supplying parameters to overide its default
 * behaviour.
 * </p>
 *
 * <p>
 * The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188"> OGC
 * Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>
 * </p>
 *
 * <p>
 * Renderers can use this information when displaying styled features,  though
 * it must be remembered that not all renderers will be able to fully
 * represent strokes as set out by this interface.  For example, opacity may
 * not be supported.
 * </p>
 *
 * <p>
 * The graphical parameters and their values are derived from SVG/CSS2
 * standards with names and semantics which are as close as possible.
 * </p>
 *
 * <p></p>
 *
 * @author James Macgill, CCG
 *
 * @source $URL$
 * @version $Id$
 */
public interface Symbolizer extends org.opengis.style.Symbolizer{
    void accept(org.geotools.styling.StyleVisitor visitor);
    
    /**
     * Defines a measure unit for the symbolizer.
     * This parameter is inherited from GML.
     * Renderers shall use the unit to correctly render symbols.
     *
     * Recommended uom definitions are :
     * <p>
     * <ul>
     *     <li>{@code metre}</li>
     *     <li>{@code foot}</li>
     *     <li>{@code pixel}</li>
     * </ul>
     * <p>
     * @since SymbologyEncoding 1.1
     * @param uom can be null, which indicates usage of the pixel unit. 
     */
    void setUnitOfMeasure(Unit<Length> uom);
    
    public Description getDescription();
    
    /**
     * Tile and Abstract of Symbolzer.
     * 
     * @since SymbologyEncoding 1.1
     * @param description
     */
    void setDescription(org.opengis.style.Description description);    
    
    /**
     * Name of symbolizer; not always human readable.
     * <p>
     * Please consider getDescription().getTitle() as an alternative if
     * presenting this symbolizer in a user interface.
     * 
     * @since SymbologyEncoding 1.1
     * @param name
     */
    void setName( String name );    
    
    /**
     * A shortcut to get the geometry property name in the case the geometry
     * expression is a PropertyName. In case the geometry expression is null,
     * and in the case the geometry expression is not a PropertyName, this
     * method will return null.
     */
    String getGeometryPropertyName();

    /**
     * A shortcut to define the geometry expression as a {@link PropertyName}
     * Typically, features only have one geometry so, in general, the need to
     * select one is not required. Note: this moves a little away from the SLD
     * spec which provides an XPath reference to a Geometry object, but does
     * follow it in spirit.
     */
    void setGeometryPropertyName(String geometryPropertyName);
    
    /**
     * This defines the geometry to be used for styling.<br>
     * The property is optional and if it is absent (null) then the "default"
     * geometry property of the feature should be used.<br>
     * Typically, features only have one geometry so, in general, the need to
     * select one is not required.<br>
     * The expression can also build a new geometry out of existing attributes 
     * or transform an existing geometry. For geometry transformations that
     * do change the geometry locations or that make up geometries out of
     * non geometric attributes it is advised that the Expression implements
     * the SpatialTransformationFunction interface
     */
    Expression getGeometry();
    
    /**
     * Sets the expression used for styling. See {@link #getGeometry()} for further
     * details.
     * @param geometry
     */
    void setGeometry(Expression geometry);
    
}
