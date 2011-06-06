package org.geotools.data.efeature;

import java.util.Date;

import org.eclipse.emf.query.conditions.DataTypeCondition;

/**
 * A <code>Condition</code> object that tests for {@link Date} arguments. The arguments being
 * evaluated are adapted to a <code>Date</code> first using a <code>DateAdapter</code> and then
 * compared to the initialization value of this <code>DateCondition</code>. It evaluates to
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
public class DateCondition extends DataTypeCondition<Date> {

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
    protected Date upperBound;

    /** Whether the upper bound is inclusive. */
    protected boolean upperInclusive;

    /** The lower bound of a range condition. */
    protected Date lowerBound;

    /** Whether the lower bound is inclusive. */
    protected boolean lowerInclusive;

    /** The operator of a relational condition. */
    protected RelationalOperator operator;

    /**
     * Initializes me with a single date against which to test input values, assuming that they will
     * be {@link Date} objects. I am, by default, an {@linkplain #EQUAL_TO equality} test.
     * 
     * @param date - the {@link Date} object to match against input values
     * 
     * @since 1.2
     */
    public DateCondition(Date date) {
        this(date, EQUAL_TO, DateAdapter.DEFAULT);
    }

    /**
     * Initializes me with a single date against which to test input values, and an adapter to
     * convert those inputs to {@link Date} objects. I am, by default, an {@linkplain #EQUAL_TO
     * equality} test.
     * 
     * @param date - the {@link Date} object to match against input values
     * @param adapter - converts input values to {@link Date} objects
     * 
     * @since 1.2
     */
    public DateCondition(Date date, DateAdapter adapter) {
        this(date, BETWEEN, adapter);
    }

    /**
     * Initializes me with a {@link Date} object against which to test input values and a relational
     * operator to apply in comparisons. I assume that inputs are {@link Date} objects.
     * 
     * @param date - the {@link Date} object to match against input values
     * @param operator - the relational operator to test
     * 
     * @since 1.2
     */
    public DateCondition(Date date, RelationalOperator operator) {
        this(date, operator, DateAdapter.DEFAULT);
    }

    /**
     * Initializes me with a {@link Date} object against which to test input values, a relational
     * operator to apply in comparisons, and an adapter to convert those inputs to {@link Date}
     * objects.
     * 
     * @param date - the {@link Date} object to match against input values
     * @param operator - the relational operator to test
     * @param adapter - converts input values to {@link Date} objects
     * 
     * @since 1.2
     */
    public DateCondition(Date date, RelationalOperator operator, DateAdapter adapter) {

        super(date, adapter);

        switch (operator) {
        case EQUAL_TO:
            this.lowerBound = date;
            this.lowerInclusive = true;
            this.upperBound = date;
            this.upperInclusive = true;
            break;
        case NOT_EQUAL_TO:
            this.lowerBound = date;
            this.lowerInclusive = false;
            this.upperBound = date;
            this.upperInclusive = false;
            break;
        case LESS_THAN:
            this.upperBound = date;
            this.upperInclusive = false;
            break;
        case LESS_THAN_OR_EQUAL_TO:
            this.upperBound = date;
            this.upperInclusive = true;
            break;
        case GREATER_THAN:
            this.lowerBound = date;
            this.lowerInclusive = false;
            break;
        case GREATER_THAN_OR_EQUAL_TO:
            this.lowerBound = date;
            this.lowerInclusive = true;
            break;
        }

        this.operator = operator;
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, assuming that
     * they will be {@link Date} objects. I am, by default, a {@linkplain #BETWEEN between} test.
     * 
     * @param lowerBound - the lower bound to test
     * @param upperBound - the upper bound to test
     * 
     * @since 1.2
     */
    public DateCondition(Date lowerBound, Date upperBound) {
        this(lowerBound, true, upperBound, true, DateAdapter.DEFAULT);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, and an adapter
     * to convert those inputs to {@link Date} objects.
     * 
     * @param lowerBound - the lower bound to test
     * @param upperBound - the upper bound to test
     * @param adapter - converts input values to {@link Date} objects
     * 
     * @since 1.2
     */
    public DateCondition(Date lowerBound, Date upperBound, DateAdapter adapter) {
        this(lowerBound, true, upperBound, true, adapter);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, assuming that
     * they will be {@link Date} objects. I am, by default, a {@linkplain #BETWEEN between} test.
     * 
     * @param lowerBound - the lower bound to test
     * @param lowerInclusive - whether the lower bound is inclusive
     * @param upperBound - the upper bound to test
     * @param upperInclusive - whether the upper bound is inclusive
     * 
     * @since 1.2
     */
    public DateCondition(Date lowerBound, boolean lowerInclusive, Date upperBound,
            boolean upperInclusive) {
        this(lowerBound, lowerInclusive, upperBound, upperInclusive, DateAdapter.DEFAULT);
    }

    /**
     * Initializes me with upper and lower bounds against which to test input values, and an adapter
     * to convert those inputs to {@link Date} objects.
     * 
     * @param lowerBound - the lower bound to test
     * @param lowerInclusive - whether the lower bound is inclusive
     * @param upperBound - the upper bound to test
     * @param upperInclusive - whether the upper bound is inclusive
     * @param adapter - to convert input values to {@link Date} objects
     * 
     * @since 1.2
     */
    public DateCondition(Date lowerBound, boolean lowerInclusive, Date upperBound,
            boolean upperInclusive, DateAdapter adapter) {
        super(lowerBound, adapter);

        this.lowerBound = lowerBound;
        this.lowerInclusive = lowerInclusive;
        this.upperBound = upperBound;
        this.upperInclusive = upperInclusive;
        this.operator = BETWEEN;
    }

    /**
     * Obtains a condition checking for values equal to the specified {@link Date}.
     * 
     * @param date - a {@link Date} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static DateCondition equals(Date date) {
        return new DateCondition(date, EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values not equal to the specified {@link Date}.
     * 
     * @param date - a {@link Date} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static DateCondition notEquals(Date date) {
        return new DateCondition(date, NOT_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values less than the specified <tt>date</tt>.
     * 
     * @param date - a {@link Date} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static DateCondition lessThan(Date date) {
        return new DateCondition(date, LESS_THAN);
    }

    /**
     * Obtains a condition checking for values less than or equal to the specified <tt>date</tt>.
     * 
     * @param date - a {@link Date} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static DateCondition lessThanOrEquals(Date date) {
        return new DateCondition(date, LESS_THAN_OR_EQUAL_TO);
    }

    /**
     * Obtains a condition checking for values greater than the specified <tt>date</tt>.
     * 
     * @param date - a {@link Date} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static DateCondition greaterThan(Date date) {
        return new DateCondition(date, GREATER_THAN);
    }

    /**
     * Obtains a condition checking for values greater than or equal to the specified <tt>date</tt>.
     * 
     * @param date - a {@link Date} to check for
     * @return a condition that does the checking
     * 
     * @since 1.2
     */
    public static DateCondition greaterThanOrEquals(Date date) {
        return new DateCondition(date, GREATER_THAN_OR_EQUAL_TO);
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
    public static DateCondition between(Date lowerBound, Date upperBound) {
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
    public static DateCondition between(Date lowerBound, boolean lowerInclusive, Date upperBound,
            boolean upperInclusive) {
        return new DateCondition(lowerBound, lowerInclusive, upperBound, upperInclusive);
    }

    /**
     * Tests if the argument's value equals/in-range the initialization date(s)
     * 
     * @param date The <code>Short</code> object whose value will be used in testing
     * @return true if values are equal/in-range, false otherwise
     */
    public boolean isSatisfied(Date date) {
        return isSatisfied(date.getTime());
    }

    /**
     * Tests if the argument's value equals/in-range the initialization date(s)
     * 
     * @param date The short value which will be used in testing
     * @return true if values are equal/in-range, false otherwise
     */
    public boolean isSatisfied(long date) {
        return (date >= lowerBound.getTime()) && (date <= upperBound.getTime());
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
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.value.compareTo(date) == 0);
            };
        },
        NOT_EQUAL_TO {
            @Override
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.value.compareTo(date) != 0);
            };
        },
        LESS_THAN {
            @Override
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.value.compareTo(date) > 0);
            };
        },
        LESS_THAN_OR_EQUAL_TO {
            @Override
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.value.compareTo(date) >= 0);
            };
        },
        GREATER_THAN {
            @Override
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.value.compareTo(date) < 0);
            };
        },
        GREATER_THAN_OR_EQUAL_TO {
            @Override
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.value.compareTo(date) <= 0);
            };
        },
        BETWEEN {
            @Override
            boolean isSatisfied(DateCondition cond, Date date) {
                return (cond.lowerInclusive ? (cond.lowerBound.compareTo(date) <= 0)
                        : (cond.lowerBound.compareTo(date) < 0))
                        && (cond.upperInclusive ? (cond.upperBound.compareTo(date) >= 0)
                                : (cond.upperBound.compareTo(date) > 0));
            };
        };

        /**
         * Tests the specified condition against an input value.
         * 
         * @param cond - the {@link Date} condition to test against
         * @param date - the input value
         * 
         * @return the result of my specific test
         */
        abstract boolean isSatisfied(DateCondition cond, Date date);
    }

}
