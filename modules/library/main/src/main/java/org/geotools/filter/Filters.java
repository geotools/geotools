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

import java.awt.*;
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
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;

/**
 * Utility class for working with Filters & Expression.
 *
 * <p>To get the full benefit you will need to create an instanceof this Object (supports your own
 * custom FilterFactory!). Additional methods to help create expressions are available.
 *
 * <p>Example use:
 *
 * <pre><code>
 * Filters filters = new Filters( factory );
 * filters.duplicate( original );
 * </code></pre>
 *
 * The above example creates a copy of the provided Filter, the factory provided will be used when
 * creating the duplicated content.
 *
 * <h3>Expression</h3>
 *
 * <p>Expressions form an interesting little semi scripting language, intended for queries. A
 * interesting Feature of Filter as a language is that it is not strongly typed. This utility class
 * many helper methods that ease the transition from Strongly typed Java to the more relaxed setting
 * of Expression where most everything can be a string.
 *
 * <pre><code>
 * double sum = Filters.number( Object ) + Filters.number( Object );
 * </code></pre>
 *
 * The above example will support the conversion of many things into a format suitable for addition
 * - the complete list is something like:
 *
 * <ul>
 *   <li>Any instance of Number
 *   <li>"1234" - aka Integer
 *   <li>"#FFF" - aka Integer
 *   <li>"123.0" - aka Double
 * </ul>
 *
 * A few things (like Geometry and "ABC") will not be considered addative. In general the scope of
 * these functions should be similar to that allowed by the XML Atomic Types, aka those that can be
 * seperated by whitespace to form a list.
 *
 * <p>We do our best to be forgiving, any Java class which takes a String as a constructor can be
 * tried, and toString() assumed to be the inverse. This lets many things (like URL and Date)
 * function without modification.
 *
 * @author Jody Garnett (LISAsoft)
 * @since GeoTools 2.2
 * @version 8.0
 */
public class Filters {
    /** <code>NOTFOUND</code> indicates int value was unavailable */
    public static final int NOTFOUND = -1;

    /**
     * Private implementation used to handle static methods. Because this is a private instance we
     * do not have to override setFilterFactory; nobody will be messing with the factory and
     * breaking things for everyone.
     *
     * <p>Alternative; each static method can use CommonFactoryFinder in order to always make use of
     * the current globally configured results.
     */
    private static Filters STATIC = new Filters();

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
     * Safe and combiner for filters, will build an and filter around them only if there is at least
     * two filters
     *
     * @param ff The filter factory used to combine filters
     * @param filters The list of filters to be combined
     * @return The combination in AND of the filters, or Filter.EXCLUDE if filters is null or empty,
     *     or the one filter found in the list, in case it has only one element
     */
    public static Filter and(org.opengis.filter.FilterFactory ff, List<Filter> filters) {
        if (filters == null || filters.isEmpty()) {
            return Filter.EXCLUDE;
        } else if (filters.size() == 1) {
            return filters.get(0);
        } else {
            return ff.and(filters);
        }
    }

    /**
     * Safe version of FilterFactory *and* that is willing to combine filter1 and filter2 correctly
     * in the even either of them is already an And filter.
     *
     * @return And
     */
    public static Filter and(org.opengis.filter.FilterFactory ff, Filter filter1, Filter filter2) {
        ArrayList<Filter> list = new ArrayList<Filter>(2);
        if (filter1 instanceof And) {
            And some = (And) filter1;
            list.addAll(some.getChildren());
        } else if (filter1 != null) {
            list.add(filter1);
        }

        if (filter2 instanceof And) {
            And more = (And) filter2;
            list.addAll(more.getChildren());
        } else if (filter2 != null) {
            list.add(filter2);
        }

        if (list.size() == 0) {
            return Filter.EXCLUDE;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            return ff.and(list);
        }
    }

    /**
     * Safe or combiner for filters, will build an and filter around them only if there is at least
     * two filters
     *
     * @param ff The filter factory used to combine filters
     * @param filters The list of filters to be combined
     * @return The combination in OR of the filters, or Filter.EXCLUDE if filters is null or empty,
     *     or the one filter found in the list, in case it has only one element
     */
    public static Filter or(org.opengis.filter.FilterFactory ff, List<Filter> filters) {
        if (filters == null || filters.isEmpty()) {
            return Filter.EXCLUDE;
        } else if (filters.size() == 1) {
            return filters.get(0);
        } else {
            return ff.or(filters);
        }
    }

