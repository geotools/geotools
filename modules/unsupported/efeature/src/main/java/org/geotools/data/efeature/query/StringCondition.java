package org.geotools.data.efeature.query;

import org.eclipse.emf.query.conditions.strings.StringAdapter;

/**
 * A <code>Condition</code> object that tests for string arguments. The arguments being evaluated
 * are adapted to a <code>String</code> first using a <code>StringAdapter</code> and then compared
 * to the initialization value of this <code>StringCondition</code>. It evaluates to
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
public class StringCondition extends org.eclipse.emf.query.conditions.strings.StringCondition {

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

    /** The upper bound of a range condition. */
    protected String upperBound;

    /** Whether the upper bound is inclusive. */
    protected boolean upperInclusive;

    /** The lower bound of a range condition. */
    protected String lowerBound;

    /** Whether the lower bound is inclusive. */
    protected boolean lowerInclusive;

    /** The operator of a relational condition. */
    protected RelationalOperator operator;

    /**
     * Initializes me with a single date against which to test input values, assuming that they will
     * be {@link String} objects. I am, by default, an {@linkplain #EQUAL_TO equality} test.
     * 
     * @param str - the {@link String} object to match against input values
     * 
     * @since 1.2
     */
    public StringCondition(String str) {
        this(str, EQUAL_TO, StringAdapter.DEFAULT);
    }

    /**
     * Initializes me with a single date against which to test input values, and an adapter to
     * convert those inputs to {@link String} objects. I am, by default, an {@linkplain #EQUAL_TO
     * equality} test.
     * 
     * @param str - the {@link String} object to match against input values
     * @param adapter - converts input values to {@link String} objects
     * 
     * @since 1.2
     */
    public StringCondition(String str, StringAdapter adapter) {
        this(str, BETWEEN, adapter);
    }

    /**
     * Initializes me with a {@link String} object against which to test input values and a
     * relational operator to apply in comparisons. I assume that inputs are {@link String} objects.
     * 
     * @param str - the {@link String} object to match against input values
     * @param operator - the relational operator to test
     * 
     * @since 1.2
     */
    public StringCondition(String str, RelationalOperator operator) {
        this(str, operator, StringAdapter.DEFAULT);
    }

    /**
     * Initializes me with a {@link String} object against which to test input values, a relational
     * operator to apply in comparisons, and an adapter to convert those inputs to {@link String}
     * objects.
     * 
     * @param str - the {@link String} object to match against input values
     * @param operator - the relational operator to test
     * @param adapter - converts input values to {@link String} objects
     * 
     * @since 1.2
     */
    public StringCondition(String str, RelationalOperator operator, StringAdapter adapter) {

        super(str, adapter);

        switch (operator) {
        case EQUAL_TO:
            this.lowerBound = str;
            this.lowerInclusive = true;
            this.upperBound = str;
            this.upperInclusive = true;
            break;
        case NOT_EQUAL_TO:
            this.lowerBound = str;
            this.lowerInclusive = false;
            this.upperBound = str;
            this.upperInclusive = false;
            break;
        case LESS_THAN:
            this.upperBound = str;
            this.upperInclusive = false;
            break;
        case LESS_THAN_OR_EQUAL_TO:
            this.upperBound = str;
            this.upperInclusive = true;
            break;
        case GREATER_THAN:
            this.lowerBound = str;
            this.lowerInclusive = false;
            break;
        case GREATER_THAN_OR_EQUAL_TO:
            this.lowerBound = str;
            this.lowerInclusive = true;
            break;
        }

        this.operator = operator;
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, assuming that
     * they will be {@link String} objects. I am, by default, a {@linkplain #BETWEEN between} test.
     * 
     * @param lowerBound - the lower bound to test
     * @param upperBound - the upper bound to test
     * 
     * @since 1.2
     */
    public StringCondition(String lowerBound, String upperBound) {
        this(lowerBound, true, upperBound, true, StringAdapter.DEFAULT);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, and an adapter
     * to convert those inputs to {@link String} objects.
     * 
     * @param lowerBound - the lower bound to test
     * @param upperBound - the upper bound to test
     * @param adapter - converts input values to {@link String} objects
     * 
     * @since 1.2
     */
    public StringCondition(String lowerBound, String upperBound, StringAdapter adapter) {
        this(lowerBound, true, upperBound, true, adapter);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, assuming that
     * they will be {@link String} objects. I am, by default, a {@linkplain #BETWEEN between} test.
     * 
     * @param lowerBound - the lower bound to test
     * @param lowerInclusive - whether the lower bound is inclusive
     * @param upperBound - the upper bound to test
     * @param upperInclusive - whether the upper bound is inclusive
     * 
     * @since 1.2
     */
    public StringCondition(String lowerBound, boolean lowerInclusive, String upperBound,
            boolean upperInclusive) {
        this(lowerBound, lowerInclusive, upperBound, upperInclusive, StringAdapter.DEFAULT);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, and an adapter
     * to convert those inputs to {@link String} objects.
     * 
     * @param lowerBound - the lower bound to test
     * @param lowerInclusive - whether the lower bound is inclusive
     * @param upperBound - the upper bound to test
     * @param upperInclusive - whether the upper bound is inclusive
     * @param adapter - to convert input values to {@link String} objects
     * 
     * @since 1.2
     */
    public StringCondition(String lowerBound, boolean lowerInclusive, String upperBound,
            boolean upperInclusive, StringAdapter adapter) {
        super(lowerBound, adapter);

        this.lowerBound = lowerBound;
        this.lowerInclusive = lowerInclusive;
        this.upperBound = upperBound;
        this.upperInclusive = upperInclusive;
        this.operator = BETWEEN;
    }

    /**
     * Obtains a condition checking for values equal to the specified {@link String}.
     * 
     * @param str - a {@link String} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition equals(String str) {
        return new StringCondition(str, EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values not equal to the specified {@link String}.
     * 
     * @param str - a {@link String} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition notEquals(String str) {
        return new StringCondition(str, NOT_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values less than the specified <tt>str</tt>.
     * 
     * @param str - a {@link String} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition lessThan(String str) {
        return new StringCondition(str, LESS_THAN);
    }

    /**
     * Obtains a condition checking for values less than or equal to the specified <tt>str</tt>.
     * 
     * @param str - a {@link String} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition lessThanOrEquals(String str) {
        return new StringCondition(str, LESS_THAN_OR_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values greater than the specified <tt>str</tt>.
     * 
     * @param str - a {@link String} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition greaterThan(String str) {
        return new StringCondition(str, GREATER_THAN);
    }

    /**
     * Obtains a condition checking for values greater than or equal to the specified <tt>str</tt>.
     * 
     * @param str - a {@link String} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition greaterThanOrEquals(String str) {
        return new StringCondition(str, GREATER_THAN_OR_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values in the range to the specified <tt>lowerBound</tt> and
     * <tt>upperBound</tt> (inclusive).
     * 
     * @param lowerBound the lower bound of numbers to check for (inclusive)
     * @param upperBound the upper bound of numbers to check for (inclusive)
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition between(String lowerBound, String upperBound) {
        return between(lowerBound, true, upperBound, true);
    }

    /**
     * Obtains a condition checking for values in the range to the specified <tt>lowerBound</tt> and
     * <tt>upperBound</tt>.
     * 
     * @param lowerBound the lower bound of numbers to check for
     * @param lowerInclusive whether the lower bound is inclusive
     * @param upperBound the upper bound of numbers to check for
     * @param upperInclusive whether the upper bound is inclusive
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static StringCondition between(String lowerBound, boolean lowerInclusive,
            String upperBound, boolean upperInclusive) {
        return new StringCondition(lowerBound, lowerInclusive, upperBound, upperInclusive);
    }

    /**
     * Tests if the argument's value equals/in-range the initialization str(s)
     * 
     * @param str The <code>Short</code> object whose value will be used in testing
     * @return true if values are equal/in-range, false otherwise
     */
    @Override
    public boolean isSatisfied(String str) {
        return (lowerBound.compareTo(str) >= 0) && (upperBound.compareTo(str) <= 0);
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
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.value.compareTo(str) == 0);
            };
        },
        NOT_EQUAL_TO {
            @Override
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.value.compareTo(str) != 0);
            };
        },
        LESS_THAN {
            @Override
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.value.compareTo(str) > 0);
            };
        },
        LESS_THAN_OR_EQUAL_TO {
            @Override
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.value.compareTo(str) >= 0);
            };
        },
        GREATER_THAN {
            @Override
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.value.compareTo(str) < 0);
            };
        },
        GREATER_THAN_OR_EQUAL_TO {
            @Override
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.value.compareTo(str) <= 0);
            };
        },
        BETWEEN {
            @Override
            boolean isSatisfied(StringCondition cond, String str) {
                return (cond.lowerInclusive ? (cond.lowerBound.compareTo(str) <= 0)
                        : (cond.lowerBound.compareTo(str) < 0))
                        && (cond.upperInclusive ? (cond.upperBound.compareTo(str) >= 0)
                                : (cond.upperBound.compareTo(str) > 0));
            };
        };

        /**
         * Tests the specified condition against an input value.
         * 
         * @param cond - the {@link String} condition to test against
         * @param str - the input value
         * 
         * @return the result of my specific test
         */
        abstract boolean isSatisfied(StringCondition cond, String str);
    }

}
