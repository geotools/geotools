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

import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Description;
import org.geotools.api.style.LineSymbolizer;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;

/**
 * Provides a representation of a LineSymbolizer in an SLD Document. A LineSymbolizer defines how a line geometry should
 * be rendered.
 *
 * @author James Macgill
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class LineSymbolizerImpl extends AbstractSymbolizer implements LineSymbolizer, Cloneable {

    private Expression offset;

    private StrokeImpl stroke = null;

    /** Creates a new instance of DefaultLineSymbolizer */
    protected LineSymbolizerImpl() {
        this(null, null, null, null, null, null);
    }

    protected LineSymbolizerImpl(
            Stroke stroke, Expression offset, Unit<Length> uom, String geom, String name, Description desc) {
        super(name, desc, geom, uom);
    }

    @Override
    public Expression getPerpendicularOffset() {
        return offset;
    }

    @Override
    public void setPerpendicularOffset(Expression offset) {
        this.offset = offset;
    }

    /**
     * Provides the graphical-symbolization parameter to use for the linear geometry.
     *
     * @return The Stroke style to use when rendering lines.
     */
    @Override
    public StrokeImpl getStroke() {
        return stroke;
    }

    /**
     * Sets the graphical-symbolization parameter to use for the linear geometry.
     *
     * @param stroke The Stroke style to use when rendering lines.
     */
    @Override
    public void setStroke(org.geotools.api.style.Stroke stroke) {
        if (this.stroke == stroke) {
            return;
        }
        this.stroke = StrokeImpl.cast(stroke);
    }

    /**
     * Accepts a StyleVisitor to perform some operation on this LineSymbolizer.
     *
     * @param visitor The visitor to accept.
     */
    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a deep copy clone.
     *
     * @return The deep copy clone.
     */
    @Override
    public Object clone() {
        LineSymbolizerImpl clone;

        try {
            clone = (LineSymbolizerImpl) super.clone();

            if (stroke != null && stroke instanceof Cloneable) {
                clone.stroke = (StrokeImpl) stroke.clone();
            }

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen.
        }

        return clone;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<LineSymbolizerImp property=");
        buf.append(getGeometryPropertyName());
        buf.append(" uom=");
        buf.append(unitOfMeasure);
        buf.append(" stroke=");
        buf.append(stroke);
        buf.append(">");
        return buf.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (offset == null ? 0 : offset.hashCode());
        result = prime * result + (stroke == null ? 0 : stroke.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        LineSymbolizerImpl other = (LineSymbolizerImpl) obj;
        if (offset == null) {
            if (other.offset != null) return false;
        } else if (!offset.equals(other.offset)) return false;
        if (stroke == null) {
            if (other.stroke != null) return false;
        } else if (!stroke.equals(other.stroke)) return false;
        return true;
    }

    static LineSymbolizerImpl cast(org.geotools.api.style.Symbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        }
        if (symbolizer instanceof LineSymbolizerImpl) {
            return (LineSymbolizerImpl) symbolizer;
        } else if (symbolizer instanceof org.geotools.api.style.LineSymbolizer) {
            org.geotools.api.style.LineSymbolizer lineSymbolizer = (org.geotools.api.style.LineSymbolizer) symbolizer;
            LineSymbolizerImpl copy = new LineSymbolizerImpl();
            copy.setDescription(lineSymbolizer.getDescription());
            copy.setGeometryPropertyName(lineSymbolizer.getGeometryPropertyName());
            copy.setName(lineSymbolizer.getName());
            copy.setPerpendicularOffset(lineSymbolizer.getPerpendicularOffset());
            copy.setStroke(lineSymbolizer.getStroke());
            copy.setUnitOfMeasure(lineSymbolizer.getUnitOfMeasure());
            return copy;
        }
        return null; // not a line symbolizer
    }
}
