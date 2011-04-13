/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.AbstractSearchFilterVisitor;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Utility class for working with Filters & Expression.
 * <p>
 * To get the full benefit you will need to create an instanceof
 * this Object (supports your own custom FilterFactory!). Additional
 * methods to help create expressions are available.
 * </p>
 * <p>
 * Example use:
 * <pre><code>
 * Filters filters = new Filters( factory );
 * filters.duplicate( original );
 * </code></pre>
 * The above example creates a copy of the provided Filter,
 * the factory provided will be used when creating the duplicated
 * content.
 * </p>
 * <h3>Expression</h3>
 * <p>
 * Expressions form an interesting little semi scripting language,
 * intended for queries.  A interesting Feature of Filter as a language
 * is that it is not strongly typed. This utility class many helper
 * methods that ease the transition from Strongly typed Java to the more
 * relaxed setting of Expression where most everything can be a string.
 * </p>
 * <pre><code>
 * double sum = Filters.number( Object ) + Filters.number( Object );
 * </code></pre>
 * The above example will support the conversion of many things into a format
 * suitable for addition - the complete list is something like:
 * <ul>
 * <li>Any instance of Number
 * <li>"1234" - aka Integer
 * <li>"#FFF" - aka Integer 
 * <li>"123.0" - aka Double
 * </ul>
 * A few things (like Geometry and "ABC") will not be considered addative.
 * </p>
 * In general the scope of these functions should be similar to that
 * allowed by the XML Atomic Types, aka those that can be seperated by
 * whitespace to form a list.
 * </p>
 * <p>
 * We do our best to be forgiving, any Java class which takes a String as
 * a constructor can be tried, and toString() assumed to be the inverse. This
 * lets many things (like URL and Date) function without modification.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research
 * @since GeoTools 2.2.M3
 * @source $URL$
 */
public class Filters {
    /** <code>NOTFOUND</code> indicates int value was unavailable */
    public static final int NOTFOUND = -1;
    
    /**
     * Private implementation used to handle static methods. Because this is a private instance we
     * do not have to override setFilterFactory; nobody will be messing with the factory and
     * breaking things for everyone.
     * <p>
     * Alternative; each static method can use CommonFactoryFinder in order to always make
     * use of the current globally configured results.
     */
    private static Filters STATIC = new Filters();

    /**
     * Set to true to start throwing exceptions when org.geotools.filter.Filter is used.
     */
    private static final boolean STRICT = false;

    org.opengis.filter.FilterFactory2 ff;
	
    /** Create Filters helper object using global FilterFactory provided by CommonFactoryFinder */
    public Filters() {
        this(CommonFactoryFinder.getFilterFactory2(null));
    }
    /** Create a Filters helper using the provided FilterFactory */
    public Filters(org.opengis.filter.FilterFactory2 factory) {
        ff = factory;
    }
    public void setFilterFactory(org.opengis.filter.FilterFactory2 factory) {
        ff = factory;
    }
    
	/**
	 * Safe version of FilterFactory *and* that is willing to combine
	 * filter1 and filter2 correctly in the even either of them is already
	 * an And filter.
	 * 
	 * @param ff
	 * @param filter1
	 * @param filter2
	 * @return And
	 */
    public static Filter and( org.opengis.filter.FilterFactory ff, Filter filter1, Filter filter2 ){
        ArrayList<Filter> list = new ArrayList<Filter>(2);
        if( filter1 == null ){
            // ignore
        }
        else if( filter1 instanceof And){
            And some = (And) filter1;            
            list.addAll( some.getChildren() );
        }
        else {
            list.add( filter1 );
        }
        
        if( filter2 == null ){
            // ignore
        }
        else if( filter2 instanceof And){
            And more = (And) filter2;            
            list.addAll( more.getChildren() );
        }
        else {
            list.add( filter2 );
        }
        
        if( list.size() == 0 ){
            return Filter.EXCLUDE;
        }
        else if( list.size() == 1 ){
            return list.get(0);
        }
        else {
            return ff.and( list );
        }
    }
    /**
     * Safe version of FilterFactory *or* that is willing to combine
     * filter1 and filter2 correctly in the even either of them is already
     * an Or filter.
     * 
     * @param ff
     * @param filter1
     * @param filter2
     * @return
     */
    public static Filter or( org.opengis.filter.FilterFactory ff, Filter filter1, Filter filter2 ){
        ArrayList<Filter> list = new ArrayList<Filter>();
        if( filter1 == null ){
            // ignore
        }
        else if( filter1 instanceof Or){
            Or some = (Or) filter1;            
            list.addAll( some.getChildren() );
        }
        else {
            list.add( filter1 );
        }
        
        if( filter2 == null){
            // ignore
        }
        else if( filter2 instanceof Or){
            Or more = (Or) filter2;            
            list.addAll( more.getChildren() );
        }
        else {
            list.add( filter2 );
        }
        
        if( list.size() == 0 ){
            return Filter.EXCLUDE;
        }
        else if( list.size() == 1 ){
            return list.get(0);
        }
        else {
            return ff.or( list );
        }
    }
    
