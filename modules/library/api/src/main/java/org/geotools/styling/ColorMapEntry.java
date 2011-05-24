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

import org.opengis.filter.expression.Expression;


/**
 * A basic interface for objects which can hold color map entries.
 * <pre>
 *  &lt;xs:element name="ColorMapEntry"&gt;
 *  &lt;xs:complexType&gt;
 *  &lt;xs:attribute name="color" type="xs:string" use="required"/&gt;
 *  &lt;xs:attribute name="opacity" type="xs:double"/&gt;
 *  &lt;xs:attribute name="quantity" type="xs:double"/&gt;
 *  &lt;xs:attribute name="label" type="xs:string"/&gt;
 *  &lt;/xs:complexType&gt;
 *  &lt;/xs:element&gt;
 *  </pre>
 *
 * @source $URL$
 */
public interface ColorMapEntry {
    /** Label for this Color Map Entry */
    String getLabel();
    
    /**
     * @param label
     */
    void setLabel(String label);

    /**
     * Expression resulting in a color
     * @param color
     */
    void setColor(Expression color);

    /**
     * @return Expression evaualted into a color
     */
    Expression getColor();

    /**
     * @param opacity Expressed as a value between 0 and 1
     */
    void setOpacity(Expression opacity);
    /**
     * 
     * @return Opacity expressed as a value between 0 and 1
     */
    Expression getOpacity();

    /**
     * Quantity marking the start of this color map entry.
     * 
     * @param quantity
     */
    void setQuantity(Expression quantity);
    
    /**
     * @return Quanity marking the start of this color map entry
     */
    Expression getQuantity();

    void accept(org.geotools.styling.StyleVisitor visitor);
}
