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
// import java.util.logging.Logger;
// OpenGIS dependencies

import java.util.ArrayList;
import java.util.List;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Font;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

/**
 * Provides a Java representation of the Font element of an SLD.
 *
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class FontImpl implements Font, Cloneable {
    /** The logger for the default core module. */

    // private static final Logger LOGGER =
    // org.geotools.util.logging.Logging.getLogger(FontImpl.class);
    private final List<Expression> fontFamily = new ArrayList<>();

    private Expression fontSize = null;
    private Expression fontStyle = null;
    private Expression fontWeight = null;

    /** Creates a new instance of DefaultFont */
    protected FontImpl() {}

    @Override
    public List<Expression> getFamily() {
        return fontFamily;
    }

    @Override
    public Expression getSize() {
        return fontSize;
    }

    @Override
    public void setSize(Expression size) {
        this.fontSize = size;
    }

    @Override
    public Expression getStyle() {
        return fontStyle;
    }

    @Override
    public void setStyle(Expression style) {
        fontStyle = style;
    }

    @Override
    public Expression getWeight() {
        return fontWeight;
    }

    @Override
    public void setWeight(Expression weight) {
        fontWeight = weight;
    }

    /**
     * Creates a clone of the font.
     *
     * @see Cloneable#clone()
     */
    @Override
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
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (fontFamily != null) {
            result = PRIME * result + fontFamily.hashCode();
        }

        if (fontSize != null) {
            result = PRIME * result + fontSize.hashCode();
        }

        if (fontStyle != null) {
            result = PRIME * result + fontStyle.hashCode();
        }

        if (fontWeight != null) {
            result = PRIME * result + fontWeight.hashCode();
        }

        return result;
    }

    /**
     * Compares this font with another for equality. Two fonts are equal if their family, style, weight and size are
     * equal.
     *
     * @return True if this and oth are equal.
     */
    @Override
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

    /** Utility method to capture the default font in one place. */
    static Font createDefault(FilterFactory filterFactory) {
        Font font = new FontImpl();
        try {
            font.setSize(filterFactory.literal(Integer.valueOf(10)));
            font.setStyle(filterFactory.literal("normal"));
            font.setWeight(filterFactory.literal("normal"));
            font.getFamily().add(filterFactory.literal("Serif"));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            throw new RuntimeException("Error creating default", ife);
        }
        return font;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    static FontImpl cast(org.geotools.api.style.Font font) {
        if (font == null) {
            return null;
        } else if (font instanceof FontImpl) {
            return (FontImpl) font;
        } else {
            FontImpl copy = new FontImpl();
            copy.getFamily().addAll(font.getFamily());
            copy.setSize(font.getSize());
            copy.setStyle(font.getStyle());
            copy.setWeight(font.getWeight());
            return copy;
        }
    }
}