    /**
     * Safe version of FilterFactory *or* that is willing to combine filter1 and filter2 correctly
     * in the even either of them is already an Or filter.
     */
    public static Filter or(org.opengis.filter.FilterFactory ff, Filter filter1, Filter filter2) {
        ArrayList<Filter> list = new ArrayList<Filter>();
        if (filter1 instanceof Or) {
            Or some = (Or) filter1;
            list.addAll(some.getChildren());
        } else if (filter1 != null) {
            list.add(filter1);
        }

        if (filter2 instanceof Or) {
            Or more = (Or) filter2;
            list.addAll(more.getChildren());
        } else if (filter2 != null) {
            list.add(filter2);
        }

        if (list.size() == 0) {
            return Filter.EXCLUDE;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            return ff.or(list);
        }
    }

    /**
     * Deep copy the filter.
     *
     * <p>Filter objects are mutable, when copying a rich data structure (like SLD) you will need to
     * duplicate the Filters referenced therein.
     */
    public Filter duplicate(Filter filter) {
        DuplicatingFilterVisitor xerox = new DuplicatingFilterVisitor(ff);
        Filter copy = (Filter) filter.accept(xerox, ff);
        return copy;
    }
    /**
     * Convert expression to a constant for use in switch statements. This is an alternative to
     * performing instanceof checks. p> This utility method for those upgrading to a newer version
     * of GeoTools, instance of checks are preferred as they will take into account new kinds of
     * expressions as the filter specification grows over time. Example:
     *
     * <pre>
     * <code>
     * BEFORE: expression.getType() == ExpressionType.MATH_ADD
     * QUICK:  Filters.getExpressionType( expression ) == ExpressionType.MATH_ADD
     * AFTER: expression instanceof Add
     * </code>
     * </pre>
     *
     * @see ExpressionType
     * @return ExpressionType constant.
     */
    public static short getExpressionType(org.opengis.filter.expression.Expression experssion) {
        if (experssion == null) return 0;
        else if (experssion instanceof PropertyName) return ExpressionType.ATTRIBUTE;
        else if (experssion instanceof Function) return ExpressionType.FUNCTION;
        else if (experssion instanceof Literal) {
            Literal literal = (Literal) experssion;
            Object value = literal.getValue();
            if (value == null) {
                return ExpressionType.LITERAL_UNDECLARED;
            } else if (value instanceof Double) {
                return ExpressionType.LITERAL_DOUBLE;
            } else if (value instanceof Integer) {
                return ExpressionType.LITERAL_INTEGER;
            } else if (value instanceof Long) {
                return ExpressionType.LITERAL_LONG;
            } else if (value instanceof String) {
                return ExpressionType.LITERAL_STRING;
            } else if (value instanceof Geometry) {
                return ExpressionType.LITERAL_GEOMETRY;
            } else if (value instanceof Envelope) {
                return ExpressionType.LITERAL_GEOMETRY;
            } else {
                return ExpressionType.LITERAL_UNDECLARED;
            }
        } else if (experssion instanceof Add) {
            return ExpressionType.MATH_ADD;
        } else if (experssion instanceof Divide) {
            return ExpressionType.MATH_DIVIDE;
        } else if (experssion instanceof Multiply) {
            return ExpressionType.MATH_MULTIPLY;
        } else if (experssion instanceof Subtract) {
            return ExpressionType.MATH_SUBTRACT;
        } else {
            return 0;
        }
    }

