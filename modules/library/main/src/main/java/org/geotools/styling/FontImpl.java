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


// J2SE dependencies
//import java.util.logging.Logger;
// OpenGIS dependencies
import java.util.ArrayList;
import java.util.List;

import org.geotools.util.Utilities;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * Provides a Java representation of the Font element of an SLD.
 *
 * @author Ian Turton, CCG
 * @source $URL$
 * @version $Id$
 */
public class FontImpl implements Font, Cloneable {
    /** The logger for the default core module. */

    //private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");
    private final List<Expression> fontFamily = new ArrayList<Expression>();
    private Expression fontSize = null;
    private Expression fontStyle = null;
    private Expression fontWeight = null;

    /**
     * Creates a new instance of DefaultFont
     */
    protected FontImpl() {
    }

    /**
     * Getter for property fontFamily.
     *
     * @return Value of property fontFamily.
     */
    @Deprecated  
    public Expression getFontFamily() {
        if (fontFamily.isEmpty()) {
            return null;
        } else {
            return fontFamily.get(0);
        }
    }

    public List<Expression> getFamily() {
        return fontFamily;
    }

    /**
     * Setter for property fontFamily.
     *
     * @param fontFamily New value of property fontFamily.
     */
    @Deprecated
    public void setFontFamily(Expression fontFamily) {
        this.fontFamily.clear();
        this.fontFamily.add(fontFamily);
    }

    /**
     * Getter for property fontSize.
     *
     * @return Value of property fontSize.
     */
    @Deprecated
    public Expression getFontSize() {
        return fontSize;
    }
	public Expression getSize() {
		return fontSize;
	}
	public void setSize(Expression size) {
		this.fontSize = size;
	}
    /**
     * Setter for property fontSize.
     *
     * @param fontSize New value of property fontSize.
     */
    @Deprecated
    public void setFontSize(Expression fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * Getter for property fontStyle.
     *
     * @return Value of property fontStyle.
     */
    @Deprecated
    public Expression getFontStyle() {
        return fontStyle;
    }
    
    public Expression getStyle() {
    	return fontStyle;
    }
    public void setStyle(Expression style) {
    	fontStyle = style;
    }
    /**
     * Setter for property fontStyle.
     *
     * @param fontStyle New value of property fontStyle.
     */
    @Deprecated
    public void setFontStyle(Expression fontStyle) {
        this.fontStyle = fontStyle;
    }

    /**
     * Getter for property fontWeight.
     *
     * @return Value of property fontWeight.
     */
    @Deprecated
    public Expression getFontWeight() {
        return fontWeight;
    }
    
    public Expression getWeight() {
    	return fontWeight;
    }
	public void setWeight(Expression weight) {
		fontWeight = weight;
	}
    /**
     * Setter for property fontWeight.
     *
     * @param fontWeight New value of property fontWeight.
     */
    @Deprecated
    public void setFontWeight(Expression fontWeight) {
        this.fontWeight = fontWeight;
    }

    /**
     * Creates a clone of the font.
     *
     * @see Cloneable#clone()
     */
    public Object clone() {
        try {
            // all the members are immutable expression
            // super.clone() is enough.
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This should not happen", e);
        }
    }

    /**
     * Generates the hashcode for the font.
     *
     * @return the hash code.
     */
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (fontFamily != null) {
            result = (PRIME * result) + fontFamily.hashCode();
        }

        if (fontSize != null) {
            result = (PRIME * result) + fontSize.hashCode();
        }

        if (fontStyle != null) {
            result = (PRIME * result) + fontStyle.hashCode();
        }

        if (fontWeight != null) {
            result = (PRIME * result) + fontWeight.hashCode();
        }

        return result;
    }

    /**
     * Compares this font with another for equality.  Two fonts are equal if
     * their family, style, weight  and size are equal.
     *
     * @param oth DOCUMENT ME!
     *
     * @return True if this and oth are equal.
     */
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth == null) {
            return false;
        }

        if (oth instanceof FontImpl) {
            FontImpl other = (FontImpl) oth;

            return Utilities.equals(this.fontFamily, other.fontFamily)
            && Utilities.equals(this.fontSize, other.fontSize)
            && Utilities.equals(this.fontStyle, other.fontStyle)
            && Utilities.equals(this.fontWeight, other.fontWeight);
        }

        return false;
    }
    
    /**
     * Utility method to capture the default font in one place.
     * @return
     */
    static Font createDefault( FilterFactory filterFactory ) {
        Font font = new FontImpl();
        try {
            font.setSize(filterFactory.literal(
                    new Integer(10)));
            font.setStyle(filterFactory.literal("normal"));
            font.setWeight(filterFactory.literal("normal"));
            font.setFontFamily(filterFactory.literal("Serif"));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            throw new RuntimeException("Error creating default", ife);
        }
        return font;
    }

    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }
    
    static FontImpl cast( org.opengis.style.Font font ){
        if( font == null ) {
            return null;
        }
        else if (font instanceof FontImpl ){
            return (FontImpl) font;            
        }
        else {
            FontImpl copy = new FontImpl();
            copy.getFamily().addAll( font.getFamily() );
            copy.setSize(font.getSize());
            copy.setStyle(font.getStyle());
            copy.setWeight(font.getWeight());
            return copy;
        }
    }
    
}