    /**
     * Safely visit the provided filter.
     * <p>
     * This method handles the case of:
     * <ul>
     * <li>Filter.INCLUDES: will call FilterVisitor2 method if available
     * <li>Filter.EXCLUDES: will call FilterVisitor2 method if available
     * <li>org.geotools.filter.Filter: will visit
     * </ul>
     * Please note that when called with a strict *org.opengis.filter.Filter* this
     * method will fail with a ClassCastException
     * 
     * @param filter
     * @param visitor
     * @deprecated Please update your code to a org.opengis.filter.FilterVisitor
     */
    public static void accept( org.opengis.filter.Filter filter, FilterVisitor visitor ){        
       if( filter == Filter.EXCLUDE ){
           if( visitor instanceof FilterVisitor2 ){
               ((FilterVisitor2)visitor).visit( (ExcludeFilter) Filter.EXCLUDE );
           }
           return;
       }
       else if( filter == Filter.INCLUDE ){
           if( visitor instanceof FilterVisitor2 ){
               ((FilterVisitor2)visitor).visit( (IncludeFilter) Filter.INCLUDE);
           }
           return;
       }
       
       if( filter instanceof org.geotools.filter.Filter ){
           ((org.geotools.filter.Filter) filter).accept( visitor );
       }
       else {
           if( STRICT ){
               // don't even try ..
               throw new ClassCastException("Please update your code to a org.opengis.filter.FilterVisitor");
           }
           // Copy the provided filter into the old org.geotools.filter.Filter api           
           FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
           DuplicatingFilterVisitor xerox = new DuplicatingFilterVisitor( ff );           
           org.geotools.filter.Filter copy = (org.geotools.filter.Filter) filter.accept( xerox, ff );
           
           // Visit the resulting copy
           copy.accept(visitor);
       }       
    }
    /**
     * Deep copy the filter.
     * <p>
     * Filter objects are mutable, when copying a rich
     * data structure (like SLD) you will need to duplicate
     * the Filters referenced therein.
     * </p>
     */
    public Filter duplicate( Filter filter ){
    	DuplicatingFilterVisitor xerox = new DuplicatingFilterVisitor( ff );
    	Filter copy = (Filter) filter.accept( xerox, ff );
    	return copy;
    }
        