    /**
     * Obtain the provided Expression as an integer.
     *
     * <p>This method is quickly used to safely check Literal expressions.
     *
     * @return int value of first Number, or NOTFOUND
     */
    public static int asInt(Expression expr) {
        if (expr == null) return NOTFOUND;
        try {
            Integer number = expr.evaluate(null, Integer.class);
            if (number == null) {
                return NOTFOUND;
            }
            return number;
        } catch (NullPointerException npe) {
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
     *
     * <p>This method only reliably works when the Expression is a Literal.
     *
     * @return Expression as a String, or null
     */
    public static String asString(Expression expr) {
        if (expr == null) return null;
        try {
            return expr.evaluate(null, String.class);
        } catch (NullPointerException npe) {
            // must be a more complicated expression than a literal
            return null;
        }
    }

    /**
     * Obtain the provided Expression as a double.
     *
     * @return int value of first Number, or Double.NaN
     */
    public static double asDouble(Expression expr) {
        if (expr == null) {
            return Double.NaN;
        }
        try {
            Double number = expr.evaluate(null, Double.class);
            if (number == null) {
                return Double.NaN;
            }
            return number.doubleValue();
        } catch (NullPointerException npe) {
            // must be a more complicated expression than a literal
            return Double.NaN;
        }
    }

    /**
     * Treat provided value as a Number, used for math opperations.
     *
     * <p>This function allows for the non stongly typed Math Opperations favoured by the Expression
     * standard.
     *
     * <p>Able to hanle:
     *
     * <ul>
     *   <li>null - to NaN
     *   <li>Number
     *   <li>String - valid Integer and Double encodings
     * </ul>
     *
     * @return double or Double.NaN;
     * @throws IllegalArgumentException For non numerical among us -- like Geometry
     */
    public static double number(Object value) {
        if (value == null) return Double.NaN;
        if (value instanceof Number) {
            Number number = (Number) value;
            return number.doubleValue();
        }
        if (value instanceof String) {
            String text = (String) value;
            try {
                Number number = (Number) gets(text, Number.class);
                return number.doubleValue();
            } catch (Throwable e) {
                throw new IllegalArgumentException("Unable to decode '" + text + "' as a number");
            }
        }
        if (value instanceof Expression) {
            throw new IllegalArgumentException("Cannot deal with un evaulated Expression");
        }
        throw new IllegalArgumentException(
                "Unable to evaulate " + value.getClass() + " in a numeric context");
    }

    /**
     * Used to upcovnert a "Text Value" into the provided TYPE.
     *
     * <p>Used to tread softly on the Java typing system, because Filter/Expression is not strongly
     * typed. Values in in Expression land are often not the the real Java Objects we wish they were
     * - it is reall a small, lax, query language and Java objects need a but of help getting
     * through.
     *
     * <p>A couple notes:
     *
     * <ul>
     *   <li>Usual trick of reflection for a Constructors that supports a String parameter is used
     *       as a last ditch effort.
     *   <li>will do its best to turn Object into the indicated Class
     *   <li>will be used for ordering literals against attribute values are calculated at runtime
     *       (like Date.)
     * </ul>
     *
     * Remember Strong typing is for whimps who know what they are doing ahead of time. Real
     * programmers let their program learn at runtime... :-)
     *
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
            Constructor<T> create = TYPE.getConstructor(new Class[] {String.class});
            return create.newInstance(new Object[] {text});
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
     *
     * <p>Examples:
     *
     * <ul>
     *   <li>Filters.puts( 3.14 ) => "3.14"
     *   <li>Filters.puts( 1.0 ) => "1"
     * </ul>
     *
     * @return text representation
     */
    public static String puts(double number) {
        if (Math.rint(number) == number) {
            return Integer.toString((int) number);
        }
        return Double.toString(number);
    }
    /**
     * Inverse of eval, used to softly type supported types into Text for use as literals.
     *
     * <p>This method has been superseeded by Converters which offers a more general and open ended
     * solution.
     *
     * @return String representation of provided object
     */
    public static String puts(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
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
        String text = Converters.convert(obj, String.class);
        if (text != null) {
            return text;
        }
        return obj.toString();
    }

    /**
     * Inverse of eval, used to softly type supported types into Text for use as literals.
     *
     * <p>This method has been superseeded by Converters which offers a more general and open ended
     * solution.
     *
     * @return String representation of provided color.
     */
    public static String puts(Color color) {
        String redCode = Integer.toHexString(color.getRed());
        String greenCode = Integer.toHexString(color.getGreen());
        String blueCode = Integer.toHexString(color.getBlue());

        if (redCode.length() == 1) redCode = "0" + redCode;
        if (greenCode.length() == 1) greenCode = "0" + greenCode;
        if (blueCode.length() == 1) blueCode = "0" + blueCode;

        return "#" + redCode + greenCode + blueCode;
    }
    //
    // FilterUtils from Eric Sword
    //
    //    static boolean isLogicFilter(Filter filter) {
    //        return (isGroupFilter(filter) || (filter instanceof Not));
    //    }

    /**
     * Returns true if the given filter can contain more than one subfilter. Only And and Or filters
     * match this now.
     */
    //    static boolean isGroupFilter(Filter filter) {
    //        //Note: Can't use BinaryLogicOperator here because the Not implementation also
    // inherits from it.
    //        return ( (filter instanceof And) || (filter instanceof Or));
    //    }

    /**
     * Removes the targetFilter from the baseFilter if the baseFilter is a group filter (And or
     * Or),recursing into any sub-logic filters to find the targetFilter if necessary.
     *
     * <ul>
     *   <li>If the targetFilter equals the baseFilter, then Filter.INCLUDE is returned to indicate
     *       that no filters are left.
     *   <li>If the targetFilter does not equal the base filter, no change is made and the
     *       baseFilter is returned.
     *   <li>If removing the targetFilter would leave only a single term within the baseFilter, then
     *       the single remaining term is returned instead of the (now invalid) baseFilter. If the
     *       last item is removed from an Or statement then Filter.EXCLUDE is return If the last
     *       item is removed from an And statement then Filter.INCLUDE is returned
     * </ul>
     */
    public Filter remove(Filter baseFilter, Filter targetFilter) {
        return remove(baseFilter, targetFilter, true);
    }

    public static Filter removeFilter(Filter baseFilter, Filter targetFilter) {
        return STATIC.remove(baseFilter, targetFilter);
    }

    /**
     * Removes the targetFilter from the baseFilter if the baseFilter is a group filter (And or Or).
     * See {@link #removeFilter(org.opengis.filter.Filter, org.opengis.filter.Filter)} for details,
     * except this method includes the option to not recurse into child filters.
     *
     * @param recurse true if the method should descend into child group filters looking for the
     *     target
     */
    public Filter remove(Filter baseFilter, final Filter targetFilter, boolean recurse) {
        if (baseFilter == null) {
            // return null if nothing to start with
            return baseFilter;
        }
        if (targetFilter == null) {
            // similarly, just return the existing one if the target is null
            return baseFilter;
        }
        if (baseFilter.equals(targetFilter)) {
            // if they are the same filter, return Filter.INCLUDE to signify no filters left
            return Filter.INCLUDE;
        }
        if (!(baseFilter instanceof BinaryLogicOperator)) {
            return baseFilter; // nothing to traverse
        }
        if (recurse) {
            DuplicatingFilterVisitor remove =
                    new DuplicatingFilterVisitor() {
                        public Object visit(Or filter, Object extraData) {
                            List<Filter> newChildren = children(filter, targetFilter, extraData);
                            if (newChildren.isEmpty()) {
                                // every time you remove a filter from an Or
                                // expression you get less stuff, so removing the last is ...
                                return Filter.EXCLUDE;
                            } else if (newChildren.size() == 1) {
                                return newChildren.get(0);
                            } else {
                                return getFactory(extraData).or(newChildren);
                            }
                        }

                        public Object visit(And filter, Object extraData) {
                            List<Filter> newChildren = children(filter, targetFilter, extraData);
                            if (newChildren.isEmpty()) {
                                // every time you remove a filter from an And
                                // filter you get more stuff, so removing the last is ...
                                return Filter.INCLUDE;
                            } else if (newChildren.size() == 1) {
                                return newChildren.get(0);
                            } else {
                                return getFactory(extraData).and(newChildren);
                            }
                        }

                        private List<Filter> children(
                                BinaryLogicOperator filter,
                                final Filter targetFilter,
                                Object extraData) {
                            List<Filter> children = filter.getChildren();
                            List<Filter> newChildren = new ArrayList<Filter>();
                            for (Iterator<Filter> iter = children.iterator(); iter.hasNext(); ) {
                                Filter child = iter.next();
                                if (targetFilter.equals(child)) {
                                    continue; // skip this one
                                }
                                if (child != null) {
                                    Filter newChild = (Filter) child.accept(this, extraData);
                                    newChildren.add(newChild);
                                }
                            }
                            return newChildren;
                        }
                    };
            return (Filter) baseFilter.accept(remove, ff);
        } else {
            BinaryLogicOperator blo = (BinaryLogicOperator) baseFilter;
            List<Filter> children = blo.getChildren();
            if (children == null) {
                children = Collections.emptyList();
            }

            List<Filter> copy = new ArrayList<Filter>(children.size());
            for (Filter filter : children) {
                if (targetFilter.equals(filter)) {
                    continue; // skip this one
                }
                copy.add(filter);
            }
            if (copy.isEmpty()) {
                if (baseFilter instanceof And) {
                    // every time you remove a filter from an And
                    // filter you get more stuff, so removing the last is ...
                    return Filter.INCLUDE;
                } else if (baseFilter instanceof Or) {
                    // every time you remove a filter from an Or
                    // expression you get less stuff, so removing the last is ...
                    return Filter.EXCLUDE;
                } else {
                    return Filter.EXCLUDE;
                }
            } else if (copy.size() == 1) {
                return copy.get(0);
            } else if (baseFilter instanceof And) {
                return ff.and(children);
            } else if (baseFilter instanceof Or) {
                return ff.or(children);
            } else {
                return Filter.INCLUDE;
            }
        }
    }

    public static Filter removeFilter(Filter baseFilter, Filter targetFilter, boolean recurse) {
        return STATIC.remove(baseFilter, targetFilter, recurse);
    }

    /**
     * Uses FilterAttributeExtractor to return the list of all mentioned attribute names.
     *
     * <p>You can use this method to quickly build up the set of any mentioned attribute names.
     *
     * @return Set of propertyNames
     */
    public Set<String> attributeNames(Filter filter) {
        if (filter == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
        filter.accept(extractor, new HashSet<String>());
        return extractor.getAttributeNameSet();
    }

    /**
     * Traverses the filter and returns any encountered property names.
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
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
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     */
    public static Set<PropertyName> propertyNames(
            Filter filter, final SimpleFeatureType featureType) {
        if (filter == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        filter.accept(attExtractor, null);
        Set<PropertyName> propertyNames = attExtractor.getPropertyNameSet();
        return propertyNames;
    }

    /** Traverses the filter and returns any encountered property names. */
    public static Set<PropertyName> propertyNames(Filter filter) {
        return propertyNames(filter, null);
    }

    /**
     * Traverses the expression and returns any encountered property names.
     *
     * <p>The feature type is supplied as contexts used to lookup expressions in cases where the
     * attributeName does not match the actual name of the type.
     */
    public static Set<PropertyName> propertyNames(
            Expression expression, final SimpleFeatureType featureType) {
        if (expression == null) {
            return Collections.emptySet();
        }
        FilterAttributeExtractor attExtractor = new FilterAttributeExtractor(featureType);
        expression.accept(attExtractor, null);
        Set<PropertyName> propertyNames = attExtractor.getPropertyNameSet();
        return propertyNames;
    }

    /** Traverses the expression and returns any encountered property names. */
    public static Set<PropertyName> propertyNames(Expression expression) {
        return propertyNames(expression, null);
    }
    /**
     * True if the filter makes use of propertyName
     *
     * <p>Note this is a simple test and is faster than calling <code>
     * attributeNames( filter ).contains( name )</code>
     *
     * @param propertyName - name of the property to look for
     */
    static boolean uses(Filter filter, final String propertyName) {
        if (filter == null) {
            return false;
        }
        class SearchFilterVisitor extends AbstractSearchFilterVisitor {
            protected boolean found(Object data) {
                return Boolean.TRUE == data;
            }

            public Object visit(PropertyName name, Object data) {
                if (Utilities.equals(name.getPropertyName(), propertyName)) {
                    return true;
                }
                return data;
            }
        };
        SearchFilterVisitor search = new SearchFilterVisitor();
        boolean found = (Boolean) filter.accept(search, false);
        return found;
    }
    /**
     * Check if the provided filter has child filters of some sort.
     *
     * <p>Where a child filter is considered:
     *
     * <ul>
     *   <li>Not: has a single child filter being negated
     *   <li>And: has a list of child filters
     *   <li>Or: has a list of child filters
     * </ul>
     *
     * Any other filter will return false.
     *
     * @return list of child filters
     */
    public static boolean hasChildren(Filter filter) {
        return filter instanceof BinaryLogicOperator || filter instanceof Not;
    }

    /**
     * List of child filters.
     *
     * <p>Where a child filter is considered:
     *
     * <ul>
     *   <li>Not: has a single child filter being negated
     *   <li>And: has a list of child filters
     *   <li>Or: has a list of child filters
     * </ul>
     *
     * Any other filters will return false.
     *
     * <p>This represents the space covered by a number of the search functions.
     *
     * <p>The returned list is a mutable copy that can be used with filter factory to construct a
     * new filter when you are ready. To make that explicit I am returning an ArrayList so it is
     * clear that the result can be modified.
     *
     * @return are belong to us
     */
    public static ArrayList<Filter> children(Filter filter) {
        return children(filter, false);
    }
    /**
     * List of child filters.
     *
     * <p>Where a child filter is considered:
     *
     * <ul>
     *   <li>Not: has a single child filter being negated
     *   <li>And: has a list of child filters
     *   <li>Or: has a list of child filters
     * </ul>
     *
     * Any other filters will return false.
     *
     * <p>This represents the space covered by a number of the search functions, if *all* is true
     * this function will recursively search for additional child filters beyond those directly
     * avaialble from your filter.
     *
     * <p>The returned list is a mutable copy that can be used with filter factory to construct a
     * new filter when you are ready. To make that explicit I am returning an ArrayList so it is
     * clear that the result can be modified.
     *
     * @param all true to recurse into the filter and retrieve all children; false to only return
     *     the top level children
     * @return are belong to us
     */
    public static ArrayList<Filter> children(Filter filter, boolean all) {
        final ArrayList<Filter> children = new ArrayList<Filter>();
        if (filter == null) {
            return children;
        }
        if (all) {
            filter.accept(
                    new DefaultFilterVisitor() {
                        public Object visit(And filter, Object data) {
                            List<Filter> childList = filter.getChildren();
                            if (childList != null) {
                                for (Filter child : childList) {
                                    if (child == null) continue;

                                    children.add(child);
                                    data = child.accept(this, data);
                                }
                            }
                            return data;
                        }

                        public Object visit(Or filter, Object data) {
                            List<Filter> childList = filter.getChildren();
                            if (childList != null) {
                                for (Filter child : childList) {
                                    if (child == null) continue;

                                    children.add(child);
                                    data = child.accept(this, data);
                                }
                            }
                            return data;
                        }

                        public Object visit(Not filter, Object data) {
                            Filter child = filter.getFilter();
                            if (child != null) {
                                children.add(child);
                                data = child.accept(this, data);
                            }
                            return data;
                        }
                    },
                    null);
        } else {
            if (filter instanceof Not) {
                Not not = (Not) filter;
                if (not.getFilter() != null) {
                    children.add(not.getFilter());
                }
            }
            if (filter instanceof BinaryLogicOperator) {
                BinaryLogicOperator parent = (BinaryLogicOperator) filter;
                List<Filter> reviewChildren = parent.getChildren();
                if (reviewChildren != null) {
                    for (Filter child : reviewChildren) {
                        if (child != null) {
                            children.add(child);
                        }
                    }
                }
            }
        }
        return children;
    }

    /**
     * Find the first child-filter (or the base filter itself) that is of the given type and uses
     * the specified property.
     *
     * @param filterType - class of the filter to look for
     * @param propertyName - name of the property to look for
     */
    public static <T extends Filter> T search(
            Filter filter, Class<T> filterType, String propertyName) {
        List<Filter> allBase = children(filter);
        for (Filter base : allBase) {
            if (filterType.isInstance(base) && uses(base, propertyName)) {
                return filterType.cast(base);
            }
        }
        return null; // not found
    }

    /**
     * Given a filter which contains a term which is a PropertyName, returns the name of the
     * property. Returns null if no PropertyName is passed
     */
    public static String findPropertyName(Filter filter) {
        if (filter == null) return null;

        class SearchFilterVisitor extends AbstractSearchFilterVisitor {
            protected boolean found(Object data) {
                return data != null;
            }

            public Object visit(PropertyName name, Object data) {
                return name.getPropertyName();
            }
        };
        SearchFilterVisitor search = new SearchFilterVisitor();
        return (String) filter.accept(search, null);
    }

    /**
     * Find all filters (including the base filter itself) that are of the given type and use the
     * specified property.
     *
     * @return all filters that are of the given type using the specified property
     */
    static <T extends Filter> List<T> findAllByTypeAndName(
            Filter filter, Class<T> filterType, String property) {
        List<T> retVal = new ArrayList<T>();
        List<Filter> allBase = children(filter);
        allBase.add(0, filter);
        for (Filter base : allBase) {
            if (filterType.isInstance(base) && uses(base, property)) {
                retVal.add(filterType.cast(base));
            }
        }
        return retVal;
    }
}
