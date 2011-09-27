package org.geotools.data.efeature.query;

import java.util.Date;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.Not;
import org.eclipse.emf.query.conditions.booleans.BooleanCondition;
import org.eclipse.emf.query.conditions.eobjects.EObjectInstanceCondition;
import org.eclipse.emf.query.conditions.numbers.NumberCondition;
import org.eclipse.emf.query.conditions.strings.StringRegularExpressionValue;
import org.geotools.data.efeature.DataBuilder;
import org.geotools.data.efeature.DataTypes;
import org.opengis.filter.expression.Literal;

/**
 * 
 *
 * @source $URL$
 */
public class ConditionEncoder {

    public static final Condition IS_NULL = EObjectInstanceCondition.IS_NULL;

    // ----------------------------------------------------- 
    //  Generic comparison methods
    // -----------------------------------------------------

    public static Condition eq(Literal value) throws EFeatureEncoderException {
        return eq(value.getValue());
    }

    public static Condition eq(EDataType type, Literal value) throws EFeatureEncoderException {
        return eq(DataBuilder.toValue(type, value));
    }

    public static Condition eq(EDataType type, Object value) throws EFeatureEncoderException {
        return eq(DataBuilder.toValue(type, value));
    }
        
    public static Condition eq(Object value) throws EFeatureEncoderException {
        if (DataTypes.isNumeric(value)) {
            return eq((Number) value);
        } else if (DataTypes.isDate(value)) {
            return eq((Date) value);
        } else if (DataTypes.isBoolean(value,false)) {
            return eq((Boolean) value);
        } else if (DataTypes.isString(value)) {
            return eq((String) value);
        } else if (DataTypes.isCharacter(value)) {
            return eq((Character) value);
        }
        throw new EFeatureEncoderException("EQ: Literal " + value + " not supported");
    }
    
    public static Condition ne(Literal value) throws EFeatureEncoderException {
        return ne(value.getValue());
    }
    
    public static Condition ne(EDataType type, Literal value) throws EFeatureEncoderException {
        return ne(DataBuilder.toValue(type, value));
    }    

    public static Condition ne(EDataType type, Object value) throws EFeatureEncoderException {
        return ne(DataBuilder.toValue(type, value));
    }    

    public static Condition ne(Object value) throws EFeatureEncoderException {
        if (DataTypes.isNumeric(value)) {
            return ne((Number) value);
        } else if (DataTypes.isDate(value)) {
            return ne((Date) value);
        } else if (DataTypes.isBoolean(value,false)) {
            return ne((Boolean) value);
        } else if (DataTypes.isString(value)) {
            return ne((String) value);
        } else if (DataTypes.isCharacter(value)) {
            return ne((Character) value);
        }
        throw new EFeatureEncoderException("NE: Literal " + value + " not supported");
    }

    public static Condition lt(Literal value) throws EFeatureEncoderException {
        return lt(value.getValue());
    }
    
    public static Condition lt(EDataType type, Literal value) throws EFeatureEncoderException {
        return lt(DataBuilder.toValue(type, value));
    }    
    
    public static Condition lt(EDataType type, Object value) throws EFeatureEncoderException {
        return lt(DataBuilder.toValue(type, value));
    }    
    
    public static Condition lt(Object value) throws EFeatureEncoderException {
        if (DataTypes.isNumeric(value)) {
            return lt((Number) value);
        } else if (DataTypes.isDate(value)) {
            return lt((Date) value);
        } else if (DataTypes.isString(value)) {
            return lt((String) value);
        } else if (DataTypes.isCharacter(value)) {
            return lt((Character) value);
        }
        throw new EFeatureEncoderException("LT: Literal " + value + " not supported");
    }

    public static Condition le(Literal value) throws EFeatureEncoderException {
        return le(value.getValue());
    }
    
    public static Condition le(EDataType type, Literal value) throws EFeatureEncoderException {
        return le(DataBuilder.toValue(type, value));
    }  
    
    public static Condition le(EDataType type, Object value) throws EFeatureEncoderException {
        return le(DataBuilder.toValue(type, value));
    }  

    public static Condition le(Object value) throws EFeatureEncoderException {
        if (DataTypes.isNumeric(value)) {
            return le((Number) value);
        } else if (DataTypes.isDate(value)) {
            return le((Date) value);
        } else if (DataTypes.isString(value)) {
            return le((String) value);
        } else if (DataTypes.isCharacter(value)) {
            return le((Character) value);
        }
        throw new EFeatureEncoderException("LE: Literal " + value + " not supported");
    }

