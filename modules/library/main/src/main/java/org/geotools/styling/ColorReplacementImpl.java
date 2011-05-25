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

import org.opengis.filter.expression.Function;
import org.opengis.style.StyleVisitor;


/**
 * An implementation of ColorReplacement; this is a wrapper around an implementaiton
 * of the "Recode" function as defined by SymbologyEncoding 1.1.
 *
 * @author Jody Garnett
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/styling/ColorReplacementImpl.java $
 */
public class ColorReplacementImpl implements org.geotools.styling.ColorReplacement {

    private Function function;

    public ColorReplacementImpl( Function function ){
        this.function = function;
    }

    /**
     * Function used to perform color Replacement.
     * <p>
     * It is assumed this function is defined in accordance with the "Recode" function
     * from Symbology Encoding 1.1.
     * 
     * @return Implementation of "Recode" function
     */
    public Function getRecoding() {
        return function;
    }

    public Object accept(StyleVisitor visitor, Object extraData) {
        return visitor.visit( this, extraData );
    }

    public void setRecoding(Function function) {
        this.function = function;
    }

}
