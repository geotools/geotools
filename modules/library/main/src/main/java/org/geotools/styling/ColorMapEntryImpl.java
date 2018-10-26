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
 */
public class ColorMapEntryImpl implements ColorMapEntry {
    // private static final java.util.logging.Logger LOGGER =
    // org.geotools.util.logging.Logging.getLogger(ColorMapEntryImpl.class);
    // private static final FilterFactory filterFactory =
    // CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
    private Expression quantity;
    private Expression opacity;
    private Expression color;
    private String label;

    /** @see org.geotools.styling.ColorMapEntry#getLabel */
    public String getLabel() {
        return this.label;
    }

    /** @see org.geotools.styling.ColorMapEntry#setLabel */
    public void setLabel(String label) {
        this.label = label;
    }

    /** @see org.geotools.styling.ColorMapEntry#setColor */
    public void setColor(Expression color) {
        this.color = color;
    }

    /** @see org.geotools.styling.ColorMapEntry#getColor */
    public Expression getColor() {
        return this.color;
    }

    /** @see org.geotools.styling.ColorMapEntry#setOpacity */
    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }

    /** @see org.geotools.styling.ColorMapEntry#getOpacity() */
    public Expression getOpacity() {
        return this.opacity;
    }

    /** @see org.geotools.styling.ColorMapEntry#setQuantity */
    public void setQuantity(Expression quantity) {
        this.quantity = quantity;
    }

    /** @see org.geotools.styling.ColorMapEntry#getQuantity() */
    public Expression getQuantity() {
        return quantity;
    }

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((opacity == null) ? 0 : opacity.hashCode());
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ColorMapEntryImpl other = (ColorMapEntryImpl) obj;
        if (color == null) {
            if (other.color != null) return false;
        } else if (!color.equals(other.color)) return false;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        if (opacity == null) {
            if (other.opacity != null) return false;
        } else if (!opacity.equals(other.opacity)) return false;
        if (quantity == null) {
            if (other.quantity != null) return false;
        } else if (!quantity.equals(other.quantity)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ColorMapEntryImpl [quantity="
                + quantity
                + ", opacity="
                + opacity
                + ", color="
                + color
                + ", label="
                + label
                + "]";
    }
}
