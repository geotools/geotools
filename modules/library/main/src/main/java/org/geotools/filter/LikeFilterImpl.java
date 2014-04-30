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


import java.util.Collection;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opengis.filter.FilterVisitor;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

/**
 * Defines a like filter, which checks to see if an attribute matches a REGEXP.
 *
 * @author Rob Hranac, Vision for New York
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class LikeFilterImpl extends AbstractFilter implements PropertyIsLike {

    /** The attribute value, which must be an attribute expression. */
    private org.opengis.filter.expression.Expression attribute = null;

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
    
    /** Used to indicate action with multiple values **/
    protected MatchAction matchAction;
    
    /**
     * Given OGC PropertyIsLike Filter information, construct
     * an SQL-compatible 'like' pattern.
     * 
     *   SQL   % --> match any number of characters
     *         _ --> match a single character
     * 
     *    NOTE; the SQL command is 'string LIKE pattern [ESCAPE escape-character]'
     *    We could re-define the escape character, but I'm not doing to do that in this code
     *    since some databases will not handle this case.
     * 
     *   Method:
     *     1. 
     * 
     *  Examples: ( escape ='!',  multi='*',    single='.'  )
     *    broadway*  -> 'broadway%'
     *    broad_ay   -> 'broad_ay'
     *    broadway   -> 'broadway'
     *    
     *    broadway!* -> 'broadway*'  (* has no significance and is escaped)
     *    can't      -> 'can''t'     ( ' escaped for SQL compliance)
     * 
     * 
     *  NOTE: we also handle "'" characters as special because they are
     *        end-of-string characters.  SQL will convert ' to '' (double single quote).
     * 
     *  NOTE: we dont handle "'" as a 'special' character because it would be 
     *        too confusing to have a special char as another special char.
     *        Using this will throw an error  (IllegalArgumentException).
     * 
     * @param escape
     * @param multi
     * @param single
     * @param pattern
     * 
     */
    public static String convertToSQL92(char escape, char multi, char single, boolean matchCase, 
            String pattern ) throws IllegalArgumentException {
        if ( (escape == '\'') || (multi  == '\'') || (single  == '\'')  )
            throw new IllegalArgumentException("do not use single quote (') as special char!");
        
         StringBuffer result = new StringBuffer(pattern.length()+5);
         for (int i = 0; i < pattern.length(); i++) 
         {
            char chr = pattern.charAt(i);
            if (chr == escape)
            {
                // emit the next char and skip it
                if (i!= (pattern.length()-1) )
                     result.append( pattern.charAt(i+1) );// 
                i++; // skip next char
            }
            else if (chr == single)
            {
                result.append('_');
            }
            else if (chr == multi)
            {
                result.append('%');
            }
            else if (chr == '\'')
            {
                result.append('\'');
                result.append('\'');
            }
            else 
            { 
                result.append(matchCase ? chr : Character.toUpperCase(chr));
            }
         }
            
        return result.toString();
    }
    
    /**
     * see convertToSQL92
     * 
     * @throws IllegalArgumentException
     */
    public String getSQL92LikePattern() throws IllegalArgumentException
    {
        if (escape.length() !=1)
        {
            throw new IllegalArgumentException("Like Pattern --> escape char should be of length exactly 1");
        }
        if (wildcardSingle.length() !=1)
        {
            throw new IllegalArgumentException("Like Pattern --> wildcardSingle char should be of length exactly 1");
        }
        if (wildcardMulti.length() !=1)
        {
            throw new IllegalArgumentException("Like Pattern --> wildcardMulti char should be of length exactly 1");
        }
        return LikeFilterImpl.convertToSQL92(
                    escape.charAt(0),
                    wildcardMulti.charAt(0), 
                    wildcardSingle.charAt(0),
                    matchingCase,
                    pattern);
    }
    
    public void setWildCard(String wildCard) {
        this.wildcardMulti = wildCard;
        compPattern = null;
    }
    
    public void setSingleChar(String singleChar) {
        this.wildcardSingle = singleChar;
        compPattern = null;
    }
    
    public void setEscape(String escape) {
        this.escape = escape;
        compPattern = null;
    }

    public void setMatchCase(boolean matchingCase){
        this.matchingCase = matchingCase;
        compPattern = null;
    }
    
    public boolean isMatchingCase() {
            return matchingCase;
    }
    
    public MatchAction getMatchAction() {
        return matchAction;
    }

    public void setMatchingCase(boolean matchingCase) {
            this.matchingCase = matchingCase;
    }
    
    private Matcher getMatcher(String string){
        if(compPattern == null){
            // protect the vars as this is moved code

            String pattern1 = new String(this.pattern);
            String wildcardMulti1 = new String(this.wildcardMulti);
            String wildcardSingle1 = new String(this.wildcardSingle);
            String escape1 = new String(this.escape);
            
//          The following things happen for both wildcards:
            //  (1) If a user-defined wildcard exists, replace with Java wildcard
            //  (2) If a user-defined escape exists, Java wildcard + user-escape
            //  Then, test for matching pattern and return result.
            char esc = escape1.charAt(0);
            if( LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("wildcard " + wildcardMulti1 + " single " + wildcardSingle1);
                LOGGER.finer("escape " + escape1 + " esc " + esc + " esc == \\ "
                        + (esc == '\\'));
            }

            String escapedWildcardMulti = fixSpecials(wildcardMulti1);
            String escapedWildcardSingle = fixSpecials(wildcardSingle1);

            // escape any special chars which are not our wildcards
            StringBuffer tmp = new StringBuffer("");

            boolean escapedMode = false;

            for (int i = 0; i < pattern1.length(); i++) {
                char chr = pattern1.charAt(i);
                if( LOGGER.isLoggable(Level.FINER)) {
                    LOGGER.finer("tmp = " + tmp + " looking at " + chr);
                }

                if (pattern1.regionMatches(false, i, escape1, 0, escape1.length())) {
                    // skip the escape string
                    LOGGER.finer("escape ");
                    escapedMode = true;

                    i += escape1.length();
                    chr = pattern1.charAt(i);
                }

                if (pattern1.regionMatches(false, i, wildcardMulti1, 0,
                            wildcardMulti1.length())) { // replace with java wildcard
                    LOGGER.finer("multi wildcard");

                    if (escapedMode) {
                        LOGGER.finer("escaped ");
                        tmp.append(escapedWildcardMulti);
                    } else {
                        tmp.append(".*");
                    }

                    i += (wildcardMulti1.length() - 1);
                    escapedMode = false;

                    continue;
                }

                if (pattern1.regionMatches(false, i, wildcardSingle1, 0,
                            wildcardSingle1.length())) {
                    // replace with java single wild card
                    LOGGER.finer("single wildcard");

                    if (escapedMode) {
                        LOGGER.finer("escaped ");
                        tmp.append(escapedWildcardSingle);
                    } else {
                        // From the OpenGIS filter encoding spec, 
                        // "the single singleChar character matches exactly one character"
                        tmp.append(".{1}");
                    }

                    i += (wildcardSingle1.length() - 1);
                    escapedMode = false;

                    continue;
                }

                if (isSpecial(chr)) {
                    LOGGER.finer("special");
                    tmp.append(this.escape + chr);
                    escapedMode = false;

                    continue;
                }

                tmp.append(chr);
                escapedMode = false;
            }

            pattern1 = tmp.toString();
            if( LOGGER.isLoggable(Level.FINER)) {
                LOGGER.finer("final pattern " + pattern1);
            }
            compPattern = isMatchingCase()  
                ? Pattern.compile(pattern1)
                : Pattern.compile(pattern1, Pattern.CASE_INSENSITIVE);
        }
        return compPattern.matcher(string);
    }

    /**
     * Constructor which flags the operator as like.
     */
    protected LikeFilterImpl() {
        this(MatchAction.ANY);
    }

    public LikeFilterImpl(org.opengis.filter.expression.Expression expr, String pattern, String wildcardMulti,
            String wildcardSingle, String escape) {
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

    public LikeFilterImpl(org.opengis.filter.expression.Expression expr, String pattern, String wildcardMulti,
            String wildcardSingle, String escape, MatchAction matchAction) {
        this(matchAction);
        setExpression(expr);
        setLiteral(pattern);
        setWildCard(wildcardMulti);
        setSingleChar(wildcardSingle);
        setEscape(escape);
    }

    /**
     * Sets the expression to be evalutated as being like the pattern
     *
     * @param attribute The value of the attribute for comparison.
     *
     * @throws IllegalFilterException Filter is illegal.
     * @deprecated Use {@link #setExpression(org.opengis.filter.expression.Expression)}
     */
    public final void setValue(Expression attribute) throws IllegalFilterException {
        setExpression(attribute);
    }

    /**
     * Gets the expression for hte filter.
     * <p>
     * This method calls th deprecated {@link #getValue()} for backwards 
     * compatability with subclasses.
     * </p>
     */
    public org.opengis.filter.expression.Expression getExpression() {
        return attribute;
    }
    
    public void setExpression(org.opengis.filter.expression.Expression e) {
        this.attribute = e;
    }
    
    /**
     * Sets the match pattern for this FilterLike.
     *
     * @param p the expression which evaluates to the match pattern for this
     *        filter
     * @param wildcardMulti the string that represents a mulitple character
     *        (1->n) wildcard
     * @param wildcardSingle the string that represents a single character (1)
     *        wildcard
     * @param escape the string that represents an escape character
     * 
     * @deprecated use one of 
     *  {@link PropertyIsLike#setExpression(Expression)}
     *  {@link PropertyIsLike#setWildCard(String)}
     *  {@link PropertyIsLike#setSingleChar(String)}
     *  {@link PropertyIsLike#setEscape(String)}
     */
    public final void setPattern(org.opengis.filter.expression.Expression p, String wildcardMulti,
        String wildcardSingle, String escape) {
        if( p instanceof Literal){
            Literal literal = (Literal) p;
            Object value = literal.getValue();
            if( value != null && value instanceof String){
                String pattern = (String) value;
                setPattern(pattern, wildcardMulti, wildcardSingle, escape);
            }
            else {
                throw new ClassCastException("Pattern Literal must be a string:"+value);
            }
        }
        else {
            throw new ClassCastException("Pattern must be a literal String");
        }
        
    }

    /**
     * Sets the match pattern for this FilterLike.
     *
     * @param pattern the string which contains the match pattern for this
     *        filter
     * @param wildcardMulti the string that represents a mulitple character
     *        (1->n) wildcard
     * @param wildcardSingle the string that represents a single character (1)
     *        wildcard
     * @param escape the string that represents an escape character
     * 
     * @deprecated use one of 
     *  {@link PropertyIsLike#setLiteral(String)}
     *  {@link PropertyIsLike#setWildCard(String)}
     *  {@link PropertyIsLike#setSingleChar(String)}
     *  {@link PropertyIsLike#setEscape(String)}
     */
    public final void setPattern(String pattern, String wildcardMulti,
        String wildcardSingle, String escape) {
        
        setLiteral(pattern);
        setWildCard(wildcardMulti);
        setSingleChar(wildcardSingle);
        setEscape(escape);
    }

    /**
     * Accessor method to retrieve the pattern.
     *
     * @return the pattern being matched.
     * 
     * @deprecated use {@link #getLiteral()}
     */
    public final String getPattern() {
        return getLiteral();
    }
    
    /**
     * Returns the pattern.
     */
    public String getLiteral() {
        return this.pattern;
    }

    /**
     * Sets the pattern.
     */
    public void setLiteral(String literal) {
        this.pattern = literal;
        compPattern = null;
    }
    
    /**
      * Determines whether or not a given feature matches this pattern.
      *
      * @param feature Specified feature to examine.
      *
      * @return Flag confirming whether or not this feature is inside the
      *         filter.
      *
      * @task REVISIT: could the pattern be null such that a null = null?
      */
    public boolean evaluate(Object feature) {
        //Checks to ensure that the attribute has been set
        if (attribute == null) {
            return false;
        }
            // Note that this converts the attribute to a string
            //  for comparison.  Unlike the math or geometry filters, which
            //  require specific types to function correctly, this filter
            //  using the mandatory string representation in Java
            // Of course, this does not guarantee a meaningful result, but it
            //  does guarantee a valid result.
            //LOGGER.finest("pattern: " + pattern);
            //LOGGER.finest("string: " + attribute.getValue(feature));
            //return attribute.getValue(feature).toString().matches(pattern);
            
            Object value = eval(attribute, feature);

            if (null == value) {
                return false;
            }
            
            //NC - support multiple values            
            if (value instanceof Collection) {
                int count = 0;
                
                for (Object element : (Collection<Object>) value){
                    Matcher matcher = getMatcher(element.toString());
                    boolean temp = matcher.matches();
                    if (temp) {
                        count++;
                    }
                       
                    switch (matchAction){
                        case ONE: if (count > 1) return false; break;
                        case ALL: if (!temp) return false; break;
                        case ANY: if (temp) return true; break;
                    } 
                }
                switch (matchAction){
                    case ONE: return (count == 1);
                    case ALL: return true;
                    case ANY: return false;
                    default: return false;
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
    public String toString() {
        return "[ " + attribute.toString() + " is like " + pattern + " ]";
    }

    /**
     * Getter for property escape.
     *
     * @return Value of property escape.
     */
    public java.lang.String getEscape() {
        return escape;
    }
    
     /**
     * Getter for property wildcardMulti.
     *
     * @return Value of property wildcardMulti.
     * 
     * @deprecated use {@link #getWildCard()}.
     *  
     */
    public final String getWildcardMulti() {
        return wildcardMulti;
    }

    /**
     * <p>
     * THis method calls {@link #getWildcardMulti()} for subclass backwards
     * compatability.
     * </p>
     * 
     * @see org.opengis.filter.PropertyIsLike#getWildCard().
     */
    public String getWildCard() {
        return getWildcardMulti();
    }
    
    /**
     * Getter for property wildcardSingle.
     *
     * @return Value of property wildcardSingle.
     * 
     * @deprecated use {@link #getSingleChar()}
     */
    public final String getWildcardSingle() {
        return wildcardSingle;
    }
    
    /**
     * <p>
     * THis method calls {@link #getWildcardSingle()()} for subclass backwards
     * compatability.
     * </p>
     * 
     * @see org.opengis.filter.PropertyIsLike#getSingleChar()().
     */
    public String getSingleChar() {
        return getWildcardSingle();
    }

    /**
     * convienience method to determine if a character is special to the regex
     * system.
     *
     * @param chr the character to test
     *
     * @return is the character a special character.
     */
    private boolean isSpecial(final char chr) {
        return ((chr == '.') || (chr == '?') || (chr == '*') || (chr == '^')
        || (chr == '$') || (chr == '+') || (chr == '[') || (chr == ']')
        || (chr == '(') || (chr == ')') || (chr == '|') || (chr == '\\')
        || (chr == '&'));
    }

    /**
     * convienience method to escape any character that is special to the regex
     * system.
     *
     * @param inString the string to fix
     *
     * @return the fixed string
     */
    private String fixSpecials(final String inString) {
        StringBuffer tmp = new StringBuffer("");

        for (int i = 0; i < inString.length(); i++) {
            char chr = inString.charAt(i);

            if (isSpecial(chr)) {
                tmp.append(this.escape + chr);
            } else {
                tmp.append(chr);
            }
        }

        return tmp.toString();
    }

    /**
     * Compares this filter to the specified object.  Returns true  if the
     * passed in object is the same as this filter.  Checks  to make sure the
     * filter types, the value, and the pattern are the same. &
     *
     * @param obj - the object to compare this LikeFilter against.
     *
     * @return true if specified object is equal to this filter; false
     *         otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof LikeFilterImpl) {
            LikeFilterImpl lFilter = (LikeFilterImpl) obj;

            //REVISIT: check for nulls.
            
            return ((Filters.getFilterType( lFilter ) == Filters.getFilterType( this ))
            && lFilter.getExpression().equals(this.attribute)
            && lFilter.getPattern().equals(this.pattern));
        }
        return false;
    }

    /**
     * Override of hashCode method.
     *
     * @return the hash code for this like filter implementation.
     */
    public int hashCode() {
        int result = 17;
        result = (37 * result)
            + ((attribute == null) ? 0 : attribute.hashCode());
        result = (37 * result) + ((pattern == null) ? 0 : pattern.hashCode());

        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance.
     * Typicaly used by Filter decoders, but may also be used by any thing
     * which needs infomration from filter structure. Implementations should
     * always call: visitor.visit(this); It is importatant that this is not
     * left to a parent class unless the parents API is identical.
     *
     * @param visitor The visitor which requires access to this filter, the
     *        method must call visitor.visit(this);
     */
     public Object accept(FilterVisitor visitor, Object extraData) {
        return visitor.visit(this,extraData);
    }
}
