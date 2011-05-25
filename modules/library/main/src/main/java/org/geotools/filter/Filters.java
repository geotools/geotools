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
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.And;
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
 * To get the full benifit you will need to create an instanceof
 * this Object (supports your own custom FilterFactory!). Additional
 * methods to help create expressions are available.
 * </p>
 * <p>
 * Example use:
 * <pre><code>
 * Filters filters = new Filters( factory );
 * filters.duplicate( origional );
 * </code></pre>
 * The above example creates a copy of the provided Filter,
 * the factory provided will be used when creating the duplicated
 * content.
 * </p>
 * <h3>Expression</h3>
 * <p>
 * Expressions form an interesting little semi scripting languge,
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
 *
 * @source $URL$
 */
public class Filters {
	/** <code>NOTFOUND</code> indicates int value was unavailable */
	public static final int NOTFOUND = -1;

	/**
	 * Set to true to start throwing exceptions when org.geotools.filter.Filter is used.
	 */
    private static final boolean STRICT = false;
	
	org.opengis.filter.FilterFactory2 ff;
	
	public Filters(){
		this( CommonFactoryFinder.getFilterFactory2(null) );
	}	
	public Filters( org.opengis.filter.FilterFactory2 factory ){
		ff = factory;
	}
	public void setFilterFactory( org.opengis.filter.FilterFactory2 factory ){
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
     * Object value = expr.getValue( feature );
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
        		List params = function.getParameters();
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
    public static Object gets( String text, Class TYPE ) throws Throwable {
    	if( text == null ) return null;
    	if( TYPE == String.class ) return text;
    	if( TYPE == Integer.class ) {
    		return Integer.decode( text );    		
    	}
    	if( TYPE == Double.class ){
    		return Double.valueOf( text );
    	}
    	if( TYPE == Number.class ){
    		try {
    			return Double.valueOf( text );
    		}
    		catch( NumberFormatException ignore ){
    		}
    		return Integer.decode( text );    		
    	}
    	if( TYPE == Color.class ){
    		return new Color( Integer.decode( text ).intValue() );
    	}    	
    	try {
			Constructor create = TYPE.getConstructor( new Class[]{String.class});
			return create.newInstance( new Object[]{ text } );
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
    	return null;
    }
    
    public static String puts( double number ){
    	if( Math.rint(number) == number ){
    		return Integer.toString( (int) number );
    	}
    	return Double.toString( number );    	
    }
    /**
     * Inverse of eval, used to softly type supported
     * types into Text for use as literals.
     */
    public static String puts( Object obj ){
    	if( obj == null ) return null;
    	if( obj instanceof String) return (String) obj;    	
    	if( obj instanceof Color ){
    		Color color = (Color) obj;
    		return puts( color );
    	}
    	if( obj instanceof Number ){
    		Number number = (Number) obj;
    		return puts( number.doubleValue() );    		
    	}
    	return obj.toString();
    }
    
    public static String puts( Color color ){
    	String redCode = Integer.toHexString(color.getRed());
        String greenCode = Integer.toHexString(color.getGreen());
        String blueCode = Integer.toHexString(color.getBlue());

        if (redCode.length() == 1) redCode = "0" + redCode;
        if (greenCode.length() == 1) greenCode = "0" + greenCode;
        if (blueCode.length() == 1) blueCode = "0" + blueCode;
        
        return "#" + redCode + greenCode + blueCode;  
    }
}

