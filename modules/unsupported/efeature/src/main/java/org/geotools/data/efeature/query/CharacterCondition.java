package org.geotools.data.efeature.query;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;
import org.geotools.data.efeature.adapters.CharacterAdapter;

/**
 * A {@link Condition} object that tests for {@link Character} arguments. The arguments being evaluated
 * are adapted to a <code>Character</code> first using a {@link CharacterAdapter} and then compared
 * to the initialization value of this <code>CharacterAdapter</code>. It evaluates to
 * <code>true</code> when the values are equal or if the value is within the range of supplied
 * lower-bound and upper-bound.
 * <p>
 * This implementation is based on the {@link NumberCondition} source code by Christian W. Damus
 * (cdamus), IBM.
 * </p>
 * 
 * @author kengu
 * 
 */
public class CharacterCondition extends Condition {

    /** The numeric {@literal =} operator. */
    public static RelationalOperator EQUAL_TO = RelationalOperator.EQUAL_TO;

    /** The numeric {@literal !=} operator. */
    public static RelationalOperator NOT_EQUAL_TO = RelationalOperator.NOT_EQUAL_TO;

    /** The numeric {@literal <} operator. */
    public static RelationalOperator LESS_THAN = RelationalOperator.LESS_THAN;

    /** The numeric {@literal <=} operator. */
    public static RelationalOperator LESS_THAN_OR_EQUAL_TO = RelationalOperator.LESS_THAN_OR_EQUAL_TO;

    /** The numeric {@literal >} operator. */
    public static RelationalOperator GREATER_THAN = RelationalOperator.GREATER_THAN;

    /** The numeric {@literal >=} operator. */
    public static RelationalOperator GREATER_THAN_OR_EQUAL_TO = RelationalOperator.GREATER_THAN_OR_EQUAL_TO;

    /** The numeric "between" operator. */
    public static RelationalOperator BETWEEN = RelationalOperator.BETWEEN;

    /** The numeric "outside" operator. */
    public static RelationalOperator OUTSIDE = RelationalOperator.OUTSIDE;
    
    /** The upper bound of a range condition. */
    protected Character upper;

    /** Whether the upper bound is inclusive. */
    protected boolean upperInclusive;

    /** The lower bound of a range condition. */
    protected Character lower;

    /** Whether the lower bound is inclusive. */
    protected boolean lowerInclusive;

    /** The operator of a relational condition. */
    protected RelationalOperator operator;
    
    /** The {@link IDataTypeAdapter character} adapter. */
    protected CharacterAdapter adapter;
    
    /** The condition value. */
    protected Character value;

    /**
     * Initializes me with a single date against which to test input values, assuming that they will
     * be {@link Character} objects. I am, by default, an {@linkplain #EQUAL_TO equality} test.
     * 
     * @param c - the {@link Character} object to match against input values
     * 
     * @since 1.2
     */
    public CharacterCondition(Character c) {
        this(c, EQUAL_TO, CharacterAdapter.DEFAULT);
    }

    /**
     * Initializes me with a single date against which to test input values, and an adapter to
     * convert those inputs to {@link Character} objects. I am, by default, an {@linkplain #EQUAL_TO
     * equality} test.
     * 
     * @param c - the {@link Character} object to match against input values
     * @param adapter - converts input values to {@link Character} objects
     * 
     * @since 1.2
     */
    public CharacterCondition(Character c, CharacterAdapter adapter) {
        this(c, EQUAL_TO, adapter);
    }

    /**
     * Initializes me with a {@link Character} object against which to test input values and a
     * relational operator to apply in comparisons. I assume that inputs are {@link Character} objects.
     * 
     * @param c - the {@link Character} object to match against input values
     * @param operator - the relational operator to test
     * 
     * @since 1.2
     */
    public CharacterCondition(Character c, RelationalOperator operator) {
        this(c, operator, CharacterAdapter.DEFAULT);
    }

