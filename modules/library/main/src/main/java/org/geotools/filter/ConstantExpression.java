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
package org.geotools.filter;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Date;
import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Literal;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Geometry;

/**
 * The Expression class is not immutable!
 *
 * <p>However we do have a need for immutable literal expressions when defining our API for SLD, and any other standards
 * based on Expression.
 *
 * @author Jody Garnett, Refractions Research
 */
public class ConstantExpression implements Literal, Cloneable {
    public static final ConstantExpression NULL = constant(null);
    public static final ConstantExpression BLACK = color(Color.BLACK);
    public static final ConstantExpression ZERO = constant(0);
    public static final ConstantExpression ONE = constant(1);
    public static final ConstantExpression TWO = constant(2);
    public static final ConstantExpression UNNAMED = constant("");
    final short type;
    Object value;

    protected ConstantExpression(Object value) {
        this(type(value), value);
    }

    protected ConstantExpression(short type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public Object evaluate(Object object) {
        return getValue();
    }

    @Override
    public <T> T evaluate(Object object, Class<T> context) {
        return Converters.convert(getValue(), context);
    }

    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object constant) {
        throw new UnsupportedOperationException("Default value is immutable");
    }

    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ConstantExpression(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Literal)) {
            return false;
        }

        Literal other = (Literal) obj;
        Object otherLiteral = other.getValue();

        if (value == null) {
            return otherLiteral == null;
        }

        if (value.getClass().isAssignableFrom(otherLiteral.getClass())) {
            return value.equals(other.getValue());
        }

        if (value instanceof Number) {
            if (otherLiteral instanceof Number) {
                Number myNumber = (Number) value;
                Number otherNumber = (Number) otherLiteral;

                return myNumber.doubleValue() == otherNumber.doubleValue();
            }
        }

        // Okay we are into String Compare land!
        String myString = value.toString();
        String otherString = otherLiteral.toString();

        return myString.equals(otherString);
    }

    @Override
    public int hashCode() {
        if (value instanceof Geometry || value instanceof Date) {
            // forms of complex content ...
            return value.hashCode();
        }

        return value == null ? 0 : value.toString().hashCode();
    }

    @Override
    public String toString() {
        if (value instanceof Color) {
            Color color = (Color) value;
            String redCode = Integer.toHexString(color.getRed());
            String greenCode = Integer.toHexString(color.getGreen());
            String blueCode = Integer.toHexString(color.getBlue());

            if (redCode.length() == 1) {
                redCode = "0" + redCode;
            }

            if (greenCode.length() == 1) {
                greenCode = "0" + greenCode;
            }

            if (blueCode.length() == 1) {
                blueCode = "0" + blueCode;
            }
        }

        return value == null ? "NULL" : value.toString();
    }

    /** Encode provided color as a String */
    public static ConstantExpression color(Color color) {
        if (color == null) {
            return NULL;
        }

        String redCode = Integer.toHexString(color.getRed());
        String greenCode = Integer.toHexString(color.getGreen());
        String blueCode = Integer.toHexString(color.getBlue());

        if (redCode.length() == 1) {
            redCode = "0" + redCode;
        }

        if (greenCode.length() == 1) {
            greenCode = "0" + greenCode;
        }

        if (blueCode.length() == 1) {
            blueCode = "0" + blueCode;
        }

        String colorCode = ("#" + redCode + greenCode + blueCode).toUpperCase();

        return new ConstantExpression(colorCode);
    }

    public static ConstantExpression constant(double number) {
        return new ConstantExpression(Double.valueOf(number));
    }

    public static ConstantExpression constant(int number) {
        return new ConstantExpression(Integer.valueOf(number));
    }

    public static ConstantExpression constant(Object value) {
        return new ConstantExpression(value);
    }

    static short type(Object value) {
        if (value instanceof Number) {
            if (value instanceof Double) {
                return ExpressionType.LITERAL_DOUBLE;
            } else if (value instanceof BigDecimal) {
                return ExpressionType.LITERAL_DOUBLE;
            } else {
                return ExpressionType.LITERAL_INTEGER;
            }
        } else if (value instanceof Geometry) {
            return ExpressionType.LITERAL_GEOMETRY;
        }

        return ExpressionType.LITERAL_STRING;
    }
}
