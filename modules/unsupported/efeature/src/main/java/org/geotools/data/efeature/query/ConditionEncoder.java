package org.geotools.data.efeature.query;

import java.util.Date;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.numbers.NumberCondition;
import org.eclipse.emf.query.conditions.strings.StringRegularExpressionValue;
import org.geotools.data.efeature.DataTypes;
import org.geotools.data.efeature.DateCondition;
import org.opengis.filter.expression.Literal;

public class ConditionEncoder {

    public static final Condition IS_NULL = isNull();

    // ----------------------------------------------------- 
    //  Generic comparison methods
    // -----------------------------------------------------

    public static Condition isNull() {
        return new Condition() {

            @Override
            public boolean isSatisfied(Object object) {
                return object == null;
            }
        };
    }

    public static Condition equals(Literal value) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isNumeric(v)) {
            return equals((Number) v);
        } else if (DataTypes.isDate(v)) {
            return equals((Date) v);
        } else if (DataTypes.isString(v)) {
            return equals((String) v);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition notEquals(Literal value) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isNumeric(v)) {
            return notEquals((Number) v);
        } else if (DataTypes.isDate(v)) {
            return notEquals((Date) v);
        } else if (DataTypes.isString(v)) {
            return notEquals((String) v);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition lessThan(Literal value) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isNumeric(v)) {
            return lessThan((Number) v);
        } else if (DataTypes.isDate(v)) {
            return lessThan((Date) v);
        } else if (DataTypes.isString(v)) {
            return lessThan((String) v);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition lessThanOrEquals(Literal value) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isNumeric(v)) {
            return lessThanOrEquals((Number) v);
        } else if (DataTypes.isDate(v)) {
            return lessThanOrEquals((Date) v);
        } else if (DataTypes.isString(v)) {
            return lessThanOrEquals((String) v);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition greaterThan(Literal value) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isNumeric(v)) {
            return greaterThan((Number) v);
        } else if (DataTypes.isDate(v)) {
            return greaterThan((Date) v);
        } else if (DataTypes.isString(v)) {
            return greaterThan((String) v);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition greaterThanOrEquals(Literal value) throws EFeatureEncoderException {
        Object v = value.getValue();
        if (DataTypes.isNumeric(v)) {
            return greaterThanOrEquals((Number) v);
        } else if (DataTypes.isDate(v)) {
            return greaterThanOrEquals((Date) v);
        } else if (DataTypes.isString(v)) {
            return greaterThanOrEquals((String) v);
        }
        throw new EFeatureEncoderException("Literal " + value + " not supported");
    }

    public static Condition between(Literal lower, Literal upper) throws EFeatureEncoderException {
        Object vl = lower.getValue();
        Object vu = upper.getValue();
        isSameType(vl, "lower", vu, "upper", true);
        if (DataTypes.isNumeric(vl)) {
            return between((Number) vl, (Number) vu);
        } else if (DataTypes.isDate(vl)) {
            return between((Date) vl, (Date) vu);
        } else if (DataTypes.isString(vl)) {
            return between((String) vl, (String) vu);
        }
        throw new EFeatureEncoderException("Literals '" + lower + "' and '" + upper
                + "' not supported");
    }

    // ----------------------------------------------------- 
    //  Number comparison methods
    // -----------------------------------------------------

    public static NumberCondition<?> equals(Number number) throws EFeatureEncoderException {
        isSane(number, "number", true);
        if (number instanceof Integer)
            return NumberCondition.equals((Integer) number);
        else if (number instanceof Double)
            return NumberCondition.equals((Double) number);
        else if (number instanceof Long)
            return NumberCondition.equals((Long) number);
        else if (number instanceof Float)
            return NumberCondition.equals((Float) number);
        else if (number instanceof Short)
            return NumberCondition.equals((Short) number);
        else if (number instanceof Byte)
            return NumberCondition.equals((Byte) number);
        throw new EFeatureEncoderException("Type " + number + " not supported");
    }

    public static NumberCondition<?> notEquals(Number number) throws EFeatureEncoderException {
        isSane(number, "number", true);
        if (number instanceof Integer)
            return NumberCondition.notEquals((Integer) number);
        else if (number instanceof Double)
            return NumberCondition.notEquals((Double) number);
        else if (number instanceof Long)
            return NumberCondition.notEquals((Long) number);
        else if (number instanceof Float)
            return NumberCondition.notEquals((Float) number);
        else if (number instanceof Short)
            return NumberCondition.notEquals((Short) number);
        else if (number instanceof Byte)
            return NumberCondition.notEquals((Byte) number);
        throw new EFeatureEncoderException("Type " + number + " not supported");
    }

    public static NumberCondition<?> lessThan(Number number) throws EFeatureEncoderException {
        isSane(number, "number", true);
        if (number instanceof Integer)
            return NumberCondition.lessThan((Integer) number);
        else if (number instanceof Double)
            return NumberCondition.lessThan((Double) number);
        else if (number instanceof Long)
            return NumberCondition.lessThan((Long) number);
        else if (number instanceof Float)
            return NumberCondition.lessThan((Float) number);
        else if (number instanceof Short)
            return NumberCondition.lessThan((Short) number);
        else if (number instanceof Byte)
            return NumberCondition.lessThan((Byte) number);
        throw new EFeatureEncoderException("Type " + number + " not supported");
    }

    public static NumberCondition<?> lessThanOrEquals(Number number)
            throws EFeatureEncoderException {
        isSane(number, "number", true);
        if (number instanceof Integer)
            return NumberCondition.lessThanOrEquals((Integer) number);
        else if (number instanceof Double)
            return NumberCondition.lessThanOrEquals((Double) number);
        else if (number instanceof Long)
            return NumberCondition.lessThanOrEquals((Long) number);
        else if (number instanceof Float)
            return NumberCondition.lessThanOrEquals((Float) number);
        else if (number instanceof Short)
            return NumberCondition.lessThanOrEquals((Short) number);
        else if (number instanceof Byte)
            return NumberCondition.lessThanOrEquals((Byte) number);
        throw new EFeatureEncoderException("Type " + number + " not supported");
    }

    public static NumberCondition<?> greaterThan(Number number) throws EFeatureEncoderException {
        isSane(number, "number", true);
        if (number instanceof Integer)
            return NumberCondition.greaterThan((Integer) number);
        else if (number instanceof Double)
            return NumberCondition.greaterThan((Double) number);
        else if (number instanceof Long)
            return NumberCondition.greaterThan((Long) number);
        else if (number instanceof Float)
            return NumberCondition.greaterThan((Float) number);
        else if (number instanceof Short)
            return NumberCondition.greaterThan((Short) number);
        else if (number instanceof Byte)
            return NumberCondition.greaterThan((Byte) number);
        throw new EFeatureEncoderException("Type " + number + " not supported");
    }

    public static NumberCondition<?> greaterThanOrEquals(Number number)
            throws EFeatureEncoderException {
        isSane(number, "number", true);
        if (number instanceof Integer)
            return NumberCondition.greaterThanOrEquals((Integer) number);
        else if (number instanceof Double)
            return NumberCondition.greaterThanOrEquals((Double) number);
        else if (number instanceof Long)
            return NumberCondition.greaterThanOrEquals((Long) number);
        else if (number instanceof Float)
            return NumberCondition.greaterThanOrEquals((Float) number);
        else if (number instanceof Short)
            return NumberCondition.greaterThanOrEquals((Short) number);
        else if (number instanceof Byte)
            return NumberCondition.greaterThanOrEquals((Byte) number);
        throw new EFeatureEncoderException("Type " + number + " not supported");
    }

    public static NumberCondition<?> between(Number lower, Number upper)
            throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        if (lower instanceof Integer)
            return NumberCondition.between((Integer) lower, (Integer) upper);
        else if (lower instanceof Double)
            return NumberCondition.between((Double) lower, (Double) upper);
        else if (lower instanceof Long)
            return NumberCondition.between((Long) lower, (Long) upper);
        else if (lower instanceof Float)
            return NumberCondition.between((Float) lower, (Float) upper);
        else if (lower instanceof Short)
            return NumberCondition.between((Short) lower, (Short) upper);
        else if (lower instanceof Byte)
            return NumberCondition.between((Byte) lower, (Byte) upper);
        throw new EFeatureEncoderException("Type " + lower + " not supported");
    }

    // ----------------------------------------------------- 
    //  Date comparison methods
    // -----------------------------------------------------

    public static DateCondition equals(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.equals(date);
    }

    public static DateCondition notEquals(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.notEquals(date);
    }

    public static DateCondition lessThan(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.lessThan(date);
    }

    public static DateCondition lessThanOrEquals(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.lessThanOrEquals(date);
    }

    public static DateCondition greaterThan(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.greaterThan(date);
    }

    public static DateCondition greaterThanOrEquals(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.greaterThanOrEquals(date);
    }

    public static DateCondition between(Date lower, Date upper) throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return DateCondition.between(lower, upper);
    }

    // ----------------------------------------------------- 
    //  String comparison methods
    // -----------------------------------------------------

    public static StringCondition equals(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.equals(str);
    }

    public static StringCondition notEquals(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.notEquals(str);
    }

    public static StringCondition lessThan(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.lessThan(str);
    }

    public static StringCondition lessThanOrEquals(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.lessThanOrEquals(str);
    }

    public static StringCondition greaterThan(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.greaterThan(str);
    }

    public static StringCondition greaterThanOrEquals(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.greaterThanOrEquals(str);
    }

    public static StringCondition between(String lower, String upper)
            throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return StringCondition.between(lower, upper);
    }

    public static StringRegularExpressionValue like(String pattern) throws EFeatureEncoderException {
        isSane(pattern, "pattern", true);
        return new StringRegularExpressionValue(pattern);
    }

    // ----------------------------------------------------- 
    //  Public helper methods
    // -----------------------------------------------------

    public static boolean isSane(Object object, String name, boolean exception)
            throws EFeatureEncoderException {
        if (object == null) {
            return failure("Parameter '" + name + "' can not be null", exception);
        }
        return true;
    }

    public static boolean isSameType(Object obj1, String name1, 
            Object obj2, String name2, boolean exception) 
            throws EFeatureEncoderException {
        if (!isSane(obj1, name1, exception))
            return false;
        if (!isSane(obj2, name2, exception))
            return false;
        if (obj2.getClass() != obj2.getClass()) {
            return failure("Parameters are not of same type", exception);
        }
        return true;
    }

    public static boolean failure(String message, boolean exception)
            throws EFeatureEncoderException {
        if (exception)
            throw new EFeatureEncoderException(message);
        return false;
    }

}