    /**
     * Initializes me with a {@link Character} object against which to test input values, a relational
     * operator to apply in comparisons, and an adapter to convert those inputs to {@link Character}
     * objects.
     * 
     * @param c - the {@link Character} object to match against input values
     * @param operator - the relational operator to test
     * @param adapter - converts input values to {@link Character} objects
     * 
     * @since 1.2
     */
    public CharacterCondition(Character c, RelationalOperator operator, CharacterAdapter adapter) {

        this.value = c;
        this.adapter = adapter;

        switch (operator) {
        case EQUAL_TO:
            this.lower = c;
            this.lowerInclusive = true;
            this.upper = c;
            this.upperInclusive = true;
            break;
        case NOT_EQUAL_TO:
            this.lower = c;
            this.lowerInclusive = false;
            this.upper = c;
            this.upperInclusive = false;
            break;
        case LESS_THAN:
            this.upper = c;
            this.upperInclusive = false;
            break;
        case LESS_THAN_OR_EQUAL_TO:
            this.upper = c;
            this.upperInclusive = true;
            break;
        case GREATER_THAN:
            this.lower = c;
            this.lowerInclusive = false;
            break;
        case GREATER_THAN_OR_EQUAL_TO:
            this.lower = c;
            this.lowerInclusive = true;
            break;
        }

        this.operator = operator;
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, assuming that
     * they will be {@link Character} objects. I am, by default, a {@linkplain #BETWEEN between} test.
     * @param between - if <code>true</code>, use the {@link #BETWEEN} operator. Otherwise use the {@link #OUTSIDE} operator
     * @param lower - the lower bound to test
     * @param upper - the upper bound to test
     * 
     * @since 1.2
     */
    public CharacterCondition(boolean between, Character lower, Character upper) {
        this(between, lower, true, upper, true, CharacterAdapter.DEFAULT);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, and an adapter
     * to convert those inputs to {@link Character} objects.
     * @param between - if <code>true</code>, use the {@link #BETWEEN} operator. Otherwise use the {@link #OUTSIDE} operator
     * @param lower - the lower bound to test
     * @param upper - the upper bound to test
     * @param adapter - converts input values to {@link Character} objects
     * 
     * @since 1.2
     */
    public CharacterCondition(boolean between, Character lower, Character upper, CharacterAdapter adapter) {
        this(between, lower, true, upper, true, adapter);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, assuming that
     * they will be {@link Character} objects.
     * @param between - if <code>true</code>, use the {@link #BETWEEN} operator. Otherwise use the {@link #OUTSIDE} operator
     * @param lower - the lower bound to test
     * @param lowerInclusive - whether the lower bound is inclusive
     * @param upper - the upper bound to test
     * @param upperInclusive - whether the upper bound is inclusive
     * 
     * @since 1.2
     */
    public CharacterCondition(boolean between, Character lower, boolean lowerInclusive,
            Character upper, boolean upperInclusive) {
        this(between, lower, lowerInclusive, upper, upperInclusive, CharacterAdapter.DEFAULT);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, and an adapter
     * to convert those inputs to {@link Character} objects.
     * @param between - if <code>true</code>, use the {@link #BETWEEN} operator. Otherwise use the {@link #OUTSIDE} operator
     * @param lower - the lower bound to test
     * @param lowerInclusive - whether the lower bound is inclusive
     * @param upper - the upper bound to test
     * @param upperInclusive - whether the upper bound is inclusive
     * @param adapter - to convert input values to {@link Character} objects
     * 
     * @since 1.2
     */
    public CharacterCondition(boolean between, Character lower, boolean lowerInclusive,
            Character upper, boolean upperInclusive, CharacterAdapter adapter) {
        
        this.value = lower;
        this.adapter = adapter;

        this.lower = lower;
        this.lowerInclusive = lowerInclusive;
        this.upper = upper;
        this.upperInclusive = upperInclusive;
        this.operator = between ? BETWEEN : OUTSIDE;
    }

    /**
     * Obtains a condition checking for values equal to the specified {@link Character}.
     * 
     * @param c - a {@link Character} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition equals(Character c) {
        return new CharacterCondition(c, EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values not equal to the specified {@link Character}.
     * 
     * @param c - a {@link Character} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition notEquals(Character c) {
        return new CharacterCondition(c, NOT_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values less than the specified <tt>c</tt>.
     * 
     * @param c - a {@link Character} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition lessThan(Character c) {
        return new CharacterCondition(c, LESS_THAN);
    }

    /**
     * Obtains a condition checking for values less than or equal to the specified <tt>c</tt>.
     * 
     * @param c - a {@link Character} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition lessThanOrEquals(Character c) {
        return new CharacterCondition(c, LESS_THAN_OR_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values greater than the specified <tt>c</tt>.
     * 
     * @param c - a {@link Character} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition greaterThan(Character c) {
        return new CharacterCondition(c, GREATER_THAN);
    }

    /**
     * Obtains a condition checking for values greater than or equal to the specified <tt>c</tt>.
     * 
     * @param c - a {@link Character} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition greaterThanOrEquals(Character c) {
        return new CharacterCondition(c, GREATER_THAN_OR_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values in the range to the specified <tt>lower</tt> and
     * <tt>upper</tt> (inclusive).
     * 
     * @param lower the lower bound of numbers to check for (inclusive)
     * @param upper the upper bound of numbers to check for (inclusive)
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition between(Character lower, Character upper) {
        return between(lower, true, upper, true);
    }

    /**
     * Obtains a condition checking for values in the range to the specified <tt>lower</tt> and
     * <tt>upper</tt>.
     * 
     * @param lower the lower bound of numbers to check for
     * @param lowerInclusive whether the lower bound is inclusive
     * @param upper the upper bound of numbers to check for
     * @param upperInclusive whether the upper bound is inclusive
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition between(Character lower, boolean lowerInclusive,
            Character upper, boolean upperInclusive) {
        return new CharacterCondition(true, lower, lowerInclusive, upper, upperInclusive);
    }

    /**
     * Obtains a condition checking for values in the range to the specified <tt>lower</tt> and
     * <tt>upper</tt> (inclusive).
     * 
     * @param lower the lower bound of numbers to check for (inclusive)
     * @param upper the upper bound of numbers to check for (inclusive)
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition outside(Character lower, Character upper) {
        return outside(lower, true, upper, true);
    }

    /**
     * Obtains a condition checking for values in the range to the specified <tt>lower</tt> and
     * <tt>upper</tt>.
     * 
     * @param lower the lower bound of numbers to check for
     * @param lowerInclusive whether the lower bound is inclusive
     * @param upper the upper bound of numbers to check for
     * @param upperInclusive whether the upper bound is inclusive
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static CharacterCondition outside(Character lower, boolean lowerInclusive,
            Character upper, boolean upperInclusive) {
        return new CharacterCondition(false, lower, lowerInclusive, upper, upperInclusive);
    }
    
    /**
     * Tests if the argument's value equals/in-range the initialization c(s)
     * 
     * @param c The <code>Short</code> object whose value will be used in testing
     * @return true if values are equal/in-range, false otherwise
     */
    public boolean isSatisfied(Character c) {
        return (lower.compareTo(c) >= 0) && (upper.compareTo(c) <= 0);
    }

    @Override
    public boolean isSatisfied(Object object) {
        return operator.isSatisfied(this, adapter.adapt(object));
    }

    /**
     * The relational operator that a {@link NumberCondition} applies to test input values against
     * its own value or, in the case of {@link #BETWEEN}, its upper and lower bounds.
     * 
     * @since 1.2
     */
    public static enum RelationalOperator {
        EQUAL_TO {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.value.compareTo(c) == 0);
            };
        },
        NOT_EQUAL_TO {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.value.compareTo(c) != 0);
            };
        },
        LESS_THAN {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.value.compareTo(c) > 0);
            };
        },
        LESS_THAN_OR_EQUAL_TO {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.value.compareTo(c) >= 0);
            };
        },
        GREATER_THAN {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.value.compareTo(c) < 0);
            };
        },
        GREATER_THAN_OR_EQUAL_TO {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.value.compareTo(c) <= 0);
            };
        },
        BETWEEN {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.lowerInclusive ? (cond.lower.compareTo(c) <= 0)
                        : (cond.lower.compareTo(c) < 0))
                        && (cond.upperInclusive ? (cond.upper.compareTo(c) >= 0)
                                : (cond.upper.compareTo(c) > 0));
            };
        },
        OUTSIDE {
            @Override
            boolean isSatisfied(CharacterCondition cond, Character c) {
                return (cond.lowerInclusive ? (cond.lower.compareTo(c) >= 0)
                        : (cond.lower.compareTo(c) > 0))
                        || (cond.upperInclusive ? (cond.upper.compareTo(c) <= 0)
                                : (cond.upper.compareTo(c) < 0));
            };
        };

        /**
         * Tests the specified condition against an input value.
         * 
         * @param cond - the {@link Character} condition to test against
         * @param c - the input value
         * 
         * @return the result of my specific test
         */
        abstract boolean isSatisfied(CharacterCondition cond, Character c);
    }

}
