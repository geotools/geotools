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


// OpenGIS dependencies
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.util.Utilities;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * DOCUMENT ME!
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 * @version $Id$
 */
public class PointPlacementImpl implements PointPlacement, Cloneable {
    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");

    // TODO: make container ready
    private final FilterFactory filterFactory;
    private AnchorPointImpl anchorPoint = new AnchorPointImpl();
    private DisplacementImpl displacement = new DisplacementImpl();
    private Expression rotation = null;

    public PointPlacementImpl(){
        this( CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public PointPlacementImpl(FilterFactory factory) {
        filterFactory = factory;
        try {
            rotation = filterFactory.literal(new Integer(0));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultPointPlacement: " + ife);
        }
    }

    /**
     * Returns the AnchorPoint which identifies the location inside a text
     * label to use as an "anchor" for positioning it relative to a point
     * geometry.
     *
     * @return Label's AnchorPoint.
     */
    public AnchorPointImpl getAnchorPoint() {
        return anchorPoint;
    }

    /**
     * Setter for property anchorPoint.
     *
     * @param anchorPoint New value of property anchorPoint.
     */
    public void setAnchorPoint(org.opengis.style.AnchorPoint anchorPoint) {
        if( this.anchorPoint == anchorPoint ){
            return;
        }
        this.anchorPoint = AnchorPointImpl.cast( anchorPoint );
    }

    /**
     * Returns the Displacement which gives X and Y offset displacements to use
     * for rendering a text label near a point.
     *
     * @return The label displacement.
     */
    public Displacement getDisplacement() {
        return displacement;
    }

    /**
     * Setter for property displacement.
     *
     * @param displacement New value of property displacement.
     */
    public void setDisplacement(org.opengis.style.Displacement displacement) {
        if (this.displacement == displacement) {
            return;
        }
        this.displacement = DisplacementImpl.cast( displacement );
    }

    /**
     * Returns the rotation of the label.
     *
     * @return The rotation of the label.
     */
    public Expression getRotation() {
        return rotation;
    }

    /**
     * Setter for property rotation.
     *
     * @param rotation New value of property rotation.
     */
    public void setRotation(Expression rotation) {
        this.rotation = rotation;
    }

    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    /* (non-Javadoc)
     * @see Cloneable#clone()
     */
    public Object clone() {
        try {
            PointPlacementImpl clone = (PointPlacementImpl) super.clone();
            clone.anchorPoint = (AnchorPointImpl) ((Cloneable) anchorPoint).clone();
            clone.displacement = (DisplacementImpl) ((Cloneable) displacement)
                .clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Won't happen");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof PointPlacementImpl) {
            PointPlacementImpl other = (PointPlacementImpl) obj;

            return Utilities.equals(anchorPoint, other.anchorPoint)
            && Utilities.equals(displacement, other.displacement)
            && Utilities.equals(rotation, other.rotation);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        final int PRIME = 37;
        int result = 17;

        if (anchorPoint != null) {
            result = (result * PRIME) + anchorPoint.hashCode();
        }

        if (displacement != null) {
            result = (result * PRIME) + displacement.hashCode();
        }

        if (rotation != null) {
            result = (result * PRIME) + rotation.hashCode();
        }

        return result;
    }

    static PointPlacementImpl cast(org.opengis.style.LabelPlacement placement) {
        if( placement == null ){
            return null;
        }
        else if (placement instanceof PointPlacementImpl){
            return (PointPlacementImpl) placement;
        }
        else if (placement instanceof org.opengis.style.PointPlacement){
            org.opengis.style.PointPlacement pointPlacement = (org.opengis.style.PointPlacement) placement;
            PointPlacementImpl copy = new PointPlacementImpl();
            copy.setAnchorPoint( AnchorPointImpl.cast( pointPlacement.getAnchorPoint() ) );
            copy.setDisplacement( DisplacementImpl.cast( pointPlacement.getDisplacement() ));
            return copy;
        }
        return null;
    }
}