    /**
     * Utility method used to transition to geoapi filter.
     * <p>
     * This utility method is designed to help people port their
     * code quickly, an instanceof check is much preferred.
     * </p>
     * Example:<pre><code>
     * BEFORE: filter.getFilterType() == FilterType.GEOMETRY_CONTAINS
     * QUICK:  Filters.getFilterType( filter ) == FilterType.GEOMETRY_CONTAINS
     * AFTER: filter instanceof Contains
     * </code></pre>
     * @param filter
     * @deprecated please use instanceof checks
     */
    public static short getFilterType( org.opengis.filter.Filter filter ){
        if( filter == org.opengis.filter.Filter.EXCLUDE ) return FilterType.ALL;
        if( filter == org.opengis.filter.Filter.INCLUDE ) return FilterType.NONE;
        if( filter instanceof org.geotools.filter.Filter){
            return ((org.geotools.filter.Filter)filter).getFilterType();
        }
        if( filter instanceof PropertyIsBetween ) return FilterType.BETWEEN;
        if( filter instanceof PropertyIsEqualTo ) return FilterType.COMPARE_EQUALS;
        if( filter instanceof PropertyIsGreaterThan ) return FilterType.COMPARE_GREATER_THAN;
        if( filter instanceof PropertyIsGreaterThanOrEqualTo ) return FilterType.COMPARE_GREATER_THAN_EQUAL;
        if( filter instanceof PropertyIsLessThan) return FilterType.COMPARE_LESS_THAN;
        if( filter instanceof PropertyIsLessThanOrEqualTo ) return FilterType.COMPARE_LESS_THAN_EQUAL;
        if( filter instanceof PropertyIsNotEqualTo ) return FilterType.COMPARE_NOT_EQUALS;
        if( filter instanceof Id ) return FilterType.FID;
        if( filter instanceof BBOX ) return FilterType.GEOMETRY_BBOX;
        if( filter instanceof Beyond) return FilterType.GEOMETRY_BEYOND;
        if( filter instanceof Contains ) return FilterType.GEOMETRY_CONTAINS;
        if( filter instanceof Crosses ) return FilterType.GEOMETRY_CROSSES;
        if( filter instanceof Disjoint ) return FilterType.GEOMETRY_DISJOINT;
        if( filter instanceof DWithin) return FilterType.GEOMETRY_DWITHIN;
        if( filter instanceof Equals) return FilterType.GEOMETRY_EQUALS;
        if( filter instanceof Intersects) return FilterType.GEOMETRY_INTERSECTS;
        if( filter instanceof Overlaps) return FilterType.GEOMETRY_OVERLAPS;
        if( filter instanceof Touches) return FilterType.GEOMETRY_TOUCHES;
        if( filter instanceof Within) return FilterType.GEOMETRY_WITHIN;
        if( filter instanceof PropertyIsLike) return FilterType.LIKE;
        if( filter instanceof And) return FilterType.LOGIC_AND;
        if( filter instanceof Not) return FilterType.LOGIC_NOT;
        if( filter instanceof Or ) return FilterType.LOGIC_OR;        
        if( filter instanceof PropertyIsNull) return FilterType.NULL;
        
        if( filter instanceof Filter){
            return 0;
        }        
        return 0;
    }
    /**
     * Obtain the provided Expression as an integer.
     * <p>
     * This method is quickly used to safely check Literal expressions.
     * 
     * @param expr
     * @return int value of first Number, or NOTFOUND
     */
    public static int asInt( Expression expr ) {
        if( expr == null ) return NOTFOUND;
        try {
            Integer number = expr.evaluate( null, Integer.class );
            if( number == null ){
                return NOTFOUND;
            }
            return number;
        }
        catch( NullPointerException npe ){
            return NOTFOUND; // well that was not unexpected
        }
        /*
        Number number = (Number) asType(expr, Number.class);
 	
        if (number != null) {
            return number.intValue();
        }

        //look for a string
        String string = (String) asType(expr,String.class);
        if (string != null) {
        	//try parsing into a integer
        	try {
        		return Integer.parseInt(string);
        	}
        	catch(NumberFormatException e) {}
        }
        
        //no dice
        return NOTFOUND;
        */
    }

    /**
     * Obtain the provided Expression as a String.
     * <p>
     * This method only reliably works when the Expression is a Literal.
     *
     * @param expr
     *
     * @return Expression as a String, or null
     */
    public static String asString(Expression expr) {
        if( expr == null ) return null;
        try {
            return expr.evaluate( null, String.class );
        }
        catch( NullPointerException npe){
            // must be a more complicated expression than a literal
            return null;            
        }
    }

    /**
     * Obtain the provided Expression as a double.
     * @param expr
     * @return int value of first Number, or Double.NaN
     */
    public static double asDouble(Expression expr) {
        if( expr == null ) {
            return Double.NaN;
        }        
        try {
            Double number = expr.evaluate(null, Double.class );
            if( number == null ) {
                return Double.NaN;
            }   
            return number.doubleValue();
        }
        catch( NullPointerException npe){
            // must be a more complicated expression than a literal
            return Double.NaN;            
        }
    }
    
