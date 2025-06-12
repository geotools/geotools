/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import java.util.Collection;
import java.util.Objects;
import org.geotools.api.filter.FilterVisitor;
import org.geotools.api.filter.PropertyIsLike;

/**
 * Defines a like filter, which checks to see if an attribute matches a REGEXP.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class LikeFilterImpl extends AbstractFilter implements PropertyIsLike {

    /** The attribute value, which must be an attribute expression. */
    private org.geotools.api.filter.expression.Expression attribute = null;

    /** The (limited) REGEXP pattern. */
    private String pattern = null;

    /** The single wildcard for the REGEXP pattern. */
    private String wildcardSingle = ".?";

    /** The multiple wildcard for the REGEXP pattern. */
    private String wildcardMulti = ".*";

    /** The escape sequence for the REGEXP pattern. */
    private String escape = "\\";

    /** the pattern compiled into a java regex */
    private Pattern compPattern = null;

    /** Used to indicate if case should be ignored or not */
    boolean matchingCase;

    /** Used to indicate action with multiple values * */
    protected MatchAction matchAction;

    /**
     * Given OGC PropertyIsLike Filter information, construct an SQL-compatible 'like' pattern.
     *
     * <p>SQL % --> match any number of characters _ --> match a single character
     *
     * <p>NOTE; the SQL command is 'string LIKE pattern [ESCAPE escape-character]' We could re-define the escape
     * character, but I'm not doing to do that in this code since some databases will not handle this case.
     *
     * <p>Method: 1.
     *
     * <p>Examples: ( escape ='!', multi='*', single='.' ) broadway* -> 'broadway%' broad_ay -> 'broad_ay' broadway ->
     * 'broadway'
     *
     * <p>broadway!* -> 'broadway*' (* has no significance and is escaped) can't -> 'can''t' ( ' escaped for SQL
     * compliance)
     *
     * <p>NOTE: we also handle "'" characters as special because they are end-of-string characters. SQL will convert '
     * to '' (double single quote).
     *
     * <p>NOTE: we dont handle "'" as a 'special' character because it would be too confusing to have a special char as
     * another special char. Using this will throw an error (IllegalArgumentException).
     */
    @Deprecated
    public static String convertToSQL92(char escape, char multi, char single, boolean matchCase, String pattern)
            throws IllegalArgumentException {
        return convertToSQL92(escape, multi, single, matchCase, pattern, true);
    }

    /**
     * Given OGC PropertyIsLike Filter information, construct an SQL-compatible 'like' pattern.
     *
     * <p>SQL % --> match any number of characters _ --> match a single character
     *
     * <p>NOTE; the SQL command is 'string LIKE pattern [ESCAPE escape-character]' We could re-define the escape
     * character, but I'm not doing to do that in this code since some databases will not handle this case.
     *
     * <p>Method: 1.
     *
     * <p>Examples: ( escape ='!', multi='*', single='.' ) broadway* -> 'broadway%' broad_ay -> 'broad_ay' broadway ->
     * 'broadway'
     *
     * <p>broadway!* -> 'broadway*' (* has no significance and is escaped) can't -> 'can''t' ( ' escaped for SQL
     * compliance)
     *
     * <p>NOTE: when the escapeSingleQuote parameter is false, this method will not convert ' to '' (double single
     * quote) and it is the caller's responsibility to ensure that the resulting pattern is used safely in SQL queries.
     *
     * <p>NOTE: we dont handle "'" as a 'special' character because it would be too confusing to have a special char as
     * another special char. Using this will throw an error (IllegalArgumentException).
     */
    public static String convertToSQL92(
            char escape, char multi, char single, boolean matchCase, String pattern, boolean escapeSingleQuote) {
        if ((escape == '\'') || (multi == '\'') || (single == '\''))
            throw new IllegalArgumentException("do not use single quote (') as special char!");

        StringBuilder result = new StringBuilder(pattern.length() + 5);
        for (int i = 0; i < pattern.length(); i++) {
            char chr = pattern.charAt(i);
            if (chr == escape) {
                // emit the next char and skip it
                if (i != (pattern.length() - 1)) result.append(pattern.charAt(i + 1)); //
                i++; // skip next char
            } else if (chr == single) {
                result.append('_');
            } else if (chr == multi) {
                result.append('%');
            } else if (chr == '\'' && escapeSingleQuote) {
                result.append('\'');
                result.append('\'');
            } else {
                result.append(matchCase ? chr : Character.toUpperCase(chr));
            }
        }

        return result.toString();
    }

    /** see convertToSQL92 */
    @Deprecated
    public String getSQL92LikePattern() throws IllegalArgumentException {
        if (escape.length() != 1) {
            throw new IllegalArgumentException("Like Pattern --> escape char should be of length exactly 1");
        }
        if (wildcardSingle.length() != 1) {
            throw new IllegalArgumentException("Like Pattern --> wildcardSingle char should be of length exactly 1");
        }
        if (wildcardMulti.length() != 1) {
            throw new IllegalArgumentException("Like Pattern --> wildcardMulti char should be of length exactly 1");
        }
        return LikeFilterImpl.convertToSQL92(
                escape.charAt(0), wildcardMulti.charAt(0), wildcardSingle.charAt(0), matchingCase, pattern, true);
    }

    public void setWildCard(String wildCard) {
        if (wildCard == null || wildCard.isEmpty() || wildCard.length() > 1) {
            throw new IllegalArgumentException("Like Pattern --> wildcardMulti char should be of length exactly 1");
        }
        this.wildcardMulti = wildCard;
        compPattern = null;
    }

    public void setSingleChar(String singleChar) {
        if (singleChar == null || singleChar.length() != 1) {
            throw new IllegalArgumentException("Like Pattern --> wildcardSingle char should be of length exactly 1");
        }
        this.wildcardSingle = singleChar;
        compPattern = null;
    }

    public void setEscape(String escape) {

        if (escape.startsWith("\\")) {
            if (escape.length() < 1 || escape.length() > 3) {
                throw new IllegalArgumentException(
                        "Like Pattern --> escape char should be of length exactly 1, not " + escape.length());
            }
        } else if (!escape.isEmpty() && escape.length() > 1) {
            throw new IllegalArgumentException(
                    "Like Pattern --> escape char should be of length exactly 1, not " + escape.length());
        }
        this.escape = escape;
        compPattern = null;
    }

    public void setMatchCase(boolean matchingCase) {
        this.matchingCase = matchingCase;
        compPattern = null;
    }

    @Override
    public boolean isMatchingCase() {
        return matchingCase;
    }

    @Override
    public MatchAction getMatchAction() {
        return matchAction;
    }

    public void setMatchingCase(boolean matchingCase) {
        this.matchingCase = matchingCase;
    }

    private Matcher getMatcher(String string) {
        if (compPattern == null) {
            String pattern = new LikeToRegexConverter(this).getPattern();
            compPattern = isMatchingCase()
                    ? Pattern.compile(pattern)
                    : Pattern.compile(pattern, Pattern.CASE_INSENSITIVE /* | Pattern.UNICODE_CASE */);
        }
        return compPattern.matcher(string);
    }

    /** Constructor which flags the operator as like. */
    protected LikeFilterImpl() {
        this(MatchAction.ANY);
    }

    public LikeFilterImpl(
            org.geotools.api.filter.expression.Expression expr,
            String pattern,
            String wildcardMulti,
            String wildcardSingle,
            String escape) {
        this();
        setExpression(expr);
        setLiteral(pattern);
        setWildCard(wildcardMulti);
        setSingleChar(wildcardSingle);
        setEscape(escape);
    }

    protected LikeFilterImpl(MatchAction matchAction) {
        this.matchAction = matchAction;
    }

    public LikeFilterImpl(
            org.geotools.api.filter.expression.Expression expr,
            String pattern,
            String wildcardMulti,
            String wildcardSingle,
            String escape,
            MatchAction matchAction) {
        this(matchAction);
        setExpression(expr);
        setLiteral(pattern);
        setWildCard(wildcardMulti);
        setSingleChar(wildcardSingle);
        setEscape(escape);
    }

    /**
     * Gets the expression for hte filter.
     *
     * <p>This method calls th deprecated {@link #getValue()} for backwards compatability with subclasses.
     */
    @Override
    public org.geotools.api.filter.expression.Expression getExpression() {
        return attribute;
    }

    public void setExpression(org.geotools.api.filter.expression.Expression e) {
        this.attribute = e;
    }

    /** Returns the pattern. */
    @Override
    public String getLiteral() {
        return this.pattern;
    }

    /** Sets the pattern. */
    public void setLiteral(String literal) {
        this.pattern = literal;
        compPattern = null;
    }

    /**
     * Determines whether or not a given feature matches this pattern.
     *
     * @param feature Specified feature to examine.
     * @return Flag confirming whether or not this feature is inside the filter.
     * @task REVISIT: could the pattern be null such that a null = null?
     */
    @Override
    public boolean evaluate(Object feature) {
        // Checks to ensure that the attribute has been set
        if (attribute == null) {
            return false;
        }
        // Note that this converts the attribute to a string
        //  for comparison.  Unlike the math or geometry filters, which
        //  require specific types to function correctly, this filter
        //  using the mandatory string representation in Java
        // Of course, this does not guarantee a meaningful result, but it
        //  does guarantee a valid result.
        // LOGGER.finest("pattern: " + pattern);
        // LOGGER.finest("string: " + attribute.getValue(feature));
        // return attribute.getValue(feature).toString().matches(pattern);

        Object value = eval(attribute, feature);

        if (null == value) {
            return false;
        }

        // NC - support multiple values
        if (value instanceof Collection) {
            int count = 0;

            for (Object element : (Collection) value) {
                Matcher matcher = getMatcher(element.toString());
                boolean temp = matcher.matches();
                if (temp) {
                    count++;
                }

                switch (matchAction) {
                    case ONE:
                        if (count > 1) return false;
                        break;
                    case ALL:
                        if (!temp) return false;
                        break;
                    case ANY:
                        if (temp) return true;
                        break;
                }
            }
            switch (matchAction) {
                case ONE:
                    return count == 1;
                case ALL:
                    return true;
                case ANY:
                    return false;
                default:
                    return false;
            }
        } else {
            Matcher matcher = getMatcher(value.toString());
            return matcher.matches();
        }
    }

    /**
     * Return this filter as a string.
     *
     * @return String representation of this like filter.
     */
    @Override
    public String toString() {
        return "[ " + attribute.toString() + " is like " + pattern + " ]";
    }

    /**
     * Getter for property escape.
     *
     * @return Value of property escape.
     */
    @Override
    public java.lang.String getEscape() {
        return escape;
    }

    /**
     * Getter for property wildcardMulti
     *
     * @see org.geotools.api.filter.PropertyIsLike#getWildCard().
     */
    @Override
    public String getWildCard() {
        return wildcardMulti;
    }

    /**
     * Getter for property wildcardSingle.
     *
     * @see org.geotools.api.filter.PropertyIsLike#getSingleChar()().
     */
    @Override
    public String getSingleChar() {
        return wildcardSingle;
    }

    /**
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this filter.
     * Checks to make sure the filter types, the value, and the pattern are the same. &
     *
     * @param o - the object to compare this LikeFilter against.
     * @return true if specified object is equal to this filter; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LikeFilterImpl that = (LikeFilterImpl) o;
        return Objects.equals(attribute, that.attribute) && Objects.equals(pattern, that.pattern);
    }

    /**
     * Override of hashCode method.
     *
     * @return the hash code for this like filter implementation.
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = (37 * result) + ((attribute == null) ? 0 : attribute.hashCode());
        result = (37 * result) + ((pattern == null) ? 0 : pattern.hashCode());

        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance. Typicaly used by Filter decoders, but may
     * also be used by any thing which needs infomration from filter structure. Implementations should always call:
     * visitor.visit(this); It is importatant that this is not left to a parent class unless the parents API is
     * identical.
     *
     * @param visitor The visitor which requires access to this filter, the method must call visitor.visit(this);
     */
    @Override
    public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