    public static Condition gt(Literal value) throws EFeatureEncoderException {
        return gt(value.getValue());
    }
    
    public static Condition gt(EDataType type, Literal value) throws EFeatureEncoderException {
        return gt(DataBuilder.toValue(type, value));
    }  
    
    public static Condition gt(EDataType type, Object value) throws EFeatureEncoderException {
        return gt(DataBuilder.toValue(type, value));
    }  
    
    public static Condition gt(Object value) throws EFeatureEncoderException {
        if (DataTypes.isNumeric(value)) {
            return gt((Number) value);
        } else if (DataTypes.isDate(value)) {
            return gt((Date) value);
        } else if (DataTypes.isString(value)) {
            return gt((String) value);
        } else if (DataTypes.isCharacter(value)) {
            return gt((Character) value);
        }
        throw new EFeatureEncoderException("GT: Literal " + value + " not supported");
    }

    public static Condition ge(Literal value) throws EFeatureEncoderException {
        return ge(value.getValue());
    }
    
    public static Condition ge(EDataType type, Literal value) throws EFeatureEncoderException {
        return ge(DataBuilder.toValue(type, value));
    }  
        
    public static Condition ge(EDataType type, Object value) throws EFeatureEncoderException {
        return ge(DataBuilder.toValue(type, value));
    }  
        
    public static Condition ge(Object value) throws EFeatureEncoderException {
        if (DataTypes.isNumeric(value)) {
            return ge((Number) value);
        } else if (DataTypes.isDate(value)) {
            return ge((Date) value);
        } else if (DataTypes.isString(value)) {
            return ge((String) value);
        } else if (DataTypes.isCharacter(value)) {
            return ge((Character) value);
        }
        throw new EFeatureEncoderException("GE: Literal " + value + " not supported");
    }
    
    public static Condition like(Literal value) throws EFeatureEncoderException {
        return like(value.getValue());
    }
    
    public static Condition like(EDataType type, Literal value) throws EFeatureEncoderException {
        return like(DataBuilder.toValue(type, value));
    } 
    
    public static Condition like(EDataType type, Object value) throws EFeatureEncoderException {
        return like(DataBuilder.toValue(type, value));
    } 
    
    public static Condition like(Object value) throws EFeatureEncoderException {
        if (DataTypes.isString(value)) {
            return like((String) value);
        }
        throw new EFeatureEncoderException("LIKE: Literal " + value + " not supported");
    }    

    public static Condition between(Literal lower, Literal upper) throws EFeatureEncoderException {
        return between(lower.getValue(),upper.getValue());
    }
    
    public static Condition between(EDataType type, Literal lower, Literal upper) throws EFeatureEncoderException {
        return between(DataBuilder.toValue(type, lower),DataBuilder.toValue(type, upper));
    } 
    
    public static Condition between(EDataType type, Object lower, Object upper) throws EFeatureEncoderException {
        return between(DataBuilder.toValue(type, lower),DataBuilder.toValue(type, upper));
    } 
    