    /**
     * Navigate through the expression searching for something that can be a TYPE.
     * <p>
     * This will work even with dynamic expression that would normally require a
     * feature. It works especially well when the Expression is a Literal
     * literal (which is usually the case).
     * </p>
     * 
     * <p>
     * If you have a specific Feature, please do this:
     * <pre><code>
     * Color value = expr.evaualte( feature, Color.class );
     * return value instanceof Color ? (Color) value : null;
     * </code></pre>
     * </p>
     *
     * @param expr This only really works for down casting literals to a value
     * @param Target type
     *
     * @return expr smunched into indicated type
     * @deprecated This is not a good idea; use expr.evaulate( null, TYPE )
     */
    public static <T> T asType(Expression expr, Class<T> TYPE) {
        if (expr == null) {
            return null;
        }        
        if( STRICT ){
            return expr.evaluate(null, TYPE );
        }        
        else if (expr instanceof Literal) {
        		Literal literal = (Literal) expr;        		
        	return (T) literal.evaluate(null, TYPE );            
        }
        else if (expr instanceof Function) {
        		Function function = (Function) expr;
        		List<Expression> params = function.getParameters();
            if ( params != null && params.size() != 0 ) {
                for (int i = 0; i < params.size(); i++) {
                    Expression e = (Expression) params.get(i);
                    T value = asType(e, TYPE);

                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        else {
            try { // this is a bad idea, not expected to work much
                T value = expr.evaluate(null, TYPE );

                if (TYPE.isInstance(value)) {
                    return value;
                }
            } catch (NullPointerException expected) {
                return null; // well that was not unexpected
            } catch (Throwable ignore) { // I did say that was a bad idea                
            }
        }
        return null; // really need a Feature to acomplish this one
    }
    
    /**
     * Treat provided value as a Number, used for math opperations.
     * <p>
     * This function allows for the non stongly typed Math Opperations
     * favoured by the Expression standard.
     * </p>
     * <p>
     * Able to hanle:
     * <ul>
     * <li>null - to NaN
     * <li>Number
     * <li>String - valid Integer and Double encodings
     * </ul>
     * 
     * </p>
     * @param value
     * @return double or Double.NaN;
     * @throws IllegalArgumentException For non numerical among us -- like Geometry 
     */
    public static double number(Object value) {
    	if( value == null ) return Double.NaN;
    	if( value instanceof Number ){
    		Number number = (Number) value;
    		return number.doubleValue();
    	}
    	if( value instanceof String ){
    		String text = (String) value;
    		try {
				Number number = (Number) gets( text, Number.class );
				return number.doubleValue();
			} catch (Throwable e) {
				throw new IllegalArgumentException("Unable to decode '"+text+"' as a number" );				
			}    		
    	}
    	if( value instanceof Expression ){
    		throw new IllegalArgumentException("Cannot deal with un evaulated Expression");
    	}
    	throw new IllegalArgumentException("Unable to evaulate "+value.getClass()+" in a numeric context");
    }
    
    /**
     * Used to upcovnert a "Text Value" into the provided TYPE.
     * <p>
     * Used to tread softly on the Java typing system, because
     * Filter/Expression is not strongly typed. Values in in
     * Expression land are often not the the real Java Objects
     * we wish they were - it is reall a small, lax, query
     * language and Java objects need a but of help getting
     * through.
     * <p>
     * </p>
     * A couple notes:
     * <ul>
     * <li>Usual trick of reflection for a Constructors that
     *     supports a String parameter is used as a last ditch effort.
     *     </li>
     * <li>will do its best to turn Object into the indicated Class
     * <li>will be used for ordering literals against attribute values
     *     are calculated at runtime (like Date.)
     * </ul>
     * Remember Strong typing is for whimps who know what they are
     * doing ahead of time. Real programmers let their program
     * learn at runtime... :-)
     * </p>
     * 
     * @param text
     * @param TYPE
     * @throws open set of Throwable reflection for TYPE( String )
     */
    public static <T> T gets(String text, Class<T> TYPE) throws Throwable {
        if (text == null) {
            return null;
        }
        if (TYPE == String.class) {
            return TYPE.cast(text);
        }
        if (TYPE == Integer.class) {
            return TYPE.cast(Integer.decode(text));
        }
        if (TYPE == Double.class) {
            return TYPE.cast(Double.valueOf(text));
        }
        if (TYPE == Number.class) {
            try {
                return TYPE.cast(Double.valueOf(text));
            } catch (NumberFormatException ignore) {
            }
            return TYPE.cast(Integer.decode(text));
        }
        if (TYPE == Color.class) {
            return TYPE.cast(new Color(Integer.decode(text).intValue()));
        }
        // fallback try converters
        Object value = Converters.convert(text, TYPE);
        if (value != null) {
            return TYPE.cast(value);
        }
        // Original fall back position of reflection against constructor
        try {
            Constructor<T> create = TYPE.getConstructor(new Class[] { String.class });
            return create.newInstance(new Object[] { text });
        } catch (SecurityException e) {
            // hates you
        } catch (NoSuchMethodException e) {
            // nope
        } catch (IllegalArgumentException e) {
            // should not occur
        } catch (InstantiationException e) {
            // should not occur, perhaps the class was abstract?
            // eg. Number.class is a bad idea
        } catch (IllegalAccessException e) {
            // hates you
        } catch (InvocationTargetException e) {
            // should of worked but we got a real problem,
            // an actual problem
            throw e.getCause();
        }
        return null; // give up
    }
    /**
     * Convert provided number to a suitable text representation
     * <p>
     * Examples:
     * <ul>
     * <li>Filters.puts( 3.14 ) => "3.14"</li>
     * <li>Filters.puts( 1.0 ) => "1"</li>
     * </ul>
     * @param number
     * @return text representation
     */
    public static String puts(double number) {
        if (Math.rint(number) == number) {
            return Integer.toString((int) number);
        }
        return Double.toString(number);
    }
    /**
     * Inverse of eval, used to softly type supported
     * types into Text for use as literals.
     * <p>
     * This method has been superseeded by Converters
     * which offers a more general and open ended solution.
     * </p>
     * @return String representation of provided object
     */
    public static String puts(Object obj) {
        if (obj == null){
            return null;
        }
        if (obj instanceof String){
            return (String) obj;
        }
        if (obj instanceof Color) {
            Color color = (Color) obj;
            return puts(color);
        }
        if (obj instanceof Number) {
            Number number = (Number) obj;
            return puts(number.doubleValue());
        }
        String text = Converters.convert( obj, String.class );
        if( text != null ){
            return text;
        }
        return obj.toString();
    }

    /**
     * Inverse of eval, used to softly type supported types into Text for use as literals.
     * <p>
     * This method has been superseeded by Converters which offers a more general and open ended
     * solution.
     * </p>
     * 
     * @param color
     * @return String representation of provided color.
     */
    public static String puts(Color color) {
        String redCode = Integer.toHexString(color.getRed());
        String greenCode = Integer.toHexString(color.getGreen());
        String blueCode = Integer.toHexString(color.getBlue());

        if (redCode.length() == 1)
            redCode = "0" + redCode;
        if (greenCode.length() == 1)
            greenCode = "0" + greenCode;
        if (blueCode.length() == 1)
            blueCode = "0" + blueCode;

        return "#" + redCode + greenCode + blueCode;
    }
    //
    // FilterUtils from Eric Sword
    //
//    static boolean isLogicFilter(Filter filter) {
//        return (isGroupFilter(filter) || (filter instanceof Not));
//    }

    /**
     * Returns true if the given filter can contain more than one subfilter.  Only And and Or filters match this now.
     * @param filter
     * @return
     */
//    static boolean isGroupFilter(Filter filter) {
//        //Note: Can't use BinaryLogicOperator here because the Not implementation also inherits from it.
//        return ( (filter instanceof And) || (filter instanceof Or));
//    }

    /**
     * Creates a new filter by appending a newFilter to a baseFilter.
     * <p>
     * This works in a smooth fashion if the baseFilter is a BinaryLogicalOperator
     * (And or Or). The created filter will be a combination of the baseFilter children with the
     * newFilter appended on the end.
     * <p>
     * Otherwise this method will create an And filter to append newFilter to the end of baseFilter.
     * @param baseFilter
     * @param newFilter
     * @return the combined filters
     */
    public Filter append(Filter baseFilter, Filter addFilter) {
        return append(baseFilter, addFilter, false);
    }
    /**
     * Create a new filter by appending newFilter onto the end of baseFilter.
     * <p>
     * Provides quick static access to filters.append( baseFilter, addFilter );
     * @param ff FilterFactory to use when creating a new filter
     * @param baseFilter baseFilter 
     * @param addFilter addFilter being appended
     * @return
     */
    public static Filter appendFilter( Filter baseFilter, Filter addFilter ){
        return STATIC.append( baseFilter, addFilter );
    }

    /**
     * Creates a new filter by appending a newFilter to a baseFilter.
     * <p>
     * This works in a smooth fashion if the baseFilter is a BinaryLogicalOperator
     * (And or Or). The created filter will be a combination of the baseFilter children with the
     * newFilter appended on the end.
     * <p>
     * Otherwise this method will:
     * <il>
     * <li>if createOr is true: combine the two filters using Or</li>
     * <li>if createOr is false: combine the two filters using And</li>
     * </ul>
     * filter to append newFilter to the end of baseFilter.
     * @param baseFilter
     * @param newFilter
     * @param createOr True to append with an Or filter if needed, false to append with And
     * @return the combined filters
     */
     public Filter append(Filter baseFilter, Filter newFilter, boolean createOr) {
        //no need to create a grouping filter if none currently exists
        if (baseFilter == null){
            return newFilter;
        }
        //similarly, just return the existing one if the new one is null
        if (newFilter == null){
            return baseFilter;
        }
        if (baseFilter instanceof And){
            And and = (And) baseFilter;
            List<Filter> children = new ArrayList<Filter>( and.getChildren() );
            children.add(newFilter);
            return ff.and( children );            
        }
        if (baseFilter instanceof Or){
            Or or = (Or) baseFilter;
            List<Filter> children = new ArrayList<Filter>( or.getChildren() );
            children.add(newFilter);
            return ff.or( children );            
        }
        if (createOr){
            return ff.or( baseFilter, newFilter );
        }
        else {
            return ff.and( baseFilter, newFilter );            
        }
    }
     /**
      * Create a new filter by appending newFilter onto the end of baseFilter.
      * <p>
      * Provides quick static access to filters.append( baseFilter, addFilter );
      * @param ff FilterFactory to use when creating a new filter
      * @param baseFilter baseFilter 
      * @param addFilter addFilter being appended
      * @return
      */
     public static Filter appendFilter( Filter baseFilter, Filter addFilter, boolean createOr  ){
         return STATIC.append( baseFilter, addFilter, createOr );
     }

    /**
     * Removes the targetFilter from the baseFilter if the baseFilter is a group filter (And or Or), recursing into any
     * sub-logic filters to find the targetFilter if necessary.  If removing the targetFilter would leave only a single
     * term within the baseFilter, then the single remaining term is returned instead of the (now invalid) baseFilter.
     * If the baseFilter is not a group filter:
     * <ul>
     * <li> If the targetFilter equals the baseFilter, then null is returned to indicate that no filters
     * are left.
     * <li> If the targetFilter does not equal the base filter, no change is made and the baseFilter is returned.
     * </ul>
     * @param baseFilter
     * @param targetFilter
     * @return
     */
    public Filter remove(Filter baseFilter, Filter targetFilter) {
        return remove(baseFilter, targetFilter, true);
    }
    
    public static Filter removeFilter( Filter baseFilter, Filter targetFilter ){
        return STATIC.remove(baseFilter, targetFilter);
    }
    
    /**
     * Removes the targetFilter from the baseFilter if the baseFilter is a group filter (And or Or).  See
     * {@link #removeFilter(org.opengis.filter.Filter, org.opengis.filter.Filter)} for details, except this method
     * includes the option to not recurse into child filters.
     * @param baseFilter
     * @param targetFilter
     * @param recurse true if the method should descend into child group filters looking for the target
     * @return
     */
    public Filter remove(Filter baseFilter, final Filter targetFilter, boolean recurse) {
        if (baseFilter == null){
            //return null if nothing to start with
            return baseFilter;
        }
        if (targetFilter == null){
            //similarly, just return the existing one if the target is null
            return baseFilter;
        }
        if (baseFilter == targetFilter){
            //if they are the same filter, return null to signify no filters left
            return null;
        }
        if( !(baseFilter instanceof BinaryLogicOperator)){
            return baseFilter; // nothing to traverse
        }
        if (recurse) {
            DuplicatingFilterVisitor remove = new DuplicatingFilterVisitor() {
                public Object visit(Or filter, Object extraData) {
                    List<Filter> children = filter.getChildren();
                    List<Filter> newChildren = new ArrayList<Filter>();
                    for (Iterator<Filter> iter = children.iterator(); iter.hasNext();) {
                        Filter child = iter.next();
                        if( targetFilter.equals(child)){
                            continue; // skip this one
                        }
                        if (child != null) {
                            Filter newChild = (Filter) child.accept(this, extraData);
                            newChildren.add(newChild);
                        }
                    }
                    if( newChildren.size() == 1 ){
                        return newChildren.get(0);
                    }
                    return getFactory(extraData).or(newChildren);
                }
                public Object visit(And filter, Object extraData) {
                    List<Filter> children = filter.getChildren();
                    List<Filter> newChildren = new ArrayList<Filter>();
                    for (Iterator<Filter> iter = children.iterator(); iter.hasNext();) {
                        Filter child = iter.next();
                        if( targetFilter.equals(child)){
                            continue; // skip this one
                        }
                        if (child != null) {
                            Filter newChild = (Filter) child.accept(this, extraData);
                            newChildren.add(newChild);
                        }
                    }
                    if( newChildren.size() == 1 ){
                        return newChildren.get(0);
                    }
                    return getFactory(extraData).and(newChildren);
                }
            };
            return (Filter) baseFilter.accept(remove, ff);
        }
        else {
            BinaryLogicOperator blo = (BinaryLogicOperator) baseFilter;
            List<Filter> children = blo.getChildren();
            if (children == null ){
                children = Collections.emptyList();
            }
            
            List<Filter> copy = new ArrayList<Filter>( children.size() );
            for( Filter filter : children ){
                if( targetFilter.equals(filter) ){
                    continue; // skip this one
                }
                copy.add( filter );
            }
            if( copy.isEmpty() ){
                return Filter.EXCLUDE;
            }
            else if (copy.size() == 1){
                return copy.get(0); 
            }
            else if (baseFilter instanceof And){
                return ff.and( children );
            }
            else if (baseFilter instanceof Or){
                return ff.or( children );
            }
            else {
                return Filter.EXCLUDE;
            }
        }
    }
    
    public static Filter removeFilter(Filter baseFilter, Filter targetFilter, boolean recurse) {
        return STATIC.remove(baseFilter, targetFilter, recurse );
    }
    
    /**
     * Uses FilterAttributeExtractor to return the list of all mentioned attribute names.
     * <p>
     * You can use this method to quickly build up the set of any mentioned attribute names.
     * 
     * @param filter
     * @return Set of propertyNames
     */
    public Set<String> attributeNames( Filter filter ){
        if( filter == null ){
            return Collections.emptySet();
        }
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        filter.accept( extractor, new HashSet<String>() );
        return extractor.getAttributeNameSet();
    }
    
    /**
     * Traverses the filter and returns any encountered property names.
     * <p>
     * The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     * </p>
     */
    public static String[] attributeNames(Filter filter, final SimpleFeatureType featureType) {
        if (filter == null) {
            return new String[0];
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        filter.accept(attExtractor, null);
        String[] attributeNames = attExtractor.getAttributeNames();
        return attributeNames;
    }

    /**
     * Traverses the filter and returns any encountered property names.
     * <p>
     * The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     * </p>
     */
    public static Set<PropertyName> propertyNames(Filter filter, final SimpleFeatureType featureType) {
        if (filter == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        filter.accept(attExtractor, null);
        Set<PropertyName> propertyNames = attExtractor.getPropertyNameSet();
        return propertyNames;
    }

    /**
     * Traverses the filter and returns any encountered property names.
     */
    public static Set<PropertyName> propertyNames(Filter filter) {
        return propertyNames(filter, null);
    }

    /**
     * Traverses the expression and returns any encountered property names.
     * <p>
     * The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     * </p>
     */
    public static Set<PropertyName> propertyNames(Expression expression,
            final SimpleFeatureType featureType) {
        if (expression == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        expression.accept(attExtractor, null);
        Set<PropertyName> propertyNames = attExtractor.getPropertyNameSet();
        return propertyNames;
    }

    /**
     * Traverses the expression and returns any encountered property names.
     */
    public static Set<PropertyName> propertyNames(Expression expression) {
        return propertyNames(expression, null);
    }
    /**
     * True if the filter makes use of propertyName
     * <p>
     * Note this is a simple test and is faster than calling
     * <code>attributeNames( filter ).contains( name )</code>
     * @param filter
     * @param property - name of the property to look for
     * @return
     */
    static boolean uses(Filter filter, final String propertyName ) {
        if (filter == null) {
            return false;
        }
        class SearchFilterVisitor extends AbstractSearchFilterVisitor {
            protected boolean found(Object data) {
                return Boolean.TRUE == data;
            }
            public Object visit(PropertyName name, Object data) {                
                if( Utilities.equals(name.getPropertyName(), propertyName ) ){
                    return true;
                }
                return data;
            }
        };
        SearchFilterVisitor search = new SearchFilterVisitor();
        boolean found = (Boolean) filter.accept(search, false );
        return found;
    }

    /**
     * Returns all the base filters, where base is the provided filter (and in the event it is a
     * BinaryLogicalOperator all of its child features). This represents the space covered by
     * a number of the search functions.
     * <p>
     * The returned list is a mutable copy that can be used with filter factory to construct a
     * new filter when you are ready. To make that explicit I am returning an ArrayList so it
     * is clear that the result can be modified.
     * 
     * @param filter
     * @return are belong to us
     */
    static public ArrayList<Filter> allYourBase( Filter filter ){
        if( filter == null ){
            return new ArrayList<Filter>();
        }
        else if (filter instanceof BinaryLogicOperator ){
            BinaryLogicOperator parent = (BinaryLogicOperator) filter;
            List<Filter> children = parent.getChildren();
            ArrayList<Filter> base = new ArrayList<Filter>(children !=null ? children.size()+1:1 );
            base.add(0,parent);
            if( children!=null){
                base.addAll(children);
            }
            return base;
        }
        else {
            ArrayList<Filter> base = new ArrayList<Filter>(1);
            base.add(filter);
            return base;
        }
    }
    
    /**
     * Find the first child-filter (or the base filter itself) that is of the given type and uses
     * the specified property.
     * @param filter
     * @param filterType - class of the filter to look for
     * @param property - name of the property to look for
     * @return
     */
    public static <T extends Filter> T search(Filter filter, Class<T> filterType, String propertyName ){
        ArrayList<Filter> allBase = allYourBase(filter); 
        for( Filter base : allBase ){
            if( filterType.isInstance(base) && uses(base, propertyName) ){                
                return filterType.cast(base);
            }
        }
        return null; // not found
    }
    
    /**
     * Find all filters (including the base filter itself) that are of the given type and use
     * the specified property.
     * @param filter
     * @param filterType
     * @param property
     * @return
     */
    static <T extends Filter> List<T> findAllByTypeAndName(Filter filter, Class<T> filterType, String property) {
        List<T> retVal = new ArrayList<T>();
        
        ArrayList<Filter> allBase = allYourBase(filter); 
        // The first visitor finds all filters of the specified type, and the second visitor finds all
        // filters that use the given property.  Intersecting the two gives us all that use both.
        
        //FindType findType = new FindType(filterType);
        //FindProperty findProperty = new FindProperty(property);        
        for( Filter base : allBase ){
            if( filterType.isInstance(base) && uses(base, property) ){                
                retVal.add( filterType.cast(base) );
            }
        }
//        Collection<Filter> intersectFilters = (Collection<Filter>) filter.accept(findType, new LinkedHashSet());
//        if (intersectFilters != null && !intersectFilters.isEmpty()) {
//
//            Collection<Filter> geoFilters = (Collection<Filter>) filter.accept(findProperty, new LinkedHashSet());
//            intersectFilters.retainAll(geoFilters);
//            retVal = new ArrayList<Filter>(intersectFilters);
//        }
        return retVal;
    }
}

