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

import java.util.List;

import org.geotools.util.ConverterFactory;
import org.geotools.util.Utilities;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;


/**
 * A system-independent object for holding SLD font information. This holds
 * information on the text font to use in text processing. Font-family,
 * font-style, font-weight and font-size.
 *
 * @author Ian Turton, CCG
 * @source $URL$
 * @version $Id$
 */
public interface Font extends org.opengis.style.Font{
    /** default font-size value **/
    static final int DEFAULT_FONTSIZE = 10;

    /**
     * @deprecated use getFamilly().get(0) for the preferred font
     */
    Expression getFontFamily();
    
    /**
     * SVG font-family parameters in preferred order.
     * @return live list of font-family parameters in preferred order
     */
    List<Expression> getFamily();

    /**
     * @param family Expression indicating the font fact to use
     * @deprecated Please use getFontFamilly.set( 0, expression )
     */
    void setFontFamily(Expression family);

    /**
     * The "font-style" SVG parameter should be "normal", "italic", or "oblique".
     * <p>
     * If null is returned the default value should be considered "normal".
     * @return Expression or null
     */
    Expression getStyle();
    
    /**
     * @param style The "font-style" SVG parameter (one of "normal", "italic", or "oblique"
     */
    void setStyle( Expression style );
    

    /**
     * The "font-weight" SVG parameter should be "normal" or "bold".
     * <p>
     * If null the default should be considered as "normal"
     * @return font-weight SVG parameter
     */
    Expression getWeight();
    
    /**
     * @param weight The "font-weight" SVG parameter (one of "normal", "bold")
     */
    void setWeight(Expression weight);
    
    /**
     * Font size in pixels with a default of 10 pixels.
     * <p>
     * Please note this is specified in pixels so you may need to take the
     * resolution of your output into account when providing a size.
     * 
     * @return font size
     */
    Expression getSize();
    
    /**
     * @param size the font size in pixels
     */
    void setSize(Expression size);
    
    //
    // Depreciated names used from GeoTools 2.0-2.5
    //
    /**
     * @deprecated Please use getStyle in 2.6.x
     */
    Expression getFontStyle();

    
    /**
     * @deprecated Please use setStyle( style )
     */
    void setFontStyle(Expression style);

    /**
     * @deprecated use getWeight
     */
    Expression getFontWeight();

    /**
     * @deprecated Please use setWeight( weight )
     */
    void setFontWeight(Expression weight);


    /**
     * @deprecated use getSize
     */
    Expression getFontSize();

    /**
     * @deprecated symbolizers and underneath classes will be immutable in 2.6.x
     */
    void setFontSize(Expression size);

    /**
     * Enumeration of allow font-style values.
     * <p>
     * This is a way to document the constants allowable for the setStyle method
     *
    enum Style2 implements Literal {
        NORMAL("normal"),
        ITALIC("italic"),
        OBLIQUE("oblique");
        
        final String literal;
        final static int count=0;
        private Style2(String constant) {
            literal = constant;
        }
        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return visitor.visit( this, extraData );
        }
        public Object evaluate(Object object) {
            return literal;
        }
        public <T> T evaluate(Object object, Class<T> context) {
            // return Converters.convert(literal, context);
            if( context.isInstance( literal) ){
                return context.cast(literal);                
            }
            return null;
        }
        public Object getValue() {
            return literal;
        }
    }
    */
    
    interface Style {
        static final String NORMAL = "normal";
        static final String ITALIC = "italic";
        static final String OBLIQUE = "oblique";
    }

    /**
     * Enumeration of allow font-weight values.
     */
    interface Weight {
        static final String NORMAL = "normal";
        static final String BOLD = "bold";
    }
}