    public static Condition between(Object lower, Object upper) throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        if (DataTypes.isNumeric(lower)) {
            return between((Number) lower, (Number) upper);
        } else if (DataTypes.isDate(lower)) {
            return between((Date) lower, (Date) upper);
        } else if (DataTypes.isString(lower)) {
            return between((String) lower, (String) upper);
        } else if (DataTypes.isCharacter(lower)) {
            return between((Character) lower,(Character) upper);
        }
        throw new EFeatureEncoderException("BETWEEN: Literals '" 
                + lower + "' and '" + upper + "' not supported");
    }
    
    public static Condition outside(Literal lower, Literal upper) throws EFeatureEncoderException {
        return outside(lower.getValue(),upper.getValue());
    }
    
    public static Condition outside(EDataType type, Literal lower, Literal upper) throws EFeatureEncoderException {
        return outside(DataBuilder.toValue(type, lower),DataBuilder.toValue(type, upper));
    } 
    
    public static Condition outside(EDataType type, Object lower, Object upper) throws EFeatureEncoderException {
        return outside(DataBuilder.toValue(type, lower),DataBuilder.toValue(type, upper));
    }
    
    public static Condition outside(Object lower, Object upper) throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        if (DataTypes.isNumeric(lower)) {
            return outside((Number) lower, (Number) upper);
        } else if (DataTypes.isDate(lower)) {
            return outside((Date) lower, (Date) upper);
        } else if (DataTypes.isString(lower)) {
            return outside((String) lower, (String) upper);
        } else if (DataTypes.isCharacter(lower)) {
            return outside((Character) lower,(Character) upper);
        }
        throw new EFeatureEncoderException("OUTSIDE: Literals '" 
                + lower + "' and '" + upper + "' not supported");
    }    

    // ----------------------------------------------------- 
    //  Number comparison methods
    // -----------------------------------------------------

    public static NumberCondition<?> eq(Number number) throws EFeatureEncoderException {
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

    public static NumberCondition<?> ne(Number number) throws EFeatureEncoderException {
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

    public static NumberCondition<?> lt(Number number) throws EFeatureEncoderException {
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

    public static NumberCondition<?> le(Number number)
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

    public static NumberCondition<?> gt(Number number) throws EFeatureEncoderException {
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

    public static NumberCondition<?> ge(Number number)
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
    
    public static Condition outside(Number lower, Number upper) throws EFeatureEncoderException {
        return new Not(between(lower, upper));
    }

    // ----------------------------------------------------- 
    //  Date comparison methods
    // -----------------------------------------------------

    public static DateCondition eq(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.equals(date);
    }

    public static DateCondition ne(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.notEquals(date);
    }

    public static DateCondition lt(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.lessThan(date);
    }

    public static DateCondition le(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.lessThanOrEquals(date);
    }

    public static DateCondition gt(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.greaterThan(date);
    }

    public static DateCondition ge(Date date) throws EFeatureEncoderException {
        isSane(date, "date", true);
        return DateCondition.greaterThanOrEquals(date);
    }

    public static DateCondition between(Date lower, Date upper) throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return DateCondition.between(lower, upper);
    }
    
    public static DateCondition outside(Date lower, Date upper) throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return DateCondition.outside(lower, upper);
    }
    
    
    // ----------------------------------------------------- 
    //  Boolean comparison methods
    // -----------------------------------------------------

    public static BooleanCondition eq(Boolean state) throws EFeatureEncoderException {
        isSane(state, "state", true);
        return new BooleanCondition(state);
    }

    public static BooleanCondition ne(Boolean state) throws EFeatureEncoderException {
        isSane(state, "state", true);
        return new BooleanCondition(!state);
    }

    // ----------------------------------------------------- 
    //  String comparison methods
    // -----------------------------------------------------

    public static StringCondition eq(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.equals(str);
    }

    public static StringCondition ne(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.notEquals(str);
    }

    public static StringCondition lt(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.lessThan(str);
    }

    public static StringCondition le(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.lessThanOrEquals(str);
    }

    public static StringCondition gt(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.greaterThan(str);
    }

    public static StringCondition ge(String str) throws EFeatureEncoderException {
        isSane(str, "str", true);
        return StringCondition.greaterThanOrEquals(str);
    }

    public static StringCondition between(String lower, String upper)
            throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return StringCondition.between(lower, upper);
    }
    
    public static StringCondition outside(String lower, String upper) 
        throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return StringCondition.outside(lower, upper);
    }    

    public static StringRegularExpressionValue like(String pattern) throws EFeatureEncoderException {
        isSane(pattern, "pattern", true);
        return new StringRegularExpressionValue(pattern);
    }

    // ----------------------------------------------------- 
    //  Character comparison methods
    // -----------------------------------------------------

    public static CharacterCondition eq(Character c) throws EFeatureEncoderException {
        isSane(c, "c", true);
        return CharacterCondition.equals(c);
    }

    public static CharacterCondition ne(Character c) throws EFeatureEncoderException {
        isSane(c, "c", true);
        return CharacterCondition.notEquals(c);
    }

    public static CharacterCondition lt(Character c) throws EFeatureEncoderException {
        isSane(c, "c", true);
        return CharacterCondition.lessThan(c);
    }

    public static CharacterCondition le(Character c) throws EFeatureEncoderException {
        isSane(c, "c", true);
        return CharacterCondition.lessThanOrEquals(c);
    }

    public static CharacterCondition gt(Character c) throws EFeatureEncoderException {
        isSane(c, "c", true);
        return CharacterCondition.greaterThan(c);
    }

    public static CharacterCondition ge(Character c) throws EFeatureEncoderException {
        isSane(c, "c", true);
        return CharacterCondition.greaterThanOrEquals(c);
    }

    public static CharacterCondition between(Character lower, Character upper)
            throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return CharacterCondition.between(lower, upper);
    }
    
    public static CharacterCondition outside(Character lower, Character upper) 
        throws EFeatureEncoderException {
        isSameType(lower, "lower", upper, "upper", true);
        return CharacterCondition.outside(lower, upper);
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
