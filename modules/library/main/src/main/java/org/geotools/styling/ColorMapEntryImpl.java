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
 * Default color map entry implementation
 *
 * @author aaime
 *
 * @source $URL$
 */
public class ColorMapEntryImpl implements ColorMapEntry {
    //private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");
    //private static final FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
    private Expression quantity;
    private Expression opacity;
    private Expression color;
    private String label;

    /**
     * @see org.geotools.styling.ColorMapEntry#getLabel()
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#setLabel(java.lang.String)
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#setColor(org.geotools.filter.Expression)
     */
    public void setColor(Expression color) {
        this.color = color;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#getColor()
     */
    public Expression getColor() {
        return this.color;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#setOpacity(org.geotools.filter.Expression)
     */
    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#getOpacity()
     */
    public Expression getOpacity() {
        return this.opacity;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#setQuantity(org.geotools.filter.Expression)
     */
    public void setQuantity(Expression quantity) {
        this.quantity = quantity;
    }

    /**
     * @see org.geotools.styling.ColorMapEntry#getQuantity()
     */
    public Expression getQuantity() {
        return quantity;
    }

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }
}
