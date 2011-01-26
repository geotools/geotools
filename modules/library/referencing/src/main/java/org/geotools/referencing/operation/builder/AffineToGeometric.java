/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import org.geotools.referencing.operation.transform.AffineTransform2D;

/**
 * Helper class for converting values from affine transformation matrix to its geometric form.
 * Development carried out thanks to R&D grant DC08P02OUK006 - Old Maps Online
 * (www.oldmapsonline.org) from Ministry of Culture of the Czech Republic
 * 
 * @see http://groups.csail.mit.edu/graphics/classes/6.837/F98/Notes/lecture10.ps
 * @author jezekjan
 * @source $URL$
 * @version $Id$
 */
public class AffineToGeometric {
    /** translation in x */
    private double tx;

    /** translation in y */
    private double ty;

    /** scale in x */
    private double sx;

    /** scale in y */
    private double sy;

    /** x rotation in radians */
    private double phix;
    
    /** y rotation in radians */
    private double phiy;

    /** skew */
    private double sxy;   

    /**
     * Constructs AffineToGeometric from AffineTransform2D
     * @param trans Affine transformation from which we want to get geometric coefficients.
     */
    public AffineToGeometric(AffineTransform2D trans) {           
        
    	sx = Math.pow(Math.pow(trans.getShearY(), 2) + Math.pow(trans.getScaleX(), 2), 0.5);
    	sy = Math.pow(Math.pow(trans.getScaleY(), 2) + Math.pow(trans.getShearX(), 2), 0.5);       
        phix =  Math.acos(Math.signum(trans.getShearY())*trans.getScaleX() / sx);        
        phiy = Math.acos( Math.signum(-trans.getShearX())*trans.getScaleY() / sy);     
        sxy = phix - phiy;
        tx = trans.getTranslateX();
        ty = trans.getTranslateY();

    }

    /**
     * Returns Scale in x direction
     * @return scale in x direction   
     */
    public double getXScale() {
        return sx;
    }

    /**
     * Returns Scale in y direction
     * @return scale in y direction  
     */
    public double getYScale() {
        return sy;
    }

    /**
     * Returns skew
     * @return skew   
     */
    public double getSkew() {
        return sxy;
    }

    /**
     * Returns translation in x direction
     * @return translation in x direction 
     */
    public double getXTranslate() {
        return tx;
    }

    /**
     * Returns translation in y direction
     * @return translation in y direction
     */
    public double getYTranslate() {
        return ty;
    }

    /**
     * Returns rotation in radians
     * @return rotation in radians
     */
    public double getXRotation() {
        return phix;
    }
    
    public double getYRotation() {
        return phiy;
    }
}
