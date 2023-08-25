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
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.util.SimpleInternationalString;

/**
 * Provides a Java representation of the PointSymbolizer. This defines how points are to be
 * rendered.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class PointSymbolizer extends AbstractSymbolizer
        implements Cloneable, org.geotools.api.style.PointSymbolizer, Symbolizer {

    /**
     * Boolean vendor option, defaults to true. If true, in case no specified mark or graphics can
     * be used, the default square mark will be used instead. If false, the symbol will not be
     * painted.
     */
    public static final String FALLBACK_ON_DEFAULT_MARK = "fallbackOnDefaultMark";

    private Graphic graphic = new Graphic();

    /** Creates a new instance of DefaultPointSymbolizer */
    protected PointSymbolizer() {
        this(
                new Graphic(),
                null,
                null,
                null,
                new Description(
                        new SimpleInternationalString("title"),
                        new SimpleInternationalString("abstract")));
    }

    protected PointSymbolizer(
            Graphic graphic, Unit<Length> uom, String geom, String name, Description desc) {
        super(name, desc, geom, uom);
        this.graphic = Graphic.cast(graphic);
    }

    /**
     * Provides the graphical-symbolization parameter to use for the point geometry.
     *
     * @return The Graphic to be used when drawing a point
     */
    @Override
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * Setter for property graphic.
     *
     * @param graphic New value of property graphic.
     */
    public void setGraphic(org.geotools.api.style.Graphic graphic) {
        if (this.graphic == graphic) {
            return;
        }
        this.graphic = Graphic.cast(graphic);
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
        PointSymbolizer clone;

        try {
            clone = (PointSymbolizer) super.clone();
            if (graphic != null) clone.graphic = (Graphic) ((Cloneable) graphic).clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen.
        }

        return clone;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((graphic == null) ? 0 : graphic.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        PointSymbolizer other = (PointSymbolizer) obj;
        if (graphic == null) {
            if (other.graphic != null) return false;
        } else if (!graphic.equals(other.graphic)) return false;
        return true;
    }

    static PointSymbolizer cast(org.geotools.api.style.Symbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        } else if (symbolizer instanceof PointSymbolizer) {
            return (PointSymbolizer) symbolizer;
        } else if (symbolizer instanceof org.geotools.api.style.PointSymbolizer) {
            org.geotools.api.style.PointSymbolizer pointSymbolizer =
                    (org.geotools.api.style.PointSymbolizer) symbolizer;
            PointSymbolizer copy = new PointSymbolizer();
            copy.setDescription(pointSymbolizer.getDescription());
            copy.setGeometryPropertyName(pointSymbolizer.getGeometryPropertyName());
            copy.setGraphic(pointSymbolizer.getGraphic());
            copy.setName(pointSymbolizer.getName());
            copy.setUnitOfMeasure(pointSymbolizer.getUnitOfMeasure());
            return copy;
        }
        return null; // not a PointSymbolizer
    }
}
