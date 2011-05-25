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

import java.util.HashMap;
import java.util.Map;

import org.geotools.filter.capability.ArithmeticOperatorsImpl;
import org.geotools.filter.capability.ComparisonOperatorsImpl;
import org.geotools.filter.capability.FilterCapabilitiesImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.filter.capability.FunctionsImpl;
import org.geotools.filter.capability.OperatorImpl;
import org.geotools.filter.capability.SpatialOperatorImpl;
import org.geotools.filter.capability.SpatialOperatorsImpl;
import org.geotools.filter.visitor.IsFullySupportedFilterVisitor;
import org.geotools.filter.visitor.IsSupportedFilterVisitor;
import org.geotools.filter.visitor.OperatorNameFilterVisitor;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
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
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.Subtract;
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
 * Allows for easier interaction with FilterCapabilities.
 * <p>
 * This class provides some out of the box FilterCapabilities constants that
 * you can quickly use to describe the encoding abilities of your service.
 * <p>
 * This class behaves similar to Citations in that the constants are to
 * be considered immutable, methods have been provided to assist
 * in composing your own set of FilterCapabilities.
 * <p>
 * Example:<pre><code>
 * Capabilities capabilities = new Capabilities();
 * capabilities.addAll( Capabilities.LOGICAL );
 * capabilities.addAll( Capabilities.SIMPLE_COMPARISONS );
 * </code></pre>
 * You can use the Capabilities class at runtime to check
 * existing filters to see if they are fully supported:<pre><code>
 * if( fullySupports( filter )) {
 *     // do something
 * }
 * </code></pre>
 * Right now the class gives no indication as to what part of the provided
 * filter was in error.
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class Capabilities {    
    private static Map<Class<?>,String> scalarNames;
    static {
        scalarNames = new HashMap<Class<?>,String>();
        scalarNames.put(PropertyIsEqualTo.class,PropertyIsEqualTo.NAME);
        scalarNames.put(PropertyIsNotEqualTo.class,PropertyIsNotEqualTo.NAME);
        scalarNames.put(PropertyIsGreaterThan.class,PropertyIsGreaterThan.NAME);
        scalarNames.put(PropertyIsGreaterThanOrEqualTo.class,PropertyIsGreaterThanOrEqualTo.NAME);
        scalarNames.put(PropertyIsLessThan.class,PropertyIsLessThan.NAME);
        scalarNames.put(PropertyIsLessThanOrEqualTo.class,PropertyIsLessThanOrEqualTo.NAME);
        scalarNames.put(PropertyIsNull.class,PropertyIsNull.NAME);
        scalarNames.put(PropertyIsLike.class,PropertyIsLike.NAME);
        scalarNames.put(PropertyIsBetween.class,PropertyIsBetween.NAME);
    }
    private static Map<Class<?>,String> spatialNames;
    static {
        spatialNames = new HashMap<Class<?>,String>();
        spatialNames.put(BBOX.class, BBOX.NAME );
        spatialNames.put(Equals.class, Equals.NAME);
        spatialNames.put(Disjoint.class,Disjoint.NAME);
        spatialNames.put(Intersects.class,Intersects.NAME);
        spatialNames.put(Touches.class,Touches.NAME);
        spatialNames.put(Crosses.class,Crosses.NAME);
        spatialNames.put(Within.class,Within.NAME);
        spatialNames.put(Contains.class,Contains.NAME);
        spatialNames.put(Overlaps.class,Overlaps.NAME);
        spatialNames.put(Beyond.class,Beyond.NAME);
        spatialNames.put(DWithin.class,DWithin.NAME);
    }
    private static Map<Class<?>,String> logicalNames;
    static {
        logicalNames = new HashMap<Class<?>,String>();
        logicalNames.put(And.class,"And"); // not an operator name, see scalarCapabilities.hasLogicalOperators()  
        logicalNames.put(Or.class, "Or"); // not an operator name, see scalarCapabilities.hasLogicalOperators()
        logicalNames.put(Not.class, "Not"); // not an operator name, see scalarCapabilities.hasLogicalOperators()
    }
    private static Map<Class<?>,String> filterNames;
    static {
        filterNames = new HashMap<Class<?>,String>();
        filterNames.putAll( scalarNames );
        filterNames.putAll( spatialNames );
        filterNames.putAll( logicalNames );
        
        filterNames.put(Id.class, "Id"); // not an operator name, see idCapabilities.hasFID() or idCapabilities.hasEID()
    }
    
    private static Map<Class<? extends Expression>,String> arithmaticNames;    
    static {
        arithmaticNames = new HashMap<Class<? extends Expression>,String>();        
        arithmaticNames.put(Add.class, Add.NAME );
        arithmaticNames.put(Subtract.class, Subtract.NAME );
        arithmaticNames.put(Multiply.class, Multiply.NAME );
        arithmaticNames.put(Divide.class, Divide.NAME);
    }
    private static Map<Class<? extends Expression>,String> exprNames;    
    static {
        exprNames = new HashMap<Class<? extends Expression>, String>();        
        exprNames.putAll( arithmaticNames );
        
        // while function is an expression, we should check the name
        exprNames.put(Function.class,"Function");
    }

    private static final OperatorNameFilterVisitor operationNameVisitor = new OperatorNameFilterVisitor();
    
    /** Support for logical types AND, OR and NOT */
    public static Capabilities LOGICAL; 
    static {
        LOGICAL = new Capabilities();
        LOGICAL.addType(And.class);
        LOGICAL.addType(Not.class);
        LOGICAL.addType(Or.class);
    }
    public static Capabilities LOGICAL_OPENGIS = LOGICAL;
    /**
     * Capabilities representing the simple comparisions.
     */
    public static Capabilities SIMPLE_COMPARISONS;
    static {
        SIMPLE_COMPARISONS = new Capabilities();
        SIMPLE_COMPARISONS.addType( PropertyIsEqualTo.class ); //COMPARE_EQUALS|
        SIMPLE_COMPARISONS.addType( PropertyIsGreaterThan.class ); // COMPARE_GREATER_THAN
        SIMPLE_COMPARISONS.addType( PropertyIsGreaterThanOrEqualTo.class ); // COMPARE_GREATER_THAN_EQUAL
        SIMPLE_COMPARISONS.addType( PropertyIsLessThan.class ); // COMPARE_LESS_THAN
        SIMPLE_COMPARISONS.addType( PropertyIsGreaterThanOrEqualTo.class ); // COMPARE_LESS_THAN_EQUAL
        SIMPLE_COMPARISONS.addType( PropertyIsNotEqualTo.class ); // COMPARE_NOT_EQUALS;
    }
    public static Capabilities SIMPLE_COMPARISONS_OPENGIS = SIMPLE_COMPARISONS;

    /**
     * This is a quick visitor (returning true / false) that only
     * checks one level deep.
     */
    IsSupportedFilterVisitor supportedVisitor;
    /**
     * Visitor (returning true / false) if the provided filter is supported
     * by our FilterCapabilities.
     */
    IsFullySupportedFilterVisitor fullySupportedVisitor;
    
    /**
     * Internal FilterCapabilities data structure used
     * to maintain state.
     */
    FilterCapabilitiesImpl contents;
     
    public Capabilities(){
        this( new FilterCapabilitiesImpl() );
    }
    
    public Capabilities( FilterCapabilities contents ){
        if( contents instanceof FilterCapabilitiesImpl){
            this.contents = (FilterCapabilitiesImpl) contents;
        }
        else {
            this.contents = new FilterCapabilitiesImpl( contents );
        }
        supportedVisitor = new IsSupportedFilterVisitor( contents );
        fullySupportedVisitor = new IsFullySupportedFilterVisitor( contents );        
    }
    
    /**
     * Returns the internal FilterCapabilities data structure
     * used for checking.
     * 
     * @return FilterCapabilities
     */
    public FilterCapabilitiesImpl getContents() {
        return contents;
    }
    
    /**
     * Adds a new support type to capabilities.
     * <p>
     * This is the same as:<code>addName( toOperationName( type ) )
     * <p>
     * @param type the Class that indicates the new support.
     */
    public void addType( Class type ){
        String name = toOperationName( type );
        if( name == null ) return;
        
        addName( name );
    }
    
    /**
     * Adds support for the provided name.
     * <p>
     * If this is a known name (avaialble as part of opengis interface)
     * it will be grouped into:
     * <ul>
     * <li>Spatial Operators: Will added a SpatialOperator into the mix with Point, LineString, Polygon as the supported geometry
     * operands (based on the assumption of JTS)
     * <li>Comparison Operators:
     * <li>Arithmetic Operators: will cause hassimpleArithmetic to be true
     * <li>Other: will be treated as a no argument function call
     * </ul>
     * This method will have no effect if the operator is already known.
     * <p>
     * Examples:<pre><code>
     * capabilities.addName("Beyond"); // will enabled Beyond Filter
     * capabilities.addName("NullCheck"); // will enable PropertyIsNull Filter
     * capabilities.addName("SUB"); // will enabled hasSimpleArithmetic
     * capabilities.addName("PI"); // add a no argument function called PI()
     * </code></pre>
     * 
     * @param name FilterCapabilities Operand name such as "BBOX", "Like" or "MUL"
     */
    public void addName( String name ){
        if( name == null ){
            return;
        }
        else if( spatialNames.containsValue( name )){
            SpatialOperatorsImpl operators = contents.getSpatialCapabilities().getSpatialOperators();
            if( operators.getOperator( name ) == null ){
                SpatialOperatorImpl operator = new SpatialOperatorImpl(name);
                // default JTS?
                operator.getGeometryOperands().add( GeometryOperand.LineString );
                operator.getGeometryOperands().add( GeometryOperand.Point );
                operator.getGeometryOperands().add( GeometryOperand.Polygon );
                
                operators.getOperators().add( operator );
            }
        }
        else if( scalarNames.containsValue( name )){
            ComparisonOperatorsImpl operators = contents.getScalarCapabilities().getComparisonOperators();
            if( operators.getOperator( name ) == null ){
                OperatorImpl operator = new OperatorImpl( name );                
                operators.getOperators().add( operator );
            }
        }
        else if( arithmaticNames.containsValue( name )){
            ArithmeticOperatorsImpl operators = contents.getScalarCapabilities().getArithmeticOperators();
            operators.setSimpleArithmetic(true);
        }
        else if( logicalNames.containsValue( name )){
            contents.getScalarCapabilities().setLogicalOperators(true);
        }
        else if( "Id".equals(name)){
            contents.getIdCapabilities().setFID(true);
        }
        else {
            FunctionsImpl functions = contents.getScalarCapabilities().getArithmeticOperators().getFunctions();
            if( functions.getFunctionName( name ) == null ){
                FunctionNameImpl function = new FunctionNameImpl( name, 0 );
                functions.getFunctionNames().add( function );
            }
        }
    }
    /**
     * Will add support for a function with the provided number of arguments
     * <p>
     * This method will have no effect if the function is already listed.
     * <p>
     * Example:<code>capabilities.addName( "Length", 1 )</code>
     * 
     * @param name
     * @param argumentCount
     */
    public void addName( String name, int argumentCount ){
        FunctionsImpl functions = contents.getScalarCapabilities().getArithmeticOperators().getFunctions();
        if( functions.getFunctionName( name ) == null ){
            FunctionNameImpl function = new FunctionNameImpl( name, argumentCount );
            functions.getFunctionNames().add( function );
        }
    }
    
    /**
     * Document support for the provided function.
     * <p>
     * This method will have no effect if the function is already listed.
     * <p>
     * Example:<code>capabilities.addName( "Min", "value1", "value2" )</code>
     * @param name
     * @param argumentCount
     */
    public void addName( String name, String... argumentNames ){
        FunctionsImpl functions = contents.getScalarCapabilities().getArithmeticOperators().getFunctions();
        if( functions.getFunctionName( name ) == null ){
            FunctionNameImpl function = new FunctionNameImpl( name, argumentNames );            
            functions.getFunctionNames().add( function );
        }
    }
    /**
     * Determines if specific filter passed in is supported.
     *
     * @see IsSupportedFilterVisitor
     * @param filter The Filter to be tested.
     * @return true if supported, false otherwise.
     */
    public boolean supports(Filter filter) {
        if (filter == null) {
            return false;
        }
        if( supportedVisitor == null ){
            supportedVisitor = new IsSupportedFilterVisitor( contents );
        }
        return (Boolean) filter.accept( supportedVisitor, null );
    }

    /**
     * Determines if the filter and all its sub filters and expressions are supported.
     * <p>
     * Is most important for logic filters, as they are the only ones with
     * subFilters. The geoapi FilterVisitor and ExpressionVisitors
     * allow for the handling of null, even so care should be taken to use
     * Filter.INCLUDE and Expression.NIL where you can.
     * <p>
     * @see IsFullySupportedFilterVisitor
     * @param filter the filter to be tested.
     * @return true if all sub filters are supported, false otherwise.
     */
    public boolean fullySupports(Filter filter) {
        if (filter == null) {
            return false;
        }
        if( fullySupportedVisitor == null ){
            fullySupportedVisitor = new IsFullySupportedFilterVisitor( contents );
        }
        return (Boolean) filter.accept( fullySupportedVisitor, null );
    }
    /**
     * Determines if the expression and all its sub expressions is supported.
     * <p>
     * The Expression visitor used for this work can handle null, even so care
     * should be taken to useExpression.NIL where you can.
     * <p>
     * @see IsFullySupportedFilterVisitor
     * @param filter the filter to be tested.
     * @return true if all sub filters are supported, false otherwise.
     */
    public boolean fullySupports(Expression expression) {
        if (expression == null) {
            return false;
        }
        if( fullySupportedVisitor == null ){
            fullySupportedVisitor = new IsFullySupportedFilterVisitor( contents );
        }
        return (Boolean) expression.accept( fullySupportedVisitor, null );
    }

    /**
     * Quickly look at the filter and determine the OperationName
     * we need to check for in the FilterCapabilities data structure.
     * 
     * @param filter
     * @return Operation name
     */
    public String toOperationName( Filter filter ){
        if( filter == null ) return null;        
        return (String) filter.accept( operationNameVisitor, null);
    }
    
    /**
     * Figure out the OperationName for the provided filterType.
     * <p>
     * The returned name can be used to check the FilterCapabilities
     * to see if it type is supported in this execution context.
     * <p>
     * This approach is not applicable for Functions.
     * <p>
     * @param filterType Filter type
     * @return Operation name for the provided FilterType
     */
    public String toOperationName( Class filterType ){
        if( filterType == null ) return null;
        
        String quick = filterNames.get( filterType );
        if( quick != null ) {
            return quick;
        }
        
        // The following is O(N) and slightly wrong in that And.class is not an operator
        for( Map.Entry<Class<?>,String> mapping : filterNames.entrySet() ){
            if( mapping.getKey().isAssignableFrom( filterType )){
                return mapping.getValue();
            }
        }
        /*
        // The following is < O(N) but more complicated to maintain
        if( SpatialOperator.class.isAssignableFrom(filterType)) {
            if( BBOX.class.isAssignableFrom(filterType)){
                return BBOX.NAME;
            }
            else if ( Contains.class.isAssignableFrom(filterType)){
                return Contains.NAME;
            }
            else if ( Crosses.class.isAssignableFrom(filterType)){
                return Crosses.NAME;
            }
            else if ( Disjoint.class.isAssignableFrom(filterType)){
                return Disjoint.NAME;
            }
            else if ( Beyond.class.isAssignableFrom(filterType)){
                return Beyond.NAME;
            }
            else if ( DWithin.class.isAssignableFrom(filterType)){
                return DWithin.NAME;
            }
            else if ( Equals.class.isAssignableFrom(filterType)){
                return Equals.NAME;
            }
            else if ( Intersects.class.isAssignableFrom(filterType)){
                return Intersects.NAME;
            }
            else if ( Overlaps.class.isAssignableFrom(filterType)){
                return Overlaps.NAME;
            }
            else if ( Touches.class.isAssignableFrom(filterType)){
                return Touches.NAME;
            }
            else if ( Within.class.isAssignableFrom(filterType)){
                return Within.NAME;
            }
        }
        else if( BinaryComparisonOperator.class.isAssignableFrom(filterType) ){
            if ( PropertyIsEqualTo.class.isAssignableFrom(filterType)){
                return PropertyIsEqualTo.NAME;
            }
            else if ( PropertyIsGreaterThan.class.isAssignableFrom(filterType)){
                return PropertyIsGreaterThan.NAME;
            }
            else if ( PropertyIsGreaterThanOrEqualTo.class.isAssignableFrom(filterType)){
                return PropertyIsGreaterThanOrEqualTo.NAME;
            }
            else if ( PropertyIsLessThan.class.isAssignableFrom(filterType)){
                return PropertyIsLessThan.NAME;
            }
            else if ( PropertyIsLessThanOrEqualTo.class.isAssignableFrom(filterType)){
                return PropertyIsLessThanOrEqualTo.NAME;
            }
            else if ( PropertyIsNotEqualTo.class.isAssignableFrom(filterType)){
                return PropertyIsNotEqualTo.NAME;
            }
        }
        else if( PropertyIsBetween.class.isAssignableFrom(filterType)) {
            return PropertyIsBetween.NAME;
        }
        else if( PropertyIsLike.class.isAssignableFrom(filterType)) {
            return PropertyIsLike.NAME;
        }
        else if( PropertyIsNull.class.isAssignableFrom(filterType)) {
            return PropertyIsNull.NAME;
        }
        else if( Function.class.isAssignableFrom(filterType)) {
            throw new IllegalArgumentException("Cannot determine function name from class");
        }
        */
        return null;
    }
    
    public void addAll( Capabilities copy ){
        addAll( copy.getContents() );
    }
    public void addAll( FilterCapabilities copy ) {
        contents.addAll( copy );
    }
}
