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

import org.geotools.util.Converters;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.NilExpression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

import com.vividsolutions.jts.geom.Geometry;


/**
 * The Expression class is not immutable!
 * <p>
 * However we do have a need for immutable literal expressions when
 * defining our API for SLD, and any other standards based on
 * Expression.
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 */
public class ConstantExpression implements LiteralExpression, Cloneable {
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

    /**
     * @deprecated use {@link #setValue(Object)}
     */
    public final void setLiteral(Object literal) throws IllegalFilterException {
        throw new UnsupportedOperationException("Default value is immutable");
    }

    /**
     * @deprecated use {@link #evaluate(Feature)}
     */
    public final Object getValue(SimpleFeature feature) {
        return evaluate(feature);
    }

    public Object evaluate(SimpleFeature feature) {
        return getValue();
    }

    public Object evaluate(Object object) {
        return getValue();
    }

    public <T> T evaluate(Object object, Class<T> context) {
        return Converters.convert(getValue(), context);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object constant) {
        throw new UnsupportedOperationException("Default value is immutable");
    }

    public short getType() {
        return type;
    }

    /**
     * @deprecated use {@link #getValue()}
     */
    public final Object getLiteral() {
        return getValue();
    }

    /**
     * @deprecated use {@link #accept(ExpressionVisitor, Object)}.
     */
    public void accept(final FilterVisitor visitor) {
        accept(new ExpressionVisitor() {
                public Object visit(Add expression, Object extraData) {
                    return null;
                }

                public Object visit(Divide expression, Object extraData) {
                    return null;
                }

                public Object visit(Function expression, Object extraData) {
                    return null;
                }

                public Object visit(Literal expression, Object extraData) {
                    visitor.visit(ConstantExpression.this);

                    return null;
                }

                public Object visit(Multiply expression, Object extraData) {
                    return null;
                }

                public Object visit(PropertyName expression, Object extraData) {
                    return null;
                }

                public Object visit(Subtract expression, Object extraData) {
                    return null;
                }

                public Object visit(NilExpression arg0, Object arg1) {
                    return null;
                }
            }, null);
    }

    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    protected Object clone() throws CloneNotSupportedException {
        return new ConstantExpression(value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LiteralExpression)) {
            return false;
        }

        LiteralExpression other = (LiteralExpression) obj;
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

    public int hashCode() {
        if (value instanceof Geometry || value instanceof Date) {
            // forms of complex content ...
            return value.hashCode();
        }

        return (value == null) ? 0 : value.toString().hashCode();
    }

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

        return (value == null) ? "NULL" : value.toString();
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
        return new ConstantExpression(new Double(number));
    }

    public static ConstantExpression constant(int number) {
        return new ConstantExpression(new Integer(number));
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
