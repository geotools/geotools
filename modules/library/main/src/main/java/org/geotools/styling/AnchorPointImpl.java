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
import org.opengis.util.Cloneable;


/**
 * Direct implementation of AnchorPoint.
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 * @version $Id$
 */
public class AnchorPointImpl implements AnchorPoint,Cloneable {
    
    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");
    private FilterFactory filterFactory;
    private Expression anchorPointX = null;
    private Expression anchorPointY = null;

    
    static AnchorPointImpl cast( org.opengis.style.AnchorPoint anchor ){
        if( anchor == null ){
            return null;
        }
        else if (anchor instanceof AnchorPointImpl){
            return (AnchorPointImpl) anchor;
        }
        else {
            AnchorPointImpl copy = new AnchorPointImpl();
            copy.setAnchorPointX( anchor.getAnchorPointX() );
            copy.setAnchorPointY( anchor.getAnchorPointY() );
            return copy;
        }
    }
    
    public AnchorPointImpl() {
        this( CommonFactoryFinder.getFilterFactory( GeoTools.getDefaultHints() ) );
    }
    /**
     * Creates a new instance of DefaultAnchorPoint
     */
    public AnchorPointImpl( FilterFactory filterFactory ) {
        this.filterFactory = filterFactory; 
        try {
            anchorPointX = filterFactory.literal( 0.0 );
            anchorPointY = filterFactory.literal( 0.5 );
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultAnchorPoint: " + ife);
        }
    }

    public AnchorPointImpl(FilterFactory filterFactory, Expression x, Expression y) {
        this.filterFactory = filterFactory;
        anchorPointX = x;
        anchorPointY = y;
    }
    /**
     * Getter for property anchorPointX.
     *
     * @return Value of property anchorPointX.
     */
    public Expression getAnchorPointX() {
        return anchorPointX;
    }

    /**
     * Setter for property anchorPointX.
     *
     * @param anchorPointX New value of property anchorPointX.
     */
    public void setAnchorPointX(Expression anchorPointX) {
        this.anchorPointX = anchorPointX;
    }
    /**
     * Define the anchor point.
     * 
     * @param x Literal value of property anchorPointX
     */
    public void setAnchorPointX(double x){
        this.anchorPointX = filterFactory.literal( x );
    }

    /**
     * Getter for property anchorPointY.
     *
     * @return Value of property anchorPointY.
     */
    public Expression getAnchorPointY() {
        return anchorPointY;
    }

    /**
     * Setter for property anchorPointY.
     *
     * @param anchorPointY New value of property anchorPointY.
     */
    public void setAnchorPointY(Expression anchorPointY) {
        this.anchorPointY = anchorPointY;
    }

    /**
     * Define the anchor point.
     * 
     * @param x Literal value of property anchorPointX
     */
    public void getAnchorPointY(double x){
        this.anchorPointY = filterFactory.literal( x );
    }
    
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    public Object accept(org.opengis.style.StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

    /* (non-Javadoc)
     * @see Cloneable#clone()
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Never happen");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof AnchorPointImpl) {
            AnchorPointImpl other = (AnchorPointImpl) obj;

            return Utilities.equals(this.anchorPointX, other.anchorPointX)
            && Utilities.equals(this.anchorPointY, other.anchorPointY);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        final int PRIME = 37;
        int result = 17;

        if (anchorPointX != null) {
            result = (result * PRIME) + anchorPointX.hashCode();
        }

        if (anchorPointY != null) {
            result = (result * PRIME) + anchorPointY.hashCode();
        }

        return result;
    }
}
