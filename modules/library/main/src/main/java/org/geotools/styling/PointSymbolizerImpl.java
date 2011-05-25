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


import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.util.SimpleInternationalString;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * Provides a Java representation of the PointSymbolizer. This defines how
 * points are to be rendered.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 *
 * @source $URL$
 * @version $Id$
 */
public class PointSymbolizerImpl extends AbstractSymbolizer implements PointSymbolizer, Cloneable {
    
    private GraphicImpl graphic = new GraphicImpl();

    /**
     * Creates a new instance of DefaultPointSymbolizer
     */
    protected PointSymbolizerImpl() {
        this(new GraphicImpl(), 
                null,
                null,
                null,
                new DescriptionImpl(
                    new SimpleInternationalString("title"), 
                    new SimpleInternationalString("abstract")));
    }

    protected PointSymbolizerImpl(Graphic graphic, Unit<Length> uom, String geom, String name, Description desc){
        super(name, desc, geom, uom);
        this.graphic = GraphicImpl.cast(graphic);
    }

    /**
     * Provides the graphical-symbolization parameter to use for the point
     * geometry.
     *
     * @return The Graphic to be used when drawing a point
     */
    public GraphicImpl getGraphic() {
        return graphic;
    }

    /**
     * Setter for property graphic.
     *
     * @param graphic New value of property graphic.
     */
    public void setGraphic(org.opengis.style.Graphic graphic) {
        if (this.graphic == graphic) {
            return;
        }
        this.graphic = GraphicImpl.cast( graphic );
    }

    /**
     * Accept a StyleVisitor to perform an operation on this symbolizer.
     *
     * @param visitor The StyleVisitor to accept.
     */
    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }
    
    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a deep copy clone.
     *
     * @return The deep copy clone.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public Object clone() {
        PointSymbolizerImpl clone;

        try {
            clone = (PointSymbolizerImpl) super.clone();
            if(graphic != null) clone.graphic = (GraphicImpl) ((Cloneable) graphic).clone();
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
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PointSymbolizerImpl other = (PointSymbolizerImpl) obj;
        if (graphic == null) {
            if (other.graphic != null)
                return false;
        } else if (!graphic.equals(other.graphic))
            return false;
        return true;
    }

    static PointSymbolizerImpl cast(org.opengis.style.Symbolizer symbolizer) {
        if (symbolizer == null) {
            return null;
        } else if (symbolizer instanceof PointSymbolizerImpl) {
            return (PointSymbolizerImpl) symbolizer;
        } else if (symbolizer instanceof org.opengis.style.PointSymbolizer) {
            org.opengis.style.PointSymbolizer pointSymbolizer = (org.opengis.style.PointSymbolizer) symbolizer;
            PointSymbolizerImpl copy = new PointSymbolizerImpl();
            copy.setDescription( pointSymbolizer.getDescription() );
            copy.setGeometryPropertyName( pointSymbolizer.getGeometryPropertyName() );
            copy.setGraphic( pointSymbolizer.getGraphic());
            copy.setName(pointSymbolizer.getName());
            copy.setUnitOfMeasure(pointSymbolizer.getUnitOfMeasure());
            return copy;
        }
        return null; // not a PointSymbolizer
    }

